package com.petfoodmonitoring.app.model;

public class FeedingSchedule {

    private int id;
    private String feedingTime;
    private double quantity;
    private String frequency;
    private int petId;
    private int foodId;

    public FeedingSchedule() {
    }

    public FeedingSchedule(String feedingTime, double quantity, String frequency,
                           int petId, int foodId) {

        this.feedingTime = feedingTime;
        this.quantity = quantity;
        this.frequency = frequency;
        this.petId = petId;
        this.foodId = foodId;
    }

    public FeedingSchedule(int id, String feedingTime, double quantity,
                           String frequency, int petId, int foodId) {

        this.id = id;
        this.feedingTime = feedingTime;
        this.quantity = quantity;
        this.frequency = frequency;
        this.petId = petId;
        this.foodId = foodId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFeedingTime() {
        return feedingTime;
    }

    public void setFeedingTime(String feedingTime) {
        this.feedingTime = feedingTime;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public int getPetId() {
        return petId;
    }

    public void setPetId(int petId) {
        this.petId = petId;
    }

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }
}

