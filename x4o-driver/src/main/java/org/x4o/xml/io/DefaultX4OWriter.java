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
package org.x4o.xml.io;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.x4o.xml.element.Element;
import org.x4o.xml.element.ElementClass;
import org.x4o.xml.element.ElementClassAttribute;
import org.x4o.xml.element.ElementInterface;
import org.x4o.xml.element.ElementNamespace;
import org.x4o.xml.element.ElementObjectPropertyValueException;
import org.x4o.xml.io.sax.ext.PropertyConfig;
import org.x4o.xml.io.sax.ext.ContentWriterXml;
import org.x4o.xml.io.sax.ext.PropertyConfig.PropertyConfigItem;
import org.x4o.xml.lang.X4OLanguage;
import org.x4o.xml.lang.X4OLanguageSession;
import org.x4o.xml.lang.X4OLanguageModule;
import org.x4o.xml.lang.phase.X4OPhase;
import org.x4o.xml.lang.phase.X4OPhaseException;
import org.x4o.xml.lang.phase.X4OPhaseType;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * DefaultX4OWriter can write the xml language.
 * 
 * @author Willem Cazander
 * @version 1.0 Apr 6, 2013
 */
public class DefaultX4OWriter<T> extends AbstractX4OWriter<T> {
	
	private final PropertyConfig propertyConfig;
	
	private final static String PROPERTY_CONTEXT_PREFIX = PropertyConfig.X4O_PROPERTIES_PREFIX+PropertyConfig.X4O_PROPERTIES_WRITER;

	public final static PropertyConfig DEFAULT_PROPERTY_CONFIG;
	
	public final static String OUTPUT_STREAM                = PROPERTY_CONTEXT_PREFIX+"output/stream";
	public final static String SCHEMA_PRINT                 = PROPERTY_CONTEXT_PREFIX+"schema/print";
	public final static String SCHEMA_ROOT_URI              = PROPERTY_CONTEXT_PREFIX+"schema/root-uri";
	
	static {
		DEFAULT_PROPERTY_CONFIG = new PropertyConfig(true,ContentWriterXml.DEFAULT_PROPERTY_CONFIG,PROPERTY_CONTEXT_PREFIX,
				new PropertyConfigItem(true,OUTPUT_STREAM,OutputStream.class),
				new PropertyConfigItem(SCHEMA_PRINT,Boolean.class,true),
				new PropertyConfigItem(SCHEMA_ROOT_URI,String.class)
				);
	}
	
	/**
	 * Default constructor.
	 * @param language	The language for this writer.
	 */
	public DefaultX4OWriter(X4OLanguage language) {
		super(language);
		propertyConfig = new PropertyConfig(DEFAULT_PROPERTY_CONFIG,PROPERTY_CONTEXT_PREFIX);
	}
	
	/**
	 * @see org.x4o.xml.lang.X4OLanguageSession#getPropertyConfig()
	 */
	public PropertyConfig getPropertyConfig() {
		return propertyConfig;
	}
	
	X4OLanguageSession createLanguageSession() {
		return getLanguage().createLanguageSession();
	}
	
	/**
	 * @see org.x4o.xml.io.X4OWriterContext#writeContext(org.x4o.xml.lang.X4OLanguageSession, java.io.OutputStream)
	 */
	public void writeContext(X4OLanguageSession languageSession,OutputStream output) throws X4OConnectionException,SAXException,IOException {
		setProperty(OUTPUT_STREAM, output);
		addPhaseSkip(X4OPhase.WRITE_RELEASE);
		try {
			languageSession.getLanguage().getPhaseManager().runPhases(languageSession, X4OPhaseType.XML_WRITE);
		} catch (X4OPhaseException e) {
			throw new X4OConnectionException(e);
		}
		runWrite(languageSession);
	}
	
	private AttributeEntryComparator attributeEntryComparator = new AttributeEntryComparator();
	private boolean schemaUriPrint;
	private String schemaUriRoot;
	
