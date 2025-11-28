package dev.m3v;

import java.util.ArrayList;
import java.util.List;

public class Update {
    public static void update(boolean post) {
        if (!FromJson.isLoaded() || !YoutubeData.isLoaded() || !Discord.isLoaded()) return;

        try {
            List<String> newVideoIds = checkData();
            if(post && !newVideoIds.isEmpty()) Discord.sendMessage(newVideoIds);
            
            // List<String> StreamIds = checkStreamData();
            // Discord.updateMessage(); // finish this method
        } catch (Exception e) {
            System.err.println(e);
            return;
        }
    }

    public static List<String> checkData() {
        List<String> newVideoIds = new ArrayList<>();
        if (!FromJson.isLoaded() || !YoutubeData.isLoaded() || !Discord.isLoaded()) return newVideoIds;

        try {
            String lowestId = FromJson.getLowestChannelId();
            if (lowestId == null) return newVideoIds;
            newVideoIds = YoutubeData.check(YoutubeData.getYoutubeData(lowestId), lowestId);
            YoutubeData.saveVideos(newVideoIds, lowestId);
            FromJson.updateQueAndCooldownData();
            return newVideoIds;
        } catch (Exception e) {
            System.err.println(e);
            return newVideoIds;
        }
    }

    public static List<String> checkStreamData() {
        List<String> streamIds = new ArrayList<>();
        if (!FromJson.isLoaded() || !YoutubeData.isLoaded() || !Discord.isLoaded()) return streamIds;

        try {
            for (FromJson.Channel channel : FromJson.get().getChannels()) {
                String channelId = channel.getChannelId();
                // streamIds = YoutubeData.checkStream(YoutubeData.getYoutubeData(channelId), channelId);
                YoutubeData.saveVideos(streamIds, channelId);
                FromJson.updateQueAndCooldownData();
            }
        } catch (Exception e) {
            System.err.println(e);
            return streamIds;
        }
        return streamIds;
    }
}
