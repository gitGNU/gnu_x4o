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

package	org.x4o.xml.lang.phase;

import java.util.List;

import org.x4o.xml.element.Element;
import org.x4o.xml.lang.X4OLanguageContext;
import org.x4o.xml.lang.phase.X4OPhaseFactory.X4OPhaseReadRunConfigurator;


/**
 * X4OPhaseManagerFactory
 * 
 * @author Willem Cazander
 * @version 1.0 Apr 30, 2013
 */
public class X4OPhaseManagerFactory {
	
	static public X4OPhaseManager createDefaultX4OPhaseManager() {
		X4OPhaseFactory factory = new X4OPhaseFactory();
		DefaultX4OPhaseManager phaseManager = new DefaultX4OPhaseManager();
		createPhasesInit(phaseManager,factory);
		createPhasesRead(phaseManager,factory);
		return phaseManager;
	}
	
	static private void createPhasesInit(DefaultX4OPhaseManager manager,X4OPhaseFactory factory) {
		manager.addX4OPhase(factory.initX4OPhase());
		manager.addX4OPhase(factory.createLanguagePhase());
		manager.addX4OPhase(factory.createLanguageSiblingsPhase());
	}

	static private void createPhasesRead(DefaultX4OPhaseManager manager,X4OPhaseFactory factory) {
		// main startup
		manager.addX4OPhase(factory.readStartX4OPhase());
		//manager.addX4OPhase(factory.createLanguagePhase());
		//manager.addX4OPhase(factory.createLanguageSiblingsPhase());
		manager.addX4OPhase(factory.readSAXStreamPhase());
		
		// inject and opt phase
		manager.addX4OPhase(factory.configGlobalElBeansPhase());
//		if (elementLanguage.hasX4ODebugWriter()) {manager.addX4OPhaseHandler(factory.debugPhase());}
		
		// meta start point
//		manager.addX4OPhase(factory.startX4OPhase());
//		if (elementLanguage.hasX4ODebugWriter()) {manager.addX4OPhaseHandler(factory.debugPhase());}
		
		// config
		X4OPhaseReadRunConfigurator runConf = factory.readRunConfiguratorPhase();
		manager.addX4OPhase(factory.configElementPhase(runConf));
		manager.addX4OPhase(factory.configElementInterfacePhase(runConf));
		manager.addX4OPhase(factory.configGlobalElementPhase(runConf));
		manager.addX4OPhase(factory.configGlobalAttributePhase(runConf));
		
		// run all attribute events
		manager.addX4OPhase(factory.runAttributesPhase());
//		if (elementLanguage.hasX4ODebugWriter()) {manager.addX4OPhaseHandler(factory.debugPhase());}
		// templating
		manager.addX4OPhase(factory.fillTemplatingPhase());
//		if (elementLanguage.hasX4ODebugWriter()) {manager.addX4OPhaseHandler(factory.debugPhase());}

		// transforming
		manager.addX4OPhase(factory.transformPhase());
		manager.addX4OPhase(factory.runDirtyElementPhase(manager));
//		if (elementLanguage.hasX4ODebugWriter()) {manager.addX4OPhaseHandler(factory.debugPhase());}
		
		// binding elements
		manager.addX4OPhase(factory.bindElementPhase());
		
		// runing and releasing
		manager.addX4OPhase(factory.runPhase());
//		if (elementLanguage.hasX4ODebugWriter()) {manager.addX4OPhaseHandler(factory.debugPhase());}
		
		manager.addX4OPhase(runConf);
		
		manager.addX4OPhase(factory.runDirtyElementLastPhase(manager));
//		if (elementLanguage.hasX4ODebugWriter()) {manager.addX4OPhaseHandler(factory.debugPhase());}
		
		
		manager.addX4OPhase(factory.readEndX4OPhase());
		
		// after release phase Element Tree is not avible anymore
		manager.addX4OPhase(factory.releasePhase());
		
		// Add debug phase listener to all phases
//		if (elementLanguage.hasX4ODebugWriter()) {
			//for (X4OPhase h:manager.getOrderedPhases()) {
//				h.addPhaseListener(elementLanguage.getX4ODebugWriter().createDebugX4OPhaseListener());
			//}
		//}
		
		// We are done
	}
	
	enum R {
		
		/** Defines this meta startup phase. */
		startupX4OPhase(true),
		
		/** Load all meta info of the language we are creating. */
		createLanguagePhase(true),
		
		/** Load all siblings languages.  */
		createLanguageSiblingsPhase(true),
		
		/** Parse the xml from sax events. */
		parseSAXStreamPhase(true),
		
		/** Optional extra config phase for injecting bean instances into the EL context. */
		configGlobalElBeansPhase(true),
		
		/** emty meta phase to refer to that sax is ready and element s are waiting for processing. */
		startX4OPhase(true),
		
		/** re runnable phases which config xml to beans and binds them together. */
		configElementPhase,
		configElementInterfacePhase,
		configGlobalElementPhase,
		configGlobalAttributePhase,
		
		/** Fill the bean attributes from the Element xml attributes. */
		runAttributesPhase,
		
		/** Fill in the x4o templating objects. */
		fillTemplatingPhase,
		
		/** transform phase , modifies the Element Tree. */
		transformPhase,
		
		/** Run the phases which needs to be runned again from a phase. */
		runDirtyElementPhase(true),
		
		/** Binds objects together */
		bindElementPhase,
		
		/** Run action stuff, we are ready with it. */
		runPhase(true),
		
		/** Rerun all needed phases for all element that requested it. */
		runDirtyElementLastPhase,
		
		/** Releases all Elements, which clears attributes and childeren etc. */
		releasePhase(true),
		
		/** write all phases and stuff to debug sax stream. */
		debugPhase;
		
		/** Defines which phase we start, when context is created. */
		public static final R FIRST_PHASE = startupX4OPhase;
		
		/** The order in which the phases are executed */
		static final R[] PHASE_ORDER = {	startupX4OPhase,
													createLanguagePhase,
													createLanguageSiblingsPhase,
													
														parseSAXStreamPhase,
													configGlobalElBeansPhase,
												startX4OPhase,
													configElementPhase,configElementInterfacePhase,configGlobalElementPhase,
													configGlobalAttributePhase,runAttributesPhase,fillTemplatingPhase,
													transformPhase,runDirtyElementPhase,bindElementPhase,
													runPhase,runDirtyElementLastPhase,
												releasePhase
												};
		
		/** Boolean indicating that this phase only may be run'ed once. */
		private boolean runOnce = false;
		
		/**
		 * Creates an X4O Phase.
		 */
		private R() {
		}
		
		/**
		 * Creates an X4O Phase
		 * @param runOnce	Flag indicating that this phase is runnable multiple times.
		 */
		private R(boolean runOnce) {
			this.runOnce=runOnce;
		}
		
		/**
		 * Returns a flag indicating that this phase is runnable multiple times.
		 * @return	True if phase is restricted to run once.
		 */
		public boolean isRunOnce() {
			return runOnce;
		}
	}
}
