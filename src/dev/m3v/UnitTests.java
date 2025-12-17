package dev.m3v;

import dev.m3v.data.JsonStorage;
import dev.m3v.youtube.*;

public class UnitTests {
    public static void test() {
        try {
            JsonStorage.load();
            Client.initialize();
        } catch (Exception e) {
            System.err.println("Failed to initialize holoBot. Exiting.");
            e.printStackTrace();
            System.exit(1);
        }

        try {
            Parser.saveMedia(Client.getVideo("3SFk-xyQpjk"));
        } catch (Exception e) {
            System.err.println("Failed during Test");
            e.printStackTrace();
            System.exit(1);
        }
    }
}
