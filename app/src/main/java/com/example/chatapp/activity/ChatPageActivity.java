package com.example.chatapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.chatapp.adapter.ChatPageListAdapter;
import com.example.chatapp.data.Message;
import com.example.chatapp.data.PersonalInformation;
import com.example.chatapp.R;
import com.example.chatapp.Service;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class ChatPageActivity extends AppCompatActivity {
    private RecyclerView messageRecycler;
    private ChatPageListAdapter messageAdapter;
    private List<Message> list = new LinkedList<>();
    private EditText editText;
    private Service service;
    boolean firstTime = true;


    // realtime firebase example code:
    DatabaseReference parentReference = FirebaseDatabase.getInstance().getReference();
    DatabaseReference childReference = parentReference.child(PersonalInformation.id);
    DatabaseReference receiverReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_page);
        String targetID = getIntent().getStringExtra("id");
        receiverReference = parentReference.child(targetID);

//        service = new Service();
//        findViewById(R.id.chat_page_recycle_view).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                service.hideKeyboard(ChatPageActivity.this);
//            }
//        });


        messageRecycler = findViewById(R.id.chat_page_recycle_view);

        //retrieve data from database

        childReference.addValueEventListener(new ValueEventListener() {
            //this method will call when started
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount() == 0) {
                    list = new LinkedList<>();
                }

                else if (firstTime) {
                    firstTime = false;
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        list.add(dataSnapshot.getValue(Message.class));
                    }
                } else {
                    int count = 1;
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Message msg = dataSnapshot.getValue(Message.class);
                        if (count == snapshot.getChildrenCount() && !msg.getId().equals(PersonalInformation.id))
                            list.add(dataSnapshot.getValue(Message.class));
                        count++;
                    }
                }

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
                list.add(new Message("", editText.getText().toString(), Calendar.getInstance().getTimeInMillis(), PersonalInformation.id));
                childReference.setValue(list);    // REAL-TIME DATABASE CODE
                receiverReference.setValue(list);
                editText.setText("");
            }
        });
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