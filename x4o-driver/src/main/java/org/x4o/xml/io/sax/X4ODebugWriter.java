/*
 * Copyright (c) 2004-2014, Willem Cazander
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
package	org.x4o.xml.io.sax;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.x4o.xml.conv.ObjectConverter;
import org.x4o.xml.element.Element;
import org.x4o.xml.element.ElementNamespaceAttribute;
import org.x4o.xml.element.ElementBindingHandler;
import org.x4o.xml.element.ElementClass;
import org.x4o.xml.element.ElementClassAttribute;
import org.x4o.xml.element.ElementClassBase;
import org.x4o.xml.element.ElementConfigurator;
import org.x4o.xml.element.ElementConfiguratorGlobal;
import org.x4o.xml.element.ElementException;
import org.x4o.xml.element.ElementInterface;
import org.x4o.xml.element.ElementNamespace;
import org.x4o.xml.element.ElementNamespaceInstanceProvider;
import org.x4o.xml.io.X4OConnection;
import org.x4o.xml.io.XMLConstants;
import org.x4o.xml.io.sax.ext.ContentWriter;
import org.x4o.xml.io.sax.ext.ContentWriterTagWrapper;
import org.x4o.xml.lang.X4OLanguageModule;
import org.x4o.xml.lang.X4OLanguageModuleLoaderResult;
import org.x4o.xml.lang.X4OLanguageSession;
import org.x4o.xml.lang.X4OLanguageConfiguration;
import org.x4o.xml.lang.phase.X4OPhase;
import org.x4o.xml.lang.phase.X4OPhaseException;
import org.x4o.xml.lang.phase.X4OPhaseListener;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * Helps writing the xml debug output of all stuff x4o does.
 * 
 * @author Willem Cazander
 * @version 1.0 Jan 15, 2009
 */
public class X4ODebugWriter {
	
	static public final String DEBUG_URI = "http://language.x4o.org/xml/ns/debug-output"; 
	
	protected ContentWriterTagWrapper<Tag,ContentWriter> contentWriter = null;
	
	public X4ODebugWriter(ContentWriter debugWriter) {
		this.contentWriter=new ContentWriterTagWrapper<Tag,ContentWriter>(debugWriter,DEBUG_URI,XMLConstants.NULL_NS_URI);
	}	
	
	public ContentWriter getContentWriter() {
		return contentWriter.getContentWriterWrapped();
	}
	
	// TODO: rename most debug tags
	enum Tag {
		executePhase,
		executePhaseDone,
		
		X4OConnection,
		X4OLanguageSession,
		X4OLanguageSessionSkipPhase,
		X4OConnectionProperties,
		X4OConnectionProperty,
		
		X4OLanguageDefaultClasses,
		X4OLanguageDefaultClass,
		
		phaseOrder,
		phase,
		X4OPhaseListener,
		X4OPhaseDependency,
		
		ElementLanguageModules,
		ElementLanguageModule,
		ElementLanguageModuleResult,
		elementInterface,
		
		elementNamespace,
		elementNamespaceAttribute,
		nextAttribute,
		ElementNamespaceInstanceProvider,
		
		elementObject,
		element,
		
		elementClassAttribute,
		attributeAlias,
		
		SAXConfig,
		SAXConfigProperty,
		message,
		
		runElementConfigurator,
		doBind,
		
		elementClass,
		elementSkipPhase,
		
		elementClassBase,
		elementConfigurator,
		elementConfiguratorGlobal,
		
		
		objectConverter,
		
		elementBindingHandler,
		elementBindingHandlerChildClass,
		
		printElementTree,
		
		end
	}
	
	public X4OPhaseListener createDebugX4OPhaseListener() {
		return new DebugX4OPhaseListener();
	}
	
	class DebugX4OPhaseListener implements X4OPhaseListener {
		long startTime = 0;
		
		/**
		 * @throws X4OPhaseException 
		 * @see org.x4o.xml.lang.phase.X4OPhaseListener#preRunPhase(org.x4o.xml.lang.X4OLanguageSession)
		 */
		public void preRunPhase(X4OPhase phase,X4OLanguageSession elementLanguage) throws X4OPhaseException {
			startTime = System.currentTimeMillis();
			try {
				AttributesImpl atts = new AttributesImpl();
				atts.addAttribute("", "phaseId","","", phase.getId());
				if (elementLanguage!=null) {
					atts.addAttribute("", "language","","", elementLanguage.getLanguage().getLanguageName());
				}
				contentWriter.printTagStart (Tag.executePhase, atts);
			} catch (SAXException e) {
				throw new X4OPhaseException(phase,e);
			}
		}
		
