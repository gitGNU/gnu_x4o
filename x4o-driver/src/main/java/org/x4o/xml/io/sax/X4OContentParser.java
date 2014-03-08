/*
 * Copyright (c) 2004-2013, Willem Cazander
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided
 * that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this list of conditions and the
 *   following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and
 *   the following disclaimer in the documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
 * THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT
 * OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
 * TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.x4o.xml.io.sax;

import org.x4o.xml.eld.EldDriver;
import org.x4o.xml.element.ElementException;
import org.x4o.xml.io.DefaultX4OReader;
import org.x4o.xml.io.sax.ext.PropertyConfig;
import org.x4o.xml.lang.X4OLanguageSession;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * X4OContentParser Runs the SAX parser with the X4OContentHandler.
 * 
 * @author Willem Cazander
 * @version 1.0 Aug 20, 2005
 */
public class X4OContentParser {
	
	private final PropertyConfig propertyConfig;
	
	public X4OContentParser(PropertyConfig propertyConfig) {
		this.propertyConfig=propertyConfig;
	}
	
	private PropertyConfig getPropertyConfig() {
		return propertyConfig;
	}
	
	public void parse(X4OLanguageSession languageSession) throws SAXException, IOException {
		// Group debug config property messages
		if (languageSession.hasX4ODebugWriter()) {
			languageSession.getX4ODebugWriter().debugSAXConfigStart();
		}
		try {
			parseSax(languageSession);
		} finally {
			if (languageSession.hasX4ODebugWriter()) {
				languageSession.getX4ODebugWriter().debugSAXConfigEnd();
			}
		}
	}
	
	private void parseSax(X4OLanguageSession languageSession) throws SAXException, IOException {
		// If xsd caching is needed this should be the way 
		//XMLParserConfiguration config = new XIncludeAwareParserConfiguration();
		//config.setProperty("http://apache.org/xml/properties/internal/grammar-pool",myFullGrammarPool);
		//SAXParser parser = new SAXParser(config);
		
		// Create Sax parser with x4o tag handler
		X4OContentHandler xth = new X4OContentHandler(languageSession,getPropertyConfig());
		XMLReader saxParser = XMLReaderFactory.createXMLReader();
		saxParser.setErrorHandler(new X4OErrorHandler(languageSession,getPropertyConfig()));
		saxParser.setEntityResolver(new X4OEntityResolver(languageSession,getPropertyConfig()));
		saxParser.setContentHandler(xth);
		saxParser.setProperty("http://xml.org/sax/properties/lexical-handler", xth);
		saxParser.setProperty("http://xml.org/sax/properties/declaration-handler",xth);
		
		// Set properties and optional 
		Map<String,Object> saxParserProperties = getSAXParserProperties(languageSession);
		for (Map.Entry<String,Object> entry:saxParserProperties.entrySet()) {
			String name = entry.getKey();
			Object value= entry.getValue();
			saxParser.setProperty(name, value);
			debugMessage("property",name,value,languageSession);
		}
		Map<String,Object> saxParserPropertiesOptional = getSAXParserPropertiesOptional(languageSession);
		for (Map.Entry<String,Object> entry:saxParserPropertiesOptional.entrySet()) {
			String name = entry.getKey();
			Object value= entry.getValue();
			try {
				saxParser.setProperty(name, value);
				debugMessage("optional-property",name,value,languageSession);
			} catch (SAXException e) {
				debugMessageLog("Could not set optional SAX property: "+name+" to: "+value+" error: "+e.getMessage(),languageSession);
			}
		}
		
		// Set sax features and optional
		Map<String, Boolean> features = getSAXParserFeatures(languageSession);
		for (String key:features.keySet()) {
			Boolean value=features.get(key);
			saxParser.setFeature(key, value);
			debugMessage("feature",key,value,languageSession);
		}
		Map<String, Boolean> featuresOptional = getSAXParserFeaturesOptional(languageSession);
		for (String key:featuresOptional.keySet()) {
			Boolean value=featuresOptional.get(key);
			try {
				saxParser.setFeature(key, value);
				debugMessage("optional-feature",key,value,languageSession);
			} catch (SAXException e) {
				debugMessageLog("Could not set optional SAX feature: "+key+" to: "+value+" error: "+e.getMessage(),languageSession);
			}
		}
		
		// check for required features
		List<String> requiredFeatures = getSAXParserFeaturesRequired(languageSession);
		for (String requiredFeature:requiredFeatures) {
			if (saxParser.getFeature(requiredFeature)==false) {
				throw new IllegalStateException("Missing required feature: "+requiredFeature);
			}
			debugMessage("required",requiredFeature,"true",languageSession);
		}
		
		// Finally start parsing the xml input stream
		Object requestInputSource = getPropertyConfig().getProperty(DefaultX4OReader.INPUT_SOURCE);
		InputSource input = null;
		InputStream inputStream = null;
		if (requestInputSource instanceof InputSource) {
			input = (InputSource)requestInputSource;
		} else {
			inputStream = (InputStream)getPropertyConfig().getProperty(DefaultX4OReader.INPUT_STREAM);
			input = new InputSource(inputStream);
		}
		
		Object requestInputEncoding = getPropertyConfig().getProperty(DefaultX4OReader.INPUT_ENCODING);
		if (requestInputEncoding!=null && requestInputEncoding instanceof String) {
			input.setEncoding(requestInputEncoding.toString());
		}
		Object requestSystemId = getPropertyConfig().getProperty(DefaultX4OReader.INPUT_SYSTEM_ID);
		if (requestSystemId!=null && requestSystemId instanceof String) {
			input.setSystemId(requestSystemId.toString());
		}
		
		try {
			saxParser.parse(input);
		} finally {
			if (inputStream!=null) {
				inputStream.close();
			}
		}
	}
	
