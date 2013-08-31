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
package org.x4o.tool.ant.plugin;

import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.x4o.xml.lang.task.X4OLanguageTaskException;
import org.x4o.xml.lang.task.run.X4OTaskProperty;
import org.x4o.xml.lang.task.run.X4OTaskRunner;


/**
 * AbstractX4OLanguageTask is base ant x4o language task executor.
 * 
 * @author Willem Cazander
 * @version 1.0 Apr 8, 2013
 */
public class X4OTask extends Task {

	private String taskId = null;
	private String languageName = null;
	private String languageVersion = null;
	private boolean verbose = false;
	private boolean failonerror = true;
	private List<X4OTaskProperty> taskProperties = null;
	
	/**
	 * Constructs this ant x4o task.
	 */
	public X4OTask() {
		taskProperties = new ArrayList<X4OTaskProperty>(15);
	}
	
	/**
	 * Adds the ant child x4oTaskProperty element. 
	 * @param property
	 */
	public void addX4oTaskProperty(X4OTaskProperty property) {
		taskProperties.add(property);
	}
	
	/**
	 * Executes the x4o eld schema task.
	 * @see org.apache.tools.ant.Task#execute()
	 */
	@Override
	public void execute() throws BuildException {
		try {
			if (isVerbose()) {
				log("Task location: "+getLocation());
				log("X4O language name: "+getLanguageName());
				log("X4O language version: "+getLanguageVersion());
				log("Verbose: "+isVerbose());
				log("Fail on error: "+isFailonerror());
			}
			executeLanguageTask();
		} catch (BuildException e) {
			if (isFailonerror()) {
				throw e;
			} else {
				log(e.getMessage(), Project.MSG_WARN);
			}
		}
	}
	
	private void executeLanguageTask() throws BuildException {
		if (getLanguageName()==null) {
			throw new BuildException("languageName attribute is not set.");
		}
		if (getLanguageName().length()==0) {
			throw new BuildException("languageName attribute is empty.");
		}
		if (getLanguageVersion()!=null && getLanguageVersion().length()==0) {
			throw new BuildException("languageVersion attribute is empty.");
		}
		if (getTaskId()==null) {
			throw new BuildException("taskId attribute is not set.");
		}
		if (getTaskId().length()==0) {
			throw new BuildException("taskId attribute is empty.");
		}
		if (isVerbose()) {
			log("Starting "+getTaskId());
		}
		long startTime = System.currentTimeMillis();
		try {
			X4OTaskRunner.runTask(getLanguageName(),getLanguageVersion(), getTaskId(), taskProperties);
		} catch (X4OLanguageTaskException e) {
			throw new BuildException(e);
		}
		long stopTime = System.currentTimeMillis();
		log("Done "+getTaskId()+" in "+(stopTime-startTime)+" ms.");
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
	 * @return the failonerror
	 */
	public boolean isFailonerror() {
		return failonerror;
	}
	
	/**
	 * @param failonerror the failonerror to set
	 */
	public void setFailonerror(boolean failonerror) {
		this.failonerror = failonerror;
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
