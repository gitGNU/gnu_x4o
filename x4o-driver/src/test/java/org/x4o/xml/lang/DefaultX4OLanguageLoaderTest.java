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
package org.x4o.xml.lang;

import org.x4o.xml.X4ODriver;
import org.x4o.xml.lang.X4OLanguage;
import org.x4o.xml.lang.X4OLanguageLoader;
import org.x4o.xml.lang.X4OLanguageLocal;
import org.x4o.xml.test.TestDriver;
import org.x4o.xml.test.models.TestObjectRoot;

import junit.framework.TestCase;

/**
 * Tests a simple x4o language loader
 * 
 * @author Willem Cazander
 * @version 1.0 Jan 20, 2012
 */
public class DefaultX4OLanguageLoaderTest extends TestCase {

	X4OLanguage language;
	X4OLanguageLoader loader;
	
	public void setUp() throws Exception {
		X4ODriver<TestObjectRoot> driver = TestDriver.getInstance();
		//X4OReader<TestObjectRoot> reader = driver.createReader();
		//reader.readResource("tests/namespace/uri-simple.xml");
		language = driver.createLanguage();
		loader = (X4OLanguageLoader)language.getLanguageConfiguration().getDefaultLanguageLoader().newInstance();
		
	}

	public void testLanguageURINameSpaceTest() throws Exception {
		loader.loadLanguage((X4OLanguageLocal)language, "test", "1.0");
	}
	
	/*
	public void testLanguageURINameSpaceTest() throws Exception {
		Map<String,String> languageMap = loader.loadLanguageModules(parser.getElementLanguage(), "test");
		assertTrue("No uri for test.eld", languageMap.containsKey("eld.http://test.x4o.org/eld/test.eld"));
		assertEquals("test.eld", languageMap.get("eld.http://test.x4o.org/eld/test.eld"));
	}
	
	public void testLanguageURINameSpaceX4O() throws Exception {
		Map<String,String> languageMap = loader.loadLanguageModules(parser.getElementLanguage(), "x4o");
		assertTrue("No uri for x4o-lang.eld", languageMap.containsKey("eld.http://eld.x4o.org/eld/x4o-lang.eld"));
		assertEquals("x4o-lang.eld", languageMap.get("eld.http://eld.x4o.org/eld/x4o-lang.eld"));
	}
	
	public void testLanguageURINameSpaceELD() throws Exception {
		Map<String,String> languageMap = loader.loadLanguageModules(parser.getElementLanguage(), "eld");
		assertTrue("No uri for eld-lang.eld", languageMap.containsKey("eld.http://eld.x4o.org/eld/eld-lang.eld"));
		assertEquals("eld-lang.eld", languageMap.get("eld.http://eld.x4o.org/eld/eld-lang.eld"));
	}
	*/
}
