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
package org.x4o.xml.core;

import org.x4o.xml.io.DefaultX4OReader;
import org.x4o.xml.io.X4OReaderSession;
import org.x4o.xml.lang.X4OLanguageSession;
import org.x4o.xml.lang.phase.X4OPhase;
import org.x4o.xml.test.TestDriver;
import org.x4o.xml.test.models.TestObjectRoot;

import junit.framework.TestCase;

/**
 * Tests emptry uri namespace laoding.
 * 
 * @author Willem Cazander
 * @version 1.0 May 1, 2011
 */
public class NamespaceUriTest extends TestCase {
	
	public void testSimpleUri() throws Exception {
		X4OLanguageSession context = null;
		TestDriver driver = TestDriver.getInstance();
		X4OReaderSession<TestObjectRoot> reader = driver.createReaderSession();
		reader.addPhaseSkip(X4OPhase.READ_RELEASE);
		try {
			context = reader.readResourceSession("tests/namespace/uri-simple.xml");
			assertEquals(true,context.getRootElement().getChilderen().size()==1);
		} finally {
			reader.releaseSession(context);
		}
	}
	
	public void testEmptyUri() throws Exception {
		X4OLanguageSession context = null;
		TestDriver driver = TestDriver.getInstance();
		X4OReaderSession<TestObjectRoot> reader = driver.createReaderSession();
		reader.addPhaseSkip(X4OPhase.READ_RELEASE);
		reader.setProperty(DefaultX4OReader.DOC_EMPTY_NAMESPACE_URI, "http://test.x4o.org/xml/ns/test-lang");
		try {
			context = reader.readResourceSession("tests/namespace/uri-empty.xml");
			assertEquals(true,context.getRootElement().getChilderen().size()==1);
		} finally {
			reader.releaseSession(context);
		}
	}
	
	public void testSchemaUri() throws Exception {
		X4OLanguageSession context = null;
		TestDriver driver = TestDriver.getInstance();
		X4OReaderSession<TestObjectRoot> reader = driver.createReaderSession();
		reader.addPhaseSkip(X4OPhase.READ_RELEASE);
		try {
			context = reader.readResourceSession("tests/namespace/uri-schema.xml");
			assertEquals(true,context.getRootElement().getChilderen().size()==1);
		} finally {
			reader.releaseSession(context);
		}
	}
}
