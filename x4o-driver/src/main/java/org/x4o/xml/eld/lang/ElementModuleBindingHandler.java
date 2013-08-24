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
package	org.x4o.xml.eld.lang;

import org.x4o.xml.eld.EldModuleLoader;
import org.x4o.xml.element.AbstractElementBindingHandler;
import org.x4o.xml.element.Element;
import org.x4o.xml.element.ElementBindingHandler;
import org.x4o.xml.element.ElementBindingHandlerException;
import org.x4o.xml.element.ElementConfiguratorGlobal;
import org.x4o.xml.element.ElementInterface;
import org.x4o.xml.element.ElementNamespace;
import org.x4o.xml.element.ElementNamespaceInstanceProvider;
import org.x4o.xml.element.ElementNamespaceInstanceProviderException;
import org.x4o.xml.lang.X4OLanguageModule;
import org.x4o.xml.lang.X4OLanguage;
import org.x4o.xml.lang.X4OLanguageClassLoader;

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
		ElementNamespace.class,
		ElementBindingHandler.class,
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
		
		X4OLanguage x4oParsingContext = EldModuleLoader.getLanguage(childElement.getLanguageSession());
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
		if (childObject instanceof ElementNamespace) {
			ElementNamespace elementNamespace = (ElementNamespace)childObject;
			
			if (elementNamespace.getId()==null) {
				throw new NullPointerException("Can add elementNamespace without id.");
			}
			// TODO: no language here so move to EL default on eld attribute tag
			if (elementNamespace.getId()!=null) {
				StringBuffer buf = new StringBuffer(30);
				for (char c:elementNamespace.getId().toLowerCase().toCharArray()) {
					if (Character.isLetter(c))	{buf.append(c);}
					if (Character.isDigit(c))	{buf.append(c);}
					if ('-'==c)					{buf.append(c);}
				}
				String id = buf.toString();
				elementNamespace.setId(id);
			}
			if (elementNamespace.getUri()==null) {
				elementNamespace.setUri(
						"http://"+languageModule.getProviderHost()+
						"/xml/ns/"+x4oParsingContext.getLanguageName()+
						"-"+elementNamespace.getId());
			}
			if (elementNamespace.getSchemaUri()==null) {
				elementNamespace.setSchemaUri(
						"http://"+languageModule.getProviderHost()+
						"/xml/ns/"+x4oParsingContext.getLanguageName()+
						"-"+elementNamespace.getId()+
						"-"+x4oParsingContext.getLanguageVersion()+
						".xsd"
					);
			}
			if (elementNamespace.getSchemaResource()==null) {
				elementNamespace.setSchemaResource(
						x4oParsingContext.getLanguageName()+
						"-"+elementNamespace.getId()+
						"-"+x4oParsingContext.getLanguageVersion()+
						".xsd"
					);
			}
			if (elementNamespace.getSchemaPrefix()==null) {
				elementNamespace.setSchemaPrefix(elementNamespace.getId());
			}
			
			try {
				elementNamespace.setElementNamespaceInstanceProvider((ElementNamespaceInstanceProvider)X4OLanguageClassLoader.newInstance(childElement.getLanguageSession().getLanguage().getLanguageConfiguration().getDefaultElementNamespaceInstanceProvider()));
			} catch (Exception e) {
				throw new ElementBindingHandlerException("Error loading: "+e.getMessage(),e);
			}
			try {
				
				elementNamespace.getElementNamespaceInstanceProvider().start(x4oParsingContext, elementNamespace);
			} catch (ElementNamespaceInstanceProviderException e) {
				throw new ElementBindingHandlerException("Error starting: "+e.getMessage(),e);
			}
			languageModule.addElementNamespace(elementNamespace);
			return;
		}
		if (childObject instanceof ElementBindingHandler) {
			ElementBindingHandler elementBindingHandler = (ElementBindingHandler)childObject;
			languageModule.addElementBindingHandler(elementBindingHandler);
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
		for (ElementNamespace child:parent.getElementNamespaces()) {
			createChild(parentElement, child);
		}
		for (ElementBindingHandler child:parent.getElementBindingHandlers()) {
			createChild(parentElement, child);
		}
		for (ElementConfiguratorGlobal child:parent.getElementConfiguratorGlobals()) {
			createChild(parentElement, child);
		}
	}
}
