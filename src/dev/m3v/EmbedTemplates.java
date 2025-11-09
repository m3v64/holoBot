package dev.m3v;

import net.dv8tion.jda.api.EmbedBuilder;
import java.awt.Color;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class EmbedTemplates {
    public static DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    public static EmbedBuilder createLiveEmbed(String channelName, String channelUrl, String title, String description, String streamUrl, String thumbnailUrl, int averageViewers, int peakViewers, String startTime) {
        EmbedBuilder embed = new EmbedBuilder();

        embed.setColor(new Color(255, 0, 0));
        embed.setAuthor(channelName + " was live. 🔴", channelUrl);
        embed.setDescription(String.format("""
            ### [%s](%s)
            %s
            """, title, streamUrl, description));

    embed.addField("Viewers", averageViewers + " avg / " + peakViewers + " peak", false);

        embed.setImage(thumbnailUrl);
    ZonedDateTime zonedDateTime = ZonedDateTime.parse(startTime);
    embed.setFooter("Live on YouTube since • " + zonedDateTime.format(timeFormat), "https://pingcord.xyz/assets/youtube-footer.png");
        embed.setTimestamp(Instant.now());

        return embed;
    }

    public static EmbedBuilder createEndedStreamEmbed(String channelName, String channelUrl, String title, String streamUrl, String thumbnailUrl, String duration, int averageViewers, int peakViewers, String endTime) {
        EmbedBuilder embed = new EmbedBuilder();

        embed.setColor(new Color(135, 0, 0));
        embed.setAuthor(channelName + " was live.", channelUrl);
        embed.setDescription(String.format("""
            ### [%s](%s)
            Video available: [%s]
            """, title, streamUrl, duration));

    embed.addField("Viewers", averageViewers + " avg / " + peakViewers + " peak", false);

        embed.setThumbnail(thumbnailUrl);
    ZonedDateTime zonedDateTime = ZonedDateTime.parse(endTime);
    embed.setFooter("Stream ended • " + zonedDateTime.format(timeFormat), "https://pingcord.xyz/assets/youtube-footer.png");
        embed.setTimestamp(Instant.now());

        return embed;
    }

    public static EmbedBuilder createVideoEmbed(String channelName, String channelUrl, String title, String videoUrl, String thumbnailUrl, String duration, int views, String endTime) {
        EmbedBuilder embed = new EmbedBuilder();

        embed.setColor(new Color(255, 144, 0));
        embed.setAuthor(channelName + " premiered a new video on YouTube!", channelUrl);
        embed.setDescription(String.format("""
            ### [%s](%s)
            Video available: [%s]
            """, title, videoUrl, duration));

        embed.addField("Views", views + "", false);

        embed.setImage(thumbnailUrl);
    ZonedDateTime zonedDateTime = ZonedDateTime.parse(endTime);
    embed.setFooter("Video Released • " + zonedDateTime.format(timeFormat), "https://pingcord.xyz/assets/youtube-footer.png");
        embed.setTimestamp(Instant.now());

        return embed;
    }

    public static EmbedBuilder createPremierEmbed(String channelName, String channelUrl, String title, String videoUrl, String thumbnailUrl, String startTime) {
        EmbedBuilder embed = new EmbedBuilder();

        embed.setColor(new Color(40, 40, 40));
        embed.setAuthor(channelName + " is premiering a new video on YouTube!", channelUrl);
        embed.setDescription(String.format("""
            ### [%s](%s)
            """, title, videoUrl));

    ZonedDateTime zonedDateTime = ZonedDateTime.parse(startTime);
    long epochSeconds = zonedDateTime.toEpochSecond();
        embed.addField("Releases in: ", String.format("<t:%s:R>", epochSeconds), false);

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
