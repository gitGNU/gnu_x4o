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

package	org.x4o.xml.eld.lang;

import java.util.Map;

import org.x4o.xml.eld.EldModuleLoader;
import org.x4o.xml.element.AbstractElementBindingHandler;
import org.x4o.xml.element.Element;
import org.x4o.xml.element.ElementAttributeHandler;
import org.x4o.xml.element.ElementBindingHandler;
import org.x4o.xml.element.ElementBindingHandlerException;
import org.x4o.xml.element.ElementConfiguratorGlobal;
import org.x4o.xml.element.ElementInterface;
import org.x4o.xml.element.ElementNamespaceContext;
import org.x4o.xml.element.ElementNamespaceInstanceProvider;
import org.x4o.xml.element.ElementNamespaceInstanceProviderException;
import org.x4o.xml.lang.X4OLanguageModule;
import org.x4o.xml.lang.X4OLanguage;
import org.x4o.xml.lang.X4OLanguageClassLoader;
import org.x4o.xml.lang.X4OLanguageProperty;

/**
 * An ParentLanguageElementConfigurator.
 * 
 * This binds the main interfaces of the ELD language to an other Element
 * 
 * @author Willem Cazander
 * @version 1.0 Jan 19, 2007
 */
public class ElementModuleBindingHandler  extends AbstractElementBindingHandler<X4OLanguageModule> {
	
	private final static Class<?>[] CLASSES_CHILD = new Class[] {
		ElementInterface.class,
		ElementNamespaceContext.class,
		ElementBindingHandler.class,
		ElementAttributeHandler.class,
		ElementConfiguratorGlobal.class,
	};
	
	
	/**
	 * @see org.x4o.xml.element.ElementBindingHandler#getBindParentClass()
	 */
	public Class<?> getBindParentClass() {
		return X4OLanguageModule.class;
	}

	/**
	 * @see org.x4o.xml.element.ElementBindingHandler#getBindChildClasses()
	 */
	public Class<?>[] getBindChildClasses() {
		return CLASSES_CHILD;
	}

	/**
	 * @see org.x4o.xml.element.AbstractElementBindingHandler#bindChild(org.x4o.xml.element.Element, java.lang.Object, java.lang.Object)
	 */
	public void bindChild(Element childElement,X4OLanguageModule languageModule, Object childObject) throws ElementBindingHandlerException {
		
		@SuppressWarnings("rawtypes")
		Map m = (Map)childElement.getLanguageContext().getLanguageProperty(X4OLanguageProperty.EL_BEAN_INSTANCE_MAP);
		if (m==null) {
			return;
		}
		X4OLanguage x4oParsingContext = (X4OLanguage)m.get(EldModuleLoader.EL_PARENT_LANGUAGE);
		//ElementLanguageModule elementLanguageModule = (ElementLanguageModule)m.get(EldParser.PARENT_ELEMENT_LANGUAGE_MODULE);
		if (x4oParsingContext==null) {
			return;
		}
		if (languageModule==null) {
			return;
		}
		
		if (childObject instanceof ElementInterface) {
			ElementInterface elementInterface = (ElementInterface)childObject;
			languageModule.addElementInterface(elementInterface);
			return;
		}
		if (childObject instanceof ElementNamespaceContext) {
			ElementNamespaceContext elementNamespaceContext = (ElementNamespaceContext)childObject;
			try {
				elementNamespaceContext.setElementNamespaceInstanceProvider((ElementNamespaceInstanceProvider)X4OLanguageClassLoader.newInstance(childElement.getLanguageContext().getLanguage().getLanguageConfiguration().getDefaultElementNamespaceInstanceProvider()));
			} catch (Exception e) {
				throw new ElementBindingHandlerException("Error loading: "+e.getMessage(),e);
			}
			try {
				
				elementNamespaceContext.getElementNamespaceInstanceProvider().start(x4oParsingContext, elementNamespaceContext);
			} catch (ElementNamespaceInstanceProviderException e) {
				throw new ElementBindingHandlerException("Error starting: "+e.getMessage(),e);
			}
			languageModule.addElementNamespaceContext(elementNamespaceContext);
			return;
		}
		if (childObject instanceof ElementBindingHandler) {
			ElementBindingHandler elementBindingHandler = (ElementBindingHandler)childObject;
			languageModule.addElementBindingHandler(elementBindingHandler);
			return;
		}
		if (childObject instanceof ElementAttributeHandler) {
			ElementAttributeHandler elementAttributeHandler = (ElementAttributeHandler)childObject;
			languageModule.addElementAttributeHandler(elementAttributeHandler);
			return;
		}
		if (childObject instanceof ElementConfiguratorGlobal) {
			ElementConfiguratorGlobal elementConfigurator = (ElementConfiguratorGlobal)childObject;
			languageModule.addElementConfiguratorGlobal(elementConfigurator);
			return;
		}
	}
	
	/**
	 * @see org.x4o.xml.element.AbstractElementBindingHandler#createChilderen(org.x4o.xml.element.Element, java.lang.Object)
	 */
	public void createChilderen(Element parentElement,X4OLanguageModule parent) throws ElementBindingHandlerException {
		for (ElementInterface child:parent.getElementInterfaces()) {
			createChild(parentElement, child);
		}
		for (ElementNamespaceContext child:parent.getElementNamespaceContexts()) {
			createChild(parentElement, child);
		}
		for (ElementBindingHandler child:parent.getElementBindingHandlers()) {
			createChild(parentElement, child);
		}
		for (ElementAttributeHandler child:parent.getElementAttributeHandlers()) {
			createChild(parentElement, child);
		}
		for (ElementConfiguratorGlobal child:parent.getElementConfiguratorGlobals()) {
			createChild(parentElement, child);
		}
	}
}
