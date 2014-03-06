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
import java.util.HashMap;
import java.util.Map;

import org.x4o.xml.io.sax.ext.ContentWriterXml;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import junit.framework.TestCase;

/**
 * ContentWriterXmlAttributeTest test xml attribute printing. * 
 * @author Willem Cazander
 * @version 1.0 S 17, 2012
 */
public class ContentWriterXmlAttributeTest extends TestCase {
	
	public void testAttributeNormal() throws Exception {
		StringWriter outputWriter = new StringWriter();
		ContentWriterXml writer = new ContentWriterXml(outputWriter);
		
		writer.startDocument();
		AttributesImpl atts = new AttributesImpl();
		atts.addAttribute ("", "attr", "", "", "foobar");
		writer.startElementEnd("", "test", "", atts);
		writer.endDocument();
		
		String output = outputWriter.toString();
		assertNotNull(output);
		assertTrue(output.length()>0);
		assertTrue(output,output.equals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<test attr=\"foobar\"/>"));
	}
	
	public void testAttributeEscape() throws Exception {
		StringWriter outputWriter = new StringWriter();
		ContentWriterXml writer = new ContentWriterXml(outputWriter);
		
		writer.startDocument();
		AttributesImpl atts = new AttributesImpl();
		atts.addAttribute ("", "attr", "", "", "<test/> & 'foobar' is \"quoted\"!");
		writer.startElementEnd("", "test", "", atts);
		writer.endDocument();
		
		String output = outputWriter.toString();
		assertNotNull(output);
		assertTrue(output.length()>0);
		assertTrue(output,output.equals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<test attr=\"&lt;test/&gt; &amp; &apos;foobar&apos; is &quote;quoted&quote;!\"/>"));
	}
	
	private String createLongAttribute(Map<String,Object> para) throws SAXException {
		StringWriter outputWriter = new StringWriter();
		ContentWriterXml writer = new ContentWriterXml(outputWriter);
		for (String key:para.keySet()) {
			Object value = para.get(key);
			writer.getPropertyConfig().setProperty(key, value);
		}
		
		AttributesImpl atts = new AttributesImpl();
		String data = "_FOR_FOO_BAR";
		String dataValue = "LOOP";
		for (int i=0;i<15;i++) {
			atts.addAttribute("", "attr"+i, "", "", dataValue+=data);
		}
		writer.startDocument();
		writer.startElement("", "test", "", atts);
		writer.startElement("", "testNode", "", new AttributesImpl());
		writer.endElement("", "testNode", "");
		writer.endElement("", "test", "");
		writer.endDocument();
		
		String output = outputWriter.toString();
		return output;
	}
	
	public void testAttributeLongNormal() throws Exception {
		Map<String,Object> para = new HashMap<String,Object>();
		String output = createLongAttribute(para);
		int newlines = output.split("\n").length;
		assertNotNull(output);
		assertTrue("outputs: "+newlines,newlines==4);
	}
	
	public void testAttributeLongPerLine() throws Exception {
		Map<String,Object> para = new HashMap<String,Object>();
		para.put(ContentWriterXml.OUTPUT_LINE_PER_ATTRIBUTE, true);
		String output = createLongAttribute(para);
		int newlines = output.split("\n").length;
		assertNotNull(output);
		assertTrue("outputs: "+newlines,newlines==20);
	}
	
	public void testAttributeLongSplit80() throws Exception {
		Map<String,Object> para = new HashMap<String,Object>();
		para.put(ContentWriterXml.OUTPUT_LINE_BREAK_WIDTH, 80);
		String output = createLongAttribute(para);
		int newlines = output.split("\n").length;
		assertNotNull(output);
		assertTrue("outputs: "+newlines,newlines==16);
	}
	
	public void testAttributeLongSplit180() throws Exception {
		Map<String,Object> para = new HashMap<String,Object>();
		para.put(ContentWriterXml.OUTPUT_LINE_BREAK_WIDTH, 180);
		String output = createLongAttribute(para);
		int newlines = output.split("\n").length;
		assertNotNull(output);
		assertTrue("outputs: "+newlines,newlines==11);
	}
}
