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
package org.x4o.xml.lang;

import java.util.ArrayList;
import java.util.List;
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
 * AbstractX4OLanguageSession.
 * 
 * @author Willem Cazander
 * @version 1.0 Aug 20, 2005
 */
public abstract class AbstractX4OLanguageSession implements X4OLanguageSessionLocal {

	private Logger logger = null;
	private X4OLanguage language = null;
	private ExpressionFactory expressionFactory = null;
	private ELContext eLContext = null;
	private ElementAttributeValueParser elementAttributeValueParser = null;
	private ElementObjectPropertyValue elementObjectPropertyValue = null;
	private Map<Element, X4OPhase> dirtyElements = null;
	private Element rootElement = null;
	private X4ODebugWriter debugWriter = null;
	private X4OPhase phaseCurrent = null;
	private String phaseStop = null;
	private List<String> phaseSkip = null;
	
	/**
	 * Creates a new empty language context.
	 */
	public AbstractX4OLanguageSession(X4OLanguage language) {
		if (language==null) {
			throw new NullPointerException("language may not be null");
		}
		logger = Logger.getLogger(AbstractX4OLanguageSession.class.getName());
		logger.finest("Creating new ParsingContext");
		this.language=language;
		dirtyElements = new HashMap<Element, X4OPhase>(40);
		phaseSkip = new ArrayList<String>(5);
	}
	
	public X4OLanguage getLanguage() {
		return language;
	}
	
	/**
	 * @see org.x4o.xml.lang.X4OLanguageSession#getExpressionLanguageContext()
	 */
	public ELContext getExpressionLanguageContext() {
		return eLContext;
	}
	
	/**
	 * @see org.x4o.xml.lang.X4OLanguageSessionLocal#setExpressionLanguageContext(javax.el.ELContext)
	 */
	public void setExpressionLanguageContext(ELContext context) {
		if (eLContext!=null) {
			throw new IllegalStateException("Can only set elContext once.");
		}
		eLContext = context;
	}
	
	/**
	 * @see org.x4o.xml.lang.X4OLanguageSession#getExpressionLanguageFactory()
	 */
	public ExpressionFactory getExpressionLanguageFactory() {
		return expressionFactory;
	}
	
	/**
	 * @see org.x4o.xml.lang.X4OLanguageSessionLocal#setExpressionLanguageFactory(javax.el.ExpressionFactory)
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
	 * @see org.x4o.xml.lang.X4OLanguageSession#addDirtyElement(org.x4o.xml.element.Element, org.x4o.xml.lang.phase.X4OPhase)
	 */
	public void addDirtyElement(Element element, X4OPhase phase) {
		if (dirtyElements.containsKey(element)) {
			throw new IllegalArgumentException("Can't add an element twice.");
		}
		dirtyElements.put(element,phase);
	}
	
	/**
	 * @see org.x4o.xml.lang.X4OLanguageSession#getDirtyElements()
	 */
	public Map<Element, X4OPhase> getDirtyElements() {
		return dirtyElements;
	}
	
	/**
	 * @see org.x4o.xml.lang.X4OLanguageSession#getRootElement()
	 */
	public Element getRootElement() {
		return rootElement;
	}
	
	/**
	 * @see org.x4o.xml.lang.X4OLanguageSession#setRootElement(org.x4o.xml.element.Element)
	 */
	public void setRootElement(Element element) {
		if (element==null) {
			throw new NullPointerException("May not set rootElement to null");
		}
		rootElement=element;
	}
	
	/**
	 * @see org.x4o.xml.lang.X4OLanguageSession#getX4ODebugWriter()
	 */
	public X4ODebugWriter getX4ODebugWriter() {
		return debugWriter;
	}
	
	/**
	 * @see org.x4o.xml.lang.X4OLanguageSession#hasX4ODebugWriter()
	 */
	public boolean hasX4ODebugWriter() {
		return debugWriter!=null;
	}
	
	/**
	 * @see org.x4o.xml.lang.X4OLanguageSessionLocal#setX4ODebugWriter(org.x4o.xml.io.sax.X4ODebugWriter)
	 */
	public void setX4ODebugWriter(X4ODebugWriter debugWriter) {
		this.debugWriter=debugWriter;
	}
	
	/**
	 * @see org.x4o.xml.lang.X4OLanguageSession#getPhaseCurrent()
	 */
	public X4OPhase getPhaseCurrent() {
		return phaseCurrent;
	}
	
	/**
	 * @see org.x4o.xml.lang.X4OLanguageSessionLocal#setPhaseCurrent(org.x4o.xml.lang.phase.X4OPhase)
	 */
	public void setPhaseCurrent(X4OPhase phaseCurrent) {
		this.phaseCurrent = phaseCurrent;
	}
	
	/**
	 * @see org.x4o.xml.lang.X4OLanguageSession#getPhaseStop()
	 */
	public String getPhaseStop() {
		return phaseStop;
	}
	
	/**
	 * @see org.x4o.xml.lang.X4OLanguageSession#getPhaseSkip()
	 */
	public List<String> getPhaseSkip() {
		return phaseSkip;
	}
	
	/**
	 * @see org.x4o.xml.lang.X4OLanguageSessionLocal#setPhaseStop(java.lang.String)
	 */
	public void setPhaseStop(String phaseId) {
		this.phaseStop=phaseId;
	}
	
	/**
	 * @see org.x4o.xml.lang.X4OLanguageSessionLocal#addPhaseSkip(java.lang.String)
	 */
	public void addPhaseSkip(String phaseId) {
		phaseSkip.add(phaseId);
	}
}