	private void runWrite(X4OLanguageSession languageSession) throws X4OConnectionException {
		OutputStream out = (OutputStream)getProperty(OUTPUT_STREAM);
		try {
			String encoding = getPropertyConfig().getPropertyString(ContentWriterXml.OUTPUT_ENCODING);
			schemaUriPrint = getPropertyConfig().getPropertyBoolean(SCHEMA_PRINT);
			schemaUriRoot = getPropertyConfig().getPropertyString(SCHEMA_ROOT_URI);
			if (encoding==null) { encoding = XMLConstants.XML_DEFAULT_ENCODING; }
			
			Element root = languageSession.getRootElement();
			if (schemaUriRoot==null) {
				String rootUri = findElementUri(root);
				ElementNamespace ns = languageSession.getLanguage().findElementNamespace(rootUri);
				if (ns!=null) {
					schemaUriRoot = ns.getSchemaUri();
				}
			}
			
			ContentWriterXml writer = new ContentWriterXml(out,encoding);
			writer.getPropertyConfig().copyParentProperties(getPropertyConfig());

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
			throw new X4OConnectionException(e);
		} finally {
			if (out!=null) {
				try {
					out.close();
				} catch (IOException e) {
					//logger.warning(e.getMessage());
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
	class AttributeEntry {
		String id;
		String value;
		Integer writeOrder;
	}
	class AttributeEntryComparator implements Comparator<AttributeEntry> {
		public int compare(AttributeEntry o1, AttributeEntry o2) {
			return o1.writeOrder.compareTo(o2.writeOrder);
		}
	}
	private void writeTree(ContentWriterXml writer,Element element,boolean isRoot) throws SAXException, ElementObjectPropertyValueException {
		List<AttributeEntry> attr = new ArrayList<AttributeEntry>(20);
		if (element.getElementClass().getAutoAttributes()!=null && element.getElementClass().getAutoAttributes()==false) {
			for (ElementClassAttribute eca:element.getElementClass().getElementClassAttributes()) {
				if (eca.getRunBeanValue()!=null && eca.getRunBeanValue()==false) {
					continue;
				}
				
				Object value = element.getLanguageSession().getElementObjectPropertyValue().getProperty(element.getElementObject(),eca.getId());
				if (value==null) {
					continue;
				}
				AttributeEntry e = new AttributeEntry();
				e.id = eca.getId();
				e.value = ""+value;
				e.writeOrder = calcOrderNumber(e.id,eca.getWriteOrder());
				attr.add(e);
			}
			
		} else {
			for (String p:getProperties(element.getElementObject().getClass())) {
				Integer writeOrder = null;
				ElementClassAttribute eca = element.getElementClass().getElementClassAttributeByName(p);
				if (eca!=null) {
					writeOrder = eca.getWriteOrder();
				}
				if (eca!=null && eca.getRunBeanValue()!=null && eca.getRunBeanValue()) {
					continue;
				}
				boolean writeValue = true;
				for (ElementInterface ei:element.getLanguageSession().getLanguage().findElementInterfaces(element.getElementObject().getClass())) {
					eca = ei.getElementClassAttributeByName(p);
					if (eca!=null && writeOrder==null) {
						writeOrder = eca.getWriteOrder(); // add interface but allow override local
					}
					if (eca!=null && eca.getRunBeanValue()!=null && eca.getRunBeanValue()==false) {
						writeValue = false;
						break;
					}
				}
				if (writeValue==false) {
					continue;
				}
				
				// TODO: check attr see reader
				Object value = element.getLanguageSession().getElementObjectPropertyValue().getProperty(element.getElementObject(),p);
				if (value==null) {
					continue;
				}
				if (value instanceof List || value instanceof Collection) {
					continue; // TODO; filter on type of childeren
				}
				AttributeEntry e = new AttributeEntry();
				e.id = p;
				e.value = ""+value;
				e.writeOrder = calcOrderNumber(e.id,writeOrder);
				attr.add(e);
			}
		}
		
		// Create atts to write and append schema first.
		AttributesImpl atts = new AttributesImpl();
		if (isRoot && schemaUriPrint) {
			String rootUri = findElementUri(element);
			writer.startPrefixMapping("xsi", XMLConstants.XML_SCHEMA_INSTANCE_NS_URI);
			atts.addAttribute ("xsi", "schemaLocation", "", "", rootUri+" "+schemaUriRoot);
		}
		
		// Sort attributes in natural order of localName and add to attributes
		Collections.sort(attr, attributeEntryComparator);
		for (int i=0;i<attr.size();i++) {
			AttributeEntry a = attr.get(i);
			atts.addAttribute ("", a.id, "", "", a.value);
		}
		
		// Write Element tree recursive.
		String elementUri = findElementUri(element);
		writer.startElement(elementUri, element.getElementClass().getId(), "", atts);
		for (Element e:element.getChilderen()) {
			writeTree(writer,e,false);
		}
		writer.endElement(elementUri, element.getElementClass().getId(), "");
	}
	
	// TODO: move to defaults layer so visible in docs.
	private Integer calcOrderNumber(String name,Integer orderNumberOverride) {
		if (orderNumberOverride!=null) {
			return orderNumberOverride;
		}
		if (name==null) {
			throw new NullPointerException("Can't calculate order of null name.");
		}
		int nameSize = name.length();
		if (nameSize==1) {
			return (name.charAt(0) * 1000);
		}
		if (nameSize==2) {
			return (name.charAt(0) * 1000) + (name.charAt(1) * 100);
		}
		if (nameSize==3) {
			return (name.charAt(0) * 1000) + (name.charAt(1) * 100) + (name.charAt(2) * 10);
		}
		if (nameSize>3) {
			return (name.charAt(0) * 1000) + (name.charAt(1) * 100) + (name.charAt(2) * 10) + (name.charAt(3) * 1);
		}
		throw new IllegalArgumentException("Can't calculate order of empty name.");
	}
	
	private String findElementUri(Element e) {
		for (X4OLanguageModule mod:e.getLanguageSession().getLanguage().getLanguageModules()) {
			for (ElementNamespace c:mod.getElementNamespaces()) {
				ElementClass ec = c.getElementClass(e.getElementClass().getId());
				if (ec!=null) {
					return c.getUri();
				}
			}
		}
		return null;
	}
	
	private String findNamespacePrefix(Element e,String uri) {
		ElementNamespace ns = e.getLanguageSession().getLanguage().findElementNamespace(uri);
		if (ns.getPrefixMapping()!=null) {
			return ns.getPrefixMapping();
		}
		return ns.getId();
	}
}
