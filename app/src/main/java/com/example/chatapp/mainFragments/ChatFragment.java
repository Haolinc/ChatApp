package com.example.chatapp.mainFragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.example.chatapp.R;
import com.example.chatapp.activity.ChatPageActivity;

public class ChatFragment extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_main, container, true);
        rootView.findViewById(R.id.maint_talk_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(rootView.getContext(), ChatPageActivity.class);
                EditText editText = rootView.findViewById(R.id.main_talkto_edittext);
                i.putExtra("id", editText.getText().toString());
                startActivity(i);
            }
        });
        return rootView;
    }
}
