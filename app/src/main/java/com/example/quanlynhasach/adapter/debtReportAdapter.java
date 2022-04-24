package com.example.quanlynhasach.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.quanlynhasach.model.debtReportModel;
import com.example.quanlynhasach.model.reportBookModel;

import java.util.List;

import de.codecrafters.tableview.TableDataAdapter;

public class debtReportAdapter extends TableDataAdapter<debtReportModel> {
    List<debtReportModel> data;
    Context context;
    private int paddingLeft = 20;
    private int paddingTop = 15;
    private int paddingRight = 20;
    private int paddingBottom = 15;
    private int textSize = 18;
    private int typeface = Typeface.NORMAL;
    private int textColor = 0x99000000;
    private int gravity = Gravity.START;
    public debtReportAdapter(Context context, List<debtReportModel> data) {
        super(context, data);
        this.data = data;
        this.context = context;
    }

    @Override
    public View getCellView(int rowIndex, int columnIndex, ViewGroup parent) {
        debtReportModel report = data.get(rowIndex);
        TextView stt = new TextView(getContext());
        stt.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
        stt.setTypeface(stt.getTypeface(), typeface);
        stt.setTextSize(textSize);
        stt.setTextColor(textColor);
        stt.setSingleLine();
        stt.setEllipsize(TextUtils.TruncateAt.END);
        switch (columnIndex) {
            case 0:
                stt.setText(report.getStt() + "");
                break;
            case 1:
                stt.setText(report.getTenKhachHang() + "");
                break;
            case 2:
                stt.setText(report.getNgay() + "");
                break;
            case 3:
                stt.setText(report.getNoDau() + "");
                break;
            case 4:
                stt.setText(report.getPhatSinh() + "");
                break;
            case 5:
                stt.setText(report.getNoCuoi() + "");
                break;
        }

        return stt;
    }
}
