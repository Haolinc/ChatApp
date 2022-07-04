package com.example.chatapp.data;

import android.content.Context;
import android.content.SharedPreferences;

public class UserInfo {
    SharedPreferences sharedPreferences;
    public UserInfo(Context context){
        sharedPreferences = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
    }

    public String getDocumentID(){
        return sharedPreferences.getString("documentID", null);
    }
    public String getID(){
        return sharedPreferences.getString("id", null);
    }
    public String getName(){
        return sharedPreferences.getString("name", null);
    }
    public String getUserIconID(){
        return sharedPreferences.getString("userIcon", null);
    }

    public void setDocumentID(String documentID){
        sharedPreferences.edit().putString("documentID" ,documentID).apply();
    }
    public void setID(String id){
        sharedPreferences.edit().putString("id" ,id).apply();
    }
    public void setName(String name){
        sharedPreferences.edit().putString("name" ,name).apply();
    }
    public void setUserIconID(String userIconID){
        sharedPreferences.edit().putString("userIcon" ,userIconID).apply();
    }
}
