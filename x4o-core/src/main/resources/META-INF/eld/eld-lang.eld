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
	xmlns:root="http://cel.x4o.org/xml/ns/cel-root" 
	xmlns="http://cel.x4o.org/xml/ns/cel-core"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
	xsi:schemaLocation="http://cel.x4o.org/xml/ns/cel-root http://cel.x4o.org/xml/ns/cel-root-1.0.xsd"
	name="Element Language Definition"
	providerName="eld.x4o.org"
	id="eld-module"
	>
	
	<bindingHandler id="ElementClassBindingHandler"				bean.class="org.x4o.xml.eld.lang.ElementClassBindingHandler"/>
	<bindingHandler id="ElementModuleBindingHandler"			bean.class="org.x4o.xml.eld.lang.ElementModuleBindingHandler"/>
	<bindingHandler id="ElementClassAttributeBindingHandler"	bean.class="org.x4o.xml.eld.lang.ElementClassAttributeBindingHandler"/>
	<bindingHandler id="ElementInterfaceBindingHandler"			bean.class="org.x4o.xml.eld.lang.ElementInterfaceBindingHandler"/>
	<bindingHandler id="ElementNamespaceContextBindingHandler"	bean.class="org.x4o.xml.eld.lang.ElementNamespaceContextBindingHandler"/>
	<bindingHandler id="StringSplitConverterBindingHandler"		bean.class="org.x4o.xml.eld.lang.StringSplitConverterBindingHandler"/>
	<bindingHandler id="StringSplitConverterStepBindingHandler"	bean.class="org.x4o.xml.eld.lang.StringSplitConverterStepBindingHandler"/>
	
	<namespace
		uri="http://eld.x4o.org/xml/ns/eld-root"
		schemaUri="http://eld.x4o.org/xml/ns/eld-root-1.0.xsd"
		schemaResource="eld-root-1.0.xsd"
		schemaPrefix="root"
		name="Element Language Definition Root"
		languageRoot="true"
		id="eld-root"
		>
		<!-- Root Element for nice namespace'ing  -->
		<element tag="module"								objectClass="${parentLanguageConfiguration.defaultElementLanguageModule}" elementClass="org.x4o.xml.eld.lang.ModuleElement">
			<attribute name="id" required="true"/>
			<description>The module root element.</description>
		</element>
	</namespace>
	
	<namespace
		uri="http://eld.x4o.org/xml/ns/eld-lang"
		schemaUri="http://eld.x4o.org/xml/ns/eld-lang-0.8.xsd"
		schemaResource="eld-lang-1.0.xsd"
		schemaPrefix="lang"
		name="Element Language Definition"
		id="eld-lang"
		>
		
		<!-- First copy some core elements over from parent config -->
		<element tag="namespace"							objectClass="${parentLanguageConfiguration.defaultElementNamespaceContext}">
			<attribute name="uri" required="true"/>
		</element>
		<element		tag="element"						objectClass="${parentLanguageConfiguration.defaultElementClass}">
			<attribute name="objectClass">
				<classConverter/>
			</attribute>
			<attribute name="elementClass">
				<classConverter/>
			</attribute>
		</element>
		<element		tag="elementInterface"				objectClass="${parentLanguageConfiguration.defaultElementInterface}">
			<attribute name="id" required="true"/>
			<attribute name="interfaceClass" required="true">
				<classConverter/>
			</attribute>
		</element>
		<element		tag="attribute"						objectClass="${parentLanguageConfiguration.defaultElementClassAttribute}"/>
		
		<!-- Create some support elements to make language readable. -->
			
		<element tag="bindingHandler"						elementClass="org.x4o.xml.eld.lang.BeanElement">
			<description>Define an className in bean.class for an ElementBindingHandler interface.</description>
			<attribute name="id" required="true"/>
			<attribute name="bean.class" required="true"/>
			<elementParent tag="module" uri="http://eld.x4o.org/xml/ns/eld-root"/>
			<elementParent tag="elementInterface"/>
		</element>
		
		<element tag="attributeHandler"						elementClass="org.x4o.xml.eld.lang.BeanElement">
			<description>Define an className in bean.class for an ElementAttributeHandler.</description>
			<attribute name="id" required="true"/>
			<attribute name="bean.class" required="true"/>
			<attribute name="attributeName" required="true"/>
			<elementParent tag="module" uri="http://eld.x4o.org/xml/ns/eld-root"/>
		</element>
		
		<element tag="configuratorGlobal"					elementClass="org.x4o.xml.eld.lang.BeanElement">
			<description>Define an className in bean.class for an ElementConfiguratorGlobal.</description>
			<attribute name="id" required="true"/>
			<attribute name="bean.class" required="true"/>
			<attribute name="configAction"/>
			<elementParent tag="module" uri="http://eld.x4o.org/xml/ns/eld-root"/>
		</element>
		
		<element tag="configurator"							elementClass="org.x4o.xml.eld.lang.BeanElement">
			<description>Define an className in bean.class for an ElementConfigurator.</description>
			<attribute name="id" required="true"/>
			<attribute name="bean.class" required="true"/>
			<attribute name="configAction"/>
			<elementParent tag="elementInterface"/>
			<elementParent tag="element"/>
		</element>
			
		<element tag="description"							elementClass="org.x4o.xml.eld.lang.DescriptionElement"	schemaContentBase="string">
			<description>An ELD elementDescription field which lets you type text like this one in xml characters instead of an attribute.</description>
			<elementParent tag="module" uri="http://eld.x4o.org/xml/ns/eld-root"/>
			<elementParent tag="bindingHandler"/>
			<elementParent tag="attributeHandler"/>
			<elementParent tag="configurator"/>
			<elementParent tag="elementInterface"/>
			<elementParent tag="element"/>
			<elementParent tag="attributeFromBody"/>
			<elementParent tag="classBindingHandler"/>
		</element>
			
		
		<!-- Add some special tags which do actions. -->
		
		<element tag="elementParent"						elementClass="org.x4o.xml.eld.lang.ElementClassAddParentElement">
			<description>Adds an parent element tag for xsd</description>
			<elementParent tag="element"/>
			<attribute name="tag"/>
			<attribute name="namespaceUri"/>
		</element>
		
		<element tag="elementSkipPhase"						elementClass="org.x4o.xml.eld.lang.SkipPhaseElement">
			<description>Adds an phase to skip to the parent element.</description>
			<attribute name="name" required="true"/>
			<elementParent tag="element"/>
		</element>
		
		<element tag="attributeHandlerNextAttribute"		elementClass="org.x4o.xml.eld.lang.NextAttributeElement">
			<description>Defines the parameter order.</description>
			<attribute name="attributeName" required="true"/>
			<elementParent tag="attributeHandler"/>
			<elementParent tag="bean"/>
		</element>
	
		<element tag="attributeAlias"						elementClass="org.x4o.xml.eld.lang.AttributeAliasElement">
			<description>Defines an alias for an attribute name.</description>
			<attribute name="name" required="true"/>
			<elementParent tag="attribute"/>
		</element>
		
		<element tag="attributeFromBody"					objectClass="org.x4o.xml.eld.lang.AttributeFromBodyConfigurator" autoAttributes="false">
			<description>Defines an alias for an attribute name.</description>
			<attribute name="name" required="true"/>
			<attribute name="id" required="true"/>
			<attribute name="bodyType"/>
		</element>
		
		<element tag="classBindingHandler"					objectClass="org.x4o.xml.eld.lang.ElementRefectionBindingHandler"
			autoAttributes="false"
			>
			<description>Lets you dynamicly bind to object togecher.</description>
			<attribute name="id" required="true"/>
			<attribute name="parentClass">
				<classConverter/>
			</attribute>
			<attribute name="childClass">
				<classConverter/>
			</attribute>
			<attribute name="method">
			</attribute>
		</element>
		
		<!-- Config some helper elements -->
	
		<!-- Helper elements for eld -->
		<element tag="bean"									elementClass="org.x4o.xml.eld.lang.BeanElement">
			<!-- BIG NOTE: maybe beanElement most go to meta to have clean impl. -->
			<description>Loads bean into the Element</description>
			<attribute name="bean.class"/>
			<elementParent tag="module" uri="http://eld.x4o.org/xml/ns/eld-root"/>
			<elementParent tag="elementInterface"/>
			<elementParent tag="element"/>
			<elementParent tag="attribute"/>
		</element>
	
	</namespace>
	<namespace
		uri="http://eld.x4o.org/xml/ns/eld-conv"
		schemaUri="http://eld.x4o.org/xml/ns/eld-conv-0.8.xsd"
		schemaResource="eld-conv-1.0.xsd"
		schemaPrefix="conv"
		name="Element Attribute Converters"
		>
		<!-- Config all converters, move ? -->
		<element tag="beanConverter"			elementClass="org.x4o.xml.eld.lang.BeanElement">
			<description>Define an loadable in bean.class for an ObjectConverter.</description>
			<attribute name="bean.class"/>
			<elementParent tag="attribute" uri="http://eld.x4o.org/xml/ns/eld-lang"/>
			<!-- todo: make converter which loads converter so xsd is correct.  -->
		</element>
		<element tag="booleanConverter"			objectClass="org.x4o.xml.conv.text.BooleanConverter"	/>
		<element tag="byteConverter"			objectClass="org.x4o.xml.conv.text.ByteConverter"		/>
		<element tag="characterConverter"		objectClass="org.x4o.xml.conv.text.CharacterConverter"	/>
		<element tag="doubleConverter"			objectClass="org.x4o.xml.conv.text.DoubleConverter"		/>
		<element tag="floatConverter"			objectClass="org.x4o.xml.conv.text.FloatConverter"		/>
		<element tag="integerConverter"			objectClass="org.x4o.xml.conv.text.IntegerConverter"	/>
		<element tag="longConverter"			objectClass="org.x4o.xml.conv.text.LongConverter"		/>
		<element tag="urlConverter"				objectClass="org.x4o.xml.conv.text.URLConverter"		/>
		<element tag="classConverter"			objectClass="org.x4o.xml.conv.text.ClassConverter"		/>
		<element tag="enumConverter"			objectClass="org.x4o.xml.conv.text.EnumConverter"		/>
		
		<element tag="stringSplitConverter"		objectClass="org.x4o.xml.conv.text.StringSplitConverter">
			<attribute name="classTo"	required="true"><classConverter/></attribute>
			<attribute name="split"		required="true"/>
			<attribute name="splitSize"	required="true"/>
			<attribute name="singleToMethod"/>
			<attribute name="useNativeType"/>
		</element>
		<element tag="stringSplitConverterStep"	objectClass="org.x4o.xml.conv.text.StringSplitConverterStep">
			<attribute name="fromMethod"	required="true"/>
			<attribute name="toMethod"		required="false"/>
			<attribute name="fromOrder"		required="true"/>
			<attribute name="toOrder"		required="true"/>
		</element>
	</namespace>
</root:module>