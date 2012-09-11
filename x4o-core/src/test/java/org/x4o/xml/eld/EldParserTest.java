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

package org.x4o.xml.eld;

import java.util.ArrayList;
import java.util.List;

import org.x4o.xml.core.config.X4OLanguagePropertyKeys;
import org.x4o.xml.eld.EldParser;
import org.x4o.xml.element.Element;
import org.x4o.xml.element.Element.ElementType;
import org.x4o.xml.element.ElementClass;

import junit.framework.TestCase;

/**
 * Tests some code for eld
 * 
 * @author Willem Cazander
 * @version 1.0 Jan 15, 2009
 */
public class EldParserTest extends TestCase {

	public void testRunEldParserCore() throws Exception {
		EldParser parser =  new EldParser(true);
		parser.setProperty(X4OLanguagePropertyKeys.PHASE_SKIP_RELEASE, true);
		try {
			parser.parseResource("META-INF/eld/eld-lang.eld");
			List<String> resultTags = new ArrayList<String>(50);
			for (Element e:parser.getDriver().getElementLanguage().getRootElement().getAllChilderen()) {
				//System.out.println("obj: "+e.getElementObject());
				if (e.getElementType().equals(ElementType.element) && e.getElementObject() instanceof ElementClass) {
					ElementClass ec = (ElementClass)e.getElementObject();
					resultTags.add(ec.getTag());
				}
			}
			//TODO fix test
			/*
			assertTrue("No module tag found in core eld.", 				resultTags.contains("module"));
			assertTrue("No elementClass tag found in core eld.",		resultTags.contains("elementClass"));
			assertTrue("No elementInterface tag found in core eld.",	resultTags.contains("elementInterface"));
			assertTrue("No bean tag found in core eld.",				resultTags.contains("bean"));
			assertTrue("No elementConfigurator tag found in core eld.",	resultTags.contains("elementConfigurator"));
			*/
		} finally {
			parser.doReleasePhaseManual();
		}
	}
	
	public void testRunEldParser() throws Exception {
		EldParser parser =  new EldParser(false);
		parser.parseResource("META-INF/test/test-lang.eld");
	}
}
