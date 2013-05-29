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

import org.x4o.xml.element.ElementException;
import org.x4o.xml.lang.X4OLanguageContext;
import org.x4o.xml.lang.X4OLanguageProperty;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * X4OContentParser Runs the SAX parser with the X4OContentHandler.
 * 
 * @author Willem Cazander
 * @version 1.0 Aug 20, 2005
 */
public class X4OContentParser {
	
	public void parse(X4OLanguageContext languageContext) throws SAXException, IOException {
		
		// If xsd caching is needed this should be the way 
		//XMLParserConfiguration config = new XIncludeAwareParserConfiguration();
		//config.setProperty("http://apache.org/xml/properties/internal/grammar-pool",myFullGrammarPool);
		//SAXParser parser = new SAXParser(config);
		
		// Create Sax parser with x4o tag handler
		X4OContentHandler xth = new X4OContentHandler(languageContext);
		XMLReader saxParser = XMLReaderFactory.createXMLReader();
		saxParser.setErrorHandler(new X4OErrorHandler(languageContext));
		saxParser.setEntityResolver(new X4OEntityResolver(languageContext));
		saxParser.setContentHandler(xth);
		saxParser.setProperty("http://xml.org/sax/properties/lexical-handler", xth);
		saxParser.setProperty("http://xml.org/sax/properties/declaration-handler",xth);

		// Set properties and optional 
		Map<String,Object> saxParserProperties = languageContext.getLanguage().getLanguageConfiguration().getSAXParserProperties(languageContext);
		for (Map.Entry<String,Object> entry:saxParserProperties.entrySet()) {
			String name = entry.getKey();
			Object value= entry.getValue();
			saxParser.setProperty(name, value);
			debugMessage("Set SAX property: "+name+" to: "+value,languageContext);
		}
		Map<String,Object> saxParserPropertiesOptional = languageContext.getLanguage().getLanguageConfiguration().getSAXParserPropertiesOptional(languageContext);
		for (Map.Entry<String,Object> entry:saxParserPropertiesOptional.entrySet()) {
			String name = entry.getKey();
			Object value= entry.getValue();
			try {
				saxParser.setProperty(name, value);
				debugMessage("Set SAX optional property: "+name+" to: "+value,languageContext);
			} catch (SAXException e) {
				debugMessage("Could not set optional SAX property: "+name+" to: "+value+" error: "+e.getMessage(),languageContext);
			}
		}
		
		// Set sax features and optional
		Map<String, Boolean> features = languageContext.getLanguage().getLanguageConfiguration().getSAXParserFeatures(languageContext);
		for (String key:features.keySet()) {
			Boolean value=features.get(key);
			saxParser.setFeature(key, value);
			debugMessage("Set SAX feature: "+key+" to: "+value,languageContext);
		}
		Map<String, Boolean> featuresOptional = languageContext.getLanguage().getLanguageConfiguration().getSAXParserFeaturesOptional(languageContext);
		for (String key:featuresOptional.keySet()) {
			Boolean value=featuresOptional.get(key);
			try {
				saxParser.setFeature(key, value);
				debugMessage("Set SAX optional feature: "+key+" to: "+value,languageContext);
			} catch (SAXException e) {
				debugMessage("Could not set optional SAX feature: "+key+" to: "+value+" error: "+e.getMessage(),languageContext);
			}
		}
		
		// check for required features
		List<String> requiredFeatures = languageContext.getLanguage().getLanguageConfiguration().getSAXParserFeaturesRequired(languageContext);
		for (String requiredFeature:requiredFeatures) {
			debugMessage("Checking required SAX feature: "+requiredFeature,languageContext);
			if (saxParser.getFeature(requiredFeature)==false) {
				throw new IllegalStateException("Missing required feature: "+requiredFeature);
			}	
		}
		
		// Finally start parsing the xml input stream
		Object requestInputSource = languageContext.getLanguageProperty(X4OLanguageProperty.READER_INPUT_SOURCE);
		InputSource input = null;
		InputStream inputStream = null;
		if (requestInputSource instanceof InputSource) {
			input = (InputSource)requestInputSource;
		} else {
			inputStream = (InputStream)languageContext.getLanguageProperty(X4OLanguageProperty.READER_INPUT_STREAM);
			input = new InputSource(inputStream);
		}
		
		Object requestInputEncoding = languageContext.getLanguageProperty(X4OLanguageProperty.READER_INPUT_ENCODING);
		if (requestInputEncoding!=null && requestInputEncoding instanceof String) {
			input.setEncoding(requestInputEncoding.toString());
		}
		Object requestSystemId = languageContext.getLanguageProperty(X4OLanguageProperty.READER_INPUT_SYSTEM_ID);
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
	
	private void debugMessage(String message,X4OLanguageContext languageContext) throws SAXException {
		if (languageContext.hasX4ODebugWriter()) {
			try {
				languageContext.getX4ODebugWriter().debugPhaseMessage(message, X4OContentParser.class);
			} catch (ElementException ee) {
				throw new SAXException(ee);
			}
		}
	}
}
