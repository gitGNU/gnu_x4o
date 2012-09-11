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

package org.x4o.xml.test.swixml;

import java.awt.Component;

import javax.el.ValueExpression;

import org.x4o.xml.core.X4OParser;
import org.x4o.xml.core.config.X4OLanguagePropertyKeys;
import org.x4o.xml.element.ElementLanguage;

/**
 * SwiXmlParser works with the SwingEngine to config the UI tree.
 * 
 * @author Willem Cazander
 * @version 1.0 Aug 15, 2012
 */
public class SwiXmlParser extends X4OParser {

	public static final String LANGUAGE = "swixml";
	public static final String VERSION_2_NS_URI = "http://swixml.x4o.org/xml/ns/swixml-lang";
	public static final String SWING_ENGINE_EL_NAME = "swingEngine";
	
	/**
	 * Protected constructor for write schema api.
	 * @param xmlVersion	The xml version.
	 */
	protected SwiXmlParser(SwiXmlVersion xmlVersion) {
		
		// Create language by version  
		super(LANGUAGE,xmlVersion.getLanguageVersion());
		
		// Sets empty namespace uri for version 2 xml documents 
		if (SwiXmlVersion.VERSION_2.equals(xmlVersion)) {
			setProperty(X4OLanguagePropertyKeys.INPUT_EMPTY_NAMESPACE_URI, VERSION_2_NS_URI);
		}
	}
	
	/**
	 * Creates an SwiXmlParser with an SwingEngine and a xml version.
	 * @param engine		The SwingEngine to parse for.
	 * @param xmlVersion	The xml language version to parse.
	 */
	public SwiXmlParser(SwingEngine engine,SwiXmlVersion xmlVersion) {
		
		// Create language
		this(xmlVersion);
		
		// Check engine
		if (engine==null) {
			throw new NullPointerException("Can't parse with null SwingEngine.");
		}
		
		// Add engine for callback
		addELBeanInstance(SWING_ENGINE_EL_NAME,engine);
	}
	
	/**
	 * Returns after parsing the root component.
	 * @return	Returns the root component.
	 */
	public Component getRootComponent() {
		return (Component)getDriver().getElementLanguage().getRootElement().getElementObject();
	}
	
	/**
	 * Helper for while parsing to get the SwingEngine.
	 * @param elementLanguage	The elementLanguage to get the swingEngine out.
	 * @return	Returns the SwingEngine for this elementLanguage.
	 */
	static public SwingEngine getSwingEngine(ElementLanguage elementLanguage) {
		ValueExpression ee = elementLanguage.getExpressionFactory().createValueExpression(elementLanguage.getELContext(),"${"+SwiXmlParser.SWING_ENGINE_EL_NAME+"}",Object.class);    	
		SwingEngine se = (SwingEngine)ee.getValue(elementLanguage.getELContext());
		return se;
	}
	
	/**
	 * SwiXmlVersion defines the xml language version to parse.
	 */
	public enum SwiXmlVersion {
		VERSION_2("2.0"),
		VERSION_3("3.0");
		
		final private String languageVersion;
		private SwiXmlVersion(String languageVersion) {
			this.languageVersion=languageVersion;
		}
		public String getLanguageVersion() {
			return languageVersion;
		}
	}
}
