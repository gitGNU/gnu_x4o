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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.x4o.xml.eld.doc.api.ApiDocWriter;
import org.x4o.xml.eld.doc.api.DefaultPageWriterHelp;
import org.x4o.xml.eld.doc.api.DefaultPageWriterIndexAll;
import org.x4o.xml.eld.doc.api.DefaultPageWriterTree;
import org.x4o.xml.eld.doc.api.dom.ApiDoc;
import org.x4o.xml.eld.doc.api.dom.ApiDocConcept;
import org.x4o.xml.eld.doc.api.dom.ApiDocNode;
import org.x4o.xml.element.ElementNamespaceAttribute;
import org.x4o.xml.element.ElementBindingHandler;
import org.x4o.xml.element.ElementClass;
import org.x4o.xml.element.ElementClassAttribute;
import org.x4o.xml.element.ElementConfigurator;
import org.x4o.xml.element.ElementConfiguratorGlobal;
import org.x4o.xml.element.ElementInterface;
import org.x4o.xml.element.ElementException;
import org.x4o.xml.element.ElementNamespace;
import org.x4o.xml.lang.X4OLanguageModule;
import org.x4o.xml.lang.X4OLanguageSession;
import org.xml.sax.SAXException;

/**
 * EldDocWriter writes the x4o eld documentation.
 * 
 * @author Willem Cazander
 * @version 1.0 Aug 26, 2010
 */
public class EldDocWriter {
	
	// The context to write doc over.
	private X4OLanguageSession context = null;
	
	// Core concepts
	private static final String[] C_CONTEXT			= {"language","Overview","All language modules.","The loaded language modules.."};
	private static final String[] C_MODULE 			= {"module","Module","The Language Modules.","The language is build by the modules and provides the namespaces."};
	private static final String[] C_INTERFACE		= {"interface","Interface","The element interface.","The element interface."};
	private static final String[] C_NAMESPACE		= {"namespace","Namespace","The Language Namespace.","The language namespace holds all the xml elements."};
	private static final String[] C_ELEMENT			= {"element","Element","The Language Element.","The xml language element description."};
	
	// Child concepts
	private static final String[] CC_ATTRIBUTE_H	= {"attribute-handler","Attribute Handler","The attribute handler.","The attribute handler."};
	private static final String[] CC_ATTRIBUTE		= {"attribute","Attribute","The attribute config.","The attribute config."};
	private static final String[] CC_CONFIGURATOR 	= {"configurator","Configurator","The element configurator.","The element configurator."};
	private static final String[] CC_CONFIGURATOR_G = {"configurator-global","ConfiguratorGlobal","The global configurator.","The global configurator."};
	private static final String[] CC_BINDING		= {"binding","Binding","The element binding.","The element binding."};
	
