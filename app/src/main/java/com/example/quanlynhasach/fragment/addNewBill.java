package com.example.quanlynhasach.fragment;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quanlynhasach.R;
import com.example.quanlynhasach.adapter.bookAdapter;
import com.example.quanlynhasach.adapter.bookInNoteAdapter;
import com.example.quanlynhasach.adapter.noteAdapter;
import com.example.quanlynhasach.model.billModel;
import com.example.quanlynhasach.model.bookModel;
import com.example.quanlynhasach.model.customerModel;
import com.example.quanlynhasach.model.noteModel;
import com.example.quanlynhasach.model.receiptModel;
import com.example.quanlynhasach.model.ruleModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

public class addNewBill extends Fragment implements View.OnClickListener{
    TextView id;
    TextView date;
    EditText customerID;
    Button find;
    ImageButton previous;
    Integer debt = 0;
    Integer preDebt;
    Button add;
    ImageButton check;
    RecyclerView recyclerView;
    TextView customerName;
    boolean _check =true;
    LinearLayout name;
    ruleModel _rule;
    ArrayList<bookModel> bookModelArrayList = new ArrayList<>();
    com.example.quanlynhasach.adapter.bookInNoteAdapter bookInNoteAdapter;
    FirebaseDatabase database;
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM-yyyy");
    LocalDateTime now = LocalDateTime.now();
    String monthYear = dtf.format(now);
    String preMonthYear = dtf.format(now.minusMonths(1));


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_new_bill, container, false);
        database = FirebaseDatabase.getInstance("https://quanlynhasach-c1a4c-default-rtdb.asia-southeast1.firebasedatabase.app/");
        id = view.findViewById(R.id.id);
        previous = view.findViewById(R.id.previous);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack("addNewBill", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                getActivity().findViewById(R.id.appbar).setVisibility(View.VISIBLE);
            }
        });
        date = view.findViewById(R.id.date);
        customerID = view.findViewById(R.id.customerID);
        find = view.findViewById(R.id.find);
        find.setOnClickListener(this);
        add = view.findViewById(R.id.add);
        add.setOnClickListener(this);
        recyclerView = view.findViewById(R.id.recycler_view);
        bookInNoteAdapter = new bookInNoteAdapter(bookModelArrayList,getContext());
        check = view.findViewById(R.id.edit_ok);
        check.setOnClickListener(this);
        customerName = view.findViewById(R.id.customerName);
        name = view.findViewById(R.id.name);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
        recyclerView.setAdapter(bookInNoteAdapter);
        getData();
        return view;
    }

    void getData(){
        String timeStamp = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
        String str[] = UUID.randomUUID().toString().split("-");
        id.setText(str[0]);
        date.setText(timeStamp);

    }

    private Integer inStock = 0;
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.find:
                String ID = customerID.getText().toString();
                if(TextUtils.isEmpty(ID)){
                    customerID.setError("ID không được để trống");
                    customerID.requestFocus();
                }
                else {
                    DatabaseReference customerRef = database.getReference().child("customer/"+ID);
                    customerRef.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                        @Override
                        public void onSuccess(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.child("name").getValue() != null){
                                name.setVisibility(View.VISIBLE);
                                customerName.setText(dataSnapshot.child("name").getValue().toString());
                            }
                            else {
                                Toast.makeText(getContext(),"Lỗi",Toast.LENGTH_LONG).show();

                            }
                        }
                    });
                }
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
                        }else{
                            if(inStock - Integer.parseInt((quantity.getText().toString()))<=_rule.getLuongTonToiThieuBan()&&_rule.isSwitch2()){
                                Toast.makeText(getContext(),"Số sách tồn lại phải > "+_rule.getLuongTonToiThieuBan(),Toast.LENGTH_LONG).show();
                            }else{

                                DatabaseReference myRef = database.getReference().child("books/" + id.getText().toString());
                                myRef.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                    @Override
                                    public void onSuccess(DataSnapshot dataSnapshot) {
                                        bookModel bookModel = dataSnapshot.getValue(com.example.quanlynhasach.model.bookModel.class);
                                        bookModel.setSoLuongNhap(Integer.valueOf(quantity.getText().toString()));
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
                String i = customerName.getText().toString();
                if(TextUtils.isEmpty(i)){
                    Toast.makeText(getContext(),"Lỗi",Toast.LENGTH_LONG).show();
                }
                else {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                    builder1.setTitle("Thanh toán hay ghi nợ?");
                    builder1.setItems(new CharSequence[]
                                    {"Thanh toán", "Ghi nợ", "Hủy"},
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // The 'which' argument contains the index position
                                    // of the selected item
                                    switch (which) {
                                        case 0:
                                            String ID = customerID.getText().toString();
                                            DatabaseReference arrayTicketRef = database.getReference("bills");
                                            ArrayList<bookModel> bookModelArrayList = new ArrayList<>();
                                            DatabaseReference updateDebt1 = database.getReference("customer/"+ID);
                                            DatabaseReference Debt = database.getReference("debt/"+ID);

                                            for(bookModel model : bookInNoteAdapter.getBooks()){
                                                debt += model.getDonGia()*model.getSoLuongNhap();

                                            }


                                            updateDebt1.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                                @Override
                                                public void onSuccess(DataSnapshot dataSnapshot) {
                                                    preDebt = Integer.valueOf(dataSnapshot.child("debt").getValue().toString());
                                                    Integer totalDebt = preDebt+ debt;
                                                    if(!(totalDebt>_rule.getTienNoToiDa()&&_rule.isSwitch2())){
                                                        updateDebt1.child("debt").setValue(totalDebt);
                                                    }
                                                }
                                            });


                                            debt=0;
                                            for(bookModel model : bookInNoteAdapter.getBooks()){
                                                bookModel tempModel = new bookModel(model.getMaSach(),null,null,null,null,model.getSoLuongNhap(),null);
                                                bookModelArrayList.add(tempModel);

                                                debt += model.getDonGia()*model.getSoLuongNhap();
                                                DatabaseReference updateQuantity = database.getReference("books/"+model.getMaSach()+"/soLuongConLai");
                                                updateQuantity.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                                                        if(!((preDebt+debt)>_rule.getTienNoToiDa()&&_rule.isSwitch2())){
                                                            updateQuantity.setValue(Integer.valueOf(task.getResult().getValue().toString())- tempModel.getSoLuongConLai());
                                                        }
                                                    }
                                                });
                                            }
                                            Debt.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                                @Override
                                                public void onSuccess(DataSnapshot dataSnapshot) {
                                                    Integer temp = preDebt;
                                                    if(!((preDebt+debt)>_rule.getTienNoToiDa()&&_rule.isSwitch2())){
                                                    Debt.child(monthYear+"/last").setValue(temp + debt);

                                                    billModel bill = new billModel(id.getText().toString(),date.getText().toString(),customerName.getText().toString(),customerID.getText().toString(), bookModelArrayList);
                                                    arrayTicketRef.child(bill.getId()).setValue(bill);
                                                    addNewReceipt();
                                                    }
                                                    else{

                                                        Toast.makeText(getContext(),"Vượt quá nợ tối đa",Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            });



                                            break;
                                        case 1:
                                            String ID_1 = customerID.getText().toString();
                                            DatabaseReference arrayTicketRef1 = database.getReference("bills");
                                            DatabaseReference updateDebt = database.getReference("customer/"+ID_1);
                                            DatabaseReference Debt_1 = database.getReference("debt/"+ID_1);
                                            ArrayList<bookModel> bookModelArrayList1 = new ArrayList<>();

                                            for(bookModel model : bookInNoteAdapter.getBooks()){

                                                debt += model.getDonGia()*model.getSoLuongNhap();

                                            }

                                            updateDebt.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                                @Override
                                                public void onSuccess(DataSnapshot dataSnapshot) {
                                                    preDebt = Integer.valueOf(dataSnapshot.child("debt").getValue().toString());
                                                    if(!((preDebt+debt)>_rule.getTienNoToiDa()&&_rule.isSwitch2())){

                                                        updateDebt.child("debt").setValue(Integer.valueOf(dataSnapshot.child("debt").getValue().toString()) + debt);
                                                    }
                                                }
                                            });


                                            debt=0;
                                            for(bookModel model : bookInNoteAdapter.getBooks()){
                                                bookModel tempModel = new bookModel(model.getMaSach(),null,null,null,null,model.getSoLuongNhap(),null);
                                                bookModelArrayList1.add(tempModel);
                                                debt += model.getDonGia()*model.getSoLuongNhap();
                                                DatabaseReference updateQuantity = database.getReference("books/"+model.getMaSach()+"/soLuongConLai");
                                                updateQuantity.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                                                        if(!((preDebt+debt)>_rule.getTienNoToiDa()&&_rule.isSwitch2())){
                                                            updateQuantity.setValue(Integer.valueOf(task.getResult().getValue().toString())- tempModel.getSoLuongConLai());
                                                        }

                                                    }
                                                });
                                            }


                                            Debt_1.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                                @Override
                                                public void onSuccess(DataSnapshot dataSnapshot) {
                                                    if(!((preDebt+debt)>_rule.getTienNoToiDa()&&_rule.isSwitch2())) {
                                                        Integer temp = preDebt;
                                                        Debt_1.child(monthYear + "/last").setValue(temp + debt);
                                                        billModel bill1 = new billModel(id.getText().toString(),date.getText().toString(),customerName.getText().toString(),customerID.getText().toString(), bookModelArrayList1);
                                                        arrayTicketRef1.child(bill1.getId()).setValue(bill1);
                                                        getActivity().getSupportFragmentManager().popBackStack("addNewBill", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                                        getActivity().findViewById(R.id.appbar).setVisibility(View.VISIBLE);
                                                    }else{

                                                    }
                                                }
                                            });



                                            break;
                                        case 2:
                                              break;

                                    }
                                }
                            });
                    builder1.create().show();

                }
                break;
        }
    }

    void addNewReceipt(){
        LayoutInflater factory = LayoutInflater.from(getContext());
        View deleteDialogView = factory.inflate(R.layout.bill_input_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(deleteDialogView);
        EditText id = deleteDialogView.findViewById(R.id.customerID);
        id.setText(customerID.getText() + "");
        Button find = deleteDialogView.findViewById(R.id.find);
        find.setVisibility(View.GONE);
        String[] m = UUID.randomUUID().toString().split("-");
        String id_1 = m[0];
        Switch switch4 = deleteDialogView.findViewById(R.id.switch4);
        switch4.setVisibility(View.GONE);
        handle(deleteDialogView,customerID.getText().toString());
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                TextView name = deleteDialogView.findViewById(R.id.name);
                String nameC = name.getText().toString();
                EditText money = deleteDialogView.findViewById(R.id.money);
                String moneyC = money.getText().toString();
                TextView date = deleteDialogView.findViewById(R.id.date);
                DatabaseReference receipt = database.getReference().child("receipt/"+id.getText().toString());
                if(TextUtils.isEmpty(nameC)){
                    Toast.makeText(getContext(),"Không tìm thấy ID",Toast.LENGTH_LONG).show();
                }
                else if(TextUtils.isEmpty(moneyC)){
                    Toast.makeText(getContext(),"Phải nhập tiền",Toast.LENGTH_LONG).show();
                }
                else {
                        DatabaseReference customer = database.getReference().child("customer/"+customerID.getText().toString());
                        customer.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                            @Override
                            public void onSuccess(DataSnapshot dataSnapshot) {
                                Integer debt = Integer.valueOf(dataSnapshot.child("debt").getValue().toString());
                                if(Integer.valueOf(moneyC) > debt){
                                    Toast.makeText(getContext(),"Số tiền thu không được quá số tiền nợ",Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    customer.child("debt").setValue(debt - Integer.valueOf(moneyC));
                                    receiptModel receiptModel = new receiptModel(id_1,date.getText().toString(),Integer.valueOf(moneyC));
                                    receipt.child(receiptModel.getMaPhieuThu()).setValue(receiptModel);
                                    getActivity().getSupportFragmentManager().popBackStack("addNewBill", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                    getActivity().findViewById(R.id.appbar).setVisibility(View.VISIBLE);
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

    void handle(View deleteDialogView, String id){
        TextView name = deleteDialogView.findViewById(R.id.name);
        TextView address = deleteDialogView.findViewById(R.id.address);
        TextView phoneNumber = deleteDialogView.findViewById(R.id.phoneNumber);
        TextView email = deleteDialogView.findViewById(R.id.email);
        TextView date = deleteDialogView.findViewById(R.id.date);

        DatabaseReference customer = database.getReference().child("customer/"+id);
        customer.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    customerModel customerModel = dataSnapshot.getValue(customerModel.class);
                    name.setText(customerModel.getName());
                    address.setText(customerModel.getAddress());
                    email.setText(customerModel.getEmail());
                    phoneNumber.setText(customerModel.getPhoneNumber());
                    String timeStamp = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
                    date.setText(timeStamp + "");
                }
                else {
                    Toast.makeText(getContext(),"Khong tìm thấy ID",Toast.LENGTH_LONG).show();
                }
            }
        });

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