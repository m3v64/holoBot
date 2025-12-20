package dev.m3v;

import java.util.Arrays;

import dev.m3v.data.JsonStorage;
import dev.m3v.youtube.*;

public class UnitTests {
    public static void test() {
        try {
            Log.load();
            JsonStorage.load();
            Client.initialize();
        } catch (Exception e) {
            System.err.println("Failed to initialize holoBot. Exiting.");
            e.printStackTrace();
            System.exit(1);
        }

        try {
            System.out.println("stuff is loaded: " + Arrays.toString(Client.isLoaded()) + ", " + JsonStorage.isLoaded() + ", " + JsonStorage.get().getSecrets().getYoutube_api_key());
            Parser.saveMedia(Client.getVideo("AEb_093KjhM"));
        } catch (Exception e) {
            System.err.println("Failed during Test");
            e.printStackTrace();
            System.exit(1);
        }
    }
}
