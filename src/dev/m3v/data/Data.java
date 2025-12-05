package dev.m3v.data;

import java.util.ArrayList;
import java.util.List;

import dev.m3v.data.model.*;


public class Data {
    public static Data instance;
    
    private double version;
    private Secrets secrets;
    private List<Channels> channels;
    private List<Memory> memoryCache;
    private List<LiveStreams> liveStreams;
    private ConfigOptions configOptions;
    private MediaData mediaData;

    public Data(double version, Secrets secrets, List<Channels> channels, List<Memory> memoryCache, List<LiveStreams> liveStreams, ConfigOptions configOptions) {
        this.version = version;
        this.secrets = secrets;
        this.channels = channels;
        this.memoryCache = memoryCache;
        this.liveStreams = liveStreams;
        this.configOptions = configOptions;
    }

    public double getVersion() { return version; }
    public void setVersion(double version) { this.version = version; }

    public Secrets getSecrets() { return secrets; }
    public void setSecrets(Secrets secrets) { this.secrets = secrets; }

    public List<Channels> getChannels() { return channels; }

    public Channels getChannels(String channelId) {
        if (channels == null) return new Channels(channelId, 0, 0, "");
        return channels.stream()
                .filter(channel -> channel.getChannelId() != null && channel.getChannelId().equals(channelId))
                .findFirst()
                .orElse(new Channels(channelId, 0, 0, ""));
    }
    public void setChannels(List<Channels> channels) { this.channels = channels; }

    public Channels setChannels(String channelId, Channels channelData) {
        if (channelId == null) // implement error handeling 
        if (this.channels == null) this.channels = new ArrayList<>();

        Channels existing = this.channels.stream()
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

    Channels toAdd = (channelData == null)
        ? new Channels(channelId, 0, 0, "")
        : new Channels(channelId, channelData.getCheckQueue(), channelData.getCheckCooldown(), channelData.getRoleId());
    toAdd.setCheckCooldown(channelData == null ? 0 : channelData.getCheckCooldown());
        this.channels.add(toAdd);
        return toAdd;
    }

    public Channels setChannels(String channelId) { return setChannels(channelId, null); }

    public List<Memory> getMemoryCache() { return memoryCache; }
    public void setMemoryCache(List<Memory> Memory) { this.memoryCache = Memory; }

    public Memory setMemoryCache(String channelId, Memory data) {
        if (channelId == null) // implement error handeling
        if (this.memoryCache == null) this.memoryCache = new ArrayList<>();

        Memory existing = this.memoryCache.stream()
                .filter(channels -> channels.getChannelId() != null && channels.getChannelId().equals(channelId))
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

    Memory toAdd = (data == null)
        ? new Memory(channelId, "", "")
        : new Memory(channelId, data.getMediaId(), data.getRoleId());
    if (data != null) {
        toAdd.setMessageId(data.getMessageId());
        toAdd.setIsPremier(data.getIsPremier());
        toAdd.setIsVod(data.getIsVod());
        toAdd.setData(data.getData());
    }
        this.memoryCache.add(toAdd);
        return toAdd;
    }

    public Memory getMemoryCache(String channelId) {
        if (memoryCache == null) return new Memory(channelId, "", "");
        return memoryCache.stream()
                .filter(channels -> channels.getChannelId() != null && channels.getChannelId().equals(channelId))
                .findFirst()
                .orElse(new Memory(channelId, "", ""));
    }

    public List<Memory> getMemory() { return memoryCache; }
    public void setMemory(List<Memory> Memory) { this.memoryCache = Memory; }
    public Memory getMemory(String channelId) { return getMemoryCache(channelId); }

    public Memory setMemory(String channelId, Memory data) { return setMemoryCache(channelId, data); }

    public List<LiveStreams> getLiveStreams() { return liveStreams; }
    public void setLiveStreams(List<LiveStreams> liveStreams) { this.liveStreams = liveStreams; }

    public LiveStreams setLiveStreams(String channelId, LiveStreams stream) {
        if (channelId == null) // implement error handeling
        if (this.liveStreams == null) this.liveStreams = new ArrayList<>();

        LiveStreams existing = this.liveStreams.stream()
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

    LiveStreams toAdd = (stream == null)
        ? new LiveStreams(channelId, "", "")
        : new LiveStreams(channelId, stream.getMediaId(), stream.getRoleId());
    if (stream != null) {
        toAdd.setMessageId(stream.getMessageId());
        toAdd.setIsPremier(stream.getIsPremier());
        toAdd.setIsVod(stream.getIsVod());
        toAdd.setData(stream.getData());
    }
        this.liveStreams.add(toAdd);
        return toAdd;
    }

    public LiveStreams getLiveStreams(String channelId) {
        if (liveStreams == null) return new LiveStreams(channelId, "", "");
        return liveStreams.stream()
                .filter(l -> l.getChannelId() != null && l.getChannelId().equals(channelId))
                .findFirst()
                .orElse(new LiveStreams(channelId, "", ""));
    }

    public ConfigOptions getConfigOptions() { return configOptions; }
    public void setConfigOptions(ConfigOptions configOptions) { this.configOptions = configOptions; }

    public MediaData getData() { return mediaData; }
    public void setData(MediaData mediaData) { this.mediaData = mediaData; }
}
