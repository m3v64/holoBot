package dev.m3v.data.model;

public class MediaData {
    private String channelName;
    private String channelUrl;
    private String title;
    private String description;
    private String mediaUrl;
    private String thumbnailUrl;
    private String duration;
    private int views;
    private int avgViewers;
    private int peakViewers;
    private String startTime;
    private String endTime;

    public MediaData() {}
    public MediaData(String channelName, String channelUrl, String title, String description, String mediaUrl, String thumbnailUrl, String duration, int views, int avgViewers, int peakViewers, String startTime, String endTime) {
        this.channelName = channelName;
        this.channelUrl = channelUrl;
        this.title = title;
        this.description = description;
        this.mediaUrl = mediaUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.duration = duration;
        this.views = views;
        this.avgViewers = avgViewers;
        this.peakViewers = peakViewers;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getChannelName() { return channelName; }
    public void setChannelName(String channelName) { this.channelName = channelName; }

    public String getChannelUrl() { return channelUrl; }
    public void setChannelUrl(String channelUrl) { this.channelUrl = channelUrl; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getMediaUrl() { return mediaUrl; }
    public void setMediaUrl(String mediaUrl) { this.mediaUrl = mediaUrl; }

    public String getThumbnailUrl() { return thumbnailUrl; }
    public void setThumbnailUrl(String thumbnailUrl) { this.thumbnailUrl = thumbnailUrl; }

    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }

    public int getViews() { return views; }
    public void setViews(int views) { this.views = views; }

    public int getAvgViewers() { return avgViewers; }
    public void setAvgViewers(int avgViewers) { this.avgViewers = avgViewers; }

    public int getPeakViewers() { return peakViewers; }
    public void setPeakViewers(int peakViewers) { this.peakViewers = peakViewers; }

    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }

    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }
}
