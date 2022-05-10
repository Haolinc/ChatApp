package com.example.chatapp.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.chatapp.mainFragments.ChatFragment;
import com.example.chatapp.mainFragments.ContactFragment;
import com.example.chatapp.mainFragments.ProfileFragment;

import java.util.ArrayList;
import java.util.List;

public class MainFragmentAdapter extends FragmentStateAdapter {

    //IMPORTANT---------------------------------------------------------------
    //TABS: CHAT, CONTACT, PROFILE

    public MainFragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public MainFragmentAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    public MainFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
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
