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

import java.io.IOException;

import org.x4o.xml.X4ODriver;
import org.x4o.xml.eld.CelDriver;
import org.x4o.xml.io.sax.X4OEntityResolver;
import org.x4o.xml.lang.X4OLanguageContext;
import org.x4o.xml.lang.X4OLanguageProperty;
import org.x4o.xml.test.TestDriver;
import org.x4o.xml.test.models.TestObjectRoot;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import junit.framework.TestCase;

/**
 * X4OLanguageClassLoaderTest test classloader.
 * 
 * @author Willem Cazander
 * @version 1.0 Aug 27, 2012
 */
public class X4OEntityResolverTest extends TestCase {

	public void testElementLangugeNull() throws Exception {
		Exception e = null;
		try {
			new X4OEntityResolver(null);
		} catch (Exception catchE) {
			e = catchE;
		}
		assertNotNull(e);
		assertEquals(NullPointerException.class, e.getClass());
		assertTrue(e.getMessage().contains("null"));
	}
	
	public void testResolve() throws Exception {
		X4ODriver<?> driver = new CelDriver();
		X4OEntityResolver resolver = new X4OEntityResolver(driver.createLanguageContext());
		InputSource input = resolver.resolveEntity("","http://cel.x4o.org/xml/ns/cel-root-1.0.xsd");
		assertNotNull(input);
	}

	public void testResolveMissing() throws Exception {
		X4ODriver<TestObjectRoot> driver = new TestDriver();
		X4OEntityResolver resolver = new X4OEntityResolver(driver.createLanguageContext());
		Exception e = null;
		try {
			resolver.resolveEntity("","http://cel.x4o.org/xml/ns/cel-root-1.0.xsd-missing-resource");
		} catch (Exception catchE) {
			e = catchE;
		}
		assertNotNull(e);
		assertEquals(SAXException.class, e.getClass());
		assertTrue(e.getMessage().contains("missing-resource"));
	}
	
	public void testResolveProperty() throws Exception {
		X4ODriver<TestObjectRoot> driver = new TestDriver();
		X4OLanguageContext language = driver.createLanguageContext();
		language.setLanguageProperty(X4OLanguageProperty.READER_ENTITY_RESOLVER, new TestEntityResolver());
		X4OEntityResolver resolver = new X4OEntityResolver(language);
		Exception e = null;
		InputSource input = null;
		try {
			input = resolver.resolveEntity("","http://cel.x4o.org/xml/ns/cel-root-1.0.xsd");
		} catch (Exception catchE) {
			e = catchE;
		}
		assertNull(e);
		assertNotNull(input);
	}
	
	public void testResolvePropertyNull() throws Exception {
		X4ODriver<TestObjectRoot> driver = new TestDriver();
		X4OLanguageContext language = driver.createLanguageContext();
		language.setLanguageProperty(X4OLanguageProperty.READER_ENTITY_RESOLVER, new TestEntityResolver());
		X4OEntityResolver resolver = new X4OEntityResolver(language);
		Exception e = null;
		try {
			resolver.resolveEntity("","http://cel.x4o.org/xml/ns/cel-root-1.0.xsd-null");
		} catch (Exception catchE) {
			e = catchE;
		}
		assertNotNull(e);
		assertEquals(SAXException.class, e.getClass());
		assertTrue(e.getMessage().contains("null"));
	}
	
	public class TestEntityResolver implements EntityResolver {

		public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
			if (systemId.contains("null")) {
				return null;
			} else {
				return new InputSource();
			}
		}
		
	}
}
