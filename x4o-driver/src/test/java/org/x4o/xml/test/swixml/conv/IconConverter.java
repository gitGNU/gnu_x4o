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
package	org.x4o.xml.test.swixml.conv;

import java.util.Locale;

import javax.swing.Icon;
import javax.swing.UIManager;

import org.x4o.xml.conv.AbstractStringObjectConverter;
import org.x4o.xml.conv.ObjectConverter;
import org.x4o.xml.conv.ObjectConverterException;

/**
 * IconConverter test converter.
 * 
 * @author Willem Cazander
 * @version 1.0 Aug 17, 2012
 */
public class IconConverter extends AbstractStringObjectConverter {
	
	private static final long serialVersionUID = 6729812931433525103L;
	
	public Class<?> getObjectClassTo() {
		return Icon.class;
	}
		
	public String convertStringBack(Object obj,Locale locale) throws ObjectConverterException {
		return ((Icon)obj).toString();
	}

	public Object convertStringTo(String str, Locale locale) throws ObjectConverterException {
		try {
			Icon icon = UIManager.getIcon("OptionPane.questionIcon");
			
			return icon;
		} catch (Exception e) {
			throw new ObjectConverterException(this,e.getMessage(),e);
		}
	}

	@Override
	public ObjectConverter clone() throws CloneNotSupportedException {
		IconConverter result = new IconConverter();
		result.converters=cloneConverters();
		return result;
	}
	
	
}