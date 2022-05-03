package com.example.quanlynhasach.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlynhasach.R;
import com.example.quanlynhasach.fragment.updateBill;
import com.example.quanlynhasach.fragment.updateReceived;
import com.example.quanlynhasach.model.noteModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class noteAdapter extends RecyclerView.Adapter<noteAdapter.noteViewHolder>{
    ArrayList<noteModel> noteModels;
    Context context;

    public noteAdapter(ArrayList<noteModel> noteModels, Context context) {
        this.noteModels = noteModels;
        this.context = context;
    }

    @NonNull
    @Override
    public noteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note,parent,false);
        return new noteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull noteViewHolder holder, int position) {
        if(noteModels.isEmpty()) return;
        holder.noteID.setText(noteModels.get(position).getNoteID());
        holder.date.setText(noteModels.get(position).getDate());
        holder.quantity.setText(noteModels.get(position).getItems().size() + "");
        holder.whole.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                FirebaseDatabase database = FirebaseDatabase.getInstance("https://quanlynhasach-c1a4c-default-rtdb.asia-southeast1.firebasedatabase.app/");
                                DatabaseReference myRef = database.getReference().child("notes/"+noteModels.get(holder.getAdapterPosition()).getNoteID());
                                myRef.removeValue();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Bạn có muốn xóa?").setPositiveButton("Có", dialogClickListener)
                        .setNegativeButton("Không", dialogClickListener).show();
                return true;
            }
        });

        holder.whole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateReceived updateBill = new updateReceived();
                Bundle bundle = new Bundle();
                bundle.putSerializable("data",  noteModels.get(holder.getAdapterPosition()));
                updateBill.setArguments(bundle);
                ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout,updateBill).addToBackStack("updateReceived").commit();
                ((AppCompatActivity) context).findViewById(R.id.appbar).setVisibility(View.GONE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return noteModels.size();
    }

    class noteViewHolder extends RecyclerView.ViewHolder{
        TextView noteID;
        TextView date;
        TextView quantity;
        CardView whole;
        public noteViewHolder(@NonNull View itemView) {
            super(itemView);
            noteID = itemView.findViewById(R.id.noteID);
            date = itemView.findViewById(R.id.date);
            quantity = itemView.findViewById(R.id.quantity);
            whole = itemView.findViewById(R.id.whole);
        }
    }
}
