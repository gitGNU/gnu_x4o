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
package org.x4o.xml.eld.xsd;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.x4o.xml.element.ElementClass;
import org.x4o.xml.element.ElementNamespace;
import org.x4o.xml.io.sax.ext.ContentWriterXml;
import org.x4o.xml.io.sax.ext.ContentWriterXsd;
import org.x4o.xml.io.sax.ext.PropertyConfig;
import org.x4o.xml.io.sax.ext.PropertyConfig.PropertyConfigItem;
import org.x4o.xml.lang.X4OLanguageModule;
import org.x4o.xml.lang.X4OLanguage;
import org.xml.sax.SAXException;

/**
 * EldSchemaGenerator Creates XML Schema for a namespace uri from a x4o language driver. 
 * 
 * @author Willem Cazander
 * @version 1.0 Aug 8, 2012
 */
public class EldXsdWriter {
	
	private final static String PROPERTY_CONTEXT_PREFIX = PropertyConfig.X4O_PROPERTIES_PREFIX+PropertyConfig.X4O_PROPERTIES_ELD_XSD;
	public final static PropertyConfig DEFAULT_PROPERTY_CONFIG;
	
	public final static String OUTPUT_PATH              = PROPERTY_CONTEXT_PREFIX+"output/path";
	public final static String OUTPUT_DOCUMENTATION     = PROPERTY_CONTEXT_PREFIX+"output/documentation";
	public final static String FILTER_NAMESPACE         = PROPERTY_CONTEXT_PREFIX+"filter/namespace";
	public final static String FILTER_ELEMENT           = PROPERTY_CONTEXT_PREFIX+"filter/element";
	public final static String PROLOG_LICENCE_FILE      = PROPERTY_CONTEXT_PREFIX+"prolog/licence-file";
	public final static String PROLOG_LICENCE_RESOURCE  = PROPERTY_CONTEXT_PREFIX+"prolog/licence-resource";
	public final static String PROLOG_LICENCE_ENCODING  = PROPERTY_CONTEXT_PREFIX+"prolog/licence-encoding";
	public final static String PROLOG_PRINT_LICENCE     = PROPERTY_CONTEXT_PREFIX+"prolog/print-licence";
	public final static String PROLOG_PRINT_GENERATOR   = PROPERTY_CONTEXT_PREFIX+"prolog/print-generator";
	public final static String PROLOG_PRINT_PROVIDER    = PROPERTY_CONTEXT_PREFIX+"prolog/print-provider";
	public final static String PROLOG_USER_COMMENT      = PROPERTY_CONTEXT_PREFIX+"prolog/user-comment";
	
	static {
		DEFAULT_PROPERTY_CONFIG = new PropertyConfig(true,ContentWriterXml.DEFAULT_PROPERTY_CONFIG,PROPERTY_CONTEXT_PREFIX,
				new PropertyConfigItem(true,OUTPUT_PATH,File.class),
				new PropertyConfigItem(OUTPUT_DOCUMENTATION,Boolean.class,true),
				new PropertyConfigItem(false,FILTER_NAMESPACE,String.class),
				new PropertyConfigItem(false,FILTER_ELEMENT,String.class),
				new PropertyConfigItem(false,PROLOG_LICENCE_ENCODING,String.class),
				new PropertyConfigItem(false,PROLOG_LICENCE_FILE,File.class),
				new PropertyConfigItem(false,PROLOG_LICENCE_RESOURCE,String.class),
				new PropertyConfigItem(PROLOG_PRINT_LICENCE,Boolean.class,true),
				new PropertyConfigItem(PROLOG_PRINT_GENERATOR,Boolean.class,true),
				new PropertyConfigItem(PROLOG_PRINT_PROVIDER,Boolean.class,true),
				new PropertyConfigItem(false,PROLOG_USER_COMMENT,String.class)
				);
	}
	
	private final X4OLanguage language;
	private final PropertyConfig propertyConfig;
	
	public EldXsdWriter(X4OLanguage language,PropertyConfig parentConfig) {
		this.language=language;
		this.propertyConfig=new PropertyConfig(DEFAULT_PROPERTY_CONFIG,PROPERTY_CONTEXT_PREFIX);
		this.propertyConfig.copyParentProperties(parentConfig);
	}
	
	private void checkNamespace(ElementNamespace ns) {
		if (ns.getSchemaResource()==null) {
			throw new NullPointerException("Can't generate xsd for namespace without schemaResource uri: "+ns.getUri());
		}
		if (ns.getSchemaResource().length()==0) {
			throw new NullPointerException("Can't generate xsd for namespace with empty schemaResource uri: "+ns.getUri());
		}
	}
	
	public void writeSchema() throws SAXException, IOException {
		File basePath = propertyConfig.getPropertyFile(OUTPUT_PATH);
		String encoding = propertyConfig.getPropertyString(ContentWriterXml.OUTPUT_ENCODING);
		String namespace = propertyConfig.getPropertyString(FILTER_NAMESPACE);
		if (basePath==null) {
			throw new NullPointerException("Can't write schema to null output path.");
		}
		if (!basePath.exists()) {
			basePath.mkdirs();
		}
		if (namespace!=null) {
			ElementNamespace ns = language.findElementNamespace(namespace);
			if (ns==null) {
				throw new NullPointerException("Could not find namespace: "+namespace);
			}
			checkNamespace(ns);
			File outputFile = new File(basePath.getAbsolutePath()+File.separatorChar+ns.getSchemaResource());
			Writer wr = new OutputStreamWriter(new FileOutputStream(outputFile), encoding);
			try {
				ContentWriterXsd xsdWriter = new ContentWriterXsd(wr,encoding);
				xsdWriter.getPropertyConfig().copyParentProperties(propertyConfig);
				generateSchema(ns.getUri(), xsdWriter);
			} finally {
				wr.close();
			}	
			return;
		}
		for (X4OLanguageModule mod:language.getLanguageModules()) {
			for (ElementNamespace ns:mod.getElementNamespaces()) {
				checkNamespace(ns);
				File outputFile = new File(basePath.getAbsolutePath()+File.separatorChar+ns.getSchemaResource());
				Writer wr = new OutputStreamWriter(new FileOutputStream(outputFile), encoding);
				try {
					ContentWriterXsd xsdWriter = new ContentWriterXsd(wr,encoding);
					xsdWriter.getPropertyConfig().copyParentProperties(propertyConfig);
					generateSchema(ns.getUri(), xsdWriter);
				} finally {
					wr.close();
				}
			}
		}
	}
	
	private void generateSchema(String namespaceUri,ContentWriterXsd xsdWriter) throws SAXException {
		ElementNamespace ns = language.findElementNamespace(namespaceUri);
		if (ns==null) {
			throw new NullPointerException("Could not find namespace: "+namespaceUri);
		}
		
		EldXsdWriterElement xsdWriterElement = new EldXsdWriterElement(xsdWriter,language,propertyConfig);
		xsdWriterElement.startNamespaces(namespaceUri);
		xsdWriterElement.startSchema(ns);
		for (ElementClass ec:ns.getElementClasses()) {
			xsdWriterElement.writeElementClass(ec,ns);
		}
		for (ElementClass ec:ns.getElementClasses()) {
			xsdWriterElement.writeElement(ec,ns);
		}
		xsdWriterElement.endSchema();
	}
}
