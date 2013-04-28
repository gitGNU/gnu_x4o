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
package	org.x4o.xml.element;

import java.util.List;

/**
 * Handlers attributes for xml attributes of all elements processed.
 * 
 * @author Willem Cazander
 * @version 1.0 Aug 20, 2005
 */
public interface ElementAttributeHandler extends ElementConfigurator {
	
	/**
	 * Gets the attribute name this attribute handler handles.
	 * @return	Returns the attributes name of this attribute handler.
	 */
	String getAttributeName();
	
	/**
	 * Sets the attribute name this attribute handler handles.
	 * @param attributeName	The attribute to handle.
	 */
	void setAttributeName(String attributeName);
	
	/**
	 * Adds an NextAttribute.
	 * There next attributes will defines the order in which the ElementAttributeHandlers are executed.
	 * @param attribute	Add attribute which be will processed after this one.
	 */
	void addNextAttribute(String attribute);

	/**
	 * Removes an next attribute.
	 * @param attribute	Removes this next attribute.
	 */
	void removeNextAttribute(String attribute); 
	
	/**
	 * Get all next attributes.
	 * @return	Returns the list of all next attributes.
	 */
	List<String> getNextAttributes();
}
