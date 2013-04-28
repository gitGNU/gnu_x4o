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
package org.x4o.xml.conv.text;

import java.util.Locale;

import org.x4o.xml.conv.AbstractStringObjectConverter;
import org.x4o.xml.conv.ObjectConverter;
import org.x4o.xml.conv.ObjectConverterException;

/**
 * LongConverter.
 *
 * @author Willem Cazander
 * @version 1.0 Jan 20, 2012
 */
public class LongConverter extends AbstractStringObjectConverter {

	private static final long serialVersionUID = 25132217809739854L;

	/**
	 * Returns the convert to class.
	 * @see org.x4o.xml.conv.ObjectConverter#getObjectClassTo()
	 * @return The class to convert to.
	 */
	public Class<?> getObjectClassTo() {
		return Long.class;
	}
	
	/**
	 * Converts string into object.
	 * 
	 * @see org.x4o.xml.conv.AbstractStringObjectConverter#convertStringTo(java.lang.String, java.util.Locale)
	 * @param str	The string to convert to object.
	 * @param locale The locale to convert the string from.
	 * @return The object converted from the string.
	 * @throws ObjectConverterException When conversion fails.
	 */
	public Object convertStringTo(String str, Locale locale) throws ObjectConverterException {
		return new Long(str);
	}
	
	/**
	 * Converts object into string.
	 * 
	 * @see org.x4o.xml.conv.AbstractStringObjectConverter#convertStringBack(java.lang.Object, java.util.Locale)
	 * @param obj	The object to convert to string.
	 * @param locale The locale to convert the object from.
	 * @return The string converted from the object.
	 * @throws ObjectConverterException When conversion fails.
	 */
	public String convertStringBack(Object obj,Locale locale) throws ObjectConverterException {
		return ((Long)obj).toString();
	}
	
	/**
	 * Clone this ObjectConverter.
	 * @see org.x4o.xml.conv.AbstractObjectConverter#clone()
	 * @return The cloned ObjectConverter.
	 * @throws CloneNotSupportedException When cloning fails.
	 */
	@Override
	public ObjectConverter clone() throws CloneNotSupportedException {
		LongConverter result = new LongConverter();
		result.converters=cloneConverters();
		return result;
	}
}
