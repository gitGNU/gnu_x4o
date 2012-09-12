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

import java.util.ArrayList;
import java.util.List;

import org.x4o.xml.element.Element;
import org.x4o.xml.element.ElementLanguage;


/**
 * An base class for creating X4OPhaseHandlers.
 * 
 * @author Willem Cazander
 * @version 1.0 Dec 31, 2008
 */
public abstract class AbstractX4OPhaseHandler implements X4OPhaseHandler {

	protected X4OPhase phase = null;
	protected List<X4OPhaseListener> X4OPhaseListeners = null;
	
	public AbstractX4OPhaseHandler() {
		X4OPhaseListeners = new ArrayList<X4OPhaseListener>(3);
		setX4OPhase();
	}
	
	/**
	 * Is called from constuctor
	 */
	abstract protected void setX4OPhase();
	
	public X4OPhase getX4OPhase() {
		return phase;
	}

	public List<X4OPhaseListener> getX4OPhaseListeners() {
		return X4OPhaseListeners;
	}
	public void addPhaseListener(X4OPhaseListener listener) {
		X4OPhaseListeners.add(listener);
	}
	public void removePhaseListener(X4OPhaseListener listener) {
		X4OPhaseListeners.remove(listener);
	}

	public boolean isElementPhase() {
		return true;
	}

	abstract public void runElementPhase(Element element) throws X4OPhaseException;

	public void runPhase(ElementLanguage elementLanguage) throws X4OPhaseException {
	}
}
