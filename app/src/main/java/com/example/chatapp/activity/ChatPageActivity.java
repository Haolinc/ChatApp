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
    private ChatFragmentData receiverData;
    private EditText editText;
    private Service service;
    boolean firstTime = true;


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
        receiverReference = parentReference.child(targetID);


        messageRecycler = findViewById(R.id.chat_page_recycle_view);
        messageRecycler.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                service = new Service();
                service.hideKeyboard(ChatPageActivity.this);
                return true;
            }
        });

        //retrieve data from database

        userReference.addValueEventListener(new ValueEventListener() {
            //this method will call when started
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int count = 1;
                    userReference.child("setUp").child("totalUnread").setValue(0);
                List<Message> newList = new LinkedList<>();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if (!dataSnapshot.getKey().equals("setUp"))
                        newList.add(dataSnapshot.getValue(Message.class));
                }
                list = newList;
//                if (firstTime) {
//                    firstTime = false;
//                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                        if (dataSnapshot.getKey().equals("setUp")){
//                            continue;
//                        }
//                        list.add(dataSnapshot.getValue(Message.class));
//                    }
//                } else {
//                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                        Message msg = dataSnapshot.getValue(Message.class);
//                        Log.d("chatpagedebug: ", ""+msg.getText());
//                        if (count == snapshot.getChildrenCount()-1 && !msg.getId().equals(PersonalInformation.id)){
//                            list.add(dataSnapshot.getValue(Message.class));
//
//                        }
//
//                        count++;
//                    }
//                }

                adapterSetting();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        editText = findViewById(R.id.chat_page_edittext);
        //button
        findViewById(R.id.chat_page_send_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTotalUnread(targetID);
            }
        });
    }


    private void getTotalUnread(String targetID){
        receiverReference.child(PersonalInformation.id).child("setUp")
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
        Message msg = new Message("", editText.getText().toString(),currentTime,PersonalInformation.id);
        receiverData = new ChatFragmentData(totalUnread+1, "", msg.getText());
        userReference.push().setValue(msg);    // REAL-TIME DATABASE CODE
        receiverReference.child(PersonalInformation.id).child("setUp").setValue(receiverData);
        userReference.child("setUp").setValue(receiverData);
        receiverReference.child(PersonalInformation.id).push().setValue(msg);

        editText.setText("");
    }


    private void adapterSetting(){
        messageAdapter = new ChatPageListAdapter(this, list);
        messageRecycler.setLayoutManager(new LinearLayoutManager(this));
        messageRecycler.setAdapter(messageAdapter);
        messageRecycler.scrollToPosition(list.size()-1);
    }


    public void hideKeyboard(View view){
        InputMethodManager inputMethodManager = (InputMethodManager)  this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(this.getWindow().getDecorView().getRootView().getWindowToken(), 0);
    }
}