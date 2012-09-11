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

import org.x4o.xml.core.X4OPhase;
import org.x4o.xml.core.X4OPhaseHandler;
import org.x4o.xml.core.X4OPhaseManager;
import org.x4o.xml.test.TestParser;


import junit.framework.TestCase;

/**
 * Tests some code for testing x4o phases. 
 *  * 
 * @author Willem Cazander
 * @version 1.0 Jan 15, 2009
 */
public class X4OPhaseManagerTest extends TestCase {
	
	boolean phaseRunned = false;
	
	public void setUp() throws Exception {
		phaseRunned = false;
	}

	public void testPhaseOrder() throws Exception {
		TestParser parser = new TestParser();
		X4OPhaseManager manager = parser.getDriver().createX4OPhaseManager();
		
		assertEquals(false,manager.getPhases().isEmpty());
		List<X4OPhaseHandler> phases = manager.getOrderedPhases();
		
		int i = 0;
		for (X4OPhase phase:X4OPhase.PHASE_ORDER) {
			//if (X4OPhase.configOptionalPhase.equals(phase)) {
			//	continue;
			//}
			assertEquals(phase, phases.get(i).getX4OPhase());
			i++;
		}
	}
	
	public void testPhaseManager() throws Exception {
		TestParser parser = new TestParser();
		X4OPhaseManager manager = parser.getDriver().createX4OPhaseManager();
		
		Exception e = null;
		try {
			manager.addX4OPhaseHandler(null);
		} catch (NullPointerException ee) {
			e = ee;
		}
		assertEquals(true, e!=null );
	}
}
