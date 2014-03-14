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
import java.io.IOException;
import java.util.Scanner;

import org.x4o.xml.X4ODriver;
import org.x4o.xml.io.X4OReader;
import org.x4o.xml.test.TestDriver;
import org.x4o.xml.test.models.TestObjectRoot;

import junit.framework.TestCase;

/**
 * X4OWriterAttributeTest tests xml write attribute write order.
 * 
 * @author Willem Cazander
 * @version 1.0 May 19, 2013
 */
public class X4OWriterAttributeTest extends TestCase {
	

	private File createOutputFile() throws IOException {
		File outputFile = File.createTempFile("test-writer-attr", ".xml");
		outputFile.deleteOnExit();
		return outputFile;
	}
	
	public void testWriteAttrNatural() throws Exception {
		File outputFile = createOutputFile();
		X4ODriver<TestObjectRoot> driver = TestDriver.getInstance();
		X4OReader<TestObjectRoot> reader = driver.createReader();
		X4OWriter<TestObjectRoot> writer = driver.createWriter();
		
		TestObjectRoot root = reader.readResource("tests/writer/test-attribute-order.xml");
		writer.writeFile(root, outputFile);
		String text = new Scanner( outputFile ).useDelimiter("\\A").next();
		outputFile.delete();
		
		assertTrue(text.startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"));
		assertTrue(text.contains("TestAttributeOrderChildNatural"+
				" aaaName=\"NAME_1\" aabName=\"NAME_2\" aacName=\"NAME_3\""+
				" abaName=\"NAME_4\" abbName=\"NAME_5\" abcName=\"NAME_6\""+
				" caaName=\"NAME_7\" cabName=\"NAME_8\" cacName=\"NAME_9\""));
	}
	
	public void testWriteAttrOrdered() throws Exception {
		File outputFile = createOutputFile();
		X4ODriver<TestObjectRoot> driver = TestDriver.getInstance();
		X4OReader<TestObjectRoot> reader = driver.createReader();
		X4OWriter<TestObjectRoot> writer = driver.createWriter();
		
		TestObjectRoot root = reader.readResource("tests/writer/test-attribute-order.xml");
		writer.writeFile(root, outputFile);
		String text = new Scanner( outputFile ).useDelimiter("\\A").next();
		outputFile.delete();
		
		assertTrue(text.startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"));
		assertTrue(text.contains("custom-ordered-child"+
				" cacName=\"NAME_9\" cabName=\"NAME_8\" caaName=\"NAME_7\""+
				" abcName=\"NAME_6\" abbName=\"NAME_5\" abaName=\"NAME_4\""+
				" aaaName=\"NAME_1\" aabName=\"NAME_2\" aacName=\"NAME_3\""));
	}
}
