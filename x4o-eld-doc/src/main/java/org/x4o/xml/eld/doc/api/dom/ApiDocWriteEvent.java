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
package org.x4o.xml.eld.doc.api.dom;

import org.x4o.xml.eld.doc.api.ApiDocContentWriter;

/**
 * ApiDocWriteEvent holds the needed objects to process write events of content parts.
 * 
 * @author Willem Cazander
 * @version 1.0 May 12, 2013
 */
public class ApiDocWriteEvent<T> {

	private ApiDoc doc = null;
	private T event = null;
	private ApiDocContentWriter writer = null;
	
	/**
	 * Creates an ApiDocNodeBodyEvent.
	 * @param doc		The ApiDoc we are writing.
	 * @param writer	The content writer to write to.
	 * @param event		The event we are firing this event for.
	 */
	public ApiDocWriteEvent(ApiDoc doc,ApiDocContentWriter writer,T event) {
		this.doc=doc;
		this.writer=writer;
		this.event=event;
	}
	
	/**
	 * @return the doc
	 */
	public ApiDoc getDoc() {
		return doc;
	}
	
	/**
	 * @return the event
	 */
	public T getEvent() {
		return event;
	}
	
	/**
	 * @return the writer
	 */
	public ApiDocContentWriter getWriter() {
		return writer;
	}
}
