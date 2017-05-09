package com.miniProject.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.ListMessage;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.exceptions.RequestException;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.NotificationTemplate;
import com.miniProject.dao.PushClientidMapper;
import com.miniProject.dao.UserInfoMapper;
import com.miniProject.model.PushClientidForm;
import com.miniProject.model.PushHistoryForm;
import com.miniProject.model.UserHouseForm;
import com.miniProject.model.UserInfo;
import com.miniProject.service.PushServiceI;
import com.miniProject.util.JsonResponse;

@Service("pushService")
public class PushServiceImpl implements PushServiceI{	
	private static final Logger logger = LoggerFactory.getLogger(PushServiceImpl.class);
	
	@Resource
	private HouseServiceI houseService;
	@Autowired
	private PushClientidMapper pushClientidMapper;
	@Autowired
	private UserInfoMapper userInfoMapper;
	@Autowired
	private PushHistoryServiceI pushHistoryService;
	
	//定义静态成员，用于推送
	private static IGtPush pushSigleton;
	private static NotificationTemplate templateSigleton;
	
	//生成push单例需要的参数
	private final static String appId = "NIDRhC54hD7mTZDGd0zUT9";
	private final static String appkey = "thLUUBmZLyArZfbCPxVwC7";
	private final static String master = "Tv3uqAEwBW6iFLdrMiX8l6";
	//private final static String appSecret = "dL6rw4zQLR8TOh5spmDlcA";
	private final static String host = "http://sdk.open.api.igexin.com/apiex.htm";
	
	static{
		//生成push单例
		pushSigleton = new IGtPush(host, appkey, master);
		
		//生成消息模板单例 : 对 "指定列表用户" 进行推送
		templateSigleton = new NotificationTemplate(); 	
		// 设置APPID与APPKEY
		templateSigleton.setAppId(appId);
		templateSigleton.setAppkey(appkey);	
		// 配置通知是否响铃、震动或者可清除
		templateSigleton.setIsRing(true);
		templateSigleton.setIsVibrate(true);
		templateSigleton.setIsClearable(true);
		// 透传消息设置：1为强制启动，客户端接收消息后就会立即启动应用；2为等待应用启动
		templateSigleton.setTransmissionType(1);
	}
	
	//测试接口
	public void testPush(String msg){
		
		//获取推送用户ID
		String clientId = "4bd8033e9e417346da83b511b2153162";
		
		//推送图标
		String icon = "icon.png";	
		//推送标题
		String title = "推送测试";	
		//通知内容为：“2006年4月16日 星期六 直播预约被确认”
		String notifyContent = "内容测试";
		
		//透传内容包括：housePic,houseName,userName,userPortrait,date,type
		Map<String, Object> transmissionContent = new HashMap<String, Object>();
		transmissionContent.put("houseId", "houseId");
		transmissionContent.put("housePic", icon);
		transmissionContent.put("houseName", "houseName");		
		transmissionContent.put("userName", "userName");		
		transmissionContent.put("userPortrait", "userPortrait");
		transmissionContent.put("userType", "userType");
		transmissionContent.put("date", "date");
		transmissionContent.put("type", "type");
		
		this.pushMessage(clientId, icon, title, notifyContent, transmissionContent.toString());
	}
	
