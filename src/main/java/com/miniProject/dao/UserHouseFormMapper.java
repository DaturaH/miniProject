package com.miniProject.dao;

import java.util.List;

import com.miniProject.model.UserHouseForm;

public interface UserHouseFormMapper {
    int deleteByPrimaryKey(String houseId);

    int insert(UserHouseForm record);

    int insertSelective(UserHouseForm record);

    UserHouseForm selectByPrimaryKey(String houseId);
    
    int updateByPrimaryKeySelective(UserHouseForm record);
    
    int updateByPrimaryKey(UserHouseForm record);
    /***
     *更新房源直播状态
     *@param userId 用户id
     *@param houseId 房源id
     *@param liveState 房源直播状态位 true表示在直播，false表示未直播
     **/
    int updateHouseLiveState(String userId,String houseId,boolean liveState);
    //根据userId获取发布房源的数量
    int getPubHouseCountByUserId(String userId);
    
    List<UserHouseForm> getAll();

    //根据userId修改contact信息
    int updateContactByPrimaryKey(String userId, String contact);
    
    //修改房源直播状态
    int modifyHouseLiveFlagByHouseId(String houseId, boolean liveFlag);
}