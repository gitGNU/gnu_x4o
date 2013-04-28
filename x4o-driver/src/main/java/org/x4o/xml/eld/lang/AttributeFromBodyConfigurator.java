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

import java.util.List;

import org.x4o.xml.element.AbstractElementConfigurator;
import org.x4o.xml.element.Element;
import org.x4o.xml.element.Element.ElementType;
import org.x4o.xml.element.ElementConfiguratorException;

/**
 * AttributeFromBodyConfigurator sets the body as attribute.
 * 
 * @author Willem Cazander
 * @version 1.0 Aug 23, 2012
 */
public class AttributeFromBodyConfigurator extends AbstractElementConfigurator {

	private String name = null;
	private String bodyType = null;
	
	/**
	 * Config an element body as attribute of parent elememt.
	 * @param element The element to config. 
	 * @throws ElementConfiguratorException Is thrown when object tree is non valid.
	 * @see org.x4o.xml.element.ElementConfigurator#doConfigElement(org.x4o.xml.element.Element)
	 */
	public void doConfigElement(Element element) throws ElementConfiguratorException {
		if (name==null) {
			throw new ElementConfiguratorException(this,"name attribute is not set.");
		}
		if (name.length()==0) {
			throw new ElementConfiguratorException(this,"name attribute is empty.");
		}
		if (bodyType==null) {
			bodyType = ElementType.characters.name();
		}
		String value = null;
		if ("characters".equals(bodyType)) {
			value = fetchBodyType(element,ElementType.characters);
		} else if ("comment".equals(bodyType)) {
			value = fetchBodyType(element,ElementType.comment);
		} else if ("ignorableWhitespace".equals(bodyType)) {
			value = fetchBodyType(element,ElementType.ignorableWhitespace);
		} else {
			throw new ElementConfiguratorException(this,"bodyType attribute value is unknown; "+bodyType);
		}
		if (value.trim().length()==0) {
			return;
		}
		element.getAttributes().put(name, value);
	}
	
	private String fetchBodyType(Element element,ElementType elementType) {
		StringBuffer buf = new StringBuffer(300);
		List<Element> childsAll = element.getAllChilderen();
		List<Element> childs = ElementType.filterElements(childsAll, elementType);
		for (int i=0;i<childs.size();i++) {
			Element e = childs.get(i);
			buf.append(e.getElementObject().toString());
		}
		return buf.toString();
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the bodyType
	 */
	public String getBodyType() {
		return bodyType;
	}

	/**
	 * @param bodyType the bodyType to set
	 */
	public void setBodyType(String bodyType) {
		this.bodyType = bodyType;
	}
}
