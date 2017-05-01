package com.example.cyrusdeapolonia.ecceprojectblue;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ReviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        Intent receivedIntent = getIntent();
        final String professorName = receivedIntent.getStringExtra("PROFESSOR_NAME");
        final String courseTitle = receivedIntent.getStringExtra("COURSE_TITLE");
        final int index = receivedIntent.getIntExtra("POSITION", 0);

        TextView txvProfessorName = (TextView) findViewById(R.id.txv_professorname);
        txvProfessorName.setText(professorName);

        TextView txvCourseTitle = (TextView) findViewById(R.id.txv_coursetitle);

        ECCEProjectBlueApp app = (ECCEProjectBlueApp) getApplication();
        Professor professor = app.getProfessor(professorName);
        ArrayList<Review> reviewList;
        if( courseTitle == null ){
            reviewList = professor.getGeneralReviews();
            String general = "General";
            txvCourseTitle.setText(general);
        }
        else {
            reviewList = professor.getCourseReviews(courseTitle);
            txvCourseTitle.setText(courseTitle);
        }
        Review review = reviewList.get(index);
        String username = review.getUsername();
        String comment = review.getComment();
        int rating = review.getRating();
        boolean recommend = review.getRecommend();

        TextView txvUsername = (TextView) findViewById(R.id.txv_username);
        TextView txvComment = (TextView) findViewById(R.id.txv_comment);
        TextView txvRating = (TextView) findViewById(R.id.txv_rating);    //pwede pa palitan ng unclickable rating bar
        TextView txvRecommend = (TextView) findViewById(R.id.txv_recommend); //pwede pa palitan ng unclickable check box

        String reviewby = "Review by: "+username;
        txvUsername.setText(reviewby);
        txvComment.setText(comment);
        txvRating.setText(Integer.toString(rating));
        txvRecommend.setText((recommend)?"Yes":"No");
    }
}
