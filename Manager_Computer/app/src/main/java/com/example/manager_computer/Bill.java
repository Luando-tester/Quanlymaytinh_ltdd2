package com.example.manager_computer;

import com.orm.SugarRecord;

import java.util.Date;
import java.util.UUID;

public class Bill extends SugarRecord {
    private String code;
    private Date createdDate;
    private String customerCode;

    public String getCode() {
        return this.code;
    }

    public String getCustomerCode() {
        return this.customerCode;
    }

    public Date getCreateDate() {
        return this.createdDate;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public Bill() {}

    public Bill(boolean isCreate, String customerCode) {
        if (isCreate) {
            if (this.code == null || this.code == "") {
                generateCode();
            }
            if (this.createdDate == null) {
                generateCreatedDate();
            }
        }
        this.customerCode = customerCode;
    }

    public void generateCode() {
        this.code = UUID.randomUUID().toString().substring(0, 6);
    }

    public static String generateRandomCode() {
        return UUID.randomUUID().toString().substring(0, 6);
    }

    public void generateCreatedDate() {
        this.createdDate = new Date();
    }
}
