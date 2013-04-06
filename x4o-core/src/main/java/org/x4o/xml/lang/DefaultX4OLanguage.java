package org.x4o.xml.lang;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.x4o.xml.element.ElementBindingHandler;
import org.x4o.xml.element.ElementInterface;
import org.x4o.xml.element.ElementNamespaceContext;
import org.x4o.xml.lang.phase.X4OPhaseManager;

public class DefaultX4OLanguage implements X4OLanguageLocal {

	private Logger logger = null;
	private X4OLanguageConfiguration languageConfiguration = null;
	private List<X4OLanguageModule> elementLanguageModules = null;
	private String languageName = null;
	private String languageVersion = null;
	private X4OPhaseManager phaseManager = null;
	
	public DefaultX4OLanguage(X4OLanguageConfiguration languageConfiguration,X4OPhaseManager phaseManager,String languageName,String languageVersion) {
		logger = Logger.getLogger(DefaultX4OLanguage.class.getName());
		elementLanguageModules = new ArrayList<X4OLanguageModule>(20);
		this.languageConfiguration=languageConfiguration;
		this.languageName=languageName;
		this.languageVersion=languageVersion;
		this.phaseManager=phaseManager;
	}
	
	/**
	 * @see org.x4o.xml.lang.X4OLanguage#getLanguageName()
	 */
	public String getLanguageName() {
		return languageName;
	}

	/**
	 * @see org.x4o.xml.lang.X4OLanguage#getLanguageVersion()
	 */
	public String getLanguageVersion() {
		return languageVersion;
	}
	
	/**
	 * @see org.x4o.xml.lang.X4OLanguage#getPhaseManager()
	 */
	public X4OPhaseManager getPhaseManager() {
		return phaseManager;
	}

	/**
	 * @return the languageConfiguration
	 */
	public X4OLanguageConfiguration getLanguageConfiguration() {
		return languageConfiguration;
	}

	/*
	 * @param languageConfiguration the languageConfiguration to set
	 
	public void setLanguageConfiguration() {
		this.languageConfiguration = languageConfiguration;
	}*/

	/**
	 * @see org.x4o.xml.lang.X4OLanguageContext#addLanguageModule(org.x4o.xml.lang.X4OLanguageModule)
	 */
	public void addLanguageModule(X4OLanguageModule elementLanguageModule) {
		if (elementLanguageModule.getId()==null) {
			throw new NullPointerException("Can't add module without id.");
		}
		elementLanguageModules.add(elementLanguageModule);
	}

	/**
	 * @see org.x4o.xml.lang.X4OLanguageContext#getLanguageModules()
	 */
	public List<X4OLanguageModule> getLanguageModules() {
		return elementLanguageModules;
	}
	

	/**
	 * @see org.x4o.xml.lang.X4OLanguageContext#findElementBindingHandlers(java.lang.Object,java.lang.Object)
	 */
	public List<ElementBindingHandler> findElementBindingHandlers(Object parent,Object child) {
		List<ElementBindingHandler> result = new ArrayList<ElementBindingHandler>(50);
		for (int i=0;i<elementLanguageModules.size();i++) {
			X4OLanguageModule module = elementLanguageModules.get(i);
			findElementBindingHandlerInList(parent,child,result,module.getElementBindingHandlers());
		}
		for (ElementInterface ei:findElementInterfaces(parent)) {
			findElementBindingHandlerInList(parent,child,result,ei.getElementBindingHandlers());
		}
		return result;
	}

	private void findElementBindingHandlerInList(Object parent,Object child,List<ElementBindingHandler> result,List<ElementBindingHandler> checkList) {
		for (ElementBindingHandler binding:checkList) {
			boolean parentBind = false;
			if (parent instanceof Class) {
				parentBind = binding.getBindParentClass().isAssignableFrom((Class<?>)parent);
			} else {
				parentBind = binding.getBindParentClass().isInstance(parent);
			}
			if (parentBind==false) {
				continue;
			}
			boolean childBind = false;
			for (Class<?> childClass:binding.getBindChildClasses()) {
				if (child instanceof Class && childClass.isAssignableFrom((Class<?>)child)) {
					childBind=true;
					break;	
				} else if (childClass.isInstance(child)) {
					childBind=true;
					break;
				}
			}
			if (parentBind & childBind) {
				result.add(binding);
			}
		}	
	}
	
	/**
	 * @see org.x4o.xml.lang.X4OLanguageContext#findElementInterfaces(java.lang.Object)
	 */
	public List<ElementInterface> findElementInterfaces(Object elementObject) {
		if (elementObject==null) {
			throw new NullPointerException("Can't search for null object.");
		}
		List<ElementInterface> result = new ArrayList<ElementInterface>(50);
		for (int i=0;i<elementLanguageModules.size();i++) {
			X4OLanguageModule module = elementLanguageModules.get(i);
			for (ElementInterface ei:module.getElementInterfaces()) {
				Class<?> eClass = ei.getInterfaceClass();
				logger.finest("Checking interface handler: "+ei+" for class: "+eClass);
				if (elementObject instanceof Class && eClass.isAssignableFrom((Class<?>)elementObject)) {
					logger.finer("Found interface match from class; "+elementObject);
					result.add(ei);
				} else if (eClass.isInstance(elementObject)) {
					logger.finer("Found interface match from object; "+elementObject);
					result.add(ei);
				}
			}
		}
		return result;
	}

	/**
	 * @see org.x4o.xml.lang.X4OLanguageContext#findElementNamespaceContext(java.lang.String)
	 */
	public ElementNamespaceContext findElementNamespaceContext(String namespaceUri) {
		
		// TODO: refactor so no search for every tag !!
		ElementNamespaceContext result = null;
		for (int i=0;i<elementLanguageModules.size();i++) {
			X4OLanguageModule module = elementLanguageModules.get(i);
			result = module.getElementNamespaceContext(namespaceUri);
			if (result!=null) {
				return result;
			}
		}
		return result;
	}
}
