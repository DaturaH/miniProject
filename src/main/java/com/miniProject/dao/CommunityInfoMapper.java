package com.miniProject.dao;

import java.util.List;

import com.miniProject.model.CommunityInfo;

public interface CommunityInfoMapper {
    int insert(CommunityInfo record);

    int insertSelective(CommunityInfo record);
    
    List<String> match(String part);
    List<String> matchBegin(String part);
    CommunityInfo locating(String name);
}