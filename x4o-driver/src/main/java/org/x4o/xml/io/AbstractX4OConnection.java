/*
 * Copyright (c) 2004-2013, Willem Cazander
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
package org.x4o.xml.io;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.x4o.xml.io.sax.ext.PropertyConfig;
import org.x4o.xml.lang.X4OLanguage;

/**
 * AbstractX4OConnection is the read/write interface for the classes.
 * 
 * @author Willem Cazander
 * @version 1.0 Apr 6, 2013
 */
public abstract class AbstractX4OConnection implements X4OConnection {
	
	private final X4OLanguage language;
	protected String phaseStop = null;
	protected List<String> phaseSkip = null;
	
	public AbstractX4OConnection(X4OLanguage language) {
		this.language=language;
		this.phaseSkip = new ArrayList<String>(2);
	}
	
	protected X4OLanguage getLanguage() {
		return language;
	}
	
	abstract PropertyConfig getPropertyConfig();
	
	/**
	 * Sets an X4O Language property.
	 * @param key	The key of the property to set.
	 * @param value	The vlue of the property to set.
	 */
	public void setProperty(String key,Object value) {
		getPropertyConfig().setProperty(key, value);
	}
	
	public Object getProperty(String key) {
		return getPropertyConfig().getProperty(key);
	}
	
	public Collection<String> getPropertyKeys() {
		return getPropertyConfig().getPropertyKeys();
	}
	
	public void setPhaseStop(String phaseId) {
		phaseStop = phaseId;
	}
	
	public void addPhaseSkip(String phaseId) {
		phaseSkip.add( phaseId );
	}
}
