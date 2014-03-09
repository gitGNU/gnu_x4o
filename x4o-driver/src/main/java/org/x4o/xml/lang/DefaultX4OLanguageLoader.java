/*
 * Copyright (c) 2004-2013, Willem Cazander
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
package org.x4o.xml.lang;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Logger;

import org.x4o.xml.eld.EldDriver;
import org.x4o.xml.eld.EldModuleLoader;
import org.x4o.xml.lang.phase.X4OPhaseLanguageInit;
import org.x4o.xml.lang.phase.X4OPhaseLanguageInit.X4OPhaseInitLanguageSiblings;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.DefaultHandler2;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * DefaultX4OLanguageLoader loads the modules of language.
 * 
 * @author Willem Cazander
 * @version 1.0 28 Oct 2009
 */
public class DefaultX4OLanguageLoader implements X4OLanguageLoader {

	private Logger logger = null;
	
	/**
	 * Creates the DefaultX4OLanguageLoader.
	 */
	public DefaultX4OLanguageLoader() {
		logger = Logger.getLogger(DefaultX4OLanguageLoader.class.getName());
	}
	
	/**
	 * Write log message to debug writer.
	 * @param elementLanguage	The elementLanguage we are loading.
	 * @param message	The message to log to the debug output.
	 */
	private void logMessage(X4OLanguage language,String message) {
		logger.finest(message+" from: "+language.getLanguageName());
		/*
TODO:		if (language.getLanguageConfiguration().hasX4ODebugWriter()) {
			try {
				language.getLanguageConfiguration().getX4ODebugWriter().debugPhaseMessage(message, this.getClass());
			} catch (ElementException e) {
				throw new RuntimeException(e);
			}
		}
		*/
	}
	
	/**
	 * @see org.x4o.xml.lang.X4OLanguageLoader#loadLanguage(org.x4o.xml.lang.X4OLanguageLocal, java.lang.String, java.lang.String)
	 */
	public void loadLanguage(X4OLanguageLocal languageLocal, String language,String languageVersion) throws X4OLanguageLoaderException {
		logger.finer("Loading all modules for language: "+language);
		List<VersionedResources> modulesAll = loadLanguageModules(languageLocal,language);
		modulesAll = filterVersionModules(modulesAll,languageLocal,languageVersion);
		validateModules(modulesAll);
		
		int loaded = 0;
		for (VersionedResources versionedResources:modulesAll) {
			X4OLanguageModuleLoader loader;
			for (String value:versionedResources.eldResources) {
				String languagePrefix = languageLocal.getLanguageConfiguration().getLanguageResourcePathPrefix();
				String resource = languagePrefix+"/"+language+"/"+value;
				if (language.equals(EldDriver.LANGUAGE_NAME)) {
					loader = new EldModuleLoader(resource,true); // load cel
				} else {
					loader = new EldModuleLoader(resource,false); // load eld
				}
				loadModule(languageLocal,loader,value,versionedResources);
			}
			for (String value:versionedResources.moduleLoaders) {
				try {
					loader = (X4OLanguageModuleLoader)X4OLanguageClassLoader.loadClass(value).newInstance();
				} catch (Exception ee) {
					throw new X4OLanguageLoaderException("Could not load class: "+value+" error: "+ee.getMessage(),ee);
				}
				loadModule(languageLocal,loader,value,versionedResources);
			}
			for (String value:versionedResources.siblingLoaders) {
				try {
					loader = (X4OLanguageModuleLoader)X4OLanguageClassLoader.loadClass(value).newInstance();
				} catch (Exception ee) {
					throw new X4OLanguageLoaderException("Could not load class: "+value+" error: "+ee.getMessage(),ee);
				}
				loadModule(languageLocal,loader,value,versionedResources);
				if (loader instanceof X4OLanguageModuleLoaderSibling) {
					// mmm
					X4OPhaseInitLanguageSiblings sibPhase = (X4OPhaseInitLanguageSiblings)languageLocal.getPhaseManager().getPhase(X4OPhaseLanguageInit.INIT_LANG_SIB);
					sibPhase.addLanguageModuleLoaderSibling((X4OLanguageModuleLoaderSibling)loader);
				}
			}
			for (String value:versionedResources.elbResources) {
				// TODO: add elb support
				logger.finer("elb-resources are not done yet; "+value);
			}
			loaded++;
		}
		if (loaded==0) {
			throw new X4OLanguageLoaderException("No modules defined for version: "+languageVersion);
		}
	}
	
