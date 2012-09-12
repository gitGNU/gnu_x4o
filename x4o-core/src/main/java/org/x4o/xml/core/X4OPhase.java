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

package	org.x4o.xml.core;

/**
 * Defines the different phases of the x4o xml parser.
 * 
 * @author Willem Cazander
 * @version 1.0 Sep 1, 2008
 */
public enum X4OPhase {
	
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
	public static final X4OPhase FIRST_PHASE = startupX4OPhase;
	
	/** The order in which the phases are executed */
	static final X4OPhase[] PHASE_ORDER = {	startupX4OPhase,
												createLanguagePhase,createLanguageSiblingsPhase,
												parseSAXStreamPhase,configGlobalElBeansPhase,
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
	private X4OPhase() {
	}
	
	/**
	 * Creates an X4O Phase
	 * @param runOnce	Flag indicating that this phase is runnable multiple times.
	 */
	private X4OPhase(boolean runOnce) {
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
