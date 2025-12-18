package dev.m3v.data;

import java.util.ArrayList;
import java.util.List;

import dev.m3v.data.model.*;
import dev.m3v.data.model.media.*;


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
                .orElse(new Channel(channelId, 0, 0, ""));
    }

    public Channel setChannels(String channelId, Channel channelData) {
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
        this.channels.add(toAdd);
        return toAdd;
    }
}
