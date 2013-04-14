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

package org.x4o.xml.eld;

import java.util.logging.Logger;

import org.x4o.xml.conv.ObjectConverter;
import org.x4o.xml.conv.text.ClassConverter;

import org.x4o.xml.eld.lang.BeanElement;
import org.x4o.xml.eld.lang.DescriptionElement;
import org.x4o.xml.eld.lang.ElementClassAddParentElement;
import org.x4o.xml.eld.lang.ElementClassAttributeBindingHandler;
import org.x4o.xml.eld.lang.ElementClassBindingHandler;
import org.x4o.xml.eld.lang.ElementInterfaceBindingHandler;
import org.x4o.xml.eld.lang.ElementModuleBindingHandler;
import org.x4o.xml.eld.lang.ElementNamespaceContextBindingHandler;
import org.x4o.xml.eld.lang.ModuleElement;

import org.x4o.xml.element.ElementBindingHandler;
import org.x4o.xml.element.ElementClass;
import org.x4o.xml.element.ElementClassAttribute;
import org.x4o.xml.element.ElementNamespaceContext;
import org.x4o.xml.element.ElementNamespaceInstanceProvider;
import org.x4o.xml.element.ElementNamespaceInstanceProviderException;

import org.x4o.xml.lang.X4OLanguage;
import org.x4o.xml.lang.X4OLanguageLocal;
import org.x4o.xml.lang.X4OLanguageClassLoader;
import org.x4o.xml.lang.X4OLanguageModule;
import org.x4o.xml.lang.X4OLanguageModuleLoader;
import org.x4o.xml.lang.X4OLanguageModuleLoaderException;

/** 
 * EldModuleLoaderCore provides a few basic elements for the core eld x4o language.
 * 
 * @author Willem Cazander
 * @version 1.0 Jan 11, 2009
 */
public class EldModuleLoaderCore implements X4OLanguageModuleLoader {

	private Logger logger = null;
	private static final String PP_CEL_PROVIDER = "cel.x4o.org";
	private static final String PP_CEL_XMLNS = "http://"+PP_CEL_PROVIDER+"/xml/ns/";
	private static final String PP_CEL_XSD_FILE = "-1.0.xsd";
	private static final String CEL_CORE = "cel-core";
	private static final String CEL_ROOT = "cel-root";
	private static final String CEL_CORE_URI = PP_CEL_XMLNS+CEL_CORE;
	private static final String CEL_ROOT_URI = PP_CEL_XMLNS+CEL_ROOT;
	private static final String CEL_CORE_XSD_URI = CEL_CORE_URI+PP_CEL_XSD_FILE;
	private static final String CEL_ROOT_XSD_URI = CEL_ROOT_URI+PP_CEL_XSD_FILE;
	private static final String CEL_CORE_XSD_FILE = CEL_CORE+PP_CEL_XSD_FILE;
	private static final String CEL_ROOT_XSD_FILE = CEL_ROOT+PP_CEL_XSD_FILE;
	
	/**
	 * Creates the CEL module loader.
	 */
	public EldModuleLoaderCore() {
		logger = Logger.getLogger(EldModuleLoaderCore.class.getName());
	}
	
	/**
	 * Loads the CEL language into the module.
	 * @param language The langauge to load for.
	 * @param languageModule The module to load it in.
	 * @see org.x4o.xml.lang.X4OLanguageModuleLoader#loadLanguageModule(org.x4o.xml.lang.X4OLanguageLocal, org.x4o.xml.lang.X4OLanguageModule)
	 */
	public void loadLanguageModule(X4OLanguageLocal language,X4OLanguageModule languageModule) throws X4OLanguageModuleLoaderException {
		
		// Config module meta data
		configLanguageModule(languageModule);
		
		// Config language
		addBindingHandler(languageModule,new ElementClassBindingHandler(),"cel-class-bind","Binds the ElementClass childeren.");
		addBindingHandler(languageModule,new ElementModuleBindingHandler(),"cel-module-bind","Binds the LanguageModule childeren.");
		addBindingHandler(languageModule,new ElementClassAttributeBindingHandler(),"cel-class-attr-bind","Binds the ElementClassAttribute childeren.");
		addBindingHandler(languageModule,new ElementInterfaceBindingHandler(),"cel-interface-bind","Binds the ElementInterface childeren.");
		addBindingHandler(languageModule,new ElementNamespaceContextBindingHandler(),"cel-namespace-bind","Binds the Namespace childeren.");
		
		// Create cel-lang namespace in language
		ElementNamespaceContext namespace = createNamespaceContext(language,CEL_CORE,CEL_CORE_URI,CEL_CORE_XSD_URI,CEL_CORE_XSD_FILE,CEL_CORE);
		configElementClasses(language,namespace);
		startAndAddNamespace(language,languageModule,namespace);

		// Create cel-root namespace in language for schema support
		ElementNamespaceContext namespaceRoot = createNamespaceContext(language,CEL_ROOT,CEL_ROOT_URI,CEL_ROOT_XSD_URI,CEL_ROOT_XSD_FILE,CEL_ROOT);
		namespaceRoot.setLanguageRoot(true); // Only define single language root so xsd is (mostly) not cicle import.
		ElementClass rootElement = createElementClass(language,"module",language.getLanguageConfiguration().getDefaultElementLanguageModule(),ModuleElement.class,"The module tag is the root xml element for ELD language.");
		namespaceRoot.addElementClass(rootElement);
		startAndAddNamespace(language,languageModule,namespaceRoot);
	}
	
