package com.petfoodmonitoring.app.model;

public class Pet {

    private int id;
    private String petName;
    private String species;
    private String breed;
    private int age;
    private double weight;
    private String gender;
    private int userId;

    public Pet() {
    }

    public Pet(String petName, String species, String breed,
            int age, double weight, String gender, int userId) {

        this.petName = petName;
        this.species = species;
        this.breed = breed;
        this.age = age;
        this.weight = weight;
        this.gender = gender;
        this.userId = userId;
    }

    public Pet(int id, String petName, String species, String breed,
            int age, double weight, String gender, int userId) {

        this.id = id;
        this.petName = petName;
        this.species = species;
        this.breed = breed;
        this.age = age;
        this.weight = weight;
        this.gender = gender;
        this.userId = userId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getPetName() { return petName; }
    public void setPetName(String petName) { this.petName = petName; }

    public String getSpecies() { return species; }
    public void setSpecies(String species) { this.species = species; }

    public String getBreed() { return breed; }
    public void setBreed(String breed) { this.breed = breed; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
}

