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
package org.x4o.xml.lang.task.run;

/**
 * X4OTaskProperty stores the x4o language task property values for the task runner.
 * 
 * @author Willem Cazander
 * @version 1.0 Aug 30, 2013
 */
public class X4OTaskProperty {
	
	/** The key of the property. */
	private String key = null;
	
	/** The value of the property. */
	private String value = null;
	
	/**
	 * @return the key.
	 */
	public String getKey() {
		return key;
	}
	
	/**
	 * @param key the key to set.
	 */
	public void setKey(String key) {
		this.key = key;
	}
	
	/**
	 * @return the value.
	 */
	public String getValue() {
		return value;
	}
	
	/**
	 * @param value the value to set.
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
	/**
	 * Parsed line like 'key=value' into a X4OTaskProperty object.
	 * @param line	The text line to parse.
	 * @return	The filled X4OTaskProperty.
	 */
	static public X4OTaskProperty parseLine(String line) {
		if (line==null) {
			throw new NullPointerException("Can't parse null line.");
		}
		int idx = line.indexOf('=');
		if (idx<0) {
			throw new IllegalArgumentException("Can't parse line with '=' sign.");
		}
		if (idx==line.length()) {
			throw new IllegalArgumentException("Can't parse line empty value.");
		}
		String key = line.substring(0,idx);
		String value = line.substring(idx+1);
		X4OTaskProperty result = new X4OTaskProperty();
		result.setKey(key);
		result.setValue(value);
		return result;
	}
}
