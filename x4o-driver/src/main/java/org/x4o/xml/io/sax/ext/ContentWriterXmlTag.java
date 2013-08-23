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

import java.io.Writer;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * ContentWriterXml writes SAX content handler events to XML.
 * 
 * @author Willem Cazander
 * @version 1.0 May 3, 2013
 */
public class ContentWriterXmlTag<T extends Enum<?>> extends ContentWriterXml implements ContentWriterTag<T> { 

	public ContentWriterXmlTag(Writer out,String encoding) {
		super(out, encoding);
	}
	
	public String getTagNamespaceUri() {
		return "";
	}
	
	public void printTagStartEnd(T tag,Attributes atts) throws SAXException {
		printTagStart(tag,atts);
		printTagEnd(tag);
	}
	
	public void printTagStartEnd(T tag) throws SAXException {
		printTagStart(tag);
		printTagEnd(tag);
	}
	
	public void printTagStart(T tag) throws SAXException {
		printTagStart(tag,EMPTY_ATTRIBUTES);
	}
	
	public void printTagStart(T tag,Attributes atts) throws SAXException {
		startElement (getTagNamespaceUri(), tag.name(), "", atts);
	}
	
	public void printTagEnd(T tag) throws SAXException {
		endElement (getTagNamespaceUri(),tag.name() , "");
	}
	
	public void printTagCharacters(T tag,String text) throws SAXException {
		printTagStart(tag);
		if (text==null) {
			text = " ";
		}
		characters(text);
		printTagEnd(tag);
	}
}
