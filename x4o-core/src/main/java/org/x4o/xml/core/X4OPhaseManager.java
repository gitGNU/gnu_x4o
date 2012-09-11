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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.x4o.xml.core.config.X4OLanguageProperty;
import org.x4o.xml.element.Element;
import org.x4o.xml.element.ElementLanguage;


/**
 * X4OPhaseManager stores the X4OPhaseHandler and puts them in the right order
 * and will execute the phases when runPhases is called.
 * 
 * @author Willem Cazander
 * @version 1.0 Jan 6, 2008
 */
public class X4OPhaseManager {
	
	/** The X4OPhaseHandler */
	private List<X4OPhaseHandler> x4oPhases = null;
	private ElementLanguage elementLanguage = null;
	private X4OPhase stopPhase = null;
	private boolean skipReleasePhase = false;
	private boolean skipRunPhase = false;
	private boolean skipSiblingsPhase = false;
	
	/**
	 * Local package constuctor
	 */
	public X4OPhaseManager(ElementLanguage elementLanguage) {
		if (elementLanguage==null) {
			throw new NullPointerException("Can't manage phases with null elementLanguage in constuctor.");
		}
		x4oPhases = new ArrayList<X4OPhaseHandler>(15);
		this.elementLanguage = elementLanguage;
		
		// Manual
		skipReleasePhase = elementLanguage.getLanguageConfiguration().getLanguagePropertyBoolean(X4OLanguageProperty.PHASE_SKIP_RELEASE);
		skipRunPhase = elementLanguage.getLanguageConfiguration().getLanguagePropertyBoolean(X4OLanguageProperty.PHASE_SKIP_RUN);
		skipSiblingsPhase = elementLanguage.getLanguageConfiguration().getLanguagePropertyBoolean(X4OLanguageProperty.PHASE_SKIP_SIBLINGS);
		
		// Check if we need to stop after a certain phase
		Object stopPhaseObject = elementLanguage.getLanguageConfiguration().getLanguageProperty(X4OLanguageProperty.PHASE_STOP_AFTER);
		if (stopPhaseObject instanceof X4OPhase) {
			stopPhase = (X4OPhase)stopPhaseObject;
		}
	}
	
	/**
	 * Adds an X4OPhaseHandler.
	 * @param phase	The X4OPhaseHandler to add.
	 */
	public void addX4OPhaseHandler(X4OPhaseHandler phase) {
		if (phase==null) {
			throw new NullPointerException("Can't add null phase handler.");
		}
		//  context is created in first phase.
		if (X4OPhase.FIRST_PHASE.equals(elementLanguage.getCurrentX4OPhase())==false) {
			throw new IllegalStateException("Can't add new phases after first phase is completed.");
		}
		x4oPhases.add(phase);
	}
	
	/**
	 * Returns all the X4OPhaseHandlers.
	 * @return	Returns all X4OPhaseHandlers.
	 */
	protected List<X4OPhaseHandler> getPhases() {
		return x4oPhases;
	}

	/**
	 * Returns all the X4OPhaseHandlers in ordered list.
	 * @return	Returns all X4OPhaseHandler is order.
	 */
	protected List<X4OPhaseHandler> getOrderedPhases() {
		List<X4OPhaseHandler> result = new ArrayList<X4OPhaseHandler>(x4oPhases);
		Collections.sort(result,new X4OPhaseHandlerComparator());
		return result;
	}

	/**
	 * Runs all the phases in the right order.
	 * @throws X4OPhaseException
	 */
	public void runPhases() throws X4OPhaseException {

		// sort for the order
		List<X4OPhaseHandler> x4oPhasesOrder = getOrderedPhases();
		
		// debug output
		if (elementLanguage.getLanguageConfiguration().getX4ODebugWriter()!=null) {
			elementLanguage.getLanguageConfiguration().getX4ODebugWriter().debugPhaseOrder(x4oPhases);
		}
		
		// run the phases in ordered order
		for (X4OPhaseHandler phaseHandler:x4oPhasesOrder) {

			if (skipReleasePhase && X4OPhase.releasePhase.equals(phaseHandler.getX4OPhase())) {
				continue; // skip always release phase when requested by property
			}
			if (skipRunPhase && X4OPhase.runPhase.equals(phaseHandler.getX4OPhase())) {
				continue; // skip run phase on request
			}
			if (skipSiblingsPhase && X4OPhase.createLanguageSiblingsPhase.equals(phaseHandler.getX4OPhase())) {
				continue; // skip loading sibling languages
			}
			
			// debug output
			elementLanguage.setCurrentX4OPhase(phaseHandler.getX4OPhase());
			 
			// run listeners
			for (X4OPhaseListener l:phaseHandler.getX4OPhaseListeners()) {
				l.preRunPhase(phaseHandler, elementLanguage);
			}
			
			// always run endRunPhase for valid debug xml
			try {
				// do the run interface
				phaseHandler.runPhase(elementLanguage);
				
				//  run the element phase if possible
				executePhaseElement(phaseHandler);
			} finally {
				// run the listeners again
				for (X4OPhaseListener l:phaseHandler.getX4OPhaseListeners()) {
					l.endRunPhase(phaseHandler, elementLanguage);
				}
			}
			
			if (stopPhase!=null && stopPhase.equals(phaseHandler.getX4OPhase())) {
				return; // we are done
			}
		}
	}
	
