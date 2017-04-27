package com.example.cyrusdeapolonia.ecceprojectblue;

import java.util.ArrayList;

/**
 * Created by Cyrus De Apolonia on 4/25/2017.
 */

public class Course {
    String mCourseTitle = "";
    ArrayList<Review> mReviews = new ArrayList<>();

    public Course(String courseTitle){
        mCourseTitle = courseTitle;
    }

    public void addReview(Review newReview){
        mReviews.add(newReview);
    }

    public ArrayList<Review> getReviews() { return mReviews; }

    public String getCourseTitle() {return mCourseTitle; }

    public void deleteReview(int index){
        mReviews.remove(index);
    }
    public void editReview(int index, Review review){
        mReviews.add(index, review);
        mReviews.remove(index+1);
    }
}