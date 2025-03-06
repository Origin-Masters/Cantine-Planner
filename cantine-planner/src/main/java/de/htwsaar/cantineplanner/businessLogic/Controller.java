package de.htwsaar.cantineplanner.businessLogic;

import de.htwsaar.cantineplanner.codegen.tables.records.MealsRecord;
import de.htwsaar.cantineplanner.codegen.tables.records.ReviewRecord;
import de.htwsaar.cantineplanner.codegen.tables.records.UsersRecord;
import de.htwsaar.cantineplanner.dbUtils.DataBaseUtil;
import de.htwsaar.cantineplanner.exceptions.*;
import de.htwsaar.cantineplanner.presentation.ScreenManager;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class Controller {

    // Abhängigkeiten und Statusvariablen
    private final ScreenManager screenManager;
    private final CantineService cantineService;
    private final EventManager eventManager;
    private boolean running;
    private int currentMenu;
    private int currentUserId;

    String PATH_TO_PROPERTIES = "hikari.properties";


    public Controller() {
        this.eventManager = new EventManager();
        this.screenManager = new ScreenManager(eventManager);
        this.cantineService = new CantineService(PATH_TO_PROPERTIES);
        this.currentUserId = -1;
        this.running = false;
        subscribeToEvents();
    }

    /**
     * Registriert alle Events – gruppiert nach Themenbereichen.
     */
    private void subscribeToEvents() {
        // Navigation und allgemeine Nachrichten
        eventManager.subscribe("showMainMenu", (data) -> switchMenu(1));
        eventManager.subscribe("showMealMenu", (data) -> switchMenu(2));
        eventManager.subscribe("showReviewMenu", (data) -> switchMenu(3));
        eventManager.subscribe("showUserMenu", (data) -> switchMenu(4));
        eventManager.subscribe("showWeeklyMenu", (data) -> switchMenu(5));
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


    // Hauptschleife
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
                case 6:
                    adminMenu();
                    break;
                default:
            }
        }
    }

    // Menü-Navigation
    private void switchMenu(int menu) {
        screenManager.closeActiveWindow();
        currentMenu = menu;
    }

    public void loginMenu() {
        screenManager.showLoginScreen();
    }

    public void mainMenu() {
        screenManager.showMainMenuScreen();
    }

    public void mealMenu()  {
        try{
            screenManager.showMealMenuScreen(cantineService.isAdmin(currentUserId));
        } catch (SQLException | UserDoesntExistException e) {
            screenManager.showErrorScreen("There was an error while validating the user try again!");
        }
    }

    public void reviewMenu() {
        screenManager.showReviewsMenu();
    }

    public void userMenu() {
        try{
            screenManager.showUserMenuScreen(cantineService.isAdmin(currentUserId));
        } catch (SQLException | UserDoesntExistException e) {
            screenManager.showErrorScreen("There was an error while validating the user try again!");
        }
    }

    public void weeklyMenu() {
        screenManager.showWeeklyMenuScreen();
    }

    public void adminMenu() {
        screenManager.showAdminMenuScreen();
    }

    // Beenden
    private void exitApplication() {
        screenManager.closeTerminal();
        running = false;
    }

    // --- Event-Handler ---

    // User-Handler
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


    private void handleShowRegisterScreen(Object data) {
        screenManager.showInputScreenReg("Register", "register");
    }

    private boolean isAnyFieldEmpty(String... fields) {
        for (String field : fields) {
            if (Objects.equals(field, "")) {
                return true;
            }
        }
        return false;
    }

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
    public void handleShowAllMeals(Object data) {
        try {
            List<MealsRecord> meals = cantineService.getAllMeals();
            screenManager.showAllMeals(meals);
        } catch (SQLException e) {
            screenManager.showErrorScreen("There was an error while fetching all meals please try again!");
        }
    }

    private void handleShowAllAllergies(Object data) {
        try {
            List<MealsRecord> allergies = cantineService.getAllAllergies();
            screenManager.showAllAllergies(allergies);
        } catch (SQLException e) {
            screenManager.showErrorScreen("There was an error while fetching all allergies please try again!");
        }
    }

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
        } catch (MealDoesntExistException e) {
            screenManager.showErrorScreen(e.getMessage());
        } catch (SQLException e) {
            screenManager.showErrorScreen("There was an error while fetching meal please try again!");
        }
    }

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
    public void handleAddReview(Object data) {
        try {
            String[] reviewData = (String[]) data;
            int mealId = Integer.parseInt(reviewData[2]);
            int rating = Integer.parseInt(reviewData[0]);
            String comment = reviewData[1];

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

    private void handleDeleteReview(Object data) {
        try {
            String[] dataArray = (String[]) data;
            int ratingId = Integer.parseInt(dataArray[0]);
            cantineService.deleteReview(ratingId);
            screenManager.closeActiveWindow();
            screenManager.showSuccessScreen("Review deleted successfully!");
        } catch (NumberFormatException e) {
            screenManager.showErrorScreen("Invalid review ID format!");
        } catch (ReviewiDDoesntExistException e) {
            screenManager.showErrorScreen(e.getMessage());
        } catch (SQLException e) {
            screenManager.showErrorScreen("There was an error while deleting review please try again!");
        }
    }

    private void handleShowAllReviews(Object data) {
        try {
            List<ReviewRecord> reviews = cantineService.getAllReviews();
            screenManager.showAllReviews(reviews);
        } catch (SQLException e) {
            screenManager.showErrorScreen("There was an error while showing all Reviews please try again!");
        }
    }


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
    private void handleAllergeneSettings(Object data){
        try {
            cantineService.setAllergeneSettings(currentUserId, Arrays.toString((String[]) data));
            screenManager.closeActiveWindow();
            screenManager.showSuccessScreen("Allergene settings updated successfully!");
        } catch (SQLException e) {
            screenManager.showErrorScreen("There was an error while setting the allergene settings, please try again!");
        }
    }
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
    private void handleAllUser(Object data) {
        try{
            List<UsersRecord> users = cantineService.getAllUser();
            screenManager.showAllUser(users);
        } catch (SQLException e) {
            screenManager.showErrorScreen("There was an error while fetching all users please try again!");
        }
    }
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

    private void handleShowEditWeeklyPlan(Object data) {
        screenManager.showEditWeeklyPlanScreen();
    }

    private void handleResetWeeklyPlan(Object data) {
        try {
            cantineService.resetWeeklyPlan();
            screenManager.showSuccessScreen("Weekly plan has been reset!");
        } catch (SQLException e) {
            screenManager.showErrorScreen("There was an error while resetting the weekly plan, please try again!");
        }
    }

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
