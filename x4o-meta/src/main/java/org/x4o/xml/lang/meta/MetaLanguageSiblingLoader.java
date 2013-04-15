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

package org.x4o.xml.lang.meta;

import org.x4o.xml.lang.X4OLanguageModule;
import org.x4o.xml.lang.X4OLanguageModuleLoaderException;
import org.x4o.xml.lang.X4OLanguageModuleLoaderSibling;
import org.x4o.xml.lang.X4OLanguageLoader;
import org.x4o.xml.lang.X4OLanguageLoaderException;
import org.x4o.xml.lang.X4OLanguageLocal;

/**
 * MetaLanguageSiblingLoader loads the generic x4o meta language into defined language. 
 *
 * @author Willem Cazander
 * @version 1.0 Aug 7, 2012
 */
public class MetaLanguageSiblingLoader implements X4OLanguageModuleLoaderSibling {

	/** Defines the identifier of the meta x4o language. */
	public static final String META_LANGUAGE = "meta";
	
	/** Defines the version of the meta x4o language. */
	public static final String META_LANGUAGE_VERSION = "1.0";
	
	/**
	 * Loads an ElementLanguageModule.
	 * @param language	The ElementLanguage to load for.
	 * @param languageModule	The ElementLanguageModule to load into.
	 * @throws X4OLanguageModuleLoaderException Is thrown when meta language could not be loaded.
	 * @see org.x4o.xml.lang.X4OLanguageModuleLoader#loadLanguageModule(org.x4o.xml.lang.X4OLanguageLocal, org.x4o.xml.lang.X4OLanguageModule)
	 */
	public void loadLanguageModule(X4OLanguageLocal language,X4OLanguageModule languageModule) throws X4OLanguageModuleLoaderException {
		languageModule.setId(META_LANGUAGE);
		languageModule.setName(META_LANGUAGE);
		languageModule.setProviderName(MetaLanguageSiblingLoader.class.getSimpleName());
		languageModule.setDescription("X4O Meta Language");
	}

	/**
	 * Loads an sibling language.
	 * @param language	The ElementLanguage to load for.
	 * @param languageLoader	The x4o language loader.
	 * @throws X4OLanguageLoaderException 
	 * @see org.x4o.xml.lang.X4OLanguageModuleLoaderSibling#loadLanguageSibling(org.x4o.xml.lang.X4OLanguageLocal, org.x4o.xml.lang.X4OLanguageLoader)
	 */
	public void loadLanguageSibling(X4OLanguageLocal language,X4OLanguageLoader languageLoader) throws X4OLanguageLoaderException {
	
		// Load the meta language.
		languageLoader.loadLanguage(language, META_LANGUAGE, META_LANGUAGE_VERSION);
	}
}
