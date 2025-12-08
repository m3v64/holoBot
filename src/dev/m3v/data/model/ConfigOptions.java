package dev.m3v.data.model;

import dev.m3v.data.*;

public class ConfigOptions {
    private String logLevel;
    private String timezone;
    private int checkIntervalSeconds;
    private int channelCooldownMinutes;
    private int lastCheckSaveLimit;
    private int lastCheckSaveLimitPerChannel;
    private String mediaChannelId;
    private String premierChannelId;
    private String streamChannelId;
    private String modChannelId;
    private String adminPingId;

    public ConfigOptions() {}
    public ConfigOptions(String logLevel, String timezone, int checkIntervalSeconds, int channelCooldownMinutes, int lastCheckSaveLimit, int lastCheckSaveLimitPerChannel, String mediaChannelId, String premierChannelId, String streamChannelId, String modChannelId, String adminPingId) {
        this.logLevel = logLevel;
        this.timezone = timezone;
        this.checkIntervalSeconds = checkIntervalSeconds;
        this.channelCooldownMinutes = channelCooldownMinutes;
        this.lastCheckSaveLimit = lastCheckSaveLimitPerChannel * JsonStorage.get().getChannels().size();
        this.lastCheckSaveLimitPerChannel = lastCheckSaveLimitPerChannel;
        this.mediaChannelId = mediaChannelId;
        this.premierChannelId = premierChannelId;
        this.streamChannelId = streamChannelId;
        this.modChannelId = modChannelId;
        this.adminPingId = adminPingId;
    }

    public void setLastCheckSaveLimit(int lastCheckSaveLimit) { this.lastCheckSaveLimit = lastCheckSaveLimit; }
    public void setPremierChannelId(String premierChannelId) { this.premierChannelId = premierChannelId; }

    public String getLogLevel() { return logLevel; }
    public void setLogLevel(String logLevel) { this.logLevel = logLevel; }

    public String getTimezone() { return timezone; }
    public void setTimezone(String timezone) { this.timezone = timezone; }

    public int getCheckIntervalSeconds() { return checkIntervalSeconds; }
    public void setCheckIntervalSeconds(int checkIntervalSeconds) { this.checkIntervalSeconds = checkIntervalSeconds; }

    public int getChannelCooldownMinutes() { return channelCooldownMinutes; }
    public void setChannelCooldownMinutes(int channelCooldownMinutes) { this.channelCooldownMinutes = channelCooldownMinutes; }

    public int getLastCheckSaveLimit() { return lastCheckSaveLimit; }

    public int getLastCheckSaveLimitPerChannel() { return lastCheckSaveLimitPerChannel; }
    public void setLastCheckSaveLimitPerChannel(int lastCheckSaveLimitPerChannel) { this.lastCheckSaveLimitPerChannel = lastCheckSaveLimitPerChannel; }

    public String getMediaChannelId() { return mediaChannelId; }
    public void setMediaChannelId(String mediaChannelId) { this.mediaChannelId = mediaChannelId; }

    public String getPremierChannelId() { return premierChannelId; }
    public void setPremiereChannelId(String premiereChannelId) { this.premierChannelId = premiereChannelId; }

    public String getStreamChannelId() { return streamChannelId; }
    public void setStreamChannelId(String liveStreamChannelId) { this.streamChannelId = liveStreamChannelId; }

    public String getModChannelId() { return modChannelId; }
    public void setModChannelId(String modChannelId) { this.modChannelId = modChannelId; }

    public String getAdminPingId() { return adminPingId; }
    public void setAdminPingId(String adminPingId) { this.adminPingId = adminPingId; }
}
