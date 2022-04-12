package com.example.quanlynhasach.model;

import java.util.ArrayList;

public class billModel {
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
        return date;
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
