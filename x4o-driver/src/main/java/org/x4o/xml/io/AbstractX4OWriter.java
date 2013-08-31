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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import org.x4o.xml.element.Element;
import org.x4o.xml.lang.X4OLanguage;
import org.x4o.xml.lang.X4OLanguageSession;
import org.xml.sax.SAXException;

/**
 * AbstractX4OWriter.
 * 
 * @author Willem Cazander
 * @version 1.0 Apr 6, 2013
 */
public abstract class AbstractX4OWriter<T> extends AbstractX4OWriterSession<T> implements X4OWriter<T> {

	public AbstractX4OWriter(X4OLanguage language) {
		super(language);
	}
	
	abstract X4OLanguageSession createLanguageSession();
	
	private X4OLanguageSession toObjectContext(T object) throws SAXException {
		X4OLanguageSession context = createLanguageSession();
		Element rootElement = null;
		try {
			rootElement = (Element)context.getLanguage().getLanguageConfiguration().getDefaultElement().newInstance();
		} catch (InstantiationException e) {
			throw new SAXException(e);
		} catch (IllegalAccessException e) {
			throw new SAXException(e);
		}
		rootElement.setElementObject(object);
		rootElement.setLanguageSession(context);
		context.setRootElement(rootElement);
		return context;
	}
	
	public void write(T object,OutputStream output) throws X4OConnectionException,SAXException,IOException {
		writeSession(toObjectContext(object), output);
	}
	
	public void writeFile(T object,String fileName) throws X4OConnectionException,SAXException,IOException,FileNotFoundException {
		writeFileSession(toObjectContext(object), fileName);
	}
	
	public void writeFile(T object,File file) throws X4OConnectionException,SAXException,IOException,FileNotFoundException {
		writeFileSession(toObjectContext(object), file);
	}
	
	public String writeString(T object) throws X4OConnectionException,SAXException,IOException,FileNotFoundException {
		return writeStringSession(toObjectContext(object));
	}
}
