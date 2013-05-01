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
package	org.x4o.xml.io.sax;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.x4o.xml.io.XMLConstants;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * ContentWriterXml writes SAX content handler events to XML.
 * 
 * @author Willem Cazander
 * @version 1.0 Apr 17, 2005
 */
public class ContentWriterXml implements ContentWriter { 
	
	public final Attributes EMPTY_ATTRIBUTES = new AttributesImpl();
	protected String encoding = null;
	private String charNewline = null;
	private String charTab = null;
	private Writer out = null;
	private int indent = 0;
	private Map<String,String> prefixMapping = null;
	private List<String> printedMappings = null;
	private StringBuffer startElement = null;
	private boolean printReturn = false;
	private String lastElement = null;
	private boolean printCDATA = false;
	private Stack<String> elements = null;

	/**
	 * Creates XmlWriter which prints to the Writer interface.
	 * @param out	The writer to print the xml to.
	 */
	public ContentWriterXml(Writer out,String encoding,String charNewLine,String charTab) {
		if (out==null) {
			throw new NullPointerException("Can't write on null writer.");
		}
		if (encoding==null) {
			encoding = XMLConstants.XML_DEFAULT_ENCODING;
		}
		if (charNewLine==null) {
			charNewLine = XMLConstants.CHAR_NEWLINE;
		}
		if (charTab==null) {
			charTab = XMLConstants.CHAR_TAB;
		}
		this.out = out;
		this.encoding = encoding;
		this.charNewline = charNewLine;
		this.charTab = charTab;
		prefixMapping = new HashMap<String,String>(15);
		printedMappings = new ArrayList<String>(15);
		elements = new Stack<String>();
	}

	/**
	 * Creates XmlWriter which prints to the Writer interface.
	 * @param out	The writer to print the xml to.
	 */
	public ContentWriterXml(Writer out,String encoding) {
		this(out,encoding,null,null);
	}
	
	/**
	 * Creates XmlWriter which prints to the OutputStream interface.
	 * @param out	The OutputStream to write to.
	 * @throws UnsupportedEncodingException	Is thrown when UTF-8 can't we printed.
	 */
	public ContentWriterXml(OutputStream out,String encoding) throws UnsupportedEncodingException {
		this(new OutputStreamWriter(out, encoding),encoding);
	}
	
	/**
	 * Creates XmlWriter which prints to the Writer interface.
	 * @param out	The writer to print the xml to.
	 */
	public ContentWriterXml(Writer out) {
		this(out,null);
	}

	/**
	 * Creates XmlWriter which prints to the OutputStream interface.
	 * @param out	The OutputStream to write to.
	 * @throws UnsupportedEncodingException	Is thrown when UTF-8 can't we printed.
	 */
	public ContentWriterXml(OutputStream out) throws UnsupportedEncodingException {
		this(new OutputStreamWriter(out, XMLConstants.XML_DEFAULT_ENCODING),XMLConstants.XML_DEFAULT_ENCODING);
	}

