package com.example.quanlynhasach.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlynhasach.R;
import com.example.quanlynhasach.model.customerModel;
import com.example.quanlynhasach.model.receiptModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

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
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://quanlynhasach-c1a4c-default-rtdb.asia-southeast1.firebasedatabase.app/");
        holder.date.setText(receiptModels.get(position).getNgayLap());
        holder.billID.setText(receiptModels.get(position).getMaPhieuThu());
        holder.customerID.setText(receiptModels.get(position).getCustomerID());
        holder.quantity.setText(receiptModels.get(position).getSoTien() +"");
        holder.whole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater factory = LayoutInflater.from(context);
                View deleteDialogView = factory.inflate(R.layout.update_bill_dialog, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setView(deleteDialogView);
                TextView id = deleteDialogView.findViewById(R.id.customerID);
                TextView name = deleteDialogView.findViewById(R.id.name);
                TextView address = deleteDialogView.findViewById(R.id.address);
                TextView phoneNumber = deleteDialogView.findViewById(R.id.phoneNumber);
                TextView email = deleteDialogView.findViewById(R.id.email);
                TextView date = deleteDialogView.findViewById(R.id.date);
                EditText money = deleteDialogView.findViewById(R.id.money);
                DatabaseReference customer = database.getReference().child("customer/"+receiptModels.get(holder.getAdapterPosition()).getCustomerID());
                customer.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()) {
                            id.setText(receiptModels.get(holder.getAdapterPosition()).getCustomerID());
                            customerModel customerModel = dataSnapshot.getValue(customerModel.class);
                            name.setText(customerModel.getName());
                            address.setText(customerModel.getAddress());
                            email.setText(customerModel.getEmail());
                            phoneNumber.setText(customerModel.getPhoneNumber());
                            date.setText(receiptModels.get(holder.getAdapterPosition()).getNgayLap() +"");
                            money.setText(receiptModels.get(holder.getAdapterPosition()).getSoTien() +"");
                        }
                    }
                });
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String moneyC = money.getText().toString();
                        DatabaseReference receipt = database.getReference().child("receipt/"+id.getText().toString());
                        if(TextUtils.isEmpty(moneyC)){
                            Toast.makeText(context,"Phải nhập tiền",Toast.LENGTH_LONG).show();
                        }
                        else {
                            DatabaseReference customer = database.getReference().child("customer/"+id.getText().toString());
                            DatabaseReference Debt = database.getReference("debt/"+id.getText().toString());
                            customer.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                @Override
                                public void onSuccess(DataSnapshot dataSnapshot) {
                                    Integer debt = Integer.valueOf(dataSnapshot.child("debt").getValue().toString());
                                    if(Integer.valueOf(moneyC) > debt){
                                        Toast.makeText(context,"Số tiền thu không được quá số tiền nợ",Toast.LENGTH_LONG).show();
                                    }
                                    else{
                                        customer.child("debt").setValue(debt - Integer.valueOf(moneyC));
                                        receiptModel receiptModel = new receiptModel(receiptModels.get(holder.getAdapterPosition()).getMaPhieuThu(),date.getText().toString(),Integer.valueOf(moneyC));
                                        receipt.child(receiptModel.getMaPhieuThu()).setValue(receiptModel);
                                        Debt.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                            @Override
                                            public void onSuccess(DataSnapshot dataSnapshot) {
                                                String monthYear = date.getText().toString();
                                                monthYear = monthYear.split(" ")[0];
                                                monthYear = monthYear.substring(3,monthYear.length());
                                                Debt.child(monthYear+"/last").setValue(Integer.valueOf(dataSnapshot.child(monthYear+"/last").getValue().toString()) - Integer.valueOf(moneyC));
                                            }
                                        });
                                    }
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
            }
        });
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
