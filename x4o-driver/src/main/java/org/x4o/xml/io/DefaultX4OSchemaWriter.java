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
package org.x4o.xml.io;

import java.io.File;

import org.x4o.xml.eld.xsd.EldXsdXmlGenerator;
import org.x4o.xml.element.ElementException;
import org.x4o.xml.io.sax.ext.ContentWriterXml;
import org.x4o.xml.io.sax.ext.PropertyConfig;
import org.x4o.xml.io.sax.ext.PropertyConfig.PropertyConfigItem;
import org.x4o.xml.lang.X4OLanguage;

/**
 * DefaultX4OSchemaWriter can write the schema of a x4o language.
 * 
 * @author Willem Cazander
 * @version 1.0 Apr 6, 2013
 */
public class DefaultX4OSchemaWriter extends AbstractX4OConnection implements X4OSchemaWriter {
	
	private PropertyConfig propertyConfig;
	
	private final static String PROPERTY_CONTEXT_PREFIX = PropertyConfig.X4O_PROPERTIES_PREFIX+PropertyConfig.X4O_PROPERTIES_WRITER;
	private final static String PROPERTY_OUTPUT_PATH    = "output/path";
	
	public final static PropertyConfig DEFAULT_PROPERTY_CONFIG;
	public final static String OUTPUT_PATH              = PROPERTY_CONTEXT_PREFIX+PROPERTY_OUTPUT_PATH;	
	
	static {
		DEFAULT_PROPERTY_CONFIG = new PropertyConfig(true,ContentWriterXml.DEFAULT_PROPERTY_CONFIG,PROPERTY_CONTEXT_PREFIX,
				new PropertyConfigItem(PROPERTY_OUTPUT_PATH,File.class)
				);
	}
	
	public DefaultX4OSchemaWriter(X4OLanguage language) {
		super(language);
		propertyConfig = new PropertyConfig(DEFAULT_PROPERTY_CONFIG,PROPERTY_CONTEXT_PREFIX);
	}
	
	@Override
	PropertyConfig getPropertyConfig() {
		return propertyConfig;
	}
	
	/**
	 * @see org.x4o.xml.io.X4OSchemaWriter#writeSchema(java.io.File)
	 */
	public void writeSchema(File basePath) throws ElementException {
		writeSchema(basePath, null);
	}
	
	/**
	 * @see org.x4o.xml.io.X4OSchemaWriter#writeSchema(java.io.File, java.lang.String)
	 */
	public void writeSchema(File basePath, String namespace) throws ElementException {
		setProperty(OUTPUT_PATH, basePath);
		// TODO: fix create context
		EldXsdXmlGenerator xsd = new EldXsdXmlGenerator(getLanguage().createLanguageSession().getLanguage(),getPropertyConfig());
		xsd.writeSchema(namespace);		// Start xsd generator
	}
}
