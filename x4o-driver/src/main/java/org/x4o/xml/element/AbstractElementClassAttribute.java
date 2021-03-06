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
package org.x4o.xml.element;

import java.util.ArrayList;
import java.util.List;

import org.x4o.xml.conv.ObjectConverter;

/**
 * An AbstractElementClassAttribute.
 * 
 * @author Willem Cazander
 * @version 1.0 Jan 19, 2012
 */
public abstract class AbstractElementClassAttribute extends AbstractElementMetaBase implements ElementClassAttribute {

	private ObjectConverter objectConverter = null;
	private Object defaultValue = null;
	private List<String> attributeAliases = null;
	private Boolean required = null;
	private Boolean runResolveEL = null;
	//private Boolean runInterfaces = null;
	private Boolean runConverters = null;
	private Boolean runBeanValue = null;
	private Integer writeOrder = null;
	
	/**
	 * Creates a AbstractElementClassAttribute.
	 */
	public AbstractElementClassAttribute() {
		attributeAliases = new ArrayList<String>(3);
	}

	/**
	 * Returns the objectConverter.
	 * @return The objectConverter.
	 */
	public ObjectConverter getObjectConverter() {
		return objectConverter;
	}

	/**
	 * Sets the objectConverter.
	 * @param objectConverter The objectConverter to set.
	 */
	public void setObjectConverter(ObjectConverter objectConverter) {
		this.objectConverter = objectConverter;
	}

	/**
	 * Sets the default value.
	 * @param	defaultValue	The defaultValue to set.
	 * @see org.x4o.xml.element.ElementClassAttribute#setDefaultValue(java.lang.Object)
	 */
	public void setDefaultValue(Object defaultValue) {
		this.defaultValue=defaultValue;
	}

	/**
	 * Returns the default value.
	 * @return The default value.
	 * @see org.x4o.xml.element.ElementClassAttribute#getDefaultValue()
	 */
	public Object getDefaultValue() {
		return defaultValue;
	}

	/**
	 * Adds an alias of this attribute.
	 * @param	alias	The alias to add.
	 * @see org.x4o.xml.element.ElementClassAttribute#addAttributeAlias(java.lang.String)
	 */
	public void addAttributeAlias(String alias) {
		attributeAliases.add(alias);
	}

	/**
	 * Removes an alias of this attribute.
	 * @param alias	The alias to remove.
	 * @see org.x4o.xml.element.ElementClassAttribute#removeAttributeAlias(java.lang.String)
	 */
	public void removeAttributeAlias(String alias) {
		attributeAliases.remove(alias);
	}

	/**
	 * Returns all aliases of this attribute.
	 * @return	An list of aliases.
	 * @see org.x4o.xml.element.ElementClassAttribute#getAttributeAliases()
	 */
	public List<String> getAttributeAliases() {
		return attributeAliases;
	}

	/**
	 * @return the required.
	 */
	public Boolean getRequired() {
		return required;
	}

	/**
	 * @param required the required to set.
	 */
	public void setRequired(Boolean required) {
		this.required = required;
	}

	/**
	 * @return the runResolveEL.
	 */
	public Boolean getRunResolveEL() {
		return runResolveEL;
	}

	/**
	 * @param runResolveEL the runResolveEL to set.
	 */
	public void setRunResolveEL(Boolean runResolveEL) {
		this.runResolveEL = runResolveEL;
	}

	/**
	 * @return the runConverters.
	 */
	public Boolean getRunConverters() {
		return runConverters;
	}

	/**
	 * @param runConverters the runConverters to set.
	 */
	public void setRunConverters(Boolean runConverters) {
		this.runConverters = runConverters;
	}

	/**
	 * @return the runBeanValue.
	 */
	public Boolean getRunBeanValue() {
		return runBeanValue;
	}

	/**
	 * @param runBeanValue the runBeanValue to set.
	 */
	public void setRunBeanValue(Boolean runBeanValue) {
		this.runBeanValue = runBeanValue;
	}

	/**
	 * @return the writeOrder.
	 */
	public Integer getWriteOrder() {
		return writeOrder;
	}

	/**
	 * @param writeOrder the writeOrder to set.
	 */
	public void setWriteOrder(Integer writeOrder) {
		this.writeOrder = writeOrder;
	}
}
