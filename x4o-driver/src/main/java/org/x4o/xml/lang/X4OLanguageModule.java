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
package	org.x4o.xml.lang;

import java.util.List;

import org.x4o.xml.element.ElementBindingHandler;
import org.x4o.xml.element.ElementConfiguratorGlobal;
import org.x4o.xml.element.ElementInterface;
import org.x4o.xml.element.ElementMetaBase;
import org.x4o.xml.element.ElementNamespace;

/**
 * The ElementLanguageModule.<br>
 * This is an central store to element interfaces from one language module
 * 
 * @author Willem Cazander
 * @version 1.0 Aug 2, 2012
 */
public interface X4OLanguageModule extends ElementMetaBase {

	/**
	 * @return the providerName.
	 */
	String getProviderName();

	/**
	 * @param providerName the providerName to set.
	 */
	void setProviderName(String providerName);
	
	/**
	 * @return the providerHost
	 */
	String getProviderHost();
	
	/**
	 * @param providerHost the providerHost to set
	 */
	void setProviderHost(String providerHost);
	
	/**
	 * Adds an ElementBindingHanlder.
	 * @param elementBindingHandler	The ElementBindingHandler to add.
	 */
	void addElementBindingHandler(ElementBindingHandler elementBindingHandler);
	
	/**
	 * Gets all ElementBindingHandlers.
	 * @return	Returns an List with all ElementBindingHandlers.
	 */
	List<ElementBindingHandler> getElementBindingHandlers();
	
	/**
	 * Adds an ElementConfiguratorGlobal.
	 * @param elementConfigurator	The ElementConfigurtor to add.
	 */
	void addElementConfiguratorGlobal(ElementConfiguratorGlobal elementConfigurator);
	
	/**
	 * Gets all ElementConfiguratorGlobals.
	 * @return	All gloval ElementConfigurators.
	 */
	List<ElementConfiguratorGlobal> getElementConfiguratorGlobals();
	
	/**
	 * Adds an ElementInterface.
	 * @param elementInterface	The elementInterface to add.
	 */
	void addElementInterface(ElementInterface elementInterface);
	
	/**
	 * Returns list of ElementInterfaces in this context.
	 * @return	The list of elementInterfaces.
	 */
	List<ElementInterface> getElementInterfaces();
	
	/**
	 * Adds an namespace to this langauge module.
	 * @param elementNamespace Adds an ElementNamespace to this langauge module.
	 */
	void addElementNamespace(ElementNamespace elementNamespace);
	
	/**
	 * Returns the namespace context for an namespace uri.
	 * @param namespaceUri the namespace uri.
	 * @return	The ElementNamespace.
	 */
	ElementNamespace getElementNamespace(String namespaceUri);
	
	/**
	 * @return Returns a list of all namespaces defined in this language.
	 */
	List<ElementNamespace> getElementNamespaces();
	
	/**
	 * @param elementLanguageModuleLoader	Sets the loader of this module.
	 */
	void setLanguageModuleLoader(X4OLanguageModuleLoader elementLanguageModuleLoader);
	
	/**
	 * @return	Returns the ElementLanguageModuleLoader of this module.
	 */
	X4OLanguageModuleLoader getLanguageModuleLoader();
	
	/**
	 * @return the sourceResource
	 */
	String getSourceResource();

	/**
	 * @param sourceResource the sourceResource to set
	 */
	void setSourceResource(String sourceResource);
	
	/*
	 * 
	 * @param elementLanguage
	 * @param elementLanguageModule
	 * @throws ElementLanguageModuleLoaderException
	 
	void reloadModule(ElementLanguage elementLanguage,ElementLanguageModule elementLanguageModule) throws ElementLanguageModuleLoaderException;
	*/
}
