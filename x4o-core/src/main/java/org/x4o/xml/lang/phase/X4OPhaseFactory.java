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

package	org.x4o.xml.lang.phase;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.el.ValueExpression;

import org.x4o.xml.conv.ObjectConverterException;

import org.x4o.xml.element.Element;
import org.x4o.xml.element.ElementAttributeHandler;
import org.x4o.xml.element.ElementAttributeValueParser;
import org.x4o.xml.element.ElementBindingHandler;
import org.x4o.xml.element.ElementClass;
import org.x4o.xml.element.ElementClassAttribute;
import org.x4o.xml.element.ElementConfigurator;
import org.x4o.xml.element.ElementConfiguratorGlobal;
import org.x4o.xml.element.ElementException;
import org.x4o.xml.element.ElementInterface;
import org.x4o.xml.element.ElementNamespaceContext;
import org.x4o.xml.io.sax.X4ODebugWriter;
import org.x4o.xml.io.sax.X4OEntityResolver;
import org.x4o.xml.io.sax.X4OErrorHandler;
import org.x4o.xml.io.sax.X4OTagHandler;
import org.x4o.xml.lang.X4OLanguageModule;
import org.x4o.xml.lang.X4OLanguageContext;
import org.x4o.xml.lang.X4OLanguageModuleLoaderSibling;
import org.x4o.xml.lang.X4OLanguageClassLoader;
import org.x4o.xml.lang.X4OLanguageLoader;
import org.x4o.xml.lang.X4OLanguageLocal;
import org.x4o.xml.lang.X4OLanguageProperty;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.DefaultHandler2;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * Factory which can create X4OPhaseHandlers for all the predefined phases used in default x4o language parsing.
 * 
 * @author Willem Cazander
 * @version 1.0 Dec 31, 2008
 */
public class X4OPhaseFactory {

	private Logger logger = null;
	
	public X4OPhaseFactory() {
		logger = Logger.getLogger(X4OPhaseFactory.class.getName());
	}
	
	private void debugPhaseMessage(String message,X4OPhase phaseHandler,X4OLanguageContext languageContext) throws X4OPhaseException {
		if (languageContext.hasX4ODebugWriter()) {
			try {
				languageContext.getX4ODebugWriter().debugPhaseMessage(message,phaseHandler.getClass());
			} catch (ElementException ee) {
				throw new X4OPhaseException(phaseHandler,ee);
			}
		}
	}
	
	/**
	 * Creates the startupX4OPhase which is a empty meta phase.
	 * @return	The X4OPhaseHandler for this phase.
	 */
	public X4OPhase initX4OPhase() {
		class CreateInitX4OPhase extends AbstractX4OPhase {
			public X4OPhaseType getType() {
				return X4OPhaseType.INIT;
			}
			public String getId() {
				return "INIT";
			}
			public String[] getPhaseDependencies() {
				return new String[]{};
			}
			public boolean isElementPhase() {
				return false;
			}
			public void runElementPhase(Element element) throws X4OPhaseException {
				// not used.
			}
			public void runPhase(X4OLanguageContext elementLanguage) throws X4OPhaseException  {

			}
		};
		X4OPhase result = new CreateInitX4OPhase();
		return result;
	}
	
	/**
	 * Loads all the modules a language.
	 * Then creates the ElementProviders
	 * @return	The X4OPhaseHandler for this phase.
	 */
	public X4OPhase createLanguagePhase() {
		class CreateLanguagePhase extends AbstractX4OPhase {
			public X4OPhaseType getType() {
				return X4OPhaseType.INIT;
			}
			public String getId() {
				return "INIT_LANG";
			}
			public String[] getPhaseDependencies() {
				return new String[]{"INIT"};
			}
			public boolean isElementPhase() {
				return false;
			}
			public void runElementPhase(Element element) throws X4OPhaseException {
			}
			public void runPhase(X4OLanguageContext elementLanguage) throws X4OPhaseException  {
				try {
					debugPhaseMessage("Loading main language: "+elementLanguage.getLanguage(),this,elementLanguage);
					X4OLanguageLoader loader = (X4OLanguageLoader)X4OLanguageClassLoader.newInstance(elementLanguage.getLanguage().getLanguageConfiguration().getDefaultX4OLanguageLoader());
					loader.loadLanguage((X4OLanguageLocal)elementLanguage.getLanguage(),elementLanguage.getLanguage().getLanguageName(),elementLanguage.getLanguage().getLanguageVersion());
					
					if (elementLanguage.hasX4ODebugWriter()) {
						elementLanguage.getX4ODebugWriter().debugElementLanguageModules(elementLanguage);
					}
				} catch (Exception e) {
					throw new X4OPhaseException(this,e);
				}
			}
		};
		X4OPhase result = new CreateLanguagePhase();
		return result;
	}
	
