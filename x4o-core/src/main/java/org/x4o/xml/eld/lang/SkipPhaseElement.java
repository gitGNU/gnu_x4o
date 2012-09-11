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
import org.x4o.xml.element.ElementClass;
import org.x4o.xml.element.ElementException;

/**
 * SkipPhaseElement add skip phases to elements.
 * 
 * @author Willem Cazander
 * @version 1.0 Aug 26, 2012
 */
public class SkipPhaseElement extends AbstractElement {

	/**
	 * @see org.x4o.xml.element.AbstractElement#doElementEnd()
	 */
	@Override
	public void doElementEnd() throws ElementException {
		String phase = getAttributes().get("name");
		if (phase==null) {
			throw new ElementException("'name' attribute is not set on: "+getElementClass().getTag());
		}
		if (getParent().getElementObject() instanceof ElementClass) {
				((ElementClass)getParent().getElementObject()).addSkipPhase(phase);
		} else {
			throw new ElementException("Wrong parent class is not ElementClassAttribute but: "+getParent().getElementObject());
		}
	}
}
