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

/**
 * ApiDocContentCss defines the css style names used in api docs.
 * 
 * @author Willem Cazander
 * @version 1.0 May 20, 2013
 */
public enum ApiDocContentCss {
	
	indexHeader,
	indexContainer,
	
	bar,
	block,
	blockList,
	strong,
	
	topNav,
	bottomNav,
	navList,
	navBarCell1Rev,
	subNav,
	subNavList,
	
	subTitle,
	tabEnd,
	
	aboutLanguage,
	legalCopy,
	
	inheritance,
	header,
	description,
	summary,
	details,
	docSummary,
	contentContainer,
	packageSummary,
	overviewSummary,
	
	colOne,
	colFirst,
	colLast,
	
	altColor,
	rowColor,
	
	// frame names maybe can be used to create frame-less html5 JS version
	frameNavOverview,
	frameNavDetail,
	frameContent
}
