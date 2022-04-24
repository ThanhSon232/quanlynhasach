package com.example.quanlynhasach.fragment;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.quanlynhasach.R;
import com.example.quanlynhasach.adapter.debtReportAdapter;
import com.example.quanlynhasach.adapter.reportBookAdapter;
import com.example.quanlynhasach.model.billModel;
import com.example.quanlynhasach.model.bookModel;
import com.example.quanlynhasach.model.debtModel;
import com.example.quanlynhasach.model.debtReportModel;
import com.example.quanlynhasach.model.noteModel;
import com.example.quanlynhasach.model.reportBookModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.model.TableColumnDpWidthModel;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public class debtReportFragment extends Fragment {
    TableView<debtReportModel> tableView;
    ImageButton clock;
    String[] header = {"STT","Khách hàng","Tháng","Nợ đầu","Phát Sinh","Nợ cuối"};
    debtReportAdapter adapter;
    ArrayList<debtReportModel> arrayList = new ArrayList<debtReportModel>();
    FirebaseDatabase database;
    ArrayList<debtModel> _debt;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        database = FirebaseDatabase.getInstance("https://quanlynhasach-c1a4c-default-rtdb.asia-southeast1.firebasedatabase.app/");
        View view = inflater.inflate(R.layout.fragment_report_book, container, false);
        tableView = view.findViewById(R.id.tableView);
        tableView.setHeaderBackgroundColor(Color.parseColor("#2ecc71"));
        tableView.setHeaderAdapter(new SimpleTableHeaderAdapter(getContext(),header));
        TableColumnDpWidthModel columnModel = new TableColumnDpWidthModel(getContext(), 6, 100);
        columnModel.setColumnWidth(1, 200);
        tableView.setColumnModel(columnModel);
        clock = view.findViewById(R.id.clock);
        clock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MonthYearPickerDialog pd = new MonthYearPickerDialog();
                pd.setListener(new DatePickerDialog.OnDateSetListener() {
                    String date;
                    String preDate;
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        if (i1 == 0) {

                            date = i +"";

                            Toast.makeText(getContext(),date,Toast.LENGTH_LONG).show();
                                }
                        else {
                            if(i1>=10){
                                date = i1+"-"+i;
                            }else{
                                date = "0"+i1+"-"+i;
                            }

                            if(i1>2&&i1<=10){
                                preDate = "0"+(i1-1)+"-"+i;
                            }else if(i1>10){
                                preDate = (i1-1)+"-"+i;
                            }
                            else{
                                preDate=12+"-"+(i-1);
                            }
                        }
                        getData(date,preDate);
                    }
                });
                pd.show(getFragmentManager(), "MonthYearPickerDialog");
            }
        });
        adapter = new debtReportAdapter(getContext(),arrayList);
        tableView.setDataAdapter(adapter);
        return view;
    }

    void getData(String date, String preDate){

        DatabaseReference myRef = database.getReference();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int count=1;
                for(DataSnapshot data: snapshot.child("debt").getChildren()){
                    Integer stt = count;
                    String id = data.getKey();
                    String tenKhachHang = snapshot.child("customer/"+id+"/name").getValue().toString();
                    int noDau = Integer.valueOf(data.child(date+"/last").getValue().toString());
                    int noCuoi = Integer.valueOf(data.child(preDate+"/last").getValue().toString());
                    int phatSinh = noCuoi - noDau;
                    count++;
                    debtReportModel temp=new debtReportModel(stt,id,tenKhachHang,date,noDau,phatSinh,noCuoi);
                    arrayList.add(temp);
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

}

