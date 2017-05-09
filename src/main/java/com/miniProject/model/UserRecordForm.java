package com.miniProject.model;

import java.util.Date;
import java.util.List;

public class UserRecordForm {
    private String recordHouseId;

    private String recordHouseName;

    private String recordId;

    private String userId;

    private Date recordTime;

    private Integer recordType;
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

	public String getRecordHouseId() {
        return recordHouseId;
    }

    public void setRecordHouseId(String recordHouseId) {
        this.recordHouseId = recordHouseId == null ? null : recordHouseId.trim();
    }

    public String getRecordHouseName() {
        return recordHouseName;
    }

    public void setRecordHouseName(String recordHouseName) {
        this.recordHouseName = recordHouseName == null ? null : recordHouseName.trim();
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId == null ? null : recordId.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public Date getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(Date recordTime) {
        this.recordTime = recordTime;
    }

    public Integer getRecordType() {
        return recordType;
    }

    public void setRecordType(Integer recordType) {
        this.recordType = recordType;
    }
}