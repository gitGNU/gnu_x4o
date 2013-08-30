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
package org.x4o.xml.eld.doc.api;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.x4o.xml.eld.doc.api.dom.ApiDoc;
import org.x4o.xml.eld.doc.api.dom.ApiDocConcept;
import org.x4o.xml.eld.doc.api.dom.ApiDocNavLink;
import org.x4o.xml.eld.doc.api.dom.ApiDocNode;
import org.x4o.xml.eld.doc.api.dom.ApiDocNodeBody;
import org.x4o.xml.eld.doc.api.dom.ApiDocNodeData;
import org.x4o.xml.eld.doc.api.dom.ApiDocNodeDataConfigurator;
import org.x4o.xml.eld.doc.api.dom.ApiDocWriteEvent;
import org.x4o.xml.eld.doc.api.dom.ApiDocNodeWriter;
import org.x4o.xml.eld.doc.api.dom.ApiDocPage;
import org.x4o.xml.eld.doc.api.dom.ApiDocPageWriter;
import org.x4o.xml.io.XMLConstants;
import org.x4o.xml.io.sax.ext.ContentWriterHtml.DocType;
import org.x4o.xml.io.sax.ext.ContentWriterHtml.Tag;
import org.x4o.xml.lang.X4OLanguageClassLoader;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * ApiDocWriter creates all output files for the ApiDoc.
 * 
 * @author Willem Cazander
 * @version 1.0 May 1, 2013
 */
public class ApiDocWriter extends AbstractApiDocWriter {

	private ApiDoc doc = null;
	private File basePath = null;
	
	/**
	 * Writes full api doc tree files to the base path.
	 * @param doc	The ApiDoc to writer.
	 * @param basePath	The bath path to write into.
	 * @throws IOException	When file exception happens.
	 * @throws SAXException when xml exception happens.
	 */
	public void write(ApiDoc doc,File basePath) throws IOException,SAXException {
		if (doc==null) {
			throw new NullPointerException("Can't write with null ApiDoc.");
		}
		if (basePath==null) {
			throw new NullPointerException("Can't write with null basePath.");
		}
		doc.checkModel();
		this.doc=doc;
		this.basePath=basePath;
		
		// Write root and resource files
		writeStyleSheet();
		writeIndex();
		writeOverviewFrame();
		writeAllFrameNav(true);
		writeAllFrameNav(false);
		
		// Write pages
		for (ApiDocPage page:doc.getDocPages()) {
			writePage(page);
		}
		
		// Write api doc tree
		writeNode(doc.getRootNode());
	}
	
	private void writeNode(ApiDocNode node) throws SAXException {
		ApiDocConcept concept = doc.findConceptByClass(node.getUserData().getClass());
		if (concept==null) {
			concept = doc.findConceptChildByNode(node);
		}
		if (concept==null) {
			throw new IllegalStateException("No concept found for: "+node.getId()+" data: "+node.getUserData());
		}
		List<String> path = new ArrayList<String>(10);
		buildParentPath(node,path);
		if (path.size()==1) {
			path.remove(path.size()-1);
			path.add("overview-"+node.getId()+".html");
		} else {
			path.add("index.html");
		}
		String pathPrefix = "";
		for (int i=1;i<path.size();i++) {
			pathPrefix += "../";
		}
		File outputFile = createOutputPathFile(basePath,path.toArray(new String[]{}));
		ApiDocContentWriter writer = createContentWriter(outputFile);
		
		doc.getNodeData().clearGroupTypeLinks();
		doc.getNodeData().setNavSelected(concept.getId());
		configNodeData(pathPrefix,outputFile);
		configActiveNavConceptLinks(node,concept,"/..");
		configNextPrevLinks(node);
		configSubNavLinks(node);
		configData(node);
		
		ApiDocWriteEvent<ApiDocNode> bodyEvent = new ApiDocWriteEvent<ApiDocNode>(doc,writer,node);
		String titleNode = node.getName();
		String titleNodeSub = null;
		if (node.getParent()!=null) {
			titleNodeSub = node.getParent().getId()+":"+node.getId();
		}
		boolean isNodePageMode = isNodePageMode(node);
		String titleContent = titleNode;
		if (doc.isPrintConceptTitle()) {
			String conceptTitle = concept.getName();
			ApiDocConcept childConcept = doc.findConceptChildByNode(node);
			if (childConcept!=null) {
				conceptTitle = childConcept.getName();
			}
			titleContent = conceptTitle +" "+titleNode;
		}
		String titleHtml = titleNode;
		if (doc.getDocPageSubTitle()!=null) {
			titleHtml = titleNode+" ("+doc.getDocPageSubTitle()+")";
			if (node.getParent()==null) {
				titleContent = doc.getDocPageSubTitle();
				titleHtml = "Overview ("+doc.getDocPageSubTitle()+")";
			}
		}
		
		// Write node file
		writer.docHtmlStart(titleHtml, doc.getDocKeywords(),doc.getNodeData().getPrefixPath());
		docNavBar(writer,true,concept,node);
			if (isNodePageMode) {
				writer.docPageClassStart(titleContent, null,Tag.h1);
			} else {
				writer.docPageClassStart(titleContent, titleNodeSub,Tag.h2);	
			}
				
				writer.docPageContentStart();
					if (!isNodePageMode) {
						writeNodeTreePath(bodyEvent);
					}
					writeNodeDescription(bodyEvent,isNodePageMode);
					writeNodeSummary(bodyEvent,isNodePageMode);
					writeNodeDetails(bodyEvent);
				writer.docPageContentEnd();
			writer.docPageClassEnd();
		docNavBar(writer,false,concept,node);
		writer.docHtmlEnd(doc.getDocCopyright(),doc.getDocStatsJS());
		writer.closeWriterSafe();
		
		// Writer other files
		writeAllFrameNavNode(node);
		for (ApiDocNode child:node.getNodes()) {
			writeNode(child);
		}
	}
	
