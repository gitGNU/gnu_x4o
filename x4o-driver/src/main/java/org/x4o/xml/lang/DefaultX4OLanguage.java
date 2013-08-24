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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.x4o.xml.el.X4OExpressionFactory;
import org.x4o.xml.element.Element;
import org.x4o.xml.element.ElementAttributeValueParser;
import org.x4o.xml.element.ElementBindingHandler;
import org.x4o.xml.element.ElementClass;
import org.x4o.xml.element.ElementInterface;
import org.x4o.xml.element.ElementNamespace;
import org.x4o.xml.element.ElementNamespaceInstanceProviderException;
import org.x4o.xml.element.ElementObjectPropertyValue;
import org.x4o.xml.lang.phase.X4OPhaseException;
import org.x4o.xml.lang.phase.X4OPhaseManager;
import org.x4o.xml.lang.phase.X4OPhaseType;

/**
 * DefaultX4OLanguage holds all information about the x4o xml language.
 * 
 * @author Willem Cazander
 * @version 1.0 30 apr 2013
 */
public class DefaultX4OLanguage implements X4OLanguageLocal {

	private Logger logger = null;
	private X4OLanguageConfiguration languageConfiguration = null;
	private List<X4OLanguageModule> elementLanguageModules = null;
	private String languageName = null;
	private String languageVersion = null;
	private X4OPhaseManager phaseManager = null;
	
	public DefaultX4OLanguage(X4OLanguageConfiguration languageConfiguration,X4OPhaseManager phaseManager,String languageName,String languageVersion) {
		if (languageName==null) {
			throw new NullPointerException("Can't define myself with null name.");
		}
		if (languageVersion==null) {
			throw new NullPointerException("Can't define myself with null version.");
		}
		logger = Logger.getLogger(DefaultX4OLanguage.class.getName());
		elementLanguageModules = new ArrayList<X4OLanguageModule>(20);
		this.languageConfiguration=languageConfiguration;
		this.languageName=languageName;
		this.languageVersion=languageVersion;
		this.phaseManager=phaseManager;
	}
	
	/**
	 * @see org.x4o.xml.lang.X4OLanguage#getLanguageName()
	 */
	public String getLanguageName() {
		return languageName;
	}

	/**
	 * @see org.x4o.xml.lang.X4OLanguage#getLanguageVersion()
	 */
	public String getLanguageVersion() {
		return languageVersion;
	}
	
	/**
	 * @see org.x4o.xml.lang.X4OLanguage#getPhaseManager()
	 */
	public X4OPhaseManager getPhaseManager() {
		return phaseManager;
	}

	/**
	 * @return the languageConfiguration
	 */
	public X4OLanguageConfiguration getLanguageConfiguration() {
		return languageConfiguration;
	}

	/**
	 * @see org.x4o.xml.lang.X4OLanguageLocal#addLanguageModule(org.x4o.xml.lang.X4OLanguageModule)
	 */
	public void addLanguageModule(X4OLanguageModule elementLanguageModule) {
		if (elementLanguageModule.getId()==null) {
			throw new NullPointerException("Can't add module without id.");
		}
		elementLanguageModules.add(elementLanguageModule);
	}

	/**
	 * @see org.x4o.xml.lang.X4OLanguage#getLanguageModules()
	 */
	public List<X4OLanguageModule> getLanguageModules() {
		return elementLanguageModules;
	}
	
	/**
	 * @throws X4OPhaseException 
	 * @see org.x4o.xml.lang.X4OLanguage#createLanguageContext(org.x4o.xml.X4ODriver)
	 */
	public X4OLanguageSession createLanguageSession() {
		X4OLanguageSession result = buildElementLanguage(new DefaultX4OLanguageSession(this));
		try {
			getPhaseManager().runPhases(result, X4OPhaseType.INIT);
		} catch (X4OPhaseException e) {
			throw new RuntimeException(e); //TODO: change layer
		}
		return result;
	}

