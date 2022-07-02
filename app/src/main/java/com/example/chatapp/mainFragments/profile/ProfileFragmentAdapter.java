package com.example.chatapp.mainFragments.profile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.R;
import com.example.chatapp.activity.LoginActivity;
import com.example.chatapp.data.FireStorageImageService;
import com.example.chatapp.data.PersonalInformation;

public class ProfileFragmentAdapter extends RecyclerView.Adapter {
    Context context;
    ProfileAdaptor newHolder;

    public ProfileFragmentAdapter(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProfileAdaptor(LayoutInflater.from(context).inflate(R.layout.fragment_profile_listitem ,parent, false), context);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        newHolder = (ProfileAdaptor) holder;
        switch(position){
            case 0:
                newHolder.setUpButton(0);
                break;
            case 1:
                newHolder.setUpButton(1);
                break;
            case 2:
                newHolder.setUpButton(2);
                break;
            case 3:
                newHolder.setUpButton(3);
                break;
            default:
                newHolder.setUpButton(Integer.MAX_VALUE);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class ProfileAdaptor extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;
        Context context;

        public ProfileAdaptor(@NonNull View itemView, Context context) {
            super(itemView);
            textView = itemView.findViewById(R.id.profile_fragment_listitem_text);
            imageView = itemView.findViewById(R.id.profile_fragment_photo);
            FireStorageImageService.setUserIcon(imageView, PersonalInformation.userDocument);
            this.context = context;
        }

        public void setUpText(String text){
            textView.setText(text);
        }

        public void setUpButton(int caseNum){
            switch(caseNum){
                case 0:
                    setUpText("User Photo");
                    break;
                case 1:
                    setUpText("Name");
                    startingActivity(NameChangeActivity.class);
                    break;
                case 2:
                    setUpText("Change User Photo");
                    startingActivity(UserIconActivity.class);
                    break;
                case 3:
                    setUpText("Change Password");
                    startingActivity(PasswordChangeActivity.class);
                    break;
                case Integer.MAX_VALUE:
                    setUpText("Logout");
                    removeSharedPreference();
                    break;
            }
        }

        private void startingActivity(Class<?> targetClass){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, targetClass);
                    context.startActivity(i);
                }
            });
            hidePhoto();
        }

        private void removeSharedPreference(){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences sharedPreferences = context.getSharedPreferences("Login", Context.MODE_PRIVATE);
                    sharedPreferences.edit().clear().apply();
                    Intent i = new Intent(context, LoginActivity.class);
                    context.startActivity(i);
                    ((Activity)context).finish();
                }
            });
            hidePhoto();
        }

        private void hidePhoto(){
            imageView.setVisibility(View.GONE);
        }


    }

}
