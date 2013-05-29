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
package org.x4o.xml.eld.doc.api;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.x4o.xml.eld.doc.api.dom.ApiDoc;
import org.x4o.xml.eld.doc.api.dom.ApiDocNode;
import org.x4o.xml.eld.doc.api.dom.ApiDocNodeBody;
import org.x4o.xml.eld.doc.api.dom.ApiDocWriteEvent;
import org.x4o.xml.eld.doc.api.dom.ApiDocNodeWriter;
import org.xml.sax.SAXException;

/**
 * ApiDocNodeWriterBean wraps the ApiDocNodeWriterEvent to a single method of a bean.
 * 
 * @author Willem Cazander
 * @version 1.0 May 1, 2013
 */
public class ApiDocNodeWriterBean implements ApiDocNodeWriter {

	private ApiDocNodeBody nodeBody = null;
	private Integer nodeBodyOrder = null;
	private Object bean = null;
	private String method = null;
	private List<Class<?>> targetClasses = null;
	private String contentGroup = null;
	private String contentGroupType = null;
	
	public ApiDocNodeWriterBean() {
		targetClasses = new ArrayList<Class<?>>(5);
	}
	
	public ApiDocNodeWriterBean(ApiDocNodeBody nodeBody,Object bean,String method,Class<?>...classes) {
		this();
		this.nodeBody=nodeBody;
		this.bean=bean;
		this.method=method;
		if (classes!=null && classes.length>0) {
			targetClasses.addAll(Arrays.asList(classes));
		}
	}
	
	public static void addAnnotatedNodeContentWriters(ApiDoc doc,Object bean) {
		if (doc==null) {
			throw new NullPointerException("Can't add to null ApiDoc.");
		}
		if (bean==null) {
			throw new NullPointerException("Can't scan null bean.");
		}
		for (Method method:bean.getClass().getMethods()) {
			ApiDocNodeWriterMethod ammo = method.getAnnotation(ApiDocNodeWriterMethod.class);
			if (ammo==null) {
				continue;
			}
			ApiDocNodeWriterBean methodWriter = new ApiDocNodeWriterBean(ammo.nodeBody(), bean, method.getName(), ammo.targetClasses());
			if (ammo.nodeBodyOrder()!=-1) {
				methodWriter.setNodeBodyOrder(ammo.nodeBodyOrder());
			}
			if (!ammo.contentGroup().isEmpty()) {
				methodWriter.setContentGroup(ammo.contentGroup());
			}
			if (!ammo.contentGroupType().isEmpty()) {
				methodWriter.setContentGroupType(ammo.contentGroupType());
			}
			doc.addNodeBodyWriter(methodWriter);
		}
	}
	
	public void writeNodeContent(ApiDocWriteEvent<ApiDocNode> event) throws SAXException {
		Class<?> beanClass = bean.getClass();
		try {
			Method methodBean = beanClass.getMethod(method, new Class[]{ApiDocWriteEvent.class});
			methodBean.invoke(bean, new Object[]{event});
		} catch (Exception e) {
			throw new SAXException(e);
		}
	}
	
	public void addtargetClass(Class<?> targetClass) {
		targetClasses.add(targetClass);
	}
	
	public void removetargetClass(Class<?> targetClass) {
		targetClasses.remove(targetClasses);
	}
	
	public List<Class<?>> getTargetClasses() {
		return targetClasses;
	}
	
	/**
	 * @return the nodeBody
	 */
	public ApiDocNodeBody getNodeBody() {
		return nodeBody;
	}
	
	/**
	 * @param nodeBody the nodeBody to set
	 */
	public void setNodeBody(ApiDocNodeBody nodeBody) {
		this.nodeBody = nodeBody;
	}
	
	/**
	 * @return the bean
	 */
	public Object getBean() {
		return bean;
	}
	
	/**
	 * @param bean the bean to set
	 */
	public void setBean(Object bean) {
		this.bean = bean;
	}
	
	/**
	 * @return the method
	 */
	public String getMethod() {
		return method;
	}
	
	/**
	 * @param method the method to set
	 */
	public void setMethod(String method) {
		this.method = method;
	}
	
	/**
	 * @return the nodeBodyOrder
	 */
	public Integer getNodeBodyOrder() {
		return nodeBodyOrder;
	}
	
	/**
	 * @param nodeBodyOrder the nodeBodyOrder to set
	 */
	public void setNodeBodyOrder(Integer nodeBodyOrder) {
		this.nodeBodyOrder = nodeBodyOrder;
	}
	
	/**
	 * @return the contentGroup
	 */
	public String getContentGroup() {
		return contentGroup;
	}
	
	/**
	 * @param contentGroup the contentGroup to set
	 */
	public void setContentGroup(String contentGroup) {
		this.contentGroup = contentGroup;
	}
	
	/**
	 * @return the contentGroupType
	 */
	public String getContentGroupType() {
		return contentGroupType;
	}
	
	/**
	 * @param contentGroupType the contentGroupType to set
	 */
	public void setContentGroupType(String contentGroupType) {
		this.contentGroupType = contentGroupType;
	}
}