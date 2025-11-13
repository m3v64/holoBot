package dev.m3v;

import java.util.ArrayList;
import java.util.List;

public class Update {
    public static void removePremiers() {
        
    }

    public static void updateLive() {
        
    }

    public static void check() {
        if (!FromJson.isLoaded() || !YoutubeData.isLoaded() || !Discord.isLoaded()) return;

        List<String> newVideoIds = new ArrayList<>();
        try {
            String lowestId = FromJson.getLowestChannelIdAndUpdateCooldown();
            if (lowestId == null) return;
            newVideoIds = YoutubeData.check(lowestId);

            Discord.sendMessage(newVideoIds);
            YoutubeData.saveVideos(newVideoIds, lowestId);
            FromJson.updateQue();
        } catch (Exception e) {
            System.err.println(e);
            return;
        }
    }
}
