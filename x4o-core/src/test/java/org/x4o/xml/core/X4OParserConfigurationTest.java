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
import org.x4o.xml.core.config.X4OLanguagePropertyKeys;
import org.x4o.xml.element.Element;
import org.x4o.xml.element.ElementClass;
import org.x4o.xml.element.ElementClassAttribute;
import org.x4o.xml.test.TestParser;

import junit.framework.TestCase;

/**
 * Tests some resulting options from the language config.
 * 
 * @author Willem Cazander
 * @version 1.0 Jan 20, 2012
 */
public class X4OParserConfigurationTest extends TestCase {
	
	TestParser parser;
	X4OLanguageConfiguration config;
	
	public void setUp() throws Exception {
		parser = new TestParser();
		parser.setProperty(X4OLanguagePropertyKeys.PHASE_SKIP_RELEASE, true);
		try {
			parser.parseResource("tests/namespace/uri-simple.xml");
			config = parser.getElementLanguage().getLanguageConfiguration();
		} finally {
			parser.doReleasePhaseManual();
		}
	}
	
	public void testParserConfigurationLanguage() {
		assertEquals("test",config.getLanguage());
		assertEquals(X4OLanguageConfiguration.DEFAULT_LANG_MODULES_FILE,config.getLanguageResourceModulesFileName());
		assertEquals(X4OLanguageConfiguration.DEFAULT_LANG_PATH_PREFIX,config.getLanguageResourcePathPrefix());
	}
	
	public void testParserConfigurationElement() {
		assertNotNull(config.getDefaultElement());
		assertTrue("No Element Inteface", Element.class.isAssignableFrom(config.getDefaultElement()));
		
		assertNotNull(config.getDefaultElementClass());
		assertTrue("No ElementClass Inteface", ElementClass.class.isAssignableFrom(config.getDefaultElementClass()));
		
		assertNotNull(config.getDefaultElementClassAttribute());
		assertTrue("No ElementClass Inteface", ElementClassAttribute.class.isAssignableFrom(config.getDefaultElementClassAttribute()));
	}
}
