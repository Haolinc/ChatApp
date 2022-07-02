package com.example.chatapp.mainFragments.contact;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.R;
import com.example.chatapp.Service;
import com.example.chatapp.data.FireStoreDataReference;
import com.example.chatapp.data.FriendData;
import com.example.chatapp.data.PersonalInformation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.LinkedList;
import java.util.List;

public class ContactFragment extends Fragment {
    private RecyclerView friendListRecyclerView;
    private TextView text;
    private ContactFragmentAdapter contactFragmentAdapter;
    private List<FriendData> friendList = new LinkedList<>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_contact, container, true);
        friendListRecyclerView = rootView.findViewById(R.id.contact_fragment_recyclerview);
        text = rootView.findViewById(R.id.contact_fragment_textview);

        //find friend process for find button
        rootView.findViewById(R.id.contact_fragment_add_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Service.setUpLoading(getContext())) {
                    String targetID = ((EditText) rootView.findViewById(R.id.contact_fragment_edittext)).getText().toString();
                    if (targetID.equals("")) {
                        Toast.makeText(getContext(), "Field cannot be empty!", Toast.LENGTH_SHORT).show();
                    } else if (targetID.equals(PersonalInformation.id))
                        Toast.makeText(getContext(), "Cannot add yourself to contact!", Toast.LENGTH_SHORT).show();
                    else {
                        findFriend(targetID);
                    }
                    Service.stopLoading(getContext());
                }
                else
                    Service.setUpNetworkIssueToast(getContext());
            }
        });

        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Service.hideKeyboard(getActivity());
            }
        });



        return rootView;
    }

    @Override
    public void onResume(){
        super.onResume();
        getFriendListFromFireStore();
    }

    private void findFriend(String targetID){
        FireStoreDataReference.getUsersReference().whereEqualTo("id", targetID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (!task.getResult().isEmpty()) {
                            Intent i = new Intent(getContext(), ContactProfileActivity.class);
                            List<DocumentSnapshot> documentList = task.getResult().getDocuments();
                            //send intent extras
                            i.putExtra("id", targetID);
                            i.putExtra("name", (String) documentList.get(0).get("name"));
                            i.putExtra("userDocument", documentList.get(0).getId());
                            Log.d("userdoc", documentList.get(0).getId());
                            //find if target is friend already
                            FireStoreDataReference.getFriendListReference()
                                    .whereEqualTo("id", targetID)
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (!task.getResult().isEmpty()) {
                                                i.putExtra("isFriend", true);
                                            }
                                            startActivity(i);

                                        }
                                    });
                        } else {
                            Toast.makeText(getContext(), "User not found", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void updateView(){
        contactFragmentAdapter = new ContactFragmentAdapter(getContext(), friendList);
        friendListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        friendListRecyclerView.setAdapter(contactFragmentAdapter);
    }


    private void getFriendListFromFireStore(){
        //Reference to find users by id

        //Document names;
        FireStoreDataReference.getFriendListReference().get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (!task.getResult().isEmpty()){
                    friendList.clear();
                    //get friend ids
                    List<String> idList = new LinkedList<>();
                    for(QueryDocumentSnapshot querySnapshot : task.getResult()){
                        idList.add((String)querySnapshot.getData().get("id"));
                    }

                    //base in ids put them into friendList
                    FireStoreDataReference.getUsersReference()
                            .whereIn("id", idList)
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
                                friendList.add(new FriendData(documentSnapshot.getString("id"), documentSnapshot.getString("name"), documentSnapshot.getId()));
                            }
                            updateView();
                        }
                    });

                }
            }
        });
    }
}
