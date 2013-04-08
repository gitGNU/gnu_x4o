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
import java.util.Collection;
import java.util.List;

import org.x4o.xml.element.AbstractElementBindingHandler;
import org.x4o.xml.element.Element;
import org.x4o.xml.element.ElementBindingHandlerException;

/**
 * Binds to objects together with a method found by reflection.
 *
 * @author Willem Cazander
 * @version 1.0 Nov 21, 2007
 */
public class ElementRefectionBindingHandler extends AbstractElementBindingHandler<Object> {

	private Class<?> parentClass = null;
	private Class<?> childClass = null;
	private String addMethod = null;
	private String getMethod = null;
	private String skipChilderenClassRegex = null;
	
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
	 * @see org.x4o.xml.element.ElementBindingHandler#bindChild(org.x4o.xml.element.Element,java.lang.Object, java.lang.Object)
	 */
	public void bindChild(Element childElement, Object parentObject, Object childObject) throws ElementBindingHandlerException {

		if (parentClass==null | childClass==null | addMethod==null) {
			throw new IllegalStateException("Missing property: parentClass="+parentClass+" childClass="+childClass+" addMethod="+addMethod+".");
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
			if (addMethod.equalsIgnoreCase(m.getName())==false) {
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
		throw new ElementBindingHandlerException("Could not find method: "+addMethod+" on: "+childClass+" id:"+getId());
	}

	@SuppressWarnings("rawtypes")
	public void createChilderen(Element parentElement,Object parentObject) throws ElementBindingHandlerException {
		if (parentClass==null | childClass==null | getMethod==null) {
			throw new IllegalStateException("Missing property: parentClass="+parentClass+" childClass="+childClass+" getMethod="+getMethod+".");
		}
		Method[] ms = parentObject.getClass().getMethods();
		for (Method m:ms) {
			Class<?>[] types = m.getParameterTypes();
			if (types.length != 0) {
				continue;
			}
			if (getMethod.equalsIgnoreCase(m.getName())==false) {
				continue;
			}
			Object result;
			try {
				result = m.invoke(parentObject, new Object[]{});
			} catch (Exception e) {
				throw new ElementBindingHandlerException("Invoke error: "+e.getMessage()+" from: "+getMethod+" on: "+parentObject+" id:"+getId(),e);
			} 
			if (result==null) {
				break;
			}
			if (result instanceof List) {
				for (Object o:(List)result) {
					createSafeChild(parentElement, o);
				}
				return;
			} else if (result instanceof Collection) {
				for (Object o:(Collection)result) {
					createSafeChild(parentElement, o);
				}
				return;
			} else if (result.getClass().isArray()) {
				for (Object o:(Object[])result) {
					createSafeChild(parentElement, o);
				}
				return;
			} else if (childClass.isAssignableFrom(result.getClass())) {
				createSafeChild(parentElement, result);
				return;
			} else {
				throw new ElementBindingHandlerException("Unsuported return type: "+result.getClass()+" need: "+childClass+" from: "+getMethod+" on: "+parentObject+" id:"+getId());
			}
		}
		throw new ElementBindingHandlerException("Could not find method: "+getMethod+" on: "+parentObject+" id:"+getId());
	}
	
	
	protected void createSafeChild(Element parentElement,Object childObject) {
		if (childClass.isAssignableFrom(childObject.getClass())==false) {
			return;
		}
		if (skipChilderenClassRegex!=null) {
			if (childObject.getClass().getName().matches(skipChilderenClassRegex)) {
				return; // skip
			}
		}
		createChild(parentElement,childObject);
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
	 * @return the addMethod
	 */
	public String getAddMethod() {
		return addMethod;
	}

	/**
	 * @param addMethod the addMethod to set
	 */
	public void setAddMethod(String addMethod) {
		this.addMethod = addMethod;
	}

	/**
	 * @return the getMethod
	 */
	public String getGetMethod() {
		return getMethod;
	}

	/**
	 * @param getMethod the getMethod to set
	 */
	public void setGetMethod(String getMethod) {
		this.getMethod = getMethod;
	}

	/**
	 * @return the skipChilderenClassRegex
	 */
	public String getSkipChilderenClassRegex() {
		return skipChilderenClassRegex;
	}

	/**
	 * @param skipChilderenClassRegex the skipChilderenClassRegex to set
	 */
	public void setSkipChilderenClassRegex(String skipChilderenClassRegex) {
		this.skipChilderenClassRegex = skipChilderenClassRegex;
	}
}
