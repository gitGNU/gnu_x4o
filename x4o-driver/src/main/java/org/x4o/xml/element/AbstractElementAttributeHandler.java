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
package org.x4o.xml.element;

import java.util.ArrayList;
import java.util.List;

/**
 * An AbstractElementAttributeHandler.
 * 
 * @author Willem Cazander
 * @version 1.0 Aug 10, 2006
 */
public abstract class AbstractElementAttributeHandler extends AbstractElementConfigurator implements ElementAttributeHandler {
	
	private String attributeName = null;
	private List<String> nextAttributes = new ArrayList<String>(2);
	
	/**
	 * @see org.x4o.xml.element.ElementAttributeHandler#addNextAttribute(java.lang.String)
	 */
	public void addNextAttribute(String attribute) {
		if (attribute==null) {
			throw new NullPointerException("Can add null attribute for loading.");
		}
		nextAttributes.add(attribute);
	}

	/**
	 * @see org.x4o.xml.element.ElementAttributeHandler#removeNextAttribute(java.lang.String)
	 */	
	public void removeNextAttribute(String attribute) {
		if (attribute==null) {
			throw new NullPointerException("Can remove null attribute for loading.");
		}
		nextAttributes.remove(attribute);
	}

	/**
	 * @see org.x4o.xml.element.ElementAttributeHandler#getNextAttributes()
	 */
	public List<String> getNextAttributes() {
		return nextAttributes;
	}

	/**
	 * @see org.x4o.xml.element.ElementAttributeHandler#getAttributeName()
	 */
	public String getAttributeName() {
		return attributeName;
	}

	/**
	 * @see org.x4o.xml.element.ElementAttributeHandler#setAttributeName(java.lang.String)
	 */
	public void setAttributeName(String attributeName) {
		this.attributeName=attributeName;
	}
}
