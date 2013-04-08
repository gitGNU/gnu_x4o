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

package	org.x4o.xml.element;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;

import org.x4o.xml.conv.DefaultObjectConverterProvider;
import org.x4o.xml.conv.ObjectConverter;
import org.x4o.xml.conv.ObjectConverterException;

/**
 * An DefaultElementObjectPropertyValue which does does get/set operations on pojo beans.
 * 
 * @author Willem Cazander
 * @version 1.0 Feb 16, 2007
 */
public class DefaultElementObjectPropertyValue implements ElementObjectPropertyValue,Serializable {

	private static final long serialVersionUID = 1L;
	
	private Logger logger = Logger.getLogger(DefaultElementObjectPropertyValue.class.getName());
	
	private Method findMethod(Object object,String parameterName,Object parameter) {
		
		// Get class but can be null.
		Class<?> parameterClass = null;
		if(parameter!=null) {
			parameterClass=parameter.getClass();
		}
		logger.finer("Trying value: pn="+parameterName+" o="+object+" p="+parameter+"("+parameterClass+")");
		String parameterNameSet = "set"+parameterName;
		Method[] methodes = object.getClass().getMethods();
		Method lastMethodFall = null;
		for (int i=0;i<methodes.length;i++) {
			Method method = methodes[i];
			Class<?>[] types = method.getParameterTypes();
			if (types.length == 0) {
				continue;
			}
			if (types.length > 1) {
				continue;
			}
			if (method.getName().equalsIgnoreCase(parameterNameSet)) {
				lastMethodFall = method;
				if (parameterClass!=null) {
					// Check for class based parameters.
					if (types[0].isAssignableFrom(parameterClass)) {
						logger.finest("Found method type: "+method.getParameterTypes()[0]+" for parameter: "+parameterName);
						return method;
					}
					// Check the native parameter types.
					if (parameterClass.isAssignableFrom(Boolean.class) && types[0].isAssignableFrom(Boolean.TYPE) ) {
						return method;
					}
					if (parameterClass.isAssignableFrom(Integer.class) && types[0].isAssignableFrom(Integer.TYPE) ) {
						return method;
					}
					if (parameterClass.isAssignableFrom(Long.class) && types[0].isAssignableFrom(Long.TYPE) ) {
						return method;
					}
					if (parameterClass.isAssignableFrom(Double.class) && types[0].isAssignableFrom(Double.TYPE) ) {
						return method;
					}
					if (parameterClass.isAssignableFrom(Float.class) && types[0].isAssignableFrom(Float.TYPE) ) {
						return method;
					}
					if (parameterClass.isAssignableFrom(Byte.class) && types[0].isAssignableFrom(Byte.TYPE) ) {
						return method;
					}
					if (parameterClass.isAssignableFrom(Character.class) && types[0].isAssignableFrom(Character.TYPE) ) {
						return method;
					}
				}
			}
		}
		return lastMethodFall;
	}
	
	/**
	 * TODO: this function is not completed !!
	 * 
	 *
	 * 
	 * @param object
	 * @param parameterName
	 * @param parameter
	 * @throws ElementParameterException
	 * @throws ElementObjectPropertyValueException
	 */
	public void setProperty(Object object,String parameterName,Object parameter) throws ElementObjectPropertyValueException {
		
		// find the method for the parameter
		Method lastMethod = findMethod(object,parameterName,parameter);
		if (lastMethod==null) {
			logger.finest("No method found, aborting parameter: "+parameterName);
			return;
		}
		
		// Special case for null value.
		if (parameter==null) {
			logger.finest("Found parameter is null Setting method: "+lastMethod.getParameterTypes()[0]+" for parameter: "+parameterName);
			try {
				lastMethod.invoke(object,new Object[]{parameter});
				return;
			} catch (Exception e) {
				throw new ElementObjectPropertyValueException(e.getMessage(),e);
			}
		}
		
		// Invoke for class based parameters
		if (lastMethod.getParameterTypes()[0].isAssignableFrom(parameter.getClass())) {
			logger.finest("Found parameter type: "+lastMethod.getParameterTypes()[0]+" for parameter: "+parameterName+" setting value: "+parameter);
			try {
				lastMethod.invoke(object,new Object[]{parameter});
				return;
			} catch (Exception e) {
				throw new ElementObjectPropertyValueException(e.getMessage(),e);
			}
		}
		
		// Invoke for native based types
		
		
		// not found 2sec try
		logger.finest("No corresoning class is found, trying convert manualy");
		
		// special case for object.
		if (lastMethod.getParameterTypes()[0].equals(Object.class) ) {
			logger.finest("Set Special object value: "+parameterName+" parm2: "+parameter+" on2="+lastMethod.getName()+" with="+object);
			try {
				lastMethod.invoke(object,new Object[]{parameter});
			} catch (Exception e) {
				throw new ElementObjectPropertyValueException(e.getMessage(),e);
			}
			return;
		}
		
		// all below creates from string
		if (parameter.toString().length()==0) {
			return; // can't set value from string with empty size.
		}
		
		//
		// JAVA OBJECT TYPES:
		//
		Object parameter2 = null;
		
		try {
			DefaultObjectConverterProvider convProvider = new DefaultObjectConverterProvider();
			convProvider.addDefaults();
			ObjectConverter conv = convProvider.getObjectConverterForClass(lastMethod.getParameterTypes()[0]);
			if (conv!=null) {
				parameter2 = conv.convertTo(parameter.toString(), Locale.getDefault());
			}
	
			/*
			 * JAVA NATIVE TYPES:
			 * 
			 * TYPE:	Size in bits:
			 * boolean	8, unsigned
			 * byte		8
			 * char		16, unsigned
			 * short	16
			 * int		32
			 * long		64
			 * float	32
			 * double	64
			 * void		n/a
			 */
			if (lastMethod.getParameterTypes()[0].isAssignableFrom(Boolean.TYPE) ) {
				conv = convProvider.getObjectConverterForClass(Boolean.class);
				parameter2 = conv.convertTo(parameter.toString(), Locale.getDefault());
			}
			if (lastMethod.getParameterTypes()[0].isAssignableFrom(Integer.TYPE) ) {
				conv = convProvider.getObjectConverterForClass(Integer.class);
				parameter2 = conv.convertTo(parameter.toString(), Locale.getDefault());
			}
			if (lastMethod.getParameterTypes()[0].isAssignableFrom(Long.TYPE) ) {
				conv = convProvider.getObjectConverterForClass(Long.class);
				parameter2 = conv.convertTo(parameter.toString(), Locale.getDefault());
			}
			if (lastMethod.getParameterTypes()[0].isAssignableFrom(Double.TYPE) ) {
				conv = convProvider.getObjectConverterForClass(Double.class);
				parameter2 = conv.convertTo(parameter.toString(), Locale.getDefault());
			}
			if (lastMethod.getParameterTypes()[0].isAssignableFrom(Float.TYPE) ) {
				conv = convProvider.getObjectConverterForClass(Float.class);
				parameter2 = conv.convertTo(parameter.toString(), Locale.getDefault());
			}
			if (lastMethod.getParameterTypes()[0].isAssignableFrom(Byte.TYPE) ) {
				conv = convProvider.getObjectConverterForClass(Byte.class);
				parameter2 = conv.convertTo(parameter.toString(), Locale.getDefault());
			}
			if (lastMethod.getParameterTypes()[0].isAssignableFrom(Character.TYPE) ) {
				conv = convProvider.getObjectConverterForClass(Character.class);
				parameter2 = conv.convertTo(parameter.toString(), Locale.getDefault());
			}
			
		} catch (ObjectConverterException oce) {
			throw new ElementObjectPropertyValueException(oce.getMessage(),oce);
		}
		
		if (parameter2==null) {
			throw new ElementObjectPropertyValueException("Could not convert to type for parameter: '"+parameterName+"' value: '"+parameter+"'");
		}
		
		logger.finest("Set value: "+parameterName+" parm2: "+parameter2+" on2="+lastMethod.getName()+" with="+object);
		try {
			lastMethod.invoke(object,new Object[]{parameter2});
		} catch (Exception e) {
			throw new ElementObjectPropertyValueException(e.getMessage(),e);
		}
	}
	
