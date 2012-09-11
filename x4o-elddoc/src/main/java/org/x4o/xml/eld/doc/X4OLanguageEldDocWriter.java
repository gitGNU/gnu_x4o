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

import org.x4o.xml.core.X4OParserSupport;
import org.x4o.xml.core.X4OParserSupportException;
import org.x4o.xml.core.config.X4OLanguageClassLoader;
import org.x4o.xml.element.ElementLanguage;
import org.x4o.xml.element.ElementException;

/**
 * X4OLanguageHtmlWriter is support class to write html documentation from the eld.
 * 
 * @author Willem Cazander
 * @version 1.0 Aug 22, 2012
 */
public class X4OLanguageEldDocWriter {

	private Class<?> languageParserSupport = null;
	private File basePath;
	
	@SuppressWarnings("unchecked")
	static public void main(String argu[]) {
		X4OLanguageEldDocWriter languageSchema = new X4OLanguageEldDocWriter();
		List<String> arguList = Arrays.asList(argu);
		Iterator<String> arguIterator = arguList.iterator();
		while (arguIterator.hasNext()) {
			String arg = arguIterator.next();
			if ("-path".equals(arg)) {
				if (arguIterator.hasNext()==false) {
					System.out.println("No argument for -path given.");
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
			if ("-class".equals(arg)) {
				if (arguIterator.hasNext()==false) {
					System.out.println("No argument for -class given.");
					System.exit(1);
					return;
				}
				String apiClass = arguIterator.next();
				try {
					languageSchema.setLanguageParserSupport((Class<X4OParserSupport>) X4OLanguageClassLoader.loadClass(apiClass));
				} catch (ClassNotFoundException e) {
					System.out.println("Schema api class is not found: "+apiClass);
					System.exit(1);
					return;
				}
				continue;
			}
		}
		try {
			languageSchema.execute();
		} catch (X4OParserSupportException e) {
			System.out.println("Error while schema writing: "+e.getMessage());
			e.printStackTrace();
			System.exit(1);
			return;
		}
	}

	public void execute() throws X4OParserSupportException {
		try {
			// Load the support language context
			X4OParserSupport languageSupport = (X4OParserSupport)getLanguageParserSupport().newInstance();
			ElementLanguage context = languageSupport.loadElementLanguageSupport();
			
			// Start doc generator
			EldDocGenerator generator = new EldDocGenerator(context);
			generator.writeDoc(getBasePath());

		} catch (InstantiationException e) {
			throw new X4OParserSupportException(e);
		} catch (IllegalAccessException e) {
			throw new X4OParserSupportException(e);
		} catch (ElementException e) {
			throw new X4OParserSupportException(e);
		}
	}
	
	/**
	 * @return the languageParserSupport
	 */
	public Class<?> getLanguageParserSupport() {
		return languageParserSupport;
	}

	/**
	 * @param languageParserSupport the languageParserSupport to set
	 */
	public void setLanguageParserSupport(Class<?> languageParserSupport) {
		this.languageParserSupport = languageParserSupport;
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
