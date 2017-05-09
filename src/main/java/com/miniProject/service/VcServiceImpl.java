package com.miniProject.service;

import org.springframework.stereotype.Service;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.miniProject.httptools.CheckSumBuilderV;

import java.util.Date;

@Service("vcService")
public class VcServiceImpl implements VcServiceI{
	private static final Logger logger = LoggerFactory.getLogger(VcServiceImpl.class);
	
	@Override
	public String create(String name ,int type) throws Exception {
	        DefaultHttpClient httpClient = new DefaultHttpClient();
	        String url = "https://vcloud.163.com/app/channel/create";
	        HttpPost httpPost = new HttpPost(url);

	        String appKey = "1f1c6625b5f124657eb32ba211d87fd5";
	        String appSecret = "244daf359590";
	        String nonce =  "1";
	        String curTime = String.valueOf((new Date()).getTime() / 1000L);
	        String checkSum = CheckSumBuilderV.getCheckSum(appSecret, nonce ,curTime);//参考 计算CheckSum的java代码

	        // 设置请求的header
	        httpPost.addHeader("AppKey", appKey);
	        httpPost.addHeader("Nonce", nonce);
	        httpPost.addHeader("CurTime", curTime);
	        httpPost.addHeader("CheckSum", checkSum);
	        httpPost.addHeader("Content-Type", "application/json;charset=utf-8");

	        // 设置请求的参数
	        StringEntity params = new StringEntity("{\"name\":\"" + name + "\", \"type\":"+ type +"}");
	        logger.info(params.toString());
	        httpPost.setEntity(params);
			
	        // 执行请求
	        HttpResponse response = httpClient.execute(httpPost);
		    String result = EntityUtils.toString(response.getEntity(), "utf-8");
	        // 打印执行结果
		    logger.info(result);
	        return result;
	}

	@Override
	public String change(String name, String cid ,int type)  throws Exception{
        DefaultHttpClient httpClient = new DefaultHttpClient();
        String url = "https://vcloud.163.com/app/channel/create";
        HttpPost httpPost = new HttpPost(url);

        String appKey = "1f1c6625b5f124657eb32ba211d87fd5";
        String appSecret = "244daf359590";
        String nonce =  "1";
        String curTime = String.valueOf((new Date()).getTime() / 1000L);
        String checkSum = CheckSumBuilderV.getCheckSum(appSecret, nonce ,curTime);//参考 计算CheckSum的java代码

        // 设置请求的header
        httpPost.addHeader("AppKey", appKey);
        httpPost.addHeader("Nonce", nonce);
        httpPost.addHeader("CurTime", curTime);
        httpPost.addHeader("CheckSum", checkSum);
        httpPost.addHeader("Content-Type", "application/json;charset=utf-8");

        // 设置请求的参数
        StringEntity params = new StringEntity("{\"name\":\"" + name + "\", \"cid\":\""+ cid+ "\", \"type\":"+ type +"}");
        httpPost.setEntity(params);
		
        // 执行请求
        HttpResponse response = httpClient.execute(httpPost);
	    String result = EntityUtils.toString(response.getEntity(), "utf-8");
        // 打印执行结果
	    logger.info(result);
        return result;
	}

	@Override
	public String delete(String cid)  throws Exception{
        DefaultHttpClient httpClient = new DefaultHttpClient();
        String url = "https://vcloud.163.com/app/channel/delete";
        HttpPost httpPost = new HttpPost(url);

        String appKey = "1f1c6625b5f124657eb32ba211d87fd5";
        String appSecret = "244daf359590";
        String nonce =  "1";
        String curTime = String.valueOf((new Date()).getTime() / 1000L);
        String checkSum = CheckSumBuilderV.getCheckSum(appSecret, nonce ,curTime);//参考 计算CheckSum的java代码

        // 设置请求的header
        httpPost.addHeader("AppKey", appKey);
        httpPost.addHeader("Nonce", nonce);
        httpPost.addHeader("CurTime", curTime);
        httpPost.addHeader("CheckSum", checkSum);
        httpPost.addHeader("Content-Type", "application/json;charset=utf-8");

        // 设置请求的参数
        StringEntity params = new StringEntity("{\"cid\":\"" +cid +"\"}");
        httpPost.setEntity(params);
		
        // 执行请求
        HttpResponse response  = httpClient.execute(httpPost);
	    String result = EntityUtils.toString(response.getEntity(), "utf-8");
        // 打印执行结果
        logger.info(result);
        return result;
	}

	@Override
	public String channelstats(String cid) throws Exception {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        String url = "https://vcloud.163.com/app/channelstats";
        HttpPost httpPost = new HttpPost(url);

        String appKey = "1f1c6625b5f124657eb32ba211d87fd5";
        String appSecret = "244daf359590";
        String nonce =  "1";
        String curTime = String.valueOf((new Date()).getTime() / 1000L);
        String checkSum = CheckSumBuilderV.getCheckSum(appSecret, nonce ,curTime);//参考 计算CheckSum的java代码

        // 设置请求的header
        httpPost.addHeader("AppKey", appKey);
        httpPost.addHeader("Nonce", nonce);
        httpPost.addHeader("CurTime", curTime);
        httpPost.addHeader("CheckSum", checkSum);
        httpPost.addHeader("Content-Type", "application/json;charset=utf-8");

        // 设置请求的参数
        StringEntity params = new StringEntity("{\"cid\":\"" +cid +"\"}");
        httpPost.setEntity(params);
		
        // 执行请求
        HttpResponse response = httpClient.execute(httpPost);
	    String result = EntityUtils.toString(response.getEntity(), "utf-8");
        // 打印执行结果
        logger.info(result);
        return result;
	}

