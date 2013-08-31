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
package	org.x4o.xml.io.sax.ext;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.x4o.xml.io.XMLConstants;
import org.x4o.xml.io.sax.ext.PropertyConfig.PropertyConfigItem;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * AbstractContentWriterHandler writes SAX content handler events as XML.
 * 
 * @author Willem Cazander
 * @version 1.0 May 3, 2013
 */
public class AbstractContentWriterHandler implements ContentHandler { 
	
	protected final Attributes EMPTY_ATTRIBUTES = new AttributesImpl();
	private final PropertyConfig propertyConfig;
	private Writer out = null;
	private int indent = 0;
	private Map<String,String> prefixMapping = null;
	private List<String> printedMappings = null;
	private StringBuffer startElement = null;
	private boolean printReturn = false;
	private String lastElement = null;
	private Stack<String> elements = null;

	private final static String PROPERTY_CONTEXT_PREFIX = PropertyConfig.X4O_PROPERTIES_PREFIX+PropertyConfig.X4O_PROPERTIES_WRITER_XML;
	public final static PropertyConfig DEFAULT_PROPERTY_CONFIG;
	public final static String OUTPUT_ENCODING = PROPERTY_CONTEXT_PREFIX+"output/encoding";
	public final static String OUTPUT_CHAR_TAB = PROPERTY_CONTEXT_PREFIX+"output/charTab";
	public final static String OUTPUT_CHAR_NEWLINE = PROPERTY_CONTEXT_PREFIX+"output/newLine";
	
	static {
		DEFAULT_PROPERTY_CONFIG = new PropertyConfig(true,null,PROPERTY_CONTEXT_PREFIX,
				new PropertyConfigItem(OUTPUT_ENCODING,String.class,XMLConstants.XML_DEFAULT_ENCODING),
				new PropertyConfigItem(OUTPUT_CHAR_TAB,String.class,XMLConstants.CHAR_TAB+""),
				new PropertyConfigItem(OUTPUT_CHAR_NEWLINE,String.class,XMLConstants.CHAR_NEWLINE+"")
				);
	}
	
	/**
	 * Creates XmlWriter which prints to the Writer interface.
	 * @param out	The writer to print the xml to.
	 */
	public AbstractContentWriterHandler(Writer out) {
		if (out==null) {
			throw new NullPointerException("Can't write on null writer.");
		}
		this.out = out;
		prefixMapping = new HashMap<String,String>(15);
		printedMappings = new ArrayList<String>(15);
		elements = new Stack<String>();
		propertyConfig = new PropertyConfig(DEFAULT_PROPERTY_CONFIG,PROPERTY_CONTEXT_PREFIX);
	}
	
	public PropertyConfig getPropertyConfig() {
		return propertyConfig;
	}
	
	// TODO: check location of this. (add to api?)
	public void closeWriter() throws IOException {
		if (out==null) {
			return;
		}
		out.close();
	}
	
	public void closeWriterSafe() {
		try {
			closeWriter();
		} catch (IOException e) {
			e.getMessage(); // discard exception
		}
	}
	
	/**
	 * @see org.xml.sax.ContentHandler#startDocument()
	 */
	public void startDocument() throws SAXException {
		indent = 0;
		write(XMLConstants.getDocumentDeclaration(getPropertyConfig().getPropertyString(OUTPUT_ENCODING)));
	}
	
	/**
	 * @see org.xml.sax.ContentHandler#endDocument()
	 */
	public void endDocument() throws SAXException {
		writeFlush();
		if (elements.size()>0) {
			throw new SAXException("Invalid xml still have "+elements.size()+" elements open.");
		}
	}
	
