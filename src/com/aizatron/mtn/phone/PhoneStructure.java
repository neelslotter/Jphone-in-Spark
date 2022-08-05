package com.aizatron.mtn.phone;

import com.google.i18n.phonenumbers.Phonenumber;

public class PhoneStructure {

	Phonenumber.PhoneNumber phoneNumber;
	java.lang.String country;
	java.lang.String carrier;
	java.lang.String internationalFormat;
	java.lang.String nationalFormat;
	java.lang.String type;
	boolean valid;
	boolean isShort;

	boolean ported;

	public boolean isPorted() {
		return ported;
	}

	public void setPorted(boolean ported) {
		this.ported = ported;
	}

	public boolean isShort() {
		return isShort;
	}

	public void setShort(boolean aShort) {
		isShort = aShort;
	}

	public Phonenumber.PhoneNumber getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(Phonenumber.PhoneNumber phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public java.lang.String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public java.lang.String getCarrier() {
		return carrier;
	}

	public void setCarrier(String carrier) {
		this.carrier = carrier;
	}

	public java.lang.String getInternationalFormat() {
		return internationalFormat;
	}

	public void setInternationalFormat(String internationalFormat) {
		this.internationalFormat = internationalFormat;
	}

	public java.lang.String getNationalFormat() {
		return nationalFormat;
	}

	public void setNationalFormat(String nationalFormat) {
		this.nationalFormat = nationalFormat;
	}

	public java.lang.String getType() {
		return type;
	}

	public void setType(java.lang.String type) {
		this.type = type;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	@Override
	public String toString() {
		return "PhoneStructure{" + "phoneNumber=" + phoneNumber + ", country='" + country + '\'' + ", carrier='"
				+ carrier + '\'' + ", internationalFormat='" + internationalFormat + '\'' + ", nationalFormat='"
				+ nationalFormat + '\'' + ", type='" + type + '\'' + ", valid=" + valid + ", isShort=" + isShort
				+ ", ported=" + ported + '}';
	}
}
