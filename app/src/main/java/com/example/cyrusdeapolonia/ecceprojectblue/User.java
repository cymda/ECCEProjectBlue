package com.example.cyrusdeapolonia.ecceprojectblue;

/**
 * Created by Cyrus De Apolonia on 4/25/2017.
 */

public class User {
    private String mUsername = "";
    private String mPassword = "";

    public User (String username, String password) {
        mUsername = username;
        mPassword = password;
    }

    public String getUsername() { return mUsername; }
    public String getPassword() { return mPassword; }
}
