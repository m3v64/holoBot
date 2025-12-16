package dev.m3v.data.model;

import dev.m3v.data.*;

public class ConfigOptions {
    private String logLevel;
    private String timezone;
    private int checkIntervalSeconds;
    private int channelCooldownMinutes;
    private int saveLimit;
    private int saveLimitPerChannel;
    private String mediaChannelId;
    private String premierChannelId;
    private String streamChannelId;
    private String modChannelId;
    private String adminPingId;

    public ConfigOptions() {}
    public ConfigOptions(String logLevel, String timezone, int checkIntervalSeconds, int channelCooldownMinutes, int saveLimit, int saveLimitPerChannel, String mediaChannelId, String premierChannelId, String streamChannelId, String modChannelId, String adminPingId) {
        this.logLevel = logLevel;
        this.timezone = timezone;
        this.checkIntervalSeconds = checkIntervalSeconds;
        this.channelCooldownMinutes = channelCooldownMinutes;
        this.saveLimit = saveLimitPerChannel * JsonStorage.get().getChannels().size();
        this.saveLimitPerChannel = saveLimitPerChannel;
        this.mediaChannelId = mediaChannelId;
        this.premierChannelId = premierChannelId;
        this.streamChannelId = streamChannelId;
        this.modChannelId = modChannelId;
        this.adminPingId = adminPingId;
    }

    public void setsaveLimit(int saveLimit) { this.saveLimit = saveLimit; }
    public void setPremierChannelId(String premierChannelId) { this.premierChannelId = premierChannelId; }

    public String getLogLevel() { return logLevel; }
    public void setLogLevel(String logLevel) { this.logLevel = logLevel; }

    public String getTimezone() { return timezone; }
    public void setTimezone(String timezone) { this.timezone = timezone; }

    public int getCheckIntervalSeconds() { return checkIntervalSeconds; }
    public void setCheckIntervalSeconds(int checkIntervalSeconds) { this.checkIntervalSeconds = checkIntervalSeconds; }

    public int getChannelCooldownMinutes() { return channelCooldownMinutes; }
    public void setChannelCooldownMinutes(int channelCooldownMinutes) { this.channelCooldownMinutes = channelCooldownMinutes; }

    public int getsaveLimit() { return saveLimit; }

    public int getsaveLimitPerChannel() { return saveLimitPerChannel; }
    public void setsaveLimitPerChannel(int saveLimitPerChannel) { this.saveLimitPerChannel = saveLimitPerChannel; }

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
