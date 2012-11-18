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

import org.x4o.xml.core.config.X4OLanguageClassLoader;
import org.x4o.xml.core.config.X4OLanguageProperty;
import org.x4o.xml.eld.EldParser;
import org.x4o.xml.element.AbstractElementBindingHandler;
import org.x4o.xml.element.Element;
import org.x4o.xml.element.ElementAttributeHandler;
import org.x4o.xml.element.ElementBindingHandler;
import org.x4o.xml.element.ElementBindingHandlerException;
import org.x4o.xml.element.ElementConfiguratorGlobal;
import org.x4o.xml.element.ElementLanguage;
import org.x4o.xml.element.ElementInterface;
import org.x4o.xml.element.ElementLanguageModule;
import org.x4o.xml.element.ElementNamespaceContext;
import org.x4o.xml.element.ElementNamespaceInstanceProvider;
import org.x4o.xml.element.ElementNamespaceInstanceProviderException;

/**
 * An ParentLanguageElementConfigurator.
 * 
 * This binds the main interfaces of the ELD language to an other Element
 * 
 * @author Willem Cazander
 * @version 1.0 Jan 19, 2007
 */
public class ElementModuleBindingHandler  extends AbstractElementBindingHandler {
	
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
		return ElementLanguageModule.class;
	}

	/**
	 * @see org.x4o.xml.element.ElementBindingHandler#getBindChildClasses()
	 */
	public Class<?>[] getBindChildClasses() {
		return CLASSES_CHILD;
	}

	/**
	 * @see org.x4o.xml.element.ElementBindingHandler#doBind(java.lang.Object, java.lang.Object, org.x4o.xml.element.Element)
	 */
	public void doBind(Object parentObject, Object childObject,Element childElement) throws ElementBindingHandlerException {
		
		@SuppressWarnings("rawtypes")
		Map m = (Map)childElement.getElementLanguage().getLanguageConfiguration().getLanguageProperty(X4OLanguageProperty.EL_BEAN_INSTANCE_MAP);
		if (m==null) {
			return;
		}
		ElementLanguage x4oParsingContext = (ElementLanguage)m.get(EldParser.EL_PARENT_LANGUAGE_ELEMENT_LANGUAGE);
		//ElementLanguageModule elementLanguageModule = (ElementLanguageModule)m.get(EldParser.PARENT_ELEMENT_LANGUAGE_MODULE);
		ElementLanguageModule elementLanguageModule = (ElementLanguageModule)parentObject;
		if (x4oParsingContext==null) {
			return;
		}
		if (elementLanguageModule==null) {
			return;
		}
		
		if (childObject instanceof ElementInterface) {
			ElementInterface elementInterface = (ElementInterface)childObject;
			elementLanguageModule.addElementInterface(elementInterface);
			return;
		}
		if (childObject instanceof ElementNamespaceContext) {
			ElementNamespaceContext elementNamespaceContext = (ElementNamespaceContext)childObject;
			try {
				elementNamespaceContext.setElementNamespaceInstanceProvider((ElementNamespaceInstanceProvider)X4OLanguageClassLoader.newInstance(childElement.getElementLanguage().getLanguageConfiguration().getDefaultElementNamespaceInstanceProvider()));
			} catch (Exception e) {
				throw new ElementBindingHandlerException("Error loading: "+e.getMessage(),e);
			}
			try {
				
				elementNamespaceContext.getElementNamespaceInstanceProvider().start(x4oParsingContext, elementNamespaceContext);
			} catch (ElementNamespaceInstanceProviderException e) {
				throw new ElementBindingHandlerException("Error starting: "+e.getMessage(),e);
			}
			elementLanguageModule.addElementNamespaceContext(elementNamespaceContext);
			return;
		}
		if (childObject instanceof ElementBindingHandler) {
			ElementBindingHandler elementBindingHandler = (ElementBindingHandler)childObject;
			elementLanguageModule.addElementBindingHandler(elementBindingHandler);
			return;
		}
		if (childObject instanceof ElementAttributeHandler) {
			ElementAttributeHandler elementAttributeHandler = (ElementAttributeHandler)childObject;
			elementLanguageModule.addElementAttributeHandler(elementAttributeHandler);
			return;
		}
		if (childObject instanceof ElementConfiguratorGlobal) {
			ElementConfiguratorGlobal elementConfigurator = (ElementConfiguratorGlobal)childObject;
			elementLanguageModule.addElementConfiguratorGlobal(elementConfigurator);
			return;
		}
	}
}