	private boolean isNodePageMode(ApiDocNode node) {
		for (Class<?> pageModeClass:doc.getTreeNodePageModeClasses()) {
			if (pageModeClass.isAssignableFrom(node.getUserData().getClass())) {
				return true;
			}
		}
		return false;
	}
	
	private void writeNodeTreePath(ApiDocWriteEvent<ApiDocNode> event) throws SAXException {
		List<ApiDocNodeWriter> bodyWriterTreePath = findNodeBodyWriters(event.getEventObject(),ApiDocNodeBody.TREE_PATH);
		if (bodyWriterTreePath.isEmpty()) {
			defaultWriteTreePath(event.getEventObject(),event.getWriter());
		}
		for (int i=0;i<bodyWriterTreePath.size();i++) {
			ApiDocNodeWriter nodeWriter = bodyWriterTreePath.get(i);
			nodeWriter.writeNodeContent(event);
		}
	}
	
	private void defaultWriteNodeDescription(ApiDocWriteEvent<ApiDocNode> event) throws SAXException {
		event.getWriter().characters(event.getEventObject().getDescription());
	}
	
	private void writeNodeDescription(ApiDocWriteEvent<ApiDocNode> event,boolean isPageMode) throws SAXException {
		ApiDocContentWriter writer = event.getWriter();
		List<ApiDocNodeWriter> bodyWriterDescriptionLinks = findNodeBodyWriters(event.getEventObject(),ApiDocNodeBody.DESCRIPTION_LINKS);
		List<ApiDocNodeWriter> bodyWriterDescriptionNode = findNodeBodyWriters(event.getEventObject(),ApiDocNodeBody.DESCRIPTION_NODE);
		writer.printTagStart(Tag.div, ApiDocContentCss.description);
			writer.docPageBlockStart();
			if (isPageMode==false) {
				if (bodyWriterDescriptionLinks.isEmpty()) {
					//defaultWriteTreePath(node,writer);
				}
				for (int i=0;i<bodyWriterDescriptionLinks.size();i++) {
					ApiDocNodeWriter nodeWriter = bodyWriterDescriptionLinks.get(i);
					nodeWriter.writeNodeContent(event);
				}
				writer.printTagStartEnd(Tag.hr);
				writer.printTagStartEnd(Tag.br); // mmm
			}
			if (bodyWriterDescriptionNode.isEmpty()) {
				defaultWriteNodeDescription(event);
			}
			for (int i=0;i<bodyWriterDescriptionNode.size();i++) {
				ApiDocNodeWriter nodeWriter = bodyWriterDescriptionNode.get(i);
				nodeWriter.writeNodeContent(event);
			}		
			writer.docPageBlockEnd();
		writer.printTagEnd(Tag.div); // description
	}
	
	private void writeNodeSummary(ApiDocWriteEvent<ApiDocNode> event,boolean isPageMode) throws SAXException {
		ApiDocContentWriter writer = event.getWriter();
		List<ApiDocNodeWriter> bodyWriterSummary = findNodeBodyWriters(event.getEventObject(),ApiDocNodeBody.SUMMARY);
		if (!isPageMode) {
			writer.printTagStart(Tag.div, ApiDocContentCss.summary);
			writer.docPageBlockStart();
		}
			if (bodyWriterSummary.isEmpty() && event.getEventObject().getNodes().isEmpty()==false) {
				if (!isPageMode) { writer.docPageBlockStart(); }
				defaultWriteSummary(event.getEventObject(),writer);
				if (!isPageMode) { writer.docPageBlockEnd(); }
			}
			for (int i=0;i<bodyWriterSummary.size();i++) {
				ApiDocNodeWriter nodeWriter = bodyWriterSummary.get(i);
				if (!isPageMode) { writer.docPageBlockStart(); }
				writeSubNavNamedHref(event,nodeWriter);
				if (!isPageMode) { writer.printTagCharacters(Tag.h3, "Summary"); }
				nodeWriter.writeNodeContent(event);
				if (!isPageMode) { writer.docPageBlockEnd(); }
				if (isPageMode) { writer.printTagStartEnd(Tag.br); } // mm .. mm
			}
		if (!isPageMode) {
			writer.docPageBlockEnd();
			writer.printTagEnd(Tag.div); // Summary
		}
	}
	
	private void writeSubNavNamedHref(ApiDocWriteEvent<ApiDocNode> event,ApiDocNodeWriter writer) throws SAXException {
		String group = writer.getContentGroup();
		String groupTypeKey = writer.getContentGroupType();
		if (group==null | groupTypeKey==null) {
			return;
		}
		event.getWriter().printHrefNamed(groupTypeKey+"_"+group);
	}
	
	private void writeNodeDetails(ApiDocWriteEvent<ApiDocNode> event) throws SAXException {
		ApiDocContentWriter writer = event.getWriter();
		List<ApiDocNodeWriter> bodyWriterDetail = findNodeBodyWriters(event.getEventObject(),ApiDocNodeBody.DETAIL);
		if (bodyWriterDetail.isEmpty()) {
			return;// no default ..
		}
		writer.printTagStart(Tag.div, ApiDocContentCss.details);
			writer.docPageBlockStart();
			//if (bodyWriterDetail.isEmpty()) {
			//	writer.docPageBlockStart();
			//	defaultWriteDetailNode(node,writer);
			//	writer.docPageBlockEnd();
			//}
			for (int i=0;i<bodyWriterDetail.size();i++) {
				ApiDocNodeWriter nodeWriter = bodyWriterDetail.get(i);
				writer.docPageBlockStart();
				writeSubNavNamedHref(event,nodeWriter);
				writer.printTagCharacters(Tag.h3, "Detail");
				nodeWriter.writeNodeContent(event);
				writer.docPageBlockEnd();
			}
			writer.docPageBlockEnd();
		writer.printTagEnd(Tag.div); // details

	}
	
