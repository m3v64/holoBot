package dev.m3v;

// import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        try {
            FromJson.load();
            Discord.initiateBot();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to initialize holoBot. Exiting.");
            System.exit(1);
        }

        // Discord.streamAnouncement();
        Thread.sleep(6000);
        System.exit(0);

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
