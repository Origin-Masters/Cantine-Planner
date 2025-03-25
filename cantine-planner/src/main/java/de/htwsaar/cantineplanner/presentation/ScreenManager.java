package de.htwsaar.cantineplanner.presentation;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import de.htwsaar.cantineplanner.businessLogic.AllergenMapper;
import de.htwsaar.cantineplanner.businessLogic.CantineService;
import de.htwsaar.cantineplanner.businessLogic.EventManager;
import de.htwsaar.cantineplanner.businessLogic.MealTypeMapper;
import de.htwsaar.cantineplanner.businessLogic.controller.eventdata.EventType;
import de.htwsaar.cantineplanner.businessLogic.controller.eventdata.IntData;
import de.htwsaar.cantineplanner.businessLogic.controller.eventdata.ShowErrorScreenData;
import de.htwsaar.cantineplanner.codegen.tables.records.MealsRecord;
import de.htwsaar.cantineplanner.codegen.tables.records.ReviewRecord;
import de.htwsaar.cantineplanner.codegen.tables.records.UsersRecord;
import de.htwsaar.cantineplanner.presentation.pages.*;
import jdk.jfr.Event;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Manager for application screens.
 * <p>
 * This class groups methods by menus, input screens, table screens, weekly plan screens, notifications and closing operations.
 * </p>
 */
public class ScreenManager {
    private MultiWindowTextGUI gui;
    private final EventManager eventManager;
    private final CantineService cantineService;

    ////////////////////////////////////////////////////////////////////////////////
    // Constructor and Initialization
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * Constructor that initializes the terminal screen and GUI.
     *
     * @param eventManager the event manager for notifications and events.
     */
    public ScreenManager(EventManager eventManager, CantineService cantineService) {
        this.eventManager = eventManager;
        this.cantineService = cantineService;

        try {
            Screen screen = new DefaultTerminalFactory().createScreen();
            screen.startScreen();
            this.gui = new MultiWindowTextGUI(screen);
        } catch (IOException e) {
            eventManager.notify(EventType.SHOW_ERROR_SCREEN, new ShowErrorScreenData("Error starting terminal"));
        }
    }

    ////////////////////////////////////////////////////////////////////////////////
    // Menus
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * Displays the main menu screen.
     */
    public void showMainMenuScreen() {
        List<MenuBuilder.MenuButton> mainMenuButtons = Arrays.asList(
                new MenuBuilder.MenuButton("User Menu", EventType.SWITCH_MENU, new IntData(4)),
                new MenuBuilder.MenuButton("Meal Menu", EventType.SWITCH_MENU, new IntData(2)),
                new MenuBuilder.MenuButton("Review Menu", EventType.SWITCH_MENU, new IntData(3)),
                new MenuBuilder.MenuButton("Weekly Menu", EventType.SWITCH_MENU, new IntData(5)),
                new MenuBuilder.MenuButton("Logout", EventType.LOGOUT),
                new MenuBuilder.MenuButton("Exit", EventType.EXIT)
        );
        MenuBuilder mainMenu = new MenuBuilder(gui, eventManager)
                .setTitle("Main Menu")
                .setButtons(mainMenuButtons);
        mainMenu.display();
    }

    /**
     * Displays the meal menu screen.
     *
     * @param isAdmin flag indicating if admin functionalities should be available.
     */
    public void showMealMenuScreen(boolean isAdmin) {
        List<MenuBuilder.MenuButton> mealMenuButtons = new ArrayList<>(
                Arrays.asList(
                        new MenuBuilder.MenuButton("All Meal", EventType.SHOW_ALL_MEALS),
                        new MenuBuilder.MenuButton("Sort Meal", EventType.SHOW_SORT_MEALS),
                        new MenuBuilder.MenuButton("Allergies in Meals", EventType.SHOW_ALL_ALLERGIES),
                        new MenuBuilder.MenuButton("Search Meal by Name", EventType.SHOW_SEARCH_MEAL_BY_NAME),
                        new MenuBuilder.MenuButton("Search Meal by ID", EventType.SHOW_MEAL_DETAILS_BY_ID)
                ));
        if (isAdmin) {
            mealMenuButtons.add(new MenuBuilder.MenuButton("Add Meal", EventType.SHOW_ADD_MEAL));
            mealMenuButtons.add(new MenuBuilder.MenuButton("Edit Meal", EventType.SHOW_EDIT_MEAL));
            mealMenuButtons.add(new MenuBuilder.MenuButton("Delete Meal", EventType.SHOW_DELETE_MEAL));
        }
        mealMenuButtons.add(new MenuBuilder.MenuButton("Main Menu", EventType.SWITCH_MENU, new IntData(1)));
        MenuBuilder mealMenu = new MenuBuilder(gui, eventManager)
                .setTitle("Meal Menu")
                .setButtons(mealMenuButtons);
        mealMenu.display();
    }

