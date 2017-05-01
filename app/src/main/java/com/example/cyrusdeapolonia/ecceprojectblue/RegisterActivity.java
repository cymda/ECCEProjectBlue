package com.example.cyrusdeapolonia.ecceprojectblue;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {
    private RegisterUser mRegisterUser = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button btnSubmit = (Button) findViewById(R.id.btn_register);
        btnSubmit.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ECCEProjectBlueApp app = (ECCEProjectBlueApp) getApplication();
                        TextView txvUsername = (TextView) findViewById(R.id.edt_username);
                        TextView txvPassword = (TextView) findViewById(R.id.edt_password);
                        String username = txvUsername.getText().toString();
                        String password = txvPassword.getText().toString();

                        if(!username.equals("") && !password.equals("")){
                            mRegisterUser = new RegisterUser();
                            mRegisterUser.execute(username, password);
//                            THIS IS FOR FILE I/O
//                            if(app.usernameNotYetTaken(username)){
//                                app.addUser(username, password);
//                                app.saveUserList();
//
//                                finish();
//                            }
//                            else {
//                                Toast.makeText(RegisterActivity.this, "Username already taken", Toast.LENGTH_SHORT).show();
//                            }
                        }
                        else {
                            Toast.makeText(RegisterActivity.this, "Invalid username and/or password", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }

    private class RegisterUser extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            ECCEProjectBlueApp app = (ECCEProjectBlueApp) getApplication();
            boolean loadUserList = true;
            while(loadUserList){
                if(app.getUserListFromServer()) loadUserList = false;
            }
            if(!app.usernameNotYetTaken(params[0])) return false;

            boolean registerFailed = true;
            while(registerFailed){
                if(app.registerUserToServer(params[0], params[1]) && app.getUserListFromServer()){
                    if(app.userExists(params[0],params[1])) registerFailed = false;
                }
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result){
                Toast.makeText(RegisterActivity.this, "Successfully registered user", Toast.LENGTH_SHORT).show();
                finish();
            }
            else {
                Toast.makeText(RegisterActivity.this, "Username already taken", Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(result);
            return;
        }
    }
}