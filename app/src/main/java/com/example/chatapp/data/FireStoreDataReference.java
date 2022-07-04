package com.example.chatapp.data;

import android.content.Context;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class FireStoreDataReference {

    public static CollectionReference getUsersReference(){
        return FirebaseFirestore.getInstance().collection("users");
    }

    public static CollectionReference getFriendListReference(Context context){
        return FirebaseFirestore.getInstance().collection("users")
                .document(new UserInfo(context).getDocumentID())
                .collection("friends");
    }

}
