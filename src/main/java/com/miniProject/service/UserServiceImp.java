package com.miniProject.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.miniProject.httptools.CheckSumBuilderI;
import com.miniProject.util.JsonResponse;


@Service
public class UserServiceImp implements UserServiceI{
	
	@Override
	public String applyAuthCode(String iphone) {
		// TODO Auto-generated method stub
		return null;
	}

	
//	public Map<String,Object> authAccid(String accid){
//		InfoData info=new InfoData();
//		try {
//			if(isCreatedAccide(accid)){
//				info.setSuccess(false);
//				info.setMsg("当前手机已注册");
//			}else{
//				sendAuthCode(accid);
//			}
//		} catch (ClientProtocolException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return info;
//	}
	
	private boolean check(String msg) {
		Integer code = (Integer)JsonResponse.toMap(msg).get("code");
        if(code != null && code.equals(200))
        	return true;
        return false;
	}
	/**
	 * 验证手机验证码
	 * */
	public boolean authCode(String accid,String code) throws ClientProtocolException, IOException{
		String url="https://api.netease.im/sms/verifycode.action";
		Map<String, String>params=new HashMap<>();
		params.put("mobile", accid);
		params.put("code", code);
		String msg=connectRemoteServer(url,params);
		
        return check(msg);
	}
	public boolean sendAuthCode(String accid) throws ClientProtocolException, IOException{
		String url="https://api.netease.im/sms/sendcode.action";
		Map<String, String>params=new HashMap<>();
		params.put("mobile", accid);
        String msg = connectRemoteServer(url,params);
        return check(msg);
	}
	
	public String sendAuthCodeTest(String accid) throws ClientProtocolException, IOException{
		String url="https://api.netease.im/sms/sendcode.action";
		Map<String, String>params=new HashMap<>();
		params.put("mobile", accid);
        String msg = connectRemoteServer(url,params);
        return msg;
	}
	
	public boolean isCreatedAccide(String accid) throws ClientProtocolException, IOException{
		String url="https://api.netease.im/nimserver/user/update.action";
		Map<String, String>params=new HashMap<>();
		params.put("accid", accid);
		String msg=connectRemoteServer(url,params);
		return check(msg);
	}
	public  String createAccid(String accid) throws ClientProtocolException, IOException{
		String url="https://api.netease.im/nimserver/user/create.action";
		Map<String, String>params=new HashMap<>();
		params.put("accid", accid);
        return connectRemoteServer(url,params);
        
	}
	public String connectRemoteServer(String url,Map<String,String>params) throws ClientProtocolException, IOException{
		HttpPost httpPost = new HttpPost(url);
        String appKey = "1f1c6625b5f124657eb32ba211d87fd5";
        String appSecret = "244daf359590";
        String nonce =  "12345";
        String curTime = String.valueOf((new Date()).getTime() / 1000L);
        String checkSum = CheckSumBuilderI.getCheckSum(appSecret, nonce ,curTime);
        // 创建header
        httpPost.addHeader("AppKey", appKey);
        httpPost.addHeader("Nonce", nonce);
        httpPost.addHeader("CurTime", curTime);
        httpPost.addHeader("CheckSum", checkSum);
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        Set<String>keys=params.keySet();
        for(String key:keys){
        	nvps.add(new BasicNameValuePair(key, params.get(key)));
        }
        httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
        HttpClient httpClient=new DefaultHttpClient();
        HttpResponse response = httpClient.execute(httpPost);
        return EntityUtils.toString(response.getEntity(), "utf-8");
	}
	public static void main(String[] args) {
		UserServiceImp usi=new UserServiceImp();
		try {
			//System.out.println(usi.sendAuthCode("15067148271"));
			System.out.println(usi.authCode("15067148271", "9202"));
			//System.out.println(usi.isCreatedAccide("15067148271"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
