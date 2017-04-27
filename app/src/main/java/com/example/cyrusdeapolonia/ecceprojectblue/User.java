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

    /*
    private String mName = "";
    private int mIDno = 0;
    private String mCourse = "";
    private String mUsername = "";
    private String mPassword = "";
    private String mEmailAd = "";

    public User (String name, int idno, String course, String username, String password, String emailAd) {
        mName = name;
        mIDno = idno;
        mCourse = course;
        mUsername = username;
        mPassword = password;
        mEmailAd = emailAd;
    }

    public String getName() {return mName;}
    */
}
