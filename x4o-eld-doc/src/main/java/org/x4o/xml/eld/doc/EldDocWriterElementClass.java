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

import java.util.List;

import org.x4o.html.ContentWriterHtml.Tag;
import org.x4o.xml.eld.doc.EldDocXTreePageWriter.TreeNode;
import org.x4o.xml.eld.doc.api.AbstractApiDocWriter;
import org.x4o.xml.eld.doc.api.ApiDocContentWriter;
import org.x4o.xml.eld.doc.api.ApiDocNodeDataConfiguratorMethod;
import org.x4o.xml.eld.doc.api.ApiDocNodeWriterMethod;
import org.x4o.xml.eld.doc.api.dom.ApiDoc;
import org.x4o.xml.eld.doc.api.dom.ApiDocNode;
import org.x4o.xml.eld.doc.api.dom.ApiDocNodeBody;
import org.x4o.xml.eld.doc.api.dom.ApiDocNodeData;
import org.x4o.xml.eld.doc.api.dom.ApiDocWriteEvent;
import org.x4o.xml.element.ElementClass;
import org.x4o.xml.element.ElementClassAttribute;
import org.x4o.xml.element.ElementConfigurator;
import org.x4o.xml.element.ElementNamespace;
import org.x4o.xml.lang.X4OLanguage;
import org.x4o.xml.lang.X4OLanguageModule;
import org.xml.sax.SAXException;

/**
 * EldDocWriterElementClass writer all content parts for the ElementClass.
 * 
 * @author Willem Cazander
 * @version 1.0 May 29, 2013
 */
public class EldDocWriterElementClass extends AbstractApiDocWriter {
	
	@ApiDocNodeDataConfiguratorMethod(targetClasses={ElementClass.class})
	public void configNavBar(ApiDoc doc,ApiDocNode node,ApiDocNodeData data) {
		/*
		ElementClass ec  = (ElementClass)node.getUserData();
		Collection<ElementClassAttribute> list = ec.getElementClassAttributes();
		if (list.isEmpty()) {
			clearHrefContentGroupAlways(doc,"summary","attribute");
		}
		*/
		clearHrefContentGroup(doc,node,"summary","attribute",ElementClassAttribute.class);
		clearHrefContentGroup(doc,node,"summary","configurator",ElementConfigurator.class);
	}
	
	@ApiDocNodeWriterMethod(nodeBody=ApiDocNodeBody.SUMMARY,targetClasses={ElementClass.class},nodeBodyOrders={1},contentGroup="element",contentGroupType="summary")
	public void writeElementX4OSummary(ApiDocWriteEvent<ApiDocNode> event) throws SAXException {
		printApiTableBean(event, "Element", "class","id","description");
	}
	
	@ApiDocNodeWriterMethod(nodeBody=ApiDocNodeBody.SUMMARY,targetClasses={ElementClass.class},nodeBodyOrders={2},contentGroup="attribute",contentGroupType="summary")
	public void writeElementClassAttribute(ApiDocWriteEvent<ApiDocNode> event) throws SAXException {
		printApiTable(event,"Element Class Attribute Summary",ElementClassAttribute.class);
	}
	
	@ApiDocNodeWriterMethod(nodeBody=ApiDocNodeBody.SUMMARY,targetClasses={ElementClass.class},nodeBodyOrders={3},contentGroup="configurator",contentGroupType="summary")
	public void writeElementConfigurator(ApiDocWriteEvent<ApiDocNode> event) throws SAXException {
		printApiTable(event,"Element Configurator Summary",ElementConfigurator.class);
	}
	
