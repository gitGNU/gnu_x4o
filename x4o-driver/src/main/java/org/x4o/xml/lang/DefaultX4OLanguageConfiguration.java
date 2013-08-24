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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.x4o.xml.el.X4OELContext;
import org.x4o.xml.element.DefaultElement;
import org.x4o.xml.element.DefaultElementAttributeValueParser;
import org.x4o.xml.element.DefaultElementBodyCharacters;
import org.x4o.xml.element.DefaultElementBodyComment;
import org.x4o.xml.element.DefaultElementBodyWhitespace;
import org.x4o.xml.element.DefaultElementClass;
import org.x4o.xml.element.DefaultElementClassAttribute;
import org.x4o.xml.element.DefaultElementInterface;
import org.x4o.xml.element.DefaultElementNamespace;
import org.x4o.xml.element.DefaultElementNamespaceInstanceProvider;
import org.x4o.xml.element.DefaultElementObjectPropertyValue;
import org.x4o.xml.element.DefaultGlobalAttributeHandlerComparator;

/**
 * Provides all implementions of the different parts of the language parser.
 * 
 * @author Willem Cazander
 * @version 1.0 27 Oct 2009
 */
public class DefaultX4OLanguageConfiguration extends AbstractX4OLanguageConfiguration {
	
	/**
	 * Default constructor.
	 */
	public DefaultX4OLanguageConfiguration() {
	}
	
	public void fillDefaults() {
		if (getLanguageResourcePathPrefix()==null) {				setLanguageResourcePathPrefix(					X4OLanguageConfiguration.DEFAULT_LANG_PATH_PREFIX);	}
		if (getLanguageResourceModulesFileName()==null) {			setLanguageResourceModulesFileName(				X4OLanguageConfiguration.DEFAULT_LANG_MODULES_FILE);}
		if (getDefaultElementNamespace()==null) {					setDefaultElementNamespace(						DefaultElementNamespace.class);						}
		if (getDefaultElementInterface()==null) {					setDefaultElementInterface(						DefaultElementInterface.class);						}
		if (getDefaultElement()==null) {							setDefaultElement(								DefaultElement.class);								}
		if (getDefaultElementClass()==null) {						setDefaultElementClass(							DefaultElementClass.class);							}
		if (getDefaultElementClassAttribute()==null) {				setDefaultElementClassAttribute(				DefaultElementClassAttribute.class);				}
		if (getDefaultElementLanguageModule()==null) {				setDefaultElementLanguageModule(				DefaultX4OLanguageModule.class);					}
		if (getDefaultElementBodyComment()==null) {					setDefaultElementBodyComment(					DefaultElementBodyComment.class);					}
		if (getDefaultElementBodyCharacters()==null) {				setDefaultElementBodyCharacters(				DefaultElementBodyCharacters.class);				}
		if (getDefaultElementBodyWhitespace()==null) {				setDefaultElementBodyWhitespace(				DefaultElementBodyWhitespace.class);				}
		if (getDefaultElementNamespaceInstanceProvider()==null) {	setDefaultElementNamespaceInstanceProvider(		DefaultElementNamespaceInstanceProvider.class);		}
		if (getDefaultElementAttributeValueParser()==null) {		setDefaultElementAttributeValueParser(			DefaultElementAttributeValueParser.class);			}
		if (getDefaultElementObjectPropertyValue()==null) {			setDefaultElementObjectPropertyValue(			DefaultElementObjectPropertyValue.class);			}
		if (getDefaultElementAttributeHandlerComparator()==null) {	setDefaultElementAttributeHandlerComparator(	DefaultGlobalAttributeHandlerComparator.class);		}
		if (getDefaultLanguageVersionFilter()==null) {				setDefaultLanguageVersionFilter(				DefaultX4OLanguageVersionFilter.class);				}
		if (getDefaultLanguageLoader()==null) {						setDefaultLanguageLoader(						DefaultX4OLanguageLoader.class);					}
		if (getDefaultExpressionLanguageContext()==null) {			setDefaultExpressionLanguageContext(			X4OELContext.class);								}
	}
	
	/**
	 * @see org.x4o.xml.lang.X4OLanguageConfigurationLocal#createProxy()
	 */
	public X4OLanguageConfiguration createProxy() {
		Object proxy = Proxy.newProxyInstance(X4OLanguageClassLoader.getClassLoader(), new Class[]{X4OLanguageConfiguration.class}, new InvocationHandler() {
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				X4OLanguageConfigurationLocal local = DefaultX4OLanguageConfiguration.this;
				int argsLength = 0;
				if (args!=null) {
					argsLength = args.length;
				}
				Class<?>[] invokeArgs = new Class[argsLength];
				for (int i=0;i<argsLength;i++) {
					//Object o = args[i];
					invokeArgs[i] = X4OLanguageSession.class; //o.getClass(); todo fix
				}
				Method localMethod = local.getClass().getMethod(method.getName(), invokeArgs);
				Object result = localMethod.invoke(local, args);
				return result; // result is reflection safe interface hiding.
			}
		});
		return (X4OLanguageConfiguration)proxy;
	}
}
