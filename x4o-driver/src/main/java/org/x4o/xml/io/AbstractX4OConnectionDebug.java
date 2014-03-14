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

import org.x4o.xml.io.sax.X4ODebugWriter;
import org.x4o.xml.io.sax.ext.ContentWriter;
import org.x4o.xml.io.sax.ext.ContentWriterXml;
import org.x4o.xml.lang.X4OLanguageSession;
import org.x4o.xml.lang.X4OLanguageSessionLocal;
import org.x4o.xml.lang.phase.X4OPhase;
import org.x4o.xml.lang.phase.X4OPhaseException;
import org.x4o.xml.lang.phase.X4OPhaseLanguageRead;
import org.x4o.xml.lang.phase.X4OPhaseLanguageWrite;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * AbstractX4OConnectionDebug has the abstract debug code of all connections.
 * 
 * @author Willem Cazander
 * @version 1.0 Mar 14, 2014
 */
public abstract class AbstractX4OConnectionDebug implements X4OConnection {
	
	private boolean debugStarted = false;
	private boolean debugAutoCloseOutputStream = true;
	private OutputStream debugCloseOutputStream = null;
	protected final static String ABSTRACT_DEBUG_OUTPUT_HANDLER       = "debug/output-handler";
	protected final static String ABSTRACT_DEBUG_OUTPUT_STREAM        = "debug/output-stream";
	protected final static String ABSTRACT_DEBUG_OUTPUT_STREAM_CLOSE  = "debug/output-stream-close";
	
	protected void debugStart(X4OLanguageSession languageSession,String debugHandlerKey,String debugStreamKey,String debugStreamCloseKey) throws UnsupportedEncodingException, SAXException {
		debugAutoCloseOutputStream = (Boolean)getProperty(debugStreamCloseKey);
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
	
	protected void debugException(X4OLanguageSession languageSession,Exception e) throws X4OConnectionException {
		if (!languageSession.hasX4ODebugWriter()) {
			return;
		}
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
		} catch (SAXException ee) {
			throw new X4OConnectionException("Error while writing debug; "+ee.getMessage(),ee);
		}
	}
	
	protected void debugStop(X4OLanguageSession languageSession) throws X4OConnectionException {
		if (!languageSession.hasX4ODebugWriter()) {
			return;
		}
		try {
			languageSession.getX4ODebugWriter().debugConnectionEnd();
			if (!debugStarted) {
				return; // not in this instance started so no stopping then.
			}
			languageSession.getX4ODebugWriter().getContentWriter().endPrefixMapping("debug");
			languageSession.getX4ODebugWriter().getContentWriter().endDocument();
			if (debugCloseOutputStream==null) {
				return; //  we have handler
			}
			debugCloseOutputStream.flush();
			if (!debugAutoCloseOutputStream) {
				return; // no auto close
			}
			debugCloseOutputStream.close();
		} catch (SAXException ee) {
			throw new X4OConnectionException("Error while closing debug; "+ee.getMessage(),ee);
		} catch (IOException ee) {
			throw new X4OConnectionException("Error while closing debug; "+ee.getMessage(),ee);
		}
	}
}
