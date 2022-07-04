package com.example.chatapp.mainFragments.recentChat;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.R;
import com.example.chatapp.data.RecentChatFragmentData;
import com.example.chatapp.data.DateDisplay;
import com.example.chatapp.data.FireStoreDataReference;
import com.example.chatapp.data.UserData;
import com.example.chatapp.data.UserInfo;
import com.example.chatapp.mainFragments.chat.ChatPageActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class RecentChatFragment extends Fragment {
    private RecyclerView messageRecycler;
    private RecentChatFragmentAdapter messageAdapter;
    private List<RecentChatFragmentData> messageList = new LinkedList<>();
    // id, <arraylistPos, totalUnread>
    private Map<String, Pair<Integer, Integer>> messageMap = new HashMap<>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chat, container, true);
        messageRecycler = rootView.findViewById(R.id.chat_fragment_recycler_view);
        getAllRecentFromRealtimeDatabase();


        return rootView;
    }

    private void updateView(){
        messageAdapter = new RecentChatFragmentAdapter(getContext(), messageList);
        messageRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        messageRecycler.setAdapter(messageAdapter);
    }

    private void getAllRecentFromRealtimeDatabase(){
        DatabaseReference userInfo = FirebaseDatabase.getInstance().getReference().child(new UserInfo(getContext()).getID());
        //going through firebase realtime
        //to find who have chatted user
        userInfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //when just start
                if (messageList.size() == 0){
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        HashMap<String, Object> dataSnapshotValue = (HashMap<String, Object>) dataSnapshot.getValue();
                        HashMap<String, Object> newMap = (HashMap<String, Object>) dataSnapshotValue.get("setUp");
                        if (newMap != null) {
                            RecentChatFragmentData chatFragment = new RecentChatFragmentData((int)((long)newMap.get("totalUnread")), dataSnapshot.getKey(), "", (String)newMap.get("latestText"), "");

                            messageList.add(chatFragment);
                            messageMap.put(chatFragment.getId(), new Pair<>(messageList.size()-1, chatFragment.getTotalUnread()));
                        }
                    }
                    //time to fill names and document ids
                    FireStoreDataReference.getUsersReference()
                            .whereIn("id", Arrays.asList(messageMap.keySet().toArray()))
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    int count = 0;
                                    for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
                                        messageList.get(count).setTargetName(documentSnapshot.getString("name"));
                                        messageList.get(count).setTargetDocumentID(documentSnapshot.getId());
                                        count++;
                                    }
                                    updateView();
                                }
                            });
                }
                //when something has changed
                else {
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        HashMap<String, Object> dataSnapshotValue = (HashMap<String, Object>) dataSnapshot.getValue();
                        HashMap<String, Object> newMap = (HashMap<String, Object>) dataSnapshotValue.get("setUp");

                        int totalUnreads = (int)((long)newMap.get("totalUnread"));
                        //when no id found in list
                        if (!messageMap.containsKey(dataSnapshot.getKey())){
                            RecentChatFragmentData chatFragment = new RecentChatFragmentData(totalUnreads, dataSnapshot.getKey(), "", (String)newMap.get("latestText"), "");
                            messageList.add(chatFragment);
                            messageMap.put(chatFragment.getId(), new Pair<>(messageList.size()-1, chatFragment.getTotalUnread()));
                            FireStoreDataReference.getUsersReference()
                                    .whereEqualTo("id", dataSnapshot.getKey())
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                                            messageList.get(messageList.size()-1).setTargetDocumentID(documentSnapshot.getId());
                                            messageList.get(messageList.size()-1).setTargetDocumentID(documentSnapshot.getString("name"));
                                            createNotification(messageList.get(messageList.size()-1));
                                            updateView();
                                        }
                                    });

                            break;
                        }
                        //when id is same
                        else{
                            //but total unread is different, which means new message

                            if (messageMap.get(dataSnapshot.getKey()).second != totalUnreads){
                                messageList.get(messageMap.get(dataSnapshot.getKey()).first).setTotalUnread(totalUnreads);
                                createNotification(messageList.get(messageMap.get(dataSnapshot.getKey()).first));
                                updateView();
                                break;
                            }
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //OLD CODE --------------------------------------------------------------------
//        userInfo.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                List<RecentChatFragmentData> newList = new LinkedList<>();
//                List<String> idList = new LinkedList<>();
//                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
//                    HashMap<String, Object> msgList = (HashMap<String, Object>) dataSnapshot.getValue();
//                    if (msgList.size() == 1)
//                        continue;
//                    HashMap<String, Object> newMap = (HashMap<String, Object>)  msgList.get("setUp");
//                    idList.add((String)newMap.get("id"));
//
//                    //empty out targetName and documentID first, will find it later
//                    newList.add(new RecentChatFragmentData((int)((long)newMap.get("totalUnread")), dataSnapshot.getKey(), "", (String)newMap.get("latestText"), ""));
//                }
//
//                //get name from firestore to update names, and also documentID
//                if (!idList.isEmpty()) {
//                    FireStoreDataReference.getUsersReference()
//                            .whereIn("id", idList)
//                            .get()
//                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                                @Override
//                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                                    int count = 0;
//                                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
//                                        newList.get(count).setTargetName(documentSnapshot.getString("name"));
//                                        newList.get(count).setTargetDocumentID(documentSnapshot.getId());
//                                        count++;
//                                    }
//                                    messageList = newList;
//                                    updateView();
//                                }
//                            });
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

    }

    private void createNotification(RecentChatFragmentData data){
        //only create when chatActivity is not visible, and the chatActivity is not with same id as this
        if (!ChatPageActivity.active && ChatPageActivity.id != null && !ChatPageActivity.id.equals(data.getId())) {
            Intent intent = new Intent(getContext(), ChatPageActivity.class);
            intent.putExtra("userData", new UserData(data.getId(), data.getTargetName(), data.getTargetDocumentID()));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, intent, 0);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), data.getTargetDocumentID())
                    .setSmallIcon(R.drawable.loading)
                    .setContentTitle(data.getTargetName() + " " + new DateDisplay(Calendar.getInstance().getTimeInMillis()).returnCurrentTime())
                    .setContentText(data.getLatestText())
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);

            NotificationManagerCompat.from(getContext()).notify(0, builder.build());
        }
    }

}
