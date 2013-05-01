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
package	org.x4o.xml.io.sax;

import java.io.Writer;
import java.util.Calendar;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * ContentWriterHtml writes HTML events as SAX events to XML.
 * 
 * @author Willem Cazander
 * @version 1.0 Apr 30, 2013
 */
public class ContentWriterHtml extends ContentWriterXml {
	
	public ContentWriterHtml(Writer out,String encoding,String charNewLine,String charTab) {
		super(out,encoding,charNewLine,charTab);
	}
	
	public void printDocType(DocType doc) throws SAXException {
		startDTD(doc.getName(), doc.getPublicId(), doc.getSystemId());
	}
	
	public void printHtmlStart(String language) throws SAXException {
		AttributesImpl atts = new AttributesImpl();
		if (language!=null) {
			atts.addAttribute ("", "lang", "", "", language);
		}
		printTagStart(Tag.html,atts);
	}
	
	public void printHtmlEnd() throws SAXException {
		printTagEnd(Tag.html);
	}
	
	public void printHeadMetaDate() throws SAXException {
		Calendar cal = Calendar.getInstance();
		printHeadMeta("date",cal.get(Calendar.YEAR)+"-"+(cal.get(Calendar.MONTH)+1)+"-"+cal.get(Calendar.DAY_OF_MONTH));
	}
	
	public void printHeadTitle(String title) throws SAXException {
		printTagText("title",title);
	}

	public void printHeadMetaContentType() throws SAXException {
		AttributesImpl atts = new AttributesImpl();
		atts.addAttribute ("", "http-equiv", "", "", "Content-Type");
		atts.addAttribute ("", "content", "", "", "text/html");
		atts.addAttribute ("", "charset", "", "", this.encoding);
		startElementEnd("", "meta", "", atts);
	}
	
	public void printHeadMeta(String name,String content) throws SAXException {
		AttributesImpl atts = new AttributesImpl();
		atts.addAttribute ("", "name", "", "", name);
		atts.addAttribute ("", "content", "", "", content);
		startElementEnd("", "meta", "", atts);
	}
	
	public void printHeadLinkCss(String cssUrl) throws SAXException {
		AttributesImpl atts = new AttributesImpl();
		atts.addAttribute ("", "rel", "", "", "stylesheet");
		atts.addAttribute ("", "type", "", "", "text/css");
		atts.addAttribute ("", "title", "", "", "Style");
		atts.addAttribute ("", "href", "", "", cssUrl);
		startElementEnd("", "link", "", atts);
	}
	
	public void printScriptInline(String script) throws SAXException {
		AttributesImpl atts = new AttributesImpl();
		atts.addAttribute ("", "type", "", "", "text/javascript");
		printTagStart(Tag.script,atts);
		comment(script);
		printTagEnd(Tag.script);
	}
	
	public void printScriptNoDiv() throws SAXException {
		printScriptNoDiv(null);
	}
	
	public void printScriptNoDiv(String text) throws SAXException {
		if (text==null) {
			text = "JavaScript is disabled on your browser.";
		}
		printTagStart(Tag.noscript);
			printTagStart(Tag.div);characters(text);printTagEnd(Tag.div);
		printTagEnd(Tag.noscript);
	}

	public void printHrefNamed(String name) throws SAXException {
		AttributesImpl atts = new AttributesImpl();
		atts.addAttribute ("", "name", "", "", name);
		printTagStart(Tag.a,atts);
		comment(" ");
		printTagEnd(Tag.a);
	}
	
	public void printHrefTarget(String href,String title,String target) throws SAXException {
		AttributesImpl atts = new AttributesImpl();
		atts.addAttribute ("", "href", "", "", href);
		atts.addAttribute ("", "target", "", "", target);
		printTagStart(Tag.a,atts);
		characters(title);
		printTagEnd(Tag.a);
	}
	
	public void printHref(String href,String title) throws SAXException {
		printHref(href,title,title);
	}
	
	public void printHref(String href,String title,String text) throws SAXException {
		printHref(href,title,text,null);
	}
	
	public void printHref(String href,String title,String text,String spanClass) throws SAXException {
		AttributesImpl atts = new AttributesImpl();
		atts.addAttribute ("", "href", "", "", href);
		if (title!=null) {
			atts.addAttribute ("", "title", "", "", title);
		}
		printTagStart(Tag.a,atts);
		if (spanClass!=null) {
			atts = new AttributesImpl();
			if (spanClass.length()>0) {
				atts.addAttribute ("", "class", "", "", spanClass);
			}
			printTagStart(Tag.span,atts);
		}
		characters(text);
		if (spanClass!=null) {
			printTagEnd(Tag.span);
		}
		printTagEnd(Tag.a);
	}

	
	private void printTagText(String tag,String text) throws SAXException {
		startElement ("", tag, "", EMPTY_ATTRIBUTES);
		characters(text);
		endElement ("",tag , "");
	}
	
