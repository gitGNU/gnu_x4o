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

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.x4o.xml.conv.ObjectConverter;
import org.x4o.xml.core.config.X4OLanguageConfiguration;
import org.x4o.xml.core.config.X4OLanguageProperty;
import org.x4o.xml.element.Element;
import org.x4o.xml.element.ElementAttributeHandler;
import org.x4o.xml.element.ElementBindingHandler;
import org.x4o.xml.element.ElementClass;
import org.x4o.xml.element.ElementClassAttribute;
import org.x4o.xml.element.ElementClassBase;
import org.x4o.xml.element.ElementConfigurator;
import org.x4o.xml.element.ElementLanguage;
import org.x4o.xml.element.ElementException;
import org.x4o.xml.element.ElementInterface;
import org.x4o.xml.element.ElementLanguageModule;
import org.x4o.xml.element.ElementNamespaceContext;
import org.x4o.xml.element.ElementNamespaceInstanceProvider;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DefaultHandler2;
import org.xml.sax.helpers.AttributesImpl;

/**
 * Helps writing the xml debug output of all stuff x4o does.
 * 
 * @author Willem Cazander
 * @version 1.0 Jan 15, 2009
 */
public class X4ODebugWriter {
	
	
	static public final String DEBUG_URI = "http://language.x4o.org/xml/ns/debug-output"; 
	
	protected DefaultHandler2 debugWriter = null;
	
	public X4ODebugWriter(DefaultHandler2 debugWriter) {
		this.debugWriter=debugWriter;
	}	

	protected DefaultHandler2 getDebugWriter() {
		return debugWriter;
	}
	
	public X4OPhaseListener createDebugX4OPhaseListener() {
		return new DebugX4OPhaseListener();
	}
	
	class DebugX4OPhaseListener implements X4OPhaseListener {

		long startTime = 0;
		
		/**
		 * @throws X4OPhaseException 
		 * @see org.x4o.xml.core.X4OPhaseListener#preRunPhase(org.x4o.xml.element.ElementLanguage)
		 */
		public void preRunPhase(X4OPhaseHandler phase,ElementLanguage elementLanguage) throws X4OPhaseException {
			startTime = System.currentTimeMillis();
			try {
				AttributesImpl atts = new AttributesImpl();
				if (elementLanguage!=null) {
					atts.addAttribute("", "language","","", elementLanguage.getLanguageConfiguration().getLanguage());
				}
				debugWriter.startElement (DEBUG_URI, "executePhase", "", atts);
			} catch (SAXException e) {
				throw new X4OPhaseException(phase,e);
			}
			debugPhase(phase);
		}
		
		public void endRunPhase(X4OPhaseHandler phase,ElementLanguage elementLanguage) throws X4OPhaseException {
			long stopTime = System.currentTimeMillis();
			try {
				AttributesImpl atts = new AttributesImpl();
				atts.addAttribute ("", "name", "", "", phase.getX4OPhase().name());
				atts.addAttribute ("", "speed", "", "", (stopTime-startTime)+" ms");
				debugWriter.startElement (DEBUG_URI, "executePhaseDone", "", atts);
				debugWriter.endElement (DEBUG_URI, "executePhaseDone" , "");
				
				debugWriter.endElement (DEBUG_URI, "executePhase" , "");
			} catch (SAXException e) {
				throw new X4OPhaseException(phase,e);
			}
		}
	}
	
	public void debugLanguageProperties(ElementLanguage ec) throws ElementException {
		try {
			AttributesImpl atts = new AttributesImpl();
			debugWriter.startElement (DEBUG_URI, "X4OLanguageProperties", "", atts);
			for (X4OLanguageProperty p:X4OLanguageProperty.values()) {
				Object value = ec.getLanguageConfiguration().getLanguageProperty(p);
				if (value==null) {
					continue;
				}
				AttributesImpl atts2 = new AttributesImpl();
				atts2.addAttribute ("", "uri", "", "", p.toUri());
				atts2.addAttribute ("", "value", "", "", value.toString());
				debugWriter.startElement (DEBUG_URI, "X4OLanguageProperty", "", atts2);
				debugWriter.endElement(DEBUG_URI, "X4OLanguageProperty", "");
			}
			debugWriter.endElement(DEBUG_URI, "X4OLanguageProperties", "");
		} catch (SAXException e) {
			throw new ElementException(e);
		}		
	}
	
