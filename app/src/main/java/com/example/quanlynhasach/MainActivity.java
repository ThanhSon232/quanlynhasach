package com.example.quanlynhasach;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    Fragment activeFragment;
    FragmentManager fragmentManager = getSupportFragmentManager();
    FirebaseAuth mAuth;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        bottomNavigationView = findViewById(R.id.navigation);
        checkUser();
    }

//    void setUpNavigation() {
//        activeFragment = homeF;
//        bottomNavigationView = findViewById(R.id.navigation);
//        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavegationItemSelectedListener);
//        fragmentManager.beginTransaction().add(R.id.fragment_layout, _accountFragment, "accountFragment").hide(_accountFragment).commit();
//        fragmentManager.beginTransaction().add(R.id.fragment_layout, homeFragment, homeFragment.toString()).commit();
//        fragmentManager.beginTransaction().add(R.id.fragment_layout, _searchFragment, _searchFragment.getTag()).hide(_searchFragment).commit();
//        fragmentManager.beginTransaction().add(R.id.fragment_layout, activityFragment, activeFragment.toString()).hide(activityFragment).commit();
//    }

    void checkUser(){
        if(mAuth.getCurrentUser() == null){
            loginFragment loginFragment = new loginFragment();
            fragmentManager.beginTransaction().replace(R.id.fragment_layout,loginFragment).commit();
            bottomNavigationView.setVisibility(View.GONE);
        }
    }
}