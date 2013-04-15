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
import java.net.URL;

import org.x4o.xml.X4ODriver;
import org.x4o.xml.io.X4OReader;
import org.x4o.xml.lang.X4OLanguageContext;
import org.x4o.xml.test.TestDriver;
import org.x4o.xml.test.models.TestBean;
import org.x4o.xml.test.models.TestObjectRoot;

import junit.framework.TestCase;

/**
 * X4OAbstractReaderContextTest.
 * 
 * @author Willem Cazander
 * @version 1.0 Apr 15, 2013
 */
public class X4OAbstractReaderContextTest extends TestCase {
	
	private File copyResourceToTempFile() throws IOException {
		File tempFile = File.createTempFile("test-resource", ".xml");
		tempFile.deleteOnExit();
		X4ODriver<TestObjectRoot> driver = TestDriver.getInstance();
		X4OReader<TestObjectRoot> reader = driver.createReader();
		X4OWriter<TestObjectRoot> writer = driver.createWriter();
		try {
			writer.writeFile(reader.readResource("tests/attributes/test-bean.xml"), tempFile);
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
		return tempFile;
	}
	
	public void testReadFileName() throws Exception {
		TestDriver driver = TestDriver.getInstance();
		X4OReaderContext<TestObjectRoot> reader = driver.createReaderContext();
		File xmlFile = copyResourceToTempFile();
		X4OLanguageContext context = reader.readFileContext(xmlFile.getAbsolutePath());
		TestObjectRoot root = (TestObjectRoot)context.getRootElement().getElementObject();
		assertNotNull(root);
		assertTrue(root.getTestBeans().size()>0);
		TestBean bean = root.getTestBeans().get(0);
		assertNotNull(bean);
	}
	
	public void testReadFileNameNull() throws Exception {
		TestDriver driver = TestDriver.getInstance();
		X4OReaderContext<TestObjectRoot> reader = driver.createReaderContext();
		Exception e = null;
		try {
			String nullFileName = null;
			reader.readFileContext(nullFileName);
		} catch (Exception catchE) {
			e = catchE;
		}
		assertNotNull("No exception",e);
		assertEquals("Wrong exception class",NullPointerException.class, e.getClass());
		assertTrue("Wrong exception message",e.getMessage().contains("null"));
		assertTrue("Wrong exception message",e.getMessage().contains("fileName"));
	}
	
	public void testReadFile() throws Exception {
		TestDriver driver = TestDriver.getInstance();
		X4OReaderContext<TestObjectRoot> reader = driver.createReaderContext();
		File xmlFile = copyResourceToTempFile();
		X4OLanguageContext context = reader.readFileContext(xmlFile);
		TestObjectRoot root = (TestObjectRoot)context.getRootElement().getElementObject();
		assertNotNull(root);
		assertTrue(root.getTestBeans().size()>0);
		TestBean bean = root.getTestBeans().get(0);
		assertNotNull(bean);
	}
	
	public void testReadFileNull() throws Exception {
		TestDriver driver = TestDriver.getInstance();
		X4OReaderContext<TestObjectRoot> reader = driver.createReaderContext();
		Exception e = null;
		try {
			File nullFile = null;
			reader.readFileContext(nullFile);
		} catch (Exception catchE) {
			e = catchE;
		}
		assertNotNull("No exception",e);
		assertEquals("Wrong exception class",NullPointerException.class, e.getClass());
		assertTrue("Wrong exception message",e.getMessage().contains("null"));
		assertTrue("Wrong exception message",e.getMessage().contains("file"));
	}
	
	public void testReadFileNotExists() throws Exception {
		TestDriver driver = TestDriver.getInstance();
		X4OReaderContext<TestObjectRoot> reader = driver.createReaderContext();
		Exception e = null;
		try {
			File tempFile = File.createTempFile("test-file", ".xml");
			tempFile.delete();
			reader.readFileContext(tempFile);
		} catch (Exception catchE) {
			e = catchE;
		}
		assertNotNull("No exception",e);
		assertEquals("Wrong exception class",FileNotFoundException.class, e.getClass());
		assertTrue("Wrong exception message",e.getMessage().contains("exists"));
		assertTrue("Wrong exception message",e.getMessage().contains("File"));
	}
	
	
	public void testReadResource() throws Exception {
		TestDriver driver = TestDriver.getInstance();
		X4OReaderContext<TestObjectRoot> reader = driver.createReaderContext();
		X4OLanguageContext context = reader.readResourceContext("tests/attributes/test-bean.xml");
		TestObjectRoot root = (TestObjectRoot)context.getRootElement().getElementObject();
		assertNotNull(root);
	}
	
	public void testReadResourceNull() throws Exception {
		TestDriver driver = TestDriver.getInstance();
		X4OReaderContext<TestObjectRoot> reader = driver.createReaderContext();
		Exception e = null;
		try {
			reader.readResourceContext(null);
		} catch (Exception catchE) {
			e = catchE;
		}
		assertNotNull("No exception",e);
		assertEquals("Wrong exception class",NullPointerException.class, e.getClass());
		assertTrue("Wrong exception message",e.getMessage().contains("null"));
		assertTrue("Wrong exception message",e.getMessage().contains("resourceName"));
	}
	
	public void testReadString() throws Exception {
		TestDriver driver = TestDriver.getInstance();
		X4OReaderContext<TestObjectRoot> reader = driver.createReaderContext();
		X4OLanguageContext context = reader.readStringContext(
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+
				"<root:root xmlns:root=\"http://test.x4o.org/xml/ns/test-root\" xmlns=\"http://test.x4o.org/xml/ns/test-lang\">"+
				"<testBean privateIntegerTypeField=\"987654321\"/>"+
				"</root:root>"
			);
		TestObjectRoot root = (TestObjectRoot)context.getRootElement().getElementObject();
		assertNotNull(root);
		assertTrue(root.getTestBeans().size()>0);
		TestBean bean = root.getTestBeans().get(0);
		assertNotNull(bean);
		assertEquals("987654321", ""+bean.getPrivateIntegerTypeField());
	}
	
	public void testReadStringNull() throws Exception {
		TestDriver driver = TestDriver.getInstance();
		X4OReaderContext<TestObjectRoot> reader = driver.createReaderContext();
		Exception e = null;
		try {
			reader.readStringContext(null);
		} catch (Exception catchE) {
			e = catchE;
		}
		assertNotNull("No exception",e);
		assertEquals("Wrong exception class",NullPointerException.class, e.getClass());
		assertTrue("Wrong exception message",e.getMessage().contains("null"));
		assertTrue("Wrong exception message",e.getMessage().contains("string"));
	}
	
	public void testReadUrl() throws Exception {
		TestDriver driver = TestDriver.getInstance();
		X4OReaderContext<TestObjectRoot> reader = driver.createReaderContext();
		URL xmlUrl = Thread.currentThread().getContextClassLoader().getResource("tests/attributes/test-bean.xml");
		X4OLanguageContext context = reader.readUrlContext(xmlUrl);
		TestObjectRoot root = (TestObjectRoot)context.getRootElement().getElementObject();
		assertNotNull(root);
		assertTrue(root.getTestBeans().size()>0);
		TestBean bean = root.getTestBeans().get(0);
		assertNotNull(bean);
	}
	
	public void testReadUrlNull() throws Exception {
		TestDriver driver = TestDriver.getInstance();
		X4OReaderContext<TestObjectRoot> reader = driver.createReaderContext();
		Exception e = null;
		try {
			reader.readUrlContext(null);
		} catch (Exception catchE) {
			e = catchE;
		}
		assertNotNull("No exception",e);
		assertEquals("Wrong exception class",NullPointerException.class, e.getClass());
		assertTrue("Wrong exception message",e.getMessage().contains("null"));
		assertTrue("Wrong exception message",e.getMessage().contains("url"));
	}
}
