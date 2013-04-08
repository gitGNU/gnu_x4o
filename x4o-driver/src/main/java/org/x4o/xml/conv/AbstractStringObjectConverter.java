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

import java.util.Locale;

/**
 * AbstractStringObjectConverter to create ObjectConverters which work with strings.
 *
 * @author Willem Cazander
 * @version 1.0 Jan 30, 2012
 */
@SuppressWarnings("serial")
abstract public class AbstractStringObjectConverter extends AbstractObjectConverter {

	/**
	 * @see org.x4o.xml.conv.ObjectConverter#getObjectClassBack()
	 */
	public Class<?> getObjectClassBack() {
		return String.class;
	}

	public Object convertAfterTo(Object obj, Locale locale) throws ObjectConverterException {
		if (obj instanceof String) {
			return convertStringTo((String)obj,locale);
		} else {
			return convertStringTo(obj.toString(),locale);
		}
	}
	
	public Object convertAfterBack(Object obj, Locale locale) throws ObjectConverterException {
		return convertStringBack(obj,locale);
	}

	abstract public Object convertStringTo(String str, Locale locale) throws ObjectConverterException;
	abstract public String convertStringBack(Object obj,Locale locale) throws ObjectConverterException;
}
