package com.example.chatapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.chatapp.R;
import com.example.chatapp.data.UserInfo;
import com.example.chatapp.mainFragments.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginActivity extends AppCompatActivity {

    CollectionReference users = FirebaseFirestore.getInstance().collection("users");
    UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userInfo = new UserInfo(this);
        if (userInfo.getID() != null){
            Intent i = new Intent(getBaseContext(), MainActivity.class);

//            PersonalInformation.name = sharedPreferences.getString("name", null);
//            PersonalInformation.id = sharedPreferences.getString("id", null);
//            PersonalInformation.userDocument = sharedPreferences.getString("documentID", null);
//            PersonalInformation.userIconCode = sharedPreferences.getString("userIcon", null);

            startActivity(i);
            finish();
        }

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
                                    DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);

                                    //put things into preference
                                    userInfo.setName(documentSnapshot.getString("name"));
                                    userInfo.setID(username);
                                    userInfo.setDocumentID(documentSnapshot.getId());
                                    userInfo.setUserIconID(documentSnapshot.getString("userIcon"));

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