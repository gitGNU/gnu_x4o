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

package org.x4o.xml;

import java.util.Collection;

import org.x4o.xml.core.config.X4OLanguage;
import org.x4o.xml.core.config.X4OLanguagePropertyKeys;
import org.x4o.xml.core.phase.X4OPhaseException;
import org.x4o.xml.core.phase.X4OPhaseType;
import org.x4o.xml.element.ElementLanguage;
import org.x4o.xml.io.DefaultX4OReader;
import org.x4o.xml.io.DefaultX4OSchemaWriter;
import org.x4o.xml.io.DefaultX4OWriter;
import org.x4o.xml.io.X4OReader;
import org.x4o.xml.io.X4OSchemaWriter;
import org.x4o.xml.io.X4OWriter;

/**
 * This is the starting point of the XML X4O Language Driver.
 * 
 * @author Willem Cazander
 * @version 1.0 Aug 11, 2005
 */
public abstract class X4ODriver<T> {

	/** Defines the default version if none is defined. */
	public final static String DEFAULT_LANGUAGE_VERSION = "1.0";
	
	/**
	 * Force public constructor and register the driver.
	 */
	public X4ODriver() {
		X4ODriverManager.registerX4ODriver(this);
	}
	
	abstract public String getLanguageName();
	
	abstract public String[] getLanguageVersions();
	
	abstract public X4OLanguage buildLanguage(String version);
	
	public X4OLanguage createLanguage(String version) {
		X4OLanguage result = null;
		if (result==null) {
			result = buildLanguage(version);
		}
		return result;
	}
	
	public ElementLanguage createLanguageContext() {
		return createLanguageContext(getLanguageVersionDefault());
	}
	
	public ElementLanguage createLanguageContext(String version) {
		X4OLanguage language = createLanguage(version);
		ElementLanguage result = language.getLanguageConfiguration().createElementLanguage(this);
		
		try {
			result.getLanguage().getPhaseManager().runPhases(result, X4OPhaseType.INIT);
		} catch (X4OPhaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
	
	public X4OSchemaWriter createSchemaWriter() {
		return createSchemaWriter(getLanguageVersionDefault());
	}
	
	public X4OSchemaWriter createSchemaWriter(String version) {
		return new DefaultX4OSchemaWriter(createLanguageContext(version));
	}
	
	public X4OReader<T> createReader() {
		return createReader(getLanguageVersionDefault());
	}
	
	public X4OReader<T> createReader(String version) {
		return new DefaultX4OReader<T>(createLanguageContext(version));
	}
	
	public X4OWriter<T> createWriter() {
		return createWriter(getLanguageVersionDefault());
	}
	
	public X4OWriter<T> createWriter(String version) {
		return new DefaultX4OWriter<T>(createLanguageContext(version));
	}

	public String getLanguageVersionDefault() {
		String[] lang = getLanguageVersions();
		if (lang==null || lang.length==0) {
			return DEFAULT_LANGUAGE_VERSION;
		}
		String langVersion = lang[lang.length-1];
		return langVersion;
	}
	
	/**
	 * @return Returns the property keys which can be set.
	 */
	public String[] getGlobalPropertyKeySet() {
		return X4OLanguagePropertyKeys.DEFAULT_X4O_GLOBAL_KEYS;
	}
	
	/**
	 * @return Returns the property keys which are set.
	 */
	final public Collection<String> getGlobalPropertyKeys() {
		return X4ODriverManager.getGlobalPropertyKeys(this);
	}
	
	final public Object getGlobalProperty(String key) {
		return X4ODriverManager.getGlobalProperty(this, key);
	}
	
	final public void setGlobalProperty(String key,Object value) {
		X4ODriverManager.setGlobalProperty(this, key, value);
	}
}
