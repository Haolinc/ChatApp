package com.example.chatapp.data;

public class ChatFragmentData {
    int totalUnread;
    String id, latestText;

    public int getTotalUnread() {
        return totalUnread;
    }

    public String getId() {
        return id;
    }

    public String getLatestText() {
        return latestText;
    }

    public ChatFragmentData(int totalUnread, String id, String latestText) {
        this.totalUnread = totalUnread;
        this.id = id;
        this.latestText = latestText;
    }

    public ChatFragmentData(){};
}
