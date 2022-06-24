package com.example.chatapp.mainFragments.contact;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chatapp.R;
import com.example.chatapp.mainFragments.chat.ChatPageActivity;
import com.example.chatapp.data.FriendData;

import java.util.List;

public class ContactFragmentAdapter extends RecyclerView.Adapter {

    private final List<FriendData> friendList;
    private final Context context;

    public ContactFragmentAdapter(Context context, List<FriendData> friendList){
        this.context = context;
        this.friendList = friendList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FriendAdaptor(LayoutInflater.from(context).inflate(R.layout.fragment_contact_listitem, parent, false), context);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        FriendData friends = friendList.get(position);
        ((FriendAdaptor)holder).bind(friends);
    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }

    private class FriendAdaptor extends  RecyclerView.ViewHolder{
        Context context;
        ImageView image;
        TextView name;
        View itemView;
        public FriendAdaptor(@NonNull View itemView, Context context) {
            super(itemView);
            this.context = context;
            image = itemView.findViewById(R.id.contact_fragment_listitem_image);
            name = itemView.findViewById(R.id.contact_fragment_listitem_name);
            this.itemView = itemView;
        }

        private void bind(FriendData friendData){
            image.setImageResource(R.drawable.userprofileimg);
            name.setText(friendData.getName());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, ChatPageActivity.class);
                    i.putExtra("id", friendData.getId());
                    i.putExtra("name", friendData.getName());
                    context.startActivity(i);
                }
            });
        }
    }
}