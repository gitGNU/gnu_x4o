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

package org.x4o.xml.lang;

import java.util.Map;
import java.util.HashMap;
import java.util.logging.Logger;

import javax.el.ELContext;
import javax.el.ExpressionFactory;

import org.x4o.xml.element.Element;
import org.x4o.xml.element.ElementAttributeValueParser;
import org.x4o.xml.element.ElementObjectPropertyValue;
import org.x4o.xml.io.sax.X4ODebugWriter;
import org.x4o.xml.lang.phase.X4OPhase;

/**
 * An AbstractElementLanguage.
 * 
 * @author Willem Cazander
 * @version 1.0 Aug 20, 2005
 */
public abstract class AbstractX4OLanguageContext implements X4OLanguageContextLocal {

	private Logger logger = null;
	private X4OLanguage language = null;
	private ExpressionFactory expressionFactory = null;
	private ELContext eLContext = null;
	private ElementAttributeValueParser elementAttributeValueParser = null;
	private ElementObjectPropertyValue elementObjectPropertyValue = null;
	private X4OPhase currentX4OPhase = null;
	private Map<Element, X4OPhase> dirtyElements = null;
	private Element rootElement = null;
	private X4ODebugWriter debugWriter;
	private Map<String,Object> languageProperties;

	/**
	 * Creates a new empty ElementLanguage.
	 */
	public AbstractX4OLanguageContext(X4OLanguage language,String languageVersion) {
		if (language==null) {
			throw new NullPointerException("language may not be null");
		}
		if (languageVersion==null) {
			throw new NullPointerException("languageVersion may not be null");
		}
		if (languageVersion.length()==0) {
			throw new IllegalArgumentException("languageVersion may not be empty");
		}
		logger = Logger.getLogger(AbstractX4OLanguageContext.class.getName());
		logger.finest("Creating new ParsingContext");
		this.language=language;
		dirtyElements = new HashMap<Element, X4OPhase>(40);
		languageProperties = new HashMap<String,Object>(20);
		languageProperties.put(X4OLanguageProperty.LANGUAGE_NAME.toUri(), language.getLanguageName());
		languageProperties.put(X4OLanguageProperty.LANGUAGE_VERSION.toUri(), languageVersion);
	}

	public X4OLanguage getLanguage() {
		return language;
	}
	
	/**
	 * @see org.x4o.xml.lang.X4OLanguageContext#getExpressionLanguageContext()
	 */
	public ELContext getExpressionLanguageContext() {
		return eLContext;
	}

	/**
	 * @see org.x4o.xml.lang.X4OLanguageContextLocal#setExpressionLanguageContext(javax.el.ELContext)
	 */
	public void setExpressionLanguageContext(ELContext context) {
		if (eLContext!=null) {
			throw new IllegalStateException("Can only set elContext once.");
		}
		eLContext = context;
	}

	/**
	 * @see org.x4o.xml.lang.X4OLanguageContext#getExpressionLanguageFactory()
	 */
	public ExpressionFactory getExpressionLanguageFactory() {
		return expressionFactory;
	}

	/**
	 * @see org.x4o.xml.lang.X4OLanguageContextLocal#setExpressionLanguageFactory(javax.el.ExpressionFactory)
	 */
	public void setExpressionLanguageFactory(ExpressionFactory expressionFactory) {
		if (this.expressionFactory!=null) {
			throw new IllegalStateException("Can only set expressionFactory once.");
		}
		this.expressionFactory = expressionFactory;
	}
	
	/**
	 * @return the elementAttributeValueParser
	 */
	public ElementAttributeValueParser getElementAttributeValueParser() {
		return elementAttributeValueParser;
	}

	/**
	 * @param elementAttributeValueParser the elementAttributeValueParser to set
	 */
	public void setElementAttributeValueParser(ElementAttributeValueParser elementAttributeValueParser) {
		if (this.elementAttributeValueParser!=null) {
			throw new IllegalStateException("Can only set elementAttributeValueParser once.");
		}
		this.elementAttributeValueParser = elementAttributeValueParser;
	}

	/**
	 * @return the elementObjectPropertyValue
	 */
	public ElementObjectPropertyValue getElementObjectPropertyValue() {
		return elementObjectPropertyValue;
	}

	/**
	 * @param elementObjectPropertyValue the elementObjectPropertyValue to set
	 */
	public void setElementObjectPropertyValue(ElementObjectPropertyValue elementObjectPropertyValue) {
		if (this.elementObjectPropertyValue!=null) {
			throw new IllegalStateException("Can only set elementObjectPropertyValue once.");
		}
		this.elementObjectPropertyValue = elementObjectPropertyValue;
	}

