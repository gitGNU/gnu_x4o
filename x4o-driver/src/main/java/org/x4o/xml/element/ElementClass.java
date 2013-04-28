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

import java.util.List;

/**
 * The ElementClass stores all parse information to config the Element.
 * 
 * 
 * @author Willem Cazander
 * @version 1.0 Aug 11, 2005
 */
public interface ElementClass extends ElementClassBase {
	
	/**
	 * Gets the ElementClass.
	 * @return the elementClass
	 */
	Class<?> getElementClass();

	/**
	 * Sets the ElementClass.
	 * @param elementClass the elementClass to set.
	 */
	void setElementClass(Class<?> elementClass);

	/**
	 * @return the objectClass.
	 */
	Class<?> getObjectClass();

	/**
	 * @param objectClass the objectClass to set.
	 */
	void setObjectClass(Class<?> objectClass);
	
	/**
	 * @return the autoAttributes.
	 */
	Boolean getAutoAttributes();

	/**
	 * @param autoAttributes the autoAttributes to set.
	 */
	void setAutoAttributes(Boolean autoAttributes);
	
	/**
	 * @return the schemaContentBase
	 */
	String getSchemaContentBase();

	/**
	 * @param schemaContentBase the schemaContentBase to set
	 */
	void setSchemaContentBase(String schemaContentBase);
	
	/**
	 * @return the schemaContentComplex
	 */
	Boolean getSchemaContentComplex();

	/**
	 * @param schemaContentComplex the schemaContentComplex to set
	 */
	void setSchemaContentComplex(Boolean schemaContentComplex);

	/**
	 * @return the schemaContentMixed
	 */
	Boolean getSchemaContentMixed();

	/**
	 * @param schemaContentMixed the schemaContentMixed to set
	 */
	void setSchemaContentMixed(Boolean schemaContentMixed);
	
	/**
	 * Add an skip phase for this element.
	 * @param phase	The phase name.
	 */
	void addSkipPhase(String phase);
	
	/**
	 * Removes an skip phase for this element.
	 * @param phase	The phase name.
	 */
	void removeSkipPhase(String phase);
	
	/**
	 * Get all the skip phases for this element.
	 * @return	The defined phases.
	 */
	List<String> getSkipPhases();
}