    /**
     * Displays the user menu screen.
     *
     * @param isAdmin flag indicating if admin functionalities should be available.
     */
    public void showUserMenuScreen(boolean isAdmin) {
        List<MenuBuilder.MenuButton> userMenuButtons = new ArrayList<>(
                Arrays.asList(
                        new MenuBuilder.MenuButton("Edit My Data", EventType.SHOW_EDIT_NEW_USER_DATA),
                        new MenuBuilder.MenuButton("Set My Allergy", EventType.SHOW_ALLERGEN_SETTINGS),
                        new MenuBuilder.MenuButton("My Reviews", EventType.SHOW_REVIEWS_BY_USER)
                ));
        if (isAdmin) {
            userMenuButtons.add(new MenuBuilder.MenuButton("All Users", EventType.SHOW_ALL_USERS));
            userMenuButtons.add(new MenuBuilder.MenuButton("Add User", EventType.SHOW_REGISTER_SCREEN));
            userMenuButtons.add(new MenuBuilder.MenuButton("Delete User", EventType.SHOW_DELETE_USER));
            userMenuButtons.add(new MenuBuilder.MenuButton("Update User Roles", EventType.SHOW_UPDATE_USER_ROLE));
        }
        userMenuButtons.add(new MenuBuilder.MenuButton("Main Menu", EventType.SWITCH_MENU, new IntData(1)));
        MenuBuilder userMenu = new MenuBuilder(gui, eventManager)
                .setTitle("User Menu")
                .setButtons(userMenuButtons);
        userMenu.display();
    }

    /**
     * Displays the reviews menu screen.
     */
    public void showReviewsMenu() {
        List<MenuBuilder.MenuButton> reviewsMenuButtons = Arrays.asList(
                new MenuBuilder.MenuButton("All Reviews", EventType.SHOW_ALL_REVIEWS),
                new MenuBuilder.MenuButton("Add Reviews", EventType.SHOW_ADD_REVIEW),
                new MenuBuilder.MenuButton("Delete Review", EventType.DELETE_REVIEW),
                new MenuBuilder.MenuButton("Search Reviews", EventType.SEARCH_REVIEWS_BY_MEAL_NAME),
                new MenuBuilder.MenuButton("Main Menu", EventType.SWITCH_MENU, new IntData(1))
        );
        MenuBuilder reviewsMenu = new MenuBuilder(gui, eventManager)
                .setTitle("Reviews Menu")
                .setButtons(reviewsMenuButtons);
        reviewsMenu.display();
    }

    /**
     * Displays the weekly menu screen.
     *
     * @param isAdmin flag indicating if admin functionalities should be available.
     */
    public void showWeeklyMenuScreen(boolean isAdmin) {
        List<MenuBuilder.MenuButton> weeklyMenuButtons = new ArrayList<>(
                List.of(
                        new MenuBuilder.MenuButton("Weekly Plan", EventType.SHOW_WEEKLY_PLAN)
                ));
        if (isAdmin) {
            weeklyMenuButtons.add(new MenuBuilder.MenuButton("Edit Weekly Plan", EventType.EDIT_WEEKLY_PLAN));
        }
        weeklyMenuButtons.add(new MenuBuilder.MenuButton("Main Menu", EventType.SWITCH_MENU, new IntData(1)));
        MenuBuilder weeklyMenu = new MenuBuilder(gui, eventManager)
                .setTitle("Weekly Menu")
                .setButtons(weeklyMenuButtons);
        weeklyMenu.display();
    }

    ////////////////////////////////////////////////////////////////////////////////
    // Input Screens
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * Displays the login screen.
     */
    public void showLoginScreen() {
        LoginScreen loginScreen = new LoginScreen(gui, eventManager);
        loginScreen.display();
    }

