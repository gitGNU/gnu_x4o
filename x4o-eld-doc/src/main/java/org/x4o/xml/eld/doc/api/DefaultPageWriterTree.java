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

import org.x4o.xml.eld.doc.api.dom.ApiDoc;
import org.x4o.xml.eld.doc.api.dom.ApiDocNode;
import org.x4o.xml.eld.doc.api.dom.ApiDocPage;
import org.x4o.xml.eld.doc.api.dom.ApiDocPageWriter;
import org.x4o.xml.eld.doc.api.dom.ApiDocWriteEvent;
import org.x4o.xml.io.sax.ext.ContentWriterHtml.Tag;
import org.xml.sax.SAXException;

/**
 * DefaultPageWriterTree creates the default tree overview page content.
 * 
 * @author Willem Cazander
 * @version 1.0 May 22, 2013
 */
public class DefaultPageWriterTree implements ApiDocPageWriter {
	
	public static ApiDocPage createDocPage() {
		return new ApiDocPage("overview-tree","Tree","Tree of api concepts.",new DefaultPageWriterTree());
	}
	
	protected ApiDocNode selectRootNode(ApiDoc doc) {
		ApiDocNode rootNode = doc.getRootNodeTreePage();
		if (rootNode==null) {
			rootNode = doc.getRootNode();
		}
		return rootNode;
	}
	
	public void writePageContent(ApiDocWriteEvent<ApiDocPage> e) throws SAXException {
		ApiDoc doc = e.getDoc();
		ApiDocPage page = e.getEventObject();
		ApiDocContentWriter writer = e.getWriter();
		//writer.docPagePackageTitle(title, "Overview Tree");
		writer.docPageContentStart();
		writeTree(doc,selectRootNode(doc),writer,"");
		writer.docPagePackageDescription(page.getName(), "Tree","All Language elements as tree.");
		writer.docPageContentEnd();
	}
	
	private void writeTree(ApiDoc doc, ApiDocNode node,ApiDocContentWriter writer,String pathPrefix) throws SAXException {
		
		for (Class<?> excludeClass:doc.getTreeNodeClassExcludes()) {
			if (excludeClass.isAssignableFrom(node.getUserData().getClass())) {
				return;
			}
		}
		
		StringBuffer buf = new StringBuffer();
		if (!doc.getRootNode().equals(node)) {
			buildParentPath(node,buf);
		}
		buf.append("index.html");
		
		String href = buf.toString();
		
		writer.printTagStart(Tag.ul);
		writer.printTagStart(Tag.li,"",null,"circle");
		if (node.getParent()!=null) {
			writer.characters(node.getParent().getId());
			writer.characters(":");
		}
		writer.printHref(href, node.getName(), node.getName(), "strong");
		writer.printTagEnd(Tag.li);
		
		for (ApiDocNode child:node.getNodes()) {
			writeTree(doc,child,writer,pathPrefix);
		}
		writer.printTagEnd(Tag.ul);
	}
	
	private void buildParentPath(ApiDocNode node,StringBuffer buf) {
		if (node.getParent()==null) {
			buf.append(ApiDocContentWriter.toSafeUri(node.getId()));
			buf.append('/');
			return;
		}
		buildParentPath(node.getParent(),buf);
		buf.append(ApiDocContentWriter.toSafeUri(node.getId()));
		buf.append('/');
	}
}
