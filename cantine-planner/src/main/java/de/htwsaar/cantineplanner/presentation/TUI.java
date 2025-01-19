package de.htwsaar.cantineplanner.presentation;


import de.htwsaar.cantineplanner.codegen.tables.records.MealsRecord;


public class TUI {
    private ProgrammHelper programmHelper;
    public TUI() {
        programmHelper = new ProgrammHelper();
    }

    public Number test() {
        System.out.println("----Test Menü----");
        System.out.println("1. Alle Gerichte anzeigen");
        System.out.println("2. Gericht hinzufügen");
        return programmHelper.promptNumber("> ");
    }
    public MealsRecord createMeal() {
        System.out.println("----Gericht erstellen----");
        String name = programmHelper.promptString("Name");
        String allergy = programmHelper.promptString("Allergie");
        float price = (float) programmHelper.promptNumber("Preis");
        int mealId = (int) programmHelper.promptNumber("MealId");
        int calories = (int) programmHelper.promptNumber("Kalorien");
        int meat = (int) programmHelper.promptNumber("Fleisch");

        MealsRecord mealRecord = new MealsRecord();
        mealRecord.setName(name);
        mealRecord.setAllergy(allergy);
        mealRecord.setPrice(price);
        mealRecord.setMealId(mealId);
        mealRecord.setCalories(calories);
        mealRecord.setMeat(meat);

        return mealRecord;
    }
}