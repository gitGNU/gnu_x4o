<?xml version="1.0" encoding="UTF-8"?>
<!--

    Copyright (c) 2004-2012, Willem Cazander
    All rights reserved.

    Redistribution and use in source and binary forms, with or without modification, are permitted provided
    that the following conditions are met:

    * Redistributions of source code must retain the above copyright notice, this list of conditions and the
      following disclaimer.
    * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and
      the following disclaimer in the documentation and/or other materials provided with the distribution.

    THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY
    EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
    MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
    THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
    SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT
    OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
    HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
    TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
    SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

-->
<root:module
	xmlns:root="http://eld.x4o.org/xml/ns/eld-root"
	xmlns:eld="http://eld.x4o.org/xml/ns/eld-lang"
	xmlns:conv="http://eld.x4o.org/xml/ns/eld-conv"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
	xsi:schemaLocation="http://eld.x4o.org/xml/ns/eld-root http://eld.x4o.org/xml/ns/eld-root-1.0.xsd"
	providerName="test.x4o.org"
	name="Test Language"
	id="test-module"
	>
	
	<eld:classBindingHandler id="Parent-Child"	parentClass="org.x4o.xml.test.models.TestObjectParent" childClass="org.x4o.xml.test.models.TestObjectChild" method="addTestObjectChild">
		<eld:description>Binds the TestObjectChild to the TestObjectParent</eld:description>
	</eld:classBindingHandler>	
	<eld:classBindingHandler id="Root-Child"	parentClass="org.x4o.xml.test.models.TestObjectRoot" childClass="org.x4o.xml.test.models.TestObjectChild"  method="addChild">
		<eld:description>Binds the TestBean to the TestObjectChild</eld:description>
	</eld:classBindingHandler>
	<eld:classBindingHandler id="Root-Parent"	parentClass="org.x4o.xml.test.models.TestObjectRoot" childClass="org.x4o.xml.test.models.TestObjectParent" method="addParent">
		<eld:description>Binds the TestObjectParent to the TestObjectRoot</eld:description>
	</eld:classBindingHandler>
	<eld:classBindingHandler id="Root-Bean"		parentClass="org.x4o.xml.test.models.TestObjectRoot" childClass="org.x4o.xml.test.models.TestBean" method="addTestBean">
		<eld:description>Binds the TestBean to the TestObjectRoot</eld:description>
	</eld:classBindingHandler>
	
	<eld:elementInterface id="Component" interfaceClass="java.awt.Component">
		<eld:description>Configs the Component based objects.</eld:description>
		<eld:attribute name="bounds">
			<conv:stringSplitConverter classTo="java.awt.Rectangle"  split="," splitSize="4" singleToMethod="setRect" useNativeType="true">
				<conv:stringSplitConverterStep fromMethod="getX"		fromOrder="1" toOrder="1"><conv:doubleConverter/></conv:stringSplitConverterStep>
				<conv:stringSplitConverterStep fromMethod="getY"		fromOrder="2" toOrder="2"><conv:doubleConverter/></conv:stringSplitConverterStep>
				<conv:stringSplitConverterStep fromMethod="getWidth"	fromOrder="3" toOrder="3"><conv:doubleConverter/></conv:stringSplitConverterStep>
				<conv:stringSplitConverterStep fromMethod="getHeight"	fromOrder="4" toOrder="4"><conv:doubleConverter/></conv:stringSplitConverterStep>
			</conv:stringSplitConverter>
		</eld:attribute>
	</eld:elementInterface>

	<eld:elementInterface id="JComponent" interfaceClass="javax.swing.JComponent">
		<eld:description>Configs the JComponent based objects.</eld:description>
		<eld:classBindingHandler id="JComponent-JComponent" parentClass="javax.swing.JComponent" childClass="javax.swing.JComponent" method="add">
			<eld:description>Binds the JComponent to the JComponent.</eld:description>
		</eld:classBindingHandler>
	</eld:elementInterface>
	
	<eld:configurator bean.class="org.x4o.xml.test.element.TestElementConfigurator" id="testConfigGlobal">
		<eld:description>Test the global element configurator.</eld:description>
	</eld:configurator>
	
	<eld:attributeHandler attributeName="tt.attr1" bean.class="org.x4o.xml.test.element.TestElementAttributeHandler" id="attrTest1">
		<eld:description>Test the global element attribute1 handler.</eld:description>
	</eld:attributeHandler>
	<eld:attributeHandler attributeName="tt.attr2" bean.class="org.x4o.xml.test.element.TestElementAttributeHandler" id="attrTest2">
		<eld:description>Test the global element attribute2 handler.</eld:description>
		<eld:attributeHandlerNextAttribute attributeName="tt.attr1"/>
	</eld:attributeHandler>
	
	<eld:namespace
		uri="http://test.x4o.org/xml/ns/test-root"
		schemaUri="http://test.x4o.org/xml/ns/test-root-1.0.xsd"
		schemaResource="test-root-1.0.xsd"
		schemaPrefix="root"
		description="Root namespace to have nice namespaceing."
		name="Test Root Namespace"
		languageRoot="true"
		id="test-root"
		>
		<!-- Root Element for nice namespace'ing  -->
		<eld:element tag="root" objectClass="org.x4o.xml.test.models.TestObjectRoot">
			<eld:description>The test root element.</eld:description>
		</eld:element>
	</eld:namespace>
	
	<eld:namespace
		uri="http://test.x4o.org/xml/ns/test-lang"
		schemaUri="http://test.x4o.org/xml/ns/test-lang-1.0.xsd"
		schemaResource="test-lang-1.0.xsd"
		schemaPrefix="lang"
		description="Test language namespace to test some/most features"
		name="Test Language Namespace"
		id="test-lang"
		>
		<eld:element tag="testNoNSRoot" objectClass="org.x4o.xml.test.models.TestObjectRoot"/>
		
		<eld:element tag="testBean" objectClass="org.x4o.xml.test.models.TestBean">
			<eld:description>The test the bean object.</eld:description>
		</eld:element>
		
		<eld:element tag="configBean" objectClass="org.x4o.xml.test.models.TestBean">
			<eld:configurator bean.class="org.x4o.xml.test.element.TestElementConfigurator" id="testConfig1">
				<eld:description>The test element config.</eld:description>
			</eld:configurator>
			<eld:configurator bean.class="org.x4o.xml.test.element.TestElementConfigurator" id="testConfig2"/>
			<eld:elementSkipPhase name="runAttributesPhase"/>
			<eld:elementSkipPhase name="transformPhase"/>
		</eld:element>
		
		<eld:element tag="parent" objectClass="org.x4o.xml.test.models.TestObjectParent"/>
		<eld:element tag="child" objectClass="org.x4o.xml.test.models.TestObjectChild"/>
		
		<eld:element tag="testObjectParent" objectClass="org.x4o.xml.test.models.TestObjectParent"/>
		<eld:element tag="testObjectChild" objectClass="org.x4o.xml.test.models.TestObjectChild"/>
		<eld:element tag="testBeanObjectChild" objectClass="org.x4o.xml.test.models.TestObjectChild">
		<!--
		<eld:element tag="testBeanObjectChild" objectClass="org.x4o.xml.models.TestObjectChild" parentNamespace="http://test.x4o.org/eld/iets.eld" elementTag="superObject"/>
			<eld:elementClass tag="JTextArea" objectClassName="javax.swing.JTextArea"/>  
			<eld:elementClassParentElementClass namespace="http://test.x4o.org/eld/iets.eld" elementTag="superObject"/>
			<eld:elementClassParentElementClass namespace="http://test.x4o.org/eld/iets.eld" elementTag="superObject2"/>
			-->
		</eld:element>
			
		<eld:element tag="JFrame"				objectClass="javax.swing.JFrame"/>
		<eld:element tag="JFrameContentPane"	elementClass="org.x4o.xml.test.element.ContentPaneElement"/>
		<eld:element tag="JLabel"				objectClass="javax.swing.JLabel"/>
		<eld:element tag="JPanel"				objectClass="javax.swing.JPanel"/>
		<eld:element tag="JTextField"			objectClass="javax.swing.JTextField"/>
		<eld:element tag="JTextArea"			objectClass="javax.swing.JTextArea"/>
		<eld:element tag="JScrollPane"			objectClass="javax.swing.JScrollPane"/>
		
	</eld:namespace>
</root:module>