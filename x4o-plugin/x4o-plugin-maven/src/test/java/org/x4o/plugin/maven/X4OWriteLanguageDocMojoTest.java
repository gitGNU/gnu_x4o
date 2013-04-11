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
 * X4OWriteLanguageDocMojoTest.
 * 
 * @author Willem Cazander
 * @version 1.0 Apr 6, 2013
 */
public class X4OWriteLanguageDocMojoTest extends AbstractMojoTestCase {

	/** {@inheritDoc} */
	protected void setUp() throws Exception {
		super.setUp(); // required
	}

	/** {@inheritDoc} */
	protected void tearDown() throws Exception {
		super.tearDown(); // required
	}
	
	private void executeGoal() throws Exception {
		File pom = getTestFile( "src/test/resources/junit/test-write-language-doc.pom" );
		assertNotNull( pom );
		assertTrue( pom.exists() );

		X4OWriteLanguageDocMojo mojo = (X4OWriteLanguageDocMojo) lookupMojo( X4OWriteLanguageDocMojo.GOAL, pom );
		//mojo.s
		assertNotNull( mojo );
		mojo.execute();
	}
	
	public void testEldDocCel() throws Exception {
		executeGoal(); //"test-elddoc-cel"
		File testDir = new File("target/test-elddoc/cel");
		//int files = testDir.listFiles().length;
		//assertEquals("Should created more then two files", true, files>2);
	}
	
	/*
	public void testEldDocEld() {
		executeTarget("test-elddoc-eld");
		File testDir = new File("target/test-elddoc/eld");
		int files = testDir.listFiles().length;
		assertEquals("Should created more then two files", true, files>2);
	}
	
	public void testEldDocEldVerbose() {
		executeTarget("test-elddoc-cel-verbose");
		assertLogContaining("Verbose:");
	}

	public void testFailAllMissing() {
		expectBuildException("test-fail-all", "Should get exception with no attributes.");
	}
	public void testFailBasePath() {
		expectBuildException("test-fail-destdir", "Should get exception id destdir is not set.");
	}
	public void testFailBasePathError() {
		expectBuildException("test-fail-destdir-error", "Should get exception id destdir does not exists.");
	}
	public void testFailLanguage() {
		expectBuildException("test-fail-language", "Should get exception id language is not set.");
	}
	public void testFailLanguageError() {
		expectBuildException("test-fail-language-error", "Should get exception id language throws error.");
	}
	*/
}