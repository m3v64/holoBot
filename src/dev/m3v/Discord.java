package dev.m3v;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;

public class Discord {
    private static JDA api;

    public static void initiateBot() throws LoginException, InterruptedException {
        String discordApiKey = FromJson.get().getSecrets().getDiscord_api_key();
        api = JDABuilder.createDefault(discordApiKey).build();
        api.awaitReady();
    }

    public static void streamAnouncement(String mediaId) {
        if (mediaId == null || mediaId.isBlank()) return;

        String discordChannelId = FromJson.get().getConfigOptions().getVideoChannelId();
        MessageChannel channel = api.getTextChannelById(discordChannelId);
        if (channel == null) {
            return;
        }

        FromJson.Data d = null;

        var streams = FromJson.get().getLiveStreams();
        if (streams != null) {
            for (FromJson.LiveStream s : streams) {
                if (mediaId.equals(s.getMediaId())) {
                    d = s.getData();
                    break;
                }
            }
        }

        if (d == null) {
            var checks = FromJson.get().getCheckDataHistory();
            if (checks != null) {
                for (FromJson.CheckData c : checks) {
                    if (mediaId.equals(c.getMediaId())) {
                        d = c.getData();
                        break;
                    }
                }
            }
        }

        if (d == null) return;

        EmbedBuilder streamEndEmbed = EmbedTemplates.createEndedStreamEmbed(
            d.getChannelName(),
            d.getChannelurl(),
            d.getTitle(),
            d.getMediaUrl(),
            d.getThumbnailUrl(),
            d.getDuration(),
            d.getAvgViewers(),
            d.getPeakViewers(),
            d.getEndTime()
        );

        String roleId = FromJson.get().getChannels(discordChannelId).getRoleId();
        if (roleId != null && !roleId.isBlank()) {
            channel.sendMessage(String.format("<@&%s>", roleId)).queue();
        }
        channel.sendMessageEmbeds(streamEndEmbed.build()).queue();
    }

    public static void liveAnouncement(String mediaId) {
        if (mediaId == null || mediaId.isBlank()) return;

        String discordChannelId = FromJson.get().getConfigOptions().getVideoChannelId();
        MessageChannel channel = api.getTextChannelById(discordChannelId);
        if (channel == null) return;

        FromJson.Data d = null;

        var streams = FromJson.get().getLiveStreams();
        if (streams != null) {
            for (FromJson.LiveStream s : streams) {
                if (mediaId.equals(s.getMediaId())) {
                    d = s.getData();
                    break;
                }
            }
        }

        if (d == null) {
            var checks = FromJson.get().getCheckDataHistory();
            if (checks != null) {
                for (FromJson.CheckData c : checks) {
                    if (mediaId.equals(c.getMediaId())) {
                        d = c.getData();
                        break;
                    }
                }
            }
        }

        if (d == null) return;

        EmbedBuilder liveEmbed = EmbedTemplates.createLiveEmbed(
            d.getChannelName(),
            d.getChannelurl(),
            d.getTitle(),
            d.getDescription(),
            d.getMediaUrl(),
            d.getThumbnailUrl(),
            d.getAvgViewers(),
            d.getPeakViewers(),
            d.getStartTime()
        );

        String roleId = FromJson.get().getChannels(discordChannelId).getRoleId();
        if (roleId != null && !roleId.isBlank()) {
            channel.sendMessage(String.format("<@&%s>", roleId)).queue();
        }
        channel.sendMessageEmbeds(liveEmbed.build()).queue();
    }

    public static void videoAnouncement(String mediaId) {
        if (mediaId == null || mediaId.isBlank()) return;

        String discordChannelId = FromJson.get().getConfigOptions().getPremiereChannelId();
        MessageChannel channel = api.getTextChannelById(discordChannelId);
        if (channel == null) return;

        FromJson.Data d = null;

        var checks = FromJson.get().getCheckDataHistory();
        if (checks != null) {
            for (FromJson.CheckData c : checks) {
                if (mediaId.equals(c.getMediaId())) {
                    d = c.getData();
                    break;
                }
            }
        }

        if (d == null) {
            var streams = FromJson.get().getLiveStreams();
            if (streams != null) {
                for (FromJson.LiveStream s : streams) {
                    if (mediaId.equals(s.getMediaId())) {
                        d = s.getData();
                        break;
                    }
                }
            }
        }

        if (d == null) return;

        EmbedBuilder videoEmbed = EmbedTemplates.createVideoEmbed(
            d.getChannelName(),
            d.getChannelurl(),
            d.getTitle(),
            d.getMediaUrl(),
            d.getThumbnailUrl(),
            d.getDuration(),
            d.getViews(),
            d.getEndTime()
        );

        String roleId = FromJson.get().getChannels(discordChannelId).getRoleId();
        if (roleId != null && !roleId.isBlank()) {
            channel.sendMessage(String.format("<@&%s>", roleId)).queue();
        }
        channel.sendMessageEmbeds(videoEmbed.build()).queue();
    }

    public static void sendError(String location, String errorMessage) {
        EmbedBuilder errorEmbed = EmbedTemplates.createErrorEmbed(location, errorMessage);
        String discordChannelId = FromJson.get().getConfigOptions().getPremiereChannelId();
        String adminId = FromJson.get().getConfigOptions().getAdminId();
        MessageChannel channel = api.getTextChannelById(discordChannelId);

        if (channel == null) return;

        channel.sendMessage(String.format("<@&%s>", adminId)).queue();
        channel.sendMessageEmbeds(errorEmbed.build()).queue();
    }
}