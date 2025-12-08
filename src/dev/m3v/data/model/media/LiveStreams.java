package dev.m3v.data.model.media;

public class LiveStreams {
    private Media media;

    public LiveStreams() {}
    public LiveStreams(Media media) {
        this.media = media;
    }

    public Media getMedia() { return media; }
    public void setMedia(Media media) { this.media = media; }
}
