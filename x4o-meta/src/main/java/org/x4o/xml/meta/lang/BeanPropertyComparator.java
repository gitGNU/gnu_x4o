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

package org.x4o.xml.meta.lang;

import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.x4o.xml.impl.DefaultElementObjectPropertyValue;


/**
 *  Compares a property of a java bean.
 *  The property should be Comparable.
 *  
 * @author Willem Cazander
 * @version 1.0 Jan 11, 2006
 */
public class BeanPropertyComparator<T> implements Comparator<T> {
	
	/** The propery of the bean to compare. */
	private String property = null;
	/** The logger to log to. */
	private Logger logger = null;
	/** The ascending */
	private boolean ascending = true;
	
	private DefaultElementObjectPropertyValue helper = null;
	
	/**
	 * The constructor inits the logger.
	 */
	public BeanPropertyComparator() {
		logger = Logger.getLogger(BeanPropertyComparator.class.getName());
		helper = new DefaultElementObjectPropertyValue();
	}
	
	/**
	 * Creates an BeanPropertyComparator with an property
	 * @param property	The property to compare to.
	 */
	public BeanPropertyComparator(String property) {
		this();
		setProperty(property);
	}
	
	/**
	 * Creates an BeanPropertyComparator with an property
	 * @param property
	 */
	public BeanPropertyComparator(String property,boolean ascending) {
		this();
		setProperty(property);
		setAscending(ascending);
	}
	
	/**
	 * Compares 2 objects by there Comparable property.
	 * 
	 * @param o1 Object 1
	 * @param o2 Object 2
	 * @return the differce between the objects.
	 */
	public int compare(Object o1,Object o2) throws ClassCastException {
		
		Comparable<Object> c1 = getComparableProperty(o1);
		Comparable<Object> c2 = getComparableProperty(o2);

		if(c1==null && c2==null) {
			return 0;
		}		
		if(c1==null) {
			if(ascending) {
				return 1;
			} else {
				return -1;
			}
		}
		if(c2==null) {
			if(ascending) {
				return 1;
			} else {
				return -1;
			}
		}
		
		if(ascending) {
			return c1.compareTo(c2);
		} else {
			return c2.compareTo(c1);
		}
	}

	/**
	 * Returns the Comparable property of the object.
	 * @param object	The object to get the property field of.
	 * @return	Returns the Comparable casted object of the property field of the object.
	 * @throws ClassCastException
	 */
	@SuppressWarnings("unchecked")
	private Comparable<Object> getComparableProperty(Object object) throws ClassCastException {
		
		if(property==null) {
			throw new IllegalStateException("property is not set.");
		}
		Object result = null;
		try {
			result = helper.getProperty(object,property);
		} catch (Exception e) {
			logger.log(Level.WARNING,"property:"+property+" is not an property of object: "+object.getClass().getName(),e);
		}
		try {
			Comparable<Object> c = (Comparable<Object>)result;
			return c;
		} catch (ClassCastException e) {
			logger.log(Level.WARNING,"property:"+property+" is not Comparable",e);
			throw e;
		}
	}
	
	/**
	 * @return Returns the property.
	 */
	public String getProperty() {
		return property;
	}

	/**
	 * @param property The property to set.
	 */
	public void setProperty(String property) {
		if(property==null) {
			throw new NullPointerException("property may not be null");
		}
		this.property = property;
		logger.finest("property="+property);
	}

	/**
	 * @return Returns the ascending.
	 */
	public boolean isAscending() {
		return ascending;
	}

	/**
	 * @param ascending The ascending to set.
	 */
	public void setAscending(boolean ascending) {
		this.ascending = ascending;
		logger.finest("ascending="+ascending);
	}
}

