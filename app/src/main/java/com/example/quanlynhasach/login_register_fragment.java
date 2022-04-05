package com.example.quanlynhasach;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

public class login_register_fragment extends AppCompatActivity {
    FragmentManager fragmentManager = getSupportFragmentManager();
    loginFragment loginFragment = new loginFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register_fragment);
        fragmentManager.beginTransaction().replace(R.id.fragment_layout,loginFragment,"loginFragment").commit();
    }
}