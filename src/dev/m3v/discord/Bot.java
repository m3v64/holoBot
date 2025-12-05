package dev.m3v.discord;

import javax.security.auth.login.LoginException;

import dev.m3v.data.*;
import dev.m3v.data.model.*;

import net.dv8tion.jda.api.*;

public class Bot {
    private static JDA jdaClient;

    public static void initiateBot() throws LoginException, InterruptedException {
        String discordApiKey = JsonStorage.get().getSecrets().getDiscord_api_key();
        jdaClient = JDABuilder.createDefault(discordApiKey).build();
        jdaClient.awaitReady();
    }

    public static boolean isLoaded() {
        return jdaClient != null;
    }

    public static void sendEmbed(MediaData mediaData) {
        
    }
}
