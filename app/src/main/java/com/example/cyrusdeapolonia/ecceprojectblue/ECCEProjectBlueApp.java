package com.example.cyrusdeapolonia.ecceprojectblue;

import android.app.Application;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * Created by Cyrus De Apolonia on 4/25/2017.
 */

public class ECCEProjectBlueApp extends Application {
    private String mUsername = "";//naka log in na user
    private ArrayList<Professor> mProfessorList = new ArrayList<>();
    private ArrayList<User> mUserList = new ArrayList<>();



    public void initializeUsers(){
        mUserList.add(new User("Cyrus", "c"));
        mUserList.add(new User("Aija", "a"));
        mUserList.add(new User("Shin", "s"));
        mUserList.add(new User("Maki", "m"));
        mUserList.add(new User("Migs", "m"));
        saveUserList(mUserList);
    }

    public void initializeProfessors(){
        String professorNames[] = {"Tiausas", "Dumlao", "Guico", "Parocha", "Oppus", "Mayuga"};
        for(int i=0; i<professorNames.length; i++){
            mProfessorList.add(new Professor(professorNames[i]));
            for(int j=0; j<4; j++){
                mProfessorList.get(i).addGeneralReview(new Review(
                        "Cyrus", professorNames[i]+" generalcomment"+j, j, true
                ));
            }
            for(int j=0; j<4; j++){
                String courseTitle = "P" + i + "C" + j;
                mProfessorList.get(i).addCourse(courseTitle);
                for(int k=0; k<3; k++){
                    mProfessorList.get(i).addCourseReview(courseTitle, new Review(
                            "Cyrus", professorNames[i]+" comment"+k,k+1,true
                    ));
                }
            }

        }
    }

    public String getUser() { return mUsername; }
    public void setUser(String username) { mUsername = username; }
    public ArrayList<Professor> getProfessorList() { return mProfessorList; }
    public Professor getProfessor(String professorName){
        Professor professor = null;
        for(int i=0; i<mProfessorList.size(); i++){
            if( mProfessorList.get(i).getName().equals(professorName) ){
                professor = mProfessorList.get(i);
            }
        }
        return professor;
    }

    public void addReview(Review newReview, String professorName, String courseTitle){
        for(int i=0; i<mProfessorList.size(); i++){
            if( mProfessorList.get(i).getName().equals(professorName) ){
                if( courseTitle == null ) mProfessorList.get(i).addGeneralReview(newReview);
                else mProfessorList.get(i).addCourseReview(courseTitle, newReview);
                break;
            }
        }
    }

    public boolean userExists(String username, String password){
        for(int i=0; i<mUserList.size(); i++){
            User user = mUserList.get(i);
            if(user.getUsername().equals(username) && user.getPassword().equals(password))
                return true;
        }
        return false;
    }


    public boolean saveUserList(ArrayList<User> userList) {
        String filename = "userlist.txt";

        String fileContents = "";
        for (int i = 0; i < userList.size(); i++) {
            User user = userList.get(i);
            fileContents += user.getUsername() + "," +user.getPassword() + "\n";
        }

        return writeFile(filename, fileContents);
    }

    public void loadUserList(){
        String filename = "userlist.txt";
    }



    public boolean saveProfessorList() {
        String filename = "professorlist.txt";
        String fileContents = "";

        JSONObject object = new JSONObject();
        JSONArray professorArray = new JSONArray();
        for(int i=0; i<mProfessorList.size(); i++){
            professorArray.put(convertProfessorToJSONObject(mProfessorList.get(i)));
        }
        try {
            object.put("professors", professorArray);
        } catch (Exception e){
            Log.e("ECCEProjectBlueApp", "Exception occurred: " + e.getMessage());
        }

        fileContents = object.toString();

        return writeFile(filename, fileContents);
    }

    private JSONObject convertProfessorToJSONObject(Professor professor){
        JSONObject object = new JSONObject();

        JSONArray generalReviewArray = new JSONArray();
        ArrayList<Review> generalReviews = professor.getGeneralReviews();
        for(int i=0; i<generalReviews.size(); i++){
            generalReviewArray.put(convertReviewToJSONObject(generalReviews.get(i)));
        }

        JSONArray courseArray = new JSONArray();
        ArrayList<Course> courses = professor.getCourses();
        for(int i=0; i<courses.size(); i++){
            courseArray.put(convertCourseToJSONObject(courses.get(i)));
        }

        try {
            object.put("professorname", professor.getName());
            object.put("generalreviews", generalReviewArray);
            object.put("courses", courseArray);
        } catch (Exception e){
            Log.e("ECCEProjectBlueApp", "Exception occurred: " + e.getMessage());
        }

        return object;
    }

    private JSONObject convertCourseToJSONObject(Course course){
        JSONObject object = new JSONObject();
        JSONArray reviewArray = new JSONArray();
        ArrayList<Review> reviews = course.getReviews();
        for(int i=0; i<reviews.size(); i++){
            reviewArray.put(convertReviewToJSONObject(reviews.get(i)));
        }
        try {
            object.put("coursetitle", course.getCourseTitle());
            object.put("reviews", reviewArray);
        } catch (Exception e){
            Log.e("ECCEProjectBlueApp", "Exception occurred: " + e.getMessage());
        }

        return object;
    }

    private JSONObject convertReviewToJSONObject(Review review){
        JSONObject object = new JSONObject();
        try {
            object.put("username", review.getUsername());
            object.put("comment", review.getComment());
            object.put("rating", review.getRating());
            object.put("recommend", review.getRecommend());

        } catch (Exception e){
            Log.e("ECCEProjectBlueApp", "Exception occurred: " + e.getMessage());
        }
        return object;
    }



    private String getStoragePath() {
        String storagePath = getFilesDir().toString();
        return storagePath;
    }

    private boolean writeFile(String fileName, String data) {
        String savePath = getStoragePath();

        File pathStorage = new File(savePath);
        if (pathStorage.exists() == false) {
            pathStorage.mkdirs();
        }

        File saveFile = new File(savePath, fileName);
        if (saveFile.exists() == false) {
            try {
                saveFile.createNewFile();
            } catch (Exception e) {
                Log.e("ECCEProjectBlueApp", "Exception occurred: " + e.getMessage());
                return false;
            }
        }

        try {
            FileOutputStream fos = new FileOutputStream(saveFile, false);
            fos.write(data.getBytes());
            fos.close();
        } catch (Exception e) {
            Log.e("ECCEProjectBlueFaceApp", "Exception occurred: " + e.getMessage());
            return false;
        }

        return true;
    }
}