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
package	org.x4o.xml.lang.phase;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.x4o.xml.element.Element;
import org.x4o.xml.lang.X4OLanguageSession;
import org.x4o.xml.lang.X4OLanguageModuleLoaderSibling;
import org.x4o.xml.lang.X4OLanguageClassLoader;
import org.x4o.xml.lang.X4OLanguageLoader;
import org.x4o.xml.lang.X4OLanguageLocal;

/**
 * X4OPhaseLanguageInit defines all phases to initialize the language.
 * 
 * @author Willem Cazander
 * @version 1.0 Apr 7, 2013
 */
public class X4OPhaseLanguageInit {

	private Logger logger = null;
	public static final String INIT_START = "INIT_START";
	public static final String INIT_LANG = "INIT_LANG";
	public static final String INIT_LANG_SIB = "INIT_LANG_SIB";
	public static final String INIT_END = "INIT_END";
	
	public X4OPhaseLanguageInit() {
		logger = Logger.getLogger(X4OPhaseLanguageInit.class.getName());
	}
	
	public void createPhases(DefaultX4OPhaseManager manager) {
		manager.addX4OPhase(new X4OPhaseInitStart());
		manager.addX4OPhase(new X4OPhaseInitLanguage());
		manager.addX4OPhase(new X4OPhaseInitLanguageSiblings());
		manager.addX4OPhase(new X4OPhaseInitEnd());
	}
	
	/**
	 * Creates the X4OPhaseInitStart which is a empty meta phase.
	 */
	class X4OPhaseInitStart extends AbstractX4OPhase {
		public X4OPhaseType getType() {
			return X4OPhaseType.INIT;
		}
		public String getId() {
			return INIT_START;
		}
		public String[] getPhaseDependencies() {
			return new String[]{};
		}
		public boolean isElementPhase() {
			return false;
		}
		public void runElementPhase(Element element) throws X4OPhaseException {
		}
		public void runPhase(X4OLanguageSession languageSession) throws X4OPhaseException  {
			logger.finest("Run init start phase");
		}
	};
	
	/**
	 * Loads all the modules a language.
	 * Then creates the ElementProviders
	 */
	class X4OPhaseInitLanguage extends AbstractX4OPhase {
		public X4OPhaseType getType() {
			return X4OPhaseType.INIT;
		}
		public String getId() {
			return INIT_LANG;
		}
		public String[] getPhaseDependencies() {
			return new String[]{INIT_START};
		}
		public boolean isElementPhase() {
			return false;
		}
		public void runElementPhase(Element element) throws X4OPhaseException {
		}
		public void runPhase(X4OLanguageSession languageSession) throws X4OPhaseException  {
			try {
				//debugPhaseMessage("Loading main language: "+elementLanguage.getLanguage(),this,elementLanguage);
				X4OLanguageLoader loader = (X4OLanguageLoader)X4OLanguageClassLoader.newInstance(languageSession.getLanguage().getLanguageConfiguration().getDefaultLanguageLoader());
				loader.loadLanguage((X4OLanguageLocal)languageSession.getLanguage(),languageSession.getLanguage().getLanguageName(),languageSession.getLanguage().getLanguageVersion());
				
				if (languageSession.hasX4ODebugWriter()) {
					languageSession.getX4ODebugWriter().debugElementLanguageModules(languageSession);
				}
			} catch (Exception e) {
				throw new X4OPhaseException(this,e);
			}
		}
	};
	
	/**
	 * Loads all sibling languages.
	 */
	public class X4OPhaseInitLanguageSiblings extends AbstractX4OPhase {
		private List<X4OLanguageModuleLoaderSibling> siblingLoaders = new ArrayList<X4OLanguageModuleLoaderSibling>(2);
		// mmmm think of better then cast
		public void addLanguageModuleLoaderSibling(X4OLanguageModuleLoaderSibling loader) {
			siblingLoaders.add(loader);
		}
		public X4OPhaseType getType() {
			return X4OPhaseType.INIT;
		}
		public String getId() {
			return INIT_LANG_SIB;
		}
		public String[] getPhaseDependencies() {
			return new String[] {INIT_LANG};
		}
		public boolean isElementPhase() {
			return false;
		}
		public void runElementPhase(Element element) throws X4OPhaseException {
		}
		public void runPhase(X4OLanguageSession languageSession) throws X4OPhaseException {
			try {
				if (siblingLoaders.isEmpty()==false) {
					X4OLanguageLoader loader = (X4OLanguageLoader)X4OLanguageClassLoader.newInstance(languageSession.getLanguage().getLanguageConfiguration().getDefaultLanguageLoader());
					for (X4OLanguageModuleLoaderSibling siblingLoader:siblingLoaders) {
						//debugPhaseMessage("Loading sibling langauge loader: "+siblingLoader,this,elementLanguage);
						siblingLoader.loadLanguageSibling((X4OLanguageLocal)languageSession.getLanguage(), loader);
					}
					if (languageSession.hasX4ODebugWriter()) {
						languageSession.getX4ODebugWriter().debugElementLanguageModules(languageSession);
					}
					siblingLoaders.clear();
				}
			} catch (Exception e) {
				throw new X4OPhaseException(this,e);
			}
		}
	};
	
	/**
	 * Creates the X4OPhaseInitEnd which is a empty meta phase.
	 */
	class X4OPhaseInitEnd extends AbstractX4OPhase {
		public X4OPhaseType getType() {
			return X4OPhaseType.INIT;
		}
		public String getId() {
			return INIT_END;
		}
		public String[] getPhaseDependencies() {
			return new String[]{INIT_LANG_SIB};
		}
		public boolean isElementPhase() {
			return false;
		}
		public void runElementPhase(Element element) throws X4OPhaseException {
		}
		public void runPhase(X4OLanguageSession languageSession) throws X4OPhaseException  {
			logger.finest("Run init end phase");
		}
	};
}