	/**
	 * @param uri	The xml namespace uri.
	 * @param localName	The local name of the xml tag.
	 * @param name The (full) name of the xml tag.
	 * @param atts The attributes of the xml tag. 
	 * @see org.xml.sax.ContentHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	public void startElement(String uri, String localName, String name,Attributes atts) throws SAXException {
		if (localName==null) {
			throw new SAXException("LocalName may not be null.");
		}
		if (XMLConstants.isNameString(localName)==false) {
			throw new SAXException("LocalName of element is not valid in xml; '"+localName+"'");
		}
		autoCloseStartElement();
		startElement = new StringBuffer(200);
		startElement.append(getPropertyConfig().getPropertyString(OUTPUT_CHAR_NEWLINE));
		for (int i = 0; i < indent; i++) {
			startElement.append(getPropertyConfig().getPropertyString(OUTPUT_CHAR_TAB));
		}
		startElement.append(XMLConstants.TAG_OPEN);
		
		startElementTag(uri,localName);
		startElementNamespace(uri);
		startElementAttributes(atts);
		startElement.append(XMLConstants.TAG_CLOSE);
		indent++;
		lastElement = localName;
		elements.push(localName);
	}
	
	public void startElementTag(String uri,String localName) throws SAXException {
		if (XMLConstants.NULL_NS_URI.equals(uri) | uri==null) {
			startElement.append(localName);
		} else {
			String prefix = prefixMapping.get(uri);
			if (prefix==null) {
				throw new SAXException("preFixUri: "+uri+" is not started.");
			}
			if (XMLConstants.NULL_NS_URI.equals(prefix)==false) {
				startElement.append(prefix);
				startElement.append(XMLConstants.XMLNS_ASSIGN);
			}
			startElement.append(localName);
		}
	}
	
	public void startElementNamespace(String uri) throws SAXException {
		if ((uri!=null & XMLConstants.NULL_NS_URI.equals(uri)==false) && printedMappings.contains(uri)==false) {
			String prefix = prefixMapping.get(uri);
			if (prefix==null) {
				throw new SAXException("preFixUri: "+uri+" is not started.");
			}
			printedMappings.add(uri);
			
			startElement.append(' ');
			startElement.append(XMLConstants.XMLNS_ATTRIBUTE);
			if ("".equals(prefix)==false) {
				startElement.append(':');
				startElement.append(prefix);
			}
			startElement.append("=\"");
			startElement.append(uri);
			startElement.append('"');
			
			startElementNamespaceAll(uri);
		}
	}
	
	public void startElementNamespaceAll(String uri) throws SAXException {
		String prefix = null;
		boolean first = true;
		for (String uri2:prefixMapping.keySet()) {
			if (printedMappings.contains(uri2)==false) {
				prefix = prefixMapping.get(uri2);
				if (prefix==null) {
					throw new SAXException("preFixUri: "+uri+" is not started.");
				}
				printedMappings.add(uri2);
				
				if (first) {
					startElement.append(getPropertyConfig().getPropertyString(OUTPUT_CHAR_NEWLINE));
					first = false;
				}
				
				startElement.append(' ');
				startElement.append(XMLConstants.XMLNS_ATTRIBUTE);
				if ("".equals(prefix)==false) {
					startElement.append(XMLConstants.XMLNS_ASSIGN);
					startElement.append(prefix);
				}
				startElement.append("=\"");
				startElement.append(uri2);
				startElement.append('"');
				startElement.append(getPropertyConfig().getPropertyString(OUTPUT_CHAR_NEWLINE));
			}
		}
	}
	
	private void startElementAttributes(Attributes atts) throws SAXException {
		for (int i=0;i<atts.getLength();i++) {
			String attributeUri = atts.getURI(i);
			String attributeName = XMLConstants.escapeAttributeName(atts.getLocalName(i));
			String attributeValue = atts.getValue(i);
			if (attributeValue==null) {
				attributeValue = "null"; // TODO: Add null value key to config.
			}
			String attributeValueSafe = XMLConstants.escapeAttributeValue(attributeValue);
			
			startElement.append(' ');
			if (XMLConstants.NULL_NS_URI.equals(attributeUri) | attributeUri ==null) {
				startElement.append(attributeName);
			} else {
				startElement.append(attributeUri);
				startElement.append(XMLConstants.XMLNS_ASSIGN);
				startElement.append(attributeName);
			}
			startElement.append("=\"");
			startElement.append(attributeValueSafe);
			startElement.append('"');
			boolean printNewLine = attributeValueSafe.length()>80; // TODO add config
			if (printNewLine) {
				startElement.append(XMLConstants.CHAR_NEWLINE);
				for (int ii = 0; ii < indent+1; ii++) {
					startElement.append(getPropertyConfig().getPropertyString(OUTPUT_CHAR_TAB));
				}
			}
		}
	}
	
	/**
	 * @param uri	The xml namespace uri.
	 * @param localName	The local name of the xml tag.
	 * @param name The (full) name of the xml tag.
	 * @see org.xml.sax.ContentHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void endElement(String uri, String localName, String name) throws SAXException {
		
		if (elements.size()>0 && elements.peek().equals((localName))==false) {
			throw new SAXException("Unexpected end tag: "+localName+" should be: "+elements.peek());
		}
		elements.pop();
		
		if (startElement!=null) {
			String tag = startElement.toString();
			write(tag.substring(0,tag.length()-1));// rm normal close
			write(XMLConstants.TAG_CLOSE_EMPTY);
			startElement=null;
			indent--;
			return;
		}
		
		indent--;
		if (printReturn || !localName.equals(lastElement)) {
			write(getPropertyConfig().getPropertyString(OUTPUT_CHAR_NEWLINE));
			writeIndent();
		} else {
			printReturn = true;
		}
		if (localName==null) {
			localName = "null";
		}
		
		write(XMLConstants.TAG_OPEN_END);
		if (XMLConstants.NULL_NS_URI.equals(uri) | uri==null) {
			write(localName);
		} else {
			String prefix = prefixMapping.get(uri);
			if (prefix==null) {
				throw new SAXException("preFixUri: "+uri+" is not started.");
			}
			if (XMLConstants.NULL_NS_URI.equals(prefix)==false) {
				write(prefix);
				write(XMLConstants.XMLNS_ASSIGN);
			}
			write(localName);
		}
		write(XMLConstants.TAG_CLOSE);
	}
		
	/**
	 * Starts the prefix mapping of an xml namespace uri.
	 * @param prefix	The xml prefix of this xml namespace uri.
	 * @param uri	The xml namespace uri to add the prefix for.
	 * @see org.xml.sax.ContentHandler#startPrefixMapping(java.lang.String, java.lang.String)
	 */
	public void startPrefixMapping(String prefix, String uri) throws SAXException {
		prefixMapping.put(uri, prefix);
	}
	
