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

import org.x4o.xml.element.ElementBindingHandler;
import org.x4o.xml.element.ElementClass;
import org.x4o.xml.element.ElementClassAttribute;
import org.x4o.xml.element.ElementInterface;
import org.x4o.xml.element.ElementMetaBase;
import org.x4o.xml.element.ElementNamespace;
import org.x4o.xml.element.ElementNamespaceAttribute;
import org.x4o.xml.io.XMLConstants;
import org.x4o.xml.io.sax.ext.ContentWriterXsd;
import org.x4o.xml.io.sax.ext.ContentWriterXsd.Tag;
import org.x4o.xml.io.sax.ext.PropertyConfig;
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
public class EldXsdWriterElement {
	
	
	static public final String SCHEMA_URI = XMLConstants.XML_SCHEMA_NS_URI;

	private PropertyConfig propertyConfig;
	protected X4OLanguage language = null;
	protected ContentWriterXsd xsdWriter = null;
	protected String writeNamespace = null;
	protected Map<String, String> namespaces = null;
	
	public EldXsdWriterElement(ContentWriterXsd xsdWriter,X4OLanguage language,PropertyConfig propertyConfig) {
		this.xsdWriter=xsdWriter;
		this.language=language;
		this.propertyConfig=propertyConfig;
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
			for (ElementNamespace nsContext:modContext.getElementNamespaces()) {
				for (ElementClass ec:nsContext.getElementClasses()) {
					Class<?> objectClass = null;
					if (ec.getObjectClass()!=null) {
						objectClass = ec.getObjectClass();
						for (X4OLanguageModule mod:language.getLanguageModules()) {
							for (ElementNamespace ns:mod.getElementNamespaces()) {
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
	
	private void prologWriteGenerator() throws SAXException {
		if (!propertyConfig.getPropertyBoolean(EldXsdWriter.PROLOG_GENERATED_BY_ENABLE)) {
			return;
		}
		//xsdWriter.ignorableWhitespace(XMLConstants.CHAR_NEWLINE);
		xsdWriter.comment(COMMENT_SEPERATOR);

		String byValue = propertyConfig.getPropertyStringOrValue(EldXsdWriter.PROLOG_GENERATED_BY, EldXsdWriter.class.getSimpleName());
		if (!byValue.endsWith(".")) {
			byValue += '.';
		}
		
		// this is a mess;
		String desc = "Automatic generated schema for language: "+language.getLanguageName()+" by "+byValue;
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
		xsdWriter.comment(b.toString());
		xsdWriter.comment(COMMENT_SEPERATOR);
	}
	
	private void prologWriteProvider(ElementNamespace ns) throws SAXException {
		if (!propertyConfig.getPropertyBoolean(EldXsdWriter.PROLOG_PROVIDER_INFO_ENABLE)) {
			return;
		}
		X4OLanguageModule module = null;
		for (X4OLanguageModule elm:language.getLanguageModules()) {
			ElementNamespace s = elm.getElementNamespace(ns.getUri());
			if (s!=null) {
				module = elm;
				break;
			}
		}
		StringBuffer b = new StringBuffer(COMMENT_SEPERATOR.length());
		String formatLine = "\n\t%1$-20s %2$s";
		b.append(String.format(formatLine,"Id:",module.getId()));
		b.append(String.format(formatLine,"ProviderName:",module.getProviderName()));
		b.append(String.format(formatLine,"ProviderHost:",module.getProviderHost()));
		b.append(String.format(formatLine,"Namespaces:",module.getElementNamespaces().size()));
		b.append(String.format(formatLine,"Uri:",ns.getUri()));
		b.append(String.format(formatLine,"Uri schema",ns.getSchemaUri()));
		b.append(String.format(formatLine,"Created on:",new Date()));
		b.append("\n");
		
		xsdWriter.comment(b.toString());
	}
	
	public void startSchema(ElementNamespace ns) throws SAXException {
		
		xsdWriter.startDocument();
		
		prologWriteGenerator();
		prologWriteProvider(ns);
		
		xsdWriter.startPrefixMapping("", SCHEMA_URI);
		for (String uri:namespaces.keySet()) {
			String prefix = namespaces.get(uri);
			xsdWriter.startPrefixMapping(prefix, uri);
		}
		
		AttributesImpl atts = new AttributesImpl();
		atts.addAttribute ("", "version", "", "", "1.0");
		atts.addAttribute ("", "elementFormDefault", "", "", "qualified");
		atts.addAttribute ("", "attributeFormDefault", "", "", "unqualified");
		atts.addAttribute ("", "targetNamespace", "", "", ns.getUri());
		xsdWriter.startElement (SCHEMA_URI, "schema", "", atts);
		
		for (String uri:namespaces.keySet()) {
			if (ns.getUri().equals(uri)) {
				continue;
			}
			ElementNamespace nsContext = language.findElementNamespace(uri);
			xsdWriter.printXsdImport(nsContext.getUri(), nsContext.getSchemaResource());
		}
		
		writeNamespaceAttributes(ns);
	}
	
	public void endSchema() throws SAXException {
		xsdWriter.endElement (SCHEMA_URI, "schema" , "");
		xsdWriter.ignorableWhitespace(XMLConstants.CHAR_NEWLINE);
		xsdWriter.endDocument();
	}
	
	private void writeNamespaceAttributes(ElementNamespace ns) throws SAXException {
		for (ElementNamespaceAttribute eah:ns.getElementNamespaceAttributes()) {
			AttributesImpl atts = new AttributesImpl();
			atts.addAttribute ("", "name", "", "", eah.getAttributeName());
			atts.addAttribute ("", "type", "", "", "string");
			writeElementAttribute(eah,atts);	
		}
	}
	
	public void writeElementClass(ElementClass ec,ElementNamespace nsWrite) throws SAXException {
		
		AttributesImpl atts = new AttributesImpl();
		if (nsWrite.getLanguageRoot()!=null && nsWrite.getLanguageRoot()) {
			atts.addAttribute ("", "name", "", "", ec.getId());
			xsdWriter.startElement (SCHEMA_URI, "element", "", atts);	// Only in the language root xsd there is an element.
			
			atts = new AttributesImpl();
			xsdWriter.startElement (SCHEMA_URI, "complexType", "", atts);
		} else {
			atts.addAttribute ("", "name", "", "", ec.getId()+"Type");
			xsdWriter.startElement (SCHEMA_URI, "complexType", "", atts);
		}
		
		if (ec.getSchemaContentBase()!=null) {
			atts = new AttributesImpl();
			if (ec.getSchemaContentComplex()!=null && ec.getSchemaContentComplex()) {
				if (ec.getSchemaContentMixed()!=null && ec.getSchemaContentMixed()) {
					atts.addAttribute ("", "mixed", "", "", "true");
				}
				xsdWriter.startElement (SCHEMA_URI, "complexContent", "", atts);
			} else {
				xsdWriter.startElement (SCHEMA_URI, "simpleContent", "", atts);
			}
			atts = new AttributesImpl();
			atts.addAttribute ("", "base", "", "", ec.getSchemaContentBase());
			xsdWriter.startElement (SCHEMA_URI, "extension", "", atts);
		}
		
		if (ec.getSchemaContentBase()==null) {
			atts = new AttributesImpl();
			atts.addAttribute ("", "minOccurs", "", "", "0"); // TODO: make unordered elements
			atts.addAttribute ("", "maxOccurs", "", "", "unbounded");
			xsdWriter.startElement (SCHEMA_URI, "choice", "", atts);
			
			for (X4OLanguageModule mod:language.getLanguageModules()) {
				for (ElementNamespace ns:mod.getElementNamespaces()) {
					writeElementClassNamespaces(ec,nsWrite,ns);
				}
			}
			xsdWriter.endElement(SCHEMA_URI, "choice", "");
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
		
		if (ec.getAutoAttributes()!=null && ec.getAutoAttributes()==false) {
			// oke, reverse this if and rm whitespace.
			xsdWriter.ignorableWhitespace(' ');
			
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
						attrNames.add(n);
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
						xsdWriter.startElement (SCHEMA_URI, "attribute", "", atts);
						xsdWriter.endElement(SCHEMA_URI, "attribute", "");	
					}
				}
			} else {
				atts = new AttributesImpl();
				xsdWriter.startElement (SCHEMA_URI, "anyAttribute", "", atts);
				xsdWriter.endElement(SCHEMA_URI, "anyAttribute", "");
			}
		}
		if (ec.getSchemaContentBase()!=null) {
			xsdWriter.endElement(SCHEMA_URI, "extension", "");
			if (ec.getSchemaContentComplex()!=null && ec.getSchemaContentComplex()) {
				xsdWriter.endElement(SCHEMA_URI, "complexContent", "");
			} else {
				xsdWriter.endElement(SCHEMA_URI, "simpleContent", "");
			}
		}
		xsdWriter.endElement(SCHEMA_URI, "complexType", "");
		if (nsWrite.getLanguageRoot()!=null && nsWrite.getLanguageRoot()) {
			xsdWriter.endElement(SCHEMA_URI, "element", "");
		}
	}
	
	private void writeElementClassNamespaces(ElementClass ecWrite,ElementNamespace nsWrite,ElementNamespace ns) throws SAXException {
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
				xsdWriter.startElement (SCHEMA_URI, "element", "", atts);
				xsdWriter.endElement (SCHEMA_URI, "element", "");
			}
		}
	}
	
	
	public void writeElement(ElementClass ec,ElementNamespace nsWrite) throws SAXException {
		if (nsWrite.getLanguageRoot()!=null && nsWrite.getLanguageRoot()) {
			return; // is done in writeElementClass
		}
		AttributesImpl atts = new AttributesImpl();
		atts.addAttribute ("", "name", "", "", ec.getId());
		atts.addAttribute ("", "type", "", "", "this:"+ec.getId()+"Type");
		xsdWriter.startElement(SCHEMA_URI, "element", "", atts);
		writeElementMetaBase(ec);
		xsdWriter.endElement(SCHEMA_URI, "element", "");
	}
	
	private void writeElementAttribute(ElementMetaBase base,AttributesImpl atts) throws SAXException {
		xsdWriter.printTagStart(Tag.attribute,atts);
		writeElementMetaBase(base);
		xsdWriter.printTagEnd(Tag.attribute);
	}
	
	private void  writeElementMetaBase(ElementMetaBase base) throws SAXException {
		if (base==null) {
			return;
		}
		if (base.getDescription()==null) {
			return;
		}
		if (!propertyConfig.getPropertyBoolean(EldXsdWriter.OUTPUT_DOCUMENTATION)) {
			return;
		}
		xsdWriter.printXsdDocumentation(base.getDescription());
	}
}
