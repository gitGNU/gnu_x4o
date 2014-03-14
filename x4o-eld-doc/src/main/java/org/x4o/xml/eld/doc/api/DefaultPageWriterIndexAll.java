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
package org.x4o.xml.eld.doc.api;

import org.x4o.html.ContentWriterHtml.Tag;
import org.x4o.xml.eld.doc.api.dom.ApiDocPage;
import org.x4o.xml.eld.doc.api.dom.ApiDocPageWriter;
import org.x4o.xml.eld.doc.api.dom.ApiDocWriteEvent;
import org.xml.sax.SAXException;

/**
 * DefaultPageWriterIndexAll creates the index-all page content.
 * 
 * @author Willem Cazander
 * @version 1.0 May 22, 2013
 */
public class DefaultPageWriterIndexAll implements ApiDocPageWriter {
	
	public static ApiDocPage createDocPage() {
		return new ApiDocPage("index-all","Index","Index of all api ketwords.",new DefaultPageWriterIndexAll());
	}
	
	public void writePageContent(ApiDocWriteEvent<ApiDocPage> e) throws SAXException {
	//	ApiDoc doc = e.getDoc();
	//	ApiDocPage page = e.getEvent();
		ApiDocContentWriter writer = e.getWriter();
		writer.docPageContentStart();
		for (char i='A';i<='Z';i++) {
			writer.printHref("#_"+i+"_", ""+i);
			writer.printCharacters("&nbsp;");
		}
		for (char i='A';i<='Z';i++) {
			writer.printHrefNamed("_"+i+"_");
			writer.printTagCharacters(Tag.h2, ""+i);
			writer.printCharacters("TODO");
		}
		writer.docPageContentEnd();
	}
}
