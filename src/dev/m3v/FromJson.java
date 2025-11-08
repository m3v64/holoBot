package dev.m3v;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
 

public class FromJson  {
    private static FromJson instance;
    
    private double version;
    private Secrets secrets;
    private List<Channel> channels;
    private List<CheckData> checkDataHistory;
    private List<LiveStream> liveStreams;
    private ConfigOptions configOptions;

    public FromJson(double version, Secrets secrets, List<Channel> channels, List<CheckData> checkDataHistory, List<LiveStream> liveStreams, ConfigOptions configOptions) {
        this.version = version;
        this.secrets = secrets;
        this.channels = channels;
        this.checkDataHistory = checkDataHistory;
        this.liveStreams = liveStreams;
        this.configOptions = configOptions;
    }

    public static FromJson get() {
        if (instance == null) {
            throw new IllegalStateException("FromJson has not been loaded - call FromJson.load() before using FromJson.get().");
        }
        return instance;
    }

    public static boolean isLoaded() {
        return instance != null;
    }

    public double getVersion() { return version; }
    public void setVersion(double version) { this.version = version; }

    public Secrets getSecrets() { return secrets; }
    public void setSecrets(Secrets secrets) { this.secrets = secrets; }

    public List<Channel> getChannels() { return channels; }

    public Channel getChannels(String channelId) {
        if (channels == null) return new Channel(channelId, 0, "");
        return channels.stream()
                .filter(c -> c.getChannelId() != null && c.getChannelId().equals(channelId))
                .findFirst()
                .orElse(new Channel(channelId, 0, ""));
    }
    public void setChannels(List<Channel> channels) { this.channels = channels; }

    public List<CheckData> getCheckDataHistory() { return checkDataHistory; }
    public void setCheckDataHistory(List<CheckData> checkDataHistory) { this.checkDataHistory = checkDataHistory; }

    public CheckData getCheckDataHistory(String channelId) {
        if (checkDataHistory == null) return new CheckData(channelId, "", "");
        return checkDataHistory.stream()
                .filter(c -> c.getChannelId() != null && c.getChannelId().equals(channelId))
                .findFirst()
                .orElse(new CheckData(channelId, "", ""));
    }

    public List<CheckData> getCheckData() { return checkDataHistory; }
    public void setCheckData(List<CheckData> checkData) { this.checkDataHistory = checkData; }
    public CheckData getCheckData(String channelId) { return getCheckDataHistory(channelId); }

    public List<LiveStream> getLiveStreams() { return liveStreams; }
    public void setLiveStreams(List<LiveStream> liveStreams) { this.liveStreams = liveStreams; }

    public LiveStream getLiveStreams(String channelId) {
        if (liveStreams == null) return new LiveStream(channelId, "", "");
        return liveStreams.stream()
                .filter(l -> l.getChannelId() != null && l.getChannelId().equals(channelId))
                .findFirst()
                .orElse(new LiveStream(channelId, "", ""));
    }

    public ConfigOptions getConfigOptions() { return configOptions; }
    public void setConfigOptions(ConfigOptions configOptions) { this.configOptions = configOptions; }


    public static class Secrets {
        private String discord_api_key;
        private String youtube_api_key;

        public Secrets() {}
        public Secrets(String discord_api_key, String youtube_api_key) {
            this.discord_api_key = discord_api_key;
            this.youtube_api_key = youtube_api_key;
        }

        public String getDiscord_api_key() { return discord_api_key; }
        public void setDiscord_api_key(String discord_api_key) { this.discord_api_key = discord_api_key; }

        public String getYoutube_api_key() { return youtube_api_key; }
        public void setYoutube_api_key(String youtube_api_key) { this.youtube_api_key = youtube_api_key; }
    }

    public static class Channel {
        private String channelId;
        private int checkQue;
        private String roleId;

        public Channel(String channelId) {
            if (!FromJson.isLoaded()) {
                throw new IllegalStateException("FromJson is not loaded. Call FromJson.load() before creating Channel instances.");
            }

            Channel match = FromJson.get().getChannels()
                    .stream()
                    .filter(c -> c.getChannelId() != null && c.getChannelId().equals(channelId))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Channel ID not found: " + channelId));

            this.channelId = match.getChannelId();
            this.checkQue = match.getCheckQue();
            this.roleId = match.getRoleId();
        }

        public Channel(String channelId, int checkQue, String roleId) {
            this.channelId = channelId;
            this.checkQue = checkQue;
            this.roleId = roleId;
        }

        public String getChannelId() { return channelId; }
        public void setChannelId(String channelId) { this.channelId = channelId; }

        public int getCheckQue() { return checkQue; }
        public void setCheckQue(int checkQue) { this.checkQue = checkQue; }

        public String getRoleId() { return roleId; }
        public void setRoleId(String roleId) { this.roleId = roleId; }
    }
    

    public static class CheckData {
        private String channelId;
        private String mediaId;
        private String roleId;
        private Data data;

        public CheckData() {}
        public CheckData(String channelId, String mediaId, String roleId) {
            this.channelId = channelId;
            this.mediaId = mediaId;
            this.roleId = roleId;
        }

        public String getChannelId() { return channelId; }
        public void setChannelId(String channelId) { this.channelId = channelId; }

        public String getMediaId() { return mediaId; }
        public void setMediaId(String mediaId) { this.mediaId = mediaId; }

        public String getRoleId() { return roleId; }
        public void setRoleId(String roleId) { this.roleId = roleId; }

        public Data getData() { return data; }
        public void setData(Data data) { this.data = data; }

        public String getVideoId() { return mediaId; }
        public void setVideoId(String videoId) { this.mediaId = videoId; }
    }

    public static class LiveStream {
        private String channelId;
        private String mediaId;
        private String roleId;
        private Data data;

