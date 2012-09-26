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

import java.lang.reflect.Method;

import org.x4o.xml.element.AbstractElementBindingHandler;
import org.x4o.xml.element.Element;
import org.x4o.xml.element.ElementBindingHandlerException;

/**
 * Binds to objects together with a method found by reflection.
 *
 * @author Willem Cazander
 * @version 1.0 Nov 21, 2007
 */
public class ElementRefectionBindingHandler extends AbstractElementBindingHandler {

	private Class<?> parentClass = null;
	private Class<?> childClass = null;
	private String method = null;
	
	/**
	 * @see org.x4o.xml.element.ElementBindingHandler#getBindParentClass()
	 */
	public Class<?> getBindParentClass() {
		return parentClass;
	}

	/**
	 * @see org.x4o.xml.element.ElementBindingHandler#getBindChildClasses()
	 */
	public Class<?>[] getBindChildClasses() {
		return new Class[] {childClass};
	}

	/**
	 * @see org.x4o.xml.element.ElementBindingHandler#doBind(java.lang.Object, java.lang.Object, org.x4o.xml.element.Element)
	 */
	public void doBind(Object parentObject, Object childObject,	Element childElement) throws ElementBindingHandlerException {

		if (parentClass==null | childClass==null | method==null) {
			throw new IllegalStateException("Missing property: parentClass="+parentClass+" childClass="+childClass+" method="+method+".");
		}
		Method[] ms = parentObject.getClass().getMethods();
		for (Method m:ms) {
			Class<?>[] types = m.getParameterTypes();
			if (types.length == 0) {
				continue;
			}
			if (types.length > 1) {
				continue;
			}
			if (types[0].isAssignableFrom(childClass)) {
				try {
					m.invoke(parentObject, childObject);
				} catch (Exception e) {
					throw new ElementBindingHandlerException("Error invoke binding method of: "+getId()+" error: "+e.getMessage(),e);
				}
				return;
			}
		}
	}

	/**
	 * @return the parentClass
	 */
	public Class<?> getParentClass() {
		return parentClass;
	}

	/**
	 * @param parentClass the parentClass to set
	 */
	public void setParentClass(Class<?> parentClass) {
		this.parentClass = parentClass;
	}

	/**
	 * @return the childClass
	 */
	public Class<?> getChildClass() {
		return childClass;
	}

	/**
	 * @param childClass the childClass to set
	 */
	public void setChildClass(Class<?> childClass) {
		this.childClass = childClass;
	}

	/**
	 * @return the method
	 */
	public String getMethod() {
		return method;
	}

	/**
	 * @param method the method to set
	 */
	public void setMethod(String method) {
		this.method = method;
	}
}
