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
package org.x4o.xml.eld.doc.api.dom;

import java.util.ArrayList;
import java.util.List;

/**
 * ApiDocNode defines the concept impl data tree nodes for which we write documents.
 * 
 * @author Willem Cazander
 * @version 1.0 May 12, 2013
 */
public class ApiDocNode {

	private Object userData = null;
	private String id = null;
	private String name = null;
	private String description = null;
	private ApiDocNode parent = null;
	private List<ApiDocNode> nodes = null;
	
	public ApiDocNode() {
		nodes = new ArrayList<ApiDocNode>(30);
	}
	
	public ApiDocNode(Object userData,String id,String name,String description) {
		this();
		setUserData(userData);
		setId(id);
		setName(name);
		if (description==null) {
			description = name;
		}
		setDescription(description);
	}
	
	public ApiDocNode addNode(ApiDocNode node) {
		node.setParent(this);
		nodes.add(node);
		return node;
	}
	
	public boolean removeNode(ApiDocNode node) {
		return nodes.remove(node);
	}
	
	public List<ApiDocNode> getNodes() {
		return nodes;
	}
	
	/**
	 * @return the userData
	 */
	public Object getUserData() {
		return userData;
	}
	
	/**
	 * @param userData the userData to set
	 */
	public void setUserData(Object userData) {
		this.userData = userData;
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
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * @return the parent
	 */
	public ApiDocNode getParent() {
		return parent;
	}
	
	/**
	 * @param parent the parent to set
	 */
	public void setParent(ApiDocNode parent) {
		this.parent = parent;
	}
}
