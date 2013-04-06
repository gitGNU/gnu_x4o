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

package org.x4o.xml.io;

import org.x4o.xml.core.config.X4OLanguageProperty;
import org.x4o.xml.element.ElementLanguage;

public abstract class AbstractX4OConnection implements X4OConnection {
	
	private ElementLanguage languageContext = null;
	
	public AbstractX4OConnection(ElementLanguage languageContext) {
		this.languageContext=languageContext;
	}
	
	protected ElementLanguage getLanguageContext() {
		return languageContext;
	}

	/**
	 * Sets an X4O Language property.
	 * @param key	The key of the property to set.
	 * @param value	The vlue of the property to set.
	 */
	public void setProperty(String key,Object value) {
		String keyLimits[] = getPropertyKeySet();
		for (int i=0;i<keyLimits.length;i++) {
			String keyLimit = keyLimits[i];
			if (keyLimit.equals(key)) {
				//if (phaseManager!=null) {
				//	TODO: throw new IllegalStateException("Can't set property after phaseManager is created.");
				//}
				languageContext.setLanguageProperty(X4OLanguageProperty.valueByUri(key), value);
				return;
			}
		}
		throw new IllegalArgumentException("Property with key: "+key+" is protected by key limit.");
	}
	
	/**
	 * Returns the value an X4O Language property.
	 * @param key	The key of the property to get the value for.
	 * @return	Returns null or the value of the property.
	 */
	public Object getProperty(String key) {
		return languageContext.getLanguageProperty(X4OLanguageProperty.valueByUri(key));
	}
}
