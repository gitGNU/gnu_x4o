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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.x4o.xml.core.config.X4OLanguageClassLoader;
import org.x4o.xml.core.config.X4OLanguageProperty;
import org.x4o.xml.element.ElementLanguage;
import org.x4o.xml.element.ElementLanguageModule;
import org.x4o.xml.element.ElementNamespaceContext;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * X4OEntityResolver resolves internel entities and proxy to external defined ones.
 * 
 * Resolve order;
 * 1) validation base path dir 
 * 2) external resolver
 * 3) lookup for language in classpath.
 * 4) throw exception 
 * 
 * @author Willem Cazander
 * @version 1.0 Aug 22, 2012
 */
public class X4OEntityResolver implements EntityResolver {
	
	private Logger logger = null;
	private URL basePath = null;
	private ElementLanguage elementLanguage = null;
	private Map<String,String> schemaResources = null;
	private Map<String,String> schemaPathResources = null;
	
	
	protected X4OEntityResolver(ElementLanguage elementLanguage) {
		if (elementLanguage==null) {
			throw new NullPointerException("Can't provide entities with null ElementLanguage.");
		}
		this.logger=Logger.getLogger(X4OEntityResolver.class.getName());
		this.elementLanguage=elementLanguage;
		this.basePath=(URL)elementLanguage.getLanguageConfiguration().getLanguageProperty(X4OLanguageProperty.INPUT_SOURCE_BASE_PATH);
		this.schemaResources=new HashMap<String,String>(20);
		this.schemaPathResources=new HashMap<String,String>(20);
		for (ElementLanguageModule mod:elementLanguage.getElementLanguageModules()) {
			for (ElementNamespaceContext ns:mod.getElementNamespaceContexts()) {
				if (ns.getSchemaUri()==null) {
					continue;
				}
				if (ns.getSchemaResource()==null) {
					continue;
				}
				StringBuffer buf = new StringBuffer(30);
				buf.append(elementLanguage.getLanguageConfiguration().getLanguageResourcePathPrefix());
				buf.append('/');
				buf.append(elementLanguage.getLanguageConfiguration().getLanguage());
				buf.append('/');
				buf.append(ns.getSchemaResource());
				schemaResources.put( ns.getSchemaUri(), buf.toString() );
				
				buf = new StringBuffer(30);
				buf.append(elementLanguage.getLanguageConfiguration().getLanguage());
				buf.append(File.separatorChar);
				buf.append(ns.getSchemaResource());
				schemaPathResources.put( ns.getSchemaUri(), buf.toString() );
			}
		}
	}
	
	public InputSource resolveEntity(String publicId, String systemId) throws IOException,SAXException {
		
		logger.finer("Fetch sysId: "+systemId+" pubId: "+publicId);
		
		// Check if other resolver has resource
		EntityResolver resolver = (EntityResolver)elementLanguage.getLanguageConfiguration().getLanguageProperty(X4OLanguageProperty.CONFIG_ENTITY_RESOLVER);
		if (resolver!=null) {
			InputSource result = resolver.resolveEntity(publicId, systemId);
			if (result!=null) {
				return result;
			}
		}
		
		// Check if we have it on user defined schema base path
		if (schemaPathResources.containsKey(systemId)) {
			File schemaBasePath = (File)elementLanguage.getLanguageConfiguration().getLanguageProperty(X4OLanguageProperty.VALIDATION_SCHEMA_PATH);
			if (schemaBasePath!=null && schemaBasePath.exists()) {
				String schemeResource = schemaResources.get(systemId);
				File schemaFile = new File(schemaBasePath.getAbsolutePath()+File.separatorChar+schemeResource);
				if (schemaFile.exists()) {
					if (schemaFile.canRead()==false) {
						throw new SAXException("Can't read schema file: "+schemaFile);
					}
					try {
						InputSource in = new InputSource(new FileInputStream(schemaFile));
						in.setPublicId(publicId);
						in.setSystemId(systemId);
						return in;
					} catch (IOException e) {
						throw new IOException("Could not open: "+schemaFile+" error: "+e.getMessage(),e);
					}
				}
			}
		}
		
		// Check if we have it on the classpath.
		if (schemaResources.containsKey(systemId)) {
			String schemeResource = schemaResources.get(systemId);
			ClassLoader cl = X4OLanguageClassLoader.getClassLoader();
			URL resource = cl.getResource(schemeResource);
			if (resource!=null) {
				try {
					InputSource in = new InputSource(resource.openStream());
					in.setPublicId(publicId);
					in.setSystemId(systemId);
					return in;
				} catch (IOException e) {
					throw new IOException("Could not open: "+resource+" error: "+e.getMessage(),e);
				}
			}
		}
		
		if (basePath!=null && systemId!=null) {
			if (systemId.startsWith(basePath.toExternalForm())) {
				logger.finer("Base reference basePath: "+basePath+" systemId: "+systemId);
				try {
					InputSource in = new InputSource(new URL(systemId).openStream());
					in.setPublicId(publicId);
					in.setSystemId(systemId);
					return in;
				} catch (IOException e) {
					throw new IOException("Could not open: "+systemId+" error: "+e.getMessage(),e);
				}
			}
		}
		
		// always throw to remove all network fetches.
		throw new SAXException("SystemId not found: "+systemId);
	}
}