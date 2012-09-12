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

package	org.x4o.xml.eld;

import org.x4o.xml.core.X4OParserSupport;
import org.x4o.xml.core.X4OParserSupportException;
import org.x4o.xml.element.ElementLanguage;

/**
 * EldParserSupportCore can write the cel schema.
 * 
 * @author Willem Cazander
 * @version 1.0 Aug 22, 2012
 */
public class EldParserSupportCore implements X4OParserSupport {
	
	/**
	 * Loads the ElementLanguage of this language parser for support.
	 * @return	The loaded ElementLanguage.
	 * @throws X4OParserSupportException When support language could not be loaded.
	 * @see org.x4o.xml.core.X4OParserSupport#loadElementLanguageSupport()
	 */
	public ElementLanguage loadElementLanguageSupport() throws X4OParserSupportException {
		EldParser parser = new EldParser(true);
		return parser.loadElementLanguageSupport();
	}
}
