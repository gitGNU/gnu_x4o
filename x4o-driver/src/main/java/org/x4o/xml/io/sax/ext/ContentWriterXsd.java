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
package	org.x4o.xml.io.sax.ext;

import java.io.Writer;

import org.x4o.xml.io.XMLConstants;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * ContentWriterXsd writes XSD events as SAX events to XML.
 * 
 * @author Willem Cazander
 * @version 1.0 May 3, 2013
 */
public class ContentWriterXsd extends ContentWriterTagWrapper<ContentWriterXsd.Tag,ContentWriterXml> {
	
	public ContentWriterXsd(Writer out,String encoding) {
		super(new ContentWriterXml(out, encoding),XMLConstants.XML_SCHEMA_NS_URI, XMLConstants.NULL_NS_URI);
	}
	
	public PropertyConfig getPropertyConfig() {
		return getContentWriterWrapped().getPropertyConfig();
	}
	
	public void printXsdImport(String namespace,String schemaLocation) throws SAXException {
		AttributesImpl atts = new AttributesImpl();
		atts.addAttribute ("", "namespace", "", "", namespace);
		atts.addAttribute ("", "schemaLocation", "", "", schemaLocation);
		printTagStartEnd(Tag._import, atts);
	}
	
	public void printXsdDocumentation(String description) throws SAXException {
		if (description==null) {
			return;
		}
		printTagStart(Tag.annotation);
			AttributesImpl atts = new AttributesImpl();
			atts.addAttribute ("", "xml:lang", "", "", "en");
			printTagStart(Tag.documentation,atts);
				printCharacters(description);
			printTagEnd(Tag.documentation);
		printTagEnd(Tag.annotation);
	}
	
	public void printXsdElementAttribute(String name,String type,String description) throws SAXException {
		AttributesImpl atts = new AttributesImpl();
		atts.addAttribute ("", "name", "", "", name);
		atts.addAttribute ("", "type", "", "", type);
		printTagStart(Tag.attribute,atts);
			printXsdDocumentation(description);
		printTagEnd(Tag.attribute);
	}
	
	public enum Tag {
		all,annotation,any,anyAttribute,appinfo,attribute,attributeGroup,
		choise,complexContent,complexType,documentation,element,extension,
		field,group,_import,include,key,keyref,list,notation,
		redefine,restriction,schema,selector,sequence,
		simpleContent,simpleType,unoin,unique
	}
}
