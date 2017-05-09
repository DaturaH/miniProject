package com.miniProject.model;

public class LiveroomForm {
    private String channelName;

    private String creator;

    private String roomName;

    private String channelId;

    private String pushUrl;

    private String pullUrl;

    private String roomId;

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName == null ? null : channelName.trim();
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName == null ? null : roomName.trim();
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId == null ? null : channelId.trim();
    }

    public String getPushUrl() {
        return pushUrl;
    }

    public void setPushUrl(String pushUrl) {
        this.pushUrl = pushUrl == null ? null : pushUrl.trim();
    }

    public String getPullUrl() {
        return pullUrl;
    }

    public void setPullUrl(String pullUrl) {
        this.pullUrl = pullUrl == null ? null : pullUrl.trim();
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
    	this.roomId = roomId == null ? null : roomId.trim();
    }
}