package de.htwsaar.cantineplanner.businessLogic.controller;

import de.htwsaar.cantineplanner.businessLogic.CantineService;
import de.htwsaar.cantineplanner.businessLogic.EventManager;
import de.htwsaar.cantineplanner.exceptions.UserDoesntExistException;
import de.htwsaar.cantineplanner.presentation.ScreenManager;

import java.sql.SQLException;

public class MainController extends AbstractController {
    private int currentMenu;
    private boolean running;
    public MainController(ScreenManager screenManager,
                          CantineService cantineService,
                          EventManager eventManager,
                          int currentUserId) {
        super(screenManager, cantineService, eventManager, currentUserId);
    }

    @Override
    protected void subscribeToEvents() {
        //TODO switchMenu
        eventManager.subscribe("switchMenu", (data) -> switchMenu((int) data));

        eventManager.subscribe("showMainMenu", (data) -> switchMenu(1));
        eventManager.subscribe("showMealMenu", (data) -> switchMenu(2));
        eventManager.subscribe("showReviewMenu", (data) -> switchMenu(3));
        eventManager.subscribe("showUserMenu", (data) -> switchMenu(4));
        eventManager.subscribe("showWeeklyMenu", (data) -> switchMenu(5));
        eventManager.subscribe("exit", (data) -> exitApplication());
        eventManager.subscribe("success", (data) -> screenManager.showSuccessScreen((String) data));
        eventManager.subscribe("error", (data) -> screenManager.showErrorScreen((String) data));
        eventManager.subscribe("logout", (data) -> {
            switchMenu(0);
            this.currentUserId = -1;
        });
    }

    /**
     * Starts the main application loop.
     * <p>
     * Runs the main loop, switching between different menus based on the current state.
     * </p>
     */
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
                case 5:
                    weeklyMenu();
                    break;
                default:
            }
        }
    }

    /**
     * Switches the current active menu.
     *
     * @param menu the menu identifier to switch to
     */
    private void switchMenu(int menu) {
        screenManager.closeActiveWindow();
        currentMenu = menu;
    }

    /**
     * Displays the login screen.
     */
    private void loginMenu() {
        screenManager.showLoginScreen();
    }

    /**
     * Displays the main menu screen.
     */
    private void mainMenu() {
        screenManager.showMainMenuScreen();
    }

    /**
     * Displays the meal menu screen.
     * <p>
     * Determines if the current user has admin privileges to customize the display.
     * </p>
     */
    private void mealMenu()  {
        try{
            screenManager.showMealMenuScreen(cantineService.isAdmin(currentUserId));
        } catch (SQLException | UserDoesntExistException e) {
            screenManager.showErrorScreen("There was an error while validating the user try again!");
        }
    }

    /**
     * Displays the review menu screen.
     */
    private void reviewMenu() {
        screenManager.showReviewsMenu();
    }

    /**
     * Displays the user menu screen.
     * <p>
     * Checks if the current user is an admin to determine appropriate view.
     * </p>
     */
    private void userMenu() {
        try{
            screenManager.showUserMenuScreen(cantineService.isAdmin(currentUserId));
        } catch (SQLException | UserDoesntExistException e) {
            screenManager.showErrorScreen("There was an error while validating the user try again!");
        }
    }

    /**
     * Displays the weekly menu screen.
     * <p>
     * Checks if the current user is an admin to determine appropriate view.
     * </p>
     */
    private void weeklyMenu() {
        try {
            screenManager.showWeeklyMenuScreen(cantineService.isAdmin(currentUserId));
        } catch (SQLException e) {
            screenManager.showErrorScreen("There was an error while validating the user try again!");
        }
    }

    /**
     * Exits the application.
     * <p>
     * Closes the terminal and stops the main application loop.
     * </p>
     */
    private void exitApplication() {
        screenManager.closeTerminal();
        running = false;
    }
}
