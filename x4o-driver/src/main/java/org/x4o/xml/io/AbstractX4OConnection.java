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
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.x4o.xml.io.sax.X4ODebugWriter;
import org.x4o.xml.io.sax.ext.ContentWriter;
import org.x4o.xml.io.sax.ext.ContentWriterXml;
import org.x4o.xml.io.sax.ext.PropertyConfig;
import org.x4o.xml.lang.X4OLanguage;
import org.x4o.xml.lang.X4OLanguageSession;
import org.x4o.xml.lang.X4OLanguageSessionLocal;
import org.x4o.xml.lang.phase.X4OPhase;
import org.x4o.xml.lang.phase.X4OPhaseException;
import org.x4o.xml.lang.phase.X4OPhaseLanguageRead;
import org.x4o.xml.lang.phase.X4OPhaseLanguageWrite;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * AbstractX4OConnection is the read/write interface for the classes.
 * 
 * @author Willem Cazander
 * @version 1.0 Apr 6, 2013
 */
public abstract class AbstractX4OConnection implements X4OConnection {
	
	private final X4OLanguage language;
	private boolean debugStarted = false;
	private OutputStream debugCloseOutputStream = null;
	protected String phaseStop = null;
	protected List<String> phaseSkip = null;
	protected final static String ABSTRACT_DEBUG_OUTPUT_HANDLER = "debug/output-handler";
	protected final static String ABSTRACT_DEBUG_OUTPUT_STREAM  = "debug/output-stream";
	
	public AbstractX4OConnection(X4OLanguage language) {
		this.language=language;
		this.phaseSkip = new ArrayList<String>(2);
	}
	
	protected X4OLanguage getLanguage() {
		return language;
	}
	
	abstract PropertyConfig getPropertyConfig();
	
	/**
	 * Sets an X4O Language property.
	 * @param key	The key of the property to set.
	 * @param value	The vlue of the property to set.
	 */
	public void setProperty(String key,Object value) {
		getPropertyConfig().setProperty(key, value);
	}
	
	public Object getProperty(String key) {
		return getPropertyConfig().getProperty(key);
	}
	
	public Collection<String> getPropertyKeys() {
		return getPropertyConfig().getPropertyKeys();
	}
	
	public void setPhaseStop(String phaseId) {
		phaseStop = phaseId;
	}
	
	public void addPhaseSkip(String phaseId) {
		phaseSkip.add( phaseId );
	}
	
	protected void debugStart(X4OLanguageSession languageSession,String debugHandlerKey,String debugStreamKey) throws UnsupportedEncodingException, SAXException {
		Object debugOutputHandler = getProperty(debugHandlerKey);
		Object debugOutputStream = getProperty(debugStreamKey);
		if (languageSession.getX4ODebugWriter()==null) {
			ContentWriter xmlDebugWriter = null;
			if (debugOutputHandler instanceof ContentWriter) {
				xmlDebugWriter = (ContentWriter)debugOutputHandler;
			} else if (debugOutputStream instanceof OutputStream) {
				debugCloseOutputStream = (OutputStream)debugOutputStream;
				xmlDebugWriter = new ContentWriterXml(debugCloseOutputStream);
			}
			if (xmlDebugWriter!=null) {
				xmlDebugWriter.startDocument();
				xmlDebugWriter.startPrefixMapping("debug", X4ODebugWriter.DEBUG_URI);
				X4ODebugWriter debugWriter = new X4ODebugWriter(xmlDebugWriter);
				X4OLanguageSessionLocal local = (X4OLanguageSessionLocal)languageSession;
				local.setX4ODebugWriter(debugWriter);
				
				// We only close if we started it, this is for recursief debugging.
				debugStarted = true;
			}
		}
		
		// debug language
		if (languageSession.hasX4ODebugWriter()) {
			languageSession.getX4ODebugWriter().debugConnectionStart(languageSession, this);
			
			// Add debug phases for all phases
			for (String key:languageSession.getLanguage().getPhaseManager().getPhaseKeys()) {
				X4OPhase p = languageSession.getLanguage().getPhaseManager().getPhase(key);
				
				p.addPhaseListener(languageSession.getX4ODebugWriter().createDebugX4OPhaseListener());
				
				if (shouldPrintTree(p)) {
					p.addPhaseListener(languageSession.getX4ODebugWriter().createDebugPrintTreePhaseListener());
				}
			}
		}
	}
	
	private boolean shouldPrintTree(X4OPhase p) {
		String phase = p.getId();
		if (X4OPhaseLanguageWrite.WRITE_END.equals(phase)) {
			return true;
		}
		if (X4OPhaseLanguageRead.READ_END.equals(phase)) {
			return true;
		}
		if (X4OPhaseLanguageRead.READ_RUN_ATTRIBUTE.equals(phase)) {
			return true;
		}
		if (X4OPhaseLanguageRead.READ_TRANSFORM.equals(phase)) {
			return true;
		}
		if (X4OPhaseLanguageRead.READ_BIND_ELEMENT.equals(phase)) {
			return true;
		}
		return false;
	}
	
	protected void debugException(X4OLanguageSession languageSession,Exception e) {
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
				// TODO print e;
				//logger.warning(ee.getMessage());
			}
		}
	}
	
	protected void debugStop(X4OLanguageSession languageSession) throws SAXException, IOException {
		if (languageSession.hasX4ODebugWriter()) {
			languageSession.getX4ODebugWriter().debugConnectionEnd();
		}
		if (debugStarted && languageSession.hasX4ODebugWriter()) {
			languageSession.getX4ODebugWriter().getContentWriter().endPrefixMapping("debug");
			languageSession.getX4ODebugWriter().getContentWriter().endDocument();
			if (debugCloseOutputStream!=null) {
				debugCloseOutputStream.flush();
				debugCloseOutputStream.close(); // need this here ?
			}
		}
	}
}
