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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.x4o.xml.element.Element;
import org.x4o.xml.lang.X4OLanguageSession;
import org.x4o.xml.lang.X4OLanguageSessionLocal;

/**
 * X4OPhaseManager stores the X4OPhaseHandler and puts them in the right order.
 * And will execute the phases when runPhases is called.
 * 
 * @author Willem Cazander
 * @version 1.0 Jan 6, 2008
 */
public class DefaultX4OPhaseManager implements X4OPhaseManager {
	
	/** The X4OPhaseHandler */
	private List<X4OPhase> x4oPhases = null;
	
	/**
	 * Constructor.
	 */
	public DefaultX4OPhaseManager() {
		x4oPhases = new ArrayList<X4OPhase>(25);
	}
/*

PHASE_ORDER = {	*startupX4OPhase,
					*createLanguagePhase,
					*createLanguageSiblingsPhase,
					*parseSAXStreamPhase,
					*configGlobalElBeansPhase,
				*startX4OPhase,
					configElementPhase,configElementInterfacePhase,configGlobalElementPhase,
					configGlobalAttributePhase,runAttributesPhase,fillTemplatingPhase,
					transformPhase,*runDirtyElementPhase,bindElementPhase,
					*runPhase,runDirtyElementLastPhase,
				*releasePhase
				};	
* = runOnce
	
*/
	
	public X4OPhase getPhase(String phaseName) {
		for (X4OPhase phase:x4oPhases) {
			if (phase.getId().equals(phaseName)) {
				return phase;
			}
		}
		return null;
	}
	
	/**
	 * Adds an X4OPhaseHandler.
	 * @param phase	The X4OPhaseHandler to add.
	 */
	public void addX4OPhase(X4OPhase phase) {
		if (phase==null) {
			throw new NullPointerException("Can't add null phase handler.");
		}
		//  context is created in first phase.
		//if (X4OPhase.FIRST_PHASE.equals(elementLanguage.getCurrentX4OPhase())==false) {
//			throw new IllegalStateException("Can't add new phases after first phase is completed.");
		//}
		x4oPhases.add(phase);
	}
	
	/**
	 * Returns all the X4OPhaseHandlers.
	 * @return	Returns all X4OPhaseHandlers.
	 */
	public List<X4OPhase> getAllPhases() {
		return new ArrayList<X4OPhase>(x4oPhases);
	}

	/**
	 * Returns all the X4OPhaseHandlers in ordered list.
	 * @return	Returns all X4OPhaseHandler is order.
	 */
	public List<X4OPhase> getOrderedPhases(X4OPhaseType type) {
		List<X4OPhase> result = new ArrayList<X4OPhase>(x4oPhases.size());
		for (X4OPhase p:x4oPhases) {
			if (p.getType().equals(type)) {
				result.add(p);
			}
		}
		Collections.sort(result,new X4OPhaseComparator());
		return result;
	}

	/**
	 * Runs all the phases in the right order.
	 * @throws X4OPhaseException When a running handlers throws one.
	 */
	public void runPhases(X4OLanguageSession languageSession,X4OPhaseType type) throws X4OPhaseException {
		
		// sort for the order
		List<X4OPhase> x4oPhasesOrder = getOrderedPhases(type);
		
		// debug output
		if (languageSession.getX4ODebugWriter()!=null) {
			languageSession.getX4ODebugWriter().debugPhaseOrder(x4oPhases);
		}
		
		List<String> phaseSkip = languageSession.getPhaseSkip();
		String phaseStop = languageSession.getPhaseStop();
		
		// run the phases in ordered order
		for (X4OPhase phase:x4oPhasesOrder) {
			
			if (phaseSkip.contains(phase.getId())) {
				continue; // skip phase when requested by context
			}
			
			// debug output
			((X4OLanguageSessionLocal)languageSession).setPhaseCurrent(phase);
			
			// run listeners
			for (X4OPhaseListener l:phase.getPhaseListeners()) {
				l.preRunPhase(phase, languageSession);
			}
			
			// always run endRunPhase for valid debug xml
			try {
				// do the run interface
				phase.runPhase(languageSession);
				
				//  run the element phase if possible
				executePhaseRoot(languageSession,phase);
			} finally {
				// run the listeners again
				for (X4OPhaseListener l:phase.getPhaseListeners()) {
					l.endRunPhase(phase, languageSession);
				}
			}
			
			if (phaseStop!=null && phaseStop.equals(phase.getId())) {
				return; // we are done
			}
		}
	}
	