	private List<VersionedResources> filterVersionModules(List<VersionedResources> resources,X4OLanguageLocal languageLocal,String languageVersion) throws X4OLanguageLoaderException {
		List<VersionedResources> result = new ArrayList<VersionedResources>(resources.size());
		X4OLanguageVersionFilter lvf;
		try {
			lvf = (X4OLanguageVersionFilter)X4OLanguageClassLoader.newInstance(languageLocal.getLanguageConfiguration().getDefaultLanguageVersionFilter());
		} catch (InstantiationException e) {
			throw new X4OLanguageLoaderException(e);
		} catch (IllegalAccessException e) {
			throw new X4OLanguageLoaderException(e);
		}
		for (VersionedResources versionedResources:resources) {
			List<String> versions = new ArrayList<String>();
			versions.add(versionedResources.version); // FIXME
			String modulesVersion = lvf.filterVersion(languageVersion, versions);
			if (modulesVersion==null) {
				continue;
			}
			result.add(versionedResources);
		}
		return result;
	}
	
	protected void validateModules(List<VersionedResources> resources) throws X4OLanguageLoaderException {
		List<String> eldResources = new ArrayList<String>(5);
		List<String> moduleLoaders = new ArrayList<String>(5);
		List<String> elbResources = new ArrayList<String>(5);
		List<String> siblingLoaders = new ArrayList<String>(5);
		for (VersionedResources vr:resources) {
			validateModuleList(eldResources,vr.eldResources,"eld-resource");
			validateModuleList(moduleLoaders,vr.moduleLoaders,"module-loader");
			validateModuleList(elbResources,vr.elbResources,"elb-resource");
			validateModuleList(siblingLoaders,vr.siblingLoaders,"sibling-loader");
		}
	}
	
	private void validateModuleList(List<String> data,List<String> values,String xmlTag) throws X4OLanguageLoaderException {
		for (String value:values) {
			if (data.contains(value)) {
				throw new X4OLanguageLoaderException("Duplicate "+xmlTag+" entry detected; "+value);
			}
			data.add(value);
		}
	}
	
	private void loadModule(X4OLanguageLocal languageLocal,X4OLanguageModuleLoader loader,String resource,VersionedResources versionedResources) throws X4OLanguageLoaderException {
		X4OLanguageModuleLocal module;
		try {
			module = (X4OLanguageModuleLocal)X4OLanguageClassLoader.newInstance(languageLocal.getLanguageConfiguration().getDefaultElementLanguageModule());
		} catch (InstantiationException e) {
			throw new X4OLanguageLoaderException(e);
		} catch (IllegalAccessException e) {
			throw new X4OLanguageLoaderException(e);
		}
		logMessage(languageLocal,"Created module: "+module);
		long startTime = System.currentTimeMillis();
		try {
			logMessage(languageLocal,"Starting modules: "+module+" for language: "+languageLocal.getLanguageName());
			loader.loadLanguageModule(languageLocal, module);
		} catch (X4OLanguageModuleLoaderException e) {
			throw new X4OLanguageLoaderException(e); // FIXME info 
		}
		long totalTime = System.currentTimeMillis() - startTime;
		module.putLoaderResult(X4OLanguageModuleLoaderResult.LOAD_TIME, ""+totalTime);
		module.putLoaderResult(X4OLanguageModuleLoaderResult.LOAD_VERSION, versionedResources.version);
		module.putLoaderResult(X4OLanguageModuleLoaderResult.LOAD_CLASS, loader.getClass().getName());
		module.putLoaderResult(X4OLanguageModuleLoaderResult.LOAD_DATE, ""+new Date());
		if (resource!=null) {
			module.putLoaderResult(X4OLanguageModuleLoaderResult.LOAD_MODULE_RESOURCE, resource);
		}
		module.putLoaderResult(X4OLanguageModuleLoaderResult.LOAD_FROM_RESOURCE, versionedResources.loadedFrom);
		
		languageLocal.addLanguageModule(module);
	}
	
