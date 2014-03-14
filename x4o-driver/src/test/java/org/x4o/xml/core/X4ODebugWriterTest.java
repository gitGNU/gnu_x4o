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
package org.x4o.xml.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import org.x4o.xml.X4ODriver;
import org.x4o.xml.io.DefaultX4OReader;
import org.x4o.xml.io.DefaultX4OWriter;
import org.x4o.xml.io.X4OReader;
import org.x4o.xml.io.X4OWriter;
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
	
	static public String readFile(File file) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file),Charset.forName("UTF-8")));
		try {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();
			while (line != null) {
				sb.append(line);
				sb.append('\n');
				line = br.readLine();
			}
			String out = sb.toString();
			//System.out.println(out);
			return out;
		} finally {
			br.close();
		}
	}
	
	public void testDebugOutputReader() throws Exception {
		File debugFile = createDebugFile();
		X4ODriver<TestObjectRoot> driver = TestDriver.getInstance();
		X4OReader<TestObjectRoot> reader = driver.createReader();
		reader.setProperty(DefaultX4OReader.DEBUG_OUTPUT_STREAM, new FileOutputStream(debugFile));
		reader.readResource("tests/attributes/test-bean.xml");
		
		assertTrue("Debug file does not exists.",debugFile.exists());
		String debug = readFile(debugFile);
		assertNotNull(debug);
		assertFalse("no debug content",debug.length()==0);
		assertTrue("debug content to small",debug.length()>20);
		
		//System.out.println("=================== Reader Output ======================");
		//System.out.println(debug);
		debugFile.delete();
	}
	
	public void testDebugOutputWriter() throws Exception {
		File debugFile = createDebugFile();
		File writeFile = createDebugFile();
		X4ODriver<TestObjectRoot> driver = TestDriver.getInstance();
		X4OReader<TestObjectRoot> reader = driver.createReader();
		X4OWriter<TestObjectRoot> writer = driver.createWriter();
		TestObjectRoot object = reader.readResource("tests/attributes/test-bean.xml");
		
		writer.setProperty(DefaultX4OWriter.DEBUG_OUTPUT_STREAM, new FileOutputStream(debugFile));
		writer.writeFile(object, writeFile);
		
		assertTrue("Debug file does not exists.",debugFile.exists());
		String debug = readFile(debugFile);
		assertNotNull(debug);
		assertFalse("no debug content",debug.length()==0);
		assertTrue("debug content to small",debug.length()>20);
		
		//System.out.println("=================== Writer Output ======================");
		//System.out.println(debug);
		debugFile.delete();
	}
}
