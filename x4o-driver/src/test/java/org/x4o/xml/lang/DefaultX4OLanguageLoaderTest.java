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

import java.io.InputStream;
import java.util.List;

import org.x4o.xml.X4ODriver;
import org.x4o.xml.lang.X4OLanguage;
import org.x4o.xml.lang.X4OLanguageLocal;
import org.x4o.xml.lang.DefaultX4OLanguageLoader.VersionedResources;
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

	X4ODriver<TestObjectRoot> driver;
	X4OLanguage language;
	DefaultX4OLanguageLoader loader;
	
	public void setUp() throws Exception {
		driver = TestDriver.getInstance();
		language = driver.createLanguage();
		loader = (DefaultX4OLanguageLoader)language.getLanguageConfiguration().getDefaultLanguageLoader().newInstance();
	}
	
	public void testLoadingDuplicate() throws Exception {
		Exception e = null;
		try {
			loader.loadLanguage((X4OLanguageLocal)language, "test", "1.0");
		} catch (Exception ee) {
			e = ee;
		}
		assertNotNull("no duplicate module exception",e);
		assertTrue("wrong module id.",e.getMessage().contains("test"));
	}
	
	public void testModulesSimple() throws Exception {
		InputStream in  = Thread.currentThread().getContextClassLoader().getResourceAsStream("tests/modules/test-modules-simple.xml");
		assertNotNull(in);
		List<VersionedResources> result = loader.loadLanguageModulesXml(in, "test-modules-simple.xml");
		assertNotNull(result);
		assertFalse(result.isEmpty());
		assertTrue("Simple test returned non-one result: "+result.size(),result.size()==1);
	}
	
	public void testModulesFull() throws Exception {
		InputStream in  = Thread.currentThread().getContextClassLoader().getResourceAsStream("tests/modules/test-modules-full.xml");
		assertNotNull(in);
		List<VersionedResources> result = loader.loadLanguageModulesXml(in, "test-modules-full.xml");
		assertNotNull(result);
		assertFalse(result.isEmpty());
		VersionedResources vr = result.get(0);
		assertTrue(vr.eldResources.size()>1);
		assertTrue(vr.moduleLoaders.size()>1);
		assertTrue(vr.elbResources.size()>1);
		assertTrue(vr.siblingLoaders.size()==1);
	}
	
	public void testModulesDuplicateLoaderNoError() throws Exception {
		InputStream in  = Thread.currentThread().getContextClassLoader().getResourceAsStream("tests/modules/test-modules-err-loader.xml");
		assertNotNull(in);
		List<VersionedResources> result = loader.loadLanguageModulesXml(in, "test-modules-err-loader.xml");
		assertNotNull(result);
		assertFalse(result.isEmpty());
	}
	
	public void testModulesDuplicateLoader() throws Exception {
		InputStream in  = Thread.currentThread().getContextClassLoader().getResourceAsStream("tests/modules/test-modules-err-loader.xml");
		assertNotNull(in);
		List<VersionedResources> result = loader.loadLanguageModulesXml(in, "test-modules-err-loader.xml");
		assertNotNull(result);
		assertFalse(result.isEmpty());
		
		Exception e=null;
		try {
			loader.validateModules(result);
		} catch (Exception ee) {
			e=ee;
		}
		assertNotNull(e);
		assertTrue("No 'Duplicate' found in message: "+e.getMessage(),e.getMessage().contains("Duplicate"));
		assertTrue("No 'module-loader' found in message: "+e.getMessage(),e.getMessage().contains("module-loader"));
	}
	
	public void testModulesDuplicateSiblingLoader() throws Exception {
		InputStream in  = Thread.currentThread().getContextClassLoader().getResourceAsStream("tests/modules/test-modules-err-sibling.xml");
		assertNotNull(in);
		List<VersionedResources> result = loader.loadLanguageModulesXml(in, "test-modules-err-sibling.xml");
		assertNotNull(result);
		assertFalse(result.isEmpty());
		
		Exception e=null;
		try {
			loader.validateModules(result);
		} catch (Exception ee) {
			e=ee;
		}
		assertNotNull(e);
		assertTrue("No 'Duplicate' found in message: "+e.getMessage(),e.getMessage().contains("Duplicate"));
		assertTrue("No 'sibling-loader' found in message: "+e.getMessage(),e.getMessage().contains("sibling-loader"));
	}
}
