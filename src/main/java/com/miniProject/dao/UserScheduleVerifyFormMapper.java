package com.miniProject.dao;

import java.util.List;

import com.miniProject.model.UserScheduleVerifyForm;

public interface UserScheduleVerifyFormMapper {
    
	
    List<UserScheduleVerifyForm>getUserScheduleByUserId(String userId,int verifyFlag,int pageIndex);
    List<UserScheduleVerifyForm> getUserAllScheduleByUserId(String userId,int verifyFlag);
    int getUserScheduleCountByUserId(String userId,int verifyFlag);
    
}