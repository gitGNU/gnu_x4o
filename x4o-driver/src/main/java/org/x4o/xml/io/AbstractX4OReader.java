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

package	org.x4o.xml.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.x4o.xml.lang.X4OLanguageContext;
import org.x4o.xml.lang.X4OLanguagePropertyKeys;
import org.xml.sax.SAXException;

/**
 * AbstractX4OReader wraps method to contexted reader.
 * 
 * @author Willem Cazander
 * @version 1.0 Aug 11, 2005
 */
@SuppressWarnings("unchecked")
abstract public class AbstractX4OReader<T> extends AbstractX4OReaderContext<T> implements X4OReader<T> {
	
	public AbstractX4OReader(X4OLanguageContext elementLanguage) {
		super(elementLanguage);
	}

	/**
	 * @see org.x4o.xml.io.X4OConnection#getPropertyKeySet()
	 */
	public String[] getPropertyKeySet() {
		return X4OLanguagePropertyKeys.DEFAULT_X4O_READER_KEYS;
	}
	
	public T read(InputStream input, String systemId, URL basePath) throws X4OConnectionException,SAXException,IOException {
		return (T)readContext(input, systemId, basePath).getRootElement().getElementObject();
	}
	
	/**
	 * Reads the file fileName and reads it as an InputStream.
	 * @param fileName	The file name to read.
	 * @throws FileNotFoundException	Is thrown is file is not found.
	 * @throws X4OConnectionException	Is thrown after x4o exception.
	 * @throws SAXException	Is thrown after sax xml exception.
	 * @throws IOException	Is thrown after io exception. 
	 * @see org.x4o.xml.io.X4OReaderContext#readContext(java.io.InputStream,java.lang.String,java.net.URL)
	 */
	public T readFile(String fileName) throws X4OConnectionException,SAXException,IOException,FileNotFoundException {
		return (T)readFileContext(fileName).getRootElement().getElementObject();
	}
	
	/**
	 * Reads the file and reads it as an InputStream.
	 * @param file	The file to read.
	 * @throws FileNotFoundException	Is thrown is file is not found.
	 * @throws X4OConnectionException	Is thrown after x4o exception.
	 * @throws SAXException	Is thrown after sax xml exception.
	 * @throws IOException	Is thrown after io exception. 
	 * @see org.x4o.xml.io.X4OReaderContext#readContext(java.io.InputStream,java.lang.String,java.net.URL)
	 */
	public T readFile(File file) throws X4OConnectionException,SAXException,IOException,FileNotFoundException {
		return (T)readFileContext(file).getRootElement().getElementObject();
	}
	
	/**
	 * reads an resource locaction.
	 * @param resourceName	The resource to readr.
	 * @throws X4OConnectionException	Is thrown after x4o exception.
	 * @throws SAXException	Is thrown after sax xml exception.
	 * @throws IOException	Is thrown after io exception. 
	 * @see org.x4o.xml.io.X4OReaderContext#readContext(java.io.InputStream,java.lang.String,java.net.URL)
	 */
	public T readResource(String resourceName) throws X4OConnectionException,SAXException,IOException {
		return (T)readResourceContext(resourceName).getRootElement().getElementObject();
	}
	
	/**
	 * Converts a String to a InputStream to is can me readd by SAX.
	 * @param xmlString	The xml as String to read.
	 * @throws X4OConnectionException	Is thrown after x4o exception.
	 * @throws SAXException	Is thrown after sax xml exception.
	 * @throws IOException	Is thrown after io exception. 
	 * @see org.x4o.xml.io.X4OReaderContext#readContext(java.io.InputStream,java.lang.String,java.net.URL)
	 */
	public T readString(String xmlString) throws X4OConnectionException,SAXException,IOException {
		return (T)readStringContext(xmlString).getRootElement().getElementObject();
	}
	
	/**
	 * Fetched the data direct from remote url to a InputStream to is can me readd by SAX.
	 * @param url	The url to read.
	 * @throws X4OConnectionException	Is thrown after x4o exception.
	 * @throws SAXException	Is thrown after sax xml exception.
	 * @throws IOException	Is thrown after io exception. 
	 * @see org.x4o.xml.io.X4OReaderContext#readContext(java.io.InputStream,java.lang.String,java.net.URL)
	 */
	public T readUrl(URL url) throws X4OConnectionException,SAXException,IOException {
		return (T)readUrlContext(url).getRootElement().getElementObject();
	}
}
