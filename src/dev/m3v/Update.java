package dev.m3v;

import java.util.ArrayList;
import java.util.List;

public class Update {
    public static void checkOld() {
        for (FromJson.LiveStream stream : FromJson.get().getLiveStreams()) {
            
        }
    }

    public static void checkNew() {
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
