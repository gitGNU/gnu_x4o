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

package	org.x4o.xml.eld.xsd;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.x4o.xml.element.ElementAttributeHandler;
import org.x4o.xml.element.ElementBindingHandler;
import org.x4o.xml.element.ElementClass;
import org.x4o.xml.element.ElementClassAttribute;
import org.x4o.xml.element.ElementLanguage;
import org.x4o.xml.element.ElementLanguageModule;
import org.x4o.xml.element.ElementNamespaceContext;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DefaultHandler2;
import org.xml.sax.helpers.AttributesImpl;

/**
 * EldXsdXmlWriter Creates the schema from an eld resource.
 * 
 * Note: this is still writing a bit quick and hacky.
 * 
 * @author Willem Cazander
 * @version 1.0 Aug 8, 2012
 */
public class EldXsdXmlWriter {
	
	
	static public final String SCHEMA_URI = "http://www.w3.org/2001/XMLSchema";

	protected ElementLanguage context = null;
	protected DefaultHandler2 xmlWriter = null;
	protected String writeNamespace = null;
	protected Map<String, String> namespaces = null;
	
	public EldXsdXmlWriter(DefaultHandler2 xmlWriter,ElementLanguage context) {
		this.xmlWriter=xmlWriter;
		this.context=context;
		this.namespaces=new HashMap<String,String>(10);
	}
	
	private void startNamespace(String uri,String prefixNamespace) {
		String prefix = namespaces.get(uri);
		if (prefix!=null) {
			return;
		}
		if (uri.equals(writeNamespace)) {
			namespaces.put(uri, "this");
			return;
		}
		if (prefixNamespace!=null) {
			namespaces.put(uri, prefixNamespace); // let user define it
			return;
		}
		StringBuilder buf = new StringBuilder(20);
		for (char c:uri.toLowerCase().toCharArray()) {
			if (Character.isLetter(c)) {
				buf.append(c);
			}
			if (Character.isDigit(c)) {
				buf.append(c);
			}
		}
		prefix = buf.toString();
		if (prefix.startsWith("http")) {
			prefix = prefix.substring(4);
		}
		if (prefix.startsWith("uri")) {
			prefix = prefix.substring(3);
		}
		if (prefix.startsWith("url")) {
			prefix = prefix.substring(3);
		}
		namespaces.put(uri, prefix);
	}
	
	public void startNamespaces(String namespaceUri) {
		
		this.writeNamespace=namespaceUri;
		this.namespaces.clear();
		
		// redo this mess, add nice find for binding handlers
		for (ElementLanguageModule modContext:context.getElementLanguageModules()) {
			for (ElementNamespaceContext nsContext:modContext.getElementNamespaceContexts()) {
				for (ElementClass ec:nsContext.getElementClasses()) {
					Class<?> objectClass = null;
					if (ec.getObjectClass()!=null) {
						objectClass = ec.getObjectClass();
						for (ElementLanguageModule mod:context.getElementLanguageModules()) {
							for (ElementNamespaceContext ns:mod.getElementNamespaceContexts()) {
								for (ElementClass checkClass:ns.getElementClasses()) {
									if (checkClass.getObjectClass()==null) {
										continue;
									}
									Class<?> checkObjectClass = checkClass.getObjectClass();
									List<ElementBindingHandler> b = context.findElementBindingHandlers(objectClass,checkObjectClass);
									if (b.isEmpty()==false) {
										startNamespace(ns.getUri(),ns.getSchemaPrefix());
									}
								}
							}
						}
					}
				}
			}
		}
	}

	
	private static final String COMMENT_SEPERATOR = " ==================================================================== ";
	private static final String COMMENT_TEXT = "=====";
	