	protected void configNodeData(String prefixPath,File frame) throws SAXException {
		ApiDocNodeData conf = doc.getNodeData();
		String framePath = null;
		try {
			String rootPath = new File(frame.getParentFile().getPath()+File.separatorChar+prefixPath).getCanonicalPath();
			framePath = frame.getPath().substring(rootPath.length()+1);
		} catch (IOException e) {
			throw new SAXException(e);
		}
		conf.setPrefixPath(prefixPath);
		conf.setFramePath(framePath);
		
		// Reset hrefs
		for (ApiDocNavLink link:doc.getNodeData().getNavLinks()) {
			if (link.isResetHref()) {
				link.setHref(null);
			}
		}
	}
	
	private void configActiveNavConceptLinks(ApiDocNode node,ApiDocConcept concept,String prefix) {
		List<String> pathClean = new ArrayList<String>(10);
		buildParentPath(node,pathClean);
		if (concept.getParent()!=null && !concept.getParent().getId().equals(doc.getRootNode().getId())) {
			ApiDocConcept conceptParent = concept.getParent();
			ApiDocNavLink link = doc.getNodeData().getNavLinkById(conceptParent.getId());
			
			link.setHref(ApiDocContentWriter.toSafeUri(pathClean)+prefix+"/index.html");
			link.setTitle(node.getParent().getId());
			configActiveNavConceptLinks(node,concept.getParent(),prefix+"/..");
		}
	}
	
	private void configNextPrevLinks(ApiDocNode node) {
		doc.getNodeData().setPrevLink(null);
		doc.getNodeData().setNextLink(null);
		if (node.getParent()==null) {
			return;
		}
		List<ApiDocNode> pn = node.getParent().getNodes();
		int pnSize = pn.size();
		int nodeIdx = pn.indexOf(node);
		if (nodeIdx>0) {
			List<String> pathClean = new ArrayList<String>(10);
			ApiDocNode prevNode = pn.get(nodeIdx-1);
			if (node.getUserData().getClass().equals(prevNode.getUserData().getClass())) {
				buildParentPath(prevNode,pathClean);
				doc.getNodeData().setPrevLink(ApiDocContentWriter.toSafeUri(pathClean)+"/index.html");
			}
		}
		if ((nodeIdx+1)<pnSize) {
			List<String> pathClean = new ArrayList<String>(10);
			ApiDocNode nextNode = pn.get(nodeIdx+1);
			if (node.getUserData().getClass().equals(nextNode.getUserData().getClass())) {
				buildParentPath(nextNode,pathClean);
				doc.getNodeData().setNextLink(ApiDocContentWriter.toSafeUri(pathClean)+"/index.html");
			}
		}
	}

	private void configData(ApiDocNode node) {
		ApiDocNodeData confData = doc.getNodeData();
		for (ApiDocNodeDataConfigurator conf:findDataConfigurators(node)) {
			conf.configNodeData(doc, node,confData);
		}
	}
	
	private void configSubNavLinks(ApiDocNode node) {
		ApiDocNodeData conf = doc.getNodeData();
		for (ApiDocNodeWriter writer:findNodeBodyWriters(node, null)) {
			String group = writer.getContentGroup();
			String groupTypeKey = writer.getContentGroupType();
			if (group==null | groupTypeKey==null) {
				continue;
			}
			if (conf.getGroupTypeKeys().contains(groupTypeKey)==false) {
				conf.addGroupTypeKey(groupTypeKey);
			}
			String groupTypeName = doc.getGroupTypeName(groupTypeKey);
			String groupName = group.substring(0,1).toUpperCase()+group.substring(1);
			ApiDocNavLink link = new ApiDocNavLink();
			link.setId(group);
			link.setHref("#"+groupTypeKey+"_"+group);
			link.setText(groupName);
			link.setTitle(groupTypeName+" "+groupName);
			conf.addGroupTypeLink(groupTypeKey, link);
		}
	}
	
	public void defaultWriteSummary(ApiDocNode node,ApiDocContentWriter writer) throws SAXException {
		ApiDocConcept concept = doc.findConceptByClass(node.getUserData().getClass());
		printApiTable(node, node.getNodes(), writer, concept.getName()+" Summary");
	}
	
	public void defaultWriteTreePath(ApiDocNode node,ApiDocContentWriter writer) throws SAXException {
		if (node.getParent()==null) {
			return; // no tree for root
		}
		List<ApiDocNode> rootPath = new ArrayList<ApiDocNode>(8);
		defaultWriteTreePathBuildPath(node,rootPath);
		defaultWriteTreePathWalker(rootPath.iterator(),writer,rootPath.size());
	}
	
	private void defaultWriteTreePathWalker(Iterator<ApiDocNode> nodes,ApiDocContentWriter writer,int linkPrefixCount) throws SAXException {
		if (nodes.hasNext()==false) {
			return;
		} 
		ApiDocNode node = nodes.next();
		writer.printTagStart(Tag.ul, ApiDocContentCss.inheritance);
		String nodeTitle = node.getId();
		if (nodes.hasNext()==false) {
			writer.printTagStart(Tag.li);
			writer.characters(nodeTitle);
			writer.printTagEnd(Tag.li);
		} else {
			writer.printTagStart(Tag.li);
			StringBuffer buf = new StringBuffer(20);
			for (int i=1;i<linkPrefixCount;i++) {
				buf.append("../");
			}
			String linkHref = buf+"index.html";
			if (doc.getRootNode().equals(node)) {
				ApiDocConcept concept = doc.findConceptByClass(node.getUserData().getClass());
				linkHref = buf+"../overview-"+concept.getId()+".html";
			}
			writer.printHref(linkHref, node.getDescription(), nodeTitle);
			writer.printTagEnd(Tag.li);
			
			writer.printTagStart(Tag.li);
			defaultWriteTreePathWalker(nodes,writer,(linkPrefixCount-1));
			writer.printTagEnd(Tag.li);
		}
		writer.printTagEnd(Tag.ul);
	}
	
