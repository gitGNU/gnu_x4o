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
package org.x4o.xml.eld.doc.api;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.x4o.xml.eld.doc.api.dom.ApiDoc;
import org.x4o.xml.eld.doc.api.dom.ApiDocNavLink;
import org.x4o.xml.eld.doc.api.dom.ApiDocNode;
import org.x4o.xml.eld.doc.api.dom.ApiDocRemoteClass;
import org.x4o.xml.eld.doc.api.dom.ApiDocWriteEvent;
import org.xml.sax.SAXException;

/**
 * AbstractApiDocNodeWriter has some handy writer method for printing api doc html stuctures.
 * 
 * @author Willem Cazander
 * @version 1.0 Aug 11, 2013
 */
public abstract class AbstractApiDocWriter {

	public void clearHrefContentGroup(ApiDoc doc,ApiDocNode node,String groupType,String group,Class<?> filterClass) {
		boolean doClear = filterUserDataClassType(node,filterClass).isEmpty();
		if (doClear==false) {
			return;
		}
		clearHrefContentGroupAlways(doc,groupType,group);
	}
	
	public void clearHrefContentGroupAlways(ApiDoc doc,String groupType,String group) {
		ApiDocNavLink link = doc.getNodeData().getGroupTypeLink(groupType,group);
		if (link==null) {
			return;
		}
		link.setHref(null);
	}
	
	
	public void printApiTable(ApiDocWriteEvent<ApiDocNode> event,String name,Class<?> interfaceClass) throws SAXException {
		printApiTable(
				event.getEventObject(),
				filterUserDataClassType(event.getEventObject(),interfaceClass),
				event.getWriter(),
				name
		);
	}
	
	public void printApiTable(ApiDocNode parent,List<ApiDocNode> nodes,ApiDocContentWriter writer,String name) throws SAXException {
		if (nodes.isEmpty()) {
			return;
		}
		writer.docTableStart(name, "All childeren in "+name,ApiDocContentCss.overviewSummary);
		writer.docTableHeader("Name", "Description");
		for (ApiDocNode child:nodes) {
			String link = ApiDocContentWriter.toSafeUri(child.getId())+"/index.html";
			if (parent.getParent()==null) {
				link = ApiDocContentWriter.toSafeUri(parent.getId())+"/"+link; // root node
			}
			writer.docTableRowLink(link,child.getName(),child.getDescription());
		}
		writer.docTableEnd();
	}
	
	private List<ApiDocNode> filterUserDataClassType(ApiDocNode filterNode,Class<?> interfaceClass) {
		List<ApiDocNode> result = new ArrayList<ApiDocNode>(filterNode.getNodes().size()/2);
		for (ApiDocNode node:filterNode.getNodes()) {
			if (interfaceClass.isAssignableFrom(node.getUserData().getClass())) {
				result.add(node);
			}
		}
		return result;
	}
	
	public void printApiTableBean(ApiDocWriteEvent<ApiDocNode> event,String name,String...skipProperties) throws SAXException {
		printApiTableBean(event.getDoc(), event.getWriter(), event.getEventObject().getUserData(), name, skipProperties);
	}
	
	public void printApiTableBean(ApiDoc doc,ApiDocContentWriter writer,Object bean,String name,String...skipProperties) throws SAXException {
		printApiTableBeanClass(doc, writer, bean, bean.getClass(), name, skipProperties);
	}
	
	public void printApiTableBeanClass(ApiDocWriteEvent<ApiDocNode> event,Class<?> beanClass,String name,String...skipProperties) throws SAXException {
		printApiTableBeanClass(event.getDoc(), event.getWriter(), null,beanClass, name, skipProperties);
	}
	
	private void printApiTableBeanClass(ApiDoc doc,ApiDocContentWriter writer,Object bean,Class<?> beanClass,String name,String...skipProperties) throws SAXException {
		writer.docTableStart(name+" Properties", name+" properties overview.",ApiDocContentCss.overviewSummary);
		writer.docTableHeader("Name", "Value");
		for (Method m:beanClass.getMethods()) {
			if (m.getName().startsWith("get")) {
				String n = m.getName().substring(3);
				if (m.getParameterTypes().length!=0) {
					continue; // set without parameters
				}
				if (n.length()<2) {
					continue;
				}
				n = n.substring(0,1).toLowerCase()+n.substring(1,n.length());
				boolean skipNext = false;
				for (String skip:skipProperties) {
					if (n.equals(skip)) {
						skipNext = true;
						break;
					}
				}
				if (skipNext) {
					continue;
				}
				
				Object value = null;
				if (bean!=null) {
					try {
						value = m.invoke(bean, new Object[] {});
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}
				} else {
					value = m.getReturnType();
				}
				//writer.docTableRow(n,);
				writer.docTableRowLastStart(n, null);
				String c = printValue(doc,writer,value);
				if (c!=null) {
					writer.printCharacters(c);
				}
				writer.docTableRowLastEnd();
			}
		}
		writer.docTableEnd();
	}
	
	private String printValue(ApiDoc doc,ApiDocContentWriter writer,Object value) throws SAXException {
		if (value==null) {
			return "null";
		}
		if (value instanceof String) {
			return (String)value;
		}
		if (value instanceof Class) {
			Class<?> cls = (Class<?>)value;
			
			for (ApiDocRemoteClass rc:doc.getRemoteClasses()) {
				String remoteUrl = rc.getRemoteUrl(cls);
				if (remoteUrl==null) {
					continue;
				}
				writer.printHref(remoteUrl, cls.getName(), cls.getSimpleName());
				return null;
			}
			
			return "class "+cls.getName();
		}
		if (value instanceof List) {
			StringBuilder buf = new StringBuilder(100);
			buf.append("[L: ");
			List<?> l = (List<?>)value;
			if (l.isEmpty()) {
				buf.append("Empty");
			}
			for (Object o:l) {
				buf.append(""+o);
				buf.append(" ");
			}
			buf.append("]");
			return buf.toString();
		}
		if (value instanceof Object[]) {
			StringBuilder buf = new StringBuilder(100);
			buf.append("[A: ");
			Object[] l = (Object[])value;
			if (l.length==0) {
				buf.append("Empty");
			}
			for (Object o:l) {
				buf.append(""+o);
				buf.append(" ");
			}
			buf.append("]");
			return buf.toString();
		}
		
		return value.toString();
	}
}
