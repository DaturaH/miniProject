package com.miniProject.dao;

import java.util.List;

import com.miniProject.model.PushHistoryForm;

public interface PushHistoryMapper {
    int deleteByPrimaryKey(String infoId);

    int insert(PushHistoryForm record);

    int insertSelective(PushHistoryForm record);

    PushHistoryForm selectByPrimaryKey(String infoId);

    int updateByPrimaryKeySelective(PushHistoryForm record);

    int updateByPrimaryKey(PushHistoryForm record);
    
    int getPushHistoryCountByUserId(String userId);
    
    List<PushHistoryForm> getPushHistoryByUserId(String userId, int pageIndex);
    
    int deleteAllRecordsByUserId(String userId);
}