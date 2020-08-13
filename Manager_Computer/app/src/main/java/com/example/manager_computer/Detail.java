package com.example.manager_computer;

import com.orm.SugarRecord;

import java.util.UUID;

public class Detail extends SugarRecord {
    private String code;
    private String billCode;
    private String serviceCode;
    private int quantity;

    public void setCode(String code) {
        this.code = code;
    }

    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getCode() {
        return this.code;
    }

    public String getBillCode() {
        return this.billCode;
    }

    public String getServiceCode() {
        return this.serviceCode;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public Detail() {}

    public Detail(String billCode, String serviceCode, int quantity) {
        if (this.code == null || this.code == "") {
            generateCode();
        }
        this.billCode = billCode;
        this.serviceCode = serviceCode;
        this.quantity = quantity;
    }

    public void generateCode() {
        this.code = UUID.randomUUID().toString().substring(0, 6);
    }
}
