package org.x4o.xml.lang;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.el.ELContext;

import org.x4o.xml.X4ODriver;
import org.x4o.xml.el.X4OExpressionFactory;
import org.x4o.xml.element.Element;
import org.x4o.xml.element.ElementAttributeValueParser;
import org.x4o.xml.element.ElementBindingHandler;
import org.x4o.xml.element.ElementClass;
import org.x4o.xml.element.ElementInterface;
import org.x4o.xml.element.ElementNamespaceContext;
import org.x4o.xml.element.ElementNamespaceInstanceProviderException;
import org.x4o.xml.element.ElementObjectPropertyValue;
import org.x4o.xml.lang.phase.X4OPhaseException;
import org.x4o.xml.lang.phase.X4OPhaseManager;
import org.x4o.xml.lang.phase.X4OPhaseType;

public class DefaultX4OLanguage implements X4OLanguageLocal {

	private Logger logger = null;
	private X4OLanguageConfiguration languageConfiguration = null;
	private List<X4OLanguageModule> elementLanguageModules = null;
	private String languageName = null;
	private String languageVersion = null;
	private X4OPhaseManager phaseManager = null;
	
	public DefaultX4OLanguage(X4OLanguageConfiguration languageConfiguration,X4OPhaseManager phaseManager,String languageName,String languageVersion) {
		if (languageName==null) {
			throw new NullPointerException("Can't define myself with null name.");
		}
		if (languageVersion==null) {
			throw new NullPointerException("Can't define myself with null version.");
		}
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
	 * @throws X4OPhaseException 
	 * @see org.x4o.xml.lang.X4OLanguage#createLanguageContext(org.x4o.xml.X4ODriver)
	 */
	public X4OLanguageContext createLanguageContext(X4ODriver<?> driver){
		X4OLanguageContext result = buildElementLanguage(new DefaultX4OLanguageContext(this),driver);
		try {
			getPhaseManager().runPhases(result, X4OPhaseType.INIT);
		} catch (X4OPhaseException e) {
			throw new RuntimeException(e); //TODO: change layer
		}
		return result;
	}

	protected X4OLanguageContext buildElementLanguage(X4OLanguageContext languageContext,X4ODriver<?> driver) {
		if ((languageContext instanceof X4OLanguageContextLocal)==false) { 
			throw new RuntimeException("Can't init X4OLanguageContext which has not X4OLanguageContextLocal interface obj: "+languageContext);
		}
		X4OLanguageContextLocal contextInit = (X4OLanguageContextLocal)languageContext; 
		for (String key:driver.getGlobalPropertyKeys()) {
			Object value = driver.getGlobalProperty(key);
			contextInit.setLanguageProperty(key, value);
		}
		try {
			if (contextInit.getExpressionLanguageFactory()==null) {
				contextInit.setExpressionLanguageFactory(X4OExpressionFactory.createExpressionFactory(contextInit));
			}
			if (contextInit.getExpressionLanguageContext()==null) {
				contextInit.setExpressionLanguageContext((ELContext)X4OLanguageClassLoader.newInstance(getLanguageConfiguration().getDefaultExpressionLanguageContext()));
			}
			if (contextInit.getElementAttributeValueParser()==null) {
				contextInit.setElementAttributeValueParser((ElementAttributeValueParser)X4OLanguageClassLoader.newInstance(getLanguageConfiguration().getDefaultElementAttributeValueParser()));
			}
			if (contextInit.getElementObjectPropertyValue()==null) {
				contextInit.setElementObjectPropertyValue((ElementObjectPropertyValue)X4OLanguageClassLoader.newInstance(getLanguageConfiguration().getDefaultElementObjectPropertyValue()));
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(),e);
		}
		return contextInit;
	}

	/**
	 * @see org.x4o.xml.lang.X4OLanguage#createElementInstance(java.lang.Class)
	 */
	public Element createElementInstance(X4OLanguageContext context,Class<?> objectClass) {
		for (X4OLanguageModule modContext:getLanguageModules()) {
			for (ElementNamespaceContext nsContext:modContext.getElementNamespaceContexts()) {
				for (ElementClass ec:nsContext.getElementClasses()) {
					if (ec.getObjectClass()!=null && ec.getObjectClass().equals(objectClass)) { 
						try {
							return nsContext.getElementNamespaceInstanceProvider().createElementInstance(context, ec.getTag());
						} catch (ElementNamespaceInstanceProviderException e) {
							throw new RuntimeException(e.getMessage(),e); // TODO: fix me
						}
					}
				}
			}
		}
		throw new IllegalArgumentException("Could not find ElementClass for: "+objectClass.getName());
	}

	/**
	 * @see org.x4o.xml.lang.X4OLanguageContext#findElementBindingHandlers(java.lang.Object)
	 */
	public List<ElementBindingHandler> findElementBindingHandlers(Object parent) {
		List<ElementBindingHandler> result = new ArrayList<ElementBindingHandler>(50);
		for (int i=0;i<elementLanguageModules.size();i++) {
			X4OLanguageModule module = elementLanguageModules.get(i);
			findElementBindingHandlerInList(parent,null,result,module.getElementBindingHandlers(),false);
		}
		for (ElementInterface ei:findElementInterfaces(parent)) {
			findElementBindingHandlerInList(parent,null,result,ei.getElementBindingHandlers(),false);
		}
		return result;
	}

	/**
	 * @see org.x4o.xml.lang.X4OLanguageContext#findElementBindingHandlers(java.lang.Object,java.lang.Object)
	 */
	public List<ElementBindingHandler> findElementBindingHandlers(Object parent,Object child) {
		List<ElementBindingHandler> result = new ArrayList<ElementBindingHandler>(50);
		for (int i=0;i<elementLanguageModules.size();i++) {
			X4OLanguageModule module = elementLanguageModules.get(i);
			findElementBindingHandlerInList(parent,child,result,module.getElementBindingHandlers(),true);
		}
		for (ElementInterface ei:findElementInterfaces(parent)) {
			findElementBindingHandlerInList(parent,child,result,ei.getElementBindingHandlers(),true);
		}
		return result;
	}

	private void findElementBindingHandlerInList(Object parent,Object child,List<ElementBindingHandler> result,List<ElementBindingHandler> checkList,boolean checkChild) {
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
			if (checkChild==false) {
				result.add(binding); // All all handlers for parent only
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