	public void debugLanguageDefaultClasses(ElementLanguage ec) throws ElementException {
		try {
			AttributesImpl atts = new AttributesImpl();
			debugWriter.startElement (DEBUG_URI, "X4OLanguageDefaultClasses", "", atts);
			X4OLanguageConfiguration conf = ec.getLanguageConfiguration();

			debugLanguageDefaultClass("getDefaultElementNamespaceContext",conf.getDefaultElementNamespaceContext());
			debugLanguageDefaultClass("getDefaultElementInterface",conf.getDefaultElementInterface());
			debugLanguageDefaultClass("getDefaultElement",conf.getDefaultElement());
			debugLanguageDefaultClass("getDefaultElementClass",conf.getDefaultElementClass());
			debugLanguageDefaultClass("getDefaultElementClassAttribute",conf.getDefaultElementClassAttribute());
			debugLanguageDefaultClass("getDefaultElementLanguageModule",conf.getDefaultElementLanguageModule());
			debugLanguageDefaultClass("getDefaultElementBodyComment",conf.getDefaultElementBodyComment());
			debugLanguageDefaultClass("getDefaultElementBodyCharacters",conf.getDefaultElementBodyCharacters());
			debugLanguageDefaultClass("getDefaultElementBodyWhitespace",conf.getDefaultElementBodyWhitespace());
			debugLanguageDefaultClass("getDefaultElementNamespaceInstanceProvider",conf.getDefaultElementNamespaceInstanceProvider());
			debugLanguageDefaultClass("getDefaultElementAttributeValueParser",conf.getDefaultElementAttributeValueParser());
			debugLanguageDefaultClass("getDefaultElementObjectPropertyValue",conf.getDefaultElementObjectPropertyValue());
			debugLanguageDefaultClass("getDefaultElementAttributeHandlerComparator",conf.getDefaultElementAttributeHandlerComparator());
							
			debugWriter.endElement(DEBUG_URI, "X4OLanguageDefaultClasses", "");
		} catch (SAXException e) {
			throw new ElementException(e);
		}		
	}
	
	private void debugLanguageDefaultClass(String name,Class<?> clazz) throws SAXException {
		AttributesImpl atts = new AttributesImpl();
		atts.addAttribute ("", "name", "", "", name);
		atts.addAttribute ("", "className", "", "", clazz.getName());
		debugWriter.startElement (DEBUG_URI, "X4OLanguageDefaultClass", "", atts);
		debugWriter.endElement(DEBUG_URI, "X4OLanguageDefaultClass", "");
	}
	
	public void debugPhaseOrder(List<X4OPhaseHandler> phases) throws X4OPhaseException {
		X4OPhaseHandler phase = null;
		try {
			AttributesImpl atts = new AttributesImpl();
			debugWriter.startElement (DEBUG_URI, "phaseOrder", "", atts);
			for (X4OPhaseHandler phase2:phases) {
				phase = phase2;
				debugPhase(phase2);
			}
			debugWriter.endElement(DEBUG_URI, "phaseOrder", "");
		} catch (SAXException e) {
			// fall back...
			if (phase==null) {
				if (phases.isEmpty()) {
					throw new X4OPhaseException(null,e); /// mmmm
				}
				phase = phases.get(0);
			}
			throw new X4OPhaseException(phase,e);
		}	
	}
	
	private void debugPhase(X4OPhaseHandler phase) throws X4OPhaseException {
		try {
			AttributesImpl atts = new AttributesImpl();
			atts.addAttribute ("", "name", "", "", phase.getX4OPhase().name());
			atts.addAttribute ("", "runOnce", "", "", phase.getX4OPhase().isRunOnce()+"");
			atts.addAttribute ("", "listenersSize", "", "", phase.getX4OPhaseListeners().size()+"");
			
			debugWriter.startElement (DEBUG_URI, "phase", "", atts);
			for (X4OPhaseListener l:phase.getX4OPhaseListeners()) {
				atts = new AttributesImpl();
				atts.addAttribute ("", "className", "", "", l.getClass().getName());
				debugWriter.startElement (DEBUG_URI, "X4OPhaseListener", "", atts);
				debugWriter.endElement(DEBUG_URI, "X4OPhaseListener", "");
			}
			debugWriter.endElement(DEBUG_URI, "phase", "");
		} catch (SAXException e) {
			throw new X4OPhaseException(phase,e);
		}	
	}
	
