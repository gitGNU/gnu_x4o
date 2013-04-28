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
package org.x4o.xml.lang;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

/**
 * Provides all implementions of the different parts of the language parser.
 * 
 * @author Willem Cazander
 * @version 1.0 27 Oct 2009
 */
public class DefaultX4OLanguageConfiguration extends AbstractX4OLanguageConfiguration {
	
	/**
	 * Default constructor.
	 */
	public DefaultX4OLanguageConfiguration() {
	}
	
	public void fillDefaults() {
		if (getLanguageResourcePathPrefix()==null) {				setLanguageResourcePathPrefix(					X4OLanguageConfiguration.DEFAULT_LANG_PATH_PREFIX);	}
		if (getLanguageResourceModulesFileName()==null) {			setLanguageResourceModulesFileName(				X4OLanguageConfiguration.DEFAULT_LANG_MODULES_FILE);}
		if (getDefaultElementNamespaceContext()==null) {			setDefaultElementNamespaceContext(				DefaultElementNamespaceContext.class);				}
		if (getDefaultElementInterface()==null) {					setDefaultElementInterface(						DefaultElementInterface.class);						}
		if (getDefaultElement()==null) {							setDefaultElement(								DefaultElement.class);								}
		if (getDefaultElementClass()==null) {						setDefaultElementClass(							DefaultElementClass.class);							}
		if (getDefaultElementClassAttribute()==null) {				setDefaultElementClassAttribute(				DefaultElementClassAttribute.class);				}
		if (getDefaultElementLanguageModule()==null) {				setDefaultElementLanguageModule(				DefaultX4OLanguageModule.class);					}
		if (getDefaultElementBodyComment()==null) {					setDefaultElementBodyComment(					DefaultElementBodyComment.class);					}
		if (getDefaultElementBodyCharacters()==null) {				setDefaultElementBodyCharacters(				DefaultElementBodyCharacters.class);				}
		if (getDefaultElementBodyWhitespace()==null) {				setDefaultElementBodyWhitespace(				DefaultElementBodyWhitespace.class);				}
		if (getDefaultElementNamespaceInstanceProvider()==null) {	setDefaultElementNamespaceInstanceProvider(		DefaultElementNamespaceInstanceProvider.class);		}
		if (getDefaultElementAttributeValueParser()==null) {		setDefaultElementAttributeValueParser(			DefaultElementAttributeValueParser.class);			}
		if (getDefaultElementObjectPropertyValue()==null) {			setDefaultElementObjectPropertyValue(			DefaultElementObjectPropertyValue.class);			}
		if (getDefaultElementAttributeHandlerComparator()==null) {	setDefaultElementAttributeHandlerComparator(	DefaultGlobalAttributeHandlerComparator.class);		}
		if (getDefaultLanguageVersionFilter()==null) {				setDefaultLanguageVersionFilter(				DefaultX4OLanguageVersionFilter.class);				}
		if (getDefaultLanguageLoader()==null) {						setDefaultLanguageLoader(						DefaultX4OLanguageLoader.class);					}
		if (getDefaultExpressionLanguageContext()==null) {			setDefaultExpressionLanguageContext(			X4OELContext.class);								}
	}

	/**
	 * @see org.x4o.xml.lang.X4OLanguageConfigurationLocal#createProxy()
	 */
	public X4OLanguageConfiguration createProxy() {
		Object proxy = Proxy.newProxyInstance(X4OLanguageClassLoader.getClassLoader(), new Class[]{X4OLanguageConfiguration.class}, new InvocationHandler() {
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				X4OLanguageConfigurationLocal local = DefaultX4OLanguageConfiguration.this;
				int argsLength = 0;
				if (args!=null) {
					argsLength = args.length;
				}
				Class<?>[] invokeArgs = new Class[argsLength];
				for (int i=0;i<argsLength;i++) {
					//Object o = args[i];
					invokeArgs[i] = X4OLanguageContext.class; //o.getClass(); todo fix
				}
				Method localMethod = local.getClass().getMethod(method.getName(), invokeArgs);
				Object result = localMethod.invoke(local, args);
				return result; // result is reflection safe interface hiding.
			}
		});
		return (X4OLanguageConfiguration)proxy;
	}

	/**
	 * @see org.x4o.xml.lang.X4OLanguageConfiguration#getSAXParserProperties(org.x4o.xml.lang.X4OLanguageContext)
	 */
	public Map<String, Object> getSAXParserProperties(X4OLanguageContext elementContext) {
		Map<String,Object> saxParserProperties = new HashMap<String,Object>(1);
		return saxParserProperties;
	}

	/**
	 * @see org.x4o.xml.lang.X4OLanguageConfiguration#getSAXParserPropertiesOptional(org.x4o.xml.lang.X4OLanguageContext)
	 */
	public Map<String, Object> getSAXParserPropertiesOptional(X4OLanguageContext elementContext) {
		Map<String,Object> saxParserProperties = new HashMap<String,Object>(1);
		saxParserProperties.put("http://apache.org/xml/properties/input-buffer-size",elementContext.getLanguagePropertyInteger(X4OLanguageProperty.READER_BUFFER_SIZE));	// Increase buffer to 8KB
		return saxParserProperties;
	}

	/**
	 * @see org.x4o.xml.lang.X4OLanguageConfiguration#getSAXParserFeatures(org.x4o.xml.lang.X4OLanguageContext)
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
			validation = false; //TODO: elementContext.getLanguagePropertyBoolean(X4OLanguageProperty.VALIDATION_ELD);
			validationXsd = false; //elementContext.getLanguagePropertyBoolean(X4OLanguageProperty.VALIDATION_ELD_XSD);
		} else {
			validation = elementContext.getLanguagePropertyBoolean(X4OLanguageProperty.READER_VALIDATION_INPUT);
			validationXsd = elementContext.getLanguagePropertyBoolean(X4OLanguageProperty.READER_VALIDATION_INPUT_XSD);
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
	 * @see org.x4o.xml.lang.X4OLanguageConfiguration#getSAXParserFeaturesOptional(org.x4o.xml.lang.X4OLanguageContext)
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
	 * @see org.x4o.xml.lang.X4OLanguageConfiguration#getSAXParserFeaturesRequired(org.x4o.xml.lang.X4OLanguageContext)
	 */
	public List<String> getSAXParserFeaturesRequired(X4OLanguageContext elementContext) {
		List<String> result = new ArrayList<String>(5);
		result.add("http://xml.org/sax/features/use-attributes2");	// Attributes objects passed by the parser are ext.Attributes2 interface.
		result.add("http://xml.org/sax/features/use-locator2");		// Locator objects passed by the parser are org.xml.sax.ext.Locator2 interface.
		result.add("http://xml.org/sax/features/xml-1.1");			// The parser supports both XML 1.0 and XML 1.1.
		return result;
	}

}
