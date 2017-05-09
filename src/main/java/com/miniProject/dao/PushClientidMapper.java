package com.miniProject.dao;

import com.miniProject.model.PushClientidForm;

public interface PushClientidMapper {
    int deleteByPrimaryKey(String userId);

    int insert(PushClientidForm record);

    int insertSelective(PushClientidForm record);

    PushClientidForm selectByPrimaryKey(String userId);
    
    //根据用户clientid删除userId与clientId的绑定
    int deleteByClientId(String clientId);

    int updateByPrimaryKeySelective(PushClientidForm record);

    int updateByPrimaryKey(PushClientidForm record);
    
    int updateReceiveMsgFlagByUserId(String userId, boolean receive);
    
    boolean getReceiveMsgFlagByUserId(String userId);
}