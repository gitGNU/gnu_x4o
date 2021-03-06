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

/**
 * AbstractX4OLanguageConfiguration.
 * 
 * @author Willem Cazander
 * @version 1.0 Apr 28, 2013
 */
public abstract class AbstractX4OLanguageConfiguration implements X4OLanguageConfigurationLocal {
	
	private String languageResourcePathPrefix = null;
	private String languageResourceModulesFileName = null;
	
	private Class<?> defaultElementNamespace = null;
	private Class<?> defaultElementInterface = null;
	private Class<?> defaultElement = null;
	private Class<?> defaultElementClass = null;
	private Class<?> defaultElementClassAttribute = null;
	
	private Class<?> defaultElementLanguageModule = null;
	private Class<?> defaultElementBodyComment = null;
	private Class<?> defaultElementBodyCharacters = null;
	private Class<?> defaultElementBodyWhitespace = null;
	private Class<?> defaultElementNamespaceInstanceProvider = null;
	private Class<?> defaultElementAttributeValueParser = null;
	private Class<?> defaultElementObjectPropertyValue = null;
	private Class<?> defaultElementNamespaceAttributeComparator = null;
	
	private Class<?> defaultLanguageVersionFilter = null;
	private Class<?> defaultLanguageLoader = null;
	private Class<?> defaultExpressionLanguageContext = null;
	
	/**
	 * Default constructor.
	 */
	public AbstractX4OLanguageConfiguration() {
	}
	
	/**
	 * @return the languageResourcePathPrefix
	 */
	public String getLanguageResourcePathPrefix() {
		return languageResourcePathPrefix;
	}
	
	/**
	 * @param languageResourcePathPrefix the languageResourcePathPrefix to set
	 */
	public void setLanguageResourcePathPrefix(String languageResourcePathPrefix) {
		this.languageResourcePathPrefix = languageResourcePathPrefix;
	}
	
	/**
	 * @return the languageResourceModulesFileName
	 */
	public String getLanguageResourceModulesFileName() {
		return languageResourceModulesFileName;
	}
	
	/**
	 * @param languageResourceModulesFileName the languageResourceModulesFileName to set
	 */
	public void setLanguageResourceModulesFileName(String languageResourceModulesFileName) {
		this.languageResourceModulesFileName = languageResourceModulesFileName;
	}
	
	/**
	 * @return the defaultElementNamespace
	 */
	public Class<?> getDefaultElementNamespace() {
		return defaultElementNamespace;
	}
	
	/**
	 * @param defaultElementNamespace the defaultElementNamespace to set
	 */
	public void setDefaultElementNamespace(Class<?> defaultElementNamespace) {
		this.defaultElementNamespace = defaultElementNamespace;
	}
	
	/**
	 * @return the defaultElementInterface
	 */
	public Class<?> getDefaultElementInterface() {
		return defaultElementInterface;
	}
	
	/**
	 * @param defaultElementInterface the defaultElementInterface to set
	 */
	public void setDefaultElementInterface(Class<?> defaultElementInterface) {
		this.defaultElementInterface = defaultElementInterface;
	}
	
	/**
	 * @return the defaultElement
	 */
	public Class<?> getDefaultElement() {
		return defaultElement;
	}
	
	/**
	 * @param defaultElement the defaultElement to set
	 */
	public void setDefaultElement(Class<?> defaultElement) {
		this.defaultElement = defaultElement;
	}
	
	/**
	 * @return the defaultElementClass
	 */
	public Class<?> getDefaultElementClass() {
		return defaultElementClass;
	}
	
	/**
	 * @param defaultElementClass the defaultElementClass to set
	 */
	public void setDefaultElementClass(Class<?> defaultElementClass) {
		this.defaultElementClass = defaultElementClass;
	}
	
	/**
	 * @return the defaultElementClassAttribute
	 */
	public Class<?> getDefaultElementClassAttribute() {
		return defaultElementClassAttribute;
	}
	
	/**
	 * @param defaultElementClassAttribute the defaultElementClassAttribute to set
	 */
	public void setDefaultElementClassAttribute(Class<?> defaultElementClassAttribute) {
		this.defaultElementClassAttribute = defaultElementClassAttribute;
	}
	
	/**
	 * @return the defaultElementLanguageModule
	 */
	public Class<?> getDefaultElementLanguageModule() {
		return defaultElementLanguageModule;
	}
	
	/**
	 * @param defaultElementLanguageModule the defaultElementLanguageModule to set
	 */
	public void setDefaultElementLanguageModule(Class<?> defaultElementLanguageModule) {
		this.defaultElementLanguageModule = defaultElementLanguageModule;
	}
	
	/**
	 * @return the defaultElementBodyComment
	 */
	public Class<?> getDefaultElementBodyComment() {
		return defaultElementBodyComment;
	}
	
