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
package org.x4o.xml;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.x4o.xml.lang.DefaultX4OLanguage;
import org.x4o.xml.lang.DefaultX4OLanguageConfiguration;
import org.x4o.xml.lang.X4OLanguage;
import org.x4o.xml.lang.X4OLanguageClassLoader;
import org.x4o.xml.lang.X4OLanguageConfiguration;
import org.x4o.xml.lang.phase.DefaultX4OPhaseManager;
import org.x4o.xml.lang.phase.X4OPhaseLanguageInit;
import org.x4o.xml.lang.phase.X4OPhaseLanguageRead;
import org.x4o.xml.lang.phase.X4OPhaseLanguageWrite;
import org.x4o.xml.lang.phase.X4OPhaseManager;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.DefaultHandler2;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * X4ODriverManager controls all the x4o driver and languages loaded in the classpath.
 * 
 * @author Willem Cazander
 * @version 1.0 Apr 6, 2013
 */
public final class X4ODriverManager {

	public final static String X4O_DRIVERS_RESOURCE = "META-INF/x4o-drivers.xml";
	private final static X4ODriverManager instance;
	private Logger logger = null;
	private volatile boolean reloadDrivers = true;
	private Map<String,String> classdrivers = null;
	private Map<String,String> defaultDrivers = null;
	private Map<String,X4ODriver<?>> drivers = null;
	
	private X4ODriverManager() {
		logger = Logger.getLogger(X4ODriverManager.class.getName());
		classdrivers = new HashMap<String,String>(10);
		defaultDrivers = new HashMap<String,String>(10);
		drivers = new HashMap<String,X4ODriver<?>>(10);
	}
	
	static {
		instance = new X4ODriverManager();
	}
	
	static protected String getDefaultLanguageVersion(String[] languages) {
		if (languages==null || languages.length==0) {
			return X4ODriver.DEFAULT_LANGUAGE_VERSION;
		}
		String languageVersion = languages[languages.length-1];
		return languageVersion;
	}
	
	static protected X4OPhaseManager getDefaultBuildPhaseManager() {
		DefaultX4OPhaseManager manager = new DefaultX4OPhaseManager();
		new X4OPhaseLanguageInit().createPhases(manager);
		new X4OPhaseLanguageRead().createPhases(manager);
		new X4OPhaseLanguageWrite().createPhases(manager);
		return manager;
	}
	
	static protected X4OLanguage getDefaultBuildLanguage(X4ODriver<?> driver,String version) {
		if (version==null) {
			version = driver.getLanguageVersionDefault();
		}
		return new DefaultX4OLanguage(
				driver.buildLanguageConfiguration(),
				driver.buildPhaseManager(),
				driver.getLanguageName(),
				version
			);
	}
	
	static protected X4OLanguageConfiguration getDefaultBuildLanguageConfiguration() {
		DefaultX4OLanguageConfiguration config = new DefaultX4OLanguageConfiguration();
		config.fillDefaults();
		X4OLanguageConfiguration result = config.createProxy();
		return result;
	}
	
	/*
	static public void setGlobalProperty(X4ODriver<?> driver,String key,Object value) {
		Map<String,Object> driverProperties = instance.globalProperties.get(driver.getLanguageName());
		if (driverProperties==null) {
			driverProperties = new HashMap<String,Object>(20);
			instance.globalProperties.put(driver.getLanguageName(), driverProperties);
		}
		String keyLimits[] = driver.getGlobalPropertyKeySet();
		for (int i=0;i<keyLimits.length;i++) {
			String keyLimit = keyLimits[i];
			if (keyLimit.equals(key)) {
				driverProperties.put(key,value);
				return;
			}
		}
		throw new IllegalArgumentException("Property with key: "+key+" is protected by key limit.");
	}
	*/
	
	static public void registerX4ODriver(X4ODriver<?> driver) {
		if (driver==null) {
			throw new NullPointerException("Can't register null driver.");
		}
		if (driver.getLanguageName()==null) {
			throw new NullPointerException("Error in driver impl languageName is null in: "+driver.getClass());
		}
		if (driver.getLanguageName().length()==0) {
			throw new IllegalArgumentException("Error in driver impl languageName is empty in: "+driver.getClass());
		}
		if (driver.getLanguageVersions()==null) {
			throw new NullPointerException("Error in driver impl languageVersions is null in: "+driver.getClass());
		}
		if (driver.getLanguageVersions().length==0) {
			throw new IllegalArgumentException("Error in driver impl languageVersions is empty in: "+driver.getClass());
		}
		instance.drivers.put(driver.getLanguageName(), driver);
	}
	
