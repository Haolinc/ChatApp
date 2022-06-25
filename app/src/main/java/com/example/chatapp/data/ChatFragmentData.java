package com.example.chatapp.data;

public class ChatFragmentData {
    int totalUnread;
    private String id, latestText, targetName;

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


    @Override
    public String toString() {
        return "ChatFragmentData{" +
                "totalUnread=" + totalUnread +
                ", targetID='" + id + '\'' +
                ", latestText='" + latestText + '\'' +
                ", targetName='" + targetName + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public String getLatestText() {
        return latestText;
    }


    public String getTargetName(){return targetName;}

    public ChatFragmentData(int totalUnread, String targetID, String targetName, String latestText) {
        this.totalUnread = totalUnread;
        this.id = targetID;
        this.latestText = latestText;
        this.targetName = targetName;
    }

    public ChatFragmentData(){};
}