	/**
	 * @param defaultElementBodyComment the defaultElementBodyComment to set
	 */
	public void setDefaultElementBodyComment(Class<?> defaultElementBodyComment) {
		this.defaultElementBodyComment = defaultElementBodyComment;
	}
	
	/**
	 * @return the defaultElementBodyCharacters
	 */
	public Class<?> getDefaultElementBodyCharacters() {
		return defaultElementBodyCharacters;
	}
	
	/**
	 * @param defaultElementBodyCharacters the defaultElementBodyCharacters to set
	 */
	public void setDefaultElementBodyCharacters(Class<?> defaultElementBodyCharacters) {
		this.defaultElementBodyCharacters = defaultElementBodyCharacters;
	}
	
	/**
	 * @return the defaultElementBodyWhitespace
	 */
	public Class<?> getDefaultElementBodyWhitespace() {
		return defaultElementBodyWhitespace;
	}
	
	/**
	 * @param defaultElementBodyWhitespace the defaultElementBodyWhitespace to set
	 */
	public void setDefaultElementBodyWhitespace(Class<?> defaultElementBodyWhitespace) {
		this.defaultElementBodyWhitespace = defaultElementBodyWhitespace;
	}
	
	/**
	 * @return the defaultElementNamespaceInstanceProvider
	 */
	public Class<?> getDefaultElementNamespaceInstanceProvider() {
		return defaultElementNamespaceInstanceProvider;
	}
	
	/**
	 * @param defaultElementNamespaceInstanceProvider the defaultElementNamespaceInstanceProvider to set
	 */
	public void setDefaultElementNamespaceInstanceProvider(Class<?> defaultElementNamespaceInstanceProvider) {
		this.defaultElementNamespaceInstanceProvider = defaultElementNamespaceInstanceProvider;
	}
	
	/**
	 * @return the defaultElementAttributeValueParser
	 */
	public Class<?> getDefaultElementAttributeValueParser() {
		return defaultElementAttributeValueParser;
	}
	
	/**
	 * @param defaultElementAttributeValueParser the defaultElementAttributeValueParser to set
	 */
	public void setDefaultElementAttributeValueParser(Class<?> defaultElementAttributeValueParser) {
		this.defaultElementAttributeValueParser = defaultElementAttributeValueParser;
	}
	
	/**
	 * @return the defaultElementObjectPropertyValue
	 */
	public Class<?> getDefaultElementObjectPropertyValue() {
		return defaultElementObjectPropertyValue;
	}
	
	/**
	 * @param defaultElementObjectPropertyValue the defaultElementObjectPropertyValue to set
	 */
	public void setDefaultElementObjectPropertyValue(Class<?> defaultElementObjectPropertyValue) {
		this.defaultElementObjectPropertyValue = defaultElementObjectPropertyValue;
	}
	
	/**
	 * @return the defaultElementNamespaceAttributeComparator
	 */
	public Class<?> getDefaultElementNamespaceAttributeComparator() {
		return defaultElementNamespaceAttributeComparator;
	}
	
	/**
	 * @param defaultElementNamespaceAttributeComparator the defaultElementNamespaceAttributeComparator to set
	 */
	public void setDefaultElementNamespaceAttributeComparator(Class<?> defaultElementNamespaceAttributeComparator) {
		this.defaultElementNamespaceAttributeComparator = defaultElementNamespaceAttributeComparator;
	}
	
	/**
	 * @return the defaultLanguageVersionFilter
	 */
	public Class<?> getDefaultLanguageVersionFilter() {
		return defaultLanguageVersionFilter;
	}
	
	/**
	 * @param defaultLanguageVersionFilter the defaultLanguageVersionFilter to set
	 */
	public void setDefaultLanguageVersionFilter(Class<?> defaultLanguageVersionFilter) {
		this.defaultLanguageVersionFilter = defaultLanguageVersionFilter;
	}
	
	/**
	 * @return the defaultLanguageLoader
	 */
	public Class<?> getDefaultLanguageLoader() {
		return defaultLanguageLoader;
	}
	
	/**
	 * @param defaultLanguageLoader the defaultLanguageLoader to set
	 */
	public void setDefaultLanguageLoader(Class<?> defaultLanguageLoader) {
		this.defaultLanguageLoader = defaultLanguageLoader;
	}
	
	/**
	 * @return the defaultExpressionLanguageContext
	 */
	public Class<?> getDefaultExpressionLanguageContext() {
		return defaultExpressionLanguageContext;
	}
	
	/**
	 * @param defaultExpressionLanguageContext the defaultExpressionLanguageContext to set
	 */
	public void setDefaultExpressionLanguageContext(Class<?> defaultExpressionLanguageContext) {
		this.defaultExpressionLanguageContext = defaultExpressionLanguageContext;
	}
}
