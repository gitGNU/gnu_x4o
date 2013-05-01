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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.x4o.xml.eld.doc.ContentWriterDoc.NavBarConfig;
import org.x4o.xml.element.ElementAttributeHandler;
import org.x4o.xml.element.ElementBindingHandler;
import org.x4o.xml.element.ElementClass;
import org.x4o.xml.element.ElementClassAttribute;
import org.x4o.xml.element.ElementConfigurator;
import org.x4o.xml.element.ElementConfiguratorGlobal;
import org.x4o.xml.element.ElementInterface;
import org.x4o.xml.element.ElementNamespaceContext;
import org.x4o.xml.io.XMLConstants;
import org.x4o.xml.io.sax.ContentWriterHtml.DocType;
import org.x4o.xml.io.sax.ContentWriterHtml.Tag;
import org.x4o.xml.lang.X4OLanguageClassLoader;
import org.x4o.xml.lang.X4OLanguageModule;
import org.x4o.xml.lang.X4OLanguageContext;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * EldDocHtmlWriter writes simple eld documentation.
 * 
 * 
 * 
 * @author Willem Cazander
 * @version 1.0 Aug 26, 2010
 */
public class EldDocHtmlWriter {
	
	private String toSafeUri(String uri) {
		StringBuilder buf = new StringBuilder(20);
		for (char c:uri.toLowerCase().toCharArray()) {
			if (Character.isLetter(c)) {
				buf.append(c);
			}
			if (Character.isDigit(c)) {
				buf.append(c);
			}
			if ('.'==c) {
				buf.append(c);
			}
			if ('-'==c) {
				buf.append(c);
			}
			if ('_'==c) {
				buf.append(c);
			}
		}
		String prefix = buf.toString();
		if (prefix.startsWith("http")) {
			prefix = prefix.substring(4);
		}
		if (prefix.startsWith("uri")) {
			prefix = prefix.substring(3);
		}
		if (prefix.startsWith("url")) {
			prefix = prefix.substring(3);
		}
		return prefix;
	}
	
	private String toElementUri(String pathPrefix,X4OLanguageModule mod,ElementNamespaceContext namespace,ElementClass ec) {
		StringBuffer buf = new StringBuffer(100);
		if (pathPrefix!=null) {
			buf.append(pathPrefix);
		}
		buf.append(toSafeUri(mod.getId()));
		buf.append("/");
		buf.append(toSafeUri(namespace.getId()));
		buf.append("/");
		buf.append(toSafeUri(ec.getId()));
		buf.append("/index.html");
		return buf.toString();
	}
	
	private ContentWriterDoc createContentWriterDoc(File outputFile) throws SAXException {
		String encoding = XMLConstants.XML_DEFAULT_ENCODING;
		try {
			Writer out = new OutputStreamWriter(new FileOutputStream(outputFile), encoding);
			String charNewLine = XMLConstants.CHAR_NEWLINE;
			String charTab = "  ";
			ContentWriterDoc result = new ContentWriterDoc(out,encoding,charNewLine,charTab);
			return result;
		} catch (UnsupportedEncodingException e) {
			throw new SAXException(e);
		} catch (SecurityException e) {
			throw new SAXException(e);
		} catch (FileNotFoundException e) {
			throw new SAXException(e);
		}
	}
	
	protected String createLanguageAbout(X4OLanguageContext context) {
		StringBuffer buf = new StringBuffer(100);
		buf.append("XML&nbsp;X4O&nbsp;Language<br/>");
		buf.append(context.getLanguage().getLanguageName().toUpperCase());
		buf.append("&trade;&nbsp;");
		buf.append(context.getLanguage().getLanguageVersion());
		return buf.toString();
	}
	