    /**
     * Displays the edit meal input screen.
     */
    public void showEditMealScreen() {
        InputScreenBuilder inputScreenBuilder = new InputScreenBuilder(gui, eventManager, "Edit Meal by Meal ID");
        List<String> labels = List.of("ID", "Name", "Price", "Calories", "Allergy", "Meat");
        inputScreenBuilder.display(labels, EventType.EDIT_MEAL);
    }

    /**
     * Displays the update user role input screen.
     */
    public void showUpdateUserRoleScreen() {
        InputScreenBuilder inputScreenBuilder = new InputScreenBuilder(gui, eventManager, "Update User Role");
        List<String> labels = List.of("User ID", "Role");
        inputScreenBuilder.display(labels, EventType.UPDATE_USER_ROLE);
    }

    /**
     * Displays the edit user data input screen.
     */
    public void showEditUserDataScreen() {
        InputScreenBuilder inputScreenBuilder = new InputScreenBuilder(gui, eventManager, "Edit User Data");
        List<String> labels = List.of("Current Password");
        inputScreenBuilder.display(labels, EventType.EDIT_USER_DATA);
    }

    /**
     * Displays the edit new user data input screen.
     */
    public void showEditNewUserDataScreen() {
        InputScreenBuilder inputScreenBuilder = new InputScreenBuilder(gui, eventManager, "Edit New User Data");
        List<String> labels = Arrays.asList("New Password", "New Email");
        inputScreenBuilder.display(labels, EventType.EDIT_NEW_USER_DATA);
    }

    /**
     * Displays the registration input screen.
     *
     * @param title the title of the screen.
     * @param eventType the event to be triggered.
     */
    public void showInputScreenReg(String title, EventType eventType) {
        InputScreenBuilder inputScreenBuilder = new InputScreenBuilder(gui, eventManager, title);
        List<String> labels = Arrays.asList("Username", "Password", "Email");
        inputScreenBuilder.display(labels, eventType);
    }

    /**
     * Displays the add review input screen.
     */
    public void showAddReviewScreen() {
        InputScreenBuilder inputScreenBuilder = new InputScreenBuilder(gui, eventManager, "Add New Review");
        List<String> labels = Arrays.asList("Rating", "Comment", "Meal ID");
        inputScreenBuilder.display(labels, EventType.ADD_REVIEW);
    }

    /**
     * Displays the add meal input screen.
     */
    public void showAddMealScreen() {
        InputScreenBuilder inputScreenBuilder = new InputScreenBuilder(gui, eventManager, "Add New Meal");
        List<String> labels = Arrays.asList("Name", "Price", "Calories", "Allergy", "Meat");
        inputScreenBuilder.display(labels, EventType.ADD_MEAL);
    }

    /**
     * Displays the delete meal input screen.
     */
    public void showDeleteMealScreen() {
        InputScreenBuilder inputScreenBuilder = new InputScreenBuilder(gui, eventManager, "Delete Meal by Meal ID");
        List<String> labels = List.of("ID");
        inputScreenBuilder.display(labels, EventType.DELETE_MEAL);
    }

    /**
     * Displays the delete review input screen.
     */
    public void showDeleteReviewScreen() {
        InputScreenBuilder inputScreenBuilder = new InputScreenBuilder(gui, eventManager, "Delete Review by Rating ID");
        List<String> labels = List.of("ID");
        inputScreenBuilder.display(labels, EventType.DELETE_REVIEW);
    }

    /**
     * Displays the search meal by ID input screen.
     */
    public void showMealDetailsById() {
        InputScreenBuilder inputScreenBuilder = new InputScreenBuilder(gui, eventManager, "Search Meal by ID");
        List<String> labels = List.of("ID");
        inputScreenBuilder.display(labels, EventType.MEAL_DETAILS_BY_ID);
    }

    /**
     * Displays the search meal by name input screen.
     */
    public void showSearchMealByName() {
        InputScreenBuilder inputScreenBuilder = new InputScreenBuilder(gui, eventManager, "Search Meal by Name");
        List<String> labels = List.of("Name");
        inputScreenBuilder.display(labels, EventType.SEARCH_MEAL_BY_NAME);
    }

    /**
     * Displays the search reviews by meal name input screen.
     */
    public void showSearchReviewsByMealName() {
        InputScreenBuilder inputScreenBuilder = new InputScreenBuilder(gui, eventManager,
                "Search Reviews by Meal Name");
        List<String> labels = List.of("Meal Name");
        inputScreenBuilder.display(labels, EventType.SEARCH_REVIEWS_BY_MEAL_NAME);
    }

