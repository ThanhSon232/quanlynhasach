package com.example.quanlynhasach.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.quanlynhasach.R;
import com.example.quanlynhasach.adapter.bookAdapter;
import com.example.quanlynhasach.adapter.customerAdapter;
import com.example.quanlynhasach.model.bookModel;
import com.example.quanlynhasach.model.customerModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class customerFragment extends Fragment {
    RecyclerView recyclerView;
    ArrayList<customerModel> customerModels = new ArrayList<>();
    FirebaseDatabase database;
    com.example.quanlynhasach.adapter.customerAdapter customerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customer, container, false);
        database = FirebaseDatabase.getInstance("https://quanlynhasach-c1a4c-default-rtdb.asia-southeast1.firebasedatabase.app/");
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
        customerAdapter = new customerAdapter(customerModels,getContext());
        recyclerView.setAdapter(customerAdapter);
        getData();
        return view;
    }

    void getData(){
        DatabaseReference myRef = database.getReference().child("customer");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                customerModels.clear();
                for(DataSnapshot data : snapshot.getChildren()){
                    customerModels.add(data.getValue(customerModel.class));
                }
                customerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}