	//租客生成动作 和 直播时间到 推送至中介
	public void subUser2PubUser(Map<String, Object> msg){
		
		String pubUserId = (String)msg.get("pubUserId");
		String subUserId = (String)msg.get("subUserId");
		String houseId = (String)msg.get("houseId");
		
		//获取推送用户ID
		String clientId = getClientidByPrimaryKey(pubUserId);
		if (clientId == null){
			logger.warn("no clientid found with userid = " + pubUserId);
			return;
		}
		
		if (getReceiveMsgFlag(pubUserId) == false){
			logger.info(pubUserId + "do not want to receive push msg");
			return;
		}
		
		//推送图标
		String icon = "logo.png";	//客户端开发时嵌入
		//推送标题
		UserHouseForm houseForm = houseService.getByHouseId(houseId);
		String title = houseForm.getTitle();
		//通知内容为：“2006年4月16日 星期六 直播预约被确认”
		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
		String date = format.format((Date)msg.get("subBeginTime"));		
		
		String notifyContent = date + " " + this.transVerifyFlag2String((int)msg.get("verifyFlag"));
	
		//获取租客用户信息
		UserInfo userInfo = userInfoMapper.selectByPrimaryKey(subUserId);
		
		//为每条推送消息创建唯一ID
		String infoId = UUID.randomUUID().toString();
		//透传内容包括：infoId，housePic,houseName,userName,userPortrait,date,type
		Map<String, Object> transmissionContent = new HashMap<String, Object>();
		transmissionContent.put("infoId", infoId);
		transmissionContent.put("houseId", houseId);
		transmissionContent.put("housePic", houseService.getPicListByHouseId(houseId).get(0));
		transmissionContent.put("houseName", title);
		transmissionContent.put("userName", userInfo.getUserName());		
		transmissionContent.put("userPortrait", userInfo.getPortraitFileName());
		transmissionContent.put("userType", userInfo.getUserType());
		transmissionContent.put("date", ((Date)msg.get("subBeginTime")).getTime());
		transmissionContent.put("type", (int)msg.get("verifyFlag"));
		transmissionContent.put("receiveTime", System.currentTimeMillis());
		
		pushMessage(clientId, icon, houseForm.getTitle(), notifyContent, JsonResponse.toJSON(transmissionContent));
		
		//推送消息记入数据库
		PushHistoryForm pushHistory = new PushHistoryForm();
		pushHistory.setInfoId(infoId);
		pushHistory.setUserId(pubUserId);
		pushHistory.setPushMsg(JsonResponse.toJSON(transmissionContent));
		pushHistory.setInsertTime(new Date(System.currentTimeMillis()));
		
		pushHistoryService.insert(pushHistory);
	}
	
	//中介生成动作 以及 预约直播时间到, 推送给租客
	public void pubUser2SubUser(List<Map<String, Object> > msgList){
		if (msgList.size() == 0){
			logger.warn("#### empty msg list ####");
			return;
		}
		
		//获取推送信息
		Map<String, Object> msgTmp = msgList.get(0);
		
		String pubUserId = (String)msgTmp.get("pubUserId");
		String houseId = (String)msgTmp.get("houseId");
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
		String date = format.format((Date)msgTmp.get("subBeginTime"));	
			
		String icon = "logo.png";	
		UserHouseForm houseForm = houseService.getByHouseId(houseId);
		if (houseForm == null){
			logger.warn("no house found with houseid = " + houseId);
			return;
		}
		
		String title = houseForm.getTitle();
		String notifyContent = date + " " + this.transVerifyFlag2String((int)msgTmp.get("verifyFlag"));
		
		//获取中介用户信息
		UserInfo userInfo = userInfoMapper.selectByPrimaryKey(pubUserId);
		
		Map<String, Object> transmissionContent = new HashMap<String, Object>();
		transmissionContent.put("houseId", houseId);
		transmissionContent.put("housePic", houseService.getPicListByHouseId(houseId).get(0));
		transmissionContent.put("houseName", title);	
		transmissionContent.put("userName", userInfo.getUserName());		
		transmissionContent.put("userPortrait", userInfo.getPortraitFileName());
		transmissionContent.put("userType", userInfo.getUserType());
		transmissionContent.put("date", ((Date)msgTmp.get("subBeginTime")).getTime());
		transmissionContent.put("type", (int)msgTmp.get("verifyFlag"));
		transmissionContent.put("receiveTime", System.currentTimeMillis());
		
		List clientIds = new ArrayList();
		for (Map<String, Object> msg : msgList){
			String subUserId = (String)msg.get("subUserId");
			
			String clientId = getClientidByPrimaryKey(subUserId);
			if (clientId == null){
				logger.warn("no clientid found with userid = " + subUserId);
				continue;
			}
			
			if (getReceiveMsgFlag(subUserId) == false){
				continue;
			}
			
			clientIds.add(clientId);
			
			//为每条推送消息创建唯一ID
			String infoId = UUID.randomUUID().toString();
			transmissionContent.put("infoId", infoId);
			
			//推送消息记入数据库
			PushHistoryForm pushHistory = new PushHistoryForm();
			pushHistory.setInfoId(infoId);
			pushHistory.setUserId(subUserId);
			pushHistory.setPushMsg(JsonResponse.toJSON(transmissionContent));
			pushHistory.setInsertTime(new Date(System.currentTimeMillis()));
			
			pushHistoryService.insert(pushHistory);
		}
		
		pushMessage(clientIds, icon, title, notifyContent, JsonResponse.toJSON(transmissionContent));	
	}