	public void debugElementLanguageModules(ElementLanguage elementLanguage) throws ElementException {
		try {
			AttributesImpl attsEmpty = new AttributesImpl();
			debugWriter.startElement (DEBUG_URI, "ElementLanguageModules", "", attsEmpty);
			
			for (ElementLanguageModule module:elementLanguage.getElementLanguageModules()) {
				AttributesImpl atts = new AttributesImpl();
				atts.addAttribute ("", "className", "", "", module.getClass().getName());
				atts.addAttribute ("", "name", "", "", module.getName());
				atts.addAttribute ("", "providerName", "", "", module.getProviderName());
				if (module.getElementLanguageModuleLoader()==null) {
					atts.addAttribute ("", "elementLanguageModuleLoaderClassName", "", "", "null");
				} else {
					atts.addAttribute ("", "elementLanguageModuleLoaderClassName", "", "", module.getElementLanguageModuleLoader().getClass().getName());
				}
				debugWriter.startElement (DEBUG_URI, "ElementLanguageModule", "", atts);
				
				//module.getElementAttributeHandlers();
				//module.getElementBindingHandlers();
				//module.getGlobalElementConfigurators();
				//module.getElementInterfaces();
				//module.getElementNamespaceContexts();
				
				debugElementConfigurator(module.getGlobalElementConfigurators());
				debugElementBindingHandler(module.getElementBindingHandlers());
				
				for (ElementAttributeHandler p:module.getElementAttributeHandlers()) {
					atts = new AttributesImpl();
					atts.addAttribute ("", "attributeName", "", "", p.getAttributeName());
					atts.addAttribute ("", "description", "", "", p.getDescription());
					atts.addAttribute ("", "className", "", "", p.getClass().getName());
					debugWriter.startElement (DEBUG_URI, "elementAttributeHandler", "", atts);
					for (String para:p.getNextAttributes()) {
						atts = new AttributesImpl();
						atts.addAttribute ("", "attributeName", "", "", para);	
						debugWriter.startElement (DEBUG_URI, "nextAttribute", "", atts);
						debugWriter.endElement(DEBUG_URI, "nextAttribute", "");
					}
					debugWriter.endElement(DEBUG_URI, "elementAttributeHandler", "");
				}
				
				for (ElementInterface elementInterface:module.getElementInterfaces()) {
					atts = new AttributesImpl();
					atts.addAttribute ("", "className", "", "", elementInterface.getClass().getName());
					atts.addAttribute ("", "description", "", "", elementInterface.getDescription());
					atts.addAttribute ("", "interfaceClass", "", "", elementInterface.getInterfaceClass().getName());
					
					debugWriter.startElement (DEBUG_URI, "elementInterface", "", atts);
					debugElementBindingHandler(elementInterface.getElementBindingHandlers());
					debugElementClassBase(elementInterface);
					debugWriter.endElement(DEBUG_URI, "elementInterface", "");
				}
				
				for (ElementNamespaceContext enc:module.getElementNamespaceContexts()) {
					atts = new AttributesImpl();
					atts.addAttribute ("", "uri", "", "", enc.getUri());	
					atts.addAttribute ("", "description", "", "", enc.getDescription());
					atts.addAttribute ("", "schemaUri", "", "", enc.getSchemaUri());
					atts.addAttribute ("", "schemaResource", "", "", enc.getSchemaResource());
					atts.addAttribute ("", "className", "", "", enc.getClass().getName());
					
					debugWriter.startElement (DEBUG_URI, ElementNamespaceContext.class.getSimpleName(), "", atts);
					for (ElementClass ec:enc.getElementClasses()) {
						debugElementClass(ec);
					}
					
					ElementNamespaceInstanceProvider eip = enc.getElementNamespaceInstanceProvider();
					atts = new AttributesImpl();
					atts.addAttribute ("", "className", "", "", eip.getClass().getName());
					debugWriter.startElement (DEBUG_URI, ElementNamespaceInstanceProvider.class.getSimpleName(), "", atts);
					debugWriter.endElement(DEBUG_URI, ElementNamespaceInstanceProvider.class.getSimpleName(), "");
					
					debugWriter.endElement(DEBUG_URI, ElementNamespaceContext.class.getSimpleName(), "");
				}
				
				debugWriter.endElement(DEBUG_URI, "ElementLanguageModule", "");
			}
			
			debugWriter.endElement(DEBUG_URI, "ElementLanguageModules", "");
		} catch (SAXException e) {
			throw new ElementException(e);
		}
	}
	
