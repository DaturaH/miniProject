package com.miniProject.service;

import java.util.List;
import java.util.Map;

import com.miniProject.model.PushClientidForm;

public interface PushServiceI {
	
	//测试接口
	void testPush(String msg);
	
	//租客生成动作，推送至中介
	void subUser2PubUser(Map<String, Object> msg);
	
	//中介生成动作，推送给租客
	void pubUser2SubUser(List<Map<String, Object> > msgList);
	
	//直播时间到，推送给订阅用户和直播用户
	void liveTimeIsUp(List<Map<String, Object> > msgList);
	
	//clientId插入数据库
	int insert (String userId, String clientId);
	
	//根据userId查询clientId
	String getClientidByPrimaryKey(String userId);
	
	//根据uerId删除clientId
	int deleteByPrimaryKey(String userId);
	
	//设置消息是否提醒
	int setReceiveMsg(String userId, boolean receive);
	//查询是否接收提醒消息
	boolean getReceiveMsgFlag(String userId);
}