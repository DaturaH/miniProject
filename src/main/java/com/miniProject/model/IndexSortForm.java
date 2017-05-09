package com.miniProject.model;

public class IndexSortForm {
	private Double longitudeN;
	
	private Double longitudeS;
	
	private Double latitudeW;

    private Double latitudeE;
    
    private String location;
    
    private String houseType;

	private Integer pricelow;
    
    private Integer pricehigh;
    

	public Double getLongitudeN() {
		return longitudeN;
	}

	public void setLongitudeN(Double longitudeN) {
		this.longitudeN = longitudeN;
	}

	public Double getLongitudeS() {
		return longitudeS;
	}

	public void setLongitudeS(Double longitudeS) {
		this.longitudeS = longitudeS;
	}

	public Double getLatitudeW() {
		return latitudeW;
	}

	public void setLatitudeW(Double latitudeW) {
		this.latitudeW = latitudeW;
	}

	public Double getLatitudeE() {
		return latitudeE;
	}

	public void setLatitudeE(Double latitudeE) {
		this.latitudeE = latitudeE;
	}
    
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getHouseType() {
		return houseType;
	}

	public void setHouseType(String houseType) {
		this.houseType = houseType;
	}

    public Integer getPricelow() {
		return pricelow;
	}

	public void setPricelow(Integer pricelow) {
		this.pricelow = pricelow;
	}

	public Integer getPricehigh() {
		return pricehigh;
	}

	public void setPricehigh(Integer pricehigh) {
		this.pricehigh = pricehigh;
	}

}
