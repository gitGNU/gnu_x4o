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

package org.x4o.xml.eld.lang;

import org.x4o.xml.element.AbstractElement;
import org.x4o.xml.element.ElementMetaBase;
import org.x4o.xml.element.ElementException;

/**
 * Fills all the ElementDescription which the description.
 * 
 * @author Willem Cazander
 * @version 1.0 Jan 13, 2009
 */
public class DescriptionElement extends AbstractElement {
		
	/**
	 * Starts the description element and validates that it is not root and parent is meta base.
	 * @throws ElementException	When parent element object is not meta base object.
	 * @see org.x4o.xml.element.AbstractElement#doElementStart()
	 */
	@Override
	public void doElementStart() throws ElementException {
		if (getParent()==null) {
			throw new ElementException("can't be a root tag");
		}
		if (getParent().getElementObject() instanceof ElementMetaBase == false) {
			throw new ElementException("Wrong parent class is not ElementDescription");
		}
	}

	/**
	 * The description elememt body characters are stored as element object.
	 * @param characters	The text of the description.
	 * @throws ElementException	When super has error.
	 * @see org.x4o.xml.element.AbstractElement#doCharacters(java.lang.String)
	 */
	@Override
	public void doCharacters(String characters) throws ElementException {
		super.doCharacters(characters);
		setElementObject(characters);
	}

	/**
	 * Ends the description element and sets the description on the parent.
	 * @throws ElementException	When parent element object is not meta base object.
	 * @see org.x4o.xml.element.AbstractElement#doElementEnd()
	 */
	@Override
	public void doElementEnd() throws ElementException {
		if (getElementObject()==null) {
			throw new ElementException("description is not set.");
		}
		if (getParent().getElementObject() instanceof ElementMetaBase) {
			((ElementMetaBase)getParent().getElementObject()).setDescription(getElementObject().toString());
		} else {
			throw new ElementException("Wrong parent class is not ElementClass");
		}
	}
}
