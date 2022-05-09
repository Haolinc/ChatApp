package com.example.chatapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.storage.FirebaseStorage;

public class LoginActivity extends AppCompatActivity {

    Database database = new Database();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //login button
        findViewById(R.id.login_login_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText idEditText = findViewById(R.id.login_id_edittext);
                EditText passwordEditText = findViewById(R.id.login_password_edittext);
                if (database.accountVerification(idEditText.getText().toString(), passwordEditText.getText().toString())){
                    Intent i = new Intent(getBaseContext(), MainActivity.class);
                    PersonalInformation.id = idEditText.getText().toString();
                    finish();
                    startActivity(i);
                }
                else{
                    TextView textView = findViewById(R.id.login_invalidlogin_textview);
                    textView.setText("Username or password does not match!");
                    textView.setTextColor(Color.RED);
                }
            }
        });

        //register button
        findViewById(R.id.login_register_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(), RegisterActivity.class);
                startActivity(i);
            }
        });

    }

}