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
import java.util.List;

/**
 * An AbstractElementClass.
 * 
 * @author Willem Cazander
 * @version 1.0 Aug 11, 2005
 */
public abstract class AbstractElementClass extends AbstractElementClassBase implements ElementClass {

	private String tag = null;
	private Class<?> objectClass = null;
	private Class<?> elementClass = null;
	private Boolean autoAttributes = true;
	private String schemaContentBase = null;
	private Boolean schemaContentComplex = null;
	private Boolean schemaContentMixed = null;
	private List<String> skipPhases = null;
	
	public AbstractElementClass() {
		skipPhases = new ArrayList<String>(3);
	}
	
	/**
	 * @see ElementClass#getTag()
	 */
	public String getTag() {
		return tag;
	}
	
	/**
	 * @see ElementClass#setTag(String)
	 */
	public void setTag(String tag) {
		this.tag = tag;
	}

	/**
	 * @see ElementClass#getElementClass()
	 */
	public Class<?> getElementClass() {
		return elementClass;
	}

	/**
	 * @see ElementClass#setElementClass(Class)
	 */
	public void setElementClass(Class<?> elementClass) {
		this.elementClass = elementClass;
	}

	/**
	 * @see ElementClass#getObjectClass()
	 */
	public Class<?> getObjectClass() {
		return objectClass;
	}

	/**
	 * @see ElementClass#setObjectClass(Class)
	 */
	public void setObjectClass(Class<?> objectClass) {
		this.objectClass = objectClass;
	}

	/**
	 * @return the autoAttributes
	 */
	public Boolean getAutoAttributes() {
		return autoAttributes;
	}

	/**
	 * @param autoAttributes the autoAttributes to set
	 */
	public void setAutoAttributes(Boolean autoAttributes) {
		this.autoAttributes = autoAttributes;
	}

	/**
	 * @return the schemaContentBase
	 */
	public String getSchemaContentBase() {
		return schemaContentBase;
	}

	/**
	 * @param schemaContentBase the schemaContentBase to set
	 */
	public void setSchemaContentBase(String schemaContentBase) {
		this.schemaContentBase = schemaContentBase;
	}

	/**
	 * @return the schemaContentComplex
	 */
	public Boolean getSchemaContentComplex() {
		return schemaContentComplex;
	}

	/**
	 * @param schemaContentComplex the schemaContentComplex to set
	 */
	public void setSchemaContentComplex(Boolean schemaContentComplex) {
		this.schemaContentComplex = schemaContentComplex;
	}

	/**
	 * @return the schemaContentMixed
	 */
	public Boolean getSchemaContentMixed() {
		return schemaContentMixed;
	}

	/**
	 * @param schemaContentMixed the schemaContentMixed to set
	 */
	public void setSchemaContentMixed(Boolean schemaContentMixed) {
		this.schemaContentMixed = schemaContentMixed;
	}

	/**
	 * @see org.x4o.xml.element.ElementClass#addSkipPhase(java.lang.String)
	 */
	public void addSkipPhase(String phase) {
		skipPhases.add(phase);
	}

	/**
	 * @see org.x4o.xml.element.ElementClass#removeSkipPhase(java.lang.String)
	 */
	public void removeSkipPhase(String phase) {
		skipPhases.remove(phase);
	}

	/**
	 * @see org.x4o.xml.element.ElementClass#getSkipPhases()
	 */
	public List<String> getSkipPhases() {
		return skipPhases;
	}
}
