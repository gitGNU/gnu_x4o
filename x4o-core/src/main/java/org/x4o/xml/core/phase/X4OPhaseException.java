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

package	org.x4o.xml.core.phase;

/**
 * Is throw when there is en Exception within an Element.
 * 
 * @author Willem Cazander
 * @version 1.0 Jan 1, 2009
 */
public class X4OPhaseException extends Exception {

	/** The serial version uid */
	static final long serialVersionUID = 10L;

	private X4OPhase exceptionPhase = null; 
	
	/**
	 * Creates an ElementException from a parent exception.
	 * @param exceptionPhase	The handler which throwed.
	 * @param e The Exception.
	 */
	public X4OPhaseException(X4OPhase exceptionPhase,Exception e) {
		super(e);
		this.exceptionPhase=exceptionPhase;
	}
	
	/**
	 * Creates an ElementException from a parent exception.
	 * @param exceptionPhase	The handler which throwed.
	 * @param message	The message of the error.
	 */
	public X4OPhaseException(X4OPhase exceptionPhase,String message) {
		super(message);
		this.exceptionPhase=exceptionPhase;
	}
	
	/**
	 * Creates an ElementException from a parent exception.
	 * @param exceptionPhase	The handler which throwed.
	 * @param message	The message of the error.
	 * @param e The Exception.
	 */
	public X4OPhaseException(X4OPhase exceptionPhase,String message,Exception e) {
		super(message,e);
		this.exceptionPhase=exceptionPhase;
	}
	
	/**
	 * Returns the X4OPhaseHandler which created this Exception.
	 * @return	An X4OPhaseHandler
	 */
	public X4OPhase getX4OPhaseHandler() {
		return exceptionPhase;
	}
}
