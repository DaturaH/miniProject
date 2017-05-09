package com.miniProject.dao;

import java.util.List;

import com.miniProject.model.UserRecordForm;

public interface UserHisRecordFormMapper {
	List<UserRecordForm>getUserScheduleByUserId(String userId,int pageIndex);
	List<UserRecordForm>getUserAllScheduleByUserId(String userId);
	int deleteUserRecordByPublicKey(String recordId,String userId);
	int getUserRecordCountByUserId(String userId);
	
	int deleteAllRecordsByUserId(String userId);
}