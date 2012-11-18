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

import org.x4o.xml.element.ElementAttributeHandler;
import org.x4o.xml.element.ElementBindingHandler;
import org.x4o.xml.element.ElementClass;
import org.x4o.xml.element.ElementConfigurator;
import org.x4o.xml.element.ElementInterface;
import org.x4o.xml.element.ElementLanguage;
import org.x4o.xml.element.ElementException;
import org.x4o.xml.element.ElementLanguageModule;
import org.x4o.xml.element.ElementNamespaceContext;

/**
 * EldDocGenerator writes documentation.
 * 
 * @author Willem Cazander
 * @version 1.0 Aug 26, 2010
 */
public class EldDocGenerator {
	
	private ElementLanguage context = null;
	
	public EldDocGenerator(ElementLanguage context) {
		this.context=context;
	}
	
	public void writeDoc(File basePath) throws ElementException {
		EldDocHtmlWriter writer = new EldDocHtmlWriter();
		try {
			writer.writeIndex(basePath, context);
			writer.writeStylesheet(basePath);
			writer.writeOverviewModule(basePath, context);
			writer.writeOverviewNamespace(basePath, context);
			
			for (ElementLanguageModule mod:context.getElementLanguageModules()) {
				
				writer.writeOverviewModule(basePath, mod);
				
				
				for (ElementBindingHandler bind:mod.getElementBindingHandlers()) {
					writer.writeBindingHandler(basePath,bind,mod);
				}
				for (ElementAttributeHandler attr:mod.getElementAttributeHandlers()) {
					writer.writeAttributeHandler(basePath,attr,mod);
				}
				for (ElementConfigurator conf:mod.getElementConfiguratorGlobals()) {
					writer.writeElementConfigurator(basePath,conf,mod);
				}
				for (ElementInterface iface:mod.getElementInterfaces()) {
					writer.writeElementInterface(basePath,iface,mod);
					
					for (ElementBindingHandler bind:iface.getElementBindingHandlers()) {
						writer.writeBindingHandler(basePath,bind,mod,iface);
					}
					//for (ElementAttributeHandler attr:iface.getElementClassAttributes()) {
					//	writer.writeAttributeHandler(basePath,attr,mod,true);
					//}
					for (ElementConfigurator conf:iface.getElementConfigurators()) {
						writer.writeElementConfigurator(basePath,conf,mod,iface);
					}
				}
				
				for (ElementNamespaceContext ns:mod.getElementNamespaceContexts()) {
					writer.writeOverviewElement(basePath, ns,mod);
					for (ElementClass ec:ns.getElementClasses()) {
						writer.writeElement(basePath, ec, ns, mod);
						for (ElementConfigurator conf:ec.getElementConfigurators()) {
							writer.writeElementConfigurator(basePath,conf,mod,ns,ec);
						}
					}
				}
			}
			
		} catch (Exception e) {
			throw new ElementException(e); // todo rm 
		}
	}
}
