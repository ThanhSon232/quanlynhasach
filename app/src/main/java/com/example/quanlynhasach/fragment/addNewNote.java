package com.example.quanlynhasach.fragment;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quanlynhasach.R;
import com.example.quanlynhasach.adapter.bookInNoteAdapter;
import com.example.quanlynhasach.model.bookModel;
import com.example.quanlynhasach.model.noteModel;
import com.example.quanlynhasach.model.ruleModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
    ruleModel _rule;
    Integer inStock;

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
                String z[] = UUID.randomUUID().toString().split("-");
                noteID.setText(z[0]);
                break;
            case R.id.add:
                DatabaseReference rule = database.getReference().child("rule");


                rule.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        _rule = snapshot.getValue(ruleModel.class);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
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
                            if(Integer.parseInt((quantity.getText().toString()))>_rule.getLuongNhapToiThieu()&&_rule.isSwitch1()){
                                Toast.makeText(getContext(),"Vượt quá số lượng qui định",Toast.LENGTH_LONG).show();
                            }else if (inStock >=200&&_rule.isSwitch1()){
                                Toast.makeText(getContext(),"Sách còn tồn nhiều",Toast.LENGTH_LONG).show();
                            }else{
                                DatabaseReference myRef = database.getReference().child("books/" + id.getText().toString());
                                myRef.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                    @Override
                                    public void onSuccess(DataSnapshot dataSnapshot) {
                                        bookModel bookModel = dataSnapshot.getValue(com.example.quanlynhasach.model.bookModel.class);
                                        bookModel.setSoLuongNhap(Integer.valueOf(quantity.getText().toString()));
                                        bookModel.setSoLuongConLai(bookModel.getSoLuongConLai());
                                        bookModelArrayList.add(bookModel);
                                        bookInNoteAdapter.notifyDataSetChanged();
                                    }
                                });
                            }

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
                    String timeStamp = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
                    DatabaseReference arrayTicketRef = database.getReference("notes");
                    ArrayList<bookModel> bookModelArrayList = new ArrayList<>();
                    for(bookModel model : bookInNoteAdapter.getBooks()){
                        bookModel tempModel = new bookModel(model.getMaSach(),model.getTenSach(),null,null,null,model.getSoLuongConLai(),model.getSoLuongNhap(),null);
                        bookModelArrayList.add(tempModel);
                        DatabaseReference updateQuantity = database.getReference("books/"+model.getMaSach()+"/soLuongConLai");
                        updateQuantity.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                updateQuantity.setValue(Integer.valueOf(task.getResult().getValue().toString())+ tempModel.getSoLuongNhap());
                            }
                        });
                    }
                    noteModel noteModel = new noteModel(i,timeStamp, bookModelArrayList);
                    arrayTicketRef.child(noteModel.getNoteID()).setValue(noteModel);
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
                                inStock = bookModel.getSoLuongConLai();
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