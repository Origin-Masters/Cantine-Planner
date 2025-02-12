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
                new MenuBuilder.MenuButton("Gericht hinzufügen", "showAddMeal"),
                new MenuBuilder.MenuButton("Alle Allergien anzeigen", "showAllAllergies"),
                new MenuBuilder.MenuButton("Gericht löschen", "showDeleteMeal"),
                new MenuBuilder.MenuButton("Gericht nach Name suchen", "showSearchMealByName"),
                new MenuBuilder.MenuButton("Gerichtdetails nach ID anzeigen", "showMealDetailsById"),
                new MenuBuilder.MenuButton("Main Menü", "showMainMenu"),
                new MenuBuilder.MenuButton("Programm beenden", "exit")
        );

        MenuBuilder mealMenu = new MenuBuilder(gui, eventManager)
                .setTitle("Meal Menu")
                .setButtons(mealMenuButtons);
        mealMenu.display();
    }

    public void showUserMenuScreen() {
        List<MenuBuilder.MenuButton> userMenuButtons = Arrays.asList(
                new MenuBuilder.MenuButton("Benutzerdaten bearbeiten", "editUserData"),
                new MenuBuilder.MenuButton("Allergien verwalten", "manageAllergies"),
                new MenuBuilder.MenuButton("Bewertungen anzeigen", "showReviewsByUser"),
                new MenuBuilder.MenuButton("Abmelden", "logout"),
                new MenuBuilder.MenuButton("Admin Menu", "showAdminMenu"),
                new MenuBuilder.MenuButton("Main Menü", "showMainMenu"),
                new MenuBuilder.MenuButton("Programm beenden", "exit")
        );

        MenuBuilder userMenu = new MenuBuilder(gui, eventManager)
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

    public void showAllReviews(List<ReviewRecord> reviews) {
        TableBuilder tableBuilder = new TableBuilder(gui, "All Reviews")
                .addColumn("ID")
                .addColumn("Rating")
                .addColumn("Comment")
                .addColumn("Meal ID")
                .addColumn("Date")
                .addColumn("UserID");
        for (ReviewRecord review : reviews) {
            tableBuilder.addRow(Arrays.asList(
                    String.valueOf(review.getRatingId()),
                    String.valueOf(review.getRating()),
                    review.getComment(),
                    String.valueOf(review.getMealId()),
                    review.getCreatedAt().toString(),
                    String.valueOf(review.getUserid())
            ));
        }
        tableBuilder.display();
    }

    public void showMainMenuScreen() {
        List<MenuBuilder.MenuButton> mainMenuButtons = Arrays.asList(
                new MenuBuilder.MenuButton("User Menu", "showUserMenu"),
                new MenuBuilder.MenuButton("Meal Menu", "showMealMenu"),
                new MenuBuilder.MenuButton("Review Menu", "showReviewMenu"),
                new MenuBuilder.MenuButton("Weekly Menu", "showWeeklyMenu"),
                new MenuBuilder.MenuButton("Exit", "exit")
        );
        MenuBuilder mainMenu = new MenuBuilder(gui, eventManager)
                .setTitle("Main Menu")
                .setButtons(mainMenuButtons);
        mainMenu.display();
    }


    public void showWeeklyMenuScreen(){
        List<MenuBuilder.MenuButton> weeklyMenuButtons = Arrays.asList(
                new MenuBuilder.MenuButton("Weekly Plan", "showWeeklyMenu"),
                new MenuBuilder.MenuButton("Edit Weekly Plan", "editWeeklyPlan"),
                new MenuBuilder.MenuButton("Show Random Meals", "showRandomMeals"),
                new MenuBuilder.MenuButton("Main Menu", "showMainMenu")
        );
        MenuBuilder weeklyMenu = new MenuBuilder(gui, eventManager)
                .setTitle("Weekly Menu")
                .setButtons(weeklyMenuButtons);
        weeklyMenu.display();
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

        MenuBuilder allergiesMenu = new MenuBuilder(gui, eventManager)
                .setTitle("Allergies Menu")
                .setButtons(allergiesMenuButtons);
        allergiesMenu.display();
    }

    public void showReviewsMenu() {
        List<MenuBuilder.MenuButton> reviewsMenuButtons = Arrays.asList(
                new MenuBuilder.MenuButton("Alle Reviews anzeigen", "showAllReviews"),
                new MenuBuilder.MenuButton("Review hinzufügen", "showAddReview"),
                new MenuBuilder.MenuButton("Review löschen", "showDeleteReview"),
                new MenuBuilder.MenuButton("Review von einem bestimmten Gericht", "showSearchReviewsByMealName"),
                new MenuBuilder.MenuButton("Reviewdetails nach ID anzeigen", "showReviewDetailsById"),
                new MenuBuilder.MenuButton("Main Menü", "showMainMenu"),
                new MenuBuilder.MenuButton("Programm beenden", "exit")
        );

        MenuBuilder reviewsMenu = new MenuBuilder(gui, eventManager)
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
            // Verwende Optional, um Null- oder Leereinträge abzufangen
            String allergenInfo = Optional.ofNullable(meal.getAllergy())
                    .filter(allergy -> !allergy.isEmpty())
                    .map(allergy -> Arrays.stream(allergy.split(""))
                            .map(AllergenMapper::getAllergenFullName)
                            .collect(Collectors.joining(" ")))
                    .orElse("keine Allergene");

            // Ermittele den Fleisch-Typ (MealType)
            String mealType = MealTypeMapper.getMealTypeName(meal.getMeat());

            // Füge eine Zeile mit den benötigten Informationen hinzu
            tableBuilder.addRow(Arrays.asList(
                    String.valueOf(meal.getMealId()),
                    meal.getName(),
                    String.format("%.2f", meal.getPrice()),
                    String.valueOf(meal.getCalories()),
                    allergenInfo,
                    mealType
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
                .filter(allergy -> !allergy.isEmpty()) // Prüfe auch auf leere Strings
                .map(allergy -> Arrays.stream(allergy.split(""))
                        .map(AllergenMapper::getAllergenFullName)
                        .collect(Collectors.joining(" ")))
                .orElse("keine Allergene");

        // Füge die Zeile mit allen benötigten Informationen hinzu
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
                    .filter(allergy -> !allergy.isEmpty())
                    .map(allergy -> Arrays.stream(allergy.split(""))
                            .map(AllergenMapper::getAllergenFullName)
                            .collect(Collectors.joining(" ")))
                    .orElse("keine Allergene");

            tableBuilder.addRow(Arrays.asList(
                    meal.getName(),
                    allergens
            ));
        }

        tableBuilder.display();
    }
    public void showAddReviewScreen() {
        InputScreenBuilder inputScreenBuilder = new InputScreenBuilder(gui, eventManager, "Add Review");
        List<String> labels = Arrays.asList("Rating", "Comment", "Meal ID");
        inputScreenBuilder.display(labels, "addReview");
    }
    public void showAddMealScreen() {
        InputScreenBuilder inputScreenBuilder = new InputScreenBuilder(gui, eventManager, "Add Meal");
        List<String> labels = Arrays.asList("Name", "Price", "Calories", "Allergy", "Meat");
        inputScreenBuilder.display(labels, "addMeal");
    }
    public void showDeleteMealScreen() {
        InputScreenBuilder inputScreenBuilder = new InputScreenBuilder(gui, eventManager, "Delete Meal");
        List<String> labels = Arrays.asList("ID");
        inputScreenBuilder.display(labels, "deleteMeal");
    }
    public void showDeleteReviewScreen() {
        InputScreenBuilder inputScreenBuilder = new InputScreenBuilder(gui, eventManager, "Delete Review");
        List<String> labels = Arrays.asList("ID");
        inputScreenBuilder.display(labels, "deleteReview");
    }
    public void showMealDetailsById() {
        InputScreenBuilder inputScreenBuilder = new InputScreenBuilder(gui, eventManager, "Show by ID Meal");
        List<String> labels = Arrays.asList("ID");
        inputScreenBuilder.display(labels, "mealDetailsById");
    }

    public void showSearchMealByName() {
        InputScreenBuilder inputScreenBuilder = new InputScreenBuilder(gui, eventManager, "Show by Name Meal");
        List<String> labels = Arrays.asList("Name");
        inputScreenBuilder.display(labels, "searchMealByName");
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

    public void showSearchReviewsByMealId() {
        InputScreenBuilder inputScreenBuilder = new InputScreenBuilder(gui, eventManager, "Search Reviews by Meal ID");
        List<String> labels = Arrays.asList("Meal ID");
        inputScreenBuilder.display(labels, "searchReviewsByMealId");
    }

    public void showSearchReviewsByMealName() {
        InputScreenBuilder inputScreenBuilder = new InputScreenBuilder(gui, eventManager, "Search Reviews by Meal Name");
        List<String> labels = Arrays.asList("Meal Name");
        inputScreenBuilder.display(labels, "searchReviewsByMealName");
    }

    public void showAdminMenuScreen() {
        List<MenuBuilder.MenuButton> adminMenuButtons = Arrays.asList(
                new MenuBuilder.MenuButton("View Users", "showUserManagement"),
                new MenuBuilder.MenuButton("Add User", "showAddUser"),
                new MenuBuilder.MenuButton("Delete User", "showDeleteUser"),
                new MenuBuilder.MenuButton("Update User Roles", "showUpdateUserRole"),
                new MenuBuilder.MenuButton("View Meals", "showMealManagement"),
                new MenuBuilder.MenuButton("Add Meal", "showAddMeal"),
                new MenuBuilder.MenuButton("Delete Meal", "showDeleteMeal"),
                new MenuBuilder.MenuButton("Edit Meal", "showEditMeal"),
                new MenuBuilder.MenuButton("View Reviews", "showReviewManagement"),
                new MenuBuilder.MenuButton("Delete Review", "showDeleteReview"),
                new MenuBuilder.MenuButton("View Popular Meals", "showPopularMeals"),
                new MenuBuilder.MenuButton("Main Menu", "showMainMenu"),
                new MenuBuilder.MenuButton("Exit", "exit")
        );

        MenuBuilder adminMenu = new MenuBuilder(gui, eventManager)
                .setTitle("Admin Menu")
                .setButtons(adminMenuButtons);
        adminMenu.display();
    }
}