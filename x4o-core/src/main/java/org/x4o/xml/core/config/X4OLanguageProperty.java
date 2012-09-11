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

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Map;

import javax.el.ExpressionFactory;

import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.ext.DefaultHandler2;

/**
 * X4OLanguageProperty holds the language parser properties keys
 * 
 * @author Willem Cazander
 * @version 1.0 6 Aug 2012
 */
public enum X4OLanguageProperty {
	
	/** Read-Only property returning the language we are working with. */
	LANGUAGE_NAME("language/name"),
	
	/** Read-Only property returning the version of the language. */
	LANGUAGE_VERSION("language/version"),
		
	
	
	/** When set to OutputStream xml debug is written to it. note: when output-handler is set this property is ignored. */
	DEBUG_OUTPUT_STREAM("debug/output-stream",OutputStream.class),

	/** When set to DefaultHandler2 xml debug events are fired to the object. */
	DEBUG_OUTPUT_HANDLER("debug/output-handler",DefaultHandler2.class),

	/** When set to true print also phases for parsing eld files. */
	DEBUG_OUTPUT_ELD_PARSER("debug/output-eld-parser",Boolean.class,false),
	
	/** When set to true print full element tree per phase. */
	//DEBUG_OUTPUT_TREE_PER_PHASE("debug/output-tree-per-phase",Boolean.class),
	
	
	/** SAX parser input buffer size: 512-16k defaults to 8k. */
	INPUT_BUFFER_SIZE("input/buffer-size",Integer.class,4096*2),

	/** When set it allows parsing of non-namespace aware documents. */
	INPUT_EMPTY_NAMESPACE_URI("input/empty-namespace-uri"),
	
	
	
	/** The input stream to parse, note is skipped when object is set. */
	INPUT_SOURCE_STREAM("input/source/stream",InputStream.class),
	
	/** When set it overrides automatic encoding detection of sax parser. */
	INPUT_SOURCE_ENCODING("input/source/encoding"),
	
	/** When set use this InputSource instance for parsing. */
	INPUT_SOURCE_OBJECT("input/source/object",InputSource.class),
	
	/** Sets the system-id to the input source. */
	INPUT_SOURCE_SYSTEM_ID("input/source/system-id",String.class),
	
	/** Sets the base path to resolve external input sources. */
	INPUT_SOURCE_BASE_PATH("input/source/base-path",URL.class),
	
	/** Set for custom handling of validation errors. */
	CONFIG_ERROR_HANDLER("config/error-handler",ErrorHandler.class),
	
	/** Resolve more entities from local sources. */
	CONFIG_ENTITY_RESOLVER("config/entity-resolver",EntityResolver.class),
	
	//CONFIG_CACHE_HANDLER("config/cache-handler"),
	//CONFIG_MODULE_PROVIDER("config/module-provider"),
	//CONFIG_LOCK_PROPERTIES("config/lock-properties"),
	
	
	/** The beans in this map are set into the EL context for reference. */
	EL_BEAN_INSTANCE_MAP("el/bean-instance-map",Map.class),
	//EL_CONTEXT_INSTANCE("el/context-instance"),
	
	/** When set this instance is used in ElementLanguage. */
	EL_FACTORY_INSTANCE("el/factory-instance",ExpressionFactory.class),
	
	//EL_INSTANCE_FACTORY("phase/skip-elb-init",Boolean.class),
	
	/** When set to an X4OPhase then parsing stops after completing the set phase. */
	PHASE_STOP_AFTER("phase/stop-after",Boolean.class),
	
	/** When set to true skip the release phase. */
	PHASE_SKIP_RELEASE("phase/skip-release",Boolean.class,false),
	
	/** When set to true skip the run phase. */
	PHASE_SKIP_RUN("phase/skip-run",Boolean.class,false),
	
	/** When set to true skip the load siblings language phase. */
	PHASE_SKIP_SIBLINGS("phase/skip-siblings",Boolean.class,false),
	
	
	/** When set this path is searched for xsd schema files in the language sub directory. */
	VALIDATION_SCHEMA_PATH("validation/schema-path",File.class),
	
	/** When set to true then input xml is validated. */
	VALIDATION_INPUT("validation/input",Boolean.class,false),
	
	/** When set to true then input xsd xml grammer is validated. */
	VALIDATION_INPUT_XSD("validation/input/xsd",Boolean.class,false),
	
	/** When set to true then eld xml is validated. */
	VALIDATION_ELD("validation/eld",Boolean.class,false),
	
	/** When set to true than eld xsd xml grammer is also validated. */
	VALIDATION_ELD_XSD("validation/eld/xsd",Boolean.class,false);
	
	
	private final static String URI_PREFIX = "http://language.x4o.org/xml/properties/";
	private final String uriName;
	private final Class<?>[] classTypes;
	private final Object defaultValue;
	
	private X4OLanguageProperty(String uriName) {
		this(uriName,String.class);
	}
	
	private X4OLanguageProperty(String uriName,Class<?> classType) {
		this(uriName,new Class[]{classType},null);
	}
	
	private X4OLanguageProperty(String uriName,Class<?> classType,Object defaultValue) {
		this(uriName,new Class[]{classType},defaultValue);
	}
	
	private X4OLanguageProperty(String uriName,Class<?>[] classTypes,Object defaultValue) {
		this.uriName=URI_PREFIX+uriName;
		this.classTypes=classTypes;
		this.defaultValue=defaultValue;
	}
	
	/**
	 * Returns the uri defined by this property.
	 * @return The uri defined by this property.
	 */
	public String toUri() {
		return uriName;
	}
	
	/**
	 * Returns the default value for this property. 
	 * @return	The default value for this property.
	 */
	public Object getDefaultValue() {
		return defaultValue;
	}
	
	/**
	 * Checks if the object is valid to set on this property.
	 * This is done by checking the allowed class types if they are assignable from the value class.
	 * @param value	The	object to check.
	 * @return	Returns true when Object value is allowed to be set.
	 */
	public boolean isValueValid(Object value) {
		if (LANGUAGE_NAME.equals(this) | LANGUAGE_VERSION.equals(this)) {
			return false; // read only are not valid to set.
		}
		if (value==null) {
			return true;
		}
		Class<?> valueClass = value.getClass();
		for (Class<?> c:classTypes) {
			if (c.isAssignableFrom(valueClass)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Search the enum for the value defined by the given uri.
	 * @param uri	The uri to search for.
	 * @return	Return the property for the given uri.
	 * @throws IllegalArgumentException when uri is not found.
	 */
	static public X4OLanguageProperty valueByUri(String uri) {
		if (uri==null) {
			throw new NullPointerException("Can't search null uri.");
		}
		if (uri.isEmpty()) {
			throw new IllegalArgumentException("Can't search empty uri.");
		}
		if (uri.startsWith(URI_PREFIX)==false)  {
			throw new IllegalArgumentException("Can't search for other name local prefix: "+URI_PREFIX+" found: "+uri);
		}
		for (X4OLanguageProperty p:values()) {
			if (uri.equals(p.toUri())) {
				return p;
			}
		}
		throw new IllegalArgumentException("Could not find language property for uri key: "+uri);
	}
}
