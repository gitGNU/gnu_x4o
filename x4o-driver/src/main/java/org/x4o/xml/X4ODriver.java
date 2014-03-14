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
package org.x4o.xml;

import java.util.List;

import org.x4o.xml.io.DefaultX4OReader;
import org.x4o.xml.io.DefaultX4OWriter;
import org.x4o.xml.io.X4OReader;
import org.x4o.xml.io.X4OReaderSession;
import org.x4o.xml.io.X4OWriter;
import org.x4o.xml.io.X4OWriterSession;
import org.x4o.xml.lang.X4OLanguageConfiguration;
import org.x4o.xml.lang.X4OLanguage;
import org.x4o.xml.lang.phase.X4OPhaseManager;
import org.x4o.xml.lang.task.X4OLanguageTask;

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
	 * marker constructor.
	 */
	public X4ODriver(/*X4ODriverManager.ConstructorMarker marker*/) {
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
	
	
	
	// =============== Reader
	
	public X4OReader<T> createReader() {
		return createReaderSession();
	}
	
	public X4OReader<T> createReader(String version) {
		return createReaderSession(version);
	}
	
	public X4OReaderSession<T> createReaderSession() {
		return createReaderSession(getLanguageVersionDefault());
	}
	
	public X4OReaderSession<T> createReaderSession(String version) {
		return new DefaultX4OReader<T>(createLanguage(version));
	}
	
	
	
	// =============== Writer
	
	public X4OWriter<T> createWriter() {
		return createWriterSession();
	}
	
	public X4OWriter<T> createWriter(String version) {
		return createWriterSession(version);
	}
	
	public X4OWriterSession<T> createWriterSession() {
		return createWriterSession(getLanguageVersionDefault());
	}
	
	public X4OWriterSession<T> createWriterSession(String version) {
		return new DefaultX4OWriter<T>(createLanguage(version));
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
	 * Creates the X4OLanguage for the default version.
	 * @return	The created X4OLanguage.
	 */
	final public X4OLanguage createLanguage() {
		return buildLanguage(getLanguageVersionDefault());
	}
	
	/*
	 * Creates the X4OLanguageSession for the default language version.
	 * @return	The created X4OLanguageSession.
	 *
	final public X4OLanguageSession createLanguageSession() {
		return createLanguageSession(getLanguageVersionDefault());
	}
	
	*
	 * Creates the X4OLanguageSession for the specified version.
	 * @param version	The language version to create the context for.
	 * @return	The created X4OLanguageSession.
	 *
	final public X4OLanguageSession createLanguageSession(String version) {
		return createLanguage(version).createLanguageSession();
	}*/
	
	
	// =============== Language Tasks

	final public X4OLanguageTask getLanguageTask(String taskId) {
		return X4ODriverManager.getX4OLanguageTask(taskId);
	}
	
	final public List<X4OLanguageTask> getLanguageTasks() {
		return X4ODriverManager.getX4OLanguageTasks();
	}
}
