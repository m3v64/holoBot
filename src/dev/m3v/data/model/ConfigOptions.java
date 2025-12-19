package dev.m3v.data.model;

public class ConfigOptions {
    private String logLevel;
    private String timezone;
    private channelSettings channelSettingDefaults;
    private String mediaChannelId;
    private String premierChannelId;
    private String streamChannelId;
    private String modChannelId;
    private String adminPingId;

    public ConfigOptions(String logLevel, String timezone, channelSettings channelSettingDefaults,
            String mediaChannelId, String premierChannelId, String streamChannelId, String modChannelId,
            String adminPingId) {
        this.logLevel = logLevel;
        this.timezone = timezone;
        this.channelSettingDefaults = channelSettingDefaults;
        this.mediaChannelId = mediaChannelId;
        this.premierChannelId = premierChannelId;
        this.streamChannelId = streamChannelId;
        this.modChannelId = modChannelId;
        this.adminPingId = adminPingId;
    }

    public String getLogLevel() {
        return logLevel;
    }
    public void setLogLevel(String logLevel) {
        this.logLevel = logLevel;
    }
    public String getTimezone() {
        return timezone;
    }
    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }
    public channelSettings getChannelSettingDefaults() {
        return channelSettingDefaults;
    }
    public void setChannelSettingDefaults(channelSettings channelSettingDefaults) {
        this.channelSettingDefaults = channelSettingDefaults;
    }
    public String getMediaChannelId() {
        return mediaChannelId;
    }
    public void setMediaChannelId(String mediaChannelId) {
        this.mediaChannelId = mediaChannelId;
    }
    public String getPremierChannelId() {
        return premierChannelId;
    }
    public void setPremierChannelId(String premierChannelId) {
        this.premierChannelId = premierChannelId;
    }
    public String getStreamChannelId() {
        return streamChannelId;
    }
    public void setStreamChannelId(String streamChannelId) {
        this.streamChannelId = streamChannelId;
    }
    public String getModChannelId() {
        return modChannelId;
    }
    public void setModChannelId(String modChannelId) {
        this.modChannelId = modChannelId;
    }
    public String getAdminPingId() {
        return adminPingId;
    }
    public void setAdminPingId(String adminPingId) {
        this.adminPingId = adminPingId;
    }
}