	/**
	 * Gets the property of an bean.
	 * 
	 * @param object	The object to get from.
	 * @param parameterName	The parameter name of the property to get.
	 * @throws ElementParameterException
	 * @throws ElementObjectPropertyValueException
	 */
	public Object getProperty(Object object,String parameterName) throws ElementObjectPropertyValueException {

		if (object==null) {
			throw new NullPointerException("Can't get property of null object.");
		}
		if (parameterName==null) {
			throw new NullPointerException("Can't get property is the name is null.");
		}
		
		Logger logger = Logger.getLogger(DefaultElementObjectPropertyValue.class.getName());
		
		String propRest = null;
		int index = parameterName.indexOf(".");
		if(index>0) {
			propRest = parameterName.substring(index+1);
			parameterName = parameterName.substring(0,index);
			logger.finest("slit property into: '"+propRest+"' and '"+parameterName+"'");
		}

		logger.finer("Trying value: pn="+parameterName+" o="+object);
		String parameterNameSet = "get"+parameterName;
		Method[] methodes = object.getClass().getMethods();
		Method lastMethod = null;
		
		// a bit hackie
		for(int i=0;i<methodes.length;i++) {
			Method method = methodes[i];
			Class<?>[] types = method.getParameterTypes();
			if (types.length != 0) {
				continue;
			}
			if(method.getName().equalsIgnoreCase(parameterNameSet)) {
				logger.finest("Found method: "+method.getName());
				lastMethod = method;
				break;
			}
		}
		
		if (lastMethod==null) {
			for(int i=0;i<methodes.length;i++) {
				Method method = methodes[i];
				if(method.getName().equalsIgnoreCase("is"+parameterName)) {
					logger.finest("Found is method: "+method.getName());
					lastMethod = method;
					break;
				}
				if(method.getName().equalsIgnoreCase("has"+parameterName)) {
					logger.finest("Found has method: "+method.getName());
					lastMethod = method;
					break;
				}
			}
		}
		
		if (lastMethod==null) {
			throw new ElementObjectPropertyValueException("Could not find method for parameter: '"+parameterName+"' object: '"+object+"'");
		}

		Object result = null;
		try {
			result = lastMethod.invoke(object,new Object[]{});
		} catch (Exception e) {
			throw new ElementObjectPropertyValueException(e.getMessage(),e);
		}
			
		if (propRest!=null) {
			if (result==null) {
				return null; // no need to go deeper into a null value.
			}
			// recursif function:
			return getProperty(result,propRest);
		}
		
		return result;
	}
	
	/**
	 * @see org.x4o.xml.element.ElementObjectPropertyValue#setPropertyMap(java.lang.Object, java.util.Map)
	 */
	public void setPropertyMap(Object object, Map<String, Object> attributes) throws ElementObjectPropertyValueException {
		Iterator<String> keyIterator = attributes.keySet().iterator();
		while(keyIterator.hasNext()) {
			String key = keyIterator.next();
			setProperty(object,key,attributes.get(key));
		}
	}
}