		public void endRunPhase(X4OPhase phase,X4OLanguageSession elementLanguage) throws X4OPhaseException {
			long stopTime = System.currentTimeMillis();
			try {
				AttributesImpl atts = new AttributesImpl();
				atts.addAttribute ("", "phaseId", "", "", phase.getId());
				atts.addAttribute ("", "time", "", "", (stopTime-startTime)+"");
				atts.addAttribute ("", "timeUnit", "", "", "ms");
				contentWriter.printTagStartEnd (Tag.executePhaseDone, atts);
				
				contentWriter.printTagEnd (Tag.executePhase);
			} catch (SAXException e) {
				throw new X4OPhaseException(phase,e);
			}
		}
	}
	
	private String convertCharToHex(String newline) {
		StringBuilder buf = new StringBuilder();
		buf.append("0x");
		for (char c:newline.toCharArray()) {
			Integer i = new Integer(c);
			if (i<16) {
				buf.append('0');
			}
			buf.append(Integer.toHexString(i).toUpperCase());
		}
		return buf.toString();
	}
	
	public void debugConnectionStart(X4OLanguageSession languageSession,X4OConnection ec) throws SAXException {
		AttributesImpl atts = new AttributesImpl();
		atts.addAttribute ("", "language", "", "", languageSession.getLanguage().getLanguageName());
		atts.addAttribute ("", "languageVersion", "", "", languageSession.getLanguage().getLanguageVersion());
		atts.addAttribute ("", "className", "", "", ec.getClass().getName());
		atts.addAttribute ("", "currentTimeMillis", "", "", System.currentTimeMillis()+"");
		contentWriter.printTagStart(Tag.X4OConnection, atts);
		
		atts = new AttributesImpl();
		atts.addAttribute ("", "phaseStop", "", "", languageSession.getPhaseStop());
		atts.addAttribute ("", "className", "", "", languageSession.getClass().getName());
		contentWriter.printTagStart (Tag.X4OLanguageSession, atts);
		for (String skipPhase:languageSession.getPhaseSkip()) {
			contentWriter.printTagCharacters(Tag.X4OLanguageSessionSkipPhase, skipPhase);
		}
		contentWriter.printTagEnd(Tag.X4OLanguageSession);
		
		atts = new AttributesImpl();
		contentWriter.printTagStart (Tag.X4OConnectionProperties, atts);
		for (String key:ec.getPropertyKeys()) {
			Object value = ec.getProperty(key);
			AttributesImpl atts2 = new AttributesImpl();
			atts2.addAttribute ("", "key", "", "", key);
			if (value==null) {
				atts2.addAttribute ("", "valueIsNull", "", "", "true");
			} else {
				if (key.endsWith("char-newline") | key.endsWith("char-tab")) {
					value = convertCharToHex(value.toString());
				}
				atts2.addAttribute ("", "value", "", "", value.toString());
			}
			contentWriter.printTagStartEnd (Tag.X4OConnectionProperty, atts2);
		}
		contentWriter.printTagEnd (Tag.X4OConnectionProperties);
	}
	
	
	public void debugConnectionEnd() throws SAXException {
		contentWriter.printTagEnd (Tag.X4OConnection);
	}
	