	public void startSchema(ElementNamespaceContext ns,ElementLanguage elementLanguage) throws SAXException {
		
		xmlWriter.startDocument();
		
		// this is a mess;
		char[] msg;
		msg = "\n".toCharArray();
		xmlWriter.ignorableWhitespace(msg,0,msg.length);
		msg = COMMENT_SEPERATOR.toCharArray();
		xmlWriter.comment(msg,0,msg.length);
		String desc = "Automatic generated schema for language: "+elementLanguage.getLanguageConfiguration().getLanguage();
		int space = COMMENT_SEPERATOR.length()-desc.length()-(2*COMMENT_TEXT.length())-4;
		StringBuffer b = new StringBuffer(COMMENT_SEPERATOR.length());
		b.append(" ");
		b.append(COMMENT_TEXT);
		b.append("  ");
		b.append(desc);
		for (int i=0;i<space;i++) {
			b.append(' ');
		}
		b.append(COMMENT_TEXT);
		b.append(" ");
		
		msg = "\n".toCharArray();
		xmlWriter.ignorableWhitespace(msg,0,msg.length);
		msg = b.toString().toCharArray();
		xmlWriter.comment(msg,0,msg.length);
		msg = "\n".toCharArray();
		xmlWriter.ignorableWhitespace(msg,0,msg.length);
		msg = COMMENT_SEPERATOR.toCharArray();
		xmlWriter.comment(msg,0,msg.length);
		msg = "\n".toCharArray();
		xmlWriter.ignorableWhitespace(msg,0,msg.length);
		
		ElementLanguageModule module = null;
		for (ElementLanguageModule elm:elementLanguage.getElementLanguageModules()) {
			ElementNamespaceContext s = elm.getElementNamespaceContext(ns.getUri());
			if (s!=null) {
				module = elm;
				break;
			}
		}
		
		b = new StringBuffer(COMMENT_SEPERATOR.length());
		b.append("\n\tProviderName:\t");
		b.append(module.getProviderName());
		b.append("\n\tModuleName:\t\t");
		b.append(module.getName());
		b.append("\n\tNamespaces:\t\t");
		b.append(module.getElementNamespaceContexts().size());
		b.append("\n\tNamespace:\t\t");
		b.append(ns.getUri());
		b.append("\n\tCreated on:\t\t");
		b.append(new Date());
		b.append("\n");
		msg = b.toString().toCharArray();
		xmlWriter.comment(msg,0,msg.length);
		
		
		xmlWriter.startPrefixMapping("", SCHEMA_URI);
		
		for (String uri:namespaces.keySet()) {
			String prefix = namespaces.get(uri);
			xmlWriter.startPrefixMapping(prefix, uri);
		}
		
		AttributesImpl atts = new AttributesImpl();
		atts.addAttribute ("", "version", "", "", "1.0");
		atts.addAttribute ("", "elementFormDefault", "", "", "qualified");
		atts.addAttribute ("", "attributeFormDefault", "", "", "unqualified");
		atts.addAttribute ("", "targetNamespace", "", "", ns.getUri());
		xmlWriter.startElement (SCHEMA_URI, "schema", "", atts);
		
		for (String uri:namespaces.keySet()) {
			if (ns.getUri().equals(uri)) {
				continue;
			}
			ElementNamespaceContext nsContext = context.findElementNamespaceContext(uri);
			atts = new AttributesImpl();
			atts.addAttribute ("", "namespace", "", "", nsContext.getUri());
			atts.addAttribute ("", "schemaLocation", "", "", nsContext.getSchemaResource());
			xmlWriter.startElement (SCHEMA_URI, "import", "", atts);
			xmlWriter.endElement (SCHEMA_URI, "import", "");
		}
	}
	
	public void endSchema() throws SAXException {
		xmlWriter.endElement (SCHEMA_URI, "schema" , "");
		xmlWriter.endDocument();
	}
	