	protected String createLanguageCopyright(X4OLanguageContext context) {
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
	
	protected List<String> createLanguageKeywords(X4OLanguageContext context) {
		List<String> keywords = new ArrayList<String>(10);
		keywords.add(context.getLanguage().getLanguageName());
		keywords.add("x4o");
		keywords.add("xml");
		keywords.add("language");
		keywords.add("documentation");
		return keywords;
	}

	protected NavBarConfig createNavBarConfig(String pathPrefix,File frame,X4OLanguageContext context,ContentWriterDoc writer) throws SAXException {
		return createNavBarConfig(pathPrefix, null, null, frame, context, writer);
	}
	
	protected NavBarConfig createNavBarConfig(String pathPrefix,String prev,String next,File frame,X4OLanguageContext context,ContentWriterDoc writer) throws SAXException {
		String framePath = null;
		try {
			String rootPath = new File(frame.getParentFile().getPath()+File.separatorChar+pathPrefix).getCanonicalPath();
			framePath = frame.getPath().substring(rootPath.length()+1);
		} catch (IOException e) {
			throw new SAXException(e);
		}		
		NavBarConfig conf = writer.new NavBarConfig(pathPrefix,prev,next,framePath,createLanguageAbout(context));
		conf.noFrameAllLink="allelements-noframe.html";
		conf.noFrameAllName="All Elements";
		conf.linkConstructorName="Tag";
		conf.linkFieldName="Attributes";
		conf.linkMethodName="Config";
		
		conf.addNavItem("overview","overview-language.html","Overview");
		conf.addNavItem("module",null,"Module");
		conf.addNavItem("namespace",null,"Namespace");
		conf.addNavItem("element",null,"Element");
		conf.addNavItem("tree","overview-tree.html","Tree");
		conf.addNavItem("index","index-all.html","Index");
		conf.addNavItem("help","doc-help.html","Help");

		return conf;
	}
	
	private void writeStart(NavBarConfig conf,ContentWriterDoc writer,X4OLanguageContext context,String title) throws SAXException {
		writer.docHtmlStart(conf,title, createLanguageKeywords(context));
	}
	
	private void writeEnd(NavBarConfig conf,ContentWriterDoc writer,X4OLanguageContext context) throws SAXException {
		writer.docHtmlEnd(conf,createLanguageCopyright(context));
	}
	
	private File createOutputPathFile(File basePath,String...argu) {
		StringBuffer buf = new StringBuffer(200);
		buf.append(basePath.getAbsolutePath());
		buf.append(File.separatorChar);
		for (int i=0;i<argu.length-1;i++) {
			buf.append(toSafeUri(argu[i]));
			buf.append(File.separatorChar);
		}
		File outputPath = new File(buf.toString());
		if (outputPath.exists()==false) {
			//System.out.println("Creating path: "+outputPath);
			outputPath.mkdirs();
		}
		buf.append(File.separatorChar);
		buf.append(toSafeUri(argu[argu.length-1]));
		File outputFile = new File(buf.toString());
		if (outputFile.exists()) {
			outputFile.delete();
		}
		//System.out.println("Creating file: "+outputFile);
		return outputFile;
	}
	
	private void copyResourceToFile(String resource,File basePath,String...argu) throws SecurityException, IOException, InterruptedException {
		ClassLoader cl = X4OLanguageClassLoader.getClassLoader();
		InputStream inputStream = cl.getResourceAsStream(resource);
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
	
	public void writeTheme(File basePath) throws IOException {
		try {
			copyResourceToFile("org/x4o/xml/eld/doc/theme/jdk7/stylesheet.css",basePath,"resources","stylesheet.css");
			copyResourceToFile("org/x4o/xml/eld/doc/theme/jdk7/background.png",basePath,"resources","background.png");
			copyResourceToFile("org/x4o/xml/eld/doc/theme/jdk7/tab.png",basePath,"resources","tab.png");
			copyResourceToFile("org/x4o/xml/eld/doc/theme/jdk7/titlebar_end.png",basePath,"resources","titlebar_end.png");
			copyResourceToFile("org/x4o/xml/eld/doc/theme/jdk7/titlebar.png",basePath,"resources","titlebar.png");
		} catch (SecurityException e) {
			throw new IOException(e.getMessage());
		} catch (InterruptedException e) {
			throw new IOException(e.getMessage());
		}
	}
	
	private void writeHeader(ContentWriterDoc writer,String pathPrefix,String title) throws SAXException {
		writer.printTagStart(Tag.head);
			writer.docCommentGenerated();
			writer.printHeadMetaContentType();
			writer.printHeadTitle(title);
			writer.printHeadMetaDate();
			writer.printHeadLinkCss(pathPrefix+"resources/stylesheet.css");
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
		"\t\t { top.languageFrame.location = top.targetPage; }\n"+
		"}\n";
	
	public void writeIndex(File basePath,X4OLanguageContext context) throws SAXException {
		File outputFile = createOutputPathFile(basePath,"index.html");
		ContentWriterDoc writer = createContentWriterDoc(outputFile);
		try {
			writer.printDocType(DocType.HTML_4_FRAMESET);
			writer.comment("NewPage");
			writer.printHtmlStart("en");
			writeHeader(writer,"",context.getLanguage().getLanguageName()+" API");
			writer.printScriptInline(FRAME_JS);
			
			AttributesImpl atts = new AttributesImpl();
			atts.addAttribute ("", "cols", "", "", "20%,80%");
			atts.addAttribute ("", "title", "", "", "Documentation frame");
			atts.addAttribute ("", "onload", "", "", "top.loadFrames()");
			writer.printTagStart(Tag.frameset, atts);
			
			atts = new AttributesImpl();
			atts.addAttribute ("", "rows", "", "", "30%,70%");
			atts.addAttribute ("", "title", "", "", "Left frames");
			atts.addAttribute ("", "onload", "", "", "top.loadFrames()");
			writer.printTagStart(Tag.frameset, atts);
				atts = new AttributesImpl();
				atts.addAttribute ("", "src", "", "", "overview-frame.html");
				atts.addAttribute ("", "title", "", "", "All Namspaces");
				atts.addAttribute ("", "name", "", "", "namespaceListFrame");
				writer.printTagStart(Tag.frame, atts);
				writer.printTagEnd(Tag.frame);
				atts = new AttributesImpl();
				atts.addAttribute ("", "src", "", "", "allelements-frame.html");
				atts.addAttribute ("", "title", "", "", "All Elements");
				atts.addAttribute ("", "name", "", "", "elementListFrame");
				writer.printTagStart(Tag.frame, atts);
				writer.printTagEnd(Tag.frame);
			writer.printTagEnd(Tag.frameset);
			
			atts = new AttributesImpl();
			atts.addAttribute ("", "src", "", "", "overview-language.html");
			atts.addAttribute ("", "title", "", "", "All Language Components");
			atts.addAttribute ("", "name", "", "", "languageFrame");
			atts.addAttribute ("", "scrolling", "", "", "yes");
			writer.printTagStart(Tag.frame, atts);
			writer.printTagEnd(Tag.frame);
			
			writer.printTagStart(Tag.noframes);
				writer.printScriptNoDiv();
				writer.printTagText(Tag.h2, "Frame Alert");
				writer.printTagStart(Tag.p);
					writer.characters("This document is designed to be viewed using the frames feature. If you see this message, you are using a non-frame-capable web client. Link to ");
					writer.printHref("overview-language.html", "Non-frame version");
					writer.characters(".");
				writer.printTagEnd(Tag.p);
			writer.printTagEnd(Tag.noframes);
			
			writer.printTagEnd(Tag.frameset);
			writer.printHtmlEnd();
		} finally {
			writer.closeWriterSafe();
		}
	}
	
	public void writeOverviewFrame(File basePath,X4OLanguageContext context) throws SAXException {
		File outputFile = createOutputPathFile(basePath,"overview-frame.html");
		ContentWriterDoc writer = createContentWriterDoc(outputFile);
		try {
			writer.printDocType(DocType.HTML_4_TRANSITIONAL);
			writer.comment("NewPage");
			writer.printHtmlStart("en");
			writeHeader(writer,"","All Elements of "+context.getLanguage().getLanguageName());
			writer.printTagStart(Tag.body);
			
			writer.printTagStart(Tag.div,"indexHeader");
			writer.printHrefTarget("allelements-frame.html", "All Elements","elementListFrame");
			writer.printTagEnd(Tag.div);
			
			writer.printTagStart(Tag.div,"indexContainer");
			writer.printTagText(Tag.h2, "Namespaces");
			writer.printTagStart(Tag.ul);
			for (X4OLanguageModule mod:context.getLanguage().getLanguageModules()) {
				for (ElementNamespaceContext enc:mod.getElementNamespaceContexts()) {
					writer.printTagStart(Tag.li);
					writer.printHrefTarget(toSafeUri(mod.getId())+"/"+toSafeUri(enc.getId())+"/namespace-frame.html", enc.getUri(),"elementListFrame");
					writer.printTagEnd(Tag.li);
				}
			}
			writer.printTagEnd(Tag.ul);
			writer.printTagEnd(Tag.div);
			
			writer.printTagEnd(Tag.body);
			writer.printHtmlEnd();
		} finally {
			writer.closeWriterSafe();
		}
	}
	
	public void writeAllElementsFrame(File basePath,X4OLanguageContext context,boolean isFrame) throws SAXException {
		if (isFrame) {
			writeElementsFrame(basePath,"",context,isFrame,null,"allelements-frame.html");
		} else {
			writeElementsFrame(basePath,"",context,isFrame,null,"allelements-noframe.html");
		}
	}
	
	public void writeNamespaceElementsFrame(File basePath,ElementNamespaceContext ns,X4OLanguageModule mod,X4OLanguageContext context) throws SAXException {
		writeElementsFrame(basePath,"../../",context,true,ns.getUri(),mod.getId(),ns.getId(),"namespace-frame.html");
	}
	
	private void writeElementsFrame(File basePath,String pathPrefix,X4OLanguageContext context,boolean isFrame,String nsUri,String...fileName) throws SAXException {
		File outputFile = createOutputPathFile(basePath,fileName);
		ContentWriterDoc writer = createContentWriterDoc(outputFile);
		try {
			writer.printDocType(DocType.HTML_4_TRANSITIONAL);
			writer.comment("NewPage");
			writer.printHtmlStart("en");
			writeHeader(writer,pathPrefix,"All Elements of "+context.getLanguage().getLanguageName());
			writer.printTagStart(Tag.body);
			if (nsUri==null) {
				writer.printTagText(Tag.h1, "All Elements", "bar");
			} else {
				for (X4OLanguageModule mod:context.getLanguage().getLanguageModules()) {
					for (ElementNamespaceContext enc:mod.getElementNamespaceContexts()) {
						if (nsUri!=null && enc.getUri().equals(nsUri)==false) {
							continue;
						}
						writer.printTagStart(Tag.h1,"bar");
						writer.printHrefTarget(pathPrefix+toSafeUri(mod.getId())+File.separatorChar+toSafeUri(enc.getId())+File.separatorChar+"index.html", nsUri,"languageFrame");
						writer.printTagEnd(Tag.h1);
						break;
					}
				}
				
			}
			
			writer.printTagStart(Tag.div,"indexContainer");
			writer.printTagStart(Tag.ul);
			
			for (X4OLanguageModule mod:context.getLanguage().getLanguageModules()) {
				for (ElementNamespaceContext enc:mod.getElementNamespaceContexts()) {
					if (nsUri!=null && enc.getUri().equals(nsUri)==false) {
						continue;
					}
					for (ElementClass ec:enc.getElementClasses()) {
						writer.printTagStart(Tag.li);
						if (isFrame) {
							writer.printHrefTarget(pathPrefix+toElementUri(null,mod,enc,ec), enc.getId()+":"+ec.getId(),"languageFrame");
						} else {
							writer.printHref(pathPrefix+toElementUri(null,mod,enc,ec), enc.getId()+":"+ec.getId());
						}
						writer.printTagEnd(Tag.li);
					}
				}
			}
			writer.printTagEnd(Tag.ul);
			writer.printTagEnd(Tag.div);
			
			writer.printTagEnd(Tag.body);
			writer.printHtmlEnd();
		} finally {
			writer.closeWriterSafe();
		}
	}
	
	public void writeOverviewLanguage(File basePath,X4OLanguageContext context) throws SAXException {
		File outputFile = createOutputPathFile(basePath,"overview-language.html");
		ContentWriterDoc writer = createContentWriterDoc(outputFile);
		String pathPrefix = "";
		NavBarConfig conf = createNavBarConfig(pathPrefix, outputFile, context, writer);
		conf.navSelected="overview";
		conf.linkConstructorName="Language";
		conf.linkFieldName="Modules";
		conf.linkMethodName="Namespaces";
		conf.linkConstructors=true;
		conf.linkFields=true;
		conf.linkMethods=true;
		try {
			String title = "Language ("+context.getLanguage().getLanguageName()+" "+context.getLanguage().getLanguageVersion()+" ELD)";
			writeStart(conf,writer,context,title);
			writer.docPagePackageTitle(title, "Index");
			writer.docPageContentStart();
			
			int attrHandlers = 0;
			int bindHandlers = 0;
			int interFaces = 0;
			int eleConfigs = 0;
			int elements = 0;
			int namespaces = 0;
			for (X4OLanguageModule mod:context.getLanguage().getLanguageModules()) {
				attrHandlers += mod.getElementAttributeHandlers().size();
				bindHandlers += mod.getElementBindingHandlers().size();
				interFaces += mod.getElementInterfaces().size();
				eleConfigs += mod.getElementConfiguratorGlobals().size();
				for (ElementNamespaceContext ns:mod.getElementNamespaceContexts()) {
					namespaces++;
					elements += ns.getElementClasses().size();
				}
			}
			
			//pw.print("<p>Welcome to the EldDocs</p>");
			
			
			writer.printTagStart(Tag.div, "summary");
			writer.docPageBlockStart();
			
			writer.docPageBlockStart("Language Summary","constructor_summary","======== LANGUAGE SUMMARY ========");
				writer.docTableStart("Language", "Language Stats Overview");
				writer.docTableHeader("Name", "Value");
				writer.docTableRow("LanguageName:", ""+context.getLanguage().getLanguageName(), null);
				writer.docTableRow("LanguageVersion:",""+context.getLanguage().getLanguageVersion(),null);
				writer.docTableRow("Modules:",""+context.getLanguage().getLanguageModules().size(),null);
				writer.docTableRow("Namespaces:",""+namespaces,null);
				writer.docTableRow("Elements:",""+elements,null);
				writer.docTableRow("ElementInterfaces:",""+interFaces,null);
				writer.docTableRow("ElementAttributeHandlers:",""+attrHandlers,null);
				writer.docTableRow("ElementBindingHandlers:",""+bindHandlers,null);
				writer.docTableRow("ElementConfigurators:",""+eleConfigs,null);
				writer.docTableEnd();
			writer.docPageBlockEnd();
			
			writer.docPageBlockStart("Modules Summary","method_summary","======== MODULES SUMMARY ========");
				writer.docTableStart("Modules Summary", "All Language Modules Overview");
				writer.docTableHeader("URI", "Name");
				List<X4OLanguageModule> mods = context.getLanguage().getLanguageModules();
				Collections.sort(mods,new ElementLanguageModuleComparator());
				for (X4OLanguageModule mod:mods) {
					writer.docTableRowHref(toSafeUri(mod.getId())+"/index.html",mod.getId(),mod.getProviderName(),null);
				}
				writer.docTableEnd();
			writer.docPageBlockEnd();
			
			writer.docPageBlockStart("Namespace Summary","field_summary","======== NAMESPACE SUMMARY ========");
				writer.docTableStart("Namespace Summary", "All Language Namespaces Overview");
				writer.docTableHeader("ID", "URI");
				for (X4OLanguageModule mod:mods) {
					for (ElementNamespaceContext ns:mod.getElementNamespaceContexts()) {
						writer.docTableRowHref(pathPrefix+toSafeUri(mod.getId())+"/"+toSafeUri(ns.getId())+"/index.html",ns.getId(),ns.getUri(),null);
					}
				}
				writer.docTableEnd();
			writer.docPageBlockEnd();
			
			writer.docPageBlockEnd();
			writer.printTagEnd(Tag.div); // summary 
			
			writer.docPagePackageDescription(title, "Index","Language overview.");
			writer.docPageContentEnd();
			writeEnd(conf,writer,context);
		} finally {
			writer.closeWriterSafe();
		}
	}
	
	public void writeDocHelp(File basePath,X4OLanguageContext context) throws SAXException {
		File outputFile = createOutputPathFile(basePath,"doc-help.html");
		ContentWriterDoc writer = createContentWriterDoc(outputFile);
		String pathPrefix = "";
		NavBarConfig conf = createNavBarConfig(pathPrefix, outputFile, context, writer);
		conf.navSelected="help";
		try {
			String title = "API Help";
			writeStart(conf,writer,context,title);
			writer.printTagStart(Tag.div,"header");
				writer.printTagText(Tag.h1, "How This API Document Is Organized", "title");
				writer.printTagStart(Tag.div,"subTitle");
					writer.characters("This X4O (XML4Objects) language document has pages corresponding to the items in the navigation bar, described as follows.");
				writer.printTagEnd(Tag.div);
			writer.printTagEnd(Tag.div);
			
			writer.docPageContentStart();
			writer.docPageBlockStart();
			
			writer.printTagText(Tag.h2, "Overview");
			writer.printTagStart(Tag.p);
				writer.charactersRaw("The <a href=\"overview-language.html\">Overview</a> page is the front page of this XML X4O Language document and provides a list of all modules and namespaces with a summary for each.  This page can also contain an overall description of the language size.");
			writer.printTagEnd(Tag.p);
			writer.docPageBlockNext();
			
			writer.printTagText(Tag.h2, "Module");
			writer.printTagStart(Tag.p);
				writer.charactersRaw("The language is build by the modules and provides the namespaces.");
			writer.printTagEnd(Tag.p);
			writer.docPageBlockNext();
			
			writer.printTagText(Tag.h2, "Namespace");
			writer.printTagStart(Tag.p);
				writer.charactersRaw("The language namespace holds all the xml elements.");
			writer.printTagEnd(Tag.p);
			writer.docPageBlockNext();
			
			writer.printTagText(Tag.h2, "Element");
			writer.printTagStart(Tag.p);
				writer.charactersRaw("The xml language element description.");
			writer.printTagEnd(Tag.p);
			writer.docPageBlockNext();
			
			writer.printTagText(Tag.h2, "Tree");
			writer.printTagStart(Tag.p);
				writer.charactersRaw("The xml tree as the eld has defined it.");
			writer.printTagEnd(Tag.p);
			writer.docPageBlockNext();
			
			writer.printTagText(Tag.h2, "Index");
			writer.printTagStart(Tag.p);
				writer.charactersRaw("All the keywords in the language.");
			writer.printTagEnd(Tag.p);
			writer.docPageBlockNext();
			
			writer.printTagText(Tag.h2, "Help");
			writer.printTagStart(Tag.p);
				writer.charactersRaw("This page.");
			writer.printTagEnd(Tag.p);
			
			writer.docPageBlockEnd();
			writer.docPagePackageDescription(title, "Help","This help file applies to API documentation generated using the standard doclet.");
			writer.docPageContentEnd();
			writeEnd(conf,writer,context);
		} finally {
			writer.closeWriterSafe();
		}
	}
	
	public void writeIndexAll(File basePath,X4OLanguageContext context) throws SAXException {
		File outputFile = createOutputPathFile(basePath,"index-all.html");
		ContentWriterDoc writer = createContentWriterDoc(outputFile);
		String pathPrefix = "";
		NavBarConfig conf = createNavBarConfig(pathPrefix, outputFile, context, writer);
		conf.navSelected="index";
		try {
			String title = "Index All";
			writeStart(conf,writer,context,title);
			writer.docPageContentStart();
			for (char i='A';i<='Z';i++) {
				writer.printHref("#_"+i+"_", ""+i);
				writer.charactersRaw("&nbsp;");
			}
			for (char i='A';i<='Z';i++) {
				writer.printHrefNamed("_"+i+"_");
				writer.printTagText(Tag.h2, ""+i);
				writer.characters("TODO");
			}
			writer.docPageContentEnd();
			writeEnd(conf,writer,context);
		} finally {
			writer.closeWriterSafe();
		}
	}
	
	public void writeOverviewTree(File basePath,X4OLanguageContext context) throws SAXException {
		File outputFile = createOutputPathFile(basePath,"overview-tree.html");
		ContentWriterDoc writer = createContentWriterDoc(outputFile);
		String pathPrefix = "";
		NavBarConfig conf = createNavBarConfig(pathPrefix, outputFile, context, writer);
		conf.navSelected="tree";
		try {
			String title = context.getLanguage().getLanguageName()+" "+context.getLanguage().getLanguageVersion()+" ELD";
			writeStart(conf,writer,context,title);
			writer.docPagePackageTitle(title, "Overview Tree");
			writer.docPageContentStart();
			
			List<TreeNode> rootNodes = new ArrayList<TreeNode>(3);
			for (X4OLanguageModule mod:context.getLanguage().getLanguageModules()) {
				for (ElementNamespaceContext ns:mod.getElementNamespaceContexts()) {
					if (ns.getLanguageRoot()!=null && ns.getLanguageRoot()) {
						// found language root elements.
						for (ElementClass ec:ns.getElementClasses()) {
							TreeNode node = new TreeNode();
							node.context=context;
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
				walkTree(rootNode,writer,pathPrefix);
			}
			
			writer.docPagePackageDescription(title, "Tree","All Language elements as tree.");
			writer.docPageContentEnd();
			writeEnd(conf,writer,context);
		} finally {
			writer.closeWriterSafe();
		}
	}
	
	class TreeNode {
		X4OLanguageContext context;
		X4OLanguageModule module;
		ElementNamespaceContext namespace;
		ElementClass elementClass;
		TreeNode parent;
		int indent = 0;
	}
	
	private void walkTree(TreeNode node,ContentWriterDoc writer,String pathPrefix) throws SAXException {
		String href = toElementUri(pathPrefix,node.module,node.namespace,node.elementClass);
		
		writer.printTagStart(Tag.ul);
		writer.printTagStart(Tag.li,null,null,"circle");
		writer.characters(node.namespace.getId());
		writer.characters(":");
		writer.printHref(href, node.elementClass.getId(), node.elementClass.getId(), "strong");
		writer.printTagEnd(Tag.li);
		
		List<TreeNode> childs = findChilderen(node);
		for (TreeNode child:childs) {
			walkTree(child,writer,pathPrefix);
		}
		writer.printTagEnd(Tag.ul);
	}
	
	private List<TreeNode> findChilderen(TreeNode node) {
		List<TreeNode> result = new ArrayList<TreeNode>(10);
		
		if (node.indent>20) {
			return result; // hard fail limit
		}
		for (X4OLanguageModule mod:node.context.getLanguage().getLanguageModules()) {
			for (ElementNamespaceContext ns:mod.getElementNamespaceContexts()) {
				for (ElementClass ec:ns.getElementClasses()) {
					TreeNode n=null;
					List<String> tags = ec.getElementParents(node.namespace.getUri());
					if (tags!=null && tags.contains(node.elementClass.getId())) {
						n = new TreeNode();
						n.context=node.context;
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
						for (ElementInterface ei:node.context.getLanguage().findElementInterfaces(ec.getObjectClass())) {
							List<String> eiTags = ei.getElementParents(node.namespace.getUri());
							if (eiTags!=null && eiTags.contains(node.elementClass.getId())) {
								n = new TreeNode();
								n.context=node.context;
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
						List<ElementBindingHandler> binds = node.context.getLanguage().findElementBindingHandlers(node.elementClass.getObjectClass(), ec.getObjectClass());
						if (binds.isEmpty()==false) {
							n = new TreeNode();
							n.context=node.context;
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
	
	private List<TreeNode> findParents(TreeNode node) {
		List<TreeNode> result = new ArrayList<TreeNode>(10);
		TreeNode n=null;
		for (X4OLanguageModule mod:node.context.getLanguage().getLanguageModules()) {
			for (ElementNamespaceContext ns:mod.getElementNamespaceContexts()) {
				
				List<String> tags = node.elementClass.getElementParents(ns.getUri());
				if (tags!=null) {
					for (ElementClass ec:ns.getElementClasses()) {
						if (tags.contains(ec.getId())) {
							n = new TreeNode();
							n.context=node.context;
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
						for (ElementInterface ei:node.context.getLanguage().findElementInterfaces(node.elementClass.getObjectClass())) {
							List<String> eiTags = ei.getElementParents(ns.getUri());
							if (eiTags!=null && eiTags.contains(ec.getId())) {
								n = new TreeNode();
								n.context=node.context;
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
					List<ElementBindingHandler> binds = node.context.getLanguage().findElementBindingHandlers(ec.getObjectClass(),node.elementClass.getObjectClass());
					if (binds.isEmpty()==false) {
						n = new TreeNode();
						n.context=node.context;
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
	
	
	public void writeOverviewModule(File basePath,X4OLanguageModule mod,X4OLanguageContext context) throws SAXException {
		File outputFile = createOutputPathFile(basePath,mod.getId(),"index.html");
		ContentWriterDoc writer = createContentWriterDoc(outputFile);
		String pathPrefix = "../";
		NavBarConfig conf = createNavBarConfig(pathPrefix, outputFile, context, writer);
		conf.navSelected="module";
		try {
			String title = "Overview ("+mod.getId()+")";
			writeStart(conf,writer,context,title);
			writer.docPagePackageTitle(title, mod.getDescription());
			writer.docPageContentStart();
			writer.docPageBlockStart();
			
			String pathPrefixModule = pathPrefix+toSafeUri(mod.getId())+"/";
			
			printElementInterfaces(writer,mod.getElementInterfaces(),pathPrefixModule);
			writer.docPageBlockNext();
			
			printAttributeHandlers(writer,mod.getElementAttributeHandlers(),pathPrefixModule);
			writer.docPageBlockNext();
			
			printBindingHandlers(writer,mod.getElementBindingHandlers(),pathPrefixModule);
			writer.docPageBlockNext();
			
			printConfiguratorGlobals(writer, mod.getElementConfiguratorGlobals(),pathPrefixModule);
			writer.docPageBlockNext();
			
			printNamespaces(writer, mod.getElementNamespaceContexts(),pathPrefix,mod);
			
			writer.docPageBlockEnd();
			writer.docPagePackageDescription(title, "Module","All language options this modules provides.");
			writer.docPageContentEnd();
			writeEnd(conf,writer,context);
		} finally {
			writer.closeWriterSafe();
		}
	}
	
	public void writeOverviewElement(File basePath,ElementNamespaceContext ns,X4OLanguageModule mod,X4OLanguageContext context) throws SAXException {
		File outputFile = createOutputPathFile(basePath,mod.getId(),ns.getId(),"index.html");
		ContentWriterDoc writer = createContentWriterDoc(outputFile);
		String pathPrefix = "../../";
		NavBarConfig conf = createNavBarConfig(pathPrefix, outputFile, context, writer);
		conf.navSelected="namespace";
		try {
			String title = "Overview ("+ns.getUri()+")";
			writeStart(conf,writer,context,title);
			writer.docPagePackageTitle(title, ns.getDescription());
			writer.docPageContentStart();
			
			List<ElementClass> ecs = ns.getElementClasses();
			Collections.sort(ecs,new ElementClassComparator());
			
			writer.docTableStart("Element Summary", "All Element Information Overview");
			writer.docTableHeader("Tag", "Name");
			for (ElementClass ec:ecs) {
				writer.docTableRowHref(toSafeUri(ec.getId())+"/index.html",ec.getId(),ec.getDescription(),null);
			}
			writer.docTableEnd();
			
			writer.docPagePackageDescription(title, "Module","All language options this modules provides.");
			writer.docPageContentEnd();
			writeEnd(conf,writer,context);
		} finally {
			writer.closeWriterSafe();
		}
	}
	
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
	
	public void writeElementDescription(ContentWriterDoc writer,String pathPrefix,TreeNode node) throws SAXException {
		writer.printTagStart(Tag.div, "description");
		writer.docPageBlockStart();
		
		List<TreeNode> parents = findParents(node);
		writer.printTagStart(Tag.dl);
			writer.printTagText(Tag.dt,"Element Parents:");
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
		
		List<TreeNode> childs = findChilderen(node);
		writer.printTagStart(Tag.dl);
			writer.printTagText(Tag.dt,"Element Childeren:");
			writer.printTagStart(Tag.dd);
				if (parents.isEmpty()) {
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
		
		writer.printTagStartEnd(Tag.hr);
		writer.printTagStartEnd(Tag.br);
		
		writer.printTagText(Tag.div, node.elementClass.getDescription(), "block");
		
		writer.docPageBlockEnd();
		writer.printTagEnd(Tag.div); // description
	}
	
	
	public void writeElement(File basePath,ElementClass ec,ElementNamespaceContext ns,X4OLanguageModule mod,X4OLanguageContext context) throws SAXException {
		File outputFile = createOutputPathFile(basePath,mod.getId(),ns.getId(),ec.getId(),"index.html");
		ContentWriterDoc writer = createContentWriterDoc(outputFile);
		String pathPrefix = "../../../";
		NavBarConfig conf = createNavBarConfig(pathPrefix, outputFile, context, writer);
		conf.navSelected="element";
		conf.linkDetails=true;
		conf.linkConstructors=true;
		conf.linkFields=true;
		conf.linkMethods=true;
		try {
			String title = "Element "+ns.getId()+":"+ec.getId();
			
			TreeNode node = new TreeNode();
			node.context=context;
			node.module=mod;
			node.namespace=ns;
			node.elementClass=ec;
			
			
			
			writeStart(conf,writer,context,title);
			writer.docPageClassStart(title, ns.getId()+":"+ ns.getUri());
			writer.docPageContentStart();
			
			writeElementDescription(writer, pathPrefix, node);
			
			writer.printTagStart(Tag.div, "summary");
			writer.docPageBlockStart();
			
			writer.docPageBlockStart("Tag Summary","constructor_summary","======== TAG SUMMARY ========");
				writer.docTableStart("Element Properties", "Element Property Overview");
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
			writer.docPageBlockEnd();
			
			writer.docPageBlockStart("Attribute Summary","method_summary","======== ATTRIBUTE SUMMARY ========");
				printElementAttributes(writer,ec.getElementClassAttributes());
				//writer.docPageBlockNext();
				if (ec.getObjectClass()!=null) {
					printClassProperties(writer, ec.getObjectClass());
				}
			writer.docPageBlockEnd();
			
			
			writer.docPageBlockStart("Config Summary","field_summary","======== CONFIG SUMMARY ========");
				printConfigurators(writer,ec.getElementConfigurators(),"");
			writer.docPageBlockEnd();
			
			writer.docPageBlockEnd();
			writer.printTagEnd(Tag.div); // summary 
			
			
			writer.printTagStart(Tag.div, "detail");
			writer.docPageBlockStart();

			writer.docPageBlockStart("Tag Detail","constructor_detail","======== TAG DETAIL ========");
			writer.docPageBlockEnd();
			
			writer.docPageBlockStart("Attribute Detail","method_detail","======== ATTRIBUTE DETAIL ========");
			writer.docPageBlockEnd();

			writer.docPageBlockStart("Config Detail","field_detail","======== CONFIG DETAIL ========");
			writer.docPageBlockEnd();
			
			writer.docPageBlockEnd();
			writer.printTagEnd(Tag.div); // details
			
			writer.docPageContentEnd();
			writer.docPageClassEnd();
			writeEnd(conf,writer,context);
		} finally {
			writer.closeWriterSafe();
		}
	}
	
	public void writeElementInterface(File basePath,ElementInterface iface,X4OLanguageModule mod,X4OLanguageContext context) throws SAXException {
		File outputFile = createOutputPathFile(basePath,mod.getId(),"interface",iface.getId()+".html");
		ContentWriterDoc writer = createContentWriterDoc(outputFile);
		String pathPrefix = "../../";
		NavBarConfig conf = createNavBarConfig(pathPrefix, outputFile, context, writer);
		String pathLocal = toSafeUri(iface.getId())+"/";
		try {
			String title = "ElementInterface ("+iface.getId()+")";
			writeStart(conf,writer,context,title);
			writer.docPagePackageTitle(title, mod.getDescription());
			writer.docPageContentStart();
			writer.docPageBlockStart();
			
			printConfigurators(writer,iface.getElementConfigurators(),pathLocal);
			writer.docPageBlockNext();
			
			printBindingHandlers(writer,iface.getElementBindingHandlers(),pathLocal);
			writer.docPageBlockNext();
			
			printElementAttributes(writer,iface.getElementClassAttributes());
			writer.docPageBlockNext();
			
			printBeanProperties(writer, iface);
			writer.docPageBlockNext();
			
			writer.docPageBlockEnd();
			writer.docPagePackageDescription(title, "ElementInterface","All language interfaces this modules provides.");
			writer.docPageContentEnd();
			writeEnd(conf,writer,context);
		} finally {
			writer.closeWriterSafe();
		}
	}
	
	public void writeElementConfigurator(File basePath,ElementConfigurator config,X4OLanguageModule mod,X4OLanguageContext context) throws SAXException {
		File outputFile = createOutputPathFile(basePath,mod.getId(),"conf",config.getId()+".html");
		ContentWriterDoc writer = createContentWriterDoc(outputFile);
		String pathPrefix = "../../";
		NavBarConfig conf = createNavBarConfig(pathPrefix, outputFile, context, writer);
		try {
			String title = "Configurator ("+config.getId()+")";
			writeStart(conf,writer,context,title);
			writer.docPagePackageTitle(title, config.getDescription());
			writer.docPageContentStart();
			
			printBeanProperties(writer, config);
			
			writer.docPagePackageDescription(title, "Element","All meta information about the xml element.");
			writer.docPageContentEnd();
			writeEnd(conf,writer,context);
		} finally {
			writer.closeWriterSafe();
		}
	}
	
	public void writeElementConfigurator(File basePath,ElementConfigurator config,X4OLanguageModule mod,ElementInterface iface,X4OLanguageContext context) throws SAXException {
		File outputFile = createOutputPathFile(basePath,mod.getId(),"interface",iface.getId(),"conf",config.getId()+".html");
		ContentWriterDoc writer = createContentWriterDoc(outputFile);
		String pathPrefix = "../../../../";
		NavBarConfig conf = createNavBarConfig(pathPrefix, outputFile, context, writer);
		try {
			String title = "Interface Configurator ("+config.getId()+")";
			writeStart(conf,writer,context,title);
			writer.docPagePackageTitle(title, config.getDescription());
			writer.docPageContentStart();
			
			printBeanProperties(writer, config);
			
			writer.docPagePackageDescription(title, "Element","All meta information about the xml element.");
			writer.docPageContentEnd();
			writeEnd(conf,writer,context);
		} finally {
			writer.closeWriterSafe();
		}
	}
	
	public void writeElementConfigurator(File basePath,ElementConfigurator config,X4OLanguageModule mod,ElementNamespaceContext ns,ElementClass ec,X4OLanguageContext context) throws SAXException {
		File outputFile = createOutputPathFile(basePath,mod.getId(),ns.getId(),ec.getId(),"conf",config.getId()+".html");
		ContentWriterDoc writer = createContentWriterDoc(outputFile);
		String pathPrefix = "../../../../";
		NavBarConfig conf = createNavBarConfig(pathPrefix, outputFile, context, writer);
		try {
			String title = "Interface Configurator ("+config.getId()+")";
			writeStart(conf,writer,context,title);
			writer.docPagePackageTitle(title, config.getDescription());
			writer.docPageContentStart();
			
			printBeanProperties(writer, config);
			
			writer.docPagePackageDescription(title, "Interface Configurator","All meta information about the interface.");
			writer.docPageContentEnd();
			writeEnd(conf,writer,context);
		} finally {
			writer.closeWriterSafe();
		}
	}
	
	public void writeBindingHandler(File basePath,ElementBindingHandler bind,X4OLanguageModule mod,X4OLanguageContext context) throws SAXException {
		File outputFile = createOutputPathFile(basePath,mod.getId(),"bind",bind.getId()+".html");
		ContentWriterDoc writer = createContentWriterDoc(outputFile);
		String pathPrefix = "../../";
		NavBarConfig conf = createNavBarConfig(pathPrefix, outputFile, context, writer);
		try {
			String title = "BindingHandler "+bind.getId();
			writeStart(conf,writer,context,title);
			writer.docPagePackageTitle(title, bind.getDescription());
			writer.docPageContentStart();
			writer.docPageBlockStart();
			/*
			printTableStart(pw,"Child Classes");
			for (Class<?> clazz:bind.getBindChildClasses()) {
				printTableRowSummary(pw,"class",""+clazz.getName());
			}
			printTableEnd(pw);
			*/
			printBeanProperties(writer, bind);
			
			writer.docPageBlockEnd();
			writer.docPagePackageDescription(title, "Element","All meta information about the xml element.");
			writer.docPageContentEnd();
			writeEnd(conf,writer,context);
		} finally {
			writer.closeWriterSafe();
		}
	}
	
	public void writeBindingHandler(File basePath,ElementBindingHandler bind,X4OLanguageModule mod,ElementInterface iface,X4OLanguageContext context) throws SAXException {
		File outputFile = createOutputPathFile(basePath,mod.getId(),"interface",iface.getId(),"bind",bind.getId()+".html");
		ContentWriterDoc writer = createContentWriterDoc(outputFile);
		String pathPrefix = "../../../../";
		NavBarConfig conf = createNavBarConfig(pathPrefix, outputFile, context, writer);
		try {
			String title = "Interface BindingHandler ("+bind.getId()+")";
			writeStart(conf,writer,context,title);
			writer.docPagePackageTitle(title, bind.getDescription());
			writer.docPageContentStart();
			writer.docPageBlockStart();
			
			writer.docTableStart("Child Classes", "Child classes overview.");
			writer.docTableHeader("Name", "Value");
			for (Class<?> clazz:bind.getBindChildClasses()) {
				writer.docTableRow("class",""+clazz.getName());
			}
			writer.docTableEnd();
			writer.docPageBlockNext();
			printBeanProperties(writer, bind);
			
			writer.docPageBlockEnd();
			writer.docPagePackageDescription(title, "Element","All meta information about the xml element.");
			writer.docPageContentEnd();
			writeEnd(conf,writer,context);
		} finally {
			writer.closeWriterSafe();
		}
	}
	
	public void writeAttributeHandler(File basePath,ElementAttributeHandler attr,X4OLanguageModule mod,X4OLanguageContext context) throws SAXException {
		File outputFile = createOutputPathFile(basePath,mod.getId(),"attr",attr.getId()+".html");
		ContentWriterDoc writer = createContentWriterDoc(outputFile);
		String pathPrefix = "../../";
		NavBarConfig conf = createNavBarConfig(pathPrefix, outputFile, context, writer);
		try {
			String title = "AttributeHandler ("+attr.getId()+")";
			writeStart(conf,writer,context,title);
			writer.docPagePackageTitle(title, attr.getDescription());
			writer.docPageContentStart();
			
			printBeanProperties(writer, attr);
			
			writer.docPagePackageDescription(title, "Element","All meta information about the xml element.");
			writer.docPageContentEnd();
			writeEnd(conf,writer,context);
		} finally {
			writer.closeWriterSafe();
		}
	}
	
	private void printClassProperties(ContentWriterDoc writer,Class<?> beanClass) throws SAXException {
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
	
	private void printBeanProperties(ContentWriterDoc writer,Object bean) throws SAXException {
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
	
	private void printElementAttributes(ContentWriterDoc writer,Collection<ElementClassAttribute> elementClassAttributes) throws SAXException {
		writer.docTableStart("Element Attributes", "All Element Attributes Overview");
		writer.docTableHeader("URI", "Name");
		for (ElementClassAttribute attr:elementClassAttributes) {
			writer.docTableRow(attr.getId(),attr.getDescription());
		}
		writer.docTableEnd();
	}
	
	private void printNamespaces(ContentWriterDoc writer,List<ElementNamespaceContext> namespaces,String pathPrefix ,X4OLanguageModule mod) throws SAXException {
		writer.docTableStart("Namespace Summary", "All Language Namespaces Overview");
		writer.docTableHeader("ID", "URI");
		for (ElementNamespaceContext ns:namespaces) {
			writer.docTableRowHref(pathPrefix+toSafeUri(mod.getId())+"/"+toSafeUri(ns.getId())+"/index.html",ns.getId(),ns.getUri(),null);
		}
		writer.docTableEnd();
	}
	
	private void printConfigurators(ContentWriterDoc writer,List<ElementConfigurator> confs,String pathPrefix) throws SAXException {
		writer.docTableStart("Configurators", "All module provided element configurators.");
		writer.docTableHeader("ID", "Name");
		for (ElementConfigurator conf:confs) {
			writer.docTableRowHref(pathPrefix+"conf/"+toSafeUri(conf.getId())+".html",conf.getId(),conf.getDescription(),null);
		}
		writer.docTableEnd();
	}
	
	private void printConfiguratorGlobals(ContentWriterDoc writer,List<ElementConfiguratorGlobal> confs,String pathPrefix) throws SAXException {
		writer.docTableStart("Configurators Global", "All module provided element configurators.");
		writer.docTableHeader("ID", "Name");
		for (ElementConfigurator conf:confs) {
			writer.docTableRowHref(pathPrefix+"conf/"+toSafeUri(conf.getId())+".html",conf.getId(),conf.getDescription(),null);
		}
		writer.docTableEnd();
	}
	
	private void printBindingHandlers(ContentWriterDoc writer,List<ElementBindingHandler> binds,String pathPrefix) throws SAXException {
		writer.docTableStart("Binding Handlers", "All module provided binding handlers.");
		writer.docTableHeader("ID", null);
		for (ElementBindingHandler bind:binds) {
			writer.docTableRowHref(pathPrefix+"bind/"+toSafeUri(bind.getId())+".html",bind.getId(),null,null);
		}
		writer.docTableEnd();
	}
	
	private void printAttributeHandlers(ContentWriterDoc writer,List<ElementAttributeHandler> attrs,String pathPrefix) throws SAXException {
		writer.docTableStart("Attribute Handlers", "All module provided attribite handlers.");
		writer.docTableHeader("ID", "Name");
		for (ElementAttributeHandler attr:attrs) {
			writer.docTableRowHref(pathPrefix+"attr/"+toSafeUri(attr.getId())+".html",attr.getId(),attr.getDescription(),null);
		}
		writer.docTableEnd();
	}
	
	private void printElementInterfaces(ContentWriterDoc writer,List<ElementInterface> ifaces,String pathPrefix) throws SAXException {
		writer.docTableStart("Element Interfaces", "All module provided element interfaces.");
		writer.docTableHeader("ID", "Name");
		for (ElementInterface iface:ifaces) {
			writer.docTableRowHref(pathPrefix+"interface/"+toSafeUri(iface.getId())+".html",""+iface.getId(),iface.getDescription(),null);
		}
		writer.docTableEnd();
	}
	
	class ElementLanguageModuleComparator implements Comparator<X4OLanguageModule> {
		public int compare(X4OLanguageModule o1,X4OLanguageModule o2) {
			return o1.getId().compareTo(o2.getId());
		}
	}
	
	class TreeNodeComparator implements Comparator<TreeNode> {
		public int compare(TreeNode o1,TreeNode o2) {
			return o1.elementClass.getId().compareTo(o2.elementClass.getId());
		}
	}
	
	class ElementClassComparator implements Comparator<ElementClass> {
		public int compare(ElementClass o1,ElementClass o2) {
			return o1.getId().compareTo(o2.getId());
		}
	}
}
