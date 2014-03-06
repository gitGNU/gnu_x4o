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

import junit.framework.TestCase;

/**
 * ContentWriterXmlCDataTest tests cdata xml escaping.
 * 
 * @author Willem Cazander
 * @version 1.0 Sep 17, 2013
 */
public class ContentWriterXmlCDataTest extends TestCase {
	
	public void testCDATANone() throws Exception {
		StringWriter outputWriter = new StringWriter();
		ContentWriterXml writer = new ContentWriterXml(outputWriter);
		
		writer.startDocument();
		writer.characters("foobar");
		writer.endDocument();
		
		String output = outputWriter.toString();
		assertNotNull(output);
		assertTrue(output.length()>0);
		assertTrue(output,output.equals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>foobar"));
	}
	
	public void testCDATANoneTagEscape() throws Exception {
		StringWriter outputWriter = new StringWriter();
		ContentWriterXml writer = new ContentWriterXml(outputWriter);
		
		writer.startDocument();
		writer.characters("foobar<test/>");
		writer.endDocument();
		
		String output = outputWriter.toString();
		assertNotNull(output);
		assertTrue(output.length()>0);
		assertTrue(output,output.equals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>foobar&lt;test/&gt;"));
	}
	
	public void testCDATANormal() throws Exception {
		StringWriter outputWriter = new StringWriter();
		ContentWriterXml writer = new ContentWriterXml(outputWriter);
		
		writer.startDocument();
		writer.startCDATA();
		writer.characters("foobar");
		writer.endCDATA();
		writer.endDocument();
		
		String output = outputWriter.toString();
		assertNotNull(output);
		assertTrue(output.length()>0);
		assertTrue(output,output.equals("<?xml version=\"1.0\" encoding=\"UTF-8\"?><![CDATA[foobar]]>"));
	}
	
	public void testCDATAEscapeTag() throws Exception {
		StringWriter outputWriter = new StringWriter();
		ContentWriterXml writer = new ContentWriterXml(outputWriter);
		
		writer.startDocument();
		writer.startCDATA();
		writer.characters("foobar<test/>");
		writer.endCDATA();
		writer.endDocument();
		
		String output = outputWriter.toString();
		assertNotNull(output);
		assertTrue(output.length()>0);
		assertTrue(output,output.equals("<?xml version=\"1.0\" encoding=\"UTF-8\"?><![CDATA[foobar<test/>]]>"));
	}
	
	public void testCDATAEscapeStart() throws Exception {
		StringWriter outputWriter = new StringWriter();
		ContentWriterXml writer = new ContentWriterXml(outputWriter);
		
		writer.startDocument();
		writer.startCDATA();
		writer.characters("<![CDATA[foobar");
		writer.endCDATA();
		writer.endDocument();
		
		String output = outputWriter.toString();
		assertNotNull(output);
		assertTrue(output.length()>0);
		assertTrue(output,output.equals("<?xml version=\"1.0\" encoding=\"UTF-8\"?><![CDATA[foobar]]>"));
	}
	
	public void testCDATAEscapeEnd() throws Exception {
		StringWriter outputWriter = new StringWriter();
		ContentWriterXml writer = new ContentWriterXml(outputWriter);
		
		writer.startDocument();
		writer.startCDATA();
		writer.characters("foobar]]>");
		writer.endCDATA();
		writer.endDocument();
		
		String output = outputWriter.toString();
		assertNotNull(output);
		assertTrue(output.length()>0);
		assertTrue(output,output.equals("<?xml version=\"1.0\" encoding=\"UTF-8\"?><![CDATA[foobar]]>"));
	}
	
	public void testCDATAEscapeInvalid() throws Exception {
		StringWriter outputWriter = new StringWriter();
		ContentWriterXml writer = new ContentWriterXml(outputWriter);
		
		writer.startDocument();
		writer.startCDATA();
		writer.characters("<![CDATA[tokens like ']]>' are <invalid>]]>");
		writer.endCDATA();
		writer.endDocument();
		
		String output = outputWriter.toString();
		assertNotNull(output);
		assertTrue(output.length()>0);
		assertTrue(output,output.equals("<?xml version=\"1.0\" encoding=\"UTF-8\"?><![CDATA[tokens like \'\' are <invalid>]]>"));
	}
	
	public void testCDATAEscapeValid() throws Exception {
		StringWriter outputWriter = new StringWriter();
		ContentWriterXml writer = new ContentWriterXml(outputWriter);
		
		writer.startDocument();
		writer.startCDATA();
		writer.characters("<![CDATA[tokens like ']]]]><![CDATA[>' are <valid>]]>");
		writer.endCDATA();
		writer.endDocument();
		
		String output = outputWriter.toString();
		assertNotNull(output);
		assertTrue(output.length()>0);
		assertTrue(output.equals("<?xml version=\"1.0\" encoding=\"UTF-8\"?><![CDATA[tokens like \']]>\' are <valid>]]>"));
	}
}
