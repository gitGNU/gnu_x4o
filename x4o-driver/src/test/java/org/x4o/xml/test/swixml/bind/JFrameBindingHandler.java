/*
 * Copyright (c) 2004-2014, Willem Cazander
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
import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuBar;

import org.x4o.xml.element.AbstractElementBindingHandler;
import org.x4o.xml.element.Element;
import org.x4o.xml.element.ElementBindingHandlerException;

/**
 * 
 * 
 * @author Willem Cazander
 * @version 1.0 Aug 16, 2012
 */
public class JFrameBindingHandler extends AbstractElementBindingHandler<JFrame> {
	
	/**
	 * @see org.x4o.xml.element.ElementBindingHandler#getBindParentClass()
	 */
	public Class<?> getBindParentClass() {
		return JFrame.class;
	}

	/**
	 * @see org.x4o.xml.element.ElementBindingHandler#getBindChildClasses()
	 */
	public Class<?>[] getBindChildClasses() {
		return new Class[] {JMenuBar.class,JComponent.class};
	}

	/**
	 * @see org.x4o.xml.element.ElementBindingHandler#bindChild(org.x4o.xml.element.Element, java.lang.Object, java.lang.Object)
	 */
	public void bindChild(Element childElement, JFrame parentObject, Object childObject) throws ElementBindingHandlerException {
		JFrame frame = (JFrame)parentObject;
		if (childObject instanceof JMenuBar) {
			JMenuBar child = (JMenuBar)childObject;
			frame.getRootPane().setJMenuBar(child);
		} else if (childObject instanceof JComponent) {
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
			
			if (con==null) {
				frame.getContentPane().add(child);
			} else {
				frame.getContentPane().add(child,con);
			}
		}
	}
	
	/**
	 * @see org.x4o.xml.element.AbstractElementBindingHandler#createChilderen(org.x4o.xml.element.Element, java.lang.Object)
	 */
	@Override
	public void createChilderen(Element parentElement,JFrame parent) throws ElementBindingHandlerException {
		createChild(parentElement, parent.getMenuBar());
		for (Component c:parent.getComponents()) {
			if (c instanceof JComponent) {
				createChild(parentElement, c);
			}
		}
	}
}