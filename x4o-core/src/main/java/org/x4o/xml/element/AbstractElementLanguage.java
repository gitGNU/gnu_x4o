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

import javax.el.ELContext;
import javax.el.ExpressionFactory;

import org.x4o.xml.core.X4OPhase;
import org.x4o.xml.core.config.X4OLanguageConfiguration;
import org.x4o.xml.element.ElementBindingHandler;

/**
 * An AbstractElementLanguage.
 * 
 * @author Willem Cazander
 * @version 1.0 Aug 20, 2005
 */
abstract public class AbstractElementLanguage implements ElementLanguageLocal {

	private Logger logger = null;
	private ExpressionFactory expressionFactory = null;
	private ELContext eLContext = null;
	private ElementAttributeValueParser elementAttributeValueParser = null;
	private ElementObjectPropertyValue elementObjectPropertyValue = null;
	private X4OPhase currentX4OPhase = null;
	private Map<Element, X4OPhase> dirtyElements = null;
	private Element rootElement = null;
	private X4OLanguageConfiguration languageConfiguration = null;
	private List<ElementLanguageModule> elementLanguageModules = null;

	/**
	 * Creates a new empty ElementLanguage.
	 */
	public AbstractElementLanguage() {
		logger = Logger.getLogger(AbstractElementLanguage.class.getName());
		logger.finest("Creating new ParsingContext");
		elementLanguageModules = new ArrayList<ElementLanguageModule>(20);
		currentX4OPhase = X4OPhase.FIRST_PHASE;
		dirtyElements = new HashMap<Element, X4OPhase>(40);
	}
	
	/**
	 * @see org.x4o.xml.element.ElementLanguage#getELContext()
	 */
	public ELContext getELContext() {
		return eLContext;
	}

	/**
	 * @see org.x4o.xml.element.ElementLanguageLocal#setELContext(javax.el.ELContext)
	 */
	public void setELContext(ELContext context) {
		if (eLContext!=null) {
			throw new IllegalStateException("Can only set elContext once.");
		}
		eLContext = context;
	}

	/**
	 * @see org.x4o.xml.element.ElementLanguage#getExpressionFactory()
	 */
	public ExpressionFactory getExpressionFactory() {
		return expressionFactory;
	}

	/**
	 * @see org.x4o.xml.element.ElementLanguageLocal#setExpressionFactory(javax.el.ExpressionFactory)
	 */
	public void setExpressionFactory(ExpressionFactory expressionFactory) {
		if (this.expressionFactory!=null) {
			throw new IllegalStateException("Can only set expressionFactory once.");
		}
		this.expressionFactory = expressionFactory;
	}
	
	/**
	 * @return the elementAttributeValueParser
	 */
	public ElementAttributeValueParser getElementAttributeValueParser() {
		return elementAttributeValueParser;
	}

	/**
	 * @param elementAttributeValueParser the elementAttributeValueParser to set
	 */
	public void setElementAttributeValueParser(ElementAttributeValueParser elementAttributeValueParser) {
		if (this.elementAttributeValueParser!=null) {
			throw new IllegalStateException("Can only set elementAttributeValueParser once.");
		}
		this.elementAttributeValueParser = elementAttributeValueParser;
	}

	/**
	 * @return the elementObjectPropertyValue
	 */
	public ElementObjectPropertyValue getElementObjectPropertyValue() {
		return elementObjectPropertyValue;
	}

	/**
	 * @param elementObjectPropertyValue the elementObjectPropertyValue to set
	 */
	public void setElementObjectPropertyValue(ElementObjectPropertyValue elementObjectPropertyValue) {
		if (this.elementObjectPropertyValue!=null) {
			throw new IllegalStateException("Can only set elementObjectPropertyValue once.");
		}
		this.elementObjectPropertyValue = elementObjectPropertyValue;
	}

	/**
	 * @see org.x4o.xml.element.ElementLanguage#getCurrentX4OPhase()
	 */
	public X4OPhase getCurrentX4OPhase() {
		return currentX4OPhase;
	}

	/**
	 * @see org.x4o.xml.element.ElementLanguage#setCurrentX4OPhase(org.x4o.xml.core.X4OPhase)
	 */
	public void setCurrentX4OPhase(X4OPhase currentX4OPhase) {
		this.currentX4OPhase = currentX4OPhase;
	}

	/**
	 * @see org.x4o.xml.element.ElementLanguage#addDirtyElement(org.x4o.xml.element.Element, org.x4o.xml.core.X4OPhase)
	 */
	public void addDirtyElement(Element element, X4OPhase phase) {
		if (dirtyElements.containsKey(element)) {
			throw new IllegalArgumentException("Can't add an element twice.");
		}
		dirtyElements.put(element,phase);
	}

	/**
	 * @see org.x4o.xml.element.ElementLanguage#getDirtyElements()
	 */
	public Map<Element, X4OPhase> getDirtyElements() {
		return dirtyElements;
	}

