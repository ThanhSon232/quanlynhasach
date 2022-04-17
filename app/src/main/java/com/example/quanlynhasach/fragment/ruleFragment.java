package com.example.quanlynhasach.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.quanlynhasach.R;
import com.example.quanlynhasach.model.ruleModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ruleFragment extends Fragment implements View.OnClickListener {
    Switch aSwitch;
    Switch bSwitch;
    Switch cSwitch;
    EditText minQuantity;
    EditText minQuantity1;
    EditText minQuantity2;
    EditText maxDebt;
    Button apply1;
    Button apply2;
    FirebaseDatabase database;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rule, container, false);
        database = FirebaseDatabase.getInstance("https://quanlynhasach-c1a4c-default-rtdb.asia-southeast1.firebasedatabase.app/");
        minQuantity = view.findViewById(R.id.minQuantity);
        minQuantity1 = view.findViewById(R.id.minQuantity1);
        minQuantity2 = view.findViewById(R.id.minQuantity2);
        maxDebt = view.findViewById(R.id.maxDebt);
        aSwitch = view.findViewById(R.id.switch1);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                DatabaseReference myRef = database.getReference().child("rules");
                if(!TextUtils.isEmpty(minQuantity.getText().toString()) && !TextUtils.isEmpty(minQuantity1.getText().toString())) {
                    if (b) {
                        myRef.child("switch1").setValue(true);
                    } else {
                        myRef.child("switch1").setValue(false);
                    }
                }
                else {
                    aSwitch.setChecked(false);
                    Toast.makeText(getContext(),"Các trường phải được nhập",Toast.LENGTH_LONG).show();
                }
            }
        });
        bSwitch = view.findViewById(R.id.switch2);
        bSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                DatabaseReference myRef = database.getReference().child("rules");
                if(!TextUtils.isEmpty(minQuantity2.getText().toString()) && !TextUtils.isEmpty(maxDebt.getText().toString())) {
                    if (b) {
                        myRef.child("switch2").setValue(true);
                    } else {
                        myRef.child("switch2").setValue(false);
                    }
                }
                else {
                    bSwitch.setChecked(false);
                    Toast.makeText(getContext(),"Các trường phải được nhập",Toast.LENGTH_LONG).show();
                }
            }
        });
        cSwitch = view.findViewById(R.id.switch3);
        cSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                DatabaseReference myRef = database.getReference().child("rules");
                if(b) {
                    myRef.child("switch3").setValue(true);
                }
                else {
                    myRef.child("switch3").setValue(false);
                }
            }
        });
        apply1 = view.findViewById(R.id.apply1);
        apply1.setOnClickListener(this);
        apply2 = view.findViewById(R.id.apply2);
        apply2.setOnClickListener(this);
        getData();
        return view;
    }

    void getData(){
        minQuantity.setText(ruleModel.getLuongNhapToiThieu() + "");
        minQuantity1.setText(ruleModel.getLuongTonToiThieuBan() + "");
        maxDebt.setText(ruleModel.getTienNoToiDa() + "");
        minQuantity2.setText(ruleModel.getLuongTonToiThieuBan() + "");
        aSwitch.setChecked(ruleModel.isSwitch1());
        bSwitch.setChecked(ruleModel.isSwitch2());
        cSwitch.setChecked(ruleModel.isSwitch3());

        DatabaseReference myRef = database.getReference().child("rules");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()) {
                        ruleModel.setLuongNhapToiThieu(Integer.valueOf(snapshot.child("LuongNhapToiThieu").getValue().toString()));
                        ruleModel.setLuongTonToiThieuNhap(Integer.valueOf(snapshot.child("LuongTonToiThieuNhap").getValue().toString()));
                        ruleModel.setTienNoToiDa(Integer.valueOf(snapshot.child("TienNoToiDa").getValue().toString()));
                        ruleModel.setLuongTonToiThieuBan(Integer.valueOf(snapshot.child("LuongTonToiThieuBan").getValue().toString()));
                        ruleModel.setSwitch1((Boolean) snapshot.child("switch1").getValue());
                        ruleModel.setSwitch2((Boolean) snapshot.child("switch2").getValue());
                        ruleModel.setSwitch3((Boolean) snapshot.child("switch3").getValue());
                        minQuantity.setText(ruleModel.getLuongNhapToiThieu() + "");
                        minQuantity1.setText(ruleModel.getLuongTonToiThieuNhap() + "");
                        maxDebt.setText(ruleModel.getTienNoToiDa() + "");
                        minQuantity2.setText(ruleModel.getLuongTonToiThieuBan() + "");
                        aSwitch.setChecked(ruleModel.isSwitch1());
                        bSwitch.setChecked(ruleModel.isSwitch2());
                        cSwitch.setChecked(ruleModel.isSwitch3());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
    }


    @Override
    public void onClick(View view) {
        DatabaseReference myRef = database.getReference().child("rules");
        Integer i = Integer.valueOf(minQuantity.getText().toString());
        Integer j =  Integer.valueOf(minQuantity1.getText().toString());
        Integer m = Integer.valueOf(maxDebt.getText().toString());
        Integer n =  Integer.valueOf(minQuantity2.getText().toString());
        switch(view.getId()){
            default:
                if(i >=0 && j >= 0 && m > 0 && n > 0){
                    myRef.child("LuongNhapToiThieu").setValue(i);
                    myRef.child("LuongTonToiThieuNhap").setValue(j);
                    myRef.child("TienNoToiDa").setValue(m);
                    myRef.child("LuongTonToiThieuBan").setValue(n);
                    myRef.child("switch1").setValue(aSwitch.isChecked());
                    myRef.child("switch2").setValue(bSwitch.isChecked());
                    myRef.child("switch3").setValue(cSwitch.isChecked());
                }
                else {
                    Toast.makeText(getContext(),"Lỗi", Toast.LENGTH_LONG).show();
                }
        }
    }
}