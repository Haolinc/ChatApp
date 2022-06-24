package com.example.chatapp.mainFragments;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.chatapp.mainFragments.recentChat.ChatFragment;
import com.example.chatapp.mainFragments.contact.ContactFragment;
import com.example.chatapp.mainFragments.profile.ProfileFragment;

public class MainFragmentAdapter extends FragmentStateAdapter {

    //IMPORTANT---------------------------------------------------------------
    //TABS: CHAT, CONTACT, PROFILE

    public MainFragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch(position){
            case 0 : return new ChatFragment();
            case 1 : return new ContactFragment();
            case 2 : return new ProfileFragment();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
