package com.miniProject.dao;

import com.miniProject.model.HouseWantedInfo;

public interface HouseWantedInfoMapper {
    int deleteByPrimaryKey(String userId);

    int insert(HouseWantedInfo record);

    int insertSelective(HouseWantedInfo record);

    HouseWantedInfo selectByPrimaryKey(String userId);

    int updateByPrimaryKeySelective(HouseWantedInfo record);

    int updateByPrimaryKey(HouseWantedInfo record);
}