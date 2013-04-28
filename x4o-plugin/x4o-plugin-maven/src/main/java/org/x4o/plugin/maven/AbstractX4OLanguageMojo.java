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
import java.util.HashMap;
import java.util.Map;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Parameter;
import org.x4o.xml.X4ODriver;
import org.x4o.xml.X4ODriverManager;

/**
 * AbstractX4OLanguageMojo can perform a task on languages and versions.
 * 
 * @author Willem Cazander
 * @version 1.0 Apr 9, 2013
 */
public abstract class AbstractX4OLanguageMojo extends AbstractMojo {
	
	private final static String DEFAULT_OUTPUT_DIRECTORY = "target/x4o";
	
	@Parameter(property="outputDirectory",defaultValue=DEFAULT_OUTPUT_DIRECTORY)
	private File outputDirectory;
	
	@Parameter
	private Map<String,String> languages;

	@Parameter(property="languages")
	private String languageCommandLineString;
	
	@Parameter(defaultValue="false",property="verbose")
	private boolean verbose = false;
	
	@Parameter(defaultValue="true",property="failOnError")
	private boolean failOnError = true;
	
	abstract String getLanguageTaskDirectoryLabel();
	
	abstract String getLanguageTaskName();
	
	abstract void executeLanguageTask(String languageName,String languageVersion,File outputDirectory) throws MojoExecutionException;
	
	private void executeLanguageTask() throws MojoExecutionException {
		if (languages==null) {
			languages = new HashMap<String,String>(10); // maven does not support setting map on cmd line ?
		}
		if (outputDirectory==null) {
			outputDirectory = new File(DEFAULT_OUTPUT_DIRECTORY);
		}
		if (verbose) {
			getLog().info("Output directory: "+outputDirectory);
			getLog().info("Verbose: "+verbose);
			getLog().info("Fail on error: "+failOnError);
		}
		if (outputDirectory.exists()==false) {
			outputDirectory.mkdirs(); // incl parents
			if (verbose) {
				getLog().info("Created directory: "+outputDirectory);
			}
		}
		if (languageCommandLineString!=null && languageCommandLineString.startsWith("{") && languageCommandLineString.endsWith("}")) {
			languages.clear();
			String langString = languageCommandLineString.substring(1,languageCommandLineString.length()-1);
			String[] lang = langString.split(",");
			for (String l:lang) {
				String[] ll = l.split("=");
				if (ll.length!=2) {
					getLog().warn("Wrong langauge key split: '"+l+"' of languageString: '"+languageCommandLineString+"'");
					continue;
				}
				String langName = ll[0];
				String langVersion = ll[1];
				languages.put(langName,langVersion);
			}
		}
		if (languages.size()==0) {
			if (verbose) {
				getLog().info("Defaulting to all languages in classpath.");
			}
			for (String lang:X4ODriverManager.getX4OLanguages()) {
				languages.put(lang, "ALL");
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
	}
	
	private void executeLanguageTask(String languageName,String languageVersion) throws MojoExecutionException {
		long startTime = System.currentTimeMillis();
		if (verbose) {
			getLog().info("Starting "+getLanguageTaskName()+" for "+languageName+":"+languageVersion);
		}
		StringBuffer buf = new StringBuffer(100);
		buf.append(outputDirectory.getAbsolutePath());
		buf.append(File.separatorChar);
		buf.append(getLanguageTaskDirectoryLabel());
		buf.append("-");
		buf.append(languageName);
		buf.append("-");
		buf.append(languageVersion);
		File outputLanguagPath = new File(buf.toString());
		if (outputLanguagPath.exists()==false) {
			outputLanguagPath.mkdir();
			if (verbose) {
				getLog().info("Created directory: "+outputLanguagPath);
			}
		}
		executeLanguageTask(languageName,languageVersion,outputLanguagPath);
		long stopTime = System.currentTimeMillis();
		getLog().info("Done "+getLanguageTaskName()+" for "+languageName+":"+languageVersion+" in "+(stopTime-startTime)+" ms.");
	}
	
	public void execute() throws MojoExecutionException {
		try {
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
	 * Adds an language with version.
	 * @param languageName the languageName to set
	 * @param languageVersion the languageVersion to set
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
