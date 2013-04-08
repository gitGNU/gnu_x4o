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

package	org.x4o.xml.test.swixml;

import javax.swing.Action;
import javax.swing.JMenuItem;

import org.x4o.xml.element.AbstractElementConfigurator;
import org.x4o.xml.element.Element;
import org.x4o.xml.element.ElementConfiguratorException;

/**
 * SwiXmlActionConfigurator sets the Action object. 
 * 
 * @author Willem Cazander
 * @version 1.0 Aug 16, 2012
 */
public class SwiXmlActionConfigurator extends AbstractElementConfigurator {

	public void doConfigElement(Element element) throws ElementConfiguratorException {
		String actionName = element.getAttributes().get("Action");
		if (actionName==null) {
			return;
		}
		SwingEngine se = SwiXmlDriver.getSwingEngine(element.getLanguageContext());
		Action action = se.getUIActionByName(actionName);
		Object object = element.getElementObject();
		if (object instanceof JMenuItem) {
			((JMenuItem)object).setAction(action);
		}
		// etc
	}	
}