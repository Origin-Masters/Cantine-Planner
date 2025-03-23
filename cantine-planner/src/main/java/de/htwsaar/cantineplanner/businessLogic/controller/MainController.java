package de.htwsaar.cantineplanner.businessLogic.controller;

import de.htwsaar.cantineplanner.businessLogic.CantineService;
import de.htwsaar.cantineplanner.businessLogic.EventManager;
import de.htwsaar.cantineplanner.codegen.tables.Review;
import de.htwsaar.cantineplanner.codegen.tables.records.UsersRecord;
import de.htwsaar.cantineplanner.exceptions.UserDoesntExistException;
import de.htwsaar.cantineplanner.presentation.ScreenManager;

import java.sql.SQLException;

public class MainController extends AbstractController {
    private int currentMenu;
    private boolean running;

    private MealController mealController;
    private ReviewController reviewController;
    private UserController userController;
    private WeeklyController weeklyController;
    private LoginController loginController;

    private int currentUserId;


    public MainController(ScreenManager screenManager,
                          CantineService cantineService,
                          EventManager eventManager,
                          UsersRecord userRecord) {

        super(screenManager, cantineService, eventManager, userRecord);
        this.mealController = new MealController(screenManager, cantineService, eventManager, userRecord);
        this.reviewController = new ReviewController(screenManager, cantineService, eventManager, userRecord);
        this.userController = new UserController(screenManager, cantineService, eventManager, userRecord);
        this.weeklyController = new WeeklyController(screenManager, cantineService, eventManager, userRecord);
        this.loginController = new LoginController(screenManager, cantineService, eventManager, userRecord);

        this.currentUserId = userRecord.getUserid();

        subscribeToEvents();
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
     mealController.showMealMenu(currentUserId);
    }

    /**
     * Displays the review menu screen.
     */
    private void reviewMenu() {
        reviewController.showReviewMenu();
    }

    /**
     * Displays the user menu screen.
     * <p>
     * Checks if the current user is an admin to determine appropriate view.
     * </p>
     */
    private void userMenu() {

        userController.showUserMenu(currentUserId);
    }

    /**
     * Displays the weekly menu screen.
     * <p>
     * Checks if the current user is an admin to determine appropriate view.
     * </p>
     */
    private void weeklyMenu() {

        weeklyController.showWeeklyMenu(currentUserId);
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
