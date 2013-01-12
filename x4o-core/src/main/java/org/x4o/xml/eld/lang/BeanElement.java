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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.x4o.xml.core.config.X4OLanguageClassLoader;
import org.x4o.xml.element.AbstractElement;
import org.x4o.xml.element.ElementException;

/**
 * BeanElement fills it elementObject from source with the bean.class attribute.
 * 
 * @author Willem Cazander
 * @version 1.0 Jan 21, 2007
 */
public class BeanElement extends AbstractElement {
	
	private List<Object> constructorArguments = null;
	
	/**
	 * Creates a BeanElement.
	 */
	public BeanElement() {
		constructorArguments = new ArrayList<Object>(3);
	}
	
	/**
	 * On start of element create the element object, filled from the bean.class attribute.
	 * @throws ElementException When bean could not be created.
	 */
	@Override
	public void doElementStart() throws ElementException {
		String className = getAttributes().get("bean.class");
		if("".equals(className) | className==null) { throw new ElementException("Set the bean.class attribute"); }
		try {
			Class<?>  beanClass = X4OLanguageClassLoader.loadClass(className);
			if (constructorArguments.isEmpty()) {
				setElementObject(beanClass.newInstance());
			} else {
				Class<?>[] arguClass = new Class<?>[constructorArguments.size()];
				constructorArguments.toArray(arguClass);
				Constructor<?> c = beanClass.getConstructor(arguClass);
				setElementObject(c.newInstance(constructorArguments));	
			}
		} catch (ClassNotFoundException e) {
			throw new ElementException(e);
		} catch (InstantiationException e) {
			throw new ElementException(e);
		} catch (IllegalAccessException e) {
			throw new ElementException(e);
		} catch (NoSuchMethodException e) {
			throw new ElementException(e);
		} catch (IllegalArgumentException e) {
			throw new ElementException(e);
		} catch (InvocationTargetException e) {
			throw new ElementException(e);
		}
	} 
	
	/**
	 * Add bean constructor arguments.
	 * @param argu	The argument to add to constructor.
	 */
	public void addConstuctorArgument(Object argu) {
		if (argu==null) {
			throw new NullPointerException("Can't add null argument for constructor.");
		}
		constructorArguments.add(argu);
	}
}
