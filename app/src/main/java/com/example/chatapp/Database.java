
package com.example.chatapp;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class Database {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private boolean accountLoginStatus;

    CollectionReference users = db.collection("users");


    public CollectionReference getUsers(){
        return users;
    }

    public boolean accountVerification(String username, String password){
        users.whereEqualTo(username, password).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        accountLoginStatus = task.isSuccessful();
                    }
                });
        return accountLoginStatus;
    }

}
