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

package	org.x4o.xml.core;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.el.ValueExpression;

import org.x4o.xml.conv.ObjectConverterException;
import org.x4o.xml.core.config.X4OLanguageClassLoader;
import org.x4o.xml.core.config.X4OLanguageLoader;
import org.x4o.xml.core.config.X4OLanguageProperty;

import org.x4o.xml.element.Element;
import org.x4o.xml.element.ElementAttributeHandler;
import org.x4o.xml.element.ElementAttributeValueParser;
import org.x4o.xml.element.ElementBindingHandler;
import org.x4o.xml.element.ElementClass;
import org.x4o.xml.element.ElementClassAttribute;
import org.x4o.xml.element.ElementConfigurator;
import org.x4o.xml.element.ElementLanguage;
import org.x4o.xml.element.ElementException;
import org.x4o.xml.element.ElementInterface;
import org.x4o.xml.element.ElementLanguageModule;
import org.x4o.xml.element.ElementLanguageModuleLoaderSibling;
import org.x4o.xml.element.ElementNamespaceContext;

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
public class X4OPhaseHandlerFactory {

	private Logger logger = null;
	private ElementLanguage elementLanguage = null;
	private List<RunConfigurator> runConf = null;
	
	public X4OPhaseHandlerFactory(ElementLanguage elementLanguage) {
		if (elementLanguage==null) {
			throw new NullPointerException("Can't start factory with null elementLanguage.");
		}
		this.elementLanguage=elementLanguage;
		logger = Logger.getLogger(X4OPhaseHandlerFactory.class.getName());
		runConf = new ArrayList<RunConfigurator>(10);
	}
	
	class RunConfigurator {
		Element element;
		ElementConfigurator elementConfigurator;
		RunConfigurator(Element element,ElementConfigurator elementConfigurator) {
			this.element=element;
			this.elementConfigurator=elementConfigurator;
		}
	}
	