        public LiveStream() {}
        public LiveStream(String channelId, String mediaId, String roleId) {
            this.channelId = channelId;
            this.mediaId = mediaId;
            this.roleId = roleId;
        }

        public String getChannelId() { return channelId; }
        public void setChannelId(String channelId) { this.channelId = channelId; }

        public String getMediaId() { return mediaId; }
        public void setMediaId(String mediaId) { this.mediaId = mediaId; }

        public String getRoleId() { return roleId; }
        public void setRoleId(String roleId) { this.roleId = roleId; }

        public Data getData() { return data; }
        public void setData(Data data) { this.data = data; }

        public String getVideoId() { return mediaId; }
        public void setVideoId(String videoId) { this.mediaId = videoId; }
    }

    public static class Data {
        private String channelName;
        private String channelurl;
        private String title;
        private String description;
        private String mediaUrl;
        private String thumbnailUrl;
        private String duration;
        private int views;
        private int avgViewers;
        private int peakViewers;
        private String startTime;
        private String endTime;

        public Data() {}

        public String getChannelName() { return channelName; }
        public void setChannelName(String channelName) { this.channelName = channelName; }

        public String getChannelurl() { return channelurl; }
        public void setChannelurl(String channelurl) { this.channelurl = channelurl; }

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public String getMediaUrl() { return mediaUrl; }
        public void setMediaUrl(String mediaUrl) { this.mediaUrl = mediaUrl; }

        public String getThumbnailUrl() { return thumbnailUrl; }
        public void setThumbnailUrl(String thumbnailUrl) { this.thumbnailUrl = thumbnailUrl; }

        public String getDuration() { return duration; }
        public void setDuration(String duration) { this.duration = duration; }

        public int getViews() { return views; }
        public void setViews(int views) { this.views = views; }

        public int getAvgViewers() { return avgViewers; }
        public void setAvgViewers(int avgViewers) { this.avgViewers = avgViewers; }

        public int getPeakViewers() { return peakViewers; }
        public void setPeakViewers(int peakViewers) { this.peakViewers = peakViewers; }

        public String getStartTime() { return startTime; }
        public void setStartTime(String startTime) { this.startTime = startTime; }

        public String getEndTime() { return endTime; }
        public void setEndTime(String endTime) { this.endTime = endTime; }
    }

    public static class ConfigOptions {
        private String logLevel;
        private String timezone;
        private int checkIntervalSeconds;
        private int channelCooldownMinutes;
        private int lastCheckSaveLimit;
        private String mediaChannelId;
        private String premiereChannelId;
        private String adminId;

        public ConfigOptions() {}
        public ConfigOptions(String logLevel, String timezone, int checkIntervalSeconds, int channelCooldownMinutes, int lastCheckSaveLimit, String mediaChannelId, String premiereChannelId, String adminId) {
            this.logLevel = logLevel;
            this.timezone = timezone;
            this.checkIntervalSeconds = checkIntervalSeconds;
            this.channelCooldownMinutes = channelCooldownMinutes;
            this.lastCheckSaveLimit = lastCheckSaveLimit;
            this.mediaChannelId = mediaChannelId;
            this.premiereChannelId = premiereChannelId;
            this.adminId = adminId;
        }

        public String getLogLevel() { return logLevel; }
        public void setLogLevel(String logLevel) { this.logLevel = logLevel; }

        public String getTimezone() { return timezone; }
        public void setTimezone(String timezone) { this.timezone = timezone; }

        public int getCheckIntervalSeconds() { return checkIntervalSeconds; }
        public void setCheckIntervalSeconds(int checkIntervalSeconds) { this.checkIntervalSeconds = checkIntervalSeconds; }

        public int getChannelCooldownMinutes() { return channelCooldownMinutes; }
        public void setChannelCooldownMinutes(int channelCooldownMinutes) { this.channelCooldownMinutes = channelCooldownMinutes; }

        public int getLastCheckSaveLimit() { return lastCheckSaveLimit; }
        public void setLastCheckSaveLimit(int lastCheckSaveLimit) { this.lastCheckSaveLimit = lastCheckSaveLimit; }

        public String getMediaChannelId() { return mediaChannelId; }
        public void setMediaChannelId(String mediaChannelId) { this.mediaChannelId = mediaChannelId; }

        public int getcheckDataSaveLimit() { return lastCheckSaveLimit; }
        public void setcheckDataSaveLimit(int checkDataSaveLimit) { this.lastCheckSaveLimit = checkDataSaveLimit; }

        public String getVideoChannelId() { return mediaChannelId; }
        public void setVideoChannelId(String videoChannelId) { this.mediaChannelId = videoChannelId; }

        public String getPremiereChannelId() { return premiereChannelId; }
        public void setPremiereChannelId(String premiereChannelId) { this.premiereChannelId = premiereChannelId; }

        public String getAdminId() { return adminId; }
        public void setAdminId(String adminId) { this.adminId = adminId; }
    }

    public static synchronized void load() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileReader reader = new FileReader("data/data.json")) {
            FromJson data = gson.fromJson(reader, FromJson.class);
            instance = data;
        } catch (Exception e) {
            e.printStackTrace();
            instance = new FromJson(
                1.0,
                new Secrets(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ConfigOptions()
            );
            Discord.sendError("Loading Json data", e.getMessage());
        }
    }

    public static synchronized void save() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter("data/data.json")) {
            FromJson toWrite = instance == null ? new FromJson(1.0, new Secrets(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ConfigOptions()) : instance;
            gson.toJson(toWrite, writer);
        } catch (Exception e) {
            e.printStackTrace();
            Discord.sendError("Saving Json data", e.getMessage());
        }
    }
}