	/**
	 * Loads all sibling languages.
	 * @return	The X4OPhaseHandler for this phase.
	 */
	public X4OPhase createLanguageSiblingsPhase() {
		class CreateLanguageSiblingsPhase extends AbstractX4OPhase {
			public X4OPhaseType getType() {
				return X4OPhaseType.INIT;
			}
			public String getId() {
				return "INIT_LANG_SIB";
			}
			public String[] getPhaseDependencies() {
				return new String[] {"INIT_LANG"};
			}
			public boolean isElementPhase() {
				return false;
			}
			public void runElementPhase(Element element) throws X4OPhaseException {
			}
			public void runPhase(X4OLanguageContext elementLanguage) throws X4OPhaseException {
				try {
					List<X4OLanguageModuleLoaderSibling> siblingLoaders = new ArrayList<X4OLanguageModuleLoaderSibling>(3);
					for (X4OLanguageModule module:elementLanguage.getLanguage().getLanguageModules()) {	
						if (module.getLanguageModuleLoader() instanceof X4OLanguageModuleLoaderSibling) {
							siblingLoaders.add((X4OLanguageModuleLoaderSibling)module.getLanguageModuleLoader());
						}
					}
					if (siblingLoaders.isEmpty()==false) {
						X4OLanguageLoader loader = (X4OLanguageLoader)X4OLanguageClassLoader.newInstance(elementLanguage.getLanguage().getLanguageConfiguration().getDefaultX4OLanguageLoader());
						for (X4OLanguageModuleLoaderSibling siblingLoader:siblingLoaders) {
							debugPhaseMessage("Loading sibling langauge loader: "+siblingLoader,this,elementLanguage);
							siblingLoader.loadLanguageSibling((X4OLanguageLocal)elementLanguage.getLanguage(), loader);
						}
						if (elementLanguage.hasX4ODebugWriter()) {
							elementLanguage.getX4ODebugWriter().debugElementLanguageModules(elementLanguage);
						}
					}
				} catch (Exception e) {
					throw new X4OPhaseException(this,e);
				}
			}
		};
		X4OPhase result = new CreateLanguageSiblingsPhase();
		return result;
	}
	
	/**
	 * Creates the startX4OPhase which is a empty meta phase.
	 * @return	The X4OPhaseHandler for this phase.
	 */
	public X4OPhase readStartX4OPhase() {
		class ReadStartX4OPhase extends AbstractX4OPhase {
			public X4OPhaseType getType() {
				return X4OPhaseType.XML_READ;
			}
			public String getId() {
				return "READ_START";
			}
			public String[] getPhaseDependencies() {
				return new String[]{};
			}
			public boolean isElementPhase() {
				return false;
			}
			public void runElementPhase(Element element) throws X4OPhaseException {
				// not used.
			}
			public void runPhase(X4OLanguageContext elementLanguage) throws X4OPhaseException {
				// print the properties and classes for this language/config
				if (elementLanguage.hasX4ODebugWriter()) {
					try {
						elementLanguage.getX4ODebugWriter().debugLanguageProperties(elementLanguage);
						elementLanguage.getX4ODebugWriter().debugLanguageDefaultClasses(elementLanguage);
					} catch (ElementException e) {
						throw new X4OPhaseException(this,e);
					}
				}
			}
		};
		X4OPhase result = new ReadStartX4OPhase();
		return result;
	}
	
	
	/**
	 * Parses the xml resource(s) and creates an Element tree.
	 * @return	The X4OPhaseHandler for this phase.
	 */
	public X4OPhase readSAXStreamPhase() {
		class CreateSAXStreamPhase extends AbstractX4OPhase {
			public X4OPhaseType getType() {
				return X4OPhaseType.XML_READ;
			}
			public String getId() {
				return "READ_XML";
			}
			public String[] getPhaseDependencies() {
				return new String[]{"READ_START"};
			}
			public boolean isElementPhase() {
				return false;
			}
			public void runElementPhase(Element element) throws X4OPhaseException {
			}
			public void runPhase(X4OLanguageContext elementLanguage) throws X4OPhaseException {
				try {
					//XMLParserConfiguration config = new XIncludeAwareParserConfiguration();
					//config.setProperty("http://apache.org/xml/properties/internal/grammar-pool",myFullGrammarPool);
					//SAXParser parser = new SAXParser(config);
					
					// Create Sax parser with x4o tag handler
					X4OTagHandler xth = new X4OTagHandler(elementLanguage);
					XMLReader saxParser = XMLReaderFactory.createXMLReader();
					saxParser.setErrorHandler(new X4OErrorHandler(elementLanguage));
					saxParser.setEntityResolver(new X4OEntityResolver(elementLanguage));
					saxParser.setContentHandler(xth);
					saxParser.setProperty("http://xml.org/sax/properties/lexical-handler", xth);
					saxParser.setProperty("http://xml.org/sax/properties/declaration-handler",xth);

					// Set properties and optional 
					Map<String,Object> saxParserProperties = elementLanguage.getLanguage().getLanguageConfiguration().getSAXParserProperties(elementLanguage);
					for (Map.Entry<String,Object> entry:saxParserProperties.entrySet()) {
						String name = entry.getKey();
						Object value= entry.getValue();
						saxParser.setProperty(name, value);
						debugPhaseMessage("Set SAX property: "+name+" to: "+value,this,elementLanguage);
					}
					Map<String,Object> saxParserPropertiesOptional = elementLanguage.getLanguage().getLanguageConfiguration().getSAXParserPropertiesOptional(elementLanguage);
					for (Map.Entry<String,Object> entry:saxParserPropertiesOptional.entrySet()) {
						String name = entry.getKey();
						Object value= entry.getValue();
						try {
							saxParser.setProperty(name, value);
							debugPhaseMessage("Set SAX optional property: "+name+" to: "+value,this,elementLanguage);
						} catch (SAXException e) {
							debugPhaseMessage("Could not set optional SAX property: "+name+" to: "+value+" error: "+e.getMessage(),this,elementLanguage);
						}
					}
					
					// Set sax features and optional
					Map<String, Boolean> features = elementLanguage.getLanguage().getLanguageConfiguration().getSAXParserFeatures(elementLanguage);
					for (String key:features.keySet()) {
						Boolean value=features.get(key);
						saxParser.setFeature(key, value);
						debugPhaseMessage("Set SAX feature: "+key+" to: "+value,this,elementLanguage);
					}
					Map<String, Boolean> featuresOptional = elementLanguage.getLanguage().getLanguageConfiguration().getSAXParserFeaturesOptional(elementLanguage);
					for (String key:featuresOptional.keySet()) {
						Boolean value=featuresOptional.get(key);
						try {
							saxParser.setFeature(key, value);
							debugPhaseMessage("Set SAX optional feature: "+key+" to: "+value,this,elementLanguage);
						} catch (SAXException e) {
							debugPhaseMessage("Could not set optional SAX feature: "+key+" to: "+value+" error: "+e.getMessage(),this,elementLanguage);
						}
					}
					
					// check for required features
					List<String> requiredFeatures = elementLanguage.getLanguage().getLanguageConfiguration().getSAXParserFeaturesRequired(elementLanguage);
					for (String requiredFeature:requiredFeatures) {
						debugPhaseMessage("Checking required SAX feature: "+requiredFeature,this,elementLanguage);
						if (saxParser.getFeature(requiredFeature)==false) {
							Exception e = new IllegalStateException("Missing required feature: "+requiredFeature);
							throw new X4OPhaseException(this,e);
						}	
					}
					
					// Finally start parsing the xml input stream
					Object requestInputSource = elementLanguage.getLanguageProperty(X4OLanguageProperty.INPUT_SOURCE_OBJECT);
					InputSource input = null;
					InputStream inputStream = null;
					if (requestInputSource instanceof InputSource) {
						input = (InputSource)requestInputSource;
					} else {
						inputStream = (InputStream)elementLanguage.getLanguageProperty(X4OLanguageProperty.INPUT_SOURCE_STREAM);
						input = new InputSource(inputStream);
					}
					
					Object requestInputEncoding = elementLanguage.getLanguageProperty(X4OLanguageProperty.INPUT_SOURCE_ENCODING);
					if (requestInputEncoding!=null && requestInputEncoding instanceof String) {
						input.setEncoding(requestInputEncoding.toString());
					}
					Object requestSystemId = elementLanguage.getLanguageProperty(X4OLanguageProperty.INPUT_SOURCE_SYSTEM_ID);
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
				} catch (Exception e) {
					throw new X4OPhaseException(this,e);
				}
			}
		};
		X4OPhase result = new CreateSAXStreamPhase();
		return result;
	}
	
