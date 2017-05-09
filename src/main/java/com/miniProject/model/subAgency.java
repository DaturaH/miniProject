package com.miniProject.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class subAgency implements Serializable{	 	
	private String houseId;

    private String housename;

    private Boolean houseStatu;

    private String pubUserId;

    private Integer pubUserType;

	private Date subBeginTime;

    private Integer verifyFlag;

    private String liveSubInfoId;
	    
	private List<String> housePic;
	

	private String houseShowPic;

    public String getHouseShowPic() {
		return houseShowPic;
	}

	public void setHouseShowPic(String houseShowPic) {
		this.houseShowPic = houseShowPic;
	}

		
	private List<UserInfo> userlist;
	public String getHouseId() {
		return houseId;
	}

	public void setHouseId(String houseId) {
		this.houseId = houseId;
	}

	public String getHousename() {
		return housename;
	}

	public void setHousename(String housename) {
		this.housename = housename;
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

	public void setPubUserId(String pubUserId) {
		this.pubUserId = pubUserId;
	}

	public Integer getPubUserType() {
		return pubUserType;
	}

	public void setPubUserType(Integer pubUserType) {
		this.pubUserType = pubUserType;
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
		this.liveSubInfoId = liveSubInfoId;
	}

	public List<String> getHousePic() {
		return housePic;
	}

	public void setHousePic(List<String> housePic) {
		this.housePic = housePic;
	}

	public List<UserInfo> getUserlist() {
		return userlist;
	}

	public void setUserlist(List<UserInfo> userlist) {
		this.userlist = userlist;
	}	
	
    public Date getSubBeginTime() {
		return subBeginTime;
	}

	public void setSubBeginTime(Date subBeginTime) {
		this.subBeginTime = subBeginTime;
	}

}
