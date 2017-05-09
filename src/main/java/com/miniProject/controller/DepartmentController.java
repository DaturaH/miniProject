package com.miniProject.controller;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.miniProject.model.UserHouseForm;
import com.miniProject.service.DepartmentServiceI;
import com.miniProject.service.PersonalCenterServiceI;
import com.miniProject.util.AuthInfo;
import com.miniProject.util.Paging;
import com.miniProject.util.RetMapUtils;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.CommandResult;
import com.mongodb.DBObject;

@Controller
@RequestMapping("/departmentcontroller")
public class DepartmentController {
	
	private static final Logger logger = LoggerFactory.getLogger(DepartmentController.class);
	
	@Autowired
	private MongoOperations mongoOps;
	
	@Autowired
	private PersonalCenterServiceI personalCenterService;
	
	@Autowired
	private DepartmentServiceI departmentService;
	/*
	房客端首页展示
	*/
	
	@RequestMapping(value="homePage")
	@ResponseBody
	public Map<String ,Object> homePage(HttpServletRequest request){

		AuthInfo auth=AuthInfo.get(request);
		if(RetMapUtils.affirmNull(auth)||RetMapUtils.affirmNullString(auth.getUserId())){
			return RetMapUtils.createRetMap(8402, "请登录");
		}
		
		
		Map<String , Object> result = new HashMap<String ,Object>();
		List<Map<String , Object>> houseList = new ArrayList<Map<String , Object>>();
		Map<String , Object> subResult = new HashMap<String , Object>();
		int count = -1;

		String Location = "";   //默认位置为杭州
		String houseType = "";    //默认无房型
		int pricelow = 0;          //默认价格下限为0RMB
		int pricehigh = 100000;    //默认价格上限为100000RMB
		double lo = 30;             //经纬度默认为0，如果读取不到报错，缺少参数
		double la = 120;
		double distance = 100000000;   //默认最远距离为100000公里
		
		if(!RetMapUtils.affirmNull(request.getParameter("Location"))){
			Location = request.getParameter("Location");
		}
		if(!RetMapUtils.affirmNull(request.getParameter("houseType"))){
			houseType = "^" + request.getParameter("houseType");
		}
		if(request.getParameter("pricelow") != null && request.getParameter("pricelow").length() > 0){
			pricelow = Integer.parseInt(request.getParameter("pricelow"));
		}
		if(request.getParameter("pricehigh") != null && request.getParameter("pricehigh").length() > 0){
			pricehigh = Integer.parseInt(request.getParameter("pricehigh"));
		}
		if(request.getParameter("distance") != null && request.getParameter("distance").length() > 0){
			distance = (double)Double.parseDouble(request.getParameter("distance"))/6378137 * 1000;
		}		
		if(RetMapUtils.affirmNull(request.getParameter("longtitude")) || RetMapUtils.affirmNull(request.getParameter("latitude"))){
			return RetMapUtils.createRetMap(8401, "缺少请求参数");
		}else{
			lo = (double)Double.parseDouble(request.getParameter("longtitude"));
			la = (double)Double.parseDouble(request.getParameter("latitude"));
		}
	       //拿到客户端当前页  
        String cur_page = request.getParameter("cur_page");  
        
		Double[] cor = {lo ,la};

//      测试代码	
//		String location = "长河";
//		String houseType = "^1";
//		int pricelow = 1000;
//		int pricehigh = 2000;
//		Double[] cor = {120.123456 , 30.654321};
//		double distance = 10;
//	    String jsonSql = "{ geoNear : 'userHouseForm' , near : [120.123456,30.654321], distanceMultiplier: 6378137, maxDistance : 10, spherical:true, query:{'$and' : [{'price' :{'$lte' : 4000 , '$gte' : 1000 }} , {'location' : /滨江/} , {'houseType' : /^2/} ]}}";
//		System.out.println(Location + " " +
//				   houseType + " " +
//				   pricelow  + " " +
//				   pricehigh + " " +
//				   lo +        " " +
//				   la +        " " +
//				   distance  + " ");
		
		logger.info("Location : {}; houseType: {} ;pricelow : {} ; pricehigh : {} ; longtitude : {} ; latitude : {} ; distance : {}" ,
				Location , houseType , pricelow , pricehigh , lo , la , distance);
				
		BasicDBObject jsonSql = new BasicDBObject();
		BasicDBObject jsonSqlQuery = new BasicDBObject();
		BasicDBObject jsonSqlPrice = new BasicDBObject();		
			
			jsonSql.put("geoNear",  "userHouseForm");		
			jsonSql.put("near", cor);
			jsonSql.put("distanceMultiplier", 6378137);
			jsonSql.put("maxDistance", distance);
			jsonSql.put("spherical", true);
			jsonSqlPrice.put("$lte", pricehigh);
			jsonSqlPrice.put("$gte", pricelow);
	        jsonSqlQuery.put("price",jsonSqlPrice);
			jsonSqlQuery.put("location",java.util.regex.Pattern.compile(Location));
	        jsonSqlQuery.put("houseType" , java.util.regex.Pattern.compile(houseType));      

			
		
		if(auth.getUserType() == 0){	
			
			//房客端首页“智能”排序得到房源列表
			jsonSql.put("query", jsonSqlQuery);
			logger.info("This is a user , userType is {}" , auth.getUserType());

		}else{
			
			//房东中介端首页“智能”排序得到房源列表
			jsonSqlQuery.put("userId",java.util.regex.Pattern.compile(auth.getUserId()));
			jsonSql.put("query", jsonSqlQuery);
			logger.info("The is an agency , userType is {}" , auth.getUserType());
		}
 
        
        if(cur_page.equals("1")){
        	departmentService.updatehomePageSort((DBObject)jsonSql);
        }
		
		
        houseList = (List<Map<String , Object>>)departmentService.homePageSort((DBObject)jsonSql);

        if(houseList == null || houseList.size() < 1){
			return RetMapUtils.createRetMap(8404, "查询记录不存在 ");      	
        }
        count = houseList.size();
        logger.info("rowCount is {}" , count);
        
        
        //分页
        //判断第一次显示时当前页为空时  
        if(cur_page != null && cur_page.length() > 0) {  
        int curPage = Integer.parseInt(cur_page);  
        //数据显示  
        Paging page = new Paging(houseList, curPage, 10);  
        //拿到当前数据  
        houseList = (ArrayList<Map<String , Object>>)page.getData();  
        }
        
		result.put("code", 8200);
		subResult.put("rowCount", count);
		subResult.put("houseList", houseList);
		result.put("data", subResult);		
		return result;
		
	}		
	
