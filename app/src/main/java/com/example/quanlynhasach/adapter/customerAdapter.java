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
import com.example.quanlynhasach.model.customerModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class customerAdapter extends RecyclerView.Adapter<customerAdapter.customerViewHolder>{
    ArrayList<customerModel> customerModels;
    Context context;

    public customerAdapter(ArrayList<customerModel> customerModels, Context context) {
        this.customerModels = customerModels;
        this.context = context;
    }

    @NonNull
    @Override
    public customerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer,parent,false);
        return new customerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull customerViewHolder holder, int position) {
        if (customerModels.isEmpty()) return;

        holder.name.setText(customerModels.get(position).getName());
        holder.id.setText(customerModels.get(position).getCustomerID());
        holder.address.setText(customerModels.get(position).getAddress());
        holder.phoneNumber.setText(customerModels.get(position).getPhoneNumber());
        holder.email.setText(customerModels.get(position).getEmail());
        holder.debt.setText(customerModels.get(position).getDebt() + "");
        holder.customer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                FirebaseDatabase database = FirebaseDatabase.getInstance("https://quanlynhasach-c1a4c-default-rtdb.asia-southeast1.firebasedatabase.app/");
                                DatabaseReference myRef = database.getReference().child("customer/"+customerModels.get(holder.getAdapterPosition()).getCustomerID());
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
        return customerModels.size();
    }

    class customerViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView id;
        TextView address;
        TextView phoneNumber;
        TextView email;
        TextView debt;
        CardView customer;

        public customerViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            id = itemView.findViewById(R.id.customerID);
            address = itemView.findViewById(R.id.address);
            phoneNumber = itemView.findViewById(R.id.phoneNumber);
            email = itemView.findViewById(R.id.email);
            debt = itemView.findViewById(R.id.debt);
            customer = itemView.findViewById(R.id.customer);
        }
    }
}
