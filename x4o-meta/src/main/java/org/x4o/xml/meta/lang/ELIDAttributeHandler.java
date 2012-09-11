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

package org.x4o.xml.meta.lang;

import java.util.logging.Logger;

import javax.el.ValueExpression;

import org.x4o.xml.element.AbstractElementAttributeHandler;
import org.x4o.xml.element.Element;
import org.x4o.xml.element.ElementConfiguratorException;


/**
 * Stores an ElementObject into the EL context.
 * 
 * @author Willem Cazander
 * @version 1.0 Aug 23, 2006
 */
public class ELIDAttributeHandler extends AbstractElementAttributeHandler {

	/**
	 * @see org.x4o.xml.element.ElementConfigurator#doConfigElement(org.x4o.xml.element.Element)
	 */
	public void doConfigElement(Element element) throws ElementConfiguratorException {
		String attributeValue = element.getAttributes().get(getAttributeName());
		if(attributeValue==null) { return; }
		if(element.getElementObject()==null) {
			if (this.isConfigAction()) {
				throw new NullPointerException("Can't bind null object to el context");
			}
			this.setConfigAction(true);
			return;
		}
		if(element.getElementObject()==null) { throw new NullPointerException("Can't bind null object to el context"); }
		ValueExpression ee = element.getElementLanguage().getExpressionFactory().createValueExpression(element.getElementLanguage().getELContext(),"${"+attributeValue+"}", element.getElementObject().getClass());
		Logger.getLogger(ELIDAttributeHandler.class.getName()).finer("Set Variable in ELContext: "+"${"+attributeValue+"}"+" object SET: "+element.getElementObject());
		ee.setValue(element.getElementLanguage().getELContext(), element.getElementObject());
	}
}
