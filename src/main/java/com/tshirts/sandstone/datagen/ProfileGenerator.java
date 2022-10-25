package com.tshirts.sandstone.datagen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tshirts.sandstone.util.PermissionLevel;
import com.tshirts.sandstone.util.Profile;
import com.tshirts.sandstone.util.Util;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class ProfileGenerator {

    public static void main(String[] args) {
        Collection<Profile> all = Util.DB.getAll(Profile.class);
        if (all == null || all.size() == 0) {
            generateProfiles();
        } else {
            System.out.println("Profiles already exist");
            for (Profile profile : all) {
                if (!profile.getUsername().equals("username0")) {
                    Util.DB.delete(profile);
                }
            }
            generateProfiles();
        }


    }

    private static void generateProfiles() {
        System.out.println("Generating profiles");
        int count = 500;
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        ArrayList<Profile> profiles = new ArrayList<>();

        HttpsURLConnection connection;
        try {
            connection = (HttpsURLConnection) new URL("https://randomuser.me/api/?results=" + count).openConnection();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // Read the response from the api.
        String json = "";
        try {
            json = new String(connection.getInputStream().readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // Parse the json into a list of profiles.
        JsonArray results = gson.fromJson(json, JsonObject.class).getAsJsonArray("results");
        for (int i = 0; i < results.size(); i++) {
            JsonObject person = results.get(i).getAsJsonObject();
            // Replace ' with '' to escape the single quote character.
            final String firstName = person.get("name").getAsJsonObject().get("first").getAsString().replaceAll("'", "''");
            final String lastName = person.get("name").getAsJsonObject().get("last").getAsString().replaceAll("'", "''");
            if (firstName.length() < 2 || lastName.length() < 2) {
                continue;
            }
            final String email = person.get("email").getAsString().replaceAll("[^a-zA-Z0-9.@]", "");
            final String username = person.get("login").getAsJsonObject().get("username").getAsString().replaceAll("[^a-zA-Z0-9.]", "");
            final String password = person.get("login").getAsJsonObject().get("password").getAsString().replaceAll("[^a-zA-Z0-9.@!#$%&*()_-]", "");
            final String phone = person.get("phone").getAsString().replaceAll("[^0-9]", "");
            // Use the hash of the registered date as the id.
            final int profileID = Math.abs(person.get("registered").getAsJsonObject().get("date").getAsString().hashCode());
            PermissionLevel[] weights = new PermissionLevel[]{
                    PermissionLevel.ADMIN, PermissionLevel.MANAGER, PermissionLevel.MANAGER, PermissionLevel.MANAGER, PermissionLevel.USER, PermissionLevel.USER, PermissionLevel.USER, PermissionLevel.USER, PermissionLevel.USER, PermissionLevel.USER, PermissionLevel.USER, PermissionLevel.USER, PermissionLevel.USER, PermissionLevel.USER, PermissionLevel.USER, PermissionLevel.USER, PermissionLevel.GUEST, PermissionLevel.GUEST, PermissionLevel.GUEST, PermissionLevel.GUEST, PermissionLevel.GUEST, PermissionLevel.GUEST, PermissionLevel.GUEST, PermissionLevel.GUEST, PermissionLevel.GUEST, PermissionLevel.GUEST, PermissionLevel.GUEST, PermissionLevel.GUEST, PermissionLevel.GUEST
            };
            // Randomly select from the weights array.
            final PermissionLevel permissionLevel = weights[new Random().nextInt(weights.length)];

            profiles.add(new Profile(username, firstName, lastName, email, phone, password, profileID, permissionLevel));
        }
        // Add test profile for admin.
        profiles.add(new Profile("username0", "firstName0", "lastName0", "email0@gmail.com", "8675309", "password0", 0, PermissionLevel.ADMIN));
        Util.DB.createTable(Profile.class);
        Util.DB.addAll(profiles);
    }

}

