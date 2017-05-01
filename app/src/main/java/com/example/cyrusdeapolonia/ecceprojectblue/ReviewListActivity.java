package com.example.cyrusdeapolonia.ecceprojectblue;

import android.content.Intent;
import android.os.AsyncTask;
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
    String mCourseTitle = null;
    String mProfessorName = null;
    UpdateList mUpdateList = null;
    ListView mLstReviewList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_list);

        Intent receivedIntent = getIntent();
        mCourseTitle = receivedIntent.getStringExtra("COURSE_TITLE");
        mProfessorName = receivedIntent.getStringExtra("PROFESSOR_NAME");

        mUpdateList = new UpdateList();
        mUpdateList.execute(mProfessorName, mCourseTitle);

        TextView txvProfessorAndCourse = (TextView)findViewById(R.id.txv_professorname);

        if(mCourseTitle == null){
            String text = "G.Reviews: " + mProfessorName;
            txvProfessorAndCourse.setText(text);
        }
        else {
            String text = mCourseTitle + " - "+ mProfessorName;
            txvProfessorAndCourse.setText(text);
        }

        final ECCEProjectBlueApp app = (ECCEProjectBlueApp) getApplication();
        Professor professor = app.getProfessor(mProfessorName);
        if( mCourseTitle == null ) mReviewList = professor.getGeneralReviews();
        else mReviewList = professor.getCourseReviews(mCourseTitle);

        updateRatingAndRecommendNo();

        mAdapter = new ArrayAdapter<Review>(this,
                android.R.layout.simple_list_item_1, mReviewList);

        mLstReviewList = (ListView) findViewById(R.id.lst_reviewlist);
        mLstReviewList.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        Intent launchIntent = new Intent(ReviewListActivity.this,
                                ReviewActivity.class);
                        launchIntent.putExtra("PROFESSOR_NAME", mProfessorName);
                        launchIntent.putExtra("COURSE_TITLE", mCourseTitle);
                        launchIntent.putExtra("POSITION", position);
                        startActivity(launchIntent);
                    }
                }
        );
        mLstReviewList.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        Button btnAddReview = (Button) findViewById(R.id.btn_addreview);
        btnAddReview.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(userAlreadyReviewed()){
                            Toast.makeText(ReviewListActivity.this, "You can only review once!", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Intent launchIntent = new Intent(ReviewListActivity.this,
                                    CreateReviewActivity.class);
                            launchIntent.putExtra("PROFESSOR_NAME", mProfessorName);
                            launchIntent.putExtra("COURSE_TITLE", mCourseTitle);
                            startActivity(launchIntent);
                        }
                    }
                }
        );
    }


    private class UpdateList extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            ECCEProjectBlueApp app = (ECCEProjectBlueApp) getApplication();
            return app.updateReviewListFromServer(params[0], params[1]);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result){
                ECCEProjectBlueApp app = (ECCEProjectBlueApp) getApplication();
                Professor professor = app.getProfessor(mProfessorName);
                if( mCourseTitle == null ) mReviewList = professor.getGeneralReviews();
                else mReviewList = professor.getCourseReviews(mCourseTitle);
                mAdapter = new ArrayAdapter<Review>(ReviewListActivity.this,
                        android.R.layout.simple_list_item_1, mReviewList);
                mLstReviewList.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                updateRatingAndRecommendNo();
                Toast.makeText(ReviewListActivity.this, "Review list refreshed", Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(result);
            return;
        }
    }

    private void updateRatingAndRecommendNo(){
        int sum = 0; int recommends = 0;
        for(int i=0; i<mReviewList.size(); i++){
            sum+=mReviewList.get(i).getRating();
            if(mReviewList.get(i).getRecommend()) recommends++;
        }

        TextView txvAverageRating = (TextView) findViewById(R.id.txv_averating);
        TextView txvRecommendNo = (TextView) findViewById(R.id.txv_recommendno);
        txvAverageRating.setText("Ave.Rating: "+(sum/mReviewList.size()));
        txvRecommendNo.setText(recommends + "/"+mReviewList.size()+" recommends this prof");
    }

    private boolean userAlreadyReviewed(){
        ECCEProjectBlueApp app = (ECCEProjectBlueApp) getApplication();
        String user = app.getUser();
        for(int i=0; i<mReviewList.size(); i++){
            if(mReviewList.get(i).getUsername().equals(user)){
                return true;
            }
        }
        return false;
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
            mUpdateList = new UpdateList();
            mUpdateList.execute(mProfessorName,mCourseTitle);
        }
        fromPause = false;
    }
}