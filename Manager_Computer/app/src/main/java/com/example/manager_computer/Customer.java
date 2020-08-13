package com.example.manager_computer;

import com.orm.SugarRecord;

import java.util.Date;
import java.util.UUID;

public class Customer extends SugarRecord {
    private String code;
    private String name;
    private String address;
    private Date dateOfBirth;

    public String getCode() {
        return this.code;
    }

    public String getName() {
        return this.name;
    }

    public String getAddress() {
        return this.address;
    }

    public Date getDateOfBirth() {
        return this.dateOfBirth;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Customer() {}

    public Customer(String name, String address, Date dateOfBirth) {
        if (this.code == null || this.code == "") {
            generateCode();
        }
        this.name = name;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
    }

    public void generateCode() {
        this.code = UUID.randomUUID().toString().substring(0, 6);
    }

    @Override
    public String toString() {
        return this.name;
    }
}
