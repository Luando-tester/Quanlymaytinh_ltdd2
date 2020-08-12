package com.example.quanlysuachuamaytinh;

public class PhieuThu {
    private String soPT;
    private String maKh;
    private String ngay;
    private String loaphieu;
    private int sotien;
    private boolean chon;

    public PhieuThu() {
    }

    public PhieuThu(String soPT, String maKh, String ngay, String loaphieu, int sotien) {
        this.soPT = soPT;
        this.maKh = maKh;
        this.ngay = ngay;
        this.loaphieu = loaphieu;
        this.sotien = sotien;
        this.chon = false;
    }

    public String getSoPT() {
        return soPT;
    }

    public String getMaKh() {
        return maKh;
    }

    public String getNgay() {
        return ngay;
    }

    public String getLoaphieu() {
        return loaphieu;
    }

    public int getSotien() {
        return sotien;
    }

    public boolean isChon() {
        return chon;
    }

    public void setSoPT(String soPT) {
        this.soPT = soPT;
    }

    public void setMaKh(String maKh) {
        this.maKh = maKh;
    }

    public void setNgay(String ngay) {
        this.ngay = ngay;
    }

    public void setLoaphieu(String loaphieu) {
        this.loaphieu = loaphieu;
    }

    public void setSotien(int sotien) {
        this.sotien = sotien;
    }

    public void setChon(boolean chon) {
        this.chon = chon;
    }
}
