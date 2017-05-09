package com.miniProject.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.miniProject.model.UserFavorHouseForm;

public interface UserFavorHouseFormMapper {
    int deleteByPrimaryKey(String infoId);

    int insert(UserFavorHouseForm record);

    int insertSelective(UserFavorHouseForm record);

    UserFavorHouseForm selectByPrimaryKey(String infoId);
    
    UserFavorHouseForm selectById(@Param("houseid")String houseid,@Param("userid")String userid);

    int updateByPrimaryKeySelective(UserFavorHouseForm record);

    int updateByPrimaryKey(UserFavorHouseForm record);
    
    int deleteFavorHouseById(@Param("userid")String userid,@Param("houseid")String houseid);
    
    //根据userId获取收藏房源的数量
    int getFavorHouseCountByUserId(String userId);
    
    //根据userId获取收藏房源信息,分页
    List<UserFavorHouseForm> getFavorHouseByUserId(String userId, int pageIndex);
    //获取全部收藏房源
    List<UserFavorHouseForm> getAllFavorHouseByUserId(String userId);
}