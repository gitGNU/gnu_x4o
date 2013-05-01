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
 * EldDocGenerator writes documentation.
 * 
 * @author Willem Cazander
 * @version 1.0 Aug 26, 2010
 */
public class EldDocGenerator {
	
	private X4OLanguageContext context = null;
	
	/**
	 * Creates an EldDocGenerator for this langauge context.
	 * @param context	The language context to generate doc for.
	 */
	public EldDocGenerator(X4OLanguageContext context) {
		this.context=context;
	}
	
	/**
	 * Writes the language documentation to the base path.
	 * @param basePath	The path to write to documentation to.
	 * @throws ElementException	Is thrown when error is done.
	 */
	public void writeDoc(File basePath) throws ElementException {
		EldDocHtmlWriter writer = new EldDocHtmlWriter();
		try {
			
			writer.writeTheme(basePath);
			writer.writeIndex(basePath, context);
			writer.writeIndexAll(basePath, context);
			writer.writeDocHelp(basePath, context);
			writer.writeAllElementsFrame(basePath, context, true);
			writer.writeAllElementsFrame(basePath, context, false);
			writer.writeOverviewFrame(basePath, context);
			writer.writeOverviewLanguage(basePath, context);
			writer.writeOverviewTree(basePath, context);
			
			for (X4OLanguageModule mod:context.getLanguage().getLanguageModules()) {
				
				writer.writeOverviewModule(basePath, mod, context);
				
				
				for (ElementBindingHandler bind:mod.getElementBindingHandlers()) {
					writer.writeBindingHandler(basePath,bind,mod,context);
				}
				for (ElementAttributeHandler attr:mod.getElementAttributeHandlers()) {
					writer.writeAttributeHandler(basePath,attr,mod,context);
				}
				for (ElementConfigurator conf:mod.getElementConfiguratorGlobals()) {
					writer.writeElementConfigurator(basePath,conf,mod,context);
				}
				for (ElementInterface iface:mod.getElementInterfaces()) {
					writer.writeElementInterface(basePath,iface,mod,context);
					
					for (ElementBindingHandler bind:iface.getElementBindingHandlers()) {
						writer.writeBindingHandler(basePath,bind,mod,iface,context);
					}
					//for (ElementAttributeHandler attr:iface.getElementClassAttributes()) {
					//	writer.writeAttributeHandler(basePath,attr,mod,true);
					//}
					for (ElementConfigurator conf:iface.getElementConfigurators()) {
						writer.writeElementConfigurator(basePath,conf,mod,iface,context);
					}
				}
				
				for (ElementNamespaceContext ns:mod.getElementNamespaceContexts()) {
					
					writer.writeOverviewElement(basePath,ns,mod,context);
					writer.writeNamespaceElementsFrame(basePath,ns,mod,context);
					
					for (ElementClass ec:ns.getElementClasses()) {
						writer.writeElement(basePath, ec, ns, mod,context);
						for (ElementConfigurator conf:ec.getElementConfigurators()) {
							writer.writeElementConfigurator(basePath,conf,mod,ns,ec,context);
						}
					}
				}
			}
		} catch (SAXException e) {
			throw new ElementException(e); 
		} catch (IOException e) {
			throw new ElementException(e); 
		}
	}
}
