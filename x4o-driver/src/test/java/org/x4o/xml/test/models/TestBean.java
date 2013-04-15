/*
 * Copyright (c) 2004-2012, Willem Cazander
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

package org.x4o.xml.test.models;

import java.util.Date;

/**
 * Bean for property testing. 
 * 
 * @author Willem Cazander
 * @version 1.0 Jan 15, 2009
 */
public class TestBean {

	// public ... todo not implemented
	
	public int publicIntegerTypeField = 0;
	public Integer publicIntegerObjectField = new Integer(0);
	
	public long publicLongTypeField = 0;
	public Long publicLongObjectField = new Long(0l);
	
	public double publicDoubleTypeField = 0l;
	public Double publicDoubleObjectField = new Double(0);
	
	public float publicFloatTypeField = 0l;
	public Float publicFloatObjectField = new Float(0);

	public byte publicByteTypeField = 0;
	public Byte publicByteObjectField = Byte.valueOf((byte)0);
	
	public boolean publicBooleanTypeField = false;
	public Boolean publicBooleanObjectField = new Boolean(false);
	
	public char publicCharTypeField = ' ';
	public Character publicCharObjectField = new Character(' ');
	
	public String publicStringObjectField = " ";
	//public Date   publicDateObjectField = new Date(0); // TODO add date converters
	
	
	// private
	
	private int privateIntegerTypeField = 0;
	private Integer privateIntegerObjectField = new Integer(0);
	
	private long privateLongTypeField = 0;
	private Long privateLongObjectField = new Long(0l);
	
	private double privateDoubleTypeField = 0l;
	private Double privateDoubleObjectField = new Double(0);
	
	private float privateFloatTypeField = 0l;
	private Float privateFloatObjectField = new Float(0);

	private byte privateByteTypeField = 0;
	private Byte privateByteObjectField = Byte.valueOf((byte)0);
	
	private boolean privateBooleanTypeField = false;
	private Boolean privateBooleanObjectField = new Boolean(false);
	
	private char privateCharTypeField = ' ';
	private Character privateCharObjectField = new Character(' ');
	
	private String privateStringObjectField = " ";
	//private Date   privateDateObjectField = new Date(0);
	
	
	// auto gen , get/set-ers
	
