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

import org.x4o.xml.io.XMLConstants;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DefaultHandler2;


/**
 * Writes SAX event to XML.
 * 
 * @author Willem Cazander
 * @version 1.0 17/04/2005
 */
public class XMLWriter extends DefaultHandler2 {
	
	private String encoding = null;
	private String charNewline = null;
	private String charTab = null;
	private Writer out = null;
	private int indent = 0;
	private Map<String,String> prefixMapping = null;
	private List<String> printedMappings = null;
	private StringBuffer startElement = null;
	private boolean printedReturn = false;

	/**
	 * Creates XmlWriter which prints to the Writer interface.
	 * @param out	The writer to print the xml to.
	 */
	public XMLWriter(Writer out,String encoding,String charNewLine,String charTab) {
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
	}

	/**
	 * Creates XmlWriter which prints to the Writer interface.
	 * @param out	The writer to print the xml to.
	 */
	public XMLWriter(Writer out,String encoding) {
		this(out,encoding,null,null);
	}
	
	/**
	 * Creates XmlWriter which prints to the OutputStream interface.
	 * @param out	The OutputStream to write to.
	 * @throws UnsupportedEncodingException	Is thrown when UTF-8 can't we printed.
	 */
	public XMLWriter(OutputStream out,String encoding) throws UnsupportedEncodingException {
		this(new OutputStreamWriter(out, encoding),encoding);
	}
	
	/**
	 * Creates XmlWriter which prints to the Writer interface.
	 * @param out	The writer to print the xml to.
	 */
	public XMLWriter(Writer out) {
		this(out,null);
	}

	/**
	 * Creates XmlWriter which prints to the OutputStream interface.
	 * @param out	The OutputStream to write to.
	 * @throws UnsupportedEncodingException	Is thrown when UTF-8 can't we printed.
	 */
	public XMLWriter(OutputStream out) throws UnsupportedEncodingException {
		this(new OutputStreamWriter(out, XMLConstants.XML_DEFAULT_ENCODING),XMLConstants.XML_DEFAULT_ENCODING);
	}
	
	/**
	 * @see org.xml.sax.ContentHandler#startDocument()
	 */
	@Override
	public void startDocument() throws SAXException {
		indent = 0;
		write(XMLConstants.getDocumentDeclaration(encoding));
	}
	
	/**
	 * @see org.xml.sax.ContentHandler#endDocument()
	 */
	@Override
	public void endDocument() throws SAXException {
		writeFlush();
	}

	/**
	 * @param uri	The xml namespace uri.
	 * @param localName	The local name of the xml tag.
	 * @param name The (full) name of the xml tag.
	 * @param atts The attributes of the xml tag. 
	 * @see org.xml.sax.ContentHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	public void startElement(String uri, String localName, String name,Attributes atts) throws SAXException {
		if (startElement!=null) {
			write(startElement.toString());
			startElement=null;
		}
		startElement = new StringBuffer(200);
		
		if (printedReturn==false) {
			startElement.append(charNewline);
		}
		printedReturn=false;
		
		for (int i = 0; i < indent; i++) {
			startElement.append(charTab);
		}
		startElement.append(XMLConstants.TAG_OPEN);
		
		if (localName==null) {
			localName = "null";
		}
		if (XMLConstants.isNameString(localName)==false) {
			throw new SAXException("LocalName of element is not valid in xml; '"+localName+"'");
		}
		
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
		
		for (int i=0;i<atts.getLength();i++) {
			String attributeUri = atts.getURI(i);
			String attributeName = atts.getLocalName(i);
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
		startElement.append(XMLConstants.TAG_CLOSE);
		indent++;
	}
	
	/**
	 * @param uri	The xml namespace uri.
	 * @param localName	The local name of the xml tag.
	 * @param name The (full) name of the xml tag.
	 * @see org.xml.sax.ContentHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void endElement(String uri, String localName, String name) throws SAXException {
		if (startElement!=null) {
			String tag = startElement.toString();
			write(tag.substring(0,tag.length()-1));
			write(XMLConstants.TAG_CLOSE_EMPTY);
			startElement=null;
			indent--;
			return;
		}
		
		if (printedReturn==false) {
			write(charNewline);
		}
		printedReturn=false;
		indent--;
		writeIndent();
		
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
	@Override
	public void startPrefixMapping(String prefix, String uri) throws SAXException {
		prefixMapping.put(uri, prefix);
	}
	
	/**
	 * @param prefix	The xml prefix of this xml namespace uri to be ended.
	 * @see org.xml.sax.ContentHandler#endPrefixMapping(java.lang.String)
	 */
	@Override
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
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		if (startElement!=null) {
			write(startElement.toString());
			startElement=null;
		}
		for (int i=start;i<(start+length);i++) {
			char c = ch[i];
			write(c);
			if (c=='\n') {
				printedReturn=true;
			}
		}
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
	@Override
	public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
		if (startElement!=null) {
			write(startElement.toString());
			startElement=null;
		}
		write(ch, start, length);
	}

	/**
	 * Prints xml instructions.
	 * 
	 * @see org.xml.sax.ContentHandler#processingInstruction(java.lang.String, java.lang.String)
	 * @param target The target.
	 * @param data The data. 
	 */
	@Override
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
	@Override
	public void setDocumentLocator(Locator locator) {
	}

	/**
	 * Not implemented.
	 * 
	 * @see org.xml.sax.ContentHandler#skippedEntity(java.lang.String)
	 * @param name The name of the skipped entity.
	 */
	@Override
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
	@Override
	public void comment(char[] ch, int start, int length) throws SAXException {
		writeIndent();
		write(XMLConstants.COMMENT_START);
				
		/// mmm todo improve a bit
		for (int i=start;i<(start+length);i++) {
			char c = ch[i];
			if (c=='\n') {
				write(c);
				writeIndent();
				continue;
			}
			write(c);
		}
		write(XMLConstants.COMMENT_END);
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
	
	private void write(char[] ch, int start, int length) throws SAXException {
		try {
			out.write(ch,start,length);
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
