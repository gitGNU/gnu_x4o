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

import org.x4o.xml.element.ElementBindingHandler;
import org.x4o.xml.element.ElementConfiguratorGlobal;
import org.x4o.xml.element.ElementInterface;
import org.x4o.xml.element.ElementMetaBase;
import org.x4o.xml.element.ElementNamespace;

/**
 * The ElementLanguageModuleLocal is for local loading of the object.
 * 
 * @author Willem Cazander
 * @version 1.0 Mar 7, 2014
 */
public interface X4OLanguageModuleLocal extends X4OLanguageModule,ElementMetaBase {
	
	/**
	 * @param providerName the providerName to set.
	 */
	void setProviderName(String providerName);
	
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
	 * Adds an ElementConfiguratorGlobal.
	 * @param elementConfigurator	The ElementConfigurtor to add.
	 */
	void addElementConfiguratorGlobal(ElementConfiguratorGlobal elementConfigurator);
	
	/**
	 * Adds an ElementInterface.
	 * @param elementInterface	The elementInterface to add.
	 */
	void addElementInterface(ElementInterface elementInterface);
	
	/**
	 * Adds an namespace to this langauge module.
	 * @param elementNamespace Adds an ElementNamespace to this langauge module.
	 */
	void addElementNamespace(ElementNamespace elementNamespace);
	
	/**
	 * Sets module loader meta result info.
	 * @param key	The key of the info.
	 * @param value	The value of the info.
	 */
	void putLoaderResult(X4OLanguageModuleLoaderResult key,String value);
}
