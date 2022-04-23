package com.example.quanlynhasach.model;

public class receiptModel {
    String maPhieuThu;
    String customerID;
    String ngayLap;
    Integer soTien;

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }


    public receiptModel(String maPhieuThu, String ngayLap, Integer soTien) {
        this.maPhieuThu = maPhieuThu;
        this.ngayLap = ngayLap;
        this.soTien = soTien;
    }

    public receiptModel() {
    }

    public String getMaPhieuThu() {
        return maPhieuThu;
    }

    public void setMaPhieuThu(String maPhieuThu) {
        this.maPhieuThu = maPhieuThu;
    }

    public String getNgayLap() {
        return ngayLap;
    }

    public void setNgayLap(String ngayLap) {
        this.ngayLap = ngayLap;
    }

    public Integer getSoTien() {
        return soTien;
    }

    public void setSoTien(Integer soTien) {
        this.soTien = soTien;
    }
}
