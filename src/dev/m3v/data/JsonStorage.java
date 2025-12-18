package dev.m3v.data;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.io.FileWriter;
import java.util.ArrayList;

import com.google.gson.*;

import dev.m3v.data.model.*;
import dev.m3v.data.model.media.Media;

public class JsonStorage {
    private static final String PATH = "data/data.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static Data load() {
        System.out.println("JsonStorage.load: user.dir='" + System.getProperty("user.dir") + "'");
        File file = new File(PATH);
        System.out.println("JsonStorage.load: attempted path=" + file.getAbsolutePath());
        System.out.println("JsonStorage.load: exists=" + file.exists() + ", isFile=" + file.isFile() + ", length=" + (file.exists() ? file.length() : -1L));
        if (file.getParentFile() != null && file.getParentFile().exists()) {
            System.out.println("JsonStorage.load: parent exists; listing files:");
            String[] list = file.getParentFile().list();
            if (list != null) for (String f : list) System.out.println(" - " + f);
        } else {
            System.out.println("JsonStorage.load: parent directory does not exist: " + (file.getParentFile() == null ? "(null)" : file.getParentFile().getAbsolutePath()));
        }

        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
                System.out.println("JsonStorage.load: created new file at " + file.getAbsolutePath());
            } catch (Exception e) {
                System.err.println("JsonStorage.load: failed to create new file: " + e.getMessage());
                e.printStackTrace();
                return null;
            }
            Data.instance = new Data(1.0, new Secrets(), new ArrayList<Channels>(), new ArrayList<Memory>(), new ArrayList<LiveStreams>(), new ConfigOptions(), new Media());
            return Data.instance;
        }

        try {
            String content = Files.readString(file.toPath(), StandardCharsets.UTF_8);
            System.out.println("JsonStorage.load: file content length=" + content.length());
            System.out.println("JsonStorage.load: content preview: " + (content.length() > 200 ? content.substring(0,200) + "..." : content));
            try {
                Data.instance = gson.fromJson(content, Data.class);
                if (Data.instance != null && Data.instance.getSecrets() != null) {
                    System.out.println("JsonStorage.load: loaded youtube_api_key='" + Data.instance.getSecrets().getYoutube_api_key() + "'");
                } else {
                    System.out.println("JsonStorage.load: Data.instance or secrets is null after parse");
                }
            } catch (JsonSyntaxException jse) {
                System.err.println("JsonStorage.load: JSON parse failed: " + jse.getMessage());
                jse.printStackTrace();
                Data.instance = new Data(1.0, new Secrets(), new ArrayList<Channels>(), new ArrayList<Memory>(), new ArrayList<LiveStreams>(), new ConfigOptions(), new Media());
            }
            if (Data.instance == null) {
                Data.instance = new Data(1.0, new Secrets(), new ArrayList<Channels>(), new ArrayList<Memory>(), new ArrayList<LiveStreams>(), new ConfigOptions(), new Media());
            }
            return Data.instance;
        } catch (Exception e) {
            System.err.println("JsonStorage.load: unexpected error reading file: " + e.getMessage());
            e.printStackTrace();
            Data.instance = new Data(1.0, new Secrets(), new ArrayList<Channels>(), new ArrayList<Memory>(), new ArrayList<LiveStreams>(), new ConfigOptions(), new Media());
            return Data.instance;
        }
    }

    public static void save() {
        Data data = Data.instance == null ? new Data(1.0, new Secrets(), new ArrayList<Channels>(), new ArrayList<Memory>(), new ArrayList<LiveStreams>(), new ConfigOptions(), new Media()) : Data.instance;
        try (FileWriter writer = new FileWriter(PATH)) {
            gson.toJson(data, writer);
        } catch (Exception FileNotFoundException ) {
            File file = new File(PATH);
            try {
                file.createNewFile();
                FileWriter writer = new FileWriter(PATH);
                gson.toJson(data, writer);
            } catch (Exception e) {
                System.err.println("something gone wrong wrong ln 84 JsonStorage");
            }
        }
    }

    public static Data get() {
        if (Data.instance == null) {
            System.err.println("Data instance isnt initialized yet");
            System.exit(1);
        }
        return Data.instance;
    }

    public static boolean isLoaded() {
        return Data.instance != null;
    }
}

