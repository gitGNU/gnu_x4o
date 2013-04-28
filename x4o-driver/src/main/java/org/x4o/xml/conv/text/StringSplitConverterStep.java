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
package org.x4o.xml.conv.text;

import org.x4o.xml.conv.ObjectConverter;

/**
 * StringSplitConverterStep.
 *
 * @author Willem Cazander
 * @version 1.0 Aug 23, 2012
 */
public class StringSplitConverterStep {

	private ObjectConverter objectConverter = null;
	private String fromMethod = null;
	private Integer fromOrder = null;
	private String toMethod = null;
	private Integer toOrder = null;
	
	/**
	 * @return the objectConverter
	 */
	public ObjectConverter getObjectConverter() {
		return objectConverter;
	}
	
	/**
	 * @param objectConverter the objectConverter to set
	 */
	public void setObjectConverter(ObjectConverter objectConverter) {
		this.objectConverter = objectConverter;
	}
	
	/**
	 * @return the fromMethod
	 */
	public String getFromMethod() {
		return fromMethod;
	}
	
	/**
	 * @param fromMethod the fromMethod to set
	 */
	public void setFromMethod(String fromMethod) {
		this.fromMethod = fromMethod;
	}
	
	/**
	 * @return the fromOrder
	 */
	public Integer getFromOrder() {
		return fromOrder;
	}
	
	/**
	 * @param fromOrder the fromOrder to set
	 */
	public void setFromOrder(Integer fromOrder) {
		this.fromOrder = fromOrder;
	}
	
	/**
	 * @return the toMethod
	 */
	public String getToMethod() {
		return toMethod;
	}
	
	/**
	 * @param toMethod the toMethod to set
	 */
	public void setToMethod(String toMethod) {
		this.toMethod = toMethod;
	}
	
	/**
	 * @return the toOrder
	 */
	public Integer getToOrder() {
		return toOrder;
	}
	
	/**
	 * @param toOrder the toOrder to set
	 */
	public void setToOrder(Integer toOrder) {
		this.toOrder = toOrder;
	}
}
