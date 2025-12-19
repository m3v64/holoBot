package dev.m3v.discord;

import net.dv8tion.jda.api.EmbedBuilder;
import java.awt.Color;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import dev.m3v.data.model.*;

public class EmbedTemplates {
    public static DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static EmbedBuilder createStreamEmbed(MediaData mediaData) {
        String channelName = mediaData.getChannelName();
        String channelUrl = mediaData.getChannelUrl();
        String title = mediaData.getTitle();
        String description = mediaData.getDescription();
        String mediaUrl = mediaData.getMediaUrl();
        String thumbnailUrl = mediaData.getThumbnailUrl();
        int averageViewers = mediaData.getAvgViewers();
        int peakViewers = mediaData.getPeakViewers();
        String startTime = mediaData.getStartTime();

        EmbedBuilder embed = new EmbedBuilder();

        embed.setColor(new Color(255, 0, 0));
        embed.setAuthor(channelName + " was live. 🔴", channelUrl);
        embed.setDescription(String.format("""
            ### [%s](%s)
            %s
            """, title, mediaUrl, description));

        embed.addField("Viewers", averageViewers + " avg / " + peakViewers + " peak", false);

        embed.setImage(thumbnailUrl);
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(startTime);
        embed.setFooter("Live on YouTube since • " + zonedDateTime.format(timeFormat), "https://pingcord.xyz/assets/youtube-footer.png");
        embed.setTimestamp(Instant.now());

        return embed;
    }

    public static EmbedBuilder createVodEmbed(MediaData mediaData) {
        String channelName = mediaData.getChannelName();
        String channelUrl = mediaData.getChannelUrl();
        String title = mediaData.getTitle();
        String mediaUrl = mediaData.getMediaUrl();
        String thumbnailUrl = mediaData.getThumbnailUrl();
        String duration = mediaData.getDuration();
        int averageViewers = mediaData.getAvgViewers();
        int peakViewers = mediaData.getPeakViewers();
        String endTime = mediaData.getEndTime();

        EmbedBuilder embed = new EmbedBuilder();

        embed.setColor(new Color(135, 0, 0));
        embed.setAuthor(channelName + " was live.", channelUrl);
        embed.setDescription(String.format("""
            ### [%s](%s)
            Video available: [%s]
            """, title, mediaUrl, duration));

        embed.addField("Viewers", averageViewers + " avg / " + peakViewers + " peak", false);

        embed.setThumbnail(thumbnailUrl);
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(endTime);
        embed.setFooter("Stream ended • " + zonedDateTime.format(timeFormat), "https://pingcord.xyz/assets/youtube-footer.png");
        embed.setTimestamp(Instant.now());

        return embed;
    }

    public static EmbedBuilder createVideoEmbed(MediaData mediaData) {
        String channelName = mediaData.getChannelName();
        String channelUrl = mediaData.getChannelUrl();
        String title = mediaData.getTitle();
        String mediaUrl = mediaData.getMediaUrl();
        String thumbnailUrl = mediaData.getThumbnailUrl();
        String duration = mediaData.getDuration();
        int views = mediaData.getViews();
        String endTime = mediaData.getEndTime();
        EmbedBuilder embed = new EmbedBuilder();

        embed.setColor(new Color(255, 144, 0));
        embed.setAuthor(channelName + " premiered a new video on YouTube!", channelUrl);
        embed.setDescription(String.format("""
            ### [%s](%s)
            Video available: [%s]
            """, title, mediaUrl, duration));

        embed.addField("Views", views + "", false);

        embed.setImage(thumbnailUrl);
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(endTime);
        embed.setFooter("Video Released • " + zonedDateTime.format(timeFormat), "https://pingcord.xyz/assets/youtube-footer.png");
        embed.setTimestamp(Instant.now());

        return embed;
    }

    public static EmbedBuilder createPremierEmbed(MediaData mediaData) {
        String channelName = mediaData.getChannelName();
        String channelUrl = mediaData.getChannelUrl();
        String title = mediaData.getTitle();
        String mediaUrl = mediaData.getMediaUrl();
        String thumbnailUrl = mediaData.getThumbnailUrl();
        String startTime = mediaData.getStartTime();

        EmbedBuilder embed = new EmbedBuilder();

        embed.setColor(new Color(40, 40, 40));
        embed.setAuthor(channelName + " is premiering a new video on YouTube!", channelUrl);
        embed.setDescription(String.format("""
            ### [%s](%s)
            """, title, mediaUrl));

        ZonedDateTime zonedDateTime = ZonedDateTime.parse(startTime);
        long epochSeconds = zonedDateTime.toEpochSecond();
        if (epochSeconds != 0) ;
        
        String rel = Objects.requireNonNull(String.format("<t:%s:R>", epochSeconds));
        embed.addField("Releases in: ", rel, false);

        embed.setImage(thumbnailUrl);
        embed.setFooter("Video Releases • " + zonedDateTime.format(timeFormat), "https://pingcord.xyz/assets/youtube-footer.png");
        embed.setTimestamp(Instant.now());

        return embed;
    }

    public static EmbedBuilder createErrorEmbed(String location, String errorMessage) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(new Color(255, 0, 0));
        embed.setAuthor(location);
        embed.setDescription(errorMessage);
        embed.setTimestamp(Instant.now());
        return embed;
    }
}
