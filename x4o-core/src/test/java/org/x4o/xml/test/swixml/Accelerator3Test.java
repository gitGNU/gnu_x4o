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

package org.x4o.xml.test.swixml;

import java.awt.Component;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.x4o.xml.io.X4OReader;

import junit.framework.TestCase;

/**
 * Accelerator3Test test xml parsing
 * 
 * @author Willem Cazander
 * @version 1.0 Aug 31, 2012
 */
public class Accelerator3Test extends TestCase {
	
	public void testSwingMenuAbout() throws Exception {
		Accelerator3 ac3 = new Accelerator3(false);
		SwingEngine engine = new SwingEngine(ac3);
		SwiXmlDriver driver = SwiXmlDriver.getInstance();
		X4OReader<Component> reader = driver.createReader(SwiXmlDriver.LANGUAGE_VERSION_3);
		reader.addELBeanInstance(SwiXmlDriver.LANGUAGE_EL_SWING_ENGINE, engine);
		Component root = reader.readResource(Accelerator3.DESCRIPTOR);
		assertNotNull(root);
		JFrame frame = (JFrame)root;
		assertTrue(frame.getJMenuBar().getMenuCount()>0);
		JMenu helpMenu = frame.getJMenuBar().getMenu(1);
		assertEquals("Help",helpMenu.getText());
		assertTrue(helpMenu.getMenuComponentCount()>0);
		JMenuItem about = (JMenuItem)helpMenu.getMenuComponent(0);
		assertEquals("mi_about", about.getName());
	}
}
