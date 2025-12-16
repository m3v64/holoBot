package dev.m3v.youtube;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ChannelListResponse;
import com.google.api.services.youtube.model.PlaylistItemListResponse;
import com.google.api.services.youtube.model.VideoListResponse;
import com.google.api.services.youtube.model.SearchListResponse;

import dev.m3v.data.*;

public class Client {
    private static YouTube youTubeClient;
    private static final String CLIENT_SECRETS= JsonStorage.get().getSecrets().getYoutube_api_key();
    private static final Collection<String> SCOPES = Arrays.asList("https://www.googleapis.com/auth/youtube.readonly");
    private static final String APPLICATION_NAME = "holoBot";
    private static final GsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    public static Credential authorize(final NetHttpTransport httpTransport) throws IOException {
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(new ByteArrayInputStream(CLIENT_SECRETS.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8));
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecrets, SCOPES).build();
        Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
        return credential;
    }

    public static void initialize() throws GeneralSecurityException, IOException {
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        Credential credential = authorize(httpTransport);
        youTubeClient = new YouTube.Builder(httpTransport, JSON_FACTORY, credential)
            .setApplicationName(APPLICATION_NAME)
            .build();
    }

    public static boolean isLoaded() {
        return youTubeClient != null;
    }

    public static ChannelListResponse getChannel(String channelId) throws IOException, GeneralSecurityException {
        ChannelListResponse response = youTubeClient.channels()
            .list(List.of("snippet,contentDetails,status"))
            .setId(List.of(channelId))
            .execute();
        return response;
    }

    public static PlaylistItemListResponse getRecentUploadsPlayList(ChannelListResponse channelsResponse, long maxResults) throws IOException, GeneralSecurityException {
        String uploadsId = channelsResponse.getItems().get(0)
            .getContentDetails()
            .getRelatedPlaylists()
            .getUploads();

        PlaylistItemListResponse playlistResponse = youTubeClient.playlistItems()
            .list(List.of("snippet,contentDetails,status"))
            .setPlaylistId(uploadsId)
            .setMaxResults(maxResults)
            .execute();
        return playlistResponse;
    }

    public static PlaylistItemListResponse getPlayList(String playListId) throws IOException, GeneralSecurityException {
        PlaylistItemListResponse playlistResponse = youTubeClient.playlistItems()
            .list(List.of("snippet,contentDetails,status"))
            .setPlaylistId(playListId)
            .execute();
        return playlistResponse;
    }

    public static VideoListResponse getVideo(String videoId) throws IOException, GeneralSecurityException {
        VideoListResponse videoListResponse = youTubeClient.videos()
            .list(List.of("snippet,statistics,contentDetails,liveStreamingDetails"))
            .setId(List.of(videoId))
            .execute();
        return videoListResponse;
    }

    public static ChannelListResponse getBroadcast(String channelId) throws IOException, GeneralSecurityException{
        ChannelListResponse response = youTubeClient.channels()
            .list(List.of("snippet,contentDetails,status"))
            .setId(List.of(channelId))
            .execute();
        return response;
    }

    public static SearchListResponse search(String querie, long maxResults) throws IOException, GeneralSecurityException {
        YouTube.Search.List request = youTubeClient.search()
            .list(List.of("snippet"));
        SearchListResponse response = request.setMaxResults(maxResults)
            .setQ(querie)
            .execute();
        return response;
    }
}
