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

package org.x4o.xml.element;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * An AbstractElementLanguageModule.
 * 
 * @author Willem Cazander
 * @version 1.0 Aug 2, 2012
 */
public abstract class AbstractElementLanguageModule extends AbstractElementMetaBase implements ElementLanguageModule {

	private Logger logger = null;

	private String name=null;
	private String providerName=null;
	private String sourceResource = null;
	
	/** The globalAttribute handlers */
	private List<ElementAttributeHandler> elementAttributeHandlers = null;

	/** The binding rules */
	private List<ElementBindingHandler> elementBindingHandlers = null;

	private List<ElementConfiguratorGlobal> elementConfiguratorGlobals = null;

	private List<ElementInterface> elementInterfaces = null;

	private Map<String,ElementNamespaceContext> elementNamespaceContexts = null;

	private ElementLanguageModuleLoader elementLanguageModuleLoader = null;

	/**
	 * Creates a new empty ElementLanguage.
	 */
	public AbstractElementLanguageModule() {
		logger = Logger.getLogger(AbstractElementLanguage.class.getName());
		logger.finest("Creating new ParsingContext");
		elementAttributeHandlers = new ArrayList<ElementAttributeHandler>(4);
		elementBindingHandlers = new ArrayList<ElementBindingHandler>(4);
		elementConfiguratorGlobals = new ArrayList<ElementConfiguratorGlobal>(4);
		elementInterfaces = new ArrayList<ElementInterface>(20);
		elementNamespaceContexts = new HashMap<String,ElementNamespaceContext>(10);
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the providerName
	 */
	public String getProviderName() {
		return providerName;
	}

	/**
	 * @param providerName the providerName to set
	 */
	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}

	/**
	 * @see org.x4o.xml.element.ElementLanguageModule#addElementAttributeHandler(ElementAttributeHandler)
	 */
	public void addElementAttributeHandler(ElementAttributeHandler elementAttributeHandler) {
		if (elementAttributeHandler==null) {
			throw new NullPointerException("Can't add null object");
		}
		if (elementAttributeHandler.getId()==null) {
			throw new NullPointerException("Can't add with null id property.");
		}
		logger.finer("Adding ElementAttributeHandler: "+elementAttributeHandler.getAttributeName());
		elementAttributeHandlers.add(elementAttributeHandler);
	}

	/**
	 * @see org.x4o.xml.element.ElementLanguageModule#getElementAttributeHandlers()
	 */
	public List<ElementAttributeHandler> getElementAttributeHandlers() {
		return elementAttributeHandlers;
	}

	/**
	 * @see org.x4o.xml.element.ElementLanguageModule#addElementBindingHandler(ElementBindingHandler)
	 */
	public void addElementBindingHandler(ElementBindingHandler elementBindingHandler) {
		if (elementBindingHandler==null) {
			throw new NullPointerException("Can't add null binding handler.");
		}
		if (elementBindingHandler.getId()==null) {
			throw new NullPointerException("Can't add with null id property.");
		}
		// Check so doc tree does not loop; see EldDocHtmlWriter.findChilderen()
		for (Class<?> cl:elementBindingHandler.getBindChildClasses()) {
			if (elementBindingHandler.getBindParentClass().equals(cl)) {
				throw new IllegalStateException("Can't add binding handler: "+elementBindingHandler.getId()+" with same parent as child class.");
			}
		}
		logger.finer("Adding ElementBindingHandler: "+elementBindingHandler);
		elementBindingHandlers.add(elementBindingHandler);
	}

	/**
	 * @see org.x4o.xml.element.ElementLanguageModule#getElementBindingHandlers()
	 */
	public List<ElementBindingHandler> getElementBindingHandlers() {
		return elementBindingHandlers;
	}

	/**
	 * @see org.x4o.xml.element.ElementLanguageModule#addElementConfiguratorGlobal(ElementConfiguratorGlobal)
	 */
	public void addElementConfiguratorGlobal(ElementConfiguratorGlobal elementConfigurator) {
		if (elementConfigurator==null) {
			throw new NullPointerException("Can't add null");
		}
		if (elementConfigurator.getId()==null) {
			throw new NullPointerException("Can't add with null id property.");
		}
		logger.finer("Adding ElementConfiguratorGlobal: "+elementConfigurator);
		elementConfiguratorGlobals.add(elementConfigurator);
	}
	
