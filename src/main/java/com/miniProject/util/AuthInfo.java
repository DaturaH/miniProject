package com.miniProject.util;

import java.io.ObjectInputStream.GetField;

import javax.servlet.http.HttpServletRequest;

public class AuthInfo {
	
	private final String phoneNumber;
	private final String userId;
	private final int userType;
	
	public AuthInfo(String phoneNumber, String userId, int userType) {
		this.userId = userId;
		this.phoneNumber = phoneNumber;
		this.userType = userType;
	}
	
	public static AuthInfo get(HttpServletRequest req) {
		return (AuthInfo) req.getAttribute("AuthInfo");
	}
	
	public String getPhoneNumber() {
		return phoneNumber;
	}

	public String getUserId() {
		return userId;
	}

	public int getUserType() {
		return userType;
	}

	@Override
	public String toString() {
		return "AuthInfo [phoneNumber=" + phoneNumber + ", userId=" + userId + ", userType=" + userType + "]";
	}
	
	


	
	
}
