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
package org.x4o.plugin.maven;

import java.io.File;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;

import org.x4o.xml.eld.xsd.X4OWriteLanguageSchemaExecutor;
import org.x4o.xml.element.ElementException;

/**
 * X4OWriteLanguageSchemaMojo creates schema for language.
 * 
 * @author Willem Cazander
 * @version 1.0 Apr 10, 2013
 */
@Mojo( name = X4OWriteLanguageSchemaMojo.GOAL,requiresProject=true,requiresDependencyResolution=ResolutionScope.COMPILE)
public class X4OWriteLanguageSchemaMojo extends AbstractX4OLanguageMojo {
	
	static public final String GOAL = "write-language-schema"; 
	
	String getLanguageTaskDirectoryLabel() {
		return "xsd";
	}
	
	String getLanguageTaskName() {
		return "X4O Write language schema";
	}
	
	void executeLanguageTask(String languageName,String languageVersion,File basePath) throws MojoExecutionException {
		X4OWriteLanguageSchemaExecutor writer = new X4OWriteLanguageSchemaExecutor();
		writer.setBasePath(basePath);
		writer.setLanguageName(languageName);
		writer.setLanguageVersion(languageVersion);
		try {
			writer.execute();
		} catch (ElementException e) {
			throw new MojoExecutionException(e.getMessage(),e);
		}
	}
}
