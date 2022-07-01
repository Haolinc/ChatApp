package com.example.chatapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.chatapp.Service;
import com.example.chatapp.data.PersonalInformation;
import com.example.chatapp.R;
import com.example.chatapp.mainFragments.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginActivity extends AppCompatActivity {

    CollectionReference users = FirebaseFirestore.getInstance().collection("users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewById(R.id.login_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Service.hideKeyboard(LoginActivity.this);
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
        //remember to stop after
        if (Service.setUpLoading(this))
            users.whereEqualTo("id", username)
                    .whereEqualTo("password", password)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()){
                                if (!task.getResult().isEmpty()){
                                    Intent i = new Intent(getBaseContext(), MainActivity.class);
                                    PersonalInformation.name = task.getResult().getDocuments().get(0).getString("name");
                                    PersonalInformation.id = username;
                                    PersonalInformation.userDocument = task.getResult().getDocuments().get(0).getId();
                                    Log.d("userdoc", PersonalInformation.userDocument);
                                    i.putExtra("id", username);
                                    startActivity(i);
                                    finish();
                                }
                                else{
                                    Toast.makeText(LoginActivity.this, "Username or password does not match", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else
                                Toast.makeText(LoginActivity.this, "An error has occur", Toast.LENGTH_SHORT).show();
                            Service.stopLoading(LoginActivity.this);
                        }
                    });
        else{
            Service.setUpNetworkIssueToast(this);
        }

    }

}