	/**
	 * @see org.x4o.xml.element.ElementLanguage#getRootElement()
	 */
	public Element getRootElement() {
		return rootElement;
	}

	/**
	 * @see org.x4o.xml.element.ElementLanguage#setRootElement(org.x4o.xml.element.Element)
	 */
	public void setRootElement(Element element) {
		if (element==null) {
			throw new NullPointerException("May not set rootElement to null");
		}
		// allowed for reusing this context in multiple parse sessions.
		//if (rootElement!=null) {
		//	throw new IllegalStateException("Can't set rootElement when it is already set.");
		//}
		rootElement=element;
	}

	/**
	 * @return the languageConfiguration
	 */
	public X4OLanguageConfiguration getLanguageConfiguration() {
		return languageConfiguration;
	}

	/**
	 * @param languageConfiguration the languageConfiguration to set
	 */
	public void setLanguageConfiguration(X4OLanguageConfiguration languageConfiguration) {
		this.languageConfiguration = languageConfiguration;
	}

	/**
	 * @see org.x4o.xml.element.ElementLanguage#addElementLanguageModule(org.x4o.xml.element.ElementLanguageModule)
	 */
	public void addElementLanguageModule(ElementLanguageModule elementLanguageModule) {
		if (elementLanguageModule.getId()==null) {
			throw new NullPointerException("Can't add module without id.");
		}
		elementLanguageModules.add(elementLanguageModule);
	}

	/**
	 * @see org.x4o.xml.element.ElementLanguage#getElementLanguageModules()
	 */
	public List<ElementLanguageModule> getElementLanguageModules() {
		return elementLanguageModules;
	}
	

	/**
	 * @see org.x4o.xml.element.ElementLanguage#findElementBindingHandlers(java.lang.Object,java.lang.Object)
	 */
	public List<ElementBindingHandler> findElementBindingHandlers(Object parent,Object child) {
		List<ElementBindingHandler> result = new ArrayList<ElementBindingHandler>(50);
		for (int i=0;i<elementLanguageModules.size();i++) {
			ElementLanguageModule module = elementLanguageModules.get(i);
			findElementBindingHandlerInList(parent,child,result,module.getElementBindingHandlers());
		}
		for (ElementInterface ei:findElementInterfaces(parent)) {
			findElementBindingHandlerInList(parent,child,result,ei.getElementBindingHandlers());
		}
		return result;
	}

	private void findElementBindingHandlerInList(Object parent,Object child,List<ElementBindingHandler> result,List<ElementBindingHandler> checkList) {
		for (ElementBindingHandler binding:checkList) {
			boolean parentBind = false;
			if (parent instanceof Class) {
				parentBind = binding.getBindParentClass().isAssignableFrom((Class<?>)parent);
			} else {
				parentBind = binding.getBindParentClass().isInstance(parent);
			}
			if (parentBind==false) {
				continue;
			}
			boolean childBind = false;
			for (Class<?> childClass:binding.getBindChildClasses()) {
				if (child instanceof Class && childClass.isAssignableFrom((Class<?>)child)) {
					childBind=true;
					break;	
				} else if (childClass.isInstance(child)) {
					childBind=true;
					break;
				}
			}
			if (parentBind & childBind) {
				result.add(binding);
			}
		}	
	}
	
	/**
	 * @see org.x4o.xml.element.ElementLanguage#findElementInterfaces(java.lang.Object)
	 */
	public List<ElementInterface> findElementInterfaces(Object elementObject) {
		if (elementObject==null) {
			throw new NullPointerException("Can't search for null object.");
		}
		List<ElementInterface> result = new ArrayList<ElementInterface>(50);
		for (int i=0;i<elementLanguageModules.size();i++) {
			ElementLanguageModule module = elementLanguageModules.get(i);
			for (ElementInterface ei:module.getElementInterfaces()) {
				Class<?> eClass = ei.getInterfaceClass();
				logger.finest("Checking interface handler: "+ei+" for class: "+eClass);
				if (elementObject instanceof Class && eClass.isAssignableFrom((Class<?>)elementObject)) {
					logger.finer("Found interface match from class; "+elementObject);
					result.add(ei);
				} else if (eClass.isInstance(elementObject)) {
					logger.finer("Found interface match from object; "+elementObject);
					result.add(ei);
				}
			}
		}
		return result;
	}

	/**
	 * @see org.x4o.xml.element.ElementLanguage#findElementNamespaceContext(java.lang.String)
	 */
	public ElementNamespaceContext findElementNamespaceContext(String namespaceUri) {
		
		// TODO: refactor so no search for every tag !!
		ElementNamespaceContext result = null;
		for (int i=0;i<elementLanguageModules.size();i++) {
			ElementLanguageModule module = elementLanguageModules.get(i);
			result = module.getElementNamespaceContext(namespaceUri);
			if (result!=null) {
				return result;
			}
		}
		return result;
	}
}
