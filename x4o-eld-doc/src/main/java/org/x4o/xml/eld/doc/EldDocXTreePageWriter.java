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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.x4o.html.ContentWriterHtml.Tag;
import org.x4o.xml.eld.doc.api.ApiDocContentWriter;
import org.x4o.xml.eld.doc.api.DefaultPageWriterTree;
import org.x4o.xml.eld.doc.api.dom.ApiDoc;
import org.x4o.xml.eld.doc.api.dom.ApiDocNode;
import org.x4o.xml.eld.doc.api.dom.ApiDocPage;
import org.x4o.xml.eld.doc.api.dom.ApiDocPageWriter;
import org.x4o.xml.eld.doc.api.dom.ApiDocWriteEvent;
import org.x4o.xml.element.ElementBindingHandler;
import org.x4o.xml.element.ElementClass;
import org.x4o.xml.element.ElementInterface;
import org.x4o.xml.element.ElementNamespace;
import org.x4o.xml.lang.X4OLanguage;
import org.x4o.xml.lang.X4OLanguageModule;
import org.xml.sax.SAXException;

/**
 * EldDocXTreePageWriter for dom overview of tree but as seperate page.
 * 
 * @author Willem Cazander
 * @version 1.0 May 29, 2013
 */
public class EldDocXTreePageWriter extends DefaultPageWriterTree implements ApiDocPageWriter {
	
	public static ApiDocPage createDocPage() {
		return new ApiDocPage("overview-xtree","XTree","XTree of dom elements.",new EldDocXTreePageWriter());
	}
	
	// TODO: rm this old tree code;
	private void walkTree(TreeNode node,ApiDocContentWriter writer,String pathPrefix) throws SAXException {
		String href = toElementUri(pathPrefix,node.module,node.namespace,node.elementClass);
		
		writer.printTagStart(Tag.ul);
		writer.printTagStart(Tag.li,"",null,"circle");
		writer.printCharacters(node.namespace.getId());
		writer.printCharacters(":");
		writer.printHref(href, node.elementClass.getId(), node.elementClass.getId(), "strong");
		writer.printTagEnd(Tag.li);
		
		List<TreeNode> childs = findChilderen(node);
		for (TreeNode child:childs) {
			walkTree(child,writer,pathPrefix);
		}
		writer.printTagEnd(Tag.ul);
	}
	
