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
package org.x4o.xml.test.swixml;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;

/**
 * Accelerator2 test demo.
 * 
 * see swixml sample; http://www.swixml.org/samples/src/Accelerator.java
 * Added exitMethod and render boolean for unit testing.
 * 
 * @author Willem Cazander
 * @version 1.0 Aug 15, 2012
 */
public class Accelerator2 {
	
	protected static final String DESCRIPTOR = "tests/swixml/swixml-accelerator-2.0.xml";
	protected SwingEngine swix = new SwingEngine( this );
	
	public Accelerator2(boolean render) throws Exception {
		if (render) {
			swix.render( Accelerator2.DESCRIPTOR, SwiXmlDriver.LANGUAGE_VERSION_2 ).setVisible( true );
		}
	}
	
	@SuppressWarnings("serial")
	public Action newAction = new AbstractAction() {
		public void actionPerformed( ActionEvent e ) {
			JOptionPane.showMessageDialog( swix.getRootComponent(), "Sorry, not implemented yet." );
		}
	};
	
	@SuppressWarnings("serial")
	public Action aboutAction = new AbstractAction() {
		public void actionPerformed( ActionEvent e ) {
			JOptionPane.showMessageDialog( swix.getRootComponent(), "This is the Accelerator Example." );
		}
	};
	
	@SuppressWarnings("serial")
	public Action exitAction = new AbstractAction() {
		public void actionPerformed( ActionEvent e ) {
			System.exit(0);
		}
	};
	
	public static void main( String[] args ) {
		try {
			new Accelerator2(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
