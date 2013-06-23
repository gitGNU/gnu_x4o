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

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.el.ELContext;
import javax.el.ExpressionFactory;

import org.x4o.xml.io.XMLConstants;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.ext.DefaultHandler2;

/**
 * X4OLanguageProperty holds the language connection properties keys
 * 
 * @author Willem Cazander
 * @version 1.0 6 Aug 2012
 */
public enum X4OLanguageProperty {
	
	/** Read-Only property returning the language we are working with. */
	LANGUAGE_NAME(IO.GLOBAL,"language/name"),
	
	/** Read-Only property returning the version of the language. */
	LANGUAGE_VERSION(IO.GLOBAL,"language/version"),
	
	
	
	
	
	/** The input stream to parse, note is skipped when source is set. */
	READER_INPUT_STREAM(IO.READER,"reader/input/stream",InputStream.class),
	
	/** When set it overrides automatic encoding detection of sax parser. */
	READER_INPUT_ENCODING(IO.READER,"reader/input/encoding",String.class,XMLConstants.XML_DEFAULT_ENCODING),
	
	/** When set use this InputSource instance for parsing. */
	READER_INPUT_SOURCE(IO.READER,"reader/input/source",InputSource.class),
	
	/** Sets the system-id to the input source. */
	READER_INPUT_SYSTEM_ID(IO.READER,"reader/input/system-id",String.class),
	
	/** Sets the base path to resolve external input sources. */
	READER_INPUT_BASE_PATH(IO.READER,"reader/input/base-path",URL.class),
	
	
	
	/** SAX parser input buffer size: 512-16k defaults to 8k. */
	READER_BUFFER_SIZE(IO.READER,"reader/buffer-size",Integer.class,4096*2),

	/** When set it allows parsing of non-namespace aware documents. */
	READER_EMPTY_NAMESPACE_URI(IO.READER,"reader/empty-namespace-uri"),
	
	/** Set for custom handling of validation errors. */
	READER_ERROR_HANDLER(IO.READER,"reader/error-handler",ErrorHandler.class),
	
	/** Resolve more entities from local sources. */
	READER_ENTITY_RESOLVER(IO.READER,"reader/entity-resolver",EntityResolver.class),
	
	
	
	/** When set to true then input xml is validated. */
	READER_VALIDATION_SCHEMA_AUTO_WRITE(IO.READER,"reader/validation/schema-auto-write",Boolean.class,true),
	
	/** When set this path is searched for xsd schema files in the language sub directory. */
	READER_VALIDATION_SCHEMA_PATH(IO.READER,"reader/validation/schema-path",File.class),
	
	/** When set to true then input xml is validated. */
	READER_VALIDATION_INPUT(IO.READER,"reader/validation/input",Boolean.class,false),
	
	/** When set to true then input xsd xml grammer is validated. */
	READER_VALIDATION_INPUT_XSD(IO.READER,"reader/validation/input/xsd",Boolean.class,false),
	
	
	
	
	
	/** The writer output stream to write to. */
	WRITER_OUTPUT_STREAM(IO.WRITER,"writer/output/stream",OutputStream.class),
	
	/** The writer output encoding. */
	WRITER_OUTPUT_ENCODING(IO.WRITER,"writer/output/encoding",String.class,XMLConstants.XML_DEFAULT_ENCODING),
	
	/** The writer output newline. */
	WRITER_OUTPUT_CHAR_NEWLINE(IO.WRITER,"writer/output/char/newline",String.class),
	
	/** The writer output tab char. */
	WRITER_OUTPUT_CHAR_TAB(IO.WRITER,"writer/output/char/tab",String.class),
	
	/** When writing print schema uri. */
	WRITER_SCHEMA_URI_PRINT(IO.WRITER,"writer/schema/uri-print",Boolean.class,true),
	
	/** Override eld root schema uri. */
	WRITER_SCHEMA_URI_ROOT(IO.WRITER,"writer/schema/uri-root",String.class),
	
	
	
	
	
	/** The schema writer output path to write to. */
	SCHEMA_WRITER_OUTPUT_PATH(IO.SCHEMA_WRITER,"schema-writer/output/path",File.class),
	
	/** The schema writer output encoding. */
	SCHEMA_WRITER_OUTPUT_ENCODING(IO.SCHEMA_WRITER,"schema-writer/output/encoding",String.class,XMLConstants.XML_DEFAULT_ENCODING),
	
	/** The schema writer output newline. */
	SCHEMA_WRITER_OUTPUT_CHAR_NEWLINE(IO.SCHEMA_WRITER,"schema-writer/output/char/newline",String.class),
	
	/** The schema writer output tab char. */
	SCHEMA_WRITER_OUTPUT_CHAR_TAB(IO.SCHEMA_WRITER,"schema-writer/output/char/tab",String.class),
	
	
	
	
	
	/** When set to OutputStream xml debug is written to it. note: when output-handler is set this property is ignored. */
	DEBUG_OUTPUT_STREAM(IO.READER_WRITER,"debug/output-stream",OutputStream.class),

	/** When set to DefaultHandler2 xml debug events are fired to the object. */
	DEBUG_OUTPUT_HANDLER(IO.READER_WRITER,"debug/output-handler",DefaultHandler2.class),

	/* When set to true print also phases for parsing eld files. */
	//DEBUG_OUTPUT_ELD_PARSER(IO.READER_WRITER,"debug/output-eld-parser",Boolean.class,false),
	