	public void writeElementClass(ElementClass ec,ElementNamespaceContext nsWrite) throws SAXException {
		
		AttributesImpl atts = new AttributesImpl();
		if (nsWrite.getLanguageRoot()!=null && nsWrite.getLanguageRoot()) {
			atts.addAttribute ("", "name", "", "", ec.getTag());
			xmlWriter.startElement (SCHEMA_URI, "element", "", atts);	// Only in the language root xsd there is an element.
			
			atts = new AttributesImpl();
			xmlWriter.startElement (SCHEMA_URI, "complexType", "", atts);
		} else {
			atts.addAttribute ("", "name", "", "", ec.getTag()+"Type");
			xmlWriter.startElement (SCHEMA_URI, "complexType", "", atts);
		}
		
		if (ec.getSchemaContentBase()!=null) {
			atts = new AttributesImpl();
			if (ec.getSchemaContentComplex()!=null && ec.getSchemaContentComplex()) {
				if (ec.getSchemaContentMixed()!=null && ec.getSchemaContentMixed()) {
					atts.addAttribute ("", "mixed", "", "", "true");
				}
				xmlWriter.startElement (SCHEMA_URI, "complexContent", "", atts);
			} else {
				xmlWriter.startElement (SCHEMA_URI, "simpleContent", "", atts);
			}
			atts = new AttributesImpl();
			atts.addAttribute ("", "base", "", "", ec.getSchemaContentBase());
			xmlWriter.startElement (SCHEMA_URI, "extension", "", atts);
		}
		
		if (ec.getSchemaContentBase()==null) {
			atts = new AttributesImpl();
			atts.addAttribute ("", "minOccurs", "", "", "0"); // make unordered elements
			atts.addAttribute ("", "maxOccurs", "", "", "unbounded");
			xmlWriter.startElement (SCHEMA_URI, "choice", "", atts);
			for (ElementLanguageModule mod:context.getElementLanguageModules()) {
				for (ElementNamespaceContext ns:mod.getElementNamespaceContexts()) {
					writeElementClassNamespaces(ec,nsWrite,ns);
				}
			}
			xmlWriter.endElement(SCHEMA_URI, "choice", "");
		}
		
		List<String> attrNames = new ArrayList<String>(30);
		for (ElementClassAttribute eca:ec.getElementClassAttributes()) {
			attrNames.add(eca.getName());
			atts = new AttributesImpl();
			atts.addAttribute ("", "name", "", "", eca.getName());
			atts.addAttribute ("", "type", "", "", "string");
			if (eca.getRequired()!=null && eca.getRequired()) {
				atts.addAttribute ("", "use", "", "", "required");	
			}
			xmlWriter.startElement (SCHEMA_URI, "attribute", "", atts);
			xmlWriter.endElement(SCHEMA_URI, "attribute", "");	
		}
		
		for (ElementLanguageModule mod:context.getElementLanguageModules()) {
			for (ElementAttributeHandler eah:mod.getElementAttributeHandlers()) {
				attrNames.add(eah.getAttributeName());
				atts = new AttributesImpl();
				atts.addAttribute ("", "name", "", "", eah.getAttributeName());
				atts.addAttribute ("", "type", "", "", "string");
				xmlWriter.startElement (SCHEMA_URI, "attribute", "", atts);
				xmlWriter.endElement(SCHEMA_URI, "attribute", "");	
			}
		}
		
		if (ec.getAutoAttributes()!=null && ec.getAutoAttributes()==false) {
			// oke, reverse this if and rm whitespace.
			char[] msg;
			msg = " ".toCharArray();
			xmlWriter.ignorableWhitespace(msg,0,msg.length);
			
		} else {
			
			if (ec.getObjectClass()!=null) {
				for (Method m:ec.getObjectClass().getMethods()) {
					if (m.getName().startsWith("set")) {
						String n = m.getName().substring(3);
						if (m.getParameterTypes().length==0) {
							continue; // set without parameters
						}
						if (n.length()<2) {
							continue;
						}
						n = n.substring(0,1).toLowerCase()+n.substring(1,n.length());
						if (attrNames.contains(n)) {
							continue;
						}
						atts = new AttributesImpl();
						atts.addAttribute ("", "name", "", "", n);
						
						Class<?> type = m.getParameterTypes()[0]; // TODO make full list for auto attribute type resolving.
						if (type.equals(Object.class)) {
							atts.addAttribute ("", "type", "", "", "string");// object is always string because is always assignable
						} else if (type.isAssignableFrom(Boolean.class) | type.isAssignableFrom(Boolean.TYPE)) {
							atts.addAttribute ("", "type", "", "", "boolean");
						} else if (type.isAssignableFrom(Integer.class) | type.isAssignableFrom(Integer.TYPE)) {
							atts.addAttribute ("", "type", "", "", "integer");
						} else if (type.isAssignableFrom(Long.class) | type.isAssignableFrom(Long.TYPE)) {
							atts.addAttribute ("", "type", "", "", "long");
						} else if (type.isAssignableFrom(Float.class) | type.isAssignableFrom(Float.TYPE)) {
							atts.addAttribute ("", "type", "", "", "float");
						} else if (type.isAssignableFrom(Double.class) | type.isAssignableFrom(Double.TYPE)) {
							atts.addAttribute ("", "type", "", "", "double");
						} else {
							atts.addAttribute ("", "type", "", "", "string");
						}
						xmlWriter.startElement (SCHEMA_URI, "attribute", "", atts);
						xmlWriter.endElement(SCHEMA_URI, "attribute", "");	
					}
				}
			} else {
				atts = new AttributesImpl();
				xmlWriter.startElement (SCHEMA_URI, "anyAttribute", "", atts);
				xmlWriter.endElement(SCHEMA_URI, "anyAttribute", "");
			}
		}
		if (ec.getSchemaContentBase()!=null) {
			xmlWriter.endElement(SCHEMA_URI, "extension", "");
			if (ec.getSchemaContentComplex()!=null && ec.getSchemaContentComplex()) {
				xmlWriter.endElement(SCHEMA_URI, "complexContent", "");
			} else {
				xmlWriter.endElement(SCHEMA_URI, "simpleContent", "");
			}
		}
		xmlWriter.endElement(SCHEMA_URI, "complexType", "");
		if (nsWrite.getLanguageRoot()!=null && nsWrite.getLanguageRoot()) {
			xmlWriter.endElement(SCHEMA_URI, "element", "");
		}
	}
	
