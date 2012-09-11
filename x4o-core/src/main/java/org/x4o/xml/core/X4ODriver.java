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

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;

import org.x4o.xml.core.config.X4OLanguageConfiguration;
import org.x4o.xml.core.config.X4OLanguageProperty;
import org.x4o.xml.element.ElementLanguage;
import org.x4o.xml.sax.XMLWriter;

import org.xml.sax.SAXException;
import org.xml.sax.ext.DefaultHandler2;
import org.xml.sax.helpers.AttributesImpl;

/**
 * X4ODriver can load language and parse files
 * 
 * @author Willem Cazander
 * @version 1.0 Aug 9, 2012
 */
public class X4ODriver implements X4OParserSupport {
	
	/** The logger to log to. */
	private Logger logger = null;
	
	/** The ElementLanguage which hold all language config. */
	protected ElementLanguage elementLanguage = null;
	
	/** Keep is here for if we want to reuse the parser instance. */
	private X4OPhaseManager phaseManager = null;
	
	//private X4OPhaseHandler configOptionalPhase = null;
	
	/** Defines the default version if none is defined. */
	public final static String DEFAULT_LANGUAGE_VERSION = "1.0";
	
	/**
	 * Creates an X4ODriver for this config
	 *
	 * @param languageConfig	The X4O languageConfig to create this parser for..
	 */
	public X4ODriver(X4OLanguageConfiguration languageConfig) {
		if (languageConfig==null) {
			throw new NullPointerException("Can't start with null X4OLanguageConfiguration");
		}
		elementLanguage = languageConfig.createElementLanguage(); // store also language config
		logger = Logger.getLogger(X4OParser.class.getName());
		logger.fine("Creating X4O driver for language: "+languageConfig.getLanguage());
	}
	
	/**
	 * Returns the ElementLanguage
	 * @return returns the ElementLanguage.
	 */
	public ElementLanguage getElementLanguage() {
		return elementLanguage;
	}
	
	/**
	 * Creates and configs an X4OPhaseManager to parse a language.
	 * 
	 * @return An configured X4OPhaseManager
	 * @throws X4OPhaseException
	 */
	protected X4OPhaseManager createX4OPhaseManager() throws X4OPhaseException {
		
		X4OPhaseHandlerFactory factory = new X4OPhaseHandlerFactory(elementLanguage);
		X4OPhaseManager manager = new X4OPhaseManager(elementLanguage);
		
		// main startup
		//manager.addX4OPhaseHandler(factory.createContextsPhase());
		manager.addX4OPhaseHandler(factory.startupX4OPhase());
		manager.addX4OPhaseHandler(factory.createLanguagePhase());
		manager.addX4OPhaseHandler(factory.createLanguageSiblingsPhase());
		manager.addX4OPhaseHandler(factory.parseSAXStreamPhase());
		
		// inject and opt phase
		manager.addX4OPhaseHandler(factory.configGlobalElBeansPhase());
		if (elementLanguage.getLanguageConfiguration().hasX4ODebugWriter()) {manager.addX4OPhaseHandler(factory.debugPhase());}
		
		/*
		if (configOptionalPhase!=null) {
			if (X4OPhase.configOptionalPhase.equals(configOptionalPhase.getX4OPhase())==false) {
			throw new X4OPhaseException(configOptionalPhase,new IllegalStateException("createConfigOptionalPhase() did not return an X4OPhase.configOptionalPhase x4o phase."));
		}
			manager.addX4OPhaseHandler(configOptionalPhase);
		}
		 */
		
		// meta start point
		manager.addX4OPhaseHandler(factory.startX4OPhase());    	
		if (elementLanguage.getLanguageConfiguration().hasX4ODebugWriter()) {manager.addX4OPhaseHandler(factory.debugPhase());}
		
		// config
		manager.addX4OPhaseHandler(factory.configElementPhase());
		manager.addX4OPhaseHandler(factory.configElementInterfacePhase());
		manager.addX4OPhaseHandler(factory.configGlobalElementPhase());
		manager.addX4OPhaseHandler(factory.configGlobalAttributePhase());
		
		// run all attribute events
		manager.addX4OPhaseHandler(factory.runAttributesPhase());
		if (elementLanguage.getLanguageConfiguration().hasX4ODebugWriter()) {manager.addX4OPhaseHandler(factory.debugPhase());}
		// templating
		manager.addX4OPhaseHandler(factory.fillTemplatingPhase());
		if (elementLanguage.getLanguageConfiguration().hasX4ODebugWriter()) {manager.addX4OPhaseHandler(factory.debugPhase());}

		// transforming
		manager.addX4OPhaseHandler(factory.transformPhase());
		manager.addX4OPhaseHandler(factory.runDirtyElementPhase(manager));
		if (elementLanguage.getLanguageConfiguration().hasX4ODebugWriter()) {manager.addX4OPhaseHandler(factory.debugPhase());}
		
		// binding elements
		manager.addX4OPhaseHandler(factory.bindElementPhase());
		
		// runing and releasing
		manager.addX4OPhaseHandler(factory.runPhase());
		if (elementLanguage.getLanguageConfiguration().hasX4ODebugWriter()) {manager.addX4OPhaseHandler(factory.debugPhase());}
		
		manager.addX4OPhaseHandler(factory.runDirtyElementLastPhase(manager));
		if (elementLanguage.getLanguageConfiguration().hasX4ODebugWriter()) {manager.addX4OPhaseHandler(factory.debugPhase());}
		
		// after release phase Element Tree is not avible anymore
		manager.addX4OPhaseHandler(factory.releasePhase());
		
		// Add debug phase listener to all phases
		if (elementLanguage.getLanguageConfiguration().hasX4ODebugWriter()) {
			for (X4OPhaseHandler h:manager.getPhases()) {
				h.addPhaseListener(elementLanguage.getLanguageConfiguration().getX4ODebugWriter().createDebugX4OPhaseListener());
			}
		}
		
		// We are done we the manager
		return manager;
	}

