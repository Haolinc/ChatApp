package com.example.chatapp.mainFragments.profile;

import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.R;
import com.example.chatapp.mainFragments.profile.NameChangeActivity;

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
                newHolder.setUpText("User Photo");
                newHolder.setUpButton(0);
                break;
            case 1:
                newHolder.setUpText("Name");
                newHolder.setUpButton(1);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    public class ProfileAdaptor extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;
        Context context;

        public ProfileAdaptor(@NonNull View itemView, Context context) {
            super(itemView);
            textView = itemView.findViewById(R.id.profile_fragment_listitem_text);
            imageView = itemView.findViewById(R.id.profile_fragment_photo);
            imageView.setImageResource(R.drawable.userprofileimg);
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
                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent i = new Intent(context, NameChangeActivity.class);
                            context.startActivity(i);
                        }
                    });
                    hidePhoto();
                    break;
            }
        }

        private void hidePhoto(){
            imageView.setVisibility(View.GONE);
        }

    }

}
