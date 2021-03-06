/*
 * Copyright (c) 2004-2014, Willem Cazander
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

import java.util.Collection;
import java.util.List;

import org.x4o.xml.element.Element;
import org.x4o.xml.element.ElementBindingHandler;
import org.x4o.xml.element.ElementInterface;
import org.x4o.xml.element.ElementNamespace;
import org.x4o.xml.lang.phase.X4OPhaseManager;

/**
 * X4OLanguage hold all the base definition properties of x4o xml language.
 * 
 * @author Willem Cazander
 * @version 1.0 30 apr 2013
 */
public interface X4OLanguage {
	
	/**
	 * Returns the language name of this x4o xml language.
	 * @return	Returns the language name.
	 */
	String getLanguageName();
	
	/**
	 * @return	Returns the language version of this language instance.
	 */
	String getLanguageVersion();

	/**
	 * Returns the phase manager which runs the phases  
	 * @return Returns the phase manager.
	 */
	X4OPhaseManager getPhaseManager();
	
	/**
	 * @return the language configuration.
	 */
	X4OLanguageConfiguration getLanguageConfiguration();

	/**
	 * Creates and fills the initial element language used to store the language.
	 * @return	The newly created X4OLanguageSession.
	 */
	X4OLanguageSession createLanguageSession();

	/**
	 * Search language for object and create element for it.
	 * @param context The X4O language context to create for.
	 * @param objectClass The object to search for.
	 * @return	Returns an new Element instance for the object.
	 */
	Element createElementInstance(X4OLanguageSession context,Class<?> objectClass);
	
	/**
	 * Gets all ElementBindingHandlers which are possible for parent.
	 * @param parent The parent element object or class to search for.
	 * @return	Returns an List with all ElementBindingHandler for the search.
	 */
	List<ElementBindingHandler> findElementBindingHandlers(Object parent);
	
	/**
	 * Gets all ElementBindingHandlers for parent and child combination.
	 * @param parent The parent element object or class to search for.
	 * @param child The parent element object or class to search for.
	 * @return	Returns an List with all ElementBindingHandler for the search pair.
	 */
	List<ElementBindingHandler> findElementBindingHandlers(Object parent,Object child);
	
	/**
	 * Returns list of ElementInterfaces for an element.
	 * @param object The element object or class to search for.
	 * @return	The list of elementInterfaces.
	 */
	List<ElementInterface> findElementInterfaces(Object object);
	
	/**
	 * Returns the namespace context for an namespace uri.
	 * @param namespaceUri the namespace uri.
	 * @return	The ElementNamespace.
	 */
	ElementNamespace findElementNamespace(String namespaceUri);
	
	/**
	 * @return Returns a collection of element language modules in this defined and loaded language.
	 */
	Collection<X4OLanguageModule> getLanguageModules();
}
