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
package org.x4o.xml.test.element;

import java.io.StringWriter;

import org.x4o.xml.element.AbstractElement;
import org.x4o.xml.element.ElementException;
import org.x4o.xml.io.sax.XMLWriter;

/**
 * InlinePropertiesElement to test 
 * 
 * @author Willem Cazander
 * @version 1.0 Jan 23, 2012
 */
public class InlinePropertiesElement extends AbstractElement {

	StringWriter xmlString = null;
	
	/**
	 * @see org.x4o.xml.element.AbstractElement#doElementStart()
	 */
	@Override
	public void doElementStart() throws ElementException {
		StringWriter xmlString = new StringWriter();
		XMLWriter writer = new XMLWriter(xmlString);
		setElementObject(writer);
	}

	/**
	 * @see org.x4o.xml.element.AbstractElement#doElementEnd()
	 */
	@Override
	public void doElementEnd() throws ElementException {
	}

	/**
	 * @see org.x4o.xml.element.AbstractElement#getElementType()
	 */
	@Override
	public ElementType getElementType() {
		return ElementType.overrideSax;
	}
}
