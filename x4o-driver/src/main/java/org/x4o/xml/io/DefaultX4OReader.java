/*
 * Copyright (c) 2004-2014, Willem Cazander
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
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import javax.el.ValueExpression;
import javax.xml.parsers.ParserConfigurationException;

import org.x4o.xml.io.sax.X4OContentParser;
import org.x4o.xml.io.sax.ext.ContentWriter;
import org.x4o.xml.io.sax.ext.PropertyConfig;
import org.x4o.xml.io.sax.ext.PropertyConfig.PropertyConfigItem;
import org.x4o.xml.lang.X4OLanguage;
import org.x4o.xml.lang.X4OLanguageSession;
import org.x4o.xml.lang.X4OLanguageSessionLocal;
import org.x4o.xml.lang.phase.X4OPhaseException;
import org.x4o.xml.lang.phase.X4OPhaseType;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * DefaultX4OReader can read and parse the xml language.
 * 
 * @author Willem Cazander
 * @version 1.0 Aug 9, 2012
 */
public class DefaultX4OReader<T> extends AbstractX4OReader<T> {
	
	private X4OLanguageSession languageSession = null;
	
	private final PropertyConfig propertyConfig;
	
	private final static String PROPERTY_CONTEXT_PREFIX = PropertyConfig.X4O_PROPERTIES_PREFIX+PropertyConfig.X4O_PROPERTIES_READER;
	
	public final static PropertyConfig DEFAULT_PROPERTY_CONFIG;
	public final static String SAX_ERROR_HANDLER            = PROPERTY_CONTEXT_PREFIX + "sax/error-handler";
	public final static String SAX_ENTITY_RESOLVER          = PROPERTY_CONTEXT_PREFIX + "sax/entity-resolver";
	public final static String DOC_EMPTY_NAMESPACE_URI      = PROPERTY_CONTEXT_PREFIX + "doc/empty-namespace-uri";
	public final static String DOC_BUFFER_SIZE              = PROPERTY_CONTEXT_PREFIX + "doc/buffer-size";
	public final static String INPUT_STREAM                 = PROPERTY_CONTEXT_PREFIX + "input/stream";
	public final static String INPUT_ENCODING               = PROPERTY_CONTEXT_PREFIX + "input/encoding";
	public final static String INPUT_SOURCE                 = PROPERTY_CONTEXT_PREFIX + "input/source";
	public final static String INPUT_SYSTEM_ID              = PROPERTY_CONTEXT_PREFIX + "input/system-id";
	public final static String INPUT_BASE_PATH              = PROPERTY_CONTEXT_PREFIX + "input/base-path";
	public final static String VALIDATION_SCHEMA_AUTO_WRITE = PROPERTY_CONTEXT_PREFIX + "validation/schema-auto-write";
	public final static String VALIDATION_SCHEMA_PATH       = PROPERTY_CONTEXT_PREFIX + "validation/schema-path";
	public final static String VALIDATION_INPUT_DOC         = PROPERTY_CONTEXT_PREFIX + "validation/input-doc";
	public final static String VALIDATION_INPUT_SCHEMA      = PROPERTY_CONTEXT_PREFIX + "validation/input-schema";
	public final static String DEBUG_OUTPUT_HANDLER         = PROPERTY_CONTEXT_PREFIX + ABSTRACT_DEBUG_OUTPUT_HANDLER;
	public final static String DEBUG_OUTPUT_STREAM          = PROPERTY_CONTEXT_PREFIX + ABSTRACT_DEBUG_OUTPUT_STREAM;
	public final static String DEBUG_OUTPUT_STREAM_CLOSE    = PROPERTY_CONTEXT_PREFIX + ABSTRACT_DEBUG_OUTPUT_STREAM_CLOSE;
	
