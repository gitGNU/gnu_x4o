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

package	org.x4o.xml.eld.lang;

import org.x4o.xml.element.AbstractElementBindingHandler;
import org.x4o.xml.element.Element;
import org.x4o.xml.element.ElementBindingHandlerException;
import org.x4o.xml.element.ElementClass;
import org.x4o.xml.element.ElementClassAttribute;
import org.x4o.xml.element.ElementConfigurator;

/**
 * This ElementBindingHandler adds the ElementClassAttributeConverter and the
 * ElementConfigurator to the ElementClass.
 * 
 * @author Willem Cazander
 * @version 1.0 Jan 31, 2007
 */
public class ElementClassBindingHandler extends AbstractElementBindingHandler {

	private final static Class<?>[] CLASSES_CHILD = new Class[] {
		ElementClassAttribute.class,
		ElementConfigurator.class
	};
	
	/**
	 * @see org.x4o.xml.element.ElementBindingHandler#getBindParentClass()
	 */
	public Class<?> getBindParentClass() {
		return ElementClass.class;
	}

	/**
	 * @see org.x4o.xml.element.ElementBindingHandler#getBindChildClasses()
	 */
	public Class<?>[] getBindChildClasses() {
		return CLASSES_CHILD;
	}

	/**
	 * @see org.x4o.xml.element.ElementBindingHandler#doBind(java.lang.Object, java.lang.Object, org.x4o.xml.element.Element)
	 */
	public void doBind(Object parentObject, Object childObject,Element childElement) throws ElementBindingHandlerException {
		ElementClass parent = (ElementClass)parentObject;
		if (childObject instanceof ElementClassAttribute) {
			parent.addElementClassAttribute((ElementClassAttribute)childObject);
		}
		if (childObject instanceof ElementConfigurator) { 
			parent.addElementConfigurators((ElementConfigurator)childObject);
		}
	}
}
