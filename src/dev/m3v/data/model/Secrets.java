package dev.m3v.data.model;

public class Secrets {
    private String discord_api_key;
    private String youtube_api_key;

    public Secrets() {}
    public Secrets(String discord_api_key, String youtube_api_key) {
        this.discord_api_key = discord_api_key;
        this.youtube_api_key = youtube_api_key;
    }

    public String getDiscord_api_key() { return discord_api_key; }
    public void setDiscord_api_key(String discord_api_key) { this.discord_api_key = discord_api_key; }

    public String getYoutube_api_key() { return youtube_api_key; }
    public void setYoutube_api_key(String youtube_api_key) { this.youtube_api_key = youtube_api_key; }
}
