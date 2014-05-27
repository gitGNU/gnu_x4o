/*
 * Copyright (c) 2004-2014, Willem Cazander
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
package org.x4o.xml.lang.task.run;

import java.util.List;

import org.x4o.xml.X4ODriver;
import org.x4o.xml.X4ODriverManager;
import org.x4o.xml.io.sax.ext.PropertyConfig;
import org.x4o.xml.lang.X4OLanguage;
import org.x4o.xml.lang.task.X4OLanguageTask;
import org.x4o.xml.lang.task.X4OLanguageTaskException;
import org.x4o.xml.lang.task.X4OLanguageTaskExecutor;

/**
 * X4OTaskRunner finds all x4o objects and configs and then run the x4o langauge task.
 * 
 * @author Willem Cazander
 * @version 1.0 Aug 30, 2013
 */
public final class X4OTaskRunner {
	
	static public void runTask(String languageName,String languageVersion,String taskId,List<X4OTaskProperty> props) throws X4OLanguageTaskException {
		X4ODriver<?> driver = X4ODriverManager.getX4ODriver(languageName);
		X4OLanguageTask task = driver.getLanguageTask(taskId);
		if (task==null) {
			throw new NullPointerException("Could not find x4o task with id; "+taskId);
		}
		PropertyConfig config = task.createTaskConfig();
		for (X4OTaskProperty prop:props) {
			String key = prop.getKey();
			String value = prop.getValue();
			config.setPropertyParsedValue(key, value);
		}
		X4OLanguageTaskExecutor taskExecutor = task.createTaskExecutor(config);
		X4OLanguage language = null;
		if (languageVersion==null) {
			language = driver.createLanguage();
		} else {
			language = driver.createLanguage(languageVersion);
		}
		taskExecutor.execute(language);
	}
}
