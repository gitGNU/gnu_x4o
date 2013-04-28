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
package org.x4o.xml.conv;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

import org.x4o.xml.conv.text.ClassConverter;
import org.x4o.xml.conv.text.EnumConverter;
import org.x4o.xml.conv.text.URLConverter;
import org.x4o.xml.lang.phase.X4OPhaseType;

import junit.framework.TestCase;

/**
 * DefaultObjectConverterProviderTest test some basic converters.
 * 
 * @author Willem Cazander
 * @version 1.0 Jul 24, 2006
 */
public class DefaultObjectConverterProviderTest extends TestCase {
	
	Locale locale = Locale.getDefault();

	public void testConverterClone() throws Exception {
		DefaultObjectConverterProvider p = new DefaultObjectConverterProvider(true);
		for (ObjectConverter conv:p.getObjectConverters()) {
			assertNotNull(conv);
			ObjectConverter clone = conv.clone();
			assertNotNull(clone);
		}
	}
	
	public void testConverterBoolean() throws Exception {
		DefaultObjectConverterProvider p = new DefaultObjectConverterProvider(true);
		ObjectConverter conv = p.getObjectConverterForClass(Boolean.class);
		assertNotNull(conv);
		Object result = conv.convertTo("true", locale);
		assertNotNull(result);
		assertEquals("Result is not Boolean.class", Boolean.class,result.getClass());
		assertEquals("Result is not true", true,result);
		Object resultBack = conv.convertBack(result, locale);
		assertNotNull(resultBack);
		assertEquals("resultBack is not String.class", String.class,resultBack.getClass());
		assertEquals("resultBack is not true", "true",resultBack);
	}
	
	public void testConverterInteger() throws Exception {
		DefaultObjectConverterProvider p = new DefaultObjectConverterProvider(true);
		ObjectConverter conv = p.getObjectConverterForClass(Integer.class);
		assertNotNull(conv);
		Object result = conv.convertTo("123", locale);
		assertNotNull(result);
		assertEquals("Result is not Integer.class", Integer.class,result.getClass());
		assertEquals("Result is not 123", 123,result);
		Object resultBack = conv.convertBack(result, locale);
		assertNotNull(resultBack);
		assertEquals("resultBack is not String.class", String.class,resultBack.getClass());
		assertEquals("resultBack is not 123", "123",resultBack);
	}
	
	public void testConverterFloat() throws Exception {
		DefaultObjectConverterProvider p = new DefaultObjectConverterProvider(true);
		ObjectConverter conv = p.getObjectConverterForClass(Float.class);
		assertNotNull(conv);
		Object result = conv.convertTo("123.23", locale);
		assertNotNull(result);
		assertEquals("Result is not Float.class", Float.class,result.getClass());
		assertEquals("Result is not 123.23", 123.23F,result);
		Object resultBack = conv.convertBack(result, locale);
		assertNotNull(resultBack);
		assertEquals("resultBack is not String.class", String.class,resultBack.getClass());
		assertEquals("resultBack is not 123.23", "123.23",resultBack);
	}
	
	public void testConverterLong() throws Exception {
		DefaultObjectConverterProvider p = new DefaultObjectConverterProvider(true);
		ObjectConverter conv = p.getObjectConverterForClass(Long.class);
		assertNotNull(conv);
		Object result = conv.convertTo("12323", locale);
		assertNotNull(result);
		assertEquals("Result is not Long.class", Long.class,result.getClass());
		assertEquals("Result is not 12323", 12323L,result);
		Object resultBack = conv.convertBack(result, locale);
		assertNotNull(resultBack);
		assertEquals("resultBack is not String.class", String.class,resultBack.getClass());
		assertEquals("resultBack is not 12323", "12323",resultBack);
	}
	
