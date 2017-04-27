package com.example.cyrusdeapolonia.ecceprojectblue;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

public class CreateReviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_review);

        Intent receivedIntent = getIntent();
        final String courseTitle = receivedIntent.getStringExtra("COURSE_TITLE");
        final String professorName = receivedIntent.getStringExtra("PROFESSOR_NAME");

        TextView txvProfessorName = (TextView) findViewById(R.id.txv_professorname);
        TextView txvCourseTitle = (TextView) findViewById(R.id.txv_coursetitle);
        txvProfessorName.setText(professorName);
        txvCourseTitle.setText(courseTitle);

        Button btnSubmitReview = (Button) findViewById(R.id.btn_submitreview);
        btnSubmitReview.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ECCEProjectBlueApp app = (ECCEProjectBlueApp) getApplication();

                        EditText edtComment = (EditText) findViewById(R.id.edt_comment);
                        RatingBar rbrRating = (RatingBar) findViewById(R.id.rbr_rating);
                        CheckBox cbxRecommend = (CheckBox) findViewById(R.id.cbx_recommend);

                        String comment = edtComment.getText().toString();
                        int rating = rbrRating.getNumStars();
                        boolean recommend = cbxRecommend.isChecked();

                        Review newReview = new Review(app.getUser(), comment, rating, recommend);
                        app.addReview(newReview, professorName, courseTitle);
                        app.saveProfessorList();

                        finish();
                    }
                }
        );
    }
}
