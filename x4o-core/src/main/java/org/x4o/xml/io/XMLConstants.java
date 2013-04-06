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

/**
 * XMLConstants for writing XML.
 * 
 * @author Willem Cazander
 * @version 1.0 Mrt 31, 2012
 */
public final class XMLConstants {

	/**
	 * Lowcase xml.
	 */
	public static final String XML = "xml";
	
	/**
	 * XML Default encoding is utf-8.
	 */
	public static final String XML_DEFAULT_ENCODING = "UTF-8";
	
	/**
	 * XML Default version is 1.0.
	 */
	public static final String XML_DEFAULT_VERSION = "1.0";
	
	/**
	 * XML Namespace prefix attribute.
	 */
	public static final String XMLNS_ATTRIBUTE = "xmlns";

	/**
	 * XML Namespace prefix seperator
	 */
	public static final String XMLNS_ASSIGN = ":";
	
	/**
	 * XML Schema namespace URI.
	 */
	public static final String XML_SCHEMA_NS_URI = "http://www.w3.org/2001/XMLSchema";

	/**
	 * XML Schema instance namespace URI.
	 */
	public static final String XML_SCHEMA_INSTANCE_NS_URI = "http://www.w3.org/2001/XMLSchema-instance";
	
	/**
	 * Null or empty namespace uri.
	 * @see <a href="http://www.w3.org/TR/REC-xml-names/#defaulting">Namespaces in XML, 5.2 Namespace Defaulting</a>
	 */
	public static final String NULL_NS_URI = "";
	
	/**
	 * Opens xml element tag.
	 */
	public static final String TAG_OPEN = "<";
	
	/**
	 * Opens end xml element tag.
	 */
	public static final String TAG_OPEN_END = "</";
	
	/**
	 * Closes xml element tag.
	 */
	public static final String TAG_CLOSE = ">";
	
	/**
	 * Close empty xml element tag.
	 */
	public static final String TAG_CLOSE_EMPTY = "/>";
	
	/**
	 * Starts a comment.
	 */
	public static final String COMMENT_START = "<!--";
	
	/**
	 * Ends a comment.
	 */
	public static final String COMMENT_END = "-->";
	
	/**
	 * Starts a processing instruction.
	 */
	public static final String PROCESS_START = "<?";
	
	/**
	 * Ends a processing instruction.
	 */
	public static final String PROCESS_END = "?>";
	
	/**
	 * Tab char
	 */
	public static final String CHAR_TAB = "\t";
	
	/**
	 * Newline char
	 */
	public static final String CHAR_NEWLINE = "\r\n";
	
	

	
	static public String getDocumentDeclaration(String encoding) {
		return getDocumentDeclaration(encoding,null);
	}
	
	static public String getDocumentDeclaration(String encoding,String version) {
		if (encoding==null) {
			encoding=XML_DEFAULT_ENCODING;
		}
		if (version==null) {
			version=XML_DEFAULT_VERSION;
		}
		return String.format("<?xml version=\"%s\" encoding=\"%s\"?>", version,encoding);
	}
	
