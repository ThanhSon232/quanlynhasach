package com.example.quanlynhasach.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.quanlynhasach.R;
import com.example.quanlynhasach.adapter.billAdapter;
import com.example.quanlynhasach.adapter.bookAdapter;
import com.example.quanlynhasach.adapter.receiptAdapter;
import com.example.quanlynhasach.model.bookModel;
import com.example.quanlynhasach.model.receiptModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class receiptFragment extends Fragment {
    com.example.quanlynhasach.adapter.receiptAdapter receiptAdapter;
    RecyclerView recyclerView;
    ArrayList<receiptModel> receipt = new ArrayList<>();
    FirebaseDatabase database;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_receipt, container, false);
        database = FirebaseDatabase.getInstance("https://quanlynhasach-c1a4c-default-rtdb.asia-southeast1.firebasedatabase.app/");
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
        receiptAdapter = new receiptAdapter(receipt,getContext());
        recyclerView.setAdapter(receiptAdapter);
        getData();
        return view;
    }

    void getData(){
        DatabaseReference myRef = database.getReference().child("receipt");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                receipt.clear();
                for(DataSnapshot data : snapshot.getChildren()){
                    for(DataSnapshot t : data.getChildren()){
                        receiptModel model = t.getValue(receiptModel.class);
                        model.setCustomerID(data.getKey() +"");
                        receipt.add(model);
                    }

                }
                receiptAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}