package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    Database database = new Database();

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EditText username = findViewById(R.id.register_username_edittext);
        EditText password1 = findViewById(R.id.register_password_edittext);
        EditText password2 = findViewById(R.id.register_password_again_edittext);
        textView = findViewById(R.id.register_textview);
        findViewById(R.id.register_register_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password1String = password1.getText().toString();
                String password2String = password2.getText().toString();
                String usernameString = username.getText().toString();
                Log.d("password1String:", password1String);
                Log.d("password2String:", password2String);
                Log.d("usernameString:", usernameString);
                if (!password1String.equals(password2String)){
                    setTextView("Password Not Match!", Color.RED);
                }
                else
                    createAccount(usernameString, password1String);

            }
        });
    }

    //cloud database is asynchronous, so if want to change textview's text, we must
    //put it inside the get().addOnComplete so it will change AFTER the process is done
    private void createAccount(String username, String password){
        database.getUsers().document(username).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (!task.isSuccessful() || !task.getResult().exists()){
                            Map<String, Object> map = new HashMap<>();
                            map.put(username, password);
                            database.getUsers().document(username).set(map);
                            setTextView("Account Created!", Color.GREEN);
                        }
                        else
                            setTextView("Username Has Used!", Color.RED);
                    }
                });
    }

    private void setTextView(String text, int color){
        textView.setText(text);
        textView.setTextColor(color);
    }
}