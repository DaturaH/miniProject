package com.miniProject.model;

import java.util.Date;
import java.util.List;

public class BSUserSubcHouseLive {
	public List<Date> meVerifyTime;
	public List<Date> otherVerifyTime;
	public List<Date> meSubcTime;
	public List<Date> landlordRefuseTime;
	public List<Date> userRefuseTime;

	public List<Date> getMeVerifyTime() {
		return meVerifyTime;
	}

	public void setMeVerifyTime(List<Date> meVerifyTime) {
		this.meVerifyTime = meVerifyTime;
	}

	public List<Date> getOtherVerifyTime() {
		return otherVerifyTime;
	}

	public void setOtherVerifyTime(List<Date> otherVerifyTime) {
		this.otherVerifyTime = otherVerifyTime;
	}

	public List<Date> getMeSubcTime() {
		return meSubcTime;
	}

	public void setMeSubcTime(List<Date> meSubcTime) {
		this.meSubcTime = meSubcTime;
	}

	public List<Date> getLandlordRefuseTime() {
		return landlordRefuseTime;
	}

	public void setLandlordRefuseTime(List<Date> landlordRefuseTime) {
		this.landlordRefuseTime = landlordRefuseTime;
	}

	public List<Date> getUserRefuseTime() {
		return userRefuseTime;
	}

	public void setUserRefuseTime(List<Date> userRefuseTime) {
		this.userRefuseTime = userRefuseTime;
	}
}
