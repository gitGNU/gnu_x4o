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
package	org.x4o.xml.io.sax.ext;

import java.util.HashMap;
import java.util.Map;

/**
 * ContentConfig Defines checked config options.
 * 
 * @author Willem Cazander
 * @version 1.0 May 1, 2013
 */
public final class ContentConfig {
	
	private final ContentConfigItem[] items;
	private final Map<String,Integer> itemKeys;
	
	public ContentConfig(ContentConfigItem...items) {
		this.items=items;
		itemKeys = new HashMap<String,Integer>(items.length);
		for (int i=0;i<items.length;i++) {
			ContentConfigItem item = items[i];
			itemKeys.put(item.getKey(),i);
		}
	}
	
	public static final class ContentConfigItem {
		private String  key = null;
		private Class<?> valueType = null;
		private Object defaultValue = null;
		private Object value = null;
		
		public ContentConfigItem(String key,Class<?> valueType) {
			this(key,valueType,null);
		}
		public ContentConfigItem(String key,Class<?> valueType,Object defaultValue) {
			setKey(key);
			setValueType(valueType);
			setDefaultValue(defaultValue);
		}
		/**
		 * @return the key
		 */
		public String getKey() {
			return key;
		}
		/**
		 * @param key the key to set
		 */
		public void setKey(String key) {
			this.key = key;
		}
		/**
		 * @return the valueType
		 */
		public Class<?> getValueType() {
			return valueType;
		}
		/**
		 * @param valueType the valueType to set
		 */
		public void setValueType(Class<?> valueType) {
			this.valueType = valueType;
		}
		/**
		 * @return the defaultValue
		 */
		public Object getDefaultValue() {
			return defaultValue;
		}
		/**
		 * @param defaultValue the defaultValue to set
		 */
		public void setDefaultValue(Object defaultValue) {
			this.defaultValue = defaultValue;
		}
		/**
		 * @return the value
		 */
		public Object getValue() {
			return value;
		}
		/**
		 * @param value the value to set
		 */
		public void setValue(Object value) {
			this.value = value;
		}
	}
	
	private final ContentConfigItem getContentConfigItem(String key) {
		Integer keyIdx = itemKeys.get(key);
		if (keyIdx==null) {
			throw new IllegalArgumentException("Could not find config item for: "+key);	
		}
		ContentConfigItem item = items[keyIdx];
		return item;
	}
	
	public final void setProperty(String key,Object value) {
		ContentConfigItem item = getContentConfigItem(key);
		item.setValue(value);
	}
	
	public final Object getProperty(String key) {
		ContentConfigItem item = getContentConfigItem(key);
		return item.getValue();
	}
	
	public final boolean getPropertyBoolean(String key) {
		ContentConfigItem item = getContentConfigItem(key);
		Object value = item.getValue();
		if (value instanceof Boolean) {
			return (Boolean)value;
		}
		
		return (Boolean)item.getDefaultValue();
	}
	
	public final int getPropertyInteger(String key) {
		ContentConfigItem item = getContentConfigItem(key);
		Object value = item.getValue();
		if (value instanceof Integer) {
			return (Integer)value;
		}
		return (Integer)item.getDefaultValue();
	}
	
	public final String getPropertyString(String key) {
		ContentConfigItem item = getContentConfigItem(key);
		Object value = item.getValue();
		if (value instanceof String) {
			return (String)value;
		}
		return (String)item.getDefaultValue();
	}
}
