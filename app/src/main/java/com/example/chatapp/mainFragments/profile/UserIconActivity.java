package com.example.chatapp.mainFragments.profile;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.chatapp.R;
import com.example.chatapp.data.FireStorageImageService;
import com.example.chatapp.data.UserInfo;

public class UserIconActivity extends AppCompatActivity {
    final Uri[] filePath = new Uri[1];
    //must be created before onCreate
    ActivityResultLauncher<String> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
        @Override
        public void onActivityResult(Uri result) {
            if (result != null)
                filePath[0] = result;
            ((ImageView)findViewById(R.id.user_icon_image)).setImageURI(filePath[0]);
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_icon);

        //default image
        FireStorageImageService.setUserIcon(findViewById(R.id.user_icon_image), new UserInfo(this).getDocumentID());

        //select button
        findViewById(R.id.user_icon_select_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activityResultLauncher.launch("image/*");
            }
        });




        //upload button
        findViewById(R.id.user_icon_upload_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FireStorageImageService.uploadNewUserIcon(UserIconActivity.this, filePath[0]);
            }
        });


    }
}