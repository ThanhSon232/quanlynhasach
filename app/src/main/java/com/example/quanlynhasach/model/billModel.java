package com.example.quanlynhasach.model;

import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.stream.Stream;

public class billModel  implements Serializable  {
    String id;
    String date;
    String customerName;
    String customerID;
    ArrayList<bookModel> items;

    public billModel() {
    }

    public billModel(String id, String date, String customerName, String customerID, ArrayList<bookModel> items) {
        this.id = id;
        this.date = date;
        this.customerName = customerName;
        this.customerID = customerID;
        this.items = items;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date.split("_")[0];
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public ArrayList<bookModel> getItems() {
        return items;
    }

    public void setItems(ArrayList<bookModel> items) {
        this.items = items;
    }

}
