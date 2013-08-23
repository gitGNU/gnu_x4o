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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.x4o.xml.X4ODriver;
import org.x4o.xml.element.Element;
import org.x4o.xml.io.X4OReader;
import org.x4o.xml.lang.X4OLanguageContext;
import org.x4o.xml.test.TestDriver;
import org.x4o.xml.test.models.TestObjectRoot;
import org.xml.sax.SAXException;

import junit.framework.TestCase;

/**
 * X4OWriterContextTest.
 * 
 * @author Willem Cazander
 * @version 1.0 Apr 28, 2013
 */
public class X4OWriterContextTest extends TestCase {
	
	private File createOutputFile() throws IOException {
		File outputFile = File.createTempFile("test-writer-context", ".xml");
		outputFile.deleteOnExit();
		return outputFile;
	}
	
	private X4OLanguageContext createContext() throws SAXException, X4OConnectionException, IOException {
		X4ODriver<TestObjectRoot> driver = TestDriver.getInstance();
		X4OReader<TestObjectRoot> reader = driver.createReader();
		TestObjectRoot root = reader.readResource("tests/attributes/test-bean.xml");
		X4OLanguageContext context = driver.createLanguage().createLanguageContext();
		Element rootElement = null;
		try {
			rootElement = (Element)context.getLanguage().getLanguageConfiguration().getDefaultElement().newInstance();
		} catch (InstantiationException e) {
			throw new SAXException(e);
		} catch (IllegalAccessException e) {
			throw new SAXException(e);
		}
		rootElement.setElementObject(root);
		context.setRootElement(rootElement);
		return context;
	}
	
	public void testWriteFile() throws Exception {
		File outputFile = createOutputFile();
		X4ODriver<TestObjectRoot> driver = TestDriver.getInstance();
		X4OWriterContext<TestObjectRoot> writer = driver.createWriterContext();

		writer.writeFileContext(createContext(), outputFile);
		String text = X4OWriterTest.readFile( outputFile );
		outputFile.delete();

		assertTrue(text.startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"));
		assertTrue(text.contains("http://test.x4o.org/xml/ns/test-root"));
		assertTrue(text.contains("<test-lang:parent name=\"test-bean.xml\"/>"));
		assertTrue(text.contains("<test-lang:testBean"));
	}
	
	public void testWriteFileNull() throws Exception {
		TestDriver driver = TestDriver.getInstance();
		X4OWriterContext<TestObjectRoot> writer = driver.createWriterContext();
		Exception e = null;
		File nullFile = null;
		try {
			writer.writeFileContext(createContext(), nullFile);
		} catch (Exception catchE) {
			e = catchE;
		}
		assertNotNull("No exception",e);
		assertEquals("Wrong exception class",NullPointerException.class, e.getClass());
		assertTrue("Wrong exception message",e.getMessage().contains("null"));
		assertTrue("Wrong exception message",e.getMessage().contains("file"));
	}
	
	public void testWriteFileName() throws Exception {
		File outputFile = createOutputFile();
		X4ODriver<TestObjectRoot> driver = TestDriver.getInstance();
		X4OWriterContext<TestObjectRoot> writer = driver.createWriterContext();
		
		writer.writeFileContext(createContext(), outputFile.getAbsolutePath());
		String text = X4OWriterTest.readFile( outputFile );
		outputFile.delete();

		assertTrue(text.startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"));
		assertTrue(text.contains("http://test.x4o.org/xml/ns/test-root"));
		assertTrue(text.contains("<test-lang:parent name=\"test-bean.xml\"/>"));
		assertTrue(text.contains("<test-lang:testBean"));
	}
	
	public void testWriteFileNameNull() throws Exception {
		TestDriver driver = TestDriver.getInstance();
		X4OWriterContext<TestObjectRoot> writer = driver.createWriterContext();
		Exception e = null;
		String nullFileName = null;
		try {
			writer.writeFileContext(createContext(), nullFileName);
		} catch (Exception catchE) {
			e = catchE;
		}
		assertNotNull("No exception",e);
		assertEquals("Wrong exception class",NullPointerException.class, e.getClass());
		assertTrue("Wrong exception message",e.getMessage().contains("null"));
		assertTrue("Wrong exception message",e.getMessage().contains("fileName"));
	}
	
	public void testWriteStream() throws Exception {
		File outputFile = createOutputFile();
		X4ODriver<TestObjectRoot> driver = TestDriver.getInstance();
		X4OWriterContext<TestObjectRoot> writer = driver.createWriterContext();
		
		OutputStream outputStream = new FileOutputStream(outputFile);
		try {
			writer.writeContext(createContext(),outputStream);
		} finally {
			outputStream.close();
		}
		String text = X4OWriterTest.readFile( outputFile );
		outputFile.delete();

		assertTrue(text.startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"));
		assertTrue(text.contains("http://test.x4o.org/xml/ns/test-root"));
		assertTrue(text.contains("<test-lang:parent name=\"test-bean.xml\"/>"));
		assertTrue(text.contains("<test-lang:testBean"));
	}
}
