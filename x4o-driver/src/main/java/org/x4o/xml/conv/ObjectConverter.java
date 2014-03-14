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

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

/**
 * The interface to convert objects.
 *
 * @author Willem Cazander
 * @version 1.0 Aug 28, 2008
 */
public interface ObjectConverter extends Cloneable,Serializable {

	/**
	 * @return	Returns the class which we can convert to.
	 */
	Class<?> getObjectClassTo();
	
	/**
	 * @return	Returns the class which we can convert from.
	 */
	Class<?> getObjectClassBack();
	
	/**
	 * Convert to the object.
	 * @param obj		The object to convert.
	 * @param locale	The Object convert locale if needed.
	 * @return Returns the converted object.
	 * @throws ObjectConverterException	When the conversion failes.
	 */
	Object convertTo(Object obj,Locale locale) throws ObjectConverterException;
	
	/**
	 * Convert the object back.
	 * @param obj		The object to convert.
	 * @param locale	The Object convert locale if needed.
	 * @return Returns the converted object.
	 * @throws ObjectConverterException	When the conversion failes.
	 */
	Object convertBack(Object obj,Locale locale) throws ObjectConverterException;
	
	/**
	 * @return	Returns list of child converters.
	 */
	List<ObjectConverter> getObjectConverters();
	
	/**
	 * @param converter	Adds an child converter.
	 */
	void addObjectConverter(ObjectConverter converter);
	
	/**
	 * @param converter	Removes this child converter.
	 */
	void removeObjectConverter(ObjectConverter converter);
		
	/**
	 * Force impl to have public clone method.
	 * 
	 * @return	An cloned ObjectConverter.
	 * @throws CloneNotSupportedException	If thrown when cloning is not supported.
	 */
	ObjectConverter clone() throws CloneNotSupportedException;
}
