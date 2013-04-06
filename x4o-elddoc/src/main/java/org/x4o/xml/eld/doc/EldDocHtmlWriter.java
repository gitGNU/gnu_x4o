/*
 * Copyright (c) 2004-2012, Willem Cazander
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
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.x4o.xml.element.ElementAttributeHandler;
import org.x4o.xml.element.ElementBindingHandler;
import org.x4o.xml.element.ElementClass;
import org.x4o.xml.element.ElementClassAttribute;
import org.x4o.xml.element.ElementConfigurator;
import org.x4o.xml.element.ElementConfiguratorGlobal;
import org.x4o.xml.element.ElementInterface;
import org.x4o.xml.element.ElementNamespaceContext;
import org.x4o.xml.lang.X4OLanguageModule;
import org.x4o.xml.lang.X4OLanguageContext;

/**
 * EldDocHtmlWriter writes simple eld documentation.
 * 
 * 
 * 
 * @author Willem Cazander
 * @version 1.0 Aug 26, 2010
 */
public class EldDocHtmlWriter {

	private static final String TAB = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
	
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
	
	private PrintWriter createPrintWriter(File basePath,String...argu) throws FileNotFoundException {
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
		PrintWriter pw = new PrintWriter(outputFile);
		return pw;
	}
	
	public void writeStylesheet(File basePath) throws IOException {
		PrintWriter pw = createPrintWriter(basePath,"stylesheet.css");
		try {
			pw.write("\n\n");
			pw.flush();
			pw.print("body { background-color: #FFFFFF; color:#000000 }\n");
			pw.print("h1 { font-size: 145%;text-align: center; }\n");
			pw.print(".Copyright             { font-size: 70%;text-align: center; }\n");
			pw.print(".TableHeadingColor     { background: #CCCCFF; color:#000000;font-weight: bold;font-size: 110%; }\n");
			pw.print(".TableSubHeadingColor  { background: #EEEEFF; color:#000000 }\n");
			pw.print(".TableRowColor         { background: #FFFFFF; color:#000000 }\n");
			pw.print(".NavBarCell1    { background-color:#EEEEFF; color:#000000}\n");
			pw.print(".NavBarCell1Rev { background-color:#00008B; color:#FFFFFF}\n");
			pw.print(".NavBarCell1 a  { font-size: 120%;font-weight: bold;}\n");
			pw.print(".NavBarCell2    { font-size: 80%;font-family: Arial, Helvetica, sans-serif; background-color:#FFFFFF; color:#000000}\n");
			pw.print(".NavBarCell3    { font-family: Arial, Helvetica, sans-serif; background-color:#FFFFFF; color:#000000}\n");
			pw.flush();
		} finally {
			if (pw!=null) {
				pw.close();
			}
		}
	}
	
	public void writeIndex(File basePath,X4OLanguageContext context) throws IOException {
		PrintWriter pw = createPrintWriter(basePath,"index.html");
		try {
			String title = context.getLanguage().getLanguageName()+" "+context.getLanguage().getLanguageVersion()+" ELD";
			printHeader(pw,"Index ("+title+")","");
			printPageIndexTitle(pw,title,null,null);
			
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
			
			pw.print("<p>Welcome to the EldDocs</p>");
			printTableStart(pw,"Language Stats");
			printTableRowSummary(pw,"Language:",""+context.getLanguage().getLanguageName());
			printTableRowSummary(pw,"LanguageVersion:",""+context.getLanguage().getLanguageVersion());
			printTableRowSummary(pw,"Modules:",""+context.getLanguage().getLanguageModules().size());
			printTableRowSummary(pw,"Namespaces:",""+namespaces);
			printTableRowSummary(pw,"Elements:",""+elements);
			printTableRowSummary(pw,"ElementInterfaces:",""+interFaces);
			printTableRowSummary(pw,"ElementAttributeHandlers:",""+attrHandlers);
			printTableRowSummary(pw,"ElementBindingHandlers:",""+bindHandlers);
			printTableRowSummary(pw,"ElementConfigurators:",""+eleConfigs);
			printTableEnd(pw);
			
			printBottom(pw,"");
		} finally {
			if (pw!=null) {
				pw.close();
			}
		}
	}
	
