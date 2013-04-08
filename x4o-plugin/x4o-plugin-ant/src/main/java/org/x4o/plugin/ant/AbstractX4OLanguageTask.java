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

package org.x4o.plugin.ant;

import java.io.File;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;


/**
 * AbstractX4OLanguageTask is base ant x4o language task executor.
 * 
 * @author Willem Cazander
 * @version 1.0 Apr 8, 2013
 */
abstract public class AbstractX4OLanguageTask extends Task {

	private String language = null;
	private String destdir = null;
	private boolean verbose = false;
	private boolean failonerror = true;
	
	/**
	 * Executes the x4o eld schema task.
	 * @see org.apache.tools.ant.Task#execute()
	 */
	@Override
	public void execute() throws BuildException {
		try {
			if (isVerbose()) {
				log("Task Location: "+getLocation());
				log("X4O Language:"+getLanguage());
				log("Destination Dir:"+getDestdir());
				log("Verbose:"+isVerbose());
				log("Fail on error:"+isFailonerror());
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
		if (getLanguage()==null) {
			throw new BuildException("language attribute is not set.");
		}
		if (getDestdir()==null) {
			throw new BuildException("basePath attribute is not set.");
		}
		if (getLanguage().length()==0) {
			throw new BuildException("language attribute is empty.");
		}
		if (getDestdir().length()==0) {
			throw new BuildException("basePath attribute is empty.");
		}
		File basePathFile = new File(getDestdir());
		if (basePathFile.exists()==false) {
			throw new BuildException("destdir does not exists: "+basePathFile);
		}
		if (isVerbose()) {
			log("Starting "+getLanguageTaskName());
		}
		long startTime = System.currentTimeMillis();
		executeLanguageTask(basePathFile);
		long stopTime = System.currentTimeMillis();
		log("Done "+getLanguageTaskName()+" in "+(stopTime-startTime)+" ms.");
	}
	
	abstract String getLanguageTaskName();
	
	abstract void executeLanguageTask(File basePath) throws BuildException;
	
	/**
	 * @return the language
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * @param language the language to set
	 */
	public void setLanguage(String language) {
		this.language = language;
	}

	/**
	 * @return the destdir
	 */
	public String getDestdir() {
		return destdir;
	}

	/**
	 * @param destdir the destdir to set
	 */
	public void setDestdir(String destdir) {
		this.destdir = destdir;
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
}
