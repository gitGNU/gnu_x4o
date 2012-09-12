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

package org.x4o.xml.impl.config;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.x4o.xml.core.config.X4OLanguageClassLoader;
import org.x4o.xml.core.config.X4OLanguageLoader;
import org.x4o.xml.core.config.X4OLanguageLoaderException;
import org.x4o.xml.core.config.X4OLanguageVersionFilter;
import org.x4o.xml.eld.EldParser;
import org.x4o.xml.eld.EldModuleLoader;
import org.x4o.xml.element.ElementLanguage;
import org.x4o.xml.element.ElementException;
import org.x4o.xml.element.ElementLanguageModule;
import org.x4o.xml.element.ElementLanguageModuleLoader;
import org.x4o.xml.element.ElementLanguageModuleLoaderSibling;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.DefaultHandler2;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * Loads the namepsace defintions of language and create needed providers into the context.
 * 
 * @author Willem Cazander
 * @version 1.0 28 Oct 2009
 */
public class DefaultX4OLanguageLoader implements X4OLanguageLoader {

	private Logger logger = null;
	protected List<Map<String,Map<String,String>>> modulesAll = null;
	
	public DefaultX4OLanguageLoader() {
		logger = Logger.getLogger(DefaultX4OLanguageLoader.class.getName());
		modulesAll = new ArrayList<Map<String,Map<String,String>>>(20);
	}
	
