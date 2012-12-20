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

package org.x4o.xml.eld.doc;

import java.io.File;

import org.x4o.xml.eld.EldParserSupport;
import org.x4o.xml.eld.EldParserSupportCore;
import org.x4o.xml.test.TestParserSupport;
import org.x4o.xml.test.swixml.SwiXmlParserSupport2;
import org.x4o.xml.test.swixml.SwiXmlParserSupport3;

import junit.framework.TestCase;

/**
 * Test for eld doc generation 
 * 
 * @author Willem Cazander
 * @version 1.0 Aug 26, 2010
 */
public class EldDocTest extends TestCase {
	
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
	
	public void testCelDoc() throws Exception {
		X4OLanguageEldDocWriter writer = new X4OLanguageEldDocWriter();
		writer.setBasePath(getTempPath("junit-cel"));
		writer.setLanguageParserSupport(EldParserSupportCore.class);
		writer.execute();
	}
	
	public void testEldDoc() throws Exception {
		X4OLanguageEldDocWriter writer = new X4OLanguageEldDocWriter();
		writer.setBasePath(getTempPath("junit-eld"));
		writer.setLanguageParserSupport(EldParserSupport.class);
		writer.execute();
	}
	
	public void testUnitDoc() throws Exception {
		X4OLanguageEldDocWriter writer = new X4OLanguageEldDocWriter();
		writer.setBasePath(getTempPath("junit-test"));
		writer.setLanguageParserSupport(TestParserSupport.class);
		writer.execute();
	}

	public void testSwiXml2Doc() throws Exception {
		X4OLanguageEldDocWriter writer = new X4OLanguageEldDocWriter();
		writer.setBasePath(getTempPath("junit-swixml2"));
		writer.setLanguageParserSupport(SwiXmlParserSupport2.class);
		writer.execute();
	}
	
	public void testSwiXml3Doc() throws Exception {
		X4OLanguageEldDocWriter writer = new X4OLanguageEldDocWriter();
		writer.setBasePath(getTempPath("junit-swixml3"));
		writer.setLanguageParserSupport(SwiXmlParserSupport3.class);
		writer.execute();
	}
	
	
	public void testEldDocMain() throws Exception {
		X4OLanguageEldDocWriter.main(new String[] {"-path",getTempPath("junit-test-main").getAbsolutePath(),"-class",EldParserSupport.class.getName()});
	}
}
