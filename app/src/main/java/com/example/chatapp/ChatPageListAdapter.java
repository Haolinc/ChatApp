package com.example.chatapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChatPageListAdapter extends RecyclerView.Adapter {
    Context context;
    List<Message> messageList;
    private static final int SENT = 1;
    private static final int RECEIVE = 2;

    public ChatPageListAdapter(Context context, List<Message> messageList){
        this.context = context;
        this.messageList = messageList;
    }

    @Override
    public int getItemViewType(int position){
        Message message = messageList.get(position);
        if (message.getId().equals(PersonalInformation.id))
            return SENT;
        else
            return RECEIVE;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == SENT){
            view = LayoutInflater.from(context)
                    .inflate(R.layout.activity_chat_me, parent, false);
            return new SendAdapter(view);
        }
        else if (viewType == RECEIVE){
            view = LayoutInflater.from(context)
                    .inflate(R.layout.activity_chat_other, parent, false);
            return new ReceiveAdapter(view);
        }
        return null;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messageList.get(position);
        Log.d("onBindHolder: ", ""+position);
        switch (holder.getItemViewType()){
            case SENT:
                ((SendAdapter) holder).bind(message);
                break;
            case RECEIVE:
                ((ReceiveAdapter) holder).bind(message);
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }



    private class SendAdapter extends RecyclerView.ViewHolder {
        TextView chatmeText, chatmeTime;
        public SendAdapter(@NonNull View itemView) {
            super(itemView);
            chatmeText = (TextView) itemView.findViewById(R.id.chatme_text);
            chatmeTime = (TextView)itemView.findViewById(R.id.chatme_time);
        }

        void bind(Message message){
            chatmeTime.setText(new DateDisplay(message.getTime()).returnCurrentTime());
            chatmeText.setText(message.getText());
        }
    }

    private class ReceiveAdapter extends RecyclerView.ViewHolder{
        TextView chatotherName, chatotherText, chatotherTime;
        public ReceiveAdapter(@NonNull View itemView) {
            super(itemView);
            chatotherText = (TextView) itemView.findViewById(R.id.chatother_text);
            chatotherTime = (TextView) itemView.findViewById(R.id.chatother_time);
            chatotherName = (TextView) itemView.findViewById(R.id.chatother_name);
        }

        void bind(Message message){
            chatotherName.setText(message.getName());
            chatotherText.setText(message.getText());
            chatotherTime.setText(new DateDisplay(message.getTime()).returnCurrentTime());
        }
    }
}