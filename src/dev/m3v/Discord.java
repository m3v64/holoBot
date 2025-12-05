package dev.m3v;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Objects;
import javax.security.auth.login.LoginException;

import dev.m3v.data.*;
import dev.m3v.data.model.*;
import dev.m3v.discord.*;

import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;

public class Discord {
    private static JDA jdaClient;

    public static void initiateBot() throws LoginException, InterruptedException {
        String discordApiKey = JsonStorage.get().getSecrets().getDiscord_api_key();
        jdaClient = JDABuilder.createDefault(discordApiKey).build();
        jdaClient.awaitReady();
    }

    public static void updateMessage(List<String> streamIds) {

    }

    public static void sendMessage(List<String> ids) {
        if (ids == null || ids.isEmpty()) return;
        for (String mediaId : ids) {
            if (mediaId == null || mediaId.isBlank()) continue;

            Memory checkData = null;
            if (JsonStorage.get().getMemoryCache() != null) {
                checkData = JsonStorage.get().getMemoryCache().stream()
                        .filter(cd -> cd != null && mediaId.equals(cd.getMediaId()))
                        .findFirst()
                        .orElse(null);
            }

            LiveStreams liveStream = null;
            if (JsonStorage.get().getLiveStreams() != null) {
                liveStream = JsonStorage.get().getLiveStreams().stream()
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

        String discordChannelId = JsonStorage.get().getConfigOptions().getVideoChannelId();
        if (discordChannelId == null) return;
        MessageChannel channel = jdaClient.getTextChannelById(discordChannelId);
        MediaData streamData = null;

        var liveStreamsList = JsonStorage.get().getLiveStreams();
        if (liveStreamsList != null) {
            for (LiveStreams stream : liveStreamsList) {
                if (mediaId.equals(stream.getMediaId())) {
                    streamData = stream.getData();
                    break;
                }
            }
        }

        if (streamData == null) {
            var MemoryCache = JsonStorage.get().getMemoryCache();
            if (MemoryCache != null) {
                for (Memory check : MemoryCache) {
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

        if (channel == null || discordChannelId == null) return;
        String roleId = Objects.requireNonNull(JsonStorage.get().getChannels(discordChannelId).getRoleId(), "roleId must not be null");
        String mention = "<@&" + roleId + ">";
        channel.sendMessage(mention).queue();
        channel.sendMessageEmbeds(streamEndEmbed.build()).queue(
            message -> saveMessageId(mediaId, message.getId()),
            error -> sendError("vodAnouncement: saving message id", error.getMessage(), null)
        );
    }

    public static void liveAnouncement(String mediaId) {
        if (mediaId == null || mediaId.isBlank()) return;

        String discordChannelId = JsonStorage.get().getConfigOptions().getVideoChannelId();
        if (discordChannelId == null) return;
        MessageChannel channel = jdaClient.getTextChannelById(discordChannelId);
        MediaData streamData = null;

        var liveStreamsList = JsonStorage.get().getLiveStreams();
        if (liveStreamsList != null) {
            for (LiveStreams stream : liveStreamsList) {
                if (mediaId.equals(stream.getMediaId())) {
                    streamData = stream.getData();
                    break;
                }
            }
        }

        if (streamData == null) {
            var MemoryCache = JsonStorage.get().getMemoryCache();
            if (MemoryCache != null) {
                for (Memory check : MemoryCache) {
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

        if (channel == null || discordChannelId == null) return;
        String roleId = Objects.requireNonNull(JsonStorage.get().getChannels(discordChannelId).getRoleId(), "roleId must not be null");
        String mention = "<@&" + roleId + ">";
        channel.sendMessage(mention).queue();
        channel.sendMessageEmbeds(liveEmbed.build()).queue(
            message -> saveMessageId(mediaId, message.getId()),
            error -> sendError("liveAnouncement: saving message id", error.getMessage(), null)
        );
    }

    public static void primierAnouncement(String mediaId) {
        if (mediaId == null || mediaId.isBlank()) return;

        String discordChannelId = JsonStorage.get().getConfigOptions().getPremiereChannelId();
        if (discordChannelId == null) return;
        MessageChannel channel = jdaClient.getTextChannelById(discordChannelId);
        if (channel == null) return;
        MediaData streamData = null;

        var MemoryCache = JsonStorage.get().getMemoryCache();
        if (MemoryCache != null) {
            for (Memory check : MemoryCache) {
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

        String discordChannelId = JsonStorage.get().getConfigOptions().getPremiereChannelId();
        if (discordChannelId == null) return;
        MessageChannel channel = jdaClient.getTextChannelById(discordChannelId);
        MediaData streamData = null;

        var MemoryCache = JsonStorage.get().getMemoryCache();
        if (MemoryCache != null) {
            for (Memory check : MemoryCache) {
                if (mediaId.equals(check.getMediaId())) {
                    streamData = check.getData();
                    break;
                }
            }
        }

        if (streamData == null) {
            var liveStreamsList = JsonStorage.get().getLiveStreams();
            if (liveStreamsList != null) {
                for (LiveStreams stream : liveStreamsList) {
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

        if (channel == null || discordChannelId == null) return;
        String roleId = Objects.requireNonNull(JsonStorage.get().getChannels(discordChannelId).getRoleId(), "roleId must not be null");
        String mention = "<@&" + roleId + ">";
        channel.sendMessage(mention).queue();
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
        String discordChannelId = JsonStorage.get().getConfigOptions().getPremiereChannelId();
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
        boolean updated = false;

        List<Memory> checks = JsonStorage.get().getMemoryCache();
        if (checks != null) {
            for (Memory cd : checks) {
                if (cd != null && mediaId.equals(cd.getMediaId())) {
                    cd.setMessageId(messageId);
                    updated = true;
                    break;
                }
            }
        }

        if (!updated) {
            List<LiveStreams> lives = JsonStorage.get().getLiveStreams();
            if (lives != null) {
                for (LiveStreams ls : lives) {
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