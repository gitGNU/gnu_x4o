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
package	org.x4o.xml.lang;

import javax.el.ELContext;
import javax.el.ExpressionFactory;

import org.x4o.xml.element.ElementAttributeValueParser;
import org.x4o.xml.element.ElementObjectPropertyValue;
import org.x4o.xml.io.sax.X4ODebugWriter;
import org.x4o.xml.lang.phase.X4OPhase;

/**
 * ElementLanguageLocal is the local set interface for ElementLanguage.
 * 
 * @author Willem Cazander
 * @version 1.0 Oct 28, 2009
 */
public interface X4OLanguageContextLocal extends X4OLanguageContext {

	/**
	 * Sets the EL Context.
	 * @param context	The ELContext to set.
	 */
	void setExpressionLanguageContext(ELContext context);
	
	/**
	 * Sets the ExpressionFactory.
	 * @param expressionFactory	The ExpressionFactory to set.
	 */
	void setExpressionLanguageFactory(ExpressionFactory expressionFactory);
	
	/**
	 * @param elementAttributeValueParser The elementAttributeValueParser to set.
	 */
	void setElementAttributeValueParser(ElementAttributeValueParser elementAttributeValueParser);
	
	/**
	 * @param elementObjectPropertyValue	The elementObjectPropertyValue to set.
	 */
	void setElementObjectPropertyValue(ElementObjectPropertyValue elementObjectPropertyValue);
	
	/**
	 * Sets the phase of the context.
	 * @param phase	The current phase to set.
	 */
	void setCurrentPhase(X4OPhase phase);
	
	/**
	 * @param debugWriter	The debug writer to set
	 */
	void setX4ODebugWriter(X4ODebugWriter debugWriter);
}
