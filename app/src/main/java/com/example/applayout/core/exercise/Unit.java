package com.example.applayout.core.exercise;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Unit implements Serializable {
    private String unit;
    private String img;
    public Unit(){

    }

    public Unit(String unit, String img) {
        this.unit = unit;
        this.img = img;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
