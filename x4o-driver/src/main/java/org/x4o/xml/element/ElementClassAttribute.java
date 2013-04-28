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

import java.util.List;

import org.x4o.xml.conv.ObjectConverter;

/**
 * The ElementClass stores all parse information to config the Element.
 * 
 * 
 * @author Willem Cazander
 * @version 1.0 Aug 11, 2005
 */
public interface ElementClassAttribute extends ElementMetaBase {
	
	/**
	 * Gets the ObjectConverter.
	 * @return The ObjectConverter.
	 */
	ObjectConverter getObjectConverter();
	
	/**
	 * Add the ObjectConverter whichs converts.
	 * @param	objectConverter	The objectConverter to set for this attribute.
	 */
	void setObjectConverter(ObjectConverter objectConverter);
	
	/**
	 * Sets the defaultValue of this attribute.
	 * @param defaultValue	The defaultValue to set.
	 */
	void setDefaultValue(Object defaultValue);
	
	/**
	 * Gets the default value.
	 * @return	Returns the default value if any.
	 */
	Object getDefaultValue();
	
	/**
	 * Add an attribute alias for this attribute.
	 * @param alias	The alias.
	 */
	void addAttributeAlias(String alias);
	
	/**
	 * Removes an attribute alias.
	 * @param alias	The alias.
	 */
	void removeAttributeAlias(String alias);
	
	/**
	 * Get all the aliases for this attribute.
	 * @return	The defined aliases.
	 */
	List<String> getAttributeAliases();
	
	/**
	 * Gets the required state of this attribute.
	 * @return If true then attribute is required.
	 */
	Boolean getRequired();

	/**
	 * Sets the required state of this attribute.
	 * @param required the required to set.
	 */
	void setRequired(Boolean required);
	
	/**
	 * @return the runResolveEL.
	 */
	Boolean getRunResolveEL();

	/**
	 * @param runResolveEL the runResolveEL to set.
	 */
	void setRunResolveEL(Boolean runResolveEL);

	/**
	 * @return the runConverters.
	 */
	Boolean getRunConverters();

	/**
	 * @param runConverters the runConverters to set.
	 */
	void setRunConverters(Boolean runConverters);

	/**
	 * @return the runBeanValue.
	 */
	Boolean getRunBeanValue();

	/**
	 * @param runBeanValue the runBeanValue to set.
	 */
	void setRunBeanValue(Boolean runBeanValue);
	
	/**
	 * @return the writeOrder
	 */
	Integer getWriteOrder();

	/**
	 * @param writeOrder the writeOrder to set
	 */
	void setWriteOrder(Integer writeOrder);
}
