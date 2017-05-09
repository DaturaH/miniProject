package com.miniProject.dao;

import com.miniProject.model.UserInfo;

public interface UserInfoMapper {
    int deleteByPrimaryKey(String userId);

    int insert(UserInfo record);

    int insertSelective(UserInfo record);

    //根据userId查询
    UserInfo selectByPrimaryKey(String userId);
    
	//根据phoneNumber查询
    UserInfo selectByPhoneNumber(String phoneNumber);

    int updateByPrimaryKeySelective(UserInfo record);

    int updateByPrimaryKey(UserInfo record);
    
    String getPortrait(String phoneNumber);
}