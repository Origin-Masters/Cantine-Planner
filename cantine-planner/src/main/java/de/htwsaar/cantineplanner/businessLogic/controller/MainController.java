package de.htwsaar.cantineplanner.businessLogic.controller;

import de.htwsaar.cantineplanner.businessLogic.manager.SessionManager;
import de.htwsaar.cantineplanner.businessLogic.service.CantineService;
import de.htwsaar.cantineplanner.businessLogic.manager.EventManager;
import de.htwsaar.cantineplanner.businessLogic.types.eventdata.EventType;
import de.htwsaar.cantineplanner.presentation.ScreenManager;

/**
 * The MainController class is responsible for managing the main application loop.
 * <p>
 * The MainController class is responsible for managing the main application loop.
 * It switches between different menus based on the current state of the application.
 * </p>
 */
public class MainController extends AbstractController {
    private int currentMenu;
    private boolean running;

    private final MealController mealController;
    private final ReviewController reviewController;
    private final UserController userController;
    private final WeeklyController weeklyController;
    private final LoginController loginController;

    /**
     * Constructs a new MainController.
     * <p>
     * This constructor initializes the MainController with the provided ScreenManager, CantineService, and EventManager.
     * It also initializes the controllers for meals, reviews, users, weekly plans, and login, and subscribes to the relevant events.
     * </p>
     *
     * @param screenManager  the screen manager to manage UI screens
     * @param cantineService the service to handle cantine-related operations
     * @param eventManager   the event manager to handle events
     * @param sessionManager the session manager to manage user sessions
     */
    public MainController(ScreenManager screenManager,
                          CantineService cantineService,
                          EventManager eventManager,
                          SessionManager sessionManager) {

        super(screenManager, cantineService, eventManager,sessionManager);
        this.mealController = new MealController(screenManager, cantineService, eventManager,sessionManager);
        this.reviewController = new ReviewController(screenManager, cantineService, eventManager,sessionManager);
        this.userController = new UserController(screenManager, cantineService, eventManager,sessionManager);
        this.weeklyController = new WeeklyController(screenManager, cantineService, eventManager,sessionManager);
        this.loginController = new LoginController(screenManager, cantineService, eventManager,sessionManager);

        subscribeToEvents();
    }

    /**
     * Subscribes to various application event types and associates them with their respective handlers.
     * <p>
     * This method sets up the event subscriptions for handling different application events such as switching menus,
     * exiting the application, showing success and error screens, and logging out.
     * </p>
     */
    @Override
    protected void subscribeToEvents() {
        eventManager.subscribe(EventType.SWITCH_MENU, (data) -> switchMenu((int) data.getData()));
        eventManager.subscribe(EventType.EXIT, (data) -> exitApplication());
        eventManager.subscribe(EventType.SHOW_SUCCESS_SCREEN, (data) -> screenManager.showSuccessScreen((String) data.getData()));
        eventManager.subscribe(EventType.SHOW_ERROR_SCREEN, (data) -> screenManager.showErrorScreen((String) data.getData()));
        eventManager.subscribe(EventType.LOGOUT, (data) -> {
            switchMenu(0);
            sessionManager.logout();
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
    private void mealMenu() {
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
