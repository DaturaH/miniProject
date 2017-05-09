package com.miniProject.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.miniProject.model.PushHistoryForm;
import com.miniProject.service.PushHistoryServiceI;
import com.miniProject.service.PushServiceI;
import com.miniProject.util.AuthInfo;
import com.miniProject.util.JsonResponse;
import com.miniProject.util.RetMapUtils;

//直播请求
@Controller
@RequestMapping("/pushrequestcontroller")
public class PushRequestController {
	private static final Logger logger = LoggerFactory.getLogger(PushRequestController.class);
	
	@Autowired
	private PushServiceI pushService;
	
	@Autowired
	private PushHistoryServiceI pushHistoryService;
	
	@RequestMapping(value = "/insert")
	@ResponseBody	
	public Map<String, Object> insert(HttpServletRequest request, @RequestBody final Map<String, Object> msg) throws Exception{
		AuthInfo auth = AuthInfo.get(request);
		if(RetMapUtils.affirmNull(auth)||RetMapUtils.affirmNullString(auth.getUserId())){
			return RetMapUtils.createRetMap(8402, "请登录");
		}
		
		String clientId = (String)msg.get("clientId");		
		pushService.insert(auth.getUserId(), clientId);

		return RetMapUtils.createRetMap(8200, "添加用户推送ID成功");		
	}
	
	@RequestMapping(value = "/delete")
	@ResponseBody	
	public Map<String, Object> delete(HttpServletRequest request) throws Exception{
		AuthInfo auth = AuthInfo.get(request);
		if(RetMapUtils.affirmNull(auth)||RetMapUtils.affirmNullString(auth.getUserId())){
			return RetMapUtils.createRetMap(8402, "请登录");
		}
				
		pushService.deleteByPrimaryKey(auth.getUserId());
		
		return RetMapUtils.createRetMap(8200, "删除用户推送ID成功");	
	}
	
	@RequestMapping(value = "/query")
	@ResponseBody	
	public Map<String, Object> query(HttpServletRequest request, @RequestBody final Map<String, Object> msg) throws Exception{
		logger.info("##########   query push history here  ###############");
		
		AuthInfo auth = AuthInfo.get(request);
		if(RetMapUtils.affirmNull(auth)||RetMapUtils.affirmNullString(auth.getUserId())){
			return RetMapUtils.createRetMap(8402, "请登录");
		}
		
		int pageIndex=1;
		if(!RetMapUtils.affirmNullString((String)msg.get("pageIndex"))){
			pageIndex=Integer.parseInt((String)msg.get("pageIndex"));
		}
				
		int totalNum = pushHistoryService.getPushHistoryCountByUserId(auth.getUserId());
		if ((pageIndex - 1) * 10 > totalNum){
			//超出条目数，返回错误
			logger.warn("请求参数pageIndex = " + pageIndex + "超出范围, 总条目数为" + totalNum);
			return RetMapUtils.createRetMap(8507, "请求参数超出范围");
		}
		
		//查询出该用户的推送信息，在data中用List<String>形式传递给前端
		List msgList = new ArrayList();
		List<PushHistoryForm> records = pushHistoryService.getPushHistoryByUserId(auth.getUserId(), (pageIndex - 1) * 10);
		if(records != null && records.size() > 0){
			for(PushHistoryForm record : records){	
				String pushMsg = record.getPushMsg();
				logger.info("##### " + pushMsg + " #####");
				
				msgList.add(JsonResponse.toMap(pushMsg));
			}
		}
		
		logger.info("##### record size = " + msgList.size() + "####");
		
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("code", 8200);
		response.put("data", msgList);
		
		return response;
	}
	
	//测试接口
	@RequestMapping(value = "/test")
	@ResponseBody	
	public Map<String, Object> test(HttpServletRequest request) throws Exception{
		Map<String, Object> response = new HashMap<String, Object>();
		
		AuthInfo auth = AuthInfo.get(request);
		if(RetMapUtils.affirmNull(auth)||RetMapUtils.affirmNullString(auth.getUserId())){
			return RetMapUtils.createRetMap(8402, "请登录");
		}
		
		int totalNum = pushHistoryService.getPushHistoryCountByUserId(auth.getUserId());
		
		return response;
	}
}