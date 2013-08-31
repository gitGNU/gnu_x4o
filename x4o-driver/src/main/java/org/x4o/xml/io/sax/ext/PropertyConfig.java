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

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ContentConfig Defines checked config options.
 * 
 * @author Willem Cazander
 * @version 1.0 May 1, 2013
 */
public final class PropertyConfig implements Cloneable {
	
	private final Map<String,PropertyConfigItem> items;
	private final boolean readOnly;
	private final String keyPrefix;
	
	// TODO: move to ?
//	public final static String X4O_PROPERTIES_PREFIX      = "http://language.x4o.org/xml/properties/";
	public final static String X4O_PROPERTIES_PREFIX      = "http://x4o.org/properties/";
	public final static String X4O_PROPERTIES_READER      = "reader/x4o/";
	//public final static String X4O_PROPERTIES_READER_DTD  = "reader/dtd/";
	public final static String X4O_PROPERTIES_WRITER      = "writer/x4o/";
	public final static String X4O_PROPERTIES_WRITER_XML  = "content/";
	public final static String X4O_PROPERTIES_ELD_XSD  = "eld-xsd/";
	public final static String X4O_PROPERTIES_ELD_DOC  = "eld-doc/";
	//public final static String X4O_PROPERTIES_WRITER_DTD  = "writer/dtd/";
	//public final static String X4O_PROPERTIES_WRITER_HTML = "writer/html/";
	
	public PropertyConfig(String keyPrefix,PropertyConfigItem...items) {
		this(false,null,keyPrefix,items);
	}

	public PropertyConfig(PropertyConfig parentPropertyConfig,String keyPrefix,PropertyConfigItem...items) {
		this(false,parentPropertyConfig,keyPrefix,items);
	}
	
	public PropertyConfig(boolean readOnly,PropertyConfig parentPropertyConfig,String keyPrefix,PropertyConfigItem...itemConfig) {
		if (keyPrefix==null) {
			throw new NullPointerException("Can't create PropertyConfig with null keyPrefix.");
		}
		this.readOnly=readOnly;
		this.keyPrefix=appendSlashIfMissing(keyPrefix);
		Map<String,PropertyConfigItem> fillItems = new HashMap<String,PropertyConfigItem>(itemConfig.length);
		fillPropertyConfigItems(fillItems,itemConfig);
		copyParentPropertyConfig(fillItems,parentPropertyConfig);
		if (fillItems.isEmpty()) {
			throw new IllegalArgumentException("Can't create PropertyConfig with zero PropertyConfigItems.");
		}
		for (String key:fillItems.keySet()) {
			if (!key.startsWith(X4O_PROPERTIES_PREFIX)) {
				throw new IllegalArgumentException("Illegal key missing prefix; "+key);
			}
			fillItems.put(key,fillItems.get(key).clone());
		}
		items = Collections.unmodifiableMap(fillItems);
	}
	
	private final String appendSlashIfMissing(String keyPrefix) {
		if (keyPrefix.endsWith("/")==false) {
			keyPrefix += "/";
		}
		return keyPrefix;
	}
	
