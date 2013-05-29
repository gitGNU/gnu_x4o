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
import org.x4o.xml.element.ElementInterface;
import org.x4o.xml.element.ElementMetaBase;
import org.x4o.xml.element.ElementNamespaceContext;
import org.x4o.xml.io.XMLConstants;
import org.x4o.xml.io.sax.ext.ContentWriterXsd;
import org.x4o.xml.lang.X4OLanguageModule;
import org.x4o.xml.lang.X4OLanguage;
import org.xml.sax.SAXException;
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
	
	
	static public final String SCHEMA_URI = XMLConstants.XML_SCHEMA_NS_URI;

	protected X4OLanguage language = null;
	protected ContentWriterXsd xmlWriter = null;
	protected String writeNamespace = null;
	protected Map<String, String> namespaces = null;
	
	public EldXsdXmlWriter(ContentWriterXsd xmlWriter,X4OLanguage language) {
		this.xmlWriter=xmlWriter;
		this.language=language;
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
		for (X4OLanguageModule modContext:language.getLanguageModules()) {
			for (ElementNamespaceContext nsContext:modContext.getElementNamespaceContexts()) {
				for (ElementClass ec:nsContext.getElementClasses()) {
					Class<?> objectClass = null;
					if (ec.getObjectClass()!=null) {
						objectClass = ec.getObjectClass();
						for (X4OLanguageModule mod:language.getLanguageModules()) {
							for (ElementNamespaceContext ns:mod.getElementNamespaceContexts()) {
								for (ElementClass checkClass:ns.getElementClasses()) {
									if (checkClass.getObjectClass()==null) {
										continue;
									}
									Class<?> checkObjectClass = checkClass.getObjectClass();
									List<ElementBindingHandler> b = language.findElementBindingHandlers(objectClass,checkObjectClass);
									if (b.isEmpty()==false) {
										startNamespace(ns.getUri(),ns.getSchemaPrefix());
									}
								}
							}
						}
						for (ElementInterface ei:language.findElementInterfaces(objectClass)) {
							List<String> eiTags = ei.getElementParents(namespaceUri);
							if (eiTags!=null) {
								startNamespace(nsContext.getUri(),nsContext.getSchemaPrefix());
							}
						}
					}
				}
			}
		}
	}

	
	private static final String COMMENT_SEPERATOR = " ==================================================================== ";
	private static final String COMMENT_TEXT = "=====";
	
	public void startSchema(ElementNamespaceContext ns) throws SAXException {
		
		xmlWriter.startDocument();
		xmlWriter.ignorableWhitespace(XMLConstants.CHAR_NEWLINE);
		xmlWriter.comment(COMMENT_SEPERATOR);
		xmlWriter.ignorableWhitespace(XMLConstants.CHAR_NEWLINE);
		
		// this is a mess;
		String desc = "Automatic generated schema for language: "+language.getLanguageName();
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
		xmlWriter.comment(b.toString());
		xmlWriter.ignorableWhitespace(XMLConstants.CHAR_NEWLINE);
		xmlWriter.comment(COMMENT_SEPERATOR);
		xmlWriter.ignorableWhitespace(XMLConstants.CHAR_NEWLINE);
		
		X4OLanguageModule module = null;
		for (X4OLanguageModule elm:language.getLanguageModules()) {
			ElementNamespaceContext s = elm.getElementNamespaceContext(ns.getUri());
			if (s!=null) {
				module = elm;
				break;
			}
		}
		
		b = new StringBuffer(COMMENT_SEPERATOR.length());
		b.append("\n\tID:\t\t");			b.append(module.getId());
		b.append("\n\tProviderName:\t");	b.append(module.getProviderName());
		b.append("\n\tProviderHost:\t");	b.append(module.getProviderHost());
		b.append("\n\tNamespaces:\t\t");	b.append(module.getElementNamespaceContexts().size());
		b.append("\n\tUri:\t\t\t");			b.append(ns.getUri());
		b.append("\n\tUri schema:\t");		b.append(ns.getSchemaUri());
		b.append("\n\tCreated on:\t\t");	b.append(new Date());
		b.append("\n");
		xmlWriter.comment(b.toString());
		
		
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
			ElementNamespaceContext nsContext = language.findElementNamespaceContext(uri);
			atts = new AttributesImpl();
			atts.addAttribute ("", "namespace", "", "", nsContext.getUri());
			atts.addAttribute ("", "schemaLocation", "", "", nsContext.getSchemaResource());
			xmlWriter.startElement (SCHEMA_URI, "import", "", atts);
			xmlWriter.endElement (SCHEMA_URI, "import", "");
		}
	}
	
	public void endSchema() throws SAXException {
		xmlWriter.endElement (SCHEMA_URI, "schema" , "");
		xmlWriter.ignorableWhitespace(XMLConstants.CHAR_NEWLINE);
		xmlWriter.endDocument();
	}
	
	public void writeElementClass(ElementClass ec,ElementNamespaceContext nsWrite) throws SAXException {
		
		AttributesImpl atts = new AttributesImpl();
		if (nsWrite.getLanguageRoot()!=null && nsWrite.getLanguageRoot()) {
			atts.addAttribute ("", "name", "", "", ec.getId());
			xmlWriter.startElement (SCHEMA_URI, "element", "", atts);	// Only in the language root xsd there is an element.
			
			atts = new AttributesImpl();
			xmlWriter.startElement (SCHEMA_URI, "complexType", "", atts);
		} else {
			atts.addAttribute ("", "name", "", "", ec.getId()+"Type");
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
			atts.addAttribute ("", "minOccurs", "", "", "0"); // TODO: make unordered elements
			atts.addAttribute ("", "maxOccurs", "", "", "unbounded");
			xmlWriter.startElement (SCHEMA_URI, "choice", "", atts);
			
			for (X4OLanguageModule mod:language.getLanguageModules()) {
				for (ElementNamespaceContext ns:mod.getElementNamespaceContexts()) {
					writeElementClassNamespaces(ec,nsWrite,ns);
				}
			}
			xmlWriter.endElement(SCHEMA_URI, "choice", "");
		}
		
		List<String> attrNames = new ArrayList<String>(30);
		for (ElementClassAttribute eca:ec.getElementClassAttributes()) {
			attrNames.add(eca.getId());
			atts = new AttributesImpl();
			atts.addAttribute ("", "name", "", "", eca.getId());
			atts.addAttribute ("", "type", "", "", "string");
			if (eca.getRequired()!=null && eca.getRequired()) {
				atts.addAttribute ("", "use", "", "", "required");	
			}
			writeElementAttribute(eca,atts);
			
			for (String alias:eca.getAttributeAliases()) {
				attrNames.add(alias);
				atts = new AttributesImpl();
				atts.addAttribute ("", "name", "", "", alias);
				atts.addAttribute ("", "type", "", "", "string");
				writeElementAttribute(null,atts);
			}
		}
		
		for (X4OLanguageModule mod:language.getLanguageModules()) {
			for (ElementAttributeHandler eah:mod.getElementAttributeHandlers()) {
				attrNames.add(eah.getAttributeName());
				atts = new AttributesImpl();
				atts.addAttribute ("", "name", "", "", eah.getAttributeName());
				atts.addAttribute ("", "type", "", "", "string");
				writeElementAttribute(eah,atts);	
			}
		}
		
		if (ec.getAutoAttributes()!=null && ec.getAutoAttributes()==false) {
			// oke, reverse this if and rm whitespace.
			xmlWriter.ignorableWhitespace(' ');
			
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
	
	private void writeElementClassNamespaces(ElementClass ecWrite,ElementNamespaceContext nsWrite,ElementNamespaceContext ns) throws SAXException {
		AttributesImpl atts = new AttributesImpl();
		List<String> refElements = new ArrayList<String>(20);
		for (ElementClass checkClass:ns.getElementClasses()) {
			List<String> parents = checkClass.getElementParents(nsWrite.getUri());
			if (parents!=null && parents.contains(ecWrite.getId())) {
				refElements.add(checkClass.getId());
				continue;
			}
			if (checkClass.getObjectClass()==null) {
				continue;
			}
			for (ElementInterface ei:language.findElementInterfaces(checkClass.getObjectClass())) {
				parents = ei.getElementParents(nsWrite.getUri());
				if (parents!=null && parents.contains(ecWrite.getId())) {
					refElements.add(checkClass.getId());
					break;
				}
			}
			if (ecWrite.getObjectClass()==null) {
				continue;
			}
			Class<?> objectClass = ecWrite.getObjectClass();
			Class<?> checkObjectClass = checkClass.getObjectClass();
			List<ElementBindingHandler> b = language.findElementBindingHandlers(objectClass,checkObjectClass);
			if (b.isEmpty()==false) {
				refElements.add(checkClass.getId());
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
		atts.addAttribute ("", "name", "", "", ec.getId());
		atts.addAttribute ("", "type", "", "", "this:"+ec.getId()+"Type");
		xmlWriter.startElement(SCHEMA_URI, "element", "", atts);
		writeElementMetaBase(ec);
		xmlWriter.endElement(SCHEMA_URI, "element", "");
	}
	
	private void writeElementAttribute(ElementMetaBase base,AttributesImpl atts) throws SAXException {
		xmlWriter.startElement (SCHEMA_URI, "attribute", "", atts);
		writeElementMetaBase(base);
		xmlWriter.endElement(SCHEMA_URI, "attribute", "");	
	}
	
	private void  writeElementMetaBase(ElementMetaBase base) throws SAXException {
		if (base==null) {
			return;
		}
		if (base.getDescription()==null) {
			return;
		}
		AttributesImpl atts = new AttributesImpl();
		xmlWriter.startElement(SCHEMA_URI, "annotation", "", atts);
		atts = new AttributesImpl();
		atts.addAttribute ("", "xml:lang", "", "", "en");
		xmlWriter.startElement(SCHEMA_URI, "documentation", "", atts);
		xmlWriter.characters(base.getDescription());
		xmlWriter.endElement(SCHEMA_URI, "documentation", "");
		xmlWriter.endElement(SCHEMA_URI, "annotation", "");
	}
}
