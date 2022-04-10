package com.example.quanlynhasach.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.quanlynhasach.R;
import com.example.quanlynhasach.adapter.noteAdapter;
import com.example.quanlynhasach.model.noteModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class goodReceivedNoteFragment extends Fragment {
    RecyclerView recyclerView;
    com.example.quanlynhasach.adapter.noteAdapter noteAdapter;
    FirebaseDatabase database;
    ArrayList<noteModel> noteModels = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_good_received_note, container, false);
        database = FirebaseDatabase.getInstance("https://quanlynhasach-c1a4c-default-rtdb.asia-southeast1.firebasedatabase.app/");
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
        noteAdapter = new noteAdapter(noteModels,getContext());
        recyclerView.setAdapter(noteAdapter);
        getData();
        return view;
    }

    void getData(){
        DatabaseReference getNotes = database.getReference().child("notes");
        getNotes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                noteModels.clear();
                for (DataSnapshot data : snapshot.getChildren()){
                    noteModels.add(data.getValue(noteModel.class));
                }
                noteAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}