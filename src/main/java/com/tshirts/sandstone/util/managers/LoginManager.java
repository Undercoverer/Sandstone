package com.tshirts.sandstone.util.managers;

import com.tshirts.sandstone.util.Profile;

public class LoginManager {
    private static LoginManager instance = null;
    private boolean loggedIn = false;

    private Profile loggedInUser = null;

    private LoginManager() {
    }

    public static LoginManager getInstance() {
        if (instance == null) {
            instance = new LoginManager();
        }
        return instance;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public Profile getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedIn(boolean loggedIn, Profile loggedInUser) {
        this.loggedIn = loggedIn;
        this.loggedInUser = loggedInUser;
    }
}