	protected X4OPhaseManager createX4OPhaseManagerSupport() throws X4OPhaseException {
		X4OPhaseHandlerFactory factory = new X4OPhaseHandlerFactory(elementLanguage);
		X4OPhaseManager manager = new X4OPhaseManager(elementLanguage);
		manager.addX4OPhaseHandler(factory.createLanguagePhase());
		manager.addX4OPhaseHandler(factory.createLanguageSiblingsPhase());
		
		manager.addX4OPhaseHandler(factory.configGlobalElBeansPhase());
		if (elementLanguage.getLanguageConfiguration().hasX4ODebugWriter()) {manager.addX4OPhaseHandler(factory.debugPhase());}
		
		return manager;
	}
	
	/**
	 * Parses the input stream as a X4O document.
	 */
	public void parseInput() throws ParserConfigurationException,SAXException,IOException {
		if (elementLanguage.getLanguageConfiguration().getLanguage()==null) {
			throw new ParserConfigurationException("parserConfig is broken getLanguage() returns null."); 
		}
		
		// init debugWriter if enabled
		boolean startedDebugWriter = false;
		Object debugOutputHandler = elementLanguage.getLanguageConfiguration().getLanguageProperty(X4OLanguageProperty.DEBUG_OUTPUT_HANDLER);
		Object debugOutputStream = elementLanguage.getLanguageConfiguration().getLanguageProperty(X4OLanguageProperty.DEBUG_OUTPUT_STREAM);
		if (elementLanguage.getLanguageConfiguration().getX4ODebugWriter()==null) {
			DefaultHandler2 xmlDebugWriter = null;
			if (debugOutputHandler instanceof DefaultHandler2) {
				xmlDebugWriter = (DefaultHandler2)debugOutputHandler;
			} else if (debugOutputStream instanceof OutputStream) {
				xmlDebugWriter = new XMLWriter((OutputStream)debugOutputStream);
			}
			if (xmlDebugWriter!=null) {
				xmlDebugWriter.startDocument();
				xmlDebugWriter.startPrefixMapping("debug", X4ODebugWriter.DEBUG_URI);
				X4ODebugWriter debugWriter = new X4ODebugWriter(xmlDebugWriter);
				elementLanguage.getLanguageConfiguration().setX4ODebugWriter(debugWriter);
				startedDebugWriter = true;
			}
		}
		
		// debug language
		if (elementLanguage.getLanguageConfiguration().hasX4ODebugWriter()) {
			AttributesImpl atts = new AttributesImpl();
			atts.addAttribute ("", "language", "", "", elementLanguage.getLanguageConfiguration().getLanguage());
			atts.addAttribute ("", "currentTimeMillis", "", "", System.currentTimeMillis()+"");
			elementLanguage.getLanguageConfiguration().getX4ODebugWriter().getDebugWriter().startElement(X4ODebugWriter.DEBUG_URI, "X4ODriver", "", atts);
		}
		
		// start parsing language
		try {
			if (phaseManager==null) {
				phaseManager = createX4OPhaseManager();
			} 
			phaseManager.runPhases();
		} catch (Exception e) {

			// also debug exceptions
			if (elementLanguage.getLanguageConfiguration().hasX4ODebugWriter()) {
				try {
				AttributesImpl atts = new AttributesImpl();
				atts.addAttribute ("", "message", "", "", e.getMessage());
				if (e instanceof X4OPhaseException) {
					atts.addAttribute ("", "phase", "", "", ((X4OPhaseException)e).getX4OPhaseHandler().getX4OPhase().name());
				}
				elementLanguage.getLanguageConfiguration().getX4ODebugWriter().getDebugWriter().startElement(X4ODebugWriter.DEBUG_URI, "exceptionStackTrace", "", atts);
				StringWriter writer = new StringWriter();
				PrintWriter printer = new PrintWriter(writer);
				printer.append('\n');
				if (e.getCause()==null) {
					e.printStackTrace(printer);
				} else {
					e.getCause().printStackTrace(printer);
				}
				char[] stack = writer.getBuffer().toString().toCharArray();
				elementLanguage.getLanguageConfiguration().getX4ODebugWriter().getDebugWriter().characters(stack, 0, stack.length);
				elementLanguage.getLanguageConfiguration().getX4ODebugWriter().getDebugWriter().endElement(X4ODebugWriter.DEBUG_URI, "exceptionStackTrace", "");
				} catch (Exception ee) {
					ee.printStackTrace();
				}
			}
			
			// unwrap exception
			if (e.getCause() instanceof ParserConfigurationException) {
				throw (ParserConfigurationException)e.getCause();
			}
			if (e.getCause() instanceof SAXException) {
				throw (SAXException)e.getCause();
			}
			if (e.getCause() instanceof IOException) {
				throw (IOException)e.getCause();
			}
			if (e.getCause()==null) {
				throw new SAXException(e);
			} else {
				throw new SAXException((Exception)e.getCause());
			}
		} finally {
			// close all our resources.
			//if (inputStream!=null) {
			//	inputStream.close();
			//}
			if (elementLanguage.getLanguageConfiguration().hasX4ODebugWriter()) {
				elementLanguage.getLanguageConfiguration().getX4ODebugWriter().getDebugWriter().endElement(X4ODebugWriter.DEBUG_URI, "X4ODriver", "");
			}
			if (startedDebugWriter && elementLanguage.getLanguageConfiguration().hasX4ODebugWriter()) {
				elementLanguage.getLanguageConfiguration().getX4ODebugWriter().getDebugWriter().endPrefixMapping("debug");
				elementLanguage.getLanguageConfiguration().getX4ODebugWriter().getDebugWriter().endDocument();
				if (debugOutputStream instanceof OutputStream) {
					OutputStream outputStream = (OutputStream)debugOutputStream;
					outputStream.flush();
					outputStream.close(); // need this here ?
				}
			}
		}
	}
	
	
	
