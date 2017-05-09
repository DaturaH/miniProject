package com.miniProject.controller;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.miniProject.service.PersonalCenterServiceI;
import com.miniProject.util.AuthInfo;
import com.miniProject.util.RetMapUtils;

@Controller
@RequestMapping("/personalcenter")
public class PersonalCenterController{
	private static final Logger logger = LoggerFactory.getLogger(PersonalCenterController.class);
	
	@Autowired
	private PersonalCenterServiceI personalCenterService;
	
	@RequestMapping(value = "/show")
    @ResponseBody
    public Map<String, Object> show(HttpServletRequest request) {
		
		AuthInfo auth = AuthInfo.get(request);
		if(RetMapUtils.affirmNull(auth)||RetMapUtils.affirmNullString(auth.getUserId())){
			return RetMapUtils.createRetMap(8402, "请登录");
		}
		
    	return personalCenterService.getPersonalCenter(auth.getUserId(), auth.getUserType());
    }
	
	@RequestMapping(value = "/modifyusername")
    @ResponseBody
    public Map<String, Object> modifyUserName(HttpServletRequest request, @RequestBody final Map<String, Object> msg) {
		
		AuthInfo auth = AuthInfo.get(request);
		if(RetMapUtils.affirmNull(auth)||RetMapUtils.affirmNullString(auth.getUserId())){
			return RetMapUtils.createRetMap(8402, "请登录");
		}
		
		//将输入的中文转成utf-8写入数据库
		//String userNameUTF8 = EncodingTool.encodeStr((String)msg.get("userName"));
		logger.info("change user name to " + (String)msg.get("userName"));
		
    	return personalCenterService.modifyUserName(auth.getUserId(), (String)msg.get("userName"));
    }
	
	@RequestMapping("/uploadportrait")
	@ResponseBody
	public Map<String, Object> uploadPortrait(HttpServletRequest request, MultipartFile file) throws UnsupportedEncodingException {
			
		AuthInfo auth = AuthInfo.get(request);
		if(RetMapUtils.affirmNull(auth)||RetMapUtils.affirmNullString(auth.getUserId())){
			return RetMapUtils.createRetMap(8402, "请登录");
		}
		
		return personalCenterService.uploadPortraitFile(file, auth.getUserId());
	}
	
	@RequestMapping("/favoratehouse")
	@ResponseBody
	public Map<String, Object> favorateHouse(HttpServletRequest request, String index) throws UnsupportedEncodingException {
			
		AuthInfo auth = AuthInfo.get(request);
		if(RetMapUtils.affirmNull(auth)||RetMapUtils.affirmNullString(auth.getUserId())){
			return RetMapUtils.createRetMap(8402, "请登录");
		}
		
		int totalNum = personalCenterService.getFavorHouseCountByUserId(auth.getUserId());
		if (totalNum == 0){
			//超出条目数，返回错误
			logger.warn("总条目数为0");
			return RetMapUtils.createRetMap(8507, "请求参数超出范围");
		}
		
		int pageIndex=1;
		if(index != null){
			pageIndex=Integer.parseInt(index);
			
			if ((pageIndex - 1) * 10 > totalNum){
				//超出条目数，返回错误
				logger.warn("请求参数pageIndex = " + pageIndex + "超出范围, 总条目数为" + totalNum);
				return RetMapUtils.createRetMap(8507, "请求参数超出范围");
			}
			
			//分页获取收藏房源
			return personalCenterService.getFavorHouseByUserId(auth.getUserId(), (pageIndex - 1) * 10);
		}
		else{
			return personalCenterService.getAllFavorHouseByUserId(auth.getUserId());
		}	
	}
	
	@RequestMapping("/getconfig")
	@ResponseBody
	public Map<String, Object> getConfig(HttpServletRequest request) throws UnsupportedEncodingException {
			
		AuthInfo auth = AuthInfo.get(request);
		if(RetMapUtils.affirmNull(auth)||RetMapUtils.affirmNullString(auth.getUserId())){
			return RetMapUtils.createRetMap(8402, "请登录");
		}		

		return personalCenterService.getConfig(auth.getUserId());
	}
	
	@RequestMapping("/modifyconfig")
	@ResponseBody
	public Map<String, Object> modifyConfig(HttpServletRequest request, @RequestBody final Map<String, Object> msg) throws UnsupportedEncodingException {
			
		AuthInfo auth = AuthInfo.get(request);
		if(RetMapUtils.affirmNull(auth)||RetMapUtils.affirmNullString(auth.getUserId())){
			return RetMapUtils.createRetMap(8402, "请登录");
		}		

		return personalCenterService.modifyConfig(auth.getUserId(), (boolean)msg.get("receiveFlag"));
	}
	
	@RequestMapping("/clearHistory")
	@ResponseBody
	public Map<String, Object> clearHistory(HttpServletRequest request) throws UnsupportedEncodingException {
			
		AuthInfo auth = AuthInfo.get(request);
		if(RetMapUtils.affirmNull(auth)||RetMapUtils.affirmNullString(auth.getUserId())){
			return RetMapUtils.createRetMap(8402, "请登录");
		}		

		return personalCenterService.clearHistory(auth.getUserId());
	}
}