	/**
	 * Creates the configGlobalElBeansPhase which adds beans to the el context.
	 * @return	The X4OPhaseHandler for this phase.
	 */
	public X4OPhase configGlobalElBeansPhase() {
		class ConfigGlobalElBeansPhase extends AbstractX4OPhase {
			public X4OPhaseType getType() {
				return X4OPhaseType.XML_RW;
			}
			public String getId() {
				return "X4O_CONFIG_GLOBAL_EL";
			}
			public String[] getPhaseDependencies() {
				return new String[] {"READ_XML"};
			}
			public boolean isElementPhase() {
				return false;
			}
			public void runElementPhase(Element element) throws X4OPhaseException {
				// not used.
			}
			@SuppressWarnings("rawtypes")
			public void runPhase(X4OLanguageContext elementLanguage) throws X4OPhaseException {
				try {
					Map beanMap = (Map)elementLanguage.getLanguageProperty(X4OLanguageProperty.EL_BEAN_INSTANCE_MAP);
					if (beanMap==null) {
						return;
					}
					for (Object elName:beanMap.keySet()) {
						Object o = beanMap.get(elName);
						ValueExpression ve = elementLanguage.getExpressionLanguageFactory().createValueExpression(elementLanguage.getExpressionLanguageContext(),"${"+elName+"}", o.getClass());
						ve.setValue(elementLanguage.getExpressionLanguageContext(), o);
						debugPhaseMessage("Setting el bean: ${"+elName+"} to: "+o.getClass().getName(),this,elementLanguage);
					}
				} catch (Exception e) {
					throw new X4OPhaseException(this,e);
				}
			}
		};
		X4OPhase result = new ConfigGlobalElBeansPhase();
		return result;
	}
	
