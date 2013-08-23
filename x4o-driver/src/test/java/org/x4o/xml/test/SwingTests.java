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
package org.x4o.xml.test;

import java.io.File;
import java.io.FileOutputStream;

import org.x4o.xml.io.X4OReader;
import org.x4o.xml.test.models.TestObjectRoot;


import junit.framework.TestCase;

/**
 * Tests a simple x4o xml language.
 * 
 * @author Willem Cazander
 * @version 1.0 Jul 24, 2006
 */
public class SwingTests extends TestCase {
	
	public void setUp() throws Exception {
		//X4OTesting.initLogging();
	}

	
	public void testSwing() throws Exception {
		TestDriver driver = TestDriver.getInstance();
		X4OReader<TestObjectRoot> reader = driver.createReader();
		File f = File.createTempFile("test-swing", ".xml");
		//f.deleteOnExit();
		//reader.setProperty(X4OLanguagePropertyKeys.DEBUG_OUTPUT_STREAM, new FileOutputStream(f));
		//reader.setProperty(X4OLanguagePropertyKeys.DEBUG_OUTPUT_ELD_PARSER, true);
		//reader.readResource("tests/test-swing.xml");
		//Thread.sleep(30000);
	}
}
