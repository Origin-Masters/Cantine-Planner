package de.htwsaar.cantineplanner.businessLogic;

import de.htwsaar.cantineplanner.businessLogic.controller.eventdata.EventType;
import de.htwsaar.cantineplanner.codegen.tables.records.MealsRecord;
import de.htwsaar.cantineplanner.codegen.tables.records.ReviewRecord;
import de.htwsaar.cantineplanner.codegen.tables.records.UsersRecord;
import de.htwsaar.cantineplanner.exceptions.*;
import de.htwsaar.cantineplanner.presentation.ScreenManager;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * Controller class for managing the business logic and UI event handling
 * in the Cantine Planner application.
 * <p>
 * This class acts as the central controller, handling events, switching menus,
 * and delegating actions to the appropriate services.
 * </p>
 */
public class Controller {

    // Abhängigkeiten und Statusvariablen
    private final ScreenManager screenManager;
    private final CantineService cantineService;
    private final EventManager eventManager;
    private boolean running;
    private int currentMenu;
    private int currentUserId;

    String PATH_TO_PROPERTIES = "hikari.properties";

    /**
     * Constructs a new Controller instance.
     * <p>
     * Initializes the event manager, screen manager, and cantine service.
     * Also subscribes to various UI and business logic events.
     * </p>
     */
    public Controller() {
        this.eventManager = new EventManager();
        this.cantineService = new CantineService(PATH_TO_PROPERTIES);
        this.screenManager = new ScreenManager(eventManager, cantineService);
        this.currentUserId = -1;
        this.running = false;
        //subscribeToEvents();
    }

