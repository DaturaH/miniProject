package com.miniProject.dao;

import com.miniProject.model.LiveroomForm;

public interface LiveroomMapper {
    int deleteByPrimaryKey(String channelName);

    int insert(LiveroomForm record);

    int insertSelective(LiveroomForm record);

    LiveroomForm selectByPrimaryKey(String channelName);

    int updateByPrimaryKeySelective(LiveroomForm record);

    int updateByPrimaryKey(LiveroomForm record);
}