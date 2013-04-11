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
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.x4o.xml.X4ODriver;
import org.x4o.xml.X4ODriverManager;
import org.x4o.xml.element.ElementException;
import org.x4o.xml.lang.X4OLanguageContext;

/**
 * X4OWriteLanguageDoc is support class to write html documentation from the eld.
 * 
 * @author Willem Cazander
 * @version 1.0 Aug 22, 2012
 */
public class X4OWriteLanguageDocExecutor {

	private String languageName = null;
	private String languageVersion = null;
	private File basePath;
	
	static public void main(String argu[]) {
		X4OWriteLanguageDocExecutor languageSchema = new X4OWriteLanguageDocExecutor();
		List<String> arguList = Arrays.asList(argu);
		Iterator<String> arguIterator = arguList.iterator();
		while (arguIterator.hasNext()) {
			String arg = arguIterator.next();
			if ("-path".equals(arg) || "-p".equals(arg)) {
				if (arguIterator.hasNext()==false) {
					System.out.println("No argument for "+arg+" given.");
					System.exit(1);
					return;
				}
				File schemaBasePath = new File(arguIterator.next());
				if (schemaBasePath.exists()==false) {
					System.out.println("path does not exists; "+schemaBasePath);
					System.exit(1);
					return;
				}
				languageSchema.setBasePath(schemaBasePath);
				continue;
			}
			if ("-language".equals(arg) || "-l".equals(arg)) {
				if (arguIterator.hasNext()==false) {
					System.out.println("No argument for "+arg+" given.");
					System.exit(1);
					return;
				}
				String languageName = arguIterator.next();
				languageSchema.setLanguageName(languageName);
				continue;
			}
		}
		try {
			languageSchema.execute();
		} catch (ElementException e) {
			System.out.println("Error while schema writing: "+e.getMessage());
			e.printStackTrace();
			System.exit(1);
			return;
		}
	}

	public void execute() throws ElementException {
		X4ODriver<?> driver = X4ODriverManager.getX4ODriver(getLanguageName());
		X4OLanguageContext context = driver.createLanguageContext(getLanguageVersion());
			
		// Start doc generator
		EldDocGenerator generator = new EldDocGenerator(context);
		generator.writeDoc(getBasePath());
	}
	
	/**
	 * @return the languageVersion
	 */
	public String getLanguageVersion() {
		return languageVersion;
	}

	/**
	 * @param languageVersion the languageVersion to set
	 */
	public void setLanguageVersion(String languageVersion) {
		this.languageVersion = languageVersion;
	}

	/**
	 * @return the languageName
	 */
	public String getLanguageName() {
		return languageName;
	}

	/**
	 * @param languageName the languageName to set
	 */
	public void setLanguageName(String languageName) {
		this.languageName = languageName;
	}

	/**
	 * @return the basePath
	 */
	public File getBasePath() {
		return basePath;
	}

	/**
	 * @param basePath the basePath to set
	 */
	public void setBasePath(File basePath) {
		this.basePath = basePath;
	}
}
