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

/**
 * AbstractElementMetaBase stores the id and description.
 * 
 * 
 * @author Willem Cazander
 * @version 1.0 Jan 18, 2009
 */
public abstract class AbstractElementMetaBase implements ElementMetaBase {
	
	/** The id */
	private String id = null;
	
	/** The description */
	private String description = null;
	
	/**
	 * Gets the id.
	 * @see org.x4o.xml.element.ElementMetaBase#getId()
	 * @return The id.
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * Sets the id.
	 * @see org.x4o.xml.element.ElementMetaBase#setId(java.lang.String)
	 * @param id The id to set.
	 */
	public void setId(String id) {
		this.id=id;
	}
	
	/**
	 * Gets the description.
	 * @see org.x4o.xml.element.ElementConfigurator#getDescription()
	 * @return The description.
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Sets the description.
	 * @see org.x4o.xml.element.ElementConfigurator#setDescription(java.lang.String)
	 * @param description The description to set.
	 */
	public void setDescription(String description) {
		this.description=description;
	}
}
