package com.example.applayout.core.exercise;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Unit implements Serializable {
    private String unit;

    public Unit(){

    }
    public Unit( String unit) {
        this.unit = unit;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

}
