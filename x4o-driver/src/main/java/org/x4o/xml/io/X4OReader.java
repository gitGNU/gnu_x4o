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
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

/**
 * X4OReader can read different input formats.
 * 
 * @author Willem Cazander
 * @version 1.0 Apr 6, 2013
 */
public interface X4OReader<T> extends X4OConnection {

	public void addELBeanInstance(String name,Object bean);
	
	/**
	 * Method to parse the xml data.
	 * @param input	The inputStream to parse.
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	T read(InputStream input,String systemId,URL basePath) throws ParserConfigurationException,SAXException,IOException;
	
	/**
	 * Reads the file fileName and parses it as an InputStream.
	 * @param fileName	The file name to parse.
	 * @throws ParserConfigurationException
	 * @throws FileNotFoundException
	 * @throws SecurityException
	 * @throws NullPointerException
	 * @throws SAXException
	 * @throws IOException
	 * @see org.x4o.xml.io.X4OReaderContext#readContext(java.io.InputStream,java.lang.String,java.net.URL)
	 */
	T readFile(String fileName) throws ParserConfigurationException,FileNotFoundException,SecurityException,NullPointerException,SAXException,IOException;
	
	/**
	 * Reads the file and parses it as an InputStream.
	 * @param file	The file to parse.
	 * @throws ParserConfigurationException
	 * @throws FileNotFoundException
	 * @throws SecurityException
	 * @throws NullPointerException
	 * @throws SAXException
	 * @throws IOException
	 * @see org.x4o.xml.io.X4OReaderContext#readContext(java.io.InputStream,java.lang.String,java.net.URL)
	 */
	T readFile(File file) throws ParserConfigurationException,FileNotFoundException,SecurityException,NullPointerException,SAXException,IOException;
	
	/**
	 * Parses an resource locaction.
	 * @param resourceName	The resource to parser.
	 * @throws ParserConfigurationException
	 * @throws FileNotFoundException
	 * @throws SecurityException
	 * @throws NullPointerException
	 * @throws SAXException
	 * @throws IOException
	 * @see org.x4o.xml.io.X4OReaderContext#readContext(java.io.InputStream,java.lang.String,java.net.URL)
	 */
	T readResource(String resourceName) throws ParserConfigurationException,FileNotFoundException,SecurityException,NullPointerException,SAXException,IOException;
	
	/**
	 * Converts a String to a InputStream to is can me parsed by SAX.
	 * @param xmlString	The xml as String to parse.
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws NullPointerException
	 * @see org.x4o.xml.io.X4OReaderContext#readContext(java.io.InputStream,java.lang.String,java.net.URL)
	 */
	T readString(String xmlString) throws ParserConfigurationException,SAXException,IOException,NullPointerException;
	
	/**
	 * Fetched the data direct from remote url to a InputStream to is can me parsed by SAX.
	 * @param url	The url to parse.
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws NullPointerException
	 * @see org.x4o.xml.io.X4OReaderContext#readContext(java.io.InputStream,java.lang.String,java.net.URL)
	 */
	T readUrl(URL url) throws ParserConfigurationException,SAXException,IOException,NullPointerException;
}
