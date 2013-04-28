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

import java.util.ArrayList;
import java.util.List;

import org.x4o.xml.element.Element;
import org.x4o.xml.lang.X4OLanguageContext;


/**
 * AbstractX4OPhaseHandler a base class for creating a phase handler.
 * 
 * @author Willem Cazander
 * @version 1.0 Dec 31, 2008
 */
public abstract class AbstractX4OPhase implements X4OPhase {

	protected List<X4OPhaseListener> phaseListeners = null;
	
	/**
	 * Creates the AbstractX4OPhaseHandler.
	 */
	public AbstractX4OPhase() {
		phaseListeners = new ArrayList<X4OPhaseListener>(3);
	}
	
	/**
	 * defaults to false
	 */
	public boolean isRunOnce() {
		return false;
	}

	/**
	 * Gets the phase listeners.
	 * @return	The x4o phase listeners.
	 */
	public List<X4OPhaseListener> getPhaseListeners() {
		return phaseListeners;
	}
	
	/**
	 * Adds a phase listener.
	 * @param listener The phase listener to add.
	 */
	public void addPhaseListener(X4OPhaseListener listener) {
		phaseListeners.add(listener);
	}
	
	/**
	 * Removed a phase listener.
	 * @param listener The phase listener to remove.
	 */
	public void removePhaseListener(X4OPhaseListener listener) {
		phaseListeners.remove(listener);
	}

	/**
	 * If returns true then this handler will run on all elements. 
	 * @return defaults to true.
	 */
	public boolean isElementPhase() {
		return true;
	}

	/**
	 * Abstract method.
	 * @param element The element to run phase for.
	 * @throws X4OPhaseException when phase has error.
	 */
	abstract public void runElementPhase(Element element) throws X4OPhaseException;

	/**
	 * Empty method.
	 * @param elementLanguage The language to run phase for.
	 * @throws X4OPhaseException when phase has error.
	 */
	public void runPhase(X4OLanguageContext elementLanguage) throws X4OPhaseException {
	}
}
