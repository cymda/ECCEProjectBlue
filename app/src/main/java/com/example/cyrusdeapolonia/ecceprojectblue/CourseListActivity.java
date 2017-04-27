package com.example.cyrusdeapolonia.ecceprojectblue;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class CourseListActivity extends AppCompatActivity {
    ArrayList<String> mCourseList = new ArrayList<>();
    ArrayAdapter<String> mAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);

        ECCEProjectBlueApp app = (ECCEProjectBlueApp) getApplication();
        ArrayList<Professor> professorList = app.getProfessorList();

        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mCourseList);

        for(int i=0; i<professorList.size(); i++){
            ArrayList<String> courses = professorList.get(i).getCourseTitles();
            for(int j=0; j<courses.size(); j++){
                if( !mCourseList.contains(courses.get(j)) ) mCourseList.add(courses.get(j));
            }
        }

        ListView lstCourseList = (ListView) findViewById(R.id.lst_courselist);
        lstCourseList.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        Intent launchIntent = new Intent(CourseListActivity.this,
                                ProfessorListActivity.class);
                        launchIntent.putExtra("COURSE_TITLE", mCourseList.get(position));
                        startActivity(launchIntent);
                        return;
                    }
                }
        );

        lstCourseList.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

    }
}