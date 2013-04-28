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
package org.x4o.xml.lang;

/**
 * X4OLanguagePropertyKeys is shortcut to the the properties by uri.
 * 
 * @author Willem Cazander
 * @version 1.0 6 Aug 2012
 */
public class X4OLanguagePropertyKeys {
	
	public static final String LANGUAGE_NAME						= X4OLanguageProperty.LANGUAGE_NAME.toUri();
	public static final String LANGUAGE_VERSION						= X4OLanguageProperty.LANGUAGE_VERSION.toUri();
	
	public static final String READER_INPUT_STREAM					= X4OLanguageProperty.READER_INPUT_STREAM.toUri();
	public static final String READER_INPUT_ENCODING				= X4OLanguageProperty.READER_INPUT_ENCODING.toUri();
	public static final String READER_INPUT_SOURCE					= X4OLanguageProperty.READER_INPUT_SOURCE.toUri();
	public static final String READER_INPUT_SYSTEM_ID				= X4OLanguageProperty.READER_INPUT_SYSTEM_ID.toUri();
	public static final String READER_INPUT_BASE_PATH				= X4OLanguageProperty.READER_INPUT_BASE_PATH.toUri();
	public static final String READER_BUFFER_SIZE					= X4OLanguageProperty.READER_BUFFER_SIZE.toUri();
	public static final String READER_EMPTY_NAMESPACE_URI			= X4OLanguageProperty.READER_EMPTY_NAMESPACE_URI.toUri();
	public static final String READER_ERROR_HANDLER					= X4OLanguageProperty.READER_ERROR_HANDLER.toUri();
	public static final String READER_ENTITY_RESOLVER				= X4OLanguageProperty.READER_ENTITY_RESOLVER.toUri();
	public static final String READER_VALIDATION_SCHEMA_AUTO_WRITE	= X4OLanguageProperty.READER_VALIDATION_SCHEMA_AUTO_WRITE.toUri();
	public static final String READER_VALIDATION_SCHEMA_PATH		= X4OLanguageProperty.READER_VALIDATION_SCHEMA_PATH.toUri();
	public static final String READER_VALIDATION_INPUT				= X4OLanguageProperty.READER_VALIDATION_INPUT.toUri();
	public static final String READER_VALIDATION_INPUT_XSD			= X4OLanguageProperty.READER_VALIDATION_INPUT_XSD.toUri();
	
	public static final String WRITER_OUTPUT_STREAM					= X4OLanguageProperty.WRITER_OUTPUT_STREAM.toUri();
	public static final String WRITER_OUTPUT_ENCODING				= X4OLanguageProperty.WRITER_OUTPUT_ENCODING.toUri();
	public static final String WRITER_OUTPUT_CHAR_NEWLINE			= X4OLanguageProperty.WRITER_OUTPUT_CHAR_NEWLINE.toUri();
	public static final String WRITER_OUTPUT_CHAR_TAB				= X4OLanguageProperty.WRITER_OUTPUT_CHAR_TAB.toUri();
	public static final String WRITER_SCHEMA_URI_PRINT				= X4OLanguageProperty.WRITER_SCHEMA_URI_PRINT.toUri();
	public static final String WRITER_SCHEMA_URI_ROOT				= X4OLanguageProperty.WRITER_SCHEMA_URI_ROOT.toUri();
	
	public static final String SCHEMA_WRITER_OUTPUT_PATH			= X4OLanguageProperty.SCHEMA_WRITER_OUTPUT_PATH.toUri();
	public static final String SCHEMA_WRITER_OUTPUT_ENCODING		= X4OLanguageProperty.SCHEMA_WRITER_OUTPUT_ENCODING.toUri();
	public static final String SCHEMA_WRITER_OUTPUT_CHAR_NEWLINE	= X4OLanguageProperty.SCHEMA_WRITER_OUTPUT_CHAR_NEWLINE.toUri();
	public static final String SCHEMA_WRITER_OUTPUT_CHAR_TAB		= X4OLanguageProperty.SCHEMA_WRITER_OUTPUT_CHAR_TAB.toUri();
	
	public static final String DEBUG_OUTPUT_STREAM					= X4OLanguageProperty.DEBUG_OUTPUT_STREAM.toUri();
	public static final String DEBUG_OUTPUT_HANDLER					= X4OLanguageProperty.DEBUG_OUTPUT_HANDLER.toUri();
	
	public static final String EL_BEAN_INSTANCE_MAP					= X4OLanguageProperty.EL_BEAN_INSTANCE_MAP.toUri();
	public static final String EL_FACTORY_INSTANCE					= X4OLanguageProperty.EL_FACTORY_INSTANCE.toUri();
	
	public static final String PHASE_STOP_AFTER						= X4OLanguageProperty.PHASE_STOP_AFTER.toUri();
	public static final String PHASE_SKIP_RELEASE					= X4OLanguageProperty.PHASE_SKIP_RELEASE.toUri();
	public static final String PHASE_SKIP_RUN						= X4OLanguageProperty.PHASE_SKIP_RUN.toUri();

	
	public final static String[] DEFAULT_X4O_READER_KEYS;
	public final static String[] DEFAULT_X4O_WRITER_KEYS;
	public final static String[] DEFAULT_X4O_SCHEMA_WRITER_KEYS;
	
	static {
		X4OLanguageProperty[] readerKeys = X4OLanguageProperty.DEFAULT_X4O_READER_KEYS;
		String[] readerResultKeys = new String[readerKeys.length];
		for (int i=0;i<readerResultKeys.length;i++) {
			readerResultKeys[i] = readerKeys[i].toUri();
		}
		DEFAULT_X4O_READER_KEYS = readerResultKeys;
		
		X4OLanguageProperty[] writerKeys = X4OLanguageProperty.DEFAULT_X4O_WRITER_KEYS;
		String[] writerResultKeys = new String[writerKeys.length];
		for (int i=0;i<writerResultKeys.length;i++) {
			writerResultKeys[i] = writerKeys[i].toUri();
		}
		DEFAULT_X4O_WRITER_KEYS = writerResultKeys; 
		
		X4OLanguageProperty[] schemaWriterKeys = X4OLanguageProperty.DEFAULT_X4O_SCHEMA_WRITER_KEYS;
		String[] schemaWriterResultKeys = new String[schemaWriterKeys.length];
		for (int i=0;i<schemaWriterResultKeys.length;i++) {
			schemaWriterResultKeys[i] = schemaWriterKeys[i].toUri();
		}
		DEFAULT_X4O_SCHEMA_WRITER_KEYS = schemaWriterResultKeys; 
	}
}
