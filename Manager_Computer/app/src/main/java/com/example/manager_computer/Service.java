package com.example.manager_computer;

import com.orm.SugarRecord;

import java.util.UUID;

public class Service extends SugarRecord {
    private String code;
    private String name;
    private String unit;
    private String urlImage;
    private Double price;

    public String getCode() {
        return this.code;
    }

    public String getName() {
        return this.name;
    }

    public String getUrlImage() {
        return this.urlImage;
    }

    public String getUnit() {
        return this.unit;
    }

    public Double getPrice() {
        return this.price;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public void generateCode() {
        this.code = UUID.randomUUID().toString().substring(0, 6);
    }

    public Service() {}

    public Service(String name, String unit, Double price, String urlImage) {
        if (this.code == null || this.code == "") {
            generateCode();
        }
        this.name = name;
        this.unit = unit;
        this.price = price;
        this.urlImage = urlImage;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
