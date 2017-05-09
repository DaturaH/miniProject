package com.miniProject.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.miniProject.model.UserSubcHouseLiveForm;
import com.miniProject.service.AgencyScheduleServiceI;
import com.miniProject.service.HouseServiceI;
import com.miniProject.service.ImServiceI;
import com.miniProject.service.LiveroomServiceI;
import com.miniProject.service.PushServiceI;
import com.miniProject.util.AuthInfo;
import com.miniProject.util.HttpRequestUtils;
import com.miniProject.util.RetMapUtils;

//直播请求
@Controller
@RequestMapping("/livecontroller")
public class LiveroomController {
	private static final Logger logger = LoggerFactory.getLogger(LiveroomController.class);
	
	@Autowired
	private LiveroomServiceI liveroomService;
	@Autowired
	private HouseServiceI houseService;
	@Autowired
	private AgencyScheduleServiceI agencyScheduleService;
	
	@RequestMapping(value = "/create")
	@ResponseBody
	public Map<String, Object> create(@RequestBody final Map<String, Object> request,HttpServletRequest req) throws Exception{
		String creator = (String)request.get("creator");
		String houseId = (String)request.get("houseId");
		
		Map<String, Object> result = liveroomService.createLiveroom(creator, houseId);	
		if (200 != (int)result.get("code")){
			logger.error("create liveroom failed");
			return RetMapUtils.createRetMap(8501, "创建频道失败");
		}
		
		//操作成功
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("code", 8200);
		
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("cid", result.get("cid"));
		data.put("pushUrl", result.get("pushUrl"));
		data.put("rtmpPullUrl", result.get("rtmpPullUrl"));
		//roomId内部返回的是int，返回给外部的是String
		data.put("roomid", result.get("roomid").toString());	
		response.put("data", data);
		logger.info("create liveroom successfully");
		
		//创建直播间时给预约用户推送通知
		agencyScheduleService.getTimeIntervalSubInfoByHouseId(houseId);
		/**
		 *开启视频录制模式 
		 **/
/*		AuthInfo auth=AuthInfo.get(req);
		if(RetMapUtils.affirmNull(auth)||RetMapUtils.affirmNullString(auth.getUserId())){
			return RetMapUtils.createRetMap(8405, "非法请求");
		}*/
		//HttpRequestUtils.videoRecord(auth.getUserId(), houseId, (String)result.get("cid"));
		HttpRequestUtils.videoRecord(creator, houseId, (String)result.get("cid"));
		return response;
	}
	
	@RequestMapping(value = "/delete")
	@ResponseBody
	public Map<String , Object> delete(@RequestBody final Map<String, Object> request) throws Exception{
		String creator = (String)request.get("creator");
		String houseId = (String)request.get("houseId");
		String roomId = (String)request.get("roomId");
		Map<String, Object> result = liveroomService.deleteLiveroom(creator, houseId, roomId);	
		if (200 != (int)result.get("code")){
			logger.error("close liveroom failed");
			return RetMapUtils.createRetMap(8502, "删除频道失败");
		}
		
		logger.info("close liveroom successfully");
		return RetMapUtils.createRetMap(8200, "删除频道成功");
	}
	
	@RequestMapping(value = "/query")
	@ResponseBody
	//租客端请求拉流地址和聊天室ID
	public Map<String, Object> query(@RequestBody final Map<String, Object> request) throws Exception{
		
		String houseId = (String)request.get("houseId");
		
		Map<String, Object> result = liveroomService.queryLiveroom(houseId);
		if (200 != (int)result.get("code")){
			logger.error("query liveroom failed");
			return RetMapUtils.createRetMap(8503, "查询直播间失败");
		}
		
		//操作成功
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("code", 8200);
		
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("rtmpPullUrl", result.get("rtmpPullUrl"));
		data.put("roomid", result.get("roomid"));
		response.put("data", data);
		
		logger.info("query liveroom successfully");
		return response;
	}
	
	@RequestMapping(value = "/setrecord")
	@ResponseBody
	public Map<String, Object> setAlwaysRecord(@RequestBody final Map<String, Object> request) throws Exception{
		
		String cid = (String)request.get("cid");
		String needRecord = ((String)request.get("needRecord"));
		
		Map<String, Object> result = liveroomService.setChannelAlwaysRecord(cid, needRecord);		
		if (200 != (int)result.get("code")){
			logger.error("set channel record failed");
			return RetMapUtils.createRetMap(8504, "设置频道录制状态失败");
		}
		
		logger.info("set record successfully");	
		return RetMapUtils.createRetMap(8200, "设置频道录制状态成功");
	}
	
	@RequestMapping(value = "/getvideolist")
	@ResponseBody
	public Map<String, Object> getVideoList(@RequestBody final Map<String, Object> request) throws Exception{
		
		String cid = (String)request.get("cid");
		Map<String, Object> result = liveroomService.getVideoList(cid);
		if (200 != (int)result.get("code")){

			logger.error("get vide list failed");
			return RetMapUtils.createRetMap(8505, "获取频道录像列表失败");
		}
		
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("code", 8200);
		response.put("data", result.get("ret"));
		
		logger.info("get record list failed");
		return response;
	}
	
	@Autowired
	private ImServiceI imService;
	@RequestMapping(value = "/queryChatroom")
	@ResponseBody
	//查询聊天室状态
	public String queryChatroom(String roomid) throws Exception{
		
		return imService.getChatRoom(roomid, "true");
	}
	
	@RequestMapping(value = "/getportrait")
	@ResponseBody	
	public Map<String, Object> getPortrait(HttpServletRequest request, @RequestBody final Map<String, Object> msg) throws Exception{
		AuthInfo auth = AuthInfo.get(request);
		if(RetMapUtils.affirmNull(auth)||RetMapUtils.affirmNullString(auth.getUserId())){
			return RetMapUtils.createRetMap(8402, "请登录");
		}
		
		String yunxinId = (String)msg.get("yunxinId");		
		return liveroomService.getPortrait(yunxinId);	
	}
}

