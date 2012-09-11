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

package	org.x4o.xml.sax;

import	java.io.ByteArrayInputStream;
import java.io.File;
import	java.io.FileInputStream;
import	java.io.FileNotFoundException;
import	java.io.IOException;
import	java.io.InputStream;
import java.net.URL;

import	org.xml.sax.SAXException;
import	javax.xml.parsers.ParserConfigurationException;

/**
 * This is a base class for building an SAX XML parser.
 * It adds methode's for parsing different input types of 
 * xml data which gets wrapped into an InputStream for parsing.
 * 
 * @author Willem Cazander
 * @version 1.0 Aug 11, 2005
 */
abstract public class AbstractXMLParser {
	
	/**
	 * Method to parse the xml data.
	 * @param input	The inputStream to parse.
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	abstract public void parse(InputStream input,String systemId,URL basePath) throws ParserConfigurationException,SAXException,IOException;
	
	/**
	 * Reads the file fileName and parses it as an InputStream.
	 * @param fileName	The file name to parse.
	 * @throws ParserConfigurationException
	 * @throws FileNotFoundException
	 * @throws SecurityException
	 * @throws NullPointerException
	 * @throws SAXException
	 * @throws IOException
	 * @see org.x4o.xml.sax.AbstractXMLParser#parse(java.io.InputStream,java.lang.String,java.net.URL)
	 */
	public void parseFile(String fileName) throws ParserConfigurationException,FileNotFoundException,SecurityException,NullPointerException,SAXException,IOException {
		if (fileName==null) {
			throw new NullPointerException("Can't convert null fileName to file object.");
		}		
		parseFile(new File(fileName));
	}
	
	/**
	 * Reads the file and parses it as an InputStream.
	 * @param file	The file to parse.
	 * @throws ParserConfigurationException
	 * @throws FileNotFoundException
	 * @throws SecurityException
	 * @throws NullPointerException
	 * @throws SAXException
	 * @throws IOException
	 * @see org.x4o.xml.sax.AbstractXMLParser#parse(java.io.InputStream,java.lang.String,java.net.URL)
	 */
	public void parseFile(File file) throws ParserConfigurationException,FileNotFoundException,SecurityException,NullPointerException,SAXException,IOException {
		if (file==null) {
			throw new NullPointerException("Can't load null file.");
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
			parse(inputStream,file.getAbsolutePath(),basePath);
		} finally {
			if(inputStream!=null) {
				inputStream.close();
			}
		}
	}
	
	/**
	 * Parses an resource locaction.
	 * @param resourceName	The resource to parser.
	 * @throws ParserConfigurationException
	 * @throws FileNotFoundException
	 * @throws SecurityException
	 * @throws NullPointerException
	 * @throws SAXException
	 * @throws IOException
	 */
	public void parseResource(String resourceName) throws ParserConfigurationException,FileNotFoundException,SecurityException,NullPointerException,SAXException,IOException {
		if (resourceName==null) {
			throw new NullPointerException("Can't load null resourceName from classpath.");
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
			parse(inputStream,url.toExternalForm(),basePath);
		} finally {
			if(inputStream!=null) {
				inputStream.close();
			}
		}
	}
	
	/**
	 * Converts a String to a InputStream to is can me parsed by SAX.
	 * @param xmlString	The xml as String to parse.
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws NullPointerException
	 * @see org.x4o.xml.sax.AbstractXMLParser#parse(java.io.InputStream,java.lang.String,java.net.URL)
	 */
	public void parseXml(String xmlString) throws ParserConfigurationException,SAXException,IOException,NullPointerException {
		if (xmlString==null) {
			throw new NullPointerException("Can't parse null xml string.");
		}
		URL basePath = new File(System.getProperty("user.dir")).toURI().toURL();
		parse(new ByteArrayInputStream(xmlString.getBytes()),"inline-xml",basePath);
	}
	
	/**
	 * Fetched the data direct from remote url to a InputStream to is can me parsed by SAX.
	 * @param url	The url to parse.
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws NullPointerException
	 * @see org.x4o.xml.sax.AbstractXMLParser#parse(java.io.InputStream,java.lang.String,java.net.URL)
	 */
	public void parseUrl(URL url) throws ParserConfigurationException,SAXException,IOException,NullPointerException {
		if (url==null) {
			throw new NullPointerException("Can't parse null url.");
		}
		URL basePath = new URL(url.toExternalForm().substring(0,url.toExternalForm().length()-url.getFile().length()));
		parse(url.openStream(),url.toExternalForm(),basePath);
	}
}
