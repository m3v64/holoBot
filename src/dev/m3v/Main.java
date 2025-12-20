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
                Log.error("Unit test threw an exception", Main.class, e);
            }
            shutdown(null, 0);
        }

        try {
            Log.load();
            JsonStorage.load();
            Bot.initiateBot();
            Client.initialize();
        } catch (Exception e) {
            Log.error("Holobot failed to initialize", Main.class, e);
            shutdown(null, 1);
        }

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

        scheduler.scheduleAtFixedRate(() -> {

        }, 0, 60, TimeUnit.SECONDS);

        Thread.setDefaultUncaughtExceptionHandler((thread, e) -> {
            Log.error(("Uncaught exception in thread " + thread.getName()), Main.class, e);
            shutdown(scheduler, 1);
        });
    }

    public static void shutdown(ScheduledExecutorService scheduler, Integer exitCode) {
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
                Log.warn("Scheduler shutdown command was ignored, scheduler is likely not initialized", Main.class, ignored);
            }

            try {
                JsonStorage.save();
            } catch (Exception ignored) {
                Log.error("Save Json command was ignored", Main.class, ignored);
            }

            try {
                dev.m3v.discord.Bot.shutdown();
            } catch (Exception ignored) {
                Log.warn("Bot shutdown command was ignored, Bot is likely not initialized", Main.class, ignored);
            }
        };

        if (scheduler != null) {
            Runtime.getRuntime().addShutdownHook(new Thread(cleanup));
        }

        cleanup.run();
        Log.warn("Failed to shutdown cleanly, forcing shutdown", Main.class, null);
        if (exitCode != null) System.exit(exitCode);
        else System.exit(1);
    }
}