	//直播时间到，推送给中介
	@Override
	public void liveTimeIsUp(List<Map<String, Object>> msgList) {
		if (msgList.size() == 0){
			logger.warn("#### empty msg list ####");
			return;
		}
		
		//获取推送信息
		Map<String, Object> msgTmp = msgList.get(0);
		
		//推送的时间和icon全部相同
		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
		String date = format.format((Date)msgTmp.get("subBeginTime"));	
		String notifyContent = date + " " + this.transVerifyFlag2String(4);
		String icon = "logo.png";	
		
		for (Map<String, Object> msg : msgList){	
			String pubUserId = (String)msg.get("pubUserId");		
			String clientId = getClientidByPrimaryKey(pubUserId);
			if (clientId == null){
				logger.warn("no clientid found with userid = " + pubUserId);
				continue;
			}
			
			if (getReceiveMsgFlag(pubUserId) == false){
				continue;
			}
			
			String houseId = (String)msg.get("houseId");			
			UserHouseForm houseForm = houseService.getByHouseId(houseId);
			String title = houseForm.getTitle();
			
			//获取中介用户信息
			UserInfo userInfo = userInfoMapper.selectByPrimaryKey(pubUserId);
			
			Map<String, Object> transmissionContent = new HashMap<String, Object>();
			transmissionContent.put("houseId", houseId);
			transmissionContent.put("housePic", houseService.getPicListByHouseId(houseId).get(0));
			transmissionContent.put("houseName", title);	
			transmissionContent.put("userName", userInfo.getUserName());		
			transmissionContent.put("userPortrait", userInfo.getPortraitFileName());
			transmissionContent.put("userType", userInfo.getUserType());
			transmissionContent.put("date", ((Date)msg.get("subBeginTime")).getTime());
			transmissionContent.put("type", 4);
			transmissionContent.put("receiveTime", System.currentTimeMillis());
			
			//为每条推送消息创建唯一ID
			String infoId = UUID.randomUUID().toString();
			transmissionContent.put("infoId", infoId);
			
			//推送消息记入数据库
			PushHistoryForm pushHistory = new PushHistoryForm();
			pushHistory.setInfoId(infoId);
			pushHistory.setUserId(pubUserId);
			pushHistory.setPushMsg(JsonResponse.toJSON(transmissionContent));
			pushHistory.setInsertTime(new Date(System.currentTimeMillis()));
			
			pushHistoryService.insert(pushHistory);
			
			pushMessage(clientId, icon, title, notifyContent, JsonResponse.toJSON(transmissionContent));
		}
	}
	
	//clientId插入数据库
	public int insert (String userId, String clientId){
		//查询clientId是否存在：其他用户在当前手机上登陆过，需要清除该userId与clientid的绑定
		pushClientidMapper.deleteByClientId(clientId);
		
		//判断userId是否已经存在
		PushClientidForm record = pushClientidMapper.selectByPrimaryKey(userId);
		if (record == null){
			//用户Id不存在,插入新的userId与clientId的绑定关系
			record = new PushClientidForm();
			record.setUserId(userId);
			record.setClientId(clientId);
			record.setReceiveMsg(true);
			
			return pushClientidMapper.insert(record);
		}
		else{
			//用户Id已经存在
			record.setClientId(clientId);
			return pushClientidMapper.updateByPrimaryKey(record);
		}
	}
		
