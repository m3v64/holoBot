package dev.m3v.data;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

import com.google.gson.*;

import dev.m3v.data.model.*;
import dev.m3v.data.model.media.Media;

public class JsonStorage {
    private static final String PATH = "data/data.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static Data load() {
        try (FileReader reader = new FileReader(PATH)) {
            Data.instance = gson.fromJson(reader, Data.class);
            if (Data.instance == null) {
                Data.instance = new Data(1.0, new Secrets(), new ArrayList<Channels>(), new ArrayList<Memory>(), new ArrayList<LiveStreams>(), new ConfigOptions(), new Media());
            }
            return Data.instance;
        } catch (Exception FileNotFoundException ) {
            File file = new File(PATH);
            try {
                file.createNewFile();
                Data.instance = new Data(1.0, new Secrets(), new ArrayList<Channels>(), new ArrayList<Memory>(), new ArrayList<LiveStreams>(), new ConfigOptions(), new Media());
                return Data.instance;
            } catch (Exception e) {
                return null;
            }
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
                // handel exception
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

