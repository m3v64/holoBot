package dev.m3v.data;

import java.util.List;

import dev.m3v.data.model.*;

public class UpdateData {
    public static void updateQueue() {
        List<Channels> channels = JsonStorage.get().getChannels();
        if (channels == null || channels.isEmpty()) return;

        int size = channels.size();
        boolean bumped = false;
        for (Channels channel : channels) {
            if (channel.getCheckQueue() == 1) {
                channel.setCheckQueue(size + 1);
                bumped = true;
                break;
            }
        }

        if (!bumped) {
            Channels min = null;
            for (Channels channel : channels) {
                if (min == null || channel.getCheckQueue() < min.getCheckQueue()) min = channel;
            }
            if (min != null) min.setCheckQueue(size + 1);
        }

        for (Channels channel : channels) {
            channel.setCheckQueue(channel.getCheckQueue() - 1);
        }

        JsonStorage.save();
    }

    public static void updateCooldown() {
        List<Channels> channels = JsonStorage.get().getChannels();
        if (channels == null || channels.isEmpty()) return;

        for (Channels channel : channels) {
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
        List<Channels> channels = JsonStorage.get().getChannels();
        if (channels == null || channels.isEmpty()) return null;

        Channels min = null;
        for (Channels channel : channels) {
            if (min == null || channel.getCheckQueue() < min.getCheckQueue()) min = channel;
        }

        if (min != null) return min.getChannelId();
        else return null;
    }
}
