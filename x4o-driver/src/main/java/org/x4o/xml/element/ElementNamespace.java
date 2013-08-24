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
package org.x4o.xml.element;

import java.util.List;

/**
 * ElementNamespace stores all element tag classes for the namespace.
 * 
 * @author Willem Cazander
 * @version 1.0 Oct 28, 2009
 */
public interface ElementNamespace extends ElementMetaBase {
	
	/**
	 * Sets the prefix mapping.
	 * @param prefixMapping	The prefix mapping to set.
	 */
	void setPrefixMapping(String prefixMapping);
	
	/**
	 * Gets the set prefix mapping of this namespace.
	 * @return Returns the prefix mapping.
	 */
	String getPrefixMapping();
	
	/**
	 * Sets the elememen instance provider which creates the elements objects.
	 * @param elementNamespaceInstanceProvider	The ElementNamespaceInstanceProvider to set.
	 */
	void setElementNamespaceInstanceProvider(ElementNamespaceInstanceProvider elementNamespaceInstanceProvider);
	
	/**
	 * Returns the ElementProvider.
	 * @return	Returns the ElementNamespaceInstanceProvider for this namespace.
	 */
	ElementNamespaceInstanceProvider getElementNamespaceInstanceProvider();
	
	/**
	 * Adds an ElementClass.
	 * @param elementClass	The elementClass to add to this context.
	 */
	void addElementClass(ElementClass elementClass);
	
	/**
	 * Gets the ElementClass for an namespace and tag.
	 * @param tag	The tag to get the ElementClass for.
	 * @return	Returns the ElementClass for a tag in an namespace.
	 */
	ElementClass getElementClass(String tag);
	
	/**
	 * Returns the loaded ElementClass'es in an namespace in this context.
	 * @return	Returns all ElementClasses handled by this namespace.
	 */
	List<ElementClass> getElementClasses();
	
	/**
	 * @return the uri of this namespace.
	 */
	String getUri();

	/**
	 * @param uri the namespace uri to set.
	 */
	void setUri(String uri);

	/**
	 * @return the name.
	 */
	String getName();

	/**
	 * @param name the name to set.
	 */
	void setName(String name);

	/**
	 * @return the schemaUri.
	 */
	String getSchemaUri();

	/**
	 * @param schemaUri the schemaUri to set.
	 */
	void setSchemaUri(String schemaUri);

	/**
	 * @return the schemaResource.
	 */
	String getSchemaResource();

	/**
	 * @param schemaResource the schemaResource to set.
	 */
	void setSchemaResource(String schemaResource);
	
	/**
	 * @return the description.
	 */
	String getDescription();

	/**
	 * @param description the description to set.
	 */
	void setDescription(String description);
	
	/**
	 * @return the languageRoot
	 */
	Boolean getLanguageRoot();

	/**
	 * @param languageRoot the languageRoot to set
	 */
	void setLanguageRoot(Boolean languageRoot);
	
	/**
	 * @return the schemaPrefix
	 */
	String getSchemaPrefix();

	/**
	 * @param schemaPrefix the schemaPrefix to set
	 */
	void setSchemaPrefix(String schemaPrefix);
}
