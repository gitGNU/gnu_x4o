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
package org.x4o.xml.test.swixml;

import java.awt.Component;

import javax.el.ValueExpression;

import org.x4o.xml.X4ODriver;
import org.x4o.xml.X4ODriverManager;
import org.x4o.xml.lang.X4OLanguageContext;

/**
 * SwiXmlParser works with the SwingEngine to config the UI tree.
 * 
 * @author Willem Cazander
 * @version 1.0 Aug 15, 2012
 */
public class SwiXmlDriver extends X4ODriver<Component> {

	public static final String   LANGUAGE_NAME = "swixml";
	public static final String   LANGUAGE_VERSION_2 = "2.0";
	public static final String   LANGUAGE_VERSION_2_NSURI = "http://swixml.x4o.org/xml/ns/swixml-lang";
	public static final String   LANGUAGE_VERSION_3 = "3.0";
	public static final String[] LANGUAGE_VERSIONS = new String[]{LANGUAGE_VERSION_2,LANGUAGE_VERSION_3};
	public static final String   LANGUAGE_EL_SWING_ENGINE = "swingEngine";
	
	/**
	 * Helper for while parsing to get the SwingEngine.
	 * @param elementLanguage	The elementLanguage to get the swingEngine out.
	 * @return	Returns the SwingEngine for this elementLanguage.
	 */
	static public SwingEngine getSwingEngine(X4OLanguageContext elementLanguage) {
		ValueExpression ee = elementLanguage.getExpressionLanguageFactory().createValueExpression(elementLanguage.getExpressionLanguageContext(),"${"+SwiXmlDriver.LANGUAGE_EL_SWING_ENGINE+"}",Object.class);    	
		SwingEngine se = (SwingEngine)ee.getValue(elementLanguage.getExpressionLanguageContext());
		return se;
	}
	
	static public SwiXmlDriver getInstance() {
		return (SwiXmlDriver)X4ODriverManager.getX4ODriver(LANGUAGE_NAME);
	}
	
	@Override
	public String getLanguageName() {
		return LANGUAGE_NAME;
	}
	
	@Override
	public String[] getLanguageVersions() {
		return LANGUAGE_VERSIONS;
	}
}
