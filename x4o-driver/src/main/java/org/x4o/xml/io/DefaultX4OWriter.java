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

package org.x4o.xml.io;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.x4o.xml.element.Element;
import org.x4o.xml.element.ElementBindingHandler;
import org.x4o.xml.element.ElementBindingHandlerException;
import org.x4o.xml.element.ElementClass;
import org.x4o.xml.element.ElementClassAttribute;
import org.x4o.xml.element.ElementNamespaceContext;
import org.x4o.xml.element.ElementNamespaceInstanceProviderException;
import org.x4o.xml.element.ElementObjectPropertyValueException;
import org.x4o.xml.io.sax.XMLWriter;
import org.x4o.xml.lang.X4OLanguageContext;
import org.x4o.xml.lang.X4OLanguageModule;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * DefaultX4OWriter can write the xml language.
 * 
 * @author Willem Cazander
 * @version 1.0 Apr 6, 2013
 */
public class DefaultX4OWriter<T> extends AbstractX4OWriter<T> {

	public DefaultX4OWriter(X4OLanguageContext elementLanguage) {
		super(elementLanguage);
	}

	public void writeContext(X4OLanguageContext context,OutputStream out) throws ParserConfigurationException,
			FileNotFoundException, SecurityException, NullPointerException,
			SAXException, IOException {
		
		Element root = getLanguageContext().getRootElement();
		if (root.getElementClass()==null) {
			try {
				root = fillElementTree(root.getElementObject());
			} catch (ElementNamespaceInstanceProviderException e) {
				throw new SAXException(e);
			} catch (ElementBindingHandlerException e) {
				throw new SAXException(e);
			}
		}
		
		XMLWriter writer = new XMLWriter(out);
		writer.startDocument();
		
		Map<String,String> prefixes = new HashMap<String,String>();
		startPrefixTree(root,prefixes);
		for (String uri:prefixes.keySet()) {
			String prefix = prefixes.get(uri);
			writer.startPrefixMapping(prefix, uri);
		}
		
		try {
			writeTree(writer,root);
		} catch (ElementObjectPropertyValueException e) {
			throw new SAXException(e);
		}
		writer.endDocument();
		out.flush();
		out.close();
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
			if(m.getName().startsWith("getLocationOnScreen")) {
				continue; // TODO: rm this
			}
			String name = m.getName().substring(3,4).toLowerCase()+m.getName().substring(4);
			result.add(name);
			
		}
		return result;
	}
	
	private void writeTree(XMLWriter writer,Element element) throws SAXException, ElementObjectPropertyValueException {
		AttributesImpl atts = new AttributesImpl();
		
		if (element.getElementClass().getAutoAttributes()!=null && element.getElementClass().getAutoAttributes()==false) {
			for (ElementClassAttribute eca:element.getElementClass().getElementClassAttributes()) {
				Object value = element.getLanguageContext().getElementObjectPropertyValue().getProperty(element.getElementObject(),eca.getName());
				if (value==null) {
					continue;
				}
				atts.addAttribute ("", eca.getName(), "", "", ""+value);
			}
			
		} else {
			for (String p:getProperties(element.getElementObject().getClass())) {
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
		writer.startElement(elementUri, element.getElementClass().getTag(), "", atts);
		for (Element e:element.getChilderen()) {
			writeTree(writer,e);
		}
		writer.endElement(elementUri, element.getElementClass().getTag(), "");
	}
	
	private String findElementUri(Element e) {
		for (X4OLanguageModule mod:e.getLanguageContext().getLanguage().getLanguageModules()) {
			for (ElementNamespaceContext c:mod.getElementNamespaceContexts()) {
				ElementClass ec = c.getElementClass(e.getElementClass().getTag());
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
	
	private Element fillElementTree(Object object) throws ElementNamespaceInstanceProviderException, ElementBindingHandlerException {
		Element element = findRootElement(object.getClass());
		element.setElementObject(object);
		
		for (ElementBindingHandler bind:getLanguageContext().getLanguage().findElementBindingHandlers(object)) {
			bind.createChilderen(element);
			fillTree(element);
		}
		return element;
	}
	
	private void fillTree(Element element) throws ElementNamespaceInstanceProviderException, ElementBindingHandlerException {
		for (Element e:element.getChilderen()) {
			Object object = e.getElementObject();
			for (ElementBindingHandler bind:getLanguageContext().getLanguage().findElementBindingHandlers(object)) {
				bind.createChilderen(e);
				fillTree(e);
			}
		}
	}
	
	
	private Element findRootElement(Class<?> objectClass) throws ElementNamespaceInstanceProviderException {
		// redo this mess, add nice find for root
		for (X4OLanguageModule modContext:getLanguageContext().getLanguage().getLanguageModules()) {
			for (ElementNamespaceContext nsContext:modContext.getElementNamespaceContexts()) {
				if (nsContext.getLanguageRoot()!=null && nsContext.getLanguageRoot()) {
					for (ElementClass ec:nsContext.getElementClasses()) {
						if (ec.getObjectClass()!=null && ec.getObjectClass().equals(objectClass)) { 
							return nsContext.getElementNamespaceInstanceProvider().createElementInstance(getLanguageContext(), ec.getTag());
						}
					}
				}
			}
		}
		for (X4OLanguageModule modContext:getLanguageContext().getLanguage().getLanguageModules()) {
			for (ElementNamespaceContext nsContext:modContext.getElementNamespaceContexts()) {
				for (ElementClass ec:nsContext.getElementClasses()) {
					if (ec.getObjectClass()!=null && ec.getObjectClass().equals(objectClass)) { 
						return nsContext.getElementNamespaceInstanceProvider().createElementInstance(getLanguageContext(), ec.getTag());
					}
				}
			}
		}
		throw new IllegalArgumentException("Could not find ElementClass for: "+objectClass.getName());
	}
}
