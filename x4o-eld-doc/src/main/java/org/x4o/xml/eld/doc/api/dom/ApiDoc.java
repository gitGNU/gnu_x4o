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
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

/**
 * ApiDoc holds all config and data to write a full api doc structure.
 * 
 * @author Willem Cazander
 * @version 1.0 May 12, 2013
 */
public class ApiDoc {

	private String name = null;
	private String description = null;
	private ApiDocNode rootNode = null;
	private ApiDocNode rootNodeTreePage = null;
	private List<ApiDocNodeWriter> nodeBodyWriters = null;
	private List<ApiDocConcept> concepts = null;
	private String docCopyright = null;
	private String docAbout = null;
	private List<String> docKeywords = null;
	private List<ApiDocPage> docPages = null;
	private Class<?> frameNavConceptClass = null;
	private Boolean frameNavOverviewPrintParent = null;
	private Boolean frameNavPrintParentId = null;
	private Boolean frameNavPrintParent = null;
	private Boolean frameNavPrintParentParent = null;
	private List<Class<?>> treeNodeClassExcludes = null;
	
	public ApiDoc() {
		nodeBodyWriters = new ArrayList<ApiDocNodeWriter>(20);
		concepts = new ArrayList<ApiDocConcept>(10);
		docKeywords = new ArrayList<String>(5);
		docPages = new ArrayList<ApiDocPage>(5);
		treeNodeClassExcludes = new ArrayList<Class<?>>(5);
	}
	
	public void checkModel() throws NullPointerException,IllegalArgumentException {
		checkNull(name,"name");
		checkNull(description,"description");
		checkNull(docAbout,"docAbout");
		checkNull(docCopyright,"docCopyright");
		checkNull(rootNode,"rootNode");
		checkNull(frameNavConceptClass,"frameNavConceptClass");
		if (concepts.isEmpty()) {
			throw new IllegalStateException("Can't work with empty concepts");
		}
		if (nodeBodyWriters.isEmpty()) {
			throw new IllegalStateException("Can't work with empty nodeBodyWriters");
		}
		if (frameNavOverviewPrintParent==null) {
			setFrameNavOverviewPrintParent(false);
		}
		if (frameNavPrintParent==null) {
			setFrameNavPrintParent(false);
		}
		if (frameNavPrintParentParent==null) {
			setFrameNavPrintParentParent(false);
		}
		if (frameNavPrintParentId==null) {
			setFrameNavPrintParentId(false);
		}
	}
	
	private void checkNull(Object obj,String objName) {
		if (obj==null) {
			throw new NullPointerException("Can't work with null "+objName);
		}
	}
	
	public ApiDocNodeWriter addNodeBodyWriter(ApiDocNodeWriter writer) {
		nodeBodyWriters.add(writer);
		return writer;
	}
	
	public boolean removeNodeBodyWriter(ApiDocNodeWriter writer) {
		return nodeBodyWriters.remove(writer);
	}
	
	public List<ApiDocNodeWriter> getNodeBodyWriters() {
		return nodeBodyWriters;
	}
	
	public ApiDocConcept addConcept(ApiDocConcept concept) {
		concepts.add(concept);
		return concept;
	}
	
	public boolean removeConcept(ApiDocConcept concept) {
		return concepts.remove(concept);
	}
	
	public List<ApiDocConcept> getConcepts() {
		return concepts;
	}
	
	public void addDocKeyword(String keyword) {
		docKeywords.add(keyword);
	}
	
	public void addDocKeywordAll(Collection<String> keywords) {
		docKeywords.addAll(keywords);
	}
	
	public boolean removeDocKeyword(String keyword) {
		return docKeywords.remove(keyword);
	}
	
	public List<String> getDocKeywords() {
		return docKeywords;
	}
	
	/**
	 * @return the docCopyright
	 */
	public String getDocCopyright() {
		return docCopyright;
	}
	
	/**
	 * @param docCopyright the docCopyright to set
	 */
	public void setDocCopyright(String docCopyright) {
		this.docCopyright = docCopyright;
	}
	
	/**
	 * Creates default copyright message for owner.
	 * @param owner	The owner of the copyright.
	 */
	public void createDocCopyright(String owner) {
		Calendar calendar = Calendar.getInstance();
		StringBuffer buf = new StringBuffer(100);
		buf.append("Copyright&nbsp;&#x00a9;&nbsp;");
		buf.append(calendar.get(Calendar.YEAR));
		buf.append("&nbsp;");
		buf.append(owner.toUpperCase());
		buf.append("&nbsp;");
		buf.append("All Rights Reserved.");
		setDocCopyright(buf.toString());
	}
	
