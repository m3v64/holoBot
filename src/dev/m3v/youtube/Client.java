package dev.m3v.youtube;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTubeRequestInitializer;
import com.google.api.services.youtube.model.ChannelListResponse;
import com.google.api.services.youtube.model.LiveBroadcastListResponse;
import com.google.api.services.youtube.model.PlaylistItemListResponse;
import com.google.api.services.youtube.model.VideoListResponse;
import com.google.api.services.youtube.model.SearchListResponse;

import dev.m3v.data.*;

public class Client {
    private static YouTube youTubeClient;
    private static String apiKey;
    private static final String APPLICATION_NAME = "holoBot";
    private static final GsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    public static void initialize() throws GeneralSecurityException, IOException {
        apiKey = JsonStorage.get().getSecrets().getYoutube_api_key();
        System.out.println("Client.initialize: youtube_api_key='" + apiKey + "'");
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        youTubeClient = new YouTube.Builder(httpTransport, JSON_FACTORY, request -> { })
            .setApplicationName(APPLICATION_NAME)
            .setYouTubeRequestInitializer(new YouTubeRequestInitializer(apiKey))
            .build();
    }

    public static Boolean[] isLoaded() {
        Boolean[] isLoaded = {youTubeClient != null, apiKey != null};
        return isLoaded;
    }

    public static ChannelListResponse getChannel(String channelId) throws IOException {
        return youTubeClient.channels()
            .list(List.of("snippet,contentDetails,status"))
            .setId(List.of(channelId))
            .execute();
    }

    public static PlaylistItemListResponse getRecentUploadsPlayList(ChannelListResponse channelsResponse, long maxResults) throws IOException {
        String uploadsId = channelsResponse.getItems().get(0)
            .getContentDetails()
            .getRelatedPlaylists()
            .getUploads();

        return youTubeClient.playlistItems()
            .list(List.of("snippet,contentDetails,status"))
            .setPlaylistId(uploadsId)
            .setMaxResults(maxResults)
            .execute();
    }

    public static PlaylistItemListResponse getPlayList(String playListId) throws IOException {
        return youTubeClient.playlistItems()
            .list(List.of("snippet,contentDetails,status"))
            .setPlaylistId(playListId)
            .execute();
    }

    public static VideoListResponse getVideo(String videoId) throws IOException {
        return youTubeClient.videos()
            .list(List.of("snippet,statistics,contentDetails,liveStreamingDetails"))
            .setId(List.of(videoId))
            .execute();
    }

    public static LiveBroadcastListResponse getBroadcast(String channelId) throws IOException {
        return youTubeClient.liveBroadcasts()
            .list(List.of("snippet,contentDetails,status"))
            .setId(List.of(channelId))
            .execute();
    }

    public static SearchListResponse search(String querie, long maxResults) throws IOException {
        return youTubeClient.search()
            .list(List.of("snippet"))
            .setMaxResults(maxResults)
            .setQ(querie)
            .execute();
    }
}