	public void debugLanguageDefaultClasses(X4OLanguageSession ec) throws ElementException {
		try {
			AttributesImpl atts = new AttributesImpl();
			contentWriter.printTagStart (Tag.X4OLanguageDefaultClasses,atts);
			X4OLanguageConfiguration conf = ec.getLanguage().getLanguageConfiguration();
			
			debugLanguageDefaultClass("defaultElementNamespace",conf.getDefaultElementNamespace());
			debugLanguageDefaultClass("defaultElementInterface",conf.getDefaultElementInterface());
			debugLanguageDefaultClass("defaultElement",conf.getDefaultElement());
			debugLanguageDefaultClass("defaultElementClass",conf.getDefaultElementClass());
			debugLanguageDefaultClass("defaultElementClassAttribute",conf.getDefaultElementClassAttribute());
			debugLanguageDefaultClass("defaultElementLanguageModule",conf.getDefaultElementLanguageModule());
			debugLanguageDefaultClass("defaultElementBodyComment",conf.getDefaultElementBodyComment());
			debugLanguageDefaultClass("defaultElementBodyCharacters",conf.getDefaultElementBodyCharacters());
			debugLanguageDefaultClass("defaultElementBodyWhitespace",conf.getDefaultElementBodyWhitespace());
			debugLanguageDefaultClass("defaultElementNamespaceInstanceProvider",conf.getDefaultElementNamespaceInstanceProvider());
			debugLanguageDefaultClass("defaultElementAttributeValueParser",conf.getDefaultElementAttributeValueParser());
			debugLanguageDefaultClass("defaultElementObjectPropertyValue",conf.getDefaultElementObjectPropertyValue());
			debugLanguageDefaultClass("defaultElementNamespaceAttributeComparator",conf.getDefaultElementNamespaceAttributeComparator());
			
			contentWriter.printTagEnd(Tag.X4OLanguageDefaultClasses);
		} catch (SAXException e) {
			throw new ElementException(e);
		}
	}
	
	private void debugLanguageDefaultClass(String name,Class<?> clazz) throws SAXException {
		AttributesImpl atts = new AttributesImpl();
		atts.addAttribute ("", "name", "", "", name);
		atts.addAttribute ("", "className", "", "", clazz.getName());
		contentWriter.printTagStartEnd (Tag.X4OLanguageDefaultClass, atts);
	}
	
	public void debugPhaseOrder(List<X4OPhase> phases) throws X4OPhaseException {
		X4OPhase phase = null;
		try {
			contentWriter.printTagStart (Tag.phaseOrder);
			for (X4OPhase phase2:phases) {
				phase = phase2;
				debugPhase(phase2);
			}
			contentWriter.printTagEnd(Tag.phaseOrder);
		} catch (SAXException e) {
			// fall back...
			if (phase==null) {
				if (phases.isEmpty()) {
					throw new X4OPhaseException(null,e); /// FIXME: mmmm phase in exception here ?
				}
				phase = phases.get(0);
			}
			throw new X4OPhaseException(phase,e);
		}
	}
	
	private void debugPhase(X4OPhase phase) throws X4OPhaseException {
		try {
			AttributesImpl atts = new AttributesImpl();
			atts.addAttribute ("", "id", "", "", phase.getId());
			atts.addAttribute ("", "type", "", "", phase.getType().name());
			atts.addAttribute ("", "runOnce", "", "", phase.isRunOnce()+"");
			atts.addAttribute ("", "listenersSize", "", "", phase.getPhaseListeners().size()+"");
			
			contentWriter.printTagStart (Tag.phase, atts);
			for (X4OPhaseListener l:phase.getPhaseListeners()) {
				atts = new AttributesImpl();
				atts.addAttribute ("", "className", "", "", l.getClass().getName());
				contentWriter.printTagStartEnd (Tag.X4OPhaseListener, atts);
			}
			for (String dep:phase.getPhaseDependencies()) {
				contentWriter.printTagCharacters(Tag.X4OPhaseDependency, dep);
			}
			contentWriter.printTagEnd(Tag.phase);
		} catch (SAXException e) {
			throw new X4OPhaseException(phase,e);
		}
	}
	
