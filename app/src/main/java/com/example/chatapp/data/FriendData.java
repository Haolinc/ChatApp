package com.example.chatapp.data;

public class FriendData {
    String id, name, documentID;

    public String getId() {
        return id;
    }
    public String getDocumentID() {return documentID;}
    public String getName() {
        return name;
    }

    public FriendData(String id, String name, String documentID) {
        this.id = id;
        this.name = name;
        this.documentID = documentID;
    }
}