	private void defaultWriteTreePathBuildPath(ApiDocNode node,List<ApiDocNode> result) {
		if (node.getParent()!=null) {
			defaultWriteTreePathBuildPath(node.getParent(),result);
		} else if (doc.isSkipRootTreePathNode()) {
			return;
		}
		result.add(node);
	}
	
	private ApiDocContentWriter createContentWriter(File outputFile) throws SAXException {
		String encoding = XMLConstants.XML_DEFAULT_ENCODING;
		try {
			Writer out = new OutputStreamWriter(new FileOutputStream(outputFile), encoding);
			ApiDocContentWriter result = new ApiDocContentWriter(out,encoding);
			result.getPropertyConfig().setProperty(ApiDocContentWriter.OUTPUT_CHAR_NEWLINE, XMLConstants.CHAR_NEWLINE+"");
			result.getPropertyConfig().setProperty(ApiDocContentWriter.OUTPUT_CHAR_TAB, "  ");
			return result;
		} catch (UnsupportedEncodingException e) {
			throw new SAXException(e);
		} catch (SecurityException e) {
			throw new SAXException(e);
		} catch (FileNotFoundException e) {
			throw new SAXException(e);
		}
	}
	
	private List<ApiDocNodeWriter> findNodeBodyWriters(ApiDocNode node,ApiDocNodeBody nodeBody) {
		List<ApiDocNodeWriter> result = new ArrayList<ApiDocNodeWriter>();
		final Class<?> objClass = node.getUserData().getClass();
		for (ApiDocNodeWriter writer:doc.getNodeBodyWriters()) {
			if (nodeBody!=null && !nodeBody.equals(writer.getNodeBody())) {
				continue;
			}
			for (Class<?> c:writer.getTargetClasses()) {
				if (c.isAssignableFrom(objClass)) {
					result.add(writer);
				}
			}
		}
		Collections.sort(result, new Comparator<ApiDocNodeWriter>() {
			public int compare(ApiDocNodeWriter o1, ApiDocNodeWriter o2) {
				int index1 = -1;
				int index2 = -1;
				for (int i=0;i<o1.getTargetClasses().size();i++) {
					Class<?> c = o1.getTargetClasses().get(i);
					if (c.isAssignableFrom(objClass)) {
						index1 = i;
						break;
					}
				}
				for (int i=0;i<o2.getTargetClasses().size();i++) {
					Class<?> c = o2.getTargetClasses().get(i);
					if (c.isAssignableFrom(objClass)) {
						index1 = i;
						break;
					}
				}
				// TODO: note check return value if are oke in order..
				if (index1==-1 && index2==-1) {
					return 0;
				}
				if (index1==-1) {
					return 1;
				}
				if (index2==-1) {
					return -1;
				}
				int orderValue1 = o1.getNodeBodyOrders().get(index1);
				int orderValue2 = o2.getNodeBodyOrders().get(index2);
				if (orderValue1==orderValue2) {
					return 0;
				}
				if (orderValue1 > orderValue2) {
					return -1;
				}
				return 1;
			}
		});
		return result;
	}

	private List<ApiDocNodeDataConfigurator> findDataConfigurators(ApiDocNode node) {
		List<ApiDocNodeDataConfigurator> result = new ArrayList<ApiDocNodeDataConfigurator>();
		final Class<?> objClass = node.getUserData().getClass();
		for (ApiDocNodeDataConfigurator conf:doc.getDataConfigurators()) {
			for (Class<?> c:conf.getTargetClasses()) {
				if (c.isAssignableFrom(objClass)) {
					result.add(conf);
				}
			}
		}
		return result;
	}
	
	private void buildParentPath(ApiDocNode node,List<String> path) {
		if (node.getParent()==null) {
			path.add(node.getId());
			return;
		}
		buildParentPath(node.getParent(),path);
		path.add(node.getId());
	}
	
	private void writeStyleSheet() throws IOException {
		try {
			if (doc.getMetaStyleSheet()!=null) {
				copyStreamToFile(new FileInputStream(doc.getMetaStyleSheet()),basePath,"resources","stylesheet.css");
				return;
			}
			String thema = doc.getMetaStyleSheetThema();
			if (thema==null) {
				thema = "jdk7";
			}
			List<String> cssResources = new ArrayList<String>(10);
			cssResources.add("org/x4o/xml/eld/doc/theme/base/api-html.css");
			cssResources.add("org/x4o/xml/eld/doc/theme/base/api-layout.css");
			cssResources.add("org/x4o/xml/eld/doc/theme/base/api-inset.css");
			cssResources.add("org/x4o/xml/eld/doc/theme/base/api-font.css");
			cssResources.add("org/x4o/xml/eld/doc/theme/base/api-color.css");
			
			if ("jdk6".equals(thema)) {
				cssResources.add("org/x4o/xml/eld/doc/theme/jdk6/stylesheet.css");
				writeStyleSheetResources(cssResources);
				return;
			}
			if ("jdk7".equals(thema)) {
				cssResources.add("org/x4o/xml/eld/doc/theme/jdk7/stylesheet.css");
				writeStyleSheetResources(cssResources);
				copyResourceToFile("org/x4o/xml/eld/doc/theme/jdk7/background.png",basePath,"resources","background.png");
				copyResourceToFile("org/x4o/xml/eld/doc/theme/jdk7/tab.png",basePath,"resources","tab.png");
				copyResourceToFile("org/x4o/xml/eld/doc/theme/jdk7/titlebar_end.png",basePath,"resources","titlebar_end.png");
				copyResourceToFile("org/x4o/xml/eld/doc/theme/jdk7/titlebar.png",basePath,"resources","titlebar.png");
				return;
			}
			if ("jdk7-todo".equals(thema)) {
				cssResources.add("org/x4o/xml/eld/doc/theme/jdk7-todo/stylesheet.css");
				writeStyleSheetResources(cssResources);
				copyResourceToFile("org/x4o/xml/eld/doc/theme/jdk7/background.png",basePath,"resources","background.png");
				copyResourceToFile("org/x4o/xml/eld/doc/theme/jdk7/tab.png",basePath,"resources","tab.png");
				copyResourceToFile("org/x4o/xml/eld/doc/theme/jdk7/titlebar_end.png",basePath,"resources","titlebar_end.png");
				copyResourceToFile("org/x4o/xml/eld/doc/theme/jdk7/titlebar.png",basePath,"resources","titlebar.png");
			}
		} catch (SecurityException e) {
			throw new IOException(e.getMessage());
		} catch (InterruptedException e) {
			throw new IOException(e.getMessage());
		}
	}
	
