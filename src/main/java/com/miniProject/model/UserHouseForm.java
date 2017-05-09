package com.miniProject.model;

import org.apache.ibatis.jdbc.Null;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.Point;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;

@JsonIgnoreProperties({"userId","point"})
public class UserHouseForm {
	
	@GeoSpatialIndexed
	private double[] point;
	
	


	public double[] getPoint() {
		return point;
	}

	public void setPoint(double[] point) {
		this.point = point;
	}

	/**
	 *房源id 
	 **/
	//注解，作为mongodb的_id
	@Id
    private String houseId;
	
	/***
     *用户id 
     **/
    private String userId;
    /***
     * 房屋状态
     * true为已出租；false未出租
     **/
    private Boolean houseState;
    /***
     * 房源直播状态
     * 	true为正在直播；false未直播
     **/
    private Boolean liveState;
    /***
     * 房源录像
     * 无录像时为空；有即为url
     **/
    private String videoState;
    /***
     *房屋纬度信息 
     **/
    private Double latitude;
    /***
     *房屋经度信息 
     **/
    private Double longitude;
    /***
     *房源位置信息 
     **/
    private String location;
    /***
     *小区名称 
     **/
    private String villageName;
    /***
     *房屋类型
     *室|厅|卫 
     **/
    private String houseType;
    /***
     *面积（平米）
     **/
    private Integer area;
    /***
     *合租方式
     *-1表示整租；0为主卧；1为次卧；2为隔断；3为床位
     **/
    private Integer sharingWay;
    /***
     *房屋朝向
     *0为朝南；1为朝北；2为南北朝向
     **/
    private Integer houseOrientation;
    /***
     *押金付款
     *0押一付一；1押一付三
     **/
    private Integer payWay;
    /***
     *装修
     *0表示精装；1表示中装；2表示简装
     **/
    private Integer decoration;
    /***
     *房租(元/月)
     **/
    private Integer price;
    /***
     *配套设施
     *从低位起，每位表示一件设施：床（1）、衣橱（2）、桌子（4）、橱柜（8）、淋浴（16）、空调（32）、冰箱（64）、电视（128）、洗衣机（256）、网络（512）
     *判断时只需要进行为运算
     *如判断淋浴   ===>   (fitment&16)==1?true?false;
     **/
    private Integer fitment;
    /***
     *房源联系人
     *格式   ===> 姓名,联系方式
     *如：赵女士,13957150878
     **/
    private String contact;
    /***
     * 	房屋状况描述
     **/
    private String description;
    
    /***
     * 	房源标题
     **/
    private String title;
    
    /***
     * 	房源区块名，如长河
     **/
    private String blockname;
    
    //上下线
    private Boolean enabled;

    private Object position;

    public String getHouseId() {
        return houseId;
    }

    public void setHouseId(String houseId) {
        this.houseId = houseId == null ? null : houseId.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public Boolean getHouseState() {
        return houseState;
    }

    public void setHouseState(Boolean houseState) {
        this.houseState = houseState == null? false : houseState;
    }

    public Boolean getLiveState() {
        return liveState;
    }

    public void setLiveState(Boolean liveState) {
        this.liveState = liveState;
    }

    public String getVideoState() {
        return videoState;
    }

    public void setVideoState(String videoState) {
        this.videoState = videoState == null ? null : videoState.trim();
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location == null ? null : location.trim();
    }

    public String getVillageName() {
        return villageName;
    }

    public void setVillageName(String villageName) {
        this.villageName = villageName == null ? null : villageName.trim();
    }

    public String getHouseType() {
        return houseType;
    }

    public void setHouseType(String houseType) {
        this.houseType = houseType == null ? null : houseType.trim();
    }

    public Integer getArea() {
        return area;
    }

    public void setArea(Integer area) {
        this.area = area;
    }

    public Integer getSharingWay() {
        return sharingWay;
    }

    public void setSharingWay(Integer sharingWay) {
        this.sharingWay = sharingWay;
    }

    public Integer getHouseOrientation() {
        return houseOrientation;
    }

    public void setHouseOrientation(Integer houseOrientation) {
        this.houseOrientation = houseOrientation;
    }

    public Integer getPayWay() {
        return payWay;
    }

    public void setPayWay(Integer payWay) {
        this.payWay = payWay;
    }

    public Integer getDecoration() {
        return decoration;
    }

    public void setDecoration(Integer decoration) {
        this.decoration = decoration;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getFitment() {
        return fitment;
    }

    public void setFitment(Integer fitment) {
        this.fitment = fitment;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact == null ? null : contact.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }
    
    public String getBlockname() {
        return blockname;
    }

    public void setBlockname(String blockname) {
        this.blockname = blockname == null ? null : blockname.trim();
    }
    
    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
    	this.enabled = enabled;
    }

    public Object getPosition() {
        return position;
    }

    public void setPosition(Object position) {
        this.position = position;
    }
}