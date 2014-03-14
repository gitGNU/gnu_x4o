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
	xmlns:eld="http://eld.x4o.org/xml/ns/eld-lang"
	xmlns:root="http://eld.x4o.org/xml/ns/eld-root"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://eld.x4o.org/xml/ns/eld-root http://eld.x4o.org/xml/ns/eld-root-1.0.xsd"
	providerName="Meta Language Definition"
	providerHost="meta.x4o.org"
	id="meta-lang"
	>
	<eld:description>The x4o meta language lets you do special xml tricks.</eld:description>
	
	<eld:namespace
		uri="http://meta.x4o.org/xml/ns/meta-lang"
		schemaUri="http://meta.x4o.org/xml/ns/meta-lang-1.0.xsd"
		schemaResource="meta-lang-1.0.xsd"
		schemaPrefix="meta-lang"
		name="X4O Meta Language"
		id="meta-lang"
		>
		<eld:description>Some special xml tags in this namespace.</eld:description>
			
		<eld:element tag="bean"				elementClass="org.x4o.xml.eld.lang.BeanElement">
			<eld:description>
				This tag is used for creating dynamicly an bean from an classname.
				And sets it into the ElementContext so it can be used by anything that is global or works on interfaces.
			</eld:description>
		</eld:element>
		
		<eld:element tag="elReference"		elementClass="org.x4o.xml.lang.meta.ELReferenceElement">
			<eld:description>
				Used to get a reference of an Object into the Element tree.
				Mostly used in combination with the el.id tag.
			</eld:description>
		</eld:element>
		
		<eld:namespaceAttribute id="global-el-id"	attributeName="el.id" bean.class="org.x4o.xml.lang.meta.ELIDAttributeHandler">
			<eld:description>Lets you bind object into the expression language context.</eld:description>
		</eld:namespaceAttribute>
		
		<eld:element tag="parentObject"		elementClass="org.x4o.xml.lang.meta.ParentObjectElement">
			<eld:description>
				Hack in ElementTree, fills this Element with the ElementObject of his parent Element.
			</eld:description>
		</eld:element>
		
		<eld:element tag="property"			elementClass="org.x4o.xml.lang.meta.PropertyElement">
			<eld:description>
				Set an property value on the object of the parent Element.
				
				Uses some magic to try and set the property.
			</eld:description>
		</eld:element>
		
		<eld:element tag="method"			elementClass="org.x4o.xml.lang.meta.MethodElement">
			<eld:description>
				Executes an Method of an Object.
			</eld:description>
		</eld:element>
		
	</eld:namespace>
<!-- 
	<eld:namespace
		uri="http://meta.x4o.org/xml/ns/meta-reference"
		schemaUri="http://meta.x4o.org/xml/xsd/meta-reference"
		schemaResource="meta-reference.xsd"
		name="X4O Meta Language"
		>
	</eld:namespace>
 -->
</root:module>