	/**
	 * Loads the language and returns the ElementLanguage.
	 * @see org.x4o.xml.core.X4OParserSupport#loadElementLanguageSupport()
	 */
	public ElementLanguage loadElementLanguageSupport() throws X4OParserSupportException {
		try {
			X4OPhaseManager loadLanguageManager = createX4OPhaseManagerSupport();
			loadLanguageManager.runPhases();
			return elementLanguage;
		} catch (Exception e) {
			throw new X4OParserSupportException(e);
		}
	}
	
	/**
	 * Run a manual release phase to clean the parsing object tree.
	 * 
	 * @throws X4OPhaseException
	 */
	public void doReleasePhaseManual() throws X4OPhaseException {
		if (phaseManager==null) {
			throw new IllegalStateException("Can't release with null phaseManager.");
		}
		phaseManager.doReleasePhaseManual();
	}
	
	/**
	 * Sets an X4O Language property.
	 * @param key	The key of the property to set.
	 * @param value	The vlue of the property to set.
	 */
	public void setProperty(String key,Object value) {
		if (phaseManager!=null) {
			throw new IllegalStateException("Can't set property after phaseManager is created.");
		}
		elementLanguage.getLanguageConfiguration().setLanguageProperty(X4OLanguageProperty.valueByUri(key), value);
	}
	
	/**
	 * Returns the value an X4O Language property.
	 * @param key	The key of the property to get the value for.
	 * @return	Returns null or the value of the property.
	 */
	public Object getProperty(String key) {
		return elementLanguage.getLanguageConfiguration().getLanguageProperty(X4OLanguageProperty.valueByUri(key));
	}
}
