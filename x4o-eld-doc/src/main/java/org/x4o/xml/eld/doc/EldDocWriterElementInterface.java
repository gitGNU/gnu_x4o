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
import org.x4o.xml.eld.doc.api.ApiDocNodeDataConfiguratorMethod;
import org.x4o.xml.eld.doc.api.ApiDocNodeWriterMethod;
import org.x4o.xml.eld.doc.api.dom.ApiDoc;
import org.x4o.xml.eld.doc.api.dom.ApiDocNode;
import org.x4o.xml.eld.doc.api.dom.ApiDocNodeBody;
import org.x4o.xml.eld.doc.api.dom.ApiDocNodeData;
import org.x4o.xml.eld.doc.api.dom.ApiDocWriteEvent;
import org.x4o.xml.element.ElementBindingHandler;
import org.x4o.xml.element.ElementClassAttribute;
import org.x4o.xml.element.ElementConfigurator;
import org.x4o.xml.element.ElementInterface;
import org.xml.sax.SAXException;

/**
 * EldDocWriterElementInterface writers all content parts for the x4o element interface.
 * 
 * @author Willem Cazander
 * @version 1.0 Aug 17, 2013
 */
public class EldDocWriterElementInterface extends AbstractApiDocWriter {
	
	@ApiDocNodeDataConfiguratorMethod(targetClasses={ElementInterface.class})
	public void configNavBar(ApiDoc doc,ApiDocNode node,ApiDocNodeData data) {
		clearHrefContentGroup(doc,node,"summary","attribute",ElementClassAttribute.class);
		clearHrefContentGroup(doc,node,"summary","binding",ElementBindingHandler.class);
		clearHrefContentGroup(doc,node,"summary","configurator",ElementConfigurator.class);
	}
	
	
	@ApiDocNodeWriterMethod(nodeBody=ApiDocNodeBody.SUMMARY_PAGE,targetClasses={ElementInterface.class},nodeBodyOrders={1},contentGroup="interface",contentGroupType="summary")
	public void writeElementNamespaceBeanProperties(ApiDocWriteEvent<ApiDocNode> event) throws SAXException {
		printApiTableBean(event,"Interface","description");
	}
	
	@ApiDocNodeWriterMethod(nodeBody=ApiDocNodeBody.SUMMARY_PAGE,targetClasses={ElementInterface.class},nodeBodyOrders={2},contentGroup="attribute",contentGroupType="summary")
	public void writeElementClassAttribute(ApiDocWriteEvent<ApiDocNode> event) throws SAXException {
		printApiTable(event,"Element Class Attribute Summary",ElementClassAttribute.class);
	}
	
	@ApiDocNodeWriterMethod(nodeBody=ApiDocNodeBody.SUMMARY_PAGE,targetClasses={ElementInterface.class},nodeBodyOrders={3},contentGroup="binding",contentGroupType="summary")
	public void writeElementBindingHandler(ApiDocWriteEvent<ApiDocNode> event) throws SAXException {
		printApiTable(event,"Element Binding Handler Summary",ElementBindingHandler.class);
	}
	
	@ApiDocNodeWriterMethod(nodeBody=ApiDocNodeBody.SUMMARY_PAGE,targetClasses={ElementInterface.class},nodeBodyOrders={4},contentGroup="configurator",contentGroupType="summary")
	public void writeElementConfigurator(ApiDocWriteEvent<ApiDocNode> event) throws SAXException {
		printApiTable(event,"Element Configurator Summary",ElementConfigurator.class);
	}
}
