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
package org.x4o.xml.eld.doc.api.dom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * ApiDocRemoteUrl defines the javadoc package-list remote url.
 * 
 * @author Willem Cazander
 * @version 1.0 Aug 15, 2013
 */
public class ApiDocRemoteClass {

	public final static String REMOTE_FILE = "package-list";
	private Logger logger = Logger.getLogger(ApiDocRemoteClass.class.getName());
	private String docUrl = null;
	private String packageListUrl = null;
	private List<String> packageList = null;
	
	private ApiDocRemoteClass() {
		packageList = new ArrayList<String>(100);
	}
	
	public ApiDocRemoteClass(String docUrl) {
		this();
		setDocUrl(docUrl);
	}
	
	public ApiDocRemoteClass(String docUrl,String packageListUrl) {
		this(docUrl);
		setPackageListUrl(packageListUrl);
	}
	
	/**
	 * Returns remote url for a class or null if no remote package excists for the class.
	 * @param cls	The class to get the remote url for.
	 * @return	The remote url of the class requested or null if none if found.
	 */
	public String getRemoteUrl(Class<?> cls) {
		if (cls==null) {
			return null;
		}
		if (cls.isArray()) {
			return null;
		}
		if (cls.getPackage()==null) {
			return null;
		}
		String packageName = cls.getPackage().getName();
		logger.fine("Search "+packageName+" in "+packageList.size()+" of "+docUrl);
		if (packageList.contains(packageName)) {
			String baseUrl = getDocUrlClean();
			String packagePath = packageName.replaceAll("\\.", "/");
			String fullUrl = baseUrl+packagePath+"/"+cls.getSimpleName()+".html";
			return fullUrl;
		}
		return null;
	}
	
	private String getDocUrlClean() {
		String baseUrl = getDocUrl();
		if (baseUrl.endsWith("/")==false) {
			baseUrl += "/";
		}
		return baseUrl;
	}
	
	public void parseRemotePackageList() throws IOException {
		packageList.clear();
		String baseUrl = getDocUrlClean();
		baseUrl += REMOTE_FILE;
		URL url = new URL(baseUrl);
		URLConnection conn = url.openConnection();
		conn.setConnectTimeout(2000);
		conn.setReadTimeout(2000);
		parseRemoteFile(conn.getInputStream(), conn.getContentEncoding());
	}
	
	private void parseRemoteFile(InputStream in,String enc) throws IOException {
		if (enc==null) {
			enc = "UTF-8";
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(in,Charset.forName(enc)));
		try {
			String line = null;
			while ((line = br.readLine()) != null) {
				String lineClean = line.trim();
				packageList.add(lineClean);
				logger.finer("Adding remote package: '"+lineClean+"'");
			}
		} finally {
			br.close();
		}
	}
	
	/**
	 * @return the docUrl
	 */
	public String getDocUrl() {
		return docUrl;
	}

	/**
	 * @param docUrl the docUrl to set
	 */
	public void setDocUrl(String docUrl) {
		this.docUrl = docUrl;
	}
	
	/**
	 * @return the packageListUrl
	 */
	public String getPackageListUrl() {
		return packageListUrl;
	}
	
	/**
	 * @param packageListUrl the packageListUrl to set
	 */
	public void setPackageListUrl(String packageListUrl) {
		this.packageListUrl = packageListUrl;
	}
	
	/**
	 * @return the packageList
	 */
	public List<String> getPackageList() {
		return packageList;
	}
}