	/**
	 * 
	 * @return	The X4OPhaseHandler for this phase.
	 */
	public X4OPhase configElementPhase(final X4OPhaseReadRunConfigurator runConf) {
		class ConfigElementPhase extends AbstractX4OPhase {
			public X4OPhaseType getType() {
				return X4OPhaseType.XML_READ;
			}
			public String getId() {
				return "READ_CONFIG_ELEMENT";
			}
			public String[] getPhaseDependencies() {
				return new String[] {"X4O_CONFIG_GLOBAL_EL"};
			}
			public void runElementPhase(Element element) throws X4OPhaseException {
				
				// First phase is to rename attributes, maybe move so sax phase
				for (ElementClassAttribute eca:element.getElementClass().getElementClassAttributes()) {
					List<String> aliases = eca.getAttributeAliases();
					if (aliases.isEmpty()) {
						continue;
					}
					for (String alias:aliases) {
						if (element.getAttributes().containsKey(alias)) {
							String attributeValue = element.getAttributes().get(alias);
							element.getAttributes().put(eca.getName(), attributeValue);
							element.getAttributes().remove(alias);		
						}
					}
				}
				
				logger.finest("Do ElementClass Config Configurators: "+element.getElementClass().getElementConfigurators().size());
				for (ElementConfigurator ec:element.getElementClass().getElementConfigurators()) {
					runConf.runElementConfigurator(ec,element,this);
				}
			}
		};
		X4OPhase result = new ConfigElementPhase();
		return result;
	}
	
	/**
	 * 
	 * @return	The X4OPhaseHandler for this phase.
	 */
	public X4OPhase configElementInterfacePhase(final X4OPhaseReadRunConfigurator runConf) {
		class ConfigElementInterfacePhase extends AbstractX4OPhase {
			public X4OPhaseType getType() {
				return X4OPhaseType.XML_READ;
			}
			public String getId() {
				return "READ_CONFIG_ELEMENT_INTERFACE";
			}
			public String[] getPhaseDependencies() {
				return new String[] {"X4O_CONFIG_GLOBAL_EL"};
			}
			public void runElementPhase(Element element) throws X4OPhaseException {
				if (element.getElementObject()==null) {
					logger.finest("Null elementObject skipping, interfaces");
					return;
				}
				for (ElementInterface ei:element.getElementLanguage().getLanguage().findElementInterfaces(element.getElementObject())) {
					logger.finest("Do ElementInterface Config Configurators: "+ei.getElementConfigurators().size());
					for (ElementConfigurator ec:ei.getElementConfigurators()) {
						runConf.runElementConfigurator(ec,element,this);
					}
				}
			}
		};
		X4OPhase result = new ConfigElementInterfacePhase();
		return result;
	}
	
	
	/**
	 * 
	 * @return	The X4OPhaseHandler for this phase.
	 */
	public X4OPhase configGlobalElementPhase(final X4OPhaseReadRunConfigurator runConf) {
		class ConfigGlobalElementPhase extends AbstractX4OPhase {
			public X4OPhaseType getType() {
				return X4OPhaseType.XML_READ;
			}
			public String getId() {
				return "READ_CONFIG_GLOBAL_ELEMENT";
			}
			public String[] getPhaseDependencies() {
				return new String[] {"READ_CONFIG_ELEMENT","READ_CONFIG_ELEMENT_INTERFACE"};
			}
			public void runElementPhase(Element element) throws X4OPhaseException {
				for (X4OLanguageModule mod:element.getElementLanguage().getLanguage().getLanguageModules()) {
					logger.finest("Do Element Config Global Configurators: "+mod.getElementConfiguratorGlobals().size());
					for (ElementConfiguratorGlobal ec:mod.getElementConfiguratorGlobals()) {
						runConf.runElementConfigurator(ec,element,this);
					}
				}
			}
		};
		X4OPhase result = new ConfigGlobalElementPhase();
		return result;
	}
	
	/**
	 * 
	 * @return	The X4OPhaseHandler for this phase.
	 */
	public X4OPhase configGlobalAttributePhase(final X4OPhaseReadRunConfigurator runConf) {
		class ConfigGlobalAttributePhase extends AbstractX4OPhase {
			Comparator<ElementAttributeHandler> elementAttributeHandlerComparator = null;
			public X4OPhaseType getType() {
				return X4OPhaseType.XML_READ;
			}
			public String getId() {
				return "READ_CONFIG_GLOBAL_ATTRIBUTE";
			}
			public String[] getPhaseDependencies() {
				return new String[] {"READ_CONFIG_GLOBAL_ELEMENT"};
			}
			@SuppressWarnings("unchecked")
			public void runElementPhase(Element element) throws X4OPhaseException {
				if (elementAttributeHandlerComparator==null) {
					try {
						elementAttributeHandlerComparator = (Comparator<ElementAttributeHandler>)X4OLanguageClassLoader.newInstance(element.getElementLanguage().getLanguage().getLanguageConfiguration().getDefaultElementAttributeHandlerComparator());
					} catch (Exception e) {
						throw new X4OPhaseException(this,e);
					}
				}
				
				// do global parameters
				logger.finest("Do Element Global AttributeHandlers.");
				List<ElementAttributeHandler> handlers = new ArrayList<ElementAttributeHandler>();
				for (X4OLanguageModule mod:element.getElementLanguage().getLanguage().getLanguageModules()) {
					for (ElementAttributeHandler global:mod.getElementAttributeHandlers()) {
						
						String attribute = element.getAttributes().get(global.getAttributeName());
						if (attribute!=null) {
							handlers.add(global);
						}
					}
				}
				Collections.sort(handlers,elementAttributeHandlerComparator);
				List<ElementConfigurator> handlers2 = new ArrayList<ElementConfigurator>(handlers.size());
				handlers2.addAll(handlers);
				for (ElementConfigurator ec:handlers) {
					runConf.runElementConfigurator(ec,element,this);
				}
			}
		};
		X4OPhase result = new ConfigGlobalAttributePhase();
		return result;
	}
	
