package dev.m3v.data;

import java.util.List;

import dev.m3v.data.model.*;
import dev.m3v.Log;

public class UpdateData {
    public static void updateQueue() {
        Log.debug("Updating channel queue", UpdateData.class);
        List<Channel> channels = JsonStorage.get().getChannels();
        if (channels == null || channels.isEmpty()) {
            Log.info("No channels to update in queue", UpdateData.class);
            return;
        }

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
        Log.debug("Updating channel cooldowns", UpdateData.class);
        List<Channel> channels = JsonStorage.get().getChannels();
        if (channels == null || channels.isEmpty()) {
            Log.info("No channels to update cooldown", UpdateData.class);
            return;
        }

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
        Log.debug("Getting lowest channel ID from queue", UpdateData.class);
        List<Channel> channels = JsonStorage.get().getChannels();
        if (channels == null || channels.isEmpty()) {
            Log.info("No channels available to get lowest ID", UpdateData.class);
            return null;
        }

        Channel min = null;
        for (Channel channel : channels) {
            if (min == null || channel.getCheckQue() < min.getCheckQue()) min = channel;
        }

        if (min != null) return min.getChannelId();
        else return null;
    }
}
