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
package org.x4o.xml.io.sax;

import org.x4o.xml.element.Element;
import org.x4o.xml.element.Element.ElementType;
import org.x4o.xml.element.ElementException;
import org.x4o.xml.element.ElementNamespace;
import org.x4o.xml.element.ElementNamespaceInstanceProvider;
import org.x4o.xml.io.DefaultX4OReader;
import org.x4o.xml.io.sax.ext.AttributeMap;
import org.x4o.xml.io.sax.ext.PropertyConfig;
import org.x4o.xml.lang.X4OLanguageSession;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ext.DefaultHandler2;

import java.util.Map;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * X4OTagHandler Gets all SAX content handler events and converts to x4o element tree.
 * 
 * @author Willem Cazander
 * @version 1.0 Aug 20, 2005
 */
public class X4OContentHandler extends DefaultHandler2 {
	
	/** The logger to log to */
	private Logger logger = null;
	private boolean loggerFinest = false;
	
	/** The Locator for parse errors */
	private Locator locator = null;
	
	/** The element Stack */
	private Stack<Element> elementStack = null;
	
	/** The elememtContext */
	private X4OLanguageSession elementLanguage = null;
	
	/** Store the override element */
	private Element overrideSaxElement = null;
	
	/** Store the override element handler */
	private DefaultHandler2 overrideSaxHandler = null;
	
	private final PropertyConfig propertyConfig;
	
	/**
	 * Creates an X4OTagHandler 
	 * which can receice sax xml events and converts them into the Element* interfaces  events.
	 */
	public X4OContentHandler(X4OLanguageSession elementLanguage,PropertyConfig propertyConfig) {
		logger = Logger.getLogger(X4OContentHandler.class.getName());
		loggerFinest = logger.isLoggable(Level.FINEST);
		elementStack = new Stack<Element>();
		this.elementLanguage = elementLanguage;
		this.propertyConfig = propertyConfig;
	}

	/**
	 * @see org.xml.sax.helpers.DefaultHandler#setDocumentLocator(org.xml.sax.Locator)
	 */
	@Override
	public void setDocumentLocator(Locator locator) {
		this.locator=locator;
	}
	
	/**
	 * @see org.xml.sax.helpers.DefaultHandler#startPrefixMapping(java.lang.String, java.lang.String)
	 */
	@Override
	public void startPrefixMapping(String prefix, String namespaceUri) throws SAXException {
		if (overrideSaxHandler!=null) {
			overrideSaxHandler.startPrefixMapping(prefix, namespaceUri);
			return;
		}
		if ("http://www.w3.org/2001/XMLSchema-instance".equals(namespaceUri)) {
			return; // skip schema ns if non validating
		}
		if ("http://www.w3.org/2001/XInclude".equals(namespaceUri)) {
			return; // skip xinclude ns.
		}
		ElementNamespace enc = elementLanguage.getLanguage().findElementNamespace(namespaceUri);
		if (enc==null) {
			throw new SAXException("Can't find namespace uri: "+namespaceUri+" in language: "+elementLanguage.getLanguage().getLanguageName());
		}
		enc.setPrefixMapping(prefix);
	}

