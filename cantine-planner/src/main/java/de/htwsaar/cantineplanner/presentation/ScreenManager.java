package de.htwsaar.cantineplanner.presentation;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import de.htwsaar.cantineplanner.businessLogic.AllergenMapper;
import de.htwsaar.cantineplanner.businessLogic.EventManager;
import de.htwsaar.cantineplanner.businessLogic.MealTypeMapper;
import de.htwsaar.cantineplanner.codegen.tables.records.MealsRecord;
import de.htwsaar.cantineplanner.codegen.tables.records.ReviewRecord;
import de.htwsaar.cantineplanner.presentation.pages.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ScreenManager {
    private MultiWindowTextGUI gui;
    private EventManager eventManager;

    public ScreenManager(EventManager eventManager) {
        this.eventManager = eventManager;
        try {
            Screen screen = new DefaultTerminalFactory().createScreen();
            screen.startScreen();
            this.gui = new MultiWindowTextGUI(screen);
        } catch (IOException e) {
            eventManager.notify("error", "Error starting terminal");
        }
    }

    public void showLoginScreen() {
        LoginScreen loginScreen = new LoginScreen(gui, eventManager);
        loginScreen.display();
    }

    public void showMealMenuScreen() {
        List<MenuBuilder.MenuButton> mealMenuButtons = Arrays.asList(
                new MenuBuilder.MenuButton("Alle Gerichte anzeigen", "showAllMeals"),
                new MenuBuilder.MenuButton("Gericht hinzufügen", "addMeal"),
                new MenuBuilder.MenuButton("Alle Allergien anzeigen", "showAllAllergies"),
                new MenuBuilder.MenuButton("Gericht löschen", "deleteMeal"),
                new MenuBuilder.MenuButton("Gericht nach Name suchen", "searchMealByName"),
                new MenuBuilder.MenuButton("Gerichtdetails nach ID anzeigen", "showMealDetailsById"),
                new MenuBuilder.MenuButton("Main Menü", "showMainMenu"),
                new MenuBuilder.MenuButton("Programm beenden", "exit")
        );

        MenuBuilder mealMenu = new MenuBuilder(gui, eventManager, this)
                .setTitle("Meal Menu")
                .setButtons(mealMenuButtons);
        mealMenu.display();
    }

    public void showUserMenuScreen() {
        List<MenuBuilder.MenuButton> userMenuButtons = Arrays.asList(
                new MenuBuilder.MenuButton("Benutzerdaten bearbeiten", "editUserData"),
                new MenuBuilder.MenuButton("Allergien verwalten", "manageAllergies"),
                new MenuBuilder.MenuButton("Bewertungen anzeigen", "showReviews"),
                new MenuBuilder.MenuButton("Abmelden", "logout"),
                new MenuBuilder.MenuButton("Main Menü", "showMainMenu"),
                new MenuBuilder.MenuButton("Programm beenden", "exit")
        );

        MenuBuilder userMenu = new MenuBuilder(gui, eventManager, this)
                .setTitle("User Menu")
                .setButtons(userMenuButtons);
        userMenu.display();
    }

    public void showErrorScreen(String message) {
        NotificationScreenBuilder errorScreen = new NotificationScreenBuilder(gui, message,
                new TextColor.RGB(255, 0, 0));
        errorScreen.display();
    }

    public void showSuccessScreen(String message) {
        NotificationScreenBuilder successScreen = new NotificationScreenBuilder(gui, message,
                new TextColor.RGB(0, 255, 0));
        successScreen.display();
    }

    public void getAllReviews(List<ReviewRecord> reviews) {
        TableBuilder tableBuilder = new TableBuilder(gui, "All Reviews")
                // add columns to the table
                .addColumn("ID")
                .addColumn("Rating")
                .addColumn("Comment")
                .addColumn("Meal ID")
                .addColumn("User ID")
                .addColumn("Date");

        //TODO: Add all reviews to the table

        //fetch all rows from the db
        for (ReviewRecord review : reviews) {
            tableBuilder.addRow(Arrays.asList(
                    String.valueOf(review.getRatingId()),
                    String.valueOf(review.getRating()),
                    review.getComment(),
                    String.valueOf(review.getMealId()),
                    review.getCreatedAt().toString()
            ));
        }
        // display the table
        tableBuilder.display();
    }

    public void showMainMenuScreen() {
        List<MenuBuilder.MenuButton> mainMenuButtons = Arrays.asList(
                new MenuBuilder.MenuButton("User Menu", "showUserMenu"),
                new MenuBuilder.MenuButton("Meal Menu", "showMealMenu"),
                new MenuBuilder.MenuButton("Review Menu", "showReviewMenu"),
                new MenuBuilder.MenuButton("Exit", "exit")
        );
        MenuBuilder mainMenu = new MenuBuilder(gui, eventManager, this)
                .setTitle("Main Menu")
                .setButtons(mainMenuButtons);
        mainMenu.display();
    }

    public void showAllergiesMenu() {
        List<MenuBuilder.MenuButton> allergiesMenuButtons = Arrays.asList(
                new MenuBuilder.MenuButton("Alle Allergien anzeigen", "showAllAllergies"),
                new MenuBuilder.MenuButton("Allergie hinzufügen", "addAllergy"),
                new MenuBuilder.MenuButton("Allergie löschen", "deleteAllergy"),
                new MenuBuilder.MenuButton("Allergie nach Name suchen", "searchAllergyByName"),
                new MenuBuilder.MenuButton("Allergiedetails nach ID anzeigen", "showAllergyDetailsById"),
                new MenuBuilder.MenuButton("Main Menü", "showMainMenu"),
                new MenuBuilder.MenuButton("Programm beenden", "exit")
        );

        MenuBuilder allergiesMenu = new MenuBuilder(gui, eventManager, this)
                .setTitle("Allergies Menu")
                .setButtons(allergiesMenuButtons);
        allergiesMenu.display();
    }

    public void showReviewsMenu() {
        List<MenuBuilder.MenuButton> reviewsMenuButtons = Arrays.asList(
                new MenuBuilder.MenuButton("Alle Reviews anzeigen", "showAllReviews"),
                new MenuBuilder.MenuButton("Review hinzufügen", "addReview"),
                new MenuBuilder.MenuButton("Review löschen", "deleteReview"),
                new MenuBuilder.MenuButton("Review nach ID suchen", "searchReviewById"),
                new MenuBuilder.MenuButton("Reviewdetails nach ID anzeigen", "showReviewDetailsById"),
                new MenuBuilder.MenuButton("Main Menü", "showMainMenu"),
                new MenuBuilder.MenuButton("Programm beenden", "exit")
        );

        MenuBuilder reviewsMenu = new MenuBuilder(gui, eventManager, this)
                .setTitle("Reviews Menu")
                .setButtons(reviewsMenuButtons);
        reviewsMenu.display();
    }

    public void showAllMeals(List<MealsRecord> meals) {
        TableBuilder tableBuilder = new TableBuilder(gui, "All Meals")
                .addColumn("ID")
                .addColumn("Name")
                .addColumn("Price")
                .addColumn("Calories")
                .addColumn("Allergens")
                .addColumn("Meat");

        for (MealsRecord meal : meals) {
            String allergens = Optional.ofNullable(meal.getAllergy())
                    .map(allergyField -> Arrays.stream(allergyField.split(""))
                            .map(AllergenMapper::getAllergenFullName)
                            .collect(Collectors.joining(" ")))
                    .orElse("None");
            tableBuilder.addRow(Arrays.asList(
                    String.valueOf(meal.getMealId()),
                    meal.getName(),
                    String.format("%.2f", meal.getPrice()),
                    String.valueOf(meal.getCalories()),
                    allergens,
                    MealTypeMapper.getMealTypeName(meal.getMeat())
            ));
        }

        tableBuilder.display();
    }

    public void showMealDetails(MealsRecord meal) {
        TableBuilder tableBuilder = new TableBuilder(gui, "Meal Details")
                .addColumn("ID")
                .addColumn("Name")
                .addColumn("Price")
                .addColumn("Calories")
                .addColumn("Allergens")
                .addColumn("Meat");

        String allergens = Optional.ofNullable(meal.getAllergy())
                .map(allergyField -> Arrays.stream(allergyField.split(""))
                        .map(AllergenMapper::getAllergenFullName)
                        .collect(Collectors.joining(" ")))
                .orElse("None");

        tableBuilder.addRow(Arrays.asList(
                String.valueOf(meal.getMealId()),
                meal.getName(),
                String.format("%.2f", meal.getPrice()),
                String.valueOf(meal.getCalories()),
                allergens,
                MealTypeMapper.getMealTypeName(meal.getMeat())
        ));

        tableBuilder.display();
    }

    public void showInputScreenReg(String title, String event) {
        InputScreenBuilder inputScreenBuilder = new InputScreenBuilder(gui, eventManager, title);
        List<String> labels = Arrays.asList("Username", "Password", "Email");
        inputScreenBuilder.display(labels, event);
    }

    public void showAllAllergies(List<MealsRecord> meals) {
        TableBuilder tableBuilder = new TableBuilder(gui, "All Allergies")
                .addColumn("Meal Name")
                .addColumn("Allergy");

        for (MealsRecord meal : meals) {
            String allergens = Optional.ofNullable(meal.getAllergy())
                    .map(allergyField -> Arrays.stream(allergyField.split(""))
                            .map(AllergenMapper::getAllergenFullName)
                            .collect(Collectors.joining(" ")))
                    .orElse("None");
            tableBuilder.addRow(Arrays.asList(
                    meal.getName(),
                    allergens
            ));
        }

        tableBuilder.display();
    }

    public void showAddMealScreen() {
        InputScreenBuilder inputScreenBuilder = new InputScreenBuilder(gui, eventManager, "Add Meal");
        List<String> labels = Arrays.asList("Name", "Price", "Calories", "Allergy", "Meat");
        inputScreenBuilder.display(labels, "addMeal");
    }



    public void closeActiveWindow() {
        if (gui.getActiveWindow() != null) {
            gui.getActiveWindow().close();
        }
    }

    public void showInputScreen(String title, String event, List<String> labels) {
        InputScreenBuilder inputScreenBuilder = new InputScreenBuilder(gui, eventManager, title);
        inputScreenBuilder.display(labels, event);
    }

    public void closeTerminal() {
        try {
            gui.getScreen().stopScreen();
        } catch (IOException e) {
            eventManager.notify("error", "Error closing terminal");

        }
    }
}