	private final void fillPropertyConfigItems(Map<String,PropertyConfigItem> fillItems,PropertyConfigItem...itemConfig) {
		for (int i=0;i<itemConfig.length;i++) {
			PropertyConfigItem item = itemConfig[i];
			fillItems.put(item.getValueKey(),item);
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
		private final boolean valueRequired;
		private final String valueKey;
		private final Class<?> valueType;
		private final Object valueDefault;
		private Object value = null;
		
		public PropertyConfigItem(boolean valueRequired,String valueKey,Class<?> valueType) {
			this(valueRequired,valueKey,valueType,null);
		}
		
		public PropertyConfigItem(String valueKey,Class<?> valueType) {
			this(false,valueKey,valueType,null);
		}
		
		public PropertyConfigItem(String valueKey,Class<?> valueType,Object valueDefault) {
			this(false,valueKey,valueType,valueDefault); // with default then value can not be required.
		}
		
		private PropertyConfigItem(boolean valueRequired,String valueKey,Class<?> valueType,Object valueDefault) {
			if (valueKey==null) {
				throw new NullPointerException("Can't create PropertyConfigItem with null valueKey.");
			}
			if (valueType==null) {
				throw new NullPointerException("Can't create PropertyConfigItem with null valueType.");
			}
			this.valueRequired=valueRequired;
			this.valueKey=valueKey;
			this.valueType=valueType;
			this.valueDefault=valueDefault;
		}
		
		/**
		 * @return the value key.
		 */
		public String getValueKey() {
			return valueKey;
		}
		
		/**
		 * @return the valueType.
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
		 * @return the valueRequired.
		 */
		public boolean isValueRequired() {
			return valueRequired;
		}
		
		/**
		 * @return the value.
		 */
		public Object getValue() {
			return value;
		}
		
		/**
		 * @param value the value to set.
		 */
		public void setValue(Object value) {
			this.value = value;
		}
		
		/**
		 * Clones all the fields into the new PropertyConfigItem.
		 * @see java.lang.Object#clone()
		 */
		@Override
		protected PropertyConfigItem clone() {
			PropertyConfigItem clone = new PropertyConfigItem(valueRequired,valueKey,valueType,valueDefault);
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
	
	public final String getKeyPrefix() {
		return keyPrefix;
	}
	
	public final boolean isPropertyRequired(String key) {
		return getPropertyKeysRequired().contains(key);
	}
	
	public final Collection<String> getPropertyKeysRequired() {
		return findPropertyKeysRequired(false);
	}
	
	public final Collection<String> getPropertyKeysRequiredValues() {
		return findPropertyKeysRequired(true);
	}
	
	private final Collection<String> findPropertyKeysRequired(boolean checkValue) {
		List<String> result = new ArrayList<String>(10);
		for (String key:getPropertyKeys()) {
			PropertyConfigItem item = getPropertyConfigItem(key);
			if (!item.isValueRequired()) {
				continue;
			}
			if (!checkValue) {
				result.add(item.getValueKey());
			} else if (item.getValue()==null && item.getValueDefault()==null) {
				result.add(item.getValueKey());
			}
		}
		return result;
	}
	
	public final List<String> getPropertyKeys() {
		List<String> result = new ArrayList<String>(items.keySet());
		Collections.sort(result);
		return Collections.unmodifiableList(result);
	}
	
	public final void setProperty(String key,Object value) {
		if (readOnly) {
			throw new IllegalStateException("This property is readonly for key:"+key);
		}
		PropertyConfigItem item = getPropertyConfigItem(key);
		item.setValue(value);
	}
	
	public final Object getPropertyDefault(String key) {
		PropertyConfigItem item = getPropertyConfigItem(key);
		return item.getValueDefault();
	}
	
	public final Object getProperty(String key) {
		PropertyConfigItem item = getPropertyConfigItem(key);
		Object value = item.getValue();
		if (value==null) {
			value = item.getValueDefault();
		}
		return value;
	}
	
	public final Class<?> getPropertyType(String key) {
		PropertyConfigItem item = getPropertyConfigItem(key);
		return item.getValueType();
	}
	
	public final File getPropertyFile(String key) {
		Object value = getProperty(key);
		if (value instanceof File) {
			return (File)value;
		}
		if (value==null) {
			return null;
		}
		throw new IllegalStateException("Wrong value type: "+value.getClass()+" for key: "+key);
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

	@SuppressWarnings("unchecked")
	public final List<String> getPropertyList(String key) {
		Object value = getProperty(key);
		if (value instanceof List) {
			return (List<String>)value;
		}
		if (value==null) {
			return null;
		}
		throw new IllegalStateException("Wrong value type: "+value.getClass()+" for key: "+key);
	}
	
	@SuppressWarnings("unchecked")
	public final Map<String,String> getPropertyMap(String key) {
		Object value = getProperty(key);
		if (value instanceof Map) {
			return (Map<String,String>)value;
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
	
	// TODO: better name this
	public final String getPropertyStringOrValue(String key,String value) {
		String propertyValue = getPropertyString(key);
		if (propertyValue==null) {
			return value;
		} else {
			return propertyValue;
		}
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
	
	@SuppressWarnings("unchecked")
	public final void setPropertyParsedValue(String key,String value) {
		Class<?> valueType = getPropertyType(key);
		//System.out.println("key: "+key+" value: "+value+" type: "+valueType);
		if (String.class.equals(valueType)) {
			setProperty(key, value);
			return;
		}
		if (Boolean.class.equals(valueType)) {
			setProperty(key, new Boolean(value));
			return;
		}
		if (Integer.class.equals(valueType)) {
			setProperty(key, new Integer(value));
			return;
		}
		if (Double.class.equals(valueType)) {
			setProperty(key, new Double(value));
			return;
		}
		if (Float.class.equals(valueType)) {
			setProperty(key, new Float(value));
			return;
		}
		if (File.class.equals(valueType)) {
			setProperty(key, new File(value));
			return;
		}
		if (List.class.equals(valueType)) {
			String[] listValues = value.split(",");
			List<String> result = (List<String>)getProperty(key);
			if (result==null) {
				result = new ArrayList<String>(10);
				setProperty(key, result);
			}
			for (String listValue:listValues) {
				result.add(listValue);
			}
			return;
		}
		if (Map.class.equals(valueType)) {
			String[] listValues = value.split(",");
			Map<String,String> result = (Map<String,String>)getProperty(key);
			if (result==null) {
				result = new HashMap<String,String>(10);
				setProperty(key, result);
			}
			if (listValues.length!=2) {
				System.err.println("Could not parse map value: "+value);
				System.exit(1);
				return;
			}
			String mKey = listValues[0];
			String mValue = listValues[1];
			result.put(mKey, mValue);
			return;
		}
	}
	
	/**
	 * Clones all the properties into the new PropertyConfig.
	 * @see java.lang.Object#clone()
	 */
	@Override
	public PropertyConfig clone() {
		PropertyConfig clone = new PropertyConfig(this,this.keyPrefix);
		clone.copyParentProperties(this);
		return clone;
	}
}