    /**
     * Displays the allergene settings input screen.
     */
    public void showAllergeneSettings() {
        List<String> allergene = Arrays.asList("Gluten", "Crustaceans", "Eggs", "Fish", "Peanuts",
                "Soy", "Milk", "Nuts", "Celery", "Mustard", "Sesame", "Sulfites", "Lupins",
                "Molluscs");
        CheckboxScreenBuilder builder = new CheckboxScreenBuilder(gui, eventManager, "Select Allergens");
        builder.display(allergene, EventType.ALLERGENE_SETTINGS);
    }

    /**
     * Displays the delete user input screen.
     */
    public void showDeleteUserScreen() {
        InputScreenBuilder inputScreenBuilder = new InputScreenBuilder(gui, eventManager, "Delete User by User ID");
        List<String> labels = List.of("ID");
        inputScreenBuilder.display(labels, EventType.DELETE_USER);
    }

    ////////////////////////////////////////////////////////////////////////////////
    // Table/List Screens
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * Displays a table of all reviews.
     *
     * @param reviews the list of review records.
     */
    public void showAllReviews(List<ReviewRecord> reviews) {
        TableBuilder tableBuilder = new TableBuilder(gui, "All Reviews")
                .addColumn("ID")
                .addColumn("Rating")
                .addColumn("Comment")
                .addColumn("Meal ID")
                .addColumn("Date")
                .addColumn("UserID");
        for (ReviewRecord review : reviews) {
            tableBuilder.addRow(Arrays.asList(String.valueOf(review.getRatingId()),
                    String.valueOf(review.getRating()),
                    review.getComment(),
                    String.valueOf(review.getMealId()),
                    review.getCreatedAt().toString(),
                    String.valueOf(review.getUserid())));
        }
        tableBuilder.display();
    }

    /**
     * Displays a table of all meals.
     *
     * @param meals the list of meal records.
     */
    public void showAllMeals(List<MealsRecord> meals) throws SQLException {
        TableBuilder tableBuilder = new TableBuilder(gui, "All Meals")
                .addColumn("ID")
                .addColumn("Name")
                .addColumn("Price")
                .addColumn("Calories")
                .addColumn("Allergens")
                .addColumn("Meat")
                .addColumn("Median Rating");
        for (MealsRecord meal : meals) {
            String allergenInfo = Optional.ofNullable(meal.getAllergy())
                    .filter(allergy -> !allergy.isEmpty())
                    .map(allergy -> Arrays.stream(allergy.split(""))
                            .map(AllergenMapper::getAllergenFullName)
                            .collect(Collectors.joining(" ")))
                    .orElse("No Allergies");
            String mealType = MealTypeMapper.getMealTypeName(meal.getMeat());

            double medianRating = cantineService.calculateMedianRatingForMeal(meal.getMealId());

            tableBuilder.addRow(Arrays.asList(String.valueOf(meal.getMealId()),
                    meal.getName(),
                    String.format("%.2f", meal.getPrice()),
                    String.valueOf(meal.getCalories()),
                    allergenInfo,
                    mealType,
                    medianRating == -1 ? "No Reviews" : String.format("%.2f", medianRating)));
        }
        tableBuilder.display();
    }

    /**
     * Displays the sort meal screen with various sorting options.
     * This method creates a menu with buttons for sorting meals by different criteria such as price, median rating, name, allergies, and calories.
     * It also includes a button to return to the main menu.
     */
    public void showSortMealScreen() {

        List<MenuBuilder.MenuButton> sortMealButtons = Arrays.asList(
                new MenuBuilder.MenuButton("Sort by Price", EventType.SORT_MEALS_BY_PRICE),
                new MenuBuilder.MenuButton("Sort by Median Rating", EventType.SORT_MEALS_BY_RATING),
                new MenuBuilder.MenuButton("Sort by Name", EventType.SORT_MEALS_BY_NAME),
                new MenuBuilder.MenuButton("Sort by Calories", EventType.SORT_MEALS_BY_ALLERGENS),
                new MenuBuilder.MenuButton("Sort by your Allergies", EventType.SORT_MEALS_BY_CALORIES),
                new MenuBuilder.MenuButton("Quit", EventType.SWITCH_MENU, new IntData(2))
        );
        MenuBuilder sortMeal = new MenuBuilder(gui, eventManager)
                .setTitle("Sort Meal")
                .setButtons(sortMealButtons);
        sortMeal.display();
    }

