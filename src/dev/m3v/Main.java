package dev.m3v;

import java.util.concurrent.*;
import dev.m3v.discord.*;
import dev.m3v.data.*;
import dev.m3v.data.model.*;
import dev.m3v.data.model.media.*;

import java.util.List;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        UnitTests.test();
        // System.out.println("Starting holoBot...");
        // try {
        //     JsonStorage.load();
        //     Bot.initiateBot();
        //     YoutubeData.initialize();
        // } catch (Exception e) {
        //     e.printStackTrace();
        //     System.err.println("Failed to initialize holoBot. Exiting.");
        //     System.exit(1);
        // }

        // ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

        // Runnable checkYouTubeTask = () -> {
        //     try {
        //         checkStreamData();
        //         update(true);
        //     } catch (Exception e) {
        //         Bot.sendError("Main check task loop", null, e);
        //     }
        // };

        // scheduler.scheduleAtFixedRate(checkYouTubeTask, 0, 60, TimeUnit.SECONDS);

        // Runtime.getRuntime().addShutdownHook(new Thread(() -> {
        //     System.out.println("Shutting down holoBot...");
        //     scheduler.shutdown();
        //     try {
        //         if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
        //             scheduler.shutdownNow();
        //         }
        //     } catch (InterruptedException e) {
        //         scheduler.shutdownNow();
        //     }
        // }));
    }

    private static void update(boolean post) {
        if (!JsonStorage.isLoaded() || !YoutubeData.isLoaded() || !Bot.isLoaded()) return;

        try {
            List<String> newVideoIds = checkData();
            List<Media> newVideoMedia = new ArrayList<>();

            for (String videoId : newVideoIds) {
                newVideoMedia.add(JsonStorage.get().getMediaById(videoId));
            }
            for (Media media : newVideoMedia) {
                if (post && !newVideoIds.isEmpty()) Bot.sendEmbed(media);
            }

            // List<String> StreamIds = checkStreamData();
            // Discord.updateMessage(); // finish this method if needed
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    private static List<String> checkData() {
        List<String> newVideoIds = new ArrayList<>();
        if (!JsonStorage.isLoaded() || !YoutubeData.isLoaded() || !Bot.isLoaded()) return newVideoIds;

        try {
            String lowestId = UpdateData.getLowestChannelId();
            if (lowestId == null) return newVideoIds;
            newVideoIds = YoutubeData.check(YoutubeData.getYoutubeData(lowestId), lowestId);
            YoutubeData.saveVideos(newVideoIds, lowestId);
            UpdateData.updateQueAndCooldown();
            return newVideoIds;
        } catch (Exception e) {
            System.err.println(e);
            return newVideoIds;
        }
    }

    private static List<String> checkStreamData() {
        List<String> streamIds = new ArrayList<>();
        if (!JsonStorage.isLoaded() || !YoutubeData.isLoaded() || !Bot.isLoaded()) return streamIds;

        try {
            for (Channels channel : JsonStorage.get().getChannels()) {
                String channelId = channel.getChannelId();
                // streamIds = YoutubeData.checkStream(YoutubeData.getYoutubeData(channelId), channelId);
                YoutubeData.saveVideos(streamIds, channelId);
                UpdateData.updateQueAndCooldown();
            }
        } catch (Exception e) {
            System.err.println(e);
            return streamIds;
        }
        return streamIds;
    }
}