	/**
	 * @return the publicIntegerTypeField
	 */
	public int getPublicIntegerTypeField() {
		return publicIntegerTypeField;
	}
	/**
	 * @param publicIntegerTypeField the publicIntegerTypeField to set
	 */
	public void setPublicIntegerTypeField(int publicIntegerTypeField) {
		this.publicIntegerTypeField = publicIntegerTypeField;
	}
	/**
	 * @return the publicIntegerObjectField
	 */
	public Integer getPublicIntegerObjectField() {
		return publicIntegerObjectField;
	}
	/**
	 * @param publicIntegerObjectField the publicIntegerObjectField to set
	 */
	public void setPublicIntegerObjectField(Integer publicIntegerObjectField) {
		this.publicIntegerObjectField = publicIntegerObjectField;
	}
	/**
	 * @return the publicLongTypeField
	 */
	public long getPublicLongTypeField() {
		return publicLongTypeField;
	}
	/**
	 * @param publicLongTypeField the publicLongTypeField to set
	 */
	public void setPublicLongTypeField(long publicLongTypeField) {
		this.publicLongTypeField = publicLongTypeField;
	}
	/**
	 * @return the publicLongObjectField
	 */
	public Long getPublicLongObjectField() {
		return publicLongObjectField;
	}
	/**
	 * @param publicLongObjectField the publicLongObjectField to set
	 */
	public void setPublicLongObjectField(Long publicLongObjectField) {
		this.publicLongObjectField = publicLongObjectField;
	}
	/**
	 * @return the publicDoubleTypeField
	 */
	public double getPublicDoubleTypeField() {
		return publicDoubleTypeField;
	}
	/**
	 * @param publicDoubleTypeField the publicDoubleTypeField to set
	 */
	public void setPublicDoubleTypeField(double publicDoubleTypeField) {
		this.publicDoubleTypeField = publicDoubleTypeField;
	}
	/**
	 * @return the publicDoubleObjectField
	 */
	public Double getPublicDoubleObjectField() {
		return publicDoubleObjectField;
	}
	/**
	 * @param publicDoubleObjectField the publicDoubleObjectField to set
	 */
	public void setPublicDoubleObjectField(Double publicDoubleObjectField) {
		this.publicDoubleObjectField = publicDoubleObjectField;
	}
	/**
	 * @return the publicFloatTypeField
	 */
	public float getPublicFloatTypeField() {
		return publicFloatTypeField;
	}
	/**
	 * @param publicFloatTypeField the publicFloatTypeField to set
	 */
	public void setPublicFloatTypeField(float publicFloatTypeField) {
		this.publicFloatTypeField = publicFloatTypeField;
	}
	/**
	 * @return the publicFloatObjectField
	 */
	public Float getPublicFloatObjectField() {
		return publicFloatObjectField;
	}
	/**
	 * @param publicFloatObjectField the publicFloatObjectField to set
	 */
	public void setPublicFloatObjectField(Float publicFloatObjectField) {
		this.publicFloatObjectField = publicFloatObjectField;
	}
	/**
	 * @return the publicByteTypeField
	 */
	public byte getPublicByteTypeField() {
		return publicByteTypeField;
	}
	/**
	 * @param publicByteTypeField the publicByteTypeField to set
	 */
	public void setPublicByteTypeField(byte publicByteTypeField) {
		this.publicByteTypeField = publicByteTypeField;
	}
	/**
	 * @return the publicByteObjectField
	 */
	public Byte getPublicByteObjectField() {
		return publicByteObjectField;
	}
	/**
	 * @param publicByteObjectField the publicByteObjectField to set
	 */
	public void setPublicByteObjectField(Byte publicByteObjectField) {
		this.publicByteObjectField = publicByteObjectField;
	}
	/**
	 * @return the publicBooleanTypeField
	 */
	public boolean isPublicBooleanTypeField() {
		return publicBooleanTypeField;
	}
	/**
	 * @param publicBooleanTypeField the publicBooleanTypeField to set
	 */
	public void setPublicBooleanTypeField(boolean publicBooleanTypeField) {
		this.publicBooleanTypeField = publicBooleanTypeField;
	}
	/**
	 * @return the publicBooleanObjectField
	 */
	public Boolean getPublicBooleanObjectField() {
		return publicBooleanObjectField;
	}
	/**
	 * @param publicBooleanObjectField the publicBooleanObjectField to set
	 */
	public void setPublicBooleanObjectField(Boolean publicBooleanObjectField) {
		this.publicBooleanObjectField = publicBooleanObjectField;
	}
	/**
	 * @return the publicCharTypeField
	 */
	public char getPublicCharTypeField() {
		return publicCharTypeField;
	}
	/**
	 * @param publicCharTypeField the publicCharTypeField to set
	 */
	public void setPublicCharTypeField(char publicCharTypeField) {
		this.publicCharTypeField = publicCharTypeField;
	}
	/**
	 * @return the publicCharObjectField
	 */
	public Character getPublicCharObjectField() {
		return publicCharObjectField;
	}
	/**
	 * @param publicCharObjectField the publicCharObjectField to set
	 */
	public void setPublicCharObjectField(Character publicCharObjectField) {
		this.publicCharObjectField = publicCharObjectField;
	}
	/**
	 * @return the publicStringObjectField
	 */
	public String getPublicStringObjectField() {
		return publicStringObjectField;
	}
	/**
	 * @param publicStringObjectField the publicStringObjectField to set
	 */
	public void setPublicStringObjectField(String publicStringObjectField) {
		this.publicStringObjectField = publicStringObjectField;
	}
	/*
	 * @return the publicDateObjectField
	 
	public Date getPublicDateObjectField() {
		return publicDateObjectField;
	}*/
	/*
	 * @param publicDateObjectField the publicDateObjectField to set
	 
	public void setPublicDateObjectField(Date publicDateObjectField) {
		this.publicDateObjectField = publicDateObjectField;
	}*/
	/**
	 * @return the privateIntegerTypeField
	 */
	public int getPrivateIntegerTypeField() {
		return privateIntegerTypeField;
	}
	/**
	 * @param privateIntegerTypeField the privateIntegerTypeField to set
	 */
	public void setPrivateIntegerTypeField(int privateIntegerTypeField) {
		this.privateIntegerTypeField = privateIntegerTypeField;
	}
	/**
	 * @return the privateIntegerObjectField
	 */
	public Integer getPrivateIntegerObjectField() {
		return privateIntegerObjectField;
	}
	/**
	 * @param privateIntegerObjectField the privateIntegerObjectField to set
	 */
	public void setPrivateIntegerObjectField(Integer privateIntegerObjectField) {
		this.privateIntegerObjectField = privateIntegerObjectField;
	}
	/**
	 * @return the privateLongTypeField
	 */
	public long getPrivateLongTypeField() {
		return privateLongTypeField;
	}
	/**
	 * @param privateLongTypeField the privateLongTypeField to set
	 */
	public void setPrivateLongTypeField(long privateLongTypeField) {
		this.privateLongTypeField = privateLongTypeField;
	}
	/**
	 * @return the privateLongObjectField
	 */
	public Long getPrivateLongObjectField() {
		return privateLongObjectField;
	}
	/**
	 * @param privateLongObjectField the privateLongObjectField to set
	 */
	public void setPrivateLongObjectField(Long privateLongObjectField) {
		this.privateLongObjectField = privateLongObjectField;
	}
	/**
	 * @return the privateDoubleTypeField
	 */
	public double getPrivateDoubleTypeField() {
		return privateDoubleTypeField;
	}
	/**
	 * @param privateDoubleTypeField the privateDoubleTypeField to set
	 */
	public void setPrivateDoubleTypeField(double privateDoubleTypeField) {
		this.privateDoubleTypeField = privateDoubleTypeField;
	}
	/**
	 * @return the privateDoubleObjectField
	 */
	public Double getPrivateDoubleObjectField() {
		return privateDoubleObjectField;
	}
	/**
	 * @param privateDoubleObjectField the privateDoubleObjectField to set
	 */
	public void setPrivateDoubleObjectField(Double privateDoubleObjectField) {
		this.privateDoubleObjectField = privateDoubleObjectField;
	}
	/**
	 * @return the privateFloatTypeField
	 */
	public float getPrivateFloatTypeField() {
		return privateFloatTypeField;
	}
	/**
	 * @param privateFloatTypeField the privateFloatTypeField to set
	 */
	public void setPrivateFloatTypeField(float privateFloatTypeField) {
		this.privateFloatTypeField = privateFloatTypeField;
	}
	/**
	 * @return the privateFloatObjectField
	 */
	public Float getPrivateFloatObjectField() {
		return privateFloatObjectField;
	}
	/**
	 * @param privateFloatObjectField the privateFloatObjectField to set
	 */
	public void setPrivateFloatObjectField(Float privateFloatObjectField) {
		this.privateFloatObjectField = privateFloatObjectField;
	}
	/**
	 * @return the privateByteTypeField
	 */
	public byte getPrivateByteTypeField() {
		return privateByteTypeField;
	}
	/**
	 * @param privateByteTypeField the privateByteTypeField to set
	 */
	public void setPrivateByteTypeField(byte privateByteTypeField) {
		this.privateByteTypeField = privateByteTypeField;
	}
	/**
	 * @return the privateByteObjectField
	 */
	public Byte getPrivateByteObjectField() {
		return privateByteObjectField;
	}
	/**
	 * @param privateByteObjectField the privateByteObjectField to set
	 */
	public void setPrivateByteObjectField(Byte privateByteObjectField) {
		this.privateByteObjectField = privateByteObjectField;
	}
	/**
	 * @return the privateBooleanTypeField
	 */
	public boolean isPrivateBooleanTypeField() {
		return privateBooleanTypeField;
	}
	/**
	 * @param privateBooleanTypeField the privateBooleanTypeField to set
	 */
	public void setPrivateBooleanTypeField(boolean privateBooleanTypeField) {
		this.privateBooleanTypeField = privateBooleanTypeField;
	}
	/**
	 * @return the privateBooleanObjectField
	 */
	public Boolean getPrivateBooleanObjectField() {
		return privateBooleanObjectField;
	}
	/**
	 * @param privateBooleanObjectField the privateBooleanObjectField to set
	 */
	public void setPrivateBooleanObjectField(Boolean privateBooleanObjectField) {
		this.privateBooleanObjectField = privateBooleanObjectField;
	}
	/**
	 * @return the privateCharTypeField
	 */
	public char getPrivateCharTypeField() {
		return privateCharTypeField;
	}
	/**
	 * @param privateCharTypeField the privateCharTypeField to set
	 */
	public void setPrivateCharTypeField(char privateCharTypeField) {
		this.privateCharTypeField = privateCharTypeField;
	}
	/**
	 * @return the privateCharObjectField
	 */
	public Character getPrivateCharObjectField() {
		return privateCharObjectField;
	}
	/**
	 * @param privateCharObjectField the privateCharObjectField to set
	 */
	public void setPrivateCharObjectField(Character privateCharObjectField) {
		this.privateCharObjectField = privateCharObjectField;
	}
	/**
	 * @return the privateStringObjectField
	 */
	public String getPrivateStringObjectField() {
		return privateStringObjectField;
	}
	/**
	 * @param privateStringObjectField the privateStringObjectField to set
	 */
	public void setPrivateStringObjectField(String privateStringObjectField) {
		this.privateStringObjectField = privateStringObjectField;
	}
	/*
	 * @return the privateDateObjectField
	 
	public Date getPrivateDateObjectField() {
		return privateDateObjectField;
	}*/
	/*
	 * @param privateDateObjectField the privateDateObjectField to set
	 
	public void setPrivateDateObjectField(Date privateDateObjectField) {
		this.privateDateObjectField = privateDateObjectField;
	}*/
}
