package com.example.quanlynhasach;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quanlynhasach.fragment.*;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    public static final int PICK_IMAGE = 1;
    Fragment activeFragment;
    FragmentManager fragmentManager = getSupportFragmentManager();
    FirebaseAuth mAuth;
    Toolbar appbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    com.example.quanlynhasach.fragment.staffFragment staffFragment = new staffFragment();
    com.example.quanlynhasach.fragment.receiptFragment receiptFragment = new receiptFragment();
    bookFragment bookFragment = new bookFragment();
    ImageButton option;

    TextView email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser()==null){
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
        View headerView = navigationView.getHeaderView(0);
        email = headerView.findViewById(R.id.email);
        email.setText(mAuth.getCurrentUser().getEmail());
        option = findViewById(R.id.add);
        option.setOnClickListener(this);
        setUpNavigation();
    }

    void setUpNavigation() {
        fragmentManager.beginTransaction().add(R.id.fragment_layout, bookFragment, "bookFragment").commit();
        fragmentManager.beginTransaction().add(R.id.fragment_layout, staffFragment, "staffFragment").hide(staffFragment).commit();
        fragmentManager.beginTransaction().add(R.id.fragment_layout, receiptFragment, "receiptFragment").hide(receiptFragment).commit();
        activeFragment = bookFragment;
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
            case R.id.book:
                fragmentManager.beginTransaction().hide(activeFragment).show(bookFragment).commit();
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

        if(fragmentManager.findFragmentByTag("addNewItemFragment") == null){
            appbar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.add:
                openDialog();
                break;
        }
    }

    void setPickImage(){
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

        startActivityForResult(chooserIntent, PICK_IMAGE);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && data!=null) {
            final Bundle extras = new Bundle();
            extras.putParcelable("data",data.getData());
            addNewItemFragment item = new addNewItemFragment();
            item.setArguments(extras);
            fragmentManager.beginTransaction().replace(R.id.fragment_layout,item).addToBackStack("addNewItemFragment").commit();
        }
        else{
            Toast.makeText(this,"Success",Toast.LENGTH_LONG).show();
        }
    }

    private void openDialog() {
        final Dialog dialog = new Dialog(this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.up_item_fragment);

        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        option.getLocationOnScreen(new int[2]);
        wlp.gravity = Gravity.TOP | Gravity.LEFT;
        wlp.x = option.getLeft() - option.getWidth() / 2;
        wlp.y = option.getTop() + option.getHeight() / 2;
        window.setAttributes(wlp);
        dialog.show();

        LinearLayout add_book = dialog.findViewById(R.id.book_layout);
        LinearLayout reiceved_note = dialog.findViewById(R.id.received_note);
        LinearLayout bill = dialog.findViewById(R.id.bill);
        LinearLayout receipt = dialog.findViewById(R.id.receipt);
        LinearLayout rule = dialog.findViewById(R.id.rule);


        add_book.setOnClickListener(v -> {
            verifyStoragePermissions(this);
            dialog.dismiss();
            setPickImage();
        } );

        reiceved_note.setOnClickListener(v -> {
            dialog.dismiss();
            setPickImage();
        } );

        bill.setOnClickListener(v -> {
            dialog.dismiss();
            setPickImage();
        } );

        receipt.setOnClickListener(v -> {
            dialog.dismiss();
            setPickImage();
        } );

        rule.setOnClickListener(v -> {
            dialog.dismiss();
            setPickImage();
        } );
    }


    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }

    }
}