package dev.m3v;

import java.io.PrintWriter;
import java.io.StringWriter;
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

    public static boolean isLoaded() {
        return jdaClient != null;
    }

    public static void updateMessage(List<FromJson.LiveStream> streams) {
        if (streams == null || streams.isEmpty()) return;
        for (FromJson.LiveStream stream : streams) {

        }
    }

    public static void sendMessage(List<String> ids) {
        if (ids == null || ids.isEmpty()) return;
        for (String mediaId : ids) {
            if (mediaId == null || mediaId.isBlank()) continue;

            FromJson.CheckData checkData = null;
            if (FromJson.get().getCheckDataHistory() != null) {
                checkData = FromJson.get().getCheckDataHistory().stream()
                        .filter(cd -> cd != null && mediaId.equals(cd.getMediaId()))
                        .findFirst()
                        .orElse(null);
            }

            FromJson.LiveStream liveStream = null;
            if (FromJson.get().getLiveStreams() != null) {
                liveStream = FromJson.get().getLiveStreams().stream()
                        .filter(ls -> ls != null && mediaId.equals(ls.getMediaId()))
                        .findFirst()
                        .orElse(null);
            }

            if (checkData != null) {
                if (checkData.getIsVod()) {
                    vodAnouncement(mediaId);
                } else {
                    videoAnouncement(mediaId);
                }
            } else if (liveStream != null) {
                liveAnouncement(mediaId);
            }
        }
    }

    public static void vodAnouncement(String mediaId) {
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
        
        channel.sendMessageEmbeds(streamEndEmbed.build()).queue(
            message -> saveMessageId(mediaId, message.getId()),
            error -> sendError("vodAnouncement: saving message id", error.getMessage(), null)
        );
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

        channel.sendMessageEmbeds(liveEmbed.build()).queue(
            message -> saveMessageId(mediaId, message.getId()),
            error -> sendError("liveAnouncement: saving message id", error.getMessage(), null)
        );
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

        channel.sendMessageEmbeds(premierEmbed.build()).queue(
            message -> saveMessageId(mediaId, message.getId()),
            error -> sendError("primierAnouncement: saving message id", error.getMessage(), null)
        );
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
        channel.sendMessageEmbeds(videoEmbed.build()).queue(
            message -> saveMessageId(mediaId, message.getId()),
            error -> sendError("videoAnouncement: saving message id", error.getMessage(), null)
        );
    }

    public static void sendError(String location, String errorMessage, Throwable t) {
        String error;
        if (errorMessage == null) {
            StringWriter stringWriter = new StringWriter();
            t.printStackTrace(new PrintWriter(stringWriter));
            error = stringWriter.toString();
        } else {
            error = errorMessage;
        }
        EmbedBuilder errorEmbed = EmbedTemplates.createErrorEmbed(location, error);
        String discordChannelId = FromJson.get().getConfigOptions().getPremiereChannelId();
        String adminId = FromJson.get().getConfigOptions().getAdminId();
        MessageChannel channel = jdaClient.getTextChannelById(discordChannelId);

        if (channel == null) return;

        channel.sendMessage(String.format("<@&%s>", adminId)).queue();
        channel.sendMessageEmbeds(errorEmbed.build()).queue();
    }

    private static void saveMessageId(String mediaId, String messageId) {
        if (mediaId == null || messageId == null) return;
        boolean updated = false;

        List<FromJson.CheckData> checks = FromJson.get().getCheckDataHistory();
        if (checks != null) {
            for (FromJson.CheckData cd : checks) {
                if (cd != null && mediaId.equals(cd.getMediaId())) {
                    cd.setMessageId(messageId);
                    updated = true;
                    break;
                }
            }
        }

        if (!updated) {
            List<FromJson.LiveStream> lives = FromJson.get().getLiveStreams();
            if (lives != null) {
                for (FromJson.LiveStream ls : lives) {
                    if (ls != null && mediaId.equals(ls.getMediaId())) {
                        ls.setMessageId(messageId);
                        updated = true;
                        break;
                    }
                }
            }
        }

        if (updated) {
            FromJson.save();
        }
    }
}