	/* 
	 * 
	 房客端首页搜索
	 按照指定的小区名称搜索房源 
	 * 
	 */	
	@RequestMapping(value = "homePageSearch")
	@ResponseBody
	public Map<String ,Object> homePageSearch(HttpServletRequest request){

		AuthInfo auth=AuthInfo.get(request);
		if(RetMapUtils.affirmNull(auth)||RetMapUtils.affirmNullString(auth.getUserId())){
			return RetMapUtils.createRetMap(8402, "请登录");
		}
		
		String villageName = "";
		List<Map<String , Object>> houseList = new ArrayList<Map<String , Object>> ();		
		
		if(!RetMapUtils.affirmNull(request.getParameter("villageName"))){
			villageName = request.getParameter("villageName");
		}else{
			return RetMapUtils.createRetMap(8401, "缺少请求参数");
		}
		
		Map<String , Object> result = new HashMap<String , Object>();
		
		BasicDBObject jsonSql = new BasicDBObject();
		BasicDBObject jsonSqlQuery = new BasicDBObject();
		Map<String , Object> subResult = new HashMap<String , Object>();
		int count = -1;

		jsonSql.put("distinct",  "userHouseForm");
		jsonSql.put("key", "_id");
		jsonSqlQuery.put("villageName" , java.util.regex.Pattern.compile(villageName));
		
		
		if(auth.getUserType() == 0){
			//房客端
			jsonSql.put("query", jsonSqlQuery);
			logger.info("This is a user , userType is {} , userId is {}" , auth.getUserType() , auth.getUserId());

		}else{
			//房东中介端
			jsonSqlQuery.put("userId",java.util.regex.Pattern.compile(auth.getUserId()));
			jsonSql.put("query", jsonSqlQuery);
			logger.info("This is an agency , userType is {}" , auth.getUserType());
		}		
        houseList = (List<Map<String , Object>>)departmentService.PageSearch((DBObject)jsonSql);
		
        if(houseList == null || houseList.size() < 1){
			return RetMapUtils.createRetMap(8404, "查询记录不存在 ");      	
        }
        count = houseList.size();
        logger.info("rowCount is {}" , count);		
        
        //分页
        //拿到客户端当前页  
        String cur_page = request.getParameter("cur_page");  
        //判断第一次显示时当前页为空时  
        if(cur_page != null && cur_page.length() > 0) {  
        int curPage = Integer.parseInt(cur_page);  
        //数据显示  
        Paging page = new Paging(houseList, curPage, 10);  
        //拿到当前数据  
        houseList = (ArrayList<Map<String , Object>>)page.getData();  
        }
               
		result.put("code", 8200);
		subResult.put("rowCount", count);
		subResult.put("houseList", houseList);
		result.put("data", subResult);		
		return result;
		
	}	
}




