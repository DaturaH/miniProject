package com.miniProject.service;

import java.util.List;

import com.miniProject.model.PushHistoryForm;

public interface PushHistoryServiceI{
	//查询该用户接收到的推送消息总数
	int getPushHistoryCountByUserId(String userId);
	
	//根据userId和分页序号，查询推送历史
	List<PushHistoryForm> getPushHistoryByUserId(String userId, int pageIndex);
	
	//清除推送消息历史
	int deleteHistoryMsgByUserId(String userId);
	
	int insert(PushHistoryForm record);
}