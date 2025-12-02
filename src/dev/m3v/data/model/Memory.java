package dev.m3v.data.model;

public class Memory {
    private String channelId;
    private String mediaId;
    private String roleId;
    private String messageId;
    private boolean isPremier;
    private boolean isVod;
    private MediaData data;

    public Memory() {}
    public Memory(String channelId, String mediaId, String roleId) {
        this.channelId = channelId;
        this.mediaId = mediaId;
        this.roleId = roleId;
        this.isVod = false;
    }

    public Memory(String channelId, String mediaId, String roleId, boolean isVod) {
        this.channelId = channelId;
        this.mediaId = mediaId;
        this.roleId = roleId;
        this.isVod = isVod;
    }

    public String getChannelId() { return channelId; }
    public void setChannelId(String channelId) { this.channelId = channelId; }

    public String getMediaId() { return mediaId; }
    public void setMediaId(String mediaId) { this.mediaId = mediaId; }

    public String getRoleId() { return roleId; }
    public void setRoleId(String roleId) { this.roleId = roleId; }

    public String getMessageId() { return messageId; }
    public void setMessageId(String messageId) { this.messageId = messageId; }

    public boolean getIsPremier() { return isPremier; }
    public void setIsPremier(boolean isPremier) { this.isPremier = isPremier; }

    public boolean getIsVod() { return isVod; }
    public void setIsVod(boolean isVod) { this.isVod = isVod; }

    public MediaData getData() { return data; }
    public void setData(MediaData data) { this.data = data; }

    public String getVideoId() { return mediaId; }
    public void setVideoId(String videoId) { this.mediaId = videoId; }
}
