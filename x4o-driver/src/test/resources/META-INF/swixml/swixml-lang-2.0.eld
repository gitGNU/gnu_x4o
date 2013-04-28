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
	xmlns="http://eld.x4o.org/xml/ns/eld-lang"
	xmlns:conv="http://eld.x4o.org/xml/ns/eld-conv"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
	xsi:schemaLocation="http://eld.x4o.org/xml/ns/eld-root http://eld.x4o.org/xml/ns/eld-root-1.0.xsd"
	providerHost="swixml2.test.x4o.org"
	providerName="Swixml2 Language"
	id="swixml2-module"
	>
	
	<bindingHandler id="JFrameBindingHandler"			bean.class="org.x4o.xml.test.swixml.bind.JFrameBindingHandler"/>
	<bindingHandler id="JInternalFrameBindingHandler" 	bean.class="org.x4o.xml.test.swixml.bind.JInternalFrameBindingHandler"/>
	<bindingHandler id="JPanelBindingHandler"			bean.class="org.x4o.xml.test.swixml.bind.JPanelBindingHandler"/>
	<bindingHandler id="JSplitPaneBindingHandler" 		bean.class="org.x4o.xml.test.swixml.bind.JSplitPaneBindingHandler"/>
	<classBindingHandler id="JScrollPane-JComponent"		parentClass="javax.swing.JScrollPane"		childClass="javax.swing.JComponent"		addMethod="setViewportView"		getMethod="getViewport"/>
	<classBindingHandler id="JDesktopPane-JInternalFrame"	parentClass="javax.swing.JDesktopPane"		childClass="javax.swing.JInternalFrame"	addMethod="add"		getMethod="getComponents"/>
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
		<attribute id="toolTipText"><attributeAlias name="ToolTipText"/></attribute>
		<attribute id="focusPainted"><attributeAlias name="FocusPainted"/></attribute>
		<attribute id="borderPainted"><attributeAlias name="BorderPainted"/></attribute>
	</elementInterface>
		
		
	<namespace id="root" languageRoot="true">
		<element tag="frame"			objectClass="javax.swing.JFrame">
			<description>Single element in language root to create nice tree, for imports in xsd namespace aware generated files.</description>
		</element>
	</namespace>
	<namespace id="lang">
		<!-- Note frame should not be here(it can but xsd needs root), but else classic xml does not parse without xmlns additions.  -->
		<element tag="frame"			objectClass="javax.swing.JFrame"/>
		<element tag="menubar"			objectClass="javax.swing.JMenuBar"/>
		<element tag="menu"				objectClass="javax.swing.JMenu"/>
		<element tag="menuitem"			objectClass="javax.swing.JMenuItem">
			<attribute id="accelerator">
				<conv:beanConverter bean.class="org.x4o.xml.test.swixml.conv.KeyStrokeConverter"/>
				<attributeAlias name="Accelerator"/>
			</attribute>
			<attribute id="mnemonic" runBeanValue="false"/>
			<attribute id="Action" runBeanValue="false"/>
			<configurator id="menuitem-action" bean.class="org.x4o.xml.test.swixml.SwiXmlActionConfigurator"/>
		</element>
		<element tag="separator"	/>
		<element tag="panel"			objectClass="javax.swing.JPanel"/>
		<element tag="splitpane"		objectClass="javax.swing.JSplitPane">
			<attribute id="orientation">
				<conv:beanConverter bean.class="org.x4o.xml.test.swixml.conv.JSplitPaneOrientationConverter"/>
			</attribute>
			<attribute id="dividerLocation">
				<conv:integerConverter/>
			</attribute>
		</element>
		<element tag="layeredPane"		objectClass="javax.swing.JLayeredPane"/>
		<element tag="rootPane"			objectClass="javax.swing.JRootPane"/>
		<element tag="scrollPane"		objectClass="javax.swing.JScrollPane"/>
		<element tag="tree"				objectClass="javax.swing.JTree"/>
		<element tag="button"			objectClass="javax.swing.JButton">
			<attributeFromBody id="button-text" name="text"/>
		</element>
		<element tag="table"			objectClass="javax.swing.JTable"/>
		<element tag="textarea"			objectClass="javax.swing.JTextArea"/>
		<element tag="label"			objectClass="javax.swing.JLabel"/>
		<element tag="textfield"		objectClass="javax.swing.JTextField"/>
		<element tag="desktoppane"		objectClass="javax.swing.JDesktopPane"/>
		<element tag="internalframe"	objectClass="javax.swing.JInternalFrame">
			<attribute id="title"><attributeAlias name="Title"/></attribute>
			<attribute id="bounds"><attributeAlias name="Bounds"/></attribute>
			<attribute id="layout"><attributeAlias name="Layout"/></attribute>
			<attribute id="visible"><attributeAlias name="Visible"/></attribute>
			<attribute id="resizable"><attributeAlias name="Resizable"/></attribute>
		</element>
	</namespace>
</root:module>