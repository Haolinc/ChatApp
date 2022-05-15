package com.example.chatapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.chatapp.adapter.ChatFragmentAdapter;
import com.example.chatapp.adapter.ChatPageListAdapter;
import com.example.chatapp.data.ChatFragmentData;
import com.example.chatapp.data.Message;
import com.example.chatapp.data.PersonalInformation;
import com.example.chatapp.R;
import com.example.chatapp.Service;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ChatPageActivity extends AppCompatActivity {
    private RecyclerView messageRecycler;
    private ChatPageListAdapter messageAdapter;
    private List <Message> list = new LinkedList<>();
    private ChatFragmentData setupData;
    private EditText editText;


    // realtime firebase example code:
    DatabaseReference parentReference = FirebaseDatabase.getInstance().getReference();
    DatabaseReference userReference;
    DatabaseReference receiverReference;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_page);
        String targetID = getIntent().getStringExtra("id");
        userReference = parentReference.child(PersonalInformation.id).child(targetID);
        receiverReference = parentReference.child(targetID).child(PersonalInformation.id);


        messageRecycler = findViewById(R.id.chat_page_recycle_view);
        messageRecycler.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                new Service().hideKeyboard(ChatPageActivity.this);
                return true;
            }
        });

        //retrieve data from database

        userReference.addValueEventListener(new ValueEventListener() {
            //this method will call when started
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //set unread hint in recent chat page to 0 whenever start chatting
                userReference.child("setUp").child("totalUnread").setValue(0);
                List<Message> newList = new LinkedList<>();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    //ignore setup child
                    if (!dataSnapshot.getKey().equals("setUp"))
                        newList.add(dataSnapshot.getValue(Message.class));
                }
                list = newList;
                adapterSetting();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        editText = findViewById(R.id.chat_page_edittext);

        //send button
        findViewById(R.id.chat_page_send_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendToTarget(targetID);
            }
        });
    }


    private void sendToTarget(String targetID){
        receiverReference.child("setUp")
                .get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue(ChatFragmentData.class)!= null)
                    updateMessageDatabase(dataSnapshot.getValue(ChatFragmentData.class).getTotalUnread(), targetID);
                else
                    updateMessageDatabase(0, targetID);
            }
        });
    }

    private void updateMessageDatabase(int totalUnread, String targetID){
        long currentTime = Calendar.getInstance().getTimeInMillis();
        String targetName = getIntent().getStringExtra("name");


        Message msg = new Message(PersonalInformation.name, editText.getText().toString(),currentTime,PersonalInformation.id);
        setupData = new ChatFragmentData(totalUnread+1, targetID, targetName, msg.getText(), PersonalInformation.name);
        Log.d("targetname", setupData.getTargetName());
        Log.d("rData1", setupData.toString());

        userReference.push().setValue(msg);    // REAL-TIME DATABASE CODE

        userReference.child("setUp").setValue(setupData);

        userReference.child("setUp").child("targetName").setValue(targetName);


        ChatFragmentData setupData2 = new ChatFragmentData(totalUnread+1, PersonalInformation.id, PersonalInformation.name, msg.getText(), PersonalInformation.name);
        Log.d("rData2", setupData2.toString());
        receiverReference.push().setValue(msg);
        receiverReference.child("setUp").setValue(setupData2);
        editText.setText("");
    }


    private void adapterSetting(){
        messageAdapter = new ChatPageListAdapter(this, list);
        messageRecycler.setLayoutManager(new LinearLayoutManager(this));
        messageRecycler.setAdapter(messageAdapter);
        messageRecycler.scrollToPosition(list.size()-1);
    }

}