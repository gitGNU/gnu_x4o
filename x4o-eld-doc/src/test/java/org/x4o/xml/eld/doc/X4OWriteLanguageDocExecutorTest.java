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
package org.x4o.xml.eld.doc;

import java.io.File;

import org.x4o.xml.X4ODriver;
import org.x4o.xml.X4ODriverManager;
import org.x4o.xml.eld.CelDriver;
import org.x4o.xml.eld.EldDriver;
import org.x4o.xml.io.sax.ext.PropertyConfig;
import org.x4o.xml.lang.task.X4OLanguageTask;
import org.x4o.xml.test.TestDriver;

import junit.framework.TestCase;

/**
 * Test for eld doc generation 
 * 
 * @author Willem Cazander
 * @version 1.0 Aug 26, 2010
 */
public class X4OWriteLanguageDocExecutorTest extends TestCase {
	
	private File createOutputPath(String dir) throws Exception {
		File result = new File("target/tests"+File.separator+dir);
		if (result.exists()==false) {
			result.mkdirs();
		}
		return result;
	}
	
	public void testDoc(String language,String outputPostfix) throws Exception {
		X4ODriver<?> driver = X4ODriverManager.getX4ODriver(language);
		X4OLanguageTask task = driver.getLanguageTask(EldDocLanguageTask.TASK_ID);
		PropertyConfig config = task.createTaskConfig();
		File outputPath = createOutputPath(outputPostfix);
		config.setProperty(EldDocWriter.OUTPUT_PATH,outputPath);
		task.createTaskExecutor(config).execute(driver.createLanguage());
		assertTrue(outputPath.exists());
		assertTrue(outputPath.list()!=null);
		assertTrue(outputPath.list().length>2);
	}
	
	public void testCelDoc() throws Exception {
		testDoc(CelDriver.LANGUAGE_NAME,"junit-doc-cel");
	}

	public void testEldDoc() throws Exception {
		testDoc(EldDriver.LANGUAGE_NAME,"junit-doc-eld");
	}
	
	public void testUnitDoc() throws Exception {
		testDoc(TestDriver.LANGUAGE_NAME,"junit-doc-test");
	}
/*
	public void testSwiXml2Doc() throws Exception {
		testDoc(EldDriver.LANGUAGE_NAME,"junit-doc-eld");
		EldDocLanguageTask writer = new EldDocLanguageTask();
		writer.setBasePath(createOutputTargetPath("junit-swixml2"));
		writer.setLanguageName(SwiXmlDriver.LANGUAGE_NAME);
		writer.setLanguageVersion(SwiXmlDriver.LANGUAGE_VERSION_2);
		writer.execute();
	}
	
	public void testSwiXml3Doc() throws Exception {
		testDoc(EldDriver.LANGUAGE_NAME,"junit-doc-eld");
		EldDocLanguageTask writer = new EldDocLanguageTask();
		writer.setBasePath(createOutputTargetPath("junit-swixml3"));
		writer.setLanguageName(SwiXmlDriver.LANGUAGE_NAME);
		writer.setLanguageVersion(SwiXmlDriver.LANGUAGE_VERSION_3);
		writer.execute();
	}
	*/
}