	/**
	 * @param prefix	The xml prefix of this xml namespace uri to be ended.
	 * @see org.xml.sax.ContentHandler#endPrefixMapping(java.lang.String)
	 */
	public void endPrefixMapping(String prefix) throws SAXException {
		Set<Map.Entry<String,String>> s = prefixMapping.entrySet();
		String uri = null;
		for (Map.Entry<String,String> e:s) {
			if (e.getValue()==null) {
				continue; // way ?
			}
			if (e.getValue().equals(prefix)) {
				uri = e.getKey();
			}
		}
		if (uri!=null) {
			printedMappings.remove(uri);
			prefixMapping.remove(uri);
		}
	}

	/**
	 * Prints xml characters and uses characters(java.lang.String) method.
	 * 
	 * @param ch	Character buffer.
	 * @param start The start index of the chars in the ch buffer.
	 * @param length The length index of the chars in the ch buffer.
	 * @throws SAXException When IOException has happend while printing.
	 * @see org.xml.sax.ContentHandler#characters(char[], int, int)
	 */
	public void characters(char[] ch, int start, int length) throws SAXException {
		characters(new String(ch,start,length));
	}
	
	/**
	 * Escape and prints xml characters.
	 * @see org.x4o.xml.io.sax.ext.ContentWriter#characters(java.lang.String)
	 */
	public void characters(String text) throws SAXException {
		if (text==null) {
			return;
		}
		charactersRaw(XMLConstants.escapeCharacters(text));
	}
	
	public void characters(char c) throws SAXException {
		characters(new char[]{c},0,1);
	}
	
	// move or remove ?
	protected void charactersRaw(String text) throws SAXException {
		if (text==null) {
			return;
		}
		autoCloseStartElement();
		checkPrintedReturn(text);
		write(text);
	}
	
