package com.miniProject.service;

import java.util.List;
import java.util.Map;

import com.miniProject.model.AgencyScheduleVerifyForm;
import com.mongodb.DBObject;

public interface DepartmentServiceI {
	
	//首页智能排序
	public List<Map<String , Object>> homePageSort(DBObject jsonSql);
    
	//首页智能搜索
	public List<Map<String , Object>> PageSearch(DBObject jsonSql);
	
	//首页更新
	public void updatehomePageSort(DBObject jsonSql);

}
