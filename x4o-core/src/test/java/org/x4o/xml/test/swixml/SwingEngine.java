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

package org.x4o.xml.test.swixml;

import java.awt.Component;
import java.lang.reflect.Field;

import javax.swing.Action;

import org.x4o.xml.test.swixml.SwiXmlParser.SwiXmlVersion;

/**
 * SwingEngine demo.
 * 
 * @author Willem Cazander
 * @version 1.0 Aug 15, 2012
 */
public class SwingEngine {

	private Object uiHandler;
	private Component rootComponent = null;
	
	public SwingEngine(Object uiHandler) {
		this.uiHandler=uiHandler;
	}
	
	public Action getUIActionByName(String name) {
		if (name==null) {
			return null;
		}
		if (uiHandler==null) {
			return null;
		}
		try {
			for (Field f:uiHandler.getClass().getFields()) {
				if (name.equals(f.getName())) {
					Object value = f.get(uiHandler);
					if (value instanceof Action) {
						return (Action)value;
					} else {
						return null;
					}
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return null;
	}
	
	public Component render(String resource,SwiXmlVersion xmlVersion) {
		SwiXmlParser parser = new SwiXmlParser(this,xmlVersion);
		try {
			parser.parseResource(resource);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		rootComponent = parser.getRootComponent();
		return getRootComponent();
	}
	
	public Component getRootComponent() {
		return rootComponent;
	}
}
