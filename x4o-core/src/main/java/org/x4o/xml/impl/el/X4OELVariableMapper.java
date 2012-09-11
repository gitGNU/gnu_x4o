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

package org.x4o.xml.impl.el;

import java.util.HashMap;
import java.util.Map;

import javax.el.ValueExpression;
import javax.el.VariableMapper;

/**
 * X4OELVariableMapper simple EL variable mapper.
 * 
 * @author Willem Cazander
 * @version 1.0 Sep 14, 2010
 */
public class X4OELVariableMapper extends VariableMapper {
	
	/** Map to hold all the expressions used. */
	private Map<String, ValueExpression> expressions = null;
	
	/**
	 * Creates the X4OELVariableMapper.
	 */
	public X4OELVariableMapper() {
		expressions = new HashMap<String, ValueExpression>();
	}
	
	/**
	 * @see javax.el.VariableMapper#resolveVariable(java.lang.String)
	 */
	@Override
	public ValueExpression resolveVariable(String var) {
		return expressions.get(var);
	}

	/**
	 * @see javax.el.VariableMapper#setVariable(java.lang.String, javax.el.ValueExpression)
	 */
	@Override
	public ValueExpression setVariable(String var, ValueExpression expression) {
		return expressions.put(var, expression);
	}

}