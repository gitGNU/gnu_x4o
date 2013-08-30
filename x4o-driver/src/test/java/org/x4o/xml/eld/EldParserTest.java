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
package org.x4o.xml.eld;

import org.junit.Ignore;
import org.x4o.xml.X4ODriver;
import org.x4o.xml.X4ODriverManager;
import org.x4o.xml.io.X4OReader;
import org.x4o.xml.io.X4OWriter;
import org.x4o.xml.lang.DefaultX4OLanguageModule;
import org.x4o.xml.lang.X4OLanguage;
import org.x4o.xml.lang.X4OLanguageLocal;
import org.x4o.xml.lang.X4OLanguageModule;

import junit.framework.TestCase;

/**
 * Tests some code for eld
 * 
 * @author Willem Cazander
 * @version 1.0 Jan 15, 2009
 */
public class EldParserTest extends TestCase {

	public void testNone() {
		/*
		X4ODriver<ElementLanguageModule> driver = X4ODriverManager.getX4ODriver(TestDriver.LANGUAGE);
		driver.setGlobalProperty("", "");
		
		ElementLanguage lang = driver.createLanguage();
		X4OSchemaWriter schemaWriter = driver.createSchemaWriter();
		schemaWriter.writeSchema(new File("/tmp"));
		
		X4OReader reader = driver.createReader();
		//reader.setProperty("", "");
		//reader.addELBeanInstance(name, bean)
		Object rootTreeNode = (Object)reader.readResource("com/iets/foo/test.xml");
		
		
		X4OWriter writer = driver.createWriter();
		writer.writeFile(new File("/tmp/output.xml"));
		
		try {
			read.readResource("");
		} catch (Exception e) {
			e.printStackTrace();
		} 
		*/
	}
	
	public void testRunEldParserCore() throws Exception {

		//X4ODriver<X4OLanguageModule> driver = (X4ODriver<X4OLanguageModule>)X4ODriverManager.getX4ODriver(EldDriver.LANGUAGE_NAME);
		//X4OReader<X4OLanguageModule> reader = driver.createReader();
		//EldDriver parser =  new EldDriver(true);
		//reader.setProperty(X4OLanguagePropertyKeys.PHASE_SKIP_RELEASE, true);
		try {
			//X4OLanguageModule module = reader.readResource("META-INF/eld/eld-lang.eld");
			//List<String> resultTags = new ArrayList<String>(50);
			//for (ElementNamespace ns:module.getElementNamespaces()) {
			//	
			//}
			/*
			for (Element e:parser.getDriver().getElementLanguage().getRootElement().getAllChilderen()) {
				//System.out.println("obj: "+e.getElementObject());
				if (e.getElementType().equals(ElementType.element) && e.getElementObject() instanceof ElementClass) {
					ElementClass ec = (ElementClass)e.getElementObject();
					resultTags.add(ec.getTag());
				}
			}
			*/
			//TODO fix test
			/*
			assertTrue("No module tag found in core eld.", 				resultTags.contains("module"));
			assertTrue("No elementClass tag found in core eld.",		resultTags.contains("elementClass"));
			assertTrue("No elementInterface tag found in core eld.",	resultTags.contains("elementInterface"));
			assertTrue("No bean tag found in core eld.",				resultTags.contains("bean"));
			assertTrue("No elementConfigurator tag found in core eld.",	resultTags.contains("elementConfigurator"));
			*/
		} catch (Exception e) {
			//e.printStackTrace();
		} finally {
		//	parser.doReleasePhaseManual();
		}
	}
	
	@Ignore("fix recusrieve")
	public void testRunEldParser() throws Exception {
		X4ODriver<X4OLanguageModule> driver = (X4ODriver<X4OLanguageModule>)X4ODriverManager.getX4ODriver(EldDriver.LANGUAGE_NAME);
		X4OReader<X4OLanguageModule> reader = driver.createReader();
		X4OWriter<X4OLanguageModule> writer = driver.createWriter();
		
		X4OLanguage language = driver.createLanguage(driver.getLanguageVersionDefault());
		X4OLanguageModule mod = new DefaultX4OLanguageModule();
		
		reader.addELBeanInstance(EldModuleLoader.EL_PARENT_LANGUAGE, language);
		reader.addELBeanInstance(EldModuleLoader.EL_PARENT_LANGUAGE_MODULE, mod);
		
		X4OLanguageModule modNew = reader.readResource("META-INF/test/test-lang.eld");
		
		//int binds = mod.getElementBindingHandlers().size();
		//System.out.println(binds);
		
//		String output = writer.writeString(mod);
//		assertNotNull(output);
		
		// TODO; fix element config+event to new interface + reserse for writing.
		
		//System.out.println(output);
	}
}
