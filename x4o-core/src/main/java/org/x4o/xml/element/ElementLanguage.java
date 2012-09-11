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

package	org.x4o.xml.element;

import java.util.List;
import java.util.Map;

import javax.el.ELContext;
import javax.el.ExpressionFactory;

import org.x4o.xml.core.X4OPhase;
import org.x4o.xml.core.config.X4OLanguageConfiguration;


/**
 * ElementLanguage is the central store of the defined element language.
 * 
 * @author Willem Cazander
 * @version 1.0 Feb 14, 2007
 */
public interface ElementLanguage {
	
	/**
	 * Gets all ElementBindingHandlers.
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
	 * @return	The ElementNamespaceContext.
	 */
	ElementNamespaceContext findElementNamespaceContext(String namespaceUri);
	
	/**
	 * Gets the EL Context.
	 * @return	Returns the ELContext.
	 */
	ELContext getELContext();
		
	/**
	 * Gets the ExpressionFactory.
	 * @return	Returns the ExpressionFactory.
	 */
	ExpressionFactory getExpressionFactory();

	/**
	 * @return	Returns the ElementAttributeValueParser.
	 */
	ElementAttributeValueParser getElementAttributeValueParser();
		
	/**
	 * @return	Returns the ElementObjectPropertyValue.
	 */
	ElementObjectPropertyValue getElementObjectPropertyValue();
	
	/**
	 * Returns the current X4OPhase of the parser.
	 * @return	Returns the current phase.
	 */
	X4OPhase getCurrentX4OPhase();
	
	/**
	 * Sets the phase of the context.
	 * TODO: Do never call this, methode sould be moved to local interface.
	 * @param phase	The current phase to set.
	 */
	void setCurrentX4OPhase(X4OPhase phase);
	
	/**
	 * Marks an (new) Element as dirty and run the phases from this start phase.
	 * 
	 * @param element	The Element which needs the magic.
	 * @param phase		May be null, then it should defualt to configElementPhase
	 */
	void addDirtyElement(Element element,X4OPhase phase);

	/**
	 * Get all Dirty Elements.
	 * @return	Returns Map with dirty elements.
	 */
	Map<Element,X4OPhase> getDirtyElements();
	
	/**
	 * Returns the root Element which starts the xml tree.
	 * @return	Returns the root element of the document instance we parse.
	 */
	Element getRootElement();
	
	/**
	 * Sets the root element.
	 * @param element	The root element to set.
	 */
	void setRootElement(Element element);
		
	/**
	 * @return the languageConfiguration.
	 */
	X4OLanguageConfiguration getLanguageConfiguration();
	
	/**
	 * Adds an ElementLanguageModule to this language.
	 * @param elementLanguageModule The element language module to add.
	 */
	void addElementLanguageModule(ElementLanguageModule elementLanguageModule);
	
	/**
	 * @return Returns a list of element language modules in this defined and loaded language.
	 */
	List<ElementLanguageModule> getElementLanguageModules();
}
