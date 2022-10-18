package com.tshirts.sandstone;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tshirts.sandstone.vaadin.util.PermissionLevel;
import com.tshirts.sandstone.vaadin.util.Profile;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class ProfileGenerator {
    public static void main(String[] args) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Profile[] profiles = new Profile[500];
        Random random = new Random();
        for (int i = 0; i < 500; i++) {
            Profile profile = new Profile("username" + i,
                    "firstName" + i,
                    "lastName" + i,
                    "email" + i + "@gmail.com",
                    String.format("%03d", random.nextInt(999)) + "-" + String.format("%03d", random.nextInt(999)) + "-" + String.format("%03d", random.nextInt(9999)),
                    "password" + i,
                    i,
                    PermissionLevel.ADMIN);
            profiles[i] = profile;
        }

        String json = gson.toJson(profiles, Profile[].class);
        // Write to file
        try (FileWriter file = new FileWriter("src/main/resources/profiles.json")) {
            file.write(json);
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
