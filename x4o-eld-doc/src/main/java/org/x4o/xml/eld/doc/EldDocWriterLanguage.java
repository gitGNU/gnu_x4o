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

import org.x4o.xml.eld.doc.api.ApiDocContentWriter;
import org.x4o.xml.eld.doc.api.ApiDocNodeWriterMethod;
import org.x4o.xml.eld.doc.api.dom.ApiDocNode;
import org.x4o.xml.eld.doc.api.dom.ApiDocNodeBody;
import org.x4o.xml.eld.doc.api.dom.ApiDocWriteEvent;
import org.x4o.xml.element.ElementNamespaceContext;
import org.x4o.xml.lang.X4OLanguageContext;
import org.x4o.xml.lang.X4OLanguageModule;
import org.xml.sax.SAXException;

/**
 * EldDocWriterLanguage writer all content parts for the x4o language.
 * 
 * @author Willem Cazander
 * @version 1.0 May 29, 2013
 */
public class EldDocWriterLanguage {

	@ApiDocNodeWriterMethod(nodeBody=ApiDocNodeBody.SUMMARY,targetClasses={X4OLanguageContext.class})
	public void writeLanguageSummary(ApiDocWriteEvent<ApiDocNode> event) throws SAXException {
		ApiDocContentWriter writer = event.getWriter();
		ApiDocNode node = event.getEvent();
		X4OLanguageContext context = (X4OLanguageContext)node.getUserData();
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
		writer.docTableStart("Language Summary", "Language Stats Summary.");
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
	}
	
	@ApiDocNodeWriterMethod(nodeBody=ApiDocNodeBody.SUMMARY,targetClasses={X4OLanguageContext.class})
	public void writeModulesSummary(ApiDocWriteEvent<ApiDocNode> event) throws SAXException {
		ApiDocContentWriter writer = event.getWriter();
		ApiDocNode node = event.getEvent();
		writer.docTableStart("Modules Summary", "All modules.");
		writer.docTableHeader("Name", "Description");
		for (ApiDocNode child:node.getNodes()) {
			String link = ApiDocContentWriter.toSafeUri(child.getId())+"/index.html";
			if (node.getParent()==null) {
				link = ApiDocContentWriter.toSafeUri(node.getId())+"/"+link; // root node
			}
			writer.docTableRowHref(link,child.getName(),child.getDescription(),null);
		}
		writer.docTableEnd();
	}
	
	@ApiDocNodeWriterMethod(nodeBody=ApiDocNodeBody.SUMMARY,targetClasses={X4OLanguageContext.class})
	public void writeNamespaceSummary(ApiDocWriteEvent<ApiDocNode> event) throws SAXException {
		ApiDocContentWriter writer = event.getWriter();
		ApiDocNode node = event.getEvent();
		X4OLanguageContext context = (X4OLanguageContext)node.getUserData();
		writer.docTableStart("Namespace Summary", "All Language Namespaces Overview");
		writer.docTableHeader("ID", "URI");
		for (X4OLanguageModule mod:context.getLanguage().getLanguageModules()) {
			for (ElementNamespaceContext ns:mod.getElementNamespaceContexts()) {
				writer.docTableRowHref("language/"+ApiDocContentWriter.toSafeUri(mod.getId())+"/"+ApiDocContentWriter.toSafeUri(ns.getId())+"/index.html",ns.getId(),ns.getUri(),null);
			}
		}
		writer.docTableEnd();
	}
}
