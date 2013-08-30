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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.x4o.xml.eld.doc.api.ApiDocNodeDataConfiguratorBean;
import org.x4o.xml.eld.doc.api.ApiDocNodeWriterBean;

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
	private List<String> metaKeywords = null;
	private File metaStyleSheet = null;
	private String metaStyleSheetThema = null;
	private List<ApiDocPage> docPages = null;
	private Class<?> frameNavConceptClass = null;
	private Boolean frameNavOverviewPrintParent = null;
	private Boolean frameNavPrintParentId = null;
	private Boolean frameNavPrintParent = null;
	private Boolean frameNavPrintParentParent = null;
	private List<Class<?>> treeNodeClassExcludes = null;
	private List<Class<?>> treeNodePageModeClass = null;
	private List<ApiDocNodeDataConfigurator> dataConfigurators = null;
	private List<Class<?>> annotatedClasses = null;
	private ApiDocNodeData nodeData = null;
	private String docStatsJS = null;
	private String noFrameAllName = null;
	private String noFrameAllLink = null;
	private String noFrameAllTopJS = null;
	private String noFrameAllBottomJS = null;
	private boolean fillOnce = false;
	private List<ApiDocRemoteClass> remoteClasses = null;
	private boolean skipRootTreePathNode = true;
	private boolean printConceptTitle = true;
	private boolean printConceptPrevNext = true;
	private Map<String,String> groupTypeNames = null;
	private String docPageSubTitle = null;
	
	public ApiDoc() {
		nodeBodyWriters = new ArrayList<ApiDocNodeWriter>(20);
		concepts = new ArrayList<ApiDocConcept>(10);
		metaKeywords = new ArrayList<String>(5);
		docPages = new ArrayList<ApiDocPage>(5);
		treeNodeClassExcludes = new ArrayList<Class<?>>(5);
		treeNodePageModeClass = new ArrayList<Class<?>>(5);
		dataConfigurators = new ArrayList<ApiDocNodeDataConfigurator>(5);
		annotatedClasses = new ArrayList<Class<?>>(5);
		remoteClasses = new ArrayList<ApiDocRemoteClass>(5);
		groupTypeNames = new HashMap<String,String>(3);
	}
	
	public void checkModel() throws NullPointerException,IllegalArgumentException {
		checkNull(name,"name");
		checkNull(description,"description");
		checkNull(docAbout,"docAbout");
		checkNull(docCopyright,"docCopyright");
		checkNull(rootNode,"rootNode");
		checkNull(frameNavConceptClass,"frameNavConceptClass");
		checkNull(noFrameAllName,"noFrameAllName");
		
		if (concepts.isEmpty()) {
			throw new IllegalStateException("Can't work with empty concepts");
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
		
		if (noFrameAllTopJS==null) { 
			noFrameAllTopJS =	"\nallClassesLink = document.getElementById(\"allclasses_navbar_top\");\n"+
								"if(window==top) {\n\tallClassesLink.style.display = \"block\";\n} else {\n\tallClassesLink.style.display = \"none\";\n}\n";
		}
		if (noFrameAllBottomJS==null) { 
			noFrameAllBottomJS =	"\nallClassesLink = document.getElementById(\"allclasses_navbar_bottom\");\n"+
									"if(window==top) {\n\tallClassesLink.style.display = \"block\";\n} else {\n\tallClassesLink.style.display = \"none\";\n}\n";
		}
		if (noFrameAllLink==null) {
			ApiDocConcept navConcept = findConceptByClass(getFrameNavConceptClass());
			setNoFrameAllLink("all"+navConcept.getId()+"-noframe.html");
		}
		
		fillRuntimeData();
		
		if (nodeBodyWriters.isEmpty()) {
			fillOnce = false;
			dataConfigurators.clear();
			throw new IllegalStateException("Can't work with empty nodeBodyWriters");
		}
	}
	
	private void fillRuntimeData() {
		if (fillOnce) {
			return;
		}
		setNodeData(new ApiDocNodeData());
		try {
			for (Class<?> annoClass:getAnnotatedClasses()) {
				Object bean = annoClass.newInstance();
				ApiDocNodeWriterBean.addAnnotatedNodeContentWriters(this,bean);
				ApiDocNodeDataConfiguratorBean.addAnnotatedNodeDataConfigurators(this, bean);
			}
		} catch (InstantiationException e) {
			throw new IllegalArgumentException(e);
		} catch (IllegalAccessException e) {
			throw new IllegalArgumentException(e);
		}
		for (ApiDocConcept concept:getConcepts()) {
			String navLink = "overview-"+concept.getId()+".html";
			boolean resetHref = true;
			if (concept.getParent()==null) {
				resetHref = false; // don't reset root node
			} else {
				navLink = null; // rest start with null href's
			}
			ApiDocNavLink link = new ApiDocNavLink(concept.getId(), navLink, concept.getName(),concept.getName(),resetHref);
			getNodeData().addNavLink(link);
		}
		for (ApiDocPage page:getDocPages()) {
			String navLink = page.getId()+".html";
			ApiDocNavLink link = new ApiDocNavLink(page.getId(), navLink, page.getName(),page.getName(),false);
			getNodeData().addNavLink(link);
		}
		for (ApiDocRemoteClass rc:getRemoteClasses()) {
			try {
				rc.parseRemotePackageList();
			} catch (IOException e) {
				throw new IllegalStateException("While parsing: "+rc.getDocUrl()+" got: "+e.getMessage(),e);
			}
		}
		fillOnce = true;
	}
	
	private void checkNull(Object obj,String objName) {
		if (obj==null) {
			throw new NullPointerException("Can't work with null "+objName);
		}
	}
	
	public ApiDocConcept findConceptByClass(Class<?> objClass) {
		for (ApiDocConcept concept:getConcepts()) {
			if (concept.getConceptClass().isAssignableFrom(objClass)) {
				return concept;
			}
			for (ApiDocConcept c:concept.getChildConcepts()) {
				if (c.getConceptClass().isAssignableFrom(objClass)) {
					return concept;
				}
			}
		}
		return null;
	}
	
	public ApiDocConcept findConceptChildByNode(ApiDocNode node) {
		Class<?> objClass = node.getUserData().getClass();
		Class<?> parentClass = null;
		if (node.getParent()!=null) {
			parentClass = node.getParent().getUserData().getClass();
		}
		for (ApiDocConcept concept:getConcepts()) {
			if (parentClass!=null && concept.getConceptClass().isAssignableFrom(parentClass)==false) {
				continue;
			}
			for (ApiDocConcept c:concept.getChildConcepts()) {
				if (c.getConceptClass().isAssignableFrom(objClass)) {
					return c;
				}
			}
		}
		return null;
	}
	
	public List<ApiDocRemoteClass> getRemoteClasses() {
		return remoteClasses;
	}
	
	public void addRemoteClass(ApiDocRemoteClass remoteClass) {
		remoteClasses.add(remoteClass);
	}
	
	public void removeRemoteClass(ApiDocRemoteClass remoteClass) {
		remoteClasses.add(remoteClass);
	}
	
	public List<Class<?>> getAnnotatedClasses() {
		return annotatedClasses;
	}
	
	public void removeAnnotatedClasses(Class<?> annotatedClass) {
		annotatedClasses.remove(annotatedClass);
	}
	
	public void addAnnotatedClasses(Class<?> annotatedClass) {
		annotatedClasses.add(annotatedClass);
	}
	
	public List<ApiDocNodeDataConfigurator> getDataConfigurators() {
		return dataConfigurators;
	}
	
	public void removeDataConfigurator(ApiDocNodeDataConfigurator conf) {
		dataConfigurators.remove(conf);
	}
	
	public void addDataConfigurator(ApiDocNodeDataConfigurator conf) {
		dataConfigurators.add(conf);
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
	
	public void addMetaKeyword(String keyword) {
		metaKeywords.add(keyword);
	}
	
	public void addMetaKeywordAll(Collection<String> keywords) {
		metaKeywords.addAll(keywords);
	}
	
	public boolean removeMetaKeyword(String keyword) {
		return metaKeywords.remove(keyword);
	}
	
	public List<String> getDocKeywords() {
		return metaKeywords;
	}
	
	/**
	 * @return the metaStyleSheet
	 */
	public File getMetaStyleSheet() {
		return metaStyleSheet;
	}
	
	/**
	 * @param metaStyleSheet the metaStyleSheet to set
	 */
	public void setMetaStyleSheet(File metaStyleSheet) {
		this.metaStyleSheet = metaStyleSheet;
	}
	
	/**
	 * @return the metaStyleSheetThema
	 */
	public String getMetaStyleSheetThema() {
		return metaStyleSheetThema;
	}
	
	/**
	 * @param metaStyleSheetThema the metaStyleSheetThema to set
	 */
	public void setMetaStyleSheetThema(String metaStyleSheetThema) {
		this.metaStyleSheetThema = metaStyleSheetThema;
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
	
	public Class<?> addTreeNodePageModeClass(Class<?> pageModeClass) {
		treeNodePageModeClass.add(pageModeClass);
		return pageModeClass;
	}
	
	public boolean removeTreeNodePageModeClass(Class<?> pageModeClass) {
		return treeNodePageModeClass.remove(pageModeClass);
	}
	
	public List<Class<?>> getTreeNodePageModeClasses() {
		return treeNodePageModeClass;
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
	
	/**
	 * @return the nodeData
	 */
	public ApiDocNodeData getNodeData() {
		return nodeData;
	}
	
	/**
	 * @param nodeData the nodeData to set
	 */
	public void setNodeData(ApiDocNodeData nodeData) {
		this.nodeData = nodeData;
	}
	
	/**
	 * @return the docStatsJS
	 */
	public String getDocStatsJS() {
		return docStatsJS;
	}
	
	/**
	 * @param docStatsJS the docStatsJS to set
	 */
	public void setDocStatsJS(String docStatsJS) {
		this.docStatsJS = docStatsJS;
	}

	/**
	 * @return the noFrameAllName
	 */
	public String getNoFrameAllName() {
		return noFrameAllName;
	}

	/**
	 * @param noFrameAllName the noFrameAllName to set
	 */
	public void setNoFrameAllName(String noFrameAllName) {
		this.noFrameAllName = noFrameAllName;
	}

	/**
	 * @return the noFrameAllLink
	 */
	public String getNoFrameAllLink() {
		return noFrameAllLink;
	}

	/**
	 * @param noFrameAllLink the noFrameAllLink to set
	 */
	public void setNoFrameAllLink(String noFrameAllLink) {
		this.noFrameAllLink = noFrameAllLink;
	}

	/**
	 * @return the noFrameAllTopJS
	 */
	public String getNoFrameAllTopJS() {
		return noFrameAllTopJS;
	}

	/**
	 * @param noFrameAllTopJS the noFrameAllTopJS to set
	 */
	public void setNoFrameAllTopJS(String noFrameAllTopJS) {
		this.noFrameAllTopJS = noFrameAllTopJS;
	}

	/**
	 * @return the noFrameAllBottomJS
	 */
	public String getNoFrameAllBottomJS() {
		return noFrameAllBottomJS;
	}

	/**
	 * @param noFrameAllBottomJS the noFrameAllBottomJS to set
	 */
	public void setNoFrameAllBottomJS(String noFrameAllBottomJS) {
		this.noFrameAllBottomJS = noFrameAllBottomJS;
	}

	/**
	 * @return the skipRootTreePathNode
	 */
	public boolean isSkipRootTreePathNode() {
		return skipRootTreePathNode;
	}

	/**
	 * @param skipRootTreePathNode the skipRootTreePathNode to set
	 */
	public void setSkipRootTreePathNode(boolean skipRootTreePathNode) {
		this.skipRootTreePathNode = skipRootTreePathNode;
	}
	
	/**
	 * @return the printConceptTitle
	 */
	public boolean isPrintConceptTitle() {
		return printConceptTitle;
	}
	
	/**
	 * @param printConceptTitle the printConceptTitle to set
	 */
	public void setPrintConceptTitle(boolean printConceptTitle) {
		this.printConceptTitle = printConceptTitle;
	}
	
	/**
	 * @return the printConceptPrevNext
	 */
	public boolean isPrintConceptPrevNext() {
		return printConceptPrevNext;
	}
	
	/**
	 * @param printConceptPrevNext the printConceptPrevNext to set
	 */
	public void setPrintConceptPrevNext(boolean printConceptPrevNext) {
		this.printConceptPrevNext = printConceptPrevNext;
	}
	
	public String getGroupTypeName(String groupTypeKey) {
		String result = groupTypeNames.get(groupTypeKey);
		if (result==null) {
			result = groupTypeKey;
		}
		return result;
	}
	
	public void setGroupTypeName(String groupTypeKey,String name) {
		groupTypeNames.put(groupTypeKey,name);
	}
	
	/**
	 * @return the docPageSubTitle
	 */
	public String getDocPageSubTitle() {
		return docPageSubTitle;
	}
	
	/**
	 * @param docPageSubTitle the docPageSubTitle to set
	 */
	public void setDocPageSubTitle(String docPageSubTitle) {
		this.docPageSubTitle = docPageSubTitle;
	}
}
