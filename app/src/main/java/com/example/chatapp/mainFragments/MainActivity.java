package com.example.chatapp.mainFragments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.example.chatapp.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_fragment_tab);

        ViewPager2 viewPager2 = findViewById(R.id.main_fragment_viewpager2);
        MainFragmentAdapter mainFragmentAdapter = new MainFragmentAdapter(this);
        viewPager2.setAdapter(mainFragmentAdapter);
        TabLayout tabLayout = findViewById(R.id.main_fragment_tablayout);
        String[] tabText = {"CHATS", "CONTACTS", "PROFILE"};
        new TabLayoutMediator(tabLayout, viewPager2,
                (tab, position) -> tab.setText(tabText[position])
        ).attach();



    }
}