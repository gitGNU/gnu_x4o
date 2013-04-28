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
package org.x4o.xml.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.x4o.xml.X4ODriver;
import org.x4o.xml.io.X4OReader;
import org.x4o.xml.lang.X4OLanguagePropertyKeys;
import org.x4o.xml.test.TestDriver;
import org.x4o.xml.test.models.TestObjectRoot;

import junit.framework.TestCase;

/**
 * X4ODebugWriterTest runs parser with debug output.
 * 
 * @author Willem Cazander
 * @version 1.0 Aug 26, 2012
 */
public class X4ODebugWriterTest extends TestCase {
	

	private File createDebugFile() throws IOException {
		File debugFile = File.createTempFile("test-debug", ".xml");
		debugFile.deleteOnExit();
		return debugFile;
	}
	
	public void testDebugOutput() throws Exception {
		File debugFile = createDebugFile();
		X4ODriver<TestObjectRoot> driver = TestDriver.getInstance();
		X4OReader<TestObjectRoot> reader = driver.createReader();
		reader.setProperty(X4OLanguagePropertyKeys.DEBUG_OUTPUT_STREAM, new FileOutputStream(debugFile));
		reader.readResource("tests/attributes/test-bean.xml");
		
		assertTrue("Debug file does not exists.",debugFile.exists());
		debugFile.delete();
	}

	public void testDebugOutputFull() throws Exception {
		File debugFile = createDebugFile();
		X4ODriver<TestObjectRoot> driver = TestDriver.getInstance();
		X4OReader<TestObjectRoot> reader = driver.createReader();
		reader.setProperty(X4OLanguagePropertyKeys.DEBUG_OUTPUT_STREAM, new FileOutputStream(debugFile));
		//reader.setProperty(X4OLanguagePropertyKeys.DEBUG_OUTPUT_ELD_PARSER, true);
		reader.readResource("tests/attributes/test-bean.xml");
		
		assertTrue("Debug file does not exists.",debugFile.exists());
		debugFile.delete();
	}
}
