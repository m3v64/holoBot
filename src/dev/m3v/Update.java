package dev.m3v;

import java.util.ArrayList;
import java.util.List;

public class Update {
    public static void checkOld() {
        if (!FromJson.isLoaded() || !YoutubeData.isLoaded() || !Discord.isLoaded()) return;

        List<String> newVideoIds = new ArrayList<>();
        try {
            // write method to get just the data from the streams
            YoutubeData.saveVideos(); // this needs to just save video data to json, or jst write a new method

            Discord.updateMessage(); // finish this method
        } catch (Exception e) {
            System.err.println(e);
            return;
        }
    }

    public static void checkNew() {
        if (!FromJson.isLoaded() || !YoutubeData.isLoaded() || !Discord.isLoaded()) return;

        List<String> newVideoIds = new ArrayList<>();
        try {
            String lowestId = FromJson.getLowestChannelId();
            if (lowestId == null) return;
            newVideoIds = YoutubeData.check(YoutubeData.getYoutubeData(lowestId), lowestId);

            Discord.sendMessage(newVideoIds);
            YoutubeData.saveVideos(newVideoIds, lowestId);
            FromJson.updateQueAndCooldownData();
        } catch (Exception e) {
            System.err.println(e);
            return;
        }
    }
}
