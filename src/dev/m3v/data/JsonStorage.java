package dev.m3v.data;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

import com.google.gson.*;

import dev.m3v.data.model.*;

public class JsonStorage {
    private static final String PATH = "data/data.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static Data load() {
        try (FileReader reader = new FileReader(PATH)) {
            return gson.fromJson(reader, Data.class);
        } catch (Exception FileNotFoundException ) {
            File file = new File(PATH);
            try {
                file.createNewFile();
                FileReader reader = new FileReader(PATH);
                return gson.fromJson(reader, Data.class);
            } catch (Exception e) {
                return null;
            }
        }
    }

    public static void save() {
        Data data = Data.instance == null ? new Data(1.0, new Secrets(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ConfigOptions()) : Data.instance;
        try (FileWriter writer = new FileWriter(PATH)) {
            gson.toJson(data, writer);
        } catch (Exception FileNotFoundException ) {
            File file = new File(PATH);
            try {
                file.createNewFile();
                FileWriter writer = new FileWriter(PATH);
                gson.toJson(data, writer);
            } catch (Exception e) {
                // handel exception
            }
        }
    }

    public static Data get() {
        if (Data.instance == null) {
            // handle exception
        }
        return Data.instance;
    }

    public static boolean isLoaded() {
        return Data.instance != null;
    }
}

