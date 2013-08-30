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

import org.x4o.xml.eld.doc.api.AbstractApiDocWriter;
import org.x4o.xml.eld.doc.api.ApiDocContentCss;
import org.x4o.xml.eld.doc.api.ApiDocContentWriter;
import org.x4o.xml.eld.doc.api.ApiDocNodeWriterMethod;
import org.x4o.xml.eld.doc.api.dom.ApiDocNode;
import org.x4o.xml.eld.doc.api.dom.ApiDocNodeBody;
import org.x4o.xml.eld.doc.api.dom.ApiDocWriteEvent;
import org.x4o.xml.element.ElementBindingHandler;
import org.x4o.xml.element.ElementClassAttribute;
import org.x4o.xml.element.ElementConfigurator;
import org.x4o.xml.element.ElementNamespace;
import org.x4o.xml.lang.X4OLanguage;
import org.x4o.xml.lang.X4OLanguageModule;
import org.xml.sax.SAXException;

/**
 * EldDocWriterLanguage writer all content parts for the x4o language.
 * 
 * @author Willem Cazander
 * @version 1.0 May 29, 2013
 */
public class EldDocWriterLanguage extends AbstractApiDocWriter {

	// TODO move 
	@ApiDocNodeWriterMethod(nodeBody=ApiDocNodeBody.SUMMARY,targetClasses={ElementBindingHandler.class})
	public void writeElementBindingHandlerBean(ApiDocWriteEvent<ApiDocNode> event) throws SAXException {
		printApiTableBean(event,"BindingHandler","description");
	}

	// TODO move 
	@ApiDocNodeWriterMethod(nodeBody=ApiDocNodeBody.SUMMARY,targetClasses={ElementConfigurator.class})
	public void writeElementConfiguratorBean(ApiDocWriteEvent<ApiDocNode> event) throws SAXException {
		printApiTableBean(event,"Configurator","description");
	}
	
	// TODO move 
	@ApiDocNodeWriterMethod(nodeBody=ApiDocNodeBody.SUMMARY,targetClasses={ElementClassAttribute.class})
	public void writeElementClassAttributeBean(ApiDocWriteEvent<ApiDocNode> event) throws SAXException {
		printApiTableBean(event,"Element Class Attribute","description");
	}
	
	
	@ApiDocNodeWriterMethod(nodeBody=ApiDocNodeBody.SUMMARY,targetClasses={X4OLanguage.class},nodeBodyOrders={1})
	public void writeLanguageSummary(ApiDocWriteEvent<ApiDocNode> event) throws SAXException {
		ApiDocContentWriter writer = event.getWriter();
		ApiDocNode node = event.getEventObject();
		X4OLanguage language = (X4OLanguage)node.getUserData();
		int attrHandlers = 0;
		int bindHandlers = 0;
		int interFaces = 0;
		int eleConfigs = 0;
		int elements = 0;
		int namespaces = 0;
		for (X4OLanguageModule mod:language.getLanguageModules()) {
			bindHandlers += mod.getElementBindingHandlers().size();
			interFaces += mod.getElementInterfaces().size();
			eleConfigs += mod.getElementConfiguratorGlobals().size();
			for (ElementNamespace ns:mod.getElementNamespaces()) {
				namespaces++;
				elements += ns.getElementClasses().size();
				attrHandlers += ns.getElementNamespaceAttributes().size();
			}
		}
		writer.docTableStart("Language Summary", "Language Stats Summary.",ApiDocContentCss.overviewSummary);
		writer.docTableHeader("Name", "Value");
			writer.docTableRow("LanguageName:", ""+language.getLanguageName(), null);
			writer.docTableRow("LanguageVersion:",""+language.getLanguageVersion(),null);
			writer.docTableRow("Modules:",""+language.getLanguageModules().size(),null);
			writer.docTableRow("Elements:",""+elements,null);
			writer.docTableRow("ElementNamespaces:",""+namespaces,null);
			writer.docTableRow("ElementNamespaceAttribute:",""+attrHandlers,null);
			writer.docTableRow("ElementInterfaces:",""+interFaces,null);
			writer.docTableRow("ElementBindingHandlers:",""+bindHandlers,null);
			writer.docTableRow("ElementConfigurators:",""+eleConfigs,null);
		writer.docTableEnd();
	}
	
	@ApiDocNodeWriterMethod(nodeBody=ApiDocNodeBody.SUMMARY,targetClasses={X4OLanguage.class},nodeBodyOrders={2})
	public void writeModulesSummary(ApiDocWriteEvent<ApiDocNode> event) throws SAXException {
		printApiTable(event,"Module Summary",X4OLanguageModule.class);
	}
	
	@ApiDocNodeWriterMethod(nodeBody=ApiDocNodeBody.SUMMARY,targetClasses={X4OLanguage.class},nodeBodyOrders={3})
	public void writeNamespaceSummary(ApiDocWriteEvent<ApiDocNode> event) throws SAXException {
		ApiDocContentWriter writer = event.getWriter();
		ApiDocNode node = event.getEventObject();
		X4OLanguage language = (X4OLanguage)node.getUserData();
		writer.docTableStart("Namespace Summary", "All Language Namespaces Overview",ApiDocContentCss.overviewSummary);
		writer.docTableHeader("ID", "URI");
		for (X4OLanguageModule mod:language.getLanguageModules()) {
			for (ElementNamespace ns:mod.getElementNamespaces()) {
				writer.docTableRowLink("language/"+ApiDocContentWriter.toSafeUri(mod.getId())+"/"+ApiDocContentWriter.toSafeUri(ns.getId())+"/index.html",ns.getId(),ns.getUri());
			}
		}
		writer.docTableEnd();
	}
}
