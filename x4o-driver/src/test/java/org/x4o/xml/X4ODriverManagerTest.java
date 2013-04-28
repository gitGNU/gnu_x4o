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
package org.x4o.xml;

import java.util.List;

import org.x4o.xml.X4ODriverManager;

import junit.framework.TestCase;

/**
 * X4ODriverManager
 * 
 * @author Willem Cazander
 * @version 1.0 Jul 24, 2006
 */
public class X4ODriverManagerTest extends TestCase {

	public void testDefaultLanguageVersionSelect() throws Exception {
		String version = X4ODriverManager.getDefaultLanguageVersion(new String[]{"1.0","2.0","3.0"});
		assertNotNull(version);
		assertEquals("3.0",version);
	}
	
	public void testDefaultLanguageVersionEmpty() throws Exception {
		String version = X4ODriverManager.getDefaultLanguageVersion(new String[]{});
		assertNotNull(version);
		assertEquals(X4ODriver.DEFAULT_LANGUAGE_VERSION,version);
	}
	
	public void testDefaultLanguageVersionNull() throws Exception {
		String version = X4ODriverManager.getDefaultLanguageVersion(null);
		assertNotNull(version);
		assertEquals(X4ODriver.DEFAULT_LANGUAGE_VERSION,version);
	}
	
	public void testLanguageNull() throws Exception {
		String language = null;
		Exception e = null;
		try {
			X4ODriverManager.getX4ODriver(language);
		} catch (Exception catchE) {
			e = catchE;
		}
		assertNotNull(e);
		assertEquals(NullPointerException.class, e.getClass());
		assertTrue(e.getMessage().contains("language"));
	}

	public void testLanguageEmpty() throws Exception {
		String language = "";
		Exception e = null;
		try {
			X4ODriverManager.getX4ODriver(language);
		} catch (Exception catchE) {
			e = catchE;
		}
		assertNotNull(e);
		assertEquals(IllegalArgumentException.class, e.getClass());
		assertTrue("Error message string is missing language",e.getMessage().contains("language"));
	}
	
	public void testLanguageVersionNonExcisting() throws Exception {
		String language = "test";
		String version = "99.9";
		Throwable e = null;
		try {
			X4ODriver<?> driver = X4ODriverManager.getX4ODriver(language);
			driver.createLanguageContext(version);
		} catch (Throwable catchE) {
			e = catchE;
		}
		assertNotNull(e);
		/* TODO
		assertNotNull(e);
		assertNotNull(e.getCause());
		assertNotNull(e.getCause().getCause());
		assertEquals(X4OPhaseException.class, e.getCause().getClass());
		assertEquals(X4OLanguageLoaderException.class, e.getCause().getCause().getClass());
		assertTrue("Error message string is missing language",e.getMessage().contains("language"));
		assertTrue("Error message string is missing test",e.getMessage().contains("test"));
		assertTrue("Error message string is missing modules",e.getMessage().contains("modules"));
		assertTrue("Error message string is missing 2.0",e.getMessage().contains("2.0"));
		*/
	}
	
	public void testLanguageCount() throws Exception {
		List<String> languages = X4ODriverManager.getX4OLanguages();
		assertNotNull(languages);
		assertFalse(languages.isEmpty());
	}
	
	public void testLanguageNames() throws Exception {
		List<String> languages = X4ODriverManager.getX4OLanguages();
		assertNotNull(languages);
		assertTrue("cel language is missing",languages.contains("cel"));
		assertTrue("eld language is missing",languages.contains("eld"));
		assertTrue("test language is missing",languages.contains("test"));
	}

	public void testLanguagesLoopSpeed() throws Exception {
		long startTime = System.currentTimeMillis();
		for (int i=0;i<100;i++) {
			testLanguageCount();
		}
		long loopTime = System.currentTimeMillis() - startTime;
		assertEquals("Language list loop is slow;"+loopTime,true, loopTime<500);
	}
	
	public void testRegisterDriverNull() throws Exception {
		Exception e = null;
		try {
			X4ODriverManager.registerX4ODriver(null);
		} catch (Exception catchE) {
			e = catchE;
		}
		assertNotNull("No exception",e);
		assertEquals("Wrong exception class",NullPointerException.class, e.getClass());
		assertTrue("Wrong exception message",e.getMessage().contains("null"));
	}
	
