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
	providerName="Element Language Definition"
	providerHost="eld.x4o.org"
	id="eld-module"
	>
	<description>The full element language definition(eld) which is used to define xml languages.</description>
	
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
		<description>Single root namespace so xsd schema generation works correct.</description>
		<element id="module"								objectClass="${parentLanguageConfiguration.defaultElementLanguageModule}" elementClass="org.x4o.xml.eld.lang.ModuleElement">
			<description>The module root element.</description>
			<attribute id="id" required="true">
				<description>The module id.</description>
			</attribute>
			<attribute id="providerHost" required="true">
				<description>The provider host like langx.someorg.domain for uri/etc defaults.</description>
			</attribute>
			<attribute id="providerName">
				<description>The provider name in normal en:lang.</description>
			</attribute>
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
		<description>Some basic language definitions and some helper tags.</description>
		
		<!-- First copy some core elements over from parent config -->
		<element id="namespace" objectClass="${parentLanguageConfiguration.defaultElementNamespaceContext}">
			<description>Defines an namespace for the language.</description>
			<attribute id="id" required="true">
				<description>The namespace id.</description>
			</attribute>
		</element>
		<element id="element" objectClass="${parentLanguageConfiguration.defaultElementClass}">
			<description>The xml element.</description>
			<attribute id="id">
				<attributeAlias name="tag"/>
				<description>The attribute id. (with tag as alias)</description>
			</attribute>
			<attribute id="objectClass">
				<description>The class of the wrapped object.</description>
				<classConverter/>
			</attribute>
			<attribute id="elementClass">
				<description>An custom element class to config object.</description>
				<classConverter/>
			</attribute>
		</element>
		<element id="elementInterface" objectClass="${parentLanguageConfiguration.defaultElementInterface}">
			<description>Config element objects by java interface.</description>
			<attribute id="id" required="true">
				<description>The interface id.</description>
			</attribute>
			<attribute id="interfaceClass" required="true">
				<description>The interface class.</description>
				<classConverter/>
			</attribute>
		</element>
		<element id="attribute" objectClass="${parentLanguageConfiguration.defaultElementClassAttribute}">
			<description>XML Element Attribute tag.</description>
			<attribute id="id">
				<attributeAlias name="name"/>
				<description>The attribute id. (with name as alias)</description>
			</attribute>
		</element>
		
		<!-- Create some support elements to make language readable. -->
			
		<element id="bindingHandler" elementClass="org.x4o.xml.eld.lang.BeanElement">
			<description>Define an className in bean.class for an ElementBindingHandler interface.</description>
			<attribute id="id" required="true">
				<description>The binding handler id.</description>
			</attribute>
			<attribute id="bean.class" required="true">
				<description>The class of the binding handler.</description>
			</attribute>
			<elementParent tag="module" uri="http://eld.x4o.org/xml/ns/eld-root"/>
			<elementParent tag="elementInterface"/>
		</element>
		
		<element id="attributeHandler" elementClass="org.x4o.xml.eld.lang.BeanElement">
			<description>Define an className in bean.class for an ElementAttributeHandler.</description>
			<attribute id="id" required="true">
				<description>The atttribute handler id.</description>
			</attribute>
			<attribute id="bean.class" required="true">
				<description>The class of the attribute handler.</description>
			</attribute>
			<attribute id="attributeName" required="true">
				<description>The xml attribute name.</description>
			</attribute>
			<elementParent tag="module" uri="http://eld.x4o.org/xml/ns/eld-root"/>
		</element>
		
		<element id="configuratorGlobal" elementClass="org.x4o.xml.eld.lang.BeanElement">
			<description>Define an className in bean.class for an ElementConfiguratorGlobal.</description>
			<attribute id="id" required="true">
				<description>The global element configurator id.</description>
			</attribute>
			<attribute id="bean.class" required="true">
				<description>The class of the element configurator.</description>
			</attribute>
			<attribute id="configAction">
				<description>If set to true then run in config phase.</description>
			</attribute>
			<elementParent tag="module" uri="http://eld.x4o.org/xml/ns/eld-root"/>
		</element>
		
		<element id="configurator" elementClass="org.x4o.xml.eld.lang.BeanElement">
			<description>Define an className in bean.class for an ElementConfigurator.</description>
			<attribute id="id" required="true">
				<description>The element configurator id.</description>
			</attribute>
			<attribute id="bean.class" required="true">
				<description>The class of the global element configurator.</description>
			</attribute>
			<attribute id="configAction">
				<description>If set to true then run in config phase.</description>
			</attribute>
			<elementParent tag="elementInterface"/>
			<elementParent tag="element"/>
		</element>
			
		<element id="description" elementClass="org.x4o.xml.eld.lang.DescriptionElement" schemaContentBase="string">
			<description>An ELD elementDescription field which lets you type text like this one in xml characters instead of an attribute.</description>
			<elementParent tag="module" uri="http://eld.x4o.org/xml/ns/eld-root"/>
			<elementParent tag="namespace"/>
			<elementParent tag="bindingHandler"/>
			<elementParent tag="attributeHandler"/>
			<elementParent tag="configurator"/>
			<elementParent tag="configuratorGlobal"/>
			<elementParent tag="elementInterface"/>
			<elementParent tag="element"/>
			<elementParent tag="attribute"/>
			<elementParent tag="attributeFromBody"/>
			<elementParent tag="classBindingHandler"/>
		</element>
			
		
		<!-- Add some special tags which do actions. -->
		
		<element id="elementParent" elementClass="org.x4o.xml.eld.lang.ElementClassAddParentElement">
			<description>Adds an parent element tag for xsd</description>
			<elementParent tag="element"/>
			<elementParent tag="elementInterface"/>
			<attribute id="tag">
				<description>The parent tag to have object for.</description>
			</attribute>
			<attribute id="uri">
				<description>The element namespace uri if non local parent.</description>
			</attribute>
		</element>
		
		<element id="elementSkipPhase" elementClass="org.x4o.xml.eld.lang.SkipPhaseElement">
			<description>Adds an phase to skip to the parent element.</description>
			<attribute id="name" required="true">
				<description>The name of the phase to skip.</description>
			</attribute>
			<elementParent tag="element"/>
		</element>
		
		<element id="attributeHandlerNextAttribute" elementClass="org.x4o.xml.eld.lang.NextAttributeElement">
			<description>Defines the parameter order.</description>
			<attribute id="attributeName" required="true">
				<description>The attribute name to run after this attribute.</description>
			</attribute>
			<elementParent tag="attributeHandler"/>
			<elementParent tag="bean"/>
		</element>
	
		<element id="attributeAlias" elementClass="org.x4o.xml.eld.lang.AttributeAliasElement">
			<description>Defines an alias for an attribute name.</description>
			<attribute id="name" required="true">	
				<description>The alias name to add to the attribute.</description>
			</attribute>
			<elementParent tag="attribute"/>
		</element>
		
		<element id="attributeFromBody" objectClass="org.x4o.xml.eld.lang.AttributeFromBodyConfigurator" autoAttributes="false">
			<description>Defines an alias for an attribute name.</description>
			<attribute id="id" required="true">
				<description>The id for this configurator.</description>
			</attribute>
			<attribute id="name" required="true">
				<description>The attribute name to fill the value.</description>
			</attribute>
			<attribute id="bodyType">
				<description>See org.x4o.xml.element.Element.ElementType for options defaults to 'characters'.</description>
			</attribute>
		</element>
		
		<element id="classBindingHandler" objectClass="org.x4o.xml.eld.lang.ElementRefectionBindingHandler"
			autoAttributes="false"
			>
			<description>Lets you dynamicly bind to object togecher.</description>
			<attribute id="id" required="true">
				<description>The id for this binding handler.</description>
			</attribute>
			<attribute id="parentClass" required="true">
				<description>The parent class.</description>
				<classConverter/>
			</attribute>
			<attribute id="childClass" required="true">
				<description>The child class.</description>
				<classConverter/>
			</attribute>
			<attribute id="addMethod" required="true">
				<description>The method name of the method used to add the child to the parent.</description>
			</attribute>
			<attribute id="getMethod" required="true">
				<description>The method name of the method used to get the childeren of the parent.</description>
			</attribute>
		</element>
		
		<!-- Config some helper elements -->
	
		<!-- Helper elements for eld -->
		<element id="bean" elementClass="org.x4o.xml.eld.lang.BeanElement">
			<!-- BIG NOTE: maybe beanElement most go to meta to have clean impl. -->
			<description>Loads bean into the Element</description>
			<attribute id="bean.class">
				<description>The class name of the the class to load.</description>
			</attribute>
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
		id="eld-conv"
		>
		<description>Basic set of attribute value converters.</description>
		
		<element id="beanConverter" elementClass="org.x4o.xml.eld.lang.BeanElement">
			<description>Define an loadable in bean.class for an ObjectConverter.</description>
			<attribute id="bean.class">
				<description>The class name of the converter to load.</description>
			</attribute>
			<elementParent tag="attribute" uri="http://eld.x4o.org/xml/ns/eld-lang"/>
			<!-- todo: make converter which loads converter so xsd is correct.  -->
		</element>
		<element id="booleanConverter"		objectClass="org.x4o.xml.conv.text.BooleanConverter"	/>
		<element id="byteConverter"			objectClass="org.x4o.xml.conv.text.ByteConverter"		/>
		<element id="characterConverter"	objectClass="org.x4o.xml.conv.text.CharacterConverter"	/>
		<element id="doubleConverter"		objectClass="org.x4o.xml.conv.text.DoubleConverter"		/>
		<element id="floatConverter"		objectClass="org.x4o.xml.conv.text.FloatConverter"		/>
		<element id="integerConverter"		objectClass="org.x4o.xml.conv.text.IntegerConverter"	/>
		<element id="longConverter"			objectClass="org.x4o.xml.conv.text.LongConverter"		/>
		<element id="urlConverter"			objectClass="org.x4o.xml.conv.text.URLConverter"		/>
		<element id="classConverter"		objectClass="org.x4o.xml.conv.text.ClassConverter"		/>
		<element id="enumConverter"			objectClass="org.x4o.xml.conv.text.EnumConverter"		/>
		
		<element id="stringSplitConverter"		objectClass="org.x4o.xml.conv.text.StringSplitConverter">
			<attribute id="classTo"	required="true">
				<description>The class name to convert the string to.</description>
				<classConverter/>
			</attribute>
			<attribute id="split"			required="true"		description="The split regex."/>
			<attribute id="splitSize"		required="true"		description="The split value expected size."/>
			<attribute id="singleToMethod"	description="Shortcut to use only this method for the 'to' convert."/>
			<attribute id="useNativeType"	description="Set to true to convert to native data type."/>
		</element>
		<element id="stringSplitConverterStep"	objectClass="org.x4o.xml.conv.text.StringSplitConverterStep">
			<attribute id="fromMethod"		required="true"		description="The convert 'from' method step."/>
			<attribute id="toMethod"		required="false"	description="The convert 'to' method step."/>
			<attribute id="fromOrder"		required="true"		description="The convert 'from' order."/>
			<attribute id="toOrder"			required="true"		description="The convert 'to' order."/>
		</element>
	</namespace>
</root:module>