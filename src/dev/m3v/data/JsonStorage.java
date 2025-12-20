package dev.m3v.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.io.FileWriter;

import com.google.gson.*;

import dev.m3v.Log;

public class JsonStorage {
    private static final String PATH = "data/data.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static Data load() {
        File file = new File(PATH);

        if (!file.exists()) {
            Log.warn("Data json file does not exist, creating now...", JsonStorage.class, null);
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
                Log.info(("Created Json file at" + file.getAbsolutePath()), JsonStorage.class);
            } catch (Exception e) {
                System.err.println("JsonStorage.load: failed to create new file: " + e.getMessage());
                e.printStackTrace();
                return null;
            }
            Data.instance = new Data();
            return Data.instance;
        }

        try {
            String content = Files.readString(file.toPath(), StandardCharsets.UTF_8);
            try {
                Data.instance = gson.fromJson(content, Data.class);
                if (Data.instance != null && Data.instance.getSecrets() != null) {
                    System.out.println("JsonStorage.load: loaded youtube_api_key='" + Data.instance.getSecrets().getYoutube_api_key() + "'");
                } else {
                    System.out.println("JsonStorage.load: Data.instance or secrets is null after parse");
                }
            } catch (JsonSyntaxException jse) {
               
            }
            if (Data.instance == null) {
                Data.instance = new Data();
            }
            return Data.instance;
        } catch (Exception e) {
            System.err.println("JsonStorage.load: unexpected error reading file: " + e.getMessage());
            e.printStackTrace();
            Data.instance = new Data();
            return Data.instance;
        }
    }

    public static void save() {
        Data data = Data.instance == null ? new Data() : Data.instance;
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

