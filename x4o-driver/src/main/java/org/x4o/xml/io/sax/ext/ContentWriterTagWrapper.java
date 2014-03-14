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
package	org.x4o.xml.io.sax.ext;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * ContentWriterXmlTag can write enum based xml events.
 * 
 * @author Willem Cazander
 * @version 1.0 May 3, 2013
 */
public class ContentWriterTagWrapper<TAG extends Enum<?>,TAG_WRITER extends ContentWriter> implements ContentWriterTag<TAG> { 

	private final Attributes EMPTY_ATTRIBUTES = new AttributesImpl();
	private final TAG_WRITER contentWriter;
	private final String tagNamespaceUri;
	private final String tagNamespacePrefix;
	
	public ContentWriterTagWrapper(TAG_WRITER contentWriter,String tagNamespaceUri,String tagNamespacePrefix) {
		if (contentWriter==null) {
			throw new NullPointerException("Can't create wrapper on null ContentWriter");
		}
		if (tagNamespaceUri==null) {
			throw new NullPointerException("Can't create wrapper with null tagNamespaceUri");
		}
		if (tagNamespacePrefix==null) {
			throw new NullPointerException("Can't create wrapper with null tagNamespacePrefix");
		}
		this.contentWriter=contentWriter;
		this.tagNamespaceUri=tagNamespaceUri;
		this.tagNamespacePrefix=tagNamespacePrefix;
	}
	
	public TAG_WRITER getContentWriterWrapped() {
		return contentWriter;
	}
	
	public String getTagNamespaceUri() {
		return tagNamespaceUri;
	}
	
	public void startDocument() throws SAXException {
		contentWriter.startDocument();
		contentWriter.startPrefixMapping(tagNamespacePrefix, getTagNamespaceUri());
	}
	
	public void endDocument() throws SAXException {
		contentWriter.endDocument();
	}
	
	public void printTagStartEnd(TAG tag,Attributes atts) throws SAXException {
		printTagStart(tag,atts);
		printTagEnd(tag);
	}
	
	public void printTagStartEnd(TAG tag) throws SAXException {
		printTagStart(tag);
		printTagEnd(tag);
	}
	
	public void printTagStart(TAG tag) throws SAXException {
		printTagStart(tag,EMPTY_ATTRIBUTES);
	}
	
	public void printTagStart(TAG tag,Attributes atts) throws SAXException {
		contentWriter.startElement (getTagNamespaceUri(), toTagString(tag), "", atts);
	}
	
	public void printTagEnd(TAG tag) throws SAXException {
		contentWriter.endElement (getTagNamespaceUri(),toTagString(tag) , "");
	}
	
	private String toTagString(TAG tag) {
		String result = tag.name();
		if (result.startsWith("_"))
		{
			result = result.substring(1); // remove _
		}
		return result;
	}
	
	public void printTagCharacters(TAG tag,String text) throws SAXException {
		printTagStart(tag);
		if (text==null) {
			text = " ";
		}
		printCharacters(text);
		printTagEnd(tag);
	}
	
	public void printCharacters(String text) throws SAXException {
		contentWriter.characters(text);
	}
	
	public void printComment(String text) throws SAXException {
		contentWriter.comment(text);
	}
}
