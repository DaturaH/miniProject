package com.miniProject.model;

import java.util.Date;

public class PushHistoryForm {
    private String infoId;

    private String userId;

    private String pushMsg;

    private Date insertTime;

    public String getInfoId() {
        return infoId;
    }

    public void setInfoId(String infoId) {
        this.infoId = infoId == null ? null : infoId.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getPushMsg() {
        return pushMsg;
    }

    public void setPushMsg(String pushMsg) {
        this.pushMsg = pushMsg == null ? null : pushMsg.trim();
    }

    public Date getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(Date insertTime) {
        this.insertTime = insertTime;
    }
}