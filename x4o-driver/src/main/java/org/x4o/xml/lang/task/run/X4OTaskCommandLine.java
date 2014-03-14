/*
 * Copyright (c) 2004-2014, Willem Cazander
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
package	org.x4o.xml.lang.task.run;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.x4o.xml.X4ODriver;
import org.x4o.xml.X4ODriverManager;
import org.x4o.xml.io.sax.ext.PropertyConfig;
import org.x4o.xml.lang.X4OLanguage;
import org.x4o.xml.lang.task.X4OLanguageTask;
import org.x4o.xml.lang.task.X4OLanguageTaskException;
import org.x4o.xml.lang.task.X4OLanguageTaskExecutor;

/**
 * X4OLanguageTaskCommandLine runs a language task from the command line.
 * 
 * @author Willem Cazander
 * @version 1.0 Aug 24, 2013
 */
public class X4OTaskCommandLine {
	
	private X4ODriver<?> driver = null;
	private X4OLanguageTask task = null;
	private PropertyConfig config = null;
	
	public static void main(String[] argu) {
		X4OTaskCommandLine cmd = new X4OTaskCommandLine();
		cmd.parseCommandLine(argu);
		cmd.executeCommandLine();
	}
	
	private X4OTaskCommandLine() {
	}
	
	private void parseCommandLine(String[] argu) {
		List<String> arguList = Arrays.asList(argu);
		viewHelp(arguList);
		viewListings(arguList);
		findDriver(arguList.iterator());
		findTask(arguList.iterator());
		viewKeyListing(arguList.iterator());
		loadFileProperties(arguList.iterator());
		findProperties(arguList.iterator());
		checkConfig();
	}
	
