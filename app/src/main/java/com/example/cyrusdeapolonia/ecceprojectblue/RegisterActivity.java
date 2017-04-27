package com.example.cyrusdeapolonia.ecceprojectblue;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

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
                            if(app.usernameNotYetTaken(username)){
                                app.addUser(username, password);
                                app.saveUserList();
                                finish();
                            }
                            else {
                                Toast.makeText(RegisterActivity.this, "Username already taken", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Toast.makeText(RegisterActivity.this, "Invalid username and/or password", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }
}