	/* When set to true print full element tree per phase. */
	//DEBUG_OUTPUT_TREE_PER_PHASE("debug/output-tree-per-phase",Boolean.class),
	
	
	
	
	
	/** The beans in this map are set into the EL context for reference. */
	EL_BEAN_INSTANCE_MAP(IO.READER_WRITER,"el/bean-instance-map",Map.class),
	
	/** When set use this instance for the ELContext. */
	EL_CONTEXT_INSTANCE(IO.READER_WRITER,"el/context-instance",ELContext.class),
	
	/** When set use this instance as the ExpressionFactory. */
	EL_FACTORY_INSTANCE(IO.READER_WRITER,"el/factory-instance",ExpressionFactory.class),
	
	
	
	
	
	/** When set to an X4OPhase then parsing stops after completing the set phase. */
	PHASE_STOP_AFTER(IO.READER_WRITER,"phase/stop-after",String.class),
	
	/** When set to true skip the release phase. */
	PHASE_SKIP_RELEASE(IO.READER_WRITER,"phase/skip-release",Boolean.class,false),
	
	/** When set to true skip the run phase. */
	PHASE_SKIP_RUN(IO.READER_WRITER,"phase/skip-run",Boolean.class,false);
	
	
	
	
	
	// (temp) removed because init is now in driver manager
	
	/* When set to true skip the load siblings language phase. */
	//PHASE_SKIP_SIBLINGS(IO.READER,"phase/skip-siblings",Boolean.class,false);
	
	/* When set to true then eld xml is validated. */
	//VALIDATION_ELD(IO.INIT,"validation/eld",Boolean.class,false),
	
	/* When set to true than eld xsd xml grammer is also validated. */
	//VALIDATION_ELD_XSD(IO.INIT, "validation/eld/xsd",Boolean.class,false);
	
	public final static X4OLanguageProperty[] DEFAULT_X4O_READER_KEYS;
	public final static X4OLanguageProperty[] DEFAULT_X4O_WRITER_KEYS;
	public final static X4OLanguageProperty[] DEFAULT_X4O_SCHEMA_WRITER_KEYS;
	
	static {
		List<X4OLanguageProperty> readerResultKeys = new ArrayList<X4OLanguageProperty>();
		List<X4OLanguageProperty> writerResultKeys = new ArrayList<X4OLanguageProperty>();
		List<X4OLanguageProperty> schemaWriterResultKeys = new ArrayList<X4OLanguageProperty>();
		X4OLanguageProperty[] keys = X4OLanguageProperty.values();
		for (int i=0;i<keys.length;i++) {
			X4OLanguageProperty key = keys[i];
			if (IO.GLOBAL.equals(key.type) || IO.READER.equals(key.type) || IO.READER_WRITER.equals(key.type)) {
				readerResultKeys.add(key);
			}
			if (IO.GLOBAL.equals(key.type) || IO.WRITER.equals(key.type) || IO.READER_WRITER.equals(key.type)) {
				writerResultKeys.add(key);
			}
			if (IO.GLOBAL.equals(key.type) || IO.SCHEMA_WRITER.equals(key.type)) {
				schemaWriterResultKeys.add(key);
			}
		}
		DEFAULT_X4O_READER_KEYS = readerResultKeys.toArray(new X4OLanguageProperty[readerResultKeys.size()]);
		DEFAULT_X4O_WRITER_KEYS = writerResultKeys.toArray(new X4OLanguageProperty[writerResultKeys.size()]); 
		DEFAULT_X4O_SCHEMA_WRITER_KEYS = schemaWriterResultKeys.toArray(new X4OLanguageProperty[schemaWriterResultKeys.size()]);
	}
	
	enum IO {
		GLOBAL,
		READER,
		WRITER,
		READER_WRITER,
		SCHEMA_WRITER
	};
	
	private final static String URI_PREFIX = "http://language.x4o.org/xml/properties/";
	private final String uriName;
	private final Class<?>[] classTypes;
	private final Object defaultValue;
	private final X4OLanguageProperty.IO type;
	
	private X4OLanguageProperty(X4OLanguageProperty.IO type,String uriName) {
		this(type,uriName,String.class);
	}
	
	private X4OLanguageProperty(X4OLanguageProperty.IO type,String uriName,Class<?> classType) {
		this(type,uriName,new Class[]{classType},null);
	}
	
	private X4OLanguageProperty(X4OLanguageProperty.IO type,String uriName,Class<?> classType,Object defaultValue) {
		this(type,uriName,new Class[]{classType},defaultValue);
	}
	
	private X4OLanguageProperty(X4OLanguageProperty.IO type,String uriName,Class<?>[] classTypes,Object defaultValue) {
		this.type=type;
		this.uriName=URI_PREFIX+uriName;
		this.classTypes=classTypes;
		this.defaultValue=defaultValue;
	}
	
	/**
	 * Returns the uri defined by this property.
	 * @return The uri defined by this property.
	 */
	public final String toUri() {
		return uriName;
	}
	
	/**
	 * Returns the default value for this property. 
	 * @return	The default value for this property.
	 */
	public final Object getDefaultValue() {
		return defaultValue;
	}
	
	/**
	 * Checks if the object is valid to set on this property.
	 * This is done by checking the allowed class types if they are assignable from the value class.
	 * @param value	The	object to check.
	 * @return	Returns true when Object value is allowed to be set.
	 */
	public final boolean isValueValid(Object value) {
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
	public static final X4OLanguageProperty valueByUri(String uri) {
		if (uri==null) {
			throw new NullPointerException("Can't search null uri.");
		}
		if (uri.length()==0) {
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
