/*
 * Copyright (c) 2004-2014, Willem Cazander
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
package org.x4o.xml.lang.meta;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.x4o.xml.element.AbstractElement;
import org.x4o.xml.element.ElementException;


/**
 * MethodElement invokes an method on a element object.
 * 
 * TODO: add args
 *
 * @author Willem Cazander
 * @version 1.0 Nov 21, 2007
 */
public class MethodElement extends AbstractElement {

	/**
	 * @see org.x4o.xml.element.AbstractElement#doElementRun()
	 */
	@Override
	public void doElementRun() throws ElementException {
		if (getParent()==null) {
			throw new IllegalStateException("need to have parent.");
		}
		Object parent = getParent().getElementObject();
		if (parent==null) {
			throw new IllegalStateException("need to have parent ElementObject.");
		}
		String methodString = getAttributes().get("method");
		Method[] ms = parent.getClass().getMethods();
		try {
			for (Method m:ms) {
				if (methodString.equalsIgnoreCase(m.getName())) {
					m.invoke(parent);
					return;
				}
			}
		} catch (IllegalArgumentException e) {
			throw new ElementException(e);
		} catch (IllegalAccessException e) {
			throw new ElementException(e);
		} catch (InvocationTargetException e) {
			throw new ElementException(e);
		}
		throw new ElementException("could not find method on parent element object: "+methodString+" on; "+parent);
	}
}
