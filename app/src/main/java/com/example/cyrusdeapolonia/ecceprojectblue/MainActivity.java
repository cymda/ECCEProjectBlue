package com.example.cyrusdeapolonia.ecceprojectblue;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ECCEProjectBlueApp app = (ECCEProjectBlueApp) getApplication();
        app.initializeLists();

        if(app.loadUserList() && app.loadProfessorList())
            Toast.makeText(MainActivity.this, "File loaded", Toast.LENGTH_SHORT).show();


        final EditText edtUsername = (EditText) findViewById(R.id.edt_username);
        final EditText edtPassword = (EditText) findViewById(R.id.edt_password);

        String userApp = app.getUserApp();
        edtUsername.setText(userApp);
        edtPassword.setText(app.getUserPasswordApp());

        final CheckBox cbxRememberMe = (CheckBox) findViewById(R.id.cbx_remember);

        if(!userApp.equals("")) cbxRememberMe.setChecked(true);

        Button btnLogin = (Button) findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String username = edtUsername.getText().toString();
                        String password = edtPassword.getText().toString();

                        if( app.userExists(username, password) ) {
                            app.setUser(username);
                            if(cbxRememberMe.isChecked()) app.saveUserApp(username, password);
                            else app.saveUserApp("", "");
                            Intent launchintent = new Intent(MainActivity.this, HomeActivity.class);
                            startActivity(launchintent);
                        }
                        else {
                            Toast.makeText(MainActivity.this, "Invalid username and/or password", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        Button btnRegister = (Button) findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent launchintent = new Intent(MainActivity.this, RegisterActivity.class);
                        startActivity(launchintent);
                    }
                }
        );
    }
}