	private void runElementConfigurators(List<ElementConfigurator> ecs,Element e,X4OPhaseHandler phase) throws X4OPhaseException {
		int size = ecs.size();
		for (int i=0;i<size;i++) {
			ElementConfigurator ec = ecs.get(i);
			if (ec.isConfigAction()) {
				runConf.add(new RunConfigurator(e,ec));
				return;
			}
			try {
				if (hasX4ODebugWriter()) {
					getX4ODebugWriter().debugElementConfigurator(ec,e);				
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
	
	private void debugPhaseMessage(String message,X4OPhaseHandler phaseHandler) throws X4OPhaseException {
		if (hasX4ODebugWriter()) {
			try {
				getX4ODebugWriter().debugPhaseMessage(message,phaseHandler.getClass());
			} catch (ElementException ee) {
				throw new X4OPhaseException(phaseHandler,ee);
			}
		}
	}
	
	private boolean hasX4ODebugWriter() {
		return elementLanguage.getLanguageConfiguration().hasX4ODebugWriter();
	}
	
	private X4ODebugWriter getX4ODebugWriter() {
		return elementLanguage.getLanguageConfiguration().getX4ODebugWriter();
	}
		
	/**
	 * Creates the startupX4OPhase which is a empty meta phase.
	 * @return	The X4OPhaseHandler for this phase.
	 */
	public X4OPhaseHandler startupX4OPhase() {
		class CreateStartX4OPhase extends AbstractX4OPhaseHandler {
			protected void setX4OPhase() {
				phase = X4OPhase.startupX4OPhase;
			}
			public boolean isElementPhase() {
				return false;
			}
			public void runElementPhase(Element element) throws X4OPhaseException {
				// not used.
			}
			public void runPhase(ElementLanguage elementLanguage) throws X4OPhaseException  {
				// print the properties and classes for this language/config
				if (hasX4ODebugWriter()) {
					try {
						getX4ODebugWriter().debugLanguageProperties(elementLanguage);
						getX4ODebugWriter().debugLanguageDefaultClasses(elementLanguage);
					} catch (ElementException e) {
						throw new X4OPhaseException(this,e);
					}
				}
			}
		};
		X4OPhaseHandler result = new CreateStartX4OPhase();
		return result;
	}
	
	/**
	 * Loads all the modules a language.
	 * Then creates the ElementProviders
	 * @return	The X4OPhaseHandler for this phase.
	 */
	public X4OPhaseHandler createLanguagePhase() {
		class CreateLanguagePhase extends AbstractX4OPhaseHandler {
			protected void setX4OPhase() {
				phase = X4OPhase.createLanguagePhase;
			}
			public boolean isElementPhase() {
				return false;
			}
			public void runElementPhase(Element element) throws X4OPhaseException {
			}
			public void runPhase(ElementLanguage elementLanguage) throws X4OPhaseException  {
				try {
					debugPhaseMessage("Loading main language: "+elementLanguage.getLanguageConfiguration().getLanguage(),this);
					X4OLanguageLoader loader = (X4OLanguageLoader)X4OLanguageClassLoader.newInstance(elementLanguage.getLanguageConfiguration().getDefaultX4OLanguageLoader());
					loader.loadLanguage(elementLanguage,elementLanguage.getLanguageConfiguration().getLanguage(),elementLanguage.getLanguageConfiguration().getLanguageVersion());
					
					if (hasX4ODebugWriter()) {
						getX4ODebugWriter().debugElementLanguageModules(elementLanguage);
					}
				} catch (Exception e) {
					throw new X4OPhaseException(this,e);
				}
			}
		};
		X4OPhaseHandler result = new CreateLanguagePhase();
		return result;
	}
	
	/**
	 * Loads all sibling languages.
	 * @return	The X4OPhaseHandler for this phase.
	 */
	public X4OPhaseHandler createLanguageSiblingsPhase() {
		class CreateLanguageSiblingsPhase extends AbstractX4OPhaseHandler {
			protected void setX4OPhase() {
				phase = X4OPhase.createLanguageSiblingsPhase;
			}
			public boolean isElementPhase() {
				return false;
			}
			public void runElementPhase(Element element) throws X4OPhaseException {
			}
			public void runPhase(ElementLanguage elementLanguage) throws X4OPhaseException  {
				try {
					List<ElementLanguageModuleLoaderSibling> siblingLoaders = new ArrayList<ElementLanguageModuleLoaderSibling>(3);
					for (ElementLanguageModule module:elementLanguage.getElementLanguageModules()) {	
						if (module.getElementLanguageModuleLoader() instanceof ElementLanguageModuleLoaderSibling) {
							siblingLoaders.add((ElementLanguageModuleLoaderSibling)module.getElementLanguageModuleLoader());
						}
					}
					if (siblingLoaders.isEmpty()==false) {
						X4OLanguageLoader loader = (X4OLanguageLoader)X4OLanguageClassLoader.newInstance(elementLanguage.getLanguageConfiguration().getDefaultX4OLanguageLoader());
						for (ElementLanguageModuleLoaderSibling siblingLoader:siblingLoaders) {
							debugPhaseMessage("Loading sibling langauge loader: "+siblingLoader,this);
							siblingLoader.loadLanguageSibling(elementLanguage, loader);
						}
						if (hasX4ODebugWriter()) {
							getX4ODebugWriter().debugElementLanguageModules(elementLanguage);				
						}
					}
				} catch (Exception e) {
					throw new X4OPhaseException(this,e);
				}
			}
		};
		X4OPhaseHandler result = new CreateLanguageSiblingsPhase();
		return result;
	}
	
	/**
	 * Parses the xml resource(s) and creates an Element tree.
	 * @return	The X4OPhaseHandler for this phase.
	 */
	public X4OPhaseHandler parseSAXStreamPhase() {
		class CreateSAXStreamPhase extends AbstractX4OPhaseHandler {
			protected void setX4OPhase() {
				phase = X4OPhase.parseSAXStreamPhase;
			}
			public boolean isElementPhase() {
				return false;
			}
			public void runElementPhase(Element element) throws X4OPhaseException {
			}
			public void runPhase(ElementLanguage elementLanguage) throws X4OPhaseException  {
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
					Map<String,Object> saxParserProperties = elementLanguage.getLanguageConfiguration().getSAXParserProperties();
					for (Map.Entry<String,Object> entry:saxParserProperties.entrySet()) {
						String name = entry.getKey();
						Object value= entry.getValue();
						saxParser.setProperty(name, value);
						debugPhaseMessage("Set SAX property: "+name+" to: "+value,this);
					}
					Map<String,Object> saxParserPropertiesOptional = elementLanguage.getLanguageConfiguration().getSAXParserPropertiesOptional();
					for (Map.Entry<String,Object> entry:saxParserPropertiesOptional.entrySet()) {
						String name = entry.getKey();
						Object value= entry.getValue();
						try {
							saxParser.setProperty(name, value);
							debugPhaseMessage("Set SAX optional property: "+name+" to: "+value,this);
						} catch (SAXException e) {
							debugPhaseMessage("Could not set optional SAX property: "+name+" to: "+value+" error: "+e.getMessage(),this);
						}
					}
					
					// Set sax features and optional
					Map<String, Boolean> features = elementLanguage.getLanguageConfiguration().getSAXParserFeatures();
					for (String key:features.keySet()) {
						Boolean value=features.get(key);
						saxParser.setFeature(key, value);
						debugPhaseMessage("Set SAX feature: "+key+" to: "+value,this);
					}
					Map<String, Boolean> featuresOptional = elementLanguage.getLanguageConfiguration().getSAXParserFeaturesOptional();
					for (String key:featuresOptional.keySet()) {
						Boolean value=featuresOptional.get(key);
						try {
							saxParser.setFeature(key, value);
							debugPhaseMessage("Set SAX optional feature: "+key+" to: "+value,this);
						} catch (SAXException e) {
							debugPhaseMessage("Could not set optional SAX feature: "+key+" to: "+value+" error: "+e.getMessage(),this);
						}
					}
					
					// check for required features
					List<String> requiredFeatures = elementLanguage.getLanguageConfiguration().getSAXParserFeaturesRequired();
					for (String requiredFeature:requiredFeatures) {
						debugPhaseMessage("Checking required SAX feature: "+requiredFeature,this);
						if (saxParser.getFeature(requiredFeature)==false) {
							Exception e = new IllegalStateException("Missing required feature: "+requiredFeature); 
							throw new X4OPhaseException(this,e);
						}	
					}
					
					// Finally start parsing the xml input stream
					Object requestInputSource = elementLanguage.getLanguageConfiguration().getLanguageProperty(X4OLanguageProperty.INPUT_SOURCE_OBJECT);
					InputSource input = null;
					InputStream inputStream = null;
					if (requestInputSource instanceof InputSource) {
						input = (InputSource)requestInputSource;
					} else {
						inputStream = (InputStream)elementLanguage.getLanguageConfiguration().getLanguageProperty(X4OLanguageProperty.INPUT_SOURCE_STREAM);
						input = new InputSource(inputStream);
					}
					
					Object requestInputEncoding = elementLanguage.getLanguageConfiguration().getLanguageProperty(X4OLanguageProperty.INPUT_SOURCE_ENCODING);
					if (requestInputEncoding!=null && requestInputEncoding instanceof String) {
						input.setEncoding(requestInputEncoding.toString());
					}
					Object requestSystemId = elementLanguage.getLanguageConfiguration().getLanguageProperty(X4OLanguageProperty.INPUT_SOURCE_SYSTEM_ID);
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
		X4OPhaseHandler result = new CreateSAXStreamPhase();
		return result;
	}
	
	/**
	 * Creates the startX4OPhase which is a empty meta phase.
	 * @return	The X4OPhaseHandler for this phase.
	 */
	public X4OPhaseHandler startX4OPhase() {
		class CreateStartX4OPhase extends AbstractX4OPhaseHandler {
			protected void setX4OPhase() {
				phase = X4OPhase.startX4OPhase;
			}
			public boolean isElementPhase() {
				return false;
			}
			public void runElementPhase(Element element) throws X4OPhaseException {
				// not used.
			}
			public void runPhase(ElementLanguage elementLanguage) throws X4OPhaseException  {
				// empty because this is a meta phase.
			}
		};
		X4OPhaseHandler result = new CreateStartX4OPhase();
		return result;
	}
	
	/**
	 * Creates the configGlobalElBeansPhase which adds beans to the el context.
	 * @return	The X4OPhaseHandler for this phase.
	 */
	public X4OPhaseHandler configGlobalElBeansPhase() {
		class ConfigGlobalElBeansPhase extends AbstractX4OPhaseHandler {
			protected void setX4OPhase() {
				phase = X4OPhase.configGlobalElBeansPhase;
			}
			public boolean isElementPhase() {
				return false;
			}
			public void runElementPhase(Element element) throws X4OPhaseException {
				// not used.
			}
			@SuppressWarnings("rawtypes")
			public void runPhase(ElementLanguage elementLanguage) throws X4OPhaseException  {
				try {
					Map beanMap = (Map)elementLanguage.getLanguageConfiguration().getLanguageProperty(X4OLanguageProperty.EL_BEAN_INSTANCE_MAP);
					if (beanMap==null) {
						return;
					}
					for (Object elName:beanMap.keySet()) {
						Object o = beanMap.get(elName);
						ValueExpression ve = elementLanguage.getExpressionFactory().createValueExpression(elementLanguage.getELContext(),"${"+elName+"}", o.getClass());
						ve.setValue(elementLanguage.getELContext(), o);
						debugPhaseMessage("Setting el bean: ${"+elName+"} to: "+o.getClass().getName(),this);
					}
				} catch (Exception e) {
					throw new X4OPhaseException(this,e);
				}
			}
		};
		X4OPhaseHandler result = new ConfigGlobalElBeansPhase();
		return result;
	}
	
	/**
	 * 
	 * @return	The X4OPhaseHandler for this phase.
	 */
	public X4OPhaseHandler configElementPhase() {
		class ConfigElementPhase extends AbstractX4OPhaseHandler {
			protected void setX4OPhase() {
				phase = X4OPhase.configElementPhase;
			}
			public void runElementPhase(Element element) throws X4OPhaseException  {
				
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
				runElementConfigurators(element.getElementClass().getElementConfigurators(),element,this);
			}
		};
		X4OPhaseHandler result = new ConfigElementPhase();
		return result;
	}
	
	/**
	 * 
	 * @return	The X4OPhaseHandler for this phase.
	 */
	public X4OPhaseHandler configElementInterfacePhase() {
		class ConfigElementInterfacePhase extends AbstractX4OPhaseHandler {
			protected void setX4OPhase() {
				phase = X4OPhase.configElementInterfacePhase;
			}
			public void runElementPhase(Element element) throws X4OPhaseException  {
				if (element.getElementObject()==null) {
					logger.finest("Null elementObject skipping, interfaces");
					return;
				}
				for (ElementInterface ei:element.getElementLanguage().findElementInterfaces(element.getElementObject())) {
					logger.finest("Do ElementInterface Config Configurators: "+element.getElementClass().getElementConfigurators().size());
					runElementConfigurators(ei.getElementConfigurators(),element,this);
				}
			}
		};
		X4OPhaseHandler result = new ConfigElementInterfacePhase();
		return result;
	}
	
	
	/**
	 * 
	 * @return	The X4OPhaseHandler for this phase.
	 */
	public X4OPhaseHandler configGlobalElementPhase() {
		class ConfigGlobalElementPhase extends AbstractX4OPhaseHandler {
			protected void setX4OPhase() {
				phase = X4OPhase.configGlobalElementPhase;
			}
			public void runElementPhase(Element element) throws X4OPhaseException  {
				for (ElementLanguageModule mod:element.getElementLanguage().getElementLanguageModules()) {
					logger.finest("Do Element Config Global Configurators: "+mod.getGlobalElementConfigurators().size());
					runElementConfigurators(mod.getGlobalElementConfigurators(),element,this);
				}
			}
		};
		X4OPhaseHandler result = new ConfigGlobalElementPhase();
		return result;
	}
	
	/**
	 * 
	 * @return	The X4OPhaseHandler for this phase.
	 */
	public X4OPhaseHandler configGlobalAttributePhase() {
		class ConfigGlobalAttributePhase extends AbstractX4OPhaseHandler {
			Comparator<ElementAttributeHandler> elementAttributeHandlerComparator = null;
			protected void setX4OPhase() {
				phase = X4OPhase.configGlobalAttributePhase;
			}
			@SuppressWarnings("unchecked")
			public void runElementPhase(Element element) throws X4OPhaseException  {
				if (elementAttributeHandlerComparator==null) {
					try {
						elementAttributeHandlerComparator = (Comparator<ElementAttributeHandler>)X4OLanguageClassLoader.newInstance(element.getElementLanguage().getLanguageConfiguration().getDefaultElementAttributeHandlerComparator());
					} catch (Exception e) {
						throw new X4OPhaseException(this,e);
					}
				}
				
				// do global parameters
				logger.finest("Do Element Global AttributeHandlers.");
				List<ElementAttributeHandler> handlers = new ArrayList<ElementAttributeHandler>();
				for (ElementLanguageModule mod:element.getElementLanguage().getElementLanguageModules()) {
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
				runElementConfigurators(handlers2,element,this);
			}
		};
		X4OPhaseHandler result = new ConfigGlobalAttributePhase();
		return result;
	}
	
	/**
	 * 
	 * @return	The X4OPhaseHandler for this phase.
	 */
	public X4OPhaseHandler runAttributesPhase() {
		class RunAttributesPhase extends AbstractX4OPhaseHandler {
			protected void setX4OPhase() {
				phase = X4OPhase.runAttributesPhase;
			}
			public void runElementPhase(Element element) throws X4OPhaseException  {
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
		X4OPhaseHandler result = new RunAttributesPhase();
		return result;
	}
	
	/**
	 * 
	 * @return	The X4OPhaseHandler for this phase.
	 */
	public X4OPhaseHandler fillTemplatingPhase() {
		X4OPhaseHandler result = new AbstractX4OPhaseHandler() {
			protected void setX4OPhase() {
				phase = X4OPhase.fillTemplatingPhase;
			}
			public void runElementPhase(Element element) throws X4OPhaseException  {
			}
		};
		return result;
	}
	
	/**
	 * 
	 * @return	The X4OPhaseHandler for this phase.
	 */
	public X4OPhaseHandler transformPhase() {
		X4OPhaseHandler result = new AbstractX4OPhaseHandler() {
			protected void setX4OPhase() {
				phase = X4OPhase.transformPhase;
			}
			public void runElementPhase(Element element) throws X4OPhaseException  {
				if (element.isTransformingTree()==false) {
					return;
				}
				try {
					if (hasX4ODebugWriter()) {
						getX4ODebugWriter().debugElement(element);				
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
	public X4OPhaseHandler runDirtyElementPhase(final X4OPhaseManager manager) {
		class RunDirtyElementPhase extends AbstractX4OPhaseHandler {
			protected void setX4OPhase() {
				phase = X4OPhase.runDirtyElementPhase;
			}
			public void runElementPhase(Element element) throws X4OPhaseException  {
				Map<Element,X4OPhase> dirtyElements = element.getElementLanguage().getDirtyElements();
				if (dirtyElements.isEmpty()) {
					return;
				}
				debugPhaseMessage("Dirty elements: "+dirtyElements.size(), this);
				for (Element e:dirtyElements.keySet()) {
					X4OPhase p = dirtyElements.get(e);
					manager.runPhasesForElement(e, p);
				}
				element.getElementLanguage().getDirtyElements().clear();
			}
		};
		X4OPhaseHandler result = new RunDirtyElementPhase();
		return result;
	}
	
	/**
	 * 
	 * @param manager	The phasemanager.
	 * @return	The X4OPhaseHandler for this phase.
	 */
	public X4OPhaseHandler runDirtyElementLastPhase(final X4OPhaseManager manager) {
		class RunDirtyElementLastPhase extends AbstractX4OPhaseHandler {
			protected void setX4OPhase() {
				phase = X4OPhase.runDirtyElementLastPhase;
			}
			public void runElementPhase(Element element) throws X4OPhaseException  {
				Map<Element,X4OPhase> dirtyElements = element.getElementLanguage().getDirtyElements();
				if (dirtyElements.isEmpty()) {
					return;
				}
				debugPhaseMessage("Dirty elements last: "+dirtyElements.size(), this);
				for (Element e:dirtyElements.keySet()) {
					X4OPhase p = dirtyElements.get(e);
					manager.runPhasesForElement(e, p);
				}
				element.getElementLanguage().getDirtyElements().clear();
			}
		};
		X4OPhaseHandler result = new RunDirtyElementLastPhase();
		return result;
	}
	
	/**
	 * 
	 * @return	The X4OPhaseHandler for this phase.
	 */
	public X4OPhaseHandler bindElementPhase() {
		class BindElementPhase extends AbstractX4OPhaseHandler {
			protected void setX4OPhase() {
				phase = X4OPhase.bindElementPhase;
			}
			public void runElementPhase(Element element) throws X4OPhaseException  {
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
			
				List<ElementBindingHandler> binds = element.getElementLanguage().findElementBindingHandlers(parentObject, childObject);
				logger.finest("Calling bindings handlers; "+binds.size());
				try {
					for (ElementBindingHandler binding:binds) {
						if (hasX4ODebugWriter()) {
							getX4ODebugWriter().debugElementBindingHandler(binding,element);
						}
						binding.doBind(parentObject,childObject,element);
					}
				} catch (ElementException e) {
					throw new X4OPhaseException(this,"Error while binding",e);
				}
			}
		};
		X4OPhaseHandler result = new BindElementPhase();
		return result;
	}
	
	/**
	 * 
	 * @return	The X4OPhaseHandler for this phase.
	 */
	public X4OPhaseHandler runPhase() {
		X4OPhaseHandler result = new AbstractX4OPhaseHandler() {
			protected void setX4OPhase() {
				phase = X4OPhase.runPhase;
			}
			public void runElementPhase(Element element) throws X4OPhaseException  {
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
		
		class RunConfiguratorPhaseListener implements X4OPhaseListener {
			/**
			 * @see org.x4o.xml.core.X4OPhaseListener#preRunPhase(org.x4o.xml.core.X4OPhaseHandler, org.x4o.xml.element.ElementLanguage)
			 */
			public void preRunPhase(X4OPhaseHandler phase,ElementLanguage elementLanguage) throws X4OPhaseException {
			}
			/**
			 * @see org.x4o.xml.core.X4OPhaseListener#endRunPhase(org.x4o.xml.core.X4OPhaseHandler, org.x4o.xml.element.ElementLanguage)
			 */
			public void endRunPhase(X4OPhaseHandler phase,ElementLanguage elementLanguage) throws X4OPhaseException {
				for (RunConfigurator conf:runConf) {
					try {
						if (hasX4ODebugWriter()) {
							getX4ODebugWriter().debugElementConfigurator(conf.elementConfigurator,conf.element);
						}
						conf.elementConfigurator.doConfigElement(conf.element);
					} catch (ElementException e) {
						throw new X4OPhaseException(phase,e);
					}
				}
				
			}
		}
		result.addPhaseListener(new RunConfiguratorPhaseListener());
		return result;
	}
	
	/**
	 * 
	 * @return	The X4OPhaseHandler for this phase.
	 */
	public X4OPhaseHandler releasePhase() {
		
		// for debug output 
		class ReleasePhaseListener implements X4OPhaseListener {
			private int elementsReleased = 0;
			/**
			 * @see org.x4o.xml.core.X4OPhaseListener#preRunPhase(org.x4o.xml.core.X4OPhaseHandler, org.x4o.xml.element.ElementLanguage)
			 */
			public void preRunPhase(X4OPhaseHandler phase,ElementLanguage elementLanguage) throws X4OPhaseException {
				elementsReleased=0;
			}
			/**
			 * @see org.x4o.xml.core.X4OPhaseListener#endRunPhase(org.x4o.xml.core.X4OPhaseHandler, org.x4o.xml.element.ElementLanguage)
			 */
			public void endRunPhase(X4OPhaseHandler phase,ElementLanguage elementLanguage) throws X4OPhaseException {
				if (hasX4ODebugWriter()==false) {
					return;
				}
				try {
					AttributesImpl atts = new AttributesImpl();
					atts.addAttribute ("", "elements", "", "", elementsReleased+"");
					getX4ODebugWriter().getDebugWriter().startElement (X4ODebugWriter.DEBUG_URI, "executeReleases", "", atts);
					getX4ODebugWriter().getDebugWriter().endElement (X4ODebugWriter.DEBUG_URI, "executeReleases" , "");
				} catch (SAXException e) {
					throw new X4OPhaseException(phase,e);
				}
			}
			
			public void addReleasedElement() {
				elementsReleased++;
			}
		}
		
		final ReleasePhaseListener releaseCounter = new ReleasePhaseListener();
		X4OPhaseHandler result = new AbstractX4OPhaseHandler() {
			protected void setX4OPhase() {
				phase = X4OPhase.releasePhase;
			}
			public void runPhase(ElementLanguage elementLanguage) throws X4OPhaseException {
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
	public X4OPhaseHandler debugPhase() {
		X4OPhaseHandler result = new AbstractX4OPhaseHandler() {
			protected void setX4OPhase() {
				phase = X4OPhase.debugPhase;
				
				// safety check
				if (hasX4ODebugWriter()==false) {
					throw new NullPointerException("Use debugPhase only when X4OParser.debugWriter is filled.");
				}
			}
			public boolean isElementPhase() {
				return false;
			}
			public void runElementPhase(Element element) throws X4OPhaseException {
			}
			public void runPhase(ElementLanguage elementLanguage) throws X4OPhaseException {
				try {
					AttributesImpl atts = new AttributesImpl();
					//atts.addAttribute ("", key, "", "", value);
					//atts.addAttribute ("", "verbose", "", "", "true");
					getX4ODebugWriter().getDebugWriter().startElement (X4ODebugWriter.DEBUG_URI, "printElementTree", "", atts);
					startedPrefix.clear();
					printXML(elementLanguage.getRootElement());
					for (String prefix:startedPrefix) {
						getX4ODebugWriter().getDebugWriter().endPrefixMapping(prefix);
					}
					getX4ODebugWriter().getDebugWriter().endElement(X4ODebugWriter.DEBUG_URI, "printElementTree", "");
					getX4ODebugWriter().debugElementLanguage(elementLanguage);
				} catch (SAXException e) {
					throw new X4OPhaseException(this,e);
				}
			}
			List<String> startedPrefix = new ArrayList<String>(10);
			
			// note: slow version
			private String getNamespaceForElement(Element e) {
				for (ElementLanguageModule mod:e.getElementLanguage().getElementLanguageModules()) {
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
				DefaultHandler2 handler = getX4ODebugWriter().getDebugWriter();
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
				String prefix = element.getElementLanguage().findElementNamespaceContext(nameSpace).getPrefixMapping();
				
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
