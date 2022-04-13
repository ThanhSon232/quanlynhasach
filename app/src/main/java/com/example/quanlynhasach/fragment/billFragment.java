package com.example.quanlynhasach.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.quanlynhasach.R;
import com.example.quanlynhasach.adapter.billAdapter;
import com.example.quanlynhasach.model.billModel;
import com.example.quanlynhasach.model.noteModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class billFragment extends Fragment implements View.OnClickListener {
    RecyclerView recyclerView;
    com.example.quanlynhasach.adapter.billAdapter billAdapter;
    ArrayList<billModel> view_bills = new ArrayList<>();
    ArrayList<billModel> bills = new ArrayList<>();
    FirebaseDatabase database;
    EditText searchbar;
    Button searchBtn;
    Spinner spinner;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bill, container, false);
        database = FirebaseDatabase.getInstance("https://quanlynhasach-c1a4c-default-rtdb.asia-southeast1.firebasedatabase.app/");
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
        billAdapter = new billAdapter(view_bills,getContext());
        searchbar = view.findViewById(R.id.search_bar);
        searchbar.setOnClickListener(this);

        searchBtn = view.findViewById(R.id.searchButton);
        searchBtn.setOnClickListener(this);
        spinner =view.findViewById(R.id.spinner_bill);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(position==0){
                    searchbar.setHint("yyyymmdd");
                }
                if(position==1){
                    searchbar.setHint("Nhập mã");
                }
                if(position==2){
                    searchbar.setHint("Nhập tên KH");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        recyclerView.setAdapter(billAdapter);
        getData();
        return view;
    }

    void getData(){
        DatabaseReference myRef = database.getReference().child("bills");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                view_bills.clear();
                bills.clear();
                for(DataSnapshot data : snapshot.getChildren()){
                    view_bills.add(data.getValue(billModel.class));
                    bills.add(data.getValue(billModel.class));
                }
                billAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.searchButton:

                if(spinner.getSelectedItemPosition()==0){
                    view_bills.clear();
                    for(billModel b: bills){
                        if(b.getDate().contains(searchbar.getText().toString())){
                            view_bills.add(b);
                        }
                    }
                    billAdapter.notifyDataSetChanged();
                }
                if(spinner.getSelectedItemPosition()==1){
                    view_bills.clear();
                    for(billModel b: bills){
                        if(b.getId().contains(searchbar.getText().toString())){
                            view_bills.add(b);
                        }
                    }
                    billAdapter.notifyDataSetChanged();
                }
                if(spinner.getSelectedItemPosition()==2){
                    view_bills.clear();
                    for(billModel b: bills){
                        if(b.getCustomerName().toLowerCase().contains(searchbar.getText().toString().toLowerCase())){
                            view_bills.add(b);
                        }
                    }
                    billAdapter.notifyDataSetChanged();
                }


        }
    }
}