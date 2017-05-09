package com.miniProject.model;

import java.io.Serializable;

public class CommunityInfo implements Serializable{
    @Override
	public String toString() {
		return "CommunityInfo [name=" + name + ", city=" + city + ", district=" + district + ", lat=" + lat + ", lng="
				+ lng + ", address=" + address + "]";
	}

	private String name;

    private String city;

    private String district;

    private Double lat;

    private Double lng;

    private String address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district == null ? null : district.trim();
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }
}