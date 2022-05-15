package com.example.chatapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatapp.R;
import com.example.chatapp.data.PersonalInformation;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ContactProfileActivity extends AppCompatActivity {
    TextView name;
    Button addOrSendButton;
    ImageView image;
    String targetID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_profile);
        name = findViewById(R.id.contact_profile_name);
        addOrSendButton = findViewById(R.id.contact_profile_addFriendOrSend_button);
        image = findViewById(R.id.contact_profile_imageView);
        targetID = getIntent().getStringExtra("id");

        setUpViews();
    }

    private void setUpViews(){
        String targetName = getIntent().getStringExtra("name");
        name.setText(targetName);
        image.setImageResource(R.drawable.userprofileimg);
        if (getIntent().getBooleanExtra("isFriend", false)){
            addOrSendButton.setText("Send Message");
            addOrSendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(ContactProfileActivity.this, ChatPageActivity.class);
                    i.putExtra("id", targetID);
                    i.putExtra("name", targetName);
                    ContactProfileActivity.this.startActivity(i);
                    finish();
                }
            });
        }
        else{
            addOrSendButton.setText("Add Friend and Send Message");
            addOrSendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", targetID);
                    map.put("name", targetName);
                    FirebaseFirestore.getInstance()
                            .collection("users")
                            .document(PersonalInformation.id)
                            .collection("friends")
                            .document()
                            .set(map);
                    Toast.makeText(ContactProfileActivity.this, "Added", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }

    }

}