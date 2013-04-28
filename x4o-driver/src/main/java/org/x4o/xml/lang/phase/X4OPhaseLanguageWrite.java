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

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.x4o.xml.element.Element;
import org.x4o.xml.element.ElementBindingHandler;
import org.x4o.xml.element.ElementBindingHandlerException;
import org.x4o.xml.element.ElementClass;
import org.x4o.xml.element.ElementClassAttribute;
import org.x4o.xml.element.ElementInterface;
import org.x4o.xml.element.ElementNamespaceContext;
import org.x4o.xml.element.ElementNamespaceInstanceProviderException;
import org.x4o.xml.element.ElementObjectPropertyValueException;
import org.x4o.xml.io.XMLConstants;
import org.x4o.xml.io.sax.XMLWriter;
import org.x4o.xml.lang.X4OLanguageModule;
import org.x4o.xml.lang.X4OLanguageContext;
import org.x4o.xml.lang.X4OLanguageProperty;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * X4OPhaseLanguageWrite defines all phases to write the language.
 * 
 * @author Willem Cazander
 * @version 1.0 Apr 7, 2013
 */
public class X4OPhaseLanguageWrite {

	private Logger logger = null;
	
	public X4OPhaseLanguageWrite() {
		logger = Logger.getLogger(X4OPhaseLanguageWrite.class.getName());
	}
	
	public void createPhases(DefaultX4OPhaseManager manager) {
		manager.addX4OPhase(new X4OPhaseWriteStart());
		manager.addX4OPhase(new X4OPhaseWriteFillTree());
		manager.addX4OPhase(new X4OPhaseWriteXml());
		manager.addX4OPhase(new X4OPhaseWriteEnd());
	}
	
	/**
	 * Creates the X4OPhaseWriteStart which is a empty meta phase.
	 */
	class X4OPhaseWriteStart extends AbstractX4OPhase {
		public X4OPhaseType getType() {
			return X4OPhaseType.XML_WRITE;
		}
		public String getId() {
			return "WRITE_START";
		}
		public String[] getPhaseDependencies() {
			return new String[]{};
		}
		public boolean isElementPhase() {
			return false;
		}
		public void runElementPhase(Element element) throws X4OPhaseException {
		}
		public void runPhase(X4OLanguageContext languageContext) throws X4OPhaseException  {
			logger.finest("Run init start phase");
		}
	};
	
	/**
	 * Fills the element tree
	 */
	class X4OPhaseWriteFillTree extends AbstractX4OPhase {
		public X4OPhaseType getType() {
			return X4OPhaseType.XML_WRITE;
		}
		public String getId() {
			return "WRITE_FILL_TREE";
		}
		public String[] getPhaseDependencies() {
			return new String[]{"WRITE_START"};
		}
		public boolean isElementPhase() {
			return false;
		}
		public void runElementPhase(Element element) throws X4OPhaseException {
		}
		public void runPhase(X4OLanguageContext languageContext) throws X4OPhaseException  {
			try {
				Element root = languageContext.getRootElement();
				// TODO: check for read tree then write support as ec is not null then ..
				if (root.getElementClass()==null) {
					root = fillElementTree(languageContext,root.getElementObject());
				}
			} catch (Exception e) {
				throw new X4OPhaseException(this,e);
			}
		}
		
		private Element fillElementTree(X4OLanguageContext languageContext,Object object) throws ElementNamespaceInstanceProviderException, ElementBindingHandlerException {
			Element element = findRootElement(languageContext,object.getClass());
			element.setElementObject(object);
			languageContext.setRootElement(element);
			
			for (ElementBindingHandler bind:languageContext.getLanguage().findElementBindingHandlers(object)) {
				bind.createChilderen(element);
				fillTree(languageContext,element);
			}
			return element;
		}
		
		private void fillTree(X4OLanguageContext languageContext,Element element) throws ElementNamespaceInstanceProviderException, ElementBindingHandlerException {
			for (Element e:element.getChilderen()) {
				Object object = e.getElementObject();
				for (ElementBindingHandler bind:languageContext.getLanguage().findElementBindingHandlers(object)) {
					bind.createChilderen(e);
					fillTree(languageContext,e);
				}
			}
		}
		
