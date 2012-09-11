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

package org.x4o.xml.eld;

import java.util.logging.Logger;

import org.x4o.xml.element.ElementLanguage;
import org.x4o.xml.element.ElementLanguageModule;
import org.x4o.xml.element.ElementLanguageModuleLoader;
import org.x4o.xml.element.ElementLanguageModuleLoaderException;

/**
 * De default X4OElementConfigurator.
 * 
 * @author Willem Cazander
 * @version 1.0 Aug 17, 2005
 */
public class EldModuleLoader implements ElementLanguageModuleLoader {

	private Logger logger = null;
	private String eldResource = null;
	private boolean isEldCore = false;
	
	public EldModuleLoader(String eldResource,boolean isEldCore) {
		if (eldResource==null) {
			throw new NullPointerException("Can't load null eld resource.");
		}
		logger = Logger.getLogger(EldModuleLoader.class.getName());
		this.eldResource=eldResource;
		this.isEldCore=isEldCore;
	}

	/**
	 * @see org.x4o.xml.element.ElementLanguageModuleLoader#loadLanguageModule(org.x4o.xml.element.ElementLanguage, org.x4o.xml.element.ElementLanguageModule)
	 */
	public void loadLanguageModule(ElementLanguage elementLanguage,ElementLanguageModule elementLanguageModule) throws ElementLanguageModuleLoaderException {
		logger.fine("Loading name eld file from resource: "+eldResource);
		try {
			EldParser parser = new EldParser(elementLanguage,elementLanguageModule,isEldCore);
			parser.parseResource(eldResource);
		} catch (Exception e) {
			throw new ElementLanguageModuleLoaderException(this,e.getMessage()+" while parsing: "+eldResource,e);
		}
	}
}
