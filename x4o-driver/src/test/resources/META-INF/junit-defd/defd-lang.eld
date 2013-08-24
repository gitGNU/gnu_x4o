<?xml version="1.0" encoding="UTF-8"?>
<!--

    Copyright (c) 2004-2013, Willem Cazander
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
	providerHost="test.x4o.org"
	providerName="Test Language"
	id="test-module"
	>
	
	<eld:classBindingHandler id="Parent-Child"	parentClass="org.x4o.xml.test.models.TestObjectParent" childClass="org.x4o.xml.test.models.TestObjectChild" addMethod="addTestObjectChild" getMethod="getTestObjectChilds">
		<eld:description>Binds the TestObjectChild to the TestObjectParent</eld:description>
	</eld:classBindingHandler>	
	<eld:classBindingHandler id="Root-Child"	parentClass="org.x4o.xml.test.models.TestObjectRoot" childClass="org.x4o.xml.test.models.TestObjectChild"  addMethod="addChild" getMethod="getTestObjectChilds">
		<eld:description>Binds the TestBean to the TestObjectChild</eld:description>
	</eld:classBindingHandler>
	<eld:classBindingHandler id="Root-Parent"	parentClass="org.x4o.xml.test.models.TestObjectRoot" childClass="org.x4o.xml.test.models.TestObjectParent" addMethod="addParent" getMethod="getTestObjectParents">
		<eld:description>Binds the TestObjectParent to the TestObjectRoot</eld:description>
	</eld:classBindingHandler>
	<eld:classBindingHandler id="Root-Bean"		parentClass="org.x4o.xml.test.models.TestObjectRoot" childClass="org.x4o.xml.test.models.TestBean" addMethod="addTestBean" getMethod="getTestBeans">
		<eld:description>Binds the TestBean to the TestObjectRoot</eld:description>
	</eld:classBindingHandler>
	<eld:classBindingHandler id="JComponent-JComponent"	parentClass="javax.swing.JComponent" childClass="javax.swing.JComponent" addMethod="addComponent" getMethod="">
		<eld:description>Binds j components.</eld:description>
	</eld:classBindingHandler>
	<eld:classBindingHandler id="JFrame-JPanel"	parentClass="javax.swing.JFrame" childClass="javax.swing.JPanel" addMethod="add" getMethod="getComponents">
		<eld:description>Binds panel to frame components as unit check.</eld:description>
	</eld:classBindingHandler>
	
	<eld:elementInterface id="Component" interfaceClass="java.awt.Component">
		<eld:description>Configs the Component based objects.</eld:description>
		<eld:attribute id="bounds">
			<conv:stringSplitConverter classTo="java.awt.Rectangle"  split="," splitSize="4" singleToMethod="setRect" useNativeType="true">
				<conv:stringSplitConverterStep fromMethod="getX"		fromOrder="1" toOrder="1"><conv:doubleConverter/></conv:stringSplitConverterStep>
				<conv:stringSplitConverterStep fromMethod="getY"		fromOrder="2" toOrder="2"><conv:doubleConverter/></conv:stringSplitConverterStep>
				<conv:stringSplitConverterStep fromMethod="getWidth"	fromOrder="3" toOrder="3"><conv:doubleConverter/></conv:stringSplitConverterStep>
				<conv:stringSplitConverterStep fromMethod="getHeight"	fromOrder="4" toOrder="4"><conv:doubleConverter/></conv:stringSplitConverterStep>
			</conv:stringSplitConverter>
		</eld:attribute>
	</eld:elementInterface>
	
	<eld:configuratorGlobal bean.class="org.x4o.xml.test.element.TestElementConfigurator" id="testConfigGlobal">
		<eld:description>Test the global element configurator.</eld:description>
	</eld:configuratorGlobal>
	
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
		<eld:namespaceAttribute attributeName="attr1" bean.class="org.x4o.xml.test.element.TestElementNamespaceAttribute" id="attrTest1">
			<eld:description>Test the global element attribute1 handler.</eld:description>
		</eld:namespaceAttribute>
		<eld:namespaceAttribute attributeName="attr2" bean.class="org.x4o.xml.test.element.TestElementNamespaceAttribute" id="attrTest2">
			<eld:description>Test the global element attribute2 handler.</eld:description>
			<eld:namespaceAttributeNext attributeName="attr1"/>
		</eld:namespaceAttribute>
		
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
			
		<eld:element tag="JFrame"				objectClass="javax.swing.JFrame">
			<eld:elementParent tag="root" uri="http://test.x4o.org/xml/ns/test-root"/> 
		</eld:element>
		<eld:element tag="JFrameContentPane"	elementClass="org.x4o.xml.test.element.ContentPaneElement"/>
		<eld:element tag="JLabel"				objectClass="javax.swing.JLabel"/>
		<eld:element tag="JPanel"				objectClass="javax.swing.JPanel"/>
		<eld:element tag="JTextField"			objectClass="javax.swing.JTextField"/>
		<eld:element tag="JTextArea"			objectClass="javax.swing.JTextArea"/>
		<eld:element tag="JScrollPane"			objectClass="javax.swing.JScrollPane"/>
		
	</eld:namespace>
</root:module>