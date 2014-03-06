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

/**
 * ApiDocIndexItem holds data to print the index all page.
 * 
 * @author Willem Cazander
 * @version 1.0 Nov 09, 2013
 */
public class ApiDocIndexItem {

	private String linkHref = null;
	private String linkText = null;
	private String titlePostHref = null;
	private String titlePostText = null;
	
	private String title = null;
	private String description = null;
	
	public ApiDocIndexItem() {
	}
	
	/**
	 * @return the linkHref
	 */
	public String getLinkHref() {
		return linkHref;
	}
	
	/**
	 * @param linkHref the linkHref to set
	 */
	public void setLinkHref(String linkHref) {
		this.linkHref = linkHref;
	}
	
	/**
	 * @return the linkText
	 */
	public String getLinkText() {
		return linkText;
	}
	
	/**
	 * @param linkText the linkText to set
	 */
	public void setLinkText(String linkText) {
		this.linkText = linkText;
	}
	
	/**
	 * @return the titlePostHref
	 */
	public String getTitlePostHref() {
		return titlePostHref;
	}
	
	/**
	 * @param titlePostHref the titlePostHref to set
	 */
	public void setTitlePostHref(String titlePostHref) {
		this.titlePostHref = titlePostHref;
	}
	
	/**
	 * @return the titlePostText
	 */
	public String getTitlePostText() {
		return titlePostText;
	}
	
	/**
	 * @param titlePostText the titlePostText to set
	 */
	public void setTitlePostText(String titlePostText) {
		this.titlePostText = titlePostText;
	}
	
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
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
}
