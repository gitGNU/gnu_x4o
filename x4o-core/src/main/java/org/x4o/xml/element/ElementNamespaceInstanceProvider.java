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

package org.x4o.xml.element;

/**
 * ElementNamespaceInstanceProvider is provider for creating new Element instances for an xml tag.<br>
 * 
 * @author Willem Cazander
 * @version 1.0 Jul 8, 2006
 */
public interface ElementNamespaceInstanceProvider {
	
	/**
	 * Starts the ElementProvider.
	 * @param elementLanguage The ElementLanguage to start in.
	 * @param elementNamespaceContext The ElementNamespaceContext to start for.
	 * @throws ElementNamespaceInstanceProviderException	Thrown when error happened in language.
	 */
	void start(ElementLanguage elementLanguage,ElementNamespaceContext elementNamespaceContext) throws ElementNamespaceInstanceProviderException;
	
	/**
	 * Provide an Element for an xml tag.
	 * @param tag   The xml tag to create instance for.
	 * @return An new Element instance.
	 * @throws ElementNamespaceInstanceProviderException	Thrown when error happened in language.
	 */
	Element createElementInstance(String tag) throws ElementNamespaceInstanceProviderException;
}
