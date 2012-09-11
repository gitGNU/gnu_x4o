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

package	org.x4o.xml.core.config;

/**
 * Is throw when there is en Exception within an Element.
 * 
 * @author Willem Cazander
 * @version 1.0 Oct 28, 2009
 */
public class X4OLanguageLoaderException extends Exception {
	
	/** The serial version uid */
	static final long serialVersionUID = 10L;
	
	/**
	 * Constructs an X4OLanguageLoaderException without a detail message.
	 */
	public X4OLanguageLoaderException() {
		super();
	}

	/**
	 * Constructs an X4OLanguageLoaderException with a detail message.
	 * @param	message	The message of this Exception
	 */
	public X4OLanguageLoaderException(String message) {
		super(message);
	}
	
	/**
	 * Creates an X4OLanguageLoaderException from a parent exception.
	 * @param e	The exception.
	 */
	public X4OLanguageLoaderException(Exception e) {
		super(e);
	}
	
	/**
	 * Constructs an X4OLanguageLoaderException with a detail message.
	 * @param	message	The message of this Exception
	 * @param e	The exception.
	 */
	public X4OLanguageLoaderException(String message,Exception e) {
		super(message,e);
	}
}
