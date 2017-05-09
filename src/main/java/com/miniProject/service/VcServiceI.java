package com.miniProject.service;

public interface VcServiceI {    
    String create(String name , int type) throws Exception;
    
    String change(String name , String cid , int type) throws Exception;
    
    String delete(String cid) throws Exception;
    
    String channelstats(String cid) throws Exception;
    
    String channellist(int records , int pnum , String ofield , int sort) throws Exception;
    
    String address(String cid) throws Exception;
    
    //设置频道录制状态
    String setAlwaysRecord(String cid , int needRecord, int format, int duration, String filename) throws Exception;
    
    //获取录制视频文件列表
    String getVideoList(String cid) throws Exception;
}
