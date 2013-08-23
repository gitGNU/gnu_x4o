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

import org.x4o.xml.io.XMLConstants;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;

/**
 * ContentWriterXml writes SAX content handler events to XML.
 * 
 * @author Willem Cazander
 * @version 1.0 May 3, 2013
 */
public abstract class AbstractContentWriterLexical extends AbstractContentWriterHandler implements LexicalHandler { 

	protected boolean printCDATA = false;
	
	/**
	 * Creates XmlWriter which prints to the Writer interface.
	 * @param out	The writer to print the xml to.
	 */
	public AbstractContentWriterLexical(Writer out) {
		super(out);
	}

	/**
	 * @see org.xml.sax.ext.LexicalHandler#startCDATA()
	 */
	public void startCDATA() throws SAXException {
		autoCloseStartElement();
		charactersRaw(XMLConstants.CDATA_START);
		printCDATA = true;
	}
	
	/**
	 * @see org.xml.sax.ext.LexicalHandler#endCDATA()
	 */
	public void endCDATA() throws SAXException {
		charactersRaw(XMLConstants.CDATA_END);
		printCDATA = false;
	}

	/**
	 * @see org.xml.sax.ext.LexicalHandler#startDTD(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void startDTD(String name, String publicId, String systemId) throws SAXException {
		charactersRaw(XMLConstants.XML_DOCTYPE);
		charactersRaw(" ");
		charactersRaw(name);
		if (publicId!=null) {
			charactersRaw(" ");
			charactersRaw(publicId);
		}
		if (systemId!=null) {
			charactersRaw(" \"");
			charactersRaw(systemId);
			charactersRaw("\"");
		}
		charactersRaw(XMLConstants.TAG_CLOSE);
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

	/**
	 * @see org.x4o.xml.io.sax.ext.AbstractContentWriterHandler#characters(char[], int, int)
	 */
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		characters(new String(ch, start, length));
	}

	/**
	 * @see org.x4o.xml.io.sax.ext.AbstractContentWriterHandler#characters(java.lang.String)
	 */
	@Override
	public void characters(String text) throws SAXException {
		if (printCDATA) {
			charactersRaw(XMLConstants.escapeCharactersCdata(text,"",""));
		} else {
			super.characters(text);
		}
	}
}
