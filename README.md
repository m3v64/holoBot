# holoBot

Discord bot for announcing YouTube live streams, VODs, and video premieres with rich embeds.

holoBot is a Java-based Discord bot built with JDA (Java Discord API) and the YouTube Data API v3. It monitors YouTube channels for new content and automatically posts formatted announcements to configured Discord channels with role mentions.

## What does it do?

**Currently implemented:**

- **YouTube monitoring**: Polls YouTube channels every 60 seconds for new uploads using the YouTube Data API v3
- **Smart content detection**: Distinguishes between live streams, VODs, and video premieres
- **Discord announcements**: Sends rich embeds with custom formatting for:
  - Live stream start notifications
  - VOD (stream archive) announcements
  - Video premiere announcements
- **Role mentions**: Configurable per-channel role pings for notifications
- **Memory system**: Tracks previously seen videos to avoid duplicate announcements (configurable limits)
- **Live stream tracking**: Maintains state of ongoing streams
- **Error reporting**: Automatic error embeds with admin role mentions and stack traces
- **Persistent storage**: JSON-based data storage (`data/data.json`) for configuration, secrets, and state
- **Graceful shutdown**: Cleanup hooks for proper service termination

## Roadmap

**Planned improvements:**

1. **Code refactoring**
   - Move YouTube polling logic into dedicated `youtube/` package classes
   - Implement proper separation of concerns (fetch → parse → persist → announce)
   - Add comprehensive error handling throughout the codebase

2. **Song Download Command**
   - Discord slash command (e.g., `/download <url>`) for fetching audio/video
   - Integration with yt-dlp or equivalent Java wrapper
   - File size/duration limits and path sanitization
   - Metadata tagging support

3. **Containerization**
   - Production-ready multi-stage `Dockerfile`
   - Non-root user and health checks
   - Volume mounts for data persistence

4. **Configuration Enhancements**
   - Environment variable support for secrets (avoid committing API keys)
   - Per-channel polling interval configuration
   - Configurable check interval (currently hardcoded to 60 seconds)

5. **Resilience & Logging**
   - Structured logging framework (utilize existing log level config)
   - Retry logic with exponential backoff for API failures
   - Better quota management for YouTube API

6. **Testing**
   - Unit tests for data models and JSON serialization
   - Integration tests for YouTube API interactions
   - Mock Discord client for testing embed generation

**Stretch goals:**

- Automated Discord setup (auto-create roles/channels on first launch)
- Web dashboard or REST API for monitoring bot status
- Support for multiple Discord servers
- Customizable embed templates per server/channel

## Project Structure

```txt
holoBot
├── bin/                            # Compiled Java classes
├── data/
│   ├── data.json                   # Active runtime data, secrets & state
│   └── example.json                # Template configuration file
├── lib/                            # External dependencies (JDA, YouTube API)
└── src/
    └── dev/
        └── m3v/
            ├── Main.java           # Entry point, scheduler, YouTube check orchestration
            ├── YoutubeData.java    # YouTube Data API v3 integration & polling logic
            ├── data/
            │   ├── Data.java       # Core data model & state management
            │   ├── JsonStorage.java # JSON serialization/deserialization
            │   ├── UpdateData.java  # Data update utilities
            │   └── model/
            │       ├── Channels.java      # YouTube channel configuration
            │       ├── ConfigOptions.java # Bot configuration settings
            │       ├── Secrets.java       # API keys storage model
            │       └── media/
            │           ├── LiveStreams.java # Live stream state tracking
            │           ├── Media.java       # Media announcement wrapper
            │           ├── MediaData.java   # Video metadata model
            │           └── Memory.java      # Seen video tracking
            ├── discord/
            │   ├── Bot.java            # Discord JDA client & message dispatch
            │   └── EmbedTemplates.java # Rich embed builders for announcements
            └── youtube/
                ├── Client.java     # (Placeholder for future refactoring)
                ├── Parser.java     # YouTube API response parsing
                └── Services.java   # YouTube service utilities
```

## Configuration

1. Copy `data/example.json` → `data/data.json` (if not present).
2. Configure the following in `data/data.json`:

**Required fields:**

- `secrets.discord_api_key` – Bot token from Discord Developer Portal
- `secrets.youtube_api_key` – YouTube Data API v3 key (from Google Cloud Console)
- `configOptions.mediaChannelId` – Discord channel ID for VOD announcements
- `configOptions.streamChannelId` – Discord channel ID for live stream announcements
- `configOptions.premierChannelId` – Discord channel ID for premiere announcements
- `configOptions.errorChannelId` – Discord channel ID for error reports
- `configOptions.adminId` – Discord role ID to ping on errors

**Per-channel configuration:**

- `channels[]` – Array of YouTube channels to monitor:
  - `channelId` – YouTube channel ID
  - `roleId` – Discord role ID to mention for this channel's content
  - `lastCheckTimestamp` – Managed automatically
  - `uploadId` – Managed automatically

**Optional tuning:**

- `configOptions.lastCheckSaveLimit` – Maximum memory entries across all channels (default: 100)
- `configOptions.lastCheckSaveLimitPerChannel` – Maximum memory entries per channel
- Check interval is hardcoded to 60 seconds in `Main.java`

**Security tip:** Move secrets to environment variables for production (roadmap item).

## Dependencies

Required Java libraries (place in `lib/` directory):

- **JDA (Java Discord API)** – Discord bot framework
- **Google YouTube Data API v3** – YouTube channel monitoring
- **Google HTTP Client & JSON libraries** – API communication

## Usage

**Compile:**

```powershell
javac -cp "lib/*" -d bin src/dev/m3v/*.java src/dev/m3v/data/*.java src/dev/m3v/data/model/*.java src/dev/m3v/data/model/media/*.java src/dev/m3v/discord/*.java src/dev/m3v/youtube/*.java
```

**Run:**

```powershell
java -cp "lib/*;bin" dev.m3v.Main
```

The bot will:

1. Load configuration from `data/data.json`
2. Connect to Discord and YouTube APIs
3. Start polling configured channels every 60 seconds
4. Send announcements to Discord when new content is detected
5. Save state updates back to `data/data.json`

## Development Notes

- **State persistence**: Memory cache and live stream state are automatically saved to `data/data.json`
- **Error handling**: All errors are caught and reported to the configured Discord error channel
- **API quotas**: YouTube Data API has daily quota limits—monitor usage in Google Cloud Console
- **Modular design**: Data models are separated from business logic for easier testing and maintenance

## Technical Details

**Architecture:**

- **Event-driven polling**: Single-threaded scheduler checks YouTube channels at regular intervals
- **Deduplication**: Memory cache prevents duplicate announcements using video ID tracking
- **State management**: JSON-based persistence for configuration, secrets, and runtime state
- **Embed system**: Centralized template-based Discord embed generation
- **Error resilience**: Try-catch blocks with Discord error reporting for all critical operations

**API Usage:**

- **YouTube Data API v3**:
  - `channels.list` – Fetch upload playlist IDs
  - `playlistItems.list` – Retrieve recent uploads from channels
  - `videos.list` – Get video metadata (title, description, status, timestamps)

- **Discord (JDA)**:
  - Rich embeds with thumbnails, descriptions, and metadata
  - Role mentions for notifications
  - Message ID tracking for potential future updates

**Performance considerations:**

- 60-second polling interval balances responsiveness with API quota usage
- Memory limit configuration prevents unbounded state growth
- Pagination support for channels with many uploads

## Contributing

This is a personal hobby project, but suggestions and bug reports are welcome! Feel free to open issues or submit pull requests.

---

*Thanks for checking out holoBot!*
