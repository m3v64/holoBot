package dev.m3v.youtube;

import java.util.List;

import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;

import dev.m3v.data.JsonStorage;
import dev.m3v.data.model.media.*;

public class Parser {
    public static void saveMedia(VideoListResponse videoListResponse) {
        Media media = JsonStorage.get().getMemory().getFirst().getMedia();
        MediaData data = media.getData();
        List<Video> Items = videoListResponse.getItems();

        for (Video video : Items) {
            media.setChannelId(video.getSnippet().getChannelId());
            media.setMediaId(video.getId());
            media.setType(video.getKind());

            if (video.getSnippet() != null) {
                data.setChannelName(video.getSnippet().getChannelTitle());
                data.setChannelUrl("https://www.youtube.com/channel/" + video.getSnippet().getChannelId());
                data.setTitle(video.getSnippet().getTitle());
                data.setDescription(video.getSnippet().getDescription());
                data.setMediaUrl("https://www.youtube.com/watch?v=" + video.getId());
                if (video.getSnippet().getThumbnails() != null) {
                    if (video.getSnippet().getThumbnails().getMaxres() != null)
                        data.setThumbnailUrl(video.getSnippet().getThumbnails().getMaxres().setHeight(1080L).setWidth(1920L).getUrl());
                    else if (video.getSnippet().getThumbnails().getHigh() != null)
                        data.setThumbnailUrl(video.getSnippet().getThumbnails().getHigh().setHeight(1080L).setWidth(1920L).getUrl());
                    else if (video.getSnippet().getThumbnails().getDefault() != null)
                        data.setThumbnailUrl(video.getSnippet().getThumbnails().getDefault().setHeight(1080L).setWidth(1920L).getUrl());
                }
            }

            if (video.getContentDetails() != null)
                data.setDuration(video.getContentDetails().getDuration());
            if (video.getStatistics() != null && video.getStatistics().getViewCount() != null)
                data.setViews(video.getStatistics().getViewCount().intValue());
            if (video.getLiveStreamingDetails() != null) {
                if (video.getLiveStreamingDetails().getActualStartTime() != null)
                    data.setStartTime(video.getLiveStreamingDetails().getActualStartTime().toStringRfc3339());
                else if (video.getLiveStreamingDetails().getScheduledStartTime() != null)
                    data.setStartTime(video.getLiveStreamingDetails().getScheduledStartTime().toStringRfc3339());
                if (video.getLiveStreamingDetails().getActualEndTime() != null)
                    data.setEndTime(video.getLiveStreamingDetails().getActualEndTime().toStringRfc3339());
                if (video.getLiveStreamingDetails().getConcurrentViewers() != null)
                    data.setAvgViewers(video.getLiveStreamingDetails().getConcurrentViewers().intValue());
            }

            media.setData(data);
        }

        JsonStorage.save();
    }
}
