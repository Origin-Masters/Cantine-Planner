package de.htwsaar.cantineplanner.presentation;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import de.htwsaar.cantineplanner.businessLogic.AllergenMapper;
import de.htwsaar.cantineplanner.businessLogic.CantineService;
import de.htwsaar.cantineplanner.businessLogic.EventManager;
import de.htwsaar.cantineplanner.businessLogic.MealTypeMapper;
import de.htwsaar.cantineplanner.codegen.tables.records.MealsRecord;
import de.htwsaar.cantineplanner.codegen.tables.records.ReviewRecord;
import de.htwsaar.cantineplanner.codegen.tables.records.UsersRecord;
import de.htwsaar.cantineplanner.presentation.pages.*;

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
            eventManager.notify("error", "Error starting terminal");
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
                new MenuBuilder.MenuButton("User Menu", "showUserMenu"),
                new MenuBuilder.MenuButton("Meal Menu", "showMealMenu"),
                new MenuBuilder.MenuButton("Review Menu", "showReviewMenu"),
                new MenuBuilder.MenuButton("Weekly Menu", "showWeeklyMenu"),
                new MenuBuilder.MenuButton("Logout", "logout"),
                new MenuBuilder.MenuButton("Exit", "exit")
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
                        new MenuBuilder.MenuButton("All Meal", "showAllMeals"),
                        new MenuBuilder.MenuButton("Allergies in Meals", "showAllAllergies"),
                        new MenuBuilder.MenuButton("Search Meal by Name", "showSearchMealByName"),
                        new MenuBuilder.MenuButton("Search Meal by ID", "showMealDetailsById")
                ));
        if (isAdmin) {
            mealMenuButtons.add(new MenuBuilder.MenuButton("Add Meal", "showAddMeal"));
            mealMenuButtons.add(new MenuBuilder.MenuButton("Edit Meal", "showEditMeal"));
            mealMenuButtons.add(new MenuBuilder.MenuButton("Delete Meal", "showDeleteMeal"));
        }
        mealMenuButtons.add(new MenuBuilder.MenuButton("Main Menu", "showMainMenu"));
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
                        new MenuBuilder.MenuButton("Edit My Data", "showEditUserData"),
                        new MenuBuilder.MenuButton("Set My Allergy", "showAllergenSettings"),
                        new MenuBuilder.MenuButton("My Reviews", "showReviewsByUser")
                ));
        if (isAdmin) {
            userMenuButtons.add(new MenuBuilder.MenuButton("All Users", "showAllUsers"));
            userMenuButtons.add(new MenuBuilder.MenuButton("Add User", "showRegisterScreen"));
            userMenuButtons.add(new MenuBuilder.MenuButton("Delete User", "showDeleteUser"));
            userMenuButtons.add(new MenuBuilder.MenuButton("Update User Roles", "showUpdateUserRole"));
        }
        userMenuButtons.add(new MenuBuilder.MenuButton("Main Menu", "showMainMenu"));
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
                new MenuBuilder.MenuButton("All Reviews", "showAllReviews"),
                new MenuBuilder.MenuButton("Add Reviews", "showAddReview"),
                new MenuBuilder.MenuButton("Delete Review", "showDeleteReview"),
                new MenuBuilder.MenuButton("Search Reviews", "showSearchReviewsByMealName"),
                new MenuBuilder.MenuButton("Main Menu", "showMainMenu")
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
                Arrays.asList(
                        new MenuBuilder.MenuButton("Weekly Plan", "showWeeklyPlan")
                ));
        if (isAdmin) {
            weeklyMenuButtons.add(new MenuBuilder.MenuButton("Edit Weekly Plan", "editWeeklyPlan"));
        }
        weeklyMenuButtons.add(new MenuBuilder.MenuButton("Main Menu", "showMainMenu"));
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
        inputScreenBuilder.display(labels, "editMeal");
    }

    /**
     * Displays the update user role input screen.
     */
    public void showUpdateUserRoleScreen() {
        InputScreenBuilder inputScreenBuilder = new InputScreenBuilder(gui, eventManager, "Update User Role");
        List<String> labels = List.of("User ID", "Role");
        inputScreenBuilder.display(labels, "updateUserRole");
    }

    /**
     * Displays the edit user data input screen.
     */
    public void showEditUserDataScreen() {
        InputScreenBuilder inputScreenBuilder = new InputScreenBuilder(gui, eventManager, "Edit User Data");
        List<String> labels = Arrays.asList("Current Password");
        inputScreenBuilder.display(labels, "editUserData");
    }

    /**
     * Displays the edit new user data input screen.
     */
    public void showEditNewUserDataScreen() {
        InputScreenBuilder inputScreenBuilder = new InputScreenBuilder(gui, eventManager, "Edit New User Data");
        List<String> labels = Arrays.asList("New Password", "New Email");
        inputScreenBuilder.display(labels, "editNewUserData");
    }

    /**
     * Displays the registration input screen.
     *
     * @param title the title of the screen.
     * @param event the event to be triggered.
     */
    public void showInputScreenReg(String title, String event) {
        InputScreenBuilder inputScreenBuilder = new InputScreenBuilder(gui, eventManager, title);
        List<String> labels = Arrays.asList("Username", "Password", "Email");
        inputScreenBuilder.display(labels, event);
    }

    /**
     * Displays the add review input screen.
     */
    public void showAddReviewScreen() {
        InputScreenBuilder inputScreenBuilder = new InputScreenBuilder(gui, eventManager, "Add New Review");
        List<String> labels = Arrays.asList("Rating", "Comment", "Meal ID");
        inputScreenBuilder.display(labels, "addReview");
    }

    /**
     * Displays the add meal input screen.
     */
    public void showAddMealScreen() {
        InputScreenBuilder inputScreenBuilder = new InputScreenBuilder(gui, eventManager, "Add New Meal");
        List<String> labels = Arrays.asList("Name", "Price", "Calories", "Allergy", "Meat");
        inputScreenBuilder.display(labels, "addMeal");
    }

    /**
     * Displays the delete meal input screen.
     */
    public void showDeleteMealScreen() {
        InputScreenBuilder inputScreenBuilder = new InputScreenBuilder(gui, eventManager, "Delete Meal by Meal ID");
        List<String> labels = List.of("ID");
        inputScreenBuilder.display(labels, "deleteMeal");
    }

    /**
     * Displays the delete review input screen.
     */
    public void showDeleteReviewScreen() {
        InputScreenBuilder inputScreenBuilder = new InputScreenBuilder(gui, eventManager, "Delete Review by Rating ID");
        List<String> labels = List.of("ID");
        inputScreenBuilder.display(labels, "deleteReview");
    }

    /**
     * Displays the search meal by ID input screen.
     */
    public void showMealDetailsById() {
        InputScreenBuilder inputScreenBuilder = new InputScreenBuilder(gui, eventManager, "Search Meal by ID");
        List<String> labels = List.of("ID");
        inputScreenBuilder.display(labels, "mealDetailsById");
    }

    /**
     * Displays the search meal by name input screen.
     */
    public void showSearchMealByName() {
        InputScreenBuilder inputScreenBuilder = new InputScreenBuilder(gui, eventManager, "Search Meal by Name");
        List<String> labels = List.of("Name");
        inputScreenBuilder.display(labels, "searchMealByName");
    }

    /**
     * Displays the search reviews by meal name input screen.
     */
    public void showSearchReviewsByMealName() {
        InputScreenBuilder inputScreenBuilder = new InputScreenBuilder(gui, eventManager,
                "Search Reviews by Meal Name");
        List<String> labels = List.of("Meal Name");
        inputScreenBuilder.display(labels, "searchReviewsByMealName");
    }

    /**
     * Displays the allergene settings input screen.
     */
    public void showAllergeneSettings() {
        List<String> allergene = Arrays.asList("Gluten-containing cereals", "Crustaceans", "Eggs", "Fish", "Peanuts",
                "Soy", "Milk", "Nuts", "Celery", "Mustard", "Sesame seeds", "Sulfur dioxide and sulfites", "Lupins",
                "Molluscs");
        CheckboxScreenBuilder builder = new CheckboxScreenBuilder(gui, eventManager, "Select Allergens");
        builder.display(allergene, "allergeneSettings");
    }

    /**
     * Displays the delete user input screen.
     */
    public void showDeleteUserScreen() {
        InputScreenBuilder inputScreenBuilder = new InputScreenBuilder(gui, eventManager, "Delete User by User ID");
        List<String> labels = List.of("ID");
        inputScreenBuilder.display(labels, "deleteUser");
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
                .addColumn("Role");
        for (UsersRecord user : users) {
            tableBuilder.addRow(Arrays.asList(String.valueOf(user.getUserid()),
                    user.getUsername(),
                    user.getEmail(),
                    String.valueOf(user.getRole())));
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
                new MenuBuilder.MenuButton("Monday", "editWeeklyPlanMonday"),
                new MenuBuilder.MenuButton("Tuesday", "editWeeklyPlanTuesday"),
                new MenuBuilder.MenuButton("Wednesday", "editWeeklyPlanWednesday"),
                new MenuBuilder.MenuButton("Thursday", "editWeeklyPlanThursday"),
                new MenuBuilder.MenuButton("Friday", "editWeeklyPlanFriday"),
                new MenuBuilder.MenuButton("Reset Weekly Plan", "resetWeeklyPlan"),
                new MenuBuilder.MenuButton("Main Menu", "showMainMenu")
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
        inputScreenBuilder.display(labels, "editWeeklyPlanMondaySubmit");
    }

    /**
     * Displays the input screen for editing Tuesday's meal.
     */
    public void showEditWeeklyPlanTuesday() {
        InputScreenBuilder inputScreenBuilder = new InputScreenBuilder(gui, eventManager, "Edit Tuesday");
        List<String> labels = Arrays.asList("Meal Name");
        inputScreenBuilder.display(labels, "editWeeklyPlanTuesdaySubmit");
    }

    /**
     * Displays the input screen for editing Wednesday's meal.
     */
    public void showEditWeeklyPlanWednesday() {
        InputScreenBuilder inputScreenBuilder = new InputScreenBuilder(gui, eventManager, "Edit Wednesday");
        List<String> labels = Arrays.asList("Meal Name");
        inputScreenBuilder.display(labels, "editWeeklyPlanWednesdaySubmit");
    }

    /**
     * Displays the input screen for editing Thursday's meal.
     */
    public void showEditWeeklyPlanThursday() {
        InputScreenBuilder inputScreenBuilder = new InputScreenBuilder(gui, eventManager, "Edit Thursday");
        List<String> labels = Arrays.asList("Meal Name");
        inputScreenBuilder.display(labels, "editWeeklyPlanThursdaySubmit");
    }

    /**
     * Displays the input screen for editing Friday's meal.
     */
    public void showEditWeeklyPlanFriday() {
        InputScreenBuilder inputScreenBuilder = new InputScreenBuilder(gui, eventManager, "Edit Friday");
        List<String> labels = Arrays.asList("Meal Name");
        inputScreenBuilder.display(labels, "editWeeklyPlanFridaySubmit");
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
            eventManager.notify("error", "Error closing terminal");
        }
    }
}