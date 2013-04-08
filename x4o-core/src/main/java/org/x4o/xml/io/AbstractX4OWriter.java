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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.xml.parsers.ParserConfigurationException;

import org.x4o.xml.element.Element;
import org.x4o.xml.lang.X4OLanguageContext;
import org.x4o.xml.lang.X4OLanguagePropertyKeys;
import org.xml.sax.SAXException;

public abstract class AbstractX4OWriter<T> extends AbstractX4OWriterContext<T> implements X4OWriter<T> {

	public AbstractX4OWriter(X4OLanguageContext elementLanguage) {
		super(elementLanguage);
	}
	
	/**
	 * @see org.x4o.xml.io.X4OConnection#getPropertyKeySet()
	 */
	public String[] getPropertyKeySet() {
		return X4OLanguagePropertyKeys.DEFAULT_X4O_WRITER_KEYS;
	}
	
	public void write(T object,OutputStream output) throws ParserConfigurationException, SAXException, IOException {
		X4OLanguageContext context = getLanguageContext();
		Element rootElement = null;
		try {
			rootElement = (Element)context.getLanguage().getLanguageConfiguration().getDefaultElement().newInstance();
		} catch (InstantiationException e) {
			throw new SAXException(e);
		} catch (IllegalAccessException e) {
			throw new SAXException(e);
		}
		rootElement.setElementObject(object);
		context.setRootElement(rootElement);
		writeContext(context,output);
	}
	
	public void writeFile(T object,String fileName) throws ParserConfigurationException,FileNotFoundException,SecurityException,NullPointerException,SAXException,IOException {
		if (fileName==null) {
			throw new NullPointerException("Can't convert null fileName to file object.");
		}		
		writeFile(object,new File(fileName));
	}
	
	public void writeFile(T object,File file) throws ParserConfigurationException,FileNotFoundException,SecurityException,NullPointerException,SAXException,IOException {
		if (file==null) {
			throw new NullPointerException("Can't read null file.");
		}
		if (file.canWrite()==false) {
			throw new IOException("Can't write file: "+file);
		}
		OutputStream outputStream = new FileOutputStream(file);
		try {
			write(object,outputStream);
		} finally {
			if(outputStream!=null) {
				outputStream.close();
			}
		}
	}
}