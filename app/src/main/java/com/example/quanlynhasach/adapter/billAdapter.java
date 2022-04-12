package com.example.quanlynhasach.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlynhasach.R;
import com.example.quanlynhasach.model.billModel;

import java.util.ArrayList;

public class billAdapter extends RecyclerView.Adapter<billAdapter.billViewHolder> {
    ArrayList<billModel> billModelArrayList;
    Context context;

    public billAdapter(ArrayList<billModel> billModelArrayList, Context context) {
        this.billModelArrayList = billModelArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public billViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bill,parent,false);
        return new billViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull billViewHolder holder, int position) {
        if(billModelArrayList.isEmpty()) return;

        holder.name.setText(billModelArrayList.get(position).getCustomerName());
        holder.date.setText(billModelArrayList.get(position).getDate());
        holder.billID.setText(billModelArrayList.get(position).getId());
        holder.customerID.setText(billModelArrayList.get(position).getCustomerID());
        holder.quantity.setText(billModelArrayList.get(position).getItems().size()+"");

    }

    @Override
    public int getItemCount() {
        return billModelArrayList.size();
    }

    class billViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView customerID;
        TextView billID;
        TextView date;
        TextView quantity;
        public billViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            customerID = itemView.findViewById(R.id.customerID);
            billID = itemView.findViewById(R.id.bill);
            date = itemView.findViewById(R.id.date);
            quantity = itemView.findViewById(R.id.quantity);
        }
    }
}
