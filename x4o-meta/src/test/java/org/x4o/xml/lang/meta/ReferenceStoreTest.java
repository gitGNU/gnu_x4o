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

import java.util.Date;

import org.x4o.xml.io.X4OReaderContext;
import org.x4o.xml.lang.X4OLanguageContext;
import org.x4o.xml.lang.phase.X4OPhase;

import junit.framework.TestCase;

/**
 * Tests a simple x4o meta xml language.
 * 
 * @author Willem Cazander
 * @version 1.0 Jul 24, 2006
 */
public class ReferenceStoreTest extends TestCase {
	
	public void testMetaGeneric() throws Exception {
		X4OLanguageContext context = null;
		MTestDriver driver = new MTestDriver();
		X4OReaderContext<?> reader = driver.createReaderContext();
		reader.addPhaseSkip(X4OPhase.READ_RELEASE);
		try {
			context = reader.readResourceContext("junit/test-meta-generic.xml");
			assertEquals(Date.class.getName(),context.getRootElement().getChilderen().get(0).getElementObject().getClass().getName());
		} finally {
			reader.releaseContext(context);
		}
	}
	
	public void testLoadClass() throws Exception {
		X4OLanguageContext context = null;
		MTestDriver driver = new MTestDriver();
		X4OReaderContext<?> reader = driver.createReaderContext();
		reader.addPhaseSkip(X4OPhase.READ_RELEASE);
		try {
			context = reader.readResourceContext("junit/test-meta-reference.xml");
			assertEquals(Date.class.getName(),context.getRootElement().getChilderen().get(0).getElementObject().getClass().getName());
		} finally {
			reader.releaseContext(context);
		}
	}
	
	public void testStoreRef() throws Exception {
		X4OLanguageContext context = null;
		MTestDriver driver = new MTestDriver();
		X4OReaderContext<?> reader = driver.createReaderContext();
		reader.addPhaseSkip(X4OPhase.READ_RELEASE);
		try {
			context = reader.readResourceContext("junit/test-meta-reference.xml");
			assertEquals(Date.class.getName(),context.getRootElement().getChilderen().get(0).getElementObject().getClass().getName());
			assertEquals(Date.class.getName(),context.getRootElement().getChilderen().get(1).getElementObject().getClass().getName());
			assertEquals(Date.class.getName(),context.getRootElement().getChilderen().get(2).getElementObject().getClass().getName());
			assertEquals(Date.class.getName(),context.getRootElement().getChilderen().get(3).getElementObject().getClass().getName());
		} finally {
			reader.releaseContext(context);
		}
	}

}
