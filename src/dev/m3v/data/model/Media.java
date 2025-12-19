package dev.m3v.data.model;

public class Media {
    private String channelId;
    private String mediaId;
    private String roleId;
    private String messageId;
    private String type;
    private MediaData data;

    public Media() { this.data = new MediaData(); }
    public Media(String channelId, String mediaId, String roleId, String messageId, String type, MediaData data) {
        this.channelId = channelId;
        this.mediaId = mediaId;
        this.roleId = roleId;
        this.messageId = messageId;
        this.type = type;
        this.data = data;
    }

    public String getChannelId() { return channelId; }
    public String getMediaId() { return mediaId; }
    public String getRoleId() { return roleId; }
    public String getMessageId() { return messageId; }
    public String getType() { return type; }
    public MediaData getData() { return data; }

    public void setChannelId(String channelId) { this.channelId = channelId; }
    public void setMediaId(String mediaId) { this.mediaId = mediaId; }
    public void setRoleId(String roleId) { this.roleId = roleId; }
    public void setMessageId(String messageId) { this.messageId = messageId; }
    public void setType(String type) { this.type = type; }
    public void setData(MediaData data) { this.data = data; }  
}
