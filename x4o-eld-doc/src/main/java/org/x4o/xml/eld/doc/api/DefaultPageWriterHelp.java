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

import org.x4o.xml.eld.doc.api.dom.ApiDoc;
import org.x4o.xml.eld.doc.api.dom.ApiDocConcept;
import org.x4o.xml.eld.doc.api.dom.ApiDocPage;
import org.x4o.xml.eld.doc.api.dom.ApiDocPageWriter;
import org.x4o.xml.eld.doc.api.dom.ApiDocWriteEvent;
import org.x4o.xml.io.sax.ext.ContentWriterHtml.Tag;
import org.xml.sax.SAXException;

/**
 * DefaultPageWriterHelp creates the help page content.
 * 
 * @author Willem Cazander
 * @version 1.0 May 22, 2013
 */
public class DefaultPageWriterHelp implements ApiDocPageWriter {
	
	public static ApiDocPage createDocPage() {
		return new ApiDocPage("doc-help","Help","This help file applies to the API documentation generated using the standard format.",new DefaultPageWriterHelp());
	}
	
	public void writePageContent(ApiDocWriteEvent<ApiDocPage> e) throws SAXException {
		ApiDoc doc = e.getDoc();
		//ApiDocPage page = e.getEvent();
		ApiDocContentWriter writer = e.getWriter();
		
		writer.printTagStart(Tag.div,"header");
			writer.printTagCharacters(Tag.h1, "How This API Document Is Organized", "title");
			writer.printTagStart(Tag.div,"subTitle");
				writer.characters("This ApiDoc document has pages corresponding to the items in the navigation bar, described as follows.");
			writer.printTagEnd(Tag.div);
		writer.printTagEnd(Tag.div);
		
		writer.docPageContentStart();
			writer.docPageBlockStart();
			for (ApiDocConcept concept:doc.getConcepts()) {
				writer.printTagCharacters(Tag.h2, concept.getName());
				writer.printTagStart(Tag.p);
					writer.characters(concept.getDescriptionHelp());
				writer.printTagEnd(Tag.p);
				writer.docPageBlockNext();
			}
			for (ApiDocPage docPage:doc.getDocPages()) {
				writer.printTagCharacters(Tag.h2, docPage.getName());
				writer.printTagStart(Tag.p);
					writer.characters(docPage.getDescription());
				writer.printTagEnd(Tag.p);
				writer.docPageBlockNext();
			}
			writer.docPageBlockEnd();
		writer.docPageContentEnd();
	}
}
