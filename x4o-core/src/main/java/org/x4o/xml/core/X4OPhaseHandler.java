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

package	org.x4o.xml.core;

import java.util.List;

import org.x4o.xml.element.Element;
import org.x4o.xml.element.ElementLanguage;


/**
 * This is the starting point of the XML X4O parsing.
 * 
 * @author Willem Cazander
 * @version 1.0 Dec 31, 2008
 */
public interface X4OPhaseHandler {
	
	/**
	 * Returns the X4OPhase for which this handler was written.
	 * @return	Returns the phase for which this handler works.
	 */
	X4OPhase getX4OPhase();

	/**
	 * Runs this phase.
	 * @param elementLanguage
	 * @throws X4OPhaseException
	 */
	void runPhase(ElementLanguage elementLanguage) throws X4OPhaseException;
	
	/**
	 * Returns all X4OPhaseListeners which where added.
	 * @return All X4OPhaseListeners.
	 */
	List<X4OPhaseListener> getX4OPhaseListeners();
	
	/**
	 * Adds an X4OPhaseListener
	 * @param listener
	 */
	void addPhaseListener(X4OPhaseListener listener);
	
	/**
	 * Removes an X4OPhaseListener
	 * @param listener
	 */
	void removePhaseListener(X4OPhaseListener listener);

	/**
	 * runPhase is called but should do nothig.
	 * When elementPhase is enables x4o tries to merge all element phases so
	 * we don't need to loop element tree to often. 
	 * @return	True if to run in config.
	 */
	boolean isElementPhase();
	
	/**
	 * Run this phase for this Element.
	 * @param element
	 * @throws X4OPhaseException
	 */
	void runElementPhase(Element element) throws X4OPhaseException;
}