	/**
	 * 
	 * @return	The X4OPhaseHandler for this phase.
	 */
	public X4OPhase runAttributesPhase() {
		class RunAttributesPhase extends AbstractX4OPhase {
			public X4OPhaseType getType() {
				return X4OPhaseType.XML_READ;
			}
			public String getId() {
				return "READ_RUN_ATTRIBUTE";
			}
			public String[] getPhaseDependencies() {
				return new String[] {"READ_CONFIG_GLOBAL_ATTRIBUTE"};
			}
			public void runElementPhase(Element element) throws X4OPhaseException {
				// we only can config ElementObjects
				if (element.getElementObject()==null) {
					return;
				}
				
				// do config
				Map<String,String> attr = element.getAttributes();
				ElementAttributeValueParser attrParser = element.getElementLanguage().getElementAttributeValueParser();
				Boolean autoAttributes = element.getElementClass().getAutoAttributes();
				if (autoAttributes==null) {
					autoAttributes = true;
				}
				try {
					for (String name:attr.keySet()) {
						String valueString = (String)attr.get(name);
						if (valueString==null) {
							continue;
						}
						Object value = valueString;
						ElementClassAttribute attrClass = element.getElementClass().getElementClassAttributeByName(name);
						if (attrClass!=null) {
							if (attrClass.getRunResolveEL()==null || attrClass.getRunResolveEL() && attrParser.isELParameter(name, valueString, element)) {
								value = attrParser.getELParameterValue(valueString, element);
							}
							if (attrClass.getRunConverters()==null || attrClass.getRunConverters()) {
								value = attrParser.getConvertedParameterValue(name, value, element);
							}
							if (attrClass.getRunBeanFill()==null || attrClass.getRunBeanFill()) {
								element.getElementLanguage().getElementObjectPropertyValue().setProperty(element.getElementObject(), name, value);
							}
						} else if (autoAttributes) {
							
							value = attrParser.getParameterValue(name,valueString,element);
							element.getElementLanguage().getElementObjectPropertyValue().setProperty(element.getElementObject(), name, value);
							
						} else {
							continue; // skip all non auto attribute non attribute defined xml attributes, or throw exception ?
						}
					}
					// check for defaults
					for (ElementClassAttribute attrClass:element.getElementClass().getElementClassAttributes()) {
						if (attrClass.getDefaultValue()!=null && attr.containsKey(attrClass.getName())==false) {
							Object value = null;
							if (attrClass.getDefaultValue() instanceof String) {
								value = attrParser.getParameterValue(attrClass.getName(),(String)attrClass.getDefaultValue(),element);
							} else {
								value = attrClass.getDefaultValue();
							}
							if (attrClass.getRunBeanFill()==null || attrClass.getRunBeanFill()) {
								element.getElementLanguage().getElementObjectPropertyValue().setProperty(element.getElementObject(), attrClass.getName(), value);
							}
						}
					}

				} catch (ObjectConverterException oce) {
					throw new X4OPhaseException(this,"Error while converting parameters: "+oce.getMessage(),oce);
				} catch (ElementException e) {
					throw new X4OPhaseException(this,"Error while setting parameters: "+e.getMessage(),e);
				}
			}
		};
		X4OPhase result = new RunAttributesPhase();
		return result;
	}
	
	/**
	 * 
	 * @return	The X4OPhaseHandler for this phase.
	 */
	public X4OPhase fillTemplatingPhase() {
		X4OPhase result = new AbstractX4OPhase() {
			public X4OPhaseType getType() {
				return X4OPhaseType.XML_READ;
			}
			public String getId() {
				return "READ_FILL_TEMPLATE";
			}
			public String[] getPhaseDependencies() {
				return new String[] {"READ_RUN_ATTRIBUTE"};
			}
			public void runElementPhase(Element element) throws X4OPhaseException {
			}
		};
		return result;
	}
	
	/**
	 * 
	 * @return	The X4OPhaseHandler for this phase.
	 */
	public X4OPhase transformPhase() {
		X4OPhase result = new AbstractX4OPhase() {
			public X4OPhaseType getType() {
				return X4OPhaseType.XML_READ;
			}
			public String getId() {
				return "READ_TRANSFORM";
			}
			public String[] getPhaseDependencies() {
				return new String[] {"READ_FILL_TEMPLATE"};
			}
			public void runElementPhase(Element element) throws X4OPhaseException {
				if (element.isTransformingTree()==false) {
					return;
				}
				try {
					if (element.getElementLanguage().hasX4ODebugWriter()) {
						element.getElementLanguage().getX4ODebugWriter().debugElement(element);
					}
					element.doElementRun();
				} catch (ElementException e) {
					throw new X4OPhaseException(this,e);
				}
			}
		};
		return result;
	}
	