	/**
	 * Adds only Element class beans which need extra meta info for schema.
	 * @param namespace The namespace to config.
	 * @param language	The language to config for.
	 * @throws X4OLanguageModuleLoaderException 
	 */
	private void configElementClasses(X4OLanguage language,ElementNamespaceContext namespace) throws X4OLanguageModuleLoaderException {
		ElementClass ec = null;
		
		namespace.addElementClass(createElementClass(language,"attribute",language.getLanguageConfiguration().getDefaultElementClassAttribute(),null,"Defines xml element attribute."));
		namespace.addElementClass(createElementClass(language,"classConverter",ClassConverter.class,null,"Converts string attribute to java class instance."));
		
		ec = createElementClass(language,"namespace",language.getLanguageConfiguration().getDefaultElementNamespaceContext(),null,"Defines an xml namespace.");
		ec.addElementClassAttribute(createElementClassAttribute(language,"uri",true,null));
		namespace.addElementClass(ec);
		
		ec = createElementClass(language,"element",language.getLanguageConfiguration().getDefaultElementClass(),null,"Defines xml element tag.");
		ec.addElementClassAttribute(createElementClassAttribute(language,"objectClass",false,new ClassConverter()));
		ec.addElementClassAttribute(createElementClassAttribute(language,"elementClass",false,new ClassConverter()));
		namespace.addElementClass(ec);
		
		ec = createElementClass(language,"elementInterface",language.getLanguageConfiguration().getDefaultElementInterface(),null,"Defines element interface class.");
		ec.addElementClassAttribute(createElementClassAttribute(language,"interfaceClass",false,new ClassConverter()));
		namespace.addElementClass(ec);
		
		ec = createElementClass(language,"bindingHandler",null,BeanElement.class,"Defines generic binding handler for languge.");
		ec.addElementParent(CEL_ROOT_URI, "module");
		ec.addElementParent(CEL_CORE_URI, "elementInterface");
		ec.addElementClassAttribute(createElementClassAttribute(language,"id",true,null));
		ec.addElementClassAttribute(createElementClassAttribute(language,"bean.class",true,null));
		namespace.addElementClass(ec);
		
		ec = createElementClass(language,"attributeHandler",null,BeanElement.class,"Defines generic attribute handler for language.");
		ec.addElementParent(CEL_ROOT_URI, "module");
		ec.addElementClassAttribute(createElementClassAttribute(language,"bean.class",true,null));
		namespace.addElementClass(ec);
		
		ec = createElementClass(language,"configurator",null,BeanElement.class,"Define generic configurator for language.");
		ec.addElementParent(CEL_CORE_URI, "elementInterface");
		ec.addElementParent(CEL_CORE_URI, "element");
		ec.addElementClassAttribute(createElementClassAttribute(language,"bean.class",true,null));
		ec.addElementClassAttribute(createElementClassAttribute(language,"configAction",false,null));
		namespace.addElementClass(ec);
		
		ec = createElementClass(language,"configuratorGlobal",null,BeanElement.class,"Define generic global configuator for languge.");
		ec.addElementParent(CEL_ROOT_URI, "module");
		ec.addElementClassAttribute(createElementClassAttribute(language,"bean.class",true,null));
		ec.addElementClassAttribute(createElementClassAttribute(language,"configAction",false,null));
		namespace.addElementClass(ec);
		
		ec = createElementClass(language,"description",null,DescriptionElement.class,"Adds description as text on all eld elements.");
		ec.setSchemaContentBase("string");
		ec.addElementParent(CEL_ROOT_URI, "module");
		ec.addElementParent(CEL_CORE_URI, "namespace");
		ec.addElementParent(CEL_CORE_URI, "attributeHandler");
		ec.addElementParent(CEL_CORE_URI, "bindingHandler");
		ec.addElementParent(CEL_CORE_URI, "configurator");
		ec.addElementParent(CEL_CORE_URI, "configuratorGlobal");
		ec.addElementParent(CEL_CORE_URI, "elementInterface");
		ec.addElementParent(CEL_CORE_URI, "element");
		ec.addElementParent(CEL_CORE_URI, "attribute");
		namespace.addElementClass(ec);
		
		ec = createElementClass(language,"elementParent",null,ElementClassAddParentElement.class,"Added (meta) element parent.");
		ec.addElementParent(CEL_CORE_URI, "element");
		ec.addElementParent(CEL_CORE_URI, "elementInterface");
		ec.addElementClassAttribute(createElementClassAttribute(language,"tag",true,null));
		ec.addElementClassAttribute(createElementClassAttribute(language,"uri",false,null));
		namespace.addElementClass(ec);
	}
	
