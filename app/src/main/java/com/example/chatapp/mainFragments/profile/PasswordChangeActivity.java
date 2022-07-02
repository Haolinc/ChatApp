package com.example.chatapp.mainFragments.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chatapp.R;
import com.example.chatapp.Service;
import com.example.chatapp.data.FireStoreDataReference;
import com.example.chatapp.data.PersonalInformation;
import com.google.firebase.firestore.SetOptions;

public class PasswordChangeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_change);

        findViewById(R.id.password_change_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePassword();
            }
        });

    }

    public void changePassword(){
        String password1 = ((EditText)findViewById(R.id.password_change_newpassword_edittext)).getText().toString();
        String password2 = ((EditText)findViewById(R.id.password_change_confirmpassword_edittext)).getText().toString();

        if (password1.equals("") || password2.equals(""))
            Toast.makeText(this, "Password cannot be empty", Toast.LENGTH_SHORT).show();
        else if (!password1.equals(password2))
            Toast.makeText(this, "Password not matched", Toast.LENGTH_SHORT).show();
        else if (!Service.hasInternetConnection(this))
            Service.setUpNetworkIssueToast(this);
        else{
            FireStoreDataReference.getUsersReference()
                    .document(PersonalInformation.userDocument)
                    .update("password", password1);
            Toast.makeText(this, "Password Changed", Toast.LENGTH_SHORT).show();
            finish();
        }

    }
}