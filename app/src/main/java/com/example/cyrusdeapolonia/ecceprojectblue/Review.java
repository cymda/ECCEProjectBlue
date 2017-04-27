package com.example.cyrusdeapolonia.ecceprojectblue;

/**
 * Created by Cyrus De Apolonia on 4/25/2017.
 */

public class Review {
    private String mUsername = "";
    private String mComment = "";
    private int mRating = 0;
    private boolean mRecommend = false;

    public Review (String username, String comment, int rating, boolean recommend){
        mUsername = username;
        mComment = comment;
        mRating = rating;
        mRecommend = recommend;
    }

    public String getUsername() { return mUsername; }
    public String getComment(){ return mComment; }
    public int getRating() { return mRating; }
    public boolean getRecommend() { return  mRecommend; }

    public String toString() { return mUsername + ": " + mComment; }
}