	/**
	 * @see org.x4o.xml.lang.X4OLanguageContext#getCurrentX4OPhase()
	 */
	public X4OPhase getCurrentX4OPhase() {
		return currentX4OPhase;
	}

	/**
	 * @see org.x4o.xml.lang.X4OLanguageContext#setCurrentX4OPhase(org.x4o.xml.lang.phase.X4OPhase)
	 */
	public void setCurrentX4OPhase(X4OPhase currentX4OPhase) {
		this.currentX4OPhase = currentX4OPhase;
	}

	/**
	 * @see org.x4o.xml.lang.X4OLanguageContext#addDirtyElement(org.x4o.xml.element.Element, org.x4o.xml.lang.phase.X4OPhase)
	 */
	public void addDirtyElement(Element element, X4OPhase phase) {
		if (dirtyElements.containsKey(element)) {
			throw new IllegalArgumentException("Can't add an element twice.");
		}
		dirtyElements.put(element,phase);
	}

	/**
	 * @see org.x4o.xml.lang.X4OLanguageContext#getDirtyElements()
	 */
	public Map<Element, X4OPhase> getDirtyElements() {
		return dirtyElements;
	}

	/**
	 * @see org.x4o.xml.lang.X4OLanguageContext#getRootElement()
	 */
	public Element getRootElement() {
		return rootElement;
	}

	/**
	 * @see org.x4o.xml.lang.X4OLanguageContext#setRootElement(org.x4o.xml.element.Element)
	 */
	public void setRootElement(Element element) {
		if (element==null) {
			throw new NullPointerException("May not set rootElement to null");
		}
		// allowed for reusing this context in multiple parse sessions.
		//if (rootElement!=null) {
		//	throw new IllegalStateException("Can't set rootElement when it is already set.");
		//}
		rootElement=element;
	}
	
	public Object getLanguageProperty(String key) {
		return languageProperties.get(key);
	}
	
	public void setLanguageProperty(String key,Object value) {
		languageProperties.put(key, value);
	}
	
	/**
	 * @see org.x4o.xml.lang.X4OLanguageConfiguration#getLanguageProperty(org.x4o.xml.lang.X4OLanguageProperty)
	 */
	public Object getLanguageProperty(X4OLanguageProperty property) {
		return getLanguageProperty(property.toUri());
	}

	/**
	 * @see org.x4o.xml.lang.X4OLanguageConfiguration#setLanguageProperty(org.x4o.xml.lang.X4OLanguageProperty, java.lang.Object)
	 */
	public void setLanguageProperty(X4OLanguageProperty property, Object value) {
		if (property.isValueValid(value)==false) {
			throw new IllegalArgumentException("Now allowed to set value: "+value+" in property: "+property.name());
		}
		setLanguageProperty(property.toUri(), value);
	}

	/**
	 * @see org.x4o.xml.lang.X4OLanguageConfiguration#getLanguagePropertyBoolean(org.x4o.xml.lang.X4OLanguageProperty)
	 */
	public boolean getLanguagePropertyBoolean(X4OLanguageProperty property) {
		Object value = getLanguageProperty(property);
		if (value instanceof Boolean) {
			return (Boolean)value;
		}
		return (Boolean)property.getDefaultValue();
	}

	/**
	 * @see org.x4o.xml.lang.X4OLanguageConfiguration#getLanguagePropertyInteger(org.x4o.xml.lang.X4OLanguageProperty)
	 */
	public int getLanguagePropertyInteger(X4OLanguageProperty property) {
		Object value = getLanguageProperty(property);
		if (value instanceof Integer) {
			return (Integer)value;
		}
		return (Integer)property.getDefaultValue();
	}
	
	public String getLanguagePropertyString(X4OLanguageProperty property) {
		Object value = getLanguageProperty(property);
		if (value instanceof String) {
			return (String)value;
		}
		return (String)property.getDefaultValue();
	}
	
	/**
	 * @see org.x4o.xml.lang.X4OLanguageConfiguration#getX4ODebugWriter()
	 */
	public X4ODebugWriter getX4ODebugWriter() {
		return debugWriter;
	}

	/**
	 * @see org.x4o.xml.lang.X4OLanguageConfiguration#hasX4ODebugWriter()
	 */
	public boolean hasX4ODebugWriter() {
		return debugWriter!=null;
	}

	/**
	 * @see org.x4o.xml.lang.X4OLanguageConfiguration#setX4ODebugWriter(org.x4o.xml.io.sax.X4ODebugWriter)
	 */
	public void setX4ODebugWriter(X4ODebugWriter debugWriter) {
		this.debugWriter=debugWriter;
	}
}
