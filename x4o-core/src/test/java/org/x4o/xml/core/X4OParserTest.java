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

package org.x4o.xml.core;

import org.x4o.xml.core.config.X4OLanguageConfiguration;
import org.x4o.xml.core.config.X4OLanguageLoaderException;

import junit.framework.TestCase;

/**
 * X4OParserTest runs parser checks.
 * 
 * @author Willem Cazander
 * @version 1.0 Aug 26, 2012
 */
public class X4OParserTest extends TestCase {
	
	
	public void testLanguageNull() throws Exception {
		String language = null;
		Exception e = null;
		try {
			new X4OParser(language);
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
			new X4OParser(language);
		} catch (Exception catchE) {
			e = catchE;
		}
		assertNotNull(e);
		assertEquals(IllegalArgumentException.class, e.getClass());
		assertTrue("Error message string is missing language",e.getMessage().contains("language"));
	}
	
	public void testLanguageVersionNonExcisting() throws Exception {
		Exception e = null;
		try {
			X4OParser p = new X4OParser("test","2.0");
			p.loadElementLanguageSupport();
		} catch (Exception catchE) {
			e = catchE;
		}
		assertNotNull(e);
		assertNotNull(e.getCause());
		assertNotNull(e.getCause().getCause());
		assertEquals(X4OParserSupportException.class, e.getClass());
		assertEquals(X4OPhaseException.class, e.getCause().getClass());
		assertEquals(X4OLanguageLoaderException.class, e.getCause().getCause().getClass());
		assertTrue("Error message string is missing language",e.getMessage().contains("language"));
		assertTrue("Error message string is missing test",e.getMessage().contains("test"));
		assertTrue("Error message string is missing modules",e.getMessage().contains("modules"));
		assertTrue("Error message string is missing 2.0",e.getMessage().contains("2.0"));
	}
	
	
	public void testDriverConfig() throws Exception {
		X4OLanguageConfiguration config = null;
		Exception e = null;
		try {
			new X4OParser(config);
		} catch (Exception catchE) {
			e = catchE;
		}
		assertNotNull(e);
		assertEquals(NullPointerException.class, e.getClass());
		assertTrue("Error message string is missing X4OLanguageConfiguration",e.getMessage().contains("X4OLanguageConfiguration"));
	}
	
	public void testDriverNull() throws Exception {
		X4ODriver driver = null;
		Exception e = null;
		try {
			new X4OParser(driver);
		} catch (Exception catchE) {
			e = catchE;
		}
		assertNotNull(e);
		assertEquals(NullPointerException.class, e.getClass());
		assertTrue("Error message string is missing X4ODriver",e.getMessage().contains("X4ODriver"));
	}
}