    /**
     * Subscribes to all relevant application events.
     * <p>
     * Groups subscriptions by themes such as navigation, user authentication,
     * meal-related, review-related, user-related, and weekly plan events.
     * </p>
    private void subscribeToEvents() {
        // Navigation und allgemeine Nachrichten
        eventManager.subscribe("switchMenu", (data) -> switchMenu((int) data));
//        eventManager.subscribe("showMainMenu", (data) -> switchMenu(1));
//        eventManager.subscribe("showMealMenu", (data) -> switchMenu(2));
//        eventManager.subscribe("showReviewMenu", (data) -> switchMenu(3));
//        eventManager.subscribe("showUserMenu", (data) -> switchMenu(4));
//        eventManager.subscribe("showWeeklyMenu", (data) -> switchMenu(5));
        eventManager.subscribe("exit", (data) -> exitApplication());
        eventManager.subscribe("success", (data) -> screenManager.showSuccessScreen((String) data));
        eventManager.subscribe("error", (data) -> screenManager.showErrorScreen((String) data));

        // User-Authentifizierung und Registrierung
        eventManager.subscribe("login", this::handleLogin);
        eventManager.subscribe("register", this::handleRegister);
        eventManager.subscribe("showRegisterScreen", this::handleShowRegisterScreen);
        eventManager.subscribe("logout", (data) -> {
            switchMenu(0);
            this.currentUserId = -1;
        });


        // Meal-bezogene Events
        eventManager.subscribe("showAllMeals", this::handleShowAllMeals);
        eventManager.subscribe("showSortMeals", (data) -> screenManager.showSortMealScreen());

        eventManager.subscribe("sortMealByPrice", this::handleSortMealByPrice);
        eventManager.subscribe("sortMealByRating", this::handleSortMealByRating);
        eventManager.subscribe("sortMealByName", this::handleSortMealByName);
        eventManager.subscribe("sortMealByAllergies", this::handleSortMealByAllergy);
        eventManager.subscribe("sortMealByCalories", this::handleSortMealByCalories);

        eventManager.subscribe("showAllAllergies", this::handleShowAllAllergies);
        eventManager.subscribe("showAddMeal", (data) -> screenManager.showAddMealScreen());
        eventManager.subscribe("showDeleteMeal", (data) -> screenManager.showDeleteMealScreen());
        eventManager.subscribe("addMeal", this::handleAddMeal);
        eventManager.subscribe("deleteMeal", this::handleDeleteMeal);
        eventManager.subscribe("showMealDetailsById", (data) -> screenManager.showMealDetailsById());
        eventManager.subscribe("mealDetailsById", this::handleShowMealById);
        eventManager.subscribe("showSearchMealByName", (data) -> screenManager.showSearchMealByName());
        eventManager.subscribe("searchMealByName", this::handleShowMealByName);
        eventManager.subscribe("showEditMeal", (data) -> screenManager.showEditMealScreen());
        eventManager.subscribe("editMeal", this::handleEditMeal);

        // Review-bezogene Events
        eventManager.subscribe("showAddReview", (data) -> screenManager.showAddReviewScreen());
        eventManager.subscribe("addReview", this::handleAddReview);
        eventManager.subscribe("showDeleteReview", (data) -> screenManager.showDeleteReviewScreen());
        eventManager.subscribe("deleteReview", this::handleDeleteReview);
        eventManager.subscribe("showAllReviews", this::handleShowAllReviews);
        eventManager.subscribe("showSearchReviewsByMealName", (data) -> screenManager.showSearchReviewsByMealName());
        eventManager.subscribe("searchReviewsByMealName", this::handleSearchReviewsByMealName);

        // User-bezogene Events
        eventManager.subscribe("showEditUserData", (data) -> screenManager.showEditUserDataScreen());
        eventManager.subscribe("editUserData", this::handleEditUserData);
        eventManager.subscribe("showEditNewUserData", (data) -> screenManager.showEditNewUserDataScreen());
        eventManager.subscribe("editNewUserData", this::handleInputNewUserData);
        eventManager.subscribe("showReviewsByUser", this::handleShowReviewsByUser);
        eventManager.subscribe("showAdminMenu", this::handleShowAdminMenu);
        eventManager.subscribe("showAllergenSettings", (data) -> screenManager.showAllergeneSettings());
        eventManager.subscribe("allergeneSettings", this::handleAllergeneSettings);
        eventManager.subscribe("showAllUsers", this::handleAllUser);
        eventManager.subscribe("showDeleteUser", (data) -> screenManager.showDeleteUserScreen());
        eventManager.subscribe("deleteUser", this::handleDeleteUser);
        eventManager.subscribe("showUpdateUserRole", (data) -> screenManager.showUpdateUserRoleScreen());
        eventManager.subscribe("updateUserRole", this::handleUpdateUserRole);
        // Weekly Plan
        eventManager.subscribe("showWeeklyPlan", this::handleShowWeeklyPlan);
        eventManager.subscribe("editWeeklyPlan", this::handleShowEditWeeklyPlan);
        eventManager.subscribe("resetWeeklyPlan", this::handleResetWeeklyPlan);
        eventManager.subscribe("editWeeklyPlanMonday", (data) -> screenManager.showEditWeeklyPlanMonday());
        eventManager.subscribe("editWeeklyPlanMondaySubmit", this::handleEditWeeklyPlanMonday);
        eventManager.subscribe("editWeeklyPlanTuesday", (data) -> screenManager.showEditWeeklyPlanTuesday());
        eventManager.subscribe("editWeeklyPlanTuesdaySubmit", this::handleEditWeeklyPlanTuesday);
        eventManager.subscribe("editWeeklyPlanWednesday", (data) -> screenManager.showEditWeeklyPlanWednesday());
        eventManager.subscribe("editWeeklyPlanWednesdaySubmit", this::handleEditWeeklyPlanWednesday);
        eventManager.subscribe("editWeeklyPlanThursday", (data) -> screenManager.showEditWeeklyPlanThursday());
        eventManager.subscribe("editWeeklyPlanThursdaySubmit", this::handleEditWeeklyPlanThursday);
        eventManager.subscribe("editWeeklyPlanFriday", (data) -> screenManager.showEditWeeklyPlanFriday());
        eventManager.subscribe("editWeeklyPlanFridaySubmit", this::handleEditWeeklyPlanFriday);
    }
     */


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
    public void loginMenu() {
        screenManager.showLoginScreen();
    }

    /**
     * Displays the main menu screen.
     */
    public void mainMenu() {
        screenManager.showMainMenuScreen();
    }

    /**
     * Displays the meal menu screen.
     * <p>
     * Determines if the current user has admin privileges to customize the display.
     * </p>
     */
    public void mealMenu()  {
        try{
            screenManager.showMealMenuScreen(cantineService.isAdmin(currentUserId));
        } catch (SQLException | UserDoesntExistException e) {
            screenManager.showErrorScreen("There was an error while validating the user try again!");
        }
    }

