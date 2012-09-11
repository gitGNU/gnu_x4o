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

package org.x4o.plugin.ant.eld.xsd;

import java.io.File;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.x4o.xml.core.X4OParserSupportException;
import org.x4o.xml.core.config.X4OLanguageClassLoader;
import org.x4o.xml.eld.xsd.X4OLanguageEldXsdWriter;

/**
 * SchemaWriterTask creates schema for language.
 * 
 * @author Willem Cazander
 * @version 1.0 Aug 23, 2012
 */
public class EldXsdWriterTask extends Task {

	private String nsuri = null;
	private String supportclass = null;
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
			executeTask();
		} catch (BuildException e) {
			if (isFailonerror()) {
				throw e;
			} else {
				log(e.getMessage(), Project.MSG_WARN);
			}
		}
	}
	
	private void executeTask() throws BuildException {
		if (getSupportclass()==null) {
			throw new BuildException("supportclass attribute is not set.");
		}
		if (getDestdir()==null) {
			throw new BuildException("destdir attribute is not set.");
		}
		if (isVerbose()) {
			log("Execute task from: "+getLocation());
			log("destdir:"+getDestdir());
			log("supportclass:"+getSupportclass());
			log("nsuri:"+getNsuri());
			log("verbose:"+isVerbose());
			log("failonerror:"+isFailonerror());
		}
		File basePathFile = new File(getDestdir());
		if (basePathFile.exists()==false) {
			throw new BuildException("destdir does not exists: "+basePathFile);
		}
		Class<?> parserSupport = null;
		try {
			parserSupport = X4OLanguageClassLoader.loadClass(getSupportclass());
		} catch (ClassNotFoundException e) {
			throw new BuildException("Could not load class: "+getSupportclass(),e);
		}
		
		// Config and start schema writer
		X4OLanguageEldXsdWriter writer = new X4OLanguageEldXsdWriter();
		writer.setBasePath(basePathFile);
		writer.setLanguageParserSupport(parserSupport);
		writer.setLanguageNamespaceUri(getNsuri());
		try {
			if (isVerbose()) {
				log("Starting writing.");
			}
			long startTime = System.currentTimeMillis();
			writer.execute();
			long stopTime = System.currentTimeMillis();
			log("Done writing schema in "+(stopTime-startTime)+" ms.");
		} catch (X4OParserSupportException e) {
			throw new BuildException(e);
		}
	}

	/**
	 * @return the nsuri
	 */
	public String getNsuri() {
		return nsuri;
	}

	/**
	 * @param nsuri the nsuri to set
	 */
	public void setNsuri(String nsuri) {
		this.nsuri = nsuri;
	}

	/**
	 * @return the supportclass
	 */
	public String getSupportclass() {
		return supportclass;
	}

	/**
	 * @param supportclass the supportclass to set
	 */
	public void setSupportclass(String supportclass) {
		this.supportclass = supportclass;
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
