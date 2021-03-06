/*
 * Copyright (c) 2004-2014, Willem Cazander
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

import org.x4o.xml.lang.X4OLanguageSession;
import org.x4o.xml.lang.phase.X4OPhaseException;
import org.xml.sax.SAXException;

/**
 * X4OReaderSession is reader with language session.
 * 
 * @author Willem Cazander
 * @version 1.0 Apr 6, 2013
 */
public interface X4OReaderSession<T> extends X4OReader<T> {
	
	void releaseSession(X4OLanguageSession context) throws X4OPhaseException;
	
	/**
	 * Method to parse the xml data.
	 * @param input	The inputStream to parse.
	 * @throws X4OConnectionException	Is thrown after x4o exception.
	 * @throws SAXException	Is thrown after sax xml exception.
	 * @throws IOException	Is thrown after io exception.
	 */
	X4OLanguageSession readSession(InputStream input,String systemId,URL basePath) throws X4OConnectionException,SAXException,IOException;
	
	/**
	 * Reads the file fileName and parses it as an InputStream.
	 * @param fileName	The file name to parse.
	 * @throws FileNotFoundException	Is thrown is file is not found.
	 * @throws X4OConnectionException	Is thrown after x4o exception.
	 * @throws SAXException	Is thrown after sax xml exception.
	 * @throws IOException	Is thrown after io exception. 
	 * @see org.x4o.xml.io.X4OReaderSession#readSession(java.io.InputStream,java.lang.String,java.net.URL)
	 */
	X4OLanguageSession readFileSession(String fileName) throws X4OConnectionException,SAXException,IOException,FileNotFoundException;
	
	/**
	 * Reads the file and parses it as an InputStream.
	 * @param file	The file to parse.
	 * @throws FileNotFoundException	Is thrown is file is not found.
	 * @throws X4OConnectionException	Is thrown after x4o exception.
	 * @throws SAXException	Is thrown after sax xml exception.
	 * @throws IOException	Is thrown after io exception. 
	 * @see org.x4o.xml.io.X4OReaderSession#readSession(java.io.InputStream,java.lang.String,java.net.URL)
	 */
	X4OLanguageSession readFileSession(File file) throws X4OConnectionException,SAXException,IOException,FileNotFoundException;
	
	/**
	 * Parses an resource locaction.
	 * @param resourceName	The resource to parser.
	 * @throws X4OConnectionException	Is thrown after x4o exception.
	 * @throws SAXException	Is thrown after sax xml exception.
	 * @throws IOException	Is thrown after io exception.
	 * @see org.x4o.xml.io.X4OReaderSession#readSession(java.io.InputStream,java.lang.String,java.net.URL)
	 */
	X4OLanguageSession readResourceSession(String resourceName) throws X4OConnectionException,SAXException,IOException;
	
	/**
	 * Converts a String to a InputStream to is can be parsed by SAX.
	 * @param xmlString	The xml as String to parse.
	 * @throws X4OConnectionException	Is thrown after x4o exception.
	 * @throws SAXException	Is thrown after sax xml exception.
	 * @throws IOException	Is thrown after io exception.
	 * @see org.x4o.xml.io.X4OReaderSession#readSession(java.io.InputStream,java.lang.String,java.net.URL)
	 */
	X4OLanguageSession readStringSession(String xmlString) throws X4OConnectionException,SAXException,IOException;
	
	/**
	 * Fetched the data direct from remote url to a InputStream to is can be parsed by SAX.
	 * @param url	The url to parse.
	 * @throws X4OConnectionException	Is thrown after x4o exception.
	 * @throws SAXException	Is thrown after sax xml exception.
	 * @throws IOException	Is thrown after io exception.
	 * @see org.x4o.xml.io.X4OReaderSession#readSession(java.io.InputStream,java.lang.String,java.net.URL)
	 */
	X4OLanguageSession readUrlSession(URL url) throws X4OConnectionException,SAXException,IOException;
}
