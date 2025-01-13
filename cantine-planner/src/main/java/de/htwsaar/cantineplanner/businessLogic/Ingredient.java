package de.htwsaar.cantineplanner.businessLogic;

public class Ingredient {
    private String name;
    private int calories;
    private int ingredientId;
    private int carbs;
    private int protein;
    private int fat;

    public Ingredient(String name, int ingredientId, int carbs, int protein, int fat,int grams) {
        this.name = name;
        this.ingredientId = ingredientId;
        this.carbs = carbs;
        this.protein = protein;
        this.fat = fat;
        this.calories = carbs*4 + protein*4 + fat*9;
    }

    public String getName() {
        return name;
    }

    public int getCalories() {
        return calories;
    }

    public int getIngredientId() {
        return ingredientId;
    }

    public int getCarbs() {
        return carbs;
    }

    public int getProtein() {
        return protein;
    }

    public int getFat() {
        return fat;
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "name='" + name + '\'' +
                ", calories=" + calories +
                ", ingredientId=" + ingredientId +
                ", carbs=" + carbs +
                ", protein=" + protein +
                ", fat=" + fat +
                '}';
    }
}
