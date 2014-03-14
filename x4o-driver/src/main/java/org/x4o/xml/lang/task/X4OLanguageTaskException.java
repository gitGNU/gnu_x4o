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
package	org.x4o.xml.lang.task;

import org.x4o.xml.io.sax.ext.PropertyConfig;

/**
 * X4OLanguageTaskException addes the property config to the exception.
 * 
 * @author Willem Cazander
 * @version 1.0 Aug 26, 2013
 */
public class X4OLanguageTaskException extends Exception {
	
	private static final long serialVersionUID = 8490969221732950292L;
	private PropertyConfig propertyConfig = null;
	
	public X4OLanguageTaskException(PropertyConfig propertyConfig,String message) {
		super(message);
		this.propertyConfig=propertyConfig;
	}
	
	public X4OLanguageTaskException(PropertyConfig propertyConfig,String message,Exception exception) {
		super(message,exception);
		this.propertyConfig=propertyConfig;
	}

	public PropertyConfig getPropertyConfig() {
		return propertyConfig;
	}
}
