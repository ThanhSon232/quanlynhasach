package com.example.quanlynhasach.model;

public class reportBookModel {
    Integer stt;
    String tenSach;
    String ngay;
    Integer tonDau;
    Integer phatSinh;
    Integer tonCuoi;

    public reportBookModel() {
    }

    public reportBookModel(Integer stt, String tenSach, String ngay, Integer tonDau, Integer phatSinh, Integer tonCuoi) {
        this.stt = stt;
        this.tenSach = tenSach;
        this.ngay = ngay;
        this.tonDau = tonDau;
        this.phatSinh = phatSinh;
        this.tonCuoi = tonCuoi;
    }

    public Integer getStt() {
        return stt;
    }

    public void setStt(Integer stt) {
        this.stt = stt;
    }

    public String getTenSach() {
        return tenSach;
    }

    public void setTenSach(String tenSach) {
        this.tenSach = tenSach;
    }

    public String getNgay() {
        return ngay;
    }

    public void setNgay(String ngay) {
        this.ngay = ngay;
    }

    public Integer getTonDau() {
        return tonDau;
    }

    public void setTonDau(Integer tonDau) {
        this.tonDau = tonDau;
    }

    public Integer getPhatSinh() {
        return phatSinh;
    }

    public void setPhatSinh(Integer phatSinh) {
        this.phatSinh = phatSinh;
    }

    public Integer getTonCuoi() {
        return tonCuoi;
    }

    public void setTonCuoi(Integer tonCuoi) {
        this.tonCuoi = tonCuoi;
    }
}
