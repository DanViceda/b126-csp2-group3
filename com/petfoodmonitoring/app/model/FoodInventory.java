package com.petfoodmonitoring.app.model;

import java.sql.Timestamp;

public class FoodInventory {
    
    private int id;
    private double quantityAvailable;
    private String unit;
    private Timestamp lastUpdated;
    private int foodId;

    public FoodInventory() {
    }

    public FoodInventory(double quantityAvailable, String unit,
                         Timestamp lastUpdated, int foodId) {

        this.quantityAvailable = quantityAvailable;
        this.unit = unit;
        this.lastUpdated = lastUpdated;
        this.foodId = foodId;
    }

    public FoodInventory(int id, double quantityAvailable, String unit,
                         Timestamp lastUpdated, int foodId) {

        this.id = id;
        this.quantityAvailable = quantityAvailable;
        this.unit = unit;
        this.lastUpdated = lastUpdated;
        this.foodId = foodId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getQuantityAvailable() {
        return quantityAvailable;
    }

    public void setQuantityAvailable(double quantityAvailable) {
        this.quantityAvailable = quantityAvailable;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Timestamp getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Timestamp lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }
}

