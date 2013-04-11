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
import org.x4o.xml.eld.xsd.X4OWriteLanguageSchemaExecutor;
import org.x4o.xml.element.ElementException;

/**
 * X4OWriteSchemaTask creates schema for language.
 * 
 * @author Willem Cazander
 * @version 1.0 Aug 23, 2012
 */
public class X4OWriteLanguageSchemaTask extends AbstractX4OLanguageTask {

	private String nsuri = null;
	
	/**
	 * @see org.x4o.plugin.ant.AbstractX4OLanguageTask#getLanguageTaskName()
	 */
	@Override
	String getLanguageTaskName() {
		return "X4O Write language schema";
	}

	/**
	 * Config and start schema writer
	 * @see org.x4o.plugin.ant.AbstractX4OLanguageTask#executeLanguageTask(java.io.File)
	 */
	@Override
	void executeLanguageTask(File basePath) throws BuildException {
		if (isVerbose() && getNsuri()!=null) {
			log("Namespace uri: "+getNsuri());
		}
		X4OWriteLanguageSchemaExecutor writer = new X4OWriteLanguageSchemaExecutor();
		writer.setBasePath(basePath);
		writer.setLanguageName(getLanguageName());
		writer.setLanguageVersion(getLanguageVersion());
		writer.setLanguageNamespaceUri(getNsuri()); // null is all namespaces
		try {
			writer.execute();
		} catch (ElementException e) {
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
}
