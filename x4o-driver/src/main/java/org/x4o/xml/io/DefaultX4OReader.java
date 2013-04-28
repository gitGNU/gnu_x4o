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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;

import org.x4o.xml.io.sax.X4ODebugWriter;
import org.x4o.xml.io.sax.XMLWriter;
import org.x4o.xml.lang.X4OLanguageContext;
import org.x4o.xml.lang.X4OLanguageContextLocal;
import org.x4o.xml.lang.X4OLanguageProperty;
import org.x4o.xml.lang.X4OLanguagePropertyKeys;
import org.x4o.xml.lang.phase.X4OPhaseException;
import org.x4o.xml.lang.phase.X4OPhaseType;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DefaultHandler2;
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
	
	public DefaultX4OReader(X4OLanguageContext elementLanguage) {
		super(elementLanguage);
		logger = Logger.getLogger(DefaultX4OReader.class.getName());
	}
	
	public X4OLanguageContext readContext(InputStream input, String systemId, URL basePath) throws X4OConnectionException, SAXException, IOException {
		setProperty(X4OLanguagePropertyKeys.READER_INPUT_STREAM, input);
		setProperty(X4OLanguagePropertyKeys.READER_INPUT_SYSTEM_ID, systemId);
		setProperty(X4OLanguagePropertyKeys.READER_INPUT_BASE_PATH, basePath);
		read();
		return getLanguageContext();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
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
		Map map = (Map)getProperty(X4OLanguagePropertyKeys.EL_BEAN_INSTANCE_MAP);
		if (map==null) {
			map = new HashMap<String,Object>(20);
			setProperty(X4OLanguagePropertyKeys.EL_BEAN_INSTANCE_MAP, map);
		}
		logger.finer("Adding el bean: "+name+" type: "+bean.getClass());
		map.put(name,bean);
	}
	
	/**
	 * Parses the input stream as a X4O document.
	 */
	protected void read() throws X4OConnectionException,SAXException,IOException {
		X4OLanguageContext languageContext = getLanguageContext();
		if (languageContext.getLanguage()==null) {
			throw new X4OConnectionException("languageContext is broken getLanguage() returns null."); 
		}
		
		// init debugWriter if enabled
		boolean startedDebugWriter = false;
		Object debugOutputHandler = languageContext.getLanguageProperty(X4OLanguageProperty.DEBUG_OUTPUT_HANDLER);
		Object debugOutputStream = languageContext.getLanguageProperty(X4OLanguageProperty.DEBUG_OUTPUT_STREAM);
		if (languageContext.getX4ODebugWriter()==null) {
			DefaultHandler2 xmlDebugWriter = null;
			if (debugOutputHandler instanceof DefaultHandler2) {
				xmlDebugWriter = (DefaultHandler2)debugOutputHandler;
			} else if (debugOutputStream instanceof OutputStream) {
				xmlDebugWriter = new XMLWriter((OutputStream)debugOutputStream);
			}
			if (xmlDebugWriter!=null) {
				xmlDebugWriter.startDocument();
				xmlDebugWriter.startPrefixMapping("debug", X4ODebugWriter.DEBUG_URI);
				X4ODebugWriter debugWriter = new X4ODebugWriter(xmlDebugWriter);
				X4OLanguageContextLocal local = (X4OLanguageContextLocal)languageContext;
				local.setX4ODebugWriter(debugWriter);
				startedDebugWriter = true;
			}
		}
		
		// debug language
		if (languageContext.hasX4ODebugWriter()) {
			AttributesImpl atts = new AttributesImpl();
			atts.addAttribute ("", "language", "", "", languageContext.getLanguage().getLanguageName());
			atts.addAttribute ("", "currentTimeMillis", "", "", System.currentTimeMillis()+"");
			languageContext.getX4ODebugWriter().getDebugWriter().startElement(X4ODebugWriter.DEBUG_URI, "X4ODriver", "", atts);
		}
		
		// start parsing language
		try {
			getLanguageContext().getLanguage().getPhaseManager().runPhases(getLanguageContext(), X4OPhaseType.XML_READ);
		} catch (Exception e) {

			// also debug exceptions
			if (languageContext.hasX4ODebugWriter()) {
				try {
					AttributesImpl atts = new AttributesImpl();
					atts.addAttribute ("", "message", "", "", e.getMessage());
					if (e instanceof X4OPhaseException) {
						atts.addAttribute ("", "phase", "", "", ((X4OPhaseException)e).getX4OPhaseHandler().getId());
					}
					languageContext.getX4ODebugWriter().getDebugWriter().startElement(X4ODebugWriter.DEBUG_URI, "exceptionStackTrace", "", atts);
					StringWriter writer = new StringWriter();
					PrintWriter printer = new PrintWriter(writer);
					printer.append('\n');
					if (e.getCause()==null) {
						e.printStackTrace(printer);
					} else {
						e.getCause().printStackTrace(printer);
					}
					char[] stack = writer.getBuffer().toString().toCharArray();
					languageContext.getX4ODebugWriter().getDebugWriter().characters(stack, 0, stack.length);
					languageContext.getX4ODebugWriter().getDebugWriter().endElement(X4ODebugWriter.DEBUG_URI, "exceptionStackTrace", "");
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
			if (languageContext.hasX4ODebugWriter()) {
				languageContext.getX4ODebugWriter().getDebugWriter().endElement(X4ODebugWriter.DEBUG_URI, "X4ODriver", "");
			}
			if (startedDebugWriter && languageContext.hasX4ODebugWriter()) {
				languageContext.getX4ODebugWriter().getDebugWriter().endPrefixMapping("debug");
				languageContext.getX4ODebugWriter().getDebugWriter().endDocument();
				if (debugOutputStream instanceof OutputStream) {
					OutputStream outputStream = (OutputStream)debugOutputStream;
					outputStream.flush();
					outputStream.close(); // need this here ?
				}
			}
		}
	}

	public void releaseContext(X4OLanguageContext context) throws X4OPhaseException {
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
