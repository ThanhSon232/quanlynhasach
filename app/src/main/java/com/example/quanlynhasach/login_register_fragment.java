package com.example.quanlynhasach;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.example.quanlynhasach.fragment.loginFragment;

public class login_register_fragment extends AppCompatActivity {
    FragmentManager fragmentManager = getSupportFragmentManager();
    com.example.quanlynhasach.fragment.loginFragment loginFragment = new loginFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register_fragment);
        fragmentManager.beginTransaction().replace(R.id.fragment_layout,loginFragment,"loginFragment").commit();
    }
}