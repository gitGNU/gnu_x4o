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

package org.x4o.plugin.maven;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Parameter;
import org.x4o.xml.X4ODriver;
import org.x4o.xml.X4ODriverManager;

/**
 * X4OWriteLanguageDocMojo creates docs for language.
 * 
 * @author Willem Cazander
 * @version 1.0 Apr 9, 2013
 */
public abstract class AbstractX4OLanguageMojo extends AbstractMojo {
	
	@Parameter(property="outputDirectory")
	private File outputDirectory;
	
	@Parameter(required=true,property="languages")
	private Map<String,String> languages;

	@Parameter(defaultValue="false",property="verbose")
	private boolean verbose = false;
	
	@Parameter(defaultValue="true",property="failOnError")
	private boolean failOnError = true;
	
	abstract String getLanguageTaskName();
	
	abstract void executeLanguageTask(String languageName,String languageVersion,File outputDirectory) throws MojoExecutionException;
	
	private void executeLanguageTask() throws MojoExecutionException {
		if (outputDirectory==null) {
			throw new MojoExecutionException("outputDirectory attribute is not set.");
		}
		if (languages==null) {
			throw new MojoExecutionException("languages attribute is not set.");
		}
		if (languages.size()==0) {
			throw new MojoExecutionException("languages attribute is empty.");
		}
		long startTime = System.currentTimeMillis();
		if (verbose) {
			getLog().info("Starting "+getLanguageTaskName());
		}
		if (outputDirectory.exists()==false) {
			outputDirectory.mkdir();
			if (verbose) {
				getLog().info("Created directory: "+outputDirectory);
			}
		}
		for (String languageName:languages.keySet()) {
			String languageVersions = languages.get(languageName);
			if (languageVersions.contains("*") || languageVersions.contains("ALL")) {
				X4ODriver<?> driver = X4ODriverManager.getX4ODriver(languageName);
				if (driver==null) {
					throw new MojoExecutionException("Couln't load x4o language driver for: "+languageName);
				}
				for (String supportedVersion:driver.getLanguageVersions()) {
					executeLanguageTask(languageName,supportedVersion);
				}
			} else if (languageVersions.contains("-")) {
				for (String languageVersion:languageVersions.split("-")) {
					executeLanguageTask(languageName,languageVersion);
				}
			} else {
				executeLanguageTask(languageName,languageVersions); // only one version
			}
		}
		long stopTime = System.currentTimeMillis();
		getLog().info("Done "+getLanguageTaskName()+" in "+(stopTime-startTime)+" ms.");
	}
	
	private void executeLanguageTask(String languageName,String languageVersion) throws MojoExecutionException {
		File outputLanguagPath = new File(outputDirectory.getAbsolutePath()+File.separatorChar+languageName+"-"+languageVersion);
		if (outputLanguagPath.exists()==false) {
			outputLanguagPath.mkdir();
			if (verbose) {
				getLog().info("Created directory: "+outputLanguagPath);
			}
		}
		executeLanguageTask(languageName,languageVersion,outputLanguagPath);
	}
	
	public void execute() throws MojoExecutionException {
		try {
			if (verbose) {
				if (languages!=null) {
					getLog().info("X4O Languages: "+languages.size());
				}
				getLog().info("Output directory: "+outputDirectory);
				getLog().info("Verbose: "+verbose);
				getLog().info("Fail on error: "+failOnError);
			}
			executeLanguageTask();
		} catch (MojoExecutionException e) {
			if (failOnError) {
				throw e;
			} else {
				getLog().warn(e.getMessage());
			}
		}
	}

	/**
	 * @return the outputDirectory
	 */
	public File getOutputDirectory() {
		return outputDirectory;
	}

	/**
	 * @param outputDirectory the outputDirectory to set
	 */
	public void setOutputDirectory(File outputDirectory) {
		this.outputDirectory = outputDirectory;
	}

	/**
	 * @return the languages
	 */
	public Map<String, String> getLanguages() {
		return languages;
	}

	/**
	 * @param languages the languages to set
	 */
	public void addLanguage(String languageName,String languageVersion) {
		languages.put(languageName,languageVersion);
	}

	/**
	 * @return the verbose
	 */
	public boolean isVerbose() {
		return verbose;
	}

	/**
	 * @param verbose the verbose to set
	 */
	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	/**
	 * @return the failOnError
	 */
	public boolean isFailOnError() {
		return failOnError;
	}

	/**
	 * @param failOnError the failOnError to set
	 */
	public void setFailOnError(boolean failOnError) {
		this.failOnError = failOnError;
	}
}
