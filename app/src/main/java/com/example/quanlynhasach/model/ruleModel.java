package com.example.quanlynhasach.model;

public class ruleModel {
    static Integer LuongNhapToiThieu = 150;
    static Integer LuongTonToiThieuNhap = 300 ;
    static Integer TienNoToiDa = 20000;
    static Integer LuongTonToiThieuBan = 20;
    static boolean switch1 = false;
    static boolean switch2 = false;


    public ruleModel() {
    }

    public static Integer getLuongNhapToiThieu() {
        return LuongNhapToiThieu;
    }

    public static void setLuongNhapToiThieu(Integer luongNhapToiThieu) {
        LuongNhapToiThieu = luongNhapToiThieu;
    }

    public static Integer getLuongTonToiThieuNhap() {
        return LuongTonToiThieuNhap;
    }

    public static void setLuongTonToiThieuNhap(Integer luongTonToiThieuNhap) {
        LuongTonToiThieuNhap = luongTonToiThieuNhap;
    }

    public static Integer getTienNoToiDa() {
        return TienNoToiDa;
    }

    public static void setTienNoToiDa(Integer tienNoToiDa) {
        TienNoToiDa = tienNoToiDa;
    }

    public static Integer getLuongTonToiThieuBan() {
        return LuongTonToiThieuBan;
    }

    public static void setLuongTonToiThieuBan(Integer luongTonToiThieuBan) {
        LuongTonToiThieuBan = luongTonToiThieuBan;
    }

    public static boolean isSwitch1() {
        return switch1;
    }

    public static void setSwitch1(boolean switch1) {
        ruleModel.switch1 = switch1;
    }

    public static boolean isSwitch2() {
        return switch2;
    }

    public static void setSwitch2(boolean switch2) {
        ruleModel.switch2 = switch2;
    }
}
