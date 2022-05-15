package com.example.chatapp.data;

public class ChatFragmentData {
    int totalUnread;
    String id, latestText, targetName, latestTextName;

    public int getTotalUnread() {
        return totalUnread;
    }

    @Override
    public String toString() {
        return "ChatFragmentData{" +
                "totalUnread=" + totalUnread +
                ", targetID='" + id + '\'' +
                ", latestText='" + latestText + '\'' +
                ", targetName='" + targetName + '\'' +
                ", latestTextName='" + latestTextName + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public String getLatestText() {
        return latestText;
    }

    public String getLatestTextName(){return latestTextName;}

    public String getTargetName(){return targetName;}

    public ChatFragmentData(int totalUnread, String targetID, String targetName, String latestText, String latestTextName) {
        this.totalUnread = totalUnread;
        this.id = targetID;
        this.latestText = latestText;
        this.targetName = targetName;
        this.latestTextName = latestTextName;
    }

    public ChatFragmentData(){};
}
