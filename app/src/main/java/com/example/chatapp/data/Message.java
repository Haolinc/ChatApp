package com.example.chatapp.data;

public class Message {
    String name, text, id;
    long time;


    public Message(){
    }

    public Message(String name, String text, long time, String id){
        this.name = name;
        this.text = text;
        this.time = time;
        this.id = id;
    }


    public String getName() {
        return name;
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
}
