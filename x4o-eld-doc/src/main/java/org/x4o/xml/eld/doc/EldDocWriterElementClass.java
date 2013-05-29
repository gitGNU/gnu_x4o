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
package org.x4o.xml.eld.doc;

import java.lang.reflect.Method;
import java.util.List;

import org.x4o.xml.eld.doc.EldDocXTreePageWriter.TreeNode;
import org.x4o.xml.eld.doc.api.ApiDocContentWriter;
import org.x4o.xml.eld.doc.api.ApiDocNodeWriterMethod;
import org.x4o.xml.eld.doc.api.dom.ApiDocNode;
import org.x4o.xml.eld.doc.api.dom.ApiDocNodeBody;
import org.x4o.xml.eld.doc.api.dom.ApiDocWriteEvent;
import org.x4o.xml.element.ElementClass;
import org.x4o.xml.element.ElementClassAttribute;
import org.x4o.xml.element.ElementNamespaceContext;
import org.x4o.xml.io.sax.ext.ContentWriterHtml.Tag;
import org.x4o.xml.lang.X4OLanguageContext;
import org.x4o.xml.lang.X4OLanguageModule;
import org.xml.sax.SAXException;

/**
 * EldDocWriterElementClass writer all content parts for the ElementClass.
 * 
 * @author Willem Cazander
 * @version 1.0 May 29, 2013
 */
public class EldDocWriterElementClass {
	
	private String printList(List<String> list) {
		StringBuffer buf = new StringBuffer(40);
		buf.append("[L: ");
		if (list.isEmpty()) {
			buf.append("Empty.");
		}
		for (String s:list) {
			buf.append(s);
			buf.append(' ');
		}
		buf.append("]");
		return buf.toString();
	}
	
	@ApiDocNodeWriterMethod(nodeBody=ApiDocNodeBody.SUMMARY,targetClasses={ElementClass.class})
	public void writeElementX4OSummary(ApiDocWriteEvent<ApiDocNode> event) throws SAXException {
		ApiDocContentWriter writer = event.getWriter();
		ElementClass ec  = (ElementClass)event.getEvent().getUserData();
		writer.docTableStart("Element X4O Properties", "Element X4O Property Overview");
		writer.docTableHeader("Name", "Value");
			writer.docTableRow("id",""+ec.getId());
			writer.docTableRow("objectClass",""+ec.getObjectClass());
			writer.docTableRow("elementClass",""+ec.getElementClass());
			writer.docTableRow("autoAttributes",""+ec.getAutoAttributes());
			writer.docTableRow("skipPhases",printList(ec.getSkipPhases()));
			writer.docTableRow("schemaContentBase",""+ec.getSchemaContentBase());
			writer.docTableRow("schemaContentComplex",""+ec.getSchemaContentComplex());
			writer.docTableRow("schemaContentMixed",""+ec.getSchemaContentMixed());
		writer.docTableEnd();
	}
	
	@ApiDocNodeWriterMethod(nodeBody=ApiDocNodeBody.SUMMARY,targetClasses={ElementClass.class})
	public void writeElementX4OAttributeSummary(ApiDocWriteEvent<ApiDocNode> event) throws SAXException {
		ApiDocContentWriter writer = event.getWriter();
		ElementClass ec  = (ElementClass)event.getEvent().getUserData();
		writer.docTableStart("Element X4O Attributes", "All Element X4O Attributes Overview");
		writer.docTableHeader("URI", "Name");
		for (ElementClassAttribute attr:ec.getElementClassAttributes()) {
			writer.docTableRow(attr.getId(),attr.getDescription());
		}
		writer.docTableEnd();
	}
	
