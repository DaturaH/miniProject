package com.miniProject.model;

import java.util.Date;

public class UserSubcHouseLiveForm {
    private String liveSubInfoId;

    private String houseId;

    private String pubUserId;

    private String subUserId;

    private Date subBeginTime;

    private Date subEndTime;
    //0表示未确认直播，1表示直播预约被确认，2表示直播预约被取消(被拒绝)，3表示订阅人员自己取消
    private int verifyFlag;
    /***
     *插入前进行参数检查，
     *判断属性值是否正确 
     **/
    public boolean isReadyForInsert(){
    	if(liveSubInfoId==null||liveSubInfoId.trim().length()<1){
    		return false;
    	}
    	if(houseId==null||houseId.trim().length()<1){
    		return false;
    	}
    	if(pubUserId==null||pubUserId.trim().length()<1){
    		return false;
    	}
    	if(subUserId==null||subUserId.trim().length()<1){
    		return false;
    	}
    	if(subBeginTime.getTime()<System.currentTimeMillis()||verifyFlag<0||verifyFlag>4){
    		return false;
    	}
    	return true;
    }
    public String getLiveSubInfoId() {
        return liveSubInfoId;
    }

    public void setLiveSubInfoId(String liveSubInfoId) {
        this.liveSubInfoId = liveSubInfoId == null ? null : liveSubInfoId.trim();
    }

    public String getHouseId() {
        return houseId;
    }

    public void setHouseId(String houseId) {
        this.houseId = houseId == null ? null : houseId.trim();
    }

    public String getPubUserId() {
        return pubUserId;
    }

    public void setPubUserId(String pubUserId) {
        this.pubUserId = pubUserId == null ? null : pubUserId.trim();
    }

    public String getSubUserId() {
        return subUserId;
    }

    public void setSubUserId(String subUserId) {
        this.subUserId = subUserId == null ? null : subUserId.trim();
    }

    public Date getSubBeginTime() {
        return subBeginTime;
    }

    public void setSubBeginTime(Date subBeginTime) {
        this.subBeginTime = subBeginTime;
    }

    public Date getSubEndTime() {
        return subEndTime;
    }

    public void setSubEndTime(Date subEndTime) {
        this.subEndTime = subEndTime;
    }

    public int getVerifyFlag() {
        return verifyFlag;
    }

    public void setVerifyFlag(int verifyFlag) {
        this.verifyFlag = verifyFlag;
    }
}