	private void writeElementClassNamespaces(ElementClass ec,ElementNamespaceContext nsWrite,ElementNamespaceContext ns) throws SAXException {
		AttributesImpl atts = new AttributesImpl();
		List<String> refElements = new ArrayList<String>(20);
		for (ElementClass checkClass:ns.getElementClasses()) {
			List<String> parents = checkClass.getElementParents(nsWrite.getUri());
			if (parents!=null && parents.contains(ec.getTag())) {
				refElements.add(checkClass.getTag());
			}
			if (ec.getObjectClass()==null) {
				continue;
			}
			if (checkClass.getObjectClass()==null) {
				continue;
			}
			Class<?> objectClass = ec.getObjectClass();
			Class<?> checkObjectClass = checkClass.getObjectClass();
			List<ElementBindingHandler> b = context.findElementBindingHandlers(objectClass,checkObjectClass);
			if (b.isEmpty()==false) {
				refElements.add(checkClass.getTag());
			}
		}
		if (refElements.isEmpty()==false) {
			Set<String> s = new HashSet<String>(refElements.size());
			s.addAll(refElements);
			List<String> r = new ArrayList<String>(s.size());
			r.addAll(s);
			Collections.sort(r);
			
			String prefix = namespaces.get(ns.getUri());
			for (String refElement:r) {
				atts = new AttributesImpl();
				if (nsWrite.getLanguageRoot()!=null && nsWrite.getLanguageRoot()) {
					atts.addAttribute ("", "ref", "", "", prefix+":"+refElement);
				} else if (nsWrite.getUri().equals(ns.getUri())==false) {
					atts.addAttribute ("", "ref", "", "", prefix+":"+refElement);
				} else {
					atts.addAttribute ("", "name", "", "", refElement);
					atts.addAttribute ("", "type", "", "", prefix+":"+refElement+"Type");
				}
				xmlWriter.startElement (SCHEMA_URI, "element", "", atts);
				xmlWriter.endElement (SCHEMA_URI, "element", "");
			}
		}
	}
	
	
	public void writeElement(ElementClass ec,ElementNamespaceContext nsWrite) throws SAXException {
		if (nsWrite.getLanguageRoot()!=null && nsWrite.getLanguageRoot()) {
			return; // is done in writeElementClass
		}
		AttributesImpl atts = new AttributesImpl();
		atts.addAttribute ("", "name", "", "", ec.getTag());
		atts.addAttribute ("", "type", "", "", "this:"+ec.getTag()+"Type");
		xmlWriter.startElement(SCHEMA_URI, "element", "", atts);	// Only in the language root xsd there is an element.
		
		if (ec.getDescription()!=null) {
			atts = new AttributesImpl();
			xmlWriter.startElement(SCHEMA_URI, "annotation", "", atts);
			atts = new AttributesImpl();
			atts.addAttribute ("", "xml:lang", "", "", "en");
			xmlWriter.startElement(SCHEMA_URI, "documentation", "", atts);
			char[] msg = ec.getDescription().toCharArray();
			xmlWriter.characters(msg,0,msg.length);
			xmlWriter.endElement(SCHEMA_URI, "documentation", "");
			xmlWriter.endElement(SCHEMA_URI, "annotation", "");
		}
		
		
		xmlWriter.endElement(SCHEMA_URI, "element", "");
	}
}
