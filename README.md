# holoBot

Discord bot that announces YouTube live streams, VODs, and premieres to Discord channels.

Built with Java, JDA (Java Discord API), and YouTube Data API v3. Monitors YouTube channels and posts announcements with role mentions.

## Features

- Polls YouTube channels for new uploads (every 60 seconds)
- Detects live streams, VODs, and video premieres
- Sends Discord embeds with role pings
- Tracks seen videos to avoid duplicates
- Error reporting to Discord

## Project Structure

```txt
holoBot/
├── data/
│   └── data.json         # Config, secrets & state
├── example/
│   └── example.json      # Config template
├── lib/                  # JDA, YouTube API libs
└── src/dev/m3v/
    ├── Main.java
    ├── UnitTests.java
    ├── data/
    │   ├── Data.java
    │   ├── JsonStorage.java
    │   ├── UpdateData.java
    │   └── model/
    │       ├── Channel.java
    │       ├── channelSettings.java
    │       ├── ConfigOptions.java
    │       ├── Media.java
    │       ├── MediaData.java
    │       └── Secrets.java
    ├── discord/
    │   ├── Bot.java
    │   └── EmbedTemplates.java
    └── youtube/
        ├── Client.java
        ├── Parser.java
        └── Services.java
```

## Setup

1. run the program to generate program data
2. Fill in API keys and Discord channel/role IDs in `data/data.json`

## Usage

**Compile:**

```powershell
javac -cp "lib/*" -d bin src/dev/m3v/*.java src/dev/m3v/data/*.java src/dev/m3v/data/model/*.java src/dev/m3v/discord/*.java src/dev/m3v/youtube/*.java
```

**Run:**

```powershell
java -cp "lib/*;bin" dev.m3v.Main
```

## TODO / Ideas

**Bugs:**

- Channel cooldown going under 0 (results in channels never get called)

**Features:**

- Logging for actions/config changes
- API credit usage tracking
- Channel profile pics in embeds
- Code comments / readability cleanup
- Flashcard command
- Metadata scraping commands
- Music playback in VC
- More platforms (Twitch, Twitter, Bluesky, etc.)
- DockerFile

**Far Future:**

- More commands
