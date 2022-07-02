package com.example.chatapp.mainFragments.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chatapp.R;
import com.example.chatapp.Service;
import com.example.chatapp.data.FireStoreDataReference;
import com.example.chatapp.data.PersonalInformation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class NameChangeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_change);

        EditText nameChangeEdit = findViewById(R.id.name_change_edit_text);
        nameChangeEdit.setText(PersonalInformation.name);

        findViewById(R.id.name_change_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Service.hideKeyboard(NameChangeActivity.this);
            }
        });

        //change name confirm button
        findViewById(R.id.name_change_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Service.setUpLoading(NameChangeActivity.this)) {
                    String currentName = nameChangeEdit.getText().toString();

                    //update name in firestore
                    FireStoreDataReference.getUsersReference().document(PersonalInformation.userDocument).update("name", currentName);
                    PersonalInformation.name = currentName;
                    Toast.makeText(NameChangeActivity.this, "Name Changed", Toast.LENGTH_SHORT).show();
                    Service.stopLoading(NameChangeActivity.this);
                    finish();
                }
                else
                    Service.setUpNetworkIssueToast(NameChangeActivity.this);
            }
        });
    }
}