	@Override
	public String channellist(int records, int pnum, String ofield, int sort)  throws Exception{
	       DefaultHttpClient httpClient = new DefaultHttpClient();
	        String url = "https://vcloud.163.com/app/channellist";
	        HttpPost httpPost = new HttpPost(url);

	        String appKey = "1f1c6625b5f124657eb32ba211d87fd5";
	        String appSecret = "244daf359590";
	        String nonce =  "1";
	        String curTime = String.valueOf((new Date()).getTime() / 1000L);
	        String checkSum = CheckSumBuilderV.getCheckSum(appSecret, nonce ,curTime);//参考 计算CheckSum的java代码

	        // 设置请求的header
	        httpPost.addHeader("AppKey", appKey);
	        httpPost.addHeader("Nonce", nonce);
	        httpPost.addHeader("CurTime", curTime);
	        httpPost.addHeader("CheckSum", checkSum);
	        httpPost.addHeader("Content-Type", "application/json;charset=utf-8");

	        // 设置请求的参数
	        StringEntity params = new StringEntity("{\"records\":" + records + ", \"pnum\":"+ pnum +", \"ofield\":\""+ ofield +"\", \"sort\":"+ sort +"}");
	        httpPost.setEntity(params);
			
	        // 执行请求
	        HttpResponse response = httpClient.execute(httpPost);
		    String result = EntityUtils.toString(response.getEntity(), "utf-8");
	        // 打印执行结果
	        logger.info(result);
	        return result;
	}

	@Override
	public String address(String cid)  throws Exception{
        DefaultHttpClient httpClient = new DefaultHttpClient();
        String url = "https://vcloud.163.com/app/address";
        HttpPost httpPost = new HttpPost(url);

        String appKey = "1f1c6625b5f124657eb32ba211d87fd5";
        String appSecret = "244daf359590";
        String nonce =  "1";
        String curTime = String.valueOf((new Date()).getTime() / 1000L);
        String checkSum = CheckSumBuilderV.getCheckSum(appSecret, nonce ,curTime);//参考 计算CheckSum的java代码

        // 设置请求的header
        httpPost.addHeader("AppKey", appKey);
        httpPost.addHeader("Nonce", nonce);
        httpPost.addHeader("CurTime", curTime);
        httpPost.addHeader("CheckSum", checkSum);
        httpPost.addHeader("Content-Type", "application/json;charset=utf-8");

        // 设置请求的参数
        StringEntity params = new StringEntity("{\"cid\":\"" +cid +"\"}");
        httpPost.setEntity(params);
		
        // 执行请求
        HttpResponse response = httpClient.execute(httpPost);
	    String result = EntityUtils.toString(response.getEntity(), "utf-8");
        // 打印执行结果
        logger.info(result);
        return result;
	}
	
	//设置频道录制状态
	@Override
    public String setAlwaysRecord(String cid , int needRecord, int format, int duration, String filename) throws Exception{
		
		DefaultHttpClient httpClient = new DefaultHttpClient();
        String url = "https://vcloud.163.com/app/channel/setAlwaysRecord";
        HttpPost httpPost = new HttpPost(url);

        String appKey = "1f1c6625b5f124657eb32ba211d87fd5";
        String appSecret = "244daf359590";
        String nonce =  "1";
        String curTime = String.valueOf((new Date()).getTime() / 1000L);
        String checkSum = CheckSumBuilderV.getCheckSum(appSecret, nonce ,curTime);//参考 计算CheckSum的java代码

        // 设置请求的header
        httpPost.addHeader("AppKey", appKey);
        httpPost.addHeader("Nonce", nonce);
        httpPost.addHeader("CurTime", curTime);
        httpPost.addHeader("CheckSum", checkSum);
        httpPost.addHeader("Content-Type", "application/json;charset=utf-8");

        // 设置请求的参数
        StringEntity params = new StringEntity("{\"cid\":\"" + cid + "\", \"needRecord\":" + needRecord + 
        		", \"format\":" + format + ", \"duration\":" + duration + ", \"filename\":\"" + filename +"\"}");
        httpPost.setEntity(params);
		
        // 执行请求
        HttpResponse response = httpClient.execute(httpPost);
	    String result = EntityUtils.toString(response.getEntity(), "utf-8");
        // 打印执行结果
        logger.info(result);
        return result;
	}

    //获取录制视频文件列表
	@Override
    public String getVideoList(String cid) throws Exception{
		DefaultHttpClient httpClient = new DefaultHttpClient();
        String url = "https://vcloud.163.com/app/videolist";
        HttpPost httpPost = new HttpPost(url);

        String appKey = "1f1c6625b5f124657eb32ba211d87fd5";
        String appSecret = "244daf359590";
        String nonce =  "1";
        String curTime = String.valueOf((new Date()).getTime() / 1000L);
        String checkSum = CheckSumBuilderV.getCheckSum(appSecret, nonce ,curTime);//参考 计算CheckSum的java代码

        // 设置请求的header
        httpPost.addHeader("AppKey", appKey);
        httpPost.addHeader("Nonce", nonce);
        httpPost.addHeader("CurTime", curTime);
        httpPost.addHeader("CheckSum", checkSum);
        httpPost.addHeader("Content-Type", "application/json;charset=utf-8");

        // 设置请求的参数
        StringEntity params = new StringEntity("{\"cid\":\"" +cid +"\"}");
        httpPost.setEntity(params);
		
        // 执行请求
        HttpResponse response = httpClient.execute(httpPost);
	    String result = EntityUtils.toString(response.getEntity(), "utf-8");
        // 打印执行结果
        logger.info(result);
        return result;
    }
}