	private void debugMessageLog(String message,X4OLanguageSession languageSession) throws SAXException {
		if (languageSession.hasX4ODebugWriter()) {
			try {
				languageSession.getX4ODebugWriter().debugPhaseMessage(message, getClass());
			} catch (ElementException e) {
				throw new SAXException(e);
			}
		}
	}
	
	private void debugMessage(String type,String key,Object value,X4OLanguageSession languageSession) throws SAXException {
		if (languageSession.hasX4ODebugWriter()) {
			languageSession.getX4ODebugWriter().debugSAXMessage(type,key,""+value);
		}
	}
	
	/**
	 * @see org.x4o.xml.lang.X4OLanguageConfiguration#getSAXParserProperties(org.x4o.xml.lang.X4OLanguageSession)
	 */
	public Map<String, Object> getSAXParserProperties(X4OLanguageSession elementContext) {
		Map<String,Object> saxParserProperties = new HashMap<String,Object>(1);
		return saxParserProperties;
	}

	/**
	 * @see org.x4o.xml.lang.X4OLanguageConfiguration#getSAXParserPropertiesOptional(org.x4o.xml.lang.X4OLanguageSession)
	 */
	public Map<String, Object> getSAXParserPropertiesOptional(X4OLanguageSession elementContext) {
		Map<String,Object> saxParserProperties = new HashMap<String,Object>(1);
		saxParserProperties.put("http://apache.org/xml/properties/input-buffer-size",getPropertyConfig().getProperty(DefaultX4OReader.DOC_BUFFER_SIZE));	// Increase buffer to 8KB
		return saxParserProperties;
	}

	/**
	 * @see org.x4o.xml.lang.X4OLanguageConfiguration#getSAXParserFeatures(org.x4o.xml.lang.X4OLanguageSession)
	 */
	public Map<String, Boolean> getSAXParserFeatures(X4OLanguageSession elementContext) {
		
		// see example: http://xerces.apache.org/xerces2-j/features.html
		Map<String,Boolean> saxParserFeatures = new HashMap<String,Boolean>(20);
		
		// Tune Sax Parser
		saxParserFeatures.put("http://xml.org/sax/features/namespaces", 								true);	// Perform namespace processing
		saxParserFeatures.put("http://xml.org/sax/features/use-entity-resolver2", 						true);	// Use EntityResolver2 interface
		saxParserFeatures.put("http://xml.org/sax/features/lexical-handler/parameter-entities", 		true);	// Report parameter entities to a the LexicalHandler.
		
		saxParserFeatures.put("http://xml.org/sax/features/xmlns-uris", 								false); // Namespace declaration attributes are reported as having no namespace.
		saxParserFeatures.put("http://xml.org/sax/features/namespace-prefixes", 						false);	// Do not report attributes used for Namespace declarations
		
		saxParserFeatures.put("http://xml.org/sax/features/external-general-entities",					false);	// Never include the external general entries.
		saxParserFeatures.put("http://xml.org/sax/features/external-parameter-entities",				false);	// Never include the external parameter or DTD subset.
		
		saxParserFeatures.put("http://apache.org/xml/features/xinclude", 								true);	// Perform XInclude processing
		saxParserFeatures.put("http://apache.org/xml/features/xinclude/fixup-base-uris",				false);
		saxParserFeatures.put("http://apache.org/xml/features/xinclude/fixup-language",					false);
		
		boolean validation = false;
		boolean validationXsd = false;
		if (EldDriver.LANGUAGE_NAME.equals(elementContext.getLanguage().getLanguageName())) {
			validation = false; //TODO: elementContext.getLanguagePropertyBoolean(X4OLanguageProperty.VALIDATION_ELD);
			validationXsd = false; //elementContext.getLanguagePropertyBoolean(X4OLanguageProperty.VALIDATION_ELD_XSD);
		} else {
			validation = getPropertyConfig().getPropertyBoolean(DefaultX4OReader.VALIDATION_INPUT_DOC);
			validationXsd = getPropertyConfig().getPropertyBoolean(DefaultX4OReader.VALIDATION_INPUT_SCHEMA);
		}
		if (validation) {
			saxParserFeatures.put("http://xml.org/sax/features/validation", 							true);	// Validate the document and report validity errors.
			saxParserFeatures.put("http://apache.org/xml/features/validation/schema",					true);	// Insert an XML Schema validator into the pipeline.
		} else {
			saxParserFeatures.put("http://xml.org/sax/features/validation", 							false);
			saxParserFeatures.put("http://apache.org/xml/features/validation/schema",					false);
		}
		if (validation && validationXsd) {
			saxParserFeatures.put("http://apache.org/xml/features/validation/schema-full-checking",		true);	// Enable validation of the schema grammar itself for errors.
		} else {
			saxParserFeatures.put("http://apache.org/xml/features/validation/schema-full-checking",		false);	// Disable validation of the schema grammar itself for errors.
		}
		
		
		return saxParserFeatures;
	}

