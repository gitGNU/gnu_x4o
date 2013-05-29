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
package	org.x4o.xml.lang.phase;

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
import org.x4o.xml.io.sax.X4OContentParser;
import org.x4o.xml.io.sax.X4ODebugWriter;
import org.x4o.xml.io.sax.ext.ContentWriter;
import org.x4o.xml.lang.X4OLanguageModule;
import org.x4o.xml.lang.X4OLanguageContext;
import org.x4o.xml.lang.X4OLanguageClassLoader;
import org.x4o.xml.lang.X4OLanguageProperty;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * Factory which can create X4OPhaseHandlers for all the predefined phases used in default x4o language parsing.
 * 
 * @author Willem Cazander
 * @version 1.0 Dec 31, 2008
 */
public class X4OPhaseLanguageRead {

	private Logger logger = null;
	
	public X4OPhaseLanguageRead() {
		logger = Logger.getLogger(X4OPhaseLanguageRead.class.getName());
	}
	

	public void createPhases(DefaultX4OPhaseManager manager) {
		manager.addX4OPhase(new X4OPhaseReadStart());
		manager.addX4OPhase(new X4OPhaseReadXml());
		
		manager.addX4OPhase(new X4OPhaseReadConfigELBeans());
//		if (languageContext.hasX4ODebugWriter()) {manager.addX4OPhaseHandler(factory.debugPhase());}
		
		// meta start point
//		manager.addX4OPhase(factory.startX4OPhase());
//		if (languageContext.hasX4ODebugWriter()) {manager.addX4OPhaseHandler(factory.debugPhase());}
		
		// config
		X4OPhaseReadRunConfigurator runConf = new X4OPhaseReadRunConfigurator();
		manager.addX4OPhase(new X4OPhaseReadConfigElement(runConf));
		manager.addX4OPhase(new X4OPhaseReadConfigElementInterface(runConf));
		manager.addX4OPhase(new X4OPhaseReadConfigGlobalElement(runConf));
		manager.addX4OPhase(new X4OPhaseReadConfigGlobalAttribute(runConf));
		
		// run all attribute events
		manager.addX4OPhase(new X4OPhaseReadRunAttribute());
//		if (languageContext.hasX4ODebugWriter()) {manager.addX4OPhaseHandler(factory.debugPhase());}
		// templating
		manager.addX4OPhase(new X4OPhaseReadFillTemplate());
//		if (languageContext.hasX4ODebugWriter()) {manager.addX4OPhaseHandler(factory.debugPhase());}

		// transforming
		manager.addX4OPhase(new X4OPhaseReadTransform());
		manager.addX4OPhase(new X4OPhaseReadRunDirty(manager));
//		if (languageContext.hasX4ODebugWriter()) {manager.addX4OPhaseHandler(factory.debugPhase());}
		
		// binding elements
		manager.addX4OPhase(new X4OPhaseReadBindElement());
		
		// runing and releasing
		manager.addX4OPhase(new X4OPhaseReadRun());
//		if (languageContext.hasX4ODebugWriter()) {manager.addX4OPhaseHandler(factory.debugPhase());}
		
		manager.addX4OPhase(runConf);
		
		manager.addX4OPhase(new X4OPhaseReadRunDirtyLast(manager));
//		if (languageContext.hasX4ODebugWriter()) {manager.addX4OPhaseHandler(factory.debugPhase());}
		
		
		manager.addX4OPhase(new X4OPhaseReadEnd());
		
		// after release phase Element Tree is not avible anymore
		manager.addX4OPhase(releasePhase());
		
		// Add debug phase listener to all phases
//		if (languageContext.hasX4ODebugWriter()) {
			//for (X4OPhase h:manager.getOrderedPhases()) {
//				h.addPhaseListener(languageContext.getX4ODebugWriter().createDebugX4OPhaseListener());
			//}
		//}
		
		// We are done
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
	 * Creates the startX4OPhase which is a empty meta phase.
	 */
	class X4OPhaseReadStart extends AbstractX4OPhase {
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
		public void runPhase(X4OLanguageContext languageContext) throws X4OPhaseException {
			// print the properties and classes for this language/config
			if (languageContext.hasX4ODebugWriter()) {
				try {
					languageContext.getX4ODebugWriter().debugLanguageProperties(languageContext);
					languageContext.getX4ODebugWriter().debugLanguageDefaultClasses(languageContext);
				} catch (ElementException e) {
					throw new X4OPhaseException(this,e);
				}
			}
		}
	};
	
	/**
	 * Parses the xml resource(s) and creates an Element tree.
	 */
	class X4OPhaseReadXml extends AbstractX4OPhase {
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
		public void runPhase(X4OLanguageContext languageContext) throws X4OPhaseException {
			try {
				X4OContentParser parser = new X4OContentParser();
				parser.parse(languageContext);
			} catch (Exception e) {
				throw new X4OPhaseException(this,e);
			}
		}
	};
	
	/**
	 * Creates the configGlobalElBeansPhase which adds beans to the el context.
	 */
	class X4OPhaseReadConfigELBeans extends AbstractX4OPhase {
		public X4OPhaseType getType() {
			return X4OPhaseType.XML_RW;
		}
		public String getId() {
			return "READ_CONFIG_EL_BEANS";
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
		public void runPhase(X4OLanguageContext languageContext) throws X4OPhaseException {
			try {
				Map beanMap = (Map)languageContext.getLanguageProperty(X4OLanguageProperty.EL_BEAN_INSTANCE_MAP);
				if (beanMap==null) {
					return;
				}
				for (Object elName:beanMap.keySet()) {
					Object o = beanMap.get(elName);
					ValueExpression ve = languageContext.getExpressionLanguageFactory().createValueExpression(languageContext.getExpressionLanguageContext(),"${"+elName+"}", o.getClass());
					ve.setValue(languageContext.getExpressionLanguageContext(), o);
					debugPhaseMessage("Setting el bean: ${"+elName+"} to: "+o.getClass().getName(),this,languageContext);
				}
			} catch (Exception e) {
				throw new X4OPhaseException(this,e);
			}
		}
	};
	
	/**
	 * X4OPhaseReadConfigElement
	 */
	class X4OPhaseReadConfigElement extends AbstractX4OPhase {
		private X4OPhaseReadRunConfigurator runConf = null;
		public X4OPhaseReadConfigElement(X4OPhaseReadRunConfigurator runConf) {
			this.runConf=runConf;
		}
		public X4OPhaseType getType() {
			return X4OPhaseType.XML_READ;
		}
		public String getId() {
			return "READ_CONFIG_ELEMENT";
		}
		public String[] getPhaseDependencies() {
			return new String[] {"READ_CONFIG_EL_BEANS"};
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
						element.getAttributes().put(eca.getId(), attributeValue);
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
	
	/**
	 * X4OPhaseReadConfigElementInterface
	 */
	class X4OPhaseReadConfigElementInterface extends AbstractX4OPhase {
		private X4OPhaseReadRunConfigurator runConf = null;
		public X4OPhaseReadConfigElementInterface(X4OPhaseReadRunConfigurator runConf) {
			this.runConf=runConf;
		}
		public X4OPhaseType getType() {
			return X4OPhaseType.XML_READ;
		}
		public String getId() {
			return "READ_CONFIG_ELEMENT_INTERFACE";
		}
		public String[] getPhaseDependencies() {
			return new String[] {"READ_CONFIG_EL_BEANS"};
		}
		public void runElementPhase(Element element) throws X4OPhaseException {
			if (element.getElementObject()==null) {
				logger.finest("Null elementObject skipping, interfaces");
				return;
			}
			for (ElementInterface ei:element.getLanguageContext().getLanguage().findElementInterfaces(element.getElementObject())) {
				logger.finest("Do ElementInterface Config Configurators: "+ei.getElementConfigurators().size());
				for (ElementConfigurator ec:ei.getElementConfigurators()) {
					runConf.runElementConfigurator(ec,element,this);
				}
			}
		}
	};
	
	/**
	 * X4OPhaseReadConfigGlobalElement
	 */
	class X4OPhaseReadConfigGlobalElement extends AbstractX4OPhase {
		private X4OPhaseReadRunConfigurator runConf = null;
		public X4OPhaseReadConfigGlobalElement(X4OPhaseReadRunConfigurator runConf) {
			this.runConf=runConf;
		}
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
			for (X4OLanguageModule mod:element.getLanguageContext().getLanguage().getLanguageModules()) {
				logger.finest("Do Element Config Global Configurators: "+mod.getElementConfiguratorGlobals().size());
				for (ElementConfiguratorGlobal ec:mod.getElementConfiguratorGlobals()) {
					runConf.runElementConfigurator(ec,element,this);
				}
			}
		}
	};
	
	/**
	 * X4OPhaseReadConfigGlobalAttribute
	 */
	class X4OPhaseReadConfigGlobalAttribute extends AbstractX4OPhase {
		Comparator<ElementAttributeHandler> elementAttributeHandlerComparator = null;
		private X4OPhaseReadRunConfigurator runConf = null;
		public X4OPhaseReadConfigGlobalAttribute(X4OPhaseReadRunConfigurator runConf) {
			this.runConf=runConf;
		}
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
					elementAttributeHandlerComparator = (Comparator<ElementAttributeHandler>)X4OLanguageClassLoader.newInstance(element.getLanguageContext().getLanguage().getLanguageConfiguration().getDefaultElementAttributeHandlerComparator());
				} catch (Exception e) {
					throw new X4OPhaseException(this,e);
				}
			}
			
			// do global parameters
			logger.finest("Do Element Global AttributeHandlers.");
			List<ElementAttributeHandler> handlers = new ArrayList<ElementAttributeHandler>();
			for (X4OLanguageModule mod:element.getLanguageContext().getLanguage().getLanguageModules()) {
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
	
	/**
	 * X4OPhaseReadRunAttribute
	 */
	class X4OPhaseReadRunAttribute extends AbstractX4OPhase {
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
			ElementAttributeValueParser attrParser = element.getLanguageContext().getElementAttributeValueParser();
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
						if (attrClass.getRunBeanValue()==null || attrClass.getRunBeanValue()) {
							element.getLanguageContext().getElementObjectPropertyValue().setProperty(element.getElementObject(), name, value);
						}
					} else if (autoAttributes) {
						
						value = attrParser.getParameterValue(name,valueString,element);
						element.getLanguageContext().getElementObjectPropertyValue().setProperty(element.getElementObject(), name, value);
						
					} else {
						continue; // skip all non auto attribute non attribute defined xml attributes, or throw exception ?
					}
				}
				// check for defaults
				for (ElementClassAttribute attrClass:element.getElementClass().getElementClassAttributes()) {
					if (attrClass.getDefaultValue()!=null && attr.containsKey(attrClass.getId())==false) {
						Object value = null;
						if (attrClass.getDefaultValue() instanceof String) {
							value = attrParser.getParameterValue(attrClass.getId(),(String)attrClass.getDefaultValue(),element);
						} else {
							value = attrClass.getDefaultValue();
						}
						if (attrClass.getRunBeanValue()==null || attrClass.getRunBeanValue()) {
							element.getLanguageContext().getElementObjectPropertyValue().setProperty(element.getElementObject(), attrClass.getId(), value);
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
	
	/**
	 * X4OPhaseReadFillTemplate
	 */
	class X4OPhaseReadFillTemplate extends AbstractX4OPhase {
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
	
	/**
	 * X4OPhaseReadTransform
	 */
	class X4OPhaseReadTransform extends AbstractX4OPhase {
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
				if (element.getLanguageContext().hasX4ODebugWriter()) {
					element.getLanguageContext().getX4ODebugWriter().debugElement(element);
				}
				element.doElementRun();
			} catch (ElementException e) {
				throw new X4OPhaseException(this,e);
			}
		}
	};
	
	/**
	 * X4OPhaseReadRunDirty
	 */
	class X4OPhaseReadRunDirty extends AbstractX4OPhase {
		private X4OPhaseManager phaseManager = null;
		public X4OPhaseReadRunDirty(X4OPhaseManager phaseManager) {
			this.phaseManager=phaseManager;
		}
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
			Map<Element,X4OPhase> dirtyElements = element.getLanguageContext().getDirtyElements();
			if (dirtyElements.isEmpty()) {
				return;
			}
			debugPhaseMessage("Dirty elements: "+dirtyElements.size(), this,element.getLanguageContext());
			for (Element e:dirtyElements.keySet()) {
				X4OPhase p = dirtyElements.get(e);
				phaseManager.runPhasesForElement(e,getType(), p);
			}
			element.getLanguageContext().getDirtyElements().clear();
		}
	};

	/**
	 * X4OPhaseReadRunDirtyLast
	 */
	class X4OPhaseReadRunDirtyLast extends AbstractX4OPhase {
		private X4OPhaseManager phaseManager = null;
		public X4OPhaseReadRunDirtyLast(X4OPhaseManager phaseManager) {
			this.phaseManager=phaseManager;
		}
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
			Map<Element,X4OPhase> dirtyElements = element.getLanguageContext().getDirtyElements();
			if (dirtyElements.isEmpty()) {
				return;
			}
			debugPhaseMessage("Dirty elements last: "+dirtyElements.size(), this,element.getLanguageContext());
			for (Element e:dirtyElements.keySet()) {
				X4OPhase p = dirtyElements.get(e);
				phaseManager.runPhasesForElement(e,getType(), p);
			}
			element.getLanguageContext().getDirtyElements().clear();
		}
	};
	
	/**
	 * X4OPhaseReadBindElement
	 */
	class X4OPhaseReadBindElement extends AbstractX4OPhase {
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
		
			List<ElementBindingHandler> binds = element.getLanguageContext().getLanguage().findElementBindingHandlers(parentObject, childObject);
			logger.finest("Calling bindings handlers; "+binds.size());
			try {
				for (ElementBindingHandler binding:binds) {
					if (element.getLanguageContext().hasX4ODebugWriter()) {
						element.getLanguageContext().getX4ODebugWriter().debugElementBindingHandler(binding,element);
					}
					binding.bindChild(element);
				}
			} catch (ElementException e) {
				throw new X4OPhaseException(this,"Error while binding",e);
			}
		}
	};
	
	/**
	 * X4OPhaseReadRun
	 */
	class X4OPhaseReadRun extends AbstractX4OPhase {
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

	class X4OPhaseReadRunConfigurator extends AbstractX4OPhase {
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
		public void runPhase(X4OLanguageContext languageContext) throws X4OPhaseException {
			for (RunConfigurator conf:runConf) {
				try {
					if (languageContext.hasX4ODebugWriter()) {
						languageContext.getX4ODebugWriter().debugElementConfigurator(conf.elementConfigurator,conf.element);
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
				if (e.getLanguageContext().hasX4ODebugWriter()) {
					e.getLanguageContext().getX4ODebugWriter().debugElementConfigurator(ec,e);
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
	 * X4OPhaseReadEnd Is the read end phase.
	 */
	class X4OPhaseReadEnd extends AbstractX4OPhase {
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
		public void runPhase(X4OLanguageContext languageContext) throws X4OPhaseException {
			// print the properties and classes for this language/config
			if (languageContext.hasX4ODebugWriter()) {
				try {
					languageContext.getX4ODebugWriter().debugLanguageProperties(languageContext);
					languageContext.getX4ODebugWriter().debugLanguageDefaultClasses(languageContext);
				} catch (ElementException e) {
					throw new X4OPhaseException(this,e);
				}
			}
		}
	};
	
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
			public void preRunPhase(X4OPhase phase,X4OLanguageContext languageContext) throws X4OPhaseException {
				elementsReleased=0;
			}
			/**
			 * @see org.x4o.xml.lang.phase.X4OPhaseListener#endRunPhase(org.x4o.xml.lang.phase.X4OPhase, org.x4o.xml.lang.X4OLanguageContext)
			 */
			public void endRunPhase(X4OPhase phase,X4OLanguageContext languageContext) throws X4OPhaseException {
				if (languageContext.hasX4ODebugWriter()==false) {
					return;
				}
				try {
					AttributesImpl atts = new AttributesImpl();
					atts.addAttribute ("", "elements", "", "", elementsReleased+"");
					languageContext.getX4ODebugWriter().getContentWriter().startElement (X4ODebugWriter.DEBUG_URI, "executeReleases", "", atts);
					languageContext.getX4ODebugWriter().getContentWriter().endElement (X4ODebugWriter.DEBUG_URI, "executeReleases" , "");
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
			public void runPhase(X4OLanguageContext languageContext) throws X4OPhaseException {
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
			public void runPhase(X4OLanguageContext languageContext) throws X4OPhaseException {
				// safety check
				if (languageContext.hasX4ODebugWriter()==false) {
					throw new X4OPhaseException(this,"Use debugPhase only when X4OParser.debugWriter is filled.");
				}
				try {
					AttributesImpl atts = new AttributesImpl();
					//atts.addAttribute ("", key, "", "", value);
					//atts.addAttribute ("", "verbose", "", "", "true");
					languageContext.getX4ODebugWriter().getContentWriter().startElement (X4ODebugWriter.DEBUG_URI, "printElementTree", "", atts);
					startedPrefix.clear();
					printXML(languageContext.getRootElement());
					for (String prefix:startedPrefix) {
						languageContext.getX4ODebugWriter().getContentWriter().endPrefixMapping(prefix);
					}
					languageContext.getX4ODebugWriter().getContentWriter().endElement(X4ODebugWriter.DEBUG_URI, "printElementTree", "");
					languageContext.getX4ODebugWriter().debugLanguageContext(languageContext);
				} catch (SAXException e) {
					throw new X4OPhaseException(this,e);
				}
			}
			List<String> startedPrefix = new ArrayList<String>(10);
			
			// note: slow version
			private String getNamespaceForElement(Element e) {
				for (X4OLanguageModule mod:e.getLanguageContext().getLanguage().getLanguageModules()) {
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
				ContentWriter handler = element.getLanguageContext().getX4ODebugWriter().getContentWriter();
				if (element.getElementType().equals(Element.ElementType.comment)) {
					handler.comment((String)element.getElementObject());
					return;
				}
				if (element.getElementType().equals(Element.ElementType.characters)) {
					handler.characters((String)element.getElementObject());
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
				String prefix = element.getLanguageContext().getLanguage().findElementNamespaceContext(nameSpace).getPrefixMapping();
				
				if (startedPrefix.contains(prefix)==false) {
					handler.startPrefixMapping(prefix, nameSpace);
					startedPrefix.add(prefix);
				}
				handler.startElement (nameSpace, element.getElementClass().getId(), "", atts);
				for (Element e:element.getAllChilderen()) {
					printXML(e);
				}
				handler.endElement (nameSpace, element.getElementClass().getId(), "");
			}
		};
		return result;
	}
}
