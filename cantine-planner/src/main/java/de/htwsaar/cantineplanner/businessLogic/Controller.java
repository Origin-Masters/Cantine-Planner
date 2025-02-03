// src/main/java/de/htwsaar/cantineplanner/businessLogic/Controller.java
package de.htwsaar.cantineplanner.businessLogic;

import de.htwsaar.cantineplanner.presentation.ScreenManager;

import java.io.IOException;

public class Controller {
    private ScreenManager screenManager;
    private CantineService cantineService;
    private EventManager eventManager;
    private boolean running;
    private int currentMenu;

    public Controller(ScreenManager screenManager) {
        this.screenManager = screenManager;
        this.cantineService = new CantineService();
        this.eventManager = new EventManager();
        this.running = false;
        subscribeToEvents();

    }
    private void subscribeToEvents() {
        eventManager.subscribe("login", this::handleLogin);
        eventManager.subscribe("register", this::handleRegister);
        eventManager.subscribe("showRegisterScreen", this::handleShowRegisterScreen);
        eventManager.subscribe("showMenu", (data) -> switchMenu(1));
        eventManager.subscribe("showMealMenu", (data) -> switchMenu(2));
        eventManager.subscribe("showReviewMenu", (data) -> switchMenu(3));
        eventManager.subscribe("exit", (data) -> exitApplication());
    }

    public void start() {
        running = true;
        currentMenu = 0;
        while (running) {
            switch (currentMenu) {
                case 0:
                    userMenu();
                    break;
                case 1:
                    mainMenu();
                    break;
                case 2:
                    mealMenu();
                    break;
                case 3:
                    reviewMenu();
                    break;
                default:
                    System.out.println("Invalid Input");
            }
        }
    }

    private void exitApplication()  {
        try {
            screenManager.closeTerminal();
            running = false;
        } catch (IOException e) {
            screenManager.showErrorScreen("Error while closing terminal");

        }
    }

    private void handleLogin(Object data) {
        String[] credentials = (String[]) data;
        String username = credentials[0];
        String password = credentials[1];
        if (cantineService.validateUser(username, password)) {
            screenManager.closeActiveWindow(); // Close the login window
            screenManager.showSuccessScreen("Login successful!");
            switchMenu(1); // Go to main menu
        } else {
            screenManager.showErrorScreen("Username or password is incorrect. Please retry!");
        }
    }

    private void handleRegister(Object data) {
        if (data == null) {
            screenManager.showErrorScreen("Data is null");
            return;
        }
        String[] credentials = (String[]) data;
        String username = credentials[0];
        String password = credentials[1];
        String email = credentials[2];
        if (cantineService.registerUser(username, password, email)) {
            screenManager.closeActiveWindow(); // Close the register window
            screenManager.showSuccessScreen("Registration successful!");
            switchMenu(0); // Go back to login menu
        } else {
            screenManager.showErrorScreen("Registration failed");
        }
    }

    private void handleShowRegisterScreen(Object data) {
        screenManager.showRegisterScreen(eventManager);
    }

    private void switchMenu(int menu) {
        screenManager.closeActiveWindow(); // Close the current window
        currentMenu = menu;
    }

    public void userMenu() {
        screenManager.showLoginScreen(eventManager);
    }

    public void mainMenu() {
        screenManager.showMenuScreen(eventManager);
    }

    public void mealMenu() {
        // screenManager.showMealMenu();
    }

    public void reviewMenu() {
        // screenManager.showReviewMenu();
    }

    public int getCurrentMenu() {
        return currentMenu;
    }

    public boolean isRunning() {
        return running;
    }

    public CantineService getCantineService() {
        return cantineService;
    }
}