	public void debugElement(Element element) throws ElementException {
		try {
			AttributesImpl atts = new AttributesImpl();
//			atts.addAttribute ("", "tag", "", "", element.getElementClass().getTag());
			atts.addAttribute ("", "objectClass", "", "", ""+element.getElementClass().getObjectClass());
			
			boolean rootElement = element.getParent()==null;
			if (rootElement) {
				atts.addAttribute ("", "isRootElement", "", "", "true");	
			}
			
			if (element.getElementObject()==null) {
				atts.addAttribute ("", "elementObjectClassName", "", "", "null");
			} else {
				atts.addAttribute ("", "elementObjectClassName", "", "", element.getElementObject().getClass().getName());
				
				AttributesImpl atts2 = new AttributesImpl();
				try {
					for (Method m:element.getElementObject().getClass().getMethods()) {
						if (m.getName().startsWith("get")==false) {  //a bit dirty  
							continue;
						}
						if (m.getParameterTypes().length>0) {
							continue;
						}
						Object value = m.invoke(element.getElementObject());
						if (value!=null) {
							//atts2.addAttribute ("", m.getName()+".className", "", "", value.getClass().getName());
							
							if (value instanceof String) {
								atts2.addAttribute ("", m.getName(), "", "", value.toString());	
							}
							if (value instanceof Number) {
								atts2.addAttribute ("", m.getName(), "", "", value.toString());	
							}
							if (value instanceof Collection<?>) {
								atts2.addAttribute ("", m.getName()+".size", "", "", ""+((Collection<?>)value).size());	
							}
							if (value instanceof Map<?,?>) {
								atts2.addAttribute ("", m.getName()+".size", "", "", ""+((Map<?,?>)value).size());	
							}
						}
					}
				} catch (Exception e) {
					atts.addAttribute ("", "exceptionWhileGetingBeanValues", "", "", e.getMessage());
				}
				
				debugWriter.startElement (DEBUG_URI, "elementObject", "", atts2);
				debugWriter.endElement(DEBUG_URI, "elementObject", "");
			}
			
			StringBuffer elementPath = getElementPath(element,new StringBuffer());
			atts.addAttribute ("", "elementPath", "", "", elementPath.toString());
			
			debugWriter.startElement (DEBUG_URI, "element", "", atts);
			debugWriter.endElement(DEBUG_URI, "element", "");
		} catch (SAXException e) {
			throw new ElementException(e);
		}	
	}
	
	/**
	 * Todo move after xpath support
	 * @param element
	 * @param buff
	 * @return
	 */
	private StringBuffer getElementPath(Element element,StringBuffer buff) {
		if (element.getParent()==null) {
			buff.append('/'); // root slash
			buff.append(element.getElementClass().getTag());	
			return buff;
		}
		buff = getElementPath(element.getParent(),buff);
		buff.append('/');
		buff.append(element.getElementClass().getTag());
		return buff;
	}
	
	public void debugPhaseMessage(String message,Class<?> clazz) throws ElementException  {	
		AttributesImpl atts = new AttributesImpl();
		atts.addAttribute ("", "class", "", "", clazz.getName()+"");
		try {
			debugWriter.startElement (DEBUG_URI, "message", "", atts);
			char[] msg = message.toCharArray();
			debugWriter.characters(msg,0,msg.length);
			debugWriter.endElement(DEBUG_URI, "message", "");
		} catch (SAXException e) {
			throw new ElementException(e);
		}
	}
	
