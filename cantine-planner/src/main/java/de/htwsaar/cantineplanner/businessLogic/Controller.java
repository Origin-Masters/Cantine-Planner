// src/main/java/de/htwsaar/cantineplanner/businessLogic/Controller.java
package de.htwsaar.cantineplanner.businessLogic;

import de.htwsaar.cantineplanner.codegen.tables.records.MealsRecord;
import de.htwsaar.cantineplanner.exceptions.UserAlreadyExistsException;
import de.htwsaar.cantineplanner.presentation.ScreenManager;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class Controller {
    private ScreenManager screenManager;
    private CantineService cantineService;
    private EventManager eventManager;
    private boolean running;
    private int currentMenu;
    private int currentUserId;

    public Controller() {

        this.eventManager = new EventManager();
        this.screenManager = new ScreenManager(eventManager);
        this.cantineService = new CantineService(eventManager);
        this.currentUserId = -1;
        this.running = false;
        subscribeToEvents();
    }

    private void subscribeToEvents() {
        eventManager.subscribe("success", (data) -> screenManager.showSuccessScreen((String) data));
        eventManager.subscribe("error", (data) -> screenManager.showErrorScreen((String) data));
        eventManager.subscribe("login", this::handleLogin);
        eventManager.subscribe("register", this::handleRegister);
        eventManager.subscribe("showRegisterScreen", this::handleShowRegisterScreen);
        eventManager.subscribe("showMainMenu", (data) -> switchMenu(1));
        eventManager.subscribe("showMealMenu", (data) -> switchMenu(2));
        eventManager.subscribe("showReviewMenu", (data) -> switchMenu(3));
        eventManager.subscribe("showUserMenu", (data) -> switchMenu(4));

        eventManager.subscribe("showAllMeals", (data) -> {
                screenManager.showAllMeals(cantineService.getAllMeals());
        });

        eventManager.subscribe("showAllAllergies", (data) -> {
                screenManager.showAllAllergies(cantineService.getAllAllergies());
        });

        eventManager.subscribe("logout", (data) -> {
                switchMenu(0);
                this.currentUserId = -1;
        });

        eventManager.subscribe("exit", (data) -> exitApplication());

        // Review events
        eventManager.subscribe("showAllReviews", (data) -> {
                screenManager.getAllReviews(cantineService.getAllReviews());
        });

        eventManager.subscribe("addMeal",this::handleAddMeal);

    }

    public void start() {
        running = true;
        currentMenu = 0;
        while (running) {
            switch (currentMenu) {
                case 0:
                    loginMenu();
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
                    case 4:
                        userMenu();
                        break;
                default:
                    System.out.println("Invalid Input");
            }
        }
    }

    private void exitApplication() {

            screenManager.closeTerminal();
            running = false;

    }

    private void handleLogin(Object data)  {
        String[] credentials = (String[]) data;
        String username = credentials[0];
        String password = credentials[1];
        if (cantineService.validateUser(username, password)) {
            currentUserId = cantineService.getUserId(username);
            screenManager.closeActiveWindow();
            screenManager.showSuccessScreen("Login successful!");
            switchMenu(1);
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

        if (isAnyFieldEmpty(username, password, email)) {
            screenManager.showErrorScreen("Please fill in all fields!");
            return;
        }

        if (cantineService.registerUser(username, password, email)) {
            screenManager.closeActiveWindow();
            screenManager.showSuccessScreen("Registration successful!");
            switchMenu(0);
        } else {
            screenManager.showErrorScreen("Registration failed");
        }
    }

    private boolean isAnyFieldEmpty(String... fields) {
        for (String field : fields) {
            if (Objects.equals(field, "")) {
                return true;
            }
        }
        return false;
    }
    private void handleShowRegisterScreen(Object data) {
        screenManager.showInputScreenReg( "Register", "register");
    }

    private void switchMenu(int menu) {
        screenManager.closeActiveWindow();
        currentMenu = menu;
    }
    public void userMenu() {
        screenManager.showUserMenuScreen();
    }
    public void loginMenu() {
        screenManager.showLoginScreen();
    }

    public void mainMenu() {
        screenManager.showMainMenuScreen();
    }

    public void mealMenu() {
        screenManager.showMealMenuScreen();
    }

    public void reviewMenu() {
        screenManager.showReviewsMenu();
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

    public int getCurrentUserId() {
        return currentUserId;
    }

    private void handleAddMeal(Object data) {
        String[] mealData = (String[]) data;
        String mealName = mealData[0];
        float price = Float.parseFloat(mealData[1]);
        int calories = Integer.parseInt(mealData[2]);
        String allergy = mealData[3];
        MealsRecord meal = new MealsRecord();
        meal.setName(mealName);
        meal.setPrice(price);
        meal.setCalories(calories);
        meal.setAllergy(allergy);
        try {
            cantineService.addMeal(meal);
            screenManager.showSuccessScreen("Meal added successfully!");
        } catch (Exception e) {
            screenManager.showErrorScreen("Error while adding meal: " + e.getMessage());
        }
    }
}