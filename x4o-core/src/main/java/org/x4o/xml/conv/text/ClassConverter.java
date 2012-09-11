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

package org.x4o.xml.conv.text;

import java.util.Locale;

import org.x4o.xml.conv.AbstractStringObjectConverter;
import org.x4o.xml.conv.ObjectConverter;
import org.x4o.xml.conv.ObjectConverterException;
import org.x4o.xml.core.config.X4OLanguageClassLoader;

/**
 * Converts a String of an className into the the Class object.
 *
 * @author Willem Cazander
 * @version 1.0 Aug 31, 2007
 */
public class ClassConverter extends AbstractStringObjectConverter {

	private static final long serialVersionUID = -1992327327215087127L;

	public Class<?> getObjectClassTo() {
		return Class.class;
	}
	
	public Object convertStringTo(String str, Locale locale) throws ObjectConverterException {
		try {
			return X4OLanguageClassLoader.loadClass(str);
		} catch (Exception e) {
			throw new ObjectConverterException(this,e.getMessage(),e);
		}
	}
	
	public String convertStringBack(Object obj,Locale locale) throws ObjectConverterException {
		return ((Class<?>)obj).getName();
	}
	
	@Override
	public ObjectConverter clone() throws CloneNotSupportedException {
		ClassConverter result = new ClassConverter();
		result.converters=cloneConverters();
		return result;
	}
}