	/**
	 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	public void startElement(String namespaceUri, String tag, String qName,Attributes attributes) throws SAXException {
		if (loggerFinest) {
			logger.finest("XMLTAG-START: "+namespaceUri+":"+tag);
		}
		if (overrideSaxHandler!=null) {
			overrideSaxHandler.startElement(namespaceUri, tag, qName, attributes);
			return;
		}
		ElementNamespace enc = elementLanguage.getLanguage().findElementNamespace(namespaceUri);
		if (enc==null) {
			if ("".equals(namespaceUri)) {
				String configEmptryUri = propertyConfig.getPropertyString(DefaultX4OReader.DOC_EMPTY_NAMESPACE_URI);
				if (configEmptryUri!=null) {
					namespaceUri = configEmptryUri;
					enc = elementLanguage.getLanguage().findElementNamespace(namespaceUri);
				}
				if (enc==null) {	
					throw new SAXParseException("No ElementNamespace found for empty namespace.",locator);
				}
				enc.setPrefixMapping("");
			}
			if (enc==null) {	
				throw new SAXParseException("No ElementProvider found for namespace: "+namespaceUri,locator);
			}
		}
		
		ElementNamespaceInstanceProvider eip = enc.getElementNamespaceInstanceProvider();
		Element element = null;
		try {
			element = eip.createElementInstance(elementLanguage,tag);
		} catch (Exception e) {
			throw new SAXParseException("Error while creating element: "+e.getMessage(),locator,e);
		}
		enc.addElementClass(element.getElementClass());
		
		// next bind them togeter.
		if (elementStack.empty()) {
			elementLanguage.setRootElement(element); // root element !!
		} else {
			Element parent = elementStack.peek();
			element.setParent(parent); // set parent element
			parent.addChild(element);
		}
		
		// create attribute map
		Map<String,String> map = new AttributeMap<String,String>(attributes);
		element.getAttributes().putAll(map);
		
		elementStack.push(element);
		try {
			element.doElementStart();
		} catch (ElementException ee) {
			throw new SAXParseException("Error while configing element: "+ee.getMessage(),locator,ee);
		}
		if (ElementType.overrideSax.equals(element.getElementType())) {
			overrideSaxElement = element;
			overrideSaxHandler = (DefaultHandler2)element.getElementObject();
		}
	}
	
	/**
	 * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void endElement(String namespaceUri, String tag,String qName) throws SAXException {
		if (loggerFinest) {
			logger.finest("XMLTAG-END: "+namespaceUri+":"+tag);
		}
		if (overrideSaxHandler!=null) {
			if (overrideSaxElement.getElementClass().getId().equals(tag)) {
				overrideSaxHandler.endDocument();
				overrideSaxHandler = null;
				overrideSaxElement = null; // elementStack code make sure doElementEnd is runned on override element.
			} else {
				overrideSaxHandler.endElement(namespaceUri, tag, qName);
				return;
			}
		}
		if (elementStack.empty()) {
			return;
		}
		Element element = elementStack.pop();
		try {
			element.doElementEnd();
		} catch (ElementException ee) {
			throw new SAXParseException("Error while configing element: '"+tag+"' "+ee.getMessage(),locator,ee);
		}
	}
	
	/**
	 * Gets called to pass the text between XML-tags and converts it to a String.
	 * When this string is 0 length then nothing is done.
	 * If there are no element on the stact noting is done.
	 * 
	 * @see org.xml.sax.helpers.DefaultHandler#characters(char[],int,int)
	 */
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		if (overrideSaxHandler!=null) {
			overrideSaxHandler.characters(ch, start, length);
			return;
		}
		if (length==0) {
			return; // no text
		}
		String text = new String(ch,start,length);
		if (text.length()==0) {
			return; // no text
		}
		if (elementStack.isEmpty()) {
			return;  // no element
		}
		Element e = elementStack.peek();
		try {
			e.doCharacters(text);
		} catch (ElementException ee) {
			throw new SAXParseException("Error while doCharacters element: '"+e.getElementClass().getId()+"' "+ee.getMessage(),locator,ee);
		}
	}

	/**
	 * @see org.xml.sax.helpers.DefaultHandler#ignorableWhitespace(char[], int, int)
	 */
	@Override
	public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
		if (overrideSaxHandler!=null) {
			overrideSaxHandler.ignorableWhitespace(ch, start, length);
			return;
		}
		String text = new String(ch);
		text = text.substring(start, start + length);
		if (text.length()==0) {
			return; // no text
		}
		if (elementStack.empty()) {
			return;  // no element
		}
		Element e = elementStack.peek();
		try {
			e.doIgnorableWhitespace(text);
		} catch (ElementException ee) {
			throw new SAXParseException("Error while doIgnorableWhitespace element: '"+e.getElementClass().getId()+"' "+ee.getMessage(),locator,ee);
		}
	}

	/**
	 * @see org.xml.sax.ext.DefaultHandler2#comment(char[], int, int)
	 */
	@Override
	public void comment(char[] ch, int start, int length) throws SAXException {
		if (overrideSaxHandler!=null) {
			overrideSaxHandler.comment(ch, start, length);
			return;
		}
		String text = new String(ch);
		text = text.substring(start, start + length);
		if (text.length()==0) {
			return; // no text
		}
		if (elementStack.empty()) {
			return;  // no element
		}
		Element e = elementStack.peek();
		try {
			e.doComment(text);
		} catch (ElementException ee) {
			throw new SAXParseException("Error while doComment element: '"+e.getElementClass().getId()+"' "+ee.getMessage(),locator,ee);
		}
	}
	
	/**
	 * @see org.xml.sax.helpers.DefaultHandler#processingInstruction(java.lang.String, java.lang.String)
	 */
	@Override
	public void processingInstruction(String target, String data) throws SAXException {
		logger.fine("Skipping process instuctions: "+target+" data: "+data);
	}
}
