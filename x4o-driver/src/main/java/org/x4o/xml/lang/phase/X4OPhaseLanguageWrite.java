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

import java.util.logging.Logger;

import org.x4o.xml.element.Element;
import org.x4o.xml.element.ElementBindingHandler;
import org.x4o.xml.element.ElementBindingHandlerException;
import org.x4o.xml.element.ElementClass;
import org.x4o.xml.element.ElementException;
import org.x4o.xml.element.ElementNamespace;
import org.x4o.xml.element.ElementNamespaceInstanceProviderException;
import org.x4o.xml.lang.X4OLanguageModule;
import org.x4o.xml.lang.X4OLanguageSession;

/**
 * X4OPhaseLanguageWrite defines all phases to write the language.
 * 
 * @author Willem Cazander
 * @version 1.0 Apr 7, 2013
 */
public class X4OPhaseLanguageWrite {

	private Logger logger = null;
	public static final String WRITE_BEGIN = "WRITE_BEGIN";
	public static final String WRITE_FILL_TREE = "WRITE_FILL_TREE";
	public static final String WRITE_END = "WRITE_END";
	public static final String WRITE_RELEASE = "WRITE_RELEASE";
	
	public X4OPhaseLanguageWrite() {
		logger = Logger.getLogger(X4OPhaseLanguageWrite.class.getName());
	}
	
	public void createPhases(DefaultX4OPhaseManager manager) {
		manager.addX4OPhase(new X4OPhaseWriteBegin());
		manager.addX4OPhase(new X4OPhaseWriteFillTree());
		manager.addX4OPhase(new X4OPhaseWriteEnd());
		//manager.addX4OPhase(new X4OPhaseWriteRelease());
	}
	
	/**
	 * Creates the X4OPhaseWriteBegin which is a empty meta phase.
	 */
	class X4OPhaseWriteBegin extends AbstractX4OPhase {
		public X4OPhaseType getType() {
			return X4OPhaseType.XML_WRITE;
		}
		public String getId() {
			return WRITE_BEGIN;
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
			logger.finest("Run init begin phase");
		}
	};
	
	/**
	 * Fills the element tree
	 */
	class X4OPhaseWriteFillTree extends AbstractX4OPhase {
		public X4OPhaseType getType() {
			return X4OPhaseType.XML_WRITE;
		}
		public String getId() {
			return WRITE_FILL_TREE;
		}
		public String[] getPhaseDependencies() {
			return new String[]{WRITE_BEGIN};
		}
		public boolean isElementPhase() {
			return false;
		}
		public void runElementPhase(Element element) throws X4OPhaseException {
		}
		public void runPhase(X4OLanguageSession languageSession) throws X4OPhaseException  {
			try {
				Element root = languageSession.getRootElement();
				// TODO: check for read tree then write support as ec is not null then ..
				if (root.getElementClass()==null) {
					root = fillElementTree(languageSession,root.getElementObject());
				}
			} catch (Exception e) {
				throw new X4OPhaseException(this,e);
			}
		}
		
		private Element fillElementTree(X4OLanguageSession languageSession,Object object) throws ElementNamespaceInstanceProviderException, ElementBindingHandlerException {
			Element element = findRootElement(languageSession,object.getClass());
			element.setElementObject(object);
			languageSession.setRootElement(element);
			
			for (ElementBindingHandler bind:languageSession.getLanguage().findElementBindingHandlers(object)) {
				bind.createChilderen(element);
				fillTree(languageSession,element);
			}
			return element;
		}
		
		private void fillTree(X4OLanguageSession languageSession,Element element) throws ElementNamespaceInstanceProviderException, ElementBindingHandlerException {
			for (Element e:element.getChilderen()) {
				Object object = e.getElementObject();
				for (ElementBindingHandler bind:languageSession.getLanguage().findElementBindingHandlers(object)) {
					bind.createChilderen(e);
					fillTree(languageSession,e);
				}
			}
		}
		
		private Element findRootElement(X4OLanguageSession languageSession,Class<?> objectClass) throws ElementNamespaceInstanceProviderException {
			// redo this mess, add nice find for root
			for (X4OLanguageModule modContext:languageSession.getLanguage().getLanguageModules()) {
				for (ElementNamespace nsContext:modContext.getElementNamespaces()) {
					if (nsContext.getLanguageRoot()!=null && nsContext.getLanguageRoot()) {
						for (ElementClass ec:nsContext.getElementClasses()) {
							if (ec.getObjectClass()!=null && ec.getObjectClass().equals(objectClass)) { 
								return nsContext.getElementNamespaceInstanceProvider().createElementInstance(languageSession, ec.getId());
							}
						}
					}
				}
			}
			for (X4OLanguageModule modContext:languageSession.getLanguage().getLanguageModules()) {
				for (ElementNamespace nsContext:modContext.getElementNamespaces()) {
					for (ElementClass ec:nsContext.getElementClasses()) {
						if (ec.getObjectClass()!=null && ec.getObjectClass().equals(objectClass)) { 
							return nsContext.getElementNamespaceInstanceProvider().createElementInstance(languageSession, ec.getId());
						}
					}
				}
			}
			throw new IllegalArgumentException("Could not find ElementClass for: "+objectClass.getName());
		}
	};
	
	/**
	 * Creates the X4OPhaseWriteEnd which is a empty meta phase.
	 */
	class X4OPhaseWriteEnd extends AbstractX4OPhase {
		public X4OPhaseType getType() {
			return X4OPhaseType.XML_WRITE;
		}
		public String getId() {
			return WRITE_END;
		}
		public String[] getPhaseDependencies() {
			return new String[]{WRITE_FILL_TREE};
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
	
	class X4OPhaseWriteRelease extends AbstractX4OPhase {
		public X4OPhaseType getType() {
			return X4OPhaseType.XML_WRITE;
		}
		public String getId() {
			return WRITE_RELEASE;
		}
		public String[] getPhaseDependencies() {
			return new String[] {WRITE_END};
		}
		public void runPhase(X4OLanguageSession languageSession) throws X4OPhaseException {
		}
		public void runElementPhase(Element element) throws X4OPhaseException  {
			try {
				element.release();
			} catch (ElementException e) {
				throw new X4OPhaseException(this,e);
			}/* finally {
				releaseCounter.addReleasedElement();
			}*/
		}
	}
}
