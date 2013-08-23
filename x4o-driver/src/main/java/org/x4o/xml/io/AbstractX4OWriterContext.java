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
import org.x4o.xml.lang.X4OLanguageContext;
import org.xml.sax.SAXException;

/**
 * AbstractX4OWriterContext.
 * 
 * @author Willem Cazander
 * @version 1.0 Apr 6, 2013
 */
public abstract class AbstractX4OWriterContext<T> extends AbstractX4OConnection implements X4OWriterContext<T> {

	public AbstractX4OWriterContext(X4OLanguage language) {
		super(language);
	}
	
	public void writeFileContext(X4OLanguageContext context,String fileName) throws X4OConnectionException,SAXException,IOException {
		if (fileName==null) {
			throw new NullPointerException("Can't convert null fileName to file object.");
		}		
		writeFileContext(context,new File(fileName));
	}
	
	public void writeFileContext(X4OLanguageContext context,File file) throws X4OConnectionException,SAXException,IOException {
		if (file==null) {
			throw new NullPointerException("Can't read null file.");
		}
		OutputStream outputStream = new FileOutputStream(file);
		try {
			writeContext(context,outputStream);
		} finally {
			outputStream.close();
		}
	}
	
	public String writeStringContext(X4OLanguageContext context) throws X4OConnectionException,SAXException,IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream(4096);
		writeContext(context, out);
		String encoding = (String)getProperty(ContentWriterXml.OUTPUT_ENCODING);
		return out.toString(encoding);
	}
}
