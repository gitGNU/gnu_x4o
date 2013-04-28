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

package org.x4o.xml.io;

import java.io.IOException;
import java.io.OutputStream;

import org.x4o.xml.lang.X4OLanguageContext;
import org.x4o.xml.lang.X4OLanguagePropertyKeys;
import org.x4o.xml.lang.phase.X4OPhaseException;
import org.x4o.xml.lang.phase.X4OPhaseType;
import org.xml.sax.SAXException;

/**
 * DefaultX4OWriter can write the xml language.
 * 
 * @author Willem Cazander
 * @version 1.0 Apr 6, 2013
 */
public class DefaultX4OWriter<T> extends AbstractX4OWriter<T> {
	
	/**
	 * Default constructor.
	 * @param languageContext	The language context for this writer.
	 */
	public DefaultX4OWriter(X4OLanguageContext languageContext) {
		super(languageContext);
	}
	
	/**
	 * @see org.x4o.xml.io.X4OWriterContext#writeContext(org.x4o.xml.lang.X4OLanguageContext, java.io.OutputStream)
	 */
	public void writeContext(X4OLanguageContext languageContext,OutputStream output) throws X4OConnectionException,SAXException,IOException {
		languageContext.setLanguageProperty(X4OLanguagePropertyKeys.WRITER_OUTPUT_STREAM, output);
		try {
			languageContext.getLanguage().getPhaseManager().runPhases(languageContext, X4OPhaseType.XML_WRITE);
		} catch (X4OPhaseException e) {
			throw new X4OConnectionException(e);
		}
	}
}
