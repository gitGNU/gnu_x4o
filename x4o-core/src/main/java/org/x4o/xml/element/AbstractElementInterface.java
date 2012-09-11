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

package	org.x4o.xml.element;

import java.util.ArrayList;
import java.util.List;

/**
 * An AbstractElement.
 * 
 * @author Willem Cazander
 * @version 1.0 Apr 15, 2008
 */
abstract public class AbstractElementInterface extends AbstractElementClassBase implements ElementInterface {

	private Class<?> interfaceClass = null;
	private List<ElementBindingHandler> elementBindingHandlers = null;
	
	public AbstractElementInterface() {
		elementBindingHandlers = new ArrayList<ElementBindingHandler>(4);
	}
	
	/**
	 * @see org.x4o.xml.element.ElementInterface#addElementBindingHandler(org.x4o.xml.element.ElementBindingHandler)
	 */
	public void addElementBindingHandler(ElementBindingHandler elementBindingHandler) {
		elementBindingHandlers.add(elementBindingHandler);
	}

	/**
	 * @see org.x4o.xml.element.ElementInterface#getElementBindingHandlers()
	 */
	public List<ElementBindingHandler> getElementBindingHandlers() {
		return elementBindingHandlers;
	}

	/**
	 * @see org.x4o.xml.element.ElementInterface#getInterfaceClass()
	 */
	public Class<?> getInterfaceClass() {
		return interfaceClass;
	}

	/**
	 * @see org.x4o.xml.element.ElementInterface#setInterfaceClass(java.lang.Class)
	 */
	public void setInterfaceClass(Class<?> interfaceClass) {
		this.interfaceClass=interfaceClass;
	}
}
