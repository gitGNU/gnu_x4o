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

import java.io.File;

import org.x4o.xml.core.config.X4OLanguagePropertyKeys;
import org.x4o.xml.eld.xsd.EldXsdXmlGenerator;
import org.x4o.xml.element.ElementException;
import org.x4o.xml.element.ElementLanguage;

public class DefaultX4OSchemaWriter extends AbstractX4OConnection implements X4OSchemaWriter {
	
	public DefaultX4OSchemaWriter(ElementLanguage languageContext) {
		super(languageContext);
	}
	
	/**
	 * @see org.x4o.xml.io.X4OConnection#getPropertyKeySet()
	 */
	public String[] getPropertyKeySet() {
		return X4OLanguagePropertyKeys.DEFAULT_X4O_SCHEMA_WRITER_KEYS;
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
		// Start xsd generator
		EldXsdXmlGenerator xsd = new EldXsdXmlGenerator(getLanguageContext().getLanguage());
		xsd.writeSchema(basePath, namespace);
	}
}
