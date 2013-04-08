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

package org.x4o.xml.eld.xsd;

import java.io.File;

import org.x4o.xml.eld.CelDriver;
import org.x4o.xml.eld.EldDriver;
import org.x4o.xml.eld.xsd.X4OWriteLanguageSchemaExecutor;
import org.x4o.xml.test.swixml.SwiXmlDriver;

import junit.framework.TestCase;

/**
 * Tests some code for eld schema generating
 * 
 * @author Willem Cazander
 * @version 1.0 Auh 16, 2012
 */
public class X4OWriteLanguageSchemaExecutorTest extends TestCase {

	private File getTempPath(String dir) throws Exception {
		File tempFile = File.createTempFile("test-path", ".tmp"); 
		String absolutePath = tempFile.getAbsolutePath();
		String tempPath = absolutePath.substring(0,absolutePath.lastIndexOf(File.separator)+1);
		tempFile.delete();
		File result = new File(tempPath+File.separator+dir);
		if (result.exists()==false) {
			result.mkdir();
		}
		return result;
	}
	
	public void testEldSchema() throws Exception {
		X4OWriteLanguageSchemaExecutor writer = new X4OWriteLanguageSchemaExecutor();
		writer.setBasePath(getTempPath("junit-xsd-eld"));
		writer.setLanguage(EldDriver.LANGUAGE_NAME);
		writer.execute();
	}
	
	public void testEldCoreSchema() throws Exception {
		X4OWriteLanguageSchemaExecutor writer = new X4OWriteLanguageSchemaExecutor();
		writer.setBasePath(getTempPath("junit-xsd-cel"));
		writer.setLanguage(CelDriver.LANGUAGE_NAME);
		writer.execute();
	}
	
	public void testSwiXmlSchema() throws Exception {
		X4OWriteLanguageSchemaExecutor writer = new X4OWriteLanguageSchemaExecutor();
		writer.setBasePath(getTempPath("junit-xsd-swixml2"));
		writer.setLanguage(SwiXmlDriver.LANGUAGE_NAME);
		writer.execute();
	}
	
	public void testEldDocMain() throws Exception {
		X4OWriteLanguageSchemaExecutor.main(new String[] {"-p",getTempPath("junit-xsd-main").getAbsolutePath(),"-l",EldDriver.LANGUAGE_NAME});
	}
}
