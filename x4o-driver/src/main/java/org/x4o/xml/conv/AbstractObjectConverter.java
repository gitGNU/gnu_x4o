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

package org.x4o.xml.conv;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * AbstractObjectConverter to create ObjectConverters.
 *
 * @author Willem Cazander
 * @version 1.0 Jan 30, 2012
 */
@SuppressWarnings("serial")
abstract public class AbstractObjectConverter implements ObjectConverter {
	
	protected List<ObjectConverter> converters = new ArrayList<ObjectConverter>(5);

	abstract public Object convertAfterTo(Object obj, Locale locale) throws ObjectConverterException;
	abstract public Object convertAfterBack(Object obj, Locale locale) throws ObjectConverterException;
	abstract public ObjectConverter clone() throws CloneNotSupportedException;
	
	protected List<ObjectConverter> cloneConverters() throws CloneNotSupportedException {
		List<ObjectConverter> result = new ArrayList<ObjectConverter>(converters.size());
		for (ObjectConverter converter:converters) {
			result.add(converter.clone());
		}
		return result;
	}
	
	/**
	 * @see org.x4o.xml.conv.ObjectConverter#convertTo(java.lang.Object, java.util.Locale)
	 */
	public Object convertTo(Object obj, Locale locale) throws ObjectConverterException {
		if (converters.isEmpty()) {
			return convertAfterTo(obj,locale);
		}
		Object result = null;
		for (ObjectConverter conv:converters) {
			result = conv.convertTo(obj, locale);
		}
		result = convertAfterTo(obj,locale);
		return result;
	}

	/**
	 * @see org.x4o.xml.conv.ObjectConverter#convertBack(java.lang.Object, java.util.Locale)
	 */
	public Object convertBack(Object obj, Locale locale) throws ObjectConverterException {
		if (converters.isEmpty()) {
			return convertAfterBack(obj,locale);
		}
		Object result = null;
		for (ObjectConverter conv:converters) {
			result = conv.convertBack(obj, locale);
		}
		result = convertAfterBack(obj,locale);
		return result;
	}

	/**
	 * @see org.x4o.xml.conv.ObjectConverter#getObjectConverters()
	 */
	public List<ObjectConverter> getObjectConverters() {
		return converters;
	}

	/**
	 * @see org.x4o.xml.conv.ObjectConverter#addObjectConverter(org.x4o.xml.conv.ObjectConverter)
	 */
	public void addObjectConverter(ObjectConverter converter) {
		converters.add(converter);
	}

	/**
	 * @see org.x4o.xml.conv.ObjectConverter#removeObjectConverter(org.x4o.xml.conv.ObjectConverter)
	 */
	public void removeObjectConverter(ObjectConverter converter) {
		converters.remove(converter);
	}
}
