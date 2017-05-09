package com.miniProject.dao;

import java.util.List;

import com.miniProject.model.UserHousePicForm;

public interface UserHousePicFormMapper {
    int deleteByPrimaryKey(String pictureId);

    int insert(UserHousePicForm record);

    int insertSelective(UserHousePicForm record);

    UserHousePicForm selectByPrimaryKey(String pictureId);
    
    List<String> selectByHouseId(String houseId);
    
    //获取一张房源图片
    String getPicByHouseId(String houseId);
    
    List<UserHousePicForm> getPicFormsByHouseId(String houseId);

    int updateByPrimaryKeySelective(UserHousePicForm record);

    int updateByPrimaryKey(UserHousePicForm record);
}