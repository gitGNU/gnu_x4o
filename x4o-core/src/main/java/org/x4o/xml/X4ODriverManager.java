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

package org.x4o.xml;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.x4o.xml.lang.X4OLanguageClassLoader;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.DefaultHandler2;
import org.xml.sax.helpers.XMLReaderFactory;

public final class X4ODriverManager {

	public final static String X4O_DRIVERS_RESOURCE = "META-INF/x4o-drivers.xml";
	private final static X4ODriverManager instance;
	private Logger logger = null;
	private volatile boolean reloadDrivers = true;
	private Map<String,String> classdrivers = null;
	private Map<String,String> defaultDrivers = null;
	private Map<String,X4ODriver<?>> drivers = null;
	private Map<String,Map<String,Object>> globalProperties = null;
	
	private X4ODriverManager() {
		logger = Logger.getLogger(X4ODriverManager.class.getName());
		classdrivers = new HashMap<String,String>(10);
		defaultDrivers = new HashMap<String,String>(10);
		drivers = new HashMap<String,X4ODriver<?>>(10);
		globalProperties = new HashMap<String,Map<String,Object>>(20);
	}
	
	static {
		instance = new X4ODriverManager();
	}
	
	static public Collection<String> getGlobalPropertyKeys(X4ODriver<?> driver) {
		Map<String,Object> driverProperties = instance.globalProperties.get(driver.getLanguageName());
		if (driverProperties==null) {
			return Collections.emptySet();
		}
		return driverProperties.keySet();
	}
	
	static public Object getGlobalProperty(X4ODriver<?> driver,String key) {
		Map<String,Object> driverProperties = instance.globalProperties.get(driver.getLanguageName());
		if (driverProperties==null) {
			return null;
		}
		return driverProperties.get(key);
	}
	
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
	
	static public void registerX4ODriver(X4ODriver<?> driver) {
		instance.drivers.put(driver.getLanguageName(), driver);
	}
	
	static public void deregisterX4ODriver(X4ODriver<?> driver) {
		instance.drivers.remove(driver.getLanguageName());
	}
	
	static public X4ODriver<?> getX4ODriver(String language) {
		if (language==null) {
			throw new NullPointerException("Can't provider driver for null language.");
		}
		if (language.isEmpty()) {
			throw new IllegalArgumentException("Can't provider driver for empty language.");
		}
		try {
			instance.lazyInit();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		X4ODriver<?> result = null;
		try {
			result = instance.createX4ODriver(language);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (result==null) {
			throw new IllegalArgumentException("Can't find driver for language: "+language);
		}
		return result;
	}
	
	static public List<String> getX4OLanguages() {
		try {
			instance.lazyInit();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<String> result = new ArrayList<String>(10);
		result.addAll(instance.classdrivers.keySet());
		result.addAll(instance.defaultDrivers.keySet());
		Collections.sort(result);
		return result;
	}
	
	private void lazyInit() throws IOException, SAXException {
		if (reloadDrivers==false) {
			return;
		}
		instance.loadLanguageDrivers();
		reloadDrivers = false;
	}
	
	private X4ODriver<?> createX4ODriver(String language) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		if (classdrivers.containsKey(language)) {
			String driverClassName = classdrivers.get(language);
			Class<?> driverClass = X4OLanguageClassLoader.loadClass(driverClassName);
			X4ODriver<?> driver = (X4ODriver<?>)driverClass.newInstance();
			return driver;
		}
		if (defaultDrivers.containsKey(language)) {
			String driverClassName = defaultDrivers.get(language);
			Class<?> driverClass = X4OLanguageClassLoader.loadClass(driverClassName);
			X4ODriver<?> driver = (X4ODriver<?>)driverClass.newInstance();
			return driver;
		}
		return null;
	}
	
	/**
	 * Loads all defined language drivers in classpath.
	 */
	private void loadLanguageDrivers() throws IOException, SAXException {

		logger.finer("loading x4o drivers from: "+X4O_DRIVERS_RESOURCE);
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
			} else if ("defaultDriver".equals("tab")) {
				String language = attr.getValue("language");
				logger.finest("DefaultDriver language: "+language);
				if (defaultDrivers.containsKey(language)==false) {
					defaultDrivers.put(language,language);
				}
			}
		}
	}
}
