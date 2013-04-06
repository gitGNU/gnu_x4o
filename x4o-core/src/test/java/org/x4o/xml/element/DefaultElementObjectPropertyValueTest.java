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

package org.x4o.xml.element;

import java.io.InputStream;
import java.util.logging.LogManager;


import org.x4o.xml.element.DefaultElementObjectPropertyValue;
import org.x4o.xml.element.ElementObjectPropertyValueException;
import org.x4o.xml.test.models.TestObjectChild;

import junit.framework.TestCase;

/**
 * Tests a simple x4o xml language.
 * 
 * @author Willem Cazander
 * @version 1.0 Jul 24, 2006
 */
public class DefaultElementObjectPropertyValueTest extends TestCase {
	
	DefaultElementObjectPropertyValue helper = new DefaultElementObjectPropertyValue();
	
	public void setUp() throws Exception {
		// enable all logs
		InputStream loggingProperties = this.getClass().getResourceAsStream("/META-INF/logging.properties");
		LogManager.getLogManager().readConfiguration( loggingProperties );
		loggingProperties.close();
	}
	
	public void testNullValue() throws Exception {

		TestObjectChild obj = new TestObjectChild();
		obj.setName("test");
		
		assertEquals("test", obj.getName()); // test org value
		assertEquals("test", helper.getProperty(obj, "name")); // test null get value
		
		helper.setProperty(obj, "name", null);
		
		assertEquals(null, obj.getName()); // test null value
		assertEquals(null, helper.getProperty(obj, "name")); // test null get value
	}
	
	public void testIntegerValue() throws Exception {
		TestObjectChild obj = new TestObjectChild();
		helper.setProperty(obj, "price", 666);
		
		assertEquals(666,helper.getProperty(obj, "price"));
	}
	
	public void testException() throws Exception {
		TestObjectChild obj = new TestObjectChild();
		helper.setProperty(obj, "price", 666);
		
		boolean error = false;
		try {
			helper.getProperty(obj, "price2");
		} catch (ElementObjectPropertyValueException not) {
			error = true;
		}
		assertEquals(true,error);
	}
}
