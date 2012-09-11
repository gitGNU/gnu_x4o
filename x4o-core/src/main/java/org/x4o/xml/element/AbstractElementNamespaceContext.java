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

package org.x4o.xml.element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The abstract verion of ElementNamespaceContext
 * @author Willem Cazander
 * @version 1.0 Oct 28, 2009
 */
abstract public class AbstractElementNamespaceContext extends AbstractElementMetaBase implements ElementNamespaceContext {

	private ElementNamespaceInstanceProvider elementNamespaceInstanceProvider = null;
	private String prefixMapping = null;
	private Map<String,ElementClass> elementClasses = null;
	private String uri = null;
	private String name = null;
	private String schemaUri = null;
	private String schemaResource = null;
	private String schemaPrefix = null;
	private Boolean languageRoot = null;
	
	public AbstractElementNamespaceContext() {
		elementClasses = new HashMap<String,ElementClass>(100);
	}

	/**
	 * @see org.x4o.xml.element.ElementNamespaceContext#getPrefixMapping()
	 */
	public String getPrefixMapping() {
		return prefixMapping;
	}

	/**
	 * @return the elementNamespaceInstanceProvider
	 */
	public ElementNamespaceInstanceProvider getElementNamespaceInstanceProvider() {
		return elementNamespaceInstanceProvider;
	}

	/**
	 * @param elementNamespaceInstanceProvider the elementNamespaceInstanceProvider to set
	 */
	public void setElementNamespaceInstanceProvider(ElementNamespaceInstanceProvider elementNamespaceInstanceProvider) {
		this.elementNamespaceInstanceProvider = elementNamespaceInstanceProvider;
	}

	/**
	 * @see org.x4o.xml.element.ElementNamespaceContext#setPrefixMapping(java.lang.String)
	 */
	public void setPrefixMapping(String prefixMapping) {
		this.prefixMapping=prefixMapping;
	}

	/**
	 * @see org.x4o.xml.element.ElementNamespaceContext#addElementClass(org.x4o.xml.element.ElementClass)
	 */
	public void addElementClass(ElementClass elementClass) {
		if (elementClass.getTag()==null) {
			throw new NullPointerException("ElementClass not correctly configured getTag is null.");
		}
		elementClasses.put(elementClass.getTag(), elementClass);
	}

	/**
	 * @see org.x4o.xml.element.ElementNamespaceContext#getElementClass(java.lang.String)
	 */
	public ElementClass getElementClass(String tag) {
		return elementClasses.get(tag);
	}

	/**
	 * @see org.x4o.xml.element.ElementNamespaceContext#getElementClasses()
	 */
	public List<ElementClass> getElementClasses() {
		return new ArrayList<ElementClass>(elementClasses.values());
	}

	/**
	 * @return the uri
	 */
	public String getUri() {
		return uri;
	}

	/**
	 * @param uri the namespace uri to set
	 */
	public void setUri(String uri) {
		this.uri = uri;
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
	 * @return the schemaUri
	 */
	public String getSchemaUri() {
		return schemaUri;
	}

	/**
	 * @param schemaUri the schemaUri to set
	 */
	public void setSchemaUri(String schemaUri) {
		this.schemaUri = schemaUri;
	}

	/**
	 * @return the schemaResource
	 */
	public String getSchemaResource() {
		return schemaResource;
	}

	/**
	 * @param schemaResource the schemaResource to set
	 */
	public void setSchemaResource(String schemaResource) {
		this.schemaResource = schemaResource;
	}

	/**
	 * @return the languageRoot
	 */
	public Boolean getLanguageRoot() {
		return languageRoot;
	}

	/**
	 * @param languageRoot the languageRoot to set
	 */
	public void setLanguageRoot(Boolean languageRoot) {
		this.languageRoot = languageRoot;
	}

	/**
	 * @return the schemaPrefix
	 */
	public String getSchemaPrefix() {
		return schemaPrefix;
	}

	/**
	 * @param schemaPrefix the schemaPrefix to set
	 */
	public void setSchemaPrefix(String schemaPrefix) {
		this.schemaPrefix = schemaPrefix;
	}
}