	private void executeCommandLine() {
		try {
			long startTime = System.currentTimeMillis();
			executeLanguageTask();
			long totalTime = System.currentTimeMillis()-startTime;
			System.out.println("Succesfully executed task: "+task.getId()+" in "+totalTime+" ms.");
		} catch (Exception e) {
			System.err.println("Error while executing task: "+e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	private void executeLanguageTask() throws X4OLanguageTaskException {
		X4OLanguageTaskExecutor taskExecutor = task.createTaskExecutor(config);
		X4OLanguage language = driver.createLanguage();
		taskExecutor.execute(language);
	}
	
	private void systemErrExit(String message) {
		System.err.println(message);
		System.exit(1);
		return;
	}
	
	private void checkConfig() {
		Collection<String> keys = config.getPropertyKeysRequiredValues();
		if (keys.isEmpty()) {
			return;
		}
		System.err.println("Missing properties;");
		for (String key:keys) {
			System.err.println(" - "+key);
		}
		System.exit(1);
	}
	
	private void viewHelp(List<String> argu) {
		for (String arg:argu) {
			if ("-help".equals(arg) || "-h".equals(arg)) {
				System.out.println("X4O Language Task Command Line Usage;");
				System.out.println("-languages <lang>      (-l)   = Selects langauge.");
				System.out.println("-task <task>           (-t)   = Selects task.");
				System.out.println("-property <K>=<V>      (-p)   = Sets a property value of the task.");
				System.out.println("-load-props <file>     (-lp)  = Loads a properties file.");
				System.out.println("-load-props-xml <file> (-lpx) = Loads a xml properties file.");
				System.out.println("-list-languages        (-ll)  = Shows languages in classpath.");
				System.out.println("-list-language-tasks   (-llt) = Shows languages tasks in classpath.");
				System.out.println("-list-keys             (-lk)  = Shows the properties of the task.");
				System.out.println("");
				System.out.println("Property values syntax;");
				System.out.println("<K>=<V>              = Set single value.");
				System.out.println("<K>=<V1>,<V2>,<V3>   = Set list value.");
				System.out.println("<K>=<VK>,<VV>        = Set map value.");
				System.out.println("");
				System.exit(0);
				return;
			}
		}
	}
	
	private void viewListings(List<String> argu) {
		for (String arg:argu) {
			if ("-list-languages".equals(arg) || "-ll".equals(arg)) {
				System.out.println("X4O Languages;");
				for (String language:X4ODriverManager.getX4OLanguages()) {
					System.out.println("- "+language);
				}
				System.out.println();
				System.exit(0);
				return;
			}
			if ("-list-language-tasks".equals(arg) || "-llt".equals(arg)) {
				System.out.println("X4O Language Tasks;");
				int prefix = 12;
				for (X4OLanguageTask task:X4ODriverManager.getX4OLanguageTasks()) {
					int lId = task.getId().length();
					System.out.print(task.getId());
					for (int i=0;i<prefix-lId;i++) {
						System.out.print(" "); // classic, todo use formatter
					}
					System.out.println(" - "+task.getName());
					for (int i=0;i<prefix+3;i++) {
						System.out.print(" ");
					}
					System.out.println(task.getDescription());
					System.out.println();
				}
				System.exit(0);
				return;
			}
		}
	}
	
	private void viewKeyListing(Iterator<String> arguIterator) {
		if (task==null) {
			return;
		}
		while (arguIterator.hasNext()) {
			String arg = arguIterator.next();
			if ("-list-keys".equals(arg) || "-lk".equals(arg)) {
				System.out.println(task.getName()+" config keys;");
				PropertyConfig config = task.createTaskConfig();
				for (String key:config.getPropertyKeys()) {
					Class<?> keyType = config.getPropertyType(key);
					Object valueDefault = config.getPropertyDefault(key);
					String def = "";
					if (valueDefault!=null) {
						def = "(default=\""+unescapeDefault(valueDefault.toString())+"\")";
					}
					if (config.isPropertyRequired(key)) {
						def = "(required=\"true\")";
					}
					System.out.println(String.format("%1$-65s - %2$-8s %3$s", key,keyType.getSimpleName(),def));
				}
				System.out.println();
				System.exit(0);
				return;
			}
		}
	}
	
	private String unescapeDefault(String defaultValue) {
		StringBuilder buf = new StringBuilder(defaultValue.length()+10);
		for (char c:defaultValue.toCharArray()) {
			if (c=='\n') {
				buf.append("\\n");continue;
			}
			if (c=='\t') {
				buf.append("\\t");continue;
			}
			if (c=='\r') {
				buf.append("\\r");continue;
			}
			buf.append(c);
		}
		return buf.toString();
	}
	
	private void findDriver(Iterator<String> arguIterator) {
		while (arguIterator.hasNext()) {
			String arg = arguIterator.next();
			if ("-language".equals(arg) || "-l".equals(arg)) {
				if (arguIterator.hasNext()==false) {
					systemErrExit("No argument for "+arg+" given.");
				}
				String languageName = arguIterator.next();
				driver = X4ODriverManager.getX4ODriver(languageName);
				break;
			}
		}
		if (driver==null) {
			systemErrExit("No -language or -l argument with language given.");
		}
	}
	
	private void findTask(Iterator<String> arguIterator) {
		while (arguIterator.hasNext()) {
			String arg = arguIterator.next();
			if ("-task".equals(arg) || "-t".equals(arg)) {
				if (arguIterator.hasNext()==false) {
					systemErrExit("No argument for "+arg+" given.");
				}
				String taskId = arguIterator.next();
				task = driver.getLanguageTask(taskId);
				break;
			}
		}
		if (task==null) {
			systemErrExit("No -task or -t argument with task given.");
		}
	}
	
	private void findProperties(Iterator<String> arguIterator) {
		config = task.createTaskConfig();
		while (arguIterator.hasNext()) {
			String arg = arguIterator.next();
			if ("-property".equals(arg) || "-p".equals(arg)) {
				if (arguIterator.hasNext()==false) {
					systemErrExit("No argument for "+arg+" given.");
				}
				String valuePair = arguIterator.next();
				String[] values = valuePair.split("=");
				if (values.length==1) {
					systemErrExit("No property value given for key "+valuePair);
				}
				config.setPropertyParsedValue(values[0],values[1]);
			}
		}
	}
	
	private void loadFileProperties(Iterator<String> arguIterator) {
		while (arguIterator.hasNext()) {
			String arg = arguIterator.next();
			if ("-load-props".equals(arg) || "-lp".equals(arg)) {
				if (arguIterator.hasNext()==false) {
					systemErrExit("No argument for "+arg+" given.");
				}
				loadProperties(arguIterator.next(),false);
			}
			if ("-load-props-xml".equals(arg) || "-lpx".equals(arg)) {
				if (arguIterator.hasNext()==false) {
					systemErrExit("No argument for "+arg+" given.");
				}
				loadProperties(arguIterator.next(),true);
			}
		}
	}
	
	private void loadProperties(String file,boolean isXml) {
		File propFile = new File(file);
		if (!propFile.exists()) {
			systemErrExit("File does not exsits; "+propFile);
		}
		Properties p = new Properties();
		InputStream in = null;
		try {
			in = new FileInputStream(propFile);
			if (isXml) {
				p.loadFromXML(in);
			} else {
				p.load(in);
			}
			for (Object keyObj:p.keySet()) {
				String key = keyObj.toString();
				String value = p.getProperty(key);
				config.setPropertyParsedValue(key, value);
			}
		} catch (IOException e) {
			systemErrExit("Fatal-Error: "+e.getMessage());
		} finally {
			try {
				in.close();
			} catch (IOException closeSilent) {
			}
		}
	}
}
