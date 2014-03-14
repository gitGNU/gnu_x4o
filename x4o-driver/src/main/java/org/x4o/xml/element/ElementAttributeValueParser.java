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
package	org.x4o.xml.element;

import org.x4o.xml.conv.ObjectConverterException;

/**
 * Helper interface for setting properties.
 * 
 * @author Willem Cazander
 * @version 1.0 Aug 20, 2005
 */
public interface ElementAttributeValueParser {
	
	/**
	 * Checks if the value is an EL parameter.
	 * @param name	The name of the attribute.
	 * @param value	The value of the attribute.
	 * @param element	The element of the attribute.
	 * @return	Returns true if value is EL parameter.
	 */
	boolean isELParameter(String name,String value,Element element);
	
	/**
	 * Returns the object which is stored in the ELContext
	 * @param value	The attribute value.
	 * @param element	The element of the attribute.
	 * @return	Returns the resolved el parameter value.
	 * @throws ElementParameterException
	 * @throws ElementObjectPropertyValueException
	 */
	Object getELParameterValue(String value,Element element) throws ElementAttributeValueParserException,ObjectConverterException;
	
	/**
	 * Convert the value into a new value genereted by parameterConverters.
	 * @param name	The name of the attribute.
	 * @param value	The value of the attribute.
	 * @param element	The element of the attribute.
	 * @return	Returns the converted attribute value.
	 * @throws ElementParameterException
	 */
	Object getConvertedParameterValue(String name,Object value,Element element) throws ElementAttributeValueParserException,ObjectConverterException;
	
	/**
	 * Does is all, Checks if value is EL parameter and lookups the object.
	 * and converts to new object via parameter converter and return value.
	 * @param name	The name of the attribute.
	 * @param value	The value of the attribute.
	 * @param element	The element of the attribute.
	 * @return	Returns the attribute value.
	 * @throws ElementParameterException
	 */
	Object getParameterValue(String name,String value,Element element) throws ElementAttributeValueParserException,ObjectConverterException;
}
