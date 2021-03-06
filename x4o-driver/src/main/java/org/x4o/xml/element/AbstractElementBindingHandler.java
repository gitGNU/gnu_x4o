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
package	org.x4o.xml.element;


/**
 * An AbstractElementBindingHandler.<br>
 * Does nothing.
 * 
 * @author Willem Cazander
 * @version 1.0 Apr 16, 2006
 */
public abstract class AbstractElementBindingHandler<T> extends AbstractElementMetaBase implements ElementBindingHandler {

	abstract public void bindChild(Element childElement,T parentObject,Object childObject) throws ElementBindingHandlerException;
	
	abstract public void createChilderen(Element parentElement,T parentObject) throws ElementBindingHandlerException;
	
	@SuppressWarnings("unchecked")
	public void bindChild(Element childElement) throws ElementBindingHandlerException {
		bindChild(childElement,(T)childElement.getParent().getElementObject(), childElement.getElementObject());
	}
	
	@SuppressWarnings("unchecked")
	public void createChilderen(Element parentElement) throws ElementBindingHandlerException {
		createChilderen(parentElement,(T)parentElement.getElementObject());
	}
	
	protected void createChild(Element parentElement,Object childObject) {
		if (childObject==null) {
			return;
		}
		if (parentElement==null) {
			throw new NullPointerException("Can't create child with null parent.");
		}
		Element childElement = parentElement.getLanguageSession().getLanguage().createElementInstance(parentElement.getLanguageSession(), childObject.getClass());
		if (childElement==null) {
			throw new NullPointerException("Could not find Element for child: "+childObject.getClass());
		}
		childElement.setElementObject(childObject);
		childElement.setParent(parentElement);
		parentElement.addChild(childElement);
	}
}
