package com.miniProject.model;

public class HouseWantedInfo {
    private String userId;

    private Double longitude;

    private Double latitude;

    private String location;

    private String villageName;

    private Integer distance;

    private String houseType;

    private Integer area;

    private Integer sharingWay;

    private Integer priceTop;

    private Integer priceBottom;

    private Integer houseOrientation;

    private Integer decoration;

    private Integer payWay;

    private Integer fitment;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
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

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
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

    public Integer getPriceTop() {
        return priceTop;
    }

    public void setPriceTop(Integer priceTop) {
        this.priceTop = priceTop;
    }

    public Integer getPriceBottom() {
        return priceBottom;
    }

    public void setPriceBottom(Integer priceBottom) {
        this.priceBottom = priceBottom;
    }

    public Integer getHouseOrientation() {
        return houseOrientation;
    }

    public void setHouseOrientation(Integer houseOrientation) {
        this.houseOrientation = houseOrientation;
    }

    public Integer getDecoration() {
        return decoration;
    }

    public void setDecoration(Integer decoration) {
        this.decoration = decoration;
    }

    public Integer getPayWay() {
        return payWay;
    }

    public void setPayWay(Integer payWay) {
        this.payWay = payWay;
    }

    public Integer getFitment() {
        return fitment;
    }

    public void setFitment(Integer fitment) {
        this.fitment = fitment;
    }
}