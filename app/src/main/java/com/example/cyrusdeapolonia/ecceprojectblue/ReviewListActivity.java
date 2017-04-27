package com.example.cyrusdeapolonia.ecceprojectblue;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ReviewListActivity extends AppCompatActivity {
    ArrayList<Review> mReviewList = null;
    ArrayAdapter<Review> mAdapter = null;
    boolean fromPause = false;

    //gawing global yung coursetitle at professor name. pati sa ibang activities na nakalist view

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_list);

        Intent receivedIntent = getIntent();
        final String courseTitle = receivedIntent.getStringExtra("COURSE_TITLE");
        final String professorName = receivedIntent.getStringExtra("PROFESSOR_NAME");

        TextView txvProfessorAndCourse = (TextView)findViewById(R.id.txv_professorname);

        if(courseTitle == null){
            String text = "G.Reviews: " + professorName;
            txvProfessorAndCourse.setText(text);
        }
        else {
            String text = courseTitle + " - "+ professorName;
            txvProfessorAndCourse.setText(text);
        }

        ECCEProjectBlueApp app = (ECCEProjectBlueApp) getApplication();
        Professor professor = app.getProfessor(professorName);
        if( courseTitle == null ) mReviewList = professor.getGeneralReviews();
        else mReviewList = professor.getCourseReviews(courseTitle);

        mAdapter = new ArrayAdapter<Review>(this,
                android.R.layout.simple_list_item_1, mReviewList);

        ListView lstReviewList = (ListView) findViewById(R.id.lst_reviewlist);
        lstReviewList.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        Intent launchIntent = new Intent(ReviewListActivity.this,
                                ReviewActivity.class);
                        launchIntent.putExtra("PROFESSOR_NAME", professorName);
                        launchIntent.putExtra("COURSE_TITLE", courseTitle);
                        launchIntent.putExtra("POSITION", position);
                        startActivity(launchIntent);
                    }
                }
        );
        lstReviewList.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        Button btnAddReview = (Button) findViewById(R.id.btn_addreview);
        btnAddReview.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent launchIntent = new Intent(ReviewListActivity.this,
                                CreateReviewActivity.class);
                        launchIntent.putExtra("PROFESSOR_NAME", professorName);
                        launchIntent.putExtra("COURSE_TITLE", courseTitle);
                        startActivity(launchIntent);
                    }
                }
        );
    }

    @Override
    protected void onPause() {
        super.onPause();
        fromPause = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(fromPause){
            mAdapter.notifyDataSetChanged();
        }
        fromPause = false;
    }
}
