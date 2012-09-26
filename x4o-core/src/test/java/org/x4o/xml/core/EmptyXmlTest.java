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

package org.x4o.xml.core;

import java.io.FileNotFoundException;

import org.x4o.xml.core.config.X4OLanguagePropertyKeys;
import org.x4o.xml.test.TestParser;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import junit.framework.TestCase;

/**
 * Tests a simple x4o xml language.
 * 
 * @author Willem Cazander
 * @version 1.0 Jul 24, 2006
 */
public class EmptyXmlTest extends TestCase {
	
	public void setUp() throws Exception {
	}

	public void testFileNotFound() throws Exception {
		TestParser parser = new TestParser();
		try {
			parser.parseFile("tests/empty-xml/non-excisting-file.xml");
		} catch (FileNotFoundException e) {
			assertEquals(true, e.getMessage().contains("non-excisting-file.xml"));
			return;
		}
		assertEquals(true,false);
	}
	
	public void testResourceNotFound() throws Exception {
		TestParser parser = new TestParser();
		try {
			parser.parseResource("tests/empty-xml/non-excisting-resource.xml");
		} catch (NullPointerException e) {
			assertEquals(true,e.getMessage().contains("Could not find resource"));
			return;
		}
		assertEquals(true,false);
	}

	public void testResourceParsing() throws Exception {
		TestParser parser = new TestParser();
		try {
			parser.parseResource("tests/empty-xml/empty-test.xml");	
		} catch (SAXParseException e) {
			assertEquals("No ElementNamespaceContext found for empty namespace.", e.getMessage());
			return;
		}
		assertEquals(true,false);
	}

	public void testResourceEmptyReal() throws Exception {
		TestParser parser = new TestParser();
		try {
			parser.parseResource("tests/empty-xml/empty-real.xml");
		} catch (SAXException e) {
			assertEquals(true,e.getMessage().contains("Premature end of file."));
			return;
		}
		assertEquals(true,false);
	}

	public void testResourceEmptyXml() throws Exception {
		TestParser parser = new TestParser();
		try {
			parser.parseResource("tests/empty-xml/empty-xml.xml");
		} catch (SAXException e) {
			boolean hasError = e.getMessage().contains("Premature end of file.");
			if (System.getProperty("java.version").startsWith("1.5")) {
				hasError = e.getMessage().contains("A well-formed document requires a root element.");
			}
			
			assertEquals(true,hasError);
			return;
		}
		assertEquals(true,false);
	}
	
	public void testEmptyX40() throws Exception {
		TestParser parser = new TestParser();
		parser.setProperty(X4OLanguagePropertyKeys.PHASE_SKIP_RELEASE, true);
		try {
			parser.parseResource("tests/empty-xml/empty-x4o.xml");
			assertEquals(true,parser.getElementLanguage().getRootElement().getChilderen().isEmpty());
		} finally {
			parser.doReleasePhaseManual();
		}
	}
}
