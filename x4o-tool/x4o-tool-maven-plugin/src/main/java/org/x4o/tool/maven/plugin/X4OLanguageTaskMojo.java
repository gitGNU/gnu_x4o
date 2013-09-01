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
package org.x4o.tool.maven.plugin;

import java.util.ArrayList;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.x4o.xml.lang.task.X4OLanguageTaskException;
import org.x4o.xml.lang.task.run.X4OTaskProperty;
import org.x4o.xml.lang.task.run.X4OTaskRunner;

/**
 * X4OLanguageTaskMojo can execute a task on a language.
 * 
 * @author Willem Cazander
 * @version 1.0 Apr 9, 2013
 */
@Mojo( name = X4OLanguageTaskMojo.GOAL,requiresProject=true,requiresDependencyResolution=ResolutionScope.COMPILE)
public class X4OLanguageTaskMojo extends AbstractMojo {
	
	static public final String GOAL = "x4o-language-task"; 
	
	@Parameter(property="languageName")
	private String languageName = null;
	
	@Parameter(property="languageVersion")
	private String languageVersion = null;
	
	@Parameter(property="taskId")
	private String taskId = null;
	
	@Parameter(property="taskPropertyValues")
	private List<String> taskPropertyValues;
	
	@Parameter(defaultValue="false",property="verbose")
	private boolean verbose = false;
	
	@Parameter(defaultValue="true",property="failOnError")
	private boolean failOnError = true;
	
	private void executeLanguageTask() throws MojoExecutionException {
		if (taskPropertyValues==null) {
			taskPropertyValues = new ArrayList<String>(10);
		}
		if (verbose) {
			getLog().info("Verbose: "+verbose);
			getLog().info("Fail on error: "+failOnError);
		}
		long startTime = System.currentTimeMillis();
		if (verbose) {
			getLog().info("Starting "+getTaskId()+" for "+getLanguageName()); //+":"+languageVersion
		}
		List<X4OTaskProperty> taskProperties = new ArrayList<X4OTaskProperty>(20);
		for (String taskPropertyLine:taskPropertyValues) {
			taskProperties.add(X4OTaskProperty.parseLine(taskPropertyLine));
		}
		try {
			X4OTaskRunner.runTask(getLanguageName(),getLanguageVersion(), getTaskId(), taskProperties);
		} catch (X4OLanguageTaskException e) {
			throw new MojoExecutionException("Error while running task: "+getTaskId()+" error: "+e.getMessage(),e);
		}
		long stopTime = System.currentTimeMillis();
		String ver = "";
		if (languageVersion!=null) {
			ver = ":"+languageVersion;
		}
		getLog().info("Done "+getTaskId()+" for "+languageName+ver+" in "+(stopTime-startTime)+" ms.");
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
	
	public List<String> getTaskPropertyValues() {
		return taskPropertyValues;
	}
	
	public void addTaskPropertyValue(String taskPropertyLine) {
		taskPropertyValues.add(taskPropertyLine);
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
	
	/**
	 * @return the languageName
	 */
	public String getLanguageName() {
		return languageName;
	}
	
	/**
	 * @param languageName the languageName to set
	 */
	public void setLanguageName(String languageName) {
		this.languageName = languageName;
	}
	
	/**
	 * @return the languageVersion
	 */
	public String getLanguageVersion() {
		return languageVersion;
	}
	
	/**
	 * @param languageVersion the languageVersion to set
	 */
	public void setLanguageVersion(String languageVersion) {
		this.languageVersion = languageVersion;
	}
	/**
	 * @return the taskId
	 */
	public String getTaskId() {
		return taskId;
	}
	/**
	 * @param taskId the taskId to set
	 */
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
}