	private void configLanguageModule(X4OLanguageModule languageModule) {
		languageModule.setId("cel-module");
		languageModule.setName("Core Element Languag Module");
		languageModule.setProviderName(PP_CEL_PROVIDER);
		languageModule.setDescription("Core Element Language Module Loader");
		languageModule.setSourceResource(this.getClass().getSimpleName()); // todo check if oke.
	}
	
	private void startAndAddNamespace(X4OLanguageLocal language,X4OLanguageModule languageModule,ElementNamespaceContext namespace) throws X4OLanguageModuleLoaderException {
		try {
			namespace.getElementNamespaceInstanceProvider().start(language, namespace);
		} catch (ElementNamespaceInstanceProviderException e) {
			throw new X4OLanguageModuleLoaderException(this,"Error starting instance provider: "+e.getMessage(),e);
		}
		languageModule.addElementNamespaceContext(namespace);
	}
	
	private ElementNamespaceContext createNamespaceContext(X4OLanguageLocal language,String id,String uri,String schemaUri,String schemaResource,String schemaPrefix) throws X4OLanguageModuleLoaderException {
		logger.finer("Creating "+language.getLanguageName()+" namespace.");
		ElementNamespaceContext namespace;
		try {
			namespace = (ElementNamespaceContext)X4OLanguageClassLoader.newInstance(language.getLanguageConfiguration().getDefaultElementNamespaceContext());
		} catch (InstantiationException e) {
			throw new X4OLanguageModuleLoaderException(this,e.getMessage(),e);
		} catch (IllegalAccessException e) {
			throw new X4OLanguageModuleLoaderException(this,e.getMessage(),e);
		} 
		try {
			namespace.setElementNamespaceInstanceProvider((ElementNamespaceInstanceProvider)
				X4OLanguageClassLoader.newInstance(language.getLanguageConfiguration().getDefaultElementNamespaceInstanceProvider())
				);
		} catch (InstantiationException e) {
			throw new X4OLanguageModuleLoaderException(this,e.getMessage(),e);
		} catch (IllegalAccessException e) {
			throw new X4OLanguageModuleLoaderException(this,e.getMessage(),e);
		} 
		
		namespace.setId(id);
		namespace.setUri(uri);
		namespace.setSchemaUri(schemaUri);
		namespace.setSchemaResource(schemaResource);
		namespace.setSchemaPrefix(schemaPrefix);
		return namespace;
	}
	
	private ElementClass createElementClass(X4OLanguage language,String tag,Class<?> objectClass,Class<?> elementClass,String description) throws X4OLanguageModuleLoaderException {
		try {
			ElementClass result = (ElementClass)X4OLanguageClassLoader.newInstance(language.getLanguageConfiguration().getDefaultElementClass());
			result.setTag(tag);
			result.setObjectClass(objectClass);
			result.setElementClass(elementClass);
			result.setDescription(description);
			return result;
		} catch (InstantiationException e) {
			throw new X4OLanguageModuleLoaderException(this,e.getMessage(),e);
		} catch (IllegalAccessException e) {
			throw new X4OLanguageModuleLoaderException(this,e.getMessage(),e);
		}
	}
	
	/**
	 * Creates new configed ElementClassAttribute instance.
	 * @param language	The X4OLanguage to create from.
	 * @param name The name of the attribute.
	 * @param required Is the attribute required.
	 * @param converter The converter for the attribute.
	 * @return	The new ElementClassAttribute instance.
	 * @throws X4OLanguageModuleLoaderException	When class could not be created.
	 */
	private ElementClassAttribute createElementClassAttribute(X4OLanguage language,String name,boolean required,ObjectConverter converter) throws X4OLanguageModuleLoaderException {
		try {
			ElementClassAttribute result = (ElementClassAttribute)X4OLanguageClassLoader.newInstance(language.getLanguageConfiguration().getDefaultElementClassAttribute());
			result.setId(name); // ??
			result.setName(name);
			if (required) {
				result.setRequired(required);
			}
			if (converter!=null) {
				result.setObjectConverter(converter);
			}
			return result;
		} catch (InstantiationException e) {
			throw new X4OLanguageModuleLoaderException(this,e.getMessage(),e);
		} catch (IllegalAccessException e) {
			throw new X4OLanguageModuleLoaderException(this,e.getMessage(),e);
		}
	}
	
	/**
	 * Adds binding handler to module.
	 * @param languageModule The language module.
	 * @param handler The handler to add the the module.
	 * @param id	The handler id.
	 * @param description	The handler descripion.
	 */
	private void addBindingHandler(X4OLanguageModule languageModule,ElementBindingHandler handler,String id,String description) {
		handler.setId(id);
		handler.setDescription(description);
		languageModule.addElementBindingHandler(handler);
	}
}
