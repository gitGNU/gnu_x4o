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

import org.x4o.xml.core.X4ODriver;
import org.x4o.xml.core.X4OParser;
import org.x4o.xml.core.config.X4OLanguageProperty;
import org.x4o.xml.element.ElementLanguage;
import org.x4o.xml.element.ElementLanguageModule;


/**
 * An Element Language Definition X4O parser.
 * This eld parser config parent x4o parser with the eld x4o parser. 
 * 
 * @author Willem Cazander
 * @version 1.0 Aug 20, 2005
 */
public class EldParser extends X4OParser {

	/** Defines the identifier of the ELD x4o language. */
	public static final String ELD_VERSION = X4ODriver.DEFAULT_LANGUAGE_VERSION;
	
	/** Defines the identifier of the 'Element Language Description' language. */
	public static final String ELD_LANGUAGE = "eld";
	
	/** Defines the identifier of the 'Core Element Language' language. */
	public static final String CEL_LANGUAGE = "cel";
	
	protected EldParser() {
		this(false);
	}
	
	protected EldParser(boolean isEldCore) {
		super(isEldCore?CEL_LANGUAGE:ELD_LANGUAGE,ELD_VERSION);
	}
	
	/**
	 * getDriver for unit tests
	 */
	protected X4ODriver getDriver() {
		return super.getDriver();
	}
	
	/**
	 * Start the second x4o parsing for eld files.
	 * @param elementLanguage	The elementLanguage to fill.
	 * @param elementLanguageModule	The elementLanguageModule from to fill.
	 */
	public EldParser(ElementLanguage elementLanguage,ElementLanguageModule elementLanguageModule) {
		this(false);
		if (elementLanguage.getLanguageConfiguration().getLanguagePropertyBoolean(X4OLanguageProperty.DEBUG_OUTPUT_ELD_PARSER)) {
			getDriver().getElementLanguage().getLanguageConfiguration().setX4ODebugWriter(elementLanguage.getLanguageConfiguration().getX4ODebugWriter()); // move to ..
		}
		addELBeanInstance(PARENT_LANGUAGE_CONFIGURATION, elementLanguage.getLanguageConfiguration());
		addELBeanInstance(PARENT_LANGUAGE_ELEMENT_LANGUAGE, elementLanguage);
		addELBeanInstance(PARENT_ELEMENT_LANGUAGE_MODULE, elementLanguageModule);
	}
	
	public EldParser(ElementLanguage elementLanguage,ElementLanguageModule elementLanguageModule,boolean isEldCore) {
		this(isEldCore);
		if (elementLanguage.getLanguageConfiguration().getLanguagePropertyBoolean(X4OLanguageProperty.DEBUG_OUTPUT_ELD_PARSER)) {
			getDriver().getElementLanguage().getLanguageConfiguration().setX4ODebugWriter(elementLanguage.getLanguageConfiguration().getX4ODebugWriter());
		}
		addELBeanInstance(PARENT_LANGUAGE_CONFIGURATION, elementLanguage.getLanguageConfiguration());
		addELBeanInstance(PARENT_LANGUAGE_ELEMENT_LANGUAGE, elementLanguage);
		addELBeanInstance(PARENT_ELEMENT_LANGUAGE_MODULE, elementLanguageModule);
	}
	
	public final static String PARENT_LANGUAGE_CONFIGURATION = "parentLanguageConfiguration";
	public final static String PARENT_ELEMENT_LANGUAGE_MODULE = "parentElementLanguageModule";
	public final static String PARENT_LANGUAGE_ELEMENT_LANGUAGE = "parentLanguageElementLanguage";
}
