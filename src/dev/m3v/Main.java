package dev.m3v;

import java.util.concurrent.*;
import dev.m3v.discord.*;
import dev.m3v.youtube.*;
import dev.m3v.data.*;

public class Main {
    public static void main(String[] args) {
        boolean dev_mode = true;

        if (dev_mode) {
            try {
                UnitTests.test();
            } catch (Exception e) {
                Log.log("ERROR", "Test threw an exception", Main.class, e);
            }
            System.exit(0);
        }

        try {
            Log.load();
            JsonStorage.load();
            Bot.initiateBot();
            Client.initialize();
        } catch (Exception e) {
            e.printStackTrace();
            shutdown(null);
        }

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

        scheduler.scheduleAtFixedRate(() -> {

        }, 0, 60, TimeUnit.SECONDS);

        Thread.setDefaultUncaughtExceptionHandler((thread, e) -> {
            Log.log("ERROR", ("Uncaught exception in thread " + thread.getName()), Main.class, e);
            shutdown(scheduler);
        });
    }

    public static void shutdown(ScheduledExecutorService scheduler) {
        Runnable cleanup = () -> {
            try {
                if (scheduler != null) {
                    scheduler.shutdown();
                    try {
                        if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                            scheduler.shutdownNow();
                        }
                    } catch (InterruptedException e) {
                        scheduler.shutdownNow();
                    }
                }
            } catch (Exception ignored) {
                Log.log("WARN", "Scheduler shutdown command was ignored, scheduler is likely not initialized", Main.class, ignored);
            }

            try {
                JsonStorage.save();
            } catch (Exception ignored) {
                Log.log("ERROR", "Save Json command was ignored", Main.class, ignored);
            }

            try {
                dev.m3v.discord.Bot.shutdown();
            } catch (Exception ignored) {
                Log.log("WARN", "Bot shutdown command was ignored, Bot is likely not initialized", Main.class, ignored);
            }
        };

        if (scheduler != null) {
            Runtime.getRuntime().addShutdownHook(new Thread(cleanup));
        }

        cleanup.run();
        Log.log("ERROR", "Failed to shutdown cleanly, forcing shutdown", Main.class, null);
        System.exit(1);
    }
}
