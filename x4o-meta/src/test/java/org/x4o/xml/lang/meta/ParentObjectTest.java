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
package org.x4o.xml.lang.meta;

import javax.swing.JLabel;

import org.x4o.xml.element.DefaultElement;
import org.x4o.xml.element.Element;
import org.x4o.xml.io.X4OReaderContext;
import org.x4o.xml.lang.X4OLanguageContext;
import org.x4o.xml.lang.X4OLanguagePropertyKeys;

import junit.framework.TestCase;

/**
 * Tests the parent object meta element.
 * 
 * @author Willem Cazander
 * @version 1.0 Apr 15, 2012
 */
public class ParentObjectTest extends TestCase {
	
	public void testParentElement() throws Exception {
		X4OLanguageContext context = null;
		MTestDriver driver = new MTestDriver();
		X4OReaderContext<?> reader = driver.createReaderContext();
		reader.setProperty(X4OLanguagePropertyKeys.PHASE_SKIP_RELEASE, true);
		try {
			context = reader.readResourceContext("junit/test-meta-parent-element.xml");
			assertEquals(1,context.getRootElement().getChilderen().size());
			Element childElement = context.getRootElement().getChilderen().get(0);
			JLabel test = (JLabel)childElement.getElementObject();
			assertEquals("parentTest",test.getText());
		} finally {
			reader.releaseContext(context);
		}
	}
	
	public void testParentObjectElement() throws Exception {
		DefaultElement ep = new DefaultElement();
		ParentObjectElement e = new ParentObjectElement();
		Object o;
		
		// test non parent
		o = e.getElementObject();
		assertNull(o);
		e.setElementObject("test");
		o = e.getElementObject();
		assertNull(o);
		
		// test parent
		e.setParent(ep);
		o = e.getElementObject();
		assertNull(o);
		e.setElementObject("test");
		o = e.getElementObject();
		assertEquals("test",o);
		o = ep.getElementObject();
		assertEquals("test",o);
		assertEquals(e.getElementObject(),ep.getElementObject());
	}

}
