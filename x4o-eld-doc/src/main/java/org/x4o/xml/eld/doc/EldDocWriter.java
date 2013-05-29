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

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.x4o.xml.eld.doc.api.ApiDocContentWriter;
import org.x4o.xml.eld.doc.api.ApiDocNodeWriterBean;
import org.x4o.xml.eld.doc.api.ApiDocNodeWriterMethod;
import org.x4o.xml.eld.doc.api.ApiDocWriter;
import org.x4o.xml.eld.doc.api.DefaultPageWriterHelp;
import org.x4o.xml.eld.doc.api.DefaultPageWriterIndexAll;
import org.x4o.xml.eld.doc.api.DefaultPageWriterTree;
import org.x4o.xml.eld.doc.api.dom.ApiDoc;
import org.x4o.xml.eld.doc.api.dom.ApiDocConcept;
import org.x4o.xml.eld.doc.api.dom.ApiDocNode;
import org.x4o.xml.eld.doc.api.dom.ApiDocNodeBody;
import org.x4o.xml.eld.doc.api.dom.ApiDocWriteEvent;
import org.x4o.xml.element.ElementAttributeHandler;
import org.x4o.xml.element.ElementBindingHandler;
import org.x4o.xml.element.ElementClass;
import org.x4o.xml.element.ElementConfigurator;
import org.x4o.xml.element.ElementInterface;
import org.x4o.xml.element.ElementException;
import org.x4o.xml.element.ElementNamespaceContext;
import org.x4o.xml.lang.X4OLanguageModule;
import org.x4o.xml.lang.X4OLanguageContext;
import org.xml.sax.SAXException;

/**
 * EldDocWriter writes the x4o eld documentation.
 * 
 * @author Willem Cazander
 * @version 1.0 Aug 26, 2010
 */
public class EldDocWriter {
	
	private X4OLanguageContext context = null;
	private static final String[] C_CONTEXT		= {"language","Overview","All language modules.","The loaded language modules.."};
	private static final String[] C_MODULE 		= {"module","Module","The Language Modules.","The language is build by the modules and provides the namespaces."};
	private static final String[] C_NAMESPACE	= {"namespace","Namespace","The Language Namespace.","The language namespace holds all the xml elements."};
	private static final String[] C_ELEMENT		= {"element","Element","The Language Element.","The xml language element description."};
	
	/**
	 * Creates an EldDocGenerator for this langauge context.
	 * @param context	The language context to generate doc for.
	 */
	public EldDocWriter(X4OLanguageContext context) {
		this.context=context;
	}
	
	/**
	 * Writes the language documentation to the base path.
	 * @param basePath	The path to write to documentation to.
	 * @throws ElementException	Is thrown when error is done.
	 */
	public void writeDoc(File basePath) throws ElementException {
		try {
			ApiDocWriter writer = new ApiDocWriter();
			ApiDoc doc = buildLanguageDoc();
			doc.checkModel();
			writer.write(doc, basePath);
		} catch (SAXException e) {
			throw new ElementException(e);
		} catch (IOException e) {
			throw new ElementException(e);
		}
	}
	
	private ApiDoc buildLanguageDoc() {
		ApiDoc doc = new ApiDoc();
		doc.setName("X4O ELD DOC");
		doc.setDescription("X4O Meta Language Description");
		doc.setDocAbout(createLanguageAbout());
		doc.setDocCopyright(createLanguageCopyright());
		doc.addDocKeywordAll(createLanguageKeywords());
		doc.setFrameNavPrintParent(true);
		doc.setFrameNavPrintParentId(true);
		
		doc.setFrameNavConceptClass(ElementClass.class);
		ApiDocNodeWriterBean.addAnnotatedNodeContentWriters(doc,new EldDocWriterLanguage());
		ApiDocNodeWriterBean.addAnnotatedNodeContentWriters(doc,new EldDocWriterElementClass());
		ApiDocNodeWriterBean.addAnnotatedNodeContentWriters(doc,this);
		
		ApiDocConcept adc1 = doc.addConcept(new ApiDocConcept(null,C_CONTEXT,X4OLanguageContext.class));
		ApiDocConcept adc2 = doc.addConcept(new ApiDocConcept(adc1,C_MODULE,X4OLanguageModule.class,
				ElementAttributeHandler.class,ElementConfigurator.class,ElementInterface.class,ElementBindingHandler.class));
		ApiDocConcept adc3 = doc.addConcept(new ApiDocConcept(adc2,C_NAMESPACE,ElementNamespaceContext.class));
		doc.addConcept(new ApiDocConcept(adc3,C_ELEMENT,ElementClass.class));
		
		doc.addDocPage(EldDocXTreePageWriter.createDocPage());
		doc.addDocPage(DefaultPageWriterTree.createDocPage());
		doc.addDocPage(DefaultPageWriterIndexAll.createDocPage());
		doc.addDocPage(DefaultPageWriterHelp.createDocPage());
		
		ApiDocNode rootNode = new ApiDocNode(context,"language","Language","The X4O Language");
		doc.setRootNode(rootNode);
		for (X4OLanguageModule mod:context.getLanguage().getLanguageModules())			{	ApiDocNode modNode = rootNode.addNode(createNodeLanguageModule(mod));
			for (ElementBindingHandler bind:mod.getElementBindingHandlers())			{		modNode.addNode(createNodeElementBindingHandler(bind));			}
			for (ElementAttributeHandler attr:mod.getElementAttributeHandlers())		{		modNode.addNode(createNodeElementAttributeHandler(attr));		}
			for (ElementConfigurator conf:mod.getElementConfiguratorGlobals())			{		modNode.addNode(createNodeElementConfigurator(conf));			}
			for (ElementInterface iface:mod.getElementInterfaces())						{		ApiDocNode ifaceNode = modNode.addNode(createNodeElementInterface(iface));
				for (ElementBindingHandler bind:iface.getElementBindingHandlers())		{			ifaceNode.addNode(createNodeElementBindingHandler(bind));	}
				for (ElementConfigurator conf:iface.getElementConfigurators())			{			ifaceNode.addNode(createNodeElementConfigurator(conf));	}	}
			for (ElementNamespaceContext ns:mod.getElementNamespaceContexts())			{		ApiDocNode nsNode = modNode.addNode(createNodeElementNamespaceContext(ns));
				for (ElementClass ec:ns.getElementClasses())							{			ApiDocNode ecNode = nsNode.addNode(createNodeElementClass(ec));
					for (ElementConfigurator conf:ec.getElementConfigurators())			{				ecNode.addNode(createNodeElementConfigurator(conf));	}	}	}
		}
		return doc;
	}
	