	protected X4OLanguageSession buildElementLanguage(X4OLanguageSession languageSession) {
		if ((languageSession instanceof X4OLanguageSessionLocal)==false) { 
			throw new RuntimeException("Can't init X4OLanguageSession which has not X4OLanguageSessionLocal interface obj: "+languageSession);
		}
		X4OLanguageSessionLocal contextInit = (X4OLanguageSessionLocal)languageSession; 
		try {
			if (contextInit.getExpressionLanguageFactory()==null) {
				contextInit.setExpressionLanguageFactory(X4OExpressionFactory.createExpressionFactory());
			}
			if (contextInit.getExpressionLanguageContext()==null) {
				contextInit.setExpressionLanguageContext(X4OExpressionFactory.createELContext(contextInit.getLanguage().getLanguageConfiguration().getDefaultExpressionLanguageContext()));
			}
			if (contextInit.getElementAttributeValueParser()==null) {
				contextInit.setElementAttributeValueParser((ElementAttributeValueParser)X4OLanguageClassLoader.newInstance(getLanguageConfiguration().getDefaultElementAttributeValueParser()));
			}
			if (contextInit.getElementObjectPropertyValue()==null) {
				contextInit.setElementObjectPropertyValue((ElementObjectPropertyValue)X4OLanguageClassLoader.newInstance(getLanguageConfiguration().getDefaultElementObjectPropertyValue()));
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(),e);
		}
		return contextInit;
	}

	/**
	 * @see org.x4o.xml.lang.X4OLanguage#createElementInstance(org.x4o.xml.lang.X4OLanguageSession,java.lang.Class)
	 */
	public Element createElementInstance(X4OLanguageSession context,Class<?> objectClass) {
		for (X4OLanguageModule modContext:getLanguageModules()) {
			for (ElementNamespace nsContext:modContext.getElementNamespaces()) {
				for (ElementClass ec:nsContext.getElementClasses()) {
					if (ec.getObjectClass()!=null && ec.getObjectClass().equals(objectClass)) { 
						try {
							return nsContext.getElementNamespaceInstanceProvider().createElementInstance(context, ec.getId());
						} catch (ElementNamespaceInstanceProviderException e) {
							throw new RuntimeException(e.getMessage(),e); // TODO: fix me
						}
					}
				}
			}
		}
		throw new IllegalArgumentException("Could not find ElementClass for: "+objectClass.getName());
	}

	/**
	 * @see org.x4o.xml.lang.X4OLanguage#findElementBindingHandlers(java.lang.Object)
	 */
	public List<ElementBindingHandler> findElementBindingHandlers(Object parent) {
		List<ElementBindingHandler> result = new ArrayList<ElementBindingHandler>(50);
		for (int i=0;i<elementLanguageModules.size();i++) {
			X4OLanguageModule module = elementLanguageModules.get(i);
			findElementBindingHandlerInList(parent,null,result,module.getElementBindingHandlers(),false);
		}
		return result;
	}

	/**
	 * @see org.x4o.xml.lang.X4OLanguage#findElementBindingHandlers(java.lang.Object,java.lang.Object)
	 */
	public List<ElementBindingHandler> findElementBindingHandlers(Object parent,Object child) {
		List<ElementBindingHandler> result = new ArrayList<ElementBindingHandler>(50);
		for (int i=0;i<elementLanguageModules.size();i++) {
			X4OLanguageModule module = elementLanguageModules.get(i);
			findElementBindingHandlerInList(parent,child,result,module.getElementBindingHandlers(),true);
		}
		return result;
	}

	private void findElementBindingHandlerInList(Object parent,Object child,List<ElementBindingHandler> result,List<ElementBindingHandler> checkList,boolean checkChild) {
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
			if (checkChild==false) {
				result.add(binding); // All all handlers for parent only
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
	 * @see org.x4o.xml.lang.X4OLanguage#findElementInterfaces(java.lang.Object)
	 */
	public List<ElementInterface> findElementInterfaces(Object elementObject) {
		if (elementObject==null) {
			throw new NullPointerException("Can't search for null object.");
		}
		List<ElementInterface> result = new ArrayList<ElementInterface>(50);
		for (int i=0;i<elementLanguageModules.size();i++) {
			X4OLanguageModule module = elementLanguageModules.get(i);
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
	 * @see org.x4o.xml.lang.X4OLanguage#findElementNamespace(java.lang.String)
	 */
	public ElementNamespace findElementNamespace(String namespaceUri) {
		
		// TODO: refactor so no search for every tag !!
		ElementNamespace result = null;
		for (int i=0;i<elementLanguageModules.size();i++) {
			X4OLanguageModule module = elementLanguageModules.get(i);
			result = module.getElementNamespace(namespaceUri);
			if (result!=null) {
				return result;
			}
		}
		return result;
	}
}
