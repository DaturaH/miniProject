package com.miniProject.model;

import java.util.Date;
import java.util.List;
/***
 *该类用于后端与前端之间的交互信息管理
 *用于显示房源详情 
 **/
public class HouseDetailForm {
	public UserHouseForm houseForm;
	public List<String>picList;
	public boolean isFavorHouse;
	public Date beginTime;
	public String userPic;
	public int type;
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getUserPic() {
		return userPic;
	}
	public void setUserPic(String userPic) {
		this.userPic = userPic;
	}
	public UserHouseForm getHouseForm() {
		return houseForm;
	}
	public void setHouseForm(UserHouseForm houseForm) {
		this.houseForm = houseForm;
	}
	public List<String> getPicList() {
		return picList;
	}
	public void setPicList(List<String> picList) {
		this.picList = picList;
	}
	public boolean isFavorHouse() {
		return isFavorHouse;
	}
	public void setFavorHouse(boolean isFavorHouse) {
		this.isFavorHouse = isFavorHouse;
	}
	public Date getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}
}
