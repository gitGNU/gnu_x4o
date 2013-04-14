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

package org.x4o.plugin.maven;

import java.io.File;

import org.apache.maven.plugin.testing.AbstractMojoTestCase;

/**
 * X4OWriteLanguageSchemaMojoTest.
 * 
 * @author Willem Cazander
 * @version 1.0 Apr 14, 2013
 */
public class X4OWriteLanguageSchemaMojoTest extends AbstractMojoTestCase {
	
	private void executeGoal(String goal,String testFile) throws Exception {
		File pom = getTestFile(testFile);
		assertNotNull(pom);
		assertTrue(pom.exists());
		X4OWriteLanguageSchemaMojo mojo = (X4OWriteLanguageSchemaMojo) lookupMojo(goal,pom);
		assertNotNull(mojo);
		mojo.execute();
	}
	
	public void testConfAllWriteSchema() throws Exception {
		executeGoal(X4OWriteLanguageSchemaMojo.GOAL,"src/test/resources/junit/test-plugin-conf-all.pom");
		File outputDir = new File("target/jtest/test-plugin-conf-all/xsd-eld-1.0");
		assertTrue(outputDir.exists());
		int files = outputDir.listFiles().length;
		assertEquals("Should created more then two files", true, files>2);
	}
	
	
	public void testConfLangWriteSchema() throws Exception {
		executeGoal(X4OWriteLanguageSchemaMojo.GOAL,"src/test/resources/junit/test-plugin-conf-lang.pom");
		File outputDir = new File("target/jtest/test-plugin-conf-lang/xsd-cel-1.0");
		int files = outputDir.listFiles().length;
		assertEquals("Should created more then one file", true, files>1);
	}
	
	public void testConfDefaultsWriteSchema() throws Exception {
		executeGoal(X4OWriteLanguageSchemaMojo.GOAL,"src/test/resources/junit/test-plugin-defaults.pom");
		File outputDir = new File("target/x4o/xsd-cel-1.0");
		int files = outputDir.listFiles().length;
		assertEquals("Should created more then one file", true, files>1);
		outputDir = new File("target/x4o/xsd-eld-1.0");
		files = outputDir.listFiles().length;
		assertEquals("Should created more then two files", true, files>2);
	}
}
