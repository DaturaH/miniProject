package com.miniProject.dao;

import java.util.Date;
import java.util.List;

import com.miniProject.model.UserHouseForm;
import com.miniProject.model.UserSubcHouseLiveForm;

public interface UserSubcHouseLiveFormMapper {
    int deleteByPrimaryKey(String liveSubInfoId);

    int insert(UserSubcHouseLiveForm record);

    int insertSelective(UserSubcHouseLiveForm record);

    UserSubcHouseLiveForm selectByPrimaryKey(String liveSubInfoId);

    int updateByPrimaryKeySelective(UserSubcHouseLiveForm record);

    int updateByPrimaryKey(UserSubcHouseLiveForm record);
    
    int updateVerifyFlagByPublicKeySelective(UserSubcHouseLiveForm record);
    
    List<UserSubcHouseLiveForm> selectHouseDailyLiveCountByHouseId(String houseid,int intervalTime);
    
    List<UserSubcHouseLiveForm> getOwnVerifyTimeListById(String houseid,String userid,int intervalTime);
    
    List<Date> getOwnOtherVerifyTimeListById(String houseid,String userid,int intervalTime);
    
    List<UserSubcHouseLiveForm> getUserDailyInfoCount(String houseid,String userid,int intervalTime);
    
    int selectHouseLiveByBeginTime(String houseid,Date beginTime);
    
    List<UserSubcHouseLiveForm> getOtherVerifyTimeListById(String houseid,String userid,int intervalTime);
    
    List<UserSubcHouseLiveForm> getOwnSubcHouseLiveListById(String houseid,String userid,int intervalTime);
    
    List<Date> getLandlordVerifyTimeListById(String houseid,int intervalTime);    
    
    List<Date> selectHouseLiveTime(String houseid);
    /***
     * 用户取消预订直播
     * */
    int cancelHouseLiveBySelf(UserSubcHouseLiveForm record);
    /***
     *用户在直播日程中取消自己的预约消息 
     **/
    int cancelHouseLiveBySelfInUserSchedule(UserSubcHouseLiveForm record);
    
    //获取最近的直播确认时间；若没有，则返回空
    Date getNearastSubBeginTime(String houseId);
    
    //清除记录时，根据用户id清除verifyFlag为2和3的记录
    int deleteRecordsByUserIdAndFlag(String userId);
}