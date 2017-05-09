package com.miniProject.model;

import java.util.Date;

public class HouseLiveVerifyForm {
    private String liveVerifyInfoId;

    private String houseId;

    private String pubUserId;

    private String subUserId;

    private Date verifyBeginTime;

    private Date verifyEndTime;

    public String getLiveVerifyInfoId() {
        return liveVerifyInfoId;
    }

    public void setLiveVerifyInfoId(String liveVerifyInfoId) {
        this.liveVerifyInfoId = liveVerifyInfoId == null ? null : liveVerifyInfoId.trim();
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

    public Date getVerifyBeginTime() {
        return verifyBeginTime;
    }

    public void setVerifyBeginTime(Date verifyBeginTime) {
        this.verifyBeginTime = verifyBeginTime;
    }

    public Date getVerifyEndTime() {
        return verifyEndTime;
    }

    public void setVerifyEndTime(Date verifyEndTime) {
        this.verifyEndTime = verifyEndTime;
    }
}