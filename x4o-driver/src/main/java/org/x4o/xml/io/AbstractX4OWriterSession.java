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
package org.x4o.xml.io;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.x4o.xml.io.sax.ext.ContentWriterXml;
import org.x4o.xml.lang.X4OLanguage;
import org.x4o.xml.lang.X4OLanguageSession;
import org.xml.sax.SAXException;

/**
 * AbstractX4OWriterSession.
 * 
 * @author Willem Cazander
 * @version 1.0 Apr 6, 2013
 */
public abstract class AbstractX4OWriterSession<T> extends AbstractX4OConnection implements X4OWriterSession<T> {

	public AbstractX4OWriterSession(X4OLanguage language) {
		super(language);
	}
	
	public void writeFileSession(X4OLanguageSession languageSession,String fileName) throws X4OConnectionException,SAXException,IOException {
		if (fileName==null) {
			throw new NullPointerException("Can't convert null fileName to file object.");
		}		
		writeFileSession(languageSession,new File(fileName));
	}
	
	public void writeFileSession(X4OLanguageSession languageSession,File file) throws X4OConnectionException,SAXException,IOException {
		if (file==null) {
			throw new NullPointerException("Can't read null file.");
		}
		OutputStream outputStream = new FileOutputStream(file);
		try {
			writeSession(languageSession,outputStream);
		} finally {
			outputStream.close();
		}
	}
	
	public String writeStringSession(X4OLanguageSession languageSession) throws X4OConnectionException,SAXException,IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream(4096);
		writeSession(languageSession, out);
		String encoding = (String)getProperty(ContentWriterXml.OUTPUT_ENCODING);
		return out.toString(encoding);
	}
}
