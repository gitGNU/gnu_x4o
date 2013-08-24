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
import org.x4o.xml.element.ElementClass;
import org.x4o.xml.element.ElementNamespace;
import org.xml.sax.SAXException;

/**
 * EldDocWriterElementNamespace writers all content parts for the x4o element namespace context.
 * 
 * @author Willem Cazander
 * @version 1.0 Aug 14, 2013
 */
public class EldDocWriterElementNamespace extends AbstractApiDocWriter {
	
	@ApiDocNodeDataConfiguratorMethod(targetClasses={ElementNamespace.class})
	public void configNavBar(ApiDoc doc,ApiDocNode node,ApiDocNodeData data) {
		clearHrefContentGroup(doc,node,"summary","element",ElementClass.class);
	}
	
	@ApiDocNodeWriterMethod(nodeBody=ApiDocNodeBody.SUMMARY,targetClasses={ElementNamespace.class},nodeBodyOrders={1},contentGroup="namespace",contentGroupType="summary")
	public void writeElementNamespaceBeanProperties(ApiDocWriteEvent<ApiDocNode> event) throws SAXException {
		printApiTableBean(event,"Namespace","description","elementClasses","elementNamespaceInstanceProvider","prefixMapping");
	}
	
	@ApiDocNodeWriterMethod(nodeBody=ApiDocNodeBody.SUMMARY,targetClasses={ElementNamespace.class},nodeBodyOrders={2},contentGroup="element",contentGroupType="summary")
	public void writeElementNamespaceElements(ApiDocWriteEvent<ApiDocNode> event) throws SAXException {
		printApiTable(event,"Element Summary",ElementClass.class);
	}
	
	//@ApiDocNodeWriterMethod(nodeBody=ApiDocNodeBody.SUMMARY_PAGE,targetClasses={ElementNamespace.class},nodeBodyOrders={2})
	//public void writeElementNamespaceAttributes(ApiDocWriteEvent<ApiDocNode> event) throws SAXException {
	//	printApiTable(event,"Element Summary",ElementClass.class);
	//}
}
