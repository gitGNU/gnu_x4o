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

package org.x4o.xml.test;

import org.x4o.xml.test.models.TestObjectChild;
import org.x4o.xml.test.models.TestObjectParent;
import org.x4o.xml.test.models.TestObjectRoot;


import junit.framework.TestCase;

/**
 * XIncludeTest
 * 
 * @author Willem Cazander
 * @version 1.0 Aug 31, 2012
 */
public class XIncludeTest extends TestCase {
	
	
	public void testXInclude() throws Exception {
		TestParser parser = new TestParser();
		parser.parseResource("tests/xinclude/include-base.xml");
		Object root = parser.getDriver().getElementLanguage().getRootElement().getElementObject();
		assertNotNull(root);
		TestObjectRoot parentRoot = (TestObjectRoot)root;
		assertEquals(1,parentRoot.testObjectParents.size());
		TestObjectParent parent = parentRoot.testObjectParents.get(0);
		TestObjectChild child = parent.testObjectChilds.get(0);
		assertEquals("include-child.xml",child.getName());
	}
}
