package dev.m3v;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTubeRequestInitializer;
import com.google.api.services.youtube.model.ChannelListResponse;
import com.google.api.services.youtube.model.PlaylistItemListResponse;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;

public class YoutubeData {
    private static YouTube youTubeService;

    public static void initialize() throws GeneralSecurityException, IOException {
        if (youTubeService != null) return;
        if (!FromJson.isLoaded()) FromJson.load();
        String apiKey = FromJson.get().getSecrets().getYoutube_api_key();
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("youtube_api_key missing in data/data.json");
        }
        HttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();
        youTubeService = new YouTube.Builder(transport, GsonFactory.getDefaultInstance(), request -> {})
                .setApplicationName("holoBot")
                .setYouTubeRequestInitializer(new YouTubeRequestInitializer(apiKey))
                .build();
    }

    public static List<String> check(String channelId) throws IOException, GeneralSecurityException  {
        if (youTubeService == null) initialize();

        ChannelListResponse channelsResponse = youTubeService.channels()
            .list(List.of("contentDetails"))
            .setId(List.of(channelId))
            .setFields("items(contentDetails/relatedPlaylists/uploads)")
            .setMaxResults(1L)
            .execute();
        if (channelsResponse.getItems() == null || channelsResponse.getItems().isEmpty())
            throw new RuntimeException("YouTube channels.list returned no items");

        String uploadsId = channelsResponse.getItems().get(0)
            .getContentDetails()
            .getRelatedPlaylists()
            .getUploads();

        Set<String> seenVideoIds = new HashSet<>();
        for (FromJson.CheckData checkDataEntry : FromJson.get().getCheckData()) {
            if (checkDataEntry == null) continue;
            if (!channelId.equals(checkDataEntry.getChannelId())) continue;
            if (checkDataEntry.getVideoId() != null) seenVideoIds.add(checkDataEntry.getVideoId());
        }
            System.out.printf("[holoBot] Seen for %s: %d%n", channelId, seenVideoIds.size());

        List<String> newVideoIds = new ArrayList<>();
        String pageToken = null;
        final long pageSize = 50L;
        int saveLimit = 100;
        int perChannelLimit = 0;
        if (FromJson.get().getConfigOptions() != null) {
            if (FromJson.get().getConfigOptions().getLastCheckSaveLimit() > 0) {
                saveLimit = FromJson.get().getConfigOptions().getLastCheckSaveLimit();
            }
            if (FromJson.get().getConfigOptions().getLastCheckSaveLimitPerChannel() > 0) {
                perChannelLimit = FromJson.get().getConfigOptions().getLastCheckSaveLimitPerChannel();
            }
        }
        int effectiveLimit = (perChannelLimit > 0) ? Math.min(saveLimit, perChannelLimit) : saveLimit;

        while (true) {
            PlaylistItemListResponse playlistResponse = youTubeService.playlistItems()
                .list(List.of("contentDetails"))
                .setPlaylistId(uploadsId)
                .setMaxResults(pageSize)
                .setPageToken(pageToken)
                .setFields("items(contentDetails/videoId),nextPageToken")
                .execute();

            if (playlistResponse.getItems() == null || playlistResponse.getItems().isEmpty()) break;

            boolean hitKnown = false;
            for (var item : playlistResponse.getItems()) {
                String videoId = item.getContentDetails() == null ? null : item.getContentDetails().getVideoId();
                if (videoId == null) continue;
                if (seenVideoIds.contains(videoId)) {
                    hitKnown = true;
                    break;
                }
                if (!newVideoIds.contains(videoId)) newVideoIds.add(videoId);
                if (newVideoIds.size() >= effectiveLimit) {
                    hitKnown = true;
                    break;
                }
            }

            if (hitKnown || newVideoIds.size() >= effectiveLimit) break;

            pageToken = playlistResponse.getNextPageToken();
            if (pageToken == null) break;
        }
        if (!newVideoIds.isEmpty()) {
            newVideoIds.removeIf(seenVideoIds::contains);
        }
        return newVideoIds;
    }

    private static FromJson.Data toData(Video video) {
        FromJson.Data data = new FromJson.Data();
        if (video == null) return data;
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
        return data;
    }

    public static List<Video> getVideos(List<String> ids) throws IOException, GeneralSecurityException {
        if (youTubeService == null) initialize();
        if (ids == null || ids.isEmpty()) return List.of();

        List<Video> result = new ArrayList<>();
        int startIndex = 0;
        while (startIndex < ids.size()) {
            int endIndex = Math.min(startIndex + 50, ids.size());
            List<String> batch = ids.subList(startIndex, endIndex);
            VideoListResponse videoListResponse = youTubeService.videos()
                    .list(Arrays.asList("snippet", "statistics", "contentDetails", "liveStreamingDetails"))
                    .setId(batch)
                    .execute();
            if (videoListResponse.getItems() != null) {
                result.addAll(videoListResponse.getItems());
            }
            startIndex = endIndex;
        }
        return result;
    }

    public static void saveVideos(List<String> ids, String channelId) throws Exception {
        if (ids == null || ids.isEmpty()) {
            if (youTubeService == null) initialize();
            try {
                ChannelListResponse channelsResponse = youTubeService.channels()
                    .list(List.of("contentDetails"))
                    .setId(List.of(channelId))
                    .setFields("items(contentDetails/relatedPlaylists/uploads)")
                    .setMaxResults(1L)
                    .execute();
                if (channelsResponse.getItems() != null && !channelsResponse.getItems().isEmpty()) {
                    String uploadsId = channelsResponse.getItems().get(0)
                        .getContentDetails()
                        .getRelatedPlaylists()
                        .getUploads();
                    PlaylistItemListResponse playlist = youTubeService.playlistItems()
                        .list(List.of("contentDetails"))
                        .setPlaylistId(uploadsId)
                        .setMaxResults(1L)
                        .setFields("items(contentDetails/videoId)")
                        .execute();
                    if (playlist.getItems() != null && !playlist.getItems().isEmpty() &&
                        playlist.getItems().get(0).getContentDetails() != null &&
                        playlist.getItems().get(0).getContentDetails().getVideoId() != null) {
                        ids = new ArrayList<>();
                        ids.add(playlist.getItems().get(0).getContentDetails().getVideoId());
                        System.out.printf("[holoBot] Snapshot: adding latest video for %s since no new IDs were found%n", channelId);
                    }
                }
            } catch (Exception e) {
                System.err.println("[holoBot] Snapshot fetch failed: " + e.getMessage());
            }
            if (ids == null || ids.isEmpty()) return;
        }
        List<Video> videos = getVideos(ids);
        if (videos.isEmpty()) return;
        if (!FromJson.isLoaded()) FromJson.load();
        FromJson jsonData = FromJson.get();
        String roleId = jsonData.getChannels(channelId).getRoleId();

        for (int i = videos.size() - 1; i >= 0; i--) {
            Video video = videos.get(i);
            if (video == null) continue;
            FromJson.Data data = toData(video);
            String videoId = video.getId();
            String liveFlag = video.getSnippet() == null ? "none" : video.getSnippet().getLiveBroadcastContent();
            boolean isLiveLike = "live".equalsIgnoreCase(liveFlag);

            if (isLiveLike) {
                List<FromJson.LiveStream> liveStreams = jsonData.getLiveStreams();
                if (liveStreams == null) liveStreams = new ArrayList<>();
                int existingLiveIndex = -1;
                for (int streamIndex = 0; streamIndex < liveStreams.size(); streamIndex++) {
                    if (channelId.equals(liveStreams.get(streamIndex).getChannelId())) { existingLiveIndex = streamIndex; break; }
                }
                FromJson.LiveStream liveStreamEntry = new FromJson.LiveStream(channelId, videoId, roleId);
                liveStreamEntry.setData(data);
                if (existingLiveIndex >= 0) liveStreams.set(existingLiveIndex, liveStreamEntry); else liveStreams.add(liveStreamEntry);
                jsonData.setLiveStreams(liveStreams);
            } else {
                List<FromJson.CheckData> checkHistory = jsonData.getCheckDataHistory();
                if (checkHistory == null) checkHistory = new ArrayList<>();
                boolean alreadySaved = false;
                for (FromJson.CheckData historyItem : checkHistory) {
                    if (historyItem == null) continue;
                    if (channelId.equals(historyItem.getChannelId()) && videoId.equals(historyItem.getVideoId())) {
                        alreadySaved = true;
                        break;
                    }
                }
                if (!alreadySaved) {
                    FromJson.CheckData checkData = new FromJson.CheckData(channelId, videoId, roleId);
                    checkData.setData(data);
                    checkHistory.add(0, checkData);
                }

                int globalLimit = 100;
                int perChannelLimit = 20;
                if (jsonData.getConfigOptions() != null) {
                    if (jsonData.getConfigOptions().getLastCheckSaveLimit() > 0)
                        globalLimit = jsonData.getConfigOptions().getLastCheckSaveLimit();
                    if (jsonData.getConfigOptions().getLastCheckSaveLimitPerChannel() > 0)
                        perChannelLimit = jsonData.getConfigOptions().getLastCheckSaveLimitPerChannel();
                }

                if (perChannelLimit > 0) {
                    int countForChannel = 0;
                    List<FromJson.CheckData> prunedHistory = new ArrayList<>();
                    for (FromJson.CheckData historyItem : checkHistory) {
                        if (historyItem == null) continue;
                        if (channelId.equals(historyItem.getChannelId())) {
                            if (countForChannel < perChannelLimit) {
                                prunedHistory.add(historyItem);
                                countForChannel++;
                            }
                        } else {
                            prunedHistory.add(historyItem);
                        }
                        if (prunedHistory.size() >= globalLimit) break;
                    }
                    checkHistory = prunedHistory;
                }

                if (checkHistory.size() > globalLimit)
                    checkHistory = new ArrayList<>(checkHistory.subList(0, globalLimit));

                jsonData.setCheckDataHistory(checkHistory);
            }
        }
        FromJson.save();
    }
}
