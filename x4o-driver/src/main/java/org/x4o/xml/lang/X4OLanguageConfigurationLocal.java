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

/**
 * X4OLanguageConfigurationLocal is for safe dynamic configuration.
 * 
 * @author Willem Cazander
 * @version 1.0 28 Apr 2013
 */
public interface X4OLanguageConfigurationLocal extends X4OLanguageConfiguration {

	X4OLanguageConfiguration createProxy();
	
	void setLanguageResourcePathPrefix(String value);
	void setLanguageResourceModulesFileName(String value);
	
	void setDefaultElementNamespace(Class<?> value);
	void setDefaultElementInterface(Class<?> value);
	void setDefaultElement(Class<?> value);
	void setDefaultElementClass(Class<?> value);
	void setDefaultElementClassAttribute(Class<?> value);
	
	void setDefaultElementLanguageModule(Class<?> value);
	void setDefaultElementBodyComment(Class<?> value);
	void setDefaultElementBodyCharacters(Class<?> value);
	void setDefaultElementBodyWhitespace(Class<?> value);
	void setDefaultElementNamespaceInstanceProvider(Class<?> value);
	void setDefaultElementAttributeValueParser(Class<?> value);
	void setDefaultElementObjectPropertyValue(Class<?> value);
	void setDefaultElementAttributeHandlerComparator(Class<?> value);
	
	void setDefaultLanguageVersionFilter(Class<?> value);
	void setDefaultLanguageLoader(Class<?> value);
	void setDefaultExpressionLanguageContext(Class<?> value);
}
