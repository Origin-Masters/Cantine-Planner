package de.htwsaar.cantineplanner.presentation;


import de.htwsaar.cantineplanner.codegen.tables.records.MealsRecord;


public class TUI {
    private ProgrammHelper programmHelper;

    public TUI() {
        programmHelper = new ProgrammHelper();
    }

    public Number testMenue() {
        System.out.println("----Test Menü----");
        System.out.println("1. Alle Gerichte anzeigen");
        System.out.println("2. Gericht hinzufügen");
        System.out.println("3. Alle Allergien anzeigen");
        System.out.println("4. Gericht löschen");
        System.out.println("5. Gericht nach Name suchen");
        System.out.println("6. Gerichtdetails nach ID anzeigen");
        System.out.println("7. Beenden");
        return programmHelper.promptNumber("");
    }

    public MealsRecord createMeal() {
        MealsRecord mealRecord = new MealsRecord();
        System.out.println("----Gericht erstellen----");
        mealRecord.setMealId((int) programmHelper.promptNumber("MealId"));
        mealRecord.setName(programmHelper.promptString("Name"));
        mealRecord.setAllergy(programmHelper.promptString("Allergie"));
        mealRecord.setPrice((float) programmHelper.promptNumber("Preis"));
        mealRecord.setCalories((int) programmHelper.promptNumber("Kalorien"));
        mealRecord.setMeat((int) programmHelper.promptNumber("Fleisch"));
        return mealRecord;
    }

    public int deleteMeal() {
        return (int) programmHelper.promptNumber("MealId des zu löschenden Gerichts eingeben:");
    }

    public String searchMeal() {
        return programmHelper.promptString("Name des zu suchenden Gerichts eingeben:");
    }

    public int searchMealById() {
        return (int) programmHelper.promptNumber("MealId des zu suchenden Gerichts eingeben:");
    }
}