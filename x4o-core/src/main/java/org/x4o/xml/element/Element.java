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
import java.util.List;
import java.util.Map;

import org.x4o.xml.lang.X4OLanguageContext;

/**
 * Defines an XML element with an object.<br>
 * <br>
 * The main function is to store the ElementObject.<br>
 * Also we can configure the ElementObject from differted events hooks.
 * from the attibutes or parent (object)element.
 * 
 * @author Willem Cazander
 * @version 1.0 01/02/2005
 */
public interface Element {
	
	/**
	 * The ElementTypes which are possible.
	 */
	public enum ElementType {
		
		/** The normale ElementType, which is mostly bound to an object. */
		element,
		
		/** Extra meta characters in xml. */
		characters,
		
		/** The xml comments in xml. */
		comment,
		
		/** ignorableWhitespace in xml. */
		ignorableWhitespace,
		
		/** Receive raw sax event on elementObject. */
		overrideSax;
		
		/**
		 * Filters the given elments list to elementType.
		 * @param elements	The elements to filter.
		 * @param elementType	The elementType to filter on.
		 * @return	Always returns List of Elements of filter type. 
		 */
		public static List<Element> filterElements(List<Element> elements,ElementType elementType) {
			List<Element> result = new ArrayList<Element>(3);
			for (int i=0;i<elements.size();i++) {
				Element element = elements.get(i);
				if (elementType == element.getElementType()) {
					result.add(element);
				}
			}
			return result;
		}
	}
	
	/**
	 * This method is fired when the end xml tag is parsed.
	 * @throws ElementException Can be thrown when structure is not correct.
	 */
	void doElementEnd() throws ElementException;
	
	/**
	 * This method is fired when the start of xml tag is parsed.
	 * @throws ElementException Can be thrown when structure is not correct.
	 */
	void doElementStart() throws ElementException;

	/**
	 * This method is fired only once in the run phase.
	 * @throws ElementException Can be thrown when structure is not correct.
	 */
	void doElementRun() throws ElementException;
	
	/**
	 * Set the parent Element.
	 * @param element	The paraent Element to set.
	 */
	void setParent(Element element);
	
	/**
	 * Returns the parent Element.<br>
	 * Or null when there is no parent Element.
	 * 
	 * @return	Returns the parent Element
	 */
	Element getParent();
	
	/**
	 * This method get called when this Element object is not needed anymore.<br>
	 * Can be used to close resources.
	 * @throws ElementException Can be thrown when structure is not correct.
	 */
	void release() throws ElementException;
	
	/**
	 * Gives back the object this Element has made and configed.<br>
	 * So other elements can do stuff to that object.<br>
	 * 
	 * @return	An Object.
	 */
	Object getElementObject();
	
	/**
	 * Sets the object which we control.
	 * @param object	The object to configed by this element.
	 */
	void setElementObject(Object object);
	
	/**
	 * Sets the ElementLanguage.
	 * @param elementLanguage	The ElementLanguage to set.
	 */
	void setLanguageContext(X4OLanguageContext elementLanguage);
	
	/**
	 * Gets the ElementLanguage.
	 * @return	Returns the ElementLanguage.
	 */
	X4OLanguageContext getElementLanguage();
	
	/**
	 * Sets the body texts on an event based system.
	 * @param body	The body text.
	 * @throws ElementException Can be thrown when structure is not correct.
	 */
	void doCharacters(String body) throws ElementException;
	
	/**
	 * Sets the comment texts on an event based system.
	 * @param comment	The comment text.
	 * @throws ElementException Can be thrown when structure is not correct.
	 */
	void doComment(String comment) throws ElementException;
	
	/**
	 * Is called when there is whitespace in xml.
	 * @param space	The space.
	 * @throws ElementException Can be thrown when structure is not correct.
	 */
	void doIgnorableWhitespace(String space) throws ElementException; 
	
	/**
	 * Sets the ElementClass.
	 * @param elementClass	The ElementClass to set.
	 */
	void setElementClass(ElementClass elementClass);
	
	/**
	 * Gets the ElementClass.
	 * @return	Returns the ElementClass.
	 */
	ElementClass getElementClass();
	
	/**
	 * Sets the xml attributes.
	 * @param name	The name to set.
	 * @param value	The value to set.
	 */
	void setAttribute(String name,String value);
	
	/**
	 * Gets the xml attributes.
	 * @return	Returns the xml attributes. 
	 */
	Map<String,String> getAttributes();
	
	/**
	 * Gets the childeren elements.
	 * @return	Returns the childeren.
	 */
	List<Element> getChilderen();
	
	/**
	 * Gets the childeren elements including those which are comment and white space. (text)
	 * @return	Returns all the childeren.
	 */
	List<Element> getAllChilderen();
	
	/**
	 * Adds an Elment as child of this element.
	 * @param element	The child to add.
	 */
	void addChild(Element element);
	
	/**
	 * Removes an Elment as child of this element.
	 * @param element	The child to remove.
	 */
	void removeChild(Element element);
	
	/**
	 * Gets the Element type.
	 * @return	Returns the ElementType.
	 */
	ElementType getElementType();
	
	/**
	 * Returns if this elements transforms the tree.
	 * if true the the doElementRun is runned in the transform phase insteat of the run phase.
	 * 
	 * You need to add those new or modified Elements to the DirtyElement for reparsering.
	 * 
	 * @return Returns true if transforming tree.
	 */
	boolean isTransformingTree();
}