	/**
	 * @see org.x4o.xml.lang.X4OLanguageConfiguration#getSAXParserFeaturesOptional(org.x4o.xml.lang.X4OLanguageSession)
	 */
	public Map<String, Boolean> getSAXParserFeaturesOptional(X4OLanguageSession elementContext) {
		Map<String,Boolean> saxParserFeatures = new HashMap<String,Boolean>(20);
		
		// Make Sax Impl more strict.
		saxParserFeatures.put("http://apache.org/xml/features/disallow-doctype-decl", 					true);	// Throws error if document contains a DOCTYPE declaration.
		saxParserFeatures.put("http://apache.org/xml/features/validation/schema/normalized-value",		true);	// Expose normalized values for attributes and elements.
		saxParserFeatures.put("http://apache.org/xml/features/validation/warn-on-duplicate-attdef",		true);	// Report a warning when a duplicate attribute is re-declared.
		saxParserFeatures.put("http://apache.org/xml/features/warn-on-duplicate-entitydef", 			true);	// Report a warning for duplicate entity declaration.  
		saxParserFeatures.put("http://apache.org/xml/features/validation/dynamic", 						false);	// Validation is determined by the state of the validation feature.
		saxParserFeatures.put("http://apache.org/xml/features/nonvalidating/load-dtd-grammar",			false);	// Do not use the default DTD grammer
		saxParserFeatures.put("http://apache.org/xml/features/nonvalidating/load-external-dtd",			false);	// Ignore the external DTD completely.
		
		// need newer version
		//saxParserFeatures.put("http://apache.org/xml/features/standard-uri-conformant", 				true);	// Requires that a URI has to be provided where a URI is expected.
		//saxParserFeatures.put("http://apache.org/xml/features/validation/warn-on-undeclared-elemdef",	true);  // Report a warning if an element referenced in a content model is not declared.
		//saxParserFeatures.put("http://apache.org/xml/features/validation/balance-syntax-trees", 		false);	// No optimize DTD content models.
		//saxParserFeatures.put("http://apache.org/xml/features/validation/unparsed-entity-checking", 	false);	// Do not check that each value of type ENTITY in DTD.
		
		return saxParserFeatures;
	}
	
	/**
	 * @see org.x4o.xml.lang.X4OLanguageConfiguration#getSAXParserFeaturesRequired(org.x4o.xml.lang.X4OLanguageSession)
	 */
	public List<String> getSAXParserFeaturesRequired(X4OLanguageSession elementContext) {
		List<String> result = new ArrayList<String>(5);
		result.add("http://xml.org/sax/features/use-attributes2");	// Attributes objects passed by the parser are ext.Attributes2 interface.
		result.add("http://xml.org/sax/features/use-locator2");		// Locator objects passed by the parser are org.xml.sax.ext.Locator2 interface.
		result.add("http://xml.org/sax/features/xml-1.1");			// The parser supports both XML 1.0 and XML 1.1.
		return result;
	}
}