	public void debugElementConfigurator(ElementConfigurator ec,Element element) throws ElementException  {
		try {
			AttributesImpl atts = new AttributesImpl();
			//atts.addAttribute ("", key, "", "", value);
			atts.addAttribute ("", "configAction", "", "", ec.isConfigAction()+"");
			atts.addAttribute ("", "description", "", "", ec.getDescription());
			atts.addAttribute ("", "className", "", "", ec.getClass().getName());
			
			debugWriter.startElement (DEBUG_URI, "runElementConfigurator", "", atts);
			debugElement(element);
			debugWriter.endElement(DEBUG_URI, "runElementConfigurator", "");
		} catch (SAXException e) {
			throw new ElementException(e);
		}
	}
	
	public void debugElementBindingHandler(ElementBindingHandler ebh,Element element) throws ElementException  {
		try {
			AttributesImpl atts = new AttributesImpl();
			//atts.addAttribute ("", key, "", "", value);
			atts.addAttribute ("", "description", "", "", ebh.getDescription());
			atts.addAttribute ("", "className", "", "", ebh.getClass().getName()+"");
			
			atts.addAttribute ("", "parentClass", "", "", element.getParent().getElementObject().getClass()+"");
			atts.addAttribute ("", "childClass", "", "", element.getElementObject().getClass()+"");

			debugWriter.startElement (DEBUG_URI, "doBind", "", atts);
			debugElement(element);
			debugWriter.endElement(DEBUG_URI, "doBind", "");
		} catch (SAXException e) {
			throw new ElementException(e);
		}
	}
	
	public void debugElementLanguage(ElementLanguage elementLanguage) throws SAXException {
		AttributesImpl atts = new AttributesImpl();
		//atts.addAttribute ("", key, "", "", value);
		atts.addAttribute ("", "language", "", "", elementLanguage.getLanguageConfiguration().getLanguage());
		atts.addAttribute ("", "languageVersion", "", "", elementLanguage.getLanguageConfiguration().getLanguageVersion());
		atts.addAttribute ("", "className", "", "", elementLanguage.getClass().getName()+"");
		atts.addAttribute ("", "currentX4OPhase", "", "", elementLanguage.getCurrentX4OPhase().name());
		debugWriter.startElement (DEBUG_URI, "printElementLanguage", "", atts);
		debugWriter.endElement(DEBUG_URI, "printElementLanguage", "");
	}
	
	private void debugElementClass(ElementClass elementClass) throws SAXException {
		AttributesImpl atts = new AttributesImpl();
		atts.addAttribute ("", "tag", "", "", elementClass.getTag());
		atts.addAttribute ("", "description", "", "", elementClass.getDescription());
		atts.addAttribute ("", "objectClassName", "", "", ""+elementClass.getObjectClass());
		atts.addAttribute ("", "className", "", "", elementClass.getClass().getName());
		debugWriter.startElement (DEBUG_URI, "elementClass", "", atts);
		for (String phase:elementClass.getSkipPhases()) {
			atts = new AttributesImpl();
			atts.addAttribute ("", "phase", "", "", ""+phase);
			debugWriter.startElement(DEBUG_URI, "elementSkipPhase", "", atts);
			debugWriter.endElement(DEBUG_URI, "elementSkipPhase", "");
		}
		debugElementConfigurator(elementClass.getElementConfigurators());
		debugElementClassBase(elementClass);
		debugWriter.endElement(DEBUG_URI, "elementClass", "");
	}
	
	private void debugElementClassBase(ElementClassBase elementClassBase) throws SAXException {
		AttributesImpl atts = new AttributesImpl();
		atts.addAttribute ("", "description", "", "", elementClassBase.getDescription());
		atts.addAttribute ("", "className", "", "", elementClassBase.getClass().getName());
		debugWriter.startElement (DEBUG_URI, "elementClassBase", "", atts);
		debugElementConfigurator(elementClassBase.getElementConfigurators());
		debugElementClassAttributes(elementClassBase.getElementClassAttributes());
		debugWriter.endElement(DEBUG_URI, "elementClassBase", "");
	}
	
