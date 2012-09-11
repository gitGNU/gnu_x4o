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

package org.x4o.xml.impl.el;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import javax.el.FunctionMapper;

/**
 * X4OELFunctionMapper simple EL function mapper.
 * 
 * @author Willem Cazander
 * @version 1.0 Sep 14, 2010
 */
public class X4OELFunctionMapper extends FunctionMapper {
	private Map<String,Method> functionMap = null;
	
	public X4OELFunctionMapper() {
		functionMap = new HashMap<String,Method>(50);
	}
	
	@Override
	public Method resolveFunction(String prefix, String localName) {
		String key = prefix + ":" + localName;
		return functionMap.get(key);
	}

	public void addFunction(String prefix, String localName, Method method) {
		if(prefix==null || localName==null || method==null) {
			throw new NullPointerException();
		}
		int modifiers = method.getModifiers();
		if(!Modifier.isPublic(modifiers)) {
			throw new IllegalArgumentException("method not public");
		}
		if(!Modifier.isStatic(modifiers)) {
			throw new IllegalArgumentException("method not static");
		}
		Class<?> retType = method.getReturnType();
		if(retType == Void.TYPE) {
			throw new IllegalArgumentException("method returns void");
		}
		
		String key = prefix + ":" + localName;
		functionMap.put(key, method);
	}
}