	private void writeStyleSheetResources(List<String> resources) throws IOException, SecurityException, InterruptedException {
		StringBuffer cssData = new StringBuffer();
		for (String cssResource:resources) {
			appendResourceToBuffer(cssData,cssResource);
		}
		String css = cssData.toString();
		css = css.replaceAll("\\s+"," ");
		css = css.replaceAll("\\s*:\\s*",":");
		css = css.replaceAll("\\s*\\;\\s*",";");
		css = css.replaceAll("\\s*\\,\\s*",",");
		css = css.replaceAll("\\s*\\{\\s*","{");
		css = css.replaceAll("\\s*\\}\\s*","}\n"); // add return to have multi line file. 
		
		writeFileString(css,basePath,"resources","stylesheet.css");
	}
	
	private void writeHeader(ApiDocContentWriter writer,String resourcePrefix,String title) throws SAXException {
		writer.printTagStart(Tag.head);
			writer.docCommentGenerated();
			writer.printHeadMetaContentType();
			writer.printHeadTitle(title);
			writer.printHeadMetaDate();
			writer.printHeadLinkCss(resourcePrefix+"resources/stylesheet.css");
		writer.printTagEnd(Tag.head);
	}
	
	private static final String FRAME_JS = "\n"+
			"targetPage = \"\" + window.location.search;\n"+
			"if (targetPage != \"\" && targetPage != \"undefined\")\n"+
			"\t { targetPage = targetPage.substring(1); }\n"+
			"if (targetPage.indexOf(\":\") != -1)\n"+
			"\t { targetPage = \"undefined\"; }\n"+
			"function loadFrames() {\n"+
			"\tif (targetPage != \"\" && targetPage != \"undefined\")\n"+
			"\t\t { top."+ApiDocContentCss.frameContent.name()+".location = top.targetPage; }\n"+
			"}\n";
		
	public void writeIndex() throws SAXException {
		File outputFile = createOutputPathFile(basePath,"index.html");
		ApiDocContentWriter writer = createContentWriter(outputFile);
		try {
			writer.printDocType(DocType.HTML_4_FRAMESET);
			writer.comment("NewPage");
			writer.printHtmlStart("en");
			writeHeader(writer,"",doc.getName());
			writer.printScriptInline(FRAME_JS);
			
			AttributesImpl atts = new AttributesImpl();
			atts.addAttribute ("", "cols", "", "", "20%,80%");
			atts.addAttribute ("", "title", "", "", "Documentation frame");
			atts.addAttribute ("", "onload", "", "", "top.loadFrames()");
			writer.printTagStart(Tag.frameset, atts);
			
			ApiDocConcept navConcept = doc.findConceptByClass(doc.getFrameNavConceptClass());
			atts = new AttributesImpl();
			atts.addAttribute ("", "rows", "", "", "30%,70%");
			atts.addAttribute ("", "title", "", "", "Left frames");
			atts.addAttribute ("", "onload", "", "", "top.loadFrames()");
			writer.printTagStart(Tag.frameset, atts);
				atts = new AttributesImpl();
				atts.addAttribute ("", "src", "", "", "overview-frame.html");
				atts.addAttribute ("", "title", "", "", "All Namspaces");
				atts.addAttribute ("", "name", "", "", ApiDocContentCss.frameNavOverview.name());
				writer.printTagStart(Tag.frame, atts);
				writer.printTagEnd(Tag.frame);
				atts = new AttributesImpl();
				atts.addAttribute ("", "src", "", "", "all"+navConcept.getId()+"-frame.html");
				atts.addAttribute ("", "title", "", "", "All Elements");
				atts.addAttribute ("", "name", "", "", ApiDocContentCss.frameNavDetail.name());
				writer.printTagStart(Tag.frame, atts);
				writer.printTagEnd(Tag.frame);
			writer.printTagEnd(Tag.frameset);
			
			String rootLink = "overview-"+ApiDocContentWriter.toSafeUri(doc.getRootNode().getId())+".html";
			
			atts = new AttributesImpl();
			atts.addAttribute ("", "src", "", "", rootLink);
			atts.addAttribute ("", "title", "", "", "All Language Components");
			atts.addAttribute ("", "name", "", "", ApiDocContentCss.frameContent.name());
			atts.addAttribute ("", "scrolling", "", "", "yes");
			writer.printTagStart(Tag.frame, atts);
			writer.printTagEnd(Tag.frame);
			
			writer.printTagStart(Tag.noframes);
				writer.printScriptNoDiv();
				writer.printTagCharacters(Tag.h2, "Frame Alert");
				writer.printTagStart(Tag.p);
					writer.characters("This document is designed to be viewed using the frames feature. If you see this message, you are using a non-frame-capable web client. Link to ");
					writer.printHref(rootLink, "Non-frame version");
					writer.characters(".");
				writer.printTagEnd(Tag.p);
			writer.printTagEnd(Tag.noframes);
			
			writer.printTagEnd(Tag.frameset);
			writer.printHtmlEnd();
		} finally {
			writer.closeWriterSafe();
		}
	}
	