	public void testConverterDouble() throws Exception {
		DefaultObjectConverterProvider p = new DefaultObjectConverterProvider(true);
		ObjectConverter conv = p.getObjectConverterForClass(Double.class);
		assertNotNull(conv);
		Object result = conv.convertTo("1232.3", locale);
		assertNotNull(result);
		assertEquals("Result is not Double.class", Double.class,result.getClass());
		assertEquals("Result is not 1232.3", 1232.3D,result);
		Object resultBack = conv.convertBack(result, locale);
		assertNotNull(resultBack);
		assertEquals("resultBack is not String.class", String.class,resultBack.getClass());
		assertEquals("resultBack is not 1232.3", "1232.3",resultBack);
	}
	
	public void testConverterUrl() throws Exception {
		DefaultObjectConverterProvider p = new DefaultObjectConverterProvider(true);
		ObjectConverter conv = p.getObjectConverterForClass(URL.class);
		assertNotNull(conv);
		Object result = conv.convertTo("http://www.x4o.org", locale);
		assertNotNull(result);
		assertEquals("Result is not Url.class", URL.class,result.getClass());
		assertEquals("Result is not http://www.x4o.org", new URL("http://www.x4o.org"),result);
		Object resultBack = conv.convertBack(result, locale);
		assertNotNull(resultBack);
		assertEquals("resultBack is not String.class", String.class,resultBack.getClass());
		assertEquals("resultBack is not http://www.x4o.org", "http://www.x4o.org",resultBack);
	}
	
	public void testConverterUrlException() throws Exception {
		URLConverter conv = new URLConverter();
		Exception e = null;
		try {
			conv.convertStringTo("error2::s/sdf//sd!@#$%#", locale);
		} catch (Exception catchE) {
			e = catchE;
		}
		assertNotNull(e);
		assertNotNull(e.getCause());
		assertEquals(ObjectConverterException.class, e.getClass());
		assertEquals(MalformedURLException.class, e.getCause().getClass());
		assertTrue("Error message string is missing error",e.getMessage().contains("error"));
	}
	
	
	public void testConverterEnum() throws Exception {
		EnumConverter convOrg = new EnumConverter();
		convOrg.setEnumClass(X4OPhaseType.class.getName());
		ObjectConverter conv = convOrg.clone();
		Object result = conv.convertTo("XML_READ", locale);
		assertNotNull(result);
		assertEquals("XML_READ", result.toString());
		Object resultBack = conv.convertBack(result, locale);
		assertEquals("XML_READ", resultBack.toString());
	}
	
	public void testConverterEnumError() throws Exception {
		EnumConverter convOrg = new EnumConverter();
		convOrg.setEnumClass(X4OPhaseType.class.getName());
		ObjectConverter conv = convOrg.clone();
		
		Exception e = null;
		try {
			 conv.convertTo("nonEnumError", locale);
		} catch (Exception catchE) {
			e = catchE;
		}
		assertNotNull(e);
		assertEquals(ObjectConverterException.class, e.getClass());
		assertTrue(e.getMessage().contains("EnumError"));
	}
	
	public void testConverterEnumNullError() throws Exception {
		EnumConverter conv = new EnumConverter();
		Exception e = null;
		try {
			 conv.convertTo("nonEnumError", locale);
		} catch (Exception catchE) {
			e = catchE;
		}
		assertNotNull(e);
		assertEquals(ObjectConverterException.class, e.getClass());
		assertTrue(e.getMessage().contains("enumClass"));
	}
	
	public void testConverterClass() throws Exception {
		ClassConverter classOrg = new ClassConverter();
		ObjectConverter conv = classOrg.clone();
		Object result = conv.convertTo("java.lang.Object", locale);
		assertNotNull(result);
		assertEquals(Object.class, result);
		Object resultBack = conv.convertBack(result, locale);
		assertEquals("java.lang.Object", resultBack.toString());
	}
	
	public void testConverterClassError() throws Exception {
		ClassConverter classOrg = new ClassConverter();
		ObjectConverter conv = classOrg.clone();
		
		Exception e = null;
		try {
			conv.convertTo("java.lang.ObjectError", locale);
		} catch (Exception catchE) {
			e = catchE;
		}
		assertNotNull(e);
		assertEquals(ObjectConverterException.class, e.getClass());
		assertTrue(e.getMessage().contains("ObjectError"));
	}
}
