package com.example.quanlynhasach.fragment;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.quanlynhasach.R;
import com.example.quanlynhasach.adapter.reportBookAdapter;
import com.example.quanlynhasach.model.billModel;
import com.example.quanlynhasach.model.bookModel;
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
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public class reportBookFragment extends Fragment {
    TableView<reportBookModel> tableView;
    ImageButton clock;
    String[] header = {"STT","Tên sách","Tháng","Tồn đầu","Phát sinh","Tồn Cuối"};
    reportBookAdapter adapter;
    ArrayList<reportBookModel> arrayList = new ArrayList<reportBookModel>();
    FirebaseDatabase database;
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
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        if (i1 == 0) {
                            date = i +"";
                            Toast.makeText(getContext(),date,Toast.LENGTH_LONG).show();
                                }
                        else {
                            date = i1+"-"+i;
                        }
                        getData(date);
                    }
                });
                pd.show(getFragmentManager(), "MonthYearPickerDialog");
            }
        });
        adapter = new reportBookAdapter(getContext(),arrayList);
        tableView.setDataAdapter(adapter);
        return view;
    }

    void getData(String s){
        DatabaseReference myRef = database.getReference();
        Query query = myRef.child("notes").orderByChild("date");
        query.addValueEventListener(new ValueEventListener() {
            ArrayList<noteModel> noteModels = new ArrayList<>();
            ArrayList<billModel> billModels = new ArrayList<>();
            HashMap<String,reportBookModel> map = new HashMap<>();
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                for(DataSnapshot data : snapshot.getChildren()){
                    String str[] = data.child("date").getValue().toString().split(" ");
                    if(str[0].endsWith(s)){
                        noteModels.add(data.getValue(noteModel.class));
                    }
                }
                for(int i = 0 ; i < noteModels.size() ; i++){
                    for(int j = 0 ; j < noteModels.get(i).getItems().size();j++){
                        bookModel t = noteModels.get(i).getItems().get(j);
                       if(!map.containsKey(t.getMaSach())){
                           map.put(t.getMaSach(),new reportBookModel(j,t.getMaSach(),t.getTenSach(),s,t.getSoLuongConLai(),t.getSoLuongNhap(),0));
                       }
                       else {
                           map.get(t.getMaSach()).setPhatSinh(map.get(t.getMaSach()).getPhatSinh() + t.getSoLuongNhap());
                       }
                    }
                }

                Query query = myRef.child("bills").orderByChild("date");
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot data : snapshot.getChildren()){
                            String str[] = data.child("date").getValue().toString().split(" ");
                            if(str[0].endsWith(s)){
                                billModels.add(data.getValue(billModel.class));
                            }
                        }

                        for(int i = 0 ; i < billModels.size() ; i++){
                            for(int j = 0 ; j < billModels.get(i).getItems().size();j++){
                                bookModel t = billModels.get(i).getItems().get(j);
                                map.get(t.getMaSach()).setTonCuoi((map.get(t.getMaSach()).getTonCuoi() + t.getSoLuongConLai()));
                            }
                        }

                        int i = 1;
                        for(Map.Entry<String,reportBookModel> e : map.entrySet()){
                            reportBookModel temp = e.getValue();
                            temp.setStt(i);
                            temp.setTonCuoi(e.getValue().getTonDau() + e.getValue().getPhatSinh() - e.getValue().getTonCuoi());
                            arrayList.add(temp);
                            i++;
                        }


                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}

