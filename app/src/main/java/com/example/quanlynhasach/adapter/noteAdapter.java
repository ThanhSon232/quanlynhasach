package com.example.quanlynhasach.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlynhasach.R;
import com.example.quanlynhasach.model.noteModel;

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
    }

    @Override
    public int getItemCount() {
        return noteModels.size();
    }

    class noteViewHolder extends RecyclerView.ViewHolder{
        TextView noteID;
        TextView date;
        TextView quantity;
        public noteViewHolder(@NonNull View itemView) {
            super(itemView);
            noteID = itemView.findViewById(R.id.noteID);
            date = itemView.findViewById(R.id.date);
            quantity = itemView.findViewById(R.id.quantity);
        }
    }
}