	/**
	 * Runs phase on single element.
	 * @param e	The Element to process.
	 * @param p	The phase to run.
	 * @throws X4OPhaseException When a running handlers throws one.
	 */
	public void runPhasesForElement(Element e,X4OPhaseType type,X4OPhase p) throws X4OPhaseException {
		X4OLanguageSession languageSession = e.getLanguageSession();
		List<String> phaseSkip = languageSession.getPhaseSkip();
		String phaseStop = languageSession.getPhaseStop();
		
		// sort for the order
		List<X4OPhase> x4oPhasesOrder = getOrderedPhases(type);
		for (X4OPhase phase:x4oPhasesOrder) {
			if (phase.getId().equals(p.getId())==false) {
				continue; // we start running all phases from specified phase
			}
			if (phaseSkip.contains(phase.getId())) {
				continue; // skip phase when requested by context
			}
			
			// set phase
			((X4OLanguageSessionLocal)languageSession).setPhaseCurrent(phase);
			
			// do the run interface
			phase.runPhase(languageSession);
			
			//  run the element phase if possible
			executePhaseRoot(languageSession,phase);
			
			if (phaseStop!=null && phaseStop.equals(phase.getId())) {
				return; // we are done
			}
		}
	}
	
	/**
	 * Run release phase manual if auto release is disabled by config.
	 * @throws X4OPhaseException When a running handlers throws one.
	 */
	public void doReleasePhaseManual(X4OLanguageSession languageSession) throws X4OPhaseException {
		List<String> phaseSkip = languageSession.getPhaseSkip();
		String releaseRequested = null;
		if (phaseSkip.contains(X4OPhase.READ_RELEASE)) {
			releaseRequested = X4OPhase.READ_RELEASE;
		}
		if (phaseSkip.contains(X4OPhase.WRITE_RELEASE)) {
			releaseRequested = X4OPhase.WRITE_RELEASE;
		}
		if (releaseRequested==null) {
			throw new IllegalStateException("No manual release requested.");
		}
		if (languageSession.getRootElement()==null) {
			return; // no  root element , empty xml document ?
		}
		if (languageSession.getRootElement().getElementClass()==null) {
			throw new IllegalStateException("Release phase has already been runned.");
		}
		
		X4OPhase h = null;
		for (X4OPhase phase:x4oPhases) {
			if (phase.getId().equals(releaseRequested)) {
				h = phase;
				break;
			}
		}
		if (h==null) {
			throw new IllegalStateException("No release phase found in manager to run.");
		}
		
		// set phase
		((X4OLanguageSessionLocal)languageSession).setPhaseCurrent(h);
		
		// do the run interface
		h.runPhase(languageSession);
		
		//  run the element phase if possible
		executePhaseRoot(languageSession,h);
	}
	
	class X4OPhaseComparator implements Comparator<X4OPhase> {
		/**
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		public int compare(X4OPhase e1, X4OPhase e2) {
			
			String pid = e1.getId();
			String[] dpids = e2.getPhaseDependencies();
			
			for (int i=0;i<dpids.length;i++) {
				String dpid = dpids[i];
				if (pid.equals(dpid)) {
					return -1;
				}
			}
			return 0;
			
			/*
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
			*/
		}
		/*
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
		*/
	}

	/**
	 * Execute element phase handler on full tree. 
	 * @param phase	The phase to run.
	 * @throws X4OPhaseException When a running handlers throws one.
	 */
	private void executePhaseRoot(X4OLanguageSession elementLanguage,X4OPhase phase) throws X4OPhaseException {
		if (elementLanguage.getRootElement()==null) {
			return;
		}
		executePhaseTree(elementLanguage.getRootElement(),phase);
	}
	
	/**
	 * todo: rewrite to itterator for big deep trees.
	 * 
	 * @param element	The element in the tree.
	 * @param phase	The phase to run.
	 * @throws X4OPhaseException
	 */
	private void executePhaseTree(Element element,X4OPhase phase) throws X4OPhaseException {
		if (element.getElementClass()!=null && element.getElementClass().getSkipPhases().contains(phase.getId())==false) {
			phase.runElementPhase(element);	
		}
		for (Element e:element.getChilderen()) {
			executePhaseTree(e,phase);
		}
	}
}
