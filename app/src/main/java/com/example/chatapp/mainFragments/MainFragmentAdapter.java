package com.example.chatapp.mainFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.chatapp.R;
import com.example.chatapp.data.UserData;
import com.example.chatapp.mainFragments.recentChat.RecentChatFragment;
import com.example.chatapp.mainFragments.contact.ContactFragment;
import com.example.chatapp.mainFragments.profile.ProfileFragment;
import com.google.firebase.firestore.auth.User;

public class MainFragmentAdapter extends FragmentStateAdapter {

    //IMPORTANT---------------------------------------------------------------
    //TABS: CHAT, CONTACT, PROFILE

    UserData userData;

    public MainFragmentAdapter(@NonNull FragmentActivity fragmentActivity, UserData userData) {
        super(fragmentActivity);
        this.userData = userData;
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch(position){
            case 0 :
                return goToFragment();
            case 1 : return new ContactFragment();
            case 2 : return new ProfileFragment();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    private RecentChatFragment goToFragment(){
        RecentChatFragment recentChatFragment = new RecentChatFragment();
        if (userData != null) {
            Bundle bundle = new Bundle();
            bundle.putParcelable("userData", userData);
            recentChatFragment.setArguments(bundle);
        }
        return recentChatFragment;
    }
}
