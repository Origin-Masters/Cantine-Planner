package de.htwsaar.cantineplanner.businessLogic;

import java.util.ArrayList;

public class Meal {

    private String name;
    private String allergy;
    private double price;
    private int mealId;
    private int calories;
    private int meat;

    private ArrayList<String> ingredients ;

    public Meal(String name, String allergy, double price, int mealId, int calories, int meat) {

        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("The meal name cannot be empty !");
        }
        if (price < 0) {
            throw new IllegalArgumentException("The meal price cannot be negative !");
        }
        if (mealId < 0) {
            throw new IllegalArgumentException("The MealId cannot be negative !");
        }

        if ( calories <= 0){
            throw new IllegalArgumentException("The meal calorie count cannot be zero or less than zero !");
        }

        this.name = name;
        this.allergy = allergy;
        this.price = price;
        this.mealId = mealId;
        this.calories = calories;
    }



    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAllergy() {
        return allergy;
    }

    public void setAllergy(String allergy) {
        this.allergy = allergy;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getMealId() {
        return mealId;
    }

    public void setMealId(int givenMealId) {
        this.mealId = givenMealId;
    }

}
