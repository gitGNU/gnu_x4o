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
package	org.x4o.xml.lang.phase;

import java.util.List;

import org.x4o.xml.element.Element;
import org.x4o.xml.lang.X4OLanguageContext;


/**
 * X4OPhase is one small step in the read or write process of the language.
 * 
 * @author Willem Cazander
 * @version 1.0 Dec 31, 2008
 */
public interface X4OPhase {
	
	X4OPhaseType getType();
	
	/**
	 * Returns the X4OPhase for which this handler was written.
	 * @return	Returns the phase for which this handler works.
	 */
	String getId();
	
	String[] getPhaseDependencies();
	
	/**
	 * Returns a flag indicating that this phase is runnable multiple times.
	 * @return	True if phase is restricted to run once.
	 */
	boolean isRunOnce();
	
	/**
	 * Runs this phase.
	 * @param elementLanguage	The elementLanguage running this phase.
	 * @throws X4OPhaseException	When error has happend. 
	 */
	void runPhase(X4OLanguageContext elementLanguage) throws X4OPhaseException;
	
	/**
	 * Returns all phase listeners which where added.
	 * @return All X4OPhaseListeners.
	 */
	List<X4OPhaseListener> getPhaseListeners();
	
	/**
	 * Adds an X4OPhaseListener.
	 * @param listener	The listener to add.
	 */
	void addPhaseListener(X4OPhaseListener listener);
	
	/**
	 * Removes an X4OPhaseListener.
	 * @param listener	The listener to remove.
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
	 * @param element	The element to run this phase for.
	 * @throws X4OPhaseException	Is thrown when error has happen.
	 */
	void runElementPhase(Element element) throws X4OPhaseException;
}
