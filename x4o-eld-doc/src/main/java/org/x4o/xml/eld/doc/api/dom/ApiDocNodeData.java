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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ApiDocNodeData holds all data the is configed per node page.
 * 
 * @author Willem Cazander
 * @version 1.0 Aug 11, 2013
 */
public class ApiDocNodeData {

	private String prefixPath;
	private String navSelected = null;
	private List<String> groupTypeKeys = null;
	private Map<String,List<ApiDocNavLink>> groupTypeLinks = null;
	private String prevLink = null;
	private String nextLink = null;
	private String framePath = null;;
	private List<ApiDocNavLink> navLinks = null;
	private List<ApiDocIndexItem> indexItems = null;
	
	public ApiDocNodeData() {
		navLinks = new ArrayList<ApiDocNavLink>(12);
		groupTypeKeys = new ArrayList<String>(navLinks.size()/3);
		groupTypeLinks = new HashMap<String,List<ApiDocNavLink>>(groupTypeKeys.size());
		indexItems = new ArrayList<ApiDocIndexItem>(500);
	}
	
	public List<ApiDocIndexItem> getIndexItems() {
		return indexItems;
	}
	
	public void addIndexItem(ApiDocIndexItem indexItem) {
		indexItems.add(indexItem);
	}
	
	public void addGroupTypeKey(String groupTypeKey) {
		groupTypeKeys.add(groupTypeKey);
	}
	
	public List<String> getGroupTypeKeys() {
		return groupTypeKeys;
	}
	
	public void addGroupTypeLink(String groupTypeKey,ApiDocNavLink link) {
		List<ApiDocNavLink> result = groupTypeLinks.get(groupTypeKey);
		if (result==null) {
			result = new ArrayList<ApiDocNavLink>(10);
			groupTypeLinks.put(groupTypeKey, result);
		}
		result.add(link);
	}
	
	public List<ApiDocNavLink> getGroupTypeLinks(String groupTypeKey) {
		List<ApiDocNavLink> result = groupTypeLinks.get(groupTypeKey);
		if (result==null) {
			result = new ArrayList<ApiDocNavLink>(0);
		}
		return result;
	}
	
	public ApiDocNavLink getGroupTypeLink(String groupTypeKey,String group) {
		List<ApiDocNavLink> links = getGroupTypeLinks(groupTypeKey);
		for (ApiDocNavLink link:links) {
			if (link.getId().equals(group)) {
				return link;
			}
		}
		return null;
	}
	
	public void clearGroupTypeLinks() {
		groupTypeLinks.clear();	
	}
	
	public List<ApiDocNavLink> getNavLinks() {
		return navLinks;
	}
	
	public ApiDocNavLink getNavLinkById(String id) {
		for (ApiDocNavLink link:navLinks) {
			if (link.getId().equals(id)) {
				return link;
			}
		}
		return null;
	}
	
	public void addNavLink(ApiDocNavLink link) {
		navLinks.add(link);
	}
	
	public void removeNavLink(ApiDocNavLink link) {
		navLinks.remove(link);
	}
	
	/**
	 * @return the prefixPath
	 */
	public String getPrefixPath() {
		return prefixPath;
	}
	
	/**
	 * @param prefixPath the prefixPath to set
	 */
	public void setPrefixPath(String prefixPath) {
		this.prefixPath = prefixPath;
	}
	
	/**
	 * @return the navSelected
	 */
	public String getNavSelected() {
		return navSelected;
	}
	
	/**
	 * @param navSelected the navSelected to set
	 */
	public void setNavSelected(String navSelected) {
		this.navSelected = navSelected;
	}
	
	/**
	 * @return the prevLink
	 */
	public String getPrevLink() {
		return prevLink;
	}
	
	/**
	 * @param prevLink the prevLink to set
	 */
	public void setPrevLink(String prevLink) {
		this.prevLink = prevLink;
	}
	
	/**
	 * @return the nextLink
	 */
	public String getNextLink() {
		return nextLink;
	}
	
	/**
	 * @param nextLink the nextLink to set
	 */
	public void setNextLink(String nextLink) {
		this.nextLink = nextLink;
	}
	
	/**
	 * @return the framePath
	 */
	public String getFramePath() {
		return framePath;
	}
	
	/**
	 * @param framePath the framePath to set
	 */
	public void setFramePath(String framePath) {
		this.framePath = framePath;
	}
}
