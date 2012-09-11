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

package org.x4o.xml.impl;

import java.util.Comparator;

import org.x4o.xml.element.ElementAttributeHandler;


/**
 * The DefaultGlobalAttributeHandlerComparator.<br>
 * This Comparator compares the NextAttribute names with the attributeName of the ElementAttributeHandlers.<br>
 * 
 * @author Willem Cazander
 * @version 1.0 Feb 14, 2007
 */
public class DefaultGlobalAttributeHandlerComparator implements Comparator<ElementAttributeHandler> {

	/**
	 * @param	e1	The first ElementAttributeHandler to compare.
	 * @param	e2	The second ElementAttributeHandler to compare.
	 * @return	0 is same.
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(ElementAttributeHandler e1, ElementAttributeHandler e2) {

		for (String param:e1.getNextAttributes()) {
			if(param.equals(e2.getAttributeName())) {
				return -1;
			}
		}
		for (String param:e2.getNextAttributes()) {
			if(param.equals(e1.getAttributeName())) {
				return 1;
			}
		}
		return 0;
	}
}
