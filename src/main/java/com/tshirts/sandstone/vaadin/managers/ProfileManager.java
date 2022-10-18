package com.tshirts.sandstone.vaadin.managers;

import com.google.gson.Gson;
import com.tshirts.sandstone.vaadin.util.Profile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ProfileManager implements Closeable {
    private static ProfileManager instance = null;
    ArrayList<Profile> profiles = new ArrayList<>();


    private ProfileManager() {
        this.profiles = new ArrayList<>(List.of(readFromFile()));
    }

    public static ProfileManager getInstance() {
        if (instance == null) {
            instance = new ProfileManager();
        }
        return instance;
    }

    public boolean writeToFile() {
        Gson gson = new Gson();
        // To array
        Profile[] profileArray = new Profile[profiles.size()];
        profileArray = profiles.toArray(profileArray);
        // To json
        String json = gson.toJson(profileArray);
        // Write to file
        try (FileWriter file = new FileWriter("src/main/resources/profiles.json")) {
            file.write(json);
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public Profile[] readFromFile() {
        Gson gson = new Gson();
        Profile[] profileArray = null;
        try (FileReader file = new FileReader("src/main/resources/profiles.json")) {
            // Read the whole file
            BufferedReader reader = new BufferedReader(file);
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            // Parse the json
            profileArray = gson.fromJson(builder.toString(), Profile[].class);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return profileArray;
    }

    public boolean addProfile(Profile profile) {
        return profiles.add(profile);
    }

    public boolean removeProfile(Profile profile) {
        return profiles.remove(profile);
    }

    public Profile getProfile(int profileId) {
        for (Profile profile : profiles) {
            if (profile.getProfileId() == profileId) {
                return profile;
            }
        }
        return null;
    }

    public Profile getProfile(String email) {
        for (Profile profile : profiles) {
            if (profile.getEmail().equals(email)) {
                return profile;
            }
        }
        return null;
    }

    public Profile getProfile(String usernameOrEmail, String password) {
        for (Profile profile : profiles) {
            if ((profile.getUsername().equals(usernameOrEmail)  || profile.getEmail().equals(usernameOrEmail)) && profile.getPassword().equals(password)) {
                return profile;
            }
        }
        return null;
    }

    public ArrayList<Profile> getProfiles() {
        return profiles;
    }



    @Override
    public void close() throws IOException {
        if (writeToFile()) {
            System.out.println("Profiles saved to file");
            instance = null;
        } else {
            throw new IOException("Failed to save profiles to file");
        }
    }
}
