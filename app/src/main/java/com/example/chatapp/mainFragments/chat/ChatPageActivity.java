package com.example.chatapp.mainFragments.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.example.chatapp.data.ChatFragmentData;
import com.example.chatapp.data.FireStoreDataReference;
import com.example.chatapp.data.Message;
import com.example.chatapp.data.PersonalInformation;
import com.example.chatapp.R;
import com.example.chatapp.Service;
import com.example.chatapp.data.UserData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class ChatPageActivity extends AppCompatActivity {
    private RecyclerView messageRecycler;
    private ChatPageListAdapter messageAdapter;
    private List <Message> list = new LinkedList<>();
    private ChatFragmentData setupData;
    private EditText editText;
    private UserData targetData;
    private ValueEventListener setZeroListener;


    // realtime firebase example code:
    DatabaseReference parentReference = FirebaseDatabase.getInstance().getReference();
    DatabaseReference userReference;
    DatabaseReference receiverReference;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_page);
        targetData = getIntent().getParcelableExtra("userData");
        userReference = parentReference.child(PersonalInformation.id).child(targetData.getId());
        receiverReference = parentReference.child(targetData.getId()).child(PersonalInformation.id);
        getSupportActionBar().setTitle(targetData.getName());
        messageRecycler = findViewById(R.id.chat_page_recycle_view);

        //make onclick event in screen to hide keyboard
        messageRecycler.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Service.hideKeyboard(ChatPageActivity.this);
                //need to return false in order to make it scrolling
                return false;
            }
        });



        //retrieve data from database
        setZeroListener = userReference.addValueEventListener(new ValueEventListener() {
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
                Log.d("chatPage", PersonalInformation.id + ": called");
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
                sendToTarget();
            }
        });
    }

    //must remove the eventListener, otherwise will always set unread to 0 when login to other account
    //after exiting the app
    @Override
    protected void onStop() {
        super.onStop();
        userReference.removeEventListener(setZeroListener);
    }

    private void sendToTarget(){
        receiverReference.child("setUp")
                .get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                //add setup if null, otherwise update setup
                if (dataSnapshot.getValue(ChatFragmentData.class)!= null)
                    updateMessageDatabase(dataSnapshot.getValue(ChatFragmentData.class).getTotalUnread());
                else
                    updateMessageDatabase(0);
            }
        });
    }

    private void updateMessageDatabase(int totalUnread){
        long currentTime = Calendar.getInstance().getTimeInMillis();

        Message msg = new Message(PersonalInformation.id, editText.getText().toString(),currentTime);
        setupData = new ChatFragmentData(totalUnread+1, targetData.getId(), targetData.getName(), msg.getText(), targetData.getDocumentID());
        userReference.child("setUp").setValue(setupData);

        //update message for current user
        userReference.push().setValue(msg);    // REAL-TIME DATABASE CODE

        //targetName needs to get from firestore
        FireStoreDataReference.getUsersReference()
                .document(targetData.getDocumentID())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        String nameInDatabase = (String)task.getResult().get("name");
                        //only change things when name is different
                        if (nameInDatabase != null && !nameInDatabase.equals(targetData.getName())){
                            setupData.setTargetName(nameInDatabase);
                            targetData.setName(nameInDatabase);
                            userReference.child("setUp").setValue(setupData);

                        }

                    }
                });


        //update setup for receive user
        ChatFragmentData setupData2 = new ChatFragmentData(totalUnread+1, PersonalInformation.id, PersonalInformation.name, msg.getText(), PersonalInformation.userDocument);
        receiverReference.push().setValue(msg);
        receiverReference.child("setUp").setValue(setupData2);
        editText.setText("");
    }


    private void adapterSetting(){
        messageAdapter = new ChatPageListAdapter(this, list);
        messageRecycler.setLayoutManager(new LinearLayoutManager(this));
        messageRecycler.setAdapter(messageAdapter);
        ((LinearLayoutManager)messageRecycler.getLayoutManager()).setStackFromEnd(true);
    }

}