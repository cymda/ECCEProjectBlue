package com.example.cyrusdeapolonia.ecceprojectblue;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * Created by Cyrus De Apolonia on 4/25/2017.
 */

public class ECCEProjectBlueApp extends Application {
    private String mUsername = ""; //naka log in na user
    private ArrayList<Professor> mProfessorList = new ArrayList<>();
    private ArrayList<User> mUserList = new ArrayList<>();

    //Shared preferences
    public void saveUserApp(String username, String password){
        SharedPreferences prefs = getSharedPreferences("edu.ateneo.cie199.ecceprojectblue", Context.MODE_PRIVATE);
        SharedPreferences.Editor edt = prefs.edit();
        edt.putString("USERNAME", username);
        edt.putString("PASSWORD", password);
        edt.commit();
    }
    public String getUserApp(){
        SharedPreferences prefs = getSharedPreferences("edu.ateneo.cie199.ecceprojectblue", Context.MODE_PRIVATE);
        return prefs.getString("USERNAME", "");
    }
    public String getUserPasswordApp(){
        SharedPreferences prefs = getSharedPreferences("edu.ateneo.cie199.ecceprojectblue", Context.MODE_PRIVATE);
        return prefs.getString("PASSWORD", "");
    }

    public void initializeLists(){
        SharedPreferences prefs = getSharedPreferences("edu.ateneo.cie199.ecceprojectblue", Context.MODE_PRIVATE);
        boolean appIsNotYetInitialized = prefs.getBoolean("INITIALIZE_NEEDED", true);
        if(appIsNotYetInitialized){
            //initialize users
            mUserList.add(new User("Cyrus", "c"));
            mUserList.add(new User("Aija", "a"));
            mUserList.add(new User("Shin", "s"));
            mUserList.add(new User("Maki", "m"));
            mUserList.add(new User("Migs", "m"));
            saveUserList();
            mUserList.clear();

            //initialize professors
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
            saveProfessorList();
            mProfessorList.clear();

            SharedPreferences.Editor edt = prefs.edit();
            edt.putBoolean("INITIALIZE_NEEDED", false);
            edt.commit();
        }
    }

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

    public String getUser() { return mUsername; }
    public void setUser(String username) { mUsername = username; }
    public void addUser(String username, String password) { mUserList.add(new User(username, password)); }
    public boolean userExists(String username, String password){
        for(int i=0; i<mUserList.size(); i++){
            User user = mUserList.get(i);
            if(user.getUsername().equals(username) && user.getPassword().equals(password))
                return true;
        }
        return false;
    }
    public boolean usernameNotYetTaken(String username){
        for(int i=0; i<mUserList.size(); i++){
            if(mUserList.get(i).getUsername().equals(username))
                return false;
        }
        return true;
    }
    public boolean saveUserList() {
        String filename = "userlist.txt";

        String fileContents = "";

        JSONObject object = new JSONObject();
        JSONArray userArray = new JSONArray();
        for(int i=0; i<mUserList.size(); i++){
            userArray.put(convertUserToJSONObject(mUserList.get(i)));
        }

        try {
            object.put("users", userArray);
        } catch (Exception e){
            Log.e("ECCEProjectBlueApp", "Exception occurred: " + e.getMessage());
        }

        fileContents = object.toString();

        return writeFile(filename, fileContents);
    }
    public boolean loadUserList(){
        String filename = "userlist.txt";

        String loadPath = getStoragePath();

        /* Check if file exists */
        File loadFile = new File(loadPath, filename);
        if (loadFile.exists() == false) {
            Log.e("ECCEProjectBlueApp", "File not loaded because it does not exist");
            return false;
        }

        String contents = "";
        try {
            FileInputStream fis = new FileInputStream( loadFile );
            while (fis.available() > 0) {
                byte buf[] = new byte[32];
                int bytesRead = fis.read(buf, 0, 32);
                contents += new String(buf, 0, bytesRead);
            }

            fis.close();
        } catch (Exception e) {
            Log.e("ECCEProjectBlueApp", "Exception occurred: " + e.getMessage());
            return false;
        }

        Log.d("ECCEProjectBlueApp", "File Read Done:");
        Log.d("ECCEProjectBlueApp", "    " + contents );

        return parseUsersToList(contents);
//        return contents;
    }
    private boolean parseUsersToList(String contents){
        JSONObject object;
        ArrayList<User> userList = new ArrayList<>();
        try {
            object = new JSONObject(contents);
            JSONArray userArray = object.getJSONArray("users");
            for(int i=0; i<userArray.length(); i++){
                JSONObject userObject =  userArray.getJSONObject(i);
                String username = userObject.getString("username");
                String password = userObject.getString("password");
                userList.add(new User(username, password));
            }
        } catch (Exception e){
            Log.e("ECCEProjectBlueApp", "Exception occurred: " + e.getMessage());
            return false;
        }

        mUserList = userList;
        return true;
    }


