
package com.example.chatapp.data;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;


public class Database {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    CollectionReference users = db.collection("users");


    public CollectionReference getUsers(){
        return users;
    }

}
