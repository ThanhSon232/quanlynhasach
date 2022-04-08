package com.example.quanlynhasach.fragment;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.text.TextUtilsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quanlynhasach.R;
import com.example.quanlynhasach.adapter.bookInNoteAdapter;
import com.example.quanlynhasach.model.bookModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class addNewNote extends Fragment implements View.OnClickListener{
    ImageButton previous;
    EditText noteID;
    Button random;
    Button add;
    FirebaseDatabase database;
    ArrayList<bookModel> bookModelArrayList = new ArrayList<>();
    bookInNoteAdapter bookInNoteAdapter;
    RecyclerView recyclerView;
    ImageButton check;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_add_new_note, container, false);
        previous = view.findViewById(R.id.previous);
        previous.setOnClickListener(this);
        random = view.findViewById(R.id.random);
        random.setOnClickListener(this);
        noteID = view.findViewById(R.id.noteID);
        add = view.findViewById(R.id.add);
        add.setOnClickListener(this);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
        database = FirebaseDatabase.getInstance("https://quanlynhasach-c1a4c-default-rtdb.asia-southeast1.firebasedatabase.app/");
        bookInNoteAdapter = new bookInNoteAdapter(bookModelArrayList,getContext());
        recyclerView.setAdapter(bookInNoteAdapter);
        check = view.findViewById(R.id.edit_ok);
        check.setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.previous:
                getActivity().getSupportFragmentManager().popBackStack("addNewNote", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                getActivity().findViewById(R.id.appbar).setVisibility(View.VISIBLE);
                break;

            case R.id.random:
                noteID.setText(UUID.randomUUID().toString());
                break;
            case R.id.add:
                LayoutInflater factory = LayoutInflater.from(getContext());
                View deleteDialogView = factory.inflate(R.layout.note_input_dialog, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setView(deleteDialogView);
                handle(deleteDialogView);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TextView name = deleteDialogView.findViewById(R.id.name);
                        String i = name.getText().toString();
                        EditText id = deleteDialogView.findViewById(R.id.bookID);
                        EditText quantity = deleteDialogView.findViewById(R.id.receivedQuantity);
                        if(TextUtils.isEmpty(i)){
                            Toast.makeText(getContext(),"Lỗi",Toast.LENGTH_LONG).show();
                        }
                        else{
                            DatabaseReference myRef = database.getReference().child("books/" + id.getText().toString());
                            myRef.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                @Override
                                public void onSuccess(DataSnapshot dataSnapshot) {
                                    bookModel bookModel = dataSnapshot.getValue(com.example.quanlynhasach.model.bookModel.class);
                                    bookModel.setSoLuongConLai(Integer.valueOf(quantity.getText().toString()));
                                    bookModelArrayList.add(bookModel);
                                    bookInNoteAdapter.notifyDataSetChanged();
                                }
                            });
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
                break;
            case R.id.edit_ok:
                String i = noteID.getText().toString();
                if(TextUtils.isEmpty(i)){
                    Toast.makeText(getContext(),"Lỗi",Toast.LENGTH_LONG).show();
                }
                else {
                    DatabaseReference arrayTicketRef = database.getReference("notes");
                    arrayTicketRef.child(i).setValue(bookInNoteAdapter.getBooks());
                    getActivity().getSupportFragmentManager().popBackStack("addNewNote", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    getActivity().findViewById(R.id.appbar).setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    void handle(View s){
        EditText bookID = s.findViewById(R.id.bookID);
        Button find = s.findViewById(R.id.find);
        TextView name = s.findViewById(R.id.name);
        TextView author =  s.findViewById(R.id.author);
        TextView quantity =  s.findViewById(R.id.quantity);

        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String i = bookID.getText().toString();
                if(TextUtils.isEmpty(i)){
                    bookID.setError("Mã sách không được trống");
                    bookID.requestFocus();
                }
                else {
                    DatabaseReference myRef = database.getReference().child("books/" + i);
                    myRef.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                        @Override
                        public void onSuccess(DataSnapshot dataSnapshot) {
                            bookModel bookModel = dataSnapshot.getValue(com.example.quanlynhasach.model.bookModel.class);
                            if(bookModel != null){
                                name.setText(bookModel.getTenSach());
                                author.setText(bookModel.getTacGia());
                                quantity.setText(bookModel.getSoLuongConLai().toString());
                            }
                            else {
                                Toast.makeText(getContext(),"Không tìm thấy",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }
}