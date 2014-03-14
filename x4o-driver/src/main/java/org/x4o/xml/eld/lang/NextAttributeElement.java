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
package org.x4o.xml.eld.lang;

import org.x4o.xml.element.AbstractElement;
import org.x4o.xml.element.ElementNamespaceAttribute;
import org.x4o.xml.element.ElementException;

/**
 * NextAttributeElement.<br>
 * Parses the attributeName and adds it to the ElementClass
 * 
 * @author Willem Cazander
 * @version 1.0 Feb 19, 2007
 */
public class NextAttributeElement extends AbstractElement {

	@Override
	public void doElementRun() throws ElementException {
		String param = getAttributes().get("attributeName");
		if (param==null) {
			throw new ElementException("attributeName may not be null");
		}
		if (getParent()==null) {
			throw new ElementException("can't be a root tag");
		}
		if (getParent().getElementObject() instanceof ElementNamespaceAttribute) {
			((ElementNamespaceAttribute)getParent().getElementObject()).addNextAttribute(param);
		} else {
			throw new ElementException("Wrong parent class");
		}
	}
}
