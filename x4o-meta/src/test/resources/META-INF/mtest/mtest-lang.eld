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
	providerHost="mtest.x4o.org"
	providerName="Meta Test Language"
	id="mtest-lang"
	>
	<eld:namespace
		uri="http://mtest.x4o.org/xml/ns/mtest-root"
		schemaUri="http://mtest.x4o.org/xml/ns/mtest-root-1.0.xsd"
		schemaResource="mtest-root-1.0.xsd"
		schemaPrefix="mroot"
		name="MTest Root Namespace"
		languageRoot="true"
		id="mroot"
		>
		<!-- Root Element for nice namespace'ing  -->
		<eld:element tag="root" objectClass="java.lang.Object">
			<eld:description>The test root element.</eld:description>
		</eld:element>
	</eld:namespace>
	
	<eld:namespace
		uri="http://mtest.x4o.org/xml/ns/mtest-lang"
		schemaUri="http://mtest.x4o.org/xml/ns/mtest-lang-1.0.xsd"
		schemaResource="mtest-lang-1.0.xsd"
		schemaPrefix="mlang"
		name="MTest Language Namespace"
		id="mlang"
		>
		<eld:element tag="date"					objectClass="java.util.Date"/>
		<eld:element tag="JFrame"				objectClass="javax.swing.JFrame"/>
		<eld:element tag="JLabel"				objectClass="javax.swing.JLabel"/>
		<eld:element tag="JPanel"				objectClass="javax.swing.JPanel"/>
		<eld:element tag="JTextField"			objectClass="javax.swing.JTextField"/>
		<eld:element tag="JTextArea"			objectClass="javax.swing.JTextArea"/>
		<eld:element tag="JScrollPane"			objectClass="javax.swing.JScrollPane"/>
		
	</eld:namespace>
</root:module>