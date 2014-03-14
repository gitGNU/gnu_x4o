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

import java.util.Collection;
import java.util.List;

/**
 * The ElementClassBase is for all higher instances the base config of an ElementClass config structure.
 * 
 * 
 * @author Willem Cazander
 * @version 1.0 Jan 19, 2012
 */
public interface ElementClassBase extends ElementMetaBase {
	
	List<ElementConfigurator> getElementConfigurators();
	void addElementConfigurators(ElementConfigurator elementConfigurator);
	
	Collection<ElementClassAttribute> getElementClassAttributes();
	ElementClassAttribute getElementClassAttributeByName(String attributeName);
	void addElementClassAttribute(ElementClassAttribute elementClassAttribute);
	
	/**
	 * Add an parent element tag.
	 * Used: for xsd/doc only.
	 * @param namespaceUri The namespace uri of this tag relation.
	 * @param tag	The parent element tag.
	 */
	void addElementParent(String namespaceUri,String tag);
	
	/**
	 * Remove and parent element
	 * Used: for xsd/doc only. 
	 * @param namespaceUri The namespace uri of this tag relation.
	 * @param tag	The parent element tag.
	 */
	void removeElementParent(String namespaceUri,String tag);
	
	/**
	 * Returns list of parent element tags.
	 * Used: for xsd/doc only. 
	 * @param namespaceUri The namespace uri of this tag relation.
	 * @return	The list of tags.
	 */
	List<String> getElementParents(String namespaceUri);
}
