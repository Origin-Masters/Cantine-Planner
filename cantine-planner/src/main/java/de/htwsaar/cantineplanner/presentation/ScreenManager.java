// src/main/java/de/htwsaar/cantineplanner/presentation/ScreenManager.java
package de.htwsaar.cantineplanner.presentation;

import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import de.htwsaar.cantineplanner.businessLogic.EventManager;
import de.htwsaar.cantineplanner.presentation.pages.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

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

    public void showRegisterScreen(EventManager eventManager) {
        RegisterScreen registerScreen = new RegisterScreen(gui, eventManager);
        registerScreen.display();
    }

    public void showErrorScreen(String message) {
        ErrorScreen errorScreen = new ErrorScreen(gui, message);
        errorScreen.display();
    }

    public void showSuccessScreen(String message) {
        SuccessScreen successScreen = new SuccessScreen(gui, message);
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
    public void showAllMeals(EventManager eventManager){
        TableBuilder tableBuilder = new TableBuilder(gui, "Example Table")
                .addColumn("Column 1")
                .addColumn("Column 2")
                .addColumn("Column 3");

        tableBuilder.addRow(Arrays.asList("Row 1, Col 1", "Row 1, Col 2", "Row 1, Col 3"));
        tableBuilder.addRow(Arrays.asList("Row 2, Col 1", "Row 2, Col 2", "Row 2, Col 3"));
        tableBuilder.addRow(Arrays.asList("Row 3, Col 1", "Row 3, Col 2", "Row 3, Col 3"));

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