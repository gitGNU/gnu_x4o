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

package	org.x4o.xml.core;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.x4o.xml.core.config.X4OLanguageConfiguration;
import org.x4o.xml.core.config.X4OLanguagePropertyKeys;
import org.x4o.xml.element.ElementLanguage;
import org.x4o.xml.impl.config.DefaultX4OLanguageConfiguration;
import org.x4o.xml.sax.AbstractXMLParser;
import org.xml.sax.SAXException;

/**
 * This is the starting point of the XML X4O parsing.
 * 
 * @author Willem Cazander
 * @version 1.0 Aug 11, 2005
 */
public class X4OParser extends AbstractXMLParser implements X4OParserSupport {
	
	private X4ODriver driver = null;
	
	/**
	 * Creates an X4OParser object. 
	 *
	 * @param language	The X4O language to create this parser for..
	 */
	public X4OParser(String language) {
		this(language,X4ODriver.DEFAULT_LANGUAGE_VERSION);
	}
	
	public X4OParser(String language,String languageVersion) {
		this(new DefaultX4OLanguageConfiguration(language,languageVersion));
	}

	protected X4OParser(X4OLanguageConfiguration config) {
		this(new X4ODriver(config));
	}
	
	protected X4OParser(X4ODriver driver) {
		if (driver==null) {
			throw new NullPointerException("Can't start X4OParser with null X4ODriver.");
		}
		this.driver = driver;
	}
	
	protected X4ODriver getDriver() {
		return driver;
	}

	/**
	 * @see org.x4o.xml.sax.AbstractXMLParser#parse(java.io.InputStream,java.lang.String,java.net.URL)
	 */
	@Override
	public void parse(InputStream input,String systemId,URL basePath) throws ParserConfigurationException,SAXException, IOException {
		driver.setProperty(X4OLanguagePropertyKeys.INPUT_SOURCE_STREAM, input);
		driver.setProperty(X4OLanguagePropertyKeys.INPUT_SOURCE_SYSTEM_ID, systemId);
		driver.setProperty(X4OLanguagePropertyKeys.INPUT_SOURCE_BASE_PATH, basePath);
		driver.parseInput();
	}

	/**
	 * Run a manual release phase to clean the parsing object tree.
	 * 
	 * @throws X4OPhaseException
	 */
	public void doReleasePhaseManual() throws X4OPhaseException {
		driver.doReleasePhaseManual();
	}
	
	/**
	 * Sets an X4O Language property.
	 * @param key	The key of the property to set.
	 * @param value	The vlue of the property to set.
	 */
	public void setProperty(String key,Object value) {
		driver.setProperty(key, value);
	}
	
	/**
	 * Returns the value an X4O Language property.
	 * @param key	The key of the property to get the value for.
	 * @return	Returns null or the value of the property.
	 */
	public Object getProperty(String key) {
		return driver.getProperty(key);
	}
	
	/**
	 * Loads the support ElementLanguage from the driver.
	 * @see org.x4o.xml.core.X4OParserSupport#loadElementLanguageSupport()
	 */
	public ElementLanguage loadElementLanguageSupport() throws X4OParserSupportException {
		return driver.loadElementLanguageSupport();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void addELBeanInstance(String name,Object bean) {
		if (name==null) {
			throw new NullPointerException("Can't add null name.");
		}
		if (name.isEmpty()) {
			throw new NullPointerException("Can't add empty name.");
		}
		if (bean==null) {
			throw new NullPointerException("Can't add null bean.");
		}
		Map map = (Map)getProperty(X4OLanguagePropertyKeys.EL_BEAN_INSTANCE_MAP);
		if (map==null) {
			map = new HashMap<String,Object>(20);
			setProperty(X4OLanguagePropertyKeys.EL_BEAN_INSTANCE_MAP, map);
		}
		map.put(name,bean);
	}
}
