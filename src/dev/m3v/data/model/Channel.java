package dev.m3v.data.model;

public class Channel {
    private String channelId;
    private int checkQue;
    private int checkCooldown;
    private String roleId;
    private ChannelSettings settings;
    
    public Channel(String channelId, int checkQue, int checkCooldown, String roleId, ChannelSettings settings) {
        this.channelId = channelId;
        this.checkQue = checkQue;
        this.checkCooldown = checkCooldown;
        this.roleId = roleId;
        this.settings = settings;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public int getCheckQue() {
        return checkQue;
    }

    public void setCheckQue(int checkQue) {
        this.checkQue = checkQue;
    }

    public int getCheckCooldown() {
        return checkCooldown;
    }

    public void setCheckCooldown(int checkCooldown) {
        this.checkCooldown = checkCooldown;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public ChannelSettings getSettings() {
        return settings;
    }

    public void setSettings(ChannelSettings settings) {
        this.settings = settings;
    }
}
