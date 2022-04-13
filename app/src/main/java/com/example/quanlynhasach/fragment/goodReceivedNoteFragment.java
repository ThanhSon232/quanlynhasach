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
import com.example.quanlynhasach.adapter.noteAdapter;
import com.example.quanlynhasach.model.noteModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class goodReceivedNoteFragment extends Fragment implements View.OnClickListener {
    RecyclerView recyclerView;
    com.example.quanlynhasach.adapter.noteAdapter noteAdapter;
    FirebaseDatabase database;
    EditText searcbar;
    Button searcBtn;
    Spinner spinner;
    ArrayList<noteModel> view_noteModels = new ArrayList<>();
    ArrayList<noteModel> noteModels = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_good_received_note, container, false);
        database = FirebaseDatabase.getInstance("https://quanlynhasach-c1a4c-default-rtdb.asia-southeast1.firebasedatabase.app/");
        recyclerView = view.findViewById(R.id.recycler_view);
        searcbar = view.findViewById(R.id.search_bar);
        searcbar.setOnClickListener(this);

        searcBtn = view.findViewById(R.id.searchButton);
        searcBtn.setOnClickListener(this);
        spinner =view.findViewById(R.id.spinner_book);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(position==0){
                    searcbar.setHint("yyyymmdd");
                }
                if(position==1){
                    searcbar.setHint("Nhập mã");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
        noteAdapter = new noteAdapter(view_noteModels,getContext());
        recyclerView.setAdapter(noteAdapter);
        getData();
        return view;
    }

    void getData(){
        DatabaseReference getNotes = database.getReference().child("notes");
        getNotes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                view_noteModels.clear();
                noteModels.clear();
                for (DataSnapshot data : snapshot.getChildren()){
                    view_noteModels.add(data.getValue(noteModel.class));
                    noteModels.add(data.getValue(noteModel.class));

                }
                noteAdapter.notifyDataSetChanged();
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
                    view_noteModels.clear();
                    for(noteModel n:noteModels){
                        if(n.getDate().contains(searcbar.getText().toString())){
                            view_noteModels.add(n);
                        }
                    }
                    noteAdapter.notifyDataSetChanged();
                }
                if(spinner.getSelectedItemPosition()==1){
                    view_noteModels.clear();
                    for(noteModel n:noteModels){
                        if(n.getNoteID().contains(searcbar.getText().toString())){
                            view_noteModels.add(n);
                        }
                    }
                    noteAdapter.notifyDataSetChanged();
                }


        }

    }
}