// src/main/java/de/htwsaar/cantineplanner/businessLogic/Controller.java
package de.htwsaar.cantineplanner.businessLogic;

import de.htwsaar.cantineplanner.presentation.ScreenManager;
import de.htwsaar.cantineplanner.presentation.pages.RegisterScreen;

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

        eventManager.subscribe("login", this::handleLogin);
        eventManager.subscribe("register", this::handleRegister);
        eventManager.subscribe("showRegisterScreen", this::handleShowRegisterScreen);
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

    private void handleLogin(Object data) {
        String[] credentials = (String[]) data;
        String username = credentials[0];
        String password = credentials[1];
        if (cantineService.validateUser(username, password)) {
            screenManager.showSuccessScreen("Login successful!");
            currentMenu = 1; // Go to main menu
        } else {
            screenManager.showErrorScreen("Username or password is incorrect retry!");
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
        if (cantineService.registerUser(username, password,email)) {
            screenManager.showSuccessScreen("Registration successful!");
            currentMenu = 0; // Go back to login menu
        } else {
            screenManager.showErrorScreen("Registration failed");
        }
    }

    private void handleShowRegisterScreen(Object data) {
        screenManager.showRegisterScreen(eventManager);
    }

    public void userMenu() {
        screenManager.showLoginScreen(eventManager);
    }

    public void mainMenu() {
        // screenManager.showMainMenu();
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