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
import java.util.Map;

import org.x4o.xml.eld.doc.api.ApiDocWriter;
import org.x4o.xml.eld.doc.api.DefaultPageWriterHelp;
import org.x4o.xml.eld.doc.api.DefaultPageWriterIndexAll;
import org.x4o.xml.eld.doc.api.DefaultPageWriterTree;
import org.x4o.xml.eld.doc.api.dom.ApiDoc;
import org.x4o.xml.eld.doc.api.dom.ApiDocConcept;
import org.x4o.xml.eld.doc.api.dom.ApiDocNode;
import org.x4o.xml.eld.doc.api.dom.ApiDocRemoteClass;
import org.x4o.xml.element.ElementNamespaceAttribute;
import org.x4o.xml.element.ElementBindingHandler;
import org.x4o.xml.element.ElementClass;
import org.x4o.xml.element.ElementClassAttribute;
import org.x4o.xml.element.ElementConfigurator;
import org.x4o.xml.element.ElementConfiguratorGlobal;
import org.x4o.xml.element.ElementInterface;
import org.x4o.xml.element.ElementException;
import org.x4o.xml.element.ElementNamespace;
import org.x4o.xml.io.sax.ext.ContentWriterXml;
import org.x4o.xml.io.sax.ext.PropertyConfig;
import org.x4o.xml.io.sax.ext.PropertyConfig.PropertyConfigItem;
import org.x4o.xml.lang.X4OLanguage;
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
	
	private final static String DEFAULT_NAME = "X4O ELD DOC";
	private final static String DEFAULT_DESCRIPTION = "X4O Meta Language Documentation.";
	
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
	
	private final static String PROPERTY_CONTEXT_PREFIX = PropertyConfig.X4O_PROPERTIES_PREFIX+PropertyConfig.X4O_PROPERTIES_ELD_DOC;
	public final static PropertyConfig DEFAULT_PROPERTY_CONFIG;
	
	public final static String OUTPUT_PATH              = PROPERTY_CONTEXT_PREFIX+"output/path";
	public final static String DOC_NAME                 = PROPERTY_CONTEXT_PREFIX+"doc/name";
	public final static String DOC_DESCRIPTION          = PROPERTY_CONTEXT_PREFIX+"doc/description";
	public final static String DOC_ABOUT                = PROPERTY_CONTEXT_PREFIX+"doc/about";
	public final static String DOC_COPYRIGHT            = PROPERTY_CONTEXT_PREFIX+"doc/copyright";
	public final static String DOC_PAGE_SUB_TITLE       = PROPERTY_CONTEXT_PREFIX+"doc/page-sub-title";
	public final static String META_KEYWORDS            = PROPERTY_CONTEXT_PREFIX+"meta/keywords";
	public final static String META_STYLESHEET          = PROPERTY_CONTEXT_PREFIX+"meta/stylesheet";
	public final static String META_STYLESHEET_THEMA    = PROPERTY_CONTEXT_PREFIX+"meta/stylesheet-thema";
	public final static String JAVADOC_LINK             = PROPERTY_CONTEXT_PREFIX+"javadoc/link";
	public final static String JAVADOC_LINK_OFFLINE     = PROPERTY_CONTEXT_PREFIX+"javadoc/link-offline";
	public final static String PAGE_PRINT_TREE          = PROPERTY_CONTEXT_PREFIX+"page/print-tree";
	public final static String PAGE_PRINT_XTREE         = PROPERTY_CONTEXT_PREFIX+"page/print-xtree";
	public final static String PAGE_PRINT_INDEX_ALL     = PROPERTY_CONTEXT_PREFIX+"page/print-index-all";
	public final static String PAGE_PRINT_HELP          = PROPERTY_CONTEXT_PREFIX+"page/print-help";
	
	static {
		DEFAULT_PROPERTY_CONFIG = new PropertyConfig(true,ContentWriterXml.DEFAULT_PROPERTY_CONFIG,PROPERTY_CONTEXT_PREFIX,
				new PropertyConfigItem(true,OUTPUT_PATH,File.class),
				new PropertyConfigItem(false,DOC_NAME,String.class),
				new PropertyConfigItem(false,DOC_DESCRIPTION,String.class),
				new PropertyConfigItem(false,DOC_ABOUT,String.class),
				new PropertyConfigItem(false,DOC_COPYRIGHT,String.class),
				new PropertyConfigItem(false,DOC_PAGE_SUB_TITLE,String.class),
				new PropertyConfigItem(false,META_KEYWORDS,List.class),
				new PropertyConfigItem(false,META_STYLESHEET,File.class),
				new PropertyConfigItem(false,META_STYLESHEET_THEMA,String.class),
				new PropertyConfigItem(false,JAVADOC_LINK,List.class),
				new PropertyConfigItem(false,JAVADOC_LINK_OFFLINE,Map.class),
				new PropertyConfigItem(PAGE_PRINT_TREE,Boolean.class,true),
				new PropertyConfigItem(PAGE_PRINT_XTREE,Boolean.class,true),
				new PropertyConfigItem(PAGE_PRINT_INDEX_ALL,Boolean.class,true),
				new PropertyConfigItem(PAGE_PRINT_HELP,Boolean.class,true)
				);
	}
	
	/** The config of this writer. */
	private final PropertyConfig propertyConfig;
	
	/** The language to write doc over. */
	private final X4OLanguage language;
	
	/**
	 * Creates an EldDocGenerator for this langauge context.
	 * @param language	The language to generate doc for.
	 */
	public EldDocWriter(X4OLanguage language,PropertyConfig parentConfig) {
		this.language=language;
		this.propertyConfig=new PropertyConfig(DEFAULT_PROPERTY_CONFIG,PROPERTY_CONTEXT_PREFIX);
		this.propertyConfig.copyParentProperties(parentConfig);
	}
	
	/**
	 * Writes the language documentation to the base path.
	 * @param basePath	The path to write to documentation to.
	 * @throws ElementException	Is thrown when error is done.
	 */
	public void writeDocumentation() throws ElementException {
		try {
			File basePath = propertyConfig.getPropertyFile(OUTPUT_PATH);
			ApiDocWriter writer = new ApiDocWriter();
			ApiDoc doc = buildLanguageDoc();
			writer.write(doc, basePath);
		} catch (SAXException e) {
			throw new ElementException(e);
		} catch (IOException e) {
			throw new ElementException(e);
		}
	}
	
	/**
	 * Creates a fully configured ApiDco object.
	 * @return	The ApiDoc configured to write eld documentation.
	 */
	private ApiDoc buildLanguageDoc() {
		
		// Generic config
		ApiDoc doc = new ApiDoc();
		doc.setName(				propertyConfig.getPropertyString(DOC_NAME, DEFAULT_NAME));
		doc.setDescription(			propertyConfig.getPropertyString(DOC_DESCRIPTION, DEFAULT_DESCRIPTION));
		doc.setDocAbout(			propertyConfig.getPropertyString(DOC_ABOUT, createLanguageAbout()));
		doc.setDocCopyright(		propertyConfig.getPropertyString(DOC_COPYRIGHT, createLanguageCopyright()));
		doc.setDocPageSubTitle(		propertyConfig.getPropertyString(DOC_PAGE_SUB_TITLE, createPageSubTitle()));
		doc.setMetaStyleSheetThema(	propertyConfig.getPropertyString(META_STYLESHEET_THEMA));
		doc.setMetaStyleSheet(		propertyConfig.getPropertyFile(META_STYLESHEET));
		List<String> keywords =		propertyConfig.getPropertyList(META_KEYWORDS);
		if (keywords==null) {
			keywords = createLanguageKeywords();
		}
		doc.addMetaKeywordAll(keywords);
		doc.setNoFrameAllName("All Elements");
		doc.setFrameNavPrintParent(true);
		doc.setFrameNavPrintParentId(true);
		doc.setGroupTypeName("summary", "Summary",1);
		doc.setGroupTypeName("overview", "Overview",2);
		
		// Javadoc linking config
		List<String> javadocLinkList = propertyConfig.getPropertyList(JAVADOC_LINK);
		Map<String,String> javadocLinkOfflineMap = propertyConfig.getPropertyMap(JAVADOC_LINK_OFFLINE);
		if (javadocLinkList!=null) {
			for (String javadocUrl:javadocLinkList) {
				doc.addRemoteClass(new ApiDocRemoteClass(javadocUrl));
			}
		}
		if (javadocLinkOfflineMap!=null) {
			for (Map.Entry<String,String> offlineLink:javadocLinkOfflineMap.entrySet()) {
				doc.addRemoteClass(new ApiDocRemoteClass(offlineLink.getKey(),offlineLink.getValue()));
			}
		}
		
		// Tree and navagation config
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
		
		ApiDocConcept adcRoot = doc.addConcept(new ApiDocConcept(null,C_CONTEXT,X4OLanguage.class));
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
		
		// Non-tree pages config
		if (propertyConfig.getPropertyBoolean(PAGE_PRINT_XTREE)) {     doc.addDocPage(EldDocXTreePageWriter.createDocPage()); }
		if (propertyConfig.getPropertyBoolean(PAGE_PRINT_TREE)) {      doc.addDocPage(DefaultPageWriterTree.createDocPage()); }
		if (propertyConfig.getPropertyBoolean(PAGE_PRINT_INDEX_ALL)) { doc.addDocPage(DefaultPageWriterIndexAll.createDocPage()); }
		if (propertyConfig.getPropertyBoolean(PAGE_PRINT_HELP)) {      doc.addDocPage(DefaultPageWriterHelp.createDocPage()); }
		
		// Doc tree config
		ApiDocNode rootNode = new ApiDocNode(language,"language",getLanguageNameUpperCase()+" Language","The X4O "+getLanguageNameUpperCase()+" Language");
		doc.setRootNode(rootNode);
		for (X4OLanguageModule mod:language.getLanguageModules())						{	ApiDocNode modNode = rootNode.addNode(createNodeLanguageModule(mod));
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
		StringBuilder buf = new StringBuilder(100);
		buf.append(language.getLanguageName());
		buf.append(" ");// note use real space as 'html/header/title' will not always escape entities. TODO: add to html writer
		buf.append(language.getLanguageVersion());
		buf.append(" API");
		return buf.toString();
	}
	
	private String createLanguageAbout() {
		StringBuilder buf = new StringBuilder(100);
		buf.append("XML X4O Language\n");
		buf.append(language.getLanguageName().toUpperCase());
		buf.append("&trade;&nbsp;");
		buf.append(language.getLanguageVersion());
		return buf.toString();
	}
	
	private String createLanguageCopyright() {
		Calendar calendar = Calendar.getInstance();
		StringBuilder buf = new StringBuilder(100);
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
		keywords.add(language.getLanguageName());
		keywords.add("x4o");
		keywords.add("eld");
		keywords.add("xml");
		keywords.add("xsd");
		keywords.add("schema");
		keywords.add("language");
		keywords.add("documentation");
		return keywords;
	}
	
	private String getLanguageNameUpperCase() {
		return language.getLanguageName().toUpperCase();
	}
}