	/**
	 * 
	 * @param manager	The phasemanager.
	 * @return	The X4OPhaseHandler for this phase.
	 */
	public X4OPhase runDirtyElementPhase(final X4OPhaseManager manager) {
		class RunDirtyElementPhase extends AbstractX4OPhase {
			public X4OPhaseType getType() {
				return X4OPhaseType.XML_READ;
			}
			public String getId() {
				return "READ_RUN_DIRTY";
			}
			public String[] getPhaseDependencies() {
				return new String[] {"READ_TRANSFORM"};
			}
			public void runElementPhase(Element element) throws X4OPhaseException {
				Map<Element,X4OPhase> dirtyElements = element.getElementLanguage().getDirtyElements();
				if (dirtyElements.isEmpty()) {
					return;
				}
				debugPhaseMessage("Dirty elements: "+dirtyElements.size(), this,element.getElementLanguage());
				for (Element e:dirtyElements.keySet()) {
					X4OPhase p = dirtyElements.get(e);
					manager.runPhasesForElement(e,getType(), p);
				}
				element.getElementLanguage().getDirtyElements().clear();
			}
		};
		X4OPhase result = new RunDirtyElementPhase();
		return result;
	}
	
	/**
	 * 
	 * @param manager	The phasemanager.
	 * @return	The X4OPhaseHandler for this phase.
	 */
	public X4OPhase runDirtyElementLastPhase(final X4OPhaseManager manager) {
		class RunDirtyElementLastPhase extends AbstractX4OPhase {
			public X4OPhaseType getType() {
				return X4OPhaseType.XML_READ;
			}
			public String getId() {
				return "READ_RUN_DIRTY_LAST";
			}
			public String[] getPhaseDependencies() {
				return new String[] {"READ_RUN"};
			}
			public void runElementPhase(Element element) throws X4OPhaseException {
				Map<Element,X4OPhase> dirtyElements = element.getElementLanguage().getDirtyElements();
				if (dirtyElements.isEmpty()) {
					return;
				}
				debugPhaseMessage("Dirty elements last: "+dirtyElements.size(), this,element.getElementLanguage());
				for (Element e:dirtyElements.keySet()) {
					X4OPhase p = dirtyElements.get(e);
					manager.runPhasesForElement(e,getType(), p);
				}
				element.getElementLanguage().getDirtyElements().clear();
			}
		};
		X4OPhase result = new RunDirtyElementLastPhase();
		return result;
	}
	
	/**
	 * 
	 * @return	The X4OPhaseHandler for this phase.
	 */
	public X4OPhase bindElementPhase() {
		class BindElementPhase extends AbstractX4OPhase {
			public X4OPhaseType getType() {
				return X4OPhaseType.XML_READ;
			}
			public String getId() {
				return "READ_BIND_ELEMENT";
			}
			public String[] getPhaseDependencies() {
				return new String[] {"READ_RUN_DIRTY"};
			}
			public void runElementPhase(Element element) throws X4OPhaseException {
				Element parentElement = element.getParent();
				if(parentElement==null) {
					logger.finest("No parent element, so no binding needed.");
					return;
				}
				Object parentObject = parentElement.getElementObject();
				if(parentObject==null) {
					logger.finest("No parent object, so no binding needed.");
					return;
				}
				Object childObject = element.getElementObject();
				if (childObject==null) {
					logger.finest("No child object, so no binding needed.");
					return;
				}
			
				List<ElementBindingHandler> binds = element.getElementLanguage().getLanguage().findElementBindingHandlers(parentObject, childObject);
				logger.finest("Calling bindings handlers; "+binds.size());
				try {
					for (ElementBindingHandler binding:binds) {
						if (element.getElementLanguage().hasX4ODebugWriter()) {
							element.getElementLanguage().getX4ODebugWriter().debugElementBindingHandler(binding,element);
						}
						binding.doBind(parentObject,childObject,element);
					}
				} catch (ElementException e) {
					throw new X4OPhaseException(this,"Error while binding",e);
				}
			}
		};
		X4OPhase result = new BindElementPhase();
		return result;
	}
	
	/**
	 * 
	 * @return	The X4OPhaseHandler for this phase.
	 */
	public X4OPhase runPhase() {
		X4OPhase result = new AbstractX4OPhase() {
			public X4OPhaseType getType() {
				return X4OPhaseType.XML_READ;
			}
			public String getId() {
				return "READ_RUN";
			}
			public String[] getPhaseDependencies() {
				return new String[] {"READ_BIND_ELEMENT"};
			}
			public void runElementPhase(Element element) throws X4OPhaseException {
				if (element.isTransformingTree()) {
					return; // has already runned.
				}
				try {
					//if (parser.hasX4ODebugWriter()) {
					//	parser.getX4ODebugWriter().debugElement(element);
					//}
					element.doElementRun();		
				} catch (ElementException e) {
					throw new X4OPhaseException(this,e);
				}
			}
		};
		return result;
	}

