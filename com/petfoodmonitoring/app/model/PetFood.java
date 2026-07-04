package com.petfoodmonitoring.app.model;

import java.sql.Date;

public class PetFood {
    
   private int id;
    private String foodName;
    private String brand;
    private String foodType;
    private String flavor;
    private Date expirationDate;

    public PetFood() {
    }

    
    public PetFood(String foodName, String brand, String foodType,
            String flavor, Date expirationDate) {

        this.foodName = foodName;
        this.brand = brand;
        this.foodType = foodType;
        this.flavor = flavor;
        this.expirationDate = expirationDate;
    }

    public PetFood(int id, String foodName, String brand,
            String foodType, String flavor, Date expirationDate) {

        this.id = id;
        this.foodName = foodName;
        this.brand = brand;
        this.foodType = foodType;
        this.flavor = flavor;
        this.expirationDate = expirationDate;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getFoodName() { return foodName; }
    public void setFoodName(String foodName) { this.foodName = foodName; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getFoodType() { return foodType; }
    public void setFoodType(String foodType) { this.foodType = foodType; }

    public String getFlavor() { return flavor; }
    public void setFlavor(String flavor) { this.flavor = flavor; }

    public Date getExpirationDate() { return expirationDate; }
    public void setExpirationDate(Date expirationDate) { this.expirationDate = expirationDate; }
}

