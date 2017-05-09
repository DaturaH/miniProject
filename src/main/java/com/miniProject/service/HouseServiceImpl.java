package com.miniProject.service;

import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.DataFormatReaders.Match;
import com.miniProject.dao.CommunityInfoMapper;
import com.miniProject.dao.UserFavorHouseFormMapper;
import com.miniProject.dao.UserHouseFormMapper;
import com.miniProject.dao.UserHousePicFormMapper;
import com.miniProject.dao.UserSubcHouseLiveFormMapper;
import com.miniProject.model.CommunityInfo;
import com.miniProject.model.UserFavorHouseForm;
import com.miniProject.model.UserHouseForm;
import com.miniProject.model.UserSubcHouseLiveForm;
@Service("houseService")
public class HouseServiceImpl implements HouseServiceI {
	
	@Autowired
	private CommunityInfoMapper communityInfoMapper;
	@Autowired
	private UserHouseFormMapper houseMapper;
	@Autowired
	private UserHousePicFormMapper housePicMapper;
	@Autowired
	private UserFavorHouseFormMapper favorHouseFormMapper;
	@Autowired
	private UserSubcHouseLiveFormMapper userSubcHouseLiveFormMapper;	
	@Override
	public UserHouseForm getByHouseId(String houseId) {
		return houseMapper.selectByPrimaryKey(houseId);
	}
	@Override
	public List<String> getPicListByHouseId(String houseId) {
		return housePicMapper.selectByHouseId(houseId);
	}
	@Override
	public UserFavorHouseForm getFavorHouseByid(String houseid, String userid) {
		return favorHouseFormMapper.selectById(houseid, userid);
	}
	@Override
	public boolean insertFavorHouseInfo(UserFavorHouseForm userFavorHouseForm) {
		int num=favorHouseFormMapper.insert(userFavorHouseForm);
		return (num==1);
	}
	@Override
	public boolean deleteFavorHouseInfoById(String houseid, String userid) {
		int num=favorHouseFormMapper.deleteFavorHouseById(userid, houseid);
		return (num==1);
	}
	@Override
	public boolean insertSubcHouseLiveInfo(UserSubcHouseLiveForm userSubcHouseLiveForm) {
		int num=userSubcHouseLiveFormMapper.insert(userSubcHouseLiveForm);
		return (num==1);
	}
	/***
	 *依据houseid获取已确认的直播时间 (未直播)
	 **/
	@Override
	public Date getVerifyBeginTimeByHouseId(String houseid) {
		List<Date>list=userSubcHouseLiveFormMapper.selectHouseLiveTime(houseid);
		if(list==null||list.size()<1){
			return null;
		}else{
			Date receTime=list.get(0);
			if(list.size()==1)return receTime;
			for(int i=1;i<list.size();i++){
				receTime=(receTime.getTime()>list.get(i).getTime())?list.get(i):receTime;
			}
			return receTime;
		}
	}
	/***
	 *更新直播预约信息 
	 **/
	public boolean updateSubcHouseLive(UserSubcHouseLiveForm record){
		return (userSubcHouseLiveFormMapper.updateVerifyFlagByPublicKeySelective(record)==1)?true:false;
	}
	
	
	
	/***
	 *获取用户自己被确认直播的预约信息 
	 **/
	@Override
	public List<UserSubcHouseLiveForm> getOwnVerifyTimeListById(String houseid, String userid,int intervalTime) {
		return userSubcHouseLiveFormMapper.getOwnVerifyTimeListById(houseid, userid,intervalTime);
	}
	/***
	 *获取用户自己被确认直播的预约信息  
	 *不是当前房屋的其他同一时间段预约
	 **/
	@Override
	public List<Date> getOwnOtherVerifyTimeListById(String houseid, String userid, int intervalTime) {
		// TODO Auto-generated method stub
		return userSubcHouseLiveFormMapper.getOwnOtherVerifyTimeListById(houseid, userid, intervalTime);
	}
	
