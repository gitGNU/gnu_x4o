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

import java.lang.reflect.Method;
import java.util.Map;

import org.x4o.xml.conv.ObjectConverterException;
import org.x4o.xml.eld.lang.BeanElement;
import org.x4o.xml.element.AbstractElement;
import org.x4o.xml.element.ElementException;


/**
 * An PropertyElement.<br>
 * 
 * 
 * @author Willem Cazander
 * @version 1.0 Feb 14, 2007
 */
public class PropertyElement extends AbstractElement {

	/**
	 * @see org.x4o.xml.element.AbstractElement#doElementStart()
	 */
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void doElementRun() throws ElementException {
		
		if (getParent().getElementObject()==null) {
			throw new IllegalStateException("need to have parent ElementObject");
		}
		
		String name = getAttributes().get("key");
		String valueString = getAttributes().get("value");
		Object value = valueString;
		
		if (name==null) {
			name = getAttributes().get("name");
		}
		if (name==null) {
			name = getAttributes().get("field");
		}

		// convert value object
		try {
			value = getLanguageSession().getElementAttributeValueParser().getParameterValue(name,valueString,this);
		} catch (ObjectConverterException ece) {
			throw new ElementException(ece);
		}
		
		if (getParent() instanceof BeanElement) {
			BeanElement bean = (BeanElement)getParent();
			bean.addConstuctorArgument(value);
			return;
		}
		
		if (name==null) {
			throw new IllegalStateException("Can't set properties without key.");
		}
		
		// check map interface
		if (getParent().getElementObject() instanceof Map) {
			((Map)getParent().getElementObject()).put(name,value);
			return;
		}
		
		// check for setProperty(String,Object) method
		Method[] methodes = getParent().getElementObject().getClass().getMethods();
		for (int i=0;i<methodes.length;i++) {
			Method method = methodes[i];				
			if (method.getName().toLowerCase().equals("setproperty")) {
				try {
					method.invoke(getParent().getElementObject(),new Object[]{name,value});
					return;
				} catch (Exception e) {
					throw new ElementException("Could not invoke setproperty of "+method.getName()+" on: "+getParent().getElementObject(),e);
				}
			}
		}
		
		// try to set as property on bean.
		try {
			getLanguageSession().getElementObjectPropertyValue().setProperty(getParent().getElementObject(), name, value);
			return;
		} catch (Exception e) {
			throw new ElementException("Could not set property on parent element object: "+name,e);
		}
	}
}
