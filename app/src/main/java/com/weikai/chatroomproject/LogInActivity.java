package com.weikai.chatroomproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/**
 * Created by 蘇暐凱 on 12/29/2015.
 */
public class LogInActivity extends Activity {
    private EditText etUsername;
    private EditText etPassword;
    public final static String authorized_name = "Jason";
    public final static String authorized_pass = "QWEasd123";
    //public final static String LOGIN_CONFIRMED = "LOGIN_CONFIRMED";
    Button btlogin;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btlogin = (Button) findViewById(R.id.btLogin);
        btlogin.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                // if client's socket is connected
                String name = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                boolean pass = checkLogIn(name,password);
                if(pass){
                    Intent i = new Intent(LogInActivity.this, MainActivity.class);
                    //i.putExtra(LOGIN_CONFIRMED, true);
                    startActivity(i);
                }else{
                    Toast.makeText(LogInActivity.this,
                                    "Your username and password are invalid!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
    public boolean checkLogIn(String name, String password){
        return (name.equals(authorized_name) && password.equals(authorized_pass));
    }

}