	private void logMessage(ElementLanguage elementLanguage,String message) {
		logger.finest(message);
		if (elementLanguage.getLanguageConfiguration().hasX4ODebugWriter()) {
			try {
				elementLanguage.getLanguageConfiguration().getX4ODebugWriter().debugPhaseMessage(message, this.getClass());
			} catch (ElementException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	/**
	 * @see org.x4o.xml.core.config.X4OLanguageLoader#loadLanguage(org.x4o.xml.element.ElementLanguage, java.lang.String, java.lang.String)
	 */
	public void loadLanguage(ElementLanguage elementLanguage, String language,String languageVersion) throws X4OLanguageLoaderException {
		try {
			logger.finer("Loading all modules for language: "+language);
			loadLanguageModules(elementLanguage,language);
			
			X4OLanguageVersionFilter lvf = (X4OLanguageVersionFilter)X4OLanguageClassLoader.newInstance(elementLanguage.getLanguageConfiguration().getDefaultX4OLanguageVersionFilter());
			
			for (Map<String,Map<String,String>> map:modulesAll) {
				List<String> versions = new ArrayList<String>(map.keySet());
				String modulesVersion = lvf.filterVersion(languageVersion, versions);
				if (modulesVersion==null) {
					throw new X4OLanguageLoaderException("No modules config for version: "+languageVersion);
				}
				Map<String,String> modules = map.get(modulesVersion);
				logger.finer("Filtered modules to version: "+modulesVersion);
				if (modules==null) {
					throw new X4OLanguageLoaderException("No modules defined for version: "+modulesVersion);
				}
	
				for (String key:modules.keySet()) {
					String value = modules.get(key);
					ElementLanguageModule module = (ElementLanguageModule)X4OLanguageClassLoader.newInstance(elementLanguage.getLanguageConfiguration().getDefaultElementLanguageModule());
					module.setSourceResource(value);
					
					logMessage(elementLanguage,"Parsing language config key: "+key+" value: "+value);
					
					if ("module-loader".equals(key)) {
						try {
							module.setElementLanguageModuleLoader( (ElementLanguageModuleLoader)X4OLanguageClassLoader.loadClass(value).newInstance() );
						} catch (Exception ee) {
							throw new SAXException("Could not load: "+value+" error: "+ee.getMessage(),ee);
						}
						
					} else if ("eld-resource".equals(key)) {
						String languagePrefix = elementLanguage.getLanguageConfiguration().getLanguageResourcePathPrefix();
						String resource = languagePrefix+"/"+language+"/"+value; 
						if (language.equals(EldParser.ELD_LANGUAGE)) {
							module.setElementLanguageModuleLoader(new EldModuleLoader(resource,true));
						} else {
							module.setElementLanguageModuleLoader(new EldModuleLoader(resource,false));
						}
						module.setSourceResource(resource);
					} else if ("elb-resource".equals(key)) {
						
						// todo
						logger.finer("elb-resources are not done yet.");
						
					} else if ("sibling-loader".equals(key)) {
						try {
							module.setElementLanguageModuleLoader( (ElementLanguageModuleLoaderSibling)X4OLanguageClassLoader.loadClass(value).newInstance() );
						} catch (Exception ee) {
							throw new SAXException("Could not load: "+value+" error: "+ee.getMessage(),ee);
						}
					}
					
					if (module.getElementLanguageModuleLoader()==null) {
						logger.warning("module with null loader: "+module+" tag: "+key+" chars: "+value);
						continue;
					}
					
					// mmm start in order ?
					logMessage(elementLanguage,"Starting modules: "+module+" for language: "+language);
					module.getElementLanguageModuleLoader().loadLanguageModule(elementLanguage, module);
					
					elementLanguage.addElementLanguageModule(module);
				}
			}
		} catch (Exception e1) {
			throw new X4OLanguageLoaderException(e1.getMessage()+" for language: "+language,e1);
		}
	}

	/**
	 * Loads all modules of an language
	 */
	protected void loadLanguageModules(ElementLanguage elementLanguage,String language) throws IOException, SAXException {
		StringBuilder buf = new StringBuilder(150);
		buf.append(elementLanguage.getLanguageConfiguration().getLanguageResourcePathPrefix());
		buf.append('/');
		buf.append(language);
		buf.append('/');
		buf.append(language);
		buf.append(elementLanguage.getLanguageConfiguration().getLanguageResourceModulesFileName());
		
		logger.finer("loading X4O language: "+language);
		Enumeration<URL> e = Thread.currentThread().getContextClassLoader().getResources(buf.toString());
		while(e.hasMoreElements()) {
			URL u = e.nextElement();
			logMessage(elementLanguage,"Loading relative modules: "+u+" for: "+language);
			loadModuleXml(u.openStream());
		}
		
		e = Thread.currentThread().getContextClassLoader().getResources("/"+buf.toString());
		while(e.hasMoreElements()) {
			URL u = e.nextElement();
			logMessage(elementLanguage,"Loading root modules: "+u+" for: "+language);
			loadModuleXml(u.openStream());
		}
	}
	
	/**
	 * Parser xml inputstream to languge modules
	 * @param in	The inputstream to parser.
	 * @throws IOException
	 * @throws SAXException
	 */
	private void loadModuleXml(InputStream in) throws IOException, SAXException {
		if (in==null) {
			throw new NullPointerException("Can't parse null input stream");
		}
		ModulesTagHandler xth = new ModulesTagHandler();
		XMLReader saxParser = XMLReaderFactory.createXMLReader();
		saxParser.setContentHandler(xth);
		saxParser.setProperty("http://xml.org/sax/properties/lexical-handler", xth);
		saxParser.setProperty("http://xml.org/sax/properties/declaration-handler",xth);
		try {
			saxParser.parse(new InputSource(in));
		} finally {
			in.close();
		}
	}

	private class ModulesTagHandler extends DefaultHandler2 {
		private StringBuffer buf = new StringBuffer();
		private String version = null;

		@Override
		public void startDocument() throws SAXException {
			modulesAll.add(new HashMap<String,Map<String,String>>(20));
		}

		@Override
		public void startElement(String namespaceUri, String tag, String qName,Attributes attr) throws SAXException {
			if ("language".equals(tag)) {
				version = attr.getValue("version");
				logger.finest("Version attribute: "+version);
			}
		}

		@Override
		public void endElement(String namespaceUri, String tag,String qName) throws SAXException {
			
			// Get and clear text
			String value = buf.toString();
			buf = new StringBuffer();
			
			// Skip root and language and non versions
			if ("modules".equals(tag)) {
				return;
			}
			if ("language".equals(tag)) {
				return;
			}
			if (version==null) {
				return;
			}
			
			// Store in key map
			Map<String,String> modules = modulesAll.get(modulesAll.size()-1).get(version);
			if (modules==null) {
				modules = new HashMap<String,String>(20);
				modulesAll.get(modulesAll.size()-1).put(version, modules);
			}
			modules.put(tag, value);
			logger.finest("Stored tag: "+tag+" value: "+value);
		}
		
		@Override
		public void characters(char[] ch, int start, int length) throws SAXException {
			String text = new String(ch,start,length).trim();
			if (text.isEmpty()) {
				return;
			}
			buf.append(text);
		}
	}
}
