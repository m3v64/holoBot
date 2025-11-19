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
        if (channels == null) return new Channel(channelId, 0, 0, "");
        return channels.stream()
                .filter(channel -> channel.getChannelId() != null && channel.getChannelId().equals(channelId))
                .findFirst()
                .orElse(new Channel(channelId, 0, 0, ""));
    }
    public void setChannels(List<Channel> channels) { this.channels = channels; }

    public Channel setChannels(String channelId, Channel channelData) {
        if (channelId == null) throw new IllegalArgumentException("channelId cannot be null");
        if (this.channels == null) this.channels = new ArrayList<>();

        Channel existing = this.channels.stream()
                .filter(channel -> channel.getChannelId() != null && channel.getChannelId().equals(channelId))
                .findFirst()
                .orElse(null);

        if (existing != null) {
            if (channelData != null) {
                existing.setCheckQueue(channelData.getCheckQueue());
                existing.setCheckCooldown(channelData.getCheckCooldown());
                existing.setRoleId(channelData.getRoleId());
            }
            return existing;
        }

    Channel toAdd = (channelData == null)
        ? new Channel(channelId, 0, 0, "")
        : new Channel(channelId, channelData.getCheckQueue(), channelData.getCheckCooldown(), channelData.getRoleId());
    toAdd.setCheckCooldown(channelData == null ? 0 : channelData.getCheckCooldown());
        this.channels.add(toAdd);
        return toAdd;
    }

    public Channel setChannels(String channelId) { return setChannels(channelId, null); }

    public List<CheckData> getCheckDataHistory() { return checkDataHistory; }
    public void setCheckDataHistory(List<CheckData> checkDataHistory) { this.checkDataHistory = checkDataHistory; }

    public CheckData setCheckDataHistory(String channelId, CheckData data) {
        if (channelId == null) throw new IllegalArgumentException("channelId cannot be null");
        if (this.checkDataHistory == null) this.checkDataHistory = new ArrayList<>();

        CheckData existing = this.checkDataHistory.stream()
                .filter(channel -> channel.getChannelId() != null && channel.getChannelId().equals(channelId))
                .findFirst()
                .orElse(null);

        if (existing != null) {
            if (data != null) {
                existing.setMediaId(data.getMediaId());
                existing.setRoleId(data.getRoleId());
                existing.setMessageId(data.getMessageId());
                existing.setIsPremier(data.getIsPremier());
                existing.setIsVod(data.getIsVod());
                existing.setData(data.getData());
            }
            return existing;
        }

    CheckData toAdd = (data == null)
        ? new CheckData(channelId, "", "")
        : new CheckData(channelId, data.getMediaId(), data.getRoleId());
    if (data != null) {
        toAdd.setMessageId(data.getMessageId());
        toAdd.setIsPremier(data.getIsPremier());
        toAdd.setIsVod(data.getIsVod());
        toAdd.setData(data.getData());
    }
        this.checkDataHistory.add(toAdd);
        return toAdd;
    }

    public CheckData getCheckDataHistory(String channelId) {
        if (checkDataHistory == null) return new CheckData(channelId, "", "");
        return checkDataHistory.stream()
                .filter(channel -> channel.getChannelId() != null && channel.getChannelId().equals(channelId))
                .findFirst()
                .orElse(new CheckData(channelId, "", ""));
    }

    public List<CheckData> getCheckData() { return checkDataHistory; }
    public void setCheckData(List<CheckData> checkData) { this.checkDataHistory = checkData; }
    public CheckData getCheckData(String channelId) { return getCheckDataHistory(channelId); }

    public CheckData setCheckData(String channelId, CheckData data) { return setCheckDataHistory(channelId, data); }

    public List<LiveStream> getLiveStreams() { return liveStreams; }
    public void setLiveStreams(List<LiveStream> liveStreams) { this.liveStreams = liveStreams; }

    public LiveStream setLiveStreams(String channelId, LiveStream stream) {
        if (channelId == null) throw new IllegalArgumentException("channelId cannot be null");
        if (this.liveStreams == null) this.liveStreams = new ArrayList<>();

        LiveStream existing = this.liveStreams.stream()
                .filter(l -> l.getChannelId() != null && l.getChannelId().equals(channelId))
                .findFirst()
                .orElse(null);

        if (existing != null) {
            if (stream != null) {
                existing.setMediaId(stream.getMediaId());
                existing.setRoleId(stream.getRoleId());
                existing.setMessageId(stream.getMessageId());
                existing.setIsPremier(stream.getIsPremier());
                existing.setIsVod(stream.getIsVod());
                existing.setData(stream.getData());
            }
            return existing;
        }

    LiveStream toAdd = (stream == null)
        ? new LiveStream(channelId, "", "")
        : new LiveStream(channelId, stream.getMediaId(), stream.getRoleId());
    if (stream != null) {
        toAdd.setMessageId(stream.getMessageId());
        toAdd.setIsPremier(stream.getIsPremier());
        toAdd.setIsVod(stream.getIsVod());
        toAdd.setData(stream.getData());
    }
        this.liveStreams.add(toAdd);
        return toAdd;
    }

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
        private int checkCooldown;
        private String roleId;

    public Channel(String channelId) {
            if (!FromJson.isLoaded()) {
                throw new IllegalStateException("FromJson is not loaded. Call FromJson.load() before creating Channel instances.");
            }

            Channel match = FromJson.get().getChannels()
                    .stream()
                    .filter(channel -> channel.getChannelId() != null && channel.getChannelId().equals(channelId))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Channel ID not found: " + channelId));

            this.channelId = match.getChannelId();
            this.checkQue = match.getCheckQueue();
            this.checkCooldown = match.getCheckCooldown();
            this.roleId = match.getRoleId();
        }

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
    

    public static class CheckData {
        private String channelId;
        private String mediaId;
        private String roleId;
        private String messageId;
        private boolean isPremier;
        private boolean isVod;
        private Data data;

        public CheckData() {}
        public CheckData(String channelId, String mediaId, String roleId) {
            this.channelId = channelId;
            this.mediaId = mediaId;
            this.roleId = roleId;
            this.isVod = false;
        }

        public CheckData(String channelId, String mediaId, String roleId, boolean isVod) {
            this.channelId = channelId;
            this.mediaId = mediaId;
            this.roleId = roleId;
            this.isVod = isVod;
        }

        public String getChannelId() { return channelId; }
        public void setChannelId(String channelId) { this.channelId = channelId; }

        public String getMediaId() { return mediaId; }
        public void setMediaId(String mediaId) { this.mediaId = mediaId; }

        public String getRoleId() { return roleId; }
        public void setRoleId(String roleId) { this.roleId = roleId; }

        public String getMessageId() { return messageId; }
        public void setMessageId(String messageId) { this.messageId = messageId; }

        public boolean getIsPremier() { return isPremier; }
        public void setIsPremier(boolean isPremier) { this.isPremier = isPremier; }

        public boolean getIsVod() { return isVod; }
        public void setIsVod(boolean isVod) { this.isVod = isVod; }

        public Data getData() { return data; }
        public void setData(Data data) { this.data = data; }

        public String getVideoId() { return mediaId; }
        public void setVideoId(String videoId) { this.mediaId = videoId; }
    }

    public static class LiveStream {
        private String channelId;
        private String mediaId;
        private String roleId;
        private String messageId;
        private boolean isPremier;
        private boolean isVod;
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

        public String getMessageId() { return messageId; }
        public void setMessageId(String messageId) { this.messageId = messageId; }

        public boolean getIsPremier() { return isPremier; }
        public void setIsPremier(boolean isPremier) { this.isPremier = isPremier; }

        public boolean getIsVod() { return isVod; }
        public void setIsVod(boolean isVod) { this.isVod = isVod; }

        public Data getData() { return data; }
        public void setData(Data data) { this.data = data; }

        public String getVideoId() { return mediaId; }
        public void setVideoId(String videoId) { this.mediaId = videoId; }
    }

    public static class Data {
        private String channelName;
        private String channelUrl;
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

        public String getChannelUrl() { return channelUrl; }
        public void setChannelUrl(String channelUrl) { this.channelUrl = channelUrl; }

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
        private int lastCheckSaveLimitPerChannel;
        private String mediaChannelId;
        private String premiereChannelId;
        private String liveStreamChannelId;
        private String adminId;

        public ConfigOptions() {}
        public ConfigOptions(String logLevel, String timezone, int checkIntervalSeconds, int channelCooldownMinutes, int lastCheckSaveLimit, int lastCheckSaveLimitPerChannel, String mediaChannelId, String premiereChannelId, String liveStreamChannelId, String adminId) {
            this.logLevel = logLevel;
            this.timezone = timezone;
            this.checkIntervalSeconds = checkIntervalSeconds;
            this.channelCooldownMinutes = channelCooldownMinutes;
            this.lastCheckSaveLimit = lastCheckSaveLimitPerChannel * get().getChannels().size();
            this.lastCheckSaveLimitPerChannel = lastCheckSaveLimitPerChannel;
            this.mediaChannelId = mediaChannelId;
            this.premiereChannelId = premiereChannelId;
            this.liveStreamChannelId = liveStreamChannelId;
            this.adminId = adminId;
        }

        public ConfigOptions(String logLevel, String timezone, int checkIntervalSeconds, int channelCooldownMinutes, int lastCheckSaveLimit, String mediaChannelId, String premiereChannelId, String liveStreamChannelId, String adminId) {
            this(logLevel, timezone, checkIntervalSeconds, channelCooldownMinutes, lastCheckSaveLimit, 0, mediaChannelId, premiereChannelId, liveStreamChannelId, adminId);
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

        public int getLastCheckSaveLimitPerChannel() { return lastCheckSaveLimitPerChannel; }
        public void setLastCheckSaveLimitPerChannel(int lastCheckSaveLimitPerChannel) { this.lastCheckSaveLimitPerChannel = lastCheckSaveLimitPerChannel; }

        public String getMediaChannelId() { return mediaChannelId; }
        public void setMediaChannelId(String mediaChannelId) { this.mediaChannelId = mediaChannelId; }

        public String getVideoChannelId() { return mediaChannelId; }
        public void setVideoChannelId(String videoChannelId) { this.mediaChannelId = videoChannelId; }

        public String getPremiereChannelId() { return premiereChannelId; }
        public void setPremiereChannelId(String premiereChannelId) { this.premiereChannelId = premiereChannelId; }

        public String getLiveStreamChannelId() { return liveStreamChannelId; }
        public void setLiveStreamChannelId(String liveStreamChannelId) { this.liveStreamChannelId = liveStreamChannelId; }

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
            Discord.sendError("Loading Json data", null, e);
        }
    }

    public static synchronized void save() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter("data/data.json")) {
            FromJson toWrite = instance == null ? new FromJson(1.0, new Secrets(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ConfigOptions()) : instance;
            gson.toJson(toWrite, writer);
        } catch (Exception e) {
            e.printStackTrace();
            Discord.sendError("Saving Json data", null, e);
        }
    }

    public static void updateQue() {
        List<Channel> channels = get().getChannels();
        if (channels == null || channels.isEmpty()) return;

        int size = channels.size();
        boolean bumped = false;
        for (Channel channel : channels) {
            if (channel.getCheckQueue() == 1) {
                channel.setCheckQueue(size + 1);
                bumped = true;
                break;
            }
        }

        if (!bumped) {
            Channel min = null;
            for (Channel channel : channels) {
                if (min == null || channel.getCheckQueue() < min.getCheckQueue()) min = channel;
            }
            if (min != null) min.setCheckQueue(size + 1);
        }

        for (Channel channel : channels) {
            channel.setCheckQueue(channel.getCheckQueue() - 1);
            channel.setCheckCooldown(get().getConfigOptions().getChannelCooldownMinutes());
        }

        FromJson.save();
    }

    public static String getLowestChannelIdAndUpdateCooldown() {
        for (Channel channel : get().getChannels()) channel.setCheckCooldown(channel.getCheckCooldown()-1);
        for (Channel channel : get().getChannels()) {
            if (channel.getCheckQueue() == 1 ) {
                if (channel.getCheckCooldown() != 0)
                    return channel.getChannelId();
            }
        }
        return null;
    }
}
