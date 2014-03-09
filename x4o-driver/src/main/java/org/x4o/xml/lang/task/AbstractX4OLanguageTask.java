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
package	org.x4o.xml.lang.task;

import java.util.List;

import org.x4o.xml.io.sax.ext.PropertyConfig;

/**
 * AbstractX4OLanguageTask holds the language task meta info.
 * 
 * @author Willem Cazander
 * @version 1.0 Aug 24, 2013
 */
public abstract class AbstractX4OLanguageTask implements X4OLanguageTask {

	private final String id;
	private final String name;
	private final String description;
	private final PropertyConfig propertyConfig;
	
	public AbstractX4OLanguageTask(String id,PropertyConfig propertyConfig) {
		this(id,id,id,propertyConfig);
	}
	
	public AbstractX4OLanguageTask(String id,String name,String description,PropertyConfig propertyConfig) {
		this.id=id;
		this.name=name;
		this.description=description;
		this.propertyConfig=propertyConfig;
	}
	
	protected abstract X4OLanguageTaskExecutor createTaskExecutorChecked(PropertyConfig config);
	
	/**
	 * @see org.x4o.xml.lang.task.X4OLanguageTask#createTaskExecutor(org.x4o.xml.io.sax.ext.PropertyConfig)
	 */
	public X4OLanguageTaskExecutor createTaskExecutor(PropertyConfig config) {
		return createTaskExecutorChecked(checkConfig(config));
	}
	
	private PropertyConfig checkConfig(PropertyConfig config) {
		List<String> keys = config.getPropertyKeysRequiredValues();
		if (keys.isEmpty()) {
			return config;
		}
		StringBuilder buf = new StringBuilder(100);
		buf.append("Error missing value(s) for key(s) {");
		for (int i=0;i<keys.size();i++) {
			buf.append('"');
			buf.append(keys.get(i));
			buf.append('"');
			if (i<keys.size()-1) {
				buf.append(',');
			}
			buf.append('}');
		}
		throw new IllegalArgumentException(buf.toString());
	}
	
	/**
	 * @see org.x4o.xml.lang.task.X4OLanguageTask#createTaskConfig()
	 */
	public PropertyConfig createTaskConfig() {
		return propertyConfig.clone();
	}
	
	/**
	 * @see org.x4o.xml.lang.task.X4OLanguageTask#getId()
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * @see org.x4o.xml.lang.task.X4OLanguageTask#getName()
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @see org.x4o.xml.lang.task.X4OLanguageTask#getDescription()
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * @see org.x4o.xml.lang.task.X4OLanguageTask#getTaskConfig()
	 */
	public PropertyConfig getTaskConfig() {
		return propertyConfig;
	}
}