	/**
	 * @see org.x4o.xml.element.ElementLanguageModule#getElementConfiguratorGlobals()
	 */
	public List<ElementConfiguratorGlobal> getElementConfiguratorGlobals() {
		return elementConfiguratorGlobals;
	}

	/**
	 * @see org.x4o.xml.element.ElementLanguageModule#addElementInterface(org.x4o.xml.element.ElementInterface)
	 */
	public void addElementInterface(ElementInterface elementInterface) {
		if (elementInterface==null) {
			throw new NullPointerException("Can't add null.");
		}
		if (elementInterface.getId()==null) {
			throw new NullPointerException("Can't add with null id property.");
		}
		if (elementInterface.getInterfaceClass()==null) {
			throw new NullPointerException("ElementInterface not correctly configured getInterfaceClass returns null.");
		}
		elementInterfaces.add(elementInterface);
	}

	/**
	 * @see org.x4o.xml.element.ElementLanguageModule#getElementInterfaces()
	 */
	public List<ElementInterface> getElementInterfaces() {
		return elementInterfaces;
	}

	/**
	 * @see org.x4o.xml.element.ElementLanguageModule#addElementNamespaceContext(org.x4o.xml.element.ElementNamespaceContext)
	 */
	public void addElementNamespaceContext(ElementNamespaceContext elementNamespaceContext) {
		if (elementNamespaceContext==null) {
			throw new NullPointerException("Can't add null.");
		}
		if (elementNamespaceContext.getUri()==null) {
			throw new NullPointerException("Can add ElementNamespaceContext without uri.");
		}
		if (elementNamespaceContext.getId()==null) {
			StringBuffer buf = new StringBuffer(30);
			for (char c:elementNamespaceContext.getUri().toLowerCase().toCharArray()) {
				if (Character.isLetter(c))	{buf.append(c);}
				if (Character.isDigit(c))	{buf.append(c);}
			}
			String id = buf.toString();
			if (id.startsWith("http"))		{id = id.substring(4);}
			elementNamespaceContext.setId(id);
		}
		// TODO: no language here so move to EL default on eld attribute tag 
		//if (elementNamespaceContext.getSchemaUri()==null) {
		//	elementNamespaceContext.setSchemaUri(elementNamespaceContext.getUri()+elementNamespaceContext.)
		//}
		logger.fine("Adding namespaceUri: "+elementNamespaceContext.getUri());
		elementNamespaceContexts.put(elementNamespaceContext.getUri(), elementNamespaceContext);
	}

	/**
	 * @see org.x4o.xml.element.ElementLanguageModule#getElementNamespaceContext(java.lang.String)
	 */
	public ElementNamespaceContext getElementNamespaceContext(String namespaceUri) {
		return elementNamespaceContexts.get(namespaceUri);
	}

	/**
	 * @see org.x4o.xml.element.ElementLanguageModule#getElementNamespaceContexts()
	 */
	public List<ElementNamespaceContext> getElementNamespaceContexts() {
		return new ArrayList<ElementNamespaceContext>(elementNamespaceContexts.values());
	}

	/**
	 * @return the elementLanguageModuleLoader
	 */
	public ElementLanguageModuleLoader getElementLanguageModuleLoader() {
		return elementLanguageModuleLoader;
	}

	/**
	 * @param elementLanguageModuleLoader the elementLanguageModuleLoader to set
	 */
	public void setElementLanguageModuleLoader(ElementLanguageModuleLoader elementLanguageModuleLoader) {
		this.elementLanguageModuleLoader = elementLanguageModuleLoader;
	}

	/**
	 * @return the sourceResource
	 */
	public String getSourceResource() {
		return sourceResource;
	}

	/**
	 * @param sourceResource the sourceResource to set
	 */
	public void setSourceResource(String sourceResource) {
		this.sourceResource = sourceResource;
	}
	
	/**
	 * Reloads the module, experiment !!
	 */
	public void reloadModule(ElementLanguage elementLanguage,ElementLanguageModule elementLanguageModule) throws ElementLanguageModuleLoaderException {
		elementAttributeHandlers.clear();
		elementBindingHandlers.clear();
		elementInterfaces.clear();
		elementNamespaceContexts.clear();
		
		getElementLanguageModuleLoader().loadLanguageModule(elementLanguage, elementLanguageModule);
	}
}
