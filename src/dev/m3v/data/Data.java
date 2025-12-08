package dev.m3v.data;

import java.util.ArrayList;
import java.util.List;

import dev.m3v.data.model.*;
import dev.m3v.data.model.media.*;


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
        if (channelId == null); // implement error handeling 
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
        this.channels.add(toAdd);
        return toAdd;
    }

    public Channels setChannels(String channelId) { return setChannels(channelId, null); }

    public List<Memory> getMemoryCache() { return memoryCache; }
    public void setMemoryCache(List<Memory> Memory) { this.memoryCache = Memory; }

    public Memory setMemoryCache(String channelId, Memory data) {
        if (channelId == null); // implement error handeling
        if (this.memoryCache == null) this.memoryCache = new ArrayList<>();

        Memory existing = this.memoryCache.stream()
                .filter(memory -> memory.getMedia() != null && memory.getMedia().getChannelId() != null && memory.getMedia().getChannelId().equals(channelId))
                .findFirst()
                .orElse(null);

        if (existing != null) {
            if (data != null) {
                existing.setMedia(data.getMedia());
            }
            return existing;
        }

        Memory toAdd = (data == null) ? new Memory() : new Memory(data.getMedia());
        this.memoryCache.add(toAdd);
        return toAdd;
    }

    public Memory getMemoryCache(String channelId) {
        if (memoryCache == null) return new Memory();
        return memoryCache.stream()
                .filter(memory -> memory.getMedia() != null && memory.getMedia().getChannelId() != null && memory.getMedia().getChannelId().equals(channelId))
                .findFirst()
                .orElse(new Memory());
    }

    public List<Memory> getMemory() { return memoryCache; }
    public void setMemory(List<Memory> Memory) { this.memoryCache = Memory; }
    public Memory getMemory(String channelId) { return getMemoryCache(channelId); }

    public Memory setMemory(String channelId, Memory data) { return setMemoryCache(channelId, data); }

    public List<LiveStreams> getLiveStreams() { return liveStreams; }
    public void setLiveStreams(List<LiveStreams> liveStreams) { this.liveStreams = liveStreams; }

    public LiveStreams setLiveStreams(String channelId, LiveStreams stream) {
        if (channelId == null); // implement error handeling
        if (this.liveStreams == null) this.liveStreams = new ArrayList<>();

        LiveStreams existing = this.liveStreams.stream()
                .filter(l -> l.getMedia() != null && l.getMedia().getChannelId() != null && l.getMedia().getChannelId().equals(channelId))
                .findFirst()
                .orElse(null);

        if (existing != null) {
            if (stream != null) {
                existing.setMedia(stream.getMedia());
            }
            return existing;
        }

        LiveStreams toAdd = (stream == null) ? new LiveStreams() : new LiveStreams(stream.getMedia());
        this.liveStreams.add(toAdd);
        return toAdd;
    }

    public LiveStreams getLiveStreams(String channelId) {
        if (liveStreams == null) return new LiveStreams();
        return liveStreams.stream()
                .filter(l -> l.getMedia() != null && l.getMedia().getChannelId() != null && l.getMedia().getChannelId().equals(channelId))
                .findFirst()
                .orElse(new LiveStreams());
    }

    public ConfigOptions getConfigOptions() { return configOptions; }
    public void setConfigOptions(ConfigOptions configOptions) { this.configOptions = configOptions; }

    public MediaData getData() { return mediaData; }
    public void setData(MediaData mediaData) { this.mediaData = mediaData; }

    public Media getMediaById(String mediaId) {
        if (mediaId == null) return null;
        
        if (memoryCache != null) {
            Media media = memoryCache.stream()
                .map(Memory::getMedia)
                .filter(m -> m != null && mediaId.equals(m.getMediaId()))
                .findFirst()
                .orElse(null);
            if (media != null) return media;
        }
        
        if (liveStreams != null) {
            return liveStreams.stream()
                .map(LiveStreams::getMedia)
                .filter(m -> m != null && mediaId.equals(m.getMediaId()))
                .findFirst()
                .orElse(null);
        }
        
        return null;
    }
}
