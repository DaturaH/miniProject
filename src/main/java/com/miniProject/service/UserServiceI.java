package com.miniProject.service;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.springframework.cache.annotation.Cacheable;

public interface UserServiceI {
	public String applyAuthCode(String iphone);
	public boolean sendAuthCode(String phone) throws ClientProtocolException, IOException;
	
	public String sendAuthCodeTest(String phone) throws ClientProtocolException, IOException;
	
	public boolean authCode(String accid,String code) throws ClientProtocolException, IOException;

}
