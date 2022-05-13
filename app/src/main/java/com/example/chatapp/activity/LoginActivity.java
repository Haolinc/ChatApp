package com.example.chatapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.chatapp.Service;
import com.example.chatapp.data.Database;
import com.example.chatapp.data.PersonalInformation;
import com.example.chatapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginActivity extends AppCompatActivity {

    CollectionReference users = new Database().getUsers();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewById(R.id.login_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Service service = new Service();
                service.hideKeyboard(LoginActivity.this);
            }
        });

        //login button
        findViewById(R.id.login_login_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText idEditText = findViewById(R.id.login_id_edittext);
                EditText passwordEditText = findViewById(R.id.login_password_edittext);
                accountVerification(idEditText.getText().toString(), passwordEditText.getText().toString());
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

    private void accountVerification(String username, String password){
        users.document(username).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.getResult().getString("password") == null){
                    TextView textView = findViewById(R.id.login_invalidlogin_textview);
                    textView.setText("Username does not exist!");
                    textView.setTextColor(Color.RED);
                }
                else {
                    if (task.getResult().getString("password").equals(password)){
                        Intent i = new Intent(getBaseContext(), MainActivity.class);
                        PersonalInformation.id = username;
                        i.putExtra("id", username);
                        startActivity(i);
                        finish();
                    }
                    else{
                        TextView textView = findViewById(R.id.login_invalidlogin_textview);
                        textView.setText("Password does not match!");
                        textView.setTextColor(Color.RED);
                    }
                }
            }
        });
    }

}