	private void findNodeByUserDataClass(ApiDocNode node,Class<?> userDataClass,List<ApiDocNode> result) {
		if (userDataClass.isAssignableFrom(node.getUserData().getClass())) {
			result.add(node);
		}
		for (ApiDocNode child:node.getNodes()) {
			findNodeByUserDataClass((ApiDocNode)child,userDataClass,result);
		}
	}
	
	public void writeOverviewFrame() throws SAXException {
		ApiDocConcept concept = doc.findConceptByClass(doc.getFrameNavConceptClass());
		ApiDocConcept conceptParent = concept.getParent();
		List<ApiDocNode> nodes = new ArrayList<ApiDocNode>(50);
		findNodeByUserDataClass(doc.getRootNode(),conceptParent.getConceptClass(),nodes);
		
		File outputFile = createOutputPathFile(basePath,"overview-frame.html");
		ApiDocContentWriter writer = createContentWriter(outputFile);
		try {
			String conceptPlural = concept.getName()+"s";
			String conceptParentPlural = conceptParent.getName()+"s";
			
			writer.printDocType(DocType.HTML_4_TRANSITIONAL);
			writer.comment("NewPage");
			writer.printHtmlStart("en");
			writeHeader(writer,"","All "+conceptPlural+" of "+doc.getName());
			writer.printTagStart(Tag.body);
			
			writer.printTagStart(Tag.div,ApiDocContentCss.indexHeader);
			writer.printHrefTarget("all"+concept.getId()+"-frame.html", "All "+conceptPlural,ApiDocContentCss.frameNavDetail.name());
			writer.printTagEnd(Tag.div);
			
			writer.printTagStart(Tag.div,ApiDocContentCss.indexContainer);
			writer.printTagCharacters(Tag.h2, conceptParentPlural);
			writer.printTagStart(Tag.ul);
			for (ApiDocNode node:nodes) {
				String linkName = node.getName();
				if (doc.getFrameNavOverviewPrintParent()) {
					if (doc.getFrameNavPrintParentId()) {
						linkName = node.getParent().getId()+":"+node.getName();
					} else {
						linkName = node.getParent().getName()+":"+node.getName();
					}
				}
				writer.printTagStart(Tag.li);
				writer.printHrefTarget(doc.getRootNode().getId()+"/"+ApiDocContentWriter.toSafeUri(node.getParent().getId())+"/"+node.getId()+"/"+node.getId()+"-frame.html", linkName,ApiDocContentCss.frameNavDetail.name());
				writer.printTagEnd(Tag.li);
			}
			writer.printTagEnd(Tag.ul);
			writer.printTagEnd(Tag.div);
			
			writer.printTagEnd(Tag.body);
			writer.printHtmlEnd();
		} finally {
			writer.closeWriterSafe();
		}
	}
	
	public void writeAllFrameNav(boolean isFrame) throws SAXException {
		ApiDocConcept concept = doc.findConceptByClass(doc.getFrameNavConceptClass());
		if (isFrame) {
			writeAllFrameNav("",true,null,"all"+concept.getId()+"-frame.html");
		} else {
			writeAllFrameNav("",false,null,"all"+concept.getId()+"-noframe.html");
		}
	}
	
	private void writeAllFrameNavNode(ApiDocNode node) throws SAXException {
		ApiDocConcept concept = doc.findConceptByClass(doc.getFrameNavConceptClass());
		ApiDocConcept conceptParent = concept.getParent();
		if (!conceptParent.getConceptClass().isAssignableFrom(node.getUserData().getClass())) {
			return; // only frame nav nodes.
		}
		List<String> path = new ArrayList<String>(10);
		buildParentPath(node,path);
		String pathS = "";
		for (int i=0;i<path.size();i++) {
			pathS = "../"+pathS;
		}
		path.add(node.getId()+"-frame.html");
		writeAllFrameNav(pathS,true,node,path.toArray(new String[]{}));
	}
	
	private void writeAllFrameNav(String pathPrefix,boolean isFrame,ApiDocNode searchNode,String...fileName) throws SAXException {
		ApiDocConcept concept = doc.findConceptByClass(doc.getFrameNavConceptClass());
		//ApiDocConcept conceptParent = concept.getParent();
		List<ApiDocNode> nodes = new ArrayList<ApiDocNode>(50);
		findNodeByUserDataClass(doc.getRootNode(),concept.getConceptClass(),nodes);
		
		File outputFile = createOutputPathFile(basePath,fileName);
		ApiDocContentWriter writer = createContentWriter(outputFile);
		try {
			String conceptPlural = concept.getName()+"s";
			//String conceptParentPlural = conceptParent.getName()+"s";
			
			writer.printDocType(DocType.HTML_4_TRANSITIONAL);
			writer.comment("NewPage");
			writer.printHtmlStart("en");
			writeHeader(writer,pathPrefix,"All "+conceptPlural+" of "+doc.getName());
			writer.printTagStart(Tag.body);
			if (searchNode==null) {
				writer.printTagCharacters(Tag.h1, "All "+conceptPlural, "bar");
			} else {
				writer.printTagStart(Tag.h1,ApiDocContentCss.bar);
				writer.printHrefTarget("index.html", searchNode.getId(),ApiDocContentCss.frameContent.name());
				writer.printTagEnd(Tag.h1);
			}
			
			writer.printTagStart(Tag.div,ApiDocContentCss.indexContainer);
			writer.printTagStart(Tag.ul);
			
			boolean printParent = new Boolean(true).equals(doc.getFrameNavPrintParent());
			boolean printParentParent = new Boolean(true).equals(doc.getFrameNavPrintParentParent());
			
			for (ApiDocNode node:nodes) {
				List<String> nodePath = new ArrayList<String>(10);
				buildParentPath(node,nodePath);
				if (searchNode!=null) {
					// TODO: compare full tree paths.
					if (!node.getParent().getId().equals(searchNode.getId())) {
						continue;
					}
					if (searchNode.getParent()!=null && !node.getParent().getParent().getId().equals(searchNode.getParent().getId())) {
						continue;
					}
					if (searchNode.getParent().getParent()!=null && !node.getParent().getParent().getParent().getId().equals(searchNode.getParent().getParent().getId())) {
						continue;
					}
				}
				
				String linkName = node.getName();
				String linkUrl = ApiDocContentWriter.toSafeUri(nodePath)+"/index.html";
				if (searchNode!=null) {
					linkUrl = ApiDocContentWriter.toSafeUri(node.getId(),"index.html");
				}
				if (printParent) {
					if (printParentParent) {
						if (doc.getFrameNavPrintParentId()) {
							linkName = node.getParent().getParent().getId()+":"+linkName;
						} else {
							linkName = node.getParent().getParent().getName()+":"+linkName;
						}
					} else {
						if (doc.getFrameNavPrintParentId()) {
							linkName = node.getParent().getId()+":"+linkName;
						} else {
							linkName = node.getParent().getName()+":"+linkName;
						}
					}
				}
				writer.printTagStart(Tag.li);
				if (isFrame) {
					writer.printHrefTarget(linkUrl, linkName,ApiDocContentCss.frameContent.name());
				} else {
					writer.printHref(linkUrl, linkName);
				}
				writer.printTagEnd(Tag.li);
			}
			
			writer.printTagEnd(Tag.ul);
			writer.printTagEnd(Tag.div);
			
			writer.printTagEnd(Tag.body);
			writer.printHtmlEnd();
		} finally {
			writer.closeWriterSafe();
		}
	}
	
