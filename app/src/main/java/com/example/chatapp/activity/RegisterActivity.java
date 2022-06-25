package com.example.chatapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatapp.Service;
import com.example.chatapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {


    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EditText username = findViewById(R.id.register_username_edittext);
        EditText password1 = findViewById(R.id.register_password_edittext);
        EditText password2 = findViewById(R.id.register_password_again_edittext);
        EditText name = findViewById(R.id.register_name_edittext);
        findViewById(R.id.register_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Service.hideKeyboard(RegisterActivity.this);
            }
        });
        textView = findViewById(R.id.register_textview);
        findViewById(R.id.register_register_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password1String = password1.getText().toString();
                String password2String = password2.getText().toString();
                String usernameString = username.getText().toString();
                String nameString = name.getText().toString();
                Log.d("password1String:", password1String);
                Log.d("password2String:", password2String);
                Log.d("usernameString:", usernameString);
                if (!password1String.equals(password2String)){
                    setTextView("Password Not Match!", Color.RED);
                }
                else
                    createAccount(usernameString, password1String, nameString);

            }
        });
    }

    //cloud database is asynchronous, so if want to change textview's text, we must
    //put it inside the get().addOnComplete so it will change AFTER the process is done
    private void createAccount(String username, String password, String name){
        CollectionReference users = FirebaseFirestore.getInstance().collection("users");
        users.whereEqualTo("id", username)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            if (task.getResult().isEmpty()){
                                Map<String, Object> map = new HashMap<>();
                                map.put("password", password);
                                map.put("name", name);
                                map.put("id", username);

                                users.document().set(map);
                                Toast.makeText(RegisterActivity.this, "Account Created!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            else
                                setTextView("Username Has Used!", Color.RED);
                        }
                        else
                            Toast.makeText(RegisterActivity.this, "An error has occur", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setTextView(String text, int color){
        textView.setText(text);
        textView.setTextColor(color);
    }
}