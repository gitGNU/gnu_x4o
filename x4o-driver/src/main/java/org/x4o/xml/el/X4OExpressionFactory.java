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

package org.x4o.xml.el;

import javax.el.ELContext;
import javax.el.ExpressionFactory;

import org.x4o.xml.lang.X4OLanguageClassLoader;
import org.x4o.xml.lang.X4OLanguageContext;
import org.x4o.xml.lang.X4OLanguageProperty;

/**
 * X4OExpressionFactory finds and loads the needed impl. 
 * 
 * @author Willem Cazander
 * @version 1.0 Apr 7, 2013
 */
public class X4OExpressionFactory {

	static public final String EL_FACTORY_IMPL_APACHE = "org.apache.el.ExpressionFactoryImpl";
	static public final String EL_FACTORY_IMPL_ODYSSEUS = "de.odysseus.el.ExpressionFactoryImpl";
	
	static public ExpressionFactory createExpressionFactory(X4OLanguageContext languageContext) {
		ExpressionFactory result = (ExpressionFactory)languageContext.getLanguageProperty(X4OLanguageProperty.EL_FACTORY_INSTANCE);
		if (result!=null) {
			return result;
		}
		try {
			Class<?> expressionFactoryClass = X4OLanguageClassLoader.loadClass(EL_FACTORY_IMPL_APACHE);
			result = (ExpressionFactory) expressionFactoryClass.newInstance();
		} catch (Exception e) {
			try {
				Class<?> expressionFactoryClass = X4OLanguageClassLoader.loadClass(EL_FACTORY_IMPL_ODYSSEUS);
				result = (ExpressionFactory) expressionFactoryClass.newInstance();
			} catch (Exception ee) {
				throw new RuntimeException("Could not load ExpressionFactory tried: "+EL_FACTORY_IMPL_APACHE+" and "+EL_FACTORY_IMPL_ODYSSEUS+" but could not load one of them.");
			}
		}
		return result;
	}
	
	static public ELContext createELContext(X4OLanguageContext languageContext) {
		ELContext result = (ELContext)languageContext.getLanguageProperty(X4OLanguageProperty.EL_CONTEXT_INSTANCE); 
		if (result!=null) {
			return result;
		}
		try {
			result = (ELContext)X4OLanguageClassLoader.newInstance(languageContext.getLanguage().getLanguageConfiguration().getDefaultExpressionLanguageContext());
		} catch (Exception e) {
			throw new RuntimeException("Could not create instance of ELContext: "+e.getMessage(),e);
		}
		return result;
	}
}
