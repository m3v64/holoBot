package dev.m3v;

import java.util.concurrent.*;

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
            Update.run();
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
}
