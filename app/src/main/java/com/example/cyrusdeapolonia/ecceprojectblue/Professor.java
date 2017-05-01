package com.example.cyrusdeapolonia.ecceprojectblue;

import java.util.ArrayList;

/**
 * Created by Cyrus De Apolonia on 4/25/2017.
 */

public class Professor {
    private String mName = "";
    private ArrayList<Review> mGeneralReviews = new ArrayList<>();
    private ArrayList<Course> mCourses = new ArrayList<>();

    public Professor(String name){ mName = name; }

    public void addGeneralReview(Review newReview){ mGeneralReviews.add(newReview); }
    public void addCourse(String newCourse){ mCourses.add(new Course(newCourse)); }
    public void addCourse(Course course) { mCourses.add(course); }

    //adds a review to a particular course the prof is teaching
    public void addCourseReview(String courseTitle, Review newReview){
        for(int i=0; i<mCourses.size(); i++){
            if( courseTitle.equals( mCourses.get(i).getCourseTitle() ) ){
                mCourses.get(i).addReview(newReview);
            }
        }
    }

    public void updateGeneralReviews(ArrayList<Review> generalReviews){
        mGeneralReviews = generalReviews;
    }

    public void updateCourseReviews(ArrayList<Review> courseReviews, String courseTitle){
        for(int i=0; i<mCourses.size(); i++){
            if( courseTitle.equals( mCourses.get(i).getCourseTitle() ) ){
                mCourses.get(i).updateCourseReviews(courseReviews);
                break;
            }
        }
    }

    ////////////////////////getters////////////////////////
    public String getName(){ return mName; }

    public ArrayList<Review> getGeneralReviews() { return mGeneralReviews; }
    public ArrayList<Course> getCourses() { return mCourses; }

    //get list of courses the professor is teaching (in Strings)
    public ArrayList<String> getCourseTitles() {
        ArrayList<String> courseTitles = new ArrayList<>();
        for(int i=0; i<mCourses.size(); i++){
            courseTitles.add(mCourses.get(i).getCourseTitle());
        }
        return courseTitles;
    }

    //get list of reviews of a professor for a particular course
    public ArrayList<Review> getCourseReviews(String courseTitle){
        ArrayList<Review> courseReviews = new ArrayList<>();
        for(int i=0; i<mCourses.size(); i++){
            if( courseTitle.equals( mCourses.get(i).getCourseTitle() ) ){
                courseReviews = mCourses.get(i).getReviews();
            }
        }
        return courseReviews;
    }

    public String toString() {
        return getName();
    }
}