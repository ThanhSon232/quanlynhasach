package com.example.quanlynhasach;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
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
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quanlynhasach.fragment.*;
import com.example.quanlynhasach.model.customerModel;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    public static final int PICK_IMAGE = 1;
    Fragment activeFragment;
    FragmentManager fragmentManager = getSupportFragmentManager();
    FirebaseAuth mAuth;
    Toolbar appbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    com.example.quanlynhasach.fragment.staffFragment staffFragment = new staffFragment();
    billFragment billFragment = new billFragment();
    bookFragment bookFragment = new bookFragment();
    customerFragment customerFragment = new customerFragment();
    goodReceivedNoteFragment goodReceivedNoteFragment = new goodReceivedNoteFragment();
    ImageButton option;
    FirebaseDatabase database;
    ruleFragment ruleFragment = new ruleFragment();

    TextView email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser()==null){
            startActivity(new Intent(this,login_register_fragment.class));
        }
        database = FirebaseDatabase.getInstance("https://quanlynhasach-c1a4c-default-rtdb.asia-southeast1.firebasedatabase.app/");
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
        fragmentManager.beginTransaction().add(R.id.fragment_layout, billFragment, "billFragment").hide(billFragment).commit();
        fragmentManager.beginTransaction().add(R.id.fragment_layout, customerFragment, "customerFragment").hide(customerFragment).commit();
        fragmentManager.beginTransaction().add(R.id.fragment_layout, goodReceivedNoteFragment, "goodReceivedNoteFragment").hide(goodReceivedNoteFragment).commit();
        fragmentManager.beginTransaction().add(R.id.fragment_layout, ruleFragment, "ruleFragment").hide(ruleFragment).commit();
        activeFragment = bookFragment;
    }




    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.staff:
                fragmentManager.beginTransaction().hide(activeFragment).show(staffFragment).commit();
                activeFragment = staffFragment;
                break;
            case R.id.bill:
                fragmentManager.beginTransaction().hide(activeFragment).show(billFragment).commit();
                activeFragment = billFragment;
                break;
            case R.id.book:
                fragmentManager.beginTransaction().hide(activeFragment).show(bookFragment).commit();
                activeFragment = bookFragment;
                break;
            case R.id.note:
                fragmentManager.beginTransaction().hide(activeFragment).show(goodReceivedNoteFragment).commit();
                activeFragment = goodReceivedNoteFragment;
                break;

            case R.id.customer:
                fragmentManager.beginTransaction().hide(activeFragment).show(customerFragment).commit();
                activeFragment = customerFragment;
                break;

            case R.id.log_out:
                mAuth.signOut();
                startActivity(new Intent(this,login_register_fragment.class));
                break;
            case R.id.rule:
                fragmentManager.beginTransaction().hide(activeFragment).show(ruleFragment).commit();
                activeFragment = ruleFragment;
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

        if(fragmentManager.findFragmentByTag("addNewBook") == null){
            appbar.setVisibility(View.VISIBLE);
        }

        if(fragmentManager.findFragmentByTag("addNewNote") == null){
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
            addNewBook item = new addNewBook();
            item.setArguments(extras);
            fragmentManager.beginTransaction().replace(R.id.fragment_layout,item).addToBackStack("addNewBook").commit();
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
        LinearLayout customer = dialog.findViewById(R.id.customer);
        LinearLayout reiceved_note = dialog.findViewById(R.id.received_note);
        LinearLayout bill = dialog.findViewById(R.id.bill);
        LinearLayout receipt = dialog.findViewById(R.id.receipt);
        LinearLayout rule = dialog.findViewById(R.id.rule);


        add_book.setOnClickListener(v -> {
            verifyStoragePermissions(this);
            dialog.dismiss();
            setPickImage();
        } );


        customer.setOnClickListener(v -> {
            addNewCustomer();
            dialog.dismiss();
        } );


        reiceved_note.setOnClickListener(v -> {
            dialog.dismiss();
            fragmentManager.beginTransaction().replace(R.id.fragment_layout,new addNewNote()).addToBackStack("addNewNote").commit();
            appbar.setVisibility(View.GONE);
        } );

        bill.setOnClickListener(v -> {
            dialog.dismiss();
            fragmentManager.beginTransaction().replace(R.id.fragment_layout,new addNewBill()).addToBackStack("addNewBill").commit();
            appbar.setVisibility(View.GONE);
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

    void addNewCustomer(){
        LayoutInflater factory = LayoutInflater.from(this);
        View deleteDialogView = factory.inflate(R.layout.customer_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(deleteDialogView);
        String[] m = UUID.randomUUID().toString().split("-");
        String id_1 = m[0];
        TextView id = deleteDialogView.findViewById(R.id.customerID);
        id.setText(id_1);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                        EditText name = deleteDialogView.findViewById(R.id.name);
                        EditText address = deleteDialogView.findViewById(R.id.address);
                        EditText phoneNumber = deleteDialogView.findViewById(R.id.phoneNumber);
                        EditText email = deleteDialogView.findViewById(R.id.email);

                        String nameC = name.getText().toString();
                        String addressC = address.getText().toString();
                        String phoneNumberC = phoneNumber.getText().toString();
                        String emailC = email.getText().toString();
                        if (TextUtils.isEmpty(nameC)) {
                            Toast.makeText(getApplicationContext(),"Tên không được để trống",Toast.LENGTH_LONG).show();
                        } else if (TextUtils.isEmpty(addressC)) {
                            Toast.makeText(getApplicationContext(),"Địa chỉ không được để trống",Toast.LENGTH_LONG).show();
                            address.requestFocus();
                        } else if (TextUtils.isEmpty(phoneNumberC)) {
                            Toast.makeText(getApplicationContext(),"Số điện thoại không được để trống",Toast.LENGTH_LONG).show();
                        } else if (TextUtils.isEmpty(emailC)) {
                            Toast.makeText(getApplicationContext(),"Email không được để trống",Toast.LENGTH_LONG).show();
                        } else {
                            customerModel customerModel = new customerModel(id_1, nameC, addressC, phoneNumberC, emailC, 0);
                            DatabaseReference customerRef = database.getReference();
                            customerRef.child("customer/" + id_1).setValue(customerModel);
                            Toast.makeText(getApplicationContext(),"Thành công",Toast.LENGTH_LONG).show();
                        }
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();

    }
}