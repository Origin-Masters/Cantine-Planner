package de.htwsaar.cantineplanner.presentation;


import de.htwsaar.cantineplanner.codegen.tables.records.MealsRecord;
import de.htwsaar.cantineplanner.codegen.tables.records.ReviewRecord;


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
     * Method to display the user menu
     *
     * @return Number of the selected option
     */
    public Number userMenue() {
        System.out.println("----User Menü----");
        System.out.println("1. Login");
        System.out.println("2. Register");
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
        System.out.println("4. Review nach Meal iD suchen");
        System.out.println("5. Alle Reviews eines Gerichts anzeigen");
        System.out.println("6. Main Menü");
        System.out.println("7. Beenden");
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
        mealRecord.setName(programmHelper.promptString("Name"));
        mealRecord.setAllergy(programmHelper.promptString("Allergie"));
        mealRecord.setPrice((float) programmHelper.promptNumber("Preis"));
        mealRecord.setCalories((int) programmHelper.promptNumber("Kalorien"));
        mealRecord.setMeat((int) programmHelper.promptNumber("Fleisch"));
        return mealRecord;
    }

    /**
     * Method to create a review
     */
    public ReviewRecord createReview() {
        ReviewRecord reviewRecord = new ReviewRecord();
        System.out.println("----Review erstellen----");

        reviewRecord.setMealId(programmHelper.promptNumber("MealId"));
        reviewRecord.setRating(programmHelper.promptNumber("Bewertung"));
        reviewRecord.setComment(programmHelper.promptString("Kommentar"));

        return reviewRecord;
    }

    /**
     * Method to delete a meal
     *
     * @return MealId of the meal to be deleted
     */
    public int deleteMeal() {
        return (int) programmHelper.promptNumber("MealId des zu löschenden Gerichts eingeben:");
    }


    public int searchReview() {
        return programmHelper.promptNumber("ID des zu suchenden Review eingeben:");
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