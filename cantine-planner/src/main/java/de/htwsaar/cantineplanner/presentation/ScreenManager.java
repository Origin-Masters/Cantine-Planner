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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

import java.util.stream.Collectors;

public class ScreenManager {
    private static final Logger log = LoggerFactory.getLogger(ScreenManager.class);
    private MultiWindowTextGUI gui;
    private final EventManager eventManager;

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
                new MenuBuilder.MenuButton("All Meal", "showAllMeals"),
                new MenuBuilder.MenuButton("Add Meal", "showAddMeal"),
                new MenuBuilder.MenuButton("Allergies in Meals", "showAllAllergies"),
                new MenuBuilder.MenuButton("Delete Meal", "showDeleteMeal"),
                new MenuBuilder.MenuButton("Search Meal by Name", "showSearchMealByName"),
                new MenuBuilder.MenuButton("Search Meal by ID", "showMealDetailsById"),
                new MenuBuilder.MenuButton("Main Menu", "showMainMenu"), new MenuBuilder.MenuButton("Exit", "exit"));

        MenuBuilder mealMenu = new MenuBuilder(gui, eventManager).setTitle("Meal Menu").setButtons(mealMenuButtons);
        mealMenu.display();
    }

    public void showUserMenuScreen() {
        List<MenuBuilder.MenuButton> userMenuButtons = Arrays.asList(
                new MenuBuilder.MenuButton("Edit My Data", "showEditUserData"),
                new MenuBuilder.MenuButton("Set My Allergy", "showAllergenSettings"),
                new MenuBuilder.MenuButton("My Reviews", "showReviewsByUser"),
                new MenuBuilder.MenuButton("Logout", "logout"),
                new MenuBuilder.MenuButton("Admin Menu", "showAdminMenu"),
                new MenuBuilder.MenuButton("Main Menu", "showMainMenu"), new MenuBuilder.MenuButton("Exit", "exit"));

        MenuBuilder userMenu = new MenuBuilder(gui, eventManager).setTitle("User Menu").setButtons(userMenuButtons);
        userMenu.display();
    }

    public void showEditUserDataScreen() {
        InputScreenBuilder inputScreenBuilder = new InputScreenBuilder(gui, eventManager, "Edit User Data");
        List<String> labels = Arrays.asList("Current Password");
        inputScreenBuilder.display(labels, "editUserData");
    }

    public void showEditNewUserDataScreen() {
        InputScreenBuilder inputScreenBuilder = new InputScreenBuilder(gui, eventManager, "Edit New User Data");
        List<String> labels = Arrays.asList("New Password", "New Email");
        inputScreenBuilder.display(labels, "editNewUserData");
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
        TableBuilder tableBuilder = new TableBuilder(gui, "All Reviews").addColumn("ID").addColumn("Rating").addColumn(
                "Comment").addColumn("Meal ID").addColumn("Date").addColumn("UserID");
        for (ReviewRecord review : reviews) {
            tableBuilder.addRow(Arrays.asList(String.valueOf(review.getRatingId()), String.valueOf(review.getRating()),
                    review.getComment(), String.valueOf(review.getMealId()), review.getCreatedAt().toString(),
                    String.valueOf(review.getUserid())));
        }
        tableBuilder.display();
    }

    public void showMainMenuScreen() {
        List<MenuBuilder.MenuButton> mainMenuButtons = Arrays.asList(
                new MenuBuilder.MenuButton("User Menu", "showUserMenu"),
                new MenuBuilder.MenuButton("Meal Menu", "showMealMenu"),
                new MenuBuilder.MenuButton("Review Menu", "showReviewMenu"),
                new MenuBuilder.MenuButton("Weekly Menu", "showWeeklyMenu"),
                new MenuBuilder.MenuButton("Exit", "exit"));
        MenuBuilder mainMenu = new MenuBuilder(gui, eventManager).setTitle("Main Menu").setButtons(mainMenuButtons);
        mainMenu.display();
    }


    public void showWeeklyMenuScreen() {
        List<MenuBuilder.MenuButton> weeklyMenuButtons = Arrays.asList(
                new MenuBuilder.MenuButton("Weekly Plan", "showWeeklyPlan"),
                new MenuBuilder.MenuButton("Edit Weekly Plan", "editWeeklyPlan"),
                new MenuBuilder.MenuButton("Main Menu", "showMainMenu"),
                new MenuBuilder.MenuButton("Programm beenden", "exit"));
        MenuBuilder weeklyMenu = new MenuBuilder(gui, eventManager).setTitle("Weekly Menu").setButtons(
                weeklyMenuButtons);
        weeklyMenu.display();
    }

    public void showAllergiesMenu() {
        List<MenuBuilder.MenuButton> allergiesMenuButtons = Arrays.asList(
                new MenuBuilder.MenuButton("All Allergies", "showAllAllergies"),
                new MenuBuilder.MenuButton("Add Allergy", "addAllergy"),
                new MenuBuilder.MenuButton("Delete Allergy", "deleteAllergy"),
                new MenuBuilder.MenuButton("Search Allergy by Meal Name", "searchAllergyByName"),
                new MenuBuilder.MenuButton("Search Allergy by Meal ID", "showAllergyDetailsById"),
                new MenuBuilder.MenuButton("Main Menu", "showMainMenu"), new MenuBuilder.MenuButton("Exit", "exit"));

        MenuBuilder allergiesMenu = new MenuBuilder(gui, eventManager).setTitle("Allergies Menu").setButtons(
                allergiesMenuButtons);
        allergiesMenu.display();
    }

    public void showReviewsMenu() {
        List<MenuBuilder.MenuButton> reviewsMenuButtons = Arrays.asList(
                new MenuBuilder.MenuButton("All Reviews", "showAllReviews"),
                new MenuBuilder.MenuButton("Add Reviews", "showAddReview"),
                new MenuBuilder.MenuButton("Delete Review", "showDeleteReview"),
                new MenuBuilder.MenuButton("Search Reviews", "showSearchReviewsByMealName"),
                new MenuBuilder.MenuButton("Main Menu", "showMainMenu"), new MenuBuilder.MenuButton("Exit", "exit"));

        MenuBuilder reviewsMenu = new MenuBuilder(gui, eventManager).setTitle("Reviews Menu").setButtons(
                reviewsMenuButtons);
        reviewsMenu.display();
    }

    public void showAllMeals(List<MealsRecord> meals) {
        TableBuilder tableBuilder = new TableBuilder(gui, "All Meals").addColumn("ID").addColumn("Name").addColumn(
                "Price").addColumn("Calories").addColumn("Allergens").addColumn("Meat");

        for (MealsRecord meal : meals) {
            // Verwende Optional, um Null- oder Leereinträge abzufangen
            String allergenInfo = Optional.ofNullable(meal.getAllergy()).filter(allergy -> !allergy.isEmpty()).map(
                    allergy -> Arrays.stream(allergy.split("")).map(AllergenMapper::getAllergenFullName).collect(
                            Collectors.joining(" "))).orElse("No Allergies");

            // Ermittele den Fleisch-Typ (MealType)
            String mealType = MealTypeMapper.getMealTypeName(meal.getMeat());

            // Füge eine Zeile mit den benötigten Informationen hinzu
            tableBuilder.addRow(Arrays.asList(String.valueOf(meal.getMealId()), meal.getName(),
                    String.format("%.2f", meal.getPrice()), String.valueOf(meal.getCalories()), allergenInfo,
                    mealType));
        }


        tableBuilder.display();
    }

    public void showMealDetails(MealsRecord meal) {
        TableBuilder tableBuilder = new TableBuilder(gui, "Meal Details").addColumn("ID").addColumn("Name").addColumn(
                "Price").addColumn("Calories").addColumn("Allergens").addColumn("Meat");

        String allergens = Optional.ofNullable(meal.getAllergy()).filter(
                        allergy -> !allergy.isEmpty()) // Prüfe auch auf leere Strings
                .map(allergy -> Arrays.stream(allergy.split("")).map(AllergenMapper::getAllergenFullName).collect(
                        Collectors.joining(" "))).orElse("No Allergies");

        // Füge die Zeile mit allen benötigten Informationen hinzu
        tableBuilder.addRow(
                Arrays.asList(String.valueOf(meal.getMealId()), meal.getName(), String.format("%.2f", meal.getPrice()),
                        String.valueOf(meal.getCalories()), allergens, MealTypeMapper.getMealTypeName(meal.getMeat())));

        tableBuilder.display();
    }

    public void showInputScreenReg(String title, String event) {
        InputScreenBuilder inputScreenBuilder = new InputScreenBuilder(gui, eventManager, title);
        List<String> labels = Arrays.asList("Username", "Password", "Email");
        inputScreenBuilder.display(labels, event);
    }

    public void showAllAllergies(List<MealsRecord> meals) {
        TableBuilder tableBuilder = new TableBuilder(gui, "Allergies in Meals").addColumn("Meal Name").addColumn(
                "Allergy");

        for (MealsRecord meal : meals) {
            String allergens = Optional.ofNullable(meal.getAllergy()).filter(allergy -> !allergy.isEmpty()).map(
                    allergy -> Arrays.stream(allergy.split("")).map(AllergenMapper::getAllergenFullName).collect(
                            Collectors.joining(" "))).orElse("No Allergies");

            tableBuilder.addRow(Arrays.asList(meal.getName(), allergens));
        }

        tableBuilder.display();
    }

    public void showAddReviewScreen() {
        InputScreenBuilder inputScreenBuilder = new InputScreenBuilder(gui, eventManager, "Add New Review");
        List<String> labels = Arrays.asList("Rating", "Comment", "Meal ID");
        inputScreenBuilder.display(labels, "addReview");
    }

    public void showAddMealScreen() {
        InputScreenBuilder inputScreenBuilder = new InputScreenBuilder(gui, eventManager, "Add New Meal");
        List<String> labels = Arrays.asList("Name", "Price", "Calories", "Allergy", "Meat");
        inputScreenBuilder.display(labels, "addMeal");
    }

    public void showDeleteMealScreen() {
        InputScreenBuilder inputScreenBuilder = new InputScreenBuilder(gui, eventManager, "Delete Meal by Meal ID");
        List<String> labels = List.of("ID");
        inputScreenBuilder.display(labels, "deleteMeal");
    }

    public void showDeleteReviewScreen() {
        InputScreenBuilder inputScreenBuilder = new InputScreenBuilder(gui, eventManager, "Delete Review by Rating ID");
        List<String> labels = List.of("ID");
        inputScreenBuilder.display(labels, "deleteReview");
    }

    public void showMealDetailsById() {
        InputScreenBuilder inputScreenBuilder = new InputScreenBuilder(gui, eventManager, "Search Meal by ID");
        List<String> labels = List.of("ID");
        inputScreenBuilder.display(labels, "mealDetailsById");
    }

    public void showSearchMealByName() {
        InputScreenBuilder inputScreenBuilder = new InputScreenBuilder(gui, eventManager, "Search Meal by Name");
        List<String> labels = List.of("Name");
        inputScreenBuilder.display(labels, "searchMealByName");
    }


    public void closeActiveWindow() {
        if (gui.getActiveWindow() != null) {
            gui.getActiveWindow().close();
        }
    }

    public void closeTerminal() {
        try {
            gui.getScreen().stopScreen();
        } catch (IOException e) {
            eventManager.notify("error", "Error closing terminal");

        }
    }

    public void showSearchReviewsByMealName() {
        InputScreenBuilder inputScreenBuilder = new InputScreenBuilder(gui, eventManager,
                "Search Reviews by Meal Name");
        List<String> labels = List.of("Meal Name");
        inputScreenBuilder.display(labels, "searchReviewsByMealName");
    }

    public void showAllergeneSettings() {
        List<String> allergene = Arrays.asList(
                "Gluten-containing cereals",
                "Crustaceans",
                "Eggs",
                "Fish",
                "Peanuts",
                "Soy",
                "Milk",
                "Nuts",
                "Celery",
                "Mustard",
                "Sesame seeds",
                "Sulfur dioxide and sulfites",
                "Lupins",
                "Molluscs"
        );

        // CheckboxBuilder erstellen
        CheckboxScreenBuilder builder = new CheckboxScreenBuilder(gui, eventManager, "Select Allergens");


        // Menü anzeigen
        builder.display(allergene, "allergeneSettings");
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
                new MenuBuilder.MenuButton("Main Menu", "showMainMenu"), new MenuBuilder.MenuButton("Exit", "exit"));

        MenuBuilder adminMenu = new MenuBuilder(gui, eventManager).setTitle("Admin Menu").setButtons(adminMenuButtons);
        adminMenu.display();
    }

    //Weekly Plan
    public void showEditWeeklyPlanScreen() {
        List<MenuBuilder.MenuButton> weeklyPlanButtons = Arrays.asList(
                new MenuBuilder.MenuButton("Monday", "editWeeklyPlanMonday"),
                new MenuBuilder.MenuButton("Tuesday", "editWeeklyPlanTuesday"),
                new MenuBuilder.MenuButton("Wednesday", "editWeeklyPlanWednesday"),
                new MenuBuilder.MenuButton("Thursday", "editWeeklyPlanThursday"),
                new MenuBuilder.MenuButton("Friday", "editWeeklyPlanFriday"),
                new MenuBuilder.MenuButton("Reset Weekly Plan", "resetWeeklyPlan"),
                new MenuBuilder.MenuButton("Main Menu", "showMainMenu"), new MenuBuilder.MenuButton("Exit", "exit"));

        MenuBuilder weeklyPlan = new MenuBuilder(gui, eventManager).setTitle("Edit Weekly Plan").setButtons(
                weeklyPlanButtons);
        weeklyPlan.display();
    }


    public void showEditWeeklyPlanMonday() {
        InputScreenBuilder inputScreenBuilder = new InputScreenBuilder(gui, eventManager, "Edit Monday");
        List<String> labels = List.of("Meal Name");
        inputScreenBuilder.display(labels, "editWeeklyPlanMondaySubmit");
    }

    public void showEditWeeklyPlanTuesday() {
        InputScreenBuilder inputScreenBuilder = new InputScreenBuilder(gui, eventManager, "Edit Tuesday");
        List<String> labels = Arrays.asList("Meal Name");
        inputScreenBuilder.display(labels, "editWeeklyPlanTuesdaySubmit");
    }

    public void showEditWeeklyPlanWednesday() {
        InputScreenBuilder inputScreenBuilder = new InputScreenBuilder(gui, eventManager, "Edit Wednesday");
        List<String> labels = Arrays.asList("Meal Name");
        inputScreenBuilder.display(labels, "editWeeklyPlanWednesdaySubmit");
    }

    public void showEditWeeklyPlanThursday() {
        InputScreenBuilder inputScreenBuilder = new InputScreenBuilder(gui, eventManager, "Edit Thursday");
        List<String> labels = Arrays.asList("Meal Name");
        inputScreenBuilder.display(labels, "editWeeklyPlanThursdaySubmit");
    }

    public void showEditWeeklyPlanFriday() {
        InputScreenBuilder inputScreenBuilder = new InputScreenBuilder(gui, eventManager, "Edit Friday");
        List<String> labels = Arrays.asList("Meal Name");
        inputScreenBuilder.display(labels, "editWeeklyPlanFridaySubmit");
    }

    public void showWeeklyPlanScreen(List<MealsRecord> weeklyPlan) {
        TableBuilder tableBuilder = new TableBuilder(gui, "Weekly Plan").addColumn("Day").addColumn(
                "Meal Name").addColumn("Price").addColumn("Calories").addColumn("Allergens").addColumn("Meat");

        for (MealsRecord meal : weeklyPlan) {
            String allergens = Optional.ofNullable(meal.getAllergy()).filter(allergy -> !allergy.isEmpty()).map(
                    allergy -> Arrays.stream(allergy.split("")).map(AllergenMapper::getAllergenFullName).collect(
                            Collectors.joining(" "))).orElse("No Allergies");

            tableBuilder.addRow(Arrays.asList(meal.getDay(), meal.getName(), String.format("%.2f", meal.getPrice()),
                    String.valueOf(meal.getCalories()), allergens, MealTypeMapper.getMealTypeName(meal.getMeat())));
        }

        tableBuilder.display();
    }

}