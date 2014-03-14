<?xml version="1.0" encoding="UTF-8"?>
<!--

    Copyright (c) 2004-2014, Willem Cazander
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
	xmlns="http://eld.x4o.org/xml/ns/eld-lang"
	xmlns:conv="http://eld.x4o.org/xml/ns/eld-conv"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
	xsi:schemaLocation="http://eld.x4o.org/xml/ns/eld-root http://eld.x4o.org/xml/ns/eld-root-1.0.xsd"
	providerHost="swixml.x4o.org"
	providerName="Swixml3 Language"
	id="swixml3-module"
	>
	
	<bindingHandler id="JFrameBindingHandler"			bean.class="org.x4o.xml.test.swixml.bind.JFrameBindingHandler"/>
	<bindingHandler id="JInternalFrameBindingHandler" 	bean.class="org.x4o.xml.test.swixml.bind.JInternalFrameBindingHandler"/>
	<bindingHandler id="JPanelBindingHandler"			bean.class="org.x4o.xml.test.swixml.bind.JPanelBindingHandler"/>
	<bindingHandler id="JSplitPaneBindingHandler" 		bean.class="org.x4o.xml.test.swixml.bind.JSplitPaneBindingHandler"/>
	<classBindingHandler id="JScrollPane-JComponent"		parentClass="javax.swing.JScrollPane"		childClass="javax.swing.JComponent"		addMethod="setViewportView"		getMethod="getViewport"/>
	<classBindingHandler id="JDesktopPane-JInternalFrame"	parentClass="javax.swing.JDesktopPane"		childClass="javax.swing.JInternalFrame"	addMethod="add"		getMethod="getComponents" />
	<classBindingHandler id="JFrame-JDesktopPane"			parentClass="javax.swing.JFrame"			childClass="javax.swing.JDesktopPane"	addMethod="setContentPane"		getMethod="getContentPane"/>
	<classBindingHandler id="JMenuBar-JMenu"				parentClass="javax.swing.JMenuBar"			childClass="javax.swing.JMenu"			addMethod="add"		getMethod="getComponents"/>
	<classBindingHandler id="JMenu-JMenuItem"				parentClass="javax.swing.JMenu"				childClass="javax.swing.JMenuItem"		addMethod="add"		getMethod="getComponents"/>
	<classBindingHandler id="JRootPane-JComponent"			parentClass="javax.swing.JRootPane"			childClass="javax.swing.JComponent"		addMethod="add"		getMethod="getComponents"/>
	
	<elementInterface id="Component" interfaceClass="java.awt.Component">
		<attribute id="bounds">
			<conv:stringSplitConverter classTo="java.awt.Rectangle"  split="," splitSize="4" singleToMethod="setRect" useNativeType="true">
				<conv:stringSplitConverterStep fromMethod="getX"		fromOrder="1" toOrder="1"><conv:doubleConverter/></conv:stringSplitConverterStep>
				<conv:stringSplitConverterStep fromMethod="getY"		fromOrder="2" toOrder="2"><conv:doubleConverter/></conv:stringSplitConverterStep>
				<conv:stringSplitConverterStep fromMethod="getWidth"	fromOrder="3" toOrder="3"><conv:doubleConverter/></conv:stringSplitConverterStep>
				<conv:stringSplitConverterStep fromMethod="getHeight"	fromOrder="4" toOrder="4"><conv:doubleConverter/></conv:stringSplitConverterStep>
			</conv:stringSplitConverter>
		</attribute>
		<attribute id="size">
			<conv:stringSplitConverter classTo="java.awt.Dimension"  split="," splitSize="2" singleToMethod="setSize" useNativeType="true">
				<conv:stringSplitConverterStep fromMethod="getHeight"	fromOrder="1" toOrder="1"><conv:integerConverter/></conv:stringSplitConverterStep>
				<conv:stringSplitConverterStep fromMethod="getWidth"	fromOrder="2" toOrder="2"><conv:integerConverter/></conv:stringSplitConverterStep>
			</conv:stringSplitConverter>
		</attribute>
		<attribute id="icon">
			<conv:beanConverter bean.class="org.x4o.xml.test.swixml.conv.IconConverter"/>
		</attribute>
		<attribute id="background">
			<conv:beanConverter bean.class="org.x4o.xml.test.swixml.conv.ColorConverter"/>
		</attribute>
		<attribute id="location">
			<conv:stringSplitConverter classTo="java.awt.Point"  split="," splitSize="2" singleToMethod="setLocation" useNativeType="true">
				<conv:stringSplitConverterStep fromMethod="getX"	fromOrder="1" toOrder="1"><conv:integerConverter/></conv:stringSplitConverterStep>
				<conv:stringSplitConverterStep fromMethod="getY"	fromOrder="2" toOrder="2"><conv:integerConverter/></conv:stringSplitConverterStep>
			</conv:stringSplitConverter>
		</attribute>
		<attribute id="locationOnScreen" runBeanValue="false"/>
	</elementInterface>

	<elementInterface id="JComponent" interfaceClass="javax.swing.JComponent">
		<attribute id="layout">
			<conv:beanConverter bean.class="org.x4o.xml.test.swixml.conv.LayoutConverter"/>
		</attribute>
		<attribute id="border">
			<conv:beanConverter bean.class="org.x4o.xml.test.swixml.conv.BorderConverter"/>
		</attribute>
	</elementInterface>
	
	<namespace id="root" languageRoot="true">
		<element objectClass="javax.swing.JFrame"/>
	</namespace>
	<namespace id="lang">
		<namespaceAttribute attributeName="fxid" bean.class="org.x4o.xml.test.element.TestElementNamespaceAttribute" id="fxid-test"></namespaceAttribute>
		<element objectClass="javax.swing.JMenuBar"/>
		<element objectClass="javax.swing.JMenu"/>
		<element objectClass="javax.swing.JMenuItem">
			<attribute id="accelerator">
				<conv:beanConverter bean.class="org.x4o.xml.test.swixml.conv.KeyStrokeConverter"/>
				<attributeAlias name="Accelerator"/>
			</attribute>
			<attribute id="mnemonic" runBeanValue="false"/>
			<attribute id="Action" runBeanValue="false"/>
			<configurator id="menuitem-action" bean.class="org.x4o.xml.test.swixml.SwiXmlActionConfigurator"/>
		</element>
		<element id="JMenu.Separator"	description="TODO"/>
		<element objectClass="javax.swing.JPanel"/>
		<element objectClass="javax.swing.JSplitPane">
			<attribute id="orientation">
				<conv:beanConverter bean.class="org.x4o.xml.test.swixml.conv.JSplitPaneOrientationConverter"/>
			</attribute>
			<attribute id="dividerLocation">
				<conv:integerConverter/>
			</attribute>
		</element>
		
		<element objectClass="javax.swing.JLayeredPane"/>
		<element objectClass="javax.swing.JRootPane"/>
		<element objectClass="javax.swing.JScrollPane"/>
		<element objectClass="javax.swing.JTree"/>
		<element objectClass="javax.swing.JButton"/>
		<element objectClass="javax.swing.JTable"/>
		<element objectClass="javax.swing.JTextArea"/>
		<element objectClass="javax.swing.JLabel"/>
		<element objectClass="javax.swing.JTextField"/>
		<element objectClass="javax.swing.JDesktopPane"/>
		<element objectClass="javax.swing.JInternalFrame"/>
	</namespace>
</root:module>