	private ApiDocNode createNodeElementBindingHandler(ElementBindingHandler bind) {
		return new ApiDocNode(bind,bind.getId(),bind.getId(),bind.getDescription());
	}
	private ApiDocNode createNodeElementAttributeHandler(ElementAttributeHandler attr) {
		return new ApiDocNode(attr,attr.getId(),attr.getId(),attr.getDescription());
	}
	private ApiDocNode createNodeElementConfigurator(ElementConfigurator conf) {
		return new ApiDocNode(conf,conf.getId(),conf.getId(),conf.getDescription());
	}
	private ApiDocNode createNodeLanguageModule(X4OLanguageModule mod) {
		return new ApiDocNode(mod,mod.getId(),mod.getId(),mod.getDescription());
	}
	private ApiDocNode createNodeElementInterface(ElementInterface iface) {
		return new ApiDocNode(iface,iface.getId(),iface.getId(),iface.getDescription());
	}
	private ApiDocNode createNodeElementNamespaceContext(ElementNamespaceContext ns) {
		return new ApiDocNode(ns,ns.getId(),ns.getUri(),ns.getDescription());
	}
	private ApiDocNode createNodeElementClass(ElementClass ec) {
		return new ApiDocNode(ec,ec.getId(),ec.getId(),ec.getDescription());
	}
	
	private String createLanguageAbout() {
		StringBuffer buf = new StringBuffer(100);
		buf.append("XML X4O Language\n");
		buf.append(context.getLanguage().getLanguageName().toUpperCase());
		buf.append("&trade;&nbsp;");
		buf.append(context.getLanguage().getLanguageVersion());
		return buf.toString();
	}
	
	private String createLanguageCopyright() {
		Calendar calendar = Calendar.getInstance();
		StringBuffer buf = new StringBuffer(100);
		buf.append("Copyright&nbsp;&#x00a9;&nbsp;");
		buf.append(calendar.get(Calendar.YEAR));
		buf.append("&nbsp;");
		buf.append(context.getLanguage().getLanguageName().toUpperCase());
		buf.append("&nbsp;");
		buf.append("All Rights Reserved.");
		return buf.toString();
	}
	
	private List<String> createLanguageKeywords() {
		List<String> keywords = new ArrayList<String>(10);
		keywords.add(context.getLanguage().getLanguageName());
		keywords.add("x4o");
		keywords.add("xml");
		keywords.add("language");
		keywords.add("documentation");
		return keywords;
	}
	
	private void printBeanProperties(ApiDocContentWriter writer,Object bean) throws SAXException {
		writer.docTableStart("Bean Properties", "Bean properties overview.");
		writer.docTableHeader("Name", "Value");
		for (Method m:bean.getClass().getMethods()) {
			if (m.getName().startsWith("get")) {
				String n = m.getName().substring(3);
				if (m.getParameterTypes().length!=0) {
					continue; // set without parameters
				}
				if (n.length()<2) {
					continue;
				}
				n = n.substring(0,1).toLowerCase()+n.substring(1,n.length());
				Object value = null;
				try {
					value = m.invoke(bean, new Object[] {});
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
				writer.docTableRow(n,printValue(value));
			}
		}
		writer.docTableEnd();
	}
	
	private String printValue(Object value) {
		if (value==null) {
			return "null";
		}
		if (value instanceof String) {
			return (String)value;
		}
		if (value instanceof Class) {
			return "class "+((Class<?>)value).getName();
		}
		if (value instanceof List) {
			StringBuffer buf = new StringBuffer(100);
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
			StringBuffer buf = new StringBuffer(100);
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
	
	@ApiDocNodeWriterMethod(nodeBody=ApiDocNodeBody.SUMMARY,targetClasses={ElementBindingHandler.class})
	public void writeElementBindingHandlerBeanProperties(ApiDocWriteEvent<ApiDocNode> event) throws SAXException {
		printBeanProperties(event.getWriter(),event.getEvent().getUserData());
	}
	
	@ApiDocNodeWriterMethod(nodeBody=ApiDocNodeBody.SUMMARY,targetClasses={ElementConfigurator.class})
	public void writeElementConfiguratorBeanProperties(ApiDocWriteEvent<ApiDocNode> event) throws SAXException {
		printBeanProperties(event.getWriter(),event.getEvent().getUserData());
	}
}
