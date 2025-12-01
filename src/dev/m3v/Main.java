package dev.m3v;

import java.util.concurrent.*;
import java.util.List;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Starting holoBot...");
        try {
            FromJson.load();
            Discord.initiateBot();
            YoutubeData.initialize();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to initialize holoBot. Exiting.");
            System.exit(1);
        }

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

        Runnable checkYouTubeTask = () -> {
            try {
                checkStreamData();
                update(true);
            } catch (Exception e) {
                Discord.sendError("Main check task loop", null, e);
            }
        };

        scheduler.scheduleAtFixedRate(checkYouTubeTask, 0, 60, TimeUnit.SECONDS);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down holoBot...");
            scheduler.shutdown();
            try {
                if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                    scheduler.shutdownNow();
                }
            } catch (InterruptedException e) {
                scheduler.shutdownNow();
            }
        }));
    }

    private static void update(boolean post) {
        if (!FromJson.isLoaded() || !YoutubeData.isLoaded() || !Discord.isLoaded()) return;

        try {
            List<String> newVideoIds = checkData();
            if (post && !newVideoIds.isEmpty()) Discord.sendMessage(newVideoIds);

            // List<String> StreamIds = checkStreamData();
            // Discord.updateMessage(); // finish this method if needed
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    private static List<String> checkData() {
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

    private static List<String> checkStreamData() {
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