	static {
		DEFAULT_PROPERTY_CONFIG = new PropertyConfig(true,null,PROPERTY_CONTEXT_PREFIX,
				new PropertyConfigItem(SAX_ERROR_HANDLER,ErrorHandler.class),
				new PropertyConfigItem(SAX_ENTITY_RESOLVER,EntityResolver.class),
				new PropertyConfigItem(DOC_EMPTY_NAMESPACE_URI,String.class),
				new PropertyConfigItem(DOC_BUFFER_SIZE,Integer.class,4096*2),
				new PropertyConfigItem(INPUT_STREAM,InputStream.class),
				new PropertyConfigItem(INPUT_ENCODING,String.class,XMLConstants.XML_DEFAULT_ENCODING),
				new PropertyConfigItem(INPUT_SOURCE,InputSource.class),
				new PropertyConfigItem(true,INPUT_SYSTEM_ID,String.class),
				new PropertyConfigItem(true,INPUT_BASE_PATH,URL.class),
				new PropertyConfigItem(VALIDATION_SCHEMA_AUTO_WRITE,Boolean.class,true),
				new PropertyConfigItem(VALIDATION_SCHEMA_PATH,File.class),
				new PropertyConfigItem(VALIDATION_INPUT_DOC,Boolean.class,false),
				new PropertyConfigItem(VALIDATION_INPUT_SCHEMA,Boolean.class,false),
				new PropertyConfigItem(DEBUG_OUTPUT_HANDLER,ContentWriter.class),
				new PropertyConfigItem(DEBUG_OUTPUT_STREAM,OutputStream.class),
				new PropertyConfigItem(DEBUG_OUTPUT_STREAM_CLOSE,Boolean.class,true)
				);
	}
	
	public DefaultX4OReader(X4OLanguage language) {
		super(language);
		languageSession = language.createLanguageSession();
		propertyConfig = new PropertyConfig(DEFAULT_PROPERTY_CONFIG,PROPERTY_CONTEXT_PREFIX);
	}
	
	@Override
	PropertyConfig getPropertyConfig() {
		return propertyConfig;
	}
	
	public X4OLanguageSession readSession(InputStream input, String systemId, URL basePath) throws X4OConnectionException, SAXException, IOException {
		setProperty(INPUT_STREAM, input);
		setProperty(INPUT_SYSTEM_ID, systemId);
		setProperty(INPUT_BASE_PATH, basePath);
		read();
		return languageSession;
	}
	
	public void addELBeanInstance(String name,Object bean) {
		if (name==null) {
			throw new NullPointerException("Can't add null name.");
		}
		if (name.length()==0) {
			throw new NullPointerException("Can't add empty name.");
		}
		if (bean==null) {
			throw new NullPointerException("Can't add null bean.");
		}
		ValueExpression ve = languageSession.getExpressionLanguageFactory().createValueExpression(languageSession.getExpressionLanguageContext(),"${"+name+"}", bean.getClass());
		ve.setValue(languageSession.getExpressionLanguageContext(), bean);
	}
	
	/**
	 * Parses the input stream as a X4O document.
	 */
	protected void read() throws X4OConnectionException,SAXException,IOException {
		// Extra check if we have a language
		if (languageSession.getLanguage()==null) {
			throw new X4OConnectionException("languageSession is broken getLanguage() returns null."); 
		}
		
		// Insert stop/skip phase if we allowed to. TODO: move layer ?
		if (languageSession instanceof X4OLanguageSessionLocal) {
			X4OLanguageSessionLocal ll = (X4OLanguageSessionLocal)languageSession;
			if (phaseStop!=null) {
				ll.setPhaseStop(phaseStop);
			}
			for (String phaseId:phaseSkip) {
				ll.addPhaseSkip(phaseId);
			}
		}
		
		// init debug
		debugStart(languageSession, DEBUG_OUTPUT_HANDLER, DEBUG_OUTPUT_STREAM, DEBUG_OUTPUT_STREAM_CLOSE);
		
		try {
			// Run document parsing
			X4OContentParser parser = new X4OContentParser(propertyConfig);
			parser.parse(languageSession);
			
			// Run phases to build object tree
			getLanguage().getPhaseManager().runPhases(languageSession, X4OPhaseType.XML_READ);
		} catch (Exception e) {
			// also debug exceptions
			debugException(languageSession, e);
			
			// unwrap exception 
			// TODO: cleanup exceptions a bit more see X4OConnectionException
			if (e.getCause() instanceof ParserConfigurationException) {
				throw new X4OConnectionException((ParserConfigurationException)e.getCause());
			}
			if (e.getCause() instanceof SAXException) {
				throw (SAXException)e.getCause();
			}
			if (e.getCause() instanceof IOException) {
				throw (IOException)e.getCause();
			}
			if (e.getCause()==null) {
				throw new SAXException(e);
			} else {
				throw new SAXException((Exception)e.getCause());
			}
		} finally {
			debugStop(languageSession);
		}
	}
	
	public void releaseSession(X4OLanguageSession context) throws X4OPhaseException {
		if (context==null) {
			return;
		}
		if (context.getLanguage()==null) {
			return;
		}
		if (context.getLanguage().getPhaseManager()==null) {
			return;
		}
		context.getLanguage().getPhaseManager().doReleasePhaseManual(context);
	}
}