		private Element findRootElement(X4OLanguageContext languageContext,Class<?> objectClass) throws ElementNamespaceInstanceProviderException {
			// redo this mess, add nice find for root
			for (X4OLanguageModule modContext:languageContext.getLanguage().getLanguageModules()) {
				for (ElementNamespaceContext nsContext:modContext.getElementNamespaceContexts()) {
					if (nsContext.getLanguageRoot()!=null && nsContext.getLanguageRoot()) {
						for (ElementClass ec:nsContext.getElementClasses()) {
							if (ec.getObjectClass()!=null && ec.getObjectClass().equals(objectClass)) { 
								return nsContext.getElementNamespaceInstanceProvider().createElementInstance(languageContext, ec.getId());
							}
						}
					}
				}
			}
			for (X4OLanguageModule modContext:languageContext.getLanguage().getLanguageModules()) {
				for (ElementNamespaceContext nsContext:modContext.getElementNamespaceContexts()) {
					for (ElementClass ec:nsContext.getElementClasses()) {
						if (ec.getObjectClass()!=null && ec.getObjectClass().equals(objectClass)) { 
							return nsContext.getElementNamespaceInstanceProvider().createElementInstance(languageContext, ec.getId());
						}
					}
				}
			}
			throw new IllegalArgumentException("Could not find ElementClass for: "+objectClass.getName());
		}
	};
	
	/**
	 * Write xml to output.
	 */
	class X4OPhaseWriteXml extends AbstractX4OPhase {
		public X4OPhaseType getType() {
			return X4OPhaseType.XML_WRITE;
		}
		public String getId() {
			return "WRITE_XML";
		}
		public String[] getPhaseDependencies() {
			return new String[] {"WRITE_FILL_TREE"};
		}
		public boolean isElementPhase() {
			return false;
		}
		public void runElementPhase(Element element) throws X4OPhaseException {
		}
		boolean schemaUriPrint;
		String schemaUriRoot;
		public void runPhase(X4OLanguageContext languageContext) throws X4OPhaseException {
			OutputStream out = (OutputStream)languageContext.getLanguageProperty(X4OLanguageProperty.WRITER_OUTPUT_STREAM);
			try {
				String encoding = languageContext.getLanguagePropertyString(X4OLanguageProperty.WRITER_OUTPUT_ENCODING);
				String charNew = languageContext.getLanguagePropertyString(X4OLanguageProperty.WRITER_OUTPUT_CHAR_NEWLINE);
				String charTab = languageContext.getLanguagePropertyString(X4OLanguageProperty.WRITER_OUTPUT_CHAR_TAB);
				schemaUriPrint = languageContext.getLanguagePropertyBoolean(X4OLanguageProperty.WRITER_SCHEMA_URI_PRINT);
				schemaUriRoot = languageContext.getLanguagePropertyString(X4OLanguageProperty.WRITER_SCHEMA_URI_ROOT);
				if (encoding==null) { encoding = XMLConstants.XML_DEFAULT_ENCODING; }
				if (charNew==null)  { charNew = XMLConstants.CHAR_NEWLINE;			}
				if (charTab==null)  { charTab = XMLConstants.CHAR_TAB;				}
				
				Element root = languageContext.getRootElement();
				if (schemaUriRoot==null) {
					String rootUri = findElementUri(root);
					ElementNamespaceContext ns = languageContext.getLanguage().findElementNamespaceContext(rootUri);
					if (ns!=null) {
						schemaUriRoot = ns.getSchemaUri();
					}
				}
				
				XMLWriter writer = new XMLWriter(out,encoding,charNew,charTab);
				writer.startDocument();
				
				Map<String,String> prefixes = new HashMap<String,String>();
				startPrefixTree(root,prefixes);
				for (String uri:prefixes.keySet()) {
					String prefix = prefixes.get(uri);
					writer.startPrefixMapping(prefix, uri);
				}
				try {
					writeTree(writer,root,true);
				} catch (ElementObjectPropertyValueException e) {
					throw new SAXException(e);
				}
				writer.endDocument();
				out.flush();
				
			} catch (Exception e) {
				throw new X4OPhaseException(this,e);
			} finally {
				if (out!=null) {
					try {
						out.close();
					} catch (IOException e) {
						logger.warning(e.getMessage());
					}
				}
			}
		}
		
		private void startPrefixTree(Element element,Map<String,String> result) throws SAXException {
			String elementUri = findElementUri(element);
			if (result.containsKey(elementUri)==false) {
				String elementUriPrefix = findNamespacePrefix(element,elementUri);
				result.put(elementUri, elementUriPrefix);
			}
			for (Element e:element.getChilderen()) {
				startPrefixTree(e,result);
			}
		}
		
