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

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import org.x4o.xml.io.XMLConstants;

/**
 * ContentWriterXml writes SAX content handler events to XML.
 * 
 * @author Willem Cazander
 * @version 1.0 Apr 17, 2005
 */
public class ContentWriterXml extends AbstractContentWriter { 

	/**
	 * Creates XmlWriter which prints to the Writer interface.
	 * @param out	The writer to print the xml to.
	 */
	public ContentWriterXml(Writer out,String encoding,String charNewLine,String charTab) {
		super(out, encoding, charNewLine, charTab);
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
}
