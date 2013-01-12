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

package org.x4o.xml.core.config;

/**
 * X4OLanguageClassLoader is short hand for safe class loading.
 * 
 * @author Willem Cazander
 * @version 1.0 6 Aug 2012
 */
public class X4OLanguageClassLoader {
	
	/**
	 * Made X4OLanguageClassLoader have private constructor.
	 */
	private X4OLanguageClassLoader() {
	}
	
	/**
	 * Gets the thread classloader or the normal classloader.
	 * @return	Returns the ClassLoader.
	 */
	public static ClassLoader getClassLoader() {
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		if (cl == null) {
			cl = X4OLanguageClassLoader.class.getClassLoader();
		}
		return cl;
	}
	
	/**
	 * Loads a Class from the ContextClassLoader and if that is not set, then
	 * uses the class of the String className instance.
	 * 
	 * @param className	The class name to load
	 * @return	The loaded class
	 * @throws ClassNotFoundException	if class not loaded.
	 */
	public static Class<?> loadClass(String className) throws ClassNotFoundException {
		return getClassLoader().loadClass(className);
	}

	/**
	 * Creates new instance of clazz.
	 * @param clazz	The class to make object from.
	 * @return	The object of the clazz.
	 * @throws InstantiationException	When className has no default constructor.
	 * @throws IllegalAccessException	When class loading has security error.
	 */
	public static Object newInstance(Class<?> clazz) throws  InstantiationException, IllegalAccessException {
		return clazz.newInstance();
	}
	
	/**
	 * Creates new instance of className.
	 * @param className	The className to create object from.
	 * @return	The object of the className.
	 * @throws ClassNotFoundException	When className is not found.
	 * @throws InstantiationException	When className has no default constructor.
	 * @throws IllegalAccessException	When class loading has security error.
	 */
	public static Object newInstance(String className) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		return newInstance(loadClass(className));
	}
}