	private void writePage(ApiDocPage page) throws SAXException {
		File outputFile = createOutputPathFile(basePath,page.getId()+".html");
		ApiDocContentWriter writer = createContentWriter(outputFile);
		String pathPrefix = "";
		try {
			configNodeData(pathPrefix,outputFile);
			doc.getNodeData().setNavSelected(page.getId());
			String title = page.getName();
			writer.docHtmlStart(title, doc.getDocKeywords(),doc.getNodeData().getPrefixPath());
			docNavBar(writer,true,null,null);
			writer.docPageClassStart(title, page.getDescription(),Tag.h1);
			
			ApiDocWriteEvent<ApiDocPage> e = new ApiDocWriteEvent<ApiDocPage>(doc,writer,page);
			
			//writer.docPageContentStart();
			for (ApiDocPageWriter pageWriter:page.getPageWriters()) {
				pageWriter.writePageContent(e);
			}
			//writer.docPageContentEnd();
			
			writer.docPageClassEnd();
			docNavBar(writer,false,null,null);
			writer.docHtmlEnd(doc.getDocCopyright(),doc.getDocStatsJS());
		} finally {
			writer.closeWriterSafe();
		}
	}
	
	private void appendResourceToBuffer(StringBuffer buffer,String resource) throws IOException {
		ClassLoader cl = X4OLanguageClassLoader.getClassLoader();
		BufferedReader reader = null;
		try {
			InputStream inputStream = cl.getResourceAsStream(resource);
			reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
			for (int c = reader.read(); c != -1; c = reader.read()) {
				buffer.append((char)c);
			}
		} finally {
			if (reader!=null) {
				reader.close();
			}
		}
	}
	
	private void writeFileString(String text,File basePath,String...argu) throws SecurityException, IOException, InterruptedException {
		OutputStream outputStream = new FileOutputStream(createOutputPathFile(basePath,argu));
		try {
			for (int i=0;i<text.length();i++) {
				char c = text.charAt(i);
				outputStream.write(c);
				if (Thread.interrupted()) {
					throw new InterruptedException();
				}
			}	
		} finally {
			outputStream.close();
		}
	}
	
	private void copyResourceToFile(String resource,File basePath,String...argu) throws SecurityException, IOException, InterruptedException {
		ClassLoader cl = X4OLanguageClassLoader.getClassLoader();
		InputStream inputStream = cl.getResourceAsStream(resource);
		copyStreamToFile(inputStream,basePath,argu);
	}
	
	private void copyStreamToFile(InputStream inputStream,File basePath,String...argu) throws SecurityException, IOException, InterruptedException {
		
		OutputStream outputStream = new FileOutputStream(createOutputPathFile(basePath,argu));
		try {
			byte[] buffer = new byte[4096];
			int len = inputStream.read(buffer);
			while (len != -1) {
				outputStream.write(buffer, 0, len);
				len = inputStream.read(buffer);
				if (Thread.interrupted()) {
					throw new InterruptedException();
				}
			}	
		} finally {
			inputStream.close();
			outputStream.close();
		}
	}
	
	private File createOutputPathFile(File basePath,String...argu) {
		StringBuffer buf = new StringBuffer(200);
		buf.append(basePath.getAbsolutePath());
		buf.append(File.separatorChar);
		for (int i=0;i<argu.length-1;i++) {
			buf.append(ApiDocContentWriter.toSafeUri(argu[i]));
			buf.append(File.separatorChar);
		}
		File outputPath = new File(buf.toString());
		if (outputPath.exists()==false) {
			//System.out.println("Creating path: "+outputPath);
			outputPath.mkdirs();
		}
		buf.append(File.separatorChar);
		buf.append(ApiDocContentWriter.toSafeUri(argu[argu.length-1]));
		File outputFile = new File(buf.toString());
		if (outputFile.exists()) {
			outputFile.delete();
		}
		//System.out.println("Creating file: "+outputFile);
		return outputFile;
	}
	