	/**
	 * Prints xml ignorable whitespace.
	 * 
	 * @param ch	Character buffer.
	 * @param start The start index of the chars in the ch buffer.
	 * @param length The length index of the chars in the ch buffer.
	 * @throws SAXException When IOException has happend while printing.
	 * @see org.xml.sax.ContentHandler#ignorableWhitespace(char[], int, int)
	 */
	public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
		ignorableWhitespace(new String(ch,start,length));
	}
	
	/**
	 * Prints xml ignorable whitespace.
	 * 
	 * @param text	The text to print.
	 * @throws SAXException When IOException has happend while printing.
	 * @see org.x4o.xml.io.sax.ext.ContentWriter#ignorableWhitespace(java.lang.String)
	 */
	public void ignorableWhitespace(String text) throws SAXException {
		if (text==null) {
			return;
		}
		autoCloseStartElement();
		write(text); // TODO: check chars
	}
	
	/**
	 * Prints xml ignorable whitespace character.
	 * 
	 * @param c	The character to print.
	 * @throws SAXException When IOException has happend while printing.
	 */
	public void ignorableWhitespace(char c) throws SAXException {
		ignorableWhitespace(new char[]{c},0,1);
	}

	/**
	 * Prints xml instructions.
	 * 
	 * @see org.xml.sax.ContentHandler#processingInstruction(java.lang.String, java.lang.String)
	 * @param target The target.
	 * @param data The data. 
	 */
	public void processingInstruction(String target, String data) throws SAXException {
		String targetLow = target.toLowerCase();
		if (targetLow.startsWith(XMLConstants.XML)) {
			throw new SAXException("Processing instruction may not start with xml.");
		}
		if (XMLConstants.isNameString(target)==false) {
			throw new SAXException("Processing instruction target is invalid name; '"+target+"'");
		}
		if (XMLConstants.isCharString(data)==false) {
			throw new SAXException("Processing instruction data is invalid char; '"+data+"'");
		}
		autoCloseStartElement();
		write(getPropertyConfig().getPropertyString(OUTPUT_CHAR_NEWLINE));
		writeIndent();
		write(XMLConstants.PROCESS_START);
		write(target);
		write(' ');
		write(data);
		write(XMLConstants.PROCESS_END);
		writeFlush();
		printReturn = true;
	}

	/**
	 * Not implemented.
	 * 
	 * @see org.xml.sax.ContentHandler#setDocumentLocator(org.xml.sax.Locator)
	 * @param locator The DocumentLocator to set.
	 */
	public void setDocumentLocator(Locator locator) {
	}

	/**
	 * Not implemented.
	 * 
	 * @see org.xml.sax.ContentHandler#skippedEntity(java.lang.String)
	 * @param name The name of the skipped entity.
	 */
	public void skippedEntity(String name) throws SAXException {
		// is for validating parser support, so not needed in xml writing.
	}

	/**
	 * Prints xml comment.
	 * 
	 * @param ch	Character buffer.
	 * @param start The start index of the chars in the ch buffer.
	 * @param length The length index of the chars in the ch buffer.
	 * @throws SAXException When IOException has happend while printing.
	 * @see org.xml.sax.ext.DefaultHandler2#comment(char[], int, int)
	 */
	public void comment(char[] ch, int start, int length) throws SAXException {
		comment(new String(ch,start,length));
	}
	
	/**
	 * Prints xml comment.
	 * 
	 * @see org.x4o.xml.io.sax.ext.ContentWriter#comment(java.lang.String)
	 */
	public void comment(String text) throws SAXException {
		if (text==null) {
			return;
		}
		autoCloseStartElement();
		checkPrintedReturn(text);
		write(getPropertyConfig().getPropertyString(OUTPUT_CHAR_NEWLINE));
		writeIndent();
		write(XMLConstants.COMMENT_START);
		write(XMLConstants.escapeCharactersComment(text,getPropertyConfig().getPropertyString(OUTPUT_CHAR_TAB),indent));
		write(XMLConstants.COMMENT_END);
		printReturn = true;
	}
	
	/**
	 * Checks if the value contains any new lines and sets the printReturn field.
	 * 
	 * @param value	The value to check.
	 */
	private void checkPrintedReturn(String value) {
		if (value.indexOf(XMLConstants.CHAR_NEWLINE)>0) {
			printReturn = true;
		} else {
			printReturn = false;
		}
	}
	
	/**
	 * Auto close the start element if working in printing event.
	 * @throws IOException	When prints gives exception.
	 */
	protected void autoCloseStartElement() throws SAXException {
		if (startElement==null) {
			return;
		}
		write(startElement.toString());
		startElement=null;
	}
	
	/**
	 * Indent the output writer with tabs by indent count.
	 * @throws IOException	When prints gives exception.
	 */
	private void writeIndent() throws SAXException {
		for (int i = 0; i < indent; i++) {
			write(getPropertyConfig().getPropertyString(OUTPUT_CHAR_TAB));
		}
	}
	
	protected void writeFlush() throws SAXException {
		try {
			out.flush();
		} catch (IOException e) {
			throw new SAXException(e);
		}
	}
	
	private void write(String text) throws SAXException {
		try {
			out.write(text);
		} catch (IOException e) {
			throw new SAXException(e);
		}
	}
	
	private void write(char c) throws SAXException {
		try {
			out.write(c);
		} catch (IOException e) {
			throw new SAXException(e);
		}
	}
}
