package dev.m3v;

import java.util.ArrayList;
import java.util.List;

// import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        try {
            FromJson.load();
            // Discord.initiateBot();
            YoutubeData.initialize();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to initialize holoBot. Exiting.");
            System.exit(1);
        }

        List<String> newVideoIds = new ArrayList<>();
        try {
            String lowestId = FromJson.getLowestChannelId();
            System.out.println("[holoBot] Starting YouTube check...");
            newVideoIds = YoutubeData.check(lowestId);
            System.out.printf("[holoBot] Channel chosen: %s%n", lowestId);
            System.out.printf("[holoBot] New video IDs fetched: %d%n", newVideoIds.size());
            if (!newVideoIds.isEmpty()) {
                System.out.println("[holoBot] Sample IDs: " + newVideoIds.subList(0, Math.min(5, newVideoIds.size())));
            }
            YoutubeData.saveVideos(newVideoIds, lowestId);
            FromJson.updateQue();
            System.out.printf("[holoBot] New video IDs saved to json, currently saved: %s/100%n", (FromJson.get().getCheckData().size()));
            System.out.println("[holoBot] Save complete. Exiting.");
        } catch (Throwable e) {
            System.err.println("[holoBot] ERROR during test run: " + e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        
    //     ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    //     Runnable checkYouTubeTask = () -> {
    //         try {
    //             String lowestId = null;
    //             long lowestTime = Long.MAX_VALUE;

    //             for (String channelId : FromJson.youtubeIds) {
    //                 long lastCheck = FromJson.lastCheckTime(channelId);
    //                 if (lastCheck < lowestTime) {
    //                     lowestTime = lastCheck;
    //                     lowestId = channelId;
    //                 }
    //             }

    //             if (lowestId != null) {
    //                 String latestVideo = Youtube.getLatest(lowestId);
    //                 String lastKnown = FromJson.getVideoId();

    //                 if (!latestVideo.equals(lastKnown)) {
    //                     Discord.sendMessage(latestVideo);
    //                     FromJson.updateLastCheck(lowestId, latestVideo);
    //                     FromJson.save();
    //                 }

    //                 FromJson.updateLastCheckTime(lowestId, System.currentTimeMillis());
    //             }

    //         } catch (Exception e) {
    //             e.printStackTrace();
    //             Discord.sendError("Error in scheduled task: " + e.getMessage());
    //         }
    //     };
        
    //     // (Reminder!) random delay is temporary (Option C (best long-term): Split the task per channel using a small thread pool so each one has its own timer.)
    //     scheduler.scheduleAtFixedRate(checkYouTubeTask, 0, 60, TimeUnit.SECONDS);

    //     Runtime.getRuntime().addShutdownHook(new Thread(() -> {
    //         System.out.println("Shutting down holoBot...");
    //         scheduler.shutdown();
    //         try {
    //             if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
    //                 scheduler.shutdownNow();
    //             }
    //         } catch (InterruptedException e) {
    //             scheduler.shutdownNow();
    //         }
    //     }));
    }
}
