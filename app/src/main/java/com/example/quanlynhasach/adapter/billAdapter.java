package com.example.quanlynhasach.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlynhasach.MainActivity;
import com.example.quanlynhasach.R;
import com.example.quanlynhasach.fragment.addNewNote;
import com.example.quanlynhasach.fragment.updateBill;
import com.example.quanlynhasach.model.billModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
        holder.date.setText(billModelArrayList.get(position).getDate());
        holder.billID.setText(billModelArrayList.get(position).getId());
        holder.customerID.setText(billModelArrayList.get(position).getCustomerID());
        holder.quantity.setText(billModelArrayList.get(position).getItems().size()+"");
        holder.whole.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                FirebaseDatabase database = FirebaseDatabase.getInstance("https://quanlynhasach-c1a4c-default-rtdb.asia-southeast1.firebasedatabase.app/");
                                DatabaseReference myRef = database.getReference().child("bills/"+billModelArrayList.get(holder.getAdapterPosition()).getId());
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
                updateBill updateBill = new updateBill();
                Bundle bundle = new Bundle();
                bundle.putSerializable("data",  billModelArrayList.get(holder.getAdapterPosition()));
                updateBill.setArguments(bundle);
                ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout,updateBill).addToBackStack("updateBill").commit();
                ((AppCompatActivity) context).findViewById(R.id.appbar).setVisibility(View.GONE);
            }
        });
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
        CardView whole;
        public billViewHolder(@NonNull View itemView) {
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
