package com.miniProject.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class AgencyScheduleVerifyForm implements Serializable{
	private String subUserId;

    private String subUserName;

    private String subPhoneNumber;

    private String portraitFileName;

    private String houseId;

    private String housename;

    private Boolean houseStatu;

    private String pubUserId;

    private Integer pubUserType;
    
    private Date subBeginTime;

    private Integer verifyFlag;

    private String liveSubInfoId;
    


    public String getSubUserId() {
        return subUserId;
    }

    public void setSubUserId(String subUserId) {
        this.subUserId = subUserId == null ? null : subUserId.trim();
    }

    public String getSubUserName() {
        return subUserName;
    }

    public void setSubUserName(String subUserName) {
        this.subUserName = subUserName == null ? null : subUserName.trim();
    }


    public String getPortraitFileName() {
        return portraitFileName;
    }

    public void setPortraitFileName(String portraitFileName) {
        this.portraitFileName = portraitFileName == null ? null : portraitFileName.trim();
    }

    public String getHouseId() {
        return houseId;
    }

    public void setHouseId(String houseId) {
        this.houseId = houseId == null ? null : houseId.trim();
    }

    public String getHousename() {
        return housename;
    }

    public void setHousename(String housename) {
        this.housename = housename == null ? null : housename.trim();
    }

    public Boolean getHouseStatu() {
        return houseStatu;
    }

    public void setHouseStatu(Boolean houseStatu) {
        this.houseStatu = houseStatu;
    }

    public String getPubUserId() {
        return pubUserId;
    }

    public void setPubUserId(String subUserId) {
        this.pubUserId = pubUserId == null ? null : pubUserId.trim();
    }

    public Date getSubBeginTime() {
        return subBeginTime;
    }

    public void setSubBeginTime(Date subBeginTime) {
        this.subBeginTime = subBeginTime;
    }

    public Integer getVerifyFlag() {
        return verifyFlag;
    }

    public void setVerifyFlag(Integer verifyFlag) {
        this.verifyFlag = verifyFlag;
    }

    public String getLiveSubInfoId() {
        return liveSubInfoId;
    }

    public void setLiveSubInfoId(String liveSubInfoId) {
        this.liveSubInfoId = liveSubInfoId == null ? null : liveSubInfoId.trim();
    }
    
    public String getSubPhoneNumber() {
		return subPhoneNumber;
	}

	public void setSubPhoneNumber(String subPhoneNumber) {
		this.subPhoneNumber = subPhoneNumber;
	}

	public Integer getPubUserType() {
		return pubUserType;
	}

	public void setPubUserType(Integer pubUserType) {
		this.pubUserType = pubUserType;
	}
}


