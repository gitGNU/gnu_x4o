/*
 * Copyright (c) 2004-2012, Willem Cazander
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

package org.x4o.xml.lang;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.el.ExpressionFactory;

import org.x4o.xml.X4ODriver;
import org.x4o.xml.el.X4OELContext;
import org.x4o.xml.eld.EldDriver;
import org.x4o.xml.element.DefaultElement;
import org.x4o.xml.element.DefaultElementAttributeValueParser;
import org.x4o.xml.element.DefaultElementBodyCharacters;
import org.x4o.xml.element.DefaultElementBodyComment;
import org.x4o.xml.element.DefaultElementBodyWhitespace;
import org.x4o.xml.element.DefaultElementClass;
import org.x4o.xml.element.DefaultElementClassAttribute;
import org.x4o.xml.element.DefaultElementInterface;
import org.x4o.xml.element.DefaultElementNamespaceContext;
import org.x4o.xml.element.DefaultElementNamespaceInstanceProvider;
import org.x4o.xml.element.DefaultElementObjectPropertyValue;
import org.x4o.xml.element.DefaultGlobalAttributeHandlerComparator;
import org.x4o.xml.element.ElementAttributeValueParser;
import org.x4o.xml.element.ElementObjectPropertyValue;


/**
 * Provides all implementions of the different parts of the language parser.
 * 
 * @author Willem Cazander
 * @version 1.0 27 Oct 2009
 */
public class DefaultX4OLanguageConfiguration implements X4OLanguageConfiguration {
	
	public DefaultX4OLanguageConfiguration() {
	}

	/**
	 * @see org.x4o.xml.lang.X4OLanguageConfiguration#getLanguageResourcePathPrefix()
	 */
	public String getLanguageResourcePathPrefix() {
		return X4OLanguageConfiguration.DEFAULT_LANG_PATH_PREFIX;
	}

	/**
	 * @see org.x4o.xml.lang.X4OLanguageConfiguration#getLanguageResourceModulesFileName()
	 */
	public String getLanguageResourceModulesFileName() {
		return X4OLanguageConfiguration.DEFAULT_LANG_MODULES_FILE;
	}

	/**
	 * @see org.x4o.xml.lang.X4OLanguageConfiguration#getDefaultElementNamespaceContext()
	 */
	public Class<?> getDefaultElementNamespaceContext() {
		return DefaultElementNamespaceContext.class;
	}

	/**
	 * @see org.x4o.xml.lang.X4OLanguageConfiguration#getDefaultElementInterface()
	 */
	public Class<?> getDefaultElementInterface() {
		return DefaultElementInterface.class;
	}

	/**
	 * @see org.x4o.xml.lang.X4OLanguageConfiguration#getDefaultElement()
	 */
	public Class<?> getDefaultElement() {
		return DefaultElement.class;
	}

	/**
	 * @see org.x4o.xml.lang.X4OLanguageConfiguration#getDefaultElementClass()
	 */
	public Class<?> getDefaultElementClass() {
		return DefaultElementClass.class;
	}

	/**
	 * @see org.x4o.xml.lang.X4OLanguageConfiguration#getDefaultElementClassAttribute()
	 */
	public Class<?> getDefaultElementClassAttribute() {
		return DefaultElementClassAttribute.class;
	}

	/**
	 * @see org.x4o.xml.lang.X4OLanguageConfiguration#getDefaultElementLanguageModule()
	 */
	public Class<?> getDefaultElementLanguageModule() {
		return DefaultX4OLanguageModule.class;
	}

	/**
	 * @see org.x4o.xml.lang.X4OLanguageConfiguration#getDefaultElementBodyComment()
	 */
	public Class<?> getDefaultElementBodyComment() {
		return DefaultElementBodyComment.class;
	}

	/**
	 * @see org.x4o.xml.lang.X4OLanguageConfiguration#getDefaultElementBodyCharacters()
	 */
	public Class<?> getDefaultElementBodyCharacters() {
		return DefaultElementBodyCharacters.class;
	}

	/**
	 * @see org.x4o.xml.lang.X4OLanguageConfiguration#getDefaultElementBodyWhitespace()
	 */
	public Class<?> getDefaultElementBodyWhitespace() {
		return DefaultElementBodyWhitespace.class;
	}

	/**
	 * @see org.x4o.xml.lang.X4OLanguageConfiguration#getDefaultElementNamespaceInstanceProvider()
	 */
	public Class<?> getDefaultElementNamespaceInstanceProvider() {
		return DefaultElementNamespaceInstanceProvider.class;
	}

	/**
	 * @see org.x4o.xml.lang.X4OLanguageConfiguration#getDefaultElementAttributeValueParser()
	 */
	public Class<?> getDefaultElementAttributeValueParser() {
		return DefaultElementAttributeValueParser.class;
	}

	/**
	 * @see org.x4o.xml.lang.X4OLanguageConfiguration#getDefaultElementObjectPropertyValue()
	 */
	public Class<?> getDefaultElementObjectPropertyValue() {
		return DefaultElementObjectPropertyValue.class;
	}

	/**
	 * @see org.x4o.xml.lang.X4OLanguageConfiguration#getDefaultElementAttributeHandlerComparator()
	 */
	public Class<?> getDefaultElementAttributeHandlerComparator() {
		return DefaultGlobalAttributeHandlerComparator.class;
	}

	/**
	 * @see org.x4o.xml.lang.X4OLanguageConfiguration#getDefaultX4OLanguageVersionFilter()
	 */
	public Class<?> getDefaultX4OLanguageVersionFilter() {
		return DefaultX4OLanguageVersionFilter.class;
	}

	/**
	 * @see org.x4o.xml.lang.X4OLanguageConfiguration#getDefaultX4OLanguageLoader()
	 */
	public Class<?> getDefaultX4OLanguageLoader() {
		return DefaultX4OLanguageLoader.class;
	}

