package com.miniProject.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.CommandResult;
import com.mongodb.DBObject;


@Service("departmentService")
public class DepartmentServiceImpl implements DepartmentServiceI{

	@Autowired
	private MongoOperations mongoOps;
	
	@Autowired
	private PersonalCenterServiceI personalCenterService;
	
	Logger logger = LoggerFactory.getLogger(DepartmentServiceImpl.class);
	
	

	/*
	 * 
	 首页按策划妹纸要求设计的房客端智能筛选排序
	 * 
	 */	
	@Cacheable("homePageSort")
	@Override
	public List<Map<String , Object>> homePageSort(DBObject jsonSql){
		
		List<String> houseIdList = new ArrayList<String>();
		List<Map<String , Object>> houseList = new ArrayList<Map<String , Object>>();
		
		CommandResult commandResult=mongoOps.executeCommand((DBObject)jsonSql);
		
        BasicDBList list = (BasicDBList)commandResult.get("results");  
        if(list == null || list.size() < 1){
			return new ArrayList<Map<String , Object>>();
        }
        int count = list.size();
        logger.info("rowCount is {}" , count);
        for (int i = 0; i < list.size(); i ++) {
        	BasicDBObject obj = (BasicDBObject)list.get(i);
        	BasicDBObject obj1 = (BasicDBObject)obj.get("obj");
        	String houseId = (String)obj1.get("_id");    
        	houseIdList.add(houseId);
            logger.info("houseId is {}" ,houseId);
        } 
            
        houseList = personalCenterService.getHouseByHouseIdList(houseIdList);
        
        
        for(int i = 0 ; i < houseList.size() ; i++){
        	int weight = (int)((Map<String , Object>)houseList.get(i)).get("weight") ; 
        	logger.info("houseName is {} , weight is {}" ,(String)((Map<String , Object>)houseList.get(i)).get("title") ,  weight);//获取按直播时间确定的权重
        	weight = weight *  houseList.size() + houseList.size() - i;                                      //最终权重
        	((Map<String , Object>)houseList.get(i)).get(weight);
        	logger.info("weight is {}" , weight);
        }
        
        Collections.sort(houseList, new Comparator<Map<String , Object>>() {
            public int compare(Map<String , Object> arg0, Map<String , Object> arg1) {
            	int subArg0 = (int)((Map<String , Object>)arg0).get("weight");
            	int subArg1 = (int)((Map<String , Object>)arg1).get("weight");
                return subArg1 - subArg0;
            }
        });
    	return houseList;
	}
	
	
	@CacheEvict(value = { "homePageSort"})  	
	public void updatehomePageSort(DBObject jsonSql){
	}
	
	/*
	 * 
	 首页按策划妹纸要求设计的房客端智能搜索
	 * 
	 */	
	@Cacheable("PageSearch")
	@Override
	public List<Map<String , Object>> PageSearch(DBObject jsonSql){
		
		List<Map<String , Object>> houseList = new ArrayList<Map<String , Object>>();
		
		CommandResult commandResult=mongoOps.executeCommand((DBObject)jsonSql);	
		BasicDBList houseIdList = (BasicDBList)commandResult.get("values");  
		logger.info("size of houseIdList is {}" ,houseIdList.size() );

        houseList = personalCenterService.getHouseByHouseIdList((List)houseIdList);		
		
    	return houseList;
	}	

	
}
