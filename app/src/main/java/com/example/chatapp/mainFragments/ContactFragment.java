package com.example.chatapp.mainFragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.example.chatapp.R;
import com.example.chatapp.Service;
import com.example.chatapp.activity.ChatPageActivity;

public class ContactFragment extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.activity_main, container, true);
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
                Service service = new Service();
                service.hideKeyboard(getActivity());
            }
        });
        return rootView;
    }
}