	private void docNavBar(ApiDocContentWriter writer,boolean isTop,ApiDocConcept concept,ApiDocNode node) throws SAXException {
		ApiDocNodeData conf = doc.getNodeData();
		String pathPrefix = conf.getPrefixPath();
		String barComment = "TOP";
		String barCssDiv = "topNav";
		String barId = "navbar_top";
		if (isTop==false) {
			barComment = "BOTTOM";
			barCssDiv = "bottomNav";
			barId = "navbar_bottom";
		}
		writer.comment("========= START OF "+barComment+" NAVBAR =======");
		
		writer.printTagStart(Tag.div,barCssDiv);
		writer.printHrefNamed(barId);		// Print named link navigation
			AttributesImpl atts = new AttributesImpl();
			atts.addAttribute ("", "href", "", "", "#skip-"+barId);
			atts.addAttribute ("", "title", "", "", "Skip navigation links");
			writer.startElement("", "a", "", atts);
			writer.endElement("", "a", "");
			writer.printHrefNamed(barId+"_firstrow");
			
			atts = new AttributesImpl();// Print nav bar
			atts.addAttribute ("", "class", "", "", "navList");
			atts.addAttribute ("", "title", "", "", "Navigation");
			writer.startElement("", "ul", "", atts);
			
			for (ApiDocNavLink navLink:conf.getNavLinks()) {
				String selectedCss = null;
				String href = navLink.getHref();
				if (navLink.getId().equals(conf.getNavSelected())) {
					selectedCss = "navBarCell1Rev";
					href = null; // disables link
				}
				String navTitle = navLink.getTitle();
				if (navTitle==null) {
					navTitle = navLink.getText();
				}
				if (href==null) {
					writer.printTagCharacters(Tag.li, navLink.getText(), selectedCss);
				} else {
					docNavBarListItemHref(writer,pathPrefix+href,navTitle,navLink.getText(),selectedCss,null,null);
				}
			}
			writer.endElement("", "ul", "");
			
			writer.docNavBarAbout(doc.getDocAbout());

		writer.printTagEnd(Tag.div); // end barCssDiv
		
		writer.printTagStart(Tag.div,ApiDocContentCss.subNav);
			writer.printTagStart(Tag.ul,ApiDocContentCss.navList);
				String postFix = "";
				if (concept!=null && doc.isPrintConceptPrevNext()) {
					String conceptTitle = concept.getName();
					ApiDocConcept childConcept = doc.findConceptChildByNode(node);
					if (childConcept!=null) {
						conceptTitle = childConcept.getName();
					}
					postFix = " "+conceptTitle;
				}
				if (conf.getPrevLink()==null) {
					writer.printTagCharacters(Tag.li, "Prev");
				} else {
					docNavBarListItemHref(writer,pathPrefix+conf.getPrevLink(),"Previous Item","Prev"+postFix,null,"strong",null);
				}
				if (conf.getNextLink()==null) {
					writer.printTagCharacters(Tag.li, "Next");
				} else {
					docNavBarListItemHref(writer,pathPrefix+conf.getNextLink(),"Next Item","Next"+postFix,null,"strong",null);
				}
			writer.printTagEnd(Tag.ul);
			if (conf.getFramePath()!=null) {
				writer.printTagStart(Tag.ul,ApiDocContentCss.navList);
					writer.printTagStart(Tag.li);
						writer.printHrefTarget(pathPrefix+"index.html?"+conf.getFramePath(), "Frames", "_top");
					writer.printTagEnd(Tag.li);
					writer.printTagStart(Tag.li);
						writer.printHrefTarget(pathPrefix+conf.getFramePath(), "No Frames", "_top");
					writer.printTagEnd(Tag.li);
				writer.printTagEnd(Tag.ul);
			}
			if (doc.getNoFrameAllName()!=null && doc.getNoFrameAllLink()!=null) {
				writer.printTagStart(Tag.ul,ApiDocContentCss.navList,"allclasses_"+barId);
					docNavBarListItemHref(writer,pathPrefix+doc.getNoFrameAllLink(),doc.getNoFrameAllName(),doc.getNoFrameAllName(),null,null,null);
				writer.printTagEnd(Tag.ul);
				writer.printTagStart(Tag.div);
					if (isTop) {
						writer.printScriptInline(doc.getNoFrameAllTopJS());
					} else {
						writer.printScriptInline(doc.getNoFrameAllBottomJS());
					}
				writer.printTagEnd(Tag.div);
			}
			
			String tabSpace = "&nbsp;|&nbsp;";
			List<String> groupKeys = conf.getGroupTypeKeys();
			boolean printLink = groupKeys.isEmpty()==false;
			if (printLink) {
				writer.printTagStart(Tag.div);
				writer.printTagStart(Tag.ul,ApiDocContentCss.subNavList);
				for (int i=0;i<groupKeys.size();i++) {
					String groupKey = groupKeys.get(i);
					String groupName = doc.getGroupTypeName(groupKey);
					List<ApiDocNavLink> links = conf.getGroupTypeLinks(groupKey);
					if (links.isEmpty()==false) {
						writer.printTagStart(Tag.li);writer.characters(groupName+":&nbsp;");writer.printTagEnd(Tag.li);
						for (int l=0;l<links.size();l++) {
							ApiDocNavLink link = links.get(l);
							writer.printTagStart(Tag.li);
							String tab = null;
							if (l<links.size()-1) {
								tab = tabSpace;
							}
							if (link.getHref()!=null) {
								docNavBarListItemHref(writer,link.getHref(), link.getTitle(), link.getText(), null, null, tab);
							} else {
								writer.characters(link.getText());
								if (tab!=null) {
									writer.characters(tab);
								}
							}
							
							writer.printTagEnd(Tag.li);
						}
					}
				}
				writer.printTagEnd(Tag.ul);
				writer.printTagEnd(Tag.div);
			}
			
			writer.printHrefNamed("skip-"+barId);
			writer.printTagEnd(Tag.div);
		writer.comment("========= END OF "+barComment+" NAVBAR =======");
	}
	
	private void docNavBarListItemHref(ApiDocContentWriter writer,String href,String title,String text,String cssClass,String spanCss,String linkSpace) throws SAXException {
		writer.printTagStart(Tag.li,cssClass);
		writer.printHref(href,title,text,spanCss);
		writer.characters(linkSpace);
		writer.printTagEnd(Tag.li);
	}
}