    /**
     * Displays meal details in a table format.
     *
     * @param meal the meal record.
     */
    public void showMealDetails(MealsRecord meal) {
        TableBuilder tableBuilder = new TableBuilder(gui, "Meal Details")
                .addColumn("ID")
                .addColumn("Name")
                .addColumn("Price")
                .addColumn("Calories")
                .addColumn("Allergens")
                .addColumn("Meat");
        String allergens = Optional.ofNullable(meal.getAllergy())
                .filter(allergy -> !allergy.isEmpty())
                .map(allergy -> Arrays.stream(allergy.split(""))
                        .map(AllergenMapper::getAllergenFullName)
                        .collect(Collectors.joining(" ")))
                .orElse("No Allergies");
        tableBuilder.addRow(Arrays.asList(String.valueOf(meal.getMealId()),
                meal.getName(),
                String.format("%.2f", meal.getPrice()),
                String.valueOf(meal.getCalories()),
                allergens,
                MealTypeMapper.getMealTypeName(meal.getMeat())));
        tableBuilder.display();
    }

    /**
     * Displays a table of all allergies in meals.
     *
     * @param meals the list of meal records.
     */
    public void showAllAllergies(List<MealsRecord> meals) {
        TableBuilder tableBuilder = new TableBuilder(gui, "Allergies in Meals")
                .addColumn("Meal Name")
                .addColumn("Allergy");
        for (MealsRecord meal : meals) {
            String allergens = Optional.ofNullable(meal.getAllergy())
                    .filter(allergy -> !allergy.isEmpty())
                    .map(allergy -> Arrays.stream(allergy.split(""))
                            .map(AllergenMapper::getAllergenFullName)
                            .collect(Collectors.joining(" ")))
                    .orElse("No Allergies");
            tableBuilder.addRow(Arrays.asList(meal.getName(), allergens));
        }
        tableBuilder.display();
    }

    /**
     * Displays a table of all users.
     *
     * @param users the list of user records.
     */
    public void showAllUser(List<UsersRecord> users) {
        TableBuilder tableBuilder = new TableBuilder(gui, "All Users")
                .addColumn("ID")
                .addColumn("Username")
                .addColumn("Email")
                .addColumn("Role")
                .addColumn("Allergies");
        for (UsersRecord user : users) {
            tableBuilder.addRow(Arrays.asList(String.valueOf(user.getUserid()),
                    user.getUsername(),
                    user.getEmail(),
                    String.valueOf(user.getRole()),
                    user.getDontShowMeal()));
        }
        tableBuilder.display();
    }

    /**
     * Displays a table of the weekly meal plan.
     *
     * @param weeklyPlan the list of meal records for the weekly plan.
     */
    public void showWeeklyPlanScreen(List<MealsRecord> weeklyPlan) {
        TableBuilder tableBuilder = new TableBuilder(gui, "Weekly Plan")
                .addColumn("Day")
                .addColumn("Meal Name")
                .addColumn("Price")
                .addColumn("Calories")
                .addColumn("Allergens")
                .addColumn("Meat");
        for (MealsRecord meal : weeklyPlan) {
            String allergens = Optional.ofNullable(meal.getAllergy())
                    .filter(allergy -> !allergy.isEmpty())
                    .map(allergy -> Arrays.stream(allergy.split(""))
                            .map(AllergenMapper::getAllergenFullName)
                            .collect(Collectors.joining(" ")))
                    .orElse("No Allergies");
            tableBuilder.addRow(Arrays.asList(meal.getDay(),
                    meal.getName(),
                    String.format("%.2f", meal.getPrice()),
                    String.valueOf(meal.getCalories()),
                    allergens,
                    MealTypeMapper.getMealTypeName(meal.getMeat())));
        }
        tableBuilder.display();
    }