	/**
	 * TODO: remove this
	 * @see org.x4o.xml.eld.doc.api.DefaultPageWriterTree#writePageContent(org.x4o.xml.eld.doc.api.dom.ApiDocWriteEvent)
	 */
	@Override
	public void writePageContent(ApiDocWriteEvent<ApiDocPage> e) throws SAXException {
		//selectRootNode(e.getDoc()); // create
		ApiDoc doc = e.getDoc();
		X4OLanguage language = (X4OLanguage)doc.getRootNode().getUserData();
		
		String pathPrefix = "language/";
		
		// temp print old way
		List<TreeNode> rootNodes = new ArrayList<TreeNode>(3);
		for (X4OLanguageModule mod:language.getLanguageModules()) {
			for (ElementNamespace ns:mod.getElementNamespaces()) {
				if (ns.getLanguageRoot()!=null && ns.getLanguageRoot()) {
					// found language root elements.
					for (ElementClass ec:ns.getElementClasses()) {
						TreeNode node = new TreeNode();
						node.language=language;
						node.module=mod;
						node.namespace=ns;
						node.elementClass=ec;
						rootNodes.add(node);
					}
				}
			}
		}
		Collections.sort(rootNodes,new TreeNodeComparator());
		for (TreeNode rootNode:rootNodes) {
			walkTree(rootNode,e.getWriter(),pathPrefix);
		}
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


	/**
	 * Overrided to select the dom view of the tree.
	 * @see org.x4o.xml.eld.doc.api.DefaultPageWriterTree#selectRootNode(org.x4o.xml.eld.doc.api.dom.ApiDoc)
	 */
	@Override
	protected ApiDocNode selectRootNode(ApiDoc doc) {
		try {
			return createXTree(doc);
		} catch (SAXException e) {
			throw new IllegalStateException("Could not create XTree for: "+doc.getName()+" error: "+e.getMessage(),e);
		}
	}
	
	private ApiDocNode createXTree(ApiDoc doc) throws SAXException {
		
		X4OLanguage language = (X4OLanguage)doc.getRootNode().getUserData();
		ApiDocNode root = new ApiDocNode(language,"root","Root","Language root");
		
		List<TreeNode> rootNodes = new ArrayList<TreeNode>(3);
		for (X4OLanguageModule mod:language.getLanguageModules()) {
			for (ElementNamespace ns:mod.getElementNamespaces()) {
				if (ns.getLanguageRoot()!=null && ns.getLanguageRoot()) {
					// found language root elements.
					for (ElementClass ec:ns.getElementClasses()) {
						TreeNode node = new TreeNode();
						node.language=language;
						node.module=mod;
						node.namespace=ns;
						node.elementClass=ec;
						rootNodes.add(node);
					}
				}
			}
		}
		Collections.sort(rootNodes,new TreeNodeComparator());
		for (TreeNode rootNode:rootNodes) {
			walkTree(rootNode,"../");
		}
		
		
		return root;
	}
	
	private void walkTree(TreeNode node,String pathPrefix) throws SAXException {
		//String href = toElementUri(pathPrefix,node.module,node.namespace,node.elementClass);
		List<TreeNode> childs = findChilderen(node);
		for (TreeNode child:childs) {
			walkTree(child,pathPrefix);
		}
	}
	
	
	class TreeNode {
		X4OLanguage language;
		X4OLanguageModule module;
		ElementNamespace namespace;
		ElementClass elementClass;
		TreeNode parent;
		int indent = 0;
	}
	
	public List<TreeNode> findChilderen(TreeNode node) {
		List<TreeNode> result = new ArrayList<TreeNode>(10);
		
		if (node.indent>20) {
			return result; // hard fail limit
		}
		for (X4OLanguageModule mod:node.language.getLanguageModules()) {
			for (ElementNamespace ns:mod.getElementNamespaces()) {
				for (ElementClass ec:ns.getElementClasses()) {
					TreeNode n=null;
					List<String> tags = ec.getElementParents(node.namespace.getUri());
					if (tags!=null && tags.contains(node.elementClass.getId())) {
						n = new TreeNode();
						n.language=node.language;
						n.module=mod;
						n.namespace=ns;
						n.elementClass=ec;
						n.indent=node.indent+1;
						n.parent=node;
					} else {
						if (ec.getObjectClass()==null) {
							continue;
						}
						// Check interfaces of parent , and see if child tag is there.
						for (ElementInterface ei:node.language.findElementInterfaces(ec.getObjectClass())) {
							List<String> eiTags = ei.getElementParents(node.namespace.getUri());
							if (eiTags!=null && eiTags.contains(node.elementClass.getId())) {
								n = new TreeNode();
								n.language=node.language;
								n.module=mod;
								n.namespace=ns;
								n.elementClass=ec;
								n.indent=node.indent+1;
								n.parent=node;
								break;
							}
						}
						
						if (node.elementClass.getObjectClass()==null) {
							continue;
						}
						List<ElementBindingHandler> binds = node.language.findElementBindingHandlers(node.elementClass.getObjectClass(), ec.getObjectClass());
						if (binds.isEmpty()==false) {
							n = new TreeNode();
							n.language=node.language;
							n.module=mod;
							n.namespace=ns;
							n.elementClass=ec;
							n.indent=node.indent+1;
							n.parent=node;
						}
					}
					if (n!=null && isInTree(node,n)==false) {
						result.add(n);
					}
				}
			}
		}
		Collections.sort(result,new TreeNodeComparator());
		return result;
	}
	
	private boolean isInTree(TreeNode node,TreeNode checkNode) {
		
		if (	node.namespace.getUri().equals(checkNode.namespace.getUri()) &&
				node.elementClass.getId().equals(checkNode.elementClass.getId())
			) {
			return true;
		}
		if (node.parent!=null) {
			return isInTree(node.parent,checkNode);
		}
		return false;
	}
	
	public List<TreeNode> findParents(TreeNode node) {
		List<TreeNode> result = new ArrayList<TreeNode>(10);
		TreeNode n=null;
		for (X4OLanguageModule mod:node.language.getLanguageModules()) {
			for (ElementNamespace ns:mod.getElementNamespaces()) {
				
				List<String> tags = node.elementClass.getElementParents(ns.getUri());
				if (tags!=null) {
					for (ElementClass ec:ns.getElementClasses()) {
						if (tags.contains(ec.getId())) {
							n = new TreeNode();
							n.language=node.language;
							n.module=mod;
							n.namespace=ns;
							n.elementClass=ec;
							n.indent=node.indent+1;
							n.parent=node;
							result.add(n);
						}
					}
				}
				for (ElementClass ec:ns.getElementClasses()) {

					// Check interfaces of parent , and see if child tag is there.
					if (node.elementClass.getObjectClass()!=null) {
						for (ElementInterface ei:node.language.findElementInterfaces(node.elementClass.getObjectClass())) {
							List<String> eiTags = ei.getElementParents(ns.getUri());
							if (eiTags!=null && eiTags.contains(ec.getId())) {
								n = new TreeNode();
								n.language=node.language;
								n.module=mod;
								n.namespace=ns;
								n.elementClass=ec;
								n.indent=node.indent+1;
								n.parent=node;
								result.add(n);
								break;
							}
						}
					}
					if (ec.getObjectClass()==null) {
						continue;
					}
					if (node.elementClass.getObjectClass()==null) {
						continue;
					}
					List<ElementBindingHandler> binds = node.language.findElementBindingHandlers(ec.getObjectClass(),node.elementClass.getObjectClass());
					if (binds.isEmpty()==false) {
						n = new TreeNode();
						n.language=node.language;
						n.module=mod;
						n.namespace=ns;
						n.elementClass=ec;
						n.indent=node.indent+1;
						n.parent=node;
						if (isInTree(node,n)==false) {
							result.add(n);
						}
					}
				}
			}
		}
		Collections.sort(result,new TreeNodeComparator());
		return result;
	}
	
	class TreeNodeComparator implements Comparator<TreeNode> {
		public int compare(TreeNode o1,TreeNode o2) {
			return o1.elementClass.getId().compareTo(o2.elementClass.getId());
		}
	}
}
