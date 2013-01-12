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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import org.x4o.xml.conv.AbstractStringObjectConverter;
import org.x4o.xml.conv.ObjectConverter;
import org.x4o.xml.conv.ObjectConverterException;
import org.x4o.xml.core.config.X4OLanguageClassLoader;

/**
 * StringSplitConverter.
 *
 * @author Willem Cazander
 * @version 1.0 Aug 23, 2012
 */
public class StringSplitConverter extends AbstractStringObjectConverter {

	private static final long serialVersionUID = 418588893457634317L;
	private Class<?> classTo = null;
	private String split = null;
	private Integer splitSize = null;
	private String singleToMethod = null;
	private Boolean useNativeType = null;
	private List<StringSplitConverterStep> stringSplitConverterSteps = null;
	
	public StringSplitConverter() {
		stringSplitConverterSteps = new ArrayList<StringSplitConverterStep>(10);
	}
	
	/**
	 * Returns the convert to class.
	 * @see org.x4o.xml.conv.ObjectConverter#getObjectClassTo()
	 * @return The class to convert to.
	 */
	public Class<?> getObjectClassTo() {
		return classTo;
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
	@SuppressWarnings("rawtypes")
	public Object convertStringTo(String str, Locale locale) throws ObjectConverterException {
		if (split==null) {
			throw new ObjectConverterException(this,"split is not set.");
		}
		if (splitSize==null) {
			throw new ObjectConverterException(this,"splitSize is not set.");
		}
		if (classTo==null) {
			throw new ObjectConverterException(this,"classTo is not set.");
		}
		String[] strSplit = str.split(split);
		if(strSplit.length!=splitSize) {
			throw new ObjectConverterException(this,"Split size is wrong; "+strSplit.length+" need: "+splitSize);
		}
		List<StringSplitConverterStep> steps = getOrderedSteps(true);
		if (steps.size()!=splitSize) {
			throw new ObjectConverterException(this,"Step size is wrong; "+steps.size()+" need: "+splitSize);
		}
		try {
			Object[] singleMethodValues = new Object[splitSize];
			Object object = X4OLanguageClassLoader.newInstance(classTo);
			for (int i=0;i<steps.size();i++) {
				StringSplitConverterStep step = steps.get(i);
				Object stepObject = strSplit[i];
				Object stepValue = step.getObjectConverter().convertTo(stepObject, locale);
				if (singleToMethod==null) {
					Method m = classTo.getMethod(step.getToMethod(), new Class[] {stepValue.getClass()});
					m.invoke(object, stepValue);
				} else {
					singleMethodValues[i] = stepValue;
				}
			}
			if (singleToMethod!=null) {
				List<Class> arguClass = new ArrayList<Class>(singleMethodValues.length);
				for (int i=0;i<singleMethodValues.length;i++) {
					arguClass.add(singleMethodValues[i].getClass());
				}
				if (useNativeType!=null && useNativeType) {
					arguClass = convertToNative(arguClass);
				}
				Class[] arguArray = new Class[arguClass.size()];
				arguArray = arguClass.toArray(arguArray);
				Method m = classTo.getMethod(singleToMethod, arguArray);
				
				List<Object> arguValue = new ArrayList<Object>(singleMethodValues.length);
				for (int i=0;i<singleMethodValues.length;i++) {
					arguValue.add(singleMethodValues[i]);
				}
				Object[] valueArray = new Object[arguValue.size()];
				valueArray = arguValue.toArray(valueArray);
				m.invoke(object, valueArray);
			}
			return object;
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
	public String convertStringBack(Object object,Locale locale) throws ObjectConverterException {
		List<StringSplitConverterStep> steps = getOrderedSteps(false);
		if (steps.size()!=splitSize) {
			throw new ObjectConverterException(this,"Step size is wrong; "+steps.size()+" need: "+splitSize);
		}
		try {
			StringBuffer buf = new StringBuffer(200);
			for (int i=0;i<steps.size();i++) {
				StringSplitConverterStep step = steps.get(i);
				Method m = classTo.getMethod(step.getFromMethod(), new Class[] {});
				Object stepValue = m.invoke(object, new Object[] {});
				Object stepString = step.getObjectConverter().convertBack(stepValue, locale);
				buf.append(stepString.toString());
			}
			return buf.toString();
		} catch (Exception e) {
			throw new ObjectConverterException(this,e.getMessage(),e);
		}
	}
	
	/**
	 * Clone this ObjectConverter.
	 * @see org.x4o.xml.conv.AbstractObjectConverter#clone()
	 * @return The cloned ObjectConverter.
	 * @throws CloneNotSupportedException When cloning fails.
	 */
	@Override
	public ObjectConverter clone() throws CloneNotSupportedException {
		StringSplitConverter result = new StringSplitConverter();
		result.converters=cloneConverters();
		return result;
	}
	
	private List<StringSplitConverterStep> getOrderedSteps(boolean isTo) {
		List<StringSplitConverterStep> result = new ArrayList<StringSplitConverterStep>(stringSplitConverterSteps.size());
		result.addAll(stringSplitConverterSteps);
		Collections.sort(stringSplitConverterSteps,new StringSplitConverterStepComparator(isTo));
		return result;
	}

	public class StringSplitConverterStepComparator implements Comparator<StringSplitConverterStep> {
		boolean isTo = true;
		public StringSplitConverterStepComparator(boolean isTo) { this.isTo=isTo; }
		public int compare(StringSplitConverterStep e1, StringSplitConverterStep e2) {
			if (isTo) {
				return e1.getToOrder().compareTo(e2.getToOrder());
			} else {
				return e1.getFromOrder().compareTo(e2.getFromOrder());
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	private List<Class> convertToNative(List<Class> types) throws ObjectConverterException {
		List<Class> result = new ArrayList<Class>(types.size());
		for (int i=0;i<types.size();i++) {
			Class<?> clazz = types.get(i);
			if (clazz.isAssignableFrom(Integer.class)) {
				result.add(Integer.TYPE);
			} else if (clazz.isAssignableFrom(Long.class)) {
				result.add(Long.TYPE);
			} else if (clazz.isAssignableFrom(Float.class)) {
				result.add(Float.TYPE);
			} else if (clazz.isAssignableFrom(Double.class)) {
				result.add(Double.TYPE);
			} else if (clazz.isAssignableFrom(Boolean.class)) {
				result.add(Boolean.TYPE);
			} else {
				throw new ObjectConverterException(this,"Can't convert type to native; "+clazz);
			}
		}
		return result;
	}
	
	/**
	 * @return the classTo
	 */
	public Class<?> getClassTo() {
		return classTo;
	}

	/**
	 * @param classTo the classTo to set
	 */
	public void setClassTo(Class<?> classTo) {
		this.classTo = classTo;
	}

	/**
	 * @return the split
	 */
	public String getSplit() {
		return split;
	}

	/**
	 * @param split the split to set
	 */
	public void setSplit(String split) {
		this.split = split;
	}

	/**
	 * @return the splitSize
	 */
	public Integer getSplitSize() {
		return splitSize;
	}

	/**
	 * @param splitSize the splitSize to set
	 */
	public void setSplitSize(Integer splitSize) {
		this.splitSize = splitSize;
	}

	/**
	 * @return the singleToMethod
	 */
	public String getSingleToMethod() {
		return singleToMethod;
	}

	/**
	 * @param singleToMethod the singleToMethod to set
	 */
	public void setSingleToMethod(String singleToMethod) {
		this.singleToMethod = singleToMethod;
	}
	
	/**
	 * @return the useNativeType
	 */
	public Boolean getUseNativeType() {
		return useNativeType;
	}

	/**
	 * @param useNativeType the useNativeType to set
	 */
	public void setUseNativeType(Boolean useNativeType) {
		this.useNativeType = useNativeType;
	}

	public void addStringSplitConverterStep(StringSplitConverterStep stringSplitConverterStep) {
		stringSplitConverterSteps.add(stringSplitConverterStep);
	}
	public void removeStringSplitConverterStep(StringSplitConverterStep stringSplitConverterStep) {
		stringSplitConverterSteps.remove(stringSplitConverterStep);
	}
	public List<StringSplitConverterStep> getStringSplitConverterSteps() {
		return stringSplitConverterSteps;
	}
}
