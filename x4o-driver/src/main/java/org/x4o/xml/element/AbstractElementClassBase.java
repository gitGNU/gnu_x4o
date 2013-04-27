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

package	org.x4o.xml.element;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AbstractElementClassBase provides basic element meta class support.
 * 
 * @author Willem Cazander
 * @version 1.0 Jan 19, 2012
 */
public abstract class AbstractElementClassBase extends AbstractElementMetaBase implements ElementClassBase {

	private Map<String,ElementClassAttribute> elementClassAttributes = null;
	private List<ElementConfigurator> elementConfigurators = null;
	private Map<String,List<String>> elementParents = null;
	
	public AbstractElementClassBase() {
		elementConfigurators = new ArrayList<ElementConfigurator>(5);
		elementClassAttributes = new HashMap<String,ElementClassAttribute>(15);
		elementParents = new HashMap<String,List<String>>(5);
	}
	
	/**
	 * @see ElementClass#getElementConfigurators()
	 */
	public List<ElementConfigurator> getElementConfigurators() {
		return elementConfigurators;
	}

	/**
	 * @see ElementClass#addElementConfigurators(ElementConfigurator)
	 * @param elementConfigurator The ElementConfigurator to add.
	 */
	public void addElementConfigurators(ElementConfigurator elementConfigurator) {
		elementConfigurators.add(elementConfigurator);
	}

	/**
	 * @param elementClassAttribute The ElementClassAttribute to add.
	 */
	public void addElementClassAttribute(ElementClassAttribute elementClassAttribute) {
		elementClassAttributes.put(elementClassAttribute.getId(),elementClassAttribute);
	}
	
	/**
	 * @return All the element attributes.
	 */
	public Collection<ElementClassAttribute> getElementClassAttributes() {
		return elementClassAttributes.values();
	}
	
	/**
	 * Get the ElementClassAttribute from its name. 
	 * @param attributeName The attribute name.
	 * @return The element class attribute for the name.
	 */
	public ElementClassAttribute getElementClassAttributeByName(String attributeName) {
		return elementClassAttributes.get(attributeName);
	}
	
	/**
	 * Adds parent tag.
	 * @see org.x4o.xml.element.ElementClassBase#addElementParent(java.lang.String,java.lang.String)
	 * @param namespaceUri The namespace uri of the parent tag.
	 * @param tag The tag of the parent of this tag.
	 */
	public void addElementParent(String namespaceUri,String tag) {
		if (namespaceUri==null) {
			throw new NullPointerException("Can't add parent tag with null namespace uri.");
		}
		if (namespaceUri.isEmpty()) {
			throw new IllegalArgumentException("Can't add parent tag with empty namespace uri.");
		}
		List<String> tags = elementParents.get(namespaceUri);
		if (tags==null) {
			tags = new ArrayList<String>(5);
			elementParents.put(namespaceUri, tags);
		}
		tags.add(tag);
	}

	/**
	 * Removes parent tag.
	 * @see org.x4o.xml.element.ElementClassBase#removeElementParent(java.lang.String,java.lang.String)
	 * @param namespaceUri The namespace uri of the parent tag.
	 * @param tag The tag of the parent of this tag.
	 */
	public void removeElementParent(String namespaceUri,String tag) {
		List<String> tags = elementParents.get(namespaceUri);
		if (tags==null) {
			return;
		}
		tags.remove(tag);
	}

	/**
	 * Returns the parent per namespace uri.
	 * @see org.x4o.xml.element.ElementClassBase#getElementParents(java.lang.String)
	 * @param namespaceUri The namespace uri to gets the parents of.
	 * @return List of parent tags of requested parent namespace uri.
	 */
	public List<String> getElementParents(String namespaceUri) {
		return elementParents.get(namespaceUri);
	}
}
