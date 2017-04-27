package com.example.cyrusdeapolonia.ecceprojectblue;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ImageButton imgbtnProf = (ImageButton) findViewById(R.id.imgbtn_prof);
        imgbtnProf.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent launchtIntent = new Intent(HomeActivity.this, ProfessorListActivity.class);
                        startActivity(launchtIntent);
                    }
                }
        );

        ImageButton imggbtnCourse = (ImageButton) findViewById(R.id.imgbtn_course);
        imggbtnCourse.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent launchIntent = new Intent(HomeActivity.this, CourseListActivity.class);
                        startActivity(launchIntent);
                    }
                }
        );
    }
}