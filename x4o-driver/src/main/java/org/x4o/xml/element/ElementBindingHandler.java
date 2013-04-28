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


/**
 * Bind ElementObjects together.
 * 
 * This interface is used to bind a parent and child ElementObject together.
 * For example; when both objects are an JComponent then we can add the child to the parent
 * with the method: ((JComponent)parent).add((JComponent)child);
 * 
 * 
 * @author Willem Cazander
 * @version 1.0 Aug 17, 2005
 */
public interface ElementBindingHandler extends ElementMetaBase {
	
	/**
	 * @return	Returns the parent classes which this binding handler can do.
	 */
	Class<?> getBindParentClass();
	
	/**
	 * @return	Returns array of child classes which this binding handler can do.
	 */
	Class<?>[] getBindChildClasses();
		
	/**
	 * Do the binding of this child to the parent object.
	 * @param childElement	The child element to bind to the parent.'
	 * @throws ElementBindingHandlerException When binding could not happen.
	 */
	void bindChild(Element childElement) throws ElementBindingHandlerException;
	
	/**
	 * Creates the childeren of the parent object.
	 * @param parentElement	The parent element to create the childeren from.'
	 * @throws ElementBindingHandlerException When binding could not happen.
	 */
	void createChilderen(Element parentElement) throws ElementBindingHandlerException;
}