	public class X4OPhaseReadRunConfigurator extends AbstractX4OPhase {
		private List<RunConfigurator> runConf = null;
		protected X4OPhaseReadRunConfigurator() {
			runConf = new ArrayList<RunConfigurator>(10);
		}
		public X4OPhaseType getType() {
			return X4OPhaseType.XML_READ;
		}
		public String getId() {
			return "READ_RUN_CONFIGURATOR";
		}
		public String[] getPhaseDependencies() {
			return new String[] {"READ_RUN"};
		}
		public boolean isElementPhase() {
			return false;
		}
		public void runPhase(X4OLanguageContext elementLanguage) throws X4OPhaseException {
			for (RunConfigurator conf:runConf) {
				try {
					if (elementLanguage.hasX4ODebugWriter()) {
						elementLanguage.getX4ODebugWriter().debugElementConfigurator(conf.elementConfigurator,conf.element);
					}
					conf.elementConfigurator.doConfigElement(conf.element);
				} catch (ElementException e) {
					throw new X4OPhaseException(this,e);
				}
			}
		}
		@Override
		public void runElementPhase(Element element) throws X4OPhaseException {
		}
		class RunConfigurator {
			Element element;
			ElementConfigurator elementConfigurator;
			RunConfigurator(Element element,ElementConfigurator elementConfigurator) {
				this.element=element;
				this.elementConfigurator=elementConfigurator;
			}
		}
		public void runElementConfigurator(ElementConfigurator ec,Element e,X4OPhase phase) throws X4OPhaseException {
			if (ec.isConfigAction()) {
				runConf.add(new RunConfigurator(e,ec));
				return;
			}
			try {
				if (e.getElementLanguage().hasX4ODebugWriter()) {
					e.getElementLanguage().getX4ODebugWriter().debugElementConfigurator(ec,e);
				}
				ec.doConfigElement(e);
				
				// may request rerun of config 
				if (ec.isConfigAction()) {
					runConf.add(new RunConfigurator(e,ec));
				}
			} catch (ElementException ee) {
				throw new X4OPhaseException(phase,ee);
			}
		}
	}
	
	/**
	 * 
	 * @return	The X4OPhaseHandler for this phase.
	 */
	public X4OPhaseReadRunConfigurator readRunConfiguratorPhase() {
		return new X4OPhaseReadRunConfigurator();
	}
	
	/**
	 * Ends the read phase
	 * @return	The X4OPhaseHandler for this phase.
	 */
	public X4OPhase readEndX4OPhase() {
		class ReadStartX4OPhase extends AbstractX4OPhase {
			public X4OPhaseType getType() {
				return X4OPhaseType.XML_READ;
			}
			public String getId() {
				return "READ_END";
			}
			public String[] getPhaseDependencies() {
				return new String[]{"READ_RUN_CONFIGURATOR"};
			}
			public boolean isElementPhase() {
				return false;
			}
			public void runElementPhase(Element element) throws X4OPhaseException {
				// not used.
			}
			public void runPhase(X4OLanguageContext elementLanguage) throws X4OPhaseException {
				// print the properties and classes for this language/config
				if (elementLanguage.hasX4ODebugWriter()) {
					try {
						elementLanguage.getX4ODebugWriter().debugLanguageProperties(elementLanguage);
						elementLanguage.getX4ODebugWriter().debugLanguageDefaultClasses(elementLanguage);
					} catch (ElementException e) {
						throw new X4OPhaseException(this,e);
					}
				}
			}
		};
		X4OPhase result = new ReadStartX4OPhase();
		return result;
	}
	
	/**
	 * 
	 * @return	The X4OPhaseHandler for this phase.
	 */
	public X4OPhase releasePhase() {
		
		// for debug output 
		class ReleasePhaseListener implements X4OPhaseListener {
			private int elementsReleased = 0;
			/**
			 * @see org.x4o.xml.lang.phase.X4OPhaseListener#preRunPhase(org.x4o.xml.lang.phase.X4OPhase, org.x4o.xml.lang.X4OLanguageContext)
			 */
			public void preRunPhase(X4OPhase phase,X4OLanguageContext elementLanguage) throws X4OPhaseException {
				elementsReleased=0;
			}
			/**
			 * @see org.x4o.xml.lang.phase.X4OPhaseListener#endRunPhase(org.x4o.xml.lang.phase.X4OPhase, org.x4o.xml.lang.X4OLanguageContext)
			 */
			public void endRunPhase(X4OPhase phase,X4OLanguageContext elementLanguage) throws X4OPhaseException {
				if (elementLanguage.hasX4ODebugWriter()==false) {
					return;
				}
				try {
					AttributesImpl atts = new AttributesImpl();
					atts.addAttribute ("", "elements", "", "", elementsReleased+"");
					elementLanguage.getX4ODebugWriter().getDebugWriter().startElement (X4ODebugWriter.DEBUG_URI, "executeReleases", "", atts);
					elementLanguage.getX4ODebugWriter().getDebugWriter().endElement (X4ODebugWriter.DEBUG_URI, "executeReleases" , "");
				} catch (SAXException e) {
					throw new X4OPhaseException(phase,e);
				}
			}
			
			public void addReleasedElement() {
				elementsReleased++;
			}
		}
		
		final ReleasePhaseListener releaseCounter = new ReleasePhaseListener();
		X4OPhase result = new AbstractX4OPhase() {
			public X4OPhaseType getType() {
				return X4OPhaseType.XML_RW;
			}
			public String getId() {
				return "X4O_RELEASE";
			}
			public String[] getPhaseDependencies() {
				return new String[] {"READ_END"};
			}
			public void runPhase(X4OLanguageContext elementLanguage) throws X4OPhaseException {
			}
			public void runElementPhase(Element element) throws X4OPhaseException  {
				try {
					element.release();
				} catch (ElementException e) {
					throw new X4OPhaseException(this,e);
				} finally {
					releaseCounter.addReleasedElement();
				}
			}
		};
		result.addPhaseListener(releaseCounter);
		return result;
	}
	
