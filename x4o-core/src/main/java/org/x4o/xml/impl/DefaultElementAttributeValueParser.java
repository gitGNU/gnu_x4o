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

package	org.x4o.xml.impl;

import java.util.Locale;
import java.util.logging.Logger;

import javax.el.ValueExpression;

import org.x4o.xml.conv.ObjectConverterException;
import org.x4o.xml.element.Element;
import org.x4o.xml.element.ElementAttributeValueParser;
import org.x4o.xml.element.ElementAttributeValueParserException;
import org.x4o.xml.element.ElementClassAttribute;
import org.x4o.xml.element.ElementInterface;


/**
 * An DefaultElementAttributeValueParser.
 * 
 * @author Willem Cazander
 * @version 1.0 Feb 16, 2007
 */
public class DefaultElementAttributeValueParser implements ElementAttributeValueParser {


	private Logger logger = null;
	
	public DefaultElementAttributeValueParser() {
		logger = Logger.getLogger(DefaultElementAttributeValueParser.class.getName());
	}
	
	
	/**
	 * @see org.x4o.xml.element.ElementAttributeValueParser#getParameterValue(java.lang.String, java.lang.String, org.x4o.xml.element.Element)
	 */
	public Object getParameterValue(String name, String valueString, Element element) throws ElementAttributeValueParserException,ObjectConverterException {
		Object value = valueString;

		if (isELParameter(name, valueString, element)) {
			value = getELParameterValue(valueString, element);
		}
		return getConvertedParameterValue(name, value, element);
	}




	/**
	 * @throws ObjectConverterException 
	 * @see org.x4o.xml.element.ElementAttributeValueParser#getConvertedParameterValue(java.lang.String, java.lang.Object, org.x4o.xml.element.Element)
	 */
	public Object getConvertedParameterValue(String name,Object value, Element element) throws ElementAttributeValueParserException, ObjectConverterException {
		//bit slow here
		if (value==null) {
			return null; // TODO: make setting for null
		}
			
		// do converts for ElementClass
		ElementClassAttribute attr = element.getElementClass().getElementClassAttributeByName(name);
		if (attr!=null && attr.getObjectConverter()!=null && value.getClass().isAssignableFrom(attr.getObjectConverter().getObjectClassTo())==false) {
			logger.finer("attr conv: "+attr.getObjectConverter()+" for name: "+name);
			Object result = attr.getObjectConverter().convertTo(value.toString(), Locale.getDefault());
			return result;
		}
		// check interfaces
		if (element.getElementObject()==null) {
			return value;
		}
		for (ElementInterface ei:element.getElementLanguage().findElementInterfaces(element.getElementObject())) {
			logger.finer("Found interface match executing converter.");
			for (ElementClassAttribute attrClass:ei.getElementClassAttributes()) {
				if (name.equals(attrClass.getName())==false) {
					continue;
				}
				if (attrClass.getObjectConverter()==null) {
					continue;
				}
				if (value.getClass().isAssignableFrom(attrClass.getObjectConverter().getObjectClassTo())) {
					continue; // make flag ?
				}
				logger.finest("attr conv interface: "+attrClass.getObjectConverter()+" for name: "+name);
				Object result = attrClass.getObjectConverter().convertTo(value.toString(), Locale.getDefault());
				return result; // ??
			}
		}
		return value;
	}
	
	/**
	 * @see org.x4o.xml.element.ElementAttributeValueParser#getELParameterValue(java.lang.String, org.x4o.xml.element.Element)
	 */
	public Object getELParameterValue(String value, Element element) throws ElementAttributeValueParserException {
		ValueExpression e = element.getElementLanguage().getExpressionFactory().createValueExpression(element.getElementLanguage().getELContext(), (String)value,Object.class);
		return e.getValue(element.getElementLanguage().getELContext());
	}

	/**
	 * @see org.x4o.xml.element.ElementAttributeValueParser#isELParameter(java.lang.String, java.lang.String, org.x4o.xml.element.Element)
	 */
	public boolean isELParameter(String name, String value, Element element) {
		if (value==null) {
			return false;
		}
		if (value.startsWith("${")==false) {
			return false;
		}
		if (element==null) {
			return true;	// null element disables checks
		}
		ElementClassAttribute attr = element.getElementClass().getElementClassAttributeByName(name);
		if (attr!=null && attr.getRunResolveEL()!=null && attr.getRunResolveEL()==false) {
			logger.finest("Skipping EL parsing for: "+name);
			return false;
		}
		
		for (ElementInterface ei:element.getElementLanguage().findElementInterfaces(element.getElementObject())) {
			logger.finest("Found interface match checking disables el parameters.");
			
			attr = ei.getElementClassAttributeByName(name);
			if (attr!=null && attr.getRunResolveEL()!=null && attr.getRunResolveEL()==false) {
				logger.finest("Skipping EL parsing for: "+name+" in interface element.");
				return false;
			}
		}
		return true;
	}
}
