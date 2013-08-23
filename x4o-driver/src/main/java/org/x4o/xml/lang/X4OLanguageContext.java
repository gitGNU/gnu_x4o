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
package	org.x4o.xml.lang;

import java.util.List;
import java.util.Map;

import javax.el.ELContext;
import javax.el.ExpressionFactory;

import org.x4o.xml.element.Element;
import org.x4o.xml.element.ElementAttributeValueParser;
import org.x4o.xml.element.ElementObjectPropertyValue;
import org.x4o.xml.io.sax.X4ODebugWriter;
import org.x4o.xml.lang.phase.X4OPhase;

/**
 * ElementLanguage is the central store of the defined element language.
 * 
 * @author Willem Cazander
 * @version 1.0 Feb 14, 2007
 */
public interface X4OLanguageContext {
	
	X4OLanguage getLanguage();
	
	/**
	 * Gets the EL Context.
	 * @return	Returns the ELContext.
	 */
	ELContext getExpressionLanguageContext();
		
	/**
	 * Gets the ExpressionFactory.
	 * @return	Returns the ExpressionFactory.
	 */
	ExpressionFactory getExpressionLanguageFactory();
	
	/**
	 * @return	Returns the ElementAttributeValueParser.
	 */
	ElementAttributeValueParser getElementAttributeValueParser();
		
	/**
	 * @return	Returns the ElementObjectPropertyValue.
	 */
	ElementObjectPropertyValue getElementObjectPropertyValue();
	
	/**
	 * Marks an (new) Element as dirty and run the phases from this start phase.
	 * 
	 * @param element	The Element which needs the magic.
	 * @param phase		May be null, then it should defualt to configElementPhase
	 */
	void addDirtyElement(Element element,X4OPhase phase);

	/**
	 * Get all Dirty Elements.
	 * @return	Returns Map with dirty elements.
	 */
	Map<Element,X4OPhase> getDirtyElements();
	
	/**
	 * Returns the root Element which starts the xml tree.
	 * @return	Returns the root element of the document instance we parse.
	 */
	Element getRootElement();
	
	/**
	 * Sets the root element.
	 * @param element	The root element to set.
	 */
	void setRootElement(Element element);
	
	
	/**
	 * @return	Returns null or an X4ODebugWriter to write parsing steps and debug data to.
	 */
	X4ODebugWriter getX4ODebugWriter();
	
	/** 
	 * @return	Returns true if this config has a debug writer.
	 */
	boolean hasX4ODebugWriter();
	
	/**
	 * Returns the current X4OPhase of the parser.
	 * @return	Returns the current phase.
	 */
	X4OPhase getPhaseCurrent();
	
	public String getPhaseStop();
	
	public List<String> getPhaseSkip();
}