	/**
	 * Creates an debug phase
	 * @return	The X4OPhaseHandler for this phase.
	 */
	public X4OPhase debugPhase(final X4OPhase afterPhase) {
		X4OPhase result = new AbstractX4OPhase() {
			public X4OPhaseType getType() {
				return X4OPhaseType.XML_RW;
			}
			public String getId() {
				return "X4O_DEBUG_"+afterPhase.getId();
			}
			public String[] getPhaseDependencies() {
				return new String[] {afterPhase.getId()};
			}
			public boolean isElementPhase() {
				return false;
			}
			public void runElementPhase(Element element) throws X4OPhaseException {
			}
			public void runPhase(X4OLanguageContext elementLanguage) throws X4OPhaseException {
				// safety check
				if (elementLanguage.hasX4ODebugWriter()==false) {
					throw new X4OPhaseException(this,"Use debugPhase only when X4OParser.debugWriter is filled.");
				}
				try {
					AttributesImpl atts = new AttributesImpl();
					//atts.addAttribute ("", key, "", "", value);
					//atts.addAttribute ("", "verbose", "", "", "true");
					elementLanguage.getX4ODebugWriter().getDebugWriter().startElement (X4ODebugWriter.DEBUG_URI, "printElementTree", "", atts);
					startedPrefix.clear();
					printXML(elementLanguage.getRootElement());
					for (String prefix:startedPrefix) {
						elementLanguage.getX4ODebugWriter().getDebugWriter().endPrefixMapping(prefix);
					}
					elementLanguage.getX4ODebugWriter().getDebugWriter().endElement(X4ODebugWriter.DEBUG_URI, "printElementTree", "");
					elementLanguage.getX4ODebugWriter().debugElementLanguage(elementLanguage);
				} catch (SAXException e) {
					throw new X4OPhaseException(this,e);
				}
			}
			List<String> startedPrefix = new ArrayList<String>(10);
			
			// note: slow version
			private String getNamespaceForElement(Element e) {
				for (X4OLanguageModule mod:e.getElementLanguage().getLanguage().getLanguageModules()) {
					for (ElementNamespaceContext enc:mod.getElementNamespaceContexts()) {
						List<ElementClass> l = enc.getElementClasses();
						if (l.contains(e.getElementClass())) {
							return enc.getUri();
						}
					}
				}
				return null;
			}
			
			private void printXML(Element element) throws SAXException {
				if (element==null) {
					throw new SAXException("Can't print debug xml of null element.");
				}
				DefaultHandler2 handler = element.getElementLanguage().getX4ODebugWriter().getDebugWriter();
				if (element.getElementType().equals(Element.ElementType.comment)) {
					char[] msg = ((String)element.getElementObject()).toCharArray();
					handler.comment(msg,0,msg.length);
					return;
				}
				if (element.getElementType().equals(Element.ElementType.characters)) {
					char[] msg = ((String)element.getElementObject()).toCharArray();
					handler.characters(msg,0,msg.length);
					return;
				}
				if (element.getElementClass()==null) {
					throw new SAXException("Element without ElementClass is not valid: "+element+" obj: "+element.getElementObject());
				}
				
				AttributesImpl atts = new AttributesImpl();
				for (String key:element.getAttributes().keySet()) {
					String value = element.getAttributes().get(key);
					//uri, localName, xml1.0name, type, value
					atts.addAttribute ("", key, "", "", value);
				}
				
				String nameSpace = getNamespaceForElement(element);
				String prefix = element.getElementLanguage().getLanguage().findElementNamespaceContext(nameSpace).getPrefixMapping();
				
				if (startedPrefix.contains(prefix)==false) {
					handler.startPrefixMapping(prefix, nameSpace);
					startedPrefix.add(prefix);
				}
				handler.startElement (nameSpace, element.getElementClass().getTag(), "", atts);
				for (Element e:element.getAllChilderen()) {
					printXML(e);
				}
				handler.endElement (nameSpace, element.getElementClass().getTag(), "");
			}
		};
		return result;
	}
}
