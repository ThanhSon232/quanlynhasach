package com.example.quanlynhasach.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlynhasach.R;
import com.example.quanlynhasach.model.receiptModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class receiptAdapter extends RecyclerView.Adapter<receiptAdapter.receiptViewHolder>{
    ArrayList<receiptModel> receiptModels;
    Context context;

    public receiptAdapter(ArrayList<receiptModel> receiptModels, Context context) {
        this.receiptModels = receiptModels;
        this.context = context;
    }

    @NonNull
    @Override
    public receiptViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.receipt,parent,false);
        return new receiptViewHolder(view);    }

    @Override
    public void onBindViewHolder(@NonNull receiptViewHolder holder, int position) {
        if(receiptModels.isEmpty()) return;
        holder.date.setText(receiptModels.get(position).getNgayLap());
        holder.billID.setText(receiptModels.get(position).getMaPhieuThu());
        holder.customerID.setText(receiptModels.get(position).getCustomerID());
        holder.quantity.setText(receiptModels.get(position).getSoTien() +"");
        holder.whole.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                FirebaseDatabase database = FirebaseDatabase.getInstance("https://quanlynhasach-c1a4c-default-rtdb.asia-southeast1.firebasedatabase.app/");
                                DatabaseReference myRef = database.getReference().child("receipt/"+receiptModels.get(holder.getAdapterPosition()).getCustomerID() + "/" +receiptModels.get(holder.getAdapterPosition()).getMaPhieuThu());
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
    }

    @Override
    public int getItemCount() {
        return receiptModels.size();
    }

    class receiptViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView customerID;
        TextView billID;
        TextView date;
        TextView quantity;
        CardView whole;
        public receiptViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            customerID = itemView.findViewById(R.id.customerID);
            billID = itemView.findViewById(R.id.bill);
            date = itemView.findViewById(R.id.date);
            quantity = itemView.findViewById(R.id.quantity);
            whole = itemView.findViewById(R.id.whole);
        }
    }
}
