package dev.m3v.data;

import java.util.List;

import dev.m3v.data.model.*;

public class UpdateData {
    public static void updateQueue() {
        List<Channel> channels = JsonStorage.get().getChannels();
        if (channels == null || channels.isEmpty()) return;

        int size = channels.size();
        boolean bumped = false;
        for (Channel channel : channels) {
            if (channel.getCheckQue() == 1) {
                channel.setCheckQue(size + 1);
                bumped = true;
                break;
            }
        }

        if (!bumped) {
            Channel min = null;
            for (Channel channel : channels) {
                if (min == null || channel.getCheckQue() < min.getCheckQue()) min = channel;
            }
            if (min != null) min.setCheckQue(size + 1);
        }

        for (Channel channel : channels) {
            channel.setCheckQue(channel.getCheckQue() - 1);
        }

        JsonStorage.save();
    }

    public static void updateCooldown() {
        List<Channel> channels = JsonStorage.get().getChannels();
        if (channels == null || channels.isEmpty()) return;

        for (Channel channel : channels) {
            int cooldown = channel.getCheckCooldown();
            if (cooldown > 0) {
                channel.setCheckCooldown(cooldown - 1);
            }
        }

        JsonStorage.save();
    }

    public static void updateQueAndCooldown() {
        updateQueue();
        updateCooldown();
    }

    public static String getLowestChannelId() {
        List<Channel> channels = JsonStorage.get().getChannels();
        if (channels == null || channels.isEmpty()) return null;

        Channel min = null;
        for (Channel channel : channels) {
            if (min == null || channel.getCheckQue() < min.getCheckQue()) min = channel;
        }

        if (min != null) return min.getChannelId();
        else return null;
    }
}
