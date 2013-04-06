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

package	org.x4o.xml.element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.x4o.xml.core.config.X4OLanguageClassLoader;

/**
 * An AbstractElement.
 * 
 * @author Willem Cazander
 * @version 1.0 Aug 8, 2005
 */
public abstract class AbstractElement implements Element {

	/** The parent Element */
	private Element parent = null;
	/** The config object */
	private Object elementObject = null;
	/** The language parsing context */
	private ElementLanguage elementLanguage = null;
	/** The ElementClass */
	private ElementClass elementClass = null;
	/** The attributes */
	private Map<String,String> attributes = new HashMap<String,String>(10);
	/** The Childeren */
	private List<Element> childeren = new ArrayList<Element>(10);
	/** All Childeren */
	private List<Element> allChilderen = new ArrayList<Element>(10);
	
	/**
	 * @see Element#doElementStart()
	 */
	public void doElementStart() throws ElementException {
	}
	
	/**
	 * @see Element#doElementEnd()
	 */
	public void doElementEnd() throws ElementException {
	}
	
	/**
	 * @see Element#doElementRun()
	 */
	public void doElementRun() throws ElementException {
	}
	
	/**
	 * @see Element#setParent(Element)
	 */
	public void setParent(Element element) {
		parent = element;
	}
	
	/**
	 * @see Element#getParent()
	 */
	public Element getParent() {
		return parent;
	}
	
	/**
	 * Cleans the attributes and elements(class) and context.
	 * @see Element#release()
	 */
	public void release() throws ElementException {
		getAttributes().clear();
		setElementClass(null);
		setParent(null);
		setElementLanguage(null);
		attributes.clear();
		childeren.clear(); // we do not release childeren, x4o does that
		allChilderen.clear();
	}
	
	/**
	 * @see Element#getElementObject()
	 */
	public Object getElementObject() {
		return elementObject;
	}
	
	/**
	 * @see Element#setElementObject(Object)
	 */
	public void setElementObject(Object object) {
		elementObject=object;
	}
	
	/**
	 * @see Element#setElementLanguage(ElementLanguage)
	 */
	public void setElementLanguage(ElementLanguage elementLanguage) {
		this.elementLanguage=elementLanguage;
	}
	
	/**
	 * @see Element#getElementLanguage()
	 */
	public ElementLanguage getElementLanguage() {
		return elementLanguage;
	}

	/**
	 * @see org.x4o.xml.element.Element#doCharacters(java.lang.String)
	 */
	public void doCharacters(String characters) throws ElementException {
		try {
			Element e = (Element)X4OLanguageClassLoader.newInstance(getElementLanguage().getLanguage().getLanguageConfiguration().getDefaultElementBodyCharacters());
			e.setElementObject(characters);
			addChild(e);
		} catch (Exception exception) {
			throw new ElementException(exception);
		}
	}

	/**
	 * @see org.x4o.xml.element.Element#doComment(java.lang.String)
	 */
	public void doComment(String comment) throws ElementException {
		try {
			Element e = (Element)X4OLanguageClassLoader.newInstance(getElementLanguage().getLanguage().getLanguageConfiguration().getDefaultElementBodyComment());
			e.setElementObject(comment);
			addChild(e);
		} catch (Exception exception) {
			throw new ElementException(exception);
		}
	}

	/**
	 * @see org.x4o.xml.element.Element#doIgnorableWhitespace(java.lang.String)
	 */
	public void doIgnorableWhitespace(String space) throws ElementException {
		try {
			Element e = (Element)X4OLanguageClassLoader.newInstance(getElementLanguage().getLanguage().getLanguageConfiguration().getDefaultElementBodyWhitespace());
			e.setElementObject(space);
			addChild(e);
		} catch (Exception exception) {
			throw new ElementException(exception);
		}
	}

	/**
	 * @see org.x4o.xml.element.Element#setElementClass(ElementClass)
	 */
	public void setElementClass(ElementClass elementClass) {
		this.elementClass=elementClass;
	}
	
	/**
	 * @see org.x4o.xml.element.Element#getElementClass()
	 */
	public ElementClass getElementClass() {
		return elementClass;
	}

	/**
	 * @see org.x4o.xml.element.Element#getAttributes()
	 */
	public Map<String, String> getAttributes() {
		return attributes;
	}

	/**
	 *  @see org.x4o.xml.element.Element#setAttribute(java.lang.String, java.lang.String)
	 */
	public void setAttribute(String name,String value) {
		attributes.put(name, value);
	}

	/**
	 * @see org.x4o.xml.element.Element#getChilderen()
	 */
	public List<Element> getChilderen() {
		return childeren;
	}
	
	/**
	 * @see org.x4o.xml.element.Element#addChild(Element)
	 */
	public void addChild(Element element) {
		allChilderen.add(element);
		if (ElementType.element.equals(element.getElementType())) {
			childeren.add(element);
		}
	}
	
	/**
	 * @see org.x4o.xml.element.Element#removeChild(Element)
	 */
	public void removeChild(Element element) {
		childeren.remove(element);
		allChilderen.remove(element);
	}

	/**
	 * @see org.x4o.xml.element.Element#getAllChilderen()
	 */
	public List<Element> getAllChilderen() {
		return allChilderen;
	}

	/**
	 * @see org.x4o.xml.element.Element#getElementType()
	 */
	public ElementType getElementType() {
		return ElementType.element;
	}

	/**
	 * Defaults to false.
	 * @see org.x4o.xml.element.Element#isTransformingTree()
	 */
	public boolean isTransformingTree() {
		return false;
	}
}
