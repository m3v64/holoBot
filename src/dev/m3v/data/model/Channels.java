package dev.m3v.data.model;

import dev.m3v.data.*;

public class Channels {
    private String channelId;
    private int checkQue;
    private int checkCooldown;
    private String roleId;

    public Channels(String channelId) {
        if (!JsonStorage.isLoaded()) {
            throw new IllegalStateException("FromJson is not loaded. Call FromJson.load() before creating Channel instances.");
        }

        Channels match = JsonStorage.get().getChannels()
                .stream()
                .filter(channel -> channel.getChannelId() != null && channel.getChannelId().equals(channelId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Channel ID not found: " + channelId));

        this.channelId = match.getChannelId();
        this.checkQue = match.getCheckQueue();
        this.checkCooldown = match.getCheckCooldown();
        this.roleId = match.getRoleId();
    }

    public Channels(String channelId, int checkQueue, int checkCooldown, String roleId) {
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