    /**
     * Displays the review menu screen.
     */
    public void reviewMenu() {
        screenManager.showReviewsMenu();
    }

    /**
     * Displays the user menu screen.
     * <p>
     * Checks if the current user is an admin to determine appropriate view.
     * </p>
     */
    public void userMenu() {
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
    public void weeklyMenu() {
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

    // --- Event-Handler ---

    // User-Handler

    /**
     * Handles the login event.
     * <p>
     * Validates the user credentials and logs the user in if valid.
     * </p>
     *
     * @param data an Object array containing username and password as Strings
     */
    public void handleLogin(Object data) {
        String[] credentials = (String[]) data;
        String username = credentials[0];
        String password = credentials[1];
        try {
            if (cantineService.validateUser(username, password)) {
                currentUserId = cantineService.getUserId(username);
                screenManager.closeActiveWindow();
                screenManager.showSuccessScreen("Login successful!");
                switchMenu(1);
            }
        } catch (UserNotValidatedException e) {
            screenManager.showErrorScreen(e.getMessage());
        } catch (SQLException e) {
            screenManager.showErrorScreen("There was an error while logging in please try again!");
        }
    }

    /**
     * Handles user registration.
     * <p>
     * Registers a new user with the provided credentials if all fields are filled.
     * </p>
     *
     * @param data an Object array containing username, password, and email as Strings
     */
    public void handleRegister(Object data) {
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

        try {
            if (cantineService.registerUser(username, password, email)) {
                screenManager.closeActiveWindow();
                screenManager.showSuccessScreen("Registration successful!");
                switchMenu(0);
            }

        } catch (InvalidEmailTypeException | UserAlreadyExistsException e) {
            screenManager.showErrorScreen(e.getMessage());
        } catch (SQLException e) {
            screenManager.showErrorScreen("There was an error while registering please try again!");
        }
    }

    /**
     * Displays the registration input screen.
     *
     * @param data not used
     */
    private void handleShowRegisterScreen(Object data) {
        screenManager.showInputScreenReg("Register", EventType.REGISTER);
    }

    /**
     * Checks if any provided field is empty.
     *
     * @param fields variable number of String fields
     * @return true if any field is empty, false otherwise
     */
    private boolean isAnyFieldEmpty(String... fields) {
        for (String field : fields) {
            if (Objects.equals(field, "")) {
                return true;
            }
        }
        return false;
    }

    /**
     * Handles user data editing after verifying current password.
     * <p>
     * Validates the current password and then displays the screen to update user data.
     * </p>
     *
     * @param data an Object array containing the current password as a String
     */
    private void handleEditUserData(Object data) {
        if (data == null) {
            screenManager.showErrorScreen("Data is null");
            return;
        }
        String[] userData = (String[]) data;
        String currentPassword = userData[0];

        try {
            if (cantineService.validateUser(currentUserId, currentPassword)) {
                screenManager.closeActiveWindow();
                screenManager.showSuccessScreen("User validated!");
                screenManager.showEditNewUserDataScreen();
            }
        } catch (UserNotValidatedException e) {
            screenManager.showErrorScreen(e.getMessage());
        } catch (SQLException e) {
            screenManager.showErrorScreen("There was an error while validating user data, please try again!");
        }
    }

    /**
     * Handles updating the user data (password and email).
     * <p>
     * Validates input and updates the current user's data.
     * </p>
     *
     * @param data an Object array containing new password and new email as Strings
     */
    private void handleInputNewUserData(Object data) {
        if (data == null) {
            screenManager.showErrorScreen("Data is null");
            return;
        }
        String[] userData = (String[]) data;
        String newPassword = userData[0];
        String newEmail = userData[1];

        if (isAnyFieldEmpty(newPassword)) {
            screenManager.showErrorScreen("New password is required!");
            return;
        }

        try {
            cantineService.editUserData(currentUserId, newPassword, newEmail);
            screenManager.closeActiveWindow();
            screenManager.showSuccessScreen("Password and email updated successfully!");
        } catch (InvalidEmailTypeException e) {
            screenManager.showErrorScreen(e.getMessage());
        } catch (SQLException e) {
            screenManager.showErrorScreen("There was an error while updating password and email, please try again!");
        }
    }

    // Meal-Handler

    /**
     * Handles editing a meal.
     * <p>
     * Validates input data and updates the meal information accordingly.
     * </p>
     *
     * @param data an Object array containing meal details; the first element must be meal ID
     */
    public void handleEditMeal(Object data) {
        try {
            String[] mealData = (String[]) data;

            // Check if at least the meal ID is provided
            if (mealData.length < 1 || mealData[0].isEmpty()) {
                screenManager.showErrorScreen("Meal ID is required!");
                return;
            }

            int mealId = Integer.parseInt(mealData[0]);
            MealsRecord meal = new MealsRecord();
            meal.setMealId(mealId);

            // If a value exists and is not empty, set it
            if (mealData.length > 1 && !mealData[1].isEmpty()) {
                meal.setName(mealData[1]);
            }
            if (mealData.length > 2 && !mealData[2].isEmpty()) {
                meal.setPrice(Float.parseFloat(mealData[2]));
            }
            if (mealData.length > 3 && !mealData[3].isEmpty()) {
                meal.setCalories(Integer.parseInt(mealData[3]));
            }
            if (mealData.length > 4 && !mealData[4].isEmpty()) {
                meal.setAllergy(mealData[4]);
            }
            if (mealData.length > 5 && !mealData[5].isEmpty()) {
                meal.setMeat(Integer.parseInt(mealData[5]));
            }

            cantineService.editMeal(meal);
            screenManager.closeActiveWindow();
            screenManager.showSuccessScreen("Meal updated successfully!");
        } catch (NumberFormatException e) {
            screenManager.showErrorScreen("Invalid input format!");
        } catch (MealDoesntExistException e){
            screenManager.showErrorScreen(e.getMessage());
        } catch (SQLException e) {
            screenManager.showErrorScreen("There was an error while updating meal please try again!");
        }
    }

    /**
     * Handles the event to display all meals.
     * <p>
     * Retrieves a list of all meals and passes it to the screen manager for display.
     * </p>
     *
     * @param data not used
     */
    public void handleShowAllMeals(Object data) {
        try {
            List<MealsRecord> meals = cantineService.getAllMeals();
            screenManager.showAllMeals(meals);
        } catch (SQLException e) {
            screenManager.showErrorScreen("There was an error while fetching all meals please try again!");
        }
    }

    private void handleSortMealByPrice(Object data) {
        try{
            List<MealsRecord> meals = cantineService.sortMealsByPrice();
            screenManager.showAllMeals(meals);
        } catch (SQLException e) {
            screenManager.showErrorScreen("There was an error while sorting meals by price please try again!");
        }
    }

    private void handleSortMealByRating(Object data) {
        try{
            List<MealsRecord> meals = cantineService.sortMealsByRating();
            screenManager.showAllMeals(meals);
        } catch (SQLException e) {
            screenManager.showErrorScreen("There was an error while sorting meals by rating please try again!");
        }
    }

    private void handleSortMealByName(Object data) {
        try{
            List<MealsRecord> meals = cantineService.sortMealsByName();
            screenManager.showAllMeals(meals);
        } catch (SQLException e) {
            screenManager.showErrorScreen("There was an error while sorting meals by name please try again!");
        }
    }

    private void handleSortMealByCalories(Object data) {
        try{
            List<MealsRecord> meals = cantineService.sortMealsByCalories();
            screenManager.showAllMeals(meals);
        } catch (SQLException e) {
            screenManager.showErrorScreen("There was an error while sorting meals by calories please try again!");
        }
    }

    private void handleSortMealByAllergy(Object data) {
        try{
            List<MealsRecord> meals = cantineService.sortMealsByAllergy(currentUserId);
            screenManager.showAllMeals(meals);
        } catch (SQLException e) {
            screenManager.showErrorScreen("There was an error while sorting meals by allergy please try again!");
        }
    }

    /**
     * Handles displaying all allergy information.
     * <p>
     * Retrieves a list of all allergies from meals and displays them.
     * </p>
     *
     * @param data not used
     */
    private void handleShowAllAllergies(Object data) {
        try {
            List<MealsRecord> allergies = cantineService.getAllAllergies();
            screenManager.showAllAllergies(allergies);
        } catch (SQLException e) {
            screenManager.showErrorScreen("There was an error while fetching all allergies please try again!");
        }
    }

    /**
     * Handles adding a new meal.
     * <p>
     * Parses the meal details from input data and creates a new meal record.
     * </p>
     *
     * @param data an Object array containing meal name, price, calories, allergy info, and meat flag
     */
    public void handleAddMeal(Object data) {
        try {
            String[] mealData = (String[]) data;
            String mealName = mealData[0];
            float price = Float.parseFloat(mealData[1]);
            int calories = Integer.parseInt(mealData[2]);
            String allergy = mealData[3];
            int meat = Integer.parseInt(mealData[4]);

            MealsRecord meal = new MealsRecord();
            meal.setName(mealName);
            meal.setPrice(price);
            meal.setCalories(calories);
            meal.setAllergy(allergy);
            meal.setMeat(meat);
            cantineService.addMeal(meal);
            screenManager.closeActiveWindow();
            screenManager.showSuccessScreen("Meal added successfully!");
        } catch (NumberFormatException e) {
            screenManager.showErrorScreen("Invalid input format!");
        } catch (MealAlreadyExistsException e) {
            screenManager.showErrorScreen(e.getMessage());
        } catch (SQLException e) {
            screenManager.showErrorScreen("There was an error while adding meal please try again!");
        }
    }

    /**
     * Handles deleting a meal.
     * <p>
     * Parses the meal ID from input data and deletes the corresponding meal.
     * </p>
     *
     * @param data an Object array where the first element is the meal ID as a String
     */
    private void handleDeleteMeal(Object data) {
        try {
            String[] dataArray = (String[]) data;
            int mealId = Integer.parseInt(dataArray[0]);
            cantineService.deleteMeal(mealId);
            screenManager.closeActiveWindow();
            screenManager.showSuccessScreen("Meal deleted successfully!");
        } catch (NumberFormatException e) {
            screenManager.showErrorScreen("Invalid meal ID format!");
        } catch (MealiDNotFoundException e) {
            screenManager.showErrorScreen(e.getMessage());
        } catch (SQLException e) {
            screenManager.showErrorScreen("There was an error while deleting meal please try again!");
        }
    }

    /**
     * Handles displaying meal details by meal ID.
     * <p>
     * Retrieves and displays meal details based on the provided meal ID.
     * </p>
     *
     * @param data an Object array where the first element is the meal ID as a String
     */
    private void handleShowMealById(Object data) {
        try {
            String[] dataArray = (String[]) data;
            int mealId = Integer.parseInt(dataArray[0]);
            MealsRecord meal = cantineService.getMealById(mealId);
            if (meal != null) {
                screenManager.showMealDetails(meal);
            }
        } catch (NumberFormatException e) {
            screenManager.showErrorScreen("Invalid meal ID format!");
        } catch (MealiDNotFoundException e) {
            screenManager.showErrorScreen(e.getMessage());
        } catch (SQLException e) {
            screenManager.showErrorScreen("There was an error while fetching meal please try again!");
        }
    }

    /**
     * Handles displaying meal details by meal name.
     * <p>
     * Retrieves and displays meal details based on the provided meal name.
     * </p>
     *
     * @param data an Object array where the first element is the meal name as a String
     */
    private void handleShowMealByName(Object data) {
        try {
            String[] dataArray = (String[]) data;
            String mealName = dataArray[0];
            MealsRecord meal = cantineService.getMealByName(mealName);
            if (meal != null) {
                screenManager.showMealDetails(meal);
            }
        } catch (MealDoesntExistException e) {
            screenManager.showErrorScreen(e.getMessage());
        } catch (SQLException e) {
            screenManager.showErrorScreen("There was an error while fetching meal please try again!");
        }
    }

    // Review-Handler

    /**
     * Handles adding a review for a meal.
     * <p>
     * Parses review data, creates a new review record, and adds it.
     * </p>
     *
     * @param data an Object array containing rating, comment, and meal ID as Strings
     */
    public void handleAddReview(Object data) {
        try {
            String[] reviewData = (String[]) data;
            int mealId = Integer.parseInt(reviewData[2]);
            int rating = Integer.parseInt(reviewData[0]);
            String comment = reviewData[1];

            if (rating < 0 || rating > 5) {
                screenManager.showErrorScreen("Rating must be between 0 and 5.");
                return;
            }

            ReviewRecord review = new ReviewRecord();
            review.setMealId(mealId);
            review.setRating(rating);
            review.setComment(comment);
            review.setUserid(currentUserId);
            cantineService.addReview(review);
            screenManager.closeActiveWindow();
            screenManager.showSuccessScreen("Review added successfully!");
        } catch (NumberFormatException e) {
            screenManager.showErrorScreen("Invalid input format!");
        } catch (SQLException e) {
            screenManager.showErrorScreen("There was an error while adding review please try again!");
        }
    }

    /**
     * Handles deleting a review.
     * <p>
     * Parses the review ID from input data and deletes the corresponding review.
     * </p>
     *
     * @param data an Object array where the first element is the review ID as a String
     */
    private void handleDeleteReview(Object data) {
        try {
            String[] reviewData = (String[]) data;
            int reviewId = Integer.parseInt(reviewData[0]);
            int reviewUserId = cantineService.getUserIdFromReviewId(reviewId);
            // Check if the current user is admin
            boolean isAdmin = cantineService.isAdmin(currentUserId);
            // If not admin and review's user id does not match, reject deletion
            if (!isAdmin && reviewUserId != currentUserId) {
                screenManager.showErrorScreen("Unauthorized: You can only delete your own reviews.");
                return;
            }

            // Authorized to delete review
            cantineService.deleteReview(reviewId);
            screenManager.showSuccessScreen("Review deleted successfully!");
        } catch (NumberFormatException e) {
            screenManager.showErrorScreen("Invalid review ID format!");
        } catch (ReviewiDDoesntExistException e) {
            screenManager.showErrorScreen(e.getMessage());
        } catch ( SQLException e) {
            screenManager.showErrorScreen("Error deleting review, please try again!");
        }
    }

    /**
     * Handles displaying all reviews.
     * <p>
     * Retrieves a list of all reviews and displays them.
     * </p>
     *
     * @param data not used
     */
    private void handleShowAllReviews(Object data) {
        try {
            List<ReviewRecord> reviews = cantineService.getAllReviews();
            screenManager.showAllReviews(reviews);
        } catch (SQLException e) {
            screenManager.showErrorScreen("There was an error while showing all Reviews please try again!");
        }
    }

    /**
     * Handles searching reviews by meal name.
     * <p>
     * Retrieves reviews corresponding to the given meal name and displays them.
     * </p>
     *
     * @param data an Object array where the first element is the meal name as a String
     */
    private void handleSearchReviewsByMealName(Object data) {
        try {
            String[] dataArray = (String[]) data;
            String mealName = dataArray[0];
            List<ReviewRecord> reviews = cantineService.searchReviewsByMealName(mealName);
            screenManager.showAllReviews(reviews);
        } catch (MealDoesntExistException e) {
            screenManager.showErrorScreen(e.getMessage());
        } catch (SQLException e) {
            screenManager.showErrorScreen("There was an error while searching for the Reviews please try again!");
        }
    }

    // User-Handler

    /**
     * Handles updating a user's role.
     * <p>
     * Parses user ID and role from input data and updates the user's role.
     * </p>
     *
     * @param data an Object array containing user ID and role as Strings
     */
    private void handleUpdateUserRole(Object data) {
        try {
            String[] dataArray = (String[]) data;
            int userId = Integer.parseInt(dataArray[0]);
            int role = Integer.parseInt(dataArray[1]);
            cantineService.updateUserRole(userId, role);
            screenManager.closeActiveWindow();
            screenManager.showSuccessScreen("User role updated successfully!");
        } catch (NumberFormatException e) {
            screenManager.showErrorScreen("Invalid user ID or role format!");
        } catch (SQLException e) {
            screenManager.showErrorScreen("There was an error while updating user role please try again!");
        }
    }

    /**
     * Handles displaying all reviews made by the current user.
     *
     * @param data not used
     */
    private void handleShowReviewsByUser(Object data) {
        try {
            List<ReviewRecord> reviews = cantineService.getAllReviewsByUser(currentUserId);
            screenManager.showAllReviews(reviews);
        } catch (UseriDDoesntExcistException e) {
            screenManager.showErrorScreen(e.getMessage());
        } catch (SQLException e) {
            screenManager.showErrorScreen("There was an error while fetching all reviews please try again!");
        }
    }

    /**
     * Handles updating allergen settings for the current user.
     * <p>
     * Parses the allergen settings from input data and updates them.
     * </p>
     *
     * @param data an Object array containing allergen settings as Strings
     */
    private void handleAllergeneSettings(Object data){
        try {
            cantineService.setAllergeneSettings(currentUserId, Arrays.toString((String[]) data));
            screenManager.closeActiveWindow();
            screenManager.showSuccessScreen("Allergene settings updated successfully!");
        } catch (SQLException e) {
            screenManager.showErrorScreen("There was an error while setting the allergene settings, please try again!");
        }
    }

    /**
     * Handles showing the admin menu.
     * <p>
     * Verifies if the current user has admin privileges and switches to the admin menu if so.
     * </p>
     *
     * @param data not used
     */
    private void handleShowAdminMenu(Object data) {
        try {
            if (cantineService.isAdmin(currentUserId)) {
                switchMenu(6);
            } else {
                screenManager.showErrorScreen("You are not authorized to access this menu!");
            }
        } catch (SQLException | UserDoesntExistException e) {
            screenManager.showErrorScreen("There was an error while validating the user try again!");
        }
    }

    /**
     * Handles displaying all users.
     * <p>
     * Retrieves a list of all users and displays them.
     * </p>
     *
     * @param data not used
     */
    private void handleAllUser(Object data) {
        try{
            List<UsersRecord> users = cantineService.getAllUser();
            screenManager.showAllUser(users);
        } catch (SQLException e) {
            screenManager.showErrorScreen("There was an error while fetching all users please try again!");
        }
    }

    /**
     * Handles deleting a user.
     * <p>
     * Parses the user ID from input data and deletes the corresponding user.
     * </p>
     *
     * @param data an Object array where the first element is the user ID as a String
     */
    private void handleDeleteUser(Object data) {
        try {
            String[] dataArray = (String[]) data;
            int userId = Integer.parseInt(dataArray[0]);
            cantineService.deleteUser(userId);
            screenManager.closeActiveWindow();
            screenManager.showSuccessScreen("User deleted successfully!");
        } catch (NumberFormatException e) {
            screenManager.showErrorScreen("Invalid user ID format!");
        } catch (UseriDDoesntExcistException e) {
            screenManager.showErrorScreen(e.getMessage());
        } catch (SQLException e) {
            screenManager.showErrorScreen("There was an error while deleting user please try again!");
        }
    }

    // Weekly Plan

    /**
     * Handles displaying the weekly meal plan.
     * <p>
     * Retrieves the weekly plan, sorts it by day, and displays it.
     * </p>
     *
     * @param data not used
     */
    private void handleShowWeeklyPlan(Object data) {
        try {
            List<MealsRecord> weeklyPlan = cantineService.getWeeklyPlan();
            // Sort the weeklyPlan by the day field
            weeklyPlan.sort(Comparator.comparing(meal -> {
                switch (meal.getDay()) {
                    case "Mon":
                        return 1;
                    case "Tue":
                        return 2;
                    case "Wed":
                        return 3;
                    case "Thu":
                        return 4;
                    case "Fri":
                        return 5;
                    default:
                        return 6;
                }
            }));
            screenManager.showWeeklyPlanScreen(weeklyPlan);
        } catch (SQLException e) {
            screenManager.showErrorScreen("There was an error while fetching the weekly plan, please try again!");
        }
    }

    /**
     * Handles displaying the edit weekly plan screen.
     *
     * @param data not used
     */
    private void handleShowEditWeeklyPlan(Object data) {
        screenManager.showEditWeeklyPlanScreen();
    }

    /**
     * Handles resetting the weekly meal plan to its default state.
     *
     * @param data not used
     */
    private void handleResetWeeklyPlan(Object data) {
        try {
            cantineService.resetWeeklyPlan();
            screenManager.showSuccessScreen("Weekly plan has been reset!");
        } catch (SQLException e) {
            screenManager.showErrorScreen("There was an error while resetting the weekly plan, please try again!");
        }
    }

    /**
     * Handles editing the meal for Monday in the weekly plan.
     * <p>
     * Updates the meal for Monday based on the provided meal name.
     * </p>
     *
     * @param data an Object array where the first element is the meal name as a String
     */
    public void handleEditWeeklyPlanMonday(Object data) {
        String[] mealData = (String[]) data;
        String mealName = mealData[0];
        try {
            cantineService.editWeeklyPlan(mealName, "Mon");
            screenManager.closeActiveWindow();
            screenManager.showSuccessScreen("Meal updated for Monday!");
        } catch (SQLException | MealDoesntExistException e) {
            screenManager.showErrorScreen("Error updating meal for Monday: " + e.getMessage());
        }
    }

    /**
     * Handles editing the meal for Tuesday in the weekly plan.
     * <p>
     * Updates the meal for Tuesday based on the provided meal name.
     * </p>
     *
     * @param data an Object array where the first element is the meal name as a String
     */
    public void handleEditWeeklyPlanTuesday(Object data) {
        String[] mealData = (String[]) data;
        String mealName = mealData[0];
        try {
            cantineService.editWeeklyPlan(mealName, "Tue");
            screenManager.closeActiveWindow();
            screenManager.showSuccessScreen("Meal updated for Tuesday!");
        } catch (SQLException | MealDoesntExistException e) {
            screenManager.showErrorScreen("Error updating meal for Tuesday: " + e.getMessage());
        }
    }

    /**
     * Handles editing the meal for Wednesday in the weekly plan.
     * <p>
     * Updates the meal for Wednesday based on the provided meal name.
     * </p>
     *
     * @param data an Object array where the first element is the meal name as a String
     */
    public void handleEditWeeklyPlanWednesday(Object data) {
        String[] mealData = (String[]) data;
        String mealName = mealData[0];
        try {
            cantineService.editWeeklyPlan(mealName, "Wed");
            screenManager.closeActiveWindow();
            screenManager.showSuccessScreen("Meal updated for Wednesday!");
        } catch (SQLException | MealDoesntExistException e) {
            screenManager.showErrorScreen("Error updating meal for Wednesday: " + e.getMessage());
        }
    }

    /**
     * Handles editing the meal for Thursday in the weekly plan.
     * <p>
     * Updates the meal for Thursday based on the provided meal name.
     * </p>
     *
     * @param data an Object array where the first element is the meal name as a String
     */
    public void handleEditWeeklyPlanThursday(Object data) {
        String[] mealData = (String[]) data;
        String mealName = mealData[0];
        try {
            cantineService.editWeeklyPlan(mealName, "Thu");
            screenManager.closeActiveWindow();
            screenManager.showSuccessScreen("Meal updated for Thursday!");
        } catch (SQLException | MealDoesntExistException e) {
            screenManager.showErrorScreen("Error updating meal for Thursday: " + e.getMessage());
        }
    }

    /**
     * Handles editing the meal for Friday in the weekly plan.
     * <p>
     * Updates the meal for Friday based on the provided meal name.
     * </p>
     *
     * @param data an Object array where the first element is the meal name as a String
     */
    public void handleEditWeeklyPlanFriday(Object data) {
        String[] mealData = (String[]) data;
        String mealName = mealData[0];
        try {
            cantineService.editWeeklyPlan(mealName, "Fri");
            screenManager.closeActiveWindow();
            screenManager.showSuccessScreen("Meal updated for Friday!");
        } catch (SQLException | MealDoesntExistException e) {
            screenManager.showErrorScreen("Error updating meal for Friday: " + e.getMessage());
        }
    }
}
/*







█████████████████████████████████████████████████████████████████████████████████████████████████████████
█████   ████████████   ██████████████████████████████████████████████████████████████████████████████████
█████   ████████████   ██████████████████████████████████████████████████████████████████████████████████
█████   █     ████        ██   ████   █████  █████████       ██████        ██████       ██████  █    ████
█████     ██    ████   ██████  ████    ███   ████████   ████   ███   ████   ████   ███    ████       ████
█████   █████   ████   ██████   ██      ██   ████████   █████████████████   ███████████   ████   ████████
█████   █████   ████   ███████  ██  ██  █   ██████████        ████          ████          ████   ████████
█████   █████   ████   ███████     ███      ████████████████   ██   █████   ███   █████   ████   ████████
█████   █████   ████       ████    ████    ██████████   ████   ██    ██     ███   ███     ████   ████████
█████   █████   ██████     ████   ██████   ███████████       ██████     ██   ███      ██   ███   ████████
█████████████████████████████████████████████████████████████████████████████████████████████████████████

 */
