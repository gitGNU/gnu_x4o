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

package org.x4o.xml.lang.meta;

import java.util.logging.Logger;

import javax.el.ValueExpression;

import org.x4o.xml.element.AbstractElement;
import org.x4o.xml.element.ElementException;


/**
 * An ELReferenceElement.<br>
 * Fills the ElementObject with an object from el.
 * 
 * @author Willem Cazander
 * @version 1.0 Jan 23, 2007
 */
public class ELReferenceElement extends AbstractElement {
	
	@Override
	public void doElementRun() throws ElementException {
		String attributeValue = getAttributes().get("el.ref");
		if("".equals(attributeValue) | attributeValue==null) { throw new ElementException("Set the el.ref attribute"); }
		ValueExpression ee = getLanguageContext().getExpressionLanguageFactory().createValueExpression(getLanguageContext().getExpressionLanguageContext(),"${"+attributeValue+"}",Object.class);
		Logger.getLogger(ELReferenceElement.class.getName()).finer("Get Variable in ELContext: ${"+attributeValue+"}");
		setElementObject(ee.getValue(getLanguageContext().getExpressionLanguageContext()));
	} 
}