    public boolean loadProfessorList(){
        String filename = "professorlist.txt";

        String loadPath = getStoragePath();
        File loadFile = new File(loadPath, filename);
        if (loadFile.exists() == false) {
            Log.e("ECCEProjectBlueApp", "File not loaded because it does not exist");
            return false;
        }

        String contents = "";
        try {
            FileInputStream fis = new FileInputStream( loadFile );
            while (fis.available() > 0) {
                byte buf[] = new byte[32];
                int bytesRead = fis.read(buf, 0, 32);
                contents += new String(buf, 0, bytesRead);
            }
            fis.close();
        } catch (Exception e) {
            Log.e("ECCEProjectBlueApp", "Exception occurred: " + e.getMessage());
            return false;
        }

        return parseProfessorsToList(contents);
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

    private boolean parseProfessorsToList(String contents){
        ArrayList<Professor> professorList = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(contents);
            JSONArray professorArray = object.getJSONArray("professors");
            for(int i=0; i<professorArray.length(); i++){
                professorList.add(getProfessorFromJSONObject(professorArray.getJSONObject(i).toString()));
            }
        } catch (Exception e){
            Log.e("ECCEProjectBlueApp", "Exception occurred: " + e.getMessage());
            return false;
        }

        mProfessorList = professorList;
        return true;
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

    private Professor getProfessorFromJSONObject (String contents){
        try {
            JSONObject object = new JSONObject(contents);

            //Get professor name
            Professor professor = new Professor(object.getString("professorname"));

            //Get general review list
            JSONArray generalReviewArray = object.getJSONArray("generalreviews");
            for(int i=0; i<generalReviewArray.length(); i++){
                Review review = getReviewFromJSONObject(generalReviewArray.getJSONObject(i).toString());
                professor.addGeneralReview(review);
            }

            //Get course list
            JSONArray courseArray = object.getJSONArray("courses");
            for(int i=0; i<courseArray.length(); i++){
                Course course = getCourseFromJSONObject(courseArray.getJSONObject(i).toString());
                professor.addCourse(course);
            }

            return professor;
        } catch (Exception e){
            Log.e("ECCEProjectBlueApp", "Exception occurred: " + e.getMessage());
            return null;
        }
    }
    private Course getCourseFromJSONObject (String contents){
        try {
            JSONObject object = new JSONObject(contents);
            Course course = new Course(object.getString("coursetitle"));
            JSONArray reviewArray = object.getJSONArray("reviews");
            for(int i=0; i<reviewArray.length(); i++){
                Review review = getReviewFromJSONObject(reviewArray.getJSONObject(i).toString());
                course.addReview(review);
            }
            return course;
        } catch (Exception e){
            Log.e("ECCEProjectBlueApp", "Exception occurred: " + e.getMessage());
            return null;
        }
    }
    private Review getReviewFromJSONObject (String contents){
        try {
            JSONObject object = new JSONObject(contents);
            String username = object.getString("username");
            String comment = object.getString("comment");
            int rating  = object.getInt("rating");
            boolean recommend = object.getBoolean("recommend");
            return new Review (username, comment, rating, recommend);
        } catch (Exception e){
            Log.e("ECCEProjectBlueApp", "Exception occurred: " + e.getMessage());
            return null;
        }
    }

    private JSONObject convertUserToJSONObject(User user){
        JSONObject object = new JSONObject();
        try {
            object.put("username", user.getUsername());
            object.put("password", user.getPassword());
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