	static public void deregisterX4ODriver(X4ODriver<?> driver) {
		if (driver==null) {
			throw new NullPointerException("Can't deregister null driver.");
		}
		if (driver.getLanguageName()==null) {
			throw new NullPointerException("Error in driver impl languageName is null in: "+driver.getClass());
		}
		instance.drivers.remove(driver.getLanguageName());
	}
	
	static public X4ODriver<?> getX4ODriver(String language) {
		if (language==null) {
			throw new NullPointerException("Can't provider driver for null language.");
		}
		if (language.length()==0) {
			throw new IllegalArgumentException("Can't provider driver for empty language.");
		}
		if (instance.drivers.containsKey(language)) {
			return instance.drivers.get(language);
		}
		instance.lazyInit();
		X4ODriver<?> result = instance.createX4ODriver(language);
		if (result==null) {
			throw new IllegalArgumentException("Can't find driver for language: "+language);
		}
		return result;
	}
	
	static public List<String> getX4OLanguages() {
		instance.lazyInit();
		List<String> result = new ArrayList<String>(10);
		result.addAll(instance.classdrivers.keySet());
		result.addAll(instance.defaultDrivers.keySet());
		Collections.sort(result);
		return result;
	}
	
	private void lazyInit() {
		if (reloadDrivers==false) {
			return;
		}
		instance.loadLanguageDrivers();
		reloadDrivers = false;
	}
	
	private X4ODriver<?> createX4ODriver(String language) {
		String driverClassName = null;
		if (classdrivers.containsKey(language)) {
			driverClassName = classdrivers.get(language);
		} else if (defaultDrivers.containsKey(language)) {
			driverClassName = defaultDrivers.get(language);
		}
		if (driverClassName==null) {
			return null;
		}
		try {
			Class<?> driverClass = X4OLanguageClassLoader.loadClass(driverClassName);
			X4ODriver<?> driver = (X4ODriver<?>)driverClass.newInstance();
			registerX4ODriver(driver);
			return driver;
		} catch (Exception e) {
			throw new IllegalStateException(e.getMessage(),e);
		}
	}
	
	/**
	 * Loads all defined language drivers in classpath.
	 */
	private void loadLanguageDrivers() {
		logger.finer("loading x4o drivers from: "+X4O_DRIVERS_RESOURCE);
		try {
			Enumeration<URL> e = Thread.currentThread().getContextClassLoader().getResources(X4O_DRIVERS_RESOURCE);
			while(e.hasMoreElements()) {
				URL u = e.nextElement();
				loadDriversXml(u.openStream());
			}
			e = Thread.currentThread().getContextClassLoader().getResources("/"+X4O_DRIVERS_RESOURCE);
			while(e.hasMoreElements()) {
				URL u = e.nextElement();
				loadDriversXml(u.openStream());
			}
		} catch (Exception e) {
			throw new IllegalStateException(e.getMessage(),e);
		}
	}
	
	/**
	 * Parser xml inputstream and add into drivers and defaultDrivers lists.
	 * @param in	The inputstream to parser.
	 * @throws IOException
	 * @throws SAXException
	 */
	private void loadDriversXml(InputStream in) throws IOException, SAXException {
		if (in==null) {
			throw new NullPointerException("Can't parse null input stream");
		}
		DriversTagHandler xth = new DriversTagHandler();
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

	private class DriversTagHandler extends DefaultHandler2 {
		@Override
		public void startElement(String namespaceUri, String tag, String qName,Attributes attr) throws SAXException {
			if ("drivers".equals(tag)) {
				String version = attr.getValue("version");
				logger.finest("Version attribute: "+version);
			} else if ("driver".equals(tag)) {
				String language = attr.getValue("language");
				String className = attr.getValue("className");
				logger.finest("Driver className: "+className+" for language: "+language);
				if (classdrivers.containsKey(className)==false) {
					classdrivers.put(language,className);
				}
			} else if ("defaultDriver".equals(tag)) {
				String language = attr.getValue("language");
				logger.finest("DefaultDriver language: "+language);
				if (defaultDrivers.containsKey(language)==false) {
					defaultDrivers.put(language,language);
				}
			}
		}
	}
}
