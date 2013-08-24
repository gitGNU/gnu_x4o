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
package org.x4o.xml.lang;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.logging.Logger;

import org.x4o.xml.element.AbstractElementMetaBase;
import org.x4o.xml.element.ElementBindingHandler;
import org.x4o.xml.element.ElementConfiguratorGlobal;
import org.x4o.xml.element.ElementInterface;
import org.x4o.xml.element.ElementNamespace;

/**
 * An AbstractElementLanguageModule.
 * 
 * @author Willem Cazander
 * @version 1.0 Aug 2, 2012
 */
public abstract class AbstractX4OLanguageModule extends AbstractElementMetaBase implements X4OLanguageModule {

	private Logger logger = null;
	private String providerName=null;
	private String providerHost=null;
	private String sourceResource = null;
	
	private List<ElementBindingHandler> elementBindingHandlers = null;
	private List<ElementConfiguratorGlobal> elementConfiguratorGlobals = null;
	private List<ElementInterface> elementInterfaces = null;
	private Map<String,ElementNamespace> elementNamespaces = null;
	private X4OLanguageModuleLoader elementLanguageModuleLoader = null;

	/**
	 * Creates a new empty ElementLanguage.
	 */
	public AbstractX4OLanguageModule() {
		logger = Logger.getLogger(AbstractX4OLanguageSession.class.getName());
		logger.finest("Creating new ParsingContext");
		elementBindingHandlers = new ArrayList<ElementBindingHandler>(4);
		elementConfiguratorGlobals = new ArrayList<ElementConfiguratorGlobal>(4);
		elementInterfaces = new ArrayList<ElementInterface>(20);
		elementNamespaces = new HashMap<String,ElementNamespace>(10);
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
	 * @return the providerHost
	 */
	public String getProviderHost() {
		return providerHost;
	}
	
	/**
	 * @param providerHost the providerHost to set
	 */
	public void setProviderHost(String providerHost) {
		this.providerHost = providerHost;
	}

	/**
	 * @see org.x4o.xml.lang.X4OLanguageModule#addElementBindingHandler(ElementBindingHandler)
	 */
	public void addElementBindingHandler(ElementBindingHandler elementBindingHandler) {
		if (elementBindingHandler==null) {
			throw new NullPointerException("Can't add null binding handler.");
		}
		if (elementBindingHandler.getId()==null) {
			throw new NullPointerException("Can't add with null id property.");
		}
		// Check so doc tree does not loop; see EldDocHtmlWriter.findChilderen()
		/*
		for (Class<?> cl:elementBindingHandler.getBindChildClasses()) {
			if (elementBindingHandler.getBindParentClass().equals(cl)) {
				throw new IllegalStateException("Can't add binding handler: "+elementBindingHandler.getId()+" with same parent as child class.");
			}
		}*/
		logger.finer("Adding ElementBindingHandler: "+elementBindingHandler);
		elementBindingHandlers.add(elementBindingHandler);
	}

	/**
	 * @see org.x4o.xml.lang.X4OLanguageModule#getElementBindingHandlers()
	 */
	public List<ElementBindingHandler> getElementBindingHandlers() {
		return elementBindingHandlers;
	}

	/**
	 * @see org.x4o.xml.lang.X4OLanguageModule#addElementConfiguratorGlobal(ElementConfiguratorGlobal)
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
	 * @see org.x4o.xml.lang.X4OLanguageModule#getElementConfiguratorGlobals()
	 */
	public List<ElementConfiguratorGlobal> getElementConfiguratorGlobals() {
		return elementConfiguratorGlobals;
	}

	/**
	 * @see org.x4o.xml.lang.X4OLanguageModule#addElementInterface(org.x4o.xml.element.ElementInterface)
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
	 * @see org.x4o.xml.lang.X4OLanguageModule#getElementInterfaces()
	 */
	public List<ElementInterface> getElementInterfaces() {
		return elementInterfaces;
	}

	/**
	 * @see org.x4o.xml.lang.X4OLanguageModule#addElementNamespace(org.x4o.xml.element.ElementNamespace)
	 */
	public void addElementNamespace(ElementNamespace elementNamespace) {
		if (elementNamespace==null) {
			throw new NullPointerException("Can't add null.");
		}
		if (elementNamespace.getUri()==null) {
			throw new NullPointerException("Can add ElementNamespace without uri.");
		}
		logger.fine("Adding namespaceUri: "+elementNamespace.getUri());
		elementNamespaces.put(elementNamespace.getUri(), elementNamespace);
	}

	/**
	 * @see org.x4o.xml.lang.X4OLanguageModule#getElementNamespace(java.lang.String)
	 */
	public ElementNamespace getElementNamespace(String namespaceUri) {
		return elementNamespaces.get(namespaceUri);
	}

	/**
	 * @see org.x4o.xml.lang.X4OLanguageModule#getElementNamespaces()
	 */
	public List<ElementNamespace> getElementNamespaces() {
		return new ArrayList<ElementNamespace>(elementNamespaces.values());
	}

	/**
	 * @return the elementLanguageModuleLoader
	 */
	public X4OLanguageModuleLoader getLanguageModuleLoader() {
		return elementLanguageModuleLoader;
	}

	/**
	 * @param elementLanguageModuleLoader the elementLanguageModuleLoader to set
	 */
	public void setLanguageModuleLoader(X4OLanguageModuleLoader elementLanguageModuleLoader) {
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
	public void reloadModule(X4OLanguageLocal elementLanguage,X4OLanguageModule elementLanguageModule) throws X4OLanguageModuleLoaderException {
		elementBindingHandlers.clear();
		elementInterfaces.clear();
		elementNamespaces.clear();
		
		getLanguageModuleLoader().loadLanguageModule(elementLanguage, elementLanguageModule);
	}
}
