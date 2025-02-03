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

    public void closeActiveWindow() {
        if (gui.getActiveWindow() != null) {
            gui.getActiveWindow().close();
        }
    }
    public void closeTerminal() throws IOException {
        gui.getScreen().stopScreen();
    }
}