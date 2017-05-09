package com.miniProject.util;


import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;


public class HttpRequestUtils {
	/**
	 *视频录制
	 *@param userId 用户id
	 *@param houseId 房源id
	 *@param cid 频道id
	 **/
	public static String videoRecord(String userId,String houseId,String cid) throws Exception {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        String url = "http://106.2.101.156:8080/VideoRecord/video/download.do";
        url+="?userId="+userId;
        url+="&houseId="+houseId;
        url+="&cid="+cid;
        HttpGet httpGet = new HttpGet(url);
        HttpResponse response = httpClient.execute(httpGet);
	    String result = EntityUtils.toString(response.getEntity(), "utf-8");
        return result;
	}
	public static void main(String[] args) throws Exception {
		System.out.println(videoRecord("920bd6e0-efd9-481d-b0cb-26cfd560908e", "d5e3a1f0-62dc-4e46-9b20-20c3aa58c715", "a958edc78d894688a2ba6d48bc0dee78"));
	}
}
