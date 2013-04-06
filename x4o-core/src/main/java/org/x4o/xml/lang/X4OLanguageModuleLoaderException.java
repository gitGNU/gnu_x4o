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

package	org.x4o.xml.lang;

import org.x4o.xml.element.ElementException;

/**
 * ElementNamespaceLoaderException holds ElementLanguageModuleLoader which created the exception.<br>
 * 
 * @author Willem Cazander
 * @version 1.0 Aug 29, 2008
 */
@SuppressWarnings("serial")
public class X4OLanguageModuleLoaderException extends ElementException {
	
	private X4OLanguageModuleLoader elementLanguageModuleLoader = null;
	
	/**
	 * Creates module loader exception. 
	 * @param elementLanguageModuleLoader	The loader module which creates this exception.
	 * @param message	The message of the exception.
	 */
	public X4OLanguageModuleLoaderException(X4OLanguageModuleLoader elementLanguageModuleLoader,String message) {
		super(message);
		this.elementLanguageModuleLoader=elementLanguageModuleLoader;
	}
	
	/**
	 * Creates module loader exception. 
	 * @param elementLanguageModuleLoader	The loader module which creates this exception.
	 * @param message	The message of the exception.
	 * @param exception	The root cause of the exception.
	 */
	public X4OLanguageModuleLoaderException(X4OLanguageModuleLoader elementLanguageModuleLoader,String message,Exception exception) {
		super(message,exception);
		this.elementLanguageModuleLoader=elementLanguageModuleLoader;
	}
	
	/**
	 * Returns the module loader which created the exception.
	 * @return Returns the module loader.
	 */
	public X4OLanguageModuleLoader getElementProvider() {
		return elementLanguageModuleLoader;
	}
}
