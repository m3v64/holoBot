# holoBot

Small Discord bot focused on announcing YouTube live streams, ended streams, and new video premieres with rich embeds.

holoBot is a lightweight Java (JDA) bot. It loads structured state and configuration from `data/data.json` (see `data/example.json` for a blank template) and posts announcement embeds to configured Discord channels.

## What does it do?

Current functionality:

- Loads secrets, channel config, stream/video history from `data/data.json`
- Connects to Discord using JDA.
- Builds standardized embeds for:
  - Live stream announcements
  - Ended stream summaries
  - Video/premiere announcements
- Error reporting with an embed pinging an admin role.
- Data model for tracking past checks and ongoing live streams.

Note: Large parts of the main code are still commented out for testing purposes.

## Planned / Roadmap

Planned improvements:

1. Song Download Sub‑Command
    - Add a Discord command (e.g. `/download <url>`) to fetch and save audio/video to a user‑specified or configured directory.
    - Integrate yt-dlp or an equivalent Java wrapper; sanitize output paths & enforce size/time limits.
    - Add tags to the downloaded file when a reliable tagging library is available.

2. Containerization
    - Provide a production-ready `Dockerfile` (multi‑stage: build → slim runtime). Optional health check & non‑root user.

3. YouTube Polling Service
    - Activate/refactor the commented scheduler to poll channels in a fair rotation (respecting API quotas and per‑channel cooldowns).
    - Move polling logic into `Youtube` with clear separation: fetch → diff → persist → announce.

4. Configuration Enhancements
    - Environment variable overrides for secrets (avoid committing real keys in `data.json`).
    - Graceful shutdown + periodic autosave.

5. Resilience & Logging
    - Add a structured logging layer (log level already in config) and retry/backoff for transient failures.

Stretch ideas:

- Automated setup (bot automatically creates roles/channels on first launch).
- Web dashboard (read‑only status) or simple REST endpoint for health/last check times.
- Per‑channel role mention customization for each media type.

## Project Structure

src/dev/m3v/
  Main.java            # Entry point (bot init, scheduler)
  Discord.java         # Discord API interactions + embed dispatch
  FromJson.java        # JSON-backed state & config loader/saver
  EmbedTemplates.java  # Central embed builders
  Youtube.java         # polling / API integration
data/
  data.json            # Active runtime data & secrets

## Configuration

1. Copy `data/example.json` → `data/data.json` (if not present).
2. Fill in:
    - `secrets.discord_api_key` – Bot token from the Discord Developer Portal.
    - `secrets.youtube_api_key` – YouTube Data API key.
    - Channel IDs (`mediaChannelId`, `premiereChannelId`).
    - `adminId` for error pings (role or user ID).

3. Optional tuning: `checkIntervalSeconds`, `channelCooldownMinutes`, `lastCheckSaveLimit`.

Security tip: Move secrets to environment variables later (roadmap item).

## Usage

Build and run (typical Java project layout—adjust if using a build tool later):

```bash
javac -cp "lib/*" -d bin src/dev/m3v/*.java
java  -cp "lib/*;bin" dev.m3v.Main
```

On Windows PowerShell you can run:

```bash
javac -cp "lib/*" -d bin src/dev/m3v/*.java
java -cp "lib/*;bin" dev.m3v.Main
```

## Development Notes

- Add unit tests around `FromJson` parsing before expanding logic.
- Keep embed construction centralized in `EmbedTemplates` to avoid duplication.

## Download Sub‑Command (Planned)

Planned interface (subject to change):

`/download URL [--audio|--video]`

Behavior outline:

- Validate URL & media type.
- Queue the job; acknowledge immediately.
- Run the external downloader (yt-dlp) in a controlled temp directory.
- Enforce file size and duration caps.
- Reply with a share link to the song using the Navidrome Subsonic API.

## Docker (Planned)

Planned multi-stage Dockerfile sketch:

```dockerfile
FROM eclipse-temurin:24-jdk AS build
WORKDIR /holoBot
COPY . .
RUN javac -cp "lib/*" -d bin $(find src -name "*.java")

FROM eclipse-temurin:24-jre
WORKDIR /holoBot
COPY --from=build /holoBot/bin ./bin
COPY data/example.json ./data/data.json
COPY lib ./lib
ENV JAVA_OPTS=""
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -cp lib/*:bin dev.m3v.Main"]
```

(Windows note: container runtime uses Linux path separators.)

## Thanks

Thanks for checking out my hobby project—holoBot!
