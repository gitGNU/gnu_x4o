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

package org.x4o.xml.meta.test;

import org.x4o.xml.X4ODriver;
import org.x4o.xml.X4ODriverManager;
import org.x4o.xml.core.config.DefaultX4OLanguage;
import org.x4o.xml.core.config.DefaultX4OLanguageConfiguration;
import org.x4o.xml.core.config.X4OLanguage;
import org.x4o.xml.core.phase.X4OPhaseManagerFactory;
import org.x4o.xml.io.X4OReaderContext;
import org.x4o.xml.io.X4OWriterContext;

public class MTestDriver extends X4ODriver {

	public static final String LANGUAGE_NAME = "mtest";
	public static final String[] LANGUAGE_VERSIONS = new String[]{X4ODriver.DEFAULT_LANGUAGE_VERSION};
	
	@Override
	public String getLanguageName() {
		return LANGUAGE_NAME;
	}
	
	@Override
	public String[] getLanguageVersions() {
		return LANGUAGE_VERSIONS;
	}
	
	@Override
	public X4OLanguage buildLanguage(String version) {
		return new DefaultX4OLanguage(new DefaultX4OLanguageConfiguration(),X4OPhaseManagerFactory.createDefaultX4OPhaseManager(),getLanguageName(),getLanguageVersionDefault());
	}
	
	static public MTestDriver getInstance() {
		return (MTestDriver)X4ODriverManager.getX4ODriver(LANGUAGE_NAME);
	}
	
	public X4OReaderContext<?> createReaderContext() {
		return (X4OReaderContext<?>)super.createReader();
	}
	
	public X4OWriterContext<?> createWriterContext() {
		return (X4OWriterContext<?>)super.createWriter();
	}
}