	//根据userId查询clientId
	public String getClientidByPrimaryKey(String userId){
		PushClientidForm record = pushClientidMapper.selectByPrimaryKey(userId);
		//没有与该用户对应的推送id
		if (record == null){
			return null;
		}
		
		return record.getClientId();
	}
		
	//根据uerId删除clientId
	public int deleteByPrimaryKey(String userId){
		return pushClientidMapper.deleteByPrimaryKey(userId);
	}

	private void pushMessage(String clientId, String icon, String title, String notifyContent, String transmissionContent){				
		// 设置通知栏标题与题目
		templateSigleton.setTitle(title);
		templateSigleton.setText(notifyContent);
		
		// 透传消息设置
		templateSigleton.setTransmissionContent(transmissionContent);
		
		// 配置通知栏图标
		templateSigleton.setLogo(icon);
		
		SingleMessage message = new SingleMessage();
		message.setData(templateSigleton);
		
		// 设置消息离线，并设置离线时间
		message.setOffline(true);
		
		// 离线有效时间，单位为毫秒，可选
		message.setOfflineExpireTime(24 * 1000 * 3600);
		
		// 配置推送目标
		Target target = new Target();
		target.setAppId(appId);
		target.setClientId(clientId);
		
		IPushResult ret = null;
		try {
			ret = pushSigleton.pushMessageToSingle(message, target);
			logger.info("push message to " + clientId + "!!!!");
		} catch (RequestException e) {
			e.printStackTrace();
			ret = pushSigleton.pushMessageToSingle(message, target, e.getRequestId());
		}
		
		if (ret != null) {
			logger.info(ret.getResponse().toString());
		} else {
			logger.warn("服务器响应异常");
		}	
	}
	
	private void pushMessage(List<String> clientIds, String icon, String title, String notifyContent, String transmissionContent){			
		// 设置通知栏标题与题目
		templateSigleton.setTitle(title);
		templateSigleton.setText(notifyContent);
		
		// 透传消息设置
		templateSigleton.setTransmissionContent(transmissionContent);
		
		// 配置通知栏图标
		templateSigleton.setLogo(icon);
		
		ListMessage message = new ListMessage();
		message.setData(templateSigleton);
		
		// 设置消息离线，并设置离线时间
		message.setOffline(true);
		
		// 离线有效时间，单位为毫秒，可选
		message.setOfflineExpireTime(24 * 1000 * 3600);
		
		// 配置推送目标
		List targetList = new ArrayList();
		for (String clientId : clientIds){
			
			Target target = new Target();
			target.setAppId(appId);
			target.setClientId(clientId);
			targetList.add(target);
			
			logger.info("push message to " + clientId + "!!!!");
		}

		// taskId用于在推送时去查找对应的message
		String taskId = pushSigleton.getContentId(message);
		IPushResult ret = pushSigleton.pushMessageToList(taskId, targetList);
		
		logger.info(ret.getResponse().toString());		
	}

	private String transVerifyFlag2String(int flag){
		String result = "";
		
		switch(flag){
		case 0:
			result = "预约直播";
			break;
			
		case 1:
			result = "直播预约被确认";
			break;
			
		case 2:
			result = "直播预约被取消";
			break;
			
		case 3:
			result = "取消直播";
			break;
			
		case 4:
			result = "直播即将开始";
			break;
			
		case 5:
			result = "房源已下线";
			break;
			default:
				break;
		}
		
		return result;
	}

	@Override
	public int setReceiveMsg(String userId, boolean receive) {
		// TODO Auto-generated method stub
		return pushClientidMapper.updateReceiveMsgFlagByUserId(userId, receive);
	}

	@Override
	public boolean getReceiveMsgFlag(String userId) {
		// TODO Auto-generated method stub
		return pushClientidMapper.getReceiveMsgFlagByUserId(userId);
	}
}