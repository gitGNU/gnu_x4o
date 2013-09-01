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
package org.x4o.xml.eld.doc;

import org.x4o.xml.element.ElementException;
import org.x4o.xml.io.sax.ext.PropertyConfig;
import org.x4o.xml.lang.X4OLanguage;
import org.x4o.xml.lang.task.AbstractX4OLanguageTask;
import org.x4o.xml.lang.task.X4OLanguageTaskException;
import org.x4o.xml.lang.task.X4OLanguageTaskExecutor;

/**
 * X4OWriteLanguageDoc is support class to write html documentation from the eld.
 * 
 * @author Willem Cazander
 * @version 1.0 Aug 22, 2012
 */
public class EldDocLanguageTask extends AbstractX4OLanguageTask {

	public  static final String TASK_ID   = "eld-doc";
	private static final String TASK_NAME = "ELD DOC Writer Task";
	private static final String TASK_DESC = "Writes out the documentation of the language elements.";
	
	public EldDocLanguageTask() {
		super(TASK_ID,TASK_NAME,TASK_DESC,EldDocWriter.DEFAULT_PROPERTY_CONFIG);
	}
	
	/**
	 * Executes this language task.
	 */
	protected X4OLanguageTaskExecutor createTaskExecutorChecked(final PropertyConfig config) {
		return new X4OLanguageTaskExecutor() {
			public void execute(X4OLanguage language) throws X4OLanguageTaskException {
				try {
					new EldDocWriter(language,config).writeDocumentation();
				} catch (ElementException e) {
					throw new X4OLanguageTaskException(config,e.getMessage(),e);
				}
			}
		};
	}
}
