package com.example.chatapp.data;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class FireStoreDataReference {

    public static CollectionReference getUsersReference(){
        return FirebaseFirestore.getInstance().collection("users");
    }

    public static CollectionReference getFriendListReference(){
        return FirebaseFirestore.getInstance().collection("users")
                .document(PersonalInformation.userDocument)
                .collection("friends");
    }

}
