package de.htwsaar.cantineplanner.businessLogic.controller;

import de.htwsaar.cantineplanner.businessLogic.CantineService;
import de.htwsaar.cantineplanner.businessLogic.EventManager;
import de.htwsaar.cantineplanner.businessLogic.controller.eventdata.EventType;
import de.htwsaar.cantineplanner.codegen.tables.Review;
import de.htwsaar.cantineplanner.codegen.tables.records.UsersRecord;
import de.htwsaar.cantineplanner.exceptions.UserDoesntExistException;
import de.htwsaar.cantineplanner.presentation.ScreenManager;

import java.sql.SQLException;

public class MainController extends AbstractController {
    private int currentMenu;
    private boolean running;

    private final MealController mealController;
    private final ReviewController reviewController;
    private final UserController userController;
    private final WeeklyController weeklyController;
    private final LoginController loginController;



    public MainController(ScreenManager screenManager,
                          CantineService cantineService,
                          EventManager eventManager,
                          UsersRecord userRecord) {

        super(screenManager, cantineService, eventManager);
        this.mealController = new MealController(screenManager, cantineService, eventManager);
        this.reviewController = new ReviewController(screenManager, cantineService, eventManager);
        this.userController = new UserController(screenManager, cantineService, eventManager);
        this.weeklyController = new WeeklyController(screenManager, cantineService, eventManager);
        this.loginController = new LoginController(screenManager, cantineService, eventManager);

        subscribeToEvents();
    }

    @Override
    protected void subscribeToEvents() {
        eventManager.subscribe(EventType.SWITCH_MENU, (data) -> switchMenu((int) data.getData()));
        eventManager.subscribe(EventType.EXIT, (data) -> exitApplication());
        eventManager.subscribe(EventType.SHOW_SUCCESS_SCREEN, (data) -> screenManager.showSuccessScreen((String) data.getData()));
        eventManager.subscribe(EventType.SHOW_ERROR_SCREEN, (data) -> screenManager.showErrorScreen((String) data.getData()));
        eventManager.subscribe(EventType.LOGOUT, (data) -> {
            switchMenu(0);
            currentUser = null;
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
                    break;
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
     mealController.showMealMenu();
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

        userController.showUserMenu();
    }

    /**
     * Displays the weekly menu screen.
     * <p>
     * Checks if the current user is an admin to determine appropriate view.
     * </p>
     */
    private void weeklyMenu() {

        weeklyController.showWeeklyMenu();
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