	public void printTagText(Tag tag,String text) throws SAXException {
		printTagText(tag,text,null);
	}
	
	public void printTagText(Tag tag,String text,String tagClass) throws SAXException {
		printTagText(tag,text,tagClass,null);
	}
	
	public void printTagText(Tag tag,String text,String tagClass,String tagId) throws SAXException {
		printTagStart(tag,tagClass,tagId);
		if (text==null) {
			text = " ";
		}
		characters(text);
		printTagEnd(tag);
	}
	
	public void printTagStartEnd(Tag tag) throws SAXException {
		printTagStart(tag,null,null);
		printTagEnd(tag);
	}
	
	public void printTagStart(Tag tag) throws SAXException {
		printTagStart(tag,null,null);
	}
	
	public void printTagStart(Tag tag,String tagClass) throws SAXException {
		printTagStart(tag,tagClass,null);
	}
	
	public void printTagStart(Tag tag,String tagClass,String tagId) throws SAXException {
		printTagStart(tag,tagClass,tagId,null);
	}
	
	public void printTagStart(Tag tag,String tagClass,String tagId,String typeId) throws SAXException {
		AttributesImpl atts = new AttributesImpl();
		if (tagId!=null && tagId.length()>0) {
			atts.addAttribute ("", "id", "", "", tagId);
		}
		if (tagClass!=null && tagClass.length()>0) {
			atts.addAttribute ("", "class", "", "", tagClass);
		}
		if (typeId!=null && typeId.length()>0) {
			atts.addAttribute ("", "type", "", "", typeId);
		}
		printTagStart(tag,atts);
	}
	
	public void printTagStart(Tag tag,Attributes atts) throws SAXException {
		startElement ("", tag.name(), "", atts);
	}
	
	public void printTagEnd(Tag tag) throws SAXException {
		endElement ("",tag.name() , "");
	}
	
	public enum Tag {
		
		/* Deprecated TAGS */
		frameset,frame,noframes,tt,font,dir,center,strike,
		big,basefont,acronym,applet,iframe,
		
		/* HTML 4 TAGS */
		html,head,title,meta,link,base,body,script,style,
		
		h1,h2,h3,h4,h5,h6,
		a,div,span,p,pre,img,hr,br,
		b,em,strong,small,noscript,
		
		ul,li,dl,dt,dd,ol,
		table,thead,tfoot,tbody,caption,th,tr,td,
		
		abbr,address,area,bdo,blockquote,
		cite,code,col,colgroup,del,dfn,i,ins,
		kbd,legend,map,menu,object,param,
		optgroup,q,s,samp,sub,u,var,
		
		form,fieldset,input,option,
		label,button,select,textarea,
		
		/* HTML 5 TAGS */
		canvas,audio,video,source,embed,track,
		datalist,keygen,output,
		article,aside,bdi,command,details,dialog,summary,
		figure,figcaption,footer,header,hgroup,mark,meter,
		nav,progress,ruby,rt,rp,section,time,wbr
	}
	
	private final static String DOCTYPE_NAME = "HTML PUBLIC";
	public enum DocType {
		/* Order from worst to better. */
		HTML_5("html","",""),
		
		HTML_4_FRAMESET(DOCTYPE_NAME,"\"-//W3C//DTD HTML 4.01 Frameset//EN\"","http://www.w3.org/TR/html4/frameset.dtd"),
		HTML_4_TRANSITIONAL(DOCTYPE_NAME,"\"-//W3C//DTD HTML 4.01 Transitional//EN\"","http://www.w3.org/TR/html4/loose.dtd"),
		HTML_4_STRICT(DOCTYPE_NAME,"\"-//W3C//DTD HTML 4.01//EN\"","http://www.w3.org/TR/html4/strict.dtd"),

		XHTML_1_FRAMESET(DOCTYPE_NAME,"\"-//W3C//DTD XHTML 1.0 Frameset//EN\"","http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd"),
		XHTML_1_TRANSITIONAL(DOCTYPE_NAME,"\"-//W3C//DTD XHTML 1.0 Transitional//EN\"","http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"),
		XHTML_1_STRICT(DOCTYPE_NAME,"\"-//W3C//DTD XHTML 1.0 Strict//EN\"","http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd"),
		
		XHTML_11(DOCTYPE_NAME,"\"-//W3C//DTD XHTML 1.1//EN\"","http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd");
		
		private final String name;
		private final String publicId;
		private final String systemId;
		private DocType(String name, String publicId, String systemId) {
			this.name=name;
			this.publicId=publicId;
			this.systemId=systemId;
		}
		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}
		/**
		 * @return the publicId
		 */
		public String getPublicId() {
			return publicId;
		}
		/**
		 * @return the systemId
		 */
		public String getSystemId() {
			return systemId;
		}
	}
}
