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

import java.io.Writer;
import java.util.Date;
import java.util.List;

import org.x4o.html.ContentWriterHtml;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * ContentWriterHtml Writes eld/java documentation in html.
 * 
 * @author Willem Cazander
 * @version 1.0 Apr 30, 2013
 */
public class ApiDocContentWriter extends ContentWriterHtml {
	
	private boolean isAltRow = true;
	
	public ApiDocContentWriter(Writer out,String encoding) {
		super(out,encoding);
	}
	
	public void docCommentGenerated() throws SAXException {
		comment("Generated by "+ApiDocContentWriter.class.getSimpleName()+" on "+new Date());
	}
	
	public void docHtmlStart(String title,List<String> keywords,String pathPrefix) throws SAXException {
		printDocType(DocType.HTML_4_TRANSITIONAL);
		comment("NewPage");
		printHtmlStart("en");
		
		// ====== Write head
		printTagStart(Tag.head);
			docCommentGenerated();
			printHeadMetaContentType();
			printHeadTitle(title);
			printHeadMetaDate();
			for (String keyword:keywords) {
				printHeadMeta("keywords",keyword);
			}
			printHeadLinkCss(pathPrefix+"resources/stylesheet.css");
		printTagEnd(Tag.head);
		
		// ======= Write body
		printTagStart(Tag.body);
		
		StringBuilder script = new StringBuilder();
		script.append("\n");
		script.append("\tif (location.href.indexOf('is-external=true') == -1) {\n");
		script.append("\t\tparent.document.title=\"");script.append(title);script.append("\";\n");
		script.append("\t}\n");
		printScriptInline(script.toString());
		printScriptNoDiv();
	}
	
	public void docHtmlEnd(String copyright,String statsJS) throws SAXException {
		printTagStart(Tag.p,ApiDocContentCss.legalCopy);
			printTagStart(Tag.small);
			characters(copyright);
			printTagEnd(Tag.small);
		printTagEnd(Tag.p);
		if (statsJS!=null) {
			printScriptInline(statsJS);
		}
		printTagEnd(Tag.body);
		printHtmlEnd();
	}
	
	public void docNavBarAbout(String about) throws SAXException {
		printTagStart(Tag.div,ApiDocContentCss.aboutLanguage); // Print about language
		printTagStart(Tag.em);
		printTagStart(Tag.strong);
		String[] lines = about.split("\n");
		for (int i=0;i<lines.length;i++) {
			String line = lines[i];
			characters(line);
			if (i<lines.length-1) {
				printTagStartEnd(Tag.br);
			}
		}
		printTagEnd(Tag.strong);
		printTagEnd(Tag.em);
		printTagEnd(Tag.div); 
	}
	
	public void docPagePackageTitle(String title,String summary) throws SAXException {
		printTagStart(Tag.div,ApiDocContentCss.header);
			printTagCharacters(Tag.h1, title,"title");
			printTagStart(Tag.div,ApiDocContentCss.docSummary);
				printTagCharacters(Tag.div, summary,ApiDocContentCss.block.name());
			printTagEnd(Tag.div);
			printTagStart(Tag.p);
				characters("See:&nbsp;");
				printHref("#package_description", "Description");
			printTagEnd(Tag.p);
		printTagEnd(Tag.div);
	}
	
	public void docPagePackageDescription(String title,String summary,String description) throws SAXException {
		printHrefNamed("package_description");
		printTagCharacters(Tag.h2, title);
		printTagCharacters(Tag.div, summary,ApiDocContentCss.block.name());
		characters(description);
	}
	
	public void docPageClassStart(String title,String subTitle,Tag titleTag) throws SAXException {
		comment("======== START OF CLASS DATA ========");
		printTagStart(Tag.div,ApiDocContentCss.header);
		if (subTitle!=null) {
			printTagStart(Tag.div,ApiDocContentCss.subTitle);
			characters(subTitle);
			printTagEnd(Tag.div);
		}
		printTagCharacters(titleTag, title, "title");
		printTagEnd(Tag.div);
	}
	
	public void docPageClassEnd() throws SAXException {
		comment("======== END OF CLASS DATA ========");
	}
	
	public void docPageContentStart() throws SAXException {
		printTagStart(Tag.div,ApiDocContentCss.contentContainer);
	}
	
	public void docPageContentEnd() throws SAXException {
		printTagEnd(Tag.div);
	}
	
	public void docPageBlockStart(String title,String namedLink,String comment) throws SAXException {
		if (comment!=null) {
			comment(comment);
		}
		docPageBlockStart();
		printHrefNamed(namedLink);
		printTagCharacters(Tag.h3, title);
	}
	
	public void docPageBlockStart() throws SAXException {
		printTagStart(Tag.ul,ApiDocContentCss.blockList);
		printTagStart(Tag.li,ApiDocContentCss.blockList);
	}
	
	public void docPageBlockEnd() throws SAXException {
		printTagEnd(Tag.li);
		printTagEnd(Tag.ul);
	}
	
	public void docPageBlockNext() throws SAXException {
		printTagEnd(Tag.li);
		printTagStart(Tag.li,ApiDocContentCss.blockList);
	}
	
