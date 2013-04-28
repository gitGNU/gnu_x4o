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
package	org.x4o.xml.conv.text;

import java.util.Locale;

import org.x4o.xml.conv.AbstractStringObjectConverter;
import org.x4o.xml.conv.ObjectConverter;
import org.x4o.xml.conv.ObjectConverterException;
import org.x4o.xml.lang.X4OLanguageClassLoader;

/**
 * Converts Sring of an Enum into the enum value.
 *
 * @author Willem Cazander
 * @version 1.0 Aug 31, 2007
 */
@SuppressWarnings({"rawtypes","unchecked"})
public class EnumConverter extends AbstractStringObjectConverter {
	
	private static final long serialVersionUID = 8860785472427794548L;


	private String enumClass = null;
	
	
	private Class enumObjectClass = null;
	
	/**
	 * Returns the convert to class.
	 * @see org.x4o.xml.conv.ObjectConverter#getObjectClassTo()
	 * @return The class to convert to.
	 */
	public Class<?> getObjectClassTo() {
		return Enum.class;
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
		if (getEnumClass()==null) {
			throw new ObjectConverterException(this,"enumClass String attribute is not set.");
		}
		//if (value instanceof Enum) {
		//	return value;
		//}
		String v = str; //value.toString();
		try {
			if (enumObjectClass==null) {
				enumObjectClass = (Class<?>)X4OLanguageClassLoader.loadClass(getEnumClass());
			}
			if (enumObjectClass==null) {
				throw new ObjectConverterException(this,"Could not load enumClass");
			}
			return Enum.valueOf(enumObjectClass, v);
		} catch (Exception e) {
			throw new ObjectConverterException(this,e.getMessage(),e);
		}
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
		return ((Enum<?>)obj).name();
	}

	/**
	 * @return the enumClass
	 */
	public String getEnumClass() {
		return enumClass;
	}


	/**
	 * @param enumClass the enumClass to set
	 */
	public void setEnumClass(String enumClass) {
		this.enumClass = enumClass;
	}
	
	/**
	 * Clone this ObjectConverter.
	 * @see org.x4o.xml.conv.AbstractObjectConverter#clone()
	 * @return The cloned ObjectConverter.
	 * @throws CloneNotSupportedException When cloning fails.
	 */
	@Override
	public ObjectConverter clone() throws CloneNotSupportedException {
		EnumConverter result = new EnumConverter();
		result.converters=cloneConverters();
		result.enumClass=enumClass;
		return result;
	}
}
