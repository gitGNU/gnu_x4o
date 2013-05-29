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
package org.x4o.xml.io.sax.ext;

import org.xml.sax.Attributes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Maps an SAX attributes set to an Map interface.
 * 
 * @author Willem Cazander
 * @version 1.0 Apr 17, 2005
 * @param <K> The key class.
 * @param <V> The value class.
 */
public class AttributeMap<K,V> implements Map<K,V> {
	
	/** stores the SAX attributes set */
	private Attributes attributes = null;

	/** stores the namespace uri for this attributes */
	private String uri = null;

	/**
	 * Constuctes an new AttributeMap.
	 * 
	 * @param attributes The data backend of this map.
	 */
	public AttributeMap(Attributes attributes) {
		setAttributes(attributes);
	}

	/**
	 * Constructes an new AttributesMap.
	 * 
	 * @param attributes	The data backed of this map.
	 * @param uri			The namespace of these attributes.
	 */
	public AttributeMap(Attributes attributes, String uri) {
		setAttributes(attributes);
		setNameSpace(uri);
	}

	/**
	 * Sets the data backend of this map.
	 * 
	 * @param attributes	The Attributes to be used as data backend.
	 */
	public void setAttributes(Attributes attributes) {
		this.attributes = attributes;
	}

	/**
	 * Return the data backend of this map.
	 * 
	 * @return The data backend of attributes
	 */
	public Attributes getAttributes() {
		return attributes;
	}

	/**
	 * Sets the namespace uri, when set to null it is disabled(default).
	 * 
	 * @param uri	The namespace uri for when parsing when an certain namespace is required.
	 */
	public void setNameSpace(String uri) {
		this.uri = uri;
	}

	/**
	 * Returns the namespace used for these attributes.
	 * 
	 * @return The namespace uri for the attributes.
	 */
	public String getNameSpace() {
		return uri;
	}

	// --------------- inner util functions.

	/**
	 * Gets the local of full name by index.
	 * 
	 * @param index	The index of the attribute.
	 * @return The name of the attribute.
	 */
	private String getName(int index) {
		if (uri == null) {
			return attributes.getLocalName(index);
		}
		return attributes.getQName(index);
	}

	/**
	 * Gets the value of the attributes. uses the uri if not null.
	 * 
	 * @param name The name of the attributes.
	 * @return The value of the attribute.
	 */
	private String getValue(String name) {
		if (name == null) {
			return null;
		}
		if (uri != null) {
			return attributes.getValue(uri, name);
		}
		return attributes.getValue(name);
	}

	/**
	 * Gets the attribute value.
	 * @param key The name of the attribute.
	 * @return The value of the attribute.
	 */
	private String getValue(Object key) {
		if (key == null) {
			return null;
		}
		return getValue(key.toString());
	}

	// ------------------------------ MAP intreface

	/**
	 * Returns the size of the map.
	 * 
	 * @return The size of the map.
	 */
	public int size() {
		return attributes.getLength();
	}

	/**
	 * Checks if there are attributes in this map.
	 * 
	 * @return True if there are attributes entrys in this map.
	 */
	public boolean isEmpty() {
		if (size() == 0) {
			return true;
		}
		return false;
	}

	/**
	 * Checks if there is an attributes with an certain key.
	 * 
	 * @param key	The name of an attribute.
	 * @return True if the attributes excist.
	 */
	public boolean containsKey(Object key) {
		if (getValue(key) != null) {
			return true;
		}
		return false;
	}

	/**
	 * Checks if there is an attributes with an value.
	 * 
	 * @param value	The value to check.
	 * @return True if an attributes has this value.
	 */
	public boolean containsValue(Object value) {
		for (int i = 0; i < size(); i++) {
			if (attributes.getValue(i).equals(value)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns The value of an attribute.
	 * 
	 * @param key	The name of the attribute.
	 * @return The value of the attribute.
	 */
	@SuppressWarnings("unchecked")
	public V get(Object key) {
		return (V)getValue(key);
	}

	/**
	 * Function not implements. because we can't add to the attributes.
	 * 
	 * @param key	ignored.
	 * @param value	ignored.
	 * @return always null
	 */
	public V put(K key, V value) {
		return null; // we can't add.
	}

	/**
	 * Function not implements. because we can't remove to the attributes.
	 * 
	 * @param key	ignored.
	 * @return always null
	 */
	public V remove(Object key) {
		return null ;// we can't remove
	}

	/**
	 * Function not implements. because we can't add to the attributes.
	 * 
	 * @param t	ignored.
	 */
	@SuppressWarnings("rawtypes")
	public void putAll(Map t) {
		// we can't add.
	}

	/**
	 * Function not implements. because we can't clear the attributes.
	 */
	public void clear() {
		// we can't clear
	}

	/**
	 * Returns a new Set whith the names of the attributes.
	 * 
	 * @return An set with attributes names.
	 */
	@SuppressWarnings("unchecked")
	public Set<K> keySet() {
		HashSet<K> result = new HashSet<K>();
		for (int i = 0; i < size(); i++) {
			result.add((K)getName(i));
		}
		return result;
	}

	/**
	 * Returns an Collection of the values in the attributes list.
	 * 
	 * @return An Collection of values.
	 */
	@SuppressWarnings("unchecked")
	public Collection<V> values() {
		ArrayList<V> result = new ArrayList<V>();
		for (int i = 0; i < size(); i++) {
			result.add((V)attributes.getValue(i));
		}
		return result;
	}

	/**
	 * Returns an Set made of Map.Entry. note: This Map.Entry is not refenced by
	 * the attribute list. so changes to not reflect to this map object.
	 * 
	 * @see java.util.Map#entrySet()
	 * @return An entry set.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Set entrySet() {
		HashSet result = new HashSet();
		for (int i = 0; i < size(); i++) {
			Map.Entry mapEntry = new AttributeMapEntry(getName(i));
			mapEntry.setValue(attributes.getValue(i));
			result.add(mapEntry);
		}
		return result;
	}

	/**
	 * Compares the SAX attribute list.
	 * 
	 * @param o	The Object to compare with.
	 * @return True if the object are equal.
	 */
	public boolean equals(Object o) {
		return attributes.equals(o); // compare to attributes
	}

	/**
	 * returns the hashCode of the attribute list.
	 * 
	 * @return An hashCode.
	 */
	public int hashCode() {
		return attributes.hashCode();
	}

	/**
	 * An innerclass used by entrySet().
	 */
	@SuppressWarnings("rawtypes")
	class AttributeMapEntry implements Map.Entry {
		
		private Object key = null;
		private Object value = null;
		
		/**
		 * Creates AttributeMapEntry with key object.
		 * @param key	The key.
		 */
		protected AttributeMapEntry(Object key) {
			this.key = key;
		}
		
		/**
		 * @return Returns the key.
		 */
		public Object getKey() {
			return key;
		}
		
		/**
		 * Sets the value of this Map.Entry.
		 * @param value The value to set.
		 * @return The old value.
		 */
		public Object setValue(Object value) {
			Object result = this.value;
			this.value = value;
			return result;
		}
		
		/**
		 * @return The value of this Map.Entry.
		 */
		public Object getValue() {
			return value;
		}
		
		/**
		 * @param o Check if o is equal.
		 * @return True if key and value of Map.Entry are equal.
		 */
		public boolean equals(Object o) {
			if (o instanceof Map.Entry) {
				Map.Entry mapEntry = (Map.Entry) o;
				if (mapEntry.getKey().equals(key) & mapEntry.getValue().equals(value)) {
					return true;
				}
			}
			return false;
		}
	}
}