	public void debugElementLanguageModules(X4OLanguageSession elementLanguage) throws ElementException {
		try {
			contentWriter.printTagStart (Tag.ElementLanguageModules);
			
			for (X4OLanguageModule module:elementLanguage.getLanguage().getLanguageModules()) {
				AttributesImpl atts = new AttributesImpl();
				atts.addAttribute ("", "className", "", "", module.getClass().getName());
				atts.addAttribute ("", "id", "", "", module.getId());
				atts.addAttribute ("", "providerName", "", "", module.getProviderName());
				atts.addAttribute ("", "providerHost", "", "", module.getProviderHost());
				contentWriter.printTagStart (Tag.ElementLanguageModule, atts);
				
				for (X4OLanguageModuleLoaderResult result:X4OLanguageModuleLoaderResult.values()) {
					String value = module.getLoaderResult(result);
					if (value==null) {
						continue;
					}
					atts = new AttributesImpl();
					atts.addAttribute ("", "resultKey", "", "", result.name());
					atts.addAttribute ("", "resultValue", "", "", value);
					contentWriter.printTagStartEnd (Tag.ElementLanguageModuleResult, atts);
				}
				
				debugElementConfiguratorGlobal(module.getElementConfiguratorGlobals());
				debugElementBindingHandler(module.getElementBindingHandlers());
				
				for (ElementInterface elementInterface:module.getElementInterfaces()) {
					atts = new AttributesImpl();
					atts.addAttribute ("", "className", "", "", elementInterface.getClass().getName());
					atts.addAttribute ("", "description", "", "", elementInterface.getDescription());
					atts.addAttribute ("", "interfaceClass", "", "", elementInterface.getInterfaceClass().getName());
					
					contentWriter.printTagStart (Tag.elementInterface, atts);
					debugElementClassBase(elementInterface);
					contentWriter.printTagEnd(Tag.elementInterface);
				}
				
				for (ElementNamespace enc:module.getElementNamespaces()) {
					atts = new AttributesImpl();
					atts.addAttribute ("", "uri", "", "", enc.getUri());	
					atts.addAttribute ("", "description", "", "", enc.getDescription());
					atts.addAttribute ("", "schemaUri", "", "", enc.getSchemaUri());
					atts.addAttribute ("", "schemaResource", "", "", enc.getSchemaResource());
					atts.addAttribute ("", "className", "", "", enc.getClass().getName());
					
					contentWriter.printTagStart (Tag.elementNamespace, atts);
					
					for (ElementNamespaceAttribute p:enc.getElementNamespaceAttributes()) {
						atts = new AttributesImpl();
						atts.addAttribute ("", "attributeName", "", "", p.getAttributeName());
						atts.addAttribute ("", "description", "", "", p.getDescription());
						atts.addAttribute ("", "className", "", "", p.getClass().getName());
						contentWriter.printTagStart (Tag.elementNamespaceAttribute, atts);
						for (String para:p.getNextAttributes()) {
							atts = new AttributesImpl();
							atts.addAttribute ("", "attributeName", "", "", para);	
							contentWriter.printTagStartEnd (Tag.nextAttribute, atts);
						}
						contentWriter.printTagEnd(Tag.elementNamespaceAttribute);
					}
					for (ElementClass ec:enc.getElementClasses()) {
						debugElementClass(ec);
					}
					
					ElementNamespaceInstanceProvider eip = enc.getElementNamespaceInstanceProvider();
					atts = new AttributesImpl();
					atts.addAttribute ("", "className", "", "", eip.getClass().getName());
					contentWriter.printTagStartEnd (Tag.ElementNamespaceInstanceProvider, atts);
					
					contentWriter.printTagEnd(Tag.elementNamespace);
				}
				
				contentWriter.printTagEnd(Tag.ElementLanguageModule);
			}
			
			contentWriter.printTagEnd(Tag.ElementLanguageModules);
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
				
				contentWriter.printTagStartEnd (Tag.elementObject, atts2);
			}
			
			StringBuilder elementPath = getElementPath(element,new StringBuilder());
			atts.addAttribute ("", "elementPath", "", "", elementPath.toString());
			
			contentWriter.printTagStart(Tag.element, atts);
			// FIXME put elementObject herer ?
			contentWriter.printTagEnd(Tag.element);
		} catch (SAXException e) {
			throw new ElementException(e);
		}
	}
	
	/**
	 * Todo move after xpath support.
	 * @param element	The element.
	 * @param buff		The buffer.
	 * @return	Returns the buffer of the builf path.
	 */
	private StringBuilder getElementPath(Element element,StringBuilder buff) {
		if (element.getParent()==null) {
			buff.append('/'); // root slash
			buff.append(element.getElementClass().getId());
			return buff;
		}
		buff = getElementPath(element.getParent(),buff);
		buff.append('/');
		buff.append(element.getElementClass().getId());
		return buff;
	}
	
	public void debugSAXConfigStart() throws SAXException {
		contentWriter.printTagStart (Tag.SAXConfig);
	}
	
	public void debugSAXConfigEnd() throws SAXException {
		contentWriter.printTagEnd (Tag.SAXConfig);
	}
	
	public void debugSAXMessage(String type,String key,String value) throws SAXException {
		AttributesImpl atts = new AttributesImpl();
		atts.addAttribute ("", "key", "", "", key);
		//atts.addAttribute ("", "value", "", "", value);
		atts.addAttribute ("", "type", "", "", type);
		contentWriter.printTagStart (Tag.SAXConfigProperty, atts);
		contentWriter.printCharacters(value);
		contentWriter.printTagEnd(Tag.SAXConfigProperty);
	}
	
	public void debugPhaseMessage(String message,Class<?> clazz) throws ElementException  {	
		AttributesImpl atts = new AttributesImpl();
		atts.addAttribute ("", "fromClass", "", "", clazz.getName()+"");
		try {
			contentWriter.printTagStart (Tag.message, atts);
			contentWriter.printCharacters(message);
			contentWriter.printTagEnd(Tag.message);
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
			
			contentWriter.printTagStart (Tag.runElementConfigurator, atts);
			debugElement(element);
			contentWriter.printTagEnd(Tag.runElementConfigurator);
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

			contentWriter.printTagStart (Tag.doBind, atts);
			debugElement(element);
			contentWriter.printTagEnd(Tag.doBind);
		} catch (SAXException e) {
			throw new ElementException(e);
		}
	}
	
	private void debugElementClass(ElementClass elementClass) throws SAXException {
		AttributesImpl atts = new AttributesImpl();
		atts.addAttribute ("", "id", "", "", elementClass.getId());
		atts.addAttribute ("", "autoAttributes", "", "", ""+elementClass.getAutoAttributes());
		//atts.addAttribute ("", "schemaContentBase", "", "", ""+elementClass.getSchemaContentBase());
		atts.addAttribute ("", "description", "", "", elementClass.getDescription());
		atts.addAttribute ("", "objectClassName", "", "", ""+elementClass.getObjectClass());
		atts.addAttribute ("", "className", "", "", elementClass.getClass().getName());
		contentWriter.printTagStart (Tag.elementClass, atts);
		for (String phase:elementClass.getSkipPhases()) {
			atts = new AttributesImpl();
			atts.addAttribute ("", "phase", "", "", ""+phase);
			contentWriter.printTagStartEnd(Tag.elementSkipPhase, atts);
		}
		debugElementConfigurator(elementClass.getElementConfigurators());
		debugElementClassBase(elementClass);
		contentWriter.printTagEnd(Tag.elementClass);
	}
	
	private void debugElementClassBase(ElementClassBase elementClassBase) throws SAXException {
		AttributesImpl atts = new AttributesImpl();
		atts.addAttribute ("", "description", "", "", elementClassBase.getDescription());
		atts.addAttribute ("", "className", "", "", elementClassBase.getClass().getName());
		contentWriter.printTagStart (Tag.elementClassBase, atts);
		debugElementConfigurator(elementClassBase.getElementConfigurators());
		debugElementClassAttributes(elementClassBase.getElementClassAttributes());
		contentWriter.printTagEnd(Tag.elementClassBase);
	}
	
	private void debugElementConfigurator(List<ElementConfigurator> elementConfigurators) throws SAXException {
		for (ElementConfigurator elementConfigurator:elementConfigurators) {
			AttributesImpl atts = new AttributesImpl();
			atts.addAttribute ("", "description", "", "", elementConfigurator.getDescription());
			atts.addAttribute ("", "className", "", "", elementConfigurator.getClass().getName());
			contentWriter.printTagStartEnd (Tag.elementConfigurator, atts);
		}
	}
	
	private void debugElementConfiguratorGlobal(List<ElementConfiguratorGlobal> elementConfigurators) throws SAXException {
		for (ElementConfiguratorGlobal elementConfigurator:elementConfigurators) {
			AttributesImpl atts = new AttributesImpl();
			atts.addAttribute ("", "description", "", "", elementConfigurator.getDescription());
			atts.addAttribute ("", "className", "", "", elementConfigurator.getClass().getName());
			contentWriter.printTagStartEnd (Tag.elementConfiguratorGlobal, atts);
		}
	}
	
	private void debugElementClassAttributes(Collection<ElementClassAttribute> elementClassAttributes) throws SAXException {
		for (ElementClassAttribute elementClassAttribute:elementClassAttributes) {
			AttributesImpl atts = new AttributesImpl();
			atts.addAttribute ("", "id", "", "", elementClassAttribute.getId());
			atts.addAttribute ("", "description", "", "", elementClassAttribute.getDescription());
			atts.addAttribute ("", "className", "", "", elementClassAttribute.getClass().getName());
			atts.addAttribute ("", "defaultValue", "", "", ""+elementClassAttribute.getDefaultValue());
			atts.addAttribute ("", "runBeanValue", "", "", ""+elementClassAttribute.getRunBeanValue());
			atts.addAttribute ("", "runConverters", "", "", ""+elementClassAttribute.getRunConverters());
			//atts.addAttribute ("", "runInterfaces", "", "", ""+elementClassAttribute.getRunInterfaces());
			atts.addAttribute ("", "runResolveEL", "", "", ""+elementClassAttribute.getRunResolveEL());
			contentWriter.printTagStart(Tag.elementClassAttribute, atts);
			if (elementClassAttribute.getObjectConverter()!=null) {
				debugObjectConverter(elementClassAttribute.getObjectConverter());
			}
			for (String alias:elementClassAttribute.getAttributeAliases()) {
				atts = new AttributesImpl();
				atts.addAttribute ("", "name", "", "", ""+alias);
				contentWriter.printTagStartEnd(Tag.attributeAlias, atts);
			}
			contentWriter.printTagEnd(Tag.elementClassAttribute);
		}	
	}
	
	private void debugObjectConverter(ObjectConverter objectConverter) throws SAXException {
		AttributesImpl atts = new AttributesImpl();
		atts.addAttribute ("", "objectClassTo", "", "", objectConverter.getObjectClassTo().getName());
		atts.addAttribute ("", "objectClassBack", "", "", objectConverter.getObjectClassBack().getName());
		atts.addAttribute ("", "className", "", "", objectConverter.getClass().getName());
		contentWriter.printTagStart (Tag.objectConverter, atts);
		//for (ObjectConverter oc:objectConverter.getObjectConverters()) {
		//	debugObjectConverter(oc); // TODO: turn me on
		//}
		contentWriter.printTagEnd(Tag.objectConverter);
	}
	
	private void debugElementBindingHandler(List<ElementBindingHandler> elementBindingHandlers) throws SAXException {
		for (ElementBindingHandler bind:elementBindingHandlers) {
			AttributesImpl atts = new AttributesImpl();
			atts.addAttribute ("", "className", "", "", bind.getClass().getName());
			atts.addAttribute ("", "description", "", "", bind.getDescription());
			atts.addAttribute ("", "bindParentClass", "", "", bind.getBindParentClass().toString());
			contentWriter.printTagStart (Tag.elementBindingHandler, atts);
			
			for (Class<?> clazz:bind.getBindChildClasses()) {
				AttributesImpl atts2 = new AttributesImpl();
				atts2.addAttribute ("", "className", "", "", clazz.getName());
				contentWriter.printTagStartEnd (Tag.elementBindingHandlerChildClass, atts2);
			}
			
			contentWriter.printTagEnd(Tag.elementBindingHandler);
		}
	}
	
	/**
	 * Creates an debug phase
	 * @return	The X4OPhaseHandler for this phase.
	 */
	public X4OPhaseListener createDebugPrintTreePhaseListener() {
		return new X4OPhaseListener() {
			List<String> startedPrefix = new ArrayList<String>(10);
			public void preRunPhase(X4OPhase phase,X4OLanguageSession languageSession) throws X4OPhaseException {
			}
			public void endRunPhase(X4OPhase phase,X4OLanguageSession languageSession) throws X4OPhaseException {
				if (languageSession.hasX4ODebugWriter()==false) {
					throw new X4OPhaseException(phase,"Use debugPhase only when X4OParser.debugWriter is filled.");
				}
				try {
					AttributesImpl atts = new AttributesImpl();
					contentWriter.printTagStart (Tag.printElementTree, atts);
					startedPrefix.clear();
					printXML(languageSession.getRootElement());
					for (String prefix:startedPrefix) {
						contentWriter.getContentWriterWrapped().endPrefixMapping(prefix);
					}
					contentWriter.printTagEnd(Tag.printElementTree);
					
				} catch (SAXException e) {
					throw new X4OPhaseException(phase,e);
				}
			}
			
			// note: slow version
			private String getNamespaceForElement(Element e) {
				for (X4OLanguageModule mod:e.getLanguageSession().getLanguage().getLanguageModules()) {
					for (ElementNamespace enc:mod.getElementNamespaces()) {
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
				ContentWriter handler = getContentWriter();
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
				ElementNamespace en = element.getLanguageSession().getLanguage().findElementNamespace(nameSpace);
				
				String prefix = en.getPrefixMapping(); // TODO: note is for reading; getPrefixMapping 
				if (prefix==null) {
					prefix = en.getSchemaPrefix();
				}
				if (prefix==null) {
					prefix = en.getId();
				}
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
	}
}
