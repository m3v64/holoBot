package dev.m3v.discord;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Objects;

import javax.security.auth.login.LoginException;

import dev.m3v.data.*;
import dev.m3v.data.model.*;
import dev.m3v.Log;

import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;

public class Bot {
    private static JDA jdaClient;

    public static void initiateBot() throws LoginException, InterruptedException {
        String discordApiKey = JsonStorage.get().getSecrets().getDiscord_api_key();
        Log.info("Initiating Discord bot...", Bot.class);
        jdaClient = JDABuilder.createDefault(discordApiKey).build();
        jdaClient.awaitReady();
        Log.info("Discord bot is ready", Bot.class);
    }

    public static void shutdown() {
        if (jdaClient != null) {
            try {
                Log.info("Shutting down Discord bot...", Bot.class);
                jdaClient.shutdown();
            } catch (Exception e) {
                Log.warn("Exception during bot shutdown", Bot.class, e);
            }
            jdaClient = null;
        }
    }

    public static boolean isLoaded() {
        return jdaClient != null;
    }

    public static void sendEmbed(Media media) {
        if (media == null) {
            Log.warn("sendEmbed called with null media", Bot.class, null);
            return;
        }
        String mediaChannelId = JsonStorage.get().getConfigOptions().getMediaChannelId();
        String streamChannelId = JsonStorage.get().getConfigOptions().getStreamChannelId();
        String premierChannelId = JsonStorage.get().getConfigOptions().getPremierChannelId();
        if (mediaChannelId == null || streamChannelId == null || premierChannelId == null) {
            Log.warn("Channel IDs not configured properly", Bot.class, null);
            return;
        }
        switch (media.getType()) {
            case "STREAM":
                Log.info("Sending stream embed for media: {}", Bot.class, media.getMediaId());
                EmbedBuilder streamEmbed = EmbedTemplates.createStreamEmbed(media.getData());
                MessageChannel streamChannel = jdaClient.getTextChannelById(streamChannelId);
                if (streamChannel == null || streamChannelId == null) return;
                String streamRoleId = Objects.requireNonNull(JsonStorage.get().getChannel(media.getChannelId()).getRoleId(), "roleId must not be null");
                String streamMention = "<@&" + streamRoleId + ">";
                streamChannel.sendMessage(streamMention).queue();
                streamChannel.sendMessageEmbeds(streamEmbed.build()).queue(
                    message -> saveMessageId(media.getMediaId(), message.getId()),
                    error -> sendError("streamAnouncement: saving message id", error.getMessage(), null)
                );
                break;
            case "VOD":
                Log.info("Sending VOD embed for media: {}", Bot.class, media.getMediaId());
                EmbedBuilder vodEmbed = EmbedTemplates.createVodEmbed(media.getData());
                MessageChannel vodChannel = jdaClient.getTextChannelById(mediaChannelId);
                if (vodChannel == null || mediaChannelId == null) return;
                String vodRoleId = Objects.requireNonNull(JsonStorage.get().getChannel(media.getChannelId()).getRoleId(), "roleId must not be null");
                String vodMention = "<@&" + vodRoleId + ">";
                vodChannel.sendMessage(vodMention).queue();
                vodChannel.sendMessageEmbeds(vodEmbed.build()).queue(
                    message -> saveMessageId(media.getMediaId(), message.getId()),
                    error -> sendError("vodAnouncement: saving message id", error.getMessage(), null)
                );
                break;
            case "PREMIER":
                Log.info("Sending premier embed for media: {}", Bot.class, media.getMediaId());
                EmbedBuilder premierEmbed = EmbedTemplates.createPremierEmbed(media.getData());
                MessageChannel premierChannel = jdaClient.getTextChannelById(premierChannelId);
                if (premierChannel == null || premierChannelId == null) return;
                String premierRoleId = Objects.requireNonNull(JsonStorage.get().getChannel(media.getChannelId()).getRoleId(), "roleId must not be null");
                String premierMention = "<@&" + premierRoleId + ">";
                premierChannel.sendMessage(premierMention).queue();
                premierChannel.sendMessageEmbeds(premierEmbed.build()).queue(
                    message -> saveMessageId(media.getMediaId(), message.getId()),
                    error -> sendError("premierAnouncement: saving message id", error.getMessage(), null)
                );
                break;
            case "VIDEO":
                Log.info("Sending video embed for media: {}", Bot.class, media.getMediaId());
                EmbedBuilder videoEmbed = EmbedTemplates.createVideoEmbed(media.getData());
                MessageChannel videoChannel = jdaClient.getTextChannelById(mediaChannelId);
                if (videoChannel == null || mediaChannelId == null) return;
                String videoRoleId = Objects.requireNonNull(JsonStorage.get().getChannel(media.getChannelId()).getRoleId(), "roleId must not be null");
                String videoMention = "<@&" + videoRoleId + ">";
                videoChannel.sendMessage(videoMention).queue();
                videoChannel.sendMessageEmbeds(videoEmbed.build()).queue(
                    message -> saveMessageId(media.getMediaId(), message.getId()),
                    error -> sendError("videoAnouncement: saving message id", error.getMessage(), null)
                );
                break;
            default:
                Log.warn("Unknown media type", Bot.class, new IllegalArgumentException("Media type: " + media.getType()));
                sendError("sendEmbed", "no type was found", null);
                break;
        }
    }

    public static void sendError(String location, String errorMessage, Throwable t) {
        if (t != null) {
            Log.error("Error in " + location + ": " + errorMessage, Bot.class, t);
        } else {
            Log.info("Error reported in {}: {}", Bot.class, location, errorMessage);
        }
        String error;
        if (errorMessage == null) {
            StringWriter stringWriter = new StringWriter();
            t.printStackTrace(new PrintWriter(stringWriter));
            error = stringWriter.toString();
        } else {
            error = errorMessage;
        }
        EmbedBuilder errorEmbed = EmbedTemplates.createErrorEmbed(location, error);
        String discordChannelId = JsonStorage.get().getConfigOptions().getPremierChannelId();
        if (discordChannelId == null) return;
        MessageChannel channel = jdaClient.getTextChannelById(discordChannelId);
        if (channel == null) return;

        String adminId = Objects.requireNonNull(JsonStorage.get().getConfigOptions().getAdminPingId(), "roleId must not be null");
        String mention = "<@&" + adminId + ">";
        channel.sendMessage(mention).queue();
        channel.sendMessageEmbeds(errorEmbed.build()).queue();
    }

    private static void saveMessageId(String mediaId, String messageId) {
        if (mediaId == null || messageId == null) return;
        Log.debug("Saving message ID {} for media {}", Bot.class, messageId, mediaId);
        boolean updated = false;

        List<Media> checks = JsonStorage.get().getMemory();
        if (checks != null) {
            for (Media cd : checks) {
                if (cd != null && mediaId.equals(cd.getMediaId())) {
                    cd.setMessageId(messageId);
                    updated = true;
                    break;
                }
            }
        }

        if (!updated) {
            List<Media> lives = JsonStorage.get().getLiveStreams();
            if (lives != null) {
                for (Media ls : lives) {
                    if (ls != null && mediaId.equals(ls.getMediaId())) {
                        ls.setMessageId(messageId);
                        updated = true;
                        break;
                    }
                }
            }
        }

        if (updated) {
            JsonStorage.save();
        }
    }
}
