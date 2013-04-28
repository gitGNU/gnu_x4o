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

import java.util.List;

import org.x4o.xml.X4ODriver;
import org.x4o.xml.io.X4OReader;
import org.x4o.xml.test.TestDriver;
import org.x4o.xml.test.models.TestBean;
import org.x4o.xml.test.models.TestObjectChild;
import org.x4o.xml.test.models.TestObjectRoot;

import junit.framework.TestCase;

/**
 * Tests a simple x4o xml language.
 * 
 * @author Willem Cazander
 * @version 1.0 Jul 24, 2006
 */
public class AttributeBeanTest extends TestCase {
	
	public void testBeanBody() throws Exception {
		X4ODriver<TestObjectRoot> driver = TestDriver.getInstance();
		X4OReader<TestObjectRoot> reader = driver.createReader();
		
		TestObjectRoot root = reader.readResource("tests/attributes/test-body.xml");
		assertNotNull(root);
		List<TestObjectChild> childs = root.getTestObjectChilds();
		assertEquals(2,childs.size());
		TestObjectChild child0 = childs.get(0);
		TestObjectChild child1 = childs.get(1);
		assertEquals("attr-name",child0.getName());
		assertEquals("body-name",child1.getName());
	}
	
	public void testBeanProperties() throws Exception {
		X4ODriver<TestObjectRoot> driver = TestDriver.getInstance();
		X4OReader<TestObjectRoot> reader = driver.createReader();
		
		TestObjectRoot root = reader.readResource("tests/attributes/test-bean.xml");
		assertNotNull(root);
		assertNotNull(root.getTestBeans());
		assertEquals(2, root.getTestBeans().size());
		TestBean b = root.getTestBeans().get(0);
		
		assertEquals(123		,0+	b.getPrivateIntegerTypeField());
		assertEquals(123		,0+	b.getPrivateIntegerObjectField());
		
		assertEquals(123l		,0+	b.getPrivateLongTypeField());
		assertEquals(123l		,0+	b.getPrivateLongObjectField());
		
		assertEquals(123.45d	,0+	b.getPrivateDoubleTypeField());
		assertEquals(123.45d	,0+	b.getPrivateDoubleObjectField());
		
		assertEquals(123.45f	,0+	b.getPrivateFloatTypeField());
		assertEquals(123.45f	,0+	b.getPrivateFloatObjectField());
		
		assertEquals(67			,0+	b.getPrivateByteTypeField());
		assertEquals(67			,0+	b.getPrivateByteObjectField());
		
		assertEquals(true,		b.isPrivateBooleanTypeField());
		assertEquals(new Boolean(true),	b.getPrivateBooleanObjectField());
		
		assertEquals('W'		,0+ b.getPrivateCharTypeField());
		assertEquals('C'		,0+	b.getPrivateCharObjectField());
		
		assertEquals("x4o"		,b.getPrivateStringObjectField());
		//TODO: add again: assertEquals(true		,null!=b.getPrivateDateObjectField());
	}
}
