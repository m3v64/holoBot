package dev.m3v.data.model;

public class Channel {
    private String channelId;
    private int checkQue;
    private int checkCooldown;
    private String roleId;

    public Channel() {}
    public Channel(String channelId, int checkQueue, int checkCooldown, String roleId) {
        this.channelId = channelId;
        this.checkQue = checkQueue;
        this.checkCooldown = checkCooldown;
        this.roleId = roleId;
    }

    public String getChannelId() { return channelId; }
    public void setChannelId(String channelId) { this.channelId = channelId; }

    public int getCheckQueue() { return checkQue; }
    public void setCheckQueue(int checkQueue) { this.checkQue = checkQueue; }

    public int getCheckCooldown() { return checkCooldown; }
    public void setCheckCooldown(int checkCooldown) { this.checkCooldown = checkCooldown; }

    public String getRoleId() { return roleId; }
    public void setRoleId(String roleId) { this.roleId = roleId; }
}
