// src/main/java/de/htwsaar/cantineplanner/presentation/ScreenManager.java
package de.htwsaar.cantineplanner.presentation;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import de.htwsaar.cantineplanner.businessLogic.AllergenMapper;
import de.htwsaar.cantineplanner.businessLogic.EventManager;
import de.htwsaar.cantineplanner.businessLogic.MealTypeMapper;
import de.htwsaar.cantineplanner.codegen.tables.records.MealsRecord;
import de.htwsaar.cantineplanner.dataAccess.DBConnection;
import de.htwsaar.cantineplanner.presentation.pages.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ScreenManager {
    private MultiWindowTextGUI gui;

    public ScreenManager() {
        try {
            Screen screen = new DefaultTerminalFactory().createScreen();
            screen.startScreen();
            this.gui = new MultiWindowTextGUI(screen);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showLoginScreen(EventManager eventManager) {
        LoginScreen loginScreen = new LoginScreen(gui, eventManager);
        loginScreen.display();
    }

    public void showMealMenuScreen(EventManager eventManager) {
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

        MenuBuilder mealMenu = new MenuBuilder(gui, eventManager)
                .setTitle("Meal Menu")
                .setButtons(mealMenuButtons);
        mealMenu.display();
    }

    public void showUserMenuScreen(EventManager eventManager) {
        List<MenuBuilder.MenuButton> userMenuButtons = Arrays.asList(
                new MenuBuilder.MenuButton("Benutzerdaten bearbeiten", "editUserData"),
                new MenuBuilder.MenuButton("Allergien verwalten", "manageAllergies"),
                new MenuBuilder.MenuButton("Bewertungen anzeigen", "showReviews"),
                new MenuBuilder.MenuButton("Abmelden", "logout"),
                new MenuBuilder.MenuButton("Main Menü", "showMainMenu"),
                new MenuBuilder.MenuButton("Programm beenden", "exit")
        );

        MenuBuilder userMenu = new MenuBuilder(gui, eventManager)
                .setTitle("User Menu")
                .setButtons(userMenuButtons);
        userMenu.display();
    }

    public void showErrorScreen(String message) {
        NotificationScreenBuilder errorScreen = new NotificationScreenBuilder(gui, message, new TextColor.RGB(255, 0, 0));
        errorScreen.display();
    }

    public void showSuccessScreen(String message) {
        NotificationScreenBuilder successScreen = new NotificationScreenBuilder(gui, message, new TextColor.RGB(0, 255, 0));
        successScreen.display();
    }

    public void showMainMenuScreen(EventManager eventManager) {
        List<MenuBuilder.MenuButton> mainMenuButtons = Arrays.asList(
                new MenuBuilder.MenuButton("User Menu", "showUserMenu"),
                new MenuBuilder.MenuButton("Meal Menu", "showMealMenu"),
                new MenuBuilder.MenuButton("Review Menu", "showReviewMenu"),
                new MenuBuilder.MenuButton("Exit", "exit")
        );
        MenuBuilder mainMenu = new MenuBuilder(gui, eventManager)
                .setTitle("Main Menu")
                .setButtons(mainMenuButtons);
        mainMenu.display();
    }

    public void showAllergiesMenu(EventManager eventManager) {
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

    public void showReviewsMenu(EventManager eventManager) {
        List<MenuBuilder.MenuButton> reviewsMenuButtons = Arrays.asList(
                new MenuBuilder.MenuButton("Alle Reviews anzeigen", "showAllReviews"),
                new MenuBuilder.MenuButton("Review hinzufügen", "addReview"),
                new MenuBuilder.MenuButton("Review löschen", "deleteReview"),
                new MenuBuilder.MenuButton("Review nach ID suchen", "searchReviewById"),
                new MenuBuilder.MenuButton("Reviewdetails nach ID anzeigen", "showReviewDetailsById"),
                new MenuBuilder.MenuButton("Main Menü", "showMainMenu"),
                new MenuBuilder.MenuButton("Programm beenden", "exit")
        );

        MenuBuilder reviewsMenu = new MenuBuilder(gui, eventManager)
                .setTitle("Reviews Menu")
                .setButtons(reviewsMenuButtons);
        reviewsMenu.display();
    }

    public void showAllMeals(EventManager eventManager, List<MealsRecord> meals) {
        TableBuilder tableBuilder = new TableBuilder(gui, "All Meals")
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
                    meal.getName(),
                    String.format("%.2f", meal.getPrice()),
                    String.valueOf(meal.getCalories()),
                    allergens,
                    MealTypeMapper.getMealTypeName(meal.getMeat())
            ));
        }

        tableBuilder.display();
    }
    public void showInputScreenReg(EventManager eventManager, String title, String event) {
        InputScreenBuilder inputScreenBuilder = new InputScreenBuilder(gui, eventManager,title);
        List<String> labels = Arrays.asList("Username", "Password", "Email");
        inputScreenBuilder.display(labels, event);
    }
    public void showAllAllergies(EventManager eventManager, List<String> allergies) {
        TableBuilder tableBuilder = new TableBuilder(gui, "All Allergies")
                .addColumn("Allergy");


       for(String allergy : allergies) {
           tableBuilder.addRow(Arrays.asList(allergy));
       }

         tableBuilder.display();
    }

    public void closeActiveWindow() {
        if (gui.getActiveWindow() != null) {
            gui.getActiveWindow().close();
        }
    }

    public void closeTerminal() throws IOException {
        gui.getScreen().stopScreen();
    }
}