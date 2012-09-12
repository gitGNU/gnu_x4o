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

package org.x4o.xml.core.config;

/**
 * X4OLanguagePropertyKeys is shortcut to the the properties by uri.
 * 
 * @author Willem Cazander
 * @version 1.0 6 Aug 2012
 */
public class X4OLanguagePropertyKeys {
	
	public static final String LANGUAGE_NAME				= X4OLanguageProperty.LANGUAGE_NAME.toUri();
	public static final String LANGUAGE_VERSION				= X4OLanguageProperty.LANGUAGE_VERSION.toUri();
		
	public static final String DEBUG_OUTPUT_STREAM			= X4OLanguageProperty.DEBUG_OUTPUT_STREAM.toUri();
	public static final String DEBUG_OUTPUT_HANDLER			= X4OLanguageProperty.DEBUG_OUTPUT_HANDLER.toUri();
	public static final String DEBUG_OUTPUT_ELD_PARSER		= X4OLanguageProperty.DEBUG_OUTPUT_ELD_PARSER.toUri();
	
	public static final String INPUT_BUFFER_SIZE			= X4OLanguageProperty.INPUT_BUFFER_SIZE.toUri();
	public static final String INPUT_EMPTY_NAMESPACE_URI	= X4OLanguageProperty.INPUT_EMPTY_NAMESPACE_URI.toUri();
	
	public static final String INPUT_SOURCE_STREAM			= X4OLanguageProperty.INPUT_SOURCE_STREAM.toUri();
	public static final String INPUT_SOURCE_ENCODING		= X4OLanguageProperty.INPUT_SOURCE_ENCODING.toUri();
	public static final String INPUT_SOURCE_OBJECT			= X4OLanguageProperty.INPUT_SOURCE_OBJECT.toUri();
	public static final String INPUT_SOURCE_SYSTEM_ID		= X4OLanguageProperty.INPUT_SOURCE_SYSTEM_ID.toUri();
	public static final String INPUT_SOURCE_BASE_PATH		= X4OLanguageProperty.INPUT_SOURCE_BASE_PATH.toUri();
	
	public static final String CONFIG_ERROR_HANDLER			= X4OLanguageProperty.CONFIG_ERROR_HANDLER.toUri();
	public static final String CONFIG_ENTITY_RESOLVER		= X4OLanguageProperty.CONFIG_ENTITY_RESOLVER.toUri();
	
	public static final String EL_BEAN_INSTANCE_MAP			= X4OLanguageProperty.EL_BEAN_INSTANCE_MAP.toUri();
	public static final String EL_FACTORY_INSTANCE			= X4OLanguageProperty.EL_FACTORY_INSTANCE.toUri();
	
	public static final String PHASE_STOP_AFTER				= X4OLanguageProperty.PHASE_STOP_AFTER.toUri();
	public static final String PHASE_SKIP_RELEASE			= X4OLanguageProperty.PHASE_SKIP_RELEASE.toUri();
	public static final String PHASE_SKIP_RUN				= X4OLanguageProperty.PHASE_SKIP_RUN.toUri();
	public static final String PHASE_SKIP_SIBLINGS			= X4OLanguageProperty.PHASE_SKIP_SIBLINGS.toUri();
	
	public static final String VALIDATION_SCHEMA_PATH		= X4OLanguageProperty.VALIDATION_SCHEMA_PATH.toUri();
	public static final String VALIDATION_INPUT				= X4OLanguageProperty.VALIDATION_INPUT.toUri();
	public static final String VALIDATION_INPUT_XSD			= X4OLanguageProperty.VALIDATION_INPUT_XSD.toUri();
	public static final String VALIDATION_ELD				= X4OLanguageProperty.VALIDATION_ELD.toUri();
	public static final String VALIDATION_ELD_XSD			= X4OLanguageProperty.VALIDATION_ELD_XSD.toUri();
}