	/***
	 *获取他人预约直播被确认信息 
	 *当前方法不会取与自己重合的时间
	 *比如 我： 2015/4/14 9:10
	 *  他人：2015/4/14 9:10
	 *  这个时间点不会被取出  这点需要在考虑一下，因为可能需要这个时间点，以便用户取消了以后再加入(加入时需要直接设置verifyFlag为1)
	 **/
	@Override
	public List<UserSubcHouseLiveForm> getOtherVerifyTimeListById(String houseid, String userid,int intervalTime) {
		return userSubcHouseLiveFormMapper.getOtherVerifyTimeListById(houseid, userid,intervalTime);
	}
	@Override
	public List<UserSubcHouseLiveForm> getOwnSubcHouseLiveListById(String houseid, String userid,int intervalTime) {
		List<UserSubcHouseLiveForm>list=userSubcHouseLiveFormMapper.getOwnSubcHouseLiveListById(houseid,userid,intervalTime);
		return list;
	}
	/**
	 *获取房东当天的直播安排 
	 **/
	@Override
	public List<Date> getLandlordVerifyTimeListById(String houseid, int intervalTime) {
		// TODO Auto-generated method stub
		return userSubcHouseLiveFormMapper.getLandlordVerifyTimeListById(houseid, intervalTime);
	}
	/***
	 *依据房源以及时间信息
	 *获取房源直播预约数目(已确认的) 
	 **/
	@Override
	public int selectHouseLiveByBeginTime(String houseid, Date beginTime) {
		return userSubcHouseLiveFormMapper.selectHouseLiveByBeginTime(houseid, beginTime);
	}
	
	/***
	 *更新房源信息 
	 **/
	@Override
	public boolean updateHouseInfo(UserHouseForm record) {
		return (houseMapper.updateByPrimaryKeySelective(record)>0)?true:false;
	}
	
	public boolean  cancelHouseLiveBySelf(UserSubcHouseLiveForm record) {
		return (userSubcHouseLiveFormMapper.cancelHouseLiveBySelf(record)>0)?true:false;
	}
	/**
	 * 修改预约状态字段 verify_flag
	 **/
	@Override
	public boolean refuseSubcHouseLive(UserSubcHouseLiveForm record) {
		return (userSubcHouseLiveFormMapper.updateVerifyFlagByPublicKeySelective(record)>0)?true:false;
	}
	/***
	 * 房源发布端确认直播时间
	 * */
	@Override
	public boolean verifySubcHouseLive(UserSubcHouseLiveForm record) {
		return (userSubcHouseLiveFormMapper.updateVerifyFlagByPublicKeySelective(record)>0)?true:false;
	}
	@Override
	public int getUserDailyInfoCount(String houseid, String userid, int intervalTime) {
		// TODO Auto-generated method stub
		List<UserSubcHouseLiveForm>list=userSubcHouseLiveFormMapper.getUserDailyInfoCount(houseid, userid, intervalTime);
		return (list==null?0:list.size());
	}
	
	
	@Cacheable(value="locating",key="'locating'+#name")
	public CommunityInfo locating(String name) {
		return communityInfoMapper.locating(name);
	}
	
	@Cacheable(value="match",key="'match'+#name")
	public List<String> match(String name) {

		return communityInfoMapper.match(name);
	}
	
	@Cacheable(value ="matchBegin",key="'matchBegin'+#name")
	public List<String> matchBegin(String name) {
		return communityInfoMapper.matchBegin(name);
	}
	
	@Cacheable(value="getLocation",key="'getLocation'+#name")
	public  Map<String, Object> getLocation(String name) {
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet("http://i.anjuke.com/ajax/autocommlist/?query="+URLEncoder.encode(name,"utf-8"));
	      
        	httpGet.addHeader("User-Agent", "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:46.0) Gecko/20100101 Firefox/46.0");
			httpGet.addHeader("Cookie", " ctid=18; ");
			// 执行请求
	        HttpResponse response = httpClient.execute(httpGet);
	        String result = EntityUtils.toString(response.getEntity());
	        result = StringEscapeUtils.unescapeJava(result);
	        ObjectMapper mapper = new ObjectMapper();
	        List<Map<String, Object>> list = mapper.readValue(result, List.class);
	        if(list.size() == 0)
	        	return null;
	        Map<String, Object> map = list.get(0);
	        return map;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return null;
	}
	@Override
	public int modifyHouseLiveFlag(String houseId, boolean liveFlag) {	
		return houseMapper.modifyHouseLiveFlagByHouseId(houseId, liveFlag);
	}
	@Override
	public boolean setHouseLiving(String userId, String houseId) {
		return (houseMapper.updateHouseLiveState(userId, houseId, true)>0?true:false);
	}
	@Override
	public boolean cancelHouseLiving(String userId, String houseId) {
		// TODO Auto-generated method stub
		return (houseMapper.updateHouseLiveState(userId, houseId, false)>0?true:false);
	}	 
}
