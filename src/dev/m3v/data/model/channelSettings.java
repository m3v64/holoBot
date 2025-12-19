package dev.m3v.data.model;

public class channelSettings {
    private int checkIntervalSeconds;
    private int channelCooldownMinutes;
    private int saveLimit;

    public channelSettings(int checkIntervalSeconds, int channelCooldownMinutes, int saveLimit) {
        this.checkIntervalSeconds = checkIntervalSeconds;
        this.channelCooldownMinutes = channelCooldownMinutes;
        this.saveLimit = saveLimit;
    }
    
    public int getCheckIntervalSeconds() {
        return checkIntervalSeconds;
    }
    public void setCheckIntervalSeconds(int checkIntervalSeconds) {
        this.checkIntervalSeconds = checkIntervalSeconds;
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
