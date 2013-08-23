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
package org.x4o.xml.io.sax;

import org.x4o.xml.element.ElementException;
import org.x4o.xml.io.DefaultX4OReader;
import org.x4o.xml.io.sax.ext.PropertyConfig;
import org.x4o.xml.lang.X4OLanguageContext;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * X4OErrorHandler prints the SAX2 Errors and Warsnings when parsing xml.
 * 
 * @author Willem Cazander
 * @version 1.0 Feb 8, 2007
 */
public class X4OErrorHandler implements ErrorHandler {
	
	private X4OLanguageContext languageContext = null;
	private ErrorHandler errorHandler = null;
	
	/**
	 * Construct a new SAXErrorPrinter
	 * @param languageContext	The language to get errors to.
	 */
	public X4OErrorHandler(X4OLanguageContext languageContext,PropertyConfig propertyConfig) {
		if (languageContext==null) {
			throw new NullPointerException("Can't debug and proxy errors with null languageContext.");
		}
		this.languageContext=languageContext;
		this.errorHandler=(ErrorHandler)propertyConfig.getProperty(DefaultX4OReader.SAX_ERROR_HANDLER);
	}

	/**
	 * Prints the error message to debug output.
	 */
	private void printError(boolean isError, SAXParseException exception) throws SAXException {
		if (languageContext.hasX4ODebugWriter()==false) {
			return;
		}
		String message = printErrorString(isError,exception);
		try {
			languageContext.getX4ODebugWriter().debugPhaseMessage(message, X4OErrorHandler.class);
		} catch (ElementException e) {
			throw new SAXException(e);
		}
	}
	
	/**
	 * Prints the error message to string.
	 */
	private String printErrorString(boolean isError, SAXParseException exception) {
		StringBuffer buf = new StringBuffer(50);
		buf.append(exception.getSystemId());
		buf.append(":");
		buf.append(exception.getLineNumber());
		buf.append(":");
		buf.append(exception.getColumnNumber());
		buf.append(" ");
		buf.append((isError?"Error: ":"Warning: "));
		buf.append(exception.getMessage());
		return buf.toString();
	}

	// ========= ErrorHandler
	
	/**
	 * Receive notification of a SAX warning.
	 */
	public void warning(SAXParseException exception) throws SAXException {
		printError(false, exception);
		if (errorHandler!=null) {
			errorHandler.warning(exception);
		} else {
			throw new SAXException(printErrorString(false,exception));
		}
	}

	/**
	 * Receive notification of a SAX recoverable error.
	 */
	public void error(SAXParseException exception) throws SAXException {
		printError(true, exception);
		if (errorHandler!=null) {
			errorHandler.error(exception);
		} else {
			throw new SAXException(printErrorString(true,exception));
		}
	}

	/**
	 * Receive notification of a SAX non-recoverable error.
	 */
	public void fatalError(SAXParseException exception) throws SAXException {
		printError(true, exception);
		if (errorHandler!=null) {
			errorHandler.fatalError(exception);
		} else {
			throw new SAXException(printErrorString(true,exception));
		}
	}
}
