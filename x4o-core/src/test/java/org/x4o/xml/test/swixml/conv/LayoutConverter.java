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

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.util.Locale;

import org.x4o.xml.conv.AbstractStringObjectConverter;
import org.x4o.xml.conv.ObjectConverter;
import org.x4o.xml.conv.ObjectConverterException;

/**
 * 
 * 
 * @author Willem Cazander
 * @version 1.0 Aug 17, 2012
 */
public class LayoutConverter extends AbstractStringObjectConverter {
	
	private static final long serialVersionUID = 6729812931433525103L;
	
	public Class<?> getObjectClassTo() {
		return LayoutManager.class;
	}
		
	public String convertStringBack(Object obj,Locale locale) throws ObjectConverterException {
		return ((LayoutManager)obj).toString();
	}

	public Object convertStringTo(String str, Locale locale) throws ObjectConverterException {
		try {
			if ("borderlayout".equals(str)) {
				return new BorderLayout();
			} else if (str.startsWith("FlowLayout")) {
				
				if (str.contains("RIGHT")) {
					return new FlowLayout(FlowLayout.RIGHT);
				} else if (str.contains("LEFT")) {
					return new FlowLayout(FlowLayout.LEFT);
				} else if (str.contains("CENTER")) {
					return new FlowLayout(FlowLayout.CENTER);
				} else if (str.contains("LEADING")) {
					return new FlowLayout(FlowLayout.LEADING);
				} else if (str.contains("TRAILING")) {
					return new FlowLayout(FlowLayout.TRAILING);
				} else {
					return new FlowLayout();
				}
			
			} else if (str.startsWith("GridLayout")) {
				
				int indexStart = str.indexOf('(');
				int indexMid = str.indexOf(',');
				int indexEnd = str.indexOf(')');
				if (indexStart>0 && indexMid>0 && indexEnd>0) {
					
					Integer rows = new Integer(str.substring(indexStart+1,indexMid));
					Integer cols = new Integer(str.substring(indexMid+1,indexEnd));
					
					return new GridLayout(rows,cols);
					
				} else {
					throw new ObjectConverterException(this,"Could not parse arguments: "+str);	
				}
				
			} else if (str.startsWith("GridBagLayout")) {
				
				return new GridBagLayout();
				
			} else {
				throw new ObjectConverterException(this,"Unknow layout requested: "+str);	
			}
		} catch (Exception e) {
			throw new ObjectConverterException(this,e.getMessage(),e);
		}
	}

	@Override
	public ObjectConverter clone() throws CloneNotSupportedException {
		LayoutConverter result = new LayoutConverter();
		result.converters=cloneConverters();
		return result;
	}
	
	
}