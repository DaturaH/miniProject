package com.miniProject.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.miniProject.dao.AgencyScheduleVerifyMapper;
import com.miniProject.dao.UserSubcHouseLiveFormMapper;
import com.miniProject.model.AgencyScheduleVerifyForm;
import com.miniProject.model.UserSubcHouseLiveForm;
import com.miniProject.model.subAgency;
import com.miniProject.util.RetMapUtils;


@Service("agencyScheduleService")
public class AgencyScheduleServiceImpl implements AgencyScheduleServiceI{
	@Autowired
	private AgencyScheduleVerifyMapper agencySchVerFormMapper;
	@Autowired
	private PushServiceI pushService;

	
	//查找主播自己的预约，是否有冲突
	//按主键方式获得各个房源发布方房源的用户列表
	public List<AgencyScheduleVerifyForm> getAgencyScheduleVerifyByUserId(String userId){
		return agencySchVerFormMapper.getAgencyScheduleByUserId(userId,1);
	}
	
	//按主键方式获得各个房源发布方房源的用户列表
	public List<AgencyScheduleVerifyForm> getAgencyWaitScheduleVerifyByUserId(String userId){
		return agencySchVerFormMapper.getAgencyScheduleByUserId(userId,0);
	}
	
	@Override
	public int getAgencyScheduleVerifyCountByUserId(String userId){
		return agencySchVerFormMapper.getAgencyScheduleCountByUserId(userId);
	}



	@Override
	//直播日程房源列表
	public List<subAgency> getAgencyScheduleUser(String id ) {
		return (agencySchVerFormMapper.getDiffHouseId(id));
	}

	//等待确认房源列表
	@Override
	public List<subAgency> getAgencyWaitScheduleUser(String id) {
		return (agencySchVerFormMapper.getWaitDiffHouseId(id));
	}

    //通过直播预约人id , datetime 获取订阅人ID
	/**
	 *通过直播预约人id , datetime 获取订阅人ID
	 *@param pub_user_id 发布者id
	 *@param subBeginTime 预约时间
	 *@param verify_flag 订阅标志位 0表示预约未确认，1表示预约已确认，2表示预约被拒绝，3,表示预约被取消
	 **/
	@Override
	public List<UserSubcHouseLiveForm> getSubInfoByliveUserAndTime(UserSubcHouseLiveForm record) {
		return agencySchVerFormMapper.getSubInfoByliveUserAndTime(record);
	}


	//房源发布方取消或拒绝直播
	@Override
	public boolean cancelorRejectHouseLive(UserSubcHouseLiveForm record) {
		return (agencySchVerFormMapper.cancelorRejectHouseLiveBySelf(record)>0?true:false);
	}
	
	
	//房源方确认预约请求
	@Override
	public boolean verifyAgencySubcHouseLive(UserSubcHouseLiveForm record) {
		return agencySchVerFormMapper.verifyHouseLive(record)>0?true:false;
	}
	
	
    //通过HouseId , subBeginTime , verifyFlag ＝ 0 获取当天所有的直播列表
	@Override
	public List<UserSubcHouseLiveForm> getOwnSubcHouseLiveList(
			String houseid, int intervalTime) {
		return agencySchVerFormMapper.getOwnSubcHouseLiveList(houseid , intervalTime);
	}

    //通过HouseId , subBeginTime , verifyFlag ＝ 0 获取当天所有的直播列表数量
	@Override
	public int getOwnSubcHouseLiveCount(String houseid, int intervalTime) {
		return agencySchVerFormMapper.getOwnSubcHouseLiveCount(houseid , intervalTime);
	}

	//通过HouseId获取所有相关订阅信息，并进行取消(0->2 , 1->2)
	@Override
	public List<UserSubcHouseLiveForm> cancelSubInfoByHouseId(String houseId) {
		List<UserSubcHouseLiveForm> list = agencySchVerFormMapper.getSubInfoByHouseId(houseId);
		agencySchVerFormMapper.cancelSubInfoByHouseId(houseId);
		return list;
	}
	
    //通过HouseId 获取一定时间区间内的订阅用户
	@Override
	public void getTimeIntervalSubInfoByHouseId(String houseId) {
		
		List<UserSubcHouseLiveForm> userSubList = agencySchVerFormMapper.getTimeIntervalSubInfoByHouseId(houseId);
		
		//创建直播间时给预约用户推送通知
		List<Map<String, Object>> subList = new ArrayList<>();
		if(userSubList != null && userSubList.size() > 0){
			for(int i = 0 ; i < userSubList.size(); i++){
				Map<String , Object> subResult = new HashMap<>();
				subResult.put("liveSubInfoId" ,userSubList.get(i).getLiveSubInfoId() );
				subResult.put("houseId" , userSubList.get(i).getHouseId());
				subResult.put("pubUserId" , userSubList.get(i).getPubUserId());
				subResult.put("subUserId" , userSubList.get(i).getSubUserId());
				subResult.put("subBeginTime" , userSubList.get(i).getSubBeginTime());
				//subResult.put("subEndTime" , list.get(i).getSubEndTime());
				subResult.put("verifyFlag" , 4);
				subList.add(subResult);
			}
		}else{
			return;
		}	

		pushService.pubUser2SubUser(subList);
	}
	
	
	
	@Override
	public int addNewHouseLiveByAgency(UserSubcHouseLiveForm record) {
		return agencySchVerFormMapper.addNewHouseLiveByAgency(record);
	}

	@Override
	public List<UserSubcHouseLiveForm> getSubInfoByliveUserAndTimeAndHouse(UserSubcHouseLiveForm record) {
		return agencySchVerFormMapper.getSubInfoByliveUserAndTimeAndHouse(record);
	}

	@Override
	public List<UserSubcHouseLiveForm> getAgencySelfScheduleByUserId(String pubUserId, int intervalTime) {
		return agencySchVerFormMapper.getAgencySelfScheduleByUserId(pubUserId, intervalTime);
	}

	@Override
	public List<UserSubcHouseLiveForm> getlSubInfoByTimeInterval() {
		return agencySchVerFormMapper.getlSubInfoByTimeInterval();
	}

}