	public void docTableStart(String tableTitle,String tableDescription,ApiDocContentCss tableCss) throws SAXException {
		AttributesImpl atts = new AttributesImpl();
		if (tableCss!=null) {
			atts.addAttribute ("", "class", "", "", tableCss.name());
		}
		atts.addAttribute ("", "border", "", "", "0");
		atts.addAttribute ("", "cellpadding", "", "", "3");
		atts.addAttribute ("", "cellspacing", "", "", "0");
		if (tableDescription!=null) {
			atts.addAttribute ("", "summary", "", "", tableDescription);
		}
		startElement("", "table", "", atts);
		
		printTagStart(Tag.caption);
			printTagStart(Tag.span);characters(tableTitle);printTagEnd(Tag.span);
			printTagStart(Tag.span,ApiDocContentCss.tabEnd);characters("&nbsp;");printTagEnd(Tag.span);
		printTagEnd(Tag.caption);
	}
	
	public void docTableEnd() throws SAXException {
		printTagEnd(Tag.table);
		isAltRow = true;
	}
	
	public void docTableHeader(String titleFirst,String titleLast) throws SAXException {
		printTagStart(Tag.tr);
		AttributesImpl atts = new AttributesImpl();
		if (titleLast==null) {
			atts.addAttribute ("", "class", "", "", ApiDocContentCss.colOne.name());
		} else {
			atts.addAttribute ("", "class", "", "", ApiDocContentCss.colFirst.name());
		}
		atts.addAttribute ("", "scope", "", "", "col");
		startElement("", "th", "", atts);
		characters(titleFirst);
		endElement("", "th", "");
		if (titleLast==null) {
			printTagEnd(Tag.tr);
			return;
		}
		atts = new AttributesImpl();
		atts.addAttribute ("", "class", "", "", ApiDocContentCss.colLast.name());
		atts.addAttribute ("", "scope", "", "", "col");
		startElement("", "th", "", atts);
		characters(titleLast);
		printTagEnd(Tag.th);
		printTagEnd(Tag.tr);
	}
	
	public void docTableRowLink(String dataFirstHref,String dataFirst,String dataLast) throws SAXException {
		docTableRowHref(dataFirstHref,dataFirst,dataLast,null,false,false,false);
	}
	
	public void docTableRow(String dataFirst,String dataLast) throws SAXException {
		docTableRow(dataFirst,dataLast,null);
	}
	
	public void docTableRow(String dataFirst,String dataLast,String dataBlock) throws SAXException {
		docTableRowHref(null,dataFirst,dataLast,dataBlock,false,false,false);
	}
	
	public void docTableRowLastStart(String dataFirst,String dataFirstHref) throws SAXException {
		docTableRowHref(dataFirstHref,dataFirst,null,null,false,false,true);
	}
	
	public void docTableRowLastEnd() throws SAXException {
		printTagEnd(Tag.td);
		printTagEnd(Tag.tr);
	}
	
	private void docTableRowHref(String dataFirstHref,String dataFirst,String dataLast,String dataBlock,boolean dataFirstCode,boolean dataLastCode,boolean skipLast) throws SAXException {
		if (isAltRow) {
			printTagStart(Tag.tr,ApiDocContentCss.altColor);
		} else {
			printTagStart(Tag.tr,ApiDocContentCss.rowColor);
		}
		isAltRow = !isAltRow;
		if (dataLast==null) {
			printTagStart(Tag.td,ApiDocContentCss.colOne);
		} else {
			printTagStart(Tag.td,ApiDocContentCss.colFirst);
		}
		if (dataFirstCode) {
			printTagStart(Tag.code);
		}
		if (dataFirstHref==null) {
			characters(dataFirst);
		} else {
			printHref(dataFirstHref, dataFirst, dataFirst);
		}
		if (dataFirstCode) {
			printTagEnd(Tag.code);
		}
		printTagEnd(Tag.td);
		
		if (skipLast) {
			printTagStart(Tag.td,ApiDocContentCss.colLast);
			return;
		}
		
		if (dataLast==null) {
			printTagEnd(Tag.tr);
			return;
		}
		
		printTagStart(Tag.td,ApiDocContentCss.colLast);
			if (dataLastCode) {
				printTagStart(Tag.code);characters(dataLast);printTagEnd(Tag.code);
			} else {
				printTagStart(Tag.div,ApiDocContentCss.block);characters(dataLast);printTagEnd(Tag.div);
			}
			if (dataBlock!=null) {
				printTagStart(Tag.div,ApiDocContentCss.block);characters(dataBlock);printTagEnd(Tag.div);
			}
		printTagEnd(Tag.td);
		
		printTagEnd(Tag.tr);
	}
	
	
	static public String toSafeUri(List<String> paths) {
		return toSafeUri(paths.toArray(new String[]{}));
	}
	
	static public String toSafeUri(String...paths) {
		StringBuilder result = new StringBuilder(100);
		for (int i=0;i<paths.length;i++) {
			String path=paths[i];
			result.append(toSafeUri(path));
			if (i<(paths.length-1)) {
				result.append('/');
			}
		}
		return result.toString();
	}
	
	static public String toSafeUri(String uri) {
		StringBuilder buf = new StringBuilder(20);
		for (char c:uri.toLowerCase().toCharArray()) {
			if (Character.isLetter(c)) {
				buf.append(c);
			}
			if (Character.isDigit(c)) {
				buf.append(c);
			}
			if ('.'==c) {
				buf.append(c);
			}
			if ('-'==c) {
				buf.append(c);
			}
			if ('_'==c) {
				buf.append(c);
			}
		}
		String prefix = buf.toString();
		if (prefix.startsWith("http")) {
			prefix = prefix.substring(4);
		}
		if (prefix.startsWith("uri")) {
			prefix = prefix.substring(3);
		}
		if (prefix.startsWith("url")) {
			prefix = prefix.substring(3);
		}
		return prefix;
	}
}
