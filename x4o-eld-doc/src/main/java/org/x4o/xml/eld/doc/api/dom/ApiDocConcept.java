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
package org.x4o.xml.eld.doc.api.dom;

import java.util.ArrayList;
import java.util.List;

/**
 * ApiDocConcept defines the prime concepts which for which we write documents.
 * 
 * @author Willem Cazander
 * @version 1.0 May 12, 2013
 */
public class ApiDocConcept {

	private String id = null;
	private String name = null;
	private String descriptionName = null;
	private String descriptionHelp = null;
	private ApiDocConcept parent = null;
	private Class<?> conceptClass = null;
	private List<ApiDocConcept> childConcepts = null;
	
	public ApiDocConcept() {
		childConcepts = new ArrayList<ApiDocConcept>(5);
	}
	
	public ApiDocConcept(ApiDocConcept parent,String id,Class<?> conceptClass) {
		this();
		setId(id);
		setConceptClass(conceptClass);
		setParent(parent);
	}
	
	public ApiDocConcept(ApiDocConcept parent,String[] text,Class<?> conceptClass) {
		this(parent,text[0],text[1],text[2],text[3],conceptClass);
	}
	
	public ApiDocConcept(ApiDocConcept parent,String id,String name,String descriptionName,String descriptionHelp,Class<?> conceptClass) {
		this(parent,id,conceptClass);
		setName(name);
		setDescriptionName(descriptionName);
		setDescriptionHelp(descriptionHelp);
	}
	
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
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
	 * @return the descriptionName
	 */
	public String getDescriptionName() {
		return descriptionName;
	}
	
	/**
	 * @param descriptionName the descriptionName to set
	 */
	public void setDescriptionName(String descriptionName) {
		this.descriptionName = descriptionName;
	}
	
	/**
	 * @return the descriptionHelp
	 */
	public String getDescriptionHelp() {
		return descriptionHelp;
	}
	
	/**
	 * @param descriptionHelp the descriptionHelp to set
	 */
	public void setDescriptionHelp(String descriptionHelp) {
		this.descriptionHelp = descriptionHelp;
	}
	
	/**
	 * @return the conceptClass
	 */
	public Class<?> getConceptClass() {
		return conceptClass;
	}
	
	/**
	 * @param conceptClass the conceptClass to set
	 */
	public void setConceptClass(Class<?> conceptClass) {
		this.conceptClass = conceptClass;
	}
	
	public void addChildConcepts(ApiDocConcept childConcept) {
		childConcepts.add(childConcept);
	}
	
	public void removeChildConcept(ApiDocConcept childConcept) {
		childConcepts.remove(childConcept);
	}
	
	public List<ApiDocConcept> getChildConcepts() {
		return childConcepts;
	}

	/**
	 * @return the parent
	 */
	public ApiDocConcept getParent() {
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(ApiDocConcept parent) {
		this.parent = parent;
	}
	
	
}

