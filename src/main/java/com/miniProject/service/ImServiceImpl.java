package com.miniProject.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.miniProject.controller.LiveroomController;
import com.miniProject.httptools.CheckSumBuilderI;

@Service("imService")
public class ImServiceImpl implements ImServiceI{
	private static final Logger logger = LoggerFactory.getLogger(ImServiceImpl.class);
	
	@Override
	//创建云信ID
	public String create(String accid) throws Exception {
        String url = "https://api.netease.im/nimserver/user/create.action";

        // 设置请求的参数
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("accid", accid));
        // 打印执行结果
        String result = connection(url , nvps);
        logger.info(result);
        return result;
	  }

	@Override
	//云信ID更新
	public String update(String accid, String props, String token)
			throws Exception {
        String url = "https://api.netease.im/nimserver/user/update.action";

        // 设置请求的参数
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("accid", accid));
        nvps.add(new BasicNameValuePair("props", props));
        nvps.add(new BasicNameValuePair("token" ,token));

        // 打印执行结果
        String result = connection(url , nvps);
        logger.info(result);
        return result;		
	}
	
	@Override
	//更新并获取新token
	public String refreshToken(String accid) throws Exception {
        String url = "https://api.netease.im/nimserver/user/refreshToken.action";

        // 设置请求的参数
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("accid", accid));

        // 打印执行结果
        String result = connection(url , nvps);
        return result;		
	}
	
	@Override
	//封禁云信ID
	public String block(String accid) throws Exception {
        String url = "https://api.netease.im/nimserver/user/block.action";

        // 设置请求的参数
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("accid", accid));

        // 打印执行结果
        String result = connection(url , nvps);
        logger.info(result);
        return result;		
	}

	@Override
	//解禁云信ID
	public String unblock(String accid) throws Exception {
        String url = "https://api.netease.im/nimserver/user/unblock.action";

        // 设置请求的参数
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("accid", accid));

        // 打印执行结果
        String result = connection(url , nvps);
        logger.info(result);
        return result;
	}

	@Override
	//更新用户名片
	public String updateUinfo(String accid, String name, String icon,
		String sign, String email, String birth, String mobile,
		String gender, String ex) throws Exception {

        String url = "https://api.netease.im/nimserver/user/updateUinfo.action";

        // 设置请求的参数
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
	    nvps.add(new BasicNameValuePair("accid", accid));
	    nvps.add(new BasicNameValuePair("name", name));
	    nvps.add(new BasicNameValuePair("icon", icon));
	    nvps.add(new BasicNameValuePair("sign", sign));
	    nvps.add(new BasicNameValuePair("email", email));
	    nvps.add(new BasicNameValuePair("birth", birth));
	    nvps.add(new BasicNameValuePair("mobile", mobile));
	    nvps.add(new BasicNameValuePair("gender", gender));
	    nvps.add(new BasicNameValuePair("ex", ex));


        // 打印执行结果
        String result = connection(url , nvps);
        logger.info(result);
        return result;
	}

	@Override
	//获取用户名片
	public String getUinfos(String accids) throws Exception {

		String url = "https://api.netease.im/nimserver/user/getUinfos.action";

        // 设置请求的参数
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("accids", accids));

        // 打印执行结果
        String result = connection(url , nvps);
        logger.info(result);
        return result;	
	}	
	
	
	
	@Override
	//发送普通消息
	public String sendMsg(String from, String ope, String to, String type,
			String body, String option) throws Exception {
		String url = "https://api.netease.im/nimserver/msg/sendMsg.action";

        // 设置请求的参数
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("from", from));
        nvps.add(new BasicNameValuePair("ope", ope));
        nvps.add(new BasicNameValuePair("to", to));
        nvps.add(new BasicNameValuePair("type", type));
        nvps.add(new BasicNameValuePair("body", body));
        nvps.add(new BasicNameValuePair("option", option));


        // 打印执行结果
        String result = connection(url , nvps);
        logger.info(result);
        return result;	
	}

	
	
	
	@Override
	//历史记录
	public String history(String from, String to, String begintime,
			String endtime, String limit, String reverse) throws Exception {
		String url = "https://api.netease.im/nimserver/history/querySessionMsg.action";

        // 设置请求的参数
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("from", from));
        nvps.add(new BasicNameValuePair("to", to));
        nvps.add(new BasicNameValuePair("begintime", begintime));
        nvps.add(new BasicNameValuePair("endtime", endtime));
        nvps.add(new BasicNameValuePair("limit", limit));
        nvps.add(new BasicNameValuePair("reverse", reverse));
        
        // 打印执行结果
        String result = connection(url , nvps);
        logger.info(result);
        return result;
	}
	
	
	@Override
	//创建聊天室
	public String createChatRoom(String creator, String name,
			String announcement, String broadcasturl, String ext)
			throws Exception {
		String url = "https://api.netease.im/nimserver/chatroom/create.action";

        // 设置请求的参数
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("creator", creator));
        nvps.add(new BasicNameValuePair("name", name));
        nvps.add(new BasicNameValuePair("announcement", announcement));
        nvps.add(new BasicNameValuePair("broadcasturl", broadcasturl));
        nvps.add(new BasicNameValuePair("ext", ext));
        
        // 打印执行结果
        String result = connection(url , nvps);
        logger.info(result);
        return result;
	}

	
	@Override
	//查询聊天室信息
	public String getChatRoom(String roomid, String needOnlineUserCount)
			throws Exception {
		String url = "https://api.netease.im/nimserver/chatroom/get.action";

        // 设置请求的参数
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("roomid", roomid));
        nvps.add(new BasicNameValuePair("needOnlineUserCount", needOnlineUserCount));

        // 打印执行结果
        String result = connection(url , nvps);
        logger.info(result);
        return result;
	}

	@Override
	//更新聊天室信息
	public String updateChatRoom(String roomid, String name,
			String announcement, String broadcasturl, String ext,
			String needNotify, String notifyExt) throws Exception {
		String url = "https://api.netease.im/nimserver/chatroom/update.action";

        // 设置请求的参数
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("roomid", roomid));
        nvps.add(new BasicNameValuePair("name", name));
        nvps.add(new BasicNameValuePair("announcement", announcement));
        nvps.add(new BasicNameValuePair("broadcasturl", broadcasturl));
        nvps.add(new BasicNameValuePair("ext", ext));
        nvps.add(new BasicNameValuePair("needNotify", needNotify));
        nvps.add(new BasicNameValuePair("notifyExt", notifyExt));

        
        // 打印执行结果
        String result = connection(url , nvps);
        logger.info(result);
        return result;
	}

	@Override
	//修改聊天室开／关闭状态
	public String toggleCloseStatChatRoom(String roomid, String operator,
			String valid) throws Exception {
		String url = "https://api.netease.im/nimserver/chatroom/toggleCloseStat.action";

        // 设置请求的参数
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("roomid", roomid));
        nvps.add(new BasicNameValuePair("operator", operator));
        nvps.add(new BasicNameValuePair("valid", valid));
        
        // 打印执行结果
        String result = connection(url , nvps);
        logger.info(result);
        return result;
	}

	@Override
	//设置聊天室内用户角色
	public String setMemberRoleChatRoom(String roomid, String operator,
			String target, String opt, String optvalue) throws Exception {
		String url = "https://api.netease.im/nimserver/chatroom/setMemberRole.action";

        // 设置请求的参数
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("roomid", roomid));
        nvps.add(new BasicNameValuePair("operator", operator));
        nvps.add(new BasicNameValuePair("target", target));
        nvps.add(new BasicNameValuePair("opt", opt));
        nvps.add(new BasicNameValuePair("optvalue", optvalue));
       
        // 打印执行结果
        String result = connection(url , nvps);
        logger.info(result);
        return result;
	}

	@Override
	//请求聊天室地址
	public String requestAddrChatRoom(String roomid, String accid,
			String clienttype) throws Exception {
		String url = "https://api.netease.im/nimserver/chatroom/requestAddr.action";

        // 设置请求的参数
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("roomid", roomid));
        nvps.add(new BasicNameValuePair("accid", accid));
        nvps.add(new BasicNameValuePair("clienttype", clienttype));

       
        // 打印执行结果
        String result = connection(url , nvps);
        logger.info(result);
        return result;
	}

	@Override
	public String sendMsgChatRoom(String roomid, String msgld,
			String fromAccid, String msgType, String resendFlag, String attach,
			String ext) throws Exception {
		String url = "https://api.netease.im/nimserver/chatroom/sendMsg.action ";

        // 设置请求的参数
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("roomid", roomid));
        nvps.add(new BasicNameValuePair("msgld", msgld));
        nvps.add(new BasicNameValuePair("fromAccid", fromAccid));
        nvps.add(new BasicNameValuePair("msgType", msgType));
        nvps.add(new BasicNameValuePair("resendFlag", resendFlag));
        nvps.add(new BasicNameValuePair("attach", attach));
        nvps.add(new BasicNameValuePair("ext", ext));
      
        // 打印执行结果
        String result = connection(url , nvps);
        logger.info(result);
        return result;
	}

	
	
	public static String connection(String url , List<NameValuePair> nvps) throws Exception{
	       DefaultHttpClient httpClient = new DefaultHttpClient();
	        HttpPost httpPost = new HttpPost(url);

	        String appKey = "1f1c6625b5f124657eb32ba211d87fd5";
	        String appSecret = "244daf359590";
	        String nonce =  "12345";
	        String curTime = String.valueOf((new Date()).getTime() / 1000L);
	        String checkSum = CheckSumBuilderI.getCheckSum(appSecret, nonce ,curTime);//参考 计算CheckSum的java代码

	        // 设置请求的header
	        httpPost.addHeader("AppKey", appKey);
	        httpPost.addHeader("Nonce", nonce);
	        httpPost.addHeader("CurTime", curTime);
	        httpPost.addHeader("CheckSum", checkSum);
	        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
	        // 设置请求的参数

	        httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
	        // 执行请求
	        HttpResponse response = httpClient.execute(httpPost);
	        String result = EntityUtils.toString(response.getEntity(), "utf-8");
	        // 打印执行结果
	        logger.info(result);
	        return result;	
	}


}








