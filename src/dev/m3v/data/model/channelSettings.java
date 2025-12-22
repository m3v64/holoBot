package dev.m3v.data.model;

public class ChannelSettings {
    private int channelCooldownMinutes;
    private int saveLimit;

    public ChannelSettings(int channelCooldownMinutes, int saveLimit) {
        this.channelCooldownMinutes = channelCooldownMinutes;
        this.saveLimit = saveLimit;
    }
    
    public int getChannelCooldownMinutes() {
        return channelCooldownMinutes;
    }
    public void setChannelCooldownMinutes(int channelCooldownMinutes) {
        this.channelCooldownMinutes = channelCooldownMinutes;
    }
    public int getSaveLimit() {
        return saveLimit;
    }
    public void setSaveLimit(int saveLimit) {
        this.saveLimit = saveLimit;
    }
}