	/**
	 * Creates an EldDocGenerator for this langauge context.
	 * @param context	The language context to generate doc for.
	 */
	public EldDocWriter(X4OLanguageSession context) {
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
		doc.setDocPageSubTitle(createPageSubTitle());
		doc.addDocKeywordAll(createLanguageKeywords());
		doc.setNoFrameAllName("All Elements");
		doc.setFrameNavPrintParent(true);
		doc.setFrameNavPrintParentId(true);
		doc.setGroupTypeName("summary", "Summary");
		doc.setGroupTypeName("overview", "Overview");
		
		// TODO: add config bean to task launcher 
		//doc.addRemoteClass(new ApiDocRemoteClass("file:///home/willemc/devv/git/x4o/x4o-driver/target/apidocs"));
		//doc.addRemoteClass(new ApiDocRemoteClass("http://docs.oracle.com/javase/7/docs/api/"));
		
		doc.setFrameNavConceptClass(ElementClass.class);
		
		doc.addTreeNodePageModeClass(X4OLanguageSession.class);
		doc.addTreeNodePageModeClass(X4OLanguageModule.class);
		doc.addTreeNodePageModeClass(ElementInterface.class);
		doc.addTreeNodePageModeClass(ElementNamespace.class);
		
		doc.addAnnotatedClasses(EldDocWriterLanguage.class);
		doc.addAnnotatedClasses(EldDocWriterLanguageModule.class);
		doc.addAnnotatedClasses(EldDocWriterElementClass.class);
		doc.addAnnotatedClasses(EldDocWriterElementNamespace.class);
		doc.addAnnotatedClasses(EldDocWriterElementInterface.class);
		
		ApiDocConcept adcRoot = doc.addConcept(new ApiDocConcept(null,C_CONTEXT,X4OLanguageSession.class));
		ApiDocConcept adcMod = doc.addConcept(new ApiDocConcept(adcRoot,C_MODULE,X4OLanguageModule.class));
		ApiDocConcept adcIface = doc.addConcept(new ApiDocConcept(adcMod,C_INTERFACE,ElementInterface.class));
		ApiDocConcept adcNs = doc.addConcept(new ApiDocConcept(adcMod,C_NAMESPACE,ElementNamespace.class));
		ApiDocConcept adcEc = doc.addConcept(new ApiDocConcept(adcNs,C_ELEMENT,ElementClass.class));
		
		// mm maybe redo something here
		adcMod.addChildConcepts(new ApiDocConcept(adcMod,CC_ATTRIBUTE_H,ElementNamespaceAttribute.class));
		adcMod.addChildConcepts(new ApiDocConcept(adcMod,CC_CONFIGURATOR_G,ElementConfiguratorGlobal.class));
		adcMod.addChildConcepts(new ApiDocConcept(adcMod,CC_BINDING,ElementBindingHandler.class));
		adcIface.addChildConcepts(new ApiDocConcept(adcMod,CC_ATTRIBUTE,ElementClassAttribute.class));
		adcIface.addChildConcepts(new ApiDocConcept(adcMod,CC_CONFIGURATOR,ElementConfigurator.class));
		adcEc.addChildConcepts(new ApiDocConcept(adcEc,CC_CONFIGURATOR,ElementConfigurator.class));
		adcEc.addChildConcepts(new ApiDocConcept(adcEc,CC_ATTRIBUTE,ElementClassAttribute.class));
		
		doc.addDocPage(EldDocXTreePageWriter.createDocPage());
		doc.addDocPage(DefaultPageWriterTree.createDocPage());
		doc.addDocPage(DefaultPageWriterIndexAll.createDocPage());
		doc.addDocPage(DefaultPageWriterHelp.createDocPage());
		
		ApiDocNode rootNode = new ApiDocNode(context,"language",getLanguageNameUpperCase()+" Language","The X4O "+getLanguageNameUpperCase()+" Language");
		doc.setRootNode(rootNode);
		for (X4OLanguageModule mod:context.getLanguage().getLanguageModules())			{	ApiDocNode modNode = rootNode.addNode(createNodeLanguageModule(mod));
			for (ElementBindingHandler bind:mod.getElementBindingHandlers())			{		modNode.addNode(createNodeElementBindingHandler(bind));			}
			for (ElementConfiguratorGlobal conf:mod.getElementConfiguratorGlobals())	{		modNode.addNode(createNodeElementConfiguratorGlobal(conf));		}
			for (ElementInterface iface:mod.getElementInterfaces())						{		ApiDocNode ifaceNode = modNode.addNode(createNodeElementInterface(iface));
				for (ElementClassAttribute eca:iface.getElementClassAttributes())		{			ifaceNode.addNode(createNodeElementClassAttribute(eca));	}
				for (ElementConfigurator conf:iface.getElementConfigurators())			{			ifaceNode.addNode(createNodeElementConfigurator(conf));	}	}
			for (ElementNamespace ns:mod.getElementNamespaces())						{		ApiDocNode nsNode = modNode.addNode(createNodeElementNamespace(ns));
				for (ElementNamespaceAttribute attr:ns.getElementNamespaceAttributes())	{			nsNode.addNode(createNodeElementNamespaceAttribute(attr));		}
				for (ElementClass ec:ns.getElementClasses())							{			ApiDocNode ecNode = nsNode.addNode(createNodeElementClass(ec));
					for (ElementClassAttribute eca:ec.getElementClassAttributes())		{				ecNode.addNode(createNodeElementClassAttribute(eca));	}
					for (ElementConfigurator conf:ec.getElementConfigurators())			{				ecNode.addNode(createNodeElementConfigurator(conf));	}	}	}
		}
		return doc;
	}
	
	private ApiDocNode createNodeElementBindingHandler(ElementBindingHandler bind) {
		return new ApiDocNode(bind,bind.getId(),bind.getId(),bind.getDescription());
	}
	private ApiDocNode createNodeElementNamespaceAttribute(ElementNamespaceAttribute attr) {
		return new ApiDocNode(attr,attr.getId(),attr.getId(),attr.getDescription());
	}
	private ApiDocNode createNodeElementConfiguratorGlobal(ElementConfiguratorGlobal conf) {
		return new ApiDocNode(conf,conf.getId(),conf.getId(),conf.getDescription());
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
	private ApiDocNode createNodeElementNamespace(ElementNamespace ns) {
		return new ApiDocNode(ns,ns.getId(),ns.getUri(),ns.getDescription());
	}
	private ApiDocNode createNodeElementClass(ElementClass ec) {
		return new ApiDocNode(ec,ec.getId(),ec.getId(),ec.getDescription());
	}
	private ApiDocNode createNodeElementClassAttribute(ElementClassAttribute eca) {
		return new ApiDocNode(eca,eca.getId(),eca.getId(),eca.getDescription());
	}
	
	private String createPageSubTitle() {
		StringBuffer buf = new StringBuffer(100);
		buf.append(context.getLanguage().getLanguageName());
		buf.append(" ");// note use real space as 'html/header/title' will not always escape entities. TODO: add to html writer
		buf.append(context.getLanguage().getLanguageVersion());
		buf.append(" API");
		return buf.toString();
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
		buf.append(getLanguageNameUpperCase());
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
	
	private String getLanguageNameUpperCase() {
		return context.getLanguage().getLanguageName().toUpperCase();
	}
}
