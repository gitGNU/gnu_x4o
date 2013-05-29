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
 * ApiDocPage defines seperate pages for the documentation.
 * 
 * @author Willem Cazander
 * @version 1.0 May 12, 2013
 */
public class ApiDocPage {
	
	private String id = null;
	private String name = null;
	private String description = null;
	private List<ApiDocPageWriter> pageWriters = null;
	
	public ApiDocPage() {
		pageWriters = new ArrayList<ApiDocPageWriter>(30);
	}
	
	public ApiDocPage(String id,String name,String description,ApiDocPageWriter...writers) {
		this();
		setId(id);
		setName(name);
		setDescription(description);
		for (ApiDocPageWriter writer:writers) {
			addPageWriter(writer);
		}
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
	 * @return the page writers.
	 */
	public List<ApiDocPageWriter> getPageWriters() {
		return pageWriters;
	}
	
	/**
	 * @param writer the writer to add.
	 */
	public void addPageWriter(ApiDocPageWriter writer) {
		pageWriters.add(writer);
	}
	
	/**
	 * @param writer the writer to add.
	 */
	public void removePageWriter(ApiDocPageWriter writer) {
		pageWriters.remove(writer);
	}
}
