package com.example.cyrusdeapolonia.ecceprojectblue;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ProfessorListActivity extends AppCompatActivity {
    ArrayList<Professor> mProfessorList = new ArrayList<>();
    ArrayAdapter<Professor> mAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_list);

        Intent receivedIntent = getIntent();
        final String courseTitle = receivedIntent.getStringExtra("COURSE_TITLE");

        if(courseTitle != null){
            TextView txvCourseTitleProfessors = (TextView) findViewById(R.id.txv_coursetitle_professors);
            String text = courseTitle + " Professors:";
            txvCourseTitleProfessors.setText(text);
        }

        ECCEProjectBlueApp app = (ECCEProjectBlueApp) getApplication();

        if( courseTitle == null ) mProfessorList = app.getProfessorList();
        else {
            ArrayList<Professor> professorList = app.getProfessorList();
            for(int i=0; i<professorList.size(); i++){
                ArrayList<String> courses = professorList.get(i).getCourseTitles();
                for(int j=0; j<courses.size(); j++){
                    if( courses.get(j).equals(courseTitle) ){
                        mProfessorList.add( professorList.get(i) );
                        break;
                    }
                }
            }
        }

        mAdapter = new ArrayAdapter<Professor>(this,
                android.R.layout.simple_list_item_1, mProfessorList);

        ListView lstProfessorList = (ListView) findViewById(R.id.lst_professorlist);
        lstProfessorList.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        Intent launchIntent = new Intent(ProfessorListActivity.this,
                                ReviewListActivity.class);
                        String professorName = mProfessorList.get(position).getName();
                        launchIntent.putExtra("PROFESSOR_NAME", professorName);
                        launchIntent.putExtra("COURSE_TITLE", courseTitle);
                        startActivity(launchIntent);

                        return;
                    }
                }
        );

        lstProfessorList.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }
}