	/*
	@ApiDocNodeWriterMethod(nodeBody=ApiDocNodeBody.SUMMARY,targetClasses={ElementClass.class},nodeBodyOrders={2},contentGroup="attribute",contentGroupType="summary")
	public void writeElementX4OAttributeSummary(ApiDocWriteEvent<ApiDocNode> event) throws SAXException {
		ElementClass ec  = (ElementClass)event.getEventObject().getUserData();
		Collection<ElementClassAttribute> list = ec.getElementClassAttributes();
		if (list.isEmpty()) {
			return;
		}
		ApiDocContentWriter writer = event.getWriter();
		writer.docTableStart("Element X4O Attributes", "All Element X4O Attributes Overview",ApiDocContentCss.overviewSummary);
		writer.docTableHeader("URI", "Name");
		for (ElementClassAttribute attr:list) {
			writer.docTableRow(attr.getId(),attr.getDescription());
		}
		writer.docTableEnd();
	}
	*/
	
	@ApiDocNodeWriterMethod(nodeBody=ApiDocNodeBody.SUMMARY,targetClasses={ElementClass.class},nodeBodyOrders={10},contentGroup="bean",contentGroupType="summary")
	public void writeElementObjectPropertiesSummary(ApiDocWriteEvent<ApiDocNode> event) throws SAXException {
		ElementClass ec  = (ElementClass)event.getEventObject().getUserData();
		Class<?> beanClass = ec.getObjectClass();
		if (beanClass==null) {
			return;
		}
		printApiTableBeanClass(event, beanClass, "Object");
	}
	
	@ApiDocNodeWriterMethod(nodeBody=ApiDocNodeBody.DESCRIPTION_LINKS,targetClasses={ElementClass.class})
	public void writeElementRelationLinks(ApiDocWriteEvent<ApiDocNode> event) throws SAXException {
		ApiDocContentWriter writer = event.getWriter();
		ElementClass ec  = (ElementClass)event.getEventObject().getUserData();
		ElementNamespace ns = (ElementNamespace)event.getEventObject().getParent().getUserData();
		X4OLanguageModule mod = (X4OLanguageModule)event.getEventObject().getParent().getParent().getUserData();
		X4OLanguage language = (X4OLanguage)event.getEventObject().getParent().getParent().getParent().getUserData();
		
		// TODO: this is hacky
		EldDocXTreePageWriter xtree = (EldDocXTreePageWriter)event.getDoc().findDocPageById("overview-xtree").getPageWriters().get(0);
		
		TreeNode node = xtree.new TreeNode();
		node.language=language;
		node.module=mod;
		node.namespace=ns;
		node.elementClass=ec;
		
		String pathPrefix = "../../../../language/";
		
		List<TreeNode> parents = xtree.findParents(node);
		writer.printTagStart(Tag.dl);
			writer.printTagCharacters(Tag.dt,"All Element Parents:");
			writer.printTagStart(Tag.dd);
				if (parents.isEmpty()) {
					writer.characters("No parent.");
				}
				for (int i=0;i<parents.size();i++) {
					TreeNode n = parents.get(i);
					String uri = toElementUri(pathPrefix, n.module, n.namespace, n.elementClass);
					writer.printHref(uri, n.namespace.getId()+":"+n.elementClass.getId());
					if (i<parents.size()-1) {
						writer.characters(",&nbsp;");
					}
				}
			writer.printTagEnd(Tag.dd);
		writer.printTagEnd(Tag.dl);
		
		List<TreeNode> childs = xtree.findChilderen(node);
		writer.printTagStart(Tag.dl);
			writer.printTagCharacters(Tag.dt,"All Element Childeren:");
			writer.printTagStart(Tag.dd);
				if (childs.isEmpty()) {
					writer.characters("No childeren.");
				}
				for (int i=0;i<childs.size();i++) {
					TreeNode n = childs.get(i);
					String uri = toElementUri(pathPrefix, n.module, n.namespace, n.elementClass);
					writer.printHref(uri, n.namespace.getId()+":"+n.elementClass.getId());
					if (i<childs.size()-1) {
						writer.characters(",&nbsp;");
					}
				}
			writer.printTagEnd(Tag.dd);
		writer.printTagEnd(Tag.dl);
	}
	
	private String toElementUri(String pathPrefix,X4OLanguageModule mod,ElementNamespace namespace,ElementClass ec) {
		StringBuilder buf = new StringBuilder(100);
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
