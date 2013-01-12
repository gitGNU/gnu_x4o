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

package	org.x4o.xml.sax;

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

	private Writer out = null;
	private int indent = 0;
	private Map<String,String> prefixMapping = new HashMap<String,String>(10);
	private List<String> printedMappings = new ArrayList<String>(10);
	private StringBuffer startElement = null;
	private boolean printedReturn = false;

	/**
	 * Creates XmlWriter which prints to the Writer interface.
	 * @param out	The writer to print the xml to.
	 */
	public XMLWriter(Writer out) {
		this.out = out;
	}

	/**
	 * Creates XmlWriter which prints to the OutputStream interface.
	 * @param out	The OutputStream to write to.
	 * @throws UnsupportedEncodingException	Is thrown when UTF-8 can't we printed.
	 */
	public XMLWriter(OutputStream out) throws UnsupportedEncodingException {
		this.out = new OutputStreamWriter(out, "UTF-8");
	}
	
	/**
	 * @see org.xml.sax.ContentHandler#startDocument()
	 */
	@Override
	public void startDocument() throws SAXException {
		indent = 0;
		try {
			out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		} catch (IOException e) {
			throw new SAXException(e);
		}
	}
	
	/**
	 * @see org.xml.sax.ContentHandler#endDocument()
	 */
	@Override
	public void endDocument() throws SAXException {
		try {
			out.flush();  
		} catch (IOException e) {
			throw new SAXException(e);
		}
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
		try {
			if (startElement!=null) {
				out.write(startElement.toString());
				startElement=null;
			}
			
			startElement = new StringBuffer(200);
			
			if (printedReturn==false) {
				startElement.append("\r\n");
			}
			printedReturn=false;
			
			for (int i = 0; i < indent; i++) {
				startElement.append('\t');
			}
			startElement.append("<");
			
			if (localName==null) {
				localName = "null";
			}
			
			if ("".equals(uri) | uri==null) {
				startElement.append(localName);
			} else {
				String prefix = prefixMapping.get(uri);
				if (prefix==null) {
					throw new SAXException("preFixUri: "+uri+" is not started.");
				}
				if ("".equals(prefix)==false) {
					startElement.append(prefix);
					startElement.append(':');
				}
				startElement.append(localName);
			}
		

			if ((uri!=null & "".equals(uri)==false) && printedMappings.contains(uri)==false) {
				String prefix = prefixMapping.get(uri);
				if (prefix==null) {
					throw new SAXException("preFixUri: "+uri+" is not started.");
				}
				printedMappings.add(uri);
				
				startElement.append(' ');
				startElement.append("xmlns");
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
							startElement.append('\n');
							first = false;
						}
						
						startElement.append(' ');
						startElement.append("xmlns");
						if ("".equals(prefix)==false) {
							startElement.append(':');
							startElement.append(prefix);
						}
						startElement.append("=\"");
						startElement.append(uri2);
						startElement.append('"');
						startElement.append('\n');
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
				if ("".equals(attributeUri) | attributeUri ==null) {
					startElement.append(attributeName);					
				} else {
					startElement.append(attributeUri);
					startElement.append(':');
					startElement.append(attributeName);
				}
				
				startElement.append("=\"");
				
				// TODO: xml escaping of attributes
				if (attributeValue.contains("&")) {
					attributeValue=attributeValue.replaceAll("&", "&amp;");
				}
				if (attributeValue.contains("\"")) {
					attributeValue=attributeValue.replaceAll("\"", "&quote;");
				}
				if (attributeValue.contains("<")) {
					attributeValue=attributeValue.replaceAll("<", "&lt;");
				}
				if (attributeValue.contains(">")) {
					attributeValue=attributeValue.replaceAll(">", "&gt;");
				}
				
				startElement.append(attributeValue);
				startElement.append('"');
			}
			startElement.append(">");
		} catch (IOException e) {
			throw new SAXException(e);
		} finally {
			indent++;
		}
	}
	
	/**
	 * @param uri	The xml namespace uri.
	 * @param localName	The local name of the xml tag.
	 * @param name The (full) name of the xml tag.
	 * @see org.xml.sax.ContentHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void endElement(String uri, String localName, String name) throws SAXException {
		try {
			if (startElement!=null) {
				String ss = startElement.toString();
				out.write(ss,0,ss.length()-1);
				out.write("/>");
				startElement=null;
				indent--;
				return;
			}
			
			if (printedReturn==false) {
				out.write("\r\n");
			}
			printedReturn=false;
			indent--;
			indent();
			
			if (localName==null) {
				localName = "null";
			}
			
			out.write("</");
			if ("".equals(uri) | uri==null) {
				out.write(localName);
			} else {
				String prefix = prefixMapping.get(uri);
				if (prefix==null) {
					throw new SAXException("preFixUri: "+uri+" is not started.");
				}
				if ("".equals(prefix)==false) {
					out.write(prefix);
					out.write(':');
				}
				out.write(localName);
			}
			out.write(">");   
		} catch (IOException e) {
			throw new SAXException(e);
		} 
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
		Set<Map.Entry<String,String>> s=prefixMapping.entrySet();
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
		try {
			if (startElement!=null) {
				out.write(startElement.toString());
				startElement=null;
			}
			/// mmm todo improve a bit
			for (int i=start;i<(start+length);i++) {
				char c = ch[i];
				out.write(c);
				if (c=='\n') {
					printedReturn=true;
					continue;
				}
			}
			//out.write(ch, start, length); 
		} catch (IOException e) {
			throw new SAXException(e);
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
		try {
			if (startElement!=null) {
				out.write(startElement.toString());
				startElement=null;
			}
			out.write(ch, start, length); 
		} catch (IOException e) {
			throw new SAXException(e);
		}
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
		try {
			indent();
			out.write("<?" + target + " " + data + "?>\r\n"); 
		} catch (IOException e) {
			throw new SAXException(e);
		} 
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
		try {
			indent();
			out.write("<!--");
					
			/// mmm todo improve a bit
			for (int i=start;i<(start+length);i++) {
				char c = ch[i];
				if (c=='\n') {
					out.write(c);
					indent();
					continue;
				}
				out.write(c);
			}
			out.write("-->");
		} catch (IOException e) {
			throw new SAXException(e);
		}
	}

	/**
	 * Indent the output writer with tabs by indent count.
	 * @throws IOException	When prints gives exception.
	 */
	private void indent() throws IOException {
		for (int i = 0; i < indent; i++) {
			out.write('\t');
		}
	}
}
