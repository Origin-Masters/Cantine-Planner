package de.htwsaar.cantineplanner.presentation;


import de.htwsaar.cantineplanner.codegen.tables.records.MealsRecord;


public class TUI {
    private ProgrammHelper programmHelper;

    public TUI() {
        programmHelper = new ProgrammHelper();
    }

    /**
     * Method to display the main menu
     *
     * @return Number of the selected option
     */
    public Number mainMenue() {
        System.out.println("----Main Menü----");
        System.out.println("1. Meal Menü");
        System.out.println("2. Review Menü");
        System.out.println("3. Beenden");
        return programmHelper.promptNumber("");
    }

    /**
     * Method to display the main menu
     *
     * @return Number of the selected option
     */
    public Number mealMenue() {
        System.out.println("----Meal Menü----");
        System.out.println("1. Alle Gerichte anzeigen");
        System.out.println("2. Gericht hinzufügen");
        System.out.println("3. Alle Allergien anzeigen");
        System.out.println("4. Gericht löschen");
        System.out.println("5. Gericht nach Name suchen");
        System.out.println("6. Gerichtdetails nach ID anzeigen");
        System.out.println("7. Main Menü");
        System.out.println("8. Beenden");
        return programmHelper.promptNumber("");
    }

    /**
     * Method to display the review menu
     *
     * @return Number of the selected option
     */

    public Number reviewMenue() {
        System.out.println("----Review Menü----");
        System.out.println("1. Alle Reviews anzeigen");
        System.out.println("2. Review hinzufügen");
        System.out.println("3. Review löschen");
        System.out.println("5. Review nach ID suchen");
        System.out.println("6. Alle Reviews eines Gerichts anzeigen");
        System.out.println("7. Main Menü");
        System.out.println("8. Beenden");
        return programmHelper.promptNumber("");
    }

    /**
     * Method to create a meal
     *
     * @return Meal record with the entered values
     */
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

    /**
     * Method to delete a meal
     *
     * @return MealId of the meal to be deleted
     */
    public int deleteMeal() {
        return (int) programmHelper.promptNumber("MealId des zu löschenden Gerichts eingeben:");
    }

    /**
     * Method to search for a meal by name
     *
     * @return Name of the meal to search for
     */
    public String searchMeal() {
        return programmHelper.promptString("Name des zu suchenden Gerichts eingeben:");
    }

    /**
     * Method to search for a meal by ID
     *
     * @return MealId of the meal to search for
     */
    public int searchMealById() {
        return (int) programmHelper.promptNumber("MealId des zu suchenden Gerichts eingeben:");
    }
}