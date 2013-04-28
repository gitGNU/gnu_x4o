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
import org.x4o.xml.element.ElementClass;
import org.x4o.xml.element.ElementClassAttribute;
import org.x4o.xml.element.ElementInterface;
import org.x4o.xml.element.ElementNamespaceContext;
import org.x4o.xml.element.ElementObjectPropertyValueException;
import org.x4o.xml.io.sax.XMLWriter;
import org.x4o.xml.lang.X4OLanguageContext;
import org.x4o.xml.lang.X4OLanguageModule;
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

	public DefaultX4OWriter(X4OLanguageContext languageContext) {
		super(languageContext);
	}

	public void writeContext(X4OLanguageContext languageContext,OutputStream out) throws ParserConfigurationException,
			FileNotFoundException, SecurityException, NullPointerException,
			SAXException, IOException {
		
		try {
			languageContext.getLanguage().getPhaseManager().runPhases(languageContext, X4OPhaseType.XML_WRITE);
		} catch (X4OPhaseException e1) {
			throw new SAXException(e1);
		}
		Element root = languageContext.getRootElement();
		
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
			String name = m.getName().substring(3,4).toLowerCase()+m.getName().substring(4);
			result.add(name);
			
		}
		return result;
	}
	
	private void writeTree(XMLWriter writer,Element element) throws SAXException, ElementObjectPropertyValueException {
		AttributesImpl atts = new AttributesImpl();
		
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
			writeTree(writer,e);
		}
		writer.endElement(elementUri, element.getElementClass().getId(), "");
	}
	
	private String findElementUri(Element e) {
		for (X4OLanguageModule mod:getLanguageContext().getLanguage().getLanguageModules()) {
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
		ElementNamespaceContext ns = getLanguageContext().getLanguage().findElementNamespaceContext(uri);
		if (ns.getPrefixMapping()!=null) {
			return ns.getPrefixMapping();
		}
		return ns.getId();
	}

}
