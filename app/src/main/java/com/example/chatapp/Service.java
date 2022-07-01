package com.example.chatapp;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimatedImageDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.ConnectivityManager;
import android.os.Build;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;

public class Service {


    public static void hideKeyboard(Activity activity){
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getWindow().getDecorView().getRootView().getWindowToken(), 0);
    }

    private static LinearLayout layout;
    private static ImageView imageView;

    public static boolean hasInternetConnection(Context context){
        return ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
    }

    //set up spinning loading gif
    public static boolean setUpLoading(Context context){
        //if network is bad
        if (((ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() == null){
            return false;
        }

        //otherwise put loading
        imageView = new ImageView(context);
        layout = new LinearLayout(context);

        //imageview
        imageView.setImageResource(R.drawable.loading);
        ((ViewGroup)((Activity)context).getWindow().getDecorView().getRootView()).addView(imageView);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(150,150);
        layoutParams.gravity = Gravity.CENTER;
        imageView.setLayoutParams(layoutParams);
        Glide.with(context).load(R.drawable.loading).into(imageView);

        //empty layout to prevent clicking
        layout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        ((ViewGroup)((Activity)context).getWindow().getDecorView().getRootView()).addView(layout);
        return true;
    }

    //remove gif
    public static void stopLoading(Context context){
        ((ViewGroup)((Activity)context).getWindow().getDecorView().getRootView()).removeView(layout);
        ((ViewGroup)((Activity)context).getWindow().getDecorView().getRootView()).removeView(imageView);
    }

    //set up only one button dialogue
    public static void setUpOneOptionDialogue(Context context, String title, String content){
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(content)
                .setPositiveButton("OK", null)
                .setCancelable(true)
                .create();

        alertDialog.show();

        //set button to middle
        Button btn = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(btn.getLayoutParams());
        params.width = LinearLayout.LayoutParams.MATCH_PARENT;
        btn.setLayoutParams(params);
    }

    //internet connection issue toast
    public static void setUpNetworkIssueToast(Context context){
        Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
    }



}
