package com.example.chatapp.data;

import androidx.annotation.NonNull;

public class Message {
    String text, id;
    long time;


    public Message(){
    }

    public Message(String id, String text, long time){
        this.id = id;
        this.text = text;
        this.time = time;
    }


    public String getText() {
        return text;
    }

    public long getTime() {
        return time;
    }
    public String getId() {
        return id;
    }

    @NonNull
    @Override
    public String toString(){
        return "text: " + text + ", time: " + time;
    }
}
