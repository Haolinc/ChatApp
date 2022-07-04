package com.example.chatapp.mainFragments.recentChat;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.R;
import com.example.chatapp.data.FireStorageImageService;
import com.example.chatapp.data.UserData;
import com.example.chatapp.mainFragments.chat.ChatPageActivity;
import com.example.chatapp.data.RecentChatFragmentData;

import java.util.List;

public class RecentChatFragmentAdapter extends RecyclerView.Adapter{

    private final List<RecentChatFragmentData> recentMessageList;
    private final Context context;

    public RecentChatFragmentAdapter(Context context, List<RecentChatFragmentData> recentMessageList){
        this.context = context;
        this.recentMessageList = recentMessageList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MessageAdaptor(LayoutInflater.from(context).inflate(R.layout.fragment_chat_list_item, parent, false), context);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        RecentChatFragmentData recentMessage = recentMessageList.get(position);
        ((MessageAdaptor)holder).bind(recentMessage);
    }

    @Override
    public int getItemCount() {
        return recentMessageList.size();
    }

    private static class MessageAdaptor extends RecyclerView.ViewHolder{
        TextView unread, name, recent;
        View itemView;
        Context context;
        ImageView imageView;

        //initialization
        public MessageAdaptor(@NonNull View itemView, Context context) {
            super(itemView);
            unread = itemView.findViewById(R.id.chat_fragment_list_unread_textview);
            name = itemView.findViewById(R.id.chat_fragment_list_name_textview);
            recent = itemView.findViewById(R.id.chat_fragment_list_recent_textview);
            imageView = itemView.findViewById(R.id.chat_fragment_list_imageView);
            this.itemView = itemView;
            this.context = context;
        }

        //setup textview and onClickListener
        private void bind(RecentChatFragmentData setupContent){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, ChatPageActivity.class);
                    i.putExtra("userData", new UserData(setupContent.getId(), setupContent.getTargetName(), setupContent.getTargetDocumentID()));
                    context.startActivity(i);
                }
            });
            if (setupContent.getTotalUnread()>99)
                unread.setText("99+");
            else
                unread.setText(setupContent.getTotalUnread()+"");

            String latestMessage = setupContent.getLatestText();
            if (latestMessage.length()>40)
                recent.setText(latestMessage.substring(0, 40) + "...");
            else
                recent.setText(latestMessage);

            FireStorageImageService.setUserIcon(imageView, setupContent.getTargetDocumentID());

            name.setText(setupContent.getTargetName());
        }

    }
}
