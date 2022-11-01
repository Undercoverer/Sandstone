package com.tshirts.sandstone.util.managers;

import com.google.gson.Gson;
import com.tshirts.sandstone.util.Profile;
import com.tshirts.sandstone.util.Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public class ProfileManager implements IManager<Profile> {
    static ArrayList<Profile> profiles;
    private static ProfileManager instance = null;


    private ProfileManager() {
        Util.DB.createTable(Profile.class);
        profiles = new ArrayList<>();
        Collection<Profile> all = Util.DB.getAll(Profile.class);
        if (all != null){
            profiles.addAll(all);
        } else {
            System.out.println("There are no profiles! Login is impossible");
        }
    }

    public static ProfileManager getInstance() {
        if (instance == null) {
            instance = new ProfileManager();
        }
        return instance;
    }

    public Profile[] findBy(String type, String value) {
        Stream<Profile> profileStream = profiles.stream();
        return switch (type) {
            case "username" ->
                    profileStream.filter(profile -> profile.getUsername().toLowerCase().contains(value.toLowerCase())).toArray(Profile[]::new);
            case "email" ->
                    profileStream.filter(profile -> profile.getEmail().toLowerCase().contains(value.toLowerCase())).toArray(Profile[]::new);
            case "id" ->
                    profileStream.filter(profile -> String.valueOf(profile.getProfileId()).contains(value.toLowerCase())).toArray(Profile[]::new);
            case "firstName" ->
                    profileStream.filter(profile -> profile.getFirstName().toLowerCase().contains(value.toLowerCase())).toArray(Profile[]::new);
            case "lastName" ->
                    profileStream.filter(profile -> profile.getLastName().toLowerCase().contains(value.toLowerCase())).toArray(Profile[]::new);
            case "phoneNumber" ->
                    profileStream.filter(profile -> profile.getPhone().toLowerCase().contains(value.toLowerCase())).toArray(Profile[]::new);
            case "permissionLevel" ->
                    profileStream.filter(profile -> profile.getPermissionLevel().toString().toLowerCase().contains(value.toLowerCase())).toArray(Profile[]::new);
            default -> null;
        };
    }

    public boolean export(File path) {
        Gson gson = new Gson();
        // To array
        Profile[] profileArray = new Profile[profiles.size()];
        profileArray = profiles.toArray(profileArray);
        // To json
        return Util.write(path, gson, profileArray);
    }

    public boolean import_(File path) {
        Gson gson = new Gson();
        Profile[] profileArray;
        try (FileReader file = new FileReader(path)) {
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

        // All added
        return profiles.addAll(List.of(profileArray));
    }

    public boolean add(Profile profile) {
        profiles.add(profile);
        return Util.DB.add(profile);
    }

    public boolean remove(Profile profile) {
        profiles.remove(profile);
        return Util.DB.delete(profile);
    }

    @Override
    public boolean update(Profile profile, String field, Object value) {
        try {
            Field f = profile.getClass().getDeclaredField(field);
            f.setAccessible(true);
            f.set(profile, value);
            return Util.DB.update(profile, field, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return false;
        }
    }


    public Profile get(int profileId) {
        return Util.DB.get(Profile.class, "profileId", profileId);
    }

    public Profile get(String email) {
        return Util.DB.get(Profile.class,"email", email);
    }

    public Profile getProfile(String usernameOrEmail, String password) {
        Profile profile = Util.DB.get(Profile.class,"username", usernameOrEmail);
        if (profile == null) {
            profile = Util.DB.get(Profile.class,"email", usernameOrEmail);
        }
        if (profile != null && profile.getHashedPassword() == password.hashCode()) {
            return profile;
        }
        return null;
    }

    public Profile[] getAll() {
        return profiles.toArray(new Profile[0]);
    }
}
