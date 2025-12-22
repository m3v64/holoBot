package dev.m3v.data;

import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.FileTime;
import java.io.IOException;
import java.util.*;

import com.google.gson.*;

import dev.m3v.Log;
import dev.m3v.Main;

public class JsonStorage {
    private static Path PATH = Paths.get("data", "data.json");
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public synchronized static void setPath(String path) {
        try {
            PATH = Paths.get(path);
        } catch (Exception e) {
            Log.warn("Failed to set path", JsonStorage.class, e);
        }
    }

    public synchronized static void load() {
        Path file = PATH;
        Data current = Data.instance == null ? new Data() : Data.instance;

        try {
            if (Files.notExists(file)) {
                Log.warn("Data json file does not exist, creating now...", JsonStorage.class, null);
                try {
                    if (file.getParent() != null) {
                        Files.createDirectories(file.getParent());
                    }
                    String json = gson.toJson(current);
                    try {
                        Files.write(file, json.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE_NEW);
                        Log.info("Created Json file at " + file.toAbsolutePath(), JsonStorage.class);
                    } catch (FileAlreadyExistsException e) {
                        Log.info("Json file was created concurrently: " + file.toAbsolutePath(), JsonStorage.class);
                    }
                } catch (IOException e) {
                    Log.error("Failed to create json file", JsonStorage.class, e);
                    Main.shutdown(null, 1);
                    return;
                }
            }

            long size = Files.size(file);
            if (size == 0) {
                Data.instance = current;
                return;
            }

            final String content;
            try {
                content = Files.readString(file, StandardCharsets.UTF_8);
            } catch (IOException e) {
                Log.error("Could not read data from the Json file", JsonStorage.class, e);
                Main.shutdown(null, 1);
                return;
            }

            try {
                Data parsed = gson.fromJson(content, Data.class);
                if (parsed != null) {
                    Data.instance = parsed;
                } else {
                    Log.warn("Parsed JSON was null; keeping existing data instance", JsonStorage.class, null);
                    Data.instance = current;
                }
            } catch (JsonParseException e) {
                Log.error("Failed to parse JSON - cannot continue", JsonStorage.class, e);
                Main.shutdown(null, 1);
                return;
            }
        } catch (IOException e) {
            Log.error("I/O error while loading JSON file", JsonStorage.class, e);
            Main.shutdown(null, 1);
        } catch (Exception e) {
            Log.error("Exception while loading Json data", JsonStorage.class, e);
            Main.shutdown(null, 1);
        }
    }

    public synchronized static void save() {
        Data data = Data.instance == null ? new Data() : Data.instance;
        Path file = PATH;
        try {
            if (file.getParent() != null) {
                Files.createDirectories(file.getParent());
            }

            String json = gson.toJson(data);
            Path temp = Files.createTempFile(file.getParent(), "data", ".tmp");
            Files.writeString(temp, json, StandardCharsets.UTF_8, StandardOpenOption.TRUNCATE_EXISTING);

            if (Files.exists(file)) {
                Path backup = file.resolveSibling(file.getFileName().toString() + "." + System.currentTimeMillis() + ".bak");
                try {
                    Files.copy(file, backup, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    Log.warn("Failed to create backup file: " + backup, JsonStorage.class, e);
                }

                try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(file.getParent(), file.getFileName().toString() + ".*.bak")) {
                    List<Path> backups = new ArrayList<>();
                    for (Path path : directoryStream) {
                        backups.add(path);
                    }
                    if (backups.size() > 1) {
                        backups.sort((a, b) -> {
                            try {
                                FileTime timeA = Files.getLastModifiedTime(a);
                                FileTime timeB = Files.getLastModifiedTime(b);
                                return timeA.compareTo(timeB);
                            } catch (IOException ex) {
                                return 0;
                            }
                        });
                        for (int i = 1; i < backups.size(); i++) {
                            try {
                                Files.deleteIfExists(backups.get(i));
                            } catch (IOException ex) {
                                Log.warn("Failed to delete old backup file: " + backups.get(i), JsonStorage.class, ex);
                            }
                        }
                    }
                } catch (IOException ex) {
                    Log.warn("Failed to prune backup files", JsonStorage.class, ex);
                }
            }

            try {
                Files.move(temp, file, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            } catch (AtomicMoveNotSupportedException e) {
                Files.move(temp, file, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            Log.error("Saving Json data failed", JsonStorage.class, e);
            throw new RuntimeException(e);
        }
    }

    public synchronized static Data get() {
        if (Data.instance == null) {
            Log.error("Data class isnt initialized", JsonStorage.class, null);
        }
        return Data.instance;
    }

    public synchronized static boolean isLoaded() {
        return Data.instance != null;
    }
}

