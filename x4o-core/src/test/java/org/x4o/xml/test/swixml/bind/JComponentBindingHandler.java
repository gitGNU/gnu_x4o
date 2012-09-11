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

package	org.x4o.xml.test.swixml.bind;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import org.x4o.xml.element.AbstractElementBindingHandler;
import org.x4o.xml.element.Element;
import org.x4o.xml.element.ElementBindingHandlerException;

/**
 * 
 * 
 * @author Willem Cazander
 * @version 1.0 Aug 16, 2012
 */
public class JComponentBindingHandler extends AbstractElementBindingHandler {
	
	/**
	 * @see org.x4o.xml.element.ElementBindingHandler#getBindParentClass()
	 */
	public Class<?> getBindParentClass() {
		return JComponent.class;
		//return new Class[] {JPanel.class,JScrollPane.class,JSplitPane.class,JFrame.class,JInternalFrame.class};
	}

	/**
	 * @see org.x4o.xml.element.ElementBindingHandler#getBindChildClasses()
	 */
	public Class<?>[] getBindChildClasses() {
		return new Class[] {JComponent.class};
	}

	/**
	 * @see org.x4o.xml.element.ElementBindingHandler#doBind(java.lang.Object, java.lang.Object, org.x4o.xml.element.Element)
	 */
	public void doBind(Object parentObject, Object childObject,	Element childElement) throws ElementBindingHandlerException {
		JComponent child = (JComponent)childObject;
		
		String c = childElement.getAttributes().get("constraints");
		Object con = null;
		if ("BorderLayout.CENTER".equals(c)) {
			con = BorderLayout.CENTER;
		} else if ("BorderLayout.NORTH".equals(c)) {
			con = BorderLayout.NORTH;
		} else if ("BorderLayout.CENTER".equals(c)) {
			con = BorderLayout.CENTER;
		} else if ("BorderLayout.SOUTH".equals(c)) {
			con = BorderLayout.SOUTH;
		}
		
		if (parentObject instanceof JSplitPane) {
			JSplitPane pane = (JSplitPane)parentObject;
			if (pane.getLeftComponent() instanceof JButton) { // strange swing constructor for splitpane
				pane.setLeftComponent(child);
			} else if (pane.getRightComponent() instanceof JButton) {
				pane.setRightComponent(child);
			} else {
				throw new ElementBindingHandlerException("SplitPane is full.");
			}
		} else if (parentObject instanceof JScrollPane) {
			JScrollPane pane = (JScrollPane)parentObject;
			pane.setViewportView(child);
		} else if (parentObject instanceof JFrame) {
			JFrame frame = (JFrame)parentObject;
			if (con==null) {
				frame.getContentPane().add(child);
			} else {
				frame.getContentPane().add(child,con);
			}
		} else if (parentObject instanceof JInternalFrame) {
			JInternalFrame frame = (JInternalFrame)parentObject;
			if (con==null) {
				frame.getContentPane().add(child);
			} else {
				frame.getContentPane().add(child,con);
			}
		} else if (parentObject instanceof JPanel) {
			JPanel parent = (JPanel)parentObject;
			if (con==null) {
				parent.add(child);
			} else {
				parent.add(child,con);
			}			
		} else {
			// No bind but this destroys the xsd, change into multiple classes.
		}
	}
}