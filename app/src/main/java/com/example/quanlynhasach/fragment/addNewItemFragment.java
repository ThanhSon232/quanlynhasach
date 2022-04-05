package com.example.quanlynhasach.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import com.example.quanlynhasach.R;

public class addNewItemFragment extends Fragment implements View.OnClickListener {
    Toolbar appbar;
    ImageButton previous;
    ImageView avatar;
    Bundle bundle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_new_item, container, false);
       appbar = getActivity().findViewById(R.id.appbar);
       appbar.setVisibility(View.GONE);
       previous = view.findViewById(R.id.previous);
       previous.setOnClickListener(this);
       avatar = view.findViewById(R.id.avatar);
       bundle = this.getArguments();
       setAvatar();
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.previous:
                getActivity().getSupportFragmentManager().popBackStack("addNewItemFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                appbar.setVisibility(View.VISIBLE);
                break;
        }
    }

    void setAvatar(){
        Uri selectedImage = bundle.getParcelable("data");
        System.out.println(selectedImage);
        String[] filePathColumn = { MediaStore.Images.Media.DATA };

        Cursor cursor = getContext().getContentResolver().query(selectedImage,
                filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();

        avatar.setImageBitmap(BitmapFactory.decodeFile(picturePath));
    }


}