	/**
	 * Creates XmlWriter which prints to the OutputStream interface.
	 * @param out	The OutputStream to write to.
	 * @param encoding	The OutputStream encoding.
	 * @param charNewLine	The newline char.
	 * @param charTab	The tab char.
	 * @throws UnsupportedEncodingException	Is thrown when UTF-8 can't we printed.
	 */
	public ContentWriterXml(OutputStream out,String encoding,String charNewLine,String charTab) throws UnsupportedEncodingException {
		this(new OutputStreamWriter(out, encoding),encoding,charNewLine,charTab);
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
		write(XMLConstants.getDocumentDeclaration(encoding));
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
	 * Starts and end then element.
	 * @see org.x4o.xml.io.sax.ContentWriter#startElementEnd(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	public void startElementEnd(String uri, String localName, String name,Attributes atts) throws SAXException {
		startElement(uri,localName,name,atts);
		endElement(uri, localName, name);
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
			localName = "null"; // mmm rm ?
		}
		if (XMLConstants.isNameString(localName)==false) {
			throw new SAXException("LocalName of element is not valid in xml; '"+localName+"'");
		}
		autoCloseStartElement();
		startElement = new StringBuffer(200);
		startElement.append(charNewline);
		for (int i = 0; i < indent; i++) {
			startElement.append(charTab);
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
					startElement.append(charNewline);
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
				startElement.append(charNewline);
			}
		}
	}
	
	private void startElementAttributes(Attributes atts) throws SAXException {
		for (int i=0;i<atts.getLength();i++) {
			String attributeUri = atts.getURI(i);
			String attributeName = XMLConstants.escapeAttributeName(atts.getLocalName(i));
			String attributeValue = atts.getValue(i);
			if (attributeValue==null) {
				attributeValue = "null";
			}
			startElement.append(' ');
			
			if (XMLConstants.NULL_NS_URI.equals(attributeUri) | attributeUri ==null) {
				startElement.append(attributeName);
			} else {
				startElement.append(attributeUri);
				startElement.append(XMLConstants.XMLNS_ASSIGN);
				startElement.append(attributeName);
			}
			
			startElement.append("=\"");
			startElement.append(XMLConstants.escapeAttributeValue(attributeValue));
			startElement.append('"');
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
			write(charNewline);
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
	 * Prints xml characters.
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
	 * @see org.x4o.xml.io.sax.ContentWriter#characters(java.lang.String)
	 */
	public void characters(String text) throws SAXException {
		if (text==null) {
			return;
		}
		autoCloseStartElement();
		checkPrintedReturn(text);
		if (printCDATA) {
			text = XMLConstants.escapeCharactersCdata(text,"","");
		} else {
			text = XMLConstants.escapeCharacters(text);
		}
		write(text);
	}
	
	// move or remove ?
	public void charactersRaw(String text) throws SAXException {
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
	 * @see org.x4o.xml.io.sax.ContentWriter#ignorableWhitespace(java.lang.String)
	 */
	public void ignorableWhitespace(String text) throws SAXException {
		if (text==null) {
			return;
		}
		autoCloseStartElement();
		write(text); // TODO: check chars
	}

	/**
	 * Prints xml instructions.
	 * 
	 * @see org.xml.sax.ContentHandler#processingInstruction(java.lang.String, java.lang.String)
	 * @param target The target.
	 * @param data The data. 
	 */
	public void processingInstruction(String target, String data) throws SAXException {
		writeIndent();
		write(XMLConstants.PROCESS_START);
		write(target);
		write(' ');
		write(data);
		write(XMLConstants.PROCESS_END);
		write(charNewline);
		writeFlush();
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
	 * @see org.x4o.xml.io.sax.ContentWriter#comment(java.lang.String)
	 */
	public void comment(String text) throws SAXException {
		if (text==null) {
			return;
		}
		autoCloseStartElement();
		checkPrintedReturn(text);
		write(charNewline);
		writeIndent();
		write(XMLConstants.COMMENT_START);
		write(" ");
		write(XMLConstants.escapeCharactersComment(text,charTab,indent));
		write(" ");
		write(XMLConstants.COMMENT_END);
		printReturn = true;
	}
	
	/**
	 * @see org.xml.sax.ext.LexicalHandler#startCDATA()
	 */
	public void startCDATA() throws SAXException {
		autoCloseStartElement();
		write(XMLConstants.CDATA_START);
		printCDATA = true;
	}
	
	/**
	 * @see org.xml.sax.ext.LexicalHandler#endCDATA()
	 */
	public void endCDATA() throws SAXException {
		write(XMLConstants.CDATA_END);
		printCDATA = false;
	}

	/**
	 * @see org.xml.sax.ext.LexicalHandler#startDTD(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void startDTD(String name, String publicId, String systemId) throws SAXException {
		write(XMLConstants.XML_DOCTYPE);
		write(" ");
		write(name);
		if (publicId!=null) {
			write(" ");
			write(publicId);
		}
		if (systemId!=null) {
			write(" \"");
			write(systemId);
			write("\"");
		}
		write(XMLConstants.TAG_CLOSE);
	}
	
	/**
	 * @see org.xml.sax.ext.LexicalHandler#endDTD()
	 */
	public void endDTD() throws SAXException {
		writeFlush();
	}

	/**
	 * @see org.xml.sax.ext.LexicalHandler#startEntity(java.lang.String)
	 */
	public void startEntity(String arg0) throws SAXException {
	}
	
	/**
	 * @see org.xml.sax.ext.LexicalHandler#endEntity(java.lang.String)
	 */
	public void endEntity(String arg0) throws SAXException {	
	}
	
	
	
	private void checkPrintedReturn(String value) {
		if (value.indexOf('\n')>0) {
			printReturn = true;
		} else {
			printReturn = false;
		}
	}
	
	/**
	 * Auto close the start element if working in printing event.
	 * @throws IOException	When prints gives exception.
	 */
	private void autoCloseStartElement() throws SAXException {
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
			write(charTab);
		}
	}
	
	private void writeFlush() throws SAXException {
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