    ////////////////////////////////////////////////////////////////////////////////
    // Weekly Plan Input Screens
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * Displays the weekly plan editing menu.
     */
    public void showEditWeeklyPlanScreen() {
        List<MenuBuilder.MenuButton> weeklyPlanButtons = Arrays.asList(
                new MenuBuilder.MenuButton("Monday", EventType.EDIT_WEEKLY_PLAN_MONDAY),
                new MenuBuilder.MenuButton("Tuesday", EventType.EDIT_WEEKLY_PLAN_TUESDAY),
                new MenuBuilder.MenuButton("Wednesday", EventType.EDIT_WEEKLY_PLAN_WEDNESDAY),
                new MenuBuilder.MenuButton("Thursday", EventType.EDIT_WEEKLY_PLAN_THURSDAY),
                new MenuBuilder.MenuButton("Friday", EventType.EDIT_WEEKLY_PLAN_FRIDAY),
                new MenuBuilder.MenuButton("Reset Weekly Plan", EventType.RESET_WEEKLY_PLAN),
                new MenuBuilder.MenuButton("Main Menu", EventType.SWITCH_MENU, new IntData(1))
        );
        MenuBuilder weeklyPlan = new MenuBuilder(gui, eventManager)
                .setTitle("Edit Weekly Plan")
                .setButtons(weeklyPlanButtons);
        weeklyPlan.display();
    }

    /**
     * Displays the input screen for editing Monday's meal.
     */
    public void showEditWeeklyPlanMonday() {
        InputScreenBuilder inputScreenBuilder = new InputScreenBuilder(gui, eventManager, "Edit Monday");
        List<String> labels = List.of("Meal Name");
        inputScreenBuilder.display(labels, EventType.EDIT_WEEKLY_PLAN_MONDAY_SUBMIT);
    }

    /**
     * Displays the input screen for editing Tuesday's meal.
     */
    public void showEditWeeklyPlanTuesday() {
        InputScreenBuilder inputScreenBuilder = new InputScreenBuilder(gui, eventManager, "Edit Tuesday");
        List<String> labels = List.of("Meal Name");
        inputScreenBuilder.display(labels, EventType.EDIT_WEEKLY_PLAN_TUESDAY_SUBMIT);
    }

    /**
     * Displays the input screen for editing Wednesday's meal.
     */
    public void showEditWeeklyPlanWednesday() {
        InputScreenBuilder inputScreenBuilder = new InputScreenBuilder(gui, eventManager, "Edit Wednesday");
        List<String> labels = List.of("Meal Name");
        inputScreenBuilder.display(labels, EventType.EDIT_WEEKLY_PLAN_WEDNESDAY_SUBMIT);
    }

    /**
     * Displays the input screen for editing Thursday's meal.
     */
    public void showEditWeeklyPlanThursday() {
        InputScreenBuilder inputScreenBuilder = new InputScreenBuilder(gui, eventManager, "Edit Thursday");
        List<String> labels = List.of("Meal Name");
        inputScreenBuilder.display(labels, EventType.EDIT_WEEKLY_PLAN_THURSDAY_SUBMIT);
    }

    /**
     * Displays the input screen for editing Friday's meal.
     */
    public void showEditWeeklyPlanFriday() {
        InputScreenBuilder inputScreenBuilder = new InputScreenBuilder(gui, eventManager, "Edit Friday");
        List<String> labels = List.of("Meal Name");
        inputScreenBuilder.display(labels, EventType.EDIT_WEEKLY_PLAN_FRIDAY_SUBMIT);
    }

    ////////////////////////////////////////////////////////////////////////////////
    // Notification and Closing Screens
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * Displays an error notification screen.
     *
     * @param message the error message to show.
     */
    public void showErrorScreen(String message) {
        NotificationScreenBuilder errorScreen = new NotificationScreenBuilder(gui, message,
                new TextColor.RGB(255, 0, 0));
        errorScreen.display();
    }

    /**
     * Displays a success notification screen.
     *
     * @param message the success message to show.
     */
    public void showSuccessScreen(String message) {
        NotificationScreenBuilder successScreen = new NotificationScreenBuilder(gui, message,
                new TextColor.RGB(0, 255, 0));
        successScreen.display();
    }

    /**
     * Closes the currently active window.
     */
    public void closeActiveWindow() {
        if (gui.getActiveWindow() != null) {
            gui.getActiveWindow().close();
        }
    }

    /**
     * Closes the terminal screen.
     */
    public void closeTerminal() {
        try {
            gui.getScreen().stopScreen();
        } catch (IOException e) {
            eventManager.notify(EventType.SHOW_ERROR_SCREEN, new ShowErrorScreenData("Error closing terminal"));
        }
    }
}