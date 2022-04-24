package com.example.quanlynhasach.model;

public class debtReportModel {
    Integer stt;
    String id;
    String tenKhachHang;
    String ngay;
    Integer noDau;
    Integer phatSinh;
    Integer noCuoi;

    public debtReportModel(Integer stt, String id, String tenKhachHang, String ngay, Integer noDau, Integer phatSinh, Integer noCuoi) {
        this.stt = stt;
        this.id = id;
        this.tenKhachHang = tenKhachHang;
        this.ngay = ngay;
        this.noDau = noDau;
        this.phatSinh = phatSinh;
        this.noCuoi = noCuoi;
    }

    public void setStt(Integer stt) {
        this.stt = stt;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTenKhachHang(String tenKhachHang) {
        this.tenKhachHang = tenKhachHang;
    }

    public void setNgay(String ngay) {
        this.ngay = ngay;
    }

    public void setNoDau(Integer noDau) {
        this.noDau = noDau;
    }

    public void setPhatSinh(Integer phatSinh) {
        this.phatSinh = phatSinh;
    }

    public void setNoCuoi(Integer noCuoi) {
        this.noCuoi = noCuoi;
    }

    public Integer getStt() {
        return stt;
    }

    public String getId() {
        return id;
    }

    public String getTenKhachHang() {
        return tenKhachHang;
    }

    public String getNgay() {
        return ngay;
    }

    public Integer getNoDau() {
        return noDau;
    }

    public Integer getPhatSinh() {
        return phatSinh;
    }

    public Integer getNoCuoi() {
        return noCuoi;
    }
}