	public void writeOverviewModule(File basePath,X4OLanguageContext context) throws IOException {
		PrintWriter pw = createPrintWriter(basePath,"module-overview.html");
		try {
			String title = context.getLanguage().getLanguageName()+" "+context.getLanguage().getLanguageVersion()+" ELD";
			printHeader(pw,"Overview Modules ("+title+")","");
			printPageIndexTitle(pw,title,null,null);
			printTableStart(pw,"Modules");
			List<X4OLanguageModule> mods = context.getLanguage().getLanguageModules();
			Collections.sort(mods,new ElementLanguageModuleComparator());
			for (X4OLanguageModule mod:mods) {
				printTableRowOverview(pw,toSafeUri(mod.getId())+"/index.html",mod.getId(),mod.getName());
			}
			printTableEnd(pw);
			printBottom(pw,"");
		} finally {
			if (pw!=null) {
				pw.close();
			}
		}
	}
	
	public void writeOverviewNamespace(File basePath,X4OLanguageContext context) throws IOException {
		PrintWriter pw = createPrintWriter(basePath,"namespace-overview.html");
		String pathPrefix = "";
		try {
			String title = context.getLanguage().getLanguageName()+" "+context.getLanguage().getLanguageVersion()+" ELD";
			printHeader(pw,"Overview Namespace("+title+")",pathPrefix);
			printPageIndexTitle(pw,title,null,null);
			List<X4OLanguageModule> mods = context.getLanguage().getLanguageModules();
			Collections.sort(mods,new ElementLanguageModuleComparator());
			for (X4OLanguageModule mod:mods) {
				printNamespaces(pw,mod.getElementNamespaceContexts(),pathPrefix,mod);
			}
			printBottom(pw,pathPrefix);
		} finally {
			if (pw!=null) {
				pw.close();
			}
		}
	}
	
