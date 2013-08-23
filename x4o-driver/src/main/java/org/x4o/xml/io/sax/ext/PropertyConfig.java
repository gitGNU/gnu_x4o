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

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * ContentConfig Defines checked config options.
 * 
 * @author Willem Cazander
 * @version 1.0 May 1, 2013
 */
public final class PropertyConfig {
	 
	private final Map<String,PropertyConfigItem> items;
	private final boolean readOnly;
	
	// TODO: move to ?
	public final static String X4O_PROPERTIES_PREFIX      = "http://language.x4o.org/xml/properties/";
	public final static String X4O_PROPERTIES_READER      = "reader/x4o/";
	public final static String X4O_PROPERTIES_READER_DTD  = "reader/dtd/";
	public final static String X4O_PROPERTIES_WRITER      = "writer/x4o/";
	public final static String X4O_PROPERTIES_WRITER_XML  = "writer/xml/";
	public final static String X4O_PROPERTIES_WRITER_XSD  = "writer/xsd/";
	public final static String X4O_PROPERTIES_WRITER_DTD  = "writer/dtd/";
	public final static String X4O_PROPERTIES_WRITER_HTML = "writer/html/";
	
	public PropertyConfig(String prefix,PropertyConfigItem...items) {
		this(false,null,prefix,items);
	}

	public PropertyConfig(PropertyConfig parentPropertyConfig,String prefix,PropertyConfigItem...items) {
		this(false,parentPropertyConfig,prefix,items);
	}
	
	public PropertyConfig(boolean readOnly,PropertyConfig parentPropertyConfig,String prefix,PropertyConfigItem...itemConfig) {
		if (prefix==null) {
			throw new NullPointerException("Can't create PropertyConfig with null prefix.");
		}
		this.readOnly=readOnly;
		Map<String,PropertyConfigItem> fillItems = new HashMap<String,PropertyConfigItem>(itemConfig.length);
		fillPropertyConfigItems(fillItems,appendSlashIfMissing(prefix),itemConfig);
		copyParentPropertyConfig(fillItems,parentPropertyConfig);
		if (fillItems.isEmpty()) {
			throw new IllegalArgumentException("Can't create PropertyConfig with zero PropertyConfigItems.");
		}
		for (String key:fillItems.keySet()) {
			fillItems.put(key,fillItems.get(key).clone());
		}
		items = Collections.unmodifiableMap(fillItems);
	}
	
	private final String appendSlashIfMissing(String prefix) {
		if (prefix.endsWith("/")==false) {
			prefix += "/";
		}
		return prefix;
	}
	
	private final void fillPropertyConfigItems(Map<String,PropertyConfigItem> fillItems,String prefix,PropertyConfigItem...itemConfig) {
		for (int i=0;i<itemConfig.length;i++) {
			PropertyConfigItem item = itemConfig[i];
			fillItems.put(prefix+item.getValueKey(),item);
		}
	}
	
	private final void copyParentPropertyConfig(Map<String,PropertyConfigItem> fillItems,PropertyConfig parentPropertyConfig) {
		if (parentPropertyConfig==null) {
			return;
		}
		for (String key:parentPropertyConfig.getPropertyKeys()) {
			fillItems.put(key, parentPropertyConfig.getPropertyConfigItem(key));
		}
	}
	
	public static final class PropertyConfigItem implements Cloneable {
		private final String valueKey;
		private final Class<?> valueType;
		private final Object valueDefault;
		private final boolean valueLock; // TODO: check if possible
		private Object value = null;
		
		public PropertyConfigItem(String valueKey,Class<?> valueType) {
			this(valueKey,valueType,null,false);
		}
		
		public PropertyConfigItem(String valueKey,Class<?> valueType,Object valueDefault) {
			this(valueKey,valueType,valueDefault,false);
		}
		
		private PropertyConfigItem(String valueKey,Class<?> valueType,Object valueDefault,boolean valueLock) {
			this.valueKey=valueKey;
			this.valueType=valueType;
			this.valueLock=valueLock;
			this.valueDefault=valueDefault;
		}
		
		/**
		 * @return the value key.
		 */
		public String getValueKey() {
			return valueKey;
		}
		
		/**
		 * @return the valueType
		 */
		public Class<?> getValueType() {
			return valueType;
		}
		
		/**
		 * @return the value default.
		 */
		public Object getValueDefault() {
			return valueDefault;
		}
		
		/**
		 * @return the valueLock
		 */
		public boolean isValueLock() {
			return valueLock;
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
		
		/**
		 * @see java.lang.Object#clone()
		 */
		@Override
		protected PropertyConfigItem clone() {
			PropertyConfigItem clone = new PropertyConfigItem(valueKey,valueType,valueDefault,valueLock);
			clone.setValue(getValue());
			return clone;
		}
	}
	
	private final PropertyConfigItem getPropertyConfigItem(String key) {
		if (key==null) {
			throw new NullPointerException("Can't search with null key.");
		}
		PropertyConfigItem item = items.get(key);
		if (item==null) {
			throw new IllegalArgumentException("No config item found for key: "+key);
		}
		return item;
	}
	
	public final Collection<String> getPropertyKeys() {
		return Collections.unmodifiableCollection(items.keySet());
	}
	
	public final void setProperty(String key,Object value) {
		if (readOnly) {
			throw new IllegalStateException("This property is readonly for key:"+key);
		}
		PropertyConfigItem item = getPropertyConfigItem(key);
		item.setValue(value);
	}
	
	public final Object getProperty(String key) {
		PropertyConfigItem item = getPropertyConfigItem(key);
		Object value = item.getValue();
		if (value==null) {
			value = item.getValueDefault();
		}
		return value;
	}
	
	public final Boolean getPropertyBoolean(String key) {
		Object value = getProperty(key);
		if (value instanceof Boolean) {
			return (Boolean)value;
		}
		if (value==null) {
			return null;
		}
		throw new IllegalStateException("Wrong value type: "+value.getClass()+" for key: "+key);
	}
	
	public final Integer getPropertyInteger(String key) {
		Object value = getProperty(key);
		if (value instanceof Integer) {
			return (Integer)value;
		}
		if (value==null) {
			return null;
		}
		throw new IllegalStateException("Wrong value type: "+value.getClass()+" for key: "+key);
	}
	
	public final String getPropertyString(String key) {
		Object value = getProperty(key);
		if (value instanceof String) {
			return (String)value;
		}
		if (value==null) {
			return null;
		}
		throw new IllegalStateException("Wrong value type: "+value.getClass()+" for key: "+key);
	}
	
	public final void copyParentProperties(PropertyConfig config) {
		for (String key:getPropertyKeys()) {
			Object value = config.getProperty(key);
			if (value==null) {
				continue;
			}
			setProperty(key, value);
		}
	}
}
