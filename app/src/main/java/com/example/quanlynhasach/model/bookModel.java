package com.example.quanlynhasach.model;

public class bookModel {
    String maSach;
    String tenSach;
    String theLoai;
    String tacGia;
    Integer donGia;
    Integer soLuongConLai;
    String hinhAnh;

    public bookModel() {
    }

    public bookModel(String maSach, String tenSach, String theLoai, String tacGia, Integer donGia, Integer soLuongConLai, String hinhAnh) {
        this.maSach = maSach;
        this.tenSach = tenSach;
        this.theLoai = theLoai;
        this.tacGia = tacGia;
        this.donGia = donGia;
        this.soLuongConLai = soLuongConLai;
        this.hinhAnh = hinhAnh;
    }

    public String getMaSach() {
        return maSach;
    }

    public void setMaSach(String maSach) {
        this.maSach = maSach;
    }

    public String getTenSach() {
        return tenSach;
    }

    public void setTenSach(String tenSach) {
        this.tenSach = tenSach;
    }

    public String getTheLoai() {
        return theLoai;
    }

    public void setTheLoai(String theLoai) {
        this.theLoai = theLoai;
    }

    public String getTacGia() {
        return tacGia;
    }

    public void setTacGia(String tacGia) {
        this.tacGia = tacGia;
    }

    public Integer getDonGia() {
        return donGia;
    }

    public void setDonGia(Integer donGia) {
        this.donGia = donGia;
    }

    public Integer getSoLuongConLai() {
        return soLuongConLai;
    }

    public void setSoLuongConLai(Integer soLuongConLai) {
        this.soLuongConLai = soLuongConLai;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }
}
