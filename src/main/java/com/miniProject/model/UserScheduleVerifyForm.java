package com.miniProject.model;

import java.util.Date;
import java.util.List;

public class UserScheduleVerifyForm {
	/**
	 *房源发布者id 
	 **/
    private String pubUserId;
    /***
     *房源发布者名称 
     **/
    private String pubUserName;
    /***
     *房源发布者类型
     *0表示用户
     *1为中介；2为个人房东
     **/
    private Integer pubUserType;
    /**
     *房源id 
     * 
     **/
    private String houseId;
    /***
     *房源别名 
     **/
    private String housename;
    /***
     *房源状态
     *true表示正在直播
     **/
    private Boolean houseStatu;
    /***
     *预约房源用户id
     **/
    private String subUserId;
    /***
     *房源直播时间
     **/
    private Date verifyBeginTime;

    private int verifyFlag;
    
    
    private String liveSubInfoId;
    
    private String portraitFileName;
    
    private List<String>housePic;
    private String houseShowPic;
    
    
	public String getHouseShowPic() {
		return houseShowPic;
	}

	public void setHouseShowPic(String houseShowPic) {
		this.houseShowPic = houseShowPic;
	}

	public List<String> getHousePic() {
		return housePic;
	}

	public void setHousePic(List<String> housePic) {
		this.housePic = housePic;
	}

	public String getPortraitFileName() {
		return portraitFileName;
	}

	public void setPortraitFileName(String portraitFileName) {
		this.portraitFileName = portraitFileName;
	}

	public String getLiveSubInfoId() {
		return liveSubInfoId;
	}

	public void setLiveSubInfoId(String liveSubInfoId) {
		this.liveSubInfoId = liveSubInfoId;
	}

	public int getVerifyFlag() {
		return verifyFlag;
	}

	public void setVerifyFlag(int verifyFlag) {
		this.verifyFlag = verifyFlag;
	}

	public String getPubUserId() {
        return pubUserId;
    }

    public void setPubUserId(String pubUserId) {
        this.pubUserId = pubUserId == null ? null : pubUserId.trim();
    }

    public String getPubUserName() {
        return pubUserName;
    }

    public void setPubUserName(String pubUserName) {
        this.pubUserName = pubUserName == null ? null : pubUserName.trim();
    }

    public Integer getPubUserType() {
        return pubUserType;
    }

    public void setPubUserType(Integer pubUserType) {
        this.pubUserType = pubUserType;
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

    public String getSubUserId() {
        return subUserId;
    }

    public void setSubUserId(String subUserId) {
        this.subUserId = subUserId == null ? null : subUserId.trim();
    }

    public Date getVerifyBeginTime() {
        return verifyBeginTime;
    }

    public void setVerifyBeginTime(Date verifyBeginTime) {
        this.verifyBeginTime = verifyBeginTime;
    }
}