	static public boolean isChar(int c) {
		// Exclude "compatibility characters", as defined in section 2.3 of [Unicode]
		if (c>=0x7F & c<=0x84) {			return false;	}
		if (c>=0x86 & c<=0x9F) {			return false;	}
		if (c>=0xFDD0 & c<=0xFDEF) {		return false;	}
		if ((c>=0x1FFFE & c<=0x1FFFF)||(c>=0x2FFFE & c<=0x2FFFF)|(c>=0x3FFFE & c<=0x3FFFF)) {	return false;	}
		if ((c>=0x4FFFE & c<=0x4FFFF)||(c>=0x5FFFE & c<=0x5FFFF)|(c>=0x6FFFE & c<=0x6FFFF)) {	return false;	}
		if ((c>=0x7FFFE & c<=0x7FFFF)||(c>=0x8FFFE & c<=0x8FFFF)|(c>=0x9FFFE & c<=0x9FFFF)) {	return false;	}
		if ((c>=0xAFFFE & c<=0xAFFFF)||(c>=0xBFFFE & c<=0xBFFFF)|(c>=0xCFFFE & c<=0xCFFFF)) {	return false;	}
		if ((c>=0xDFFFE & c<=0xDFFFF)||(c>=0xEFFFE & c<=0xEFFFF)|(c>=0xFFFFE & c<=0xFFFFF)) {	return false;	}
		if (c>=0x10FFFE & c<=0x10FFFF) {	return false;	}
		
		// Source;
		// #x9 | #xA | #xD | [#x20-#xD7FF] | [#xE000-#xFFFD] | [#x10000-#x10FFFF]
		if (c==0x9) {					return true;	}
		if (c==0xA) {					return true;	}
		if (c==0xD) {					return true;	}
		if (c>=0x20 & c<=0xD7FF) {		return true;	}
		if (c>=0xE000 & c<=0xFFFD) {	return true;	}
		if (c>=0x10000 & c<=0x10FFFF) {	return true;	}
		return false;
	}
	
	static public boolean isNameStartChar(int c) {
		// Source;
		// 	":" | [A-Z] | "_" | [a-z] | [#xC0-#xD6] | [#xD8-#xF6] | [#xF8-#x2FF] | [#x370-#x37D] | [#x37F-#x1FFF] | [#x200C-#x200D] | [#x2070-#x218F] | [#x2C00-#x2FEF] | [#x3001-#xD7FF] | [#xF900-#xFDCF] | [#xFDF0-#xFFFD] | [#x10000-#xEFFFF]
		if (c>='a' & c<='z') {			return true;	}
		if (c>='A' & c<='Z') {			return true;	}
		if (c==':' || c=='_') {			return true;	}
		if (c>=0xC0 & c<=0xD6) {		return true;	}
		if (c>=0xD8 & c<=0xF6) {		return true;	}
		if (c>=0xF8 & c<=0x2FF) {		return true;	}
		if (c>=0x370 & c<=0x37D) {		return true;	}
		if (c>=0x37F & c<=0x1FFF) {		return true;	}
		if (c>=0x200C & c<=0x200D) {	return true;	}
		if (c>=0x2070 & c<=0x218F) {	return true;	}
		if (c>=0x2C00 & c<=0x2FEF) {	return true;	}
		if (c>=0x3001 & c<=0xD7FF) {	return true;	}
		if (c>=0xF900 & c<=0xFDCF) {	return true;	}
		if (c>=0xFDF0 & c<=0xFFFD) {	return true;	}
		if (c>=0x10000 & c<=0xEFFFF) {	return true;	}
		return false;
	}
	
	static public boolean isNameChar(int c) {
		// Source;
		// NameStartChar | "-" | "." | [0-9] | #xB7 | [#x0300-#x036F] | [#x203F-#x2040]
		if (isNameStartChar(c)) {
			return true;
		}
		if (c=='-' || c=='.') {			return true;	}
		if (c>='0' & c<='9') {			return true;	}
		if (c==0xB7) {					return true;	}
		if (c>=0x0300 & c<=0x036F) {	return true;	}
		if (c>=0x203F & c<=0x2040) {	return true;	}
		return false;
	}
	
	static public String escapeAttributeValue(String value) {
		int l = value.length();
		StringBuffer result = new StringBuffer(l);
		for (int i=0;i<l;i++) {
			char c = value.charAt(i);
			if (c=='<') {
				result.append("&lt;");
				continue;
			}
			if (c=='>') {
				result.append("&gt;");
				continue;
			}
			if (c=='&') {
				result.append("&amp;");
				continue;
			}
			if (c=='\"') {
				result.append("&quote;");
				continue;
			}
			if (c=='\'') {
				result.append("&apos;");
				continue;
			}
			if (isNameChar(c)==false) { 
				result.append("#x");
				result.append(Integer.toHexString(c));
				result.append(";");
				continue;
			} else {
				result.append(c);
			}
		}
		return result.toString();
	}
}
