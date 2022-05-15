package com.example.chatapp.mainFragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.R;
import com.example.chatapp.Service;
import com.example.chatapp.activity.ChatPageActivity;
import com.example.chatapp.activity.ContactProfileActivity;
import com.example.chatapp.adapter.ContactFragmentAdapter;
import com.example.chatapp.data.FriendData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ContactFragment extends Fragment {
    private RecyclerView friendListRecyclerView;
    private TextView text;
    private ContactFragmentAdapter contactFragmentAdapter;
    private List<FriendData> friendList = new LinkedList<>();
    private CollectionReference fireStoreFriendList;
    private String personalID;
    private CollectionReference users = FirebaseFirestore.getInstance().collection("users");

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_contact, container, true);
        friendListRecyclerView = rootView.findViewById(R.id.contact_fragment_recyclerview);
        personalID =getActivity().getIntent().getStringExtra("id");
        text = rootView.findViewById(R.id.contact_fragment_textview);
        fireStoreFriendList = FirebaseFirestore.getInstance().collection("users").document(personalID).collection("friends");

        //find friend process
        rootView.findViewById(R.id.contact_fragment_add_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String targetID = ((EditText)rootView.findViewById(R.id.contact_fragment_edittext)).getText().toString();
                Log.d("targetID", targetID);
                if (targetID.equals("")){
                    changeText("Field cannot be empty!");
                }
                else if (targetID.equals(personalID))
                    changeText("Cannot add yourself to contact!");
                else{
                    users.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            boolean foundAccount = false;
                            for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                String id = queryDocumentSnapshot.getId();
                                if (id.equals(targetID)) {
                                    Map<String, Object> userInfo = queryDocumentSnapshot.getData();
                                    Intent i = new Intent(getContext(), ContactProfileActivity.class);

                                    //send intent extras
                                    i.putExtra("id", targetID);
                                    i.putExtra("name", (String) userInfo.get("name"));

                                    //check if target already friend
                                    users.document(personalID)
                                            .collection("friends")
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    for(DocumentSnapshot documentSnapshot : task.getResult()){
                                                        Map<String, Object> friendListMap = documentSnapshot.getData();
                                                        if (targetID.equals(friendListMap.get("id"))){
                                                            i.putExtra("isFriend", true);
                                                            returnToNormalText();
                                                        }
                                                    }
                                                    startActivity(i);
                                                }
                                            });
                                    foundAccount = true;
                                    break;
                                }
                            }
                            if (!foundAccount)
                                changeText("User not found");
                        }
                    });
                }
            }
        });

        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Service().hideKeyboard(getActivity());
            }
        });

        getFriendListFromFireStore();


        return rootView;
    }

    @Override
    public void onResume(){
        super.onResume();
        returnToNormalText();
        getFriendListFromFireStore();
    }

    private void changeText(String changedText){
        text.setText(changedText);
        text.setTextColor(Color.RED);
    }

    private void returnToNormalText(){
        text.setText("Talk Directly To User: ");
        text.setTextColor(Color.GRAY);
    }

    private void updateView(){
        contactFragmentAdapter = new ContactFragmentAdapter(getContext(), friendList);
        friendListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        friendListRecyclerView.setAdapter(contactFragmentAdapter);
    }

    private void getFriendListFromFireStore(){
        fireStoreFriendList.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.getResult().isEmpty())
                    Log.d("testTask1", "yes");
                else{
                    List<FriendData> newFriendList = new LinkedList<>();
                    for(QueryDocumentSnapshot querySnapshot : task.getResult()){
                        Map<String, Object> map = querySnapshot.getData();
                        newFriendList.add(new FriendData((String)map.get("id"), (String)map.get("name")));
                    }
                    friendList = newFriendList;
                    updateView();
                }
            }
        });
    }
}