	@ApiDocNodeWriterMethod(nodeBody=ApiDocNodeBody.SUMMARY,targetClasses={ElementClass.class})
	public void writeElementObjectPropertiesSummary(ApiDocWriteEvent<ApiDocNode> event) throws SAXException {
		ApiDocContentWriter writer = event.getWriter();
		ElementClass ec  = (ElementClass)event.getEvent().getUserData();
		Class<?> beanClass = ec.getElementClass();
		if (beanClass==null) {
			return;
		}
		writer.docTableStart("Class Properties", "Bean class properties overview.");
		writer.docTableHeader("Name", "Value");
		for (Method m:beanClass.getMethods()) {
			if (m.getName().startsWith("set")) {
				String n = m.getName().substring(3);
				if (m.getParameterTypes().length==0) {
					continue; // set without parameters
				}
				if (n.length()<2) {
					continue;
				}
				n = n.substring(0,1).toLowerCase()+n.substring(1,n.length());
				Class<?> type = m.getParameterTypes()[0]; // TODO make full list for auto attribute type resolving.
				writer.docTableRow(n,""+type);
			}
		}
		writer.docTableEnd();
	}
	
	@ApiDocNodeWriterMethod(nodeBody=ApiDocNodeBody.DESCRIPTION_LINKS,targetClasses={ElementClass.class})
	public void writeElementRelationLinks(ApiDocWriteEvent<ApiDocNode> event) throws SAXException {
		ApiDocContentWriter writer = event.getWriter();
		ElementClass ec  = (ElementClass)event.getEvent().getUserData();
		ElementNamespaceContext ns = (ElementNamespaceContext)event.getEvent().getParent().getUserData();
		X4OLanguageModule mod = (X4OLanguageModule)event.getEvent().getParent().getParent().getUserData();
		X4OLanguageContext context = (X4OLanguageContext)event.getEvent().getParent().getParent().getParent().getUserData();
		
		// TODO: this is hacky
		EldDocXTreePageWriter xtree = (EldDocXTreePageWriter)event.getDoc().findDocPageById("overview-xtree").getPageWriters().get(0);
		
		TreeNode node = xtree.new TreeNode();
		node.context=context;
		node.module=mod;
		node.namespace=ns;
		node.elementClass=ec;
		
		String pathPrefix = "../../../../language/";
		
		List<TreeNode> parents = xtree.findParents(node);
		writer.printTagStart(Tag.dl);
			writer.printTagCharacters(Tag.dt,"Element Parents:");
			writer.printTagStart(Tag.dd);
				if (parents.isEmpty()) {
					writer.characters("No parent.");
				}
				for (int i=0;i<parents.size();i++) {
					TreeNode n = parents.get(i);
					String uri = toElementUri(pathPrefix, n.module, n.namespace, n.elementClass);
					writer.printHref(uri, n.namespace.getId()+":"+n.elementClass.getId());
					if (i<parents.size()-1) {
						writer.charactersRaw(",&nbsp;");
					}
				}
			writer.printTagEnd(Tag.dd);
		writer.printTagEnd(Tag.dl);
		
		List<TreeNode> childs = xtree.findChilderen(node);
		writer.printTagStart(Tag.dl);
			writer.printTagCharacters(Tag.dt,"Element Childeren:");
			writer.printTagStart(Tag.dd);
				if (childs.isEmpty()) {
					writer.characters("No childeren.");
				}
				for (int i=0;i<childs.size();i++) {
					TreeNode n = childs.get(i);
					String uri = toElementUri(pathPrefix, n.module, n.namespace, n.elementClass);
					writer.printHref(uri, n.namespace.getId()+":"+n.elementClass.getId());
					if (i<childs.size()-1) {
						writer.charactersRaw(",&nbsp;");
					}
				}
			writer.printTagEnd(Tag.dd);
		writer.printTagEnd(Tag.dl);
	}
	
	private String toElementUri(String pathPrefix,X4OLanguageModule mod,ElementNamespaceContext namespace,ElementClass ec) {
		StringBuffer buf = new StringBuffer(100);
		if (pathPrefix!=null) {
			buf.append(pathPrefix);
		}
		buf.append(ApiDocContentWriter.toSafeUri(mod.getId()));
		buf.append("/");
		buf.append(ApiDocContentWriter.toSafeUri(namespace.getId()));
		buf.append("/");
		buf.append(ApiDocContentWriter.toSafeUri(ec.getId()));
		buf.append("/index.html");
		return buf.toString();
	}
}
