package dev.m3v;

import java.util.List;
import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;

public class Discord {
    private static JDA jdaClient;

    public static void initiateBot() throws LoginException, InterruptedException {
        String discordApiKey = FromJson.get().getSecrets().getDiscord_api_key();
        jdaClient = JDABuilder.createDefault(discordApiKey).build();
        jdaClient.awaitReady();
    }

    public static void sendMessage(List<String> ids) {
        for (String id : ids) {
            
        }
    }

    public static void streamAnouncement(String mediaId) {
        if (mediaId == null || mediaId.isBlank()) return;

        String discordChannelId = FromJson.get().getConfigOptions().getVideoChannelId();
        MessageChannel channel = jdaClient.getTextChannelById(discordChannelId);
        if (channel == null) {
            return;
        }
        FromJson.Data streamData = null;

        var liveStreamsList = FromJson.get().getLiveStreams();
        if (liveStreamsList != null) {
            for (FromJson.LiveStream stream : liveStreamsList) {
                if (mediaId.equals(stream.getMediaId())) {
                    streamData = stream.getData();
                    break;
                }
            }
        }

        if (streamData == null) {
            var checkHistoryList = FromJson.get().getCheckDataHistory();
            if (checkHistoryList != null) {
                for (FromJson.CheckData check : checkHistoryList) {
                    if (mediaId.equals(check.getMediaId())) {
                        streamData = check.getData();
                        break;
                    }
                }
            }
        }

        if (streamData == null) return;

        EmbedBuilder streamEndEmbed = EmbedTemplates.createEndedStreamEmbed(
            streamData.getChannelName(),
            streamData.getChannelUrl(),
            streamData.getTitle(),
            streamData.getMediaUrl(),
            streamData.getThumbnailUrl(),
            streamData.getDuration(),
            streamData.getAvgViewers(),
            streamData.getPeakViewers(),
            streamData.getEndTime()
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
        MessageChannel channel = jdaClient.getTextChannelById(discordChannelId);
        if (channel == null) return;
        FromJson.Data streamData = null;

        var liveStreamsList = FromJson.get().getLiveStreams();
        if (liveStreamsList != null) {
            for (FromJson.LiveStream stream : liveStreamsList) {
                if (mediaId.equals(stream.getMediaId())) {
                    streamData = stream.getData();
                    break;
                }
            }
        }

        if (streamData == null) {
            var checkHistoryList = FromJson.get().getCheckDataHistory();
            if (checkHistoryList != null) {
                for (FromJson.CheckData check : checkHistoryList) {
                    if (mediaId.equals(check.getMediaId())) {
                        streamData = check.getData();
                        break;
                    }
                }
            }
        }

        if (streamData == null) return;

        EmbedBuilder liveEmbed = EmbedTemplates.createLiveEmbed(
            streamData.getChannelName(),
            streamData.getChannelUrl(),
            streamData.getTitle(),
            streamData.getDescription(),
            streamData.getMediaUrl(),
            streamData.getThumbnailUrl(),
            streamData.getAvgViewers(),
            streamData.getPeakViewers(),
            streamData.getStartTime()
        );

        String roleId = FromJson.get().getChannels(discordChannelId).getRoleId();
        if (roleId != null && !roleId.isBlank()) {
            channel.sendMessage(String.format("<@&%s>", roleId)).queue();
        }
        channel.sendMessageEmbeds(liveEmbed.build()).queue();
    }

    public static void primierAnouncement(String mediaId) {
        if (mediaId == null || mediaId.isBlank()) return;

        String discordChannelId = FromJson.get().getConfigOptions().getPremiereChannelId();
        MessageChannel channel = jdaClient.getTextChannelById(discordChannelId);
        if (channel == null) return;
        FromJson.Data streamData = null;

        var checkHistoryList = FromJson.get().getCheckDataHistory();
        if (checkHistoryList != null) {
            for (FromJson.CheckData check : checkHistoryList) {
                if (mediaId.equals(check.getMediaId())) {
                    streamData = check.getData();
                    break;
                }
            }
        }

        if (streamData == null) return;

        EmbedBuilder premierEmbed = EmbedTemplates.createPremierEmbed(
            streamData.getChannelName(),
            streamData.getChannelUrl(),
            streamData.getTitle(),
            streamData.getMediaUrl(),
            streamData.getThumbnailUrl(),
            streamData.getStartTime()
        );

        channel.sendMessageEmbeds(premierEmbed.build()).queue();
    }

    public static void videoAnouncement(String mediaId) {
        if (mediaId == null || mediaId.isBlank()) return;

        String discordChannelId = FromJson.get().getConfigOptions().getPremiereChannelId();
        MessageChannel channel = jdaClient.getTextChannelById(discordChannelId);
        if (channel == null) return;
        FromJson.Data streamData = null;

        var checkHistoryList = FromJson.get().getCheckDataHistory();
        if (checkHistoryList != null) {
            for (FromJson.CheckData check : checkHistoryList) {
                if (mediaId.equals(check.getMediaId())) {
                    streamData = check.getData();
                    break;
                }
            }
        }

        if (streamData == null) {
            var liveStreamsList = FromJson.get().getLiveStreams();
            if (liveStreamsList != null) {
                for (FromJson.LiveStream stream : liveStreamsList) {
                    if (mediaId.equals(stream.getMediaId())) {
                        streamData = stream.getData();
                        break;
                    }
                }
            }
        }

        if (streamData == null) return;

        EmbedBuilder videoEmbed = EmbedTemplates.createVideoEmbed(
            streamData.getChannelName(),
            streamData.getChannelUrl(),
            streamData.getTitle(),
            streamData.getMediaUrl(),
            streamData.getThumbnailUrl(),
            streamData.getDuration(),
            streamData.getViews(),
            streamData.getEndTime()
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
    MessageChannel channel = jdaClient.getTextChannelById(discordChannelId);

        if (channel == null) return;

        channel.sendMessage(String.format("<@&%s>", adminId)).queue();
        channel.sendMessageEmbeds(errorEmbed.build()).queue();
    }
}