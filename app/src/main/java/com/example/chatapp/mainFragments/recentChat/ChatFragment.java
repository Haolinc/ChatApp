package com.example.chatapp.mainFragments.recentChat;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.R;
import com.example.chatapp.data.ChatFragmentData;
import com.example.chatapp.data.FireStoreDataReference;
import com.example.chatapp.data.FriendData;
import com.example.chatapp.data.PersonalInformation;
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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class ChatFragment extends Fragment {
    private RecyclerView messageRecycler;
    private ChatFragmentAdapter messageAdapter;
    private List<ChatFragmentData> messageList = new LinkedList<>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chat, container, true);
        messageRecycler = rootView.findViewById(R.id.chat_fragment_recycler_view);
        getAllRecentFromRealtimeDatabase();


        return rootView;
    }

    private void updateView(){
        messageAdapter = new ChatFragmentAdapter(getContext(), messageList);
        messageRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        messageRecycler.setAdapter(messageAdapter);
    }

    private void getAllRecentFromRealtimeDatabase(){
        DatabaseReference parent = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userInfo = parent.child(PersonalInformation.id);
        userInfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<ChatFragmentData> newList = new LinkedList<>();
                List<String> idList = new LinkedList<>();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    HashMap<String, Object> msgList = (HashMap<String, Object>) dataSnapshot.getValue();
                    if (msgList.size() == 1)
                        continue;
                    HashMap<String, Object> newMap = (HashMap<String, Object>)  msgList.get("setUp");
                    idList.add((String)newMap.get("id"));
                    newList.add(new ChatFragmentData((int)((long)newMap.get("totalUnread")), dataSnapshot.getKey(), "", (String)newMap.get("latestText")));
                }

                //get name from firestore to update names
                if (!idList.isEmpty()) {
                    FireStoreDataReference.getUsersReference()
                            .whereIn("id", idList)
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    int count = 0;
                                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                                        newList.get(count).setTargetName(documentSnapshot.getString("name"));
                                        count++;
                                    }
                                    messageList = newList;
                                    updateView();
                                }
                            });
                }



            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
