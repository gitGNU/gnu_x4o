/*
 * Copyright (c) 2004-2014, Willem Cazander
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
package org.x4o.xml.conv;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.x4o.xml.conv.text.BooleanConverter;
import org.x4o.xml.conv.text.ByteConverter;
import org.x4o.xml.conv.text.CharacterConverter;
import org.x4o.xml.conv.text.ClassConverter;
import org.x4o.xml.conv.text.DoubleConverter;
import org.x4o.xml.conv.text.FloatConverter;
import org.x4o.xml.conv.text.IntegerConverter;
import org.x4o.xml.conv.text.LongConverter;
import org.x4o.xml.conv.text.URLConverter;

/**
 * DefaultObjectConverterProvider holds the defined converts.
 *
 * @author Willem Cazander
 * @version 1.0 Jan 20, 2012
 */
public class DefaultObjectConverterProvider implements ObjectConverterProvider {
	
	private Map<Class<?>,ObjectConverter> converters = null;
	
	/**
	 * Create new DefaultObjectConverterProvider.
	 */
	public DefaultObjectConverterProvider() {
		converters = new HashMap<Class<?>,ObjectConverter>(20);
	}
	
	/**
	 * Create new DefaultObjectConverterProvider.
	 * @param addDefaults	When true do the addDefaults().
	 */
	public DefaultObjectConverterProvider(boolean addDefaults) {
		this();
		if (addDefaults) {
			addDefaults();
		}
	}
	
	/**
	 * Adds the default converters.
	 */
	public void addDefaults() {
		addObjectConverter(new BooleanConverter());
		addObjectConverter(new ByteConverter());
		addObjectConverter(new CharacterConverter());
		addObjectConverter(new DoubleConverter());
		addObjectConverter(new FloatConverter());
		addObjectConverter(new IntegerConverter());
		addObjectConverter(new LongConverter());
		addObjectConverter(new URLConverter());
		addObjectConverter(new ClassConverter());
	}
	
	/**
	 * @param converter	The converter to add.
	 */
	public void addObjectConverter(ObjectConverter converter) {
		converters.put(converter.getObjectClassTo(), converter);
	}

	/**
	 * @see org.x4o.xml.conv.ObjectConverterProvider#getObjectConverterForClass(java.lang.Class)
	 * @param clazz The Class to search an ObjectConverter for.
	 * @return The ObjectConverter or null  for the class.
	 */
	public ObjectConverter getObjectConverterForClass(Class<?> clazz) {
		return converters.get(clazz);
	}
	
	/**
	 * @return	Returns all ObjectConverted stored in this class.
	 */
	protected Collection<ObjectConverter> getObjectConverters() {
		return converters.values();
	}
}
