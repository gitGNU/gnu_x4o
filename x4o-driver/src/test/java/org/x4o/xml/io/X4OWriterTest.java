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

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import org.x4o.xml.X4ODriver;
import org.x4o.xml.io.X4OReader;
import org.x4o.xml.test.TestDriver;
import org.x4o.xml.test.models.TestObjectRoot;
import org.x4o.xml.test.swixml.Accelerator3;
import org.x4o.xml.test.swixml.SwiXmlDriver;
import org.x4o.xml.test.swixml.SwingEngine;

import junit.framework.TestCase;

/**
 * X4ODebugWriterTest runs parser with debug output.
 * 
 * @author Willem Cazander
 * @version 1.0 Aug 26, 2012
 */
public class X4OWriterTest extends TestCase {
	

	private File createOutputFile() throws IOException {
		File outputFile = File.createTempFile("test-writer", ".xml");
		outputFile.deleteOnExit();
		return outputFile;
	}
	
	public void testWriterOutput() throws Exception {
		File outputFile = createOutputFile();
		X4ODriver<TestObjectRoot> driver = TestDriver.getInstance();
		X4OReader<TestObjectRoot> reader = driver.createReader();
		X4OWriter<TestObjectRoot> writer = driver.createWriter();
		
		TestObjectRoot root = reader.readResource("tests/attributes/test-bean.xml");
		writer.writeFile(root, outputFile);
		
		String text = new Scanner( outputFile ).useDelimiter("\\A").next();
		outputFile.delete();
		//System.out.println("Output: '\n"+text+"\n' end in "+outputFile.getAbsolutePath());
		
		assertTrue(text.startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"));
		assertTrue(text.contains("http://test.x4o.org/xml/ns/test-root"));
		assertTrue(text.contains("<test-lang:parent name=\"test-bean.xml\"/>"));
		assertTrue(text.contains("<test-lang:testBean"));
		assertTrue(text.contains("privateIntegerTypeField=\"123\""));
		assertTrue(text.contains("privateDoubleObjectField=\"123.45\""));
		assertTrue(text.contains("privateStringObjectField=\"x4o\""));
	}
	
	public void testWriterSwiXmlOutput() throws Exception {
		Accelerator3 ac3 = new Accelerator3(false);
		SwingEngine engine = new SwingEngine(ac3);
		
		File outputFile = createOutputFile();
		X4ODriver<Component> driver = SwiXmlDriver.getInstance();
		X4OReader<Component> reader = driver.createReader();
		X4OWriter<Component> writer = driver.createWriter();
		
		reader.addELBeanInstance(SwiXmlDriver.LANGUAGE_EL_SWING_ENGINE, engine);
		Component root = reader.readResource("tests/swixml/swixml-accelerator-3.0.xml");
		writer.writeFile(root, outputFile);
		
		assertTrue("Debug file does not exists.",outputFile.exists());
		
		//String text = new Scanner( outputFile ).useDelimiter("\\A").next();
		//System.out.println("Output: '\n"+text+"\n' end in "+outputFile.getAbsolutePath());
		
		outputFile.delete();
	}
}
