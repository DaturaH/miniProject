package com.miniProject.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.miniProject.model.LiveroomForm;
import com.miniProject.service.VcServiceI;
import com.miniProject.util.JsonResponse;
import com.miniProject.util.RetMapUtils;
import com.miniProject.controller.LiveroomController;
import com.miniProject.dao.LiveroomMapper;
import com.miniProject.dao.UserInfoMapper;

@Service("liveroomService")
public class LiveroomServiceImpl implements LiveroomServiceI{
	private static final Logger logger = LoggerFactory.getLogger(LiveroomController.class);
	
	@Autowired
	private VcServiceI vcservice;
	@Autowired
	private ImServiceI imservice;
	@Autowired
	private LiveroomMapper liveroomMapper;
	@Autowired
	private HouseServiceI houseService;
	@Autowired
	private UserInfoMapper userMapper;
	
	//直播创建请求：包括频道创建和聊天室创建
	public Map<String, Object> createLiveroom(String creator, String houseId) throws Exception{
		Map<String, Object> result = new HashMap<String, Object>();
		
		//频道名为houseId
		Map<String, Object> createChanResult = JsonResponse.toMap(vcservice.create(houseId, 0));
		if ((int)createChanResult.get("code") != 200)
		{
			if ((int)createChanResult.get("code") != 604){
				//创建频道失败,且原因不是 频道名已存在
				logger.error("create channel failed");
				return RetMapUtils.createRetMap((int)createChanResult.get("code"), "创建频道失败");
			}
			
			// 频道名称已存在，从数据库中查询到频道的对应信息直接返回
			logger.info("channel name = " + houseId + ", exist in database, so return");
			LiveroomForm liveroomForm = liveroomMapper.selectByPrimaryKey(houseId);
			result.put("code", 200);
			//频道ID
			result.put("cid", liveroomForm.getChannelId());
			//推流地址
			result.put("pushUrl", liveroomForm.getPushUrl());
			//拉流地址
			result.put("rtmpPullUrl", liveroomForm.getPullUrl());
			//聊天室ID
			result.put("roomid", liveroomForm.getRoomId());
			
			//重新打开已有的聊天室（此处不判断聊天室是否处于打开状态）
			imservice.toggleCloseStatChatRoom(liveroomForm.getRoomId(), creator, "true");
			//更改房源直播状态
			houseService.modifyHouseLiveFlag(houseId, true);	
			return result;
		}
		
		Map<String, Object> chanRet = (Map<String, Object>)createChanResult.get("ret");
		//频道ID
		result.put("cid", chanRet.get("cid"));
		//推流地址
		result.put("pushUrl", chanRet.get("pushUrl"));
		//拉流地址
		result.put("rtmpPullUrl", chanRet.get("rtmpPullUrl"));
			
		//聊天室属主账号creator为云信ID；聊天室名称为houseId
		Map<String, Object> createChatroomResult = 
				JsonResponse.toMap(imservice.createChatRoom(creator, houseId, "", "", ""));
		if ((int)createChatroomResult.get("code") != 200)
		{
			//创建聊天室失败
			logger.error("create chat room failed");
			result.put("code", createChatroomResult.get("code"));
			//删除前面创建的频道
			String deleteChan = vcservice.delete((String)chanRet.get("cid"));	
			return result;
		}
		
		Map<String, Object> chatroom = (Map<String, Object>)createChatroomResult.get("chatroom");
		//聊天室ID
		result.put("roomid", chatroom.get("roomid"));
		
		LiveroomForm liveroomForm = new LiveroomForm();
		liveroomForm.setChannelName(houseId);
		liveroomForm.setCreator(creator);
		liveroomForm.setRoomName(houseId);
		liveroomForm.setChannelId((String)chanRet.get("cid"));
		liveroomForm.setPushUrl((String)chanRet.get("pushUrl"));
		liveroomForm.setPullUrl((String)chanRet.get("rtmpPullUrl"));
		liveroomForm.setRoomId(chatroom.get("roomid").toString());	
		
		//数据库中插入直播间信息
		liveroomMapper.insert(liveroomForm);
		result.put("code", 200);
		
		//更改房源直播状态
		houseService.modifyHouseLiveFlag(houseId, true);
		logger.info("change house = " + houseId + " live state to false");
		
		return result;
	}
	
	//请求删除直播间，包括删除频道和聊天室
	public Map<String, Object> deleteLiveroom(String creator, String houseId, String roomId) throws Exception{
		//更改房源直播状态
		houseService.modifyHouseLiveFlag(houseId, false);
		logger.info("change house = " + houseId + " live state to false");
		
		//根据houseId即chanName,查询数据库，找到cid，执行频道删除操作
		LiveroomForm liveroom = liveroomMapper.selectByPrimaryKey(houseId);
		if (liveroom == null){
			logger.warn("empty liveroom");
			return RetMapUtils.createRetMap(8404, "直播间不存在");
		}
		
		Map<String, Object> result = new HashMap<String, Object>();
		String cid = liveroom.getChannelId();
		
		//删除频道:根据频道ID
		Map<String, Object> delChanResult = JsonResponse.toMap(vcservice.delete(cid));
		if ((int)delChanResult.get("code") != 200)
		{
			//删除频道失败
			logger.error("delete channel failed");	
			result.put("code", delChanResult.get("code"));
			
			//删除频道失败仍然继续删除聊天室，并将记录从数据库中删除
			//return result;
		}
		
		//根据聊天室id、操作者账号关闭聊天室
		Map<String, Object> delChatroomResult = 
				JsonResponse.toMap(imservice.toggleCloseStatChatRoom(roomId, creator, "false"));
		//该操作返回值没有code
		if ((int)delChatroomResult.get("code") != 200)
		{
			//关闭聊天室失败
			logger.error("close chat room failed");
			result.put("code", delChatroomResult.get("code"));
			
			return result;
		}
		
		//从数据库中删除直播间信息
		liveroomMapper.deleteByPrimaryKey(houseId);
		result.put("code", 200);
		return result;
	}
	
	//查询拉流地址和聊天室ID
	public Map<String, Object> queryLiveroom(String houseId) throws Exception{
			
		LiveroomForm liveroom = liveroomMapper.selectByPrimaryKey(houseId);
		if (liveroom == null){
			logger.warn("empty liveroom");
			return RetMapUtils.createRetMap(8404, "直播间不存在");
		}
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("rtmpPullUrl", liveroom.getPullUrl());
		result.put("roomid", liveroom.getRoomId());
		result.put("code", 200);
		
		return result;
	}
	
	//设置频道录制状态
	public Map<String, Object> setChannelAlwaysRecord(String cid, String needRecord) throws Exception{
		
		String result = vcservice.setAlwaysRecord(cid, Integer.parseInt(needRecord), 1, 120, cid);
		return JsonResponse.toMap(result);
	}
	
	//获取录制视频文件列表
	public Map<String, Object> getVideoList(String cid) throws Exception{
		String result = vcservice.getVideoList(cid);
		return  JsonResponse.toMap(result);
	}

	@Override
	public Map<String, Object> getPortrait(String yunxinId) {
		// TODO Auto-generated method stub
		String portrait = userMapper.getPortrait(yunxinId);
		if (portrait == null){
			logger.warn("no portrait file with yunxin id = " + yunxinId);
			return RetMapUtils.createRetMap(8508, "用户信息不存在");
		}
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 8200);
		
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("portrait", portrait);
		result.put("data", data);
		
		return result;
	}
}