	private void debugElementConfigurator(List<ElementConfigurator> elementConfigurators) throws SAXException {
		for (ElementConfigurator elementConfigurator:elementConfigurators) {
			AttributesImpl atts = new AttributesImpl();
			atts.addAttribute ("", "description", "", "", elementConfigurator.getDescription());
			atts.addAttribute ("", "className", "", "", elementConfigurator.getClass().getName());
			debugWriter.startElement (DEBUG_URI, "elementConfigurator", "", atts);
			debugWriter.endElement(DEBUG_URI, "elementConfigurator", "");
		}	
	}
	
	private void debugElementClassAttributes(Collection<ElementClassAttribute> elementClassAttributes) throws SAXException {
		for (ElementClassAttribute elementClassAttribute:elementClassAttributes) {
			AttributesImpl atts = new AttributesImpl();
			atts.addAttribute ("", "attributeName", "", "", elementClassAttribute.getName());
			atts.addAttribute ("", "description", "", "", elementClassAttribute.getDescription());
			atts.addAttribute ("", "className", "", "", elementClassAttribute.getClass().getName());
			atts.addAttribute ("", "defaultValue", "", "", ""+elementClassAttribute.getDefaultValue());
			atts.addAttribute ("", "runBeanFill", "", "", ""+elementClassAttribute.getRunBeanFill());
			atts.addAttribute ("", "runConverters", "", "", ""+elementClassAttribute.getRunConverters());
			//atts.addAttribute ("", "runInterfaces", "", "", ""+elementClassAttribute.getRunInterfaces());
			atts.addAttribute ("", "runResolveEL", "", "", ""+elementClassAttribute.getRunResolveEL());
			debugWriter.startElement(DEBUG_URI, "elementClassAttribute", "", atts);
			if (elementClassAttribute.getObjectConverter()!=null) {
				debugObjectConverter(elementClassAttribute.getObjectConverter());
			}
			for (String alias:elementClassAttribute.getAttributeAliases()) {
				atts = new AttributesImpl();
				atts.addAttribute ("", "name", "", "", ""+alias);
				debugWriter.startElement(DEBUG_URI, "attributeAlias", "", atts);
				debugWriter.endElement(DEBUG_URI, "attributeAlias", "");
			}
			debugWriter.endElement(DEBUG_URI, "elementClassAttribute", "");
		}	
	}
	
	private void debugObjectConverter(ObjectConverter objectConverter) throws SAXException {
		AttributesImpl atts = new AttributesImpl();
		atts.addAttribute ("", "objectClassTo", "", "", objectConverter.getObjectClassTo().getName());
		atts.addAttribute ("", "objectClassBack", "", "", objectConverter.getObjectClassBack().getName());
		atts.addAttribute ("", "className", "", "", objectConverter.getClass().getName());
		debugWriter.startElement (DEBUG_URI, "objectConverter", "", atts);
		debugWriter.endElement(DEBUG_URI, "objectConverter", "");
	}
	
	private void debugElementBindingHandler(List<ElementBindingHandler> elementBindingHandlers) throws SAXException {
		for (ElementBindingHandler bind:elementBindingHandlers) {
			AttributesImpl atts = new AttributesImpl();
			atts.addAttribute ("", "className", "", "", bind.getClass().getName());
			atts.addAttribute ("", "description", "", "", bind.getDescription());
			atts.addAttribute ("", "bindParentClass", "", "", bind.getBindParentClass().toString());
			debugWriter.startElement (DEBUG_URI, "elementBindingHandler", "", atts);

			for (Class<?> clazz:bind.getBindChildClasses()) {
				AttributesImpl atts2 = new AttributesImpl();
				atts2.addAttribute ("", "className", "", "", clazz.getName());
				debugWriter.startElement (DEBUG_URI, "elementBindingHandlerChildClass", "", atts2);
				debugWriter.endElement (DEBUG_URI, "elementBindingHandlerChildClass", "");
			}
			
			debugWriter.endElement(DEBUG_URI, "elementBindingHandler", "");
		}
	}
}
