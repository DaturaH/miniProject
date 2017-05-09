package com.miniProject.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.miniProject.dao.PushHistoryMapper;
import com.miniProject.model.PushHistoryForm;
import com.miniProject.service.PushHistoryServiceI;

@Service("pushHistoryService")
public class PushHistoryServiceImpl implements PushHistoryServiceI{

	@Autowired
	private PushHistoryMapper pushHistoryMapper;
	
	@Override
	public int getPushHistoryCountByUserId(String userId) {
	
		return pushHistoryMapper.getPushHistoryCountByUserId(userId);
	}

	@Override
	public List<PushHistoryForm> getPushHistoryByUserId(String userId, int pageIndex) {
		
		return pushHistoryMapper.getPushHistoryByUserId(userId, pageIndex);
	}
	
	@Override
	public int deleteHistoryMsgByUserId(String userId) {
		// TODO Auto-generated method stub
		return pushHistoryMapper.deleteAllRecordsByUserId(userId);
	}

	@Override
	public int insert(PushHistoryForm record) {
		// TODO Auto-generated method stub
		return pushHistoryMapper.insert(record);
	}
}