	public void writeOverviewTree(File basePath,X4OLanguageContext context) throws IOException {
		PrintWriter pw = createPrintWriter(basePath,"tree-overview.html");
		String pathPrefix = "";
		try {
			String title = context.getLanguage().getLanguageName()+" "+context.getLanguage().getLanguageVersion()+" ELD";
			printHeader(pw,"Overview Tree ("+title+")",pathPrefix);
			printPageIndexTitle(pw,title,null,null);
			
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
				walkTree(rootNode,pw,pathPrefix);
			}
			
			printBottom(pw,pathPrefix);
		} finally {
			if (pw!=null) {
				pw.close();
			}
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
	
	private void walkTree(TreeNode node,PrintWriter pw,String pathPrefix) {
		
		for (int i=0;i<node.indent;i++) {
			pw.print(TAB);
		}
		pw.print("-&nbsp;<a href=\"");
		pw.print(pathPrefix);
		pw.print(toSafeUri(node.module.getId()));
		pw.print("/");
		pw.print(toSafeUri(node.namespace.getId()));
		pw.print("/");
		pw.print(toSafeUri(node.elementClass.getTag()));
		pw.print("/index.html\">");
		pw.print(node.namespace.getId());
		pw.print(":");
		pw.print(node.elementClass.getTag());
		pw.print("</a><br/>\n");
		
		List<TreeNode> childs = findChilderen(node);
		for (TreeNode child:childs) {
			walkTree(child,pw,pathPrefix);
		}
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
					if (tags!=null && tags.contains(node.elementClass.getTag())) {
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
							if (eiTags!=null && eiTags.contains(node.elementClass.getTag())) {
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
				node.elementClass.getTag().equals(checkNode.elementClass.getTag())
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
						if (tags.contains(ec.getTag())) {
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
							if (eiTags!=null && eiTags.contains(ec.getTag())) {
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
	
	
	public void writeOverviewModule(File basePath,X4OLanguageModule mod) throws IOException {
		PrintWriter pw = createPrintWriter(basePath,mod.getId(),"index.html");
		String pathPrefix = "../";
		try {
			printHeader(pw,"Overview ("+mod.getId()+")",pathPrefix);
			printPageTitle(pw,"Module",mod.getName(),mod.getDescription());

			String pathPrefixModule = pathPrefix+toSafeUri(mod.getId())+"/";
			
			printElementInterfaces(pw,mod.getElementInterfaces(),pathPrefixModule);
			printAttributeHandlers(pw,mod.getElementAttributeHandlers(),pathPrefixModule);
			printBindingHandlers(pw,mod.getElementBindingHandlers(),pathPrefixModule);
			printConfiguratorGlobals(pw, mod.getElementConfiguratorGlobals(),pathPrefixModule);
			printNamespaces(pw, mod.getElementNamespaceContexts(),pathPrefix,mod);
			
			printBottom(pw,pathPrefix);
		} finally {
			if (pw!=null) {
				pw.close();
			}
		}
	}
	
	public void writeOverviewElement(File basePath,ElementNamespaceContext ns,X4OLanguageModule mod) throws IOException {
		PrintWriter pw = createPrintWriter(basePath,mod.getId(),ns.getId(),"index.html");
		String pathPrefix = "../../";
		try {
			String title = ns.getUri();
			printHeader(pw,"Overview ("+title+")",pathPrefix);
			printPageTitle(pw,"Namespace",ns.getUri(),ns.getDescription());
			
			printTableStart(pw,"Element Summary");
			List<ElementClass> ecs = ns.getElementClasses();
			Collections.sort(ecs,new ElementClassComparator());
			for (ElementClass ec:ecs) {
				printTableRowOverview(pw,toSafeUri(ec.getTag())+"/index.html",ec.getTag(),ec.getDescription());
			}
			printTableEnd(pw);
			printBottom(pw,pathPrefix);
		} finally {
			if (pw!=null) {
				pw.close();
			}
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
	
	
	public void writeElement(File basePath,ElementClass ec,ElementNamespaceContext ns,X4OLanguageModule mod,X4OLanguageContext context) throws IOException {
		PrintWriter pw = createPrintWriter(basePath,mod.getId(),ns.getId(),ec.getTag(),"index.html");
		String pathPrefix = "../../../";
		try {
			printHeader(pw,"Tag ("+ec.getTag()+")",pathPrefix);
			printPageTitle(pw,"Tag",ec.getTag(),ec.getDescription());
			
			TreeNode node = new TreeNode();
			node.context=context;
			node.module=mod;
			node.namespace=ns;
			node.elementClass=ec;
			
			List<TreeNode> parents = findParents(node);
			printTableStart(pw,"Element Parents");
			pw.print("\t<tr class=\"TableRowColor\">\n");
			pw.print("\t\t<td colspan=\"2\">");
			if (parents.isEmpty()) {
				pw.print("No parent.");
			}
			for (TreeNode n:parents) {
				pw.print("\t\t\t&nbsp;<a href=\"");
				pw.print(pathPrefix);
				pw.print(toSafeUri(n.module.getId()));
				pw.print("/");
				pw.print(toSafeUri(n.namespace.getId()));
				pw.print("/");
				pw.print(toSafeUri(n.elementClass.getTag()));
				pw.print("/index.html\">");
				pw.print(n.namespace.getId());
				pw.print(":");
				pw.print(n.elementClass.getTag());
				pw.print("</a>\n");
			}
			pw.print("</td>\n");
			pw.print("\t</tr>\n");
			printTableEnd(pw);
			
			List<TreeNode> childs = findChilderen(node);
			printTableStart(pw,"Element Childeren");
			pw.print("\t<tr class=\"TableRowColor\">\n");
			pw.print("\t\t<td colspan=\"2\">");
			if (childs.isEmpty()) {
				pw.print("No childeren.");
			}
			for (TreeNode n:childs) {
				pw.print("\t\t\t&nbsp;<a href=\"");
				pw.print(pathPrefix);
				pw.print(toSafeUri(n.module.getId()));
				pw.print("/");
				pw.print(toSafeUri(n.namespace.getId()));
				pw.print("/");
				pw.print(toSafeUri(n.elementClass.getTag()));
				pw.print("/index.html\">");
				pw.print(n.namespace.getId());
				pw.print(":");
				pw.print(n.elementClass.getTag());
				pw.print("</a>\n");
			}
			pw.print("</td>\n");
			pw.print("\t</tr>\n");
			printTableEnd(pw);
			
			
			printTableStart(pw,"Element Properties");
			printTableRowSummary(pw,"id",""+ec.getId());
			printTableRowSummary(pw,"tag",""+ec.getTag());
			printTableRowSummary(pw,"objectClass",""+ec.getObjectClass());
			printTableRowSummary(pw,"elementClass",""+ec.getElementClass());
			printTableRowSummary(pw,"autoAttributes",""+ec.getAutoAttributes());
			printTableRowSummary(pw,"skipPhases",printList(ec.getSkipPhases()));
			printTableRowSummary(pw,"schemaContentBase",""+ec.getSchemaContentBase());
			printTableRowSummary(pw,"schemaContentComplex",""+ec.getSchemaContentComplex());
			printTableRowSummary(pw,"schemaContentMixed",""+ec.getSchemaContentMixed());
			//printTableRowSummary(pw,"description",""+ec.getDescription());
			printTableEnd(pw);
			printElementAttributes(pw,ec.getElementClassAttributes());
			if (ec.getObjectClass()!=null) {
				printClassProperties(pw, ec.getObjectClass());
			}
			
			printConfigurators(pw,ec.getElementConfigurators(),"");
			printBottom(pw,pathPrefix);
		} finally {
			if (pw!=null) {
				pw.close();
			}
		}
	}
	
	public void writeElementInterface(File basePath,ElementInterface iface,X4OLanguageModule mod) throws IOException {
		PrintWriter pw = createPrintWriter(basePath,mod.getId(),"interface",iface.getId()+".html");
		String pathPrefix = "../../";
		String pathLocal = toSafeUri(iface.getId())+"/";
		try {
			printHeader(pw,"ElementInterface ("+iface.getId()+")",pathPrefix);
			printPageTitle(pw,"ElementInterface",iface.getId(),iface.getDescription());
			
			printConfigurators(pw,iface.getElementConfigurators(),pathLocal);
			printBindingHandlers(pw,iface.getElementBindingHandlers(),pathLocal);
			
			printElementAttributes(pw,iface.getElementClassAttributes());
			
			printBeanProperties(pw, iface);
			printBottom(pw,pathPrefix);
		} finally {
			if (pw!=null) {
				pw.close();
			}
		}
	}
	
	public void writeElementConfigurator(File basePath,ElementConfigurator conf,X4OLanguageModule mod) throws IOException {
		PrintWriter pw = createPrintWriter(basePath,mod.getId(),"conf",conf.getId()+".html");
		String pathPrefix = "../../";
		try {
			printHeader(pw,"Configurator ("+conf.getId()+")",pathPrefix);
			printPageTitle(pw,"Configurator",conf.getId(),conf.getDescription());
			
			printBeanProperties(pw, conf);
			printBottom(pw,pathPrefix);
		} finally {
			if (pw!=null) {
				pw.close();
			}
		}
	}
	
	public void writeElementConfigurator(File basePath,ElementConfigurator conf,X4OLanguageModule mod,ElementInterface iface) throws IOException {
		PrintWriter pw = createPrintWriter(basePath,mod.getId(),"interface",iface.getId(),"conf",conf.getId()+".html");
		String pathPrefix = "../../../../";
		try {
			printHeader(pw,"Interface Configurator ("+conf.getId()+")",pathPrefix);
			printPageTitle(pw,"Interface Configurator",conf.getId(),conf.getDescription());
			
			printBeanProperties(pw, conf);
			printBottom(pw,pathPrefix);
		} finally {
			if (pw!=null) {
				pw.close();
			}
		}
	}
	
	public void writeElementConfigurator(File basePath,ElementConfigurator conf,X4OLanguageModule mod,ElementNamespaceContext ns,ElementClass ec) throws IOException {
		PrintWriter pw = createPrintWriter(basePath,mod.getId(),ns.getId(),ec.getTag(),"conf",conf.getId()+".html");
		String pathPrefix = "../../../../";
		try {
			printHeader(pw,"Interface Configurator ("+conf.getId()+")",pathPrefix);
			printPageTitle(pw,"Interface Configurator",conf.getId(),conf.getDescription());
			
			printBeanProperties(pw, conf);
			printBottom(pw,pathPrefix);
		} finally {
			if (pw!=null) {
				pw.close();
			}
		}
	}
	
	public void writeBindingHandler(File basePath,ElementBindingHandler bind,X4OLanguageModule mod) throws IOException {
		PrintWriter pw = createPrintWriter(basePath,mod.getId(),"bind",bind.getId()+".html");
		String pathPrefix = "../../";
		try {
			printHeader(pw,"BindingHandler ("+bind.getId()+")",pathPrefix);
			printPageTitle(pw,"BindingHandler",bind.getId(),bind.getDescription());
			
			/*
			printTableStart(pw,"Child Classes");
			for (Class<?> clazz:bind.getBindChildClasses()) {
				printTableRowSummary(pw,"class",""+clazz.getName());
			}
			printTableEnd(pw);
			*/
			printBeanProperties(pw, bind);
			printBottom(pw,pathPrefix);
		} finally {
			if (pw!=null) {
				pw.close();
			}
		}
	}
	
	public void writeBindingHandler(File basePath,ElementBindingHandler bind,X4OLanguageModule mod,ElementInterface iface) throws IOException {
		PrintWriter pw = createPrintWriter(basePath,mod.getId(),"interface",iface.getId(),"bind",bind.getId()+".html");
		String pathPrefix = "../../../../";
		try {
			printHeader(pw,"Interface BindingHandler ("+bind.getId()+")",pathPrefix);
			printPageTitle(pw,"Interface BindingHandler",bind.getId(),bind.getDescription());
			
			printTableStart(pw,"Child Classes");
			for (Class<?> clazz:bind.getBindChildClasses()) {
				printTableRowSummary(pw,"class",""+clazz.getName());
			}
			printTableEnd(pw);
			printBeanProperties(pw, bind);
			printBottom(pw,pathPrefix);
		} finally {
			if (pw!=null) {
				pw.close();
			}
		}
	}
	
	public void writeAttributeHandler(File basePath,ElementAttributeHandler attr,X4OLanguageModule mod) throws IOException {
		PrintWriter pw = createPrintWriter(basePath,mod.getId(),"attr",attr.getId()+".html");
		String pathPrefix = "../../";
		try {
			printHeader(pw,"AttributeHandler ("+attr.getId()+")",pathPrefix);
			printPageTitle(pw,"AttributeHandler",attr.getId(),attr.getDescription());
			printBeanProperties(pw, attr);
			printBottom(pw,pathPrefix);
		} finally {
			if (pw!=null) {
				pw.close();
			}
		}
	}
	
	private void printClassProperties(PrintWriter pw,Class<?> beanClass) {
		printTableStart(pw,"Class Properties");
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
				printTableRowSummary(pw,n,""+type);
			}
		}
		printTableEnd(pw);
	}
	
	private void printBeanProperties(PrintWriter pw,Object bean) {
		printTableStart(pw,"Bean Properties");
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
				
				printTableRowSummary(pw,n,printValue(value));
			}
		}
		printTableEnd(pw);
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
	
	private void printElementAttributes(PrintWriter pw,Collection<ElementClassAttribute> elementClassAttributes) {
		printTableStart(pw,"Element Attributes");
		for (ElementClassAttribute attr:elementClassAttributes) {
			printTableRowSummary(pw,attr.getName(),attr.getDescription());
		}
		printTableEnd(pw);
	}
	
	private void printNamespaces(PrintWriter pw,List<ElementNamespaceContext> namespaces,String pathPrefix ,X4OLanguageModule mod) {
		printTableStart(pw,"Namespaces");
		for (ElementNamespaceContext ns:namespaces) {
			printTableRowOverview(pw,pathPrefix+toSafeUri(mod.getId())+"/"+toSafeUri(ns.getId())+"/index.html",ns.getId(),"<b>"+ns.getUri()+"</b><br/>"+TAB+ns.getName());
		}
		printTableEnd(pw);
	}
	
	private void printConfigurators(PrintWriter pw,List<ElementConfigurator> confs,String pathPrefix) {
		printTableStart(pw,"Configurators");
		for (ElementConfigurator conf:confs) {
			printTableRowOverview(pw,pathPrefix+"conf/"+toSafeUri(conf.getId())+".html",conf.getId(),conf.getDescription());
		}
		printTableEnd(pw);
	}
	
	private void printConfiguratorGlobals(PrintWriter pw,List<ElementConfiguratorGlobal> confs,String pathPrefix) {
		printTableStart(pw,"Configurators");
		for (ElementConfigurator conf:confs) {
			printTableRowOverview(pw,pathPrefix+"conf/"+toSafeUri(conf.getId())+".html",conf.getId(),conf.getDescription());
		}
		printTableEnd(pw);
	}
	
	private void printBindingHandlers(PrintWriter pw,List<ElementBindingHandler> binds,String pathPrefix) {
		printTableStart(pw,"Binding handlers");
		for (ElementBindingHandler bind:binds) {
			printTableRowOverview(pw,pathPrefix+"bind/"+toSafeUri(bind.getId())+".html",bind.getId(),bind.getDescription());
		}
		printTableEnd(pw);
	}
	
	private void printAttributeHandlers(PrintWriter pw,List<ElementAttributeHandler> attrs,String pathPrefix) {
		printTableStart(pw,"Attribute handlers");
		for (ElementAttributeHandler attr:attrs) {
			printTableRowOverview(pw,pathPrefix+"attr/"+toSafeUri(attr.getId())+".html",attr.getId(),attr.getDescription());
		}
		printTableEnd(pw);
	}
	
	private void printElementInterfaces(PrintWriter pw,List<ElementInterface> ifaces,String pathPrefix) {
		printTableStart(pw,"Element Interfaces");
		for (ElementInterface iface:ifaces) {
			printTableRowOverview(pw,pathPrefix+"interface/"+toSafeUri(iface.getId())+".html",""+iface.getId(),iface.getDescription());
		}
		printTableEnd(pw);
	}
	
	private void printTableStart(PrintWriter pw,String title) {
		pw.print("<table border=\"1\" width=\"100%\" cellpadding=\"3\" cellspacing=\"0\" summary=\"\">\n");
		pw.print("\t<tr class=\"TableHeadingColor\">\n");
		pw.print("\t\t<th align=\"left\" colspan=\"2\">\n");
		pw.print(title);
		pw.print("</th>\n\t</tr>\n");
	}
	
	private void printTableRowSummary(PrintWriter pw,String name,String description) {
		pw.print("\t<tr class=\"TableRowColor\">\n");
		pw.print("\t\t<td width=\"20%\">");
		pw.print(name);
		pw.print("</td>\n");
		pw.print("\t\t<td>");
		if (description!=null) {
			pw.print(description);
		}
		pw.print("\t\t</td>\n");
		pw.print("\t</tr>\n");
	}
	
	private void printTableRowOverview(PrintWriter pw,String href,String hrefTitle,String description) {
		pw.print("\t<tr class=\"TableRowColor\">\n");
		pw.print("\t\t<td width=\"20%\"><b><a href=\"\n");
		pw.print(href);
		pw.print("\">");
		pw.print(hrefTitle);
		pw.print("</a></td>\n");
		pw.print("\t\t<td>");
		if (description!=null) {
			pw.print(description);
		}
		pw.print("\t\t</td>\n");
		pw.print("\t</tr>\n");
	}
	
	private void printTableEnd(PrintWriter pw) {
		pw.print("</table>\n");
		pw.print("<br/>");
	}
	
	private void printPageTitle(PrintWriter pw,String title,String titleContext,String description) {
		pw.print("<h2>");
		pw.print(title);
		pw.print(" ");
		if (titleContext!=null) {
			pw.print(titleContext);
		}
		pw.print("</h2>\n");
		if (description!=null) {
			pw.print(description);
			pw.print("<br/>\n");
			pw.print("<br/>\n");
		}
	}
	
	private void printPageIndexTitle(PrintWriter pw,String title,String titleContext,String description) {
		pw.print("<h1>");
		pw.print(title);
		pw.print(" ");
		if (titleContext!=null) {
			pw.print(titleContext);
		}
		pw.print("</h1>\n");
		if (description!=null) {
			pw.print(description);
		}
	}
	
	private void printHeader(PrintWriter pw,String title,String pathPrefix) throws IOException {
		pw.print("\n");
		pw.print("<html>\n");
		pw.print("<head>\n");
		pw.print("<!-- Generated by ");
		pw.print(EldDocHtmlWriter.class.getSimpleName());
		pw.print(" on ");
		pw.print(new Date());
		pw.print("-->\n");
		pw.print("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>\n");
		pw.print("<title>");
		pw.print(title);
		pw.print("</title>\n");
		//pw.print("<meta name=\"date\" content=")
		pw.print("<link rel=\"stylesheet\" type=\"text/css\" href=\""+pathPrefix+"stylesheet.css\" title=\"Style\"/>\n");
		pw.print("</head>\n");
		pw.print("<body>\n");
		
		pw.print("<hr/>\n");
		
		pw.print("<a name=\"navbar_top\"><!-- --></a>\n");
		pw.print("<a href=\"#skip-navbar_top\" title=\"Skip navigation links\"></a>\n");
		pw.print("<table border=\"0\" width=\"100%\" cellpadding=\"1\" cellspacing=\"0\" summary=\"\">\n");
		pw.print("<tr>\n");
		pw.print("<td colspan=\"2\" class=\"NavBarCell1\">\n");
		pw.print("<a name=\"navbar_top_firstrow\"><!-- --></a>\n");
		
		pw.print("<table border=\"0\" cellpadding=\"1\" cellspacing=\"0\" summary=\"\">\n");
		pw.print("\t<tr align=\"center\" valign=\"top\">\n");
		printNavBar(pw,pathPrefix);
		pw.print("\t</tr>\n");
		pw.print("</table>\n");
		
		pw.print("</td>\n");
		pw.print("<td align=\"right\" valign=\"top\" rowspan=\"3\"><em>\n</em></td>");
		pw.print("</tr>\n");
		
		pw.print("<tr>\n");
		pw.print("<td class=\"NavBarCell2\">\n");
		pw.print("&nbsp;PREV&nbsp;&nbsp;NEXT</td>\n");
		pw.print("<td class=\"NavBarCell2\"></td>\n");
		pw.print("</tr>\n");
		
		pw.print("</table>\n");
		pw.print("<a name=\"skip-navbar_top\"></a>\n");
		pw.print("<hr/>\n");
		pw.print("\n");
		pw.flush();
	}
	
	private void printBottom(PrintWriter pw,String pathPrefix) throws IOException {
		
		pw.print("<hr/>\n");
		
		pw.print("<a name=\"navbar_bottom\"><!-- --></a>\n");
		pw.print("<a href=\"#skip-navbar_bottom\" title=\"Skip navigation links\"></a>\n");
		pw.print("<table border=\"0\" width=\"100%\" cellpadding=\"1\" cellspacing=\"0\" summary=\"\">\n");
		pw.print("<tr>\n");
		pw.print("<td colspan=\"2\" class=\"NavBarCell1\">\n");
		pw.print("<a name=\"navbar_bottom_firstrow\"><!-- --></a>\n");
		
		pw.print("<table border=\"0\" cellpadding=\"1\" cellspacing=\"0\" summary=\"\">\n");
		pw.print("\t<tr align=\"center\" valign=\"top\">\n");
		printNavBar(pw,pathPrefix);
		pw.print("\t</tr>\n");
		pw.print("</table>\n");
		
		pw.print("</td>\n");
		pw.print("<td align=\"right\" valign=\"top\" rowspan=\"3\"><em>\n</em></td>");
		pw.print("</tr>\n");
		
		pw.print("<tr>\n");
		pw.print("<td class=\"NavBarCell2\">\n");
		pw.print("&nbsp;PREV&nbsp;&nbsp;NEXT</td>\n");
		pw.print("<td class=\"NavBarCell2\"></td>\n");
		pw.print("</tr>\n");
		
		pw.print("</table>\n");
		pw.print("<a name=\"skip-navbar_bottom\"></a>\n");
		pw.print("<hr/>\n");
		pw.print("\n");
		
		pw.print("<div class=\"Copyright\">");
		pw.print("Copyright &#169; todo. All Rights Reserved.\n");
		pw.print("</div>\n");
		
		pw.print("\n");
		pw.print("</body>\n");
		pw.print("</html>\n\n");
		pw.flush();
	}
	
	private void printNavBar(PrintWriter pw,String pathPrefix) throws IOException {
		pw.print("\t\t<td class=\"NavBarCell1\"><a href=\""+pathPrefix+"index.html\">Index</a>&nbsp;</td>\n");
		pw.print("\t\t<td class=\"NavBarCell1\"><a href=\""+pathPrefix+"module-overview.html\">Modules</a>&nbsp;</td>\n");
		pw.print("\t\t<td class=\"NavBarCell1\"><a href=\""+pathPrefix+"namespace-overview.html\">Namespaces</a>&nbsp;</td>\n");
		pw.print("\t\t<td class=\"NavBarCell1\"><a href=\""+pathPrefix+"tree-overview.html\">Tree</a>&nbsp;</td>\n");
	}
	
	class ElementLanguageModuleComparator implements Comparator<X4OLanguageModule> {
		public int compare(X4OLanguageModule o1,X4OLanguageModule o2) {
			return o1.getId().compareTo(o2.getId());
		}
	}
	
	class TreeNodeComparator implements Comparator<TreeNode> {
		public int compare(TreeNode o1,TreeNode o2) {
			return o1.elementClass.getTag().compareTo(o2.elementClass.getTag());
		}
	}
	
	class ElementClassComparator implements Comparator<ElementClass> {
		public int compare(ElementClass o1,ElementClass o2) {
			return o1.getTag().compareTo(o2.getTag());
		}
	}
}
