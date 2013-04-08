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

package org.x4o.xml.lang;

import org.x4o.xml.lang.X4OLanguageProperty;
import org.x4o.xml.lang.X4OLanguagePropertyKeys;

import junit.framework.TestCase;

/**
 * X4OLanguagePropertyTest test static enum code.
 * 
 * @author Willem Cazander
 * @version 1.0 Aug 27, 2012
 */
public class X4OLanguagePropertyTest extends TestCase {

	public void testUriValue() throws Exception {
		new X4OLanguagePropertyKeys();
		X4OLanguageProperty prop = X4OLanguageProperty.valueByUri(X4OLanguagePropertyKeys.LANGUAGE_NAME);
		assertNotNull(prop);
		assertEquals(X4OLanguagePropertyKeys.LANGUAGE_NAME, prop.toUri());
	}
	
	public void testUriValueNullError() throws Exception {
		Exception e = null;
		try {
			X4OLanguageProperty.valueByUri(null);
		} catch (Exception catchE) {
			e = catchE;
		}
		assertNotNull(e);
		assertEquals(NullPointerException.class, e.getClass());
		assertTrue(e.getMessage().contains("uri"));
	}
	
	public void testUriValueEmptyError() throws Exception {
		Exception e = null;
		try {
			X4OLanguageProperty.valueByUri("");
		} catch (Exception catchE) {
			e = catchE;
		}
		assertNotNull(e);
		assertEquals(IllegalArgumentException.class, e.getClass());
		assertTrue(e.getMessage().contains("empty"));
	}
	
	public void testUriValuePrefixError() throws Exception {
		Exception e = null;
		try {
			X4OLanguageProperty.valueByUri("foobar");
		} catch (Exception catchE) {
			e = catchE;
		}
		assertNotNull(e);
		assertEquals(IllegalArgumentException.class, e.getClass());
		assertTrue(e.getMessage().contains("foobar"));
	}
	
	public void testUriValueMissingError() throws Exception {
		Exception e = null;
		try {
			X4OLanguageProperty.valueByUri("http://language.x4o.org/xml/properties/some-missing-property");
		} catch (Exception catchE) {
			e = catchE;
		}
		assertNotNull(e);
		assertEquals(IllegalArgumentException.class, e.getClass());
		assertTrue(e.getMessage().contains("some-missing-property"));
	}
	
	public void testValidValue() throws Exception {
		X4OLanguageProperty langName = X4OLanguageProperty.valueByUri(X4OLanguagePropertyKeys.LANGUAGE_NAME);
		X4OLanguageProperty langVersion = X4OLanguageProperty.valueByUri(X4OLanguagePropertyKeys.LANGUAGE_VERSION);
		assertEquals(false, langName.isValueValid("new-name"));
		assertEquals(false, langVersion.isValueValid("new-version"));
	}
	
	public void testValidValueNull() throws Exception {
		X4OLanguageProperty elMap = X4OLanguageProperty.valueByUri(X4OLanguagePropertyKeys.EL_BEAN_INSTANCE_MAP);
		assertEquals(true, elMap.isValueValid(null));
	}
	
	public void testValidValueObject() throws Exception {
		X4OLanguageProperty elMap = X4OLanguageProperty.valueByUri(X4OLanguagePropertyKeys.EL_BEAN_INSTANCE_MAP);
		assertEquals(false, elMap.isValueValid("string-object"));
	}
}
