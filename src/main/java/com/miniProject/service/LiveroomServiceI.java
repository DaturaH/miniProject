package com.miniProject.service;

import java.util.Map;

public interface LiveroomServiceI {
	
	//创建直播间：houseId为频道名；creator为聊天室所属账号，houseId为聊天室名称
	Map<String, Object> createLiveroom(String creator, String houseId) throws Exception;
	//删除直播间：根据创建者账号和houseId、roomId
	Map<String, Object> deleteLiveroom(String creator, String houseId, String roomId) throws Exception;
	//查询直播间：根据houseId，查询到聊天室ID和对应频道的拉流地址
	Map<String, Object> queryLiveroom(String houseId) throws Exception;
	
	//设置频道录制状态
	Map<String, Object> setChannelAlwaysRecord(String cid, String needRecord) throws Exception;
	//获取录制视频文件列表
	Map<String, Object> getVideoList(String cid) throws Exception;
	
	Map<String, Object> getPortrait(String yunxinId);
}