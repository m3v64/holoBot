package dev.m3v.data;

import java.util.List;

import dev.m3v.data.model.*;

public class Data {
    public static Data instance;
    
    private double version;
    private Secrets secrets;
    private ConfigOptions configOptions;
    private List<Channel> channels;
    private List<Media> memory;
    private List<Media> liveStreams;

    public Channel getChannel(String channelId) {
        return channels.stream()
                .filter(channel -> channel.getChannelId() != null && channel.getChannelId().equals(channelId))
                .findFirst()
                .orElse(null);
    }

    public Channel setChannels(String channelId, Channel channelData) {
        Channel existing = this.channels.stream()
                .filter(channel -> channel.getChannelId() != null && channel.getChannelId().equals(channelId))
                .findFirst()
                .orElse(null);

        if (existing != null) {
            if (channelData != null) {
                existing.setCheckQue(channelData.getCheckQue());
                existing.setCheckCooldown(channelData.getCheckCooldown());
                existing.setRoleId(channelData.getRoleId());
                existing.setSettings(channelData.getSettings());
            }
            return existing;
        }

        Channel toAdd = (channelData == null)
            ? new Channel(channelId, 0, 0, "", JsonStorage.get().getConfigOptions().getChannelSettingDefaults())
            : new Channel(channelId, channelData.getCheckQue(), channelData.getCheckCooldown(), channelData.getRoleId(), channelData.getSettings());
        this.channels.add(toAdd);
        return toAdd;
    }

    public Media getMemory(String channelId) {
        return memory.stream()
                .filter(memory -> memory.getChannelId() != null && memory.getChannelId().equals(channelId))
                .findFirst()
                .orElse(null);
    }

    public Media setMemory(String channelId, Media memoryData) {
        Media existing = this.memory.stream()
                .filter(memory -> memory.getChannelId() != null && memory.getChannelId().equals(channelId))
                .findFirst()
                .orElse(null);

        if (existing != null) {
            if (memoryData != null) {
                existing.setData(memoryData.getData());
                existing.setMediaId(memoryData.getMediaId());
                existing.setMessageId(memoryData.getMessageId());
                existing.setRoleId(memoryData.getRoleId());
                existing.setType(memoryData.getType());
            }
            return existing;
        }

        Media toAdd = (memoryData == null)
            ? new Media()
            : new Media(channelId, memoryData.getMediaId(), memoryData.getRoleId(), memoryData.getMessageId(), memoryData.getType(), memoryData.getData());
        this.memory.add(toAdd);
        return toAdd;
    }

    public Media getLiveStreams(String channelId) {
        return liveStreams.stream()
                .filter(liveStreams -> liveStreams.getChannelId() != null && liveStreams.getChannelId().equals(channelId))
                .findFirst()
                .orElse(null);
    }

    public Media setLiveStreams(String channelId, Media streamData) {
        Media existing = this.liveStreams.stream()
                .filter(liveStreams -> liveStreams.getChannelId() != null && liveStreams.getChannelId().equals(channelId))
                .findFirst()
                .orElse(null);

        if (existing != null) {
            if (streamData != null) {
                existing.setData(streamData.getData());
                existing.setMediaId(streamData.getMediaId());
                existing.setMessageId(streamData.getMessageId());
                existing.setRoleId(streamData.getRoleId());
                existing.setType(streamData.getType());
            }
            return existing;
        }

        Media toAdd = (streamData == null)
            ? new Media()
            : new Media(channelId, streamData.getMediaId(), streamData.getRoleId(), streamData.getMessageId(), streamData.getType(), streamData.getData());
        this.liveStreams.add(toAdd);
        return toAdd;
    }

    public Media getMedia(String channelId) {
        Media result;
        if ((result = memory.stream().filter(memory -> memory.getChannelId() != null && memory.getChannelId().equals(channelId)).findFirst().orElse(null)) != null) return result;
        else if ((result = liveStreams.stream().filter(liveStreams -> liveStreams.getChannelId() != null && liveStreams.getChannelId().equals(channelId)).findFirst().orElse(null)) != null) return result;
        return null;    
    }

    public Media setMedia(String channelId, Media memoryData) {
        Media existingMemory = this.memory.stream()
                .filter(memory -> memory.getChannelId() != null && memory.getChannelId().equals(channelId))
                .findFirst()
                .orElse(null);

        if (existingMemory != null) {
            if (memoryData != null) {
                existingMemory.setData(memoryData.getData());
                existingMemory.setMediaId(memoryData.getMediaId());
                existingMemory.setMessageId(memoryData.getMessageId());
                existingMemory.setRoleId(memoryData.getRoleId());
                existingMemory.setType(memoryData.getType());
            }
            return existingMemory;
        }

        Media existingStream = this.liveStreams.stream()
                .filter(liveStream -> liveStream.getChannelId() != null && liveStream.getChannelId().equals(channelId))
                .findFirst()
                .orElse(null);

        if (existingStream != null) {
            if (memoryData != null) {
                existingStream.setData(memoryData.getData());
                existingStream.setMediaId(memoryData.getMediaId());
                existingStream.setMessageId(memoryData.getMessageId());
                existingStream.setRoleId(memoryData.getRoleId());
                existingStream.setType(memoryData.getType());
            }
            return existingStream;
        }

        Media toAdd = (memoryData == null)
            ? new Media()
            : new Media(channelId, memoryData.getMediaId(), memoryData.getRoleId(), memoryData.getMessageId(), memoryData.getType(), memoryData.getData());
        this.memory.add(toAdd);
        return toAdd;
    }

    public static Data getInstance() {
        return instance;
    }

    public static void setInstance(Data instance) {
        Data.instance = instance;
    }

    public double getVersion() {
        return version;
    }

    public void setVersion(double version) {
        this.version = version;
    }

    public Secrets getSecrets() {
        return secrets;
    }

    public void setSecrets(Secrets secrets) {
        this.secrets = secrets;
    }

    public ConfigOptions getConfigOptions() {
        return configOptions;
    }

    public void setConfigOptions(ConfigOptions configOptions) {
        this.configOptions = configOptions;
    }

    public List<Channel> getChannels() {
        return channels;
    }

    public void setChannels(List<Channel> channels) {
        this.channels = channels;
    }

    public List<Media> getMemory() {
        return memory;
    }

    public void setMemory(List<Media> memory) {
        this.memory = memory;
    }

    public List<Media> getLiveStreams() {
        return liveStreams;
    }

    public void setLiveStreams(List<Media> liveStreams) {
        this.liveStreams = liveStreams;
    }
}
