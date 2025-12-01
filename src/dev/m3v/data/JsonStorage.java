package dev.m3v.data;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.*;

public class JsonStorage {
    private static final String PATH = "data/data.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static Data load() throws IOException {
        try (FileReader reader = new FileReader(PATH)) {
            return gson.fromJson(reader, Data.class);
        }
    }

    public static void save(Data data) throws IOException {
        try (FileWriter writer = new FileWriter(PATH)) {
            gson.toJson(data, writer);
        }
    }
}

