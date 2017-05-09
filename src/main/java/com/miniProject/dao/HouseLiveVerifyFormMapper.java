package com.miniProject.dao;

import java.util.Date;
import java.util.List;

import com.miniProject.model.HouseLiveVerifyForm;

public interface HouseLiveVerifyFormMapper {
    int deleteByPrimaryKey(String liveVerifyInfoId);

    int insert(HouseLiveVerifyForm record);

    int insertSelective(HouseLiveVerifyForm record);

    HouseLiveVerifyForm selectByPrimaryKey(String liveVerifyInfoId);

    int updateByPrimaryKeySelective(HouseLiveVerifyForm record);

    int updateByPrimaryKey(HouseLiveVerifyForm record);
    
    List<Date> selectHouseLiveTime(String houseid);
    
    public List<HouseLiveVerifyForm> getOwnVerifyTimeListById(String houseid,String userid);
    
	public List<HouseLiveVerifyForm> getOtherVerifyTimeListById(String houseid,String userid);
}