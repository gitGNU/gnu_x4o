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

package org.x4o.xml.test.models;

import java.util.ArrayList;
import java.util.List;

public class TestObjectRoot {
	
	private List<TestObjectChild> testObjectChilds = new ArrayList<TestObjectChild>(2);
	private List<TestObjectParent> testObjectParents = new ArrayList<TestObjectParent>(2);
	private List<TestBean> testBeans = new ArrayList<TestBean>(2);
	private List<Object> testObjects = new ArrayList<Object>(2);
	
	public void addChild(TestObjectChild c) {
		testObjectChilds.add(c);
	}
	
	public void addParent(TestObjectParent c) {
		testObjectParents.add(c);
	}
	
	public void addTestBean(TestBean c) {
		testBeans.add(c);
	}
	
	public void addObject(Object c) {
		testObjects.add(c);
	}

	/**
	 * @return the testObjectChilds
	 */
	public List<TestObjectChild> getTestObjectChilds() {
		return testObjectChilds;
	}

	/**
	 * @return the testObjectParents
	 */
	public List<TestObjectParent> getTestObjectParents() {
		return testObjectParents;
	}

	/**
	 * @return the testBeans
	 */
	public List<TestBean> getTestBeans() {
		return testBeans;
	}

	/**
	 * @return the testObjects
	 */
	public List<Object> getTestObjects() {
		return testObjects;
	}
	
	
}