	/**
	 * Loads all modules of an language.
	 * @param languageLocal	The ElementLanguage to load for.
	 * @param language The language to load.
	 */
	protected List<VersionedResources> loadLanguageModules(X4OLanguageLocal languageLocal,String language) throws X4OLanguageLoaderException {
		List<VersionedResources> result = new ArrayList<VersionedResources>(15);
		StringBuilder buf = new StringBuilder(150);
		buf.append(languageLocal.getLanguageConfiguration().getLanguageResourcePathPrefix());
		buf.append('/');
		buf.append(language);
		buf.append('/');
		buf.append(language);
		buf.append(languageLocal.getLanguageConfiguration().getLanguageResourceModulesFileName());
		
		logger.finer("loading X4O language: "+language);
		try {
			Enumeration<URL> e = Thread.currentThread().getContextClassLoader().getResources(buf.toString());
			while(e.hasMoreElements()) {
				URL u = e.nextElement();
				logMessage(languageLocal,"Loading relative modules: "+u+" for: "+language);
				result.addAll(loadLanguageModulesXml(u.openStream(),u.toString()));
			}
			
			e = Thread.currentThread().getContextClassLoader().getResources("/"+buf.toString());
			while(e.hasMoreElements()) {
				URL u = e.nextElement();
				logMessage(languageLocal,"Loading root modules: "+u+" for: "+language);
				result.addAll(loadLanguageModulesXml(u.openStream(),u.toString()));
			}
			return result;
		} catch (IOException e) {
			throw new X4OLanguageLoaderException(e);
		} catch (SAXException e) {
			throw new X4OLanguageLoaderException(e);
		}
	}
	
	/**
	 * Parser xml inputstream to languge modules.
	 * @param in	The inputstream to parser.
	 * @throws IOException
	 * @throws SAXException
	 */
	protected List<VersionedResources> loadLanguageModulesXml(InputStream in,String loadedFrom) throws IOException, SAXException {
		if (in==null) {
			throw new NullPointerException("Can't parse null input stream");
		}
		ModulesTagHandler xth = new ModulesTagHandler(loadedFrom);
		XMLReader saxParser = XMLReaderFactory.createXMLReader();
		saxParser.setContentHandler(xth);
		saxParser.setProperty("http://xml.org/sax/properties/lexical-handler", xth);
		saxParser.setProperty("http://xml.org/sax/properties/declaration-handler",xth);
		try {
			saxParser.parse(new InputSource(in));
			return xth.getResult();
		} finally {
			in.close();
		}
	}
	
	private class ModulesTagHandler extends DefaultHandler2 {
		private StringBuilder buf = new StringBuilder();
		private String loadedFrom = null;
		private VersionedResources versionedResources = null;
		private List<VersionedResources> result = null;
		
		public ModulesTagHandler(String loadedFrom) {
			this.loadedFrom=loadedFrom;
			this.result = new ArrayList<VersionedResources>(5);
		}
		
		public List<VersionedResources> getResult()
		{
			return result;
		}
		
		@Override
		public void startDocument() throws SAXException {
		}
		
		@Override
		public void startElement(String namespaceUri, String tag, String qName,Attributes attr) throws SAXException {
			if ("language".equals(tag)) {
				String version = attr.getValue("version");
				versionedResources = new VersionedResources();
				versionedResources.version=version;
				versionedResources.loadedFrom=loadedFrom;
				logger.finest("Version attribute: "+version);
			}
		}
		
		@Override
		public void endElement(String namespaceUri, String tag,String qName) throws SAXException {
			
			// Get and clear text
			String value = buf.toString();
			buf = new StringBuilder();
			
			// Skip root and language and non versions
			if ("modules".equals(tag)) {
				return;
			}
			if ("language".equals(tag)) {
				result.add(versionedResources);
				versionedResources = null;
				return;
			}
			if (versionedResources==null) {
				return;
			}
			
			if ("eld-resource".equals(tag)) {
				versionedResources.eldResources.add(value);
			} else if ("module-loader".equals(tag)) {
				versionedResources.moduleLoaders.add(value);
			} else if ("elb-resource".equals(tag)) {
				versionedResources.elbResources.add(value);
			} else if ("sibling-loader".equals(tag)) {
				versionedResources.siblingLoaders.add(value);
			}
			logger.finest("Stored tag: "+tag+" value: "+value);
		}
		
		@Override
		public void characters(char[] ch, int start, int length) throws SAXException {
			String text = new String(ch,start,length).trim();
			if (text.length()==0) {
				return;
			}
			buf.append(text);
		}
	}
	
	protected class VersionedResources {
		public String version;
		public String loadedFrom;
		public List<String> eldResources = new ArrayList<String>(5);
		public List<String> moduleLoaders = new ArrayList<String>(5);
		public List<String> elbResources = new ArrayList<String>(5);
		public List<String> siblingLoaders = new ArrayList<String>(5);
	}
}
