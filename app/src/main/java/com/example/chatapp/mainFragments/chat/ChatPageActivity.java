package com.example.chatapp.mainFragments.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chatapp.data.RecentChatFragmentData;
import com.example.chatapp.data.FireStoreDataReference;
import com.example.chatapp.data.Message;
import com.example.chatapp.R;
import com.example.chatapp.Service;
import com.example.chatapp.data.UserData;
import com.example.chatapp.data.UserInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class ChatPageActivity extends AppCompatActivity {
    private RecyclerView messageRecycler;
    private List <Message> list = new LinkedList<>();
    private RecentChatFragmentData setupData;
    private EditText editText;
    private UserData targetData;
    private ValueEventListener setZeroListener;
    public static boolean active = false;
    public static String id = null;
    private UserInfo userInfo;


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
        userInfo = new UserInfo(this);
        userReference = parentReference.child(userInfo.getID()).child(targetData.getId());
        receiverReference = parentReference.child(targetData.getId()).child(userInfo.getID());
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

    //when the current activity is active
    @Override
    protected void onResume(){
        super.onResume();
        active = true;
        id = targetData.getId();
    }

    //when this activity is not visible
    protected void onPause(){
        super.onPause();
        active = false;
        id = null;
    }

    private void sendToTarget(){
        //can only send when user have network connection
        if (Service.setUpLoading(this)) {
            receiverReference.child("setUp")
                    .get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                @Override
                public void onSuccess(DataSnapshot dataSnapshot) {
                    //add setup if null, otherwise update setup
                    if (dataSnapshot.getValue(RecentChatFragmentData.class) != null)
                        updateMessageDatabase(dataSnapshot.getValue(RecentChatFragmentData.class).getTotalUnread());
                    else
                        updateMessageDatabase(0);
                    Service.stopLoading(ChatPageActivity.this);
                }
            });
        }
        else
            Toast.makeText(this, "Fail to send due to network issue", Toast.LENGTH_SHORT).show();
    }

    private void updateMessageDatabase(int totalUnread){
        long currentTime = Calendar.getInstance().getTimeInMillis();

        Message msg = new Message(userInfo.getID(), editText.getText().toString(),currentTime);
        setupData = new RecentChatFragmentData(totalUnread+1, targetData.getId(), targetData.getName(), msg.getText(), targetData.getDocumentID());
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
        RecentChatFragmentData setupData2 = new RecentChatFragmentData(totalUnread+1, userInfo.getID(), userInfo.getName(), msg.getText(), userInfo.getDocumentID());
        receiverReference.push().setValue(msg);
        receiverReference.child("setUp").setValue(setupData2);
        editText.setText("");
    }


    private void adapterSetting(){
        ChatPageListAdapter messageAdapter = new ChatPageListAdapter(this, list);
        messageRecycler.setLayoutManager(new LinearLayoutManager(this));
        messageRecycler.setAdapter(messageAdapter);
        ((LinearLayoutManager)messageRecycler.getLayoutManager()).setStackFromEnd(true);
    }

}