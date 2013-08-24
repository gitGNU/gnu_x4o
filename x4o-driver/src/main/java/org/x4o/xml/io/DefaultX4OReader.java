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
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.logging.Logger;

import javax.el.ValueExpression;
import javax.xml.parsers.ParserConfigurationException;

import org.x4o.xml.io.sax.X4OContentParser;
import org.x4o.xml.io.sax.X4ODebugWriter;
import org.x4o.xml.io.sax.ext.ContentWriter;
import org.x4o.xml.io.sax.ext.ContentWriterXml;
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
import org.xml.sax.helpers.AttributesImpl;

/**
 * DefaultX4OReader can read and parse the xml language.
 * 
 * @author Willem Cazander
 * @version 1.0 Aug 9, 2012
 */
public class DefaultX4OReader<T> extends AbstractX4OReader<T> {
	
	/** The logger to log to. */
	private Logger logger = null;
	
	private X4OLanguageSession languageSession = null;
	
	private final PropertyConfig propertyConfig;
	
	private final static String PROPERTY_CONTEXT_PREFIX = PropertyConfig.X4O_PROPERTIES_PREFIX+PropertyConfig.X4O_PROPERTIES_READER;
	private final static String PROPERTY_SAX_ERROR_HANDLER            = "sax/error-handler";
	private final static String PROPERTY_SAX_ENTITY_RESOLVER          = "sax/entity-resolver";
	private final static String PROPERTY_DOC_EMPTY_NAMESPACE_URI      = "doc/empty-namespace-uri";
	private final static String PROPERTY_DOC_BUFFER_SIZE              = "doc/buffer-size";
	private final static String PROPERTY_INPUT_STREAM                 = "input/stream";
	private final static String PROPERTY_INPUT_ENCODING               = "input/encoding";
	private final static String PROPERTY_INPUT_SOURCE                 = "input/source";
	private final static String PROPERTY_INPUT_SYSTEM_ID              = "input/system-id";
	private final static String PROPERTY_INPUT_BASE_PATH              = "input/base-path";
	private final static String PROPERTY_VALIDATION_SCHEMA_AUTO_WRITE = "validation/schema-auto-write";
	private final static String PROPERTY_VALIDATION_SCHEMA_PATH       = "validation/schema-path";
	private final static String PROPERTY_VALIDATION_INPUT_DOC         = "validation/input-doc";
	private final static String PROPERTY_VALIDATION_INPUT_SCHEMA      = "validation/input-schema";
	
	public final static PropertyConfig DEFAULT_PROPERTY_CONFIG;
	public final static String SAX_ERROR_HANDLER            = PROPERTY_CONTEXT_PREFIX+PROPERTY_SAX_ERROR_HANDLER;
	public final static String SAX_ENTITY_RESOLVER          = PROPERTY_CONTEXT_PREFIX+PROPERTY_SAX_ENTITY_RESOLVER;
	public final static String DOC_EMPTY_NAMESPACE_URI      = PROPERTY_CONTEXT_PREFIX+PROPERTY_DOC_EMPTY_NAMESPACE_URI;
	public final static String DOC_BUFFER_SIZE              = PROPERTY_CONTEXT_PREFIX+PROPERTY_DOC_BUFFER_SIZE;
	public final static String INPUT_STREAM                 = PROPERTY_CONTEXT_PREFIX+PROPERTY_INPUT_STREAM;
	public final static String INPUT_ENCODING               = PROPERTY_CONTEXT_PREFIX+PROPERTY_INPUT_ENCODING;
	public final static String INPUT_SOURCE                 = PROPERTY_CONTEXT_PREFIX+PROPERTY_INPUT_SOURCE;
	public final static String INPUT_SYSTEM_ID              = PROPERTY_CONTEXT_PREFIX+PROPERTY_INPUT_SYSTEM_ID;
	public final static String INPUT_BASE_PATH              = PROPERTY_CONTEXT_PREFIX+PROPERTY_INPUT_BASE_PATH;
	public final static String VALIDATION_SCHEMA_AUTO_WRITE = PROPERTY_CONTEXT_PREFIX+PROPERTY_VALIDATION_SCHEMA_AUTO_WRITE;
	public final static String VALIDATION_SCHEMA_PATH       = PROPERTY_CONTEXT_PREFIX+PROPERTY_VALIDATION_SCHEMA_PATH;
	public final static String VALIDATION_INPUT_DOC         = PROPERTY_CONTEXT_PREFIX+PROPERTY_VALIDATION_INPUT_DOC;
	public final static String VALIDATION_INPUT_SCHEMA      = PROPERTY_CONTEXT_PREFIX+PROPERTY_VALIDATION_INPUT_SCHEMA;
	
	static {
		DEFAULT_PROPERTY_CONFIG = new PropertyConfig(true,null,PROPERTY_CONTEXT_PREFIX,
				new PropertyConfigItem(PROPERTY_SAX_ERROR_HANDLER,ErrorHandler.class),
				new PropertyConfigItem(PROPERTY_SAX_ENTITY_RESOLVER,EntityResolver.class),
				new PropertyConfigItem(PROPERTY_DOC_EMPTY_NAMESPACE_URI,String.class),
				new PropertyConfigItem(PROPERTY_DOC_BUFFER_SIZE,Integer.class,4096*2),
				new PropertyConfigItem(PROPERTY_INPUT_STREAM,InputStream.class),
				new PropertyConfigItem(PROPERTY_INPUT_ENCODING,String.class,XMLConstants.XML_DEFAULT_ENCODING),
				new PropertyConfigItem(PROPERTY_INPUT_SOURCE,InputSource.class),
				new PropertyConfigItem(PROPERTY_INPUT_SYSTEM_ID,String.class),
				new PropertyConfigItem(PROPERTY_INPUT_BASE_PATH,URL.class),
				new PropertyConfigItem(PROPERTY_VALIDATION_SCHEMA_AUTO_WRITE,Boolean.class,true),
				new PropertyConfigItem(PROPERTY_VALIDATION_SCHEMA_PATH,File.class),
				new PropertyConfigItem(PROPERTY_VALIDATION_INPUT_DOC,Boolean.class,false),
				new PropertyConfigItem(PROPERTY_VALIDATION_INPUT_SCHEMA,Boolean.class,false)
				);
	}
	
	public DefaultX4OReader(X4OLanguage language) {
		super(language);
		logger = Logger.getLogger(DefaultX4OReader.class.getName());
		languageSession = language.createLanguageSession();
		propertyConfig = new PropertyConfig(DEFAULT_PROPERTY_CONFIG,PROPERTY_CONTEXT_PREFIX);
	}
	
	@Override
	PropertyConfig getPropertyConfig() {
		return propertyConfig;
	}
	
	public X4OLanguageSession readContext(InputStream input, String systemId, URL basePath) throws X4OConnectionException, SAXException, IOException {
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
		if (languageSession.getLanguage()==null) {
			throw new X4OConnectionException("languageSession is broken getLanguage() returns null."); 
		}
		
		
		if (languageSession instanceof X4OLanguageSessionLocal) {
			X4OLanguageSessionLocal ll = (X4OLanguageSessionLocal)languageSession;
			if (phaseStop!=null) {
				ll.setPhaseStop(phaseStop);
			}
			for (String phaseId:phaseSkip) {
				ll.addPhaseSkip(phaseId);
			}
		}
		
		// init debugWriter if enabled
		boolean startedDebugWriter = false;
		Object debugOutputHandler = null; //TODO: getProperty(X4OLanguageProperty.DEBUG_OUTPUT_HANDLER.name());
		Object debugOutputStream = null; //getProperty(X4OLanguageProperty.DEBUG_OUTPUT_STREAM.name());
		if (languageSession.getX4ODebugWriter()==null) {
			ContentWriter xmlDebugWriter = null;
			if (debugOutputHandler instanceof ContentWriter) {
				xmlDebugWriter = (ContentWriter)debugOutputHandler;
			} else if (debugOutputStream instanceof OutputStream) {
				xmlDebugWriter = new ContentWriterXml((OutputStream)debugOutputStream);
			}
			if (xmlDebugWriter!=null) {
				xmlDebugWriter.startDocument();
				xmlDebugWriter.startPrefixMapping("debug", X4ODebugWriter.DEBUG_URI);
				X4ODebugWriter debugWriter = new X4ODebugWriter(xmlDebugWriter);
				X4OLanguageSessionLocal local = (X4OLanguageSessionLocal)languageSession;
				local.setX4ODebugWriter(debugWriter);
				startedDebugWriter = true;
			}
		}
		
		// debug language
		if (languageSession.hasX4ODebugWriter()) {
			AttributesImpl atts = new AttributesImpl();
			atts.addAttribute ("", "language", "", "", languageSession.getLanguage().getLanguageName());
			atts.addAttribute ("", "currentTimeMillis", "", "", System.currentTimeMillis()+"");
			languageSession.getX4ODebugWriter().getContentWriter().startElement(X4ODebugWriter.DEBUG_URI, "X4ODriver", "", atts);
		}
		
		// start parsing language
		try {
			X4OContentParser parser = new X4OContentParser(propertyConfig);
			parser.parse(languageSession);
			
			getLanguage().getPhaseManager().runPhases(languageSession, X4OPhaseType.XML_READ);
		} catch (Exception e) {

			// also debug exceptions
			if (languageSession.hasX4ODebugWriter()) {
				try {
					AttributesImpl atts = new AttributesImpl();
					atts.addAttribute ("", "message", "", "", e.getMessage());
					if (e instanceof X4OPhaseException) {
						atts.addAttribute ("", "phase", "", "", ((X4OPhaseException)e).getX4OPhaseHandler().getId());
					}
					languageSession.getX4ODebugWriter().getContentWriter().startElement(X4ODebugWriter.DEBUG_URI, "exceptionStackTrace", "", atts);
					StringWriter writer = new StringWriter();
					PrintWriter printer = new PrintWriter(writer);
					printer.append('\n');
					if (e.getCause()==null) {
						e.printStackTrace(printer);
					} else {
						e.getCause().printStackTrace(printer);
					}
					char[] stack = writer.getBuffer().toString().toCharArray();
					languageSession.getX4ODebugWriter().getContentWriter().characters(stack, 0, stack.length);
					languageSession.getX4ODebugWriter().getContentWriter().endElement(X4ODebugWriter.DEBUG_URI, "exceptionStackTrace", "");
				} catch (Exception ee) {
					logger.warning(ee.getMessage());
				}
			}
			
			// unwrap exception
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
			if (languageSession.hasX4ODebugWriter()) {
				languageSession.getX4ODebugWriter().getContentWriter().endElement(X4ODebugWriter.DEBUG_URI, "X4ODriver", "");
			}
			if (startedDebugWriter && languageSession.hasX4ODebugWriter()) {
				languageSession.getX4ODebugWriter().getContentWriter().endPrefixMapping("debug");
				languageSession.getX4ODebugWriter().getContentWriter().endDocument();
				if (debugOutputStream instanceof OutputStream) {
					OutputStream outputStream = (OutputStream)debugOutputStream;
					outputStream.flush();
					outputStream.close(); // need this here ?
				}
			}
		}
	}

	public void releaseContext(X4OLanguageSession context) throws X4OPhaseException {
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