	public void testRegisterDriverNameNull() throws Exception {
		Exception e = null;
		try {
			X4ODriverManager.registerX4ODriver(new X4ODriver<Object>() {
				@Override public String[] getLanguageVersions() {	return new String[]{"1.0"};	}
				@Override public String getLanguageName() {			return null;	}
				
			});
		} catch (Exception catchE) {
			e = catchE;
		}
		assertNotNull("No exception",e);
		assertEquals("Wrong exception class",NullPointerException.class, e.getClass());
		assertTrue("Wrong exception message",e.getMessage().contains("null"));
	}
	
	public void testRegisterDriverNameEmpty() throws Exception {
		Exception e = null;
		try {
			X4ODriverManager.registerX4ODriver(new X4ODriver<Object>() {
				@Override public String[] getLanguageVersions() {	return new String[]{"1.0"};	}
				@Override public String getLanguageName() {			return "";	}
				
			});
		} catch (Exception catchE) {
			e = catchE;
		}
		assertNotNull("No exception",e);
		assertEquals("Wrong exception class",IllegalArgumentException.class, e.getClass());
		assertTrue("Wrong exception message",e.getMessage().contains("empty"));
	}
	
	public void testRegisterDriverVersionNull() throws Exception {
		Exception e = null;
		try {
			X4ODriverManager.registerX4ODriver(new X4ODriver<Object>() {
				@Override public String[] getLanguageVersions() {	return null;		}
				@Override public String getLanguageName() {			return "junit-driver-test";	}
				
			});
		} catch (Exception catchE) {
			e = catchE;
		}
		assertNotNull("No exception",e);
		assertEquals("Wrong exception class",NullPointerException.class, e.getClass());
		assertTrue("Wrong exception message",e.getMessage().contains("null"));
	}
	
	public void testRegisterDriverVersionEmpty() throws Exception {
		Exception e = null;
		try {
			X4ODriverManager.registerX4ODriver(new X4ODriver<Object>() {
				@Override public String[] getLanguageVersions() {	return new String[]{};		}
				@Override public String getLanguageName() {			return "junit-driver-test";	}
				
			});
		} catch (Exception catchE) {
			e = catchE;
		}
		assertNotNull("No exception",e);
		assertEquals("Wrong exception class",IllegalArgumentException.class, e.getClass());
		assertTrue("Wrong exception message",e.getMessage().contains("empty"));
	}
	
	public void testDeregisterDriverNull() throws Exception {
		Exception e = null;
		try {
			X4ODriverManager.deregisterX4ODriver(null);
		} catch (Exception catchE) {
			e = catchE;
		}
		assertNotNull("No exception",e);
		assertEquals("Wrong exception class",NullPointerException.class, e.getClass());
		assertTrue("Wrong exception message",e.getMessage().contains("null"));
	}
	
	public void testDeregisterDriverNameNull() throws Exception {
		Exception e = null;
		try {
			X4ODriverManager.deregisterX4ODriver(new X4ODriver<Object>() {
				@Override public String[] getLanguageVersions() {	return new String[]{"1.0"};	}
				@Override public String getLanguageName() {			return null;	}
				
			});
		} catch (Exception catchE) {
			e = catchE;
		}
		assertNotNull("No exception",e);
		assertEquals("Wrong exception class",NullPointerException.class, e.getClass());
		assertTrue("Wrong exception message",e.getMessage().contains("null"));
	}
	
	public void testDeregisterDriverInstance() throws Exception {
		Exception e = null;
		try {
			X4ODriver<?> driver = new X4ODriver<Object>() {
				@Override public String[] getLanguageVersions() {	return new String[]{"1.0"};	}
				@Override public String getLanguageName() {			return "junit-driver-test";	}
			};
			X4ODriverManager.registerX4ODriver(driver);
			assertEquals(driver.hashCode(), X4ODriverManager.getX4ODriver("junit-driver-test").hashCode());
			X4ODriverManager.deregisterX4ODriver(driver);
			X4ODriverManager.getX4ODriver("junit-driver-test");
		} catch (Exception catchE) {
			e = catchE;
		}
		assertNotNull("No exception",e);
		assertEquals("Wrong exception class",IllegalArgumentException.class, e.getClass());
		assertTrue("Wrong exception message",e.getMessage().contains("junit-driver-test"));
	}
}
