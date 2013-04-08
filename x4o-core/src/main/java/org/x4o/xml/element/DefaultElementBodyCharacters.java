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

import org.x4o.xml.lang.X4OLanguageContext;


/**
 * DefaultElementBodyCharacters the default characters element.
 * 
 * @author Willem Cazander
 * @version 1.0 Jan 9, 2009
 */
public class DefaultElementBodyCharacters implements Element {

	/** The parent Element */
	private Element parent = null;
	/** The config object */
	private Object elementObject = null;
	
	/**
	 * @return	The ElementType for characters.
	 * @see org.x4o.xml.element.Element#getElementType()
	 */
	public ElementType getElementType() {
		return ElementType.characters;
	}

	// ===== REST Element interface is non-supported

	/**
	 * @see org.x4o.xml.element.Element#addChild(org.x4o.xml.element.Element)
	 */
	public void addChild(Element element) {
	}

	/**
	 * @see org.x4o.xml.element.Element#doCharacters(java.lang.String)
	 */
	public void doCharacters(String body) throws ElementException {
	}

	/**
	 * @see org.x4o.xml.element.Element#doComment(java.lang.String)
	 */
	public void doComment(String comment) throws ElementException {
	}

	/**
	 * @see org.x4o.xml.element.Element#doElementEnd()
	 */
	public void doElementEnd() throws ElementException {
	}

	/**
	 * @see org.x4o.xml.element.Element#doElementRun()
	 */
	public void doElementRun() throws ElementException {
	}

	/**
	 * @see org.x4o.xml.element.Element#doElementStart()
	 */
	public void doElementStart() throws ElementException {
	}

	/**
	 * @see org.x4o.xml.element.Element#doIgnorableWhitespace(java.lang.String)
	 */
	public void doIgnorableWhitespace(String space) throws ElementException {
	}

	/**
	 * @see org.x4o.xml.element.Element#getAllChilderen()
	 */
	public List<Element> getAllChilderen() {
		return new ArrayList<Element>(0);
	}

	/**
	 * @see org.x4o.xml.element.Element#getAttributes()
	 */
	public Map<String, String> getAttributes() {
		return new HashMap<String,String>(0);
	}

	/**
	 * @see org.x4o.xml.element.Element#getChilderen()
	 */
	public List<Element> getChilderen() {
		return getAllChilderen();
	}

	/**
	 * @see org.x4o.xml.element.Element#getElementClass()
	 */
	public ElementClass getElementClass() {
		return null;
	}

	/**
	 * @see org.x4o.xml.element.Element#getLanguageContext()
	 */
	public X4OLanguageContext getLanguageContext() {
		return null;
	}

	/**
	 * @see org.x4o.xml.element.Element#getElementObject()
	 */
	public Object getElementObject() {
		return elementObject;
	}

	/**
	 * @see org.x4o.xml.element.Element#getParent()
	 */
	public Element getParent() {
		return parent;
	}

	/**
	 * @see org.x4o.xml.element.Element#isTransformingTree()
	 */
	public boolean isTransformingTree() {
		return false;
	}

	/**
	 * @see org.x4o.xml.element.Element#release()
	 */
	public void release() throws ElementException {
	}

	/**
	 * @see org.x4o.xml.element.Element#removeChild(org.x4o.xml.element.Element)
	 */
	public void removeChild(Element element) {		
	}

	/**
	 * @see org.x4o.xml.element.Element#setAttribute(java.lang.String, java.lang.String)
	 */
	public void setAttribute(String name, String value) {
	}

	/**
	 * @see org.x4o.xml.element.Element#setElementClass(org.x4o.xml.element.ElementClass)
	 */
	public void setElementClass(ElementClass elementClass) {
	}

	/**
	 * @see org.x4o.xml.element.Element#setLanguageContext(org.x4o.xml.lang.X4OLanguageContext)
	 */
	public void setLanguageContext(X4OLanguageContext elementLanguage) {
	}

	/**
	 * @see org.x4o.xml.element.Element#setElementObject(java.lang.Object)
	 */
	public void setElementObject(Object elementObject) {
		this.elementObject=elementObject;
	}

	/**
	 * @see org.x4o.xml.element.Element#setParent(org.x4o.xml.element.Element)
	 */
	public void setParent(Element parent) {
		this.parent=parent;
	}
}
