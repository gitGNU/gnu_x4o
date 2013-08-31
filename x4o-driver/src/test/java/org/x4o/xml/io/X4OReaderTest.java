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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.x4o.xml.X4ODriver;
import org.x4o.xml.io.X4OReader;
import org.x4o.xml.test.TestDriver;
import org.x4o.xml.test.models.TestBean;
import org.x4o.xml.test.models.TestObjectRoot;

import junit.framework.TestCase;

/**
 * X4OReaderTest.
 * 
 * @author Willem Cazander
 * @version 1.0 Apr 15, 2013
 */
public class X4OReaderTest extends TestCase {
	
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
	
	public void testReadInputStream() throws Exception {
		TestDriver driver = TestDriver.getInstance();
		X4OReaderSession<TestObjectRoot> reader = driver.createReaderSession();
		File xmlFile = copyResourceToTempFile();
		URL basePath = new File(xmlFile.getAbsolutePath()).toURI().toURL();
		InputStream inputStream = new FileInputStream(xmlFile);
		TestObjectRoot root = null;
		try {
			root = reader.read(inputStream, xmlFile.getAbsolutePath(), basePath);
		} finally {
			inputStream.close();
		}
		assertNotNull(root);
		assertTrue(root.getTestBeans().size()>0);
		TestBean bean = root.getTestBeans().get(0);
		assertNotNull(bean);
	}
	
	public void testReadResource() throws Exception {
		X4ODriver<TestObjectRoot> driver = TestDriver.getInstance();
		X4OReader<TestObjectRoot> reader = driver.createReader();
		TestObjectRoot root = reader.readResource("tests/attributes/test-bean.xml");
		assertNotNull(root);
	}
	
	public void testReadResourceNull() throws Exception {
		Exception e = null;
		try {
			X4ODriver<TestObjectRoot> driver = TestDriver.getInstance();
			X4OReader<TestObjectRoot> reader = driver.createReader();
			reader.readResource(null);
		} catch (Exception catchE) {
			e = catchE;
		}
		assertNotNull("No exception",e);
		assertEquals("Wrong exception class",NullPointerException.class, e.getClass());
		assertTrue("Wrong exception message",e.getMessage().contains("null"));
		assertTrue("Wrong exception message",e.getMessage().contains("resourceName"));
	}
	
	public void testReadString() throws Exception {
		X4ODriver<TestObjectRoot> driver = TestDriver.getInstance();
		X4OReader<TestObjectRoot> reader = driver.createReader();
		TestObjectRoot root = reader.readString(
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+
				"<root:root xmlns:root=\"http://test.x4o.org/xml/ns/test-root\" xmlns=\"http://test.x4o.org/xml/ns/test-lang\">"+
				"<testBean privateIntegerTypeField=\"987654321\"/>"+
				"</root:root>"
			);
		assertNotNull(root);
		assertTrue(root.getTestBeans().size()>0);
		TestBean bean = root.getTestBeans().get(0);
		assertNotNull(bean);
		assertEquals("987654321", ""+bean.getPrivateIntegerTypeField());
	}
	
	public void testReadStringNull() throws Exception {
		Exception e = null;
		try {
			X4ODriver<TestObjectRoot> driver = TestDriver.getInstance();
			X4OReader<TestObjectRoot> reader = driver.createReader();
			reader.readString(null);
		} catch (Exception catchE) {
			e = catchE;
		}
		assertNotNull("No exception",e);
		assertEquals("Wrong exception class",NullPointerException.class, e.getClass());
		assertTrue("Wrong exception message",e.getMessage().contains("null"));
		assertTrue("Wrong exception message",e.getMessage().contains("string"));
	}
	
	public void testReadUrl() throws Exception {
		X4ODriver<TestObjectRoot> driver = TestDriver.getInstance();
		X4OReader<TestObjectRoot> reader = driver.createReader();
		URL xmlUrl = Thread.currentThread().getContextClassLoader().getResource("tests/attributes/test-bean.xml");
		TestObjectRoot root = reader.readUrl(xmlUrl);
		assertNotNull(root);
		assertTrue(root.getTestBeans().size()>0);
		TestBean bean = root.getTestBeans().get(0);
		assertNotNull(bean);
	}
	
	public void testReadUrlNull() throws Exception {
		Exception e = null;
		try {
			X4ODriver<TestObjectRoot> driver = TestDriver.getInstance();
			X4OReader<TestObjectRoot> reader = driver.createReader();
			reader.readUrl(null);
		} catch (Exception catchE) {
			e = catchE;
		}
		assertNotNull("No exception",e);
		assertEquals("Wrong exception class",NullPointerException.class, e.getClass());
		assertTrue("Wrong exception message",e.getMessage().contains("null"));
		assertTrue("Wrong exception message",e.getMessage().contains("url"));
	}
	
	public void testReadFileName() throws Exception {
		X4ODriver<TestObjectRoot> driver = TestDriver.getInstance();
		X4OReader<TestObjectRoot> reader = driver.createReader();
		File xmlFile = copyResourceToTempFile();
		TestObjectRoot root = reader.readFile(xmlFile.getAbsolutePath());
		assertNotNull(root);
		assertTrue(root.getTestBeans().size()>0);
		TestBean bean = root.getTestBeans().get(0);
		assertNotNull(bean);
	}
	
	public void testReadFileNameNull() throws Exception {
		Exception e = null;
		try {
			X4ODriver<TestObjectRoot> driver = TestDriver.getInstance();
			X4OReader<TestObjectRoot> reader = driver.createReader();
			String nullFileName = null;
			reader.readFile(nullFileName);
		} catch (Exception catchE) {
			e = catchE;
		}
		assertNotNull("No exception",e);
		assertEquals("Wrong exception class",NullPointerException.class, e.getClass());
		assertTrue("Wrong exception message",e.getMessage().contains("null"));
		assertTrue("Wrong exception message",e.getMessage().contains("fileName"));
	}
	
	public void testReadFile() throws Exception {
		X4ODriver<TestObjectRoot> driver = TestDriver.getInstance();
		X4OReader<TestObjectRoot> reader = driver.createReader();
		File xmlFile = copyResourceToTempFile();
		TestObjectRoot root = reader.readFile(xmlFile);
		assertNotNull(root);
		assertTrue(root.getTestBeans().size()>0);
		TestBean bean = root.getTestBeans().get(0);
		assertNotNull(bean);
	}
	
	public void testReadFileNull() throws Exception {
		Exception e = null;
		try {
			X4ODriver<TestObjectRoot> driver = TestDriver.getInstance();
			X4OReader<TestObjectRoot> reader = driver.createReader();
			File nullFile = null;
			reader.readFile(nullFile);
		} catch (Exception catchE) {
			e = catchE;
		}
		assertNotNull("No exception",e);
		assertEquals("Wrong exception class",NullPointerException.class, e.getClass());
		assertTrue("Wrong exception message",e.getMessage().contains("null"));
		assertTrue("Wrong exception message",e.getMessage().contains("file"));
	}
}
