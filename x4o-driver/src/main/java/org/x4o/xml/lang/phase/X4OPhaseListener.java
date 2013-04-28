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

import org.x4o.xml.lang.X4OLanguageContext;


/**
 * An X4OPhaseListener can be placed on an X4OPhaseHandler and is called
 * before and after the phase has runned.
 * 
 * @author Willem Cazander
 * @version 1.0 Dec 31, 2008
 */
public interface X4OPhaseListener {
	
	/**
	 * Gets called before the X4OPhaseHandler is run.
	 * @param phase	The phase to be run.
	 * @param elementLanguage	The elementLanguage of the driver.
	 * @throws X4OPhaseException	Is throws when listeners has error.
	 */
	void preRunPhase(X4OPhase phase,X4OLanguageContext elementLanguage) throws X4OPhaseException;
	
	/**
	 * Gets called after the X4OPhaseHandler is runned.
	 * @param phase	The phase just run.
	 * @param elementLanguage	The elementLanguage of the driver.
	 * @throws X4OPhaseException	Is throws when listeners has error.
	 */
	void endRunPhase(X4OPhase phase,X4OLanguageContext elementLanguage) throws X4OPhaseException;
}
