package com.example.chatapp.mainFragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.R;
import com.example.chatapp.Service;
import com.example.chatapp.activity.ChatPageActivity;
import com.example.chatapp.adapter.ChatFragmentAdapter;
import com.example.chatapp.adapter.ContactFragmentAdapter;
import com.example.chatapp.data.ChatFragmentData;
import com.example.chatapp.data.FriendData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ContactFragment extends Fragment {
    private RecyclerView friendListRecyclerView;
    private ContactFragmentAdapter contactFragmentAdapter;
    private List<FriendData> friendList = new LinkedList<>();
    private CollectionReference fireStoreFriendList;
    private String personalID;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_contact, container, true);
        friendListRecyclerView = rootView.findViewById(R.id.contact_fragment_recyclerview);
        personalID =getActivity().getIntent().getStringExtra("id");

        fireStoreFriendList = FirebaseFirestore.getInstance().collection("users").document(personalID).collection("friends");
        rootView.findViewById(R.id.maint_talk_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editText = rootView.findViewById(R.id.main_talkto_edittext);
                Intent i = new Intent(getContext(), ChatPageActivity.class);
                i.putExtra("id", editText.getText().toString());
                startActivity(i);
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
                    for(QueryDocumentSnapshot querySnapshot : task.getResult()){
                        Map<String, Object> map = querySnapshot.getData();
                        friendList.add(new FriendData((String)map.get("id"), (String)map.get("name")));
                    }
                    updateView();
                }
            }
        });
    }
}