	/**
	 * @see org.x4o.xml.lang.X4OLanguageConfiguration#createElementLanguage()
	 */
	public X4OLanguageContext createElementLanguage(X4ODriver<?> driver) {
		String v = X4ODriver.DEFAULT_LANGUAGE_VERSION; // TODO:fixme
		return configElementLanguage(new DefaultX4OLanguageContext(driver.createLanguage(v),v),driver);
	}

	protected X4OLanguageContext configElementLanguage(X4OLanguageContext elementLanguage,X4ODriver<?> driver) {
		if ((elementLanguage instanceof X4OLanguageContextLocal)==false) { 
			throw new RuntimeException("Can't init ElementLanguage which has not ElementLanguageLocal interface obj: "+elementLanguage);
		}
		X4OLanguageContextLocal contextInit = (X4OLanguageContextLocal)elementLanguage; 
		//contextInit.setLanguageConfiguration(this);
		for (String key:driver.getGlobalPropertyKeys()) {
			Object value = driver.getGlobalProperty(key);
			contextInit.setLanguageProperty(key, value);
		}
		
		if (contextInit.getExpressionLanguageFactory()==null) {
			contextInit.setExpressionLanguageFactory(configExpressionFactory(contextInit));
		}
		if (contextInit.getExpressionLanguageContext()==null) {
			contextInit.setExpressionLanguageContext(new X4OELContext());
		}
		try {
			if (contextInit.getElementAttributeValueParser()==null) {
				contextInit.setElementAttributeValueParser((ElementAttributeValueParser)X4OLanguageClassLoader.newInstance(getDefaultElementAttributeValueParser()));
			}
			if (contextInit.getElementObjectPropertyValue()==null) {
				contextInit.setElementObjectPropertyValue((ElementObjectPropertyValue)X4OLanguageClassLoader.newInstance(getDefaultElementObjectPropertyValue()));
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(),e);
		}
		return elementLanguage;
	}

	protected ExpressionFactory configExpressionFactory(X4OLanguageContext elementContext) {
		ExpressionFactory factory = (ExpressionFactory)elementContext.getLanguageProperty(X4OLanguageProperty.EL_FACTORY_INSTANCE);
		if (factory!=null) {
			return factory;
		}
		try {
			Class<?> expressionFactoryClass = X4OLanguageClassLoader.loadClass("org.apache.el.ExpressionFactoryImpl");
			ExpressionFactory expressionFactory = (ExpressionFactory) expressionFactoryClass.newInstance();
			return expressionFactory;
		} catch (Exception e) {
			try {
				Class<?> expressionFactoryClass = X4OLanguageClassLoader.loadClass("de.odysseus.el.ExpressionFactoryImpl");
				ExpressionFactory expressionFactory = (ExpressionFactory) expressionFactoryClass.newInstance();
				return expressionFactory;
			} catch (Exception ee) {
				throw new RuntimeException("Could not load ExpressionFactory tried: org.apache.el.ExpressionFactoryImpl and de.odysseus.el.ExpressionFactoryImpl but could not load one of them.");
			}
		}
	}

	/**
	 * @see org.x4o.xml.lang.X4OLanguageConfiguration#getSAXParserProperties()
	 */
	public Map<String, Object> getSAXParserProperties(X4OLanguageContext elementContext) {
		Map<String,Object> saxParserProperties = new HashMap<String,Object>(1);
		return saxParserProperties;
	}

	/**
	 * @see org.x4o.xml.lang.X4OLanguageConfiguration#getSAXParserPropertiesOptional()
	 */
	public Map<String, Object> getSAXParserPropertiesOptional(X4OLanguageContext elementContext) {
		Map<String,Object> saxParserProperties = new HashMap<String,Object>(1);
		saxParserProperties.put("http://apache.org/xml/properties/input-buffer-size",elementContext.getLanguagePropertyInteger(X4OLanguageProperty.INPUT_BUFFER_SIZE));	// Increase buffer to 8KB
		return saxParserProperties;
	}

	/**
	 * @see org.x4o.xml.lang.X4OLanguageConfiguration#getSAXParserFeatures()
	 */
	public Map<String, Boolean> getSAXParserFeatures(X4OLanguageContext elementContext) {
		
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
			validation = elementContext.getLanguagePropertyBoolean(X4OLanguageProperty.VALIDATION_ELD);
			validationXsd = elementContext.getLanguagePropertyBoolean(X4OLanguageProperty.VALIDATION_ELD_XSD);
		} else {
			validation = elementContext.getLanguagePropertyBoolean(X4OLanguageProperty.VALIDATION_INPUT);
			validationXsd = elementContext.getLanguagePropertyBoolean(X4OLanguageProperty.VALIDATION_INPUT_XSD);
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
	 * @see org.x4o.xml.lang.X4OLanguageConfiguration#getSAXParserFeaturesOptional()
	 */
	public Map<String, Boolean> getSAXParserFeaturesOptional(X4OLanguageContext elementContext) {
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
	 * @see org.x4o.xml.lang.X4OLanguageConfiguration#getSAXParserFeaturesRequired()
	 */
	public List<String> getSAXParserFeaturesRequired(X4OLanguageContext elementContext) {
		List<String> result = new ArrayList<String>(5);
		result.add("http://xml.org/sax/features/use-attributes2");	// Attributes objects passed by the parser are ext.Attributes2 interface.
		result.add("http://xml.org/sax/features/use-locator2");		// Locator objects passed by the parser are org.xml.sax.ext.Locator2 interface.
		result.add("http://xml.org/sax/features/xml-1.1");			// The parser supports both XML 1.0 and XML 1.1.
		return result;
	}

}