	public void runPhasesForElement(Element e,X4OPhase p) throws X4OPhaseException {
		
		// sort for the order
		List<X4OPhaseHandler> x4oPhasesOrder = getOrderedPhases();
		for (X4OPhaseHandler phaseHandler:x4oPhasesOrder) {
			if (phaseHandler.getX4OPhase().equals(p)==false) {
				continue; // we start running all phases from specified phase
			}
			if (X4OPhase.releasePhase.equals(phaseHandler.getX4OPhase())) {
				continue; // skip always release phase in dirty extra runs.
			}
			if (skipRunPhase && X4OPhase.runPhase.equals(phaseHandler.getX4OPhase())) {
				continue; // skip run phase on request
			}
			
			// set phase
			elementLanguage.setCurrentX4OPhase(phaseHandler.getX4OPhase());
			
			// do the run interface
			phaseHandler.runPhase(elementLanguage);
			
			//  run the element phase if possible
			executePhaseElement(phaseHandler);
			
			if (stopPhase!=null && stopPhase.equals(phaseHandler.getX4OPhase())) {
				return; // we are done
			}
		}
	}
	
	public void doReleasePhaseManual() throws X4OPhaseException {
		if (skipReleasePhase==false) {
			throw new IllegalStateException("No manual release requested.");
		}
		if (elementLanguage.getRootElement()==null) {
			return; // no  root element , empty xml document ?
		}
		if (elementLanguage.getRootElement().getElementClass()==null) {
			throw new IllegalStateException("Release phase has already been runned.");
		}
		
		X4OPhaseHandler h = null;
		for (X4OPhaseHandler phase:x4oPhases) {
			if (phase.getX4OPhase().equals(X4OPhase.releasePhase)) {
				h = phase;
				break;
			}
		}
		if (h==null) {
			throw new IllegalStateException("No release phase found in manager to run.");
		}
		
		// set phase
		elementLanguage.setCurrentX4OPhase(h.getX4OPhase());
		
		// do the run interface
		h.runPhase(elementLanguage);
		
		//  run the element phase if possible
		executePhaseElement(h);
	}
	
	class X4OPhaseHandlerComparator implements Comparator<X4OPhaseHandler> {
		/**
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		public int compare(X4OPhaseHandler e1, X4OPhaseHandler e2) {
			
			X4OPhase p1 = e1.getX4OPhase();
			X4OPhase p2 = e2.getX4OPhase();
			
			if (p1==p2) {
				return 0;
			}
			if (p1==X4OPhase.debugPhase) {
				return 0;
			}
			if (p2==X4OPhase.debugPhase) {
				return 0;
			}
			
			int i1 = phaseOrder(p1);
			int i2 = phaseOrder(p2);
			
			if (i1>i2) {
				return 1;
			}
			if (i1<i2) {
				return -1;
			}
			return 0;
		}
		
		private int phaseOrder(X4OPhase check) {
			int result=0;
			for (X4OPhase p:X4OPhase.PHASE_ORDER) {
				if (p==check) {
					return result;
				}
				result++;
			}
			return -1;
		}
		
	}

	private void executePhaseElement(X4OPhaseHandler phase) throws X4OPhaseException {
		if (elementLanguage.getRootElement()==null) {
			return;
		}
		executePhaseTree(elementLanguage.getRootElement(),phase);
	}
	
	/**
	 * todo: rewrite to itterator for big deep trees
	 * 
	 * @param element
	 * @param phase
	 * @throws X4OPhaseException
	 */
	private void executePhaseTree(Element element,X4OPhaseHandler phase) throws X4OPhaseException {
		if (element.getElementClass().getSkipPhases().contains(phase.getX4OPhase().name())==false) {
			phase.runElementPhase(element);	
		}
		for (Element e:element.getChilderen()) {
			executePhaseTree(e,phase);
		}
	}
}
