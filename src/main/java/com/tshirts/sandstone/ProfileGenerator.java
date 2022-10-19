package com.tshirts.sandstone;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tshirts.sandstone.vaadin.util.PermissionLevel;
import com.tshirts.sandstone.vaadin.util.Profile;

import javax.net.ssl.HttpsURLConnection;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Random;

public class ProfileGenerator {

    public static void main(String[] args) {
        int count = 3000;
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Profile[] profiles = new Profile[count];

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
            final String firstName = person.get("name").getAsJsonObject().get("first").getAsString();
            final String lastName = person.get("name").getAsJsonObject().get("last").getAsString();
            final String email = person.get("email").getAsString();
            final String username = person.get("login").getAsJsonObject().get("username").getAsString();
            final String password = person.get("login").getAsJsonObject().get("password").getAsString();
            final String phone = person.get("phone").getAsString().replaceAll("[^0-9]", "");
            // Use the hash of the registered date as the id.
            final int profileID = Math.abs(person.get("registered").getAsJsonObject().get("date").getAsString().hashCode());
            PermissionLevel[] weights = new PermissionLevel[]{
                    PermissionLevel.ADMIN, PermissionLevel.MANAGER, PermissionLevel.MANAGER, PermissionLevel.MANAGER, PermissionLevel.USER, PermissionLevel.USER, PermissionLevel.USER, PermissionLevel.USER, PermissionLevel.USER, PermissionLevel.USER, PermissionLevel.USER, PermissionLevel.USER, PermissionLevel.USER, PermissionLevel.USER, PermissionLevel.USER, PermissionLevel.USER, PermissionLevel.GUEST, PermissionLevel.GUEST, PermissionLevel.GUEST, PermissionLevel.GUEST, PermissionLevel.GUEST, PermissionLevel.GUEST, PermissionLevel.GUEST, PermissionLevel.GUEST, PermissionLevel.GUEST, PermissionLevel.GUEST, PermissionLevel.GUEST, PermissionLevel.GUEST, PermissionLevel.GUEST
            };
            // Randomly select from the weights array.
            final PermissionLevel permissionLevel = weights[new Random().nextInt(weights.length)];

            profiles[i] = new Profile(username, firstName, lastName, email, phone, password, profileID, permissionLevel);
        }
        // Add test profile for admin.
        profiles[0] = new Profile("username0", "firstName0", "lastName0", "email0@gmail.com", "8675309", "password0".hashCode()+ "", 0, PermissionLevel.ADMIN);

        // Write the profiles to a json file.
        try {
            FileWriter writer = new FileWriter("src/main/resources/profiles.json");
            gson.toJson(profiles, writer);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

