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
package org.x4o.xml.lang;

import java.util.List;


/**
 * DefaultX4OLanguageVersionFilter makes best filter match attempt.
 * 
 * @author Willem Cazander
 * @version 1.0 6 Aug 2012
 */
public class DefaultX4OLanguageVersionFilter implements X4OLanguageVersionFilter {

	/**
	 * Filters to the best version.
	 * @see org.x4o.xml.lang.X4OLanguageVersionFilter#filterVersion(java.lang.String, java.util.List)
	 * @return The perfect or best match or null if no match for requested language.
	 * @param requestVersion	The language version to search for.
	 * @param versions The list of version to search in.
	 */
	public String filterVersion(String requestVersion, List<String> versions) {
		for (int i=0;i<versions.size();i++) {
			String version = versions.get(i);
			if (version.equals(requestVersion)) {
				return version; // full match
			}
		}
		String requestVersionDot = requestVersion;
		if (requestVersion.length()>2) {
			requestVersionDot = requestVersion.substring(0,requestVersion.length()-2);
		}
		for (int i=0;i<versions.size();i++) {
			String version = versions.get(i);
			if (version.endsWith(".*")) {
				String versionDot = version.substring(0,version.length()-2);
				if (versionDot.equals(requestVersionDot)) {
					return version; // star match
				}
			}
		}
		return null;
	}
}
