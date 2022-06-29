package com.example.chatapp.data;

import android.os.Parcel;
import android.os.Parcelable;

public class ChatFragmentData {
    int totalUnread;
    private String id, latestText, targetName, targetDocumentID;

    public int getTotalUnread() {
        return totalUnread;
    }

    public void setTotalUnread(int totalUnread) {
        this.totalUnread = totalUnread;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLatestText(String latestText) {
        this.latestText = latestText;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public void setTargetDocumentID(String targetDocumentID) {
        this.targetDocumentID = targetDocumentID;
    }

    public String getTargetDocumentID() {
        return targetDocumentID;
    }

    @Override
    public String toString() {
        return "ChatFragmentData{" +
                "totalUnread= " + totalUnread +
                ", targetID= " + id +
                ", latestText= " + latestText +
                ", targetName= " + targetName +
                ", targetDocumentID= " + targetDocumentID +
                '}';
    }

    public String getId() {
        return id;
    }

    public String getLatestText() {
        return latestText;
    }


    public String getTargetName(){return targetName;}

    public ChatFragmentData(int totalUnread, String targetID, String targetName, String latestText, String targetDocumentID) {
        this.totalUnread = totalUnread;
        this.id = targetID;
        this.latestText = latestText;
        this.targetName = targetName;
        this.targetDocumentID = targetDocumentID;
    }


    //MUST-HAVE
    public ChatFragmentData(){

    }



}
