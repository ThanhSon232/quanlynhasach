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

import com.example.quanlynhasach.R;
import com.example.quanlynhasach.adapter.*;
import com.example.quanlynhasach.model.bookModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;


public class bookFragment extends Fragment implements View.OnClickListener {
    bookAdapter bookAdapter;
    RecyclerView recyclerView;
    Button search;
    EditText searchBar;
    Spinner spinner;
    ArrayList<bookModel> view_books = new ArrayList<>();
    ArrayList<bookModel> books = new ArrayList<>();
    FirebaseDatabase database;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book, container, false);
        database = FirebaseDatabase.getInstance("https://quanlynhasach-c1a4c-default-rtdb.asia-southeast1.firebasedatabase.app/");
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
        bookAdapter = new bookAdapter(view_books,getContext());
        search = view.findViewById(R.id.searchButton);
        search.setOnClickListener(this);
        searchBar = view.findViewById(R.id.search_bar);
        spinner = view.findViewById(R.id.spinner_book);
        searchBar.setOnClickListener(this);
        recyclerView.setAdapter(bookAdapter);
        getData();
        return view;
    }

    void getData(){
        DatabaseReference myRef = database.getReference().child("books");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                view_books.clear();
                books.clear();
                for(DataSnapshot data : snapshot.getChildren()){
                    view_books.add(data.getValue(bookModel.class));
                    books.add(data.getValue(bookModel.class));
                }
                bookAdapter.notifyDataSetChanged();
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
                if(spinner.getSelectedItem().toString().equals("Tên")){
                    String key = searchBar.getText().toString();
                    view_books.clear();
                    for (bookModel b:books){
                        if(b.getTenSach().toLowerCase().contains(key.toLowerCase())){
                            view_books.add(b);
                        }
                    }
                    bookAdapter.notifyDataSetChanged();
                }
                else if(spinner.getSelectedItem().toString().equals("Mã")){
                    String key = searchBar.getText().toString();
                    view_books.clear();
                    for (bookModel b:books){
                        if(b.getMaSach()!=null){
                            if(b.getMaSach().toLowerCase().contains(key.toLowerCase())){
                                view_books.add(b);
                            }
                        }
                    }
                    bookAdapter.notifyDataSetChanged();
                }


        }

    }
}