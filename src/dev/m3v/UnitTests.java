package dev.m3v;

import dev.m3v.data.JsonStorage;
import dev.m3v.youtube.*;

public class UnitTests {
    public static void test() {
        try {
            Log.load();
            JsonStorage.load();
            Client.initialize();
        } catch (Exception e) {
            Log.log("ERROR", "Failed to initialize bot", UnitTests.class, e);
            return;
        }

        try {
            System.out.println("stuff is loaded: " + Client.isLoaded() + ", " + JsonStorage.isLoaded());
            Parser.saveMedia(Client.getVideo("AEb_093KjhM"));
        } catch (Exception e) {
            Log.log("WARN", "Unit test failed", UnitTests.class, e);
            return;
        }
    }
}
