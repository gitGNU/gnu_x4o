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

import java.util.Iterator;
import java.util.Map;

import javax.el.ELContext;
import javax.el.ELResolver;
import javax.el.MapELResolver;


/**
 * X4OELResolver simple EL resolver.
 * 
 * @author Willem Cazander
 * @version 1.0 Sep 14, 2010
 */
public class X4OELResolver extends ELResolver {
	
	private ELResolver delegate = null;
	private Map<Object,Object> objectStore = null;
	
	public X4OELResolver(Map<Object, Object> objectStore) {
		this.objectStore = objectStore;
		delegate = new MapELResolver();
	}
	
	private Object checkBase(Object base) {
		if (base==null) {
			return objectStore;
		}
		return base;
	}
	
	@Override
	public Object getValue(ELContext context, Object base, Object property) {
		base = checkBase(base);
		return delegate.getValue(context, base, property);
	}
	
	@Override
	public Class<?> getCommonPropertyType(ELContext context, Object base) {
		base = checkBase(base);
		return delegate.getCommonPropertyType(context, base);
	}

	@Override
	@SuppressWarnings("rawtypes")
	public Iterator getFeatureDescriptors(ELContext context,Object base) {
		base = checkBase(base);
		return delegate.getFeatureDescriptors(context, base);
	}

	@Override
	public Class<?> getType(ELContext context, Object base, Object property) {
		base = checkBase(base);
		return delegate.getType(context, base, property);
	}
	
	@Override
	public boolean isReadOnly(ELContext context, Object base, Object property) {
		base = checkBase(base);
		return delegate.isReadOnly(context, base, property);
	}

	@Override
	public void setValue(ELContext context, Object base, Object property, Object value) {
		base = checkBase(base);
		delegate.setValue(context, base, property, value);
	}
}
