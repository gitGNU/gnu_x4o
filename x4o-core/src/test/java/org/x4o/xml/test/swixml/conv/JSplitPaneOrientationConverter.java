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

package	org.x4o.xml.test.swixml.conv;

import java.util.Locale;

import javax.swing.JSplitPane;

import org.x4o.xml.conv.AbstractStringObjectConverter;
import org.x4o.xml.conv.ObjectConverter;
import org.x4o.xml.conv.ObjectConverterException;

/**
 * JSplitPaneOrientationConverter test converter.
 * 
 * @author Willem Cazander
 * @version 1.0 Aug 17, 2012
 */
public class JSplitPaneOrientationConverter extends AbstractStringObjectConverter {
	
	private static final long serialVersionUID = 6729812931433525103L;
	
	public Class<?> getObjectClassTo() {
		return Integer.class;
	}
		
	public String convertStringBack(Object obj,Locale locale) throws ObjectConverterException {
		return ((Integer)obj).toString();
	}

	public Object convertStringTo(String str, Locale locale) throws ObjectConverterException {
		try {
			if ("HORIZONTAL".equals(str)) {
				return JSplitPane.HORIZONTAL_SPLIT;
			} else if ("VERTICAL".equals(str)) {
				return JSplitPane.VERTICAL_SPLIT;
			} else {
				throw new ObjectConverterException(this,"Unknown orientation: "+str);	
			}
		} catch (Exception e) {
			throw new ObjectConverterException(this,e.getMessage(),e);
		}
	}

	@Override
	public ObjectConverter clone() throws CloneNotSupportedException {
		JSplitPaneOrientationConverter result = new JSplitPaneOrientationConverter();
		result.converters=cloneConverters();
		return result;
	}
}
