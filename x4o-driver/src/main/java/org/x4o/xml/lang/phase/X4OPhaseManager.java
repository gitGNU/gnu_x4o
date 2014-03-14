/*
 * Copyright (c) 2004-2014, Willem Cazander
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
import org.x4o.xml.lang.X4OLanguageSession;

/**
 * X4OPhaseManager stores the X4OPhaseHandler and puts them in the right order.
 * And will execute the phases when runPhases is called.
 * 
 * @author Willem Cazander
 * @version 1.0 Jan 6, 2008
 */
public interface X4OPhaseManager {
	
	/**
	 * Gets an X4OPhase object by the phaseName.
	 * @param phaseName	The phaseName to lookup.
	 * @return	The X4OPhase requested or null.
	 */
	X4OPhase getPhase(String phaseName);
	
	/**
	 * Gets all the keys of the phases registrated whith this phase manager.
	 * @return	The phase keys.
	 */
	List<String> getPhaseKeys();
	
	/**
	 * Runs release phase if it was requested not to run it automaticly. 
	 * @param languageSession	The session to release.
	 * @throws X4OPhaseException When a running handlers throws one.
	 */
	void doReleasePhaseManual(X4OLanguageSession languageSession) throws X4OPhaseException;
	
	/**
	 * Runs all the phases in the right order.
	 * @throws X4OPhaseException When a running handlers throws one.
	 */
	void runPhases(X4OLanguageSession elementContext,X4OPhaseType type) throws X4OPhaseException;
	
	/**
	 * Runs phase on single element.
	 * @param e	The Element to process.
	 * @param p	The phase to run.
	 * @throws X4OPhaseException When a running handlers throws one.
	 */
	void runPhasesForElement(Element e,X4OPhaseType type,X4OPhase p) throws X4OPhaseException;
}