		private List<String> getProperties(Class<?> objectClass) {
			List<String> result = new ArrayList<String>();
			for (Method m:objectClass.getMethods()) {
				Class<?>[] types = m.getParameterTypes();
				if (types.length != 0) {
					continue;
				}
				if (m.getName().equals("getClass")) {
					continue;
				}
				if (m.getName().startsWith("get")==false) {
					continue;
				}
				String name = m.getName().substring(3,4).toLowerCase()+m.getName().substring(4);
				result.add(name);
				
			}
			return result;
		}
		
		private void writeTree(XMLWriter writer,Element element,boolean isRoot) throws SAXException, ElementObjectPropertyValueException {
			AttributesImpl atts = new AttributesImpl();
			
			if (isRoot && schemaUriPrint) {
				String rootUri = findElementUri(element);
				writer.startPrefixMapping("xsi", XMLConstants.XML_SCHEMA_INSTANCE_NS_URI);
				atts.addAttribute ("xsi", "schemaLocation", "", "", rootUri+" "+schemaUriRoot);
			}
			if (element.getElementClass().getAutoAttributes()!=null && element.getElementClass().getAutoAttributes()==false) {
				for (ElementClassAttribute eca:element.getElementClass().getElementClassAttributes()) {
					if (eca.getRunBeanValue()!=null && eca.getRunBeanValue()==false) {
						continue;
					}
					
					Object value = element.getLanguageContext().getElementObjectPropertyValue().getProperty(element.getElementObject(),eca.getId());
					if (value==null) {
						continue;
					}
					atts.addAttribute ("", eca.getId(), "", "", ""+value);
				}
				
			} else {
				for (String p:getProperties(element.getElementObject().getClass())) {

					ElementClassAttribute eca = element.getElementClass().getElementClassAttributeByName(p);
					if (eca!=null && eca.getRunBeanValue()!=null && eca.getRunBeanValue()) {
						continue;
					}
					boolean writeValue = true;
					for (ElementInterface ei:element.getLanguageContext().getLanguage().findElementInterfaces(element.getElementObject().getClass())) {
						eca = ei.getElementClassAttributeByName(p);
						if (eca!=null && eca.getRunBeanValue()!=null && eca.getRunBeanValue()==false) {
							writeValue = false;
							break;
						}
					}
					if (writeValue==false) {
						continue;
					}
					
					// TODO: check attr see reader
					Object value = element.getLanguageContext().getElementObjectPropertyValue().getProperty(element.getElementObject(),p);
					if (value==null) {
						continue;
					}
					if (value instanceof List || value instanceof Collection) {
						continue; // TODO; filter on type of childeren
					}
					atts.addAttribute ("", p, "", "", ""+value);
				}
			}
			
			String elementUri = findElementUri(element);
			writer.startElement(elementUri, element.getElementClass().getId(), "", atts);
			for (Element e:element.getChilderen()) {
				writeTree(writer,e,false);
			}
			writer.endElement(elementUri, element.getElementClass().getId(), "");
		}
		
		private String findElementUri(Element e) {
			for (X4OLanguageModule mod:e.getLanguageContext().getLanguage().getLanguageModules()) {
				for (ElementNamespaceContext c:mod.getElementNamespaceContexts()) {
					ElementClass ec = c.getElementClass(e.getElementClass().getId());
					if (ec!=null) {
						return c.getUri();
					}
				}
			}
			return null;
		}
		
		private String findNamespacePrefix(Element e,String uri) {
			ElementNamespaceContext ns = e.getLanguageContext().getLanguage().findElementNamespaceContext(uri);
			if (ns.getPrefixMapping()!=null) {
				return ns.getPrefixMapping();
			}
			return ns.getId();
		}
	};
	
	/**
	 * Creates the X4OPhaseWriteEnd which is a empty meta phase.
	 */
	class X4OPhaseWriteEnd extends AbstractX4OPhase {
		public X4OPhaseType getType() {
			return X4OPhaseType.XML_WRITE;
		}
		public String getId() {
			return "WRITE_END";
		}
		public String[] getPhaseDependencies() {
			return new String[]{"WRITE_XML"};
		}
		public boolean isElementPhase() {
			return false;
		}
		public void runElementPhase(Element element) throws X4OPhaseException {
		}
		public void runPhase(X4OLanguageContext languageContext) throws X4OPhaseException  {
			logger.finest("Run init end phase");
		}
	};
}
