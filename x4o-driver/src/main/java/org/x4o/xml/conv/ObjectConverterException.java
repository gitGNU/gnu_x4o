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

/**
 * ObjectConverterException is thrown by an ObjectConverter.
 *
 * @author Willem Cazander
 * @version 1.0 Jan 20, 2012
 */
public class ObjectConverterException extends Exception {
	
	private static final long serialVersionUID = 1L;
	private final ObjectConverter converter;

	/**
	 * Creates an ObjectConverterException.
	 * @param converter	The converter which has the exception.
	 * @param message	The exception message.
	 */
	public ObjectConverterException(ObjectConverter converter,String message) {
		super(message);
		this.converter=converter;
	}
	
	/**
	 * Creates an ObjectConverterException.
	 * @param converter	The converter which has the exception.
	 * @param message	The exception message.
	 * @param exception	The parent exception. 
	 */
	public ObjectConverterException(ObjectConverter converter,String message,Exception exception) {
		super(message,exception);
		this.converter=converter;
	}
	
	/**
	 * @return	Returns the ObjectConverter of this exception.
	 */
	public ObjectConverter getObjectConverter() {
		return converter;
	}
}
