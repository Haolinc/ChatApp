package com.example.chatapp;

import android.app.Activity;
import android.view.inputmethod.InputMethodManager;

public class Service {


    public static void hideKeyboard(Activity activity){
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getWindow().getDecorView().getRootView().getWindowToken(), 0);
    }


}
