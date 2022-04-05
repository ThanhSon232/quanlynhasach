package com.example.quanlynhasach;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Fragment activeFragment;
    FragmentManager fragmentManager = getSupportFragmentManager();
    FirebaseAuth mAuth;
    Toolbar appbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    staffFragment staffFragment = new staffFragment();
    receiptFragment receiptFragment = new receiptFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser()!=null){
            startActivity(new Intent(this,login_register_fragment.class));
        }
        appbar = findViewById(R.id.appbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        setSupportActionBar(appbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, appbar, R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        setUpNavigation();
    }

    void setUpNavigation() {
        fragmentManager.beginTransaction().add(R.id.fragment_layout, staffFragment, "staffFragment").commit();
        fragmentManager.beginTransaction().add(R.id.fragment_layout, receiptFragment, "receiptFragment").hide(receiptFragment).commit();
        activeFragment = staffFragment;
    }




    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.staff:
                fragmentManager.beginTransaction().hide(activeFragment).show(staffFragment).commit();
                activeFragment = staffFragment;
                break;
            case R.id.receipt:
                fragmentManager.beginTransaction().hide(activeFragment).show(receiptFragment).commit();
                activeFragment = receiptFragment;
                break;
            case R.id.log_out:
                mAuth.signOut();
                startActivity(new Intent(this,login_register_fragment.class));
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }
}