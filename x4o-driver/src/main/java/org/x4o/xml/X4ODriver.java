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
package org.x4o.xml;

import org.x4o.xml.io.DefaultX4OReader;
import org.x4o.xml.io.DefaultX4OSchemaWriter;
import org.x4o.xml.io.DefaultX4OWriter;
import org.x4o.xml.io.X4OReader;
import org.x4o.xml.io.X4OReaderContext;
import org.x4o.xml.io.X4OSchemaWriter;
import org.x4o.xml.io.X4OWriter;
import org.x4o.xml.io.X4OWriterContext;

import org.x4o.xml.lang.X4OLanguageConfiguration;
import org.x4o.xml.lang.X4OLanguageContext;
import org.x4o.xml.lang.X4OLanguage;
import org.x4o.xml.lang.phase.X4OPhaseManager;

/**
 * X4ODriver Is the x4o language driver to interact with xml.
 * 
 * @author Willem Cazander
 * @version 1.0 Aug 11, 2005
 */
public abstract class X4ODriver<T> {

	/** Defines the default version if none is defined. */
	public final static String DEFAULT_LANGUAGE_VERSION = "1.0";
	
	/**
	 * Public constructor.
	 */
	public X4ODriver() {
	}
	
	/**
	 * @return	Returns the langauge name of this driver.
	 */
	abstract public String getLanguageName();
	
	/**
	 * @return	Returns the supported language versions for this driver.
	 */
	abstract public String[] getLanguageVersions();
	
	
	
	// =============== build methods to override
	
	protected X4OLanguage buildLanguage(String version) {
		return X4ODriverManager.getDefaultBuildLanguage(this, version);
	}
	
	protected X4OPhaseManager buildPhaseManager() {
		return X4ODriverManager.getDefaultBuildPhaseManager();
	}
	
	protected X4OLanguageConfiguration buildLanguageConfiguration() {
		return X4ODriverManager.getDefaultBuildLanguageConfiguration();
	}
	
	
	
	// =============== SchemaWriter
	
	/**
	 * Creates a schema writer for the default language version.
	 * @return	The schema writer for this language.
	 */
	public X4OSchemaWriter createSchemaWriter() {
		return createSchemaWriter(getLanguageVersionDefault());
	}
	
	/**
	 * Creates a schema writer for a version of the language.
	 * @param version	The version of the language.
	 * @return	The schema writer for this language.
	 */
	public X4OSchemaWriter createSchemaWriter(String version) {
		return new DefaultX4OSchemaWriter(createLanguageContext(version));
	}
	
	
	
	// =============== Reader
	
	public X4OReader<T> createReader() {
		return createReaderContext();
	}
	
	public X4OReader<T> createReader(String version) {
		return createReaderContext(version);
	}
	
	public X4OReaderContext<T> createReaderContext() {
		return createReaderContext(getLanguageVersionDefault());
	}
	
	public X4OReaderContext<T> createReaderContext(String version) {
		return new DefaultX4OReader<T>(createLanguageContext(version));
	}
	
	
	
	// =============== Writer
	
	public X4OWriter<T> createWriter() {
		return createWriterContext();
	}
	
	public X4OWriter<T> createWriter(String version) {
		return createWriterContext(version);
	}
	
	public X4OWriterContext<T> createWriterContext() {
		return createWriterContext(getLanguageVersionDefault());
	}
	
	public X4OWriterContext<T> createWriterContext(String version) {
		return new DefaultX4OWriter<T>(createLanguageContext(version));
	}
	
	
	
	// =============== Language
	
	/**
	 * Returns the default language which is the latest version.
	 * @return	The default language version.
	 */
	final public String getLanguageVersionDefault() {
		return X4ODriverManager.getDefaultLanguageVersion(getLanguageVersions());
	}	
	
	/**
	 * Creates the X4OLanguage for the specified version.
	 * @param version	The language version to create.
	 * @return	The created X4OLanguage.
	 */
	final public X4OLanguage createLanguage(String version) {
		return buildLanguage(version);
	}
	
	/**
	 * Creates the X4OLanguageContext for the default language version.
	 * @return	The created X4OLanguageContext.
	 */
	final public X4OLanguageContext createLanguageContext() {
		return createLanguageContext(getLanguageVersionDefault());
	}
	
	/**
	 * Creates the X4OLanguageContext for the specified version.
	 * @param version	The language version to create the context for.
	 * @return	The created X4OLanguageContext.
	 */
	final public X4OLanguageContext createLanguageContext(String version) {
		return createLanguage(version).createLanguageContext(this);
	}
}
