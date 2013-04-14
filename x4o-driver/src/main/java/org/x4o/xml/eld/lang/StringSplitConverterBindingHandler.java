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

import org.x4o.xml.conv.text.StringSplitConverter;
import org.x4o.xml.conv.text.StringSplitConverterStep;
import org.x4o.xml.element.AbstractElementBindingHandler;
import org.x4o.xml.element.Element;
import org.x4o.xml.element.ElementBindingHandlerException;

/**
 * StringSplitConverterBindingHandler binds the string split converter step to parent.
 * 
 * @author Willem Cazander
 * @version 1.0 Aug 21, 2012
 */
public class StringSplitConverterBindingHandler extends AbstractElementBindingHandler<StringSplitConverter> {

	
	private final static Class<?>[] CLASSES_CHILD = new Class[] {
		StringSplitConverterStep.class
	};
	
	/**
	 * @see org.x4o.xml.element.ElementBindingHandler#getBindParentClass()
	 */
	public Class<?> getBindParentClass() {
		return StringSplitConverter.class;
	}

	/**
	 * @see org.x4o.xml.element.ElementBindingHandler#getBindChildClasses()
	 */
	public Class<?>[] getBindChildClasses() {
		return CLASSES_CHILD;
	}

	/**
	 * @see org.x4o.xml.element.AbstractElementBindingHandler#bindChild(org.x4o.xml.element.Element, java.lang.Object, java.lang.Object)
	 */
	public void bindChild(Element childElement,StringSplitConverter parent, Object childObject) throws ElementBindingHandlerException {
		if (childObject instanceof StringSplitConverterStep) { 
			parent.addStringSplitConverterStep((StringSplitConverterStep)childObject);
		}
	}

	/**
	 * @see org.x4o.xml.element.AbstractElementBindingHandler#createChilderen(org.x4o.xml.element.Element, java.lang.Object)
	 */
	@Override
	public void createChilderen(Element parentElement,StringSplitConverter parentObject) throws ElementBindingHandlerException {
		for (StringSplitConverterStep child:parentObject.getStringSplitConverterSteps()) {
			createChild(parentElement, child);
		}
	}
}