	/**
	 * @return the docAbout
	 */
	public String getDocAbout() {
		return docAbout;
	}
	
	/**
	 * @param docAbout the docAbout to set
	 */
	public void setDocAbout(String docAbout) {
		this.docAbout = docAbout;
	}
	
	public ApiDocPage addDocPage(ApiDocPage page) {
		docPages.add(page);
		return page;
	}
	
	public boolean removeDocPage(ApiDocPage page) {
		return docPages.remove(page);
	}
	
	public List<ApiDocPage> getDocPages() {
		return docPages;
	}
	
	public ApiDocPage findDocPageById(String docPageId) {
		if (docPageId==null) {
			throw new NullPointerException("Can't search for null id.");
		}
		for (ApiDocPage page:docPages) {
			if (page.getId().equals(docPageId)) {
				return page;
			}
		}
		return null;
	}
	
	public Class<?> addTreeNodeClassExclude(Class<?> excludeClass) {
		treeNodeClassExcludes.add(excludeClass);
		return excludeClass;
	}
	
	public boolean removeTreeNodeClassExclude(Class<?> excludeClass) {
		return treeNodeClassExcludes.remove(excludeClass);
	}
	
	public List<Class<?>> getTreeNodeClassExcludes() {
		return treeNodeClassExcludes;
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
	 * @return the rootNodeTreePage
	 */
	public ApiDocNode getRootNodeTreePage() {
		return rootNodeTreePage;
	}
	
	/**
	 * @param rootNodeTreePage the rootNodeTreePage to set
	 */
	public void setRootNodeTreePage(ApiDocNode rootNodeTreePage) {
		this.rootNodeTreePage = rootNodeTreePage;
	}
	
	/**
	 * @return the rootNode
	 */
	public ApiDocNode getRootNode() {
		return rootNode;
	}
	
	/**
	 * @param rootNode the rootNode to set
	 */
	public void setRootNode(ApiDocNode rootNode) {
		this.rootNode = rootNode;
	}
	
	/**
	 * @return the frameNavConceptClass
	 */
	public Class<?> getFrameNavConceptClass() {
		return frameNavConceptClass;
	}
	
	/**
	 * @param frameNavConceptClass the frameNavConceptClass to set
	 */
	public void setFrameNavConceptClass(Class<?> frameNavConceptClass) {
		this.frameNavConceptClass = frameNavConceptClass;
	}
	
	/**
	 * @return the frameNavPrintParent
	 */
	public Boolean getFrameNavPrintParent() {
		return frameNavPrintParent;
	}
	
	/**
	 * @param frameNavPrintParent the frameNavPrintParent to set
	 */
	public void setFrameNavPrintParent(Boolean frameNavPrintParent) {
		this.frameNavPrintParent = frameNavPrintParent;
	}
	
	/**
	 * @return the frameNavPrintParentParent
	 */
	public Boolean getFrameNavPrintParentParent() {
		return frameNavPrintParentParent;
	}
	
	/**
	 * @param frameNavPrintParentParent the frameNavPrintParentParent to set
	 */
	public void setFrameNavPrintParentParent(Boolean frameNavPrintParentParent) {
		this.frameNavPrintParentParent = frameNavPrintParentParent;
	}
	
	/**
	 * @return the frameNavOverviewPrintParent
	 */
	public Boolean getFrameNavOverviewPrintParent() {
		return frameNavOverviewPrintParent;
	}
	
	/**
	 * @param frameNavOverviewPrintParent the frameNavOverviewPrintParent to set
	 */
	public void setFrameNavOverviewPrintParent(Boolean frameNavOverviewPrintParent) {
		this.frameNavOverviewPrintParent = frameNavOverviewPrintParent;
	}
	
	/**
	 * @return the frameNavPrintParentId
	 */
	public Boolean getFrameNavPrintParentId() {
		return frameNavPrintParentId;
	}
	
	/**
	 * @param frameNavPrintParentId the frameNavPrintParentId to set
	 */
	public void setFrameNavPrintParentId(Boolean frameNavPrintParentId) {
		this.frameNavPrintParentId = frameNavPrintParentId;
	}
}
