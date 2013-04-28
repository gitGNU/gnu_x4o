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

import	java.io.ByteArrayInputStream;
import	java.io.File;
import	java.io.FileInputStream;
import	java.io.FileNotFoundException;
import	java.io.IOException;
import	java.io.InputStream;
import	java.net.URL;

import org.x4o.xml.lang.X4OLanguageContext;
import	org.xml.sax.SAXException;

/**
 * AbstractX4OContextReader

 * 
 * @author Willem Cazander
 * @version 1.0 Apr 6, 2013
 */
abstract public class AbstractX4OReaderContext<T> extends AbstractX4OConnection implements X4OReaderContext<T> {
	
	public AbstractX4OReaderContext(X4OLanguageContext languageContext) {
		super(languageContext);
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
	public X4OLanguageContext readFileContext(String fileName) throws X4OConnectionException,SAXException,IOException,FileNotFoundException {
		if (fileName==null) {
			throw new NullPointerException("Can't convert null fileName to file object.");
		}		
		return readFileContext(new File(fileName));
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
	public X4OLanguageContext readFileContext(File file) throws X4OConnectionException,SAXException,IOException,FileNotFoundException {
		if (file==null) {
			throw new NullPointerException("Can't read null file.");
		}
		if (file.exists()==false) {
			throw new FileNotFoundException("File does not exists; "+file);
		}
		if (file.canRead()==false) {
			throw new IOException("File exists but can't read file: "+file);
		}
		URL basePath = new File(file.getAbsolutePath()).toURI().toURL();
		InputStream inputStream = new FileInputStream(file);
		try {
			return readContext(inputStream,file.getAbsolutePath(),basePath);
		} finally {
			if(inputStream!=null) {
				inputStream.close();
			}
		}
	}
	
	/**
	 * reads an resource locaction.
	 * @param resourceName	The resource to readr.
	 * @throws X4OConnectionException	Is thrown after x4o exception.
	 * @throws SAXException	Is thrown after sax xml exception.
	 * @throws IOException	Is thrown after io exception.
	 * @see org.x4o.xml.io.X4OReaderContext#readContext(java.io.InputStream,java.lang.String,java.net.URL)
	 */
	public X4OLanguageContext readResourceContext(String resourceName) throws X4OConnectionException,SAXException,IOException {
		if (resourceName==null) {
			throw new NullPointerException("Can't read null resourceName from classpath.");
		}
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		if (cl == null) cl = getClass().getClassLoader(); // fallback
		URL url = cl.getResource(resourceName);
		if (url==null) {
			throw new NullPointerException("Could not find resource on classpath: "+resourceName);
		}
		String baseUrl = url.toExternalForm();
		int lastSlash = baseUrl.lastIndexOf('/');
		if (lastSlash > 0 && (lastSlash+1) < baseUrl.length()) {
			baseUrl = baseUrl.substring(0,lastSlash+1);
		}
		URL basePath = new URL(baseUrl);
		InputStream inputStream = cl.getResourceAsStream(resourceName);
		try {
			return readContext(inputStream,url.toExternalForm(),basePath);
		} finally {
			if(inputStream!=null) {
				inputStream.close();
			}
		}
	}
	
	/**
	 * Converts a String to a InputStream to is can me readd by SAX.
	 * @param xmlString	The xml as String to read.
	 * @throws X4OConnectionException	Is thrown after x4o exception.
	 * @throws SAXException	Is thrown after sax xml exception.
	 * @throws IOException	Is thrown after io exception.
	 * @see org.x4o.xml.io.X4OReaderContext#readContext(java.io.InputStream,java.lang.String,java.net.URL)
	 */
	public X4OLanguageContext readStringContext(String xmlString) throws X4OConnectionException,SAXException,IOException {
		if (xmlString==null) {
			throw new NullPointerException("Can't read null xml string.");
		}
		URL basePath = new File(System.getProperty("user.dir")).toURI().toURL();
		return readContext(new ByteArrayInputStream(xmlString.getBytes()),"inline-xml",basePath);
	}
	
	/**
	 * Fetched the data direct from remote url to a InputStream to is can me readd by SAX.
	 * @param url	The url to read.
	 * @throws X4OConnectionException	Is thrown after x4o exception.
	 * @throws SAXException	Is thrown after sax xml exception.
	 * @throws IOException	Is thrown after io exception.
	 * @see org.x4o.xml.io.X4OReaderContext#readContext(java.io.InputStream,java.lang.String,java.net.URL)
	 */
	public X4OLanguageContext readUrlContext(URL url) throws X4OConnectionException,SAXException,IOException {
		if (url==null) {
			throw new NullPointerException("Can't read null url.");
		}
		URL basePath = new URL(url.toExternalForm().substring(0,url.toExternalForm().length()-url.getFile().length()));
		return readContext(url.openStream(),url.toExternalForm(),basePath);
	}
}
