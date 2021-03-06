package com.example.quanlynhasach.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class noteModel implements Serializable {
    String noteID;
    String date;
    ArrayList<bookModel> items;

    public noteModel() {
    }

    public noteModel(String noteID, String date, ArrayList<bookModel> items) {
        this.noteID = noteID;
        this.date = date;
        this.items = items;
    }

    public String getNoteID() {
        return noteID;
    }

    public void setNoteID(String noteID) {
        this.noteID = noteID;
    }

    public String getDate() {
        return date.split("_")[0];
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<bookModel> getItems() {
        return items;
    }

    public void setItems(ArrayList<bookModel> items) {
        this.items = items;
    }
}
