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
package org.x4o.xml.io.sax;

import java.io.StringWriter;

import org.x4o.xml.io.sax.ext.ContentWriterXml;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;


import junit.framework.TestCase;

/**
 * ContentWriterXml test xml escaping.
 * 
 * @author Willem Cazander
 * @version 1.0 Aug 26, 2012
 */
public class ContentWriterXmlTest extends TestCase {
	
	public void testCharactersNormal() throws Exception {
		StringWriter outputWriter = new StringWriter();
		ContentWriterXml writer = new ContentWriterXml(outputWriter);
		
		writer.startDocument();
		writer.characters("test is foobar!");
		writer.endDocument();
		
		String output = outputWriter.toString();
		assertNotNull(output);
		assertTrue(output.length()>0);
		assertTrue(output,output.equals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>test is foobar!"));
	}
	
	public void testCharactersEscape() throws Exception {
		StringWriter outputWriter = new StringWriter();
		ContentWriterXml writer = new ContentWriterXml(outputWriter);
		
		writer.startDocument();
		writer.characters("<test/> & 'foobar' is \"quoted\"!");
		writer.endDocument();
		
		String output = outputWriter.toString();
		assertNotNull(output);
		assertTrue(output.length()>0);
		assertTrue(output,output.equals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>&lt;test/&gt; &amp; &apos;foobar&apos; is &quote;quoted&quote;!"));
	}
	
	public void testCommentNormal() throws Exception {
		StringWriter outputWriter = new StringWriter();
		ContentWriterXml writer = new ContentWriterXml(outputWriter);
		
		writer.startDocument();
		writer.comment("foobar");
		writer.endDocument();
		
		String output = outputWriter.toString();
		assertNotNull(output);
		assertTrue(output.length()>0);
		assertTrue(output,output.equals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!-- foobar -->"));
	}
	
	public void testCommentEscape() throws Exception {
		StringWriter outputWriter = new StringWriter();
		ContentWriterXml writer = new ContentWriterXml(outputWriter);
		
		writer.startDocument();
		writer.comment("<!-- foobar -->");
		writer.endDocument();
		
		// note two space because auto-space is before escaping and places spaces over comment tags.
		// 1) "<!-- foobar -->"   - argu
		// 2) " <!-- foobar --> " - auto-space (default enabled)
		// 3) "  foobar  "        - escapes
		// 4) "<!--  foobar  -->" - printed
		
		String output = outputWriter.toString();
		assertNotNull(output);
		assertTrue(output.length()>0);
		assertTrue(output,output.equals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!--  foobar  -->")); 
	}
	
	public void testXmlInvalid() throws Exception {
		StringWriter outputWriter = new StringWriter();
		ContentWriterXml writer = new ContentWriterXml(outputWriter);
		AttributesImpl atts = new AttributesImpl();
		
		Exception e = null;
		try {
			writer.startDocument();
			writer.startElement("", "test", "", atts);
			writer.startElement("", "foobar", "", atts);
			writer.endElement("", "test", "");
			writer.endDocument();	
		} catch (Exception catchE) {
			e = catchE;
		}
		assertNotNull(e);
		assertEquals(SAXException.class, e.getClass());
		assertTrue(e.getMessage().contains("tag"));
		assertTrue(e.getMessage().contains("foobar"));
	}
	
	public void testXmlInvalidEnd() throws Exception {
		StringWriter outputWriter = new StringWriter();
		ContentWriterXml writer = new ContentWriterXml(outputWriter);
		AttributesImpl atts = new AttributesImpl();
		
		Exception e = null;
		try {
			writer.startDocument();
			writer.startElement("", "test", "", atts);
			writer.startElement("", "foobar", "", atts);
			writer.endDocument();	
		} catch (Exception catchE) {
			e = catchE;
		}
		assertNotNull(e);
		assertEquals(SAXException.class, e.getClass());
		assertTrue(e.getMessage().contains("Invalid"));
		assertTrue(e.getMessage().contains("2"));
		assertTrue(e.getMessage().contains("open"));
	}
	
	public void testProcessingInstruction() throws Exception {
		StringWriter outputWriter = new StringWriter();
		ContentWriterXml writer = new ContentWriterXml(outputWriter);
		AttributesImpl atts = new AttributesImpl();
		
		Exception e = null;
		try {
			writer.startDocument();
			writer.processingInstruction("target", "data");
			writer.startElement("", "test", "", atts);
			writer.endElement("", "test", "");
			writer.endDocument();	
		} catch (Exception catchE) {
			e = catchE;
		}
		String output = outputWriter.toString();
		assertNull(e);
		assertNotNull(output);
		assertTrue(output.length()>0);
		assertTrue(output,output.equals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<?target data?>\n<test/>"));
	}
	
	public void testProcessingInstructionInline() throws Exception {
		StringWriter outputWriter = new StringWriter();
		ContentWriterXml writer = new ContentWriterXml(outputWriter);
		AttributesImpl atts = new AttributesImpl();
		
		Exception e = null;
		try {
			writer.startDocument();
			writer.processingInstruction("target", "data");
			writer.startElement("", "test", "", atts);
			writer.processingInstruction("target-doc", "data-doc");
			writer.endElement("", "test", "");
			writer.endDocument();	
		} catch (Exception catchE) {
			e = catchE;
		}
		String output = outputWriter.toString();
		assertNull(e);
		assertNotNull(output);
		assertTrue(output.length()>0);
		assertTrue(output,output.equals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<?target data?>\n<test>\n\t<?target-doc data-doc?>\n</test>\n"));
	}
	
	public void testProcessingInstructionTargetXmlPrefix() throws Exception {
		StringWriter outputWriter = new StringWriter();
		ContentWriterXml writer = new ContentWriterXml(outputWriter);
		Exception e = null;
		try {
			writer.startDocument();
			writer.processingInstruction("xmlPrefix", "isInvalid");
		} catch (Exception catchE) {
			e = catchE;
		}
		assertNotNull(e);
		assertEquals(SAXException.class, e.getClass());
		assertTrue(e.getMessage().contains("instruction"));
		assertTrue(e.getMessage().contains("start with xml"));
	}
	
	public void testProcessingInstructionTargetNoneNameChar() throws Exception {
		StringWriter outputWriter = new StringWriter();
		ContentWriterXml writer = new ContentWriterXml(outputWriter);
		Exception e = null;
		try {
			writer.startDocument();
			writer.processingInstruction("4Prefix", "isInvalid");
		} catch (Exception catchE) {
			e = catchE;
		}
		assertNotNull(e);
		assertEquals(SAXException.class, e.getClass());
		assertTrue(e.getMessage().contains("instruction"));
		assertTrue(e.getMessage().contains("invalid name"));
		assertTrue(e.getMessage().contains("4Prefix"));
	}
	
	public void testProcessingInstructionDataNoneChar() throws Exception {
		StringWriter outputWriter = new StringWriter();
		ContentWriterXml writer = new ContentWriterXml(outputWriter);
		Exception e = null;
		try {
			writer.startDocument();
			writer.processingInstruction("target", "isInvalidChar="+0xD800);
		} catch (Exception catchE) {
			e = catchE;
		}
		assertNotNull(e);
		assertEquals(SAXException.class, e.getClass());
		assertTrue(e.getMessage().contains("instruction"));
		assertTrue(e.getMessage().contains("invalid char"));
		assertTrue(e.getMessage().contains("isInvalidChar=55296"));
	}
}
