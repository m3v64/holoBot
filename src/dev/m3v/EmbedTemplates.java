package dev.m3v;

import net.dv8tion.jda.api.EmbedBuilder;
import java.awt.Color;
import java.time.Instant;

public class EmbedTemplates {
    public static EmbedBuilder createLiveEmbed(String channelName, String channelUrl, String title, String description, String streamUrl, String thumbnailUrl, int avgViewers, int peakViewers, String startTime) {
        EmbedBuilder embed = new EmbedBuilder();

        embed.setColor(new Color(255, 0, 0));
        embed.setAuthor(channelName + " was live. 🔴", channelUrl);
        embed.setDescription(String.format("""
            ### [%s](%s)
            %s
            """, title, streamUrl, description));

        embed.addField("Viewers", avgViewers + " avg / " + peakViewers + " peak", false);

        embed.setImage(thumbnailUrl);
        embed.setFooter("Live on YouTube since • " + startTime, "https://images-ext-1.discordapp.net/external/Idee1-SpLF0ZZMYEYswjgJIwfBPVj-EjatBLcIe9zTM/http/content.kabii.moe%3A8080/ytlogo");
        embed.setTimestamp(Instant.now());

        return embed;
    }

    public static EmbedBuilder createEndedStreamEmbed(String channelName, String channelUrl, String title, String streamUrl, String thumbnailUrl, String duration, int avgViewers, int peakViewers, String endTime) {
        EmbedBuilder embed = new EmbedBuilder();

        embed.setColor(new Color(135, 0, 0));
        embed.setAuthor(channelName + " was live.", channelUrl);
        embed.setDescription(String.format("""
            ### [%s](%s)
            Video available: [%s]
            """, title, streamUrl, duration));

        embed.addField("Viewers", avgViewers + " avg / " + peakViewers + " peak", false);

        embed.setThumbnail(thumbnailUrl);
        embed.setFooter("Stream ended • " + endTime, "https://images-ext-1.discordapp.net/external/Idee1-SpLF0ZZMYEYswjgJIwfBPVj-EjatBLcIe9zTM/http/content.kabii.moe%3A8080/ytlogo");
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

        embed.setThumbnail(thumbnailUrl);
        embed.setFooter("Video Released • " + endTime, "https://images-ext-1.discordapp.net/external/Idee1-SpLF0ZZMYEYswjgJIwfBPVj-EjatBLcIe9zTM/http/content.kabii.moe%3A8080/ytlogo");
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
