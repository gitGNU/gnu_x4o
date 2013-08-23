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

import org.x4o.xml.X4ODriver;
import org.x4o.xml.io.X4OReader;
import org.x4o.xml.test.TestDriver;
import org.x4o.xml.test.models.TestObjectRoot;

import junit.framework.TestCase;

/**
 * X4OConnectionTest.
 * 
 * @author Willem Cazander
 * @version 1.0 Apr 28, 2013
 */
public class X4OConnectionTest extends TestCase {
	
	public void testReaderPropertyFailRead() throws Exception {
		Exception e = null;
		try {
			X4ODriver<TestObjectRoot> driver = TestDriver.getInstance();
			X4OReader<TestObjectRoot> reader = driver.createReader();
			reader.getProperty("test");
		} catch (Exception catchE) {
			e = catchE;
		}
		assertNotNull("No exception",e);
		assertEquals("Wrong exception class",IllegalArgumentException.class, e.getClass());
		assertTrue("Wrong exception message",e.getMessage().contains("key"));
		assertTrue("Wrong exception message",e.getMessage().contains("No"));
	}
	
	public void testReaderPropertyFail() throws Exception {
		Exception e = null;
		try {
			X4ODriver<TestObjectRoot> driver = TestDriver.getInstance();
			X4OReader<TestObjectRoot> reader = driver.createReader();
			reader.setProperty("test", "test");
		} catch (Exception catchE) {
			e = catchE;
		}
		assertNotNull("No exception",e);
		assertEquals("Wrong exception class",IllegalArgumentException.class, e.getClass());
		assertTrue("Wrong exception message",e.getMessage().contains("key"));
		assertTrue("Wrong exception message",e.getMessage().contains("No"));
	}
	
	public void testWriterPropertyFail() throws Exception {
		Exception e = null;
		try {
			X4ODriver<TestObjectRoot> driver = TestDriver.getInstance();
			X4OWriter<TestObjectRoot> writer = driver.createWriter();
			writer.setProperty("test", "test");
		} catch (Exception catchE) {
			e = catchE;
		}
		assertNotNull("No exception",e);
		assertEquals("Wrong exception class",IllegalArgumentException.class, e.getClass());
		assertTrue("Wrong exception message",e.getMessage().contains("key"));
		assertTrue("Wrong exception message",e.getMessage().contains("No"));
	}
	
	public void testSchemaWriterPropertyFail() throws Exception {
		Exception e = null;
		try {
			X4ODriver<TestObjectRoot> driver = TestDriver.getInstance();
			X4OSchemaWriter schemaWriter = driver.createSchemaWriter();
			schemaWriter.setProperty("test", "test");
		} catch (Exception catchE) {
			e = catchE;
		}
		assertNotNull("No exception",e);
		assertEquals("Wrong exception class",IllegalArgumentException.class, e.getClass());
		assertTrue("Wrong exception message",e.getMessage().contains("key"));
		assertTrue("Wrong exception message",e.getMessage().contains("No"));
	}
}
