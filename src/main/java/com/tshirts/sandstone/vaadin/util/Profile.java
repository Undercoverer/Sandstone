package com.tshirts.sandstone.vaadin.util;

import com.google.gson.Gson;

public class Profile {
    public String username;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String password; // ;)
    private int profileId;
    private PermissionLevel permissionLevel;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private Profile(String json) {
        Gson gson = new Gson();
        Profile profile = gson.fromJson(json, Profile.class);
        this.username = profile.username;
        this.firstName = profile.firstName;
        this.lastName = profile.lastName;
        this.email = profile.email;
        this.phone = profile.phone;
        this.password = profile.password;
        this.profileId = profile.profileId;
        this.permissionLevel = profile.permissionLevel;
    }

    public Profile(String email, String password){
        this.email = email;
        this.password = password;
    }

    public Profile(String username, String firstName, String lastName, String email, String phone, String password, int profileId, PermissionLevel permissionLevel) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.password = password.hashCode() + "";
        this.profileId = profileId;
        this.permissionLevel = permissionLevel;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password.hashCode() + "";
    }

    public int getProfileId() {
        return profileId;
    }

    public void setProfileId(int profileId) {
        this.profileId = profileId;
    }

    public PermissionLevel getPermissionLevel() {
        return permissionLevel;
    }

    public void setPermissionLevel(PermissionLevel permissionLevel) {
        this.permissionLevel = permissionLevel;
    }
}
