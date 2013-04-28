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

import java.io.IOException;
import java.util.logging.Logger;

import org.x4o.xml.X4ODriver;
import org.x4o.xml.X4ODriverManager;
import org.x4o.xml.io.DefaultX4OReader;
import org.x4o.xml.io.X4OConnectionException;
import org.x4o.xml.io.X4OReader;
import org.x4o.xml.lang.X4OLanguageModule;
import org.x4o.xml.lang.X4OLanguageContext;
import org.x4o.xml.lang.X4OLanguageModuleLoader;
import org.x4o.xml.lang.X4OLanguageModuleLoaderException;
import org.x4o.xml.lang.X4OLanguageLocal;
import org.xml.sax.SAXException;

/**
 * De default X4OElementConfigurator.
 * 
 * @author Willem Cazander
 * @version 1.0 Aug 17, 2005
 */
public class EldModuleLoader implements X4OLanguageModuleLoader {

	private Logger logger = null;
	private String eldResource = null;
	private boolean isEldCore = false;
	
	/** The EL key to access the parent language configuration. */
	public static final String EL_PARENT_LANGUAGE_CONFIGURATION = "parentLanguageConfiguration";
	
	/** The EL key to access the parent language module. */
	public static final String EL_PARENT_ELEMENT_LANGUAGE_MODULE = "parentElementLanguageModule";
	
	/** The EL key to access the parent language element langauge. */
	public static final String EL_PARENT_LANGUAGE = "parentLanguage";
	
	/**
	 * Creates an ELD/CEL module loader. 
	 * @param eldResource	The resource to load.
	 * @param isEldCore		If true then load CEL else load ELD.
	 */
	public EldModuleLoader(String eldResource,boolean isEldCore) {
		if (eldResource==null) {
			throw new NullPointerException("Can't load null eld resource.");
		}
		logger = Logger.getLogger(EldModuleLoader.class.getName());
		this.eldResource=eldResource;
		this.isEldCore=isEldCore;
	}

	/**
	 * Loads the ELD language into the module.
	 * @param language The langauge to load for.
	 * @param elementLanguageModule The module to load it in.
	 * @throws X4OLanguageModuleLoaderException When eld language could not be loaded.
	 * @see org.x4o.xml.lang.X4OLanguageModuleLoader#loadLanguageModule(org.x4o.xml.lang.X4OLanguageLocal, org.x4o.xml.lang.X4OLanguageModule)
	 */
	public void loadLanguageModule(X4OLanguageLocal language,X4OLanguageModule elementLanguageModule) throws X4OLanguageModuleLoaderException {
		logger.fine("Loading name eld file from resource: "+eldResource);
		try {
			//EldDriver parser = new EldDriver(elementLanguage,elementLanguageModule,isEldCore);
			
			X4ODriver<?> driver = null;
			if (isEldCore) {
				driver = X4ODriverManager.getX4ODriver(CelDriver.LANGUAGE_NAME);
			} else {
				driver = X4ODriverManager.getX4ODriver(EldDriver.LANGUAGE_NAME);
			}
			
			X4OLanguageContext eldLang = driver.createLanguageContext(driver.getLanguageVersionDefault()); 
			X4OReader<?> reader = new DefaultX4OReader<Object>(eldLang);
			
			reader.addELBeanInstance(EL_PARENT_LANGUAGE_CONFIGURATION, language.getLanguageConfiguration());
			reader.addELBeanInstance(EL_PARENT_LANGUAGE, language);
			reader.addELBeanInstance(EL_PARENT_ELEMENT_LANGUAGE_MODULE, elementLanguageModule);
			
//TODO:			if (language.getLanguageConfiguration().getLanguagePropertyBoolean(X4OLanguageProperty.DEBUG_OUTPUT_ELD_PARSER)) {
//				eldLang.setX4ODebugWriter(elementLanguage.getLanguageConfiguration().getX4ODebugWriter());
//			}
			
			reader.readResource(eldResource);
		} catch (X4OConnectionException e) {
			throw new X4OLanguageModuleLoaderException(this,e.getMessage()+" while parsing: "+eldResource,e);
		} catch (SAXException e) {
			throw new X4OLanguageModuleLoaderException(this,e.getMessage()+" while parsing: "+eldResource,e);
		} catch (IOException e) {
			throw new X4OLanguageModuleLoaderException(this,e.getMessage()+" while parsing: "+eldResource,e);
		}
	}
}
