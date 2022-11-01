package com.tshirts.sandstone.util;

import com.tshirts.sandstone.util.annotations.H2FieldData;
public class Profile {
    @H2FieldData(primaryKey = true, notNull = true)
    private String username;

    @H2FieldData(notNull = true)
    private String firstName;
    @H2FieldData(notNull = true)
    private String lastName;
    @H2FieldData(notNull = true)
    private String email;
    @H2FieldData(notNull = true)
    private String phone;
    @H2FieldData(notNull = true)
    private int hashedPassword;
    @H2FieldData(notNull = true)
    private int profileId;
    @H2FieldData(notNull = true)
    private PermissionLevel permissionLevel;

    public Profile(String username, String firstName, String lastName, String email, String phone, String password, int profileId, PermissionLevel permissionLevel) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone.replaceAll("[^0-9]", "");
        this.hashedPassword = password.hashCode();
        this.profileId = profileId;
        this.permissionLevel = permissionLevel;
    }

    public Profile() {
    }
    public String getUsername() {
        return username;
    }


    public void setUsername(String username) {
        this.username = username;
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
        this.phone = phone.replaceAll("[^0-9]", "");
    }

    public int getHashedPassword() {
        return hashedPassword;
    }

    public void setPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword.hashCode();
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

    @Override
    public String toString() {
        return "Profile{" +
                "username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", hashedPassword=" + hashedPassword +
                ", profileId=" + profileId +
                ", permissionLevel=